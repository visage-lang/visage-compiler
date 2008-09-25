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

package javafx.animation;

import java.lang.Object;

/**
 * Defines a key value to be interpolated for a particular interval along
 * the animation.
 * <p>
 * By default, {@code Interpolator.LINEAR} is used in the interval.
 *
 * @profile common
 * @see Interpolator
 * @see KeyValueTarget
 */
public class KeyValue {
    
    /**
     * Target variable holds the key value.
     *
     * @profile common
     */      
    public var target: KeyValueTarget;
    
    /**
     * Target value
     *
     * @profile common
     */      
    public var value: Object;
    
    /**
     * {@code Interpolator} to be used for calculating the key value
     * along the particular interval. By default, {@code Interpolator.LINEAR}
     * is used.
     *
     * @profile common
     */      
    public var interpolate: Interpolator = Interpolator.LINEAR;
}
