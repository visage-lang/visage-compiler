/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.javafx.api.JavaFXScriptEngine;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import javax.script.*;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
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
    private ByteArrayOutputStream err;
    private PrintStream stderr;
    private static PrintStream originalErr;
    
    @BeforeClass
    public static void saveSystemOutErr() {
        originalOut = System.out;
        originalErr = System.err;
    }

    @Before
    public void setUp() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine scrEng = manager.getEngineByName("javafx");
        assertTrue(scrEng instanceof JavaFXScriptEngine);
        engine = (JavaFXScriptEngine)scrEng;
        out = new ByteArrayOutputStream();
        stdout = new PrintStream(out);
        System.setOut(stdout);
        err = new ByteArrayOutputStream();
        stderr = new PrintStream(err);
        System.setErr(stderr);
    }
    
    @Test
    public void getEngineByName() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine scrEng = manager.getEngineByName("javafx");
        assertTrue(scrEng instanceof JavaFXScriptEngine);
    }
    
    @Test
    public void getEngineByBadName() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine scrEng = manager.getEngineByName("java");
        assertFalse(scrEng instanceof JavaFXScriptEngine);
    }
    
    @Test
    public void getEngineByExtension() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine scrEng = manager.getEngineByName("fx");
        assertTrue(scrEng instanceof JavaFXScriptEngine);
    }
    
    @Test
    public void getEngineByBadExtension() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine scrEng = manager.getEngineByName("java");
        assertFalse(scrEng instanceof JavaFXScriptEngine);
    }

    @Test
    public void simpleScript() throws Exception {
        engine.eval("java.lang.System.out.print(\"Hello, world\");" +
                    "java.lang.System.out.flush();");
        assertEquals("Hello, world", getOutput());
    }

    @Test
    public void scriptWithBinding() throws Exception {
        Bindings bindings = new SimpleBindings();
        bindings.put("who", "world");

        engine.eval("java.lang.System.out.print(\"Hello, {who}\");" +
                    "java.lang.System.out.flush();", bindings);
        assertEquals("Hello, world", getOutput());
    }

    @Test
    public void scriptWithConflictingBinding() throws Exception {
        Bindings bindings = new SimpleBindings();
        bindings.put("who", "world");
        bindings.put("howMany", "lots");           // type of howMany attribute is String

        engine.eval("var howMany: Integer = 1;" +  // versus declared howMany's Integer
                    "java.lang.System.out.print(\"Hello, {who}\");" +
                    "java.lang.System.out.flush();", bindings);
        assertEquals("Hello, world", getOutput());
    }
    
    @Test
    public void scriptResult() throws Exception {
        Object ret = engine.eval("var a = 1; a + 2;");
        assertNotNull(ret);
        assertEquals(((Number)ret).intValue(), 3);        
    }
    
    @Test
    public void compiledScript() throws Exception {
        CompiledScript script = engine.compile(
                "java.lang.System.out.print(\"Hello, {who}\");" +
                "java.lang.System.out.flush();");
        Bindings bindings = new SimpleBindings();
        bindings.put("who", "world");
        script.eval(bindings);
        assertEquals("Hello, world", getOutput());
    }
    
    @Test
    public void verifyGlobalBindings() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        manager.put("greeting", "Hello");
        engine = (JavaFXScriptEngine)manager.getEngineByExtension("javafx");
        String script = "java.lang.System.out.print(\"{greeting}, {who}\");" +
                        "java.lang.System.out.flush();";

        Bindings bindings = new SimpleBindings();
        bindings.put("who", "world");
        engine.eval(script, bindings);
        assertEquals("Hello, world", getOutput());
        bindings.clear();
        bindings.put("who", "moon");
        engine.eval(script, bindings);
        assertEquals("Hello, moon", getOutput());
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
            "class Test{ function hello():String {return \"Hello, world\";}}" +
            "function create():Test { return new Test(); }";

        engine.compile(script);
        Object test = engine.invokeFunction("create");
        assertNotNull(test);
        Object ret = engine.invokeMethod(test, "hello");
        assertNotNull(ret);
        assertEquals("Hello, world", ret.toString());
    }
	
	@Test
	public void invokeMethodParam() throws Exception {
        String script =
            "class Test{ function getClass(o: Test): java.lang.Class {return o.getClass();}}" +
            "function create():Test { return new Test(); }";

        engine.compile(script);
        Object test = engine.invokeFunction("create");
        assertNotNull(test);
        Object ret = engine.invokeMethod(test, "getClass", test);
        assertNotNull(ret);
        assertEquals( test.getClass(), ret );
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
            assertEquals(8, error.getLineNumber());
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
            assertEquals(8, error.getLineNumber());
        }
    }
    
    @Test
    public void verifyNoExtraBindings() throws Exception {
        // When entering var declarations in JavaFXPad, a user may reference
        // a variable before declaring it.  This should return an error, but 
        // the script engine used to declare the var a binding, causing the
        // declaration to fail when the var's type is later declared.
        // This test verifies that no bindings are inferred during an eval.
        try {
            engine.eval("x = 2;");
            fail("script should have failed due to missing declaration");
        } catch (ScriptException e) {
            Object result = engine.eval("var x = 2;");
            assertNull(result);  // declarations don't return results
        }
    }
   
    private String getOutput() {
        stdout.flush();
        String output = out.toString();
        out.reset();
        return output;
    }
    
    @AfterClass
    public static void restoreSystemOutErr() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}
