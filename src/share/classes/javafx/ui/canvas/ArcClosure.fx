/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javafx.ui.canvas;
import java.awt.geom.Arc2D;

public class ArcClosure {
    public attribute id: Number;
    public attribute name: String;
    
    public static attribute OPEN = ArcClosure { id: Arc2D.OPEN, name: "OPEN"};
    public static attribute CHORD = ArcClosure { id: Arc2D.CHORD, name: "CHORD"};
    public static attribute PIE = ArcClosure { id: Arc2D.PIE, name: "PIE"};    
}


