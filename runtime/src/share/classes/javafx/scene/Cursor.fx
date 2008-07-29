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

package javafx.scene;

import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javafx.lang.FX;

/**
 * The default cursor type (gets set if no cursor is defined).
 *
 * @profile common conditional cursor
 */      
public /* const */ def DEFAULT = Cursor{}   

/**
 * The crosshair cursor type.
 *
 * @profile common conditional cursor
 */       
public /* const */ def CROSSHAIR = Cursor{ awtType:java.awt.Cursor.CROSSHAIR_CURSOR };

/**
 * The text cursor type.
 *
 * @profile common conditional cursor
 */       
public /* const */ def TEXT = Cursor{ awtType:java.awt.Cursor.TEXT_CURSOR };

/**
 * The wait cursor type.
 *
 * @profile common conditional cursor
 */       
public /* const */ def WAIT = Cursor{ awtType:java.awt.Cursor.WAIT_CURSOR };

/**
 * The south-west-resize cursor type. 
 *
 * @profile common conditional cursor
 */       
public /* const */ def SW_RESIZE = Cursor{ awtType:java.awt.Cursor.SW_RESIZE_CURSOR };

/**
 * The south-east-resize cursor type.
 *
 * @profile common conditional cursor
 */       
public /* const */ def SE_RESIZE = Cursor{ awtType:java.awt.Cursor.SE_RESIZE_CURSOR };

/**
 * The north-west-resize cursor type.
 *
 * @profile common conditional cursor
 */       
public /* const */ def NW_RESIZE = Cursor{ awtType:java.awt.Cursor.NW_RESIZE_CURSOR };

/**
 * The north-east-resize cursor type.
 *
 * @profile common conditional cursor
 */       
public /* const */ def NE_RESIZE = Cursor{ awtType:java.awt.Cursor.NE_RESIZE_CURSOR };

/**
 * The north-resize cursor type.
 *
 * @profile common conditional cursor
 */       
public /* const */ def N_RESIZE = Cursor{ awtType:java.awt.Cursor.N_RESIZE_CURSOR };

/**
 * The south-resize cursor type.
 *
 * @profile common conditional cursor
 */       
public /* const */ def S_RESIZE = Cursor{ awtType:java.awt.Cursor.S_RESIZE_CURSOR };

/**
 * The west-resize cursor type.
 *
 * @profile common conditional cursor
 */       
public /* const */ def W_RESIZE = Cursor{ awtType:java.awt.Cursor.W_RESIZE_CURSOR };

/**
 * The east-resize cursor type.
 *
 * @profile common conditional cursor
 */       
public /* const */ def E_RESIZE = Cursor{ awtType:java.awt.Cursor.E_RESIZE_CURSOR };

/**
 * The hand cursor type.
 *
 * @profile common conditional cursor
 */       
public /* const */ def HAND = Cursor{ awtType:java.awt.Cursor.HAND_CURSOR };

/**
 * The move cursor type.
 *
 * @profile common conditional cursor
 */       
public /* const */ def MOVE = Cursor{ awtType:java.awt.Cursor.MOVE_CURSOR };

/**
 * The horizontal cursor type.
 *
 * @profile common conditional cursor
 */       
public /* const */ def H_RESIZE = W_RESIZE;

/**
 * The vertical cursor type.
 *
 * @profile common conditional cursor
 */       
public /* const */ def V_RESIZE = N_RESIZE;

/**
 * The none cursor type.
 *
 * @profile common conditional cursor
 */       
public /* const */ def NONE = Cursor { 
      awtType: java.awt.Cursor.CUSTOM_CURSOR 
    awtCursor: {
        var toolkit:Toolkit = Toolkit.getDefaultToolkit();
        var d:Dimension = toolkit.getBestCursorSize(1, 1);
        if (d.width != 0 and d.height != 0) {
            var image:java.awt.Image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
            toolkit.createCustomCursor(image, new Point(0, 0), "NONE");
        }
        else {
            null;
        }
    }
};

private /* const */ def cursors:Cursor[] = [
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
    } else if (FX.isSameObject(c, NONE.awtCursor)) {
        NONE
    } else {
        Cursor { awtType:i awtCursor:c }
    }
}

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

}
