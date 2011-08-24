package org.visage.tools.script;

import org.visage.api.VisageScriptEngine;
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
        ScriptEngine engine = manager.getEngineByExtension("visage");
        assertTrue(engine instanceof VisageScriptEngine);
        File script = new File("test/src/org/visage/tools/script/JFXC1865.visage");
        engine.getContext().setAttribute(ScriptEngine.FILENAME, script.getAbsolutePath(), ScriptContext.ENGINE_SCOPE);
        Boolean ret = (Boolean)engine.eval(new FileReader(script));
        assertTrue(ret.booleanValue());
    }
}
