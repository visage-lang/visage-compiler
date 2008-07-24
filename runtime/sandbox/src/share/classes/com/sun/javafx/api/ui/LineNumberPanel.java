/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.javafx.api.ui;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;

public class LineNumberPanel extends JPanel {

    int mLineCount;

    public void setLineCount(int lineCount) {
        mLineCount = lineCount;
        revalidate();
        repaint();
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        revalidate();
    }

    @Override
    public void setBorder(Border border) {
        super.setBorder(border);
        revalidate();
    }

    public int getLineCount() {
        return mLineCount;
    }
    

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        if (isPreferredSizeSet()) {
            return d;
        }
        Font font = getFont();
        FontMetrics metrics = getFontMetrics(font);
        String dummy = Integer.toString(mLineCount+1);
        if (dummy.length() < 2) {
            dummy = "99";
        }
        int maxWidth = metrics.stringWidth(dummy);
        int h = metrics.getHeight();
        d = new Dimension(d.width + maxWidth, d.height + h*(mLineCount+300));
        return d;
    }

    public Rectangle getCellBounds(int line) {
        Font font = getFont();
        FontMetrics metrics = getFontMetrics(font);
        int h = metrics.getHeight();
        Insets insets = getInsets();
        int width = getWidth();
        int x1 = 0;
        int x2 = width;
        int y0 = 0;
        if (insets != null) {
            x1 = insets.left;
            x2 -= insets.right;
            y0 = insets.top;
        }
        return new Rectangle(x1, y0 + line * h, x2 - x1, h);
    }

    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font font = getFont();
        FontMetrics metrics = getFontMetrics(font);
        g.setFont(font);
        Rectangle clip = g.getClipBounds();
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(clip.x, clip.y, clip.width, clip.height);
        }
        g.setColor(getForeground());
        int ascent = metrics.getAscent();
        int h = metrics.getHeight();
        int lineCount = mLineCount + 1;
        String dummy = Integer.toString(lineCount);
        if (dummy.length() < 2) {
            dummy = "99";
        }
        int maxWidth = metrics.stringWidth(dummy);
        int startLine = clip.y / h;
        int endLine = ((clip.y + clip.height) / h) + 1;
        //int startLine = 0;
        //int endLine = (getHeight()/h+1);
        Insets insets = getInsets();
        int width = getWidth();
        int x1 = 0;
        int x2 = width;
        int y0 = 0;
        if (insets != null) {
            x1 = insets.left;
            x2 -= insets.right;
            y0 = insets.top;
        }
        for (int i = startLine; ; i++) {
            String text = Integer.toString(i + 1);
            int w = metrics.stringWidth(text);
            int y = i * h + y0;
            g.drawString(text, x2-w, y + ascent);
            if (y > clip.y+clip.height + h) {
                break;
            }
        }
    }
}
