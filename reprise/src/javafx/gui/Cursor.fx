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
package javafx.gui;

import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

// PENDING_DOC_REVIEW
/**
 * A class to encapsulate the bitmap representation of the mouse cursor.
 *
 * @profile common conditional cursor
 */      
public class Cursor {

    private attribute awtType:Integer = java.awt.Cursor.DEFAULT_CURSOR;

    private attribute awtCursor:java.awt.Cursor = null;

    function getAWTCursor():java.awt.Cursor {
        if (awtCursor == null) {
            awtCursor = java.awt.Cursor.getPredefinedCursor(awtType);
        }
        awtCursor;
    }

    private function createBlankAWTCursor():java.awt.Cursor {
        var toolkit:Toolkit = Toolkit.getDefaultToolkit();
        var d:Dimension = toolkit.getBestCursorSize(1, 1);
        if (d.width <> 0 and d.height <> 0) {
            var image:java.awt.Image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
            return toolkit.createCustomCursor(image, new Point(0, 0), "NONE");
        }
        else {
            return null;
        }
    }

    /**
     * The default cursor type (gets set if no cursor is defined).
     *
     * @profile common conditional cursor
     */      
    public static attribute DEFAULT = Cursor{}   
    
    /**
     * The crosshair cursor type.
     *
     * @profile common conditional cursor
     */       
    public static attribute CROSSHAIR = Cursor{ awtType:java.awt.Cursor.CROSSHAIR_CURSOR };
    
    /**
     * The text cursor type.
     *
     * @profile common conditional cursor
     */       
    public static attribute TEXT = Cursor{ awtType:java.awt.Cursor.TEXT_CURSOR };
    
    /**
     * The wait cursor type.
     *
     * @profile common conditional cursor
     */       
    public static attribute WAIT = Cursor{ awtType:java.awt.Cursor.WAIT_CURSOR };
    
    /**
     * The south-west-resize cursor type. 
     *
     * @profile common conditional cursor
     */       
    public static attribute SW_RESIZE = Cursor{ awtType:java.awt.Cursor.SW_RESIZE_CURSOR };
    
    /**
     * The south-east-resize cursor type.
     *
     * @profile common conditional cursor
     */       
    public static attribute SE_RESIZE = Cursor{ awtType:java.awt.Cursor.SE_RESIZE_CURSOR };
    
    /**
     * The north-west-resize cursor type.
     *
     * @profile common conditional cursor
     */       
    public static attribute NW_RESIZE = Cursor{ awtType:java.awt.Cursor.NW_RESIZE_CURSOR };
    
    /**
     * The north-east-resize cursor type.
     *
     * @profile common conditional cursor
     */       
    public static attribute NE_RESIZE = Cursor{ awtType:java.awt.Cursor.NE_RESIZE_CURSOR };
    
    /**
     * The north-resize cursor type.
     *
     * @profile common conditional cursor
     */       
    public static attribute N_RESIZE = Cursor{ awtType:java.awt.Cursor.N_RESIZE_CURSOR };
    
    /**
     * The south-resize cursor type.
     *
     * @profile common conditional cursor
     */       
    public static attribute S_RESIZE = Cursor{ awtType:java.awt.Cursor.S_RESIZE_CURSOR };
    
    /**
     * The west-resize cursor type.
     *
     * @profile common conditional cursor
     */       
    public static attribute W_RESIZE = Cursor{ awtType:java.awt.Cursor.W_RESIZE_CURSOR };
    
    /**
     * The east-resize cursor type.
     *
     * @profile common conditional cursor
     */       
    public static attribute E_RESIZE = Cursor{ awtType:java.awt.Cursor.E_RESIZE_CURSOR };
    
    /**
     * The hand cursor type.
     *
     * @profile common conditional cursor
     */       
    public static attribute HAND = Cursor{ awtType:java.awt.Cursor.HAND_CURSOR };
    
    /**
     * The move cursor type.
     *
     * @profile common conditional cursor
     */       
    public static attribute MOVE = Cursor{ awtType:java.awt.Cursor.MOVE_CURSOR };
    
    /**
     * The horizontal cursor type.
     *
     * @profile common conditional cursor
     */       
    public static attribute H_RESIZE = W_RESIZE;
    
    /**
     * The vertical cursor type.
     *
     * @profile common conditional cursor
     */       
    public static attribute V_RESIZE = N_RESIZE;
    
    /**
     * The none cursor type.
     *
     * @profile common conditional cursor
     */       
    public static attribute NONE = Cursor { 
          awtType: java.awt.Cursor.CUSTOM_CURSOR 
        awtCursor: {
            var toolkit:Toolkit = Toolkit.getDefaultToolkit();
            var d:Dimension = toolkit.getBestCursorSize(1, 1);
            if (d.width <> 0 and d.height <> 0) {
                var image:java.awt.Image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
                toolkit.createCustomCursor(image, new Point(0, 0), "NONE");
            }
            else {
                null;
            }
        }
    };

    private static attribute cursors:Cursor[] = [
        DEFAULT,
        CROSSHAIR,
        TEXT,
        WAIT,
        SW_RESIZE,
        SE_RESIZE,
        NW_RESIZE,
        NE_RESIZE,
        N_RESIZE,
        S_RESIZE,
        W_RESIZE,
        E_RESIZE,
        HAND,
        MOVE];

    function fromAWTCursor(c:java.awt.Cursor):Cursor {
        var i:Integer = c.getType();
        if (i >= 0 and i < sizeof cursors) {
            cursors[i]
        }
        else {
            Cursor { awtType:i awtCursor:c }
        }
    }

}
