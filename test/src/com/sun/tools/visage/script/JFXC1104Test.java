/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.visage.script;

import com.sun.visage.api.JavaFXScriptEngine;
import java.io.File;
import java.io.FileReader;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tball
 */
public class JFXC1104Test {

    @Test
    public void jfxc1104Test() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByExtension("visage");
        assertTrue(engine instanceof JavaFXScriptEngine);
        File script = new File("test/src/com/sun/tools/visage/script/JFXC1104.visage");
        engine.getContext().setAttribute(ScriptEngine.FILENAME, script.getAbsolutePath(), ScriptContext.ENGINE_SCOPE);
        String ret = (String)engine.eval(new FileReader(script));
        assertTrue(ret.startsWith("file:") && ret.endsWith("/JFXC1104.visage"));
    }
}
