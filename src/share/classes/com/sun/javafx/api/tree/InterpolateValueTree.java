/*
 * Copyright 2005-2006 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

package com.sun.javafx.api.tree;

import com.sun.source.tree.ExpressionTree;

/**
 * A tree node for a JavaFX interpolate value description, such as 
 * "<code>500 tween LINEAR</code>", where the interpolate type is LINEAR
 * while 500 is the value to interpolate over.
 * @author tball
 */
public interface InterpolateValueTree extends JavaFXTree {
    
    /**
     * Returns the attribute to which this value applies.
     * 
     * @return the attribute name, or null if this value is declared outside of 
     *         an interpolate block expression.
     */
    ExpressionTree getAttributeName();
    
    /**
     * The interpolation to be run, such as <code>LINEAR</code> or 
     * <code>EASEIN</code>.
     * @return the interpolation name
     */
    String getInterpolation();
    
    /**
     * Returns the value for the interpolation.
     * @return the value for the interpolation.
     */
    ExpressionTree getValue();
}
