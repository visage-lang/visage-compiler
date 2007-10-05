/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javafx.ui.canvas;
import java.awt.geom.GeneralPath;

public class FillRule {
    attribute name: String;
    attribute id: Number;

    public static attribute EVEN_ODD = FillRule {
        name: "EVEN_ODD", 
        id: GeneralPath.WIND_EVEN_ODD
    };

    public static attribute NON_ZERO = FillRule {
        name: "NON_ZERO", 
        id: GeneralPath.WIND_NON_ZERO
    };    
}


