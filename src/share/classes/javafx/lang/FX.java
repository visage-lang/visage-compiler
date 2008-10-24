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

import java.util.Vector;
import com.sun.javafx.runtime.Entry;
import com.sun.javafx.functions.Function0;
import com.sun.javafx.runtime.SystemProperties;
import com.sun.javafx.runtime.FXExit;

import com.sun.javafx.runtime.sequence.Sequence;

/**
 * FX, analogous to java.lang.System, is a place to store static utility methods.  
 *
 * @author Brian Goetz
 * @author Saul Wold
 * 
 * @profile common
 */
public class FX {

    public static boolean isSameObject(Object a, Object b) {
        return Builtins.isSameObject(a, b);
    }

    /**
     * Print the Object 'val'.
     * 
     * @param val The Object to be printed
     */
    public static void print(Object val) {
        Builtins.print(val);
    }

    /**
     * Print the Object 'val' and a new-line.
     * 
     * @param val The Object to be printed
     */
    public static void println(Object val) {
        Builtins.println(val);
    }

    /**
     * Test if an instance variable has been initialized.
     * 
     * @param varRef The variable to be tested.
     */
    public static boolean isInitialized(Object varRef) {
        return Builtins.isInitialized(varRef);
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
     * @return The string value of the property
     * @throws SecurityException if a security manager exists and its checkPropertyAccess method doesn't allow access to the specified system property. 
     * @throws NullPointerException if key is null. 
     * @profile common
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
     * This may cause the Runtime to exit as {@code System.exit()} 
     * depending on the underlying implementation.
     * </p><p> 
     * Any Shutdown Actions that were previously added using the 
     * {@code addShutdownAction()} function will be exectued at this time
     * in LIFO ordering.
     * </p><p>
     * A second call to {@code FX.exit()} once {@code FX.exit()} has 
     * started will result a {@code IllegalStateException} to be thrown,
     * this can occur if a {@code Timeline} calls {@code FX.exit()} while
     * FX.exit is started.
     * If a call to {@code FX.exit()} occurs in a Shutdown Action, that
     * action's function will simply exit without completing the rest of
     * its operation and the next Shutdown Action, if any, will run.
     * </p><p>
     * This function will not normally return to the calling Script.
     * </p>
     *
     * @profile common
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
         * Use of FXExit here is needed because the EDT
         * will pass it along rather than catch and quit
         */
        throw new FXExit();
    }

    /**
     * Adds an action to the queue to be executed at {@code FX.exit()} time
     * This action will be added to the queue as a push stack, meaning that
     * they will be excuted in FILO ordering. Duplicate actions are
     * not allowed and will cause the orignal Handle to be returned with
     * no reordering.
     * 
     * @param  action of type {@code function():Void} that will be executed
     * at {@code FX.exit()} time. Only one copy of an action can be in 
     * the queue, an attempt to add the same action a second time will
     * return the previous Handle without any reodering.
     * @return Handle used to remove the action if needed.
     *
     * @profile common
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
     * @param  action of type {@code function():Void} that will be removed
     * from the Shutdown Action Stack
     * @return a Boolean value signifing sucess or failure of removing
     * the action
     *
     * @profile common
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
     * @param  action of type {@code function():Void} that will be executed
     * later based on the implementation.
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

    /**
     * For JavaFX Script applications that are started on the command
     * line,running application. Returns null if there were no incoming
     * arguments or if this application was not invoked from the
     * command line. 
     *
     * @profile common
     */
    public static Sequence<String> getArguments() {
        return Entry.getArguments();
    }

    /**
     * Returns the named incoming argument for the current JavaFX
     * Script program; this is used for certain environments (in
     * particular, applets) where incoming arguments are represented
     * as name/value pairs. This usually returns a String, but some
     * environments may return other kinds of values. Accepts numbers
     * in the form of Strings (e.g. {@code getArgument("0")}) to
     * provide unification with {@link #getArguments getArguments}.
     * Returns null if the given named argument does not exist.
     *
     * @profile common
     */
    public static Object getArgument(String key) {
        return Entry.getArgument(key);
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

            // Check for action already in Vector
            int index = handles.indexOf(hash);
            if (index != -1) {
                // Return the hash without reorder
                return hash;
            }
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
                // Execute LIFO Action
                action = (Function0<Void>) actions.lastElement();
                actions.remove(actions.lastIndexOf(action));
                if (action != null) {
                    try {
                        /*
                         * TODO: add timer to kill long running Action
                         */
                        action.invoke();
                    } catch (Throwable ignore) {
                        // Ignore all Throwables
                    }
                }
            }
            handles.removeAllElements();
        }
    }
}
