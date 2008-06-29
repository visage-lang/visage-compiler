/*
 * Copyright 2005-2006 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.api.tree;

import java.util.List;

/**
 * A tree node for a JavaFX interpolate description, such as:
 * <ul><li>
 * "<code>x =&gt; 500 tween LINEAR</code>", where x is the value to be changed
 * via interpolation, and the expression after the <b>=&gt;</b>; or</li>
 * <li><code>rect =&gt; {height: 400 tween EASEBOTH, width: 500 tween LINEAR};</code>,
 * where rect is object to change, with interpolate values for its height and
 * width attributes.</li></ul>
 * 
 * @author tball
 */
public interface InterpolateTree extends ExpressionTree {
    
    /**
     * Returns the variable to be interpolated.
     * @return the variable expression
     */
    ExpressionTree getVariable();
    
    /**
     * Returns the list of interpolation values for this variable.
     * @return the list of interpolation values
     */
    List<InterpolateValueTree> getInterpolateValues();
}
