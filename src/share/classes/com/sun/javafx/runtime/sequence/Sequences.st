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

package com.sun.javafx.runtime.sequence;

import java.util.*;

import com.sun.javafx.runtime.NumericTypeInfo;
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.Util;
import com.sun.javafx.runtime.FXObject;

/**
 * Sequences -- static helper methods for constructing derived sequences. Implements heuristics for reducing time and
 * space overhead, such as flattening complicated sequence trees where appropriate and ignoring null transformations
 * (such as appending an empty sequence). These methods are generally preferable to the constructors for
 * CompositeSequence, FilterSequence, SubSequence, etc, because they implement heuristics for sensible time-space
 * tradeoffs.
 *
 * @author Brian Goetz
 */
public final class Sequences extends SequencesBase {

    // Inhibit instantiation
    private Sequences() { }

$TEMPLATE_FROM$

$PREFIX,BOXED,PRIM:{T,Tboxed,Tprim|
    public static $Tprim$ getAs$T$FromNewElements(FXObject instance, int varNum, int loIndex, int inserted, int k) {
        if (k >= inserted)
            k = -1;
        else if (k >= 0)
            k += loIndex;
        return instance.getAs$T$\$(varNum, k);
    }

     public static Sequence<? extends $Tboxed$> insert(Sequence<? extends $Tboxed$> oldValue, $Tprim$ newValue) {
        int oldSize = oldValue.size();
        $T$ArraySequence arr;
        if (! (oldValue instanceof $T$ArraySequence)
            || (arr = ($T$ArraySequence) oldValue).isShared()) {
            arr = new $T$ArraySequence(oldValue);
            arr.incrementSharing();
        }
        arr.replace(oldSize, oldSize, newValue, true);
        return arr;
    }

   public static void insert(FXObject instance, int varNum, $Tprim$ newValue) {
        Sequence<? extends $Tboxed$> oldValue = (Sequence<? extends $Tboxed$>) instance.get\$(varNum);
        int oldSize = oldValue.size();
        Sequence<? extends $Tboxed$> arr = insert(oldValue, newValue);
        instance.invalidate\$(varNum, oldSize, oldSize, 1, FXObject.VFLGS\$IS_INVALID);
        instance.be\$(varNum, arr);
        instance.invalidate\$(varNum, oldSize, oldSize, 1,  FXObject.VFLGS\$NEEDS_TRIGGER);
    }

}$
}
