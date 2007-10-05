/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;


public class ShadowedBorder extends Border  {
    
    public function getBorder():javax.swing.border.Border {
        return new JShadowedBorder();
    }
}
