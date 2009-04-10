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
 * A $T.prim$-valued Location.  Provides $T.prim$-valued getAs$PREFIX$() and setAs$PREFIX$() methods; also provides object-valued
 * methods so a $T.Location$ can be treated as an ObjectLocation<$T.boxed$>.
 *
 * @author Brian Goetz
 */
public interface $T.Location$ extends $if(NUMERIC)$ NumericLocation $else$ Location $endif$, ObjectLocation<$T.boxed$> {
    /** Retrieve the current value of this location, recomputing if necessary */
    public $T.prim$ getAs$PREFIX$();

    /** Set the current value of this location, recomputing if necessary */
    public $T.prim$ setAs$PREFIX$($T.prim$ value);

    /** Special version of setAs$PREFIX$ for use from object literals during initialization */
    $T.prim$ setAs$PREFIX$FromLiteral($T.prim$ value);

    /** Add a change listener to this Location */
    public void addChangeListener(ChangeListener<$T.boxed$> listener);
}
