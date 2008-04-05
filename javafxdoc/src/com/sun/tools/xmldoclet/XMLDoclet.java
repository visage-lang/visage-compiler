/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * Copyright 2003-2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

package com.sun.tools.xmldoclet;

import com.sun.javadoc.*;
import com.sun.tools.javafx.code.FunctionType;
import com.sun.tools.xslhtml.XHTMLProcessingUtils;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.sax.*;

/**
 * Javadoc doclet which generates XML output.
 * @author tball
 */
public class XMLDoclet {
    private PrintWriter out;
    private Transformer serializer;
    private TransformerHandler hd;
    private AttributesImpl attrs;
    
    // option values
    private static String outFileName = null;
    private static File outDocsDir = new File("fxdocs");
    private static boolean includeAuthorTags = false;
    private static boolean includeDeprecatedTags = true;
    private static boolean includeSinceTags = true;
    private static boolean includeVersionTags = false;
    private static boolean processXSLT = true;
    
    private static ResourceBundle messageRB = null;
    private static String xsltFileName = null;
    
    private static final boolean debug = false;
    private static final Map<String,String> params = new HashMap<String, String>();
    
    static final Option[] options = {
        new Option("-o", getString("out.file.option"), getString("out.file.description")),
        new Option("-version", getString("version.description")),
        new Option("-author", getString("author.description")),
        new Option("-nosince", getString("nosince.description")),
        new Option("-nodeprecated", getString("nodeprecated.description")),
        new Option("-nohtml", getString("nohtml.description")),
        new Option("-xsltfile", getString("out.file.option"), getString("xsltfile.description")),
        new Option("-mastercss", getString("out.file.option"), getString("xsltfile.description")),
        new Option("-extracss", getString("out.file.option"), getString("xsltfile.description")),
        new Option("-extrajs", getString("out.file.option"), getString("xsltfile.description")),
        new Option("-extrajs2", getString("out.file.option"), getString("xsltfile.description")),
        new Option("-xsl:name=value", getString("xslproperty.description")),
        new Option("-d", getString("out.dir.option"), getString("out.dir.description"))
    };

    /**
     * Generate documentation here.
     * This method is required for all doclets.
     *
     * @return true on success.
     */
    public static boolean start(RootDoc root) {
        try {
            XMLDoclet doclet = new XMLDoclet();
            doclet.generateXML(root);
            
            if(processXSLT) {
                FileInputStream xsltStream = xsltFileName != null ? 
                    new FileInputStream(xsltFileName) : null;
                
                XHTMLProcessingUtils.process(outFileName, xsltStream, outDocsDir, params);
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check for doclet-added options.  Returns the number of
     * arguments you must specify on the command line for the
     * given option.  For example, "-d docs" would return 2.
     * <P>
     * This method is required if the doclet contains any options.
     * If this method is missing, Javadoc will print an invalid flag
     * error for every option.
     *
     * @return number of arguments on the command line for an option
     *         including the option name itself.  Zero return means
     *         option not known.  Negative value means error occurred.
     */
    public static int optionLength(String option) {
        if (option.equals("-help")) {
            System.out.println(getString("help.header"));
            for (Option o : options)
                System.out.println(o.help());
            return 1;
        }
        for (Option o : options) {
            if (o.name().equals(option))
                return o.length();
        }
        return 0;  // default is option unknown
    }

    /**
     * Check that options have the correct arguments.
     * Printing option related error messages (using the provided
     * DocErrorReporter) is the responsibility of this method.
     *
     * @return true if the options are valid.
     */
    public static boolean validOptions(String options[][],
                                       DocErrorReporter reporter) {
        if (debug) {
            for(int i=0; i< options.length; i++) {
                for(int j=0; j<options[i].length; j++) {
                    System.out.println("got: " + options[i][j]);
                }
            }
        }
        for (String[] option : options) {
            if (option[0].equals("-o"))
                outFileName = option[1];
            else if (option[0].equals("-version"))
                includeVersionTags = true;
            else if (option[0].equals("-author"))
                includeAuthorTags = true;
            else if (option[0].equals("-nosince"))
                includeSinceTags = false;
            else if (option[0].equals("-nodeprecated"))
                includeDeprecatedTags = false;
            else if (option[0].equals("-nohtml"))
                processXSLT = false;
            else if (option[0].equals("-xsltfile"))
                xsltFileName = option[1];
            else if (option[0].equals("-mastercss"))
                params.put("master-css",option[1]);
            else if (option[0].equals("-extracss"))
                params.put("extra-css",option[1]);
            else if (option[0].equals("-extrajs"))
                params.put("extra-js",option[1]);
            else if (option[0].equals("-extrajs2"))
                params.put("extra-js2",option[1]);
            else if (option[0].equals("-d"))
                outDocsDir = new File(option[1]);
            else if (option[0].startsWith("-xsl:")) {
                String s = option[0].substring(5);
                int i = s.indexOf('=');
                if (i == -1)
                    return false;
                String name = s.substring(0, i);
                String value = s.substring(i+1);
                params.put(name, value);
            }
        }
        if (outFileName == null) {
            try {
                File f = File.createTempFile("javadoc", ".xml");
                outFileName = f.getPath();
            } catch (IOException ex) {
                Logger.getLogger(XMLDoclet.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true;
    }

    /**
     * Return the version of the Java Programming Language supported
     * by this doclet.
     *
     * @return  the language version supported by this doclet.
     */
    public static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
    }
    
    void generateXML(RootDoc root) throws IOException, TransformerException, SAXException {
        initTransformer();
        attrs.clear();
        hd.startElement("", "", "javadoc", attrs);
        generateComment(root);
        for (PackageDoc pkg : root.specifiedPackages())
            generatePackage(pkg);
        hd.endElement("","","javadoc");
        hd.endDocument();
    }

    private void generateExecutableMember(ExecutableMemberDoc exec, String kind) throws SAXException {
        if (!exec.isSynthetic()) {
            attrs.clear();
            attrs.addAttribute("", "", "name", "CDATA", exec.name());
            attrs.addAttribute("", "", "qualifiedName", "CDATA", exec.qualifiedName());
            if (exec instanceof MethodDoc)
                attrs.addAttribute("", "", "varargs", "CDATA", Boolean.toString(exec.isVarArgs()));
            hd.startElement("", "", kind, attrs);
            generateComment(exec);
            generateAnnotations(exec.annotations());
            generateModifiers(exec);
            generateTypeParameters(exec.typeParameters());
            generateParameters(exec.parameters());
            Type[] exceptions = exec.thrownExceptionTypes();
            if (exceptions.length > 0) {
                attrs.clear();
                hd.startElement("", "", "thrownExceptions", attrs);
                for (Type t : exceptions)
                    generateTypeRef(t, "exception", null);
                hd.endElement("", "", "thrownExceptions");
            }
            if (exec instanceof MethodDoc) {
                MethodDoc m = (MethodDoc)exec;
                generateTypeRef(m.returnType(), "returns", rawReturnType(m));
                MethodDoc overridden = m.overriddenMethod();
                if (overridden != null) {
                    String name = overridden.qualifiedName();
                    attrs.clear();
                    attrs.addAttribute("", "", "name", "CDATA", name);
                    hd.startElement("", "", "overrides", attrs);
                    hd.endElement("", "", "overrides");
                }
            }
            hd.endElement("", "", kind);
        }
    }

    private void generateField(FieldDoc field, String kind) throws SAXException {
        if (!field.isSynthetic()) {
            attrs.clear();
            attrs.addAttribute("", "", "name", "CDATA", field.name());
            attrs.addAttribute("", "", "qualifiedName", "CDATA", field.qualifiedName());
            attrs.addAttribute("", "", "enumConstant", "CDATA", Boolean.toString(field.isEnumConstant()));
            hd.startElement("", "", kind, attrs);
            if("name".equals(field.name())) {
                System.out.println("generating docs for field: " + field.name());
                System.out.println("raw comments = " + field.getRawCommentText());
            }
            generateComment(field);
            generateAnnotations(field.annotations());
            generateModifiers(field);
            generateTypeRef(field.type(), "type", rawType(field));
            String constantValue = field.constantValueExpression();
            if (constantValue != null) {
                attrs.clear();
                attrs.addAttribute("", "", "value", "CDATA", constantValue);
                hd.startElement("", "", "constant", attrs);
                hd.endElement("", "", "constant");
            }
            hd.endElement("", "", kind);
        }
    }

    private void generatePackage(PackageDoc pkg) throws SAXException {
        attrs.clear();
        attrs.addAttribute("", "", "name", "CDATA", pkg.name());
        hd.startElement("", "", "package", attrs);
        generateComment(pkg);
        generateAnnotations(pkg.annotations());
        for (ClassDoc cls : pkg.allClasses())
            generateClass(cls);
        hd.endElement("", "", "package");
    }

    private void generateClass(ClassDoc cls) throws SAXException {
        boolean fxClass = isJFXClass(cls);
        String classType = 
                cls.isAnnotationType() ? "annotation" :
                cls.isEnum() ? "enum" :
                cls.isInterface() ? "interface" :
                cls.isAbstract() ? "abstractClass" :
                "class";
        attrs.clear();
        attrs.addAttribute("", "", "name", "CDATA", cls.name());
        attrs.addAttribute("", "", "qualifiedName", "CDATA", cls.qualifiedName());
        attrs.addAttribute("", "", "packageName", "CDATA", cls.containingPackage().name());
        attrs.addAttribute("", "", "language", "CDATA", fxClass ? "javafx" : "java");
        attrs.addAttribute("", "", "classType","CDATA",classType);
        hd.startElement("", "", "class", attrs);
        generateComment(cls);
        generateModifiers(cls);
        if (!fxClass) {
            generateAnnotations(cls.annotations());
            generateTypeParameters(cls.typeParameters());
        }
        if (!cls.superclass().qualifiedName().equals("java.lang.Object"))
            generateTypeRef(cls.superclass(), "superclass", null);
        attrs.clear();
        hd.startElement("", "", "interfaces", attrs);
        for (Type intf : cls.interfaces())
            generateTypeRef(intf, "interface", null);
        hd.endElement("", "", "interfaces");
        if (!fxClass) {
        for (ClassDoc inner : cls.innerClasses())
            generateClass(inner);
        for (ConstructorDoc cons : cls.constructors())
            generateExecutableMember(cons, "constructor");
        }
        
        for (MethodDoc meth : cls.methods())
            generateExecutableMember(meth, fxClass ? "function" : "method");
        for (FieldDoc field : cls.fields())
            generateField(field, fxClass ? "attribute" : "field");
        hd.endElement("", "", "class");
    }
    
    private void generateModifiers(ProgramElementDoc element) throws SAXException {
        attrs.clear();
        attrs.addAttribute("", "", "text", "CDATA", element.modifiers());
        hd.startElement("", "", "modifiers", attrs);
        attrs.clear();
        int modifiers = element.modifierSpecifier();
        if (Modifier.isPublic(modifiers)) {
            hd.startElement("", "", "public", attrs);
            hd.endElement("", "", "public");
        }
        else if (Modifier.isProtected(modifiers)) {
            hd.startElement("", "", "protected", attrs);
            hd.endElement("", "", "protected");
        }
        else if (Modifier.isPrivate(modifiers)) {
            hd.startElement("", "", "private", attrs);
            hd.endElement("", "", "private");
        }
        else {
            hd.startElement("", "", "packagePrivate", attrs);
            hd.endElement("", "", "packagePrivate");
        }
        if (Modifier.isStatic(modifiers)) {
            hd.startElement("", "", "static", attrs);
            hd.endElement("", "", "static");
        }
        if (Modifier.isFinal(modifiers)) {
            hd.startElement("", "", "final", attrs);
            hd.endElement("", "", "final");
        }
        else if (Modifier.isAbstract(modifiers)) {
            hd.startElement("", "", "abstract", attrs);
            hd.endElement("", "", "abstract");
        }
        if (Modifier.isNative(modifiers)) {
            hd.startElement("", "", "native", attrs);
            hd.endElement("", "", "native");
        }
        if (Modifier.isStrict(modifiers)) {
            hd.startElement("", "", "strictfp", attrs);
            hd.endElement("", "", "strictfp");
        }
        hd.endElement("", "", "modifiers");
    }

    private void generateParameters(Parameter[] parameters) throws SAXException {
        attrs.clear();
        hd.startElement("", "", "parameters", attrs);
        for (Parameter p : parameters) {
            attrs.clear();
            attrs.addAttribute("", "", "name", "CDATA", p.name());
            hd.startElement("", "", "parameter", attrs);
            generateTypeRef(p.type(), "type", rawType(p));
            generateAnnotations(p.annotations());
            hd.endElement("", "", "parameter");
        }
        hd.endElement("", "", "parameters");
    }

    private void generateTypeParameters(TypeVariable[] typeParameters) throws SAXException {
        if (typeParameters.length > 0) {
            attrs.clear();
            hd.startElement("", "", "typeParameters", attrs);
            for (TypeVariable tp : typeParameters) {
                attrs.clear();
                attrs.addAttribute("", "", "typeName", "CDATA", tp.typeName());
                attrs.addAttribute("", "", "simpleTypeName", "CDATA", tp.simpleTypeName());
                attrs.addAttribute("", "", "qualifiedTypeName", "CDATA", tp.qualifiedTypeName());
                hd.startElement("", "", "typeParameter", attrs);
                hd.endElement("", "", "typeParameter");
            }
            hd.endElement("", "", "typeParameters");
        }
    }
    
    private void generateTypeRef(Type type, String kind, 
                                 com.sun.tools.javac.code.Type rawType) throws SAXException {
        if (type != null) {
            attrs.clear();
            ClassDoc cd = type.asClassDoc();
            boolean isSequence = false;
            if (cd != null) {
                isSequence = isSequence(cd);
                if (isSequence) {
                    if (rawType == null)
                        throw new AssertionError("unknown sequence type");
                    type = sequenceElementType(cd, rawType);
                }
            }
            boolean isFunctionType = rawType instanceof FunctionType;
            attrs.addAttribute("", "", "typeName", "CDATA", type.typeName());
            String simpleName = isFunctionType ? 
                simpleFunctionalTypeName(cd, rawType) : type.simpleTypeName();
            attrs.addAttribute("", "", "simpleTypeName", "CDATA", simpleName);
            attrs.addAttribute("", "", "qualifiedTypeName", "CDATA", type.qualifiedTypeName());
            if(cd != null) {
                attrs.addAttribute("", "", "packageName", "CDATA", cd.containingPackage().name());
            }
            String dim = isSequence ? "[]" : type.dimension();
            attrs.addAttribute("", "", "dimension", "CDATA", dim);
            String s = type.toString() + (isSequence ? "[]" : "");
            attrs.addAttribute("", "", "toString", "CDATA", s);
            attrs.addAttribute("", "", "sequence", "CDATA", Boolean.toString(isSequence));
            attrs.addAttribute("", "", "functionType", "CDATA", Boolean.toString(isFunctionType));
            hd.startElement("", "", kind, attrs);
            hd.endElement("", "", kind);
        }
    }
    
    private void generateComment(Doc doc) throws SAXException {
        String rawCommentText = doc.getRawCommentText();
        if (rawCommentText.length() > 0) {
            attrs.clear();
            hd.startElement("", "", "docComment", attrs);            
            hd.startElement("", "", "rawCommentText", attrs);
            hd.characters(rawCommentText.toCharArray(), 0, rawCommentText.length());
            hd.endElement("", "", "rawCommentText");

            String commentText = doc.commentText();
            hd.startElement("", "", "commentText", attrs);
            hd.characters(commentText.toCharArray(), 0, commentText.length());
            hd.endElement("", "", "commentText");
            
            generateTags(doc.tags(), "tags");
            generateTags(doc.firstSentenceTags(), "firstSentenceTags");
            generateTags(doc.seeTags(), "seeTags");
            generateTags(getInlineTags(doc), "inlineTags");
            
            hd.endElement("", "", "docComment");
        }
    }

    private void generateTags(Tag[] tags, String tagKind) throws SAXException, SAXException {
        if (tags.length == 0)
            return;
        attrs.clear();
        hd.startElement("", "", tagKind, attrs);
        for (Tag t : tags) {
            String kind = t.kind();
            if (kind.startsWith("@"))
                kind = kind.substring(1);
            if (kind.equals("@author") && !includeAuthorTags)
                continue;
            if (kind.equals("@deprecated") && !includeDeprecatedTags)
                continue;
            if (kind.equals("@since") && !includeSinceTags)
                continue;
            if (kind.equals("@version") && !includeVersionTags)
                continue;
            if (!kind.matches("\\w+")) {
                System.out.println("possible invalid tag kind: " + kind);
                kind = "invalidtag";
            }
            attrs.clear();
            attrs.addAttribute("", "", "name", "CDATA", t.name());
            //process link tags specially
            if("@link".equals(t.name())) {
                attrs.addAttribute("", "", "href", "CDATA", t.text());
                String label = t.text();
                if(label.startsWith("#")) {
                    label = label.substring(1);
                }
                attrs.addAttribute("", "", "label", "CDATA", label);
            }
            hd.startElement("", "", kind, attrs);
            Tag[] inlineTags = t.inlineTags();
            if (inlineTags.length <= 1) {
                String text = t.text();
                hd.characters(text.toCharArray(), 0, text.length());
            } else {
                generateTags(inlineTags, "inlineTags");
            }
            hd.endElement("", "", kind);
        }
        hd.endElement("", "", tagKind);
    }
    
    private Tag[] getInlineTags(Doc doc) throws SAXException, SAXException {
        List<Tag> list = new ArrayList<Tag>();
        Tag[] inlineTags = doc.inlineTags();
        if (inlineTags.length > 0 && doc.isMethod()) { // inheritDoc tag only valid for methods
            list.addAll(Arrays.asList(inlineTags));
            boolean changed = false;
            for(int i=0; i<list.size(); i++) {
                Tag t = list.get(i);
                if(t.kind().matches("@inheritDoc")) {
                    Doc inherited = getInheritedDoc(doc);
                    if (inherited != null) {
                        list.remove(i);
                        list.addAll(i,Arrays.asList(getInlineTags(inherited)));
                        changed = true;
                    }
                    break;
                }
            }
            if (changed)
                inlineTags = (Tag[]) list.toArray(new Tag[0]);
        }
        return inlineTags;
    }

    
    private void generateAnnotations(AnnotationDesc[] annotations) throws SAXException {
        if (annotations.length > 0) {
            attrs.clear();
            hd.startElement("", "", "annotations", attrs);
            for (AnnotationDesc desc : annotations) {
                attrs.clear();
                hd.startElement("", "", "annotationType", attrs);
                AnnotationTypeDoc type = desc.annotationType();
                attrs.addAttribute("", "", "simpleName", "CDATA", type.simpleTypeName());
                attrs.addAttribute("", "", "qualifiedName", "CDATA", type.qualifiedTypeName());
                attrs.addAttribute("", "", "dimension", "CDATA", type.dimension());
                hd.startElement("", "", type.typeName(), attrs);

                AnnotationTypeElementDoc[] elements = type.elements();
                for (AnnotationTypeElementDoc element : elements) {
                    if (!element.isSynthetic()) {
                        attrs.clear();
                        attrs.addAttribute("", "", "name", "CDATA", element.name());
                        attrs.addAttribute("", "", "qualifiedName", "CDATA", element.qualifiedName());
                        attrs.addAttribute("", "", "commentText", "CDATA", element.commentText());
                        AnnotationValue defValue = element.defaultValue();
                        if (defValue != null)
                            attrs.addAttribute("", "", "defaultValue", "CDATA", defValue.toString());
                        hd.startElement("", "", "element", attrs);
                        hd.endElement("", "", "element");
                    }
                }

                hd.endElement("", "", type.typeName());
                hd.endElement("", "", "annotationType");
            }
            hd.endElement("", "", "annotations");
        }
    }

    private Doc getInheritedDoc(Doc doc) {
        if(doc instanceof MethodDoc) {
            return getOverriddenMethod((MethodDoc)doc);
        }
        return null;
    }


    private MethodDoc getOverriddenMethod(MethodDoc doc) {
        ClassDoc cls = doc.containingClass();
        ClassDoc scls = cls.superclass();
        MethodDoc meth = findDeclaredMethod(scls, doc);
        if (meth == null && isJFXClass(cls)) {
            for (ClassDoc intf : cls.interfaces()) {
                meth = findDeclaredMethod(intf, doc);
                if (meth != null)
                    break;
            }
        }
        return meth;
    }

    private MethodDoc findDeclaredMethod(ClassDoc scls, MethodDoc doc) {
        MethodDoc[] meths = scls.methods();
        for (MethodDoc md : meths) {
            if (md.name().equals(doc.name()) && md.signature().equals(doc.signature())) {
                return md;
            }
        }
        return null;
    }

    private void initTransformer() throws IOException, SAXException, TransformerException {
        SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        tf.setAttribute("indent-number", new Integer(3));
        hd = tf.newTransformerHandler();
        serializer = hd.getTransformer();
        serializer.setOutputProperty(OutputKeys.METHOD, "xml");
        serializer.setOutputProperty(OutputKeys.VERSION, "1.0");
        serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
        //TODO: serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"???.dtd");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");

        File f = new File(outFileName);
        f.getParentFile().mkdirs();
        out = new PrintWriter(new BufferedWriter(new FileWriter(f)));
        StreamResult streamResult = new StreamResult(out);
        attrs = new AttributesImpl();        
        hd.setResult(streamResult);
        hd.startDocument();
    }
    
    static String getString(String key) {
        ResourceBundle msgRB = messageRB;
        if (msgRB == null) {
            try {
                messageRB = msgRB =
                    ResourceBundle.getBundle("com.sun.tools.xmldoclet.resources.xmldoclet");
            } catch (MissingResourceException e) {
                throw new Error("Fatal: Resource for javafxdoc is missing");
            }
        }
        return msgRB.getString(key);
    }
    
    private static boolean isJFXClass(ClassDoc clsDoc) {
        return probe(clsDoc, "isJFXClass");
    }
    
    private static boolean isSequence(ClassDoc clsDoc) {
        return probe(clsDoc, "isSequence");
    }
    
    private static boolean probe(ClassDoc clsDoc, String method) {
        try {
            Class<?> cls = clsDoc.getClass();
            Method m = cls.getDeclaredMethod(method);
            Object result = m.invoke(clsDoc);
            return ((Boolean)result).booleanValue();
        } catch (Exception e) {
            return false;
        }
    }
    
    private static com.sun.tools.javac.code.Type rawType(FieldDoc field) {
        return rawType(field, "rawType");
    }
    
    private static com.sun.tools.javac.code.Type rawType(Parameter param) {
        return rawType(param, "rawType");
    }
    
    private static com.sun.tools.javac.code.Type rawReturnType(MethodDoc method) {
        return rawType(method, "rawReturnType");
    }
    
    private static com.sun.tools.javac.code.Type rawType(Object o, String method) {
        try {
            Class<?> cls = o.getClass();
            Method m = cls.getDeclaredMethod(method);
            Object result = m.invoke(o);
            return (com.sun.tools.javac.code.Type)result;
        } catch (Exception e) {
            return null;
        }
    }
    
    private static Type sequenceElementType(ClassDoc cd, com.sun.tools.javac.code.Type rawType) {
        try {
            Class<?> cls = cd.getClass();
            Method m = cls.getDeclaredMethod("sequenceElementType", com.sun.tools.javac.code.Type.class);
            Object result = m.invoke(cd, (Object)rawType);
            return (Type)result;
        } catch (Exception e) {
            return null;
        }
    }
    
    private static String simpleFunctionalTypeName(ClassDoc cd, com.sun.tools.javac.code.Type rawType) {
        try {
            Class<?> cls = cd.getClass();
            Method m = cls.getDeclaredMethod("simpleFunctionalTypeName", com.sun.tools.javac.code.Type.class);
            Object result = m.invoke(cd, (Object)rawType);
            return (String)result;
        } catch (Exception e) {
            return null;
        }
    }

    static class Option {
        String[] fields;
        String help;
        private static final int DESCRIPTION_COLUMN = 
            Integer.valueOf(getString("help.description.column"));
        
        Option(String field, String help) {
            fields = new String[] { field };
            this.help = help;
        }
        Option(String field, String param, String help) {
            fields = new String[] { field, param };
            this.help = help;
        }
        int length() {
            return fields.length;
        }
        String name() {
            return fields[0];
        }
        String description() {
            return fields.length == 1 ? fields[0] : fields[0] + ' ' + fields[1];
        }
        String help() {
            StringBuffer sb = new StringBuffer(description());
            while (sb.length() < DESCRIPTION_COLUMN)
                sb.append(' ');
            sb.append(help);
            return sb.toString();
        }
    }

    private static void p(String string) {
        System.out.println(string);
    }
}
