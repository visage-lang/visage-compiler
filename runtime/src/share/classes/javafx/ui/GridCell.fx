/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 * 
 * This code is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License version 2 only, as 
 * published by the Free Software Foundation.  Sun designates this 
 * particular file as subject to the "Classpath" exception as provided 
 * by Sun in the LICENSE file that accompanied this code. 
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


public class GridCell {
    protected attribute constraint: java.awt.GridBagConstraints;
    public attribute insets: Insets on replace  {
        constraint.insets = insets.awtinsets;
    };
    public attribute anchor: Anchor on replace  {
        constraint.anchor = anchor.id.intValue();
    };
    public attribute gridwidth: Number on replace  {
        constraint.gridwidth = gridwidth.intValue();
    };
    public attribute gridheight: Number on replace {
        constraint.gridheight = gridheight.intValue();
    };
    public attribute gridx: Number on replace {
        constraint.gridx = gridx.intValue();
    };
    public attribute gridy: Number on replace {
        constraint.gridy = gridy.intValue();
    };
    public attribute fill: Fill on replace  {
        constraint.fill = fill.id.intValue();
    };
    public attribute weightx: Number on replace  {
        constraint.weightx = weightx;
    };
    public attribute weighty: Number on replace  {
        constraint.weighty = weighty;
    };
    public attribute ipadx: Number on replace  {
        constraint.ipadx = ipadx.intValue();
    };
    public attribute ipady: Number on replace  {
        constraint.ipady = ipady.intValue();
    };
    public attribute content: Widget;
    init {
        anchor = Anchor.CENTER;
        fill = Fill.NONE;
        gridx = java.awt.GridBagConstraints.RELATIVE;
        gridy = java.awt.GridBagConstraints.RELATIVE;
        gridwidth = 1;
        gridheight = 1;
        weightx = 0.0;
        weighty = 0.0;
        ipadx = 0;
        ipady = 0;
        constraint = new java.awt.GridBagConstraints();
    }
}

