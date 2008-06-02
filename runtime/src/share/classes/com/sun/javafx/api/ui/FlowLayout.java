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

package com.sun.javafx.api.ui;
import java.awt.*;
import javax.swing.*;

public class FlowLayout extends java.awt.FlowLayout {
    public FlowLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        //System.out.println("target.parent = " +target.getParent());
        if (target.getParent() instanceof JViewport) {
            JViewport vp = (JViewport)target.getParent();
            int width = vp.getWidth();
            int x = 0;
            int y = 0;
            int nmembers = target.getComponentCount();
            int maxH = 0;
            int hgap = 0;
            int vgap = getVgap();
            for (int i = 0 ; i < nmembers ; i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    Dimension dim = m.getPreferredSize();
                    if (x + dim.width > width) {
                        y += vgap;
                        y += maxH;
                        maxH = dim.height;
                        x = dim.width;
                        hgap = 0;
                    } else {
                        x += hgap;
                        x += dim.width;
                        maxH = Math.max(maxH, dim.height);
                        hgap = getHgap();
                    }
                }
            }
            Dimension result = new Dimension(width, y + maxH);
            //System.out.println("result = "+result);
            return result;
        } else {
            return super.preferredLayoutSize(target);
        }
    }
}
