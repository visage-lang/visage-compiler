/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.script;

import com.sun.tools.javafx.api.JavafxcTool;
import com.sun.javafx.api.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.tools.*;
/**
 * Simple interface to the JavaFX Script compiler using JSR 199 Compiler API, 
 * based on https://scripting.dev.java.net's JavaCompiler by A. Sundararajan.
 */
public class JavaFXScriptCompiler {    
    private JavafxcTool tool;
    private StandardJavaFileManager stdManager;
    private ClassLoader parentClassLoader;
    // a map in which the key is package name and the value is list of
    // classes in that package.
    private Map<String, List<String>> packageMap;

    public JavaFXScriptCompiler(ClassLoader parent) {
	parentClassLoader = parent;
        tool = JavafxcTool.create();
        packageMap = new HashMap<String, List<String>>();
        try {
            // fill package-class-list map from parent class loader
            fillPackageMap(parentClassLoader, packageMap);
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    public Map<String, byte[]> compile(String filename, String source) {
        PrintWriter err = new PrintWriter(System.err);
        return compile(filename, source, err, null, null);
    }

    public Map<String, byte[]> compile(String fileName, String source, 
                                    Writer err) {
        return compile(fileName, source, err, null, null);
    }

    public Map<String, byte[]> compile(String fileName, String source, 
                                    Writer err, String sourcePath) {
        return compile(fileName, source, err, sourcePath, null);
    }

    public Map<String, byte[]> compile(String fileName, String source, 
                                    Writer err, String sourcePath, 
                                    String classPath) {
        return compile(fileName, source, err, sourcePath, classPath,
                new DiagnosticCollector<JavaFileObject>(), true);
    }

    public Map<String, byte[]> compile(String filename) throws IOException {
        String source = readFully(new FileReader(filename));
        PrintWriter err = new PrintWriter(System.err);
        return compile(filename, source, err, null, null);
    }

    /**
     * compile given String source and return bytecodes as a Map.
     *
     * @param fileName source fileName to be used for error messages etc.
     * @param source Java source as String
     * @param err error writer where diagnostic messages are written
     * @param sourcePath location of additional .java source files
     * @param classPath location of additional .class files
     * @param diagnostics error and warning collector
     * @param printDiagnostics true if diagnostics should be displayed
     */
    public Map<String, byte[]> compile(String fileName, String source, 
                    Writer err, String sourcePath, String classPath,
                    DiagnosticListener<JavaFileObject> diagnostics,
                    boolean printDiagnostics) {

        // create a new memory JavaFileManager
	if (stdManager == null) {
	    stdManager = tool.getStandardFileManager(diagnostics, null, null);
	}
        MemoryFileManager manager = new MemoryFileManager(stdManager, 
                parentClassLoader, packageMap);

        // prepare the compilation unit
        List<JavaFileObject> compUnits = new ArrayList<JavaFileObject>(1);
        compUnits.add(manager.makeStringSource(fileName, source));

        // javac options
        List<String> options = new ArrayList<String>();
        options.add("-Xlint:all-unchecked");
        options.add("-g");
        options.add("-deprecation");
        if (sourcePath != null) {
            options.add("-sourcepath");
            options.add(sourcePath);
        }
        if (classPath != null) {
            options.add("-classpath");
            options.add(classPath);
        }
        options.add("-target");
        options.add("1.5");
        
        options.add("-XDdumpfx=" + System.getProperty("java.io.tmpdir"));
        
        // create a compilation task
        JavafxcTask task = tool.getTask(err, manager, diagnostics, options, compUnits);

        if (task.call() == false) {
            if (printDiagnostics) {
		if (diagnostics instanceof DiagnosticCollector) {
		    printDiagnostics((DiagnosticCollector)diagnostics, err);
		}
            }
            return null;
        }
        
        Map<String, byte[]> classBytes = manager.getClassBytes();
        try {
            manager.close();
        } catch (IOException exp) {
        }

        return classBytes; 
    }
    
    static void printDiagnostics(DiagnosticCollector<JavaFileObject> diagnostics, Writer err) {
        // install customized diagnostic message formats, which don't print script name
        com.sun.tools.javac.util.Context context = new com.sun.tools.javac.util.Context();
        com.sun.tools.javac.util.Options options =
                com.sun.tools.javac.util.Options.instance(context);
        options.put("diags", "%l: %t%m|%p%m");

        PrintWriter perr = new PrintWriter(err);
        RedirectedLog log = new RedirectedLog(context, perr);
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            log.report((com.sun.tools.javac.util.JCDiagnostic)diagnostic);
        }
    }

    // read a Reader fully and return the content as string
    private String readFully(Reader reader) throws IOException {
        char[] arr = new char[8*1024]; // 8K at a time
        StringBuilder buf = new StringBuilder();
        int numChars;
        while ((numChars = reader.read(arr, 0, arr.length)) > 0) {
            buf.append(arr, 0, numChars);
        }
        return buf.toString();
    }
    
    private static class RedirectedLog extends com.sun.tools.javac.util.Log {
        RedirectedLog(com.sun.tools.javac.util.Context context, PrintWriter writer) {
            super(context, writer);
        }
    }

    /*
     * javac needs list of classes of a given package to resolve imports.
     * Refer to JavacFileManager.list() method. We want to get list of classes
     * for each package that is visible to our parent class loader. We do that by
     * looking for resource files that lists all the .class files for each package.
     * The format of the file is same as the output of "jar tf <jar-file>". Each
     * line lists a package directory or a .class file entry with full package 
     * directory.
     */
    private static final String CLASS_LIST_RESOURCE = "META-INF/CLASS.LIST";
    
    // Look for class list resources from the given class loader and fill
    // in class list for each package seen.
    private static void fillPackageMap(ClassLoader loader, 
            Map<String, List<String>> packageMap) throws IOException {
        Enumeration<URL> urls = loader.getResources(CLASS_LIST_RESOURCE);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            InputStream stream = url.openStream();
            fillPackageMap(stream, packageMap);
        }
    }

    // Given an input stream of a resource that lists .class entries, fill
    // in the package to classes list map.
    private static void fillPackageMap(InputStream stream, 
            Map<String, List<String>> packageMap) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line = null;      
        while ((line = br.readLine()) != null) {
            if (line.endsWith(".class")) {
                int lastSlash = line.lastIndexOf('/');
                String dir = (lastSlash == -1)? "" : line.substring(0, lastSlash);
                String file = (lastSlash == -1)? line : line.substring(lastSlash + 1);

                List<String> classList;
                String pkgName = dir.replace('/', '.');
                if (! packageMap.containsKey(pkgName)) {
                    classList = new ArrayList<String>();
                    packageMap.put(pkgName, classList);
                } else {
                    classList = packageMap.get(pkgName);
                }
                classList.add(file.substring(0, file.indexOf(".class")));
            }
        }
    }
}
