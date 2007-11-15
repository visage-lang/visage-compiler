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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.plaf.TabbedPaneUI;

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
    static class HashTableWrapper extends Hashtable{
        Hashtable mDelegate;
        public HashTableWrapper(Hashtable table){
            mDelegate = table;
        }
        public Object get(Object key) {
            return mDelegate.get(key);
        }
        public Object put(Object key, Object value) {
            return mDelegate.put(key, value);
        }
        public void destroy() {
            mDelegate = null;
        }
        public int size() {
            return mDelegate == null ? 0 : mDelegate.size();
        }
    }

    HashTableWrapper mImageCache;
    HashTableWrapper mSyncImageCache;
    JApplet mApplet;
    Map mResourceMap = new HashMap();
    Set mWindows = new HashSet();

    public JApplet getApplet() {
        return mApplet;
    }

    public void setApplet(JApplet applet) {
        mApplet = applet;
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
    public static void paintVerticalGradient(Component comp, Graphics g, Color start, Color end) {
        Graphics2D g2 = (Graphics2D)g;
        Paint oldPaint = g2.getPaint();
        int width = comp.getWidth();
        int height = comp.getHeight();
        GradientPaint gradient =
            new GradientPaint(0f,
                              0f,
                              start,
                              width,
                              height,
                              end);
        g2.setRenderingHint(
                            RenderingHints.KEY_COLOR_RENDERING,
                            RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(
                            RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(
                            RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);
        g2.setPaint(gradient);
        g.fillRect(
                   0,
                   0,
                   width,
                   height);
        g2.setPaint(oldPaint);
    }    

    public Object getImageCache() {
        return mImageCache;
    }

    public Object getImageCache(boolean sync) {
        return sync ? mSyncImageCache : mImageCache;
    }

    public Object createImageCache() {
        return new Hashtable() {
                public Object get(Object key) {
                    String strKey = key.toString();
                    Object result = super.get(strKey);
                    if (result != null) {
                        return result;
                    }
                    result = mImageCache.get(key);
                    if (result != null) {
                        super.put(strKey, result);
                    }
                    return result;
                }
                public Object put(Object key, Object value) {
                    return super.put(key.toString(), value);
                }
            };
    }
    public void defineImage(String url, Image image) throws MalformedURLException {
        mImageCache.put(new URL(url), image);
    }

    public Image getImage(String url) {
        long start = System.currentTimeMillis();
        Image result = getImage0(url);
        long end = System.currentTimeMillis();
        //System.out.println("getImage " + (end - start));
        return result;
    }

    static Image EMPTY_16x16_IMAGE;
    static {
        try {
            EMPTY_16x16_IMAGE =
                ImageIO.read(UIContextImpl.class.getResourceAsStream("resources/empty.gif"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Image getTransparentImage(int width, int height) {
        return EMPTY_16x16_IMAGE.getScaledInstance(width,
                                                   height,
                                                   Image.SCALE_SMOOTH);
    }
         

    Image getImage0(String url) {
        try {
            if (url.startsWith("data:")) {
                int comma = url.indexOf(",");
                if (comma >= 5) {
                    String header = url.substring(5, comma);
                    if (header.endsWith("base64")) {
                        String imageData = url.substring(13).replaceAll(" ", "\n");
                        try {
                            byte[] buf = new sun.misc.BASE64Decoder().decodeBuffer(imageData);
                            return javax.imageio.ImageIO.read(new ByteArrayInputStream(buf));
                        } catch (Exception e) {
                            System.out.println("Invalid image data:"+imageData);
                            e.printStackTrace();
                            return EMPTY_16x16_IMAGE;
                        }
                    }
                }
            }
            return (Image)mImageCache.get(makeURL(url));
        } catch (Exception e) {
            System.out.println("invalid url: " + url);
            return EMPTY_16x16_IMAGE;
        }
        //return null;
    }

    Map mAppletUrlMap = new HashMap();

    URL makeURL(String url) throws Exception {
        if (mApplet != null) {
            try {
                URL result = (URL)mAppletUrlMap.get(url);
                if (result != null) {
                    return result;
                }
                return new URL(url);
            } catch (MalformedURLException e) {
                URL result =
                    mApplet.getClass().getClassLoader().getResource(url);
                System.out.println("applet class loader returned: " + result);
                if (result != null) {
                    mAppletUrlMap.put(url, result);
                }
                return result;
            }
        } else {
            try {
                return new URL(url);
            } catch (MalformedURLException e) {
                URL result = Thread.currentThread().getContextClassLoader().getResource(url);
                if (result == null) {
                    throw e;
                }
                return result;
            }
        }
    }

    public Image getLoadedImage(String url) {
        try {
            if (url.startsWith("data:")) {
                return getImage(url); 
            }
            return (Image)mSyncImageCache.get(makeURL(url));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("invalid url: " + url);
            return EMPTY_16x16_IMAGE;
        }
        //return null;
    }

    public URL getImageURL(String urlStr) {
        try {
            URL url = new URL(urlStr);
            mImageCache.get(url);
            URL resource = (URL)mResourceMap.get(url);
            if (resource != null) {
                return resource;
            }
            return url;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public JTabbedPane createTabbedPane() {
        JTabbedPane t = new JTabbedPane();
        addDropHandler(t);
        return t;
    }

    public void registerWindow(Window win) {
        mWindows.add(win);
    }

    public void unregisterWindow(Window win) {
        mWindows.remove(win);
    }


    public void addChoosableFileFilter(JFileChooser fileChooser, FileFilter fileFilter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addDropHandler(JTabbedPane tabbedPane) {
        new DropTarget(tabbedPane, new DTListener(tabbedPane));
    }

    static class DTListener implements DropTargetListener {

        JTabbedPane mTabbedPane;
        public DTListener(JTabbedPane p) { mTabbedPane = p; }
        public void dragEnter(DropTargetDragEvent e) {
            dragOver(e);
        }
        public void dragOver(DropTargetDragEvent e) {
            Point p = e.getLocation();
            TabbedPaneUI ui = (TabbedPaneUI)mTabbedPane.getUI();
            final int select = ui.tabForCoordinate(mTabbedPane, p.x, p.y);
            if (select < 0) {
                e.rejectDrag();
                return;
            }
            Component c = mTabbedPane.getComponentAt(select);
            if (c != null) {
                Rectangle bounds = c.getBounds();
                if (bounds.contains(p)) {
                    e.rejectDrag();
                } else {
                    e.acceptDrag(e.getDropAction());
                }
            } else {
                e.rejectDrag();
            }
            if (select != mTabbedPane.getSelectedIndex()) {
                mTabbedPane.setSelectedIndex(select);
            }
        }
        public void dropActionChanged(DropTargetDragEvent e) { }
        public void dragExit(DropTargetEvent e) { }
        public void drop(DropTargetDropEvent e) {
        }
    }

 



}
