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

package com.sun.javafx.runtime.location;
import com.sun.javafx.runtime.*;

/*
 * WARNING: Automatically generated from the template $TEMPLATE_NAME$; do not edit!
 */

/**
 * $T.ChangeListener$
 *
 * @author Brian Goetz
 */
public abstract class $T.ChangeListener$ extends AbstractChangeListener<$T.boxed$> $if(NUMERIC)$ implements NumericChangeListener $endif$ {

$if(NUMERIC)$
    public ObjectChangeListener<$T.boxed$> asObjectListener() {
        return asObjectListener(TypeInfo.$T.boxed$);
    }

    public<T extends Number> ObjectChangeListener<T> asObjectListener(final NumericTypeInfo<T, ?> typeInfo) {
        return new ObjectChangeListener<T>() {
            public void onChange(T oldValue, T newValue) {
                $T.ChangeListener$.this.onChange(typeInfo.$T.prim$Value(oldValue), typeInfo.$T.prim$Value(newValue));
            }
        };
    }
$else$
    public ObjectChangeListener<$T.boxed$> asObjectListener() {
        return new ObjectChangeListener<$T.boxed$>() {
            public void onChange($T.boxed$ oldValue, $T.boxed$ newValue) {
                $T.ChangeListener$.this.onChange(oldValue, newValue);
            }
        };
    }
$endif$

    /** Notifies the listener that the contents of the location may have changed. */
    public abstract void onChange($T.prim$ oldValue, $T.prim$ newValue);

    public int getDependencyKind() {
        return AbstractLocation.CHILD_KIND_TRIGGER;
    }
}
