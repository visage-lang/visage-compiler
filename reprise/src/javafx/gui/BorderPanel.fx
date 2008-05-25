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
package javafx.gui;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javafx.lang.FX;

// PENDING_DOC_REVIEW
/**
 * Creates a panel with the applied border layout.
 * According to the general rules of the border layouting all components are 
 * resized to fit in five regions: top, bottom, left right, and center.
 * Each region may contain no more than one component, and 
 * is identified by a corresponding constant:
 * {@code TOP}, {@code BOTTOM}, {@code LEFT},
 * {@code RIGHT}, and {@code CENTER}.
 * <p/>
 * In addition, the {@code BorderPanel} class supports the relative
 * positioning constants, {@code PAGE_START}, {@code PAGE_END},
 * {@code LINE_START}, and {@code LINE_END}.
 * <p/>
 * The components are laid out according to their
 * preferred sizes and the constraints of the container's size.
 * The {@code TOP} and {@code BOTTOM} components may
 * be stretched horizontally; the {@code LEFT} and
 * {@code RIGHT} components may be stretched vertically;
 * the {@code CENTER} component may stretch both horizontally
 * and vertically to fill any space left over. 
 */
public class BorderPanel extends AbstractPanel {

   
   // PENDING_DOC_REVIEW
   /**
    * A constant to specify components location to be the
    * top portion of the border layout. 
    */
    private static /* final */ attribute TOP = BorderLayout.NORTH;
   
   // PENDING_DOC_REVIEW
   /**
    * A constant to specify components location to be the
    * bottom portion of the border layout.
    */
    private static /* final */ attribute BOTTOM = BorderLayout.SOUTH;
    
   // PENDING_DOC_REVIEW 
   /**
    * A constant to specify components location to be the
    * left portion of the border layout. 
    */
    private static /* final */ attribute LEFT = BorderLayout.WEST;
    
   // PENDING_DOC_REVIEW
   /**
    * A constant to specify components location to be the
    * right portion of the border layout. 
    */
    private static /* final */ attribute RIGHT = BorderLayout.EAST;
    
   // PENDING_DOC_REVIEW
   /**
    * A constant to specify the component comes after the last line of the
    * layout's content. For Western, left-to-right and top-to-bottom orientations,
    * this is equivalent to {@code TOP}. 
    */
    private static /* final */ attribute PAGE_START = BorderLayout.PAGE_START;
   
   // PENDING_DOC_REVIEW
   /**
    * A constant to specify the component comes after the last line of the
    * layout's content. For Western, left-to-right and top-to-bottom orientations,
    * this is equivalent to {@code BOTTOM}. 
    */
    private static /* final */ attribute PAGE_END = BorderLayout.PAGE_END;

   // PENDING_DOC_REVIEW 
   /**
    * A constant to specify the component goes at the beginning of the line
    * direction for the layout. For Western, left-to-right and top-to-bottom
    * orientations, this is equivalent to {@code LEFT}. 
    */
    private static /* final */ attribute LINE_START = BorderLayout.LINE_START;
    
   // PENDING_DOC_REVIEW
   /**
    * A constant to specify the component goes at the end of the line
    * direction for the layout. For Western, left-to-right and top-to-bottom
    * orientations, this is equivalent to {@code RIGHT}. 
    */     
    private static /* final */ attribute LINE_END = BorderLayout.LINE_END;
    
   // PENDING_DOC_REVIEW
   /**
    * A constant to specify components location to be the
    * center portion of the border layout.  
    */
    private static /* final */ attribute CENTER = BorderLayout.CENTER;

   // PENDING_DOC_REVIEW
   /**
    * Defines the top layout constraint (top of the panel).
    */
    public attribute top: Component on replace oldTop {
        update(oldTop, top, TOP);
    }

   // PENDING_DOC_REVIEW
   /**
    * Defines the bottom layout constraint (top of the panel). 
    */
    public attribute bottom: Component on replace oldBottom {
        update(oldBottom, bottom, BOTTOM);
    }

   // PENDING_DOC_REVIEW
   /**
    * Defines the left layout constraint (top of the panel). 
    */
    public attribute left: Component on replace oldLeft {
        update(oldLeft, left, LEFT);
    }

   // PENDING_DOC_REVIEW 
   /**
    * Defines the right layout constraint (top of the panel).
    */
    public attribute right: Component on replace oldRight {
        update(oldRight, right, RIGHT);
    }

   // PENDING_DOC_REVIEW
   /**
    * Defines the component comes before the first line of the layout's 
    * content.
    */
    public attribute pageStart: Component on replace oldPageStart {
        update(oldPageStart, pageStart, PAGE_START);
    }

   // PENDING_DOC_REVIEW
   /**
    * Defines the component comes after the last line of the layout's 
    * content. 
    */
    public attribute pageEnd: Component on replace oldPageEnd {
        update(oldPageEnd, pageEnd, PAGE_END);
    }

   // PENDING_DOC_REVIEW
   /**
    * Defines the component goes at the beginning of the line direction for the
    * layout. 
    */
    public attribute lineStart: Component on replace oldLineStart {
        update(oldLineStart, lineStart, LINE_START);
    }

   // PENDING_DOC_REVIEW
   /**
    * Defines the component goes at the end of the line direction for the
    * layout. 
    */
    public attribute lineEnd: Component on replace oldLineEnd {
        update(oldLineEnd, lineEnd, LINE_END);
    }

   // PENDING_DOC_REVIEW
   /**
    * Defines the center layout constraint (center of the panel). 
    */
    public attribute center: Component on replace oldCenter {
        update(oldCenter, center, CENTER);
    }

    private function update(oldComp: Component, newComp: Component, constraint: String): Void {
        // PENDING(shannonh) - need a way for the assignments in the clear method to happen
        // without the triggers firing. At that point, this clear call should go at the bottom
        // of this method.
        // http://openjfx.java.sun.com/jira/browse/JFXC-1007
        clear(newComp, constraint);

        var jPanel = getJPanel();

        if (oldComp <> null) {
            jPanel.remove(oldComp.getRootJComponent());
            unparentFromThisContainer(oldComp);
        }

        if (newComp <> null) {
            jPanel.add(newComp.getRootJComponent(), constraint);
            parentToThisContainer(newComp);
        }

        jPanel.revalidate();
        jPanel.repaint();
    }

    private function clear(comp: Component, ignore: String): Void {
        if (comp == null) {
            return;
        }

        if (ignore <> TOP and FX.isSameObject(comp, top)) {
            top = null;
        } else if (ignore <> BOTTOM and FX.isSameObject(comp, bottom)) {
            bottom = null;
        } else if (ignore <> LEFT and FX.isSameObject(comp, left)) {
            left = null;
        } else if (ignore <> RIGHT and FX.isSameObject(comp, right)) {
            right = null;
        } else if (ignore <> CENTER and FX.isSameObject(comp, center)) {
            center = null;
        } else if (ignore <> PAGE_START and FX.isSameObject(comp, pageStart)) {
            pageStart = null;
        } else if (ignore <> PAGE_END and FX.isSameObject(comp, pageEnd)) {
            pageEnd = null;
        } else if (ignore <> LINE_START and FX.isSameObject(comp, lineStart)) {
            lineStart = null;
        } else if (ignore <> LINE_END and FX.isSameObject(comp, lineEnd)) {
            lineEnd = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected /* final */ function remove(component: Component): Void {
        if (FX.isSameObject(component, top)) {
            top = null;
        } else if (FX.isSameObject(component, bottom)) {
            bottom = null;
        } else if (FX.isSameObject(component, left)) {
            left = null;
        } else if (FX.isSameObject(component, right)) {
            right = null;
        } else if (FX.isSameObject(component, center)) {
            center = null;
        } else if (FX.isSameObject(component, pageStart)) {
            pageStart = null;
        } else if (FX.isSameObject(component, pageEnd)) {
            pageEnd = null;
        } else if (FX.isSameObject(component, lineStart)) {
            lineStart = null;
        } else if (FX.isSameObject(component, lineEnd)) {
            lineEnd = null;
        }
    }

    protected /* final */ function configureJPanel(jPanel: JPanel): Void {
        jPanel.setLayout(new BorderLayout());
    }

}
