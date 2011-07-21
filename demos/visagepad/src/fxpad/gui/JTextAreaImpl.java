package fxpad.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.beans.*;

// PENDING(shannonh) - this is public only to work around a compiler bug.
// Remove public modifier when resolved.
// http://openjfx.java.sun.com/jira/browse/JFXC-1050
public class JTextAreaImpl extends JTextArea implements PropertyChangeListener {

    private java.awt.Paint backgroundPaint;

    private boolean borderless;

    private static final java.awt.Color NOCOLOR = new java.awt.Color(0, 0, 0) {
        @Override
        public String toString() {
            return "NOCOLOR";
        }

        @Override
        public boolean equals(Object other) {
            return other == this;
        }
    };

    public JTextAreaImpl() {
        backgroundPaint = getBackground();
        borderless = false;
        addPropertyChangeListener("background", this);
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
        java.awt.Paint old = this.backgroundPaint;
        this.backgroundPaint = backgroundPaint;

        if (backgroundPaint == null) {
            setBackground(null);
            setOpaque(false);
        } else if (backgroundPaint instanceof java.awt.Color && ((java.awt.Color)backgroundPaint).getAlpha() == 255) {
            setBackground((java.awt.Color)backgroundPaint);
            setOpaque(true);
        } else {
            setBackground(NOCOLOR);
            setOpaque(false);
            repaint();
        }
        firePropertyChange("backgroundPaint", old, backgroundPaint);
    }

    public java.awt.Paint getBackgroundPaint() {
        return backgroundPaint;
    }

    @Override
    public void setColumns(int columns) {
        int oldValue = getColumns();
        super.setColumns(columns);
        firePropertyChange("columns", oldValue, getColumns());
    }
    @Override
    public void setRows(int rows) {
        int oldValue = getRows();
        super.setRows(rows);
        firePropertyChange("rows", oldValue, getRows());
    }   
    @Override
    public void setLineWrap(boolean lineWrap) {
        boolean oldValue = getLineWrap();
        super.setLineWrap(lineWrap);
        firePropertyChange("lineWrap", oldValue, getLineWrap());
    } 
    @Override
    public void setTabSize(int tabSize) {
        int oldValue = getTabSize();
        super.setTabSize(tabSize);
        firePropertyChange("tabSize", oldValue, getTabSize());
    } 
    @Override
    public void setWrapStyleWord(boolean wrapStyleWord) {
        boolean oldValue = getWrapStyleWord();
        super.setWrapStyleWord(wrapStyleWord);
        firePropertyChange("wrapStyleWord", oldValue, getWrapStyleWord());
    }     

    public void propertyChange(PropertyChangeEvent pce) {
        java.awt.Color nv = (java.awt.Color)pce.getNewValue();
        if (nv != backgroundPaint && nv != NOCOLOR) {
            setBackgroundPaint(nv);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        if (!isOpaque() && backgroundPaint != null) {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setPaint(backgroundPaint);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        super.paintComponent(g);
    }

}
