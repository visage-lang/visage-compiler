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

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JApplet;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;

/**
 * Interface for accessing undelying Java components and utilities
 */
public interface UIContext {

    /**
     * Create a panel for a javafx component
     *
     * @return a panel
     */
    public JPanel createPanel();
    
    /**
     * Create a button for a javafx component
     *
     * @return a button
     */
    public XButton createButton();

    /**
     * Get a Font
     *
     * @return a font
     */
    public Font getFont(String url, int style, int size);


    public boolean isBitSet(int a, int b);

    public int setBit(int a, int b);

    public int clearBit(int a, int b);

   public JTabbedPane createTabbedPane();

    public Image getImage(String url);

    public Image getLoadedImage(String url);
    public URL getImageURL(String urlStr);
    public void defineImage(String url, Image image) throws MalformedURLException;

    public void registerWindow(Window win);
    public void unregisterWindow(Window win);
    public void addChoosableFileFilter(JFileChooser fileChooser,
                                       FileFilter fileFilter);

    public JApplet getApplet();

    public void setApplet(JApplet applet);


}
