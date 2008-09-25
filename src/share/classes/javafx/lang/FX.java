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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.sun.javafx.api.JavaFXScriptEngine;
import com.sun.javafx.runtime.location.BindableLocation;

import java.util.Vector;
import com.sun.javafx.runtime.Entry;
import com.sun.javafx.functions.Function0;
import com.sun.javafx.runtime.SystemProperties;

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
 * @author Saul Wold
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
        return ((BindableLocation) varRef).isInitialized();
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

    /**
     * Retrieve System Property.
     * System Properties in JavaFX environment can be classified into 3 types:
     * 1. Runtime platform associated property. 
     *    Those properties have an equivalent in current java runtime 
     *    environment (SE/ME). The FX.getProperty() method retrieves
     *    those properties by mapping specified key with runtime platform key. 
     * </p>
     * 2. JavaFX specific property. 
     *    Those properties are specific to JavaFX environment therefore
     *    value of the properties is specified in the JavaFX tree.
     * </p>
     * 3. The property has no association with current runtime platform 
     *    nor is JavaFX specific, therefore those properties are not 
     *    supported and null is returned.
     * @param key Environment Property to be inquired
     * @return the string value of the property
     */
    public static String getProperty (String key) {
        return SystemProperties.getProperty(key);
    }
 
    /*
     * This static will be unique to each applet, when we move
     * to the FXME/Embedded this will need to be tied the AMS
     * TODO: for Mobile Guys
     */
    private static FXSystemActionData exitData = new FXSystemActionData();

    /**
     * Exits the Script and causes any Shutdown Actions to be called
     * This map cause the Runtime to exit as System.exit() depending on
     * the underlying implementation.
     *
     * The behavoir is similar to the POSIX 'C' exit() with the
     * Shutdown Action Stack similar to atexit()/on_exit() functionality
     *
     * This function will not normally return to the calling Script
     * 
     */
    public static void exit() {
        if (exitData.called) {
            throw new IllegalStateException("Can not call FX.exit() twice");
        } else {
            exitData.called = true;
        }
        /*
         * Run the exit actions
         */
        exitData.runActions();

        /*
         * Call to Entry.java in order to get the RuntimeProvider
         * to do additional cleanup as needed for that provider
         */
        Entry.exit();

        /*
         * Mark this as false for handling restarting from the
         * same VM or Browser Context
         */
        exitData.called = false;
        
        /*
         * Use of ThreadDeath here is needed because the EDT
         * will pass it along rather than catch and quit
         */
        throw new ThreadDeath();
    }

    /**
     * Adds an action to the queue to be executed at FX.exit() time
     * This action will be added to the queue as a push stack, meaning that
     * they will be excuted in FILO ordering.
     *
     * @param  action of type function():Void  that will be executed
     * at FX.exit() time
     * @return Handle used to remove the action if needed, 0 means failure
     */
    public static int addShutdownAction(Function0<Void> action) {
        if (action == null) {
            throw new NullPointerException("Action function can not be null");
        } else {
            return exitData.addAction(action);
        }
    }

    /**
     * Removes the action from the queue specified by the actionType parameter. 
     *
     * @param  action of type function():Void that will be removed from
     * the Shutdown Action Stack
     * @return a Boolean value signifing sucess or failure of removing
     * the action
     */
    public static boolean removeShutdownAction(int handle) {
        return exitData.removeAction(handle);
    }

    /**
     * A {@code deferAction} represents an action that should be executed at a
     * later time of the system's choosing.
     * <p />
     * In systems based on event dispatch, such as Swing, execution of a
     * {@code deferAction} generally means putting it on the event queue
     * for later processing.
     *
     * @profile common
     */
    public static void deferAction(Function0<Void> action) {
        if (action == null) {
            throw new NullPointerException("Action function can not be null");
        } else {
            Entry.deferAction(action);
        }
    }

    /*
     * This inner help class is used to store the Action Data needed 
     * for exitActions, in the future there may me addition System 
     * Actions that will be required such as Low Resources or ...
     */
    private static class FXSystemActionData {
        boolean called;
        Vector actions, handles;

        private static FXSystemActionData singleton = null;

        private FXSystemActionData() {
            called = false;
            actions = new Vector();
            handles = new Vector();
        }

        int addAction(Object action) {
            int hash = action.hashCode();
            actions.addElement(action);
            handles.addElement(hash);
            return hash;
        }

        boolean removeAction(int handle) {
            if (handles.isEmpty() || !handles.contains(handle)) {
                return false;
            } else {
                try {
                    int index = handles.indexOf(handle);
                    if (index != -1) {
                        actions.removeElementAt(index);
                        handles.removeElementAt(index);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    return false;
                }
                return true;
            }
        }

        void runActions() {
            if (actions == null) {
                return;
            }
            while (!actions.isEmpty()) {
                Function0<Void> action = null;
                action = (Function0<Void>) actions.lastElement();
                actions.removeElement(action);
                if (action != null) {
                    try {
                        /*
                         * TODO: add timer to kill long running Action
                         */
                        action.invoke();
                    } catch (ThreadDeath td) {
                        throw td;
                    } catch (Throwable t) {
                        // Ignore all other Throwables
                    }
                }
            }
            handles.removeAllElements();
        }
    }
}
