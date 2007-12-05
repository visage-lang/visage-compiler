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

import com.sun.javafx.api.ui.UIContextImpl.FXTreeModel;
import com.sun.javafx.api.ui.UIContextImpl.InvisibleCaret;
import com.sun.javafx.runtime.FXObject;
import com.sun.javafx.runtime.awt.TransferHandler;
import com.sun.javafx.runtime.location.BooleanLocation;
import com.sun.javafx.runtime.location.ObjectLocation;
import com.sun.javafx.runtime.location.SequenceLocation;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SpinnerModel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.TabbedPaneUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.Position.Bias;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLEditorKit.HTMLFactory;
import javax.swing.text.html.StyleSheet;
import javax.swing.text.Element;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.JTextComponent;
import javax.swing.text.NumberFormatter;
import javax.swing.text.Position;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.ImageView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author jclarke
 */
public class UIContextImpl implements UIContext {
    XTreeCellRenderer mTreeCellRenderer = new XTreeCellRenderer();
    XTableCellRenderer mTableCellRenderer = new XTableCellRenderer();
    static Map<String, Reference>  mGlobalImageCache = Collections.synchronizedMap(new HashMap<String, Reference>(100));
    public UIContextImpl() {

        mSyncImageCache = new HashTableWrapper(new Hashtable() {
            @Override
                public Object get(Object key) {
                    Image result = (Image)mImageCache.get(key);
                    if (result != null) {
                        new ImageIcon(result);
                    }
                    return result;
                }
            });

        mImageCache = new HashTableWrapper(new Hashtable() {

            @SuppressWarnings("unchecked")
                Set mObservers = Collections.synchronizedSet(new HashSet());

                Map mSuffixCache = new HashMap();
                ReferenceQueue refQueue = new ReferenceQueue();

                class MyRef extends WeakReference {
                    Object key;
                @SuppressWarnings("unchecked")
                    MyRef(Object key, Object value, ReferenceQueue q) {
                        super(value, q);
                        this.key = key;
                    }
                }
            @Override
            @SuppressWarnings("unchecked")
                public synchronized Object put(Object key, Object value) {
                    String strKey = ((URL)key).toString();
                    return super.put(strKey, new MyRef(strKey, value, refQueue));
                }

                void pollRefs() {
                    MyRef ref;
                    while ((ref = (MyRef)refQueue.poll()) != null) {
                        if (super.get(ref.key) == ref) {
                            remove(ref.key);
                            mGlobalImageCache.remove(ref.key);
                        }
                    }
                }

            @Override
            @SuppressWarnings("unchecked")
                public synchronized Object get(final Object key) {
                    String strKey = ((URL)key).toString();
                    Reference ref  = (Reference)super.get(strKey);
                    Image result = null;
                    if (ref != null) {
                        result = (Image)ref.get();
                        if (result == null) {
                            remove(strKey);
                        }
                    }
                    pollRefs();
                    if (result == null) {
                        ref = mGlobalImageCache.get(strKey);
                        if (ref != null) {
                            result = (Image)ref.get();
                            if (result == null) {
                                mGlobalImageCache.remove(strKey);
                            } else {
                                put(key, result);
                            }
                        }
                    }
                    boolean cached = false;
                    if (result == null) {
                        URL src = (URL)key;
                        if (src.getPath().startsWith("jfilechooser/icon/")) {
                            String fileName = src.getPath().substring("jfilechooser/icon/".length());
                            createFileChooser();
                            File tmpFile = null;
                            String suffix = null;
                            try {
                                URI uri = new URI("file:"+fileName);
                                File f = new File(uri.getPath());
                                String name = f.getName();
                                int dot = name.lastIndexOf(".");
                                if (dot > 0) {
                                    suffix = name.substring(dot);
                                }
                                result = (Image)mSuffixCache.get(suffix);
                                if (result == null) {
                                    if (!f.exists()) {
                                        tmpFile = File.createTempFile("tmp", suffix);
                                        f = tmpFile;
                                    }
                                }
                                if (result == null) {
                                    if (f.exists()) {
                                        Icon icon = mFileChooser.getUI().getFileView(mFileChooser).getIcon(f);
                                        if (icon instanceof ImageIcon) {
                                            result = ((ImageIcon)icon).getImage();
                                        } else {
                                            result = new BufferedImage(icon.getIconWidth(),
                                                                       icon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR) ;
                                            Graphics g = result.getGraphics() ;
                                            icon.paintIcon(mFileChooser, g, 0, 0) ;
                                        }
                                    } else {
                                        result = EMPTY_16x16_IMAGE;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (tmpFile != null) {
                                    mSuffixCache.put(suffix, result);
                                    tmpFile.delete();
                                }
                            }
                        } else {
                            if (null == src.getProtocol() ||
                                "".equals(src.getProtocol()) ||
                                "file".equals(src.getProtocol())) {
                                //System.out.println("looking for resource: "+ src.getPath());
                                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                                InputStream is = loader.getResourceAsStream(src.getPath());
                                if (is != null) {
                                    try {
                                        URL imageUrl = loader.getResource(src.getPath());
                                        String srcName = src.toString().toLowerCase();
                                        try {
                                            if (!srcName.endsWith(".bmp")) {
                                                result = Toolkit.getDefaultToolkit().createImage(imageUrl);
                                            }
                                            if (result == null) {
                                                result = ImageIO.read(imageUrl);
                                            }
                                        } catch (Exception e) {
                                            if (result == null) {
                                                result = ImageIO.read(imageUrl);
                                            }
                                        }
                                        mResourceMap.put(key, imageUrl);
                                    } catch (IOException ioe) {
                                        ioe.printStackTrace();
                                    } finally {
                                    }
                                }
                            }
                            if (result == null) {
                                //result = getImageFromCache(src);
                                cached = result != null;
                            }
                            if (result == null) {
                                try {
                                    URL codeBase = null;
                                    //URL codeBase = mModule.getCodeBase();
                                    URL u;

                                    if (codeBase == null) {

                                            u = src;
                                    } else {
                                        u = new URL(codeBase, src.toString());
                                    }
                                    if (false && mApplet != null) {
                                        result = mApplet.getImage(u);
                                    } else {
                                        try {
                                            String srcName = u.toString().toLowerCase();
                                            if (!srcName.endsWith(".bmp")) {
                                                if ("http".equals(u.getProtocol())) {
                                                    result = Toolkit.getDefaultToolkit().createImage(new CachedImage(UIContextImpl.this, u));
                                                } else {
                                                    result = Toolkit.getDefaultToolkit().createImage(u);
                                                }
                                            }
                                            if (result == null) {
                                                result = ImageIO.read(u);
                                            }
                                        } catch (Exception e) {
                                            if (result == null) {
                                                result = ImageIO.read(u);
                                            }
                                        }

                                    }
                                } catch (java.io.IOException e) {
                                } catch (java.security.AccessControlException e) {
                                    System.out.println("access denied: " + key);
                                }
                            }

                            if (result != null) {
                                if ((src.getProtocol() == null ||
                                     src.getProtocol().equals("file") ||
                                     src.getProtocol().equals(""))) {
                                    // force load
                                    //new ImageIcon(result);
                                } else if (false) {
                                    ImageObserver observer = new ImageObserver() {
                                            public boolean imageUpdate(final Image image, final int infoflags, final int x, final
 int y, final int width, final int height) {
                                                if ((infoflags & ALLBITS) != 0) {
                                                    final ImageObserver self = this;
                                                    try {
                                                        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment(
);
                                                        GraphicsDevice gs = ge.getDefaultScreenDevice();
                                                        GraphicsConfiguration gc = gs.getDefaultConfiguration();
                                                        // Create an image that supports arbitrary levels of transparency
                                                        final Image i = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
                                                        i.getGraphics().drawImage(image, 0, 0, self);
                                                        SwingUtilities.invokeLater(new Runnable() {
                                                                public void run() {
                                                                    put(key, i);
                                                                }
                                                            });
                                                    } catch (java.security.AccessControlException e) {
                                                        System.out.println("create image access denied: " + key);
                                                    }
                                                    mObservers.remove(this);
                                                    return false;
                                                }
                                                return true;
                                            }
                                        };
                                    mObservers.add(observer);
                                    int w  = result.getWidth(observer);
                                    if (w >= 0) {
                                        int h = result.getHeight(observer);
                                        if (h >= 0) {
                                            mObservers.remove(observer);
                                        }
                                    }

                                }
                            }
                        }
                        if (result != null) {
                            mGlobalImageCache.put(strKey, new WeakReference(result));
                            put(key, result);
                        } else {
                            System.out.println("not found: " + src);
                        }
                    }
                    return result;
                }
            });
        mSyncHtmlKit = new HTMLEditorKit() {
            @Override
            @SuppressWarnings("unchecked")
            public Document createDefaultDocument() {
                StyleSheet styles = getStyleSheet();
                StyleSheet ss = new StyleSheet();
                ss.addStyleSheet(styles);
                HTMLDocument doc = new HTMLDoc(ss);
                doc.setParser(getParser());
                //doc.setAsynchronousLoadPriority(4);
                //doc.setTokenThreshold(100);
                doc.putProperty("imageCache", mSyncImageCache);
                if (mMemLeakWorkaround) {
                    mDocs.put(doc, null);
                }
                return doc;
            }
        };
        mSyncHtmlKit.setAutoFormSubmission(false);
    }
            

    class XTreeCellRenderer extends DefaultTreeCellRenderer {

        String mText;
        HTMLDocument mDoc;

        @Override
        public void setText(String t) {
            if (t != null) {
                if (t.equals(mText)) {
                    return;
                }
                mText = t;
                if (t.startsWith("<html>")) {
                    HTMLEditorKit kit = mHtmlKit;
                    if (mDoc == null) {
                        mDoc = (HTMLDocument)kit.createDefaultDocument();
                        mDoc.setPreservesUnknownTags(false);
                        mDoc.setBase(FILE_BASE);
                    }
                    HTMLDocument doc = mDoc;
                    Object base = getClientProperty("html.base");
                    if (base instanceof URL) {
                        doc.setBase((URL)base);
                    }
                    //doc.putProperty("imageCache", getImageCache());
                    Reader r = new StringReader(t);
                    try {
                        doc.remove(0, doc.getLength());
                        doc.getStyleSheet().addRule(
                                                    displayPropertiesToCSS(getFont(), getForeground()));
                        kit.read(r, doc, 0);
                    } catch (Throwable e) {
                    }
                    ViewFactory f = kit.getViewFactory();
                    View hview = f.create(doc.getDefaultRootElement());
                    View v = new Renderer(this, f, hview);
                    putClientProperty("html", v);
                } else {
                    putClientProperty("html", null);
                    mText = null;
                    super.setText(t);
                }
            } else {
                mText = null;
                super.setText(t);
            }
        }

        @Override
        public void paint(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;
            Composite oldComp = g2.getComposite();
            if (!isEnabled()) {
                if (getClientProperty("html") != null) {
                    AlphaComposite composite =
                        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
                    g2.setComposite(composite);
                }
            }
            super.paint(g2);
            g2.setComposite(oldComp);

        }


        @Override
        public String getText() {
            if (getClientProperty("html") != null) {
                return mText;
            }
            return super.getText();
        }

        String mToolTip;

        @Override
        public String getToolTipText(MouseEvent me) {
            return mToolTip;
        }

        public Component getTreeCellRendererComponent(JTree tree, String value,
                                                      boolean sel,
                                                      boolean expanded,
                                                      boolean leaf, int row,
                                                      boolean hasFocus,
                                                      String tooltip,
                                                      Color cellForeground,
                                                      Color cellBackground,
                                                      Color selectedCellForeground,
                                                      Color selectedCellBackground) {

            mToolTip = tooltip;
            super.getTreeCellRendererComponent(tree,
                                               "",
                                               sel,
                                               expanded, leaf, row, hasFocus);

            setText(value);
            setIcon(null);
            setDisabledIcon(null);
            if (cellForeground != null) {
                setTextNonSelectionColor(cellForeground);
            }
            if (cellBackground != null) {
                setBackgroundNonSelectionColor(cellBackground);
            }
            if (selectedCellForeground != null) {
                setTextSelectionColor(selectedCellForeground);
            }
            if (selectedCellBackground != null) {
                setBackgroundSelectionColor(selectedCellBackground);
            }

            return this;
        }
    }
    public class FXTreeCellRenderer implements TreeCellRenderer {
        Color mCellForeground;
        Color mCellBackground;
        Color mSelectedCellBackground;
        Color mSelectedCellForeground;
        Method mText;
        Method mToolTipText;
        public FXTreeCellRenderer() {
            try {
                Class treeCellClass = Class.forName("javafx.ui.TreeCell");
                Method[] methods = treeCellClass.getMethods();
                String textMethodName = "get$text";
                String toolTipTextName = "get$toolTipText";
                for(int i = 0; i < methods.length; i++) {
                    if(methods[i].getName().equals(textMethodName )) {
                        mText = methods[i];
                    }else if(methods[i].getName().equals(toolTipTextName)) {
                        mToolTipText = methods[i];
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(UIContextImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Type treeCellType = mModule.getType("javafx.ui.TreeCell");
            //mText = treeCellType.getAttribute("text");
            //mToolTipText = treeCellType.getAttribute("toolTipText");
            
        }
        public void setCellForeground(Color color) {
            mCellForeground = color;
        }
        public void setCellBackground(Color color) {
            mCellBackground = color;
        }
        public void setSelectedCellBackground(Color color) {
            mSelectedCellBackground = color;
        }
        public void setSelectedCellForeground(Color color) {
            mSelectedCellForeground = color;
        }

        public Component getTreeCellRendererComponent(JTree tree,
                                                      Object value,
                                                      boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            String text = "";
            String tip = null;
            if (value instanceof FXObject) {
                try {
                    //text = cell.getString(mText, 0);
                    ObjectLocation loc = (ObjectLocation) mText.invoke(value);
                    text = (String)loc.get();
                }catch(Throwable ex) {
                    Logger.getLogger(UIContextImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    //tip = cell.getString(mToolTipText, 0);
                    ObjectLocation loc = (ObjectLocation) mToolTipText.invoke(value);
                    tip = (String)loc.get();
                }catch(Throwable ex) {
                    Logger.getLogger(UIContextImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } else {
                text = String.valueOf(value);
            }
            return UIContextImpl.this.getTreeCellRendererComponent(tree, text,
                                                                   selected, expanded,
                                                                   leaf, row, hasFocus,
                                                                   tip,
                                                                   mCellForeground, mCellBackground,
                                                                   mSelectedCellForeground,
                                                                   mSelectedCellBackground);
        }
    }

    @SuppressWarnings("unchecked")
    public static class FXTreeModel implements TreeModel, TreeSelectionListener {
        Object mTree;
        Set mListeners = new HashSet();
        static Method mCells;
        static Method[] treeMethods;
        static Method[] treeCellMethods;

        static {
            try {
                Class treeCellClass = Class.forName("javafx.ui.TreeCell");
                treeCellMethods = treeCellClass.getMethods();
                mCells = getProperty(treeCellMethods, "cells");
                Class treeClass = Class.forName("javafx.ui.Tree");
                treeMethods = treeClass.getMethods();
            } catch (Exception ex) {
                Logger.getLogger(UIContextImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        public FXTreeModel(Object tree) {
            mTree = tree;
        }
        
        private static Method getProperty(Method[] methods, String basename) {
            String fxname = "get$" + basename;
            for(int i = 0; i < methods.length; i++ ) {
                if(methods[i].getName().equals(fxname)) {
                    return methods[i];
                }
            }
            return null;
        }

        public Object getRoot() {
            //return mTree.get("root", 0);
            Method meth = getProperty(treeMethods,"root");
            if(meth != null) {
                try {
                    ObjectLocation location = (ObjectLocation)meth.invoke(mTree);
                    return location.get();
                } catch (Throwable ex) {
                    Logger.getLogger(UIContextImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return null;
        }

        public Object getChild(Object parent, int i) {
            try {
                if (parent == null || ! (parent instanceof FXObject) ){
                    return null;
                }
                //Value value = (Value)parent;
                //return value.get(mCells, i);
                SequenceLocation location = (SequenceLocation) mCells.invoke(parent);
                return location.get(i);
                
            } catch (Throwable ex) {
                Logger.getLogger(UIContextImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        public int getChildCount(Object parent) {
            if (parent == null || ! (parent instanceof FXObject)) {
                return 0;
            }
            try {
            //Value value = (Value)parent;
            //return value.getCardinality(mCells);
                SequenceLocation location = (SequenceLocation) mCells.invoke(parent);
                return location.get().size();
            } catch (Throwable ex) {
                Logger.getLogger(UIContextImpl.class.getName()).log(Level.SEVERE, null, ex);
            }                
            return 0;
        }

        public boolean isLeaf(Object parent) {
            return getChildCount(parent) == 0;
        }

        public void valueForPathChanged(TreePath path, Object obj) {
        }

        public int getIndexOfChild(Object parent, Object child) {
            if(parent == null || !(parent instanceof FXObject)) {
                return -1;
            }
            /*****
            Value value = (Value)parent;
            int size = value.getCardinality(mCells);
            for (int i = 0; i < size; i++) {
                if (value.get(mCells, i) == child) {
                    return i;
                }
            }
            *****/
            try {
                SequenceLocation location = (SequenceLocation) mCells.invoke(parent);
                int size =  location.get().size();
                for(int i = 0; i < size; i++) {
                    if(location.get(i) == child) {
                        return i;
                    }
                }
            } catch (Throwable ex) {
                Logger.getLogger(UIContextImpl.class.getName()).log(Level.SEVERE, null, ex);
            } 
            return -1;
        }
        @SuppressWarnings("unchecked")
        public void addTreeModelListener(TreeModelListener listener) {
            mListeners.add(listener);
        }

        public void removeTreeModelListener(TreeModelListener listener) {
            mListeners.remove(listener);
        }

        @SuppressWarnings("unchecked")
        public void fireTreeNodesChanged(TreeModelEvent e) {
            if (mListeners.size() > 0) {
                TreeModelListener[] arr = new TreeModelListener[mListeners.size()];
                mListeners.toArray(arr);
                for (int i = 0; i < arr.length; i++) {
                    arr[i].treeNodesChanged(e);
                }
            }
        }

        @SuppressWarnings("unchecked")
        public void fireTreeNodesInserted(TreeModelEvent e) {
            if (mListeners.size() > 0) {
                TreeModelListener[] arr = new TreeModelListener[mListeners.size()];
                mListeners.toArray(arr);
                for (int i = 0; i < arr.length; i++) {
                    arr[i].treeNodesInserted(e);
                }
            }
        }

        @SuppressWarnings("unchecked")
        public void fireTreeNodesRemoved(TreeModelEvent e) {
            if (mListeners.size() > 0) {
                TreeModelListener[] arr = new TreeModelListener[mListeners.size()];
                mListeners.toArray(arr);
                for (int i = 0; i < arr.length; i++) {
                    arr[i].treeNodesRemoved(e);
                }
            }
        }

        @SuppressWarnings("unchecked")
        public void fireTreeStructureChanged(TreeModelEvent e) {
            if (mListeners.size() > 0) {
                TreeModelListener[] arr = new TreeModelListener[mListeners.size()];
                mListeners.toArray(arr);
                for (int i = 0; i < arr.length; i++) {
                    arr[i].treeStructureChanged(e);
                }
            }

        }

        public void valueChanged(TreeSelectionEvent e) {
            TreePath old = e.getOldLeadSelectionPath();
            try {
            Method inSelMethod = getProperty(FXTreeModel.treeMethods, "inSelectionChange");
            Method selectedMethod = getProperty(treeCellMethods, "selected");
            BooleanLocation inSelectionChangeLoc = (BooleanLocation)inSelMethod.invoke(mTree);
            inSelectionChangeLoc.set(true);            
            //mTree.setBoolean("inSelectionChange", 0, true);
            if (old != null) {
                Object oldLead = old.getLastPathComponent();
                if (oldLead != null) {
                    BooleanLocation selectedLoc = (BooleanLocation)selectedMethod.invoke(oldLead);
                    selectedLoc.set(false);
                    //oldLead.setBoolean("selected", 0, false);
                }
            }
            TreePath p = e.getNewLeadSelectionPath();
            if (p != null) {
                Object newLead = p.getLastPathComponent();
                if (newLead != null) {
                    BooleanLocation selectedLoc = (BooleanLocation)selectedMethod.invoke(newLead);
                    selectedLoc.set(true);
                    //newLead.setBoolean("selected", 0, true);
                }
            }
            //mTree.setBoolean("inSelectionChange", 0, false);
            inSelectionChangeLoc.set(false);
            } catch (Throwable ex) {
                Logger.getLogger(UIContextImpl.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }


    }

    class XTableCellRenderer extends DefaultTableCellRenderer {

        XSimpleLabel je;


        public Component getTableCellRendererComponent(JTable table,
                                                       String value,
                                                       boolean sel,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column,
                                                       String tooltip) {

            JLabel proto = (JLabel) super.getTableCellRendererComponent(table,
                                                                        "",
                                                                        sel,
                                                                        hasFocus, row, column);
            //value = mModel.getValueAt(value, 0);
            if (je == null) {
                je = new XSimpleLabel(true, false);
            }
            je.setOpaque(table.isOpaque());
            je.setFont(proto.getFont());
            je.setBackground(proto.getBackground());
            je.setForeground(proto.getForeground());
            je.setText(value);
            //je.setToolTipText(tooltip);
            je.setBorder(proto.getBorder());
            Component result = je;
            return result;
        }
    }

    public Component getTreeCellRendererComponent(JTree tree, String value,
                                                  boolean sel,
                                                  boolean expanded,
                                                  boolean leaf, int row,
                                                  boolean hasFocus,
                                                  String tooltip,
                                                  Color cellFg,
                                                  Color cellBg,
                                                  Color cellSelFg,
                                                  Color cellSelBg) {
        return mTreeCellRenderer.getTreeCellRendererComponent(tree, value, sel, 
                expanded, leaf, row, hasFocus, tooltip, cellFg, 
                cellBg, cellSelFg, cellSelBg);
    }

    public Component getTableCellRendererComponent(JTable table, String value,
                                                   boolean sel,
                                                   boolean hasFocus,
                                                   int row, int column,
                                                   String tooltip) {
        return mTableCellRenderer.getTableCellRendererComponent(table, value, sel, hasFocus, row, column, tooltip);
    }

    public static class XTableCell {
        String text;
        String tooltip;
        Color background;
        Color foreground;
        Font font;
        Border border;
        public XTableCell(String text, String tooltip, Color background,
                          Color foreground, Font font, Border border) {
            this.text = text;
            this.tooltip = tooltip;
            this.background = background;
            this.foreground = foreground;
            this.font = font;
            this.border = border;
        }
    }

    public static class XTableCellModel extends AbstractTableModel {
       
        List mCells = new ArrayList();
        String[] mColumnNames;
        int[] mColumnAlignments;

        public void setColumnNames(String[] names) {
            mColumnNames = names;

        }

        public void setColumnAlignments(int[] aligns) {
            mColumnAlignments = aligns;
        }

        public int getColumnAlignment(int column) {
            return mColumnAlignments[column];
        }

        @Override
        public Class getColumnClass(int column) {
            return XTableCell.class;
        }

        @SuppressWarnings("unchecked")
        public void addCell(int index, String text, String tooltip,
                            Font font, Color background, Color foreground,
                            Border border) {
            mCells.add(index, new XTableCell(text, tooltip, background,
                                             foreground, font, border));
            int col = index % getColumnCount();
            if (col == 0) {
                int row = index / getColumnCount();
                fireTableRowsInserted(row, row);
            }
        }

        public void updateCell(int index, String text, String tooltip,
                               Font font,
                               Color background, Color foreground,
                               Border border) {
            XTableCell cell = (XTableCell)mCells.get(index);
            cell.text = text;
            cell.tooltip = tooltip;
            cell.foreground = foreground;
            cell.background = background;
            cell.font = font;
            cell.border = border;
            int col = index % getColumnCount();
            int row = (index - col) / getColumnCount();
            fireTableRowsUpdated(row, row);
        }

        public void removeCell(int index) {
            mCells.remove(index);
            if (mCells.size() % getColumnCount() == 0) {
                int row = mCells.size() / getColumnCount();
                fireTableRowsDeleted(row, row);
            }
        }

        @Override
        public String getColumnName(int col) {
            return mColumnNames[col];
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        public Object getValueAt(int row, int column) {
            return mCells.get(row * getColumnCount() + column);
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
        }

        public int getColumnCount() {
            return mColumnNames == null ? 0 : mColumnNames.length;
        }

        public int getRowCount() {
            if (mColumnNames == null) return 0;
            int result = mCells.size() / mColumnNames.length;
            return result;
       }

    }
    public void installTableHeaderRenderer(JTable table) {
        TableCellRenderer def = table.getTableHeader().getDefaultRenderer();
        table.getTableHeader().setDefaultRenderer(new HeaderRenderer(def));
    }
    class HeaderRenderer implements TableCellRenderer {

        TableCellRenderer mDefaultRenderer;

        HeaderRenderer(TableCellRenderer defaultRenderer) {
            mDefaultRenderer = defaultRenderer;
        }

        HTMLDocument mDoc;

        public Component getTableCellRendererComponent(JTable table, Object value, boolean sel, boolean hasFocus, int
 row, int column) {
            // total hack...
            String str = value == null ? "" : value.toString();
            JLabel def = null;
            String x = str.startsWith("<html>") ? "" : str;
            def = (JLabel)mDefaultRenderer.getTableCellRendererComponent(table, x, sel, hasFocus, row, column);
            if (str.startsWith("<html>")) {
                HTMLEditorKit kit = mHtmlKit;
                if (mDoc == null) {
                    mDoc = (HTMLDocument)kit.createDefaultDocument();
                    mDoc.setPreservesUnknownTags(false);
                    mDoc.setBase(FILE_BASE);
                    mDoc.putProperty("imageCache", getImageCache(true));
                }
                Object base = def.getClientProperty("html.base");
                if (base instanceof URL) {
                    mDoc.setBase((URL)base);
                }
                HTMLDocument doc = mDoc;
                doc.getStyleSheet().addRule(
                                            displayPropertiesToCSS(def.getFont(),def.getForeground()));
                Reader r = new StringReader(str);
                try {
                    doc.remove(0, doc.getLength());
                    kit.read(r, doc, 0);
                } catch (Throwable e) {
                }
                ViewFactory f = kit.getViewFactory();
                View hview = f.create(doc.getDefaultRootElement());
                View v = new Renderer(def, f, hview);
                def.putClientProperty("html", v);
            }
            return def;
        }
    }

    public void installXTableCellRenderer(final JTable table) {
        table.setDefaultRenderer(XTableCell.class,
                                 new DefaultTableCellRenderer() {
                                     String mToolTip;
                                     XSimpleLabel je;

            @Override
                                     public Component getTableCellRendererComponent(JTable table,
                                                                                    Object value,
                                                                                    boolean sel,
                                                                                    boolean hasFocus,
                                                                                    int row,
                                                                                    int column) {

                                         XTableCell cell = (XTableCell)value;
                                         JLabel proto = (JLabel) super.getTableCellRendererComponent(table,
                                                             "",
                                                             sel,
                                                             hasFocus, row, column);
                                        if (je == null) {
                                             je = new XSimpleLabel() {
                                                     @Override
                                                     public String getToolTipText() {
                                                         return mToolTip;
                                                     }
                                                 };
                                             je.setOpaque(true);
                                         }
                                         if (cell.font != null) {
                                             je.setFont(cell.font);
                                         } else {
                                             je.setFont(proto.getFont());
                                         }
                                         if (cell.background != null) {
                                             je.setBackground(cell.background);
                                         } else {
                                             je.setBackground(proto.getBackground());
                                         }
                                         if (cell.foreground != null) {
                                             je.setForeground(cell.foreground);
                                         } else {
                                             je.setForeground(proto.getForeground());
                                         }
                                         mToolTip = cell.tooltip;
                                         je.setText(cell.text);
                                         if (cell.border != null) {
                                             je.setBorder(cell.border);
                                         } else {
                                             je.setBorder(proto.getBorder());
                                         }
                                         XTableCellModel model = (XTableCellModel)table.getModel();
                                         je.setHorizontalAlignment(model.getColumnAlignment(column));
                                         Component result = je;
                                         return result;
                                     }
                                 });
    }

    public class XTable extends JTable {
        XTable() {
            super(new XTableModel());
            installXTableCellRenderer(this);
        }

        @Override
        public String getToolTipText(MouseEvent event) {
            String tip = null;
            Point p = event.getPoint();


            int hitColumnIndex = columnAtPoint(p);
            int hitRowIndex = rowAtPoint(p);

            if ((hitColumnIndex != -1) && (hitRowIndex != -1)) {
                XTableCell cell =
                    (XTableCell)getModel().getValueAt(hitRowIndex,
                                                      hitColumnIndex);
                if (cell != null) {
                    return cell.tooltip;
                }
            }
            return null;
        }
    }

    
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
        @Override
        public Object get(Object key) {
            return mDelegate.get(key);
        }
        @SuppressWarnings("unchecked")
        @Override
        public Object put(Object key, Object value) {
            return mDelegate.put(key, value);
        }
        public void destroy() {
            mDelegate = null;
        }
        @Override
        public int size() {
            return mDelegate == null ? 0 : mDelegate.size();
        }
    }

        class PatchedImageView extends ImageView
        {
                float vAlign_Patched;

                public PatchedImageView(Element elem)
                {
                        super(elem);
                }

        @Override
                protected void setPropertiesFromAttributes()
                {
                        super.setPropertiesFromAttributes();

                        AttributeSet attr = getElement().getAttributes();
                        Object alignment = attr.getAttribute(HTML.Attribute.ALIGN);

                        vAlign_Patched = 1.0f;
                        if (alignment != null)
                        {
                                alignment = alignment.toString();
                                if ("top".equals(alignment))
                                {
                                        // gznote: workaround hack for Casual smiley face images
                                        int w = getImage().getWidth(null);
                                        int h = getImage().getHeight(null);
                                        if ((w==18) && (h==18))
                                        {
                                                vAlign_Patched = 0.75f;
                                        }
                                        else
                                        {
                                                vAlign_Patched = 0f;
                                        }
                                }
                                else if ("middle".equals(alignment))
                                {
                                        vAlign_Patched = .5f;
                                }
                        }
                }

        @Override
                public float getAlignment(int axis)
                {
                        switch (axis)
                        {
                                case View.Y_AXIS:
                                        return vAlign_Patched;
                                default:
                                        return super.getAlignment(axis);
                        }
                }
        }


    class PatchedHTMLFactory extends HTMLFactory
        {
        @Override
                public View create(Element elem)
                {
                        AttributeSet attrs = elem.getAttributes();
                        Object elementName = attrs.getAttribute(AbstractDocument.ElementNameAttribute);
                        Object o = (elementName != null) ? null : attrs.getAttribute(StyleConstants.NameAttribute);
                        if (o instanceof HTML.Tag)
                        {
                                HTML.Tag kind = (HTML.Tag) o;

                                if (kind==HTML.Tag.IMG)
                                {
                                        return new PatchedImageView(elem);
                                }
                                if (kind == HTML.Tag.OBJECT) {
                                    Object type = attrs.getAttribute(HTML.Attribute.TYPE);
                                    Object data = attrs.getAttribute(HTML.Attribute.DATA);
                                    if (type != null &&
                                        data != null) {
                                        String typeStr = type.toString();
                                        if (typeStr.equals("fx")) {

                                            String key = data.toString();
                                            System.out.println("UIContextImpl.PatchedHTMLFactory: FIXME: " + key);
//TODO -Need to resolve fx value from view
/*****************************************************************
                                            ValueList value = mModule.resolveValue(key);
                                            if (value != null) {
                                                Value x = value.getValue(0);
                                                Type widgetType = mModule.getType("javafx.ui.Widget");
                                                if (widgetType.isAssignableFrom(x)) {
                                                    try {
                                                        ValueList res =
                                                            mModule.callMethod(x, "getComponent", new ValueList[] {x});
                                                        if (res != null) {
                                                            Value comp =
                                                                res.getValue(0);
                                                            if (comp != null) {
                                                                JComponent jcomp =
                                                                    (JComponent)
                                                                    comp.get();
                                                                return new CompView(elem, jcomp);
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
***********************************************/
                                        }
                                    }
                                }

                        }

                        return super.create(elem);
                }
        }


        class PatchedHTMLEditorKit extends HTMLEditorKit
        {
                private final ViewFactory defaultPatchedFactory = new PatchedHTMLFactory();
        @Override
                public ViewFactory getViewFactory()
                {
                        return defaultPatchedFactory;
                }
        }

    boolean mMemLeakWorkaround = true;
    WeakHashMap mDocs = new WeakHashMap();

    static class HTMLDoc extends HTMLDocument {
        Set mDocumentListeners = new HashSet();
        HTMLDoc(StyleSheet ss) {
            super(ss);
        }
        @Override
        @SuppressWarnings("unchecked")
        public void addDocumentListener(DocumentListener l) {
            mDocumentListeners.add(l);
            super.addDocumentListener(l);

        }
        @Override
        public void removeDocumentListener(DocumentListener l) {
            mDocumentListeners.remove(l);
            super.removeDocumentListener(l);
        }

        public void removeAllDocumentListeners() {
            Object[] listeners = mDocumentListeners.toArray();
            for (int i = 0; i < listeners.length; i++) {
                removeDocumentListener((DocumentListener)listeners[i]);
            }
        }
    }


    HTMLEditorKit mHtmlKit = new PatchedHTMLEditorKit() {
        @Override
        @SuppressWarnings("unchecked")
            public Document createDefaultDocument() {
                StyleSheet styles = getStyleSheet();
                StyleSheet ss = new StyleSheet();
                ss.addStyleSheet(styles);
                HTMLDocument doc = new HTMLDoc(ss);
                doc.setParser(getParser());
                //doc.setAsynchronousLoadPriority(4);
                //doc.setTokenThreshold(100);
                doc.putProperty("imageCache", createImageCache());
                if (mMemLeakWorkaround) {
                    mDocs.put(doc, null);
                }
                return doc;
            }
        };
    {
        mHtmlKit.setAutoFormSubmission(false);
    }
    static URL FILE_BASE;
    static {
        try {
            FILE_BASE = new URL("file:");
        } catch (MalformedURLException e) {
        }
    }

    public static String displayPropertiesToCSS(Font font, Color fg) {
        StringBuffer rule = new StringBuffer("body {");
        if (font != null) {
            rule.append(" font-family: ");
            rule.append(font.getFamily());
            rule.append(" ; ");
            rule.append(" font-size: ");
            rule.append(font.getSize());
            rule.append("pt ;");
            if (font.isBold()) {
                rule.append(" font-weight: 700 ; ");
            }
            if (font.isItalic()) {
                rule.append(" font-style: italic ; ");
            }
        }
        if (fg != null) {
            rule.append(" color: #");
            if (fg.getRed() < 16) {
                rule.append('0');
            }
            rule.append(Integer.toHexString(fg.getRed()));
            if (fg.getGreen() < 16) {
                rule.append('0');
            }
            rule.append(Integer.toHexString(fg.getGreen()));
            if (fg.getBlue() < 16) {
                rule.append('0');
            }
            rule.append(Integer.toHexString(fg.getBlue()));
            rule.append(" ; ");
        }
        rule.append(" }");
        return rule.toString();
    }
    static class Renderer extends View {

        Renderer(JComponent c, ViewFactory f, View v) {
            super(null);
            host = c;
            factory = f;
            view = v;
            view.setParent(this);
            // initially layout to the preferred size
            setSize(view.getPreferredSpan(X_AXIS), view.getPreferredSpan(Y_AXIS));
        }

        /**
         * Fetches the attributes to use when rendering.  At the root
         * level there are no attributes.  If an attribute is resolved
         * up the view hierarchy this is the end of the line.
         */
        @Override
        public AttributeSet getAttributes() {
            return null;
        }

        /**
         * Determines the preferred span for this view along an axis.
         *
         * @param axis may be either X_AXIS or Y_AXIS
         * @return the span the view would like to be rendered into.
         *         Typically the view is told to render into the span
         *         that is returned, although there is no guarantee.
         *         The parent may choose to resize or break the view.
         */
        public float getPreferredSpan(int axis) {
            if (axis == X_AXIS) {
                // width currently laid out to
                return width;
            }
            return view.getPreferredSpan(axis);
        }

        /**
         * Determines the minimum span for this view along an axis.
         *
         * @param axis may be either X_AXIS or Y_AXIS
         * @return the span the view would like to be rendered into.
         *         Typically the view is told to render into the span
         *         that is returned, although there is no guarantee.
         *         The parent may choose to resize or break the view.
         */
        @Override
        public float getMinimumSpan(int axis) {
            return view.getMinimumSpan(axis);
        }

        /**
         * Determines the maximum span for this view along an axis.
         *
         * @param axis may be either X_AXIS or Y_AXIS
         * @return the span the view would like to be rendered into.
         *         Typically the view is told to render into the span
         *         that is returned, although there is no guarantee.
         *         The parent may choose to resize or break the view.
         */
        @Override
        public float getMaximumSpan(int axis) {
            return Integer.MAX_VALUE;
        }

        /**
         * Specifies that a preference has changed.
         * Child views can call this on the parent to indicate that
         * the preference has changed.  The root view routes this to
         * invalidate on the hosting component.
         * <p>
         * This can be called on a different thread from the
         * event dispatching thread and is basically unsafe to
         * propagate into the component.  To make this safe,
         * the operation is transferred over to the event dispatching
         * thread for completion.  It is a design goal that all view
         * methods be safe to call without concern for concurrency,
         * and this behavior helps make that true.
         *
         * @param child the child view
         * @param width true if the width preference has changed
         * @param height true if the height preference has changed
         */
        @Override
        public void preferenceChanged(View child, boolean width, boolean height) {
            host.revalidate();
            host.repaint();
        }

        /**
         * Determines the desired alignment for this view along an axis.
         *
         * @param axis may be either X_AXIS or Y_AXIS
         * @return the desired alignment, where 0.0 indicates the origin
         *     and 1.0 the full span away from the origin
         */
        @Override
        public float getAlignment(int axis) {
            return view.getAlignment(axis);
        }

        /**
         * Renders the view.
         *
         * @param g the graphics context
         * @param allocation the region to render into
         */
        public void paint(Graphics g, Shape allocation) {
            Rectangle alloc = allocation.getBounds();
            view.setSize(alloc.width, alloc.height);
            view.paint(g, allocation);
        }

        /**
         * Sets the view parent.
         *
         * @param parent the parent view
         */
        @Override
        public void setParent(View parent) {
            throw new Error("Can't set parent on root view");
        }


        /**
         * Provides a mapping from the document model coordinate space
         * to the coordinate space of the view mapped to it.
         *
         * @param p0 the position to convert >= 0
         * @param b0 the bias toward the previous character or the
         *  next character represented by p0, in case the
         *  position is a boundary of two views.
         * @param p1 the position to convert >= 0
         * @param b1 the bias toward the previous character or the
         *  next character represented by p1, in case the
         *  position is a boundary of two views.
         * @param a the allocated region to render into
         * @return the bounding box of the given position is returned
         * @exception BadLocationException  if the given position does
         *   not represent a valid location in the associated document
         * @exception IllegalArgumentException for an invalid bias argument
         * @see View#viewToModel
         */
        @Override
        public Shape modelToView(int p0, Position.Bias b0, int p1,
                                 Position.Bias b1, Shape a) throws BadLocationException {
            return view.modelToView(p0, b0, p1, b1, a);
        }
        public Shape modelToView(int pos, Shape a, Bias b) throws BadLocationException {
            return view.modelToView(pos, a, b);
        }
        /**
         * Provides a mapping from the view coordinate space to the logical
         * coordinate space of the model.
         *
         * @param x x coordinate of the view location to convert
         * @param y y coordinate of the view location to convert
         * @param a the allocated region to render into
         * @return the location within the model that best represents the
         *    given point in the view
         */
        public int viewToModel(float x, float y, Shape a, Position.Bias[] bias) {
            return view.viewToModel(x, y, a, bias);
        }

        /**
         * Returns the document model underlying the view.
         *
         * @return the model
         */
        @Override
        public Document getDocument() {
            return view.getDocument();
        }

        /**
         * Returns the starting offset into the model for this view.
         *
         * @return the starting offset
         */
        @Override
        public int getStartOffset() {
            return view.getStartOffset();
        }

        /**
         * Returns the ending offset into the model for this view.
         *
         * @return the ending offset
         */
        @Override
        public int getEndOffset() {
            return view.getEndOffset();
        }

        /**
         * Gets the element that this view is mapped to.
         *
         * @return the view
         */
        @Override
        public Element getElement() {
            return view.getElement();
        }

        /**
         * Sets the view size.
         *
         * @param width the width
         * @param height the height
         */
        @Override
        public void setSize(float width, float height) {
            this.width = (int) width;
            view.setSize(width, height);
        }

        /**
         * Fetches the container hosting the view.  This is useful for
         * things like scheduling a repaint, finding out the host
         * components font, etc.  The default implementation
         * of this is to forward the query to the parent view.
         *
         * @return the container
         */
        @Override
        public Container getContainer() {
            return host;
        }

        /**
         * Fetches the factory to be used for building the
         * various view fragments that make up the view that
         * represents the model.  This is what determines
         * how the model will be represented.  This is implemented
         * to fetch the factory provided by the associated
         * EditorKit.
         *
         * @return the factory
         */
        @Override
        public ViewFactory getViewFactory() {
            return factory;
        }

        private int width;
        private View view;
        private ViewFactory factory;
        private JComponent host;



    }

    class XLabelImpl extends XLabel {
        boolean mPreloadImages;

        public void setPreloadImages(boolean value) {
            mPreloadImages = value;
        }

        public boolean getPreloadImages() {
            return mPreloadImages;
        }

        @Override
        public void setText(String text) {
            if (text != null && text.startsWith("<html>")) {
                if (mPreloadImages) {
                    if (getEditorKit() != mSyncHtmlKit) {
                        setEditorKit(mSyncHtmlKit);
                    }
                } else {
                    if (getEditorKit() != mHtmlKit) {
                        setEditorKit(mHtmlKit);
                    }
                }
            } else if (text != null && text.length() > 0) {
                setContentType("text/plain");
            }
            super.setText(text);
        }

        @Override
        public void setFocusable(boolean value) {
            if (value) {
                setCaret(new DefaultCaret());
            } else {
                InvisibleCaret c = new InvisibleCaret();
                c.setUpdatePolicy(InvisibleCaret.NEVER_UPDATE);
                setCaret(c);
            }
            super.setFocusable(value);
        }

        public XLabelImpl() {
            setEditorKit(mHtmlKit);
            setEditable(false);
            setFocusable(false);
            setMargin(new Insets(0, 0, 0, 0));
            setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
            putClientProperty(HONOR_DISPLAY_PROPERTIES,
                              Boolean.TRUE);
            addHyperlinkListener(new HyperlinkListener() {
                    public void hyperlinkUpdate(HyperlinkEvent e) {
                        HyperlinkEvent.EventType type = e.getEventType();
                        if (type == HyperlinkEvent.EventType.ACTIVATED) {
                            String link = e.getDescription();
                            if (link.startsWith("object:")) {
                                //TODO what is this????
                                //mModule.call(link);
                            } else {
                                JApplet applet = getApplet();
                                if (applet != null) {
                                    try {
                                        applet.getAppletContext().showDocument(new URL(link));
                                    } catch (MalformedURLException exc) {
                                        exc.printStackTrace();
                                    }
                                }
                            }
                        } else if (type == HyperlinkEvent.EventType.ENTERED) {
                        }
                    }

                });
            ToolTipManager.sharedInstance().registerComponent(this);
        }

    }

    static class InvisibleCaret extends DefaultCaret {

        @Override
        public void install(JTextComponent comp) {
            // nothing
        }

        @Override
        public void paint(Graphics g) {
            // nothing
        }
    }

    HTMLEditorKit mSyncHtmlKit;

    public class XSimpleLabel extends JLabel {

        String mText;
        HTMLDocument mDoc;
        boolean mSync;
        LazyView mView;
        boolean mLazy;

        public XSimpleLabel() {
        }

        public XSimpleLabel(boolean sync) {
            mSync = sync;
        }

        public XSimpleLabel(boolean sync, boolean lazy) {
            mSync = sync;
            mLazy = lazy;
        }

        @Override
        public void setText(final String t) {
            if (t != null) {
                if (t.equals(mText)) {
                    return;
                }
                if (t.startsWith("<html>")) {
                    mText = t;
                    if (mView == null) {
                        mView = new LazyView(mLazy) {
                                public View getDelegate() {
                                    HTMLEditorKit kit = mHtmlKit;
                                    if (mDoc == null) {
                                        mDoc = (HTMLDocument)kit.createDefaultDocument();
                                        mDoc.setPreservesUnknownTags(false);
                                        mDoc.putProperty("imageCache", getImageCache(mSync));
                                        mDoc.setBase(FILE_BASE);
                                    }
                                    HTMLDocument doc = mDoc;
                                    doc.getStyleSheet().addRule(
                                                                displayPropertiesToCSS(getFont(),getForeground()));
                                    Reader r = new StringReader(mText);
                                    try {
                                        doc.remove(0, doc.getLength());
                                        kit.read(r, doc, 0);
                                    } catch (Throwable e) {
                                    }
                                    ViewFactory f = kit.getViewFactory();
                                    View hview = f.create(doc.getDefaultRootElement());
                                    View v = new Renderer(XSimpleLabel.this, f, hview);
                                    return v;
                                }
                            };
                    }
                    putClientProperty("html", mView);
                    mView.invalidate();
                    revalidate();
                    repaint();
                } else {
                    mText = null;
                    putClientProperty("html", null);
                    super.setText(t);
                }
            } else {
                mText = null;
                putClientProperty("html", null);
                super.setText(t);
            }
        }

        @Override
        public String getText() {
            if (getClientProperty("html") != null) {
                return mText;
            }
            return super.getText();
        }
    }
    public XLabel createLabel() {
        return new XLabelImpl();
    }

    HashTableWrapper mImageCache;
    HashTableWrapper mSyncImageCache;
    JApplet mApplet;
    Map mResourceMap = new HashMap();

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
    public JLabel createSimpleLabel(){
         return new XSimpleLabel();
    }
    public Object getImageCache(boolean sync) {
        return sync ? mSyncImageCache : mImageCache;
    }

    public Object createImageCache() {
        return new Hashtable() {
            @Override
            @SuppressWarnings("unchecked")
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
            @Override
            @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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
            TabbedPaneUI ui = mTabbedPane.getUI();
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

    JDesktopPane mDesk = new XInternalFrame.DummyDesktop();

    public XInternalFrame createInternalFrame() {
        return new XInternalFrame(mDesk);
    }


     JFileChooser mFileChooser;

     public JFileChooser createFileChooser() {
        if (mFileChooser == null) {
            mFileChooser = new JFileChooser();
            try {
                mFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mFileChooser;
    }

    class XListCellRenderer extends DefaultListCellRenderer {

        XSimpleLabel je;
        String mToolTip;


        public Component getListCellRendererComponent(JList list, String value,
                                                      int index,
                                                      boolean sel,
                                                      boolean hasFocus,
                                                      String tooltip) {
            JLabel proto = (JLabel) super.getListCellRendererComponent(
                    list, "", index, sel, hasFocus);
            if (je == null) {
                je = new XSimpleLabel(true, false) {
                        @Override
                        public String getToolTipText() {
                            return mToolTip;
                        }
                        @Override
                        public void repaint(long tm, int x, int y, int w, int h)
                        {
                            // nothing
                        }
                        @Override
                        public void validate() {
                            // nothing
                        }
                        @Override
                        public void revalidate() {
                            // nothing
                        }
                    };
                je.setOpaque(true);
            }
            je.setText("");
            je.setFont(proto.getFont());
            je.setBackground(proto.getBackground());
            je.setForeground(proto.getForeground());
            je.setBorder(proto.getBorder());
            mToolTip = tooltip;
            je.setText(value);
            return je;
        }
    }

     XListCellRenderer mListCellRenderer = new XListCellRenderer();
     public Component getListCellRendererComponent(JList list, String value,
                                                  int index, boolean isSelected,
                                                  boolean cellHasFocus,
                                                  String tooltip) {
            return mListCellRenderer.getListCellRendererComponent(list, value,
                                                  index,
                                                  isSelected, cellHasFocus, 
                                                  tooltip);
     }

    public void addTransferHandler(final JComponent comp, final Class dropType, 
            final ValueGetter exporter, final ValueSetter importer, 
            final ValueAcceptor acceptor, final VisualRepresentation rep) {
        ValueTransferHandler handler = new ValueTransferHandler(comp, dropType) {

                Object dragValue = null;

                public boolean canImport(Object valueList) {
                    System.out.println("can import "+comp);
                    dragValue = valueList;
                    boolean result = acceptor == null ? importer != null : acceptor.accept(valueList);
                    return result;
                }

                protected void notifyDragEnter() {
                    System.out.println("notify drag enter "+comp);
                    if (acceptor != null) {
                        acceptor.dragEnter(null);
                    }
                }

                protected void notifyDragOver() {
                }

                protected void notifyDragExit() {
                    System.out.println("notify drag exit");
                    if (acceptor != null) {
                        acceptor.dragExit(null);
                    }
                }

                protected void notifyDrop(DropTargetDropEvent e) {
                    if (importer != null) {
                        //importData(comp, e.getTransferable());
                    }
                }

                protected Object exportValue(JComponent c) {
                    Object result = exporter == null ? null : exporter.get();
                    return result;
                }

                protected void importValue(JComponent c, Object value) {
                    System.out.println("import value"+c);
                    if (importer != null) {
                        importer.set(value);
                    }
                }

                protected void cleanup(JComponent c, boolean remove) {
                    //System.out.println("cleanup called");
                    Object value = dragValue;
                    dragValue = null;
                    if (remove) {
                    }
                }

            @Override
                public Icon getVisualRepresentation(Transferable tr) {
                    if (rep != null) {
                        Object valueList = ((ValueSelection)tr).getValue();
                        return rep.getIcon(valueList);
                    }
                    return null;
                }

            @Override
                public Component getVisualComponent(Transferable tr) {
                    if (rep != null) {
                        Object valueList = ((ValueSelection)tr).getValue();
                        return rep.getComponent(valueList);
                    }
                    return null;
                }


            };
        TransferHandler.setTransferHandler(comp, handler);
        handler.installDropTargetListener(comp);
    }

    public JSpinner createSpinner() {

        final JSpinner spin = new JSpinner(new BigDecimalSpinnerModel()) {

            @Override
                protected JComponent createEditor(SpinnerModel model) {
                    return new NumberEditor(this);
                }
                /**
                 * This subclass of javax.swing.NumberFormatter maps the minimum/maximum
                 * properties to a SpinnerNumberModel and initializes the valueClass
                 * of the NumberFormatter to match the type of the initial models value.
                 */
                class NumberEditorFormatter extends NumberFormatter {
                    private final BigDecimalSpinnerModel model;

                    NumberEditorFormatter(BigDecimalSpinnerModel model, NumberFormat format) {
                        super(format);
                        this.model = model;
                        setValueClass(model.getValue().getClass());
                    }

                @Override
                    public void setMinimum(Comparable min) {
                        model.setMinimum(min);
                    }

                @Override
                    public Comparable getMinimum() {
                        return  model.getMinimum();
                    }

                @Override
                    public void setMaximum(Comparable max) {
                        model.setMaximum(max);
                    }

                @Override
                    public Comparable getMaximum() {
                        return model.getMaximum();
                    }
                }


                class NumberEditor extends DefaultEditor
                {
                   
                    /**
                     * Construct a <code>JSpinner</code> editor that supports displaying
                     * and editing the value of a <code>SpinnerNumberModel</code>
                     * with a <code>JFormattedTextField</code>.  <code>This</code>
                     * <code>NumberEditor</code> becomes both a <code>ChangeListener</code>
                     * on the spinner and a <code>PropertyChangeListener</code>
                     * on the new <code>JFormattedTextField</code>.
                     *
                     * @param spinner the spinner whose model <code>this</code> editor will monitor
                     * @exception IllegalArgumentException if the spinners model is not
                     *     an instance of <code>SpinnerNumberModel</code>
                     *
                     * @see #getModel
                     * @see #getFormat
                     * @see SpinnerNumberModel
                     */
                    public NumberEditor(JSpinner spinner) {
                        this(spinner, "#");
                    }

                    /**
                     * Construct a <code>JSpinner</code> editor that supports displaying
                     * and editing the value of a <code>SpinnerNumberModel</code>
                     * with a <code>JFormattedTextField</code>.  <code>This</code>
                     * <code>NumberEditor</code> becomes both a <code>ChangeListener</code>
                     * on the spinner and a <code>PropertyChangeListener</code>
                     * on the new <code>JFormattedTextField</code>.
                     *
                     * @param spinner the spinner whose model <code>this</code> editor will monitor
                     * @param decimalFormatPattern the initial pattern for the
                     *     <code>DecimalFormat</code> object that's used to display
                     *     and parse the value of the text field.
                     * @exception IllegalArgumentException if the spinners model is not
                     *     an instance of <code>SpinnerNumberModel</code> or if
                     *     <code>decimalFormatPattern</code> is not a legal
                     *     argument to <code>DecimalFormat</code>
                     *
                     * @see #getTextField
                     * @see SpinnerNumberModel
                     * @see java.text.DecimalFormat
                     */
                    public NumberEditor(JSpinner spinner, String decimalFormatPattern) {
                        this(spinner, new DecimalFormat(decimalFormatPattern));
                    }


                    /**
                     * Construct a <code>JSpinner</code> editor that supports displaying
                     * and editing the value of a <code>SpinnerNumberModel</code>
                     * with a <code>JFormattedTextField</code>.  <code>This</code>
                     * <code>NumberEditor</code> becomes both a <code>ChangeListener</code>
                     * on the spinner and a <code>PropertyChangeListener</code>
                     * on the new <code>JFormattedTextField</code>.
                     *
                     * @param spinner the spinner whose model <code>this</code> editor will monitor
                     * @param decimalFormatPattern the initial pattern for the
                     *     <code>DecimalFormat</code> object that's used to display
                     *     and parse the value of the text field.
                     * @exception IllegalArgumentException if the spinners model is not
                     *     an instance of <code>SpinnerNumberModel</code>
                     *
                     * @see #getTextField
                     * @see SpinnerNumberModel
                     * @see java.text.DecimalFormat
                     */
                    private NumberEditor(JSpinner spinner, DecimalFormat format) {
                        super(spinner);
                        if (!(spinner.getModel() instanceof BigDecimalSpinnerModel)) {
                            throw new IllegalArgumentException(
                                                               "model not a BigDecimalSpinnerModel");
                        }

                        BigDecimalSpinnerModel model = (BigDecimalSpinnerModel)spinner.getModel();
                        NumberFormatter formatter = new NumberEditorFormatter(model,
                                                                              format);
                        DefaultFormatterFactory factory = new DefaultFormatterFactory(
                                                                                      formatter);
                        JFormattedTextField ftf = getTextField();
                        ftf.setEditable(true);
                        ftf.setFormatterFactory(factory);
                        ftf.setHorizontalAlignment(JTextField.RIGHT);

                        /* TBD - initializing the column width of the text field
                         * is imprecise and doing it here is tricky because
                         * the developer may configure the formatter later.
                         */
                        try {
                            String maxString = formatter.valueToString(model.getMinimum());
                            String minString = formatter.valueToString(model.getMaximum());
                            ftf.setColumns(Math.max(maxString.length(),
                                                    minString.length()));
                        }
                        catch (ParseException e) {
                            // TBD should throw a chained error here
                        }

                    }


                    /**
                     * Returns the <code>java.text.DecimalFormat</code> object the
                     * <code>JFormattedTextField</code> uses to parse and format
                     * numbers.
                     *
                     * @return the value of <code>getTextField().getFormatter().getFormat()</code>.
                     * @see #getTextField
                     * @see java.text.DecimalFormat
                     */
                    public DecimalFormat getFormat() {
                        return (DecimalFormat)((NumberFormatter)(getTextField().getFormatter())).getFormat();
                    }


                    /**
                     * Return our spinner ancestor's <code>SpinnerNumberModel</code>.
                     *
                     * @return <code>getSpinner().getModel()</code>
                     * @see #getSpinner
                     * @see #getTextField
                     */
                    public BigDecimalSpinnerModel getModel() {
                        return (BigDecimalSpinnerModel)(getSpinner().getModel());
                    }
                }
            };
        // total hack...
        JTextField dummy = new JTextField();
        dummy.setColumns(10);
        spin.setPreferredSize(new Dimension(dummy.getPreferredSize().width,
                                            spin.getPreferredSize().height));
        return spin;
    }

    public class XToggleButtonImpl extends XToggleButton {

        String mxText;
        HTMLDocument mDoc;

        public XToggleButtonImpl() {
        }

        @Override
        public void setText(String t) {
            if (t != null) {
                if (t.equals(mxText)) {
                    return;
                }
                mxText = t;
                if (t.startsWith("<html>")) {
                    HTMLEditorKit kit = mHtmlKit;
                    if (mDoc == null) {
                        mDoc = (HTMLDocument)kit.createDefaultDocument();
                        mDoc.setPreservesUnknownTags(false);
                        mDoc.setBase(FILE_BASE);
                    }
                    HTMLDocument doc = mDoc;
                    doc.setPreservesUnknownTags(false);
                    doc.getStyleSheet().addRule(
                             displayPropertiesToCSS(getFont(),getForeground()));
                    Object base = getClientProperty("html.base");
                    if (base instanceof URL) {
                        doc.setBase((URL)base);
                    }
                    Reader r = new StringReader(t);
                    try {
                        doc.remove(0, doc.getLength());
                        kit.read(r, doc, 0);
                    } catch (Throwable e) {
                    }
                    ViewFactory f = kit.getViewFactory();
                    View hview = f.create(doc.getDefaultRootElement());
                    View v = new Renderer(this, f, hview);
                    putClientProperty("html", v);
                    revalidate();
                    repaint();
                } else {
                    mxText = null;
                    super.setText(t);
                }
            } else {
                mxText = null;
                super.setText(t);
            }
        }

        @Override
        public String getText() {
            if (getClientProperty("html") != null) {
                return mxText;
            }
            return super.getText();
        }
    }


    public XToggleButton createToggleButton() {
        return new XToggleButtonImpl();
    }

    public JTable createTable() {
        return new XTable();
    }

    public JTree createTree() {
        JTree result = new JTree();
        result.setCellRenderer(new FXTreeCellRenderer());
        ToolTipManager.sharedInstance().registerComponent(result);
        return result;

    }
    InputStream getInputStream(final URL u, final ImageDownloadObserver observer) throws IOException {
        if (!"http".equals(u.getProtocol())) {
            return u.openStream();
        }
        return new InputStream() {

                InputStream delegate;

                public int read() throws  IOException {
                    if (delegate == null) {
                        delegate = getDelegate();
                    }
                    return delegate.read();
                }
            @Override
                public int read(byte[] bytes, int start, int len) throws IOException {
                    if (delegate == null) {
                        delegate = getDelegate();
                    }
                    return delegate.read(bytes, start, len);
                }

            @Override
                public void close() throws IOException {
                    if (delegate != null) {
                        delegate.close();
                        delegate = null;
                    }
                }

                InputStream getDelegate() throws IOException {
                    String cacheName = toCacheName(u);
                    File cacheFile = new File(cacheDir, cacheName);
                    return download(u, cacheFile, observer);
                }

            };
    }
    
    @SuppressWarnings("unchecked")
    InputStream download(final URL u, final File cache, final ImageDownloadObserver observer) throws IOException {
        String contentEncoding = (String)fileCache.get(u.toString());
        boolean download = contentEncoding == null || !cache.exists();
        if (download) {
            if (!"http".equals(u.getProtocol())) {
                return u.openStream();
            }
            //System.out.println("download requested...");
            int totalRead = 0;
            int totalSize = 0;
            String etag = null;
            String lastModified = null;
            if (cache.exists()) {
                File cacheRec = new File(cache.toString() + "-cache.rec");
                if (cacheRec.exists()) {
                    BufferedReader reader = new BufferedReader(new FileReader(cacheRec));
                    etag = reader.readLine();
                    lastModified = reader.readLine();
                    contentEncoding = reader.readLine();
                    reader.close();
                }
            }
            final HttpURLConnection c =
                (HttpURLConnection)u.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            c.setRequestProperty("accept-encoding",
                                 "image/png,image/gif,image,jpeg");
            if (etag != null) {
                c.addRequestProperty("If-None-Match", etag);
            }
            if (lastModified != null) {
                c.addRequestProperty("If-Modified-Since", lastModified);
            }
            try {
                //System.out.println("connecting to: " + u);
                c.connect();
                download = c.getResponseCode() != HttpURLConnection.HTTP_NOT_MODIFIED;
                //System.out.println("connecting to: " + u + "...done");
            } catch (IOException e) {
                if (!cache.exists()) {
                    //mNotFound.add(urlStr);
                    throw e;
                }
                download = false;
            }
            if (download) {
                    if (cache.exists()) {
                    cache.delete();
                }
                return new InputStream() {
                        int total = c.getContentLength();
                        String contentEncoding = c.getContentEncoding();
                        final InputStream is = c.getInputStream();
                        ByteArrayInputStream tee;
                        int totalRead;
                        {
                            observer.contentEncoding(contentEncoding);
                            notifyProgress(0, total);
                            byte[] buf = new byte[8192];
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            int count;
                            while ((count = is.read(buf, 0, buf.length)) != -1) {
                                notifyProgress(count);
                                baos.write(buf, 0, count);
                            }
                            byte[] bytes = baos.toByteArray();
                            notifyComplete(bytes);
                            tee = new ByteArrayInputStream(bytes);
                        }
                        public int read() throws IOException {
                            return tee.read();
                        }

                    @Override
                        public int read(byte[] bytes, int start, int len) throws IOException {
                            return tee.read(bytes, start, len);
                        }

                    @Override
                        public void close() throws IOException {
                        }

                        void notifyProgress(int lastRead) {
                            totalRead += lastRead;
                            notifyProgress(totalRead, total);
                        }

                        void notifyProgress(int totalRead, int ofTotal) {
                            observer.progress(totalRead, ofTotal);
                        }

                    @SuppressWarnings("unchecked")
                        void notifyComplete(byte[] bytes) throws IOException {
                            FileOutputStream fos = new FileOutputStream(cache);
                            fos.write(bytes, 0, bytes.length);
                            fos.close();
                            String lastModified = c.getHeaderField("Last-Modified");
                            String etag = c.getHeaderField("ETag");
                            java.io.PrintWriter pw = new java.io.PrintWriter(new FileWriter(new File(cache.toString()+"-cache.rec")));
                            pw.println(etag);
                            pw.println(lastModified);
                            pw.println(contentEncoding);
                            pw.close();
                            fileCache.put(u.toString(), contentEncoding == null ? "" : contentEncoding);
                            notifyProgress(totalRead, totalRead);
                        }
                    };
            } else {
                fileCache.put(u.toString(), contentEncoding == null ? "" : contentEncoding);
            }
        }
        int size = (int)cache.length();
        //System.out.println("returning image from cache: " + u);
        observer.contentEncoding(contentEncoding);
        observer.progress(size, size);
        return new BufferedInputStream(new FileInputStream(cache));
    }

    String toCacheName(URL url) {
        try {
            return toCacheName(url.toURI().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    String toCacheName(String url) {
        String name = url;
        name = name.replace('\\', '/');
        name = name.replaceAll(":", "%3a");
        name = name.replaceAll("/", "%2f");
        name = name.replaceAll("[.]", "%2e");
        name = name.replaceAll("[&]", "%26");
        name = name.replaceAll("[=]", "%3d");
        name = name.replaceAll("[?]", "%3f");
        return name;
    }

    @SuppressWarnings("unchecked")
    static Map fileCache = Collections.synchronizedMap(new HashMap());

    File cacheDir = new File(System.getProperty("user.home")+"/.javafxcache/images");
    {
        try {
            cacheDir.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
