/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jfx.assortie.system;

import com.sun.javafx.api.JavafxcTask;
import com.sun.javafx.api.JavafxcTool;
import com.sun.tools.javafx.script.MemoryFileManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;

import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;

public class CodeManager {

    private static JavafxcTool tool = JavafxcTool.create();
        
    private static ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
    private static DiagnosticCollector diagnostics = new DiagnosticCollector(); // <JavaFileObject>();
    private static MemoryFileManager manager = new MemoryFileManager(tool.getStandardFileManager(diagnostics, null, null), currentClassLoader);
    private static PrintWriter err = new PrintWriter(System.err);
    private static List<String> options = new ArrayList<String>();
    private static MemoryClassLoader memoryClassLoader = new MemoryClassLoader();

    public static Object execute(String className, String code) {

        if (!code.contains("package")){
            String pack = className.substring(0, className.lastIndexOf('.'));
            //System.out.println("[code manager] pack: \"" + pack + "\"");
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

            MemoryClassLoader memoryClassLoader = new MemoryClassLoader();
            memoryClassLoader.loadMap(classBytes);

            return ProjectManager.runFXFile(className, memoryClassLoader);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}


class DiagnosticCollector implements DiagnosticListener{

    List<Diagnostic> diagnostics = new LinkedList<Diagnostic>();
    
    public List<Diagnostic> getDiagnostics() {
	return diagnostics;
    }
    
    public void clear(){
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


        if (classBytes.get(name) == null) {
            return super.loadClass(name, resolve);
        }

        Class result = findClass(name);

        if (resolve) {
            resolveClass(result);
        }

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


