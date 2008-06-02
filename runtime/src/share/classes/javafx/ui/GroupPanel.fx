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
 * Container which provides Group layout. You layout its content by creating
 * row and column definitions, and then assigning the contained elements
 * <code>row</code> and <code>column</code> attributes.
 */

public class GroupPanel extends Widget, GroupLayout {

    override attribute focusable = false;

    private attribute panel:javax.swing.JPanel;
    private attribute layout:org.jdesktop.layout.GroupLayout;

    /** 
     * Determines whether to automatically create gaps between elements
     * based on platform specific UI guidelines. Defaults to true.
     */
    public attribute autoCreateGaps: Boolean = true;
    /** 
     * Determines whether to automatically create container gaps between 
     * contained elements and this object based on platform specific UI 
     * guidelines. Defaults to true.
     */
    public attribute autoCreateContainerGaps: Boolean = true;

    public function createComponent():javax.swing.JComponent {
        panel = javax.swing.JPanel{};
        panel.setOpaque(false);
        layout = new org.jdesktop.layout.GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutocreateGaps(autoCreateGaps);
        layout.setAutocreateContainerGaps(autoCreateContainerGaps);
        //this.addComponents(panel);
        var hgroup = layout.createSequentialGroup();
        var vgroup = layout.createSequentialGroup();
        this.addColumns(panel as java.awt.Container, layout, hgroup);
        this.addRows(panel as java.awt.Container, layout, vgroup);
        layout.setHorizontalGroup(hgroup);
        layout.setVerticalGroup(vgroup);
        return panel;
    }
}


