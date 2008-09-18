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
import java.awt.Window;
import java.lang.ThreadDeath;
import java.util.NoSuchElementException;
import java.lang.ArrayIndexOutOfBoundsException;
import sun.awt.AppContext;
import com.sun.javafx.runtime.Entry;
import com.sun.javafx.functions.Function0;

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

    private static final String JAVAFX_EXIT_KEY = "JavaFX Exit Key";


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
        FXSystemActionData exitData = FXSystemActionData.getInstance(JAVAFX_EXIT_KEY);
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
         * Clean up any open AWT Windows so that the runtime will exit cleanly.
         * TODO: find a way to do better clean up.
         */
        for(Window w : Window.getWindows()) {
            // check for JFrame to not dispose of appletview window
            if (w instanceof javax.swing.JFrame)
                w.dispose();
        }
        exitData.called = false;
        throw new ThreadDeath();
    }

    /**
     * Adds an action to the queue to be executed at FX.exit() time
     * This action will be added to the queue as a push stack, meaning that
     * they will be excuted in FILO ordering.
     *
     * @param  action of type function():Void  that will be executed at FX.exit() time
     * @return Handle used to remove the action if needed, 0 means failure
     */
    public static int addShutdownAction(Function0<Void> action) {
        if (action == null) {
            throw new NullPointerException("Action function can not be null");
        } else {
            FXSystemActionData exitData = FXSystemActionData.getInstance(JAVAFX_EXIT_KEY);
            return exitData.addAction(action);
        }
    }

    /**
     * Removes the action from the queue specified by the actionType parameter. 
     *
     * @param  action of type function():Void that will be removed from the Shutdown Actio Stack
     * @return a Boolean value signifing sucess or failure of removing the action
     */
    public static boolean removeShutdownAction(int handle) {
        FXSystemActionData exitData = FXSystemActionData.getInstance(JAVAFX_EXIT_KEY);
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
            Entry.deferTask(action);
        }
    }

    private static class FXSystemActionData {
        boolean called;
        Vector actions, handles;
        AppContext appContext;
        Object actionKey;

        private FXSystemActionData(AppContext ac, Object key) {
            called = false;
            actions = new Vector();
            handles = new Vector();
            actionKey = key;
            appContext = ac;
        }
        static synchronized FXSystemActionData getInstance(Object key) {
            AppContext ac = java.security.AccessController.doPrivileged(
                new java.security.PrivilegedAction<AppContext>() {
                    public AppContext run() {
                        return AppContext.getAppContext();
                    }
                }
            );
            Object sd = ac.get(key);
            if (sd == null) {
                sd = new FXSystemActionData(ac, key);
                ac.put(key, sd);
            }
            return (FXSystemActionData) sd;
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
                    } catch (Throwable t) {
                        if (t instanceof ThreadDeath) {
                            throw (ThreadDeath) t;
                        }
                    }
                }
            }
            handles.removeAllElements();
            appContext.remove(actionKey);
        }
    }
}
