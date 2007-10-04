/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
