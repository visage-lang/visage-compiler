/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.lang;

import java.lang.System;
import com.sun.javafx.runtime.location.AbstractVariable;

import com.sun.javafx.api.JavaFXScriptEngine;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

// factored out to avoid linkage error for javax.script.* on Java 1.5
class Evaluator {
    static Object eval(String script) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine scrEng = manager.getEngineByExtension("javafx");
        JavaFXScriptEngine engine = (JavaFXScriptEngine)scrEng;
        if (engine == null)
            throw new ScriptException("no scripting engine available");
        return engine.eval(script);
    }
}

/**
 * FX, analogous to java.lang.System, is a place to store static utility methods.  
 *
 * @author Brian Goetz
 */
public class FX {
    public static boolean isSameObject(Object a, Object b) {
        return a == b;
    }

    /**
     * Print the Object 'val'.
     * 
     * @param val The Object to be printed
     */
    public static void print(Object val) {
        if (val == null) {
            System.out.print(val);
        } else if (val instanceof String) {
            System.out.print((String) val);
        } else {
            System.out.print(val.toString());
        }
    }

    /**
     * Print the Object 'val' and a new-line.
     * 
     * @param val The Object to be printed
     */
    public static void println(Object val) {
        if (val == null) {
            System.out.println(val);
        } else if (val instanceof String) {
            System.out.println((String) val);
        } else {
            System.out.println(val.toString());
        }
    }

    /**
     * Test if an instance variable has been initialized.
     * 
     * @param varRef The variable to be tested.
     */
    public static boolean isInitialized(Object varRef) {
        return ((AbstractVariable) varRef).isEverValid();
    }

    /**
     * Evaluates a JavaFX Script source string and returns its result, if any.
     * For example, 
     * <br/>
     * This method depends upon the JavaFX Script compiler API being accessible
     * by the application, such as including the <code>javafxc.jar</code> file
     * in the application's classpath.
     * <br/>
     * Note:  this method provides only the simplest scripting functionality;
     * the script is evaluated without any specified context state, nor can 
     * any state it creates during evaluation be reused by other scripts.  For
     * sophisticated scripting applications, use the Java Scripting API
     * (<code>javax.scripting</code>).
     * 
     * @param script the JavaFX Script source to evaluate
     * @return the results from evaluating the script, or null if no results
     *         are returned by the script.
     * @throws javax.script.ScriptException
     */
    public static Object eval(String script) {
        try {
            return Evaluator.eval(script);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
