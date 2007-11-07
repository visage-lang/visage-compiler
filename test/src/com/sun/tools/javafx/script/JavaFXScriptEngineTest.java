/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.javafx.script;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.script.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for JavaFX Script scripting engine.
 * 
 * @author Tom Ball
 */
public class JavaFXScriptEngineTest {
    private ScriptEngine engine;
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
        engine = manager.getEngineByExtension("javafx");
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
    
    @Ignore("script return values not implemented yet")
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
            CompiledScript script = ((Compilable)engine).compile(
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
        engine = manager.getEngineByExtension("javafx");
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
        ((Compilable)engine).compile(
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

        ((Compilable)engine).compile(script);
        Object test = ((Invocable)engine).invokeFunction("create");
        assertNotNull(test);
        Object ret = ((Invocable)engine).invokeMethod(test, "hello");
        assertNotNull(ret);
        assertEquals("Hello, world", ret.toString());
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