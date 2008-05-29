package com.sun.tools.javafx.preview;

import com.sun.javafx.api.JavafxcTask;
import com.sun.tools.javafx.api.JavafxcTool;
import com.sun.tools.javafx.script.MemoryFileManager;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.tools.JavaFileObject;

public class CodeManager {

    private static JavafxcTool tool = JavafxcTool.create();
    private static ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
    private static DiagnosticCollector diagnostics = new DiagnosticCollector(); // <JavaFileObject>();

    private static MemoryFileManager manager = new MemoryFileManager(tool.getStandardFileManager(diagnostics, null, null), currentClassLoader);
    private static PrintWriter err = new PrintWriter(System.err);
    private static List<String> options = new ArrayList<String>();
    private static MemoryClassLoader memoryClassLoader = new MemoryClassLoader();

    
    public static Object execute(String className, String code, String propName, String props) {

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
            classBytes.put(propName, props.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);
        }

        try {

            MemoryClassLoader memoryClassLoader = new MemoryClassLoader();
            memoryClassLoader.loadMap(classBytes);

            return Util.runFXFile(className, memoryClassLoader);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}