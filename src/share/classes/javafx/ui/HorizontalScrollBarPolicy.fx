/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;

/**
 * Provides enumerated constants that determine the
 * visibility of horizontal scrollbars.
 *
 * <code>AS_NEEDED</code>
 * Used to set the horizontal scroll bar policy so that
 * horizontal scrollbars are displayed only when needed.
 *
 * <code>ALWAYS</code>
 * Used to set the horizontal scroll bar policy so that
 * horizontal scrollbars are always displayed.
 *
 * <code>NEVER</code>
 * Used to set the horizontal scroll bar policy so that
 * horizontal scrollbars are never displayed.
 */
public class HorizontalScrollBarPolicy {
    public attribute id: Number;
    
    public static attribute AS_NEEDED = HorizontalScrollBarPolicy {
        id: javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    };

    public static attribute ALWAYS = HorizontalScrollBarPolicy {
        id: javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
    };

    public static attribute NEVER = HorizontalScrollBarPolicy {
        id: javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    };

}
