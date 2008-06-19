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

package javafx.gui.swing;

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Graphics;
import com.sun.javafx.gui.BackgroundSupport;

// PENDING(shannonh) - this is public only to work around a compiler bug.
// Remove public modifier when resolved.
// http://openjfx.java.sun.com/jira/browse/JFXC-1050
public class JTextFieldImpl extends JTextField implements BackgroundSupport.BackgroundSupportable {

    private boolean borderless;

    private BackgroundSupport bgs;

    public JTextFieldImpl() {
        borderless = false;
        bgs = new BackgroundSupport(this);
    }

    public void setBorderless(boolean borderless) {
        this.borderless = borderless;
        if (borderless) {
            setBorder(new EmptyBorder(0, 0, 0, 0));
        } else {
            setBorder(new JTextField().getBorder());
        }
    }

    public boolean isBorderless() {
        return borderless;
    }

    public void setBackgroundPaint(java.awt.Paint backgroundPaint) {
        bgs.setBackgroundPaint(backgroundPaint);
    }

    public java.awt.Paint getBackgroundPaint() {
        return bgs.getBackgroundPaint();
    }

    public void setColumns(int columns) {
        int oldValue = getColumns();
        super.setColumns(columns);
        firePropertyChange("columns", oldValue, getColumns());
    }

    public void paintComponent(Graphics g) {
        bgs.paintComponent(g);
        super.paintComponent(g);
    }

    public void fireBackgroundPaintChange(java.awt.Paint oldValue, java.awt.Paint newValue) {
        firePropertyChange(BackgroundSupport.BACKGROUND_PAINT_PROPERTY, oldValue, newValue);
    }

}
