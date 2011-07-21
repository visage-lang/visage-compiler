/*
 * Copyright 2010 Visage Project
 *
 * This file is part of Visage. Visage is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * Visage is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.visage.lang;

import com.sun.visage.api.JavaFXScriptEngine;
import java.io.File;
import java.io.FileReader;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for the Visage project Issue 19 (http://code.google.com/p/visage/issues/detail?id=19).
 *
 * @author J.H. Kuperus
 */
public class V19Test {

  /**
   * Tests for regression on the isInitialized function.
   */
  @Test
  public void isInitializedNonNullTrue() throws Exception {
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByExtension("visage");
    assertTrue(engine instanceof JavaFXScriptEngine);
    File script = new File("test/src/org/visage/lang/V19_isInitializedNonNullTrue.visage");
    engine.getContext().setAttribute(ScriptEngine.FILENAME, script.getAbsolutePath(), ScriptContext.ENGINE_SCOPE);
    Boolean ret = (Boolean)engine.eval(new FileReader(script));
    assertTrue(ret.booleanValue());
  }

  /**
   * Tests for regression on the isInitialized function.
   */
  @Test
  public void isInitializedNonNullFalse() throws Exception {
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByExtension("visage");
    assertTrue(engine instanceof JavaFXScriptEngine);
    File script = new File("test/src/org/visage/lang/V19_isInitializedNonNullFalse.visage");
    engine.getContext().setAttribute(ScriptEngine.FILENAME, script.getAbsolutePath(), ScriptContext.ENGINE_SCOPE);
    Boolean ret = (Boolean)engine.eval(new FileReader(script));
    assertFalse(ret.booleanValue());
  }

  /**
   * Tests for the actual issue on the isInitialized function.
   */
  @Test
  public void isInitializedNull() throws Exception {
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByExtension("visage");
    assertTrue(engine instanceof JavaFXScriptEngine);
    File script = new File("test/src/org/visage/lang/V19_isInitializedNull.visage");
    engine.getContext().setAttribute(ScriptEngine.FILENAME, script.getAbsolutePath(), ScriptContext.ENGINE_SCOPE);
    Boolean ret = (Boolean)engine.eval(new FileReader(script));
    assertFalse(ret.booleanValue());
  }

}