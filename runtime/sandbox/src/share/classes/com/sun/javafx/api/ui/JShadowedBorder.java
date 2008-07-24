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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;


public class JShadowedBorder implements Border {
   private Insets insets;

   public JShadowedBorder() {
      insets = new Insets(1, 1, 3, 3);
   }

   public Insets getBorderInsets(Component c) {
      return insets;
   }

   public boolean isBorderOpaque() {
      // we'll be filling in our own background.
      return true;
   }

   public void paintBorder(Component c, Graphics g, int x,
                                    int y, int w, int h) {
      // choose which colors we want to use
      Color bg = c.getBackground();
      if(c.getParent()!=null)
         bg = c.getParent().getBackground();
      Color mid = bg.darker();
      Color rect = mid.darker();
      Color edge = average(mid, bg);

      // fill in the corners with the parent-background
      // so it looks see-through
      g.setColor(bg);
      g.fillRect(0, h-3, 3, 3);
      g.fillRect(w-3, 0, 3, 3);
      g.fillRect(w-3, h-3, 3, 3);

      // draw the outline
      g.setColor(rect);
      g.drawRect(0, 0, w - 3, h - 3);

      // draw the drop-shadow
      g.setColor(mid);
      g.drawLine(1, h - 2, w - 2, h - 2);
      g.drawLine(w - 2, 1, w - 2, h - 2);

      g.setColor(edge);
      g.drawLine(2, h - 1, w - 2, h - 1);
      g.drawLine(w - 1, 2, w - 1, h - 2);
   }

   private static Color average(Color c1, Color c2) {
      int red = c1.getRed() + (c2.getRed() - c1.getRed()) / 2;
      int green = c1.getGreen() + (c2.getGreen() - c1.getGreen()) / 2;
      int blue = c1.getBlue() + (c2.getBlue() - c1.getBlue()) / 2;
      return new Color(red, green, blue);
   }
}

