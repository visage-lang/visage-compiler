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
import java.awt.image.*;

public class XInternalFrame extends JInternalFrame implements DesktopManager {

    float mOpacity = 1.0f;
    boolean mOpaque = true;
    JDesktopPane mDesk; 

    public XInternalFrame(JDesktopPane desk) {
        mDesk = desk;
    }

    public boolean isOpaque() {
        return mOpaque && mOpacity == 1.0;
    }

    public void setContentPane(Container c) {
        super.setContentPane(c);
        if (c instanceof JComponent) {
            ((JComponent)c).setOpaque(mOpaque);
        }
    }

    public void setOpaque(boolean value) {
        mOpaque = value;
        Component c = getContentPane();
        super.setOpaque(value);
        if (c instanceof JComponent) {
            ((JComponent)c).setOpaque(mOpaque);
        }
    }
    
    public void setOpacity(float value) {
        mOpacity = value;
        if (value == 1.0) {
            setOpaque(true);
        } else if (value == 0.0) {
            setOpaque(false);
        } else {
            setOpaque(true);
        }
        repaint();
    }

    public float getOpacity() {
        return mOpacity;
    }

    static class DummyDesktop extends JDesktopPane implements DesktopManager {
        public DesktopManager getDesktopManager() {
            return this;
        }
        public void openFrame(JInternalFrame f) {
            ((DesktopManager)f).openFrame(f);
        }

        public void closeFrame(JInternalFrame f) {
            ((DesktopManager)f).closeFrame(f);
        }

        public void maximizeFrame(JInternalFrame f) {
            ((DesktopManager)f).maximizeFrame(f);
        }

        public void minimizeFrame(JInternalFrame f) {
            ((DesktopManager)f).minimizeFrame(f);
        }

        public void iconifyFrame(JInternalFrame f) {
            ((DesktopManager)f).iconifyFrame(f);
        }

        public void deiconifyFrame(JInternalFrame f) {
            ((DesktopManager)f).deiconifyFrame(f);
        }

        public void activateFrame(JInternalFrame f) {
            ((DesktopManager)f).activateFrame(f);
        }

        public void deactivateFrame(JInternalFrame f) {
            ((DesktopManager)f).deactivateFrame(f);
        }

        public void beginDraggingFrame(JComponent f) {
            ((DesktopManager)f).beginDraggingFrame(f);
        }

        public void dragFrame(JComponent f, int newX, int newY) {
            ((DesktopManager)f).dragFrame(f, newX, newY);
        }

        public void endDraggingFrame(JComponent f) {
            ((DesktopManager)f).endDraggingFrame(f);
        }

        public void beginResizingFrame(JComponent f, int direction) {
            ((DesktopManager)f).beginResizingFrame(f, direction);
        }

        public void resizeFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {
            ((DesktopManager)f).resizeFrame(f, newX, newY, newWidth, newHeight);
        }

        public void endResizingFrame(JComponent frame) {
            ((DesktopManager)frame).endResizingFrame(frame);
            
        }

        public void setBoundsForFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {
            ((DesktopManager)f).setBoundsForFrame(f, newX, newY, newWidth, newHeight);
        }
    }


    boolean mGlassVisible;
    Cursor mCursor;
    boolean mResizing;
    
    public JDesktopPane getDesktopPane() {
        return mDesk;
    }


    public void openFrame(JInternalFrame f) {
    }
    
    public void closeFrame(JInternalFrame f) {
    }

    public void maximizeFrame(JInternalFrame f) {
    }
    
    public void minimizeFrame(JInternalFrame f) {
    }
    
    public void iconifyFrame(JInternalFrame f) {
    }
    
    public void deiconifyFrame(JInternalFrame f) {
    }
    
    public void activateFrame(JInternalFrame f) {
    }
    
    public void deactivateFrame(JInternalFrame f) {
    }
    
    public void beginDraggingFrame(JComponent f) {
    }
    
    public void dragFrame(JComponent f, int newX, int newY) {
        setBoundsForFrame(f, newX, newY, f.getWidth(), f.getHeight());
    }
    
    public void endDraggingFrame(JComponent f) {
    }
    
    public void beginResizingFrame(JComponent frame, int direction) {
        Container c = frame.getTopLevelAncestor();
        Component glassPane = null;
        if (c instanceof JFrame) {
            glassPane = ((JFrame)frame.getTopLevelAncestor()).getGlassPane();
        } else if (c instanceof JApplet) {
            glassPane = ((JApplet)c).getGlassPane();
        } else if (c instanceof JWindow) {
            glassPane = ((JApplet)c).getGlassPane();
        } else if (c instanceof JDialog) {
            glassPane = ((JDialog)c).getGlassPane();
        }
        mCursor = getCursor();
        if (glassPane != null) {
            mGlassVisible = glassPane.isVisible();
            setCursor(glassPane.getCursor());
        }
        mResizing = true;
    }

    public void setCursor(Cursor c) {
        if (mResizing) return;
        super.setCursor(c);
    }
    
    public void resizeFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {
        setBoundsForFrame(f, newX, newY, newWidth, newHeight);
    }
    
    public void endResizingFrame(JComponent frame) {
        Container c = frame.getTopLevelAncestor();
        if (c instanceof JFrame) {
            ((JFrame)frame.getTopLevelAncestor()).getGlassPane().setVisible(mGlassVisible);
        } else if (c instanceof JApplet) {
            ((JApplet)c).getGlassPane().setVisible(mGlassVisible);
        } else if (c instanceof JWindow) {
            ((JWindow)c).getGlassPane().setVisible(mGlassVisible);
        } else if (c instanceof JDialog) {
            ((JDialog)c).getGlassPane().setVisible(mGlassVisible);
        }
        mResizing = false;
        setCursor(mCursor);
    }

    public void setBoundsForFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {
        f.setBounds(newX, newY, newWidth, newHeight);
    }

    public void paint(Graphics g) {
        if (mOpacity != 1.0) {
            Graphics2D g2d = (Graphics2D)g;
            Composite oldComp = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, mOpacity));
            super.paint(g2d);
            g2d.setComposite(oldComp);
        } else {
            super.paint(g);
        }
    }

}