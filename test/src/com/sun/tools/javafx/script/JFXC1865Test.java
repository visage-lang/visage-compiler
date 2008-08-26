package com.sun.tools.javafx.script;

import com.sun.javafx.api.JavaFXScriptEngine;
import java.io.File;
import java.io.FileReader;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Robert Field
 * cloned from:
 * @author tball
 */
public class JFXC1865Test {

    @Test
    public void JFXC1865Test() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByExtension("javafx");
        assertTrue(engine instanceof JavaFXScriptEngine);
        File script = new File("test/src/com/sun/tools/javafx/script/JFXC1865.fx");
        engine.getContext().setAttribute(ScriptEngine.FILENAME, script.getAbsolutePath(), ScriptContext.ENGINE_SCOPE);
        Boolean ret = (Boolean)engine.eval(new FileReader(script));
        assertTrue(ret.booleanValue());
    }
}
