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

package javafx.ui;


public class Cursor {
    protected attribute awtCursor: java.awt.Cursor;
    public function getCursor(): java.awt.Cursor {
        if (awtCursor == null) then {
            awtCursor = createCursor();
        };
        return awtCursor;
    }
    protected function createCursor(): java.awt.Cursor{
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
    }
    public static attribute DEFAULT = Cursor { 
        awtCursor: java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR) };

    public static attribute CROSSHAIR = Cursor { 
        awtCursor: java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.CROSSHAIR_CURSOR)};
    public static attribute TEXT = Cursor { 
        awtCursor: java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR)};

    public static attribute WAIT = Cursor { 
        awtCursor: java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR)};

    public static attribute SW_RESIZE = Cursor { 
        awtCursor: java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SW_RESIZE_CURSOR)};
    public static attribute SE_RESIZE = Cursor { 
        awtCursor: java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SE_RESIZE_CURSOR)};
    public static attribute NW_RESIZE = Cursor { 
        awtCursor: java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NW_RESIZE_CURSOR)};
    public static attribute NE_RESIZE = Cursor { 
        awtCursor: java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NE_RESIZE_CURSOR)};
    public static attribute N_RESIZE = Cursor { 
        awtCursor: java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.N_RESIZE_CURSOR)};

    public static attribute S_RESIZE = Cursor { 
        awtCursor: java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.S_RESIZE_CURSOR)};

    public static attribute W_RESIZE = Cursor { 
        awtCursor: java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.W_RESIZE_CURSOR)};

    public static attribute E_RESIZE = Cursor { 
        awtCursor: java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.E_RESIZE_CURSOR)};

    public static attribute HAND = Cursor { 
        awtCursor: java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR)};

    public static attribute MOVE = Cursor { 
        awtCursor: java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.MOVE_CURSOR)};

    public static attribute H_RESIZE = W_RESIZE;

    public static attribute V_RESIZE = N_RESIZE;

}
