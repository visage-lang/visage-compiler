/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jfx.assortie.system;

import com.sun.javafx.api.JavafxcTask;
import com.sun.javafx.api.JavafxcTool;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javafx.script.MemoryFileManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;

import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;

public class CodeManager {

    public static Object execute(String className, String code) {

        Context context = new Context();

        JavafxcTool tool = JavafxcTool.create();

        final DiagnosticCollector diagnostics = new DiagnosticCollector<JavaFileObject>();
        DiagnosticListener<JavaFileObject> diagnosticListener = new DiagnosticListener<JavaFileObject>() {
            public void report(Diagnostic<? extends JavaFileObject> rep) {
                diagnostics.report(rep);
            }
        };

        MemoryFileManager manager = new MemoryFileManager(tool.getStandardFileManager(diagnostics, null, null), Thread.currentThread().getContextClassLoader());

        List<JavaFileObject> compUnits = new ArrayList<JavaFileObject>(1);

        compUnits.add(new FXFileObject(className, code));

        PrintWriter err = new PrintWriter(System.err);
        List<String> options = new ArrayList<String>();

        JavafxcTask task = tool.getTask(err, manager, diagnostics, options, compUnits);

        boolean call = task.call();

        Map<String, byte[]> classBytes = manager.getClassBytes();

        for (String key : classBytes.keySet()) {
            System.out.println("key: " + key);
        }

        try {

            MemoryClassLoader memoryClassLoader = new MemoryClassLoader(classBytes);
            Iterable<Class> classes = memoryClassLoader.loadAll();

            for (Class cls : classes) {
                System.out.println("[class] \"" + cls.getName() + "\"");
            }

            return ProjectManager.runFXFile(className, memoryClassLoader);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}

class MemoryClassLoader extends ClassLoader {

    Map<String, byte[]> classBytes;

    public MemoryClassLoader(Map<String, byte[]> classBytes) {
        this.classBytes = classBytes;
    }

    public Class load(String className) throws ClassNotFoundException {
        return loadClass(className);
    }

    public Iterable<Class> loadAll() throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>(classBytes.size());
        for (String name : classBytes.keySet()) {
            classes.add(loadClass(name));
        }
        return classes;
    }

    @Override
    protected Class findClass(String className) throws ClassNotFoundException {
        byte[] buf = classBytes.get(className);
        if (buf != null) {
            // clear the bytes in map -- we don't need it anymore
            classBytes.put(className, null);
            return defineClass(className, buf, 0, buf.length);
        } else {
            return super.findClass(className);
        }
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
        String fileName = className.replace('.', '/') + ".fx";
        return fileName;
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return code;
    }

    private static URI toURI(String className) {
        String fileName = className.replace('.', '/') + ".fx";
        return URI.create("mfm:///" + fileName);

    }

    private void print(String text) {
        System.out.println("[file object] " + text);
    }
}


