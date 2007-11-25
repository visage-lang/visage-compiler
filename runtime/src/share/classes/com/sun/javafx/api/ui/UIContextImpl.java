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

import com.sun.javafx.api.ui.UIContextImpl.InvisibleCaret;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.plaf.TabbedPaneUI;
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
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.ImageView;

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
        comp.setTransferHandler(handler);
        handler.installDropTargetListener(comp);
    }




}
