/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * Copyright 2003-2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.tools.xslhtml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import static java.util.logging.Level.*;

/**
 *
 * @author joshy
 */
public class XHTMLProcessingUtils {

    private static ResourceBundle messageRB = null;
    private static Logger logger = Logger.getLogger(XHTMLProcessingUtils.class.getName());;
    
    static {
        // set verbose for initial development
        logger.setLevel(ALL); //TODO: remove or set to INFO when finished
    }
    private static final String PARAMETER_PROFILES_ENABLED = "profiles-enabled";
    private static final String PARAMETER_TARGET_PROFILE = "target-profile";

    /**
     * Transform XMLDoclet output to XHTML using XSLT.
     * 
     * @param xmlInputPath the path of the XMLDoclet output to transform
     * @param xsltStream the XSLT to implement the transformation, as an input stream.
     * @throws java.lang.Exception
     */
     public static void process(String xmlInputPath, InputStream xsltStream, File docsdir,
            Map<String,String> parameters
            ) throws Exception {
        System.out.println(getString("transforming.to.html"));
        // TODO code application logic here
        
        //hack to get this to work on the mac
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
            "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
        System.setProperty("javax.xml.parsers.SAXParserFactory",
            "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
        
        if (xsltStream == null)
            xsltStream = XHTMLProcessingUtils.class.getResourceAsStream("resources/javadoc.xsl");
        
        File file = new File(xmlInputPath);
        p(INFO, MessageFormat.format(getString("reading.doc"), file.getAbsolutePath()));
        p(FINE, "exists: " + file.exists());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new ErrorHandler() {

            public void warning(SAXParseException exception) throws SAXException {
                pe(WARNING, "warning: ", exception);
            }

            public void error(SAXParseException exception) throws SAXException {
                pe(SEVERE, "error: ", exception);
            }

            public void fatalError(SAXParseException exception) throws SAXException {
                pe(SEVERE, "fatal error", exception);
            }

            private void pe(Level level, String string, SAXParseException exception) {
                p(level, string + " line: " + exception.getLineNumber() + " column: " +
                        exception.getColumnNumber() + " " + exception.getLocalizedMessage());
            }
        });
        Document doc = builder.parse(file);




        //File docsdir = new File("fxdocs");
        if (!docsdir.exists()) {
            docsdir.mkdir();
        }

        p(INFO, getString("copying"));

        copy(XHTMLProcessingUtils.class.getResource("resources/index.html"), new File(docsdir, "index.html"));
        copy(XHTMLProcessingUtils.class.getResource("resources/empty.html"), new File(docsdir, "empty.html"));
        copy(XHTMLProcessingUtils.class.getResource("resources/master.css"), new File(docsdir, "master.css"));
        copy(XHTMLProcessingUtils.class.getResource("resources/demo.css"), new File(docsdir, "demo.css"));
        copy(XHTMLProcessingUtils.class.getResource("resources/navigation.js"), new File(docsdir, "navigation.js"));
        File images = new File(docsdir,"images");
        images.mkdir();
        copy(XHTMLProcessingUtils.class.getResource("resources/quote-background-1.gif"), new File(images, "quote-background-1.gif"));
        //copy(new File("demo.css"), new File(docsdir, "demo.css"));

        p(INFO, getString("transforming"));


        //File xsltFile = new File("javadoc.xsl");
        //p("reading xslt exists in: " + xsltFile.exists());
        Source xslt = new StreamSource(xsltStream);
        Transformer trans = TransformerFactory.newInstance().newTransformer(xslt);
        for(String key : parameters.keySet()) {
            System.out.println("using key: " + key + " " + parameters.get(key));
            trans.setParameter(key, parameters.get(key));
        }
        trans.setErrorListener(new MainErrorListener());

        XPath xpath = XPathFactory.newInstance().newXPath();

        // print out packages list
        NodeList packages = (NodeList) xpath.evaluate("//package", doc, XPathConstants.NODESET); 
        p(INFO, MessageFormat.format(getString("creating.packages"), packages.getLength()));
        
        
        //build xml doc for the packages
        Document packages_doc = builder.newDocument();
        Element package_list_elem = packages_doc.createElement("packageList");
        packages_doc.appendChild(package_list_elem);

        //for each package, generate the package itself and append to package list doc
        for (int i = 0; i < packages.getLength(); i++) {
            Element pkg = ((Element) packages.item(i));
            String name = pkg.getAttribute("name");
            Element package_elem = packages_doc.createElement("package");
            package_elem.setAttribute("name", name);
            package_list_elem.appendChild(package_elem);
            copyDocComment(pkg,package_elem);
            Element first_line = packages_doc.createElement("first-line-comment");
            first_line.appendChild(packages_doc.createTextNode("first line comment"));
            package_elem.appendChild(first_line);
            processPackage(name, pkg, xpath, docsdir, trans);
        }

        //transform the package list doc
        package_list_elem.setAttribute("mode", "overview-frame");
        trans.transform(new DOMSource(packages_doc), new StreamResult(new File(docsdir,"overview-frame.html")));
        package_list_elem.setAttribute("mode", "overview-summary");
        trans.transform(new DOMSource(packages_doc), new StreamResult(new File(docsdir,"overview-summary.html")));
        p(INFO,getString("finished"));
    }

    private static void p(Transformer trans, Document packages_doc) throws TransformerException {
        trans.transform(new DOMSource(packages_doc), new StreamResult(System.out));
    }

    private static void processPackage(String packageName, Element pkg, XPath xpath, File docsdir, Transformer trans) throws TransformerException, XPathExpressionException, IOException, FileNotFoundException, ParserConfigurationException {
        File packageDir = new File(docsdir, packageName);
        packageDir.mkdir();
        
        //classes
        NodeList classesNodeList = (NodeList) xpath.evaluate(
                    "*[name() = 'class' or name() = 'abstractClass' or name() = 'interface']",
                    pkg, XPathConstants.NODESET);
        List<Element> classes = sort(classesNodeList);
        p(INFO, MessageFormat.format(getString("creating.classes"), classes.size()));
        
        Document classes_doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element class_list = classes_doc.createElement("classList");
        class_list.setAttribute("packageName", packageName);
        classes_doc.appendChild(class_list);
        
        for(Element clazz : classes) {
            processClass(clazz, class_list,  xpath, trans, packageDir);
        }
        
        class_list.setAttribute("mode", "overview-frame");
        trans.transform(new DOMSource(classes_doc), new StreamResult(new File(packageDir,"package-frame.html")));
        class_list.setAttribute("mode", "overview-summary");
        trans.transform(new DOMSource(classes_doc), new StreamResult(new File(packageDir,"package-summary.html")));
    }

    
    private static void processClass(Element clazz, Element class_list, XPath xpath, Transformer trans, File packageDir) throws TransformerException, IOException, XPathExpressionException {
        String qualifiedName = clazz.getAttribute("qualifiedName");
        String name = clazz.getAttribute("name");
        
        String profile = (String) xpath.evaluate("docComment/tags/profile/text()", clazz, XPathConstants.STRING);
        if("true".equals(trans.getParameter(PARAMETER_PROFILES_ENABLED))) {
            Object target_profile = trans.getParameter(PARAMETER_TARGET_PROFILE);
            if(profile != null && profile.equals(target_profile)) {
                //p(INFO, "profiles match");
            } else {
                //p(INFO, "Profiles don't match. skipping");
                return;
            }
        }
                
        //add to class list
        Document doc = class_list.getOwnerDocument();
        Element class_elem = doc.createElement("class");
        class_list.appendChild(class_elem);
        class_elem.setAttribute("name", name);
        class_elem.setAttribute("qualifiedName", qualifiedName);
        Element first_line = doc.createElement("first-line-comment");
        first_line.appendChild(doc.createTextNode("first line comment"));
        class_elem.appendChild(first_line);
        
        copyClassDoc(clazz,class_elem);

        File xhtmlFile = new File(packageDir, qualifiedName + ".html");
        Result xhtmlResult = new StreamResult(xhtmlFile);
        Source xmlSource = new DOMSource(clazz.getOwnerDocument());
        trans.setParameter("target-class", qualifiedName);
        trans.transform(xmlSource, xhtmlResult);
    }

    
    private static void copyClassDoc(Element clazz, Element class_elem) {
        Element docComment = (Element) clazz.getElementsByTagName("docComment").item(0);
        if(docComment == null) return;
        
        NodeList firstSent = docComment.getElementsByTagName("firstSentenceTags");
        if(firstSent.getLength() > 0) {
            class_elem.appendChild(class_elem.getOwnerDocument().importNode(firstSent.item(0),true));
        }
        NodeList tags = docComment.getElementsByTagName("tags");
        if(tags.getLength() > 0) {
            for(int i=0; i<tags.getLength(); i++) {
                Node tag = tags.item(i);
                class_elem.appendChild(class_elem.getOwnerDocument().importNode(tag,true));
            }
        }
    }
    
    private static void copyDocComment(Element pkg, Element package_elem) {
        Element docComment = (Element) pkg.getElementsByTagName("docComment").item(0);
        if (docComment != null) {
            Node copy = package_elem.getOwnerDocument().importNode(docComment, true);
            package_elem.appendChild(copy);
        }
    }
    
    private static List<Element> sort(NodeList classesNodeList) {
        List<Element> nodes = new ArrayList<Element>();
        for(int i=0; i<classesNodeList.getLength(); i++) {
            nodes.add((Element)classesNodeList.item(i));
        }
        
        Collections.sort(nodes,new Comparator<Element>() {
            public int compare(Element o1, Element o2) {
                return o1.getAttribute("qualifiedName").compareTo(
                        o2.getAttribute("qualifiedName"));
            }
        }
        );

        return nodes;
    }
    
    
    private static void copy(URL url, File file) throws FileNotFoundException, IOException {
        p(FINE, "copying from: " + url);
        p(FINE, "copying to: " + file.getAbsolutePath());
        InputStream in = url.openStream();
        FileOutputStream out = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        while (true) {
            int n = in.read(buf);
            if (n < 0) {
                break;
            }
            out.write(buf, 0, n);
        }
    }
    
    private static void copy(File infile, File outfile) throws FileNotFoundException, IOException {
        FileInputStream in = new FileInputStream(infile);
        FileOutputStream out = new FileOutputStream(outfile);
        byte[] buf = new byte[1024];
        while (true) {
            int n = in.read(buf);
            if (n < 0) {
                break;
            }
            out.write(buf, 0, n);
        }
    }
    
    static String getString(String key) {
        ResourceBundle msgRB = messageRB;
        if (msgRB == null) {
            try {
                messageRB = msgRB =
                    ResourceBundle.getBundle("com.sun.tools.xslhtml.resources.xslhtml");
            } catch (MissingResourceException e) {
                throw new Error("Fatal: Resource for javafxdoc is missing");
            }
        }
        return msgRB.getString(key);
    }

    private static void p(Level level, String string) {
        if (level.intValue() >= logger.getLevel().intValue())
            System.err.println(string);
    }

    private static void p(Level level, String string, Throwable t) {
        if (level.intValue() >= logger.getLevel().intValue()) {
            StringBuilder sb = new StringBuilder();
            sb.append(string);
            if (t != null) {
                sb.append(System.getProperty("line.separator"));
                try {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    t.printStackTrace(pw);
                    pw.close();
                    sb.append(sw.toString());
                } catch (Exception ex) {
                }
            }
            System.err.println(sb.toString());
        }
    }
    
    private static void p(String string) {
        System.out.println(string);
    }

    /**
     * Command-line/debugging entry
     */
    public static void main(String[] args) throws Exception {
        process("javadoc.xml", null, new File("fxdocs_test"), new HashMap<String, String>());
    }

    private static class MainErrorListener implements ErrorListener {

        public MainErrorListener() {
        }

        public void warning(TransformerException exception) throws TransformerException {
            p(WARNING, "warning: " + exception);
        }

        public void error(TransformerException exception) throws TransformerException {
            Throwable thr = exception;
            while (true) {
                p(SEVERE, "error: " + exception.getMessageAndLocation(), thr.getCause());
                if (thr.getCause() != null) {
                    thr = thr.getCause();
                } else {
                    break;
                }
            }
        }

        public void fatalError(TransformerException exception) throws TransformerException {
            p(SEVERE, "fatal error: " + exception.getMessageAndLocation(), exception);
        }
    }
}
