/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javafx.ui.canvas;
import java.awt.BasicStroke;

class StrokeLineCap {
    attribute id: Number;
    attribute name: String;

    public static attribute BUTT = 
	StrokeLineCap { id: BasicStroke.CAP_BUTT, name: "CAP_BUTT" };
    public static attribute ROUND =
	StrokeLineCap { id: BasicStroke.CAP_ROUND, name: "CAP_ROUND" };
    public static attribute SQUARE =
	StrokeLineCap { id: BasicStroke.CAP_SQUARE, name: "CAP_SQUARE" };    
}


