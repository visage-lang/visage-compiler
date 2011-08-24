/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package visage.lang;

import org.visage.runtime.VisageObject;

/**
 * These functions are automatically imported for
 * all Visage Files to use
 *
 * @author Brian Goetz
 * @author Saul Wold
 * @profile common
 */
public class Builtins {
    /**
     * Compare 2 Visage Objects
     * 
     * @param a The first object to be compared
     * @param b the second object to be compared
     * @return true if they are the same Object
     */
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
     * <p>
     * Can also be used on the current class via a "this" reference to determine
     * if variable initialization has completed.
     * <p>
     * When used on an instance variable, the semantics are as follows:
     * <ul>
     * <li>Initialized is false for all variables when a class is first initiated
     * <li>As variables have their initial values set this becomes true except for variables that have no default specified
     * <li>Upon setting a variable value to a non-default value this also becomes true
     * </ul>
     *
     * @param instance instance to be tested
     * @param offset offset of variable to be tested
     * @return true if the variable has been initialized
     */
    @org.visage.runtime.annotation.VisageSignature("(Ljava/lang/Object;)Z")
    public static boolean isInitialized(VisageObject instance, int offset) {
        return instance != null && (
                   offset == -1 ? instance.isInitialized$internal$() // this pointer uses -1
                   : instance.varTestBits$(offset, VisageObject.VFLGS$IS_BOUND, VisageObject.VFLGS$IS_BOUND) ||
                     instance.varTestBits$(offset, VisageObject.VFLGS$INIT$MASK, VisageObject.VFLGS$INIT$INITIALIZED_DEFAULT)
               );
    }

    /**
     * Test if an instance variable is bound.
     *
     * @param instance instance to be tested
     * @param offset offset of variable to be tested
     * @return true if the variable is bound
     */
    @org.visage.runtime.annotation.VisageSignature("(Ljava/lang/Object;)Z")
    public static boolean isReadOnly(VisageObject instance, int offset) {
        return offset == -1 ? true // this pointer uses -1 (and is read only)
               : instance.varTestBits$(offset,
                 VisageObject.VFLGS$IS_READONLY,
                 VisageObject.VFLGS$IS_READONLY);
    }
}
