/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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
