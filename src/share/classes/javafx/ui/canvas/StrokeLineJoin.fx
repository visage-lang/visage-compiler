/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javafx.ui.canvas;
import java.awt.BasicStroke;

class StrokeLineJoin {
    attribute id: Number;
    attribute name: String;
    
    public static attribute BEVEL = StrokeLineJoin { 
            id: BasicStroke.JOIN_BEVEL, 
            name: "JOIN_BEVEL" 
    };
    public static attribute MITER = StrokeLineJoin {
        id: BasicStroke.JOIN_MITER, 
        name: "JOIN_MITER" 
    };
    public static attribute ROUND = StrokeLineJoin {
        id: BasicStroke.JOIN_ROUND, 
        name: "JOIN_ROUND" 
    };
}



