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

package javafx.lang;

import com.sun.javafx.runtime.FXBase;

/**
 * These functions are automaticlly imported for
 * all JavaFX Scripts to use
 *
 * @author Brian Goetz
 * @author Saul Wold
 * @profile common
 */
public class Builtins {
    /**
     * Compare 2 JavaFX Script Objects
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
     * 
     * @param args The variable to be tested (args[0] is the FXObject instance,
     * args[1] is the var offset).
     * @return true if the object has been initialized already
     */
    public static boolean isInitialized(Object... args) {
        //should return false until flag is correctly set - otherwise
        //this will always return true - which causes some reg failures
        //the code below should be uncommented when the flag is set properly
        return false;
        /*
        FXBase inst = (FXBase)args[0];
        int offset = (Integer)args[1];
        return inst.varTestBits$(offset,
                FXBase.VFLGS$IS_INITIALIZED,
                FXBase.VFLGS$IS_INITIALIZED);
         */
    }
}
