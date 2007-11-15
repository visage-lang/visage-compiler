/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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