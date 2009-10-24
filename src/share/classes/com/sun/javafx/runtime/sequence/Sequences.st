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
import com.sun.javafx.runtime.FXBase;

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

$PREFIX:{T|
    public static $PRIM_MAP.(T)$ getAs$T$FromOldValue(ArraySequence<$BOXED_MAP.(T)$> buffer, Sequence<? extends $BOXED_MAP.(T)$> oldValue, int startPos, int endPos, int k) {
        if (oldValue != null)
            return oldValue.get(k);
        if (k >= 0) {
            if (k >= startPos)
                k += buffer.gapEnd - endPos;
            int alen = buffer.getRawArrayLength();
            if (k < alen) {
                return buffer.getRawArrayElementAsObject(k);
            }
        }
        return buffer.getDefaultValue();
    }

    public static $PRIM_MAP.(T)$ getAs$T$FromNewElements(Sequence<? extends $BOXED_MAP.(T)$> newValue, int loIndex, int inserted, int k) {
        if (k < 0 || k >= inserted)
            return newValue.getDefaultValue();
        return newValue.getAs$T$(loIndex+k);
    }
}$
}
