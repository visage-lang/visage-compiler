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


package com.sun.javafx.api.ui;
import javax.swing.*;
import java.awt.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

public class XToggleButton extends JToggleButton {

    String mText;
    Color mGradientStart;
    Color mGradientEnd;
    
    public void setGradientStart(Color c) {
        mGradientStart = c;
    }
    
    public void setGradientEnd(Color c) {
        mGradientEnd  = c;
    }

    public XToggleButton() {
        super();
    }

    public void paint(Graphics g) {
        if (mGradientStart != null && mGradientEnd != null) {
            UIContextImpl.paintVerticalGradient(this, g, mGradientStart,
                                          mGradientEnd);
        }
        Graphics2D g2 = (Graphics2D) g;
        Composite comp = null;
        if (!isEnabled()) {
            if (getClientProperty("html") != null) {
                comp = g2.getComposite();
                AlphaComposite composite = 
                    AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
                g2.setComposite(composite);
            } 
        }
        super.paint(g);
        if (comp != null) {
            g2.setComposite(comp);
        }
    }
}