/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.util;
import com.sun.javafx.runtime.sequence.BoundForHelper;

public abstract class BoundForPartSingleton /* <T> */ extends BoundForHelper.ForPart {
    //public var element : Object /* T */; // defined in sub-class
    //public var index : Integer;          // defined in sub-class
    public var result : Object /* <TT> */;

    /*override public function adjustIndex(delta :Integer) :Void {
        index += delta;
    }*/

    override public function get(index:Integer):Object {
        if (index == 0) result else null
    }

    override public function size() :Integer {
        if (result == null) 0 else 1
    }

    override public function getResult() : Object[] {
        result
    }
};
