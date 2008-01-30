/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.xhtmldoclet;

import com.sun.javadoc.RootDoc;
import com.sun.xmldoclet.XMLDoclet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author joshy
 */
public class XHTMLProcessingUtils {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        
        //hack to get this to work on the mac
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
            "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
        System.setProperty("javax.xml.parsers.SAXParserFactory",
            "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
        
        
        
        File file = new File("javadoc.xml");
        p("reading doc:  " + file.getAbsolutePath());
        p("exists: " + file.exists());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new ErrorHandler() {

            public void warning(SAXParseException exception) throws SAXException {
                p("error: " + exception.getLineNumber());
            }

            public void error(SAXParseException exception) throws SAXException {
                p("error: " + exception.getLineNumber());
            }

            public void fatalError(SAXParseException exception) throws SAXException {
                p("error: " + exception.getLineNumber());
            }
        });
        Document doc = builder.parse(file);




        File docsdir = new File("fxdocs");
        if (!docsdir.exists()) {
            docsdir.mkdir();
        }

        p("copying");

        copy(XHTMLProcessingUtils.class.getResource("resources/frameset.html"), new File(docsdir, "frameset.html"));
        copy(XHTMLProcessingUtils.class.getResource("resources/demo.css"), new File(docsdir, "demo.css"));
        //copy(new File("demo.css"), new File(docsdir, "demo.css"));

        p("transforming");


        //File xsltFile = new File("javadoc.xsl");
        //p("reading xslt exists in: " + xsltFile.exists());
        Source xslt = new StreamSource(XHTMLProcessingUtils.class.getResourceAsStream("resources/javadoc.xsl"));
        Transformer trans = TransformerFactory.newInstance().newTransformer(xslt);
        trans.setErrorListener(new ErrorListener() {

            public void warning(TransformerException exception) throws TransformerException {
                p("warning: " + exception);
            }

            public void error(TransformerException exception) throws TransformerException {
                Throwable thr = exception;
                while (true) {
                    p("error: " + exception.getMessageAndLocation());
                    p("cause = " + thr.getCause());
                    if (thr.getCause() != null) {
                        thr = thr.getCause();
                    } else {
                        break;
                    }

                }
            }

            public void fatalError(TransformerException exception) throws TransformerException {
                p("fatal error: " + exception.getLocationAsString() + " " + exception.getMessageAndLocation());
            }
        });

        XPath xpath = XPathFactory.newInstance().newXPath();

        // packages
        NodeList packages = (NodeList) xpath.evaluate("//package", doc, XPathConstants.NODESET);
        p("doing packages: " + packages.getLength());
        FileOutputStream packages_html = new FileOutputStream(new File(docsdir, "packages.html"));
        Writer packages_writer = new OutputStreamWriter(packages_html);
        packages_writer.write("<html><head><link href='demo.css' rel='stylesheet'/></head><body><ul class='package-list'>");
        FileOutputStream classes_html = new FileOutputStream(new File(docsdir, "classes.html"));
        Writer classes_writer = new OutputStreamWriter(classes_html);
        classes_writer.write("<html><head><link href='../demo.css' rel='stylesheet'/></head><body><ul>");

        for (int i = 0; i < packages.getLength(); i++) {
            Element pkg = ((Element) packages.item(i));
            String name = pkg.getAttribute("name");
            packages_writer.write("<li><a href='"+name+"/classes.html' target='classListFrame'>" + name + "</a></li>");
            processPackage(name, pkg, xpath, docsdir, trans);
        }

        classes_writer.write("</ul></body></html>");
        classes_writer.close();
        packages_writer.write("</ul></body></html>");
        packages_writer.close();
    }


    private static void processPackage(String packageName, Element pkg, XPath xpath, File docsdir, Transformer trans) throws TransformerException, XPathExpressionException, IOException, FileNotFoundException {
        File packageDir = new File(docsdir, packageName);
        packageDir.mkdir();
        //classes
        NodeList classesNodeList = (NodeList) xpath.evaluate(
                    "*[name() = 'class' or name() = 'abstractClass']",
                    pkg, XPathConstants.NODESET);
        List<Element> classes = sort(classesNodeList);
        p("classes = " + classes.size());
        FileOutputStream package_classes_html = new FileOutputStream(new File(packageDir, "classes.html"));
        Writer package_classes_writer = new OutputStreamWriter(package_classes_html);
        package_classes_writer.write("<html><head><link href='../demo.css' rel='stylesheet'/></head><body><ul class='class-list'>");
        for(Element clazz : classes) {
            processClass(clazz, package_classes_writer,trans, packageDir);
        }
        package_classes_writer.write("</ul></body></html>");
        package_classes_writer.close();
    }

    
    private static void processClass(Element clazz, Writer package_classes_writer, Transformer trans, File packageDir) throws TransformerException, IOException {
        String qualifiedName = clazz.getAttribute("qualifiedName");
        String name = clazz.getAttribute("name");
        package_classes_writer.write("<li><a href='" + qualifiedName + ".html" + "' target='classFrame'>" + name + "</a>\n");
        File xhtmlFile = new File(packageDir, qualifiedName + ".html");
        Result xhtmlResult = new StreamResult(xhtmlFile);
        Source xmlSource = new DOMSource(clazz);
        trans.transform(xmlSource, xhtmlResult);
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
        p("copying from: " + url);
        p("copying to: " + file.getAbsolutePath());
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

    private static void p(String string) {
        System.out.println(string);
    }
}
