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

package com.sun.xmldoclet;

import com.sun.javadoc.*;
import java.io.*;
import java.lang.reflect.Modifier;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;
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
        return 0;  // default is option unknown
    }

    /**
     * Check that options have the correct arguments.
     * <P>
     * This method is not required, but is recommended,
     * as every option will be considered valid if this method
     * is not present.  It will default gracefully (to true)
     * if absent.
     * <P>
     * Printing option related error messages (using the provided
     * DocErrorReporter) is the responsibility of this method.
     *
     * @return true if the options are valid.
     */
    public static boolean validOptions(String options[][],
                                       DocErrorReporter reporter) {
        return true;  // default is options are valid
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
        hd.startElement("", "", "javadoc", attrs);
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
            generateComment(exec.inlineTags());
            generateAnnotations(exec.annotations());
            generateModifiers(exec);
            generateTypeParameters(exec.typeParameters());
            generateParameters(exec.parameters());
            Type[] exceptions = exec.thrownExceptionTypes();
            if (exceptions.length > 0) {
                attrs.clear();
                hd.startElement("", "", "thrownExceptions", attrs);
                for (Type t : exceptions)
                    generateTypeRef(t, "exception");
                hd.endElement("", "", "thrownExceptions");
            }
            if (exec instanceof MethodDoc) {
                MethodDoc m = (MethodDoc)exec;
                generateTypeRef(m.returnType(), "returns");
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

    private void generateField(FieldDoc field) throws SAXException {
        if (!field.isSynthetic()) {
            attrs.clear();
            attrs.addAttribute("", "", "name", "CDATA", field.name());
            attrs.addAttribute("", "", "qualifiedName", "CDATA", field.qualifiedName());
            attrs.addAttribute("", "", "enumConstant", "CDATA", Boolean.toString(field.isEnumConstant()));
            hd.startElement("", "", "field", attrs);
            generateComment(field.inlineTags());
            generateAnnotations(field.annotations());
            generateModifiers(field);
            generateTypeRef(field.type(), "type");
            String constantValue = field.constantValueExpression();
            if (constantValue != null) {
                attrs.clear();
                attrs.addAttribute("", "", "value", "CDATA", constantValue);
                hd.startElement("", "", "constant", attrs);
            }
        }
    }

    private void generatePackage(PackageDoc pkg) throws SAXException {
        attrs.clear();
        attrs.addAttribute("", "", "name", "CDATA", pkg.name());
        hd.startElement("", "", "package", attrs);
        generateComment(pkg.inlineTags());
        generateAnnotations(pkg.annotations());
        for (ClassDoc cls : pkg.allClasses())
            generateClass(cls);
        hd.endElement("", "", "package");
    }

    private void generateClass(ClassDoc cls) throws SAXException {
        String classType = 
                cls.isAnnotationType() ? "annotation" :
                cls.isEnum() ? "enum" :
                cls.isInterface() ? "interface" :
                cls.isAbstract() ? "abstractClass" :
                "class";
        attrs.clear();
        attrs.addAttribute("", "", "name", "CDATA", cls.name());
        attrs.addAttribute("", "", "qualifiedName", "CDATA", cls.qualifiedName());
        hd.startElement("", "", classType, attrs);
        generateComment(cls.inlineTags());
        generateAnnotations(cls.annotations());
        generateModifiers(cls);
        generateTypeParameters(cls.typeParameters());
        generateTypeRef(cls.superclass(), "superclass");
        attrs.clear();
        hd.startElement("", "", "interfaces", attrs);
        for (Type intf : cls.interfaces())
            generateTypeRef(intf, "interface");
        hd.endElement("", "", "interfaces");
        for (ClassDoc inner : cls.innerClasses())
            generateClass(inner);
        for (ConstructorDoc cons : cls.constructors())
            generateExecutableMember(cons, "constructor");
        for (MethodDoc meth : cls.methods())
            generateExecutableMember(meth, "method");
        for (FieldDoc field : cls.fields())
            generateField(field);
        hd.endElement("", "", classType);
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
            generateTypeRef(p.type(), "type");
            generateAnnotations(p.annotations());
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
    
    private void generateTypeRef(Type type, String kind) throws SAXException {
        if (type != null) {
            attrs.clear();
            attrs.addAttribute("", "", "typeName", "CDATA", type.typeName());
            attrs.addAttribute("", "", "simpleTypeName", "CDATA", type.simpleTypeName());
            attrs.addAttribute("", "", "qualifiedTypeName", "CDATA", type.qualifiedTypeName());
            hd.startElement("", "", kind, attrs);
            hd.endElement("", "", kind);
        }
    }
    
    private void generateComment(Tag[] tags) throws SAXException {
        if (tags.length > 0) {
            attrs.clear();
            hd.startElement("", "", "comment", attrs);
            generateTags(tags);
            hd.endElement("", "", "comment");
        }
    }

    private void generateTags(Tag[] tags) throws SAXException, SAXException {
        for (Tag t : tags) {
            attrs.clear();
            attrs.addAttribute("", "", "name", "CDATA", t.name());
            String kind = t.kind();
            hd.startElement("", "", kind, attrs);
            Tag[] inlineTags = t.inlineTags();
            if (inlineTags.length <= 1) {
                String text = t.text();
                hd.characters(text.toCharArray(), 0, text.length());
            } else {
                generateTags(inlineTags);
            }
            hd.endElement("", "", kind);
        }
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

        //TODO: replace with output option
        out = new PrintWriter(new BufferedWriter(new FileWriter("javadoc-output.xml")));
        StreamResult streamResult = new StreamResult(out);
        attrs = new AttributesImpl();        
        hd.setResult(streamResult);
        hd.startDocument();
    }
}
