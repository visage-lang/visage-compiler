/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.javafx.script;

import com.sun.javafx.api.JavaFXScriptEngine;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import javax.script.*;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for JavaFX Script scripting engine.
 * 
 * @author Tom Ball
 */
public class JavaFXScriptEngineTest {
    private JavaFXScriptEngine engine;
    private ByteArrayOutputStream out;
    private PrintStream stdout;
    private static PrintStream originalOut;
    
    @BeforeClass
    public static void saveSystemOut() {
        originalOut = System.out;
    }

    @Before
    public void setUp() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine scrEng = manager.getEngineByExtension("javafx");
        assertTrue(scrEng instanceof JavaFXScriptEngine);
        engine = (JavaFXScriptEngine)scrEng;
        out = new ByteArrayOutputStream();
        stdout = new PrintStream(out);
    }

    @Test
    public void simpleScript() throws Exception {
        try {
            System.setOut(stdout);
            engine.eval("java.lang.System.out.println(\"Hello, world\");");
            assertEquals("Hello, world\n", getOutput());
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void scriptWithBinding() throws Exception {
        try {
            System.setOut(stdout);
            Bindings bindings = new SimpleBindings();
            bindings.put("who", "world");

            engine.eval("java.lang.System.out.println(\"Hello, {who}\");", bindings);
            assertEquals("Hello, world\n", getOutput());
        } finally {
            System.setOut(originalOut);
        }
    }
    
    @Test
    public void scriptResult() throws Exception {
        Object ret = engine.eval("var a = 1; a + 2;");
        assertNotNull(ret);
        assertEquals(((Number)ret).intValue(), 3);        
    }
    
    @Test
    public void compiledScript() throws Exception {
        try {
            System.setOut(stdout);
            CompiledScript script = engine.compile(
                    "java.lang.System.out.println(\"Hello, {who}\");");
            Bindings bindings = new SimpleBindings();
            bindings.put("who", "world");
            script.eval(bindings);
            assertEquals("Hello, world\n", getOutput());
        } finally {
            System.setOut(originalOut);
        }
    }
    
    @Test
    public void verifyGlobalBindings() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        manager.put("greeting", "Hello");
        engine = (JavaFXScriptEngine)manager.getEngineByExtension("javafx");
        String script = "java.lang.System.out.println(\"{greeting}, {who}\");";

        try {
            System.setOut(stdout);
            Bindings bindings = new SimpleBindings();
            bindings.put("who", "world");
            engine.eval(script, bindings);
            assertEquals("Hello, world\n", getOutput());
            bindings.clear();
            bindings.put("who", "moon");
            engine.eval(script, bindings);
            assertEquals("Hello, moon\n", getOutput());
        } finally {
            System.setOut(originalOut);
        }
    }
    
    @Test
    public void invokeFunction() throws Exception {
        engine.compile(
            "function add(a:Integer, b:Integer):Integer { return a + b; }");
        Object ret = ((Invocable)engine).invokeFunction("add", 1, 2);
        assertNotNull(ret);
        assertEquals(((Number)ret).intValue(), 3);        
    }
    
    @Test
    public void invokeMethod() throws Exception {
        String script =
            "class Test{ function hello():String {return \"Hello, world\";}}\n" +
            "function create():Test { return new Test(); }";

        engine.compile(script);
        Object test = engine.invokeFunction("create");
        assertNotNull(test);
        Object ret = engine.invokeMethod(test, "hello");
        assertNotNull(ret);
        assertEquals("Hello, world", ret.toString());
    }
    
    @Test
    public void verifyErrorLineNumber() throws Exception {
        // test that bindings don't disturb a script's line numbers when an error is reported
        String script =
            "class Test {                   // 1\n" +
            "   function hello():String {   // 2\n" + 
            "      return \"Hello, world\"; // 3\n" + 
            "   }                           // 4\n" +
            "}                              // 5\n" +
            "                               // 6\n" +
            "var t = new Test();            // 7\n" +
            "t.hello(1); // invalid parameter  8\n";

        DiagnosticCollector<JavaFileObject> diags = new DiagnosticCollector<JavaFileObject>();
        JavaFXScriptEngine jfxEngine = (JavaFXScriptEngine)engine;
        try {
            jfxEngine.eval(script, diags);
            fail("script should have thrown ScriptException due to bad code");
        } catch (ScriptException e) {
            List<Diagnostic<? extends JavaFileObject>> errorList = diags.getDiagnostics();
            assertTrue(errorList.size() == 1);
            Diagnostic<? extends JavaFileObject> error = errorList.get(0);
            assertTrue(error.getKind() == Diagnostic.Kind.ERROR);
            assertTrue(error.getLineNumber() == 8);
        }
    }
    
    @Test
    public void verifyErrorLineNumberWithBinding() throws Exception {
        // test that bindings don't disturb a script's line numbers when an error is reported
        String script =
            "class Test {                                    // 1\n" +
            "   function hello(s:String):String {            // 2\n" +
            "       return \"hello, {s}\";                   // 3\n" +
            "   }                                            // 4\n" +
            "}                                               // 5\n" +
            "var hello = who;       // who defined in binding   6\n" +
            "var t = new Test();                             // 7\n" +
            "t.hello(1);            // invalid parameter        8\n";

        Bindings bindings = new SimpleBindings();
        bindings.put("who", "world");

        DiagnosticCollector<JavaFileObject> diags = new DiagnosticCollector<JavaFileObject>();
        JavaFXScriptEngine jfxEngine = (JavaFXScriptEngine)engine;
        try {
            jfxEngine.eval(script, bindings, diags);
            fail("script should have thrown ScriptException due to bad code");
        } catch (ScriptException e) {
            List<Diagnostic<? extends JavaFileObject>> errorList = diags.getDiagnostics();
            assertTrue(errorList.size() == 1);
            Diagnostic<? extends JavaFileObject> error = errorList.get(0);
            assertTrue(error.getKind() == Diagnostic.Kind.ERROR);
            assertTrue(error.getLineNumber() == 8);
        }
    }
   
    private String getOutput() {
        stdout.flush();
        String output = out.toString();
        out.reset();
        return output;
    }
    
    @AfterClass
    public static void restoreSystemOut() {
        System.setOut(originalOut);
    }
}