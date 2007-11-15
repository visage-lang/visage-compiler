/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.javafx.api.ui;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.text.*;

abstract public class LazyView extends View {

    View mDelegate;
    boolean mLazy;

    void get() {
        if (mDelegate == null) {
            mDelegate = getDelegate();
        }
    }

    abstract View getDelegate();

    public void invalidate() {
        mDelegate = null;
        if (!mLazy) {
            get();
        }
    }


    /**
     * Creates a new <code>View</code> object.
     *
     * @param elem the <code>Element</code> to represent
     */
    public LazyView() {
        super(null);
        mLazy = false;
    }

    public LazyView(boolean lazy) {
        super(null);
        mLazy = lazy;
    }

    /**
     * Returns the parent of the view.
     *
     * @return the parent, or <code>null</code> if none exists
     */
    public View getParent() {
        get();
        return mDelegate.getParent();
    }

    /**
     *  Returns a boolean that indicates whether
     *  the view is visible or not.  By default
     *  all views are visible.
     *
     *  @return always returns true
     */
    public boolean isVisible() {
        get();
	return mDelegate.isVisible();
    }

	
    /**
     * Determines the preferred span for this view along an
     * axis.
     *
     * @param axis may be either <code>View.X_AXIS</code> or
     *		<code>View.Y_AXIS</code>
     * @return   the span the view would like to be rendered into.
     *           Typically the view is told to render into the span
     *           that is returned, although there is no guarantee.  
     *           The parent may choose to resize or break the view
     * @see View#getPreferredSpan
     */
    public  float getPreferredSpan(int axis) {
        get();
        return mDelegate.getPreferredSpan(axis);
    }

    /**
     * Determines the minimum span for this view along an
     * axis.
     *
     * @param axis may be either <code>View.X_AXIS</code> or
     *		<code>View.Y_AXIS</code>
     * @return  the minimum span the view can be rendered into
     * @see View#getPreferredSpan
     */
    public float getMinimumSpan(int axis) {
        get();
        return mDelegate.getMinimumSpan(axis);
    }

    /**
     * Determines the maximum span for this view along an
     * axis.
     *
     * @param axis may be either <code>View.X_AXIS</code> or
     *		<code>View.Y_AXIS</code>
     * @return  the maximum span the view can be rendered into
     * @see View#getPreferredSpan
     */
    public float getMaximumSpan(int axis) {
        get();
        return mDelegate.getMaximumSpan(axis);
    }
	
    /**
     * Child views can call this on the parent to indicate that
     * the preference has changed and should be reconsidered
     * for layout.  By default this just propagates upward to 
     * the next parent.  The root view will call 
     * <code>revalidate</code> on the associated text component.
     *
     * @param child the child view
     * @param width true if the width preference has changed
     * @param height true if the height preference has changed
     * @see javax.swing.JComponent#revalidate
     */
    public void preferenceChanged(View child, boolean width, boolean height) {
        get();
        mDelegate.preferenceChanged(child, width, height);
    }

    /**
     * Determines the desired alignment for this view along an
     * axis.  The desired alignment is returned.  This should be
     * a value >= 0.0 and <= 1.0, where 0 indicates alignment at
     * the origin and 1.0 indicates alignment to the full span
     * away from the origin.  An alignment of 0.5 would be the
     * center of the view.
     *
     * @param axis may be either <code>View.X_AXIS</code> or
     *		<code>View.Y_AXIS</code>
     * @return the value 0.5
     */
    public float getAlignment(int axis) {
        get();
        return mDelegate.getAlignment(axis);
    }

    /**
     * Renders using the given rendering surface and area on that
     * surface.  The view may need to do layout and create child
     * views to enable itself to render into the given allocation.
     *
     * @param g the rendering surface to use
     * @param allocation the allocated region to render into
     * @see View#paint
     */
    public  void paint(Graphics g, Shape allocation) {
        get();
        mDelegate.paint(g, allocation);
    }

    /**
     * Establishes the parent view for this view.  This is
     * guaranteed to be called before any other methods if the
     * parent view is functioning properly.  This is also
     * the last method called, since it is called to indicate
     * the view has been removed from the hierarchy as 
     * well. When this method is called to set the parent to
     * null, this method does the same for each of its children,
     * propogating the notification that they have been
     * disconnected from the view tree. If this is
     * reimplemented, <code>super.setParent()</code> should
     * be called.
     *
     * @param parent the new parent, or <code>null</code> if the view is
     * 		being removed from a parent
     */
    public void setParent(View parent) {
        get();
        mDelegate.setParent(parent);
    }

    /** 
     * Returns the number of views in this view.  Since
     * the default is to not be a composite view this
     * returns 0.
     *
     * @return the number of views >= 0
     * @see View#getViewCount
     */
    public int getViewCount() {
        get();
        return mDelegate.getViewCount();
    }

    /** 
     * Gets the <i>n</i>th child view.  Since there are no
     * children by default, this returns <code>null</code>.
     *
     * @param n the number of the view to get, >= 0 && < getViewCount()
     * @return the view
     */
    public View getView(int n) {
        get();
        return mDelegate.getView(n);
    }


    /**
     * Removes all of the children.  This is a convenience
     * call to <code>replace</code>.
     *
     * @since 1.3
     */
    public void removeAll() {
        get();
        mDelegate.removeAll();
    }

    /**
     * Removes one of the children at the given position.
     * This is a convenience call to <code>replace</code>.
     * @since 1.3
     */
    public void remove(int i) {
        get();
	mDelegate.remove(i);
    }

    /**
     * Inserts a single child view.  This is a convenience 
     * call to <code>replace</code>.
     *
     * @param offs the offset of the view to insert before >= 0
     * @param v the view
     * @see #replace
     * @since 1.3
     */
    public void insert(int offs, View v) {
        get();
	mDelegate.insert(offs, v);
    }

    /**
     * Appends a single child view.  This is a convenience 
     * call to <code>replace</code>.
     *
     * @param v the view
     * @see #replace
     * @since 1.3
     */
    public void append(View v) {
        get();
	mDelegate.append(v);
    }

    /**
     * Replaces child views.  If there are no views to remove
     * this acts as an insert.  If there are no views to
     * add this acts as a remove.  Views being removed will
     * have the parent set to <code>null</code>, and the internal reference
     * to them removed so that they can be garbage collected.
     * This is implemented to do nothing, because by default
     * a view has no children.
     *
     * @param offset the starting index into the child views to insert
     *   the new views.  This should be a value >= 0 and <= getViewCount
     * @param length the number of existing child views to remove
     *   This should be a value >= 0 and <= (getViewCount() - offset).
     * @param views the child views to add.  This value can be
     *   <code>null</code> to indicate no children are being added
     *   (useful to remove).
     * @since 1.3
     */
    public void replace(int offset, int length, View[] views) {
        get();
        mDelegate.replace(offset, length, views);
    }

    /**
     * Returns the child view index representing the given position in
     * the model.  By default a view has no children so this is implemented
     * to return -1 to indicate there is no valid child index for any
     * position.
     *
     * @param pos the position >= 0
     * @return  index of the view representing the given position, or 
     *   -1 if no view represents that position
     * @since 1.3
     */
    public int getViewIndex(int pos, Position.Bias b) {
        get();
        return mDelegate.getViewIndex(pos, b);
    }
    
    /**
     * Fetches the allocation for the given child view. 
     * This enables finding out where various views
     * are located, without assuming how the views store
     * their location.  This returns <code>null</code> since the
     * default is to not have any child views.
     *
     * @param index the index of the child, >= 0 && <
     *		<code>getViewCount()</code>
     * @param a  the allocation to this view
     * @return the allocation to the child
     */
    public Shape getChildAllocation(int index, Shape a) {
        get();
        return mDelegate.getChildAllocation(index, a);
    }

    /**
     * Provides a way to determine the next visually represented model 
     * location at which one might place a caret.
     * Some views may not be visible,
     * they might not be in the same order found in the model, or they just
     * might not allow access to some of the locations in the model.
     *
     * @param pos the position to convert >= 0
     * @param a the allocated region in which to render
     * @param direction the direction from the current position that can
     *  be thought of as the arrow keys typically found on a keyboard.
     *  This will be one of the following values:
     * <ul>
     * <li>SwingConstants.WEST
     * <li>SwingConstants.EAST
     * <li>SwingConstants.NORTH
     * <li>SwingConstants.SOUTH
     * </ul>
     * @return the location within the model that best represents the next
     *  location visual position
     * @exception BadLocationException
     * @exception IllegalArgumentException if <code>direction</code>
     *		doesn't have one of the legal values above
     */
    public int getNextVisualPositionFrom(int pos, Position.Bias b, Shape a, 
					 int direction, Position.Bias[] biasRet) 
        throws BadLocationException {
        get();
        return mDelegate.getNextVisualPositionFrom(pos, b, a,
                                                   direction, biasRet);

    }

    /**
     * Provides a mapping, for a given character,
     * from the document model coordinate space
     * to the view coordinate space.  
     *
     * @param pos the position of the desired character (>=0)
     * @param a the area of the view, which encompasses the requested character
     * @param b the bias toward the previous character or the
     *  next character represented by the offset, in case the 
     *  position is a boundary of two views; <code>b</code> will have one
     *  of these values:
     * <ul>
     * <li> <code>Position.Bias.Forward</code>
     * <li> <code>Position.Bias.Backward</code>
     * </ul>
     * @return the bounding box, in view coordinate space,
     *		of the character at the specified position
     * @exception BadLocationException  if the specified position does
     *   not represent a valid location in the associated document
     * @exception IllegalArgumentException if <code>b</code> is not one of the
     *		legal <code>Position.Bias</code> values listed above
     * @see View#viewToModel
     */
    public  Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
        get();
        return mDelegate.modelToView(pos, a, b);
    }

    /**
     * Provides a mapping, for a given region,
     * from the document model coordinate space
     * to the view coordinate space. The specified region is
     * created as a union of the first and last character positions.
     *
     * @param p0 the position of the first character (>=0)
     * @param b0 the bias of the first character position,
     *  toward the previous character or the
     *  next character represented by the offset, in case the 
     *  position is a boundary of two views; <code>b0</code> will have one
     *  of these values:
     * <ul>
     * <li> <code>Position.Bias.Forward</code>
     * <li> <code>Position.Bias.Backward</code>
     * </ul>
     * @param p1 the position of the last character (>=0)
     * @param b1 the bias for the second character position, defined
     *		one of the legal values shown above
     * @param a the area of the view, which encompasses the requested region
     * @return the bounding box which is a union of the region specified
     *		by the first and last character positions
     * @exception BadLocationException  if the given position does
     *   not represent a valid location in the associated document
     * @exception IllegalArgumentException if <code>b0</code> or
     *		<code>b1</code> are not one of the
     *		legal <code>Position.Bias</code> values listed above
     * @see View#viewToModel
     */
    public Shape modelToView(int p0, Position.Bias b0, int p1, Position.Bias b1, Shape a) throws BadLocationException {
        get();
        return mDelegate.modelToView(p0, b0, p1, b1, a);
    }

    /**
     * Provides a mapping from the view coordinate space to the logical
     * coordinate space of the model.  The <code>biasReturn</code>
     * argument will be filled in to indicate that the point given is
     * closer to the next character in the model or the previous
     * character in the model.
     *
     * @param x the X coordinate >= 0
     * @param y the Y coordinate >= 0
     * @param a the allocated region in which to render
     * @return the location within the model that best represents the
     *  given point in the view >= 0.  The <code>biasReturn</code>
     *  argument will be
     * filled in to indicate that the point given is closer to the next
     * character in the model or the previous character in the model.
     */
    public  int viewToModel(float x, float y, Shape a, Position.Bias[] biasReturn) {
        get();
        return mDelegate.viewToModel(x, y, a, biasReturn);
    }

    /**
     * Gives notification that something was inserted into 
     * the document in a location that this view is responsible for.  
     * To reduce the burden to subclasses, this functionality is
     * spread out into the following calls that subclasses can
     * reimplement:
     * <ol>
     * <li><a href="#updateChildren">updateChildren</a> is called
     * if there were any changes to the element this view is
     * responsible for.  If this view has child views that are
     * represent the child elements, then this method should do
     * whatever is necessary to make sure the child views correctly
     * represent the model.
     * <li><a href="#forwardUpdate">forwardUpdate</a> is called
     * to forward the DocumentEvent to the appropriate child views.
     * <li><a href="#updateLayout">updateLayout</a> is called to
     * give the view a chance to either repair its layout, to reschedule
     * layout, or do nothing.
     * </ol>
     *
     * @param e the change information from the associated document
     * @param a the current allocation of the view
     * @param f the factory to use to rebuild if the view has children
     * @see View#insertUpdate
     */
    public void insertUpdate(DocumentEvent e, Shape a, ViewFactory f) {
        get();
        mDelegate.insertUpdate(e, a, f);
    }

    /**
     * Gives notification that something was removed from the document
     * in a location that this view is responsible for.
     * To reduce the burden to subclasses, this functionality is
     * spread out into the following calls that subclasses can
     * reimplement:
     * <ol>
     * <li><a href="#updateChildren">updateChildren</a> is called
     * if there were any changes to the element this view is
     * responsible for.  If this view has child views that are
     * represent the child elements, then this method should do
     * whatever is necessary to make sure the child views correctly
     * represent the model.
     * <li><a href="#forwardUpdate">forwardUpdate</a> is called
     * to forward the DocumentEvent to the appropriate child views.
     * <li><a href="#updateLayout">updateLayout</a> is called to
     * give the view a chance to either repair its layout, to reschedule
     * layout, or do nothing.
     * </ol>
     *
     * @param e the change information from the associated document
     * @param a the current allocation of the view
     * @param f the factory to use to rebuild if the view has children
     * @see View#removeUpdate
     */
    public void removeUpdate(DocumentEvent e, Shape a, ViewFactory f) {
        get();
        mDelegate.removeUpdate(e, a, f);
    }

    /**
     * Gives notification from the document that attributes were changed
     * in a location that this view is responsible for.
     * To reduce the burden to subclasses, this functionality is
     * spread out into the following calls that subclasses can
     * reimplement:
     * <ol>
     * <li><a href="#updateChildren">updateChildren</a> is called
     * if there were any changes to the element this view is
     * responsible for.  If this view has child views that are
     * represent the child elements, then this method should do
     * whatever is necessary to make sure the child views correctly
     * represent the model.
     * <li><a href="#forwardUpdate">forwardUpdate</a> is called
     * to forward the DocumentEvent to the appropriate child views.
     * <li><a href="#updateLayout">updateLayout</a> is called to
     * give the view a chance to either repair its layout, to reschedule
     * layout, or do nothing.
     * </ol>
     *
     * @param e the change information from the associated document
     * @param a the current allocation of the view
     * @param f the factory to use to rebuild if the view has children
     * @see View#changedUpdate
     */
    public void changedUpdate(DocumentEvent e, Shape a, ViewFactory f) {
        get();
        mDelegate.changedUpdate(e, a, f);
    }

    /**
     * Fetches the model associated with the view.
     *
     * @return the view model, <code>null</code> if none
     * @see View#getDocument
     */
    public Document getDocument() {
        get();
        return mDelegate.getDocument();
    }

    /**
     * Fetches the portion of the model for which this view is
     * responsible.
     *
     * @return the starting offset into the model >= 0
     * @see View#getStartOffset
     */
    public int getStartOffset() {
        get();
        return mDelegate.getStartOffset();
    }

    /**
     * Fetches the portion of the model for which this view is
     * responsible.
     *
     * @return the ending offset into the model >= 0
     * @see View#getEndOffset
     */
    public int getEndOffset() {
        get();
        return mDelegate.getEndOffset();
    }

    /**
     * Fetches the structural portion of the subject that this
     * view is mapped to.  The view may not be responsible for the
     * entire portion of the element.
     *
     * @return the subject
     * @see View#getElement
     */
    public Element getElement() {
        get();
        return mDelegate.getElement();
    }

    /**
     * Fetch a <code>Graphics</code> for rendering.
     * This can be used to determine
     * font characteristics, and will be different for a print view
     * than a component view.
     *
     * @return a <code>Graphics</code> object for rendering
     * @since 1.3
     */
    public Graphics getGraphics() {
        get();
        return mDelegate.getGraphics();
    }

    /**
     * Fetches the attributes to use when rendering.  By default
     * this simply returns the attributes of the associated element.
     * This method should be used rather than using the element
     * directly to obtain access to the attributes to allow
     * view-specific attributes to be mixed in or to allow the
     * view to have view-specific conversion of attributes by
     * subclasses.
     * Each view should document what attributes it recognizes
     * for the purpose of rendering or layout, and should always
     * access them through the <code>AttributeSet</code> returned
     * by this method.
     */
    public AttributeSet getAttributes() {
        get();
        return mDelegate.getAttributes();
    }

    /**
     * Tries to break this view on the given axis.  This is
     * called by views that try to do formatting of their
     * children.  For example, a view of a paragraph will
     * typically try to place its children into row and 
     * views representing chunks of text can sometimes be 
     * broken down into smaller pieces.
     * <p>
     * This is implemented to return the view itself, which
     * represents the default behavior on not being
     * breakable.  If the view does support breaking, the
     * starting offset of the view returned should be the
     * given offset, and the end offset should be less than
     * or equal to the end offset of the view being broken.
     *
     * @param axis may be either <code>View.X_AXIS</code> or
     *		<code>View.Y_AXIS</code>
     * @param offset the location in the document model
     *   that a broken fragment would occupy >= 0.  This
     *   would be the starting offset of the fragment
     *   returned
     * @param pos the position along the axis that the
     *  broken view would occupy >= 0.  This may be useful for
     *  things like tab calculations
     * @param len specifies the distance along the axis
     *  where a potential break is desired >= 0
     * @return the fragment of the view that represents the
     *  given span, if the view can be broken.  If the view
     *  doesn't support breaking behavior, the view itself is
     *  returned.
     * @see ParagraphView
     */
    public View breakView(int axis, int offset, float pos, float len) {
        get();
        return mDelegate.breakView(axis, offset, pos, len);
    }

    /**
     * Creates a view that represents a portion of the element.
     * This is potentially useful during formatting operations
     * for taking measurements of fragments of the view.  If 
     * the view doesn't support fragmenting (the default), it 
     * should return itself.  
     *
     * @param p0 the starting offset >= 0.  This should be a value
     *   greater or equal to the element starting offset and
     *   less than the element ending offset.
     * @param p1 the ending offset > p0.  This should be a value
     *   less than or equal to the elements end offset and
     *   greater than the elements starting offset.
     * @return the view fragment, or itself if the view doesn't
     *   support breaking into fragments
     * @see LabelView
     */
    public View createFragment(int p0, int p1) {
        get();
        return mDelegate.createFragment(p0, p1);
    }

    /**
     * Determines how attractive a break opportunity in 
     * this view is.  This can be used for determining which
     * view is the most attractive to call <code>breakView</code>
     * on in the process of formatting.  A view that represents
     * text that has whitespace in it might be more attractive
     * than a view that has no whitespace, for example.  The
     * higher the weight, the more attractive the break.  A
     * value equal to or lower than <code>BadBreakWeight</code>
     * should not be considered for a break.  A value greater
     * than or equal to <code>ForcedBreakWeight</code> should
     * be broken.
     * <p>
     * This is implemented to provide the default behavior
     * of returning <code>BadBreakWeight</code> unless the length
     * is greater than the length of the view in which case the 
     * entire view represents the fragment.  Unless a view has
     * been written to support breaking behavior, it is not
     * attractive to try and break the view.  An example of
     * a view that does support breaking is <code>LabelView</code>.
     * An example of a view that uses break weight is 
     * <code>ParagraphView</code>.
     *
     * @param axis may be either <code>View.X_AXIS</code> or
     *		<code>View.Y_AXIS</code>
     * @param pos the potential location of the start of the 
     *   broken view >= 0.  This may be useful for calculating tab
     *   positions
     * @param len specifies the relative length from <em>pos</em>
     *   where a potential break is desired >= 0
     * @return the weight, which should be a value between
     *   ForcedBreakWeight and BadBreakWeight
     * @see LabelView
     * @see ParagraphView
     * @see #BadBreakWeight
     * @see #GoodBreakWeight
     * @see #ExcellentBreakWeight
     * @see #ForcedBreakWeight
     */
    public int getBreakWeight(int axis, float pos, float len) {
        get();
        return mDelegate.getBreakWeight(axis, pos, len);
    }

    /**
     * Determines the resizability of the view along the
     * given axis.  A value of 0 or less is not resizable.
     *
     * @param axis may be either <code>View.X_AXIS</code> or
     *		<code>View.Y_AXIS</code>
     * @return the weight
     */
    public int getResizeWeight(int axis) {
        get();
        return mDelegate.getResizeWeight(axis);
    }

    /**
     * Sets the size of the view.  This should cause 
     * layout of the view along the given axis, if it 
     * has any layout duties.
     *
     * @param width the width >= 0
     * @param height the height >= 0
     */
    public void setSize(float width, float height) {
        get();
        mDelegate.setSize(width, height);
    }

    /**
     * Fetches the container hosting the view.  This is useful for
     * things like scheduling a repaint, finding out the host 
     * components font, etc.  The default implementation
     * of this is to forward the query to the parent view.
     *
     * @return the container, <code>null</code> if none
     */
    public Container getContainer() {
        get();
        return mDelegate.getContainer();
    }

    /**
     * Fetches the <code>ViewFactory</code> implementation that is feeding
     * the view hierarchy.  Normally the views are given this
     * as an argument to updates from the model when they
     * are most likely to need the factory, but this
     * method serves to provide it at other times.
     *
     * @return the factory, <code>null</code> if none
     */
    public ViewFactory getViewFactory() {
        get();
        return mDelegate.getViewFactory();
    }

    /**
     * Returns the tooltip text at the specified location. The default
     * implementation returns the value from the child View identified by
     * the passed in location.
     *
     * @since 1.4
     * @see JTextComponent#getToolTipText
     */
    public String getToolTipText(float x, float y, Shape allocation) {
        get();
        return mDelegate.getToolTipText(x, y, allocation);
    }

    /**
     * Returns the child view index representing the given position in
     * the view. This iterates over all the children returning the
     * first with a bounds that contains <code>x</code>, <code>y</code>.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param allocation current allocation of the View.
     * @return  index of the view representing the given location, or 
     *   -1 if no view represents that position
     * @since 1.4
     */
    public int getViewIndex(float x, float y, Shape allocation) {
        get();
        return mDelegate.getViewIndex(x, y, allocation);
    }

}

