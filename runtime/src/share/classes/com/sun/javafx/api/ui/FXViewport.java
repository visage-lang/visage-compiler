/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 * 
 * This code is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License version 2 only, as 
 * published by the Free Software Foundation.  Sun designates this 
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
import javax.swing.*;
import java.awt.*;

public class FXViewport extends JViewport {
    // Trim the repaint bounds reported by the view to the  size
    // of this viewport
    @Override
    public void repaint(long tm, int x, int y, int w, int h) {
        Rectangle repaintBounds = new Rectangle(x, y, w, h);
        Rectangle bounds = new Rectangle(0, 0, getWidth(), getHeight());
        //System.out.println("repaintBounds = "+repaintBounds);
        bounds = bounds.intersection(repaintBounds);
        //System.out.println("bounds="+bounds);
        super.repaint(tm, bounds.x, bounds.y, bounds.width, bounds.height);
    }
}


