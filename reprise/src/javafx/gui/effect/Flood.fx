/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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
package javafx.gui.effect;

import javafx.gui.Color;
import javafx.gui.Paint;

/**
 * An effect that renders a rectangular region that is filled ("flooded")
 * with the given {@code Paint}.  This is equivalent to rendering a
 * filled rectangle into an image and using an {@code Identity} effect,
 * except that it is more convenient and potentially much more efficient.
 */
public class Flood extends Effect {
    private attribute flood = new com.sun.scenario.effect.Flood(java.awt.Color.RED);

    public function getImpl():com.sun.scenario.effect.Effect {
        flood
    };

    /**
     * The {@code Paint} used to flood the region.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: Color.RED
     *  Identity: n/a
     * </pre>
     */
    public attribute paint: Paint = Color.RED
        on replace { flood.setPaint(paint.getAWTPaint()); }
}
