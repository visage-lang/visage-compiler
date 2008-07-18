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
 
package javafx.ui; 

import javafx.ui.Widget;
import javafx.ui.Border;
import javafx.ui.HorizontalScrollBarPolicy;
import javafx.ui.VerticalScrollBarPolicy;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Cursor;

/**
 * Interface for widgets that support scrollbars and scrolling. Encapsulates
 * javax.swing.JScrollPane and javax.swing.JViewport.
 */

public abstract class ScrollableWidget extends Widget {
    // TODO MARK AS FINAL
    private attribute viewChangeListener: ChangeListener;

    // TODO MARK AS FINAL
    protected attribute scrollpane: javax.swing.JScrollPane;

    /**
     * Adds a child that will appear as a column on to the left or right
     * of the main view of the scrollpane depending on the current component orientation.
     */
    public attribute rowHeader:Widget on replace  {
        if (scrollpane != null) {
            var x = rowHeader.getNonScrollPaneComponent();
            scrollpane.setRowHeaderView(x);
        }
    };

    /**
     * Adds a child that will appear as a row at the top or bottom
     * of the main view of the scrollpane depending on the current component orientation.
     */
    public attribute columnHeader:Widget on replace  {
        if (scrollpane != null) {
            var x = columnHeader.getNonScrollPaneComponent();
            scrollpane.setColumnHeaderView(x);
        }
    };

    /*
     * Adds a child that will appear in lower left corner of the scroll pane
     * if there's room. 
     */
    public attribute lowerLeftCorner:Widget  on replace {
        if (scrollpane != null) {
            var x = lowerLeftCorner.getNonScrollPaneComponent();
            scrollpane.setCorner(scrollpane.LOWER_LEFT_CORNER, x);
        }
    };

    /*
     * Adds a child that will appear in lower right corner of the scroll pane
     * if there's room. 
     */
    public attribute lowerRightCorner:Widget on replace  {
        if (scrollpane != null) {
            var x = lowerRightCorner.getNonScrollPaneComponent();
            scrollpane.setCorner(scrollpane.LOWER_RIGHT_CORNER, x);
        }
    };

    /*
     * Adds a child that will appear in upper left corner of the scroll pane
     * if there's room. 
     */
    public attribute upperLeftCorner:Widget on replace {
        if (scrollpane != null) {
            var x = upperLeftCorner.getNonScrollPaneComponent();
            scrollpane.setCorner(scrollpane.UPPER_LEFT_CORNER, x);
        }
    };

    /*
     * Adds a child that will appear in upper right corner of the scroll pane
     * if there's room. 
     */
    public attribute upperRightCorner:Widget on replace {
        if (scrollpane != null) {
            var x = upperRightCorner.getNonScrollPaneComponent();
            scrollpane.setCorner(scrollpane.UPPER_RIGHT_CORNER, x);
        }
    };

    /**
     * Sets the border of the scroll pane's viewport
     */
    public attribute viewportBorder:Border on replace  {
        if (scrollpane != null) {
            scrollpane.setViewportBorder(viewportBorder.getBorder());
        }
    };

    /**
     * Sets the scroll pane's border
     */
    public attribute scrollPaneBorder:Border on replace {
        if (scrollpane != null) {
            scrollpane.setBorder(scrollPaneBorder.getBorder());
        }
    };

    /**
     * Optional handler for changes to the scroll pane's view's location.
     */
    public attribute onViewChange: function():Void on replace {
        if (scrollpane != null) {
            if (onViewChange != null) {
                this.installViewChangeListener();
            } else {
                scrollpane.getViewport().removeChangeListener(viewChangeListener);
            }
        }
    };
    
    /** The display policy for the horizontal scrollbar. Defaults to AS_NEEDED.*/
    public attribute horizontalScrollBarPolicy: HorizontalScrollBarPolicy = 
            HorizontalScrollBarPolicy.AS_NEEDED on replace {
                if (scrollpane != null) {
                    var n = horizontalScrollBarPolicy.id.intValue();
                    scrollpane.setHorizontalScrollBarPolicy(n);
                }
            };

    public attribute scrollBarBackground: AbstractColor;

    protected attribute awtScrollBarBackground: java.awt.Color = 
                bind scrollBarBackground.getColor()
          on replace {
            scrollpane.getVerticalScrollBar().setBackground(awtScrollBarBackground);
            scrollpane.getHorizontalScrollBar().setBackground(awtScrollBarBackground);
          };

    /** The display policy for the vertical scrollbar. Defaults to AS_NEEDED.*/
    public attribute verticalScrollBarPolicy: VerticalScrollBarPolicy =
        VerticalScrollBarPolicy.AS_NEEDED on replace {
            if (scrollpane != null) {
                var n = verticalScrollBarPolicy.id.intValue();
                scrollpane.setVerticalScrollBarPolicy(n);
            }
        };

    protected abstract function createView(): javax.swing.JComponent;

    protected function createScrollPane(view:javax.swing.JComponent): javax.swing.JScrollPane  {
        var sp = new com.sun.javafx.api.ui.FXScrollPane();
        //sp.setViewport(vp);
        //sp.setViewport(new javax.swing.JViewport);
        sp.setViewportView(view);
        return sp;
    }
    private function installViewChangeListener() {
        if (viewChangeListener == null) {
            viewChangeListener = ChangeListener {
                    public function stateChanged(e:ChangeEvent):Void {
                        if(onViewChange != null)
                            onViewChange();
                    }
                };
            scrollpane.getViewport().addChangeListener(viewChangeListener);
        }
    }
    
    public bound function getViewRect(): java.awt.Rectangle  {
        if (scrollpane != null) {
            return new java.awt.Rectangle(scrollpane.getViewport().getViewRect());
        } else {
            return new java.awt.Rectangle();
        }
    }
    // hide it for now - buggy <http://bugs.sun.com/bugdatabase/view_bug.dobug_id=6333318>
    private function scrollRectToVisible(rect:java.awt.Rectangle):Void {
        if (scrollpane != null) {
            if (rect != null) {
                scrollpane.getViewport().scrollRectToVisible(rect);
            }
        }
    }
    public bound function getViewPosition(): java.awt.Point  {
        if (scrollpane != null) {
            return new java.awt.Point(scrollpane.getViewport().getViewPosition());
        } else {
            return new java.awt.Point();
        }
    }
    public function setViewPosition(point:java.awt.Point):Void {
        if (scrollpane != null) {
            if (point != null) {
                //println(java.lang.Thread.currentThread());
                //println("setting viewPosition to {point}");
                scrollpane.getViewport().setViewPosition(point);
            }
        }
    }
    public function onSetOpaque(value:Boolean):Void {
        scrollpane.setOpaque(value);
        scrollpane.getVerticalScrollBar().setOpaque(value);
        scrollpane.getHorizontalScrollBar().setOpaque(value);
        scrollpane.getViewport().setOpaque(value);
        var comp = scrollpane.getViewport().getView();
        if (comp instanceof javax.swing.JComponent) {
            (comp as javax.swing.JComponent).setOpaque(value);    
        }
    }

    public function createComponent():javax.swing.JComponent {
        scrollpane = this.createScrollPane(this.createView());
        scrollpane.setOpaque(false);
        scrollpane.getViewport().setOpaque(false);
        scrollpane.setHorizontalScrollBarPolicy(horizontalScrollBarPolicy.id.intValue());
        scrollpane.setVerticalScrollBarPolicy(verticalScrollBarPolicy.id.intValue());
        scrollpane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if (rowHeader != null) {
            scrollpane.setRowHeaderView(rowHeader.getNonScrollPaneComponent());
        }
        if (columnHeader != null) {
            scrollpane.setColumnHeaderView(columnHeader.getNonScrollPaneComponent());
        }
        if (lowerLeftCorner != null) {
            scrollpane.setCorner(scrollpane.LOWER_LEFT_CORNER,
                                 lowerLeftCorner.getNonScrollPaneComponent());
        }
        if (lowerRightCorner != null) {
            scrollpane.setCorner(scrollpane.LOWER_RIGHT_CORNER,
                                 lowerRightCorner.getNonScrollPaneComponent());
        }
        if (upperLeftCorner != null) {
            scrollpane.setCorner(scrollpane.UPPER_LEFT_CORNER,
                                 upperLeftCorner.getNonScrollPaneComponent());
        }
        if (upperRightCorner != null) {
            scrollpane.setCorner(scrollpane.UPPER_RIGHT_CORNER,
                                 upperRightCorner.getNonScrollPaneComponent());
        }
        if (viewportBorder != null) {
            scrollpane.setViewportBorder(viewportBorder.getBorder());
        }
        if (scrollPaneBorder != null) {
            scrollpane.setBorder(scrollPaneBorder.getBorder());
        }
        if (onViewChange != null) {
            this.installViewChangeListener();
        }
        if(horizontalScrollBarPolicy != null) {
            var n = horizontalScrollBarPolicy.id.intValue();
            scrollpane.setHorizontalScrollBarPolicy(n);
        }
        if(verticalScrollBarPolicy != null) {
            var n = verticalScrollBarPolicy.id.intValue();
            scrollpane.setVerticalScrollBarPolicy(n);
        }
        if (awtScrollBarBackground != null) {
            scrollpane.getVerticalScrollBar().setBackground(awtScrollBarBackground);
           scrollpane.getHorizontalScrollBar().setBackground(awtScrollBarBackground);
        }
        return scrollpane;
    }

}


