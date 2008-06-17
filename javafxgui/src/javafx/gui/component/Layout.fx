/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.gui.component;

import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * A place for constants related to layout.
 */
public class Layout {

    public static /* constant */ attribute DEFAULT_SIZE: Integer = GroupLayout.DEFAULT_SIZE;

    public static /* constant */ attribute PREFERRED_SIZE: Integer = GroupLayout.PREFERRED_SIZE;

    public static /* constant */ attribute UNLIMITED_SIZE: Integer = java.lang.Short.MAX_VALUE;

    public static /* constant */ attribute BASELINE = Alignment.BASELINE;
    
    public static /* constant */ attribute LEADING = Alignment.LEADING;

    public static /* constant */ attribute TRAILING = Alignment.TRAILING;

    public static /* constant */ attribute CENTER = Alignment.CENTER;

    public static /* constant */ attribute RELATED = Relationship.RELATED;

    public static /* constant */ attribute UNRELATED = Relationship.UNRELATED;

    // public static /* constant */ attribute INDENT = Relationship.INDENT;
    
}

public class Alignment {
    
    private /* set-once */ attribute toolkitValue = GroupLayout.Alignment.BASELINE;
    
    public static /* constant */ attribute BASELINE = Alignment{};

    public static /* constant */ attribute LEADING =
        Alignment{toolkitValue: GroupLayout.Alignment.LEADING}

    public static /* constant */ attribute TRAILING =
        Alignment{toolkitValue: GroupLayout.Alignment.TRAILING}

    public static /* constant */ attribute CENTER =
        Alignment{toolkitValue: GroupLayout.Alignment.CENTER}

    function getToolkitValue() {
        toolkitValue;
    }

}

public class Relationship {

    private /* set-once */ attribute toolkitValue = ComponentPlacement.RELATED;
    
    public static /* constant */ attribute RELATED = Relationship{}; 

    public static /* constant */ attribute UNRELATED =
        Relationship{toolkitValue: ComponentPlacement.UNRELATED};

    // public static /* constant */ attribute INDENT = Relationship{toolkitValue: ComponentPlacement.INDENT};

    function getToolkitValue() {
        toolkitValue;
    }

}
