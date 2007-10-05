/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;


public class InternalFrameDragMode {
    public attribute id: Number;
    
    public static attribute LIVE = InternalFrameDragMode {
        id: javax.swing.JDesktopPane.LIVE_DRAG_MODE
    };

    public static attribute OUTLINE = InternalFrameDragMode {
        id: javax.swing.JDesktopPane.OUTLINE_DRAG_MODE
    };
               
}
