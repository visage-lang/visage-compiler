/*
 * Copyright 1999-2008 Sun Microsystems, Inc.  All Rights Reserved.
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
package assortis.core;

import com.sun.javafx.api.JavafxcTask;
import com.sun.javafx.runtime.Entry;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;

import com.sun.tools.javafx.api.JavafxcTool;
import com.sun.tools.javafx.script.MemoryFileManager;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

public class ProjectManager {

    private static final String[] commandLineArgs = new String[]{};

    public static String getFileName(String className) {
        return className.substring(className.lastIndexOf('.') + 1) + ".fx";
    }
    
    public static String getFilePath(String className) {
        return className.replace('.', '/') + ".fx";
    }
    
    public static String readResource(String className) throws Exception {
        return readResource(className, getFileName(className));
    }

    public static String readResource(String className, String resource) throws Exception {
        Class cls = Class.forName(className);
        return readResource(cls.getResourceAsStream(resource));
    }

    public static String readResource(InputStream is) throws Exception {
        StringBuffer contents = new StringBuffer();
        if (is != null) {
            //LATER: specify encoding properly!
            BufferedReader input = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = null;
            while ((line = input.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        }
        return contents.toString();
    }

    public static Object runFXFile(String name) throws Exception {
        return runFXFile(name, Thread.currentThread().getContextClassLoader());
    }

    public static Object runFXFile(String name, ClassLoader classLoader) throws Exception {
        try {
            Class cls = classLoader.loadClass(name);
            Method run = cls.getDeclaredMethod(Entry.entryMethodName(), Sequence.class);
            Object args = Sequences.make(String.class, commandLineArgs);
            return run.invoke(null, args);
        } catch (Throwable e) {
            throw new Exception("FX file: \"" + name + "\" was not compiled!!", e);
        }
    }

    public static List<LocaleItem> getFXPropertiesLocales(String className) {

        List<LocaleItem> locs = new ArrayList<LocaleItem>();

        try {
            Class cls = Class.forName(className);

            for (Locale loc : Locale.getAvailableLocales()) {
                String name = "" + cls.getSimpleName() + "_" + loc + ".fxproperties";

                String text = readResource(className, name);
                if (!"".equals(text)) {
                    locs.add(new LocaleItem(loc, text));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (locs.size() == 0) {
            locs.add(new LocaleItem(Locale.ENGLISH, ""));
        }

        return locs;
    }

    public static Object runFXCode(String className, String code, String localeName, String propertyText) throws Exception {
        return CodeManager.execute(className, code, localeName, propertyText);
    }
    
    
    public static void setLookAndFeel(){
        try{
            org.jvnet.substance.SubstanceLookAndFeel.setFontPolicy(org.jvnet.substance.fonts.SubstanceFontUtilities.getScaledFontPolicy(1.25f));
            javax.swing.UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceNebulaLookAndFeel");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}


class CodeManager {

    private static JavafxcTool tool = JavafxcTool.create();
    private static ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
    private static DiagnosticCollector diagnostics = new DiagnosticCollector(); // <JavaFileObject>();

    private static MemoryFileManager manager = new MemoryFileManager(tool.getStandardFileManager(diagnostics, null, null), currentClassLoader);
    private static PrintWriter err = new PrintWriter(System.err);
    private static List<String> options = new ArrayList<String>();
    private static MemoryClassLoader memoryClassLoader = new MemoryClassLoader();

    
    public static Object execute(String className, String code, String propName, String props) {

        if (!code.contains("package")) {
            String pack = className.substring(0, className.lastIndexOf('.'));
            code = "package " + pack + ";\n" + code;
        }
        List<JavaFileObject> compUnits = new ArrayList<JavaFileObject>(1);

        compUnits.add(new FXFileObject(className, code));

        diagnostics.clear();
        JavafxcTask task = tool.getTask(err, manager, diagnostics, options, compUnits);


        if (!task.call()) {
            return diagnostics;
        }

        Map<String, byte[]> classBytes = manager.getClassBytes();

        try {
             //LATER: deal with encoding properly
            //System.out.println("[project manager] prop name:" + propName + " props: " + props);
            classBytes.put(propName, props.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);
        }

        try {

            MemoryClassLoader memoryClassLoader = new MemoryClassLoader();
            memoryClassLoader.loadMap(classBytes);

            return ProjectManager.runFXFile(className, memoryClassLoader);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

class DiagnosticCollector implements DiagnosticListener {

    List<Diagnostic> diagnostics = new LinkedList<Diagnostic>();

    public List<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public void clear() {
        diagnostics.clear();
    }

    public void report(Diagnostic diagnostic) {
        diagnostics.add(diagnostic);
    }
}

class MemoryClassLoader extends ClassLoader {

    Map<String, byte[]> classBytes;

    public MemoryClassLoader() {
        classBytes = new HashMap<String, byte[]>();
    }

    public void loadMap(Map<String, byte[]> classBytes) throws ClassNotFoundException {
        for (String key : classBytes.keySet()) {
            this.classBytes.put(key, classBytes.get(key));
        }
    }

    protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {

        if (classBytes.get(name) == null) { return super.loadClass(name, resolve); }
        
        Class result = findClass(name);
        
        if (resolve) { resolveClass(result); }

        return result;
    }

    @Override
    protected Class findClass(String className) throws ClassNotFoundException {
        byte[] buf = classBytes.get(className);
        if (buf != null) {
            classBytes.put(className, null);
            return defineClass(className, buf, 0, buf.length);
        } else {
            return super.findClass(className);
        }
    }
    
    @Override
    public InputStream getResourceAsStream(String resource) {
        //System.out.println("[memory class loader] read resource: " + resource);
        if (resource.endsWith(".fxproperties")) {
            String localeName = Locale.getDefault().getDisplayName();
            byte[] propBytes = classBytes.get(localeName);

            if (propBytes != null) {
                return new ByteArrayInputStream(propBytes);
            }
        }
        return super.getResourceAsStream(resource);
    }
}
class FXFileObject extends SimpleJavaFileObject {

    String code;
    String className;

    public FXFileObject(String className, String code) {
        super(toURI(className), Kind.SOURCE);
        this.code = code;
        this.className = className;
    }

    public boolean isNameCompatible(String simpleName, Kind kind) {
        return true;
    }

    public URI toUri() {
        return toURI(className);
    }

    public String getName() {
        return ProjectManager.getFileName(className);
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return code;
    }

    private static URI toURI(String className) {
        return URI.create("./" + ProjectManager.getFilePath(className));
    }

    private void print(String text) {
        System.out.println("[file object] " + text);
    }
}
