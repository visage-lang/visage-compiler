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

package javafx.scene.effect.light;

import javafx.scene.paint.Color;
import java.lang.Object;

/**
 * The abstract base class for all light implementations.
 */
public abstract class Light {

    /**
     * @treatasprivate implementation detail
     */
    public abstract function impl_getImpl():com.sun.scenario.effect.light.Light;

    /**
     * The color of the light source.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: Color.WHITE
     *  Identity: n/a
     * </pre>
     */
    public attribute color: Color = Color.WHITE
        on replace { impl_getImpl().setColor(color.getAWTColor()); }
}
