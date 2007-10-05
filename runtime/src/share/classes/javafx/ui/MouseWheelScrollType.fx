/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;

import java.awt.event.MouseWheelEvent;

public class MouseWheelScrollType {
    public attribute id: Number;
    public attribute description: String;
    
    public static attribute UNIT_SCROLL = MouseWheelScrollType {
            description: "UNIT SCROLL"
            id: MouseWheelEvent.WHEEL_UNIT_SCROLL
    };

    public static attribute BLOCK_SCROLL = MouseWheelScrollType {
            description: "BLOCK SCROLL"
            id: MouseWheelEvent.WHEEL_BLOCK_SCROLL
    };
}
