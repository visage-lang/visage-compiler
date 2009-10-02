/*
 * Regression test JFXC-3309/JFXC-3489: Java Scripting API does not work in JavaFX 1.2
 *
 * @test
 * @run
 */

import java.io.FileReader;
import javax.script.ScriptEngineManager;

var path = "test/regress/jfxc3309sub.fx";
var manager = new ScriptEngineManager();
var engine = manager.getEngineByExtension("fx");

engine.eval(new FileReader(path));
