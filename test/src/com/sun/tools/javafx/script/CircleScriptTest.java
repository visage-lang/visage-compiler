/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.javafx.script;

import com.sun.javafx.api.JavaFXScriptEngine;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Create a simple graphics application.
 */
public class CircleScriptTest {
    
    //TODO: Setup X on build server  @Test
    public void circleScript() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine scrEng = manager.getEngineByExtension("javafx");
        assertTrue(scrEng instanceof JavaFXScriptEngine);
        JavaFXScriptEngine engine = (JavaFXScriptEngine)scrEng;
        String script = readScriptFile("test/src/com/sun/tools/javafx/script/CircleTest.fx");
        Object ret = engine.eval(script);
        assertEquals(ret.getClass().getName(), "javafx.ui.Frame");
    }

    private String readScriptFile(String filename) throws IOException {
        FileReader in = new FileReader(filename);
        StringWriter out = new StringWriter();
        char[] buffer = new char[1024];
        int c;
        while ((c = in.read(buffer)) > 0)
            out.write(buffer, 0, c);
        return out.toString();
    }
}
