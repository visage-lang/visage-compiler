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

package com.sun.javafx.scene;

import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javafx.scene.HorizontalAlignment;
import javafx.scene.VerticalAlignment;

// PENDING(shannonh) - don't want these public

public function HA_To_SwingConstant(value: HorizontalAlignment): Integer {
    if (value == HorizontalAlignment.LEFT) SwingConstants.LEFT
    else if (value == HorizontalAlignment.RIGHT) SwingConstants.RIGHT
    else if (value == HorizontalAlignment.LEADING) SwingConstants.LEADING
    else if (value == HorizontalAlignment.TRAILING) SwingConstants.TRAILING
    else SwingConstants.CENTER;
}

public function SwingConstant_To_HA(value: Integer): HorizontalAlignment {
    if (value == SwingConstants.LEFT) HorizontalAlignment.LEFT
    else if (value == SwingConstants.RIGHT) HorizontalAlignment.RIGHT
    else if (value == SwingConstants.LEADING) HorizontalAlignment.LEADING
    else if (value == SwingConstants.TRAILING) HorizontalAlignment.TRAILING
    else HorizontalAlignment.CENTER;
}

public function HA_To_FLConstant(value: HorizontalAlignment): Integer {
    if (value == HorizontalAlignment.LEFT) FlowLayout.LEFT
    else if (value == HorizontalAlignment.RIGHT) FlowLayout.RIGHT
    else if (value == HorizontalAlignment.LEADING) FlowLayout.LEADING
    else if (value == HorizontalAlignment.TRAILING) FlowLayout.TRAILING
    else FlowLayout.CENTER;
}

public function FLConstant_To_HA(value: Integer): HorizontalAlignment {
    if (value == FlowLayout.LEFT) HorizontalAlignment.LEFT
    else if (value == FlowLayout.RIGHT) HorizontalAlignment.RIGHT
    else if (value == FlowLayout.LEADING) HorizontalAlignment.LEADING
    else if (value == FlowLayout.TRAILING) HorizontalAlignment.TRAILING
    else HorizontalAlignment.CENTER;
}

public function VA_To_SwingConstant(value: VerticalAlignment): Integer {
    if (value == VerticalAlignment.TOP) SwingConstants.TOP
    else if (value == VerticalAlignment.BOTTOM) SwingConstants.BOTTOM
    else SwingConstants.CENTER;
}

public function SwingConstant_To_VA(value: Integer): VerticalAlignment {
    if (value == SwingConstants.TOP) VerticalAlignment.TOP
    else if (value == SwingConstants.BOTTOM) VerticalAlignment.BOTTOM
    else VerticalAlignment.CENTER;
}
