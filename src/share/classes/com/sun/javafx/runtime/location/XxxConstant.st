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

/*
 * WARNING: Automatically generated from the template $TEMPLATE_NAME$; do not edit!
 */

/**
 * $PREFIX$Constant
 *
 * @author Brian Goetz
 */
public class $T.Constant$ extends AbstractConstantLocation<$T.boxed$> implements $T.Location$ {
    private $T.prim$ \$value;

    public static $T.Location$ make($T.prim$ value) {
        return new $T.Constant$(value);
    }

    protected $T.Constant$($T.prim$ value) {
        this.\$value = value;
    }


    public $T.boxed$ get() {
        return \$value;
    }

    public $T.prim$ setAs$PREFIX$($T.prim$ value) {
        throw new UnsupportedOperationException();
    }

    public $T.prim$ setAs$PREFIX$FromLiteral($T.prim$ value) {
        throw new UnsupportedOperationException();
    }

    public void addChangeListener(PrimitiveChangeListener<$T.boxed$> listener) { }

$if(NUMERIC)$
    public byte getAsByte() { return (byte) \$value; }
    public short getAsShort() { return (short) \$value; }
    public int getAsInt() { return (int) \$value; }
    public long getAsLong() { return (long) \$value; }
    public float getAsFloat() { return (float) \$value; }
    public double getAsDouble() { return (double) \$value; }
$else$
    public $T.prim$ getAs$PREFIX$() {
        return \$value;
    }
$endif$
}
