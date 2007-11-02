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

package javafx.ui;

import javafx.ui.Color;
import java.lang.Object;

//TODO JXFC-157
//import javafx.ui.Font;
import javafx.ui.Cursor;
import java.awt.Dimension;
import java.awt.ComponentOrientation;
import javafx.ui.Border;
import javafx.ui.GroupElement;
import javafx.ui.MouseEvent;
import javafx.ui.Spring;
import javafx.ui.UIElement;
import javafx.ui.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.FocusListener;
import javax.swing.SwingUtilities;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import javafx.ui.KeyStroke;
import javafx.ui.canvas.*;

/**
 * Base class for non-window javafx graphical components
 * Encapsulates javax.swing.JComponent
*/

public class KeyboardAction {
    public attribute keyStroke: KeyStroke;
    public attribute enabled: Boolean = true;
    public attribute action: function():Void;
}

public abstract class Widget extends GroupElement, UIElement {  
    private attribute inBoundsListener: Boolean;

    private function makeKeyEvent(e:java.awt.event.KeyEvent):KeyEvent {
        var modifiers:KeyStroke[] = [
        //TODO JFXC-157
            //if (e.isControlDown() ) KeyStroke.CONTROL else null, 
            //if (e.isShiftDown()) KeyStroke.SHIFT else null,
            //if (e.isMetaDown()) KeyStroke.META else
            //if (e.isAltDown()) KeyStroke.ALT else null
            ];
        return KeyEvent {
            source: e
            keyStroke: KeyStroke.KEYBOARD.getKeyStroke(e.getKeyCode())
            //TODO JFXC-157
           // modifiers: modifiers
            keyChar: if (e.getID() == java.awt.event.KeyEvent.KEY_TYPED) "{e.getKeyChar()}" else null
        };
    }    
    protected function installMouseListener():Void {
        if (mouseListener == null) {
            mouseListener = MouseListener {
                    public function mouseEntered(e:java.awt.event.MouseEvent):Void {
                        if(onMouseEntered <> null)
                            onMouseEntered(makeMouseEvent(e));
                    }
                    public function mouseExited(e:java.awt.event.MouseEvent):Void {
                        if(onMouseExited <> null)
                            onMouseExited(makeMouseEvent(e));
                    }
                    public function mouseClicked(e:java.awt.event.MouseEvent):Void {
                        if(onMouseClicked <> null)
                            onMouseClicked(makeMouseEvent(e));
                    }
                    public function mouseReleased(e:java.awt.event.MouseEvent):Void {
                        if(onMouseReleased <> null)
                            onMouseReleased(makeMouseEvent(e));
                    }
                    public function mousePressed(e:java.awt.event.MouseEvent):Void {
                        if(onMousePressed <> null)
                            onMousePressed(makeMouseEvent(e));
                    }
                };
            if (component <> null) {
                this.getNonScrollPaneComponent().addMouseListener(mouseListener);
            }
        }
    }
    protected function installMouseWheelListener():Void {
        if (mouseWheelListener == null) {
            mouseWheelListener = MouseWheelListener {
                    public function mouseWheelMoved(e:java.awt.event.MouseWheelEvent):Void {
    //println("mouse wheel {e}");
                        if(onMouseWheelMoved <> null)
                            (onMouseWheelMoved)(makeMouseWheelEvent(e));
                    }
                };
            //println("created mouse wheel listener");
            if (component <> null) {
                //println("adding mouse wheel listener");
                this.getNonScrollPaneComponent().addMouseWheelListener(mouseWheelListener);
            }
        }
    }
    protected function installMouseMotionListener():Void {
        if (mouseMotionListener == null) {
            mouseMotionListener = MouseMotionListener {
                    public function mouseMoved(e:java.awt.event.MouseEvent):Void {
                        if(onMouseMoved <> null)
                            onMouseMoved(makeMouseEvent(e));
                    }
                    public function mouseDragged(e:java.awt.event.MouseEvent):Void {
                        if(onMouseDragged <> null)
                            onMouseDragged(makeMouseEvent(e));
                    }
                };
            if (component <> null) {
                this.getNonScrollPaneComponent().addMouseMotionListener(mouseMotionListener);
            }
        }
    }
    private function makeMouseEvent(e:java.awt.event.MouseEvent):MouseEvent {
        return MouseEvent {
            //TODO JFXC-157
            //modifiers: [if (e.isAltDown() ) KeyModifier.ALT else null,
           //             if (e.isControlDown() ) KeyModifier.CTRL else null, 
           //             if (e.isMetaDown() ) KeyModifier.META else null,
           //             if (e.isShiftDown() ) KeyModifier.SHIFT else null]
            clickCount: e.getClickCount()
            button: if (SwingUtilities.isLeftMouseButton(e)) 1 else 
                if (SwingUtilities.isRightMouseButton(e)) 3 else 2
            x: e.getX()
            y: e.getY()
            source: e
        };
    }
    private function makeMouseWheelEvent(e:java.awt.event.MouseWheelEvent):MouseEvent {
        //TODO JXFC-179
        /**********
        MouseWheelEvent {
            //TODO JFXC-157
            //modifiers: [if (e.isAltDown() ) KeyModifier.ALT else null,
            //            if (e.isControlDown() ) KeyModifier.CTRL else null, 
            //            if (e.isMetaDown() ) KeyModifier.META else null,
            //            if (e.isShiftDown() ) KeyModifier.SHIFT else null]
            //TODO JXFC-178
            //clickCount: e.getClickCount()
            //TODO JXFC-178
            //button: if (SwingUtilities.isLeftMouseButton(e) ) 1 else 
            //    if (SwingUtilities.isRightMouseButton(e) ) 3 else 2
            //TODO JXFC-178
           // x: e.getX()
            //TODO JXFC-178
           // y: e.getY()
            scrollType: if (e.getScrollType() == e.WHEEL_UNIT_SCROLL) 
                MouseWheelScrollType.UNIT_SCROLL else MouseWheelScrollType.BLOCK_SCROLL
            scrollAmount: e.getScrollAmount()
            wheelRotation: e.getWheelRotation()
            unitsToScroll: e.getUnitsToScroll()
           //TODO JXFC-178
           // source: e
        } as MouseEvent;
         * ********/
        MouseEvent{};
    }
    protected attribute mouseListener: MouseListener;
    protected attribute mouseWheelListener: MouseWheelListener;
    private attribute focusListener: FocusListener;
    private attribute keyListener: KeyListener;
    protected attribute mouseMotionListener: MouseMotionListener;
    protected function onSetOpaque(value:Boolean):Void {
        if (component <> null) {
            component.setOpaque(value);
        }
    }        

    function setBounds(b:java.awt.Rectangle):Void{
        component.setBounds(b);     
    }

    function getBounds():java.awt.Rectangle {
        return component.getBounds();     
    }
    /** Returns the bounds relative to the given widget. 
     * If the argument is null, the bounds returned are
     * relative to the top most parent. 
     **/
    function getBoundsRelativeTo(w:Widget):java.awt.Rectangle {
        var target:java.awt.Component = null;
        
        if (w == null) {
            target = component;
            while (target.getParent() <> null) {
                target = target.getParent();
            }
            //TODO JXFC-172
            target;
        } else {
            target = w.component;
        }

        var targetLoc = target.getLocationOnScreen();
        var selfLoc = component.getLocationOnScreen();

        var bounds = getBounds();
        return new java.awt.Rectangle((selfLoc.x-targetLoc.x), (selfLoc.y-targetLoc.y), bounds.width, bounds.height);     
    }  

    public attribute name: String on replace {
         component.setName(name);
    };
    
    public attribute keyboardAction: KeyboardAction[]
        on insert [indx] (newValue)   {
            var c = this.getNonScrollPaneComponent();
            var inputMap = c.getInputMap();
            var actionMap = c.getActionMap();
            //TODO JXFC-180
            var zz = newValue as Widget.KeyboardAction;
            var k = zz.keyStroke;
            var a = zz.action;
            var j = javax.swing.KeyStroke.getKeyStroke(k.id, 0);
            inputMap.put(j, newValue);
            actionMap.put(newValue as Object, javax.swing.AbstractAction {
                    public function isEnabled():Boolean {
                        //TODO JXFC-175
                        //return newValue.enabled;
                        return true;
                    }
                    public function actionPerformed(e:java.awt.event.ActionEvent):Void {
                        //TODO JXFC-175
                        //newValue.action();
                    }
                } as javax.swing.Action);
        }
        on delete [indx] (oldValue) {
            var c = this.getNonScrollPaneComponent();
            var inputMap = c.getInputMap();
            var actionMap = c.getActionMap();
            //TODO JXFC-180
            var zz = oldValue as Widget.KeyboardAction; 
            var k = zz.keyStroke;
            var j = javax.swing.KeyStroke.getKeyStroke(k.id, 0);
            inputMap.remove(j);
            actionMap.remove(zz);
        }
        on replace [indx] (oldValue) {
            var c = this.getNonScrollPaneComponent();
            var inputMap = c.getInputMap();
            var actionMap = c.getActionMap();
            var newValue = keyboardAction[indx];
            var k = newValue.keyStroke;
            var j = javax.swing.KeyStroke.getKeyStroke(k.id, 0);
            inputMap.put(j, newValue);
            actionMap.remove(oldValue);
            actionMap.put(newValue as Object, javax.swing.AbstractAction {
                    public function isEnabled():Boolean {
                        //TODO JXFC-175
                        //return newValue.enabled;
                        return true;
                    }
                    public function actionPerformed(e:java.awt.event.ActionEvent):Void {
                        //TODO JXFC-175
                        //newValue.action();
                    }
                } as javax.swing.Action);
        };
    

    public attribute focusTraversalKeysEnabled: Boolean on replace {
        if (component <> null) {
            this.getNonScrollPaneComponent().setFocusTraversalKeysEnabled(focusTraversalKeysEnabled);
        }
    };       

    /**
     *  Sets whether the this component should use a buffer to paint.
     *  If set to true, all the drawing from this component will be done
     *  in an offscreen painting buffer. The offscreen painting buffer will
     *  the be copied onto the screen.
     */
    public attribute doubleBuffered: Boolean on replace {
        component.setDoubleBuffered(doubleBuffered);
    };


    /** Sets the x coordinate of this component within its parent. Has no effect unless contained in a Panel. */
    public attribute x: Number on replace {
        if (not inBoundsListener){
            var b = this.getBounds();
            b.x = x.intValue();
            this.setBounds(b);
        }
    };        
    
    /** Sets the y coordinate of this component within its parent. Has no effect unless contained in a Panel. */
    public attribute y: Number on replace {
        if (not inBoundsListener) {
            var b = this.getBounds();
            b.y = y.intValue();
            this.setBounds(b);
        }
    };        

    /** Sets width of this component. Has no effect unless contained in a Panel.  */
    public attribute width: Number on replace {
        if (not inBoundsListener) {
            //println("width {getComponent()} = {n}");
            var b = this.getBounds();
            b.width = width.intValue();
            this.setBounds(b);
        }
    };       

    /** Sets the height of this component. Has no effect unless contained in a Panel.  */
    public attribute height: Number on replace {
        if (not inBoundsListener) {
            //println("height {getComponent()} = {n}");
            var b = this.getBounds();
            b.height = height.intValue();
            this.setBounds(b);
        }
    };
    

    /** read-only attribute providing access to the underlying Swing component */
    public attribute component: javax.swing.JComponent;

    /** Hint to GroupLayout to make this component's height equal to others in the same row */
    public attribute sizeToFitRow: Boolean;

    /** Hint to GroupLayout to make this component's width equal to others in the same column */
    public attribute sizeToFitColumn: Boolean;

    /**
     * Registers the text to display in a tool tip.
     * The text displays when the cursor lingers over the component.
     */
    public attribute toolTipText: String on replace {
        if (component <> null) {
            this.getNonScrollPaneComponent().setToolTipText(toolTipText);
        }
    };

       

    /**
     * True when the object is visible. An object that is not
     * visible is not drawn on the screen.
     */
    public attribute visible: Boolean on replace {
        if (component <> null) {
            component.setVisible(visible);
        }
    };       

    /**
     * Sets the background color of this component.
     */
    public attribute background: AbstractColor on replace {
        if (component <> null) {
            var c = this.getNonScrollPaneComponent();
            if (background <> null) {
                opaque = true;
            }
            c.setBackground(awtBackground);
        }
    };

   
    protected attribute awtBackground: java.awt.Color = bind background.getColor();
    /**
     * Sets the foreground color of this component.
     */
    public attribute foreground: Color on replace {
        if (component <> null) {
            var c = this.getNonScrollPaneComponent();
            if (foreground <> null) {
                c.setForeground(awtForeground);
            }
        }
    };
    
    protected attribute awtForeground: java.awt.Color = bind foreground.getColor();
    
    /**
     * If true the component paints every pixel within its bounds. 
     * Otherwise, the component may not paint some or all of its
     * pixels, allowing the underlying pixels to show through.
     * The default value of this attribute is <code>false</code>.
     */
    public attribute opaque: Boolean on replace {
        this.onSetOpaque(opaque);
    };
    /**
     * Sets the <code>Font</code> of this object.
     */
//TODO JXFC-157    
    //public attribute font: Font;
    
//TODO JXFC-157  
    /*********
    protected attribute awtFont: java.awt.Font = bind font.getFont() on replace {
        if (component <> null) {
            var c = this.getNonScrollPaneComponent();
            c.setFont(awtFont);
        }
    };
    *********/

    
    /**
     * Sets the focusable state of this Widget to the specified value. This
     * value overrides the Widget's default focusability, wich is true.
     */
    public attribute focusable: Boolean = true on replace {
        if (component <> null) {
            if (component instanceof javax.swing.JScrollPane) {
                (component as javax.swing.JScrollPane).getViewport().getView().setFocusable(focusable);
            } else {
                component.setFocusable(focusable);
            }
        }
    };
    
    public function requestFocus():Void {
        if (focusable) {
            this.getNonScrollPaneComponent().requestFocus();
        }
    }    
    public attribute focused: Boolean = false on replace {
        if (focused) {
            requestFocus();
            //TODO - I get a mismatch on 
            focused; 
        } else {
            var f = focusable;	
            focusable = false;
            focusable = f;
        }
    };

    
    /**
     * Resizes this component to the specified dimensions.
     */
    public attribute size: Dimension on replace  {
        if (size <> null) {
            component.setSize(size);
        }
    };
    
    /*
     * Sets the preferred size of this component.
     * If <code>preferredSize</code> is <code>null</code>, the UI will
     * be asked for the preferred size.
     */
    public attribute preferredSize: Dimension on replace {
        if (component <> null) {
            if (preferredSize.height <= 0) {
                preferredSize.height = component.getPreferredSize().height;
            }
            if (preferredSize.width <= 0) {
                preferredSize.width = component.getPreferredSize().width;
            }
            component.setPreferredSize(preferredSize);
        }
    };


    /**
     * Sets the maximum size of this component to a constant
     * value.  Subsequent calls to <code>maximumSize()</code> will always
     * return this value; the component's UI will not be asked
     * to compute it.  Setting the maximum size to <code>null</code>
     * restores the default behavior.
     */
    public attribute maximumSize: Dimension on replace {
        if (component <> null) {
            if (maximumSize.height <= 0) {
                maximumSize.height = component.getMaximumSize().height;
            }
            if (maximumSize.width <= 0) {
                maximumSize.width = component.getMaximumSize().width;
            }
            component.setMaximumSize(maximumSize);
        }
    }; 
    
    /**
     * Sets the minimum size of this component to a constant
     * value.  Subsequent calls to <code>minimumSize()</code> will always
     * return this value; the component's UI will not be asked
     * to compute it.  Setting the minimum size to <code>null</code>
     * restores the default behavior.
     */
    public attribute minimumSize: Dimension on replace {
        if (component <> null) {
            if (minimumSize.height <= 0) {
                minimumSize.height = component.getMinimumSize().height;
            }
            if (minimumSize.width <= 0) {
                minimumSize.width = component.getMinimumSize().width;
            }
            component.setMinimumSize(minimumSize);
        }
    };  
    

    /**
     * Sets the border of this component.  The <code>Border</code> object is
     * responsible for defining the insets for the component
     * (overriding any insets set directly on the component) and
     * for optionally rendering any border decorations within the
     * bounds of those insets.  Borders should be used (rather
     * than insets) for creating both decorative and non-decorative
     * (such as margins and padding) regions for a swing component.
     * Compound borders can be used to nest multiple borders within a
     * single component.
     */
    public attribute border: Border on replace {
        var c:java.awt.Component = component;
        if (c <> null) {
            if (c instanceof javax.swing.JScrollPane) {
                c = (component as javax.swing.JScrollPane).getViewport().getView();
            } 
            if (c instanceof javax.swing.JComponent) {
                try {
                   (c as javax.swing.JComponent).setBorder(border.getBorder());
                } catch (e) {
                }
            }
        }
    };

    /**
     * Sets the cursor image to the specified cursor.  This cursor
     * image is displayed when the <code>contains</code> method for
     * this component returns true for the current cursor location, and
     * this Component is visible, displayable, and enabled. Setting the
     * cursor of a <code>Container</code> causes that cursor to be displayed
     * within all of the container's subcomponents, except for those
     * that have a non-<code>null</code> cursor. 
     */
    public attribute cursor: Cursor on replace {
        component.setCursor(cursor.getCursor());
    };
    
    /**
     * True when the object is enabled. An object that is not
     * enabled does not interact with the user. Defaults to <code>true</code>.
     */
    public attribute enabled: Boolean on replace {
        if (component <> null) {
            var c = this.getNonScrollPaneComponent();
            c.setEnabled(enabled);
        }
    };
    
    
    /**
     * Returns the alignment along the x axis.  This specifies how
     * the component would like to be aligned relative to other 
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     */
    public attribute alignmentX: Number on replace {
        component.setAlignmentX(alignmentX.floatValue());
    };

    /**
     * Returns the alignment along the y axis.  This specifies how
     * the component would like to be aligned relative to other 
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     */
    public attribute alignmentY: Number on replace {
        component.setAlignmentY(alignmentY.floatValue());
    };         

    /**
     * Sets the language-sensitive orientation that is to be used to order
     * the elements or text within this component.  Language-sensitive
     * <code>LayoutManager</code> and <code>Component</code>
     * subclasses will use this property to
     * determine how to lay out and draw components.
     */
    public attribute componentOrientation: ComponentOrientation on replace {
        if (component <> null) {
            this.getNonScrollPaneComponent().setComponentOrientation(componentOrientation);
        }
    };        
    
    /** factory method to create the underlying Swing component*/
    protected abstract function createComponent():javax.swing.JComponent;
    
    public function getComponent():javax.swing.JComponent {
        if (component == null) {
            var comp = this.createComponent();
            var c = comp;
            if (c instanceof javax.swing.JScrollPane) {
                c = (c as javax.swing.JScrollPane).getViewport().getView() as javax.swing.JComponent;
            }
            c.setDoubleBuffered(doubleBuffered);
            if (cursor <> null) {
                c.setCursor(cursor.getCursor());
            }
            if (toolTipText <> null) {
                c.setToolTipText(toolTipText);
            }
            c.setVisible(visible);
            c.setOpaque(opaque);
            if (background <> null) {
                c.setBackground(background.getColor());
                c.setOpaque(true);
            }
            if (foreground <> null) {
                c.setForeground(foreground.getColor());
            }
//TODO JXFC-157
            /********
            if (awtFont <> null) {
                c.setFont(awtFont);
            }
             ***********/
            if (focusable <> c.isFocusable()) {
               c.setFocusable(focusable);
            }
            focusListener = FocusListener {
                    public function focusGained(e:java.awt.event.FocusEvent):Void {
                        focused = true;
                    }
                    public function focusLost(e:java.awt.event.FocusEvent):Void {
                        focused = false;
                    }
               };
            c.addFocusListener(focusListener);
            if (componentOrientation <> null) {
                c.setComponentOrientation(componentOrientation);
            }
            if (border <> null) {
                try {
                   c.setBorder(border.getBorder());
                } catch (e) {
                }
            }
            c.setEnabled(enabled);
            if (size <> null) {
                c.setSize(size);
            }
            c.setAlignmentX(alignmentX.floatValue());
            if (preferredSize <> null) {
                var dim = c.getPreferredSize();
                if (dim <> null) {
                    if (preferredSize.height <= 0) {
                        preferredSize.height = dim.height;
                    }
                    if (preferredSize.width <= 0) {
                        preferredSize.width = dim.width;
                    }
                }
                c.setPreferredSize(preferredSize);
            }
            c.setAlignmentY(alignmentY.floatValue());
            if (keyboardAction <> null) {
                var inputMap = c.getInputMap();
                var actionMap = c.getActionMap();
                foreach (i in keyboardAction) {
                    var k = i.keyStroke;
                    var a = i.action;
                    var j = javax.swing.KeyStroke.getKeyStroke(k.id, 0);
                    inputMap.put(j, i);
                    actionMap.put(i, javax.swing.AbstractAction {
                            public function isEnabled():Boolean {
                                //TODO JXFC-175
                                //return i.enabled;
                                return true;
                            }
                            public function actionPerformed(e:java.awt.event.ActionEvent):Void {
                                //TODO JXFC-175
                                //i.action();
                            }
                        } as javax.swing.Action);
                }
            }
            c.setFocusTraversalKeysEnabled(focusTraversalKeysEnabled);
            if (keyListener <> null) {
                c.addKeyListener(keyListener);
            }
            if (mouseListener <> null) {
                c.addMouseListener(mouseListener);
            }
            if (mouseMotionListener <> null) {
                c.addMouseMotionListener(mouseMotionListener);
            }
            if (mouseWheelListener <> null) {
                //println("adding mouse wheel...");
                c.addMouseWheelListener(mouseWheelListener);
            }
            //TODO how to tell if you need to override?
            //if (x <> null or y <> null or height <> null or width <> null) {
                var bounds = comp.getBounds();
                //if (x <> null) {
                    bounds.x = x.intValue();
                //} 
                //if (y <> null) {
                    bounds.y = y.intValue();
                //}
                //f (width <> null) {
                    bounds.width = width.intValue();
               // }
                //if (height <> null) {
                    bounds.height = height.intValue();
                //}
                if (bounds.width == 0) {
                    bounds.width = comp.getPreferredSize().width;
                }
                if (bounds.height == 0) {
                    bounds.height = comp.getPreferredSize().height;
                }
                //println("setting bounds to {bounds}");
                comp.setBounds(bounds);
            //}
            inBoundsListener = true;
            x = comp.getX();
            y = comp.getY();
            width = comp.getWidth();
            height = comp.getHeight();
            inBoundsListener = false;
            comp.addComponentListener(java.awt.event.ComponentAdapter {
                  public function componentResized(e:java.awt.event.ComponentEvent):Void {
                      //TODO JXFC-175
                      //inBoundsListener = true;
                      //height = comp.getHeight();
                      //width = comp.getWidth();
                      //inBoundsListener = false;
                  }
            } as java.awt.event.ComponentListener);
            if (focused) {
                //TODO DO LATER
                //do later {
                    requestFocus();
                //TODO DO LATER
                //}
            }
            if (name <> null) {    
                comp.setName(name);
            }
            component = comp;
        }
        return component;
    }
        
    public function getNonScrollPaneComponent():javax.swing.JComponent {
        var w = this.getComponent();
        if (w instanceof javax.swing.JScrollPane) {
            w = (w as javax.swing.JScrollPane).getViewport().getView() as
                    javax.swing.JComponent;
        }
        return w;
    }

    /** Returns the current preferred size of this component */
    //TODO JXFC-173
    public function getPreferredSize(): Dimension{
        return component.getPreferredSize();
    }
    /** Returns the current minimum size of this component */
    //TODO JXFC-173
    public function getMinimumSize(): Dimension{
        return component.getMinimumSize();
    }
    /** Returns the current maximum size of this component */
    //TODO JXFC-173
    public function getMaximumSize(): Dimension{
        return component.getMaximumSize();
    }

    //TODO JXFC-173 resolve issue with size being an attribute and a function
    public function getSize(): Dimension {
        return component.getSize();
    }

    /**
     * <code>attribute onMouseEntered: function(:MouseEvent):Void</code><br></br>
     * Optional handler for mouse enter events.
     */
    public attribute onMouseEntered: function(:MouseEvent):Void on replace {
        installMouseWheelListener();
    };
    
    /**
     * <code>attribute onMouseExited: function(:MouseEvent):Void</code><br></br>
     * Optional handler for mouse exit events.
     */
    public attribute onMouseExited: function(:MouseEvent):Void on replace {
        installMouseWheelListener();
    };
    
    /**
     * <code>attribute onMousePressed: function(:MouseEvent):Void</code><br></br>
     * Optional handler for mouse press events.
     */
    public attribute onMousePressed: function(:MouseEvent):Void on replace {
        installMouseWheelListener();
    };
    
    /**
     * <code>attribute onMouseReleased: function(:MouseEvent):Void</code><br></br>
     * Optional handler for mouse release events.
     */
    public attribute onMouseReleased: function(:MouseEvent):Void on replace {
        installMouseWheelListener();
    };
    
    /**
     * <code>attribute onMouseClicked: function(:MouseEvent):Void</code><br></br>
     * Optional handler for mouse exit events.
     */
    public attribute onMouseClicked: function(:MouseEvent):Void on replace {
        installMouseWheelListener();
    };
    
    /**
     * <code>attribute onMouseMoved: function(:MouseEvent):Void</code><br></br>
     * Optional handler for mouse motion events.
     */
    public attribute onMouseMoved: function(:MouseEvent):Void on replace {
        installMouseWheelListener();
    };
    
    /**
     * <code>attribute onMouseDragged: function(:MouseEvent):Void</code><br></br>
     * Optional handler for mouse dragged events.
     */
    public attribute onMouseDragged: function(:MouseEvent):Void on replace {
        installMouseWheelListener();
    };
    
    /**
     * <code>attribute onMouseWheelMoved: function(:MouseWheelEvent):Void</code><br></br>
     * Optional handler for mouse wheel events.
     */
    public attribute onMouseWheelMoved: function(:MouseWheelEvent):Void on replace {
        installMouseWheelListener();
    };        

    /**
     * <code>attribute onKeyUp: function(:KeyEvent):Void;
     * Optional handler for key release events.
     */
    public attribute onKeyUp: function(:KeyEvent):Void on replace {
        if (onKeyUp <> null) {
            installKeyListener();
        } 
    };
    /**
     * <code>attribute onKeyDown: function(:KeyEvent):Void;
     * Optional handler for key press events.
     */
    public attribute onKeyDown: function(:KeyEvent):Void on replace {
        if (onKeyDown <> null) {
            installKeyListener();
        } 
    };

    /**
     * <code>attribute onKeyTyped: function(:KeyEvent):Void
     * Optional handler for key typed events.
     */
    public attribute onKeyTyped: function(:KeyEvent):Void on replace {
        if (onKeyTyped <> null) {
            installKeyListener();
        } 
    };

    public function getWindow():java.awt.Window {
        return javax.swing.SwingUtilities.getWindowAncestor(this.getComponent());
    }  
    
    private function installKeyListener():Void {
        if (keyListener == null) {
            keyListener = KeyListener {
                    public function keyTyped(e:java.awt.event.KeyEvent):Void {
                        if(onKeyTyped <> null) 
                            onKeyTyped(makeKeyEvent(e));
                    }
                    public function keyPressed(e:java.awt.event.KeyEvent):Void {
                        if(onKeyDown <> null) 
                            onKeyDown(makeKeyEvent(e));
                    }
                    public function keyReleased(e:java.awt.event.KeyEvent):Void {
                        if(onKeyUp <> null) 
                            onKeyUp(makeKeyEvent(e));
                    }
                };
            if (component <> null) {
                getNonScrollPaneComponent().addKeyListener(keyListener);
            }
        }
    } 
    
}
