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


public class GridCell {
    protected attribute constraint: java.awt.GridBagConstraints = new java.awt.GridBagConstraints();
    public attribute insets: Insets = Insets{} on replace  {
        constraint.insets = insets.awtinsets;
    };
    public attribute anchor: Anchor = Anchor.CENTER on replace  {
        constraint.anchor = anchor.id.intValue();
    };
    public attribute gridwidth: Number = 1 on replace  {
        constraint.gridwidth = gridwidth.intValue();
    };
    public attribute gridheight: Number = 1 on replace {
        constraint.gridheight = gridheight.intValue();
    };
    public attribute gridx: Number = java.awt.GridBagConstraints.RELATIVE on replace {
        constraint.gridx = gridx.intValue();
    };
    public attribute gridy: Number = java.awt.GridBagConstraints.RELATIVE on replace {
        constraint.gridy = gridy.intValue();
    };
    public attribute fill: Fill = Fill.NONE on replace  {
        constraint.fill = fill.id.intValue();
    };
    public attribute weightx: Number = 0.0 on replace  {
        constraint.weightx = weightx;
    };
    public attribute weighty: Number = 0.0 on replace  {
        constraint.weighty = weighty;
    };
    public attribute ipadx: Number = 0 on replace  {
        constraint.ipadx = ipadx.intValue();
    };
    public attribute ipady: Number = 0 on replace  {
        constraint.ipady = ipady.intValue();
    };
    public attribute content: Widget;
    
}

