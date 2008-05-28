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


public class TitledBorderPosition {
    public attribute id: Number;
    public attribute name: String;
    
    public static attribute DEFAULT = TitledBorderPosition {
        id: javax.swing.border.TitledBorder.DEFAULT_POSITION
        name: "DEFAULT"
    };

    public static attribute ABOVE_TOP = TitledBorderPosition {
        id: javax.swing.border.TitledBorder.ABOVE_TOP
        name: "ABOVE_TOP"
    };

    public static attribute TOP = TitledBorderPosition {
        id: javax.swing.border.TitledBorder.TOP
        name: "TOP"
    };

    public static attribute BELOW_TOP = TitledBorderPosition {
        id: javax.swing.border.TitledBorder.BELOW_TOP
        name: "BELOW_TOP"
    };

    public static attribute ABOVE_BOTTOM = TitledBorderPosition {
        id: javax.swing.border.TitledBorder.ABOVE_BOTTOM
        name: "ABOVE_BOTTOM"
    };

    public static attribute BOTTOM = TitledBorderPosition {
        id: javax.swing.border.TitledBorder.BOTTOM
        name: "BOTTOM"
    };

    public static attribute BELOW_BOTTOM = TitledBorderPosition {
        id: javax.swing.border.TitledBorder.BELOW_BOTTOM
        name: "BELOW_BOTTOM"
    };    
}
