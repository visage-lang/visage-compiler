/*
 * Copyright 1999-2005 Sun Microsystems, Inc.  All Rights Reserved.
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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Scrollable;

/**
 *
 * @author jclarke
 */
public class UIContextImpl implements UIContext {
    
    public static class XPanel extends JPanel implements Scrollable {
        public XPanel() {
            setFocusable(false);
        }

        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 10;
        }
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 100;
        }
        public boolean getScrollableTracksViewportWidth() {
            if (getParent() instanceof JViewport) {
                return (((JViewport)getParent()).getWidth() > getPreferredSize().width);
            }
            return false;
        }
        public boolean getScrollableTracksViewportHeight() {
            if (getParent() instanceof JViewport) {
                return (((JViewport)getParent()).getHeight() > getPreferredSize().height);
            }
            return false;
        }
    }

    public JPanel createPanel() {
        return new XPanel();
    }
    

    public XButton createButton() {
        return new XButton();
    }

    protected Map<String, Font> mFontMap = Collections.synchronizedMap(new HashMap<String, Font>());

    public Font getFont(String url, int style, int size) {
        Font f = mFontMap.get(url);
        if (f == null) {
            try {
                f = Font.createFont(Font.TRUETYPE_FONT,
                                    new URL(url).openStream());
                mFontMap.put(url, f);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return f.deriveFont(style, size);
    }

    public boolean isBitSet(int a, int b) {
        return (a & b) != 0;
    }

    public int setBit(int a, int b) {
        return a | b;
    }

    public int clearBit(int a, int b) {
        return a & ~b;
    }


}
