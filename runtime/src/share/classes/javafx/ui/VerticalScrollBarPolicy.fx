/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;

/**
 * Provides enumerated constants that determine the
 * visibility of vertical scrollbars.
 *
 * <code>AS_NEEDED</code>
 * Used to set the vertical scroll bar policy so that
 * vertical scrollbars are displayed only when needed.
 *
 * <code>ALWAYS</code>
 * Used to set the vertical scroll bar policy so that
 * vertical scrollbars are always displayed.
 *
 * <code>NEVER</code>
 * Used to set the horizontal scroll bar policy so that
 * vertical scrollbars are never displayed.
 */
public class VerticalScrollBarPolicy {
    public attribute id: Number;
    
    public static attribute AS_NEEDED = VerticalScrollBarPolicy {
        id: javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    };

    public static attribute ALWAYS = VerticalScrollBarPolicy {
        id: javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
    };

    public static attribute NEVER = VerticalScrollBarPolicy {
        id: javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER
    };

}
