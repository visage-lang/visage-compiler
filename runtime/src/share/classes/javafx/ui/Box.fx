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


public class Box extends Widget {
    private attribute box:javax.swing.JPanel;

    public attribute orientation: Orientation on replace {
        if (orientation == Orientation.HORIZONTAL) {
            box.setLayout(new javax.swing.BoxLayout(box, javax.swing.BoxLayout.X_AXIS));
        } else if (orientation == Orientation.LINE) {
            box.setLayout(new javax.swing.BoxLayout(box, javax.swing.BoxLayout.LINE_AXIS));
        } else if (orientation == Orientation.PAGE) {
            box.setLayout(new javax.swing.BoxLayout(box, javax.swing.BoxLayout.PAGE_AXIS));
        } else {
            box.setLayout(new javax.swing.BoxLayout(box, javax.swing.BoxLayout.Y_AXIS));
        }
    };
    public attribute content: Widget[] on replace oldValue[lo..hi]=newVals {
        for(k in [lo..hi]) { box.remove(lo) };
        var ndx = lo;
        for(n in newVals) box.add(n.getComponent(), ndx++);
        box.validate();
        box.repaint();
    };
    public function createComponent():javax.swing.JComponent {
        box = UIElement.context.createPanel();
        box.setOpaque(false);
        if (orientation == Orientation.HORIZONTAL) {
            box.setLayout(new javax.swing.BoxLayout(box, javax.swing.BoxLayout.X_AXIS));
        } else if (orientation == Orientation.LINE) {
            box.setLayout(new javax.swing.BoxLayout(box, javax.swing.BoxLayout.LINE_AXIS));
        } else if (orientation == Orientation.PAGE) {
            box.setLayout(new javax.swing.BoxLayout(box, javax.swing.BoxLayout.PAGE_AXIS));
        } else {
            box.setLayout(new javax.swing.BoxLayout(box, javax.swing.BoxLayout.Y_AXIS));
        }
        for (i in content) {
            box.add(i.getComponent());
        }
        return box;
    }    
}





