/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

/**
 * A boolean-valued Location.  Provides boolean-valued getAsBoolean() and setAsBoolean() methods; also provides object-valued
 * methods so a BooleanLocation can be treated as an ObjectLocation<Boolean>.
 *
 * @author Brian Goetz
 */
public interface BooleanLocation extends Location, ObjectLocation<Boolean> {
    /** Retrieve the current value of this location, recomputing if necessary */
    boolean getAsBoolean();

    /** Set the current value of this location, recomputing if necessary */
    boolean setAsBoolean(boolean value);

    /** Set this location to its default value */
    public void setDefault();

    /** Special version of setAsBoolean for use from object literals during initialization */
    boolean setAsBooleanFromLiteral(boolean value);

    /** Add a change listener to this Location */
    public void addChangeListener(BooleanChangeListener listener);
}
