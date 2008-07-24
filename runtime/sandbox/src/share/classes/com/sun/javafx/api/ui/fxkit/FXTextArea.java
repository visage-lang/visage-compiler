/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.api.ui.fxkit;

import java.awt.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.plaf.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class FXTextArea extends JEditorPane {

    
    public interface LineAnnotation {
        public int getLine();
        public int getColumn();
        public int getLength();
        public String getToolTipText();
        public void setBounds(int x, int y, int w, int h);
        public Component getComponent();
        public void addChangeListener(ChangeListener l);
        public void removeChangeListener(ChangeListener l);
    }

    List mAnnotations = new ArrayList();
    ChangeListener mChangeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                //System.out.println("state change....");
                updateAnnotations();
            }
        };

    public void addAnnotation(int index, LineAnnotation  a) {
        mAnnotations.add(index, a);
        updateAnnotation(a);
        a.addChangeListener(mChangeListener);
        repaint();
    }

    public void removeAnnotation(int index) {
        LineAnnotation a = (LineAnnotation)mAnnotations.remove(index);
        a.removeChangeListener(mChangeListener);
        repaint();
    }

    public LineAnnotation getAnnotation(int index) {
        return (LineAnnotation)mAnnotations.get(index);
    }

    public void setAnnotation(int index, LineAnnotation a) {
        LineAnnotation old = (LineAnnotation)mAnnotations.set(index, a);
        old.removeChangeListener(mChangeListener);
        updateAnnotation(a);
        a.addChangeListener(mChangeListener);
        repaint();
    }

    public int countAnnotations() {
        return mAnnotations.size();
    }

    void updateAnnotations() {
        if (mAnnotations.size() > 0) {
            LineAnnotation[] as = new LineAnnotation[mAnnotations.size()];
            mAnnotations.toArray(as);
            for (int i = 0; i < as.length; i++) {
                updateAnnotation(as[i]);
            }
        }
    }

    void updateAnnotation(LineAnnotation a) {
        int line = a.getLine()-1;
        int column = a.getColumn()-1;
        int length = a.getLength();
        try {
            int off = getLineStartOffset(line);
            Rectangle r1 = modelToView(off+column);
            Rectangle r2 = modelToView(off+column+length);
            if (r1 != null && r2 != null) {
                a.setBounds(r1.x, r1.y, r2.x - r1.x+1, r1.height);
                a.getComponent().setBounds(r1.x, r1.y, r2.x - r1.x+1, r1.height);
            }
        } catch (BadLocationException e) {
            // ignore
        }
    }

    void paintAnnotations(Graphics g) {
        Rectangle clip = g.getClipBounds();
        if (mAnnotations.size() > 0) {
            LineAnnotation[] as = new LineAnnotation[mAnnotations.size()];
            mAnnotations.toArray(as);
            CellRendererPane r = new CellRendererPane();
            for (int i = 0; i < as.length; i++) {
                LineAnnotation a = as[i];
                int line = a.getLine()-1;
                int column = a.getColumn()-1;
                int length = a.getLength();
                try {
                    int off = getLineStartOffset(line);
                    Rectangle r1 = modelToView(off+column);
                    Rectangle r2 = modelToView(off+column+length);
                    r1.width = r2.x - r1.x + r2.width;
                    if (r1.intersects(clip)) {
                        Component c = a.getComponent();
                        r.paintComponent(g, c, this, 
                                         r1.x,
                                         r1.y,
                                         r1.width,
                                         r1.height);
                    }
                } catch (BadLocationException e) {
                    // ignor
                }
            }        
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintAnnotations(g);
    }


    /**
     * Constructs a new TextArea.  A default model is set, the initial string
     * is null, and rows/columns are set to 0.
     */
    public FXTextArea() {
        this(null, null, 0, 0);
    }

    /**
     * Constructs a new TextArea with the specified text displayed.
     * A default model is created and rows/columns are set to 0.
     *
     * @param text the text to be displayed, or null
     */
    public FXTextArea(String text) {
        this(null, text, 0, 0);
    }

    /**
     * Constructs a new empty TextArea with the specified number of
     * rows and columns.  A default model is created, and the initial
     * string is null.
     *
     * @param rows the number of rows >= 0
     * @param columns the number of columns >= 0
     * @exception IllegalArgumentException if the rows or columns
     *  arguments are negative.
     */
    public FXTextArea(int rows, int columns) {
        this(null, null, rows, columns);
    }

    /**
     * Constructs a new TextArea with the specified text and number
     * of rows and columns.  A default model is created.
     *
     * @param text the text to be displayed, or null
     * @param rows the number of rows >= 0
     * @param columns the number of columns >= 0
     * @exception IllegalArgumentException if the rows or columns
     *  arguments are negative.
     */
    public FXTextArea(String text, int rows, int columns) {
        this(null, text, rows, columns);
    }

    /**
     * Constructs a new FXTextArea with the given document model, and defaults
     * for all of the other arguments (null, 0, 0).
     *
     * @param doc  the model to use
     */
    public FXTextArea(Document doc) {
        this(doc, null, 0, 0);
    }

    /**
     * Constructs a new FXTextArea with the specified number of rows
     * and columns, and the given model.  All of the constructors
     * feed through this constructor.
     *
     * @param doc the model to use, or create a default one if null
     * @param text the text to be displayed, null if none
     * @param rows the number of rows >= 0
     * @param columns the number of columns >= 0
     * @exception IllegalArgumentException if the rows or columns
     *  arguments are negative.
     */
    public FXTextArea(Document doc, String text, int rows, int columns) {
        super();
        setEditorKit(new DefaultEditorKit());
        this.rows = rows;
        this.columns = columns;
        if (doc == null) {
            doc = createDefaultModel();
        }
        setDocument(doc);
        if (text != null) {
            setText(text);
            select(0, 0);
        }
	if (rows < 0) {
	    throw new IllegalArgumentException("rows: " + rows);
	}
	if (columns < 0) {
	    throw new IllegalArgumentException("columns: " + columns);
	}
        /*
        LookAndFeel.installProperty(this,
                                    "focusTraversalKeysForward", 
                                    JComponent.
                                    getManagingFocusForwardTraversalKeys());
        LookAndFeel.installProperty(this,
                                    "focusTraversalKeysBackward", 
                                    JComponent.
                                    getManagingFocusBackwardTraversalKeys());
        */
    }


    /**
     * Creates the default implementation of the model
     * to be used at construction if one isn't explicitly 
     * given.  A new instance of PlainDocument is returned.
     *
     * @return the default document model
     */
    protected Document createDefaultModel() {
        return new PlainDocument();
    }

    /**
     * Sets the number of characters to expand tabs to.
     * This will be multiplied by the maximum advance for
     * variable width fonts.  A PropertyChange event ("tabSize") is fired
     * when the tab size changes.
     *
     * @param size number of characters to expand to
     * @see #getTabSize
     * @beaninfo
     *   preferred: true
     *       bound: true
     * description: the number of characters to expand tabs to
     */
    public void setTabSize(int size) {
        Document doc = getDocument();
        if (doc != null) {
            int old = getTabSize();
            doc.putProperty(PlainDocument.tabSizeAttribute, new Integer(size));
            firePropertyChange("tabSize", old, size);
        }
    }


    /**
     * Gets the number of characters used to expand tabs.  If the document is
     * null or doesn't have a tab setting, return a default of 8.
     *
     * @return the number of characters
     */
    public int getTabSize() {
        int size = 8;
        Document doc = getDocument();
        if (doc != null) {
            Integer i = (Integer) doc.getProperty(PlainDocument.tabSizeAttribute);
            if (i != null) {
                size = i.intValue();
            }
        }
        return size;
    }

    /**
     * Sets the line-wrapping policy of the text area.  If set
     * to true the lines will be wrapped if they are too long
     * to fit within the allocated width.  If set to false,
     * the lines will always be unwrapped.  A <code>PropertyChange</code>
     * event ("lineWrap") is fired when the policy is changed.
     * By default this property is false.
     *
     * @param wrap indicates if lines should be wrapped
     * @see #getLineWrap
     * @beaninfo
     *   preferred: true
     *       bound: true
     * description: should lines be wrapped
     */
    public void setLineWrap(boolean wrap) {
        boolean old = this.wrap;
        this.wrap = wrap;
        firePropertyChange("lineWrap", old, wrap);
    }

    /**
     * Gets the line-wrapping policy of the text area.  If set
     * to true the lines will be wrapped if they are too long
     * to fit within the allocated width.  If set to false,
     * the lines will always be unwrapped.
     *
     * @return if lines will be wrapped
     */
    public boolean getLineWrap() {
        return wrap;
    }

    /**
     * Sets the style of wrapping used if the text area is wrapping
     * lines.  If set to true the lines will be wrapped at word
     * boundaries (whitespace) if they are too long
     * to fit within the allocated width.  If set to false,
     * the lines will be wrapped at character boundaries.
     * By default this property is false.
     *
     * @param word indicates if word boundaries should be used
     *   for line wrapping
     * @see #getWrapStyleWord
     * @beaninfo
     *   preferred: false
     *       bound: true
     * description: should wrapping occur at word boundaries
     */
    public void setWrapStyleWord(boolean word) {
        boolean old = this.word;
        this.word = word;
        firePropertyChange("wrapStyleWord", old, word);
    }

    /**
     * Gets the style of wrapping used if the text area is wrapping
     * lines.  If set to true the lines will be wrapped at word
     * boundaries (ie whitespace) if they are too long
     * to fit within the allocated width.  If set to false,
     * the lines will be wrapped at character boundaries.
     *
     * @return if the wrap style should be word boundaries
     *  instead of character boundaries
     * @see #setWrapStyleWord
     */
    public boolean getWrapStyleWord() {
        return word;
    }

    /**
     * Translates an offset into the components text to a 
     * line number.
     *
     * @param offset the offset >= 0
     * @return the line number >= 0
     * @exception BadLocationException thrown if the offset is
     *   less than zero or greater than the document length.
     */
    public int getLineOfOffset(int offset) throws BadLocationException {
        Document doc = getDocument();
        if (offset < 0) {
            throw new BadLocationException("Can't translate offset to line", -1);
        } else if (offset > doc.getLength()) {
            throw new BadLocationException("Can't translate offset to line", doc.getLength()+1);
        } else {
            Element map = getDocument().getDefaultRootElement();
            return map.getElementIndex(offset);
        }
    }

    /**
     * Determines the number of lines contained in the area.
     *
     * @return the number of lines > 0
     */
    public int getLineCount() {
        Element map = getDocument().getDefaultRootElement();
        return map.getElementCount();
    }

    /**
     * Determines the offset of the start of the given line.
     *
     * @param line  the line number to translate >= 0
     * @return the offset >= 0
     * @exception BadLocationException thrown if the line is
     * less than zero or greater or equal to the number of
     * lines contained in the document (as reported by 
     * getLineCount).
     */
    public int getLineStartOffset(int line) throws BadLocationException {
        int lineCount = getLineCount();
        if (line < 0) {
            throw new BadLocationException("Negative line", -1);
        } else if (line >= lineCount) {
            throw new BadLocationException("No such line", getDocument().getLength()+1);
        } else {
            Element map = getDocument().getDefaultRootElement();
            Element lineElem = map.getElement(line);
            return lineElem.getStartOffset();
        }
    }

    /**
     * Determines the offset of the end of the given line.
     *
     * @param line  the line >= 0
     * @return the offset >= 0
     * @exception BadLocationException Thrown if the line is
     * less than zero or greater or equal to the number of
     * lines contained in the document (as reported by 
     * getLineCount).
     */
    public int getLineEndOffset(int line) throws BadLocationException {
        int lineCount = getLineCount();
        if (line < 0) {
            throw new BadLocationException("Negative line", -1);
        } else if (line >= lineCount) {
            throw new BadLocationException("No such line", getDocument().getLength()+1);
        } else {
            Element map = getDocument().getDefaultRootElement();
            Element lineElem = map.getElement(line);
            int endOffset = lineElem.getEndOffset();
            // hide the implicit break at the end of the document
            return ((line == lineCount - 1) ? (endOffset - 1) : endOffset);
        }
    }

    // --- java.awt.TextArea methods ---------------------------------

    /**
     * Inserts the specified text at the specified position.  Does nothing
     * if the model is null or if the text is null or empty.
     * <p>
     * This method is thread safe, although most Swing methods
     * are not. Please see 
     * <A HREF="http://java.sun.com/products/jfc/swingdoc-archive/threads.html">Threads
     * and Swing</A> for more information.     
     *
     * @param str the text to insert
     * @param pos the position at which to insert >= 0
     * @exception IllegalArgumentException  if pos is an
     *  invalid position in the model
     * @see TextComponent#setText
     * @see #replaceRange
     */
    public void insert(String str, int pos) {
        Document doc = getDocument();
        if (doc != null) {
            try {
                doc.insertString(pos, str, null);
            } catch (BadLocationException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    /**
     * Appends the given text to the end of the document.  Does nothing if
     * the model is null or the string is null or empty.
     * <p>
     * This method is thread safe, although most Swing methods
     * are not. Please see 
     * <A HREF="http://java.sun.com/products/jfc/swingdoc-archive/threads.html">Threads
     * and Swing</A> for more information.     
     *
     * @param str the text to insert
     * @see #insert
     */
    public void append(String str) {
        Document doc = getDocument();
        if (doc != null) {
            try {
                doc.insertString(doc.getLength(), str, null);
            } catch (BadLocationException e) {
            }
        }
    }

    /**
     * Replaces text from the indicated start to end position with the
     * new text specified.  Does nothing if the model is null.  Simply
     * does a delete if the new string is null or empty.
     * <p>
     * This method is thread safe, although most Swing methods
     * are not. Please see 
     * <A HREF="http://java.sun.com/products/jfc/swingdoc-archive/threads.html">Threads
     * and Swing</A> for more information.     
     *
     * @param str the text to use as the replacement
     * @param start the start position >= 0
     * @param end the end position >= start
     * @exception IllegalArgumentException  if part of the range is an
     *  invalid position in the model
     * @see #insert
     * @see #replaceRange
     */
    public void replaceRange(String str, int start, int end) {
        if (end < start) {
            throw new IllegalArgumentException("end before start");
        }
        Document doc = getDocument();
        if (doc != null) {
            try {
                if (doc instanceof AbstractDocument) {
                    ((AbstractDocument)doc).replace(start, end - start, str,
                                                    null);
                }
                else {
                    doc.remove(start, end - start);
                    doc.insertString(start, str, null);
                }
            } catch (BadLocationException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    /**
     * Returns the number of rows in the TextArea.
     *
     * @return the number of rows >= 0
     */
    public int getRows() {
        return rows;
    }

    /**
     * Sets the number of rows for this TextArea.  Calls invalidate() after
     * setting the new value.
     *
     * @param rows the number of rows >= 0
     * @exception IllegalArgumentException if rows is less than 0
     * @see #getRows
     * @beaninfo
     * description: the number of rows preferred for display
     */
    public void setRows(int rows) {
        int oldVal = this.rows;
        if (rows < 0) {
            throw new IllegalArgumentException("rows less than zero.");
        }
        if (rows != oldVal) {
            this.rows = rows;
            invalidate();
        }
    }

    /**
     * Defines the meaning of the height of a row.  This defaults to
     * the height of the font.
     *
     * @return the height >= 1
     */
    protected int getRowHeight() {
        if (rowHeight == 0) {
            FontMetrics metrics = getFontMetrics(getFont());
            rowHeight = metrics.getHeight();
        }
        return rowHeight;
    }

    /**
     * Returns the number of columns in the TextArea.
     *
     * @return number of columns >= 0
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Sets the number of columns for this TextArea.  Does an invalidate()
     * after setting the new value.
     *
     * @param columns the number of columns >= 0
     * @exception IllegalArgumentException if columns is less than 0
     * @see #getColumns
     * @beaninfo
     * description: the number of columns preferred for display
     */
    public void setColumns(int columns) {
        int oldVal = this.columns;
        if (columns < 0) {
            throw new IllegalArgumentException("columns less than zero.");
        }
        if (columns != oldVal) {
            this.columns = columns;
            invalidate();
        }
    }

    /**
     * Gets column width.
     * The meaning of what a column is can be considered a fairly weak
     * notion for some fonts.  This method is used to define the width
     * of a column.  By default this is defined to be the width of the
     * character <em>m</em> for the font used.  This method can be 
     * redefined to be some alternative amount.
     *
     * @return the column width >= 1
     */
    protected int getColumnWidth() {
        if (columnWidth == 0) {
            FontMetrics metrics = getFontMetrics(getFont());
            columnWidth = metrics.charWidth('m');
        }
        return columnWidth;
    }

    // --- Component methods -----------------------------------------

    /**
     * Returns the preferred size of the TextArea.  This is the
     * maximum of the size needed to display the text and the
     * size requested for the viewport.
     *
     * @return the size
     */
    @Override
    public Dimension getPreferredSize() {
	Dimension d = super.getPreferredSize();
        d = (d == null) ? new Dimension(400,400) : d;
        Insets insets = getInsets();

        if (columns != 0) {
            d.width = Math.max(d.width, columns * getColumnWidth() + 
                    insets.left + insets.right); 	
        }
	if (rows != 0) {
	    d.height = Math.max(d.height, rows * getRowHeight() +
                                insets.top + insets.bottom);
	}
	return d;
    }

    /**
     * Sets the current font.  This removes cached row height and column
     * width so the new font will be reflected, and calls revalidate().
     *
     * @param f the font to use as the current font
     */
    @Override
    public void setFont(Font f) {
        super.setFont(f);
        rowHeight = 0; 
        columnWidth = 0;
    }


    /**
     * Returns a string representation of this FXTextArea. This method 
     * is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between      
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this FXTextArea.
     */
    @Override
    protected String paramString() {
        String wrapString = (wrap ?
			     "true" : "false");
        String wordString = (word ?
			     "true" : "false");

        return super.paramString() +
        ",colums=" + columns +
        ",columWidth=" + columnWidth +
        ",rows=" + rows +
        ",rowHeight=" + rowHeight +
        ",word=" + wordString +
        ",wrap=" + wrapString;
    }

    // --- Scrollable methods ----------------------------------------

    /**
     * Returns true if a viewport should always force the width of this 
     * Scrollable to match the width of the viewport.  This is implemented
     * to return true if the line wrapping policy is true, and false
     * if lines are not being wrapped.
     * 
     * @return true if a viewport should force the Scrollables width
     * to match its own.
     */
    @Override
    public boolean getScrollableTracksViewportWidth() {
        return (wrap) ? true : super.getScrollableTracksViewportWidth();
    }

    /**
     * Returns the preferred size of the viewport if this component
     * is embedded in a JScrollPane.  This uses the desired column
     * and row settings if they have been set, otherwise the superclass
     * behavior is used.
     * 
     * @return The preferredSize of a JViewport whose view is this Scrollable.
     * @see JViewport#getPreferredSize
     */
    @Override
    public Dimension getPreferredScrollableViewportSize() {
        Dimension size = super.getPreferredScrollableViewportSize();

        size = (size == null) ? new Dimension(400,400) : size;
	if (columns !=0 || rows != 0) {
	    size.width = (columns == 0) ? size.width : columns * getColumnWidth();
	    size.height = (rows == 0) ? size.height : rows * getRowHeight();
	} else {
	    if (!wrap) {
		size.width = getPreferredSize().width;
	    }
	}
        return size;
    }

    /**
     * Components that display logical rows or columns should compute
     * the scroll increment that will completely expose one new row
     * or column, depending on the value of orientation.  This is implemented
     * to use the values returned by the <code>getRowHeight</code> and
     * <code>getColumnWidth</code> methods.
     * <p>
     * Scrolling containers, like JScrollPane, will use this method
     * each time the user requests a unit scroll.
     * 
     * @param visibleRect the view area visible within the viewport
     * @param orientation Either SwingConstants.VERTICAL or
     *   SwingConstants.HORIZONTAL.
     * @param direction Less than zero to scroll up/left,
     *   greater than zero for down/right.
     * @return The "unit" increment for scrolling in the specified direction
     * @exception IllegalArgumentException for an invalid orientation
     * @see JScrollBar#setUnitIncrement
     * @see #getRowHeight
     * @see #getColumnWidth
     */
    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        switch (orientation) {
        case SwingConstants.VERTICAL:
            return getRowHeight();
        case SwingConstants.HORIZONTAL:
            return getColumnWidth();
        default:
            throw new IllegalArgumentException("Invalid orientation: " + orientation);
        }
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        switch (orientation) {
        case SwingConstants.VERTICAL:
            return visibleRect.height;
        case SwingConstants.HORIZONTAL:
            return visibleRect.width;
        default:
            throw new IllegalArgumentException("Invalid orientation: " + orientation);
        }
    }

    @Override
    public String getToolTipText(MouseEvent me) {
        int x = me.getX();
        int y = me.getY();
        Iterator iter = mAnnotations.iterator();
        while (iter.hasNext()) {
            LineAnnotation an = (LineAnnotation)iter.next();
            try {
                int line = an.getLine()-1;
                int column = an.getColumn()-1;
                int length = an.getLength();
                int off = getLineStartOffset(line);
                Rectangle r1 = modelToView(off+column);
                Rectangle r2 = modelToView(off+column+length);
                r1.width = r2.width + r2.x - r1.x;
                if (r1.contains(x, y)) {
                    String tip = an.getToolTipText();
                    if (tip != null) {
                        return tip;
                    }
                }
            } catch (BadLocationException e) {
            }
        }
        return super.getToolTipText(me);
    }


    private int rows;
    private int columns;
    private int columnWidth;
    private int rowHeight;
    private boolean wrap;
    private boolean word;

}
