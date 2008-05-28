/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 * 
 * This code is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License version 2 only, as 
 * published by the Free Software Foundation.  Sun designates this 
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
 
package javafx.ui.canvas; 

import java.lang.Exception;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImageOp;
import java.lang.Math;
import java.lang.System;
import javax.swing.SwingUtilities;
import javafx.ui.Canvas;
import javafx.ui.Cursor;
import javafx.ui.KeyEvent;
import javafx.ui.KeyStroke;
import javafx.ui.KeyModifier;
import javafx.ui.HorizontalAlignment;
import javafx.ui.VerticalAlignment;
import javafx.ui.XY;
import javafx.ui.filter.Filter;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.event.SGNodeListener;
import com.sun.scenario.scenegraph.event.SGNodeEvent;
import com.sun.scenario.scenegraph.event.SGMouseListener;
import com.sun.scenario.scenegraph.event.SGKeyListener;
import com.sun.scenario.scenegraph.event.SGMouseAdapter;
import com.sun.scenario.scenegraph.fx.FXNode;
import java.lang.System;

/**
 * Common base class for all objects that appear in a Canvas.
 */
public abstract class Node extends CanvasElement, Transformable {

    // fix me: these can't be static
    public static attribute MOUSE_PRESS:MouseEvent = null;
    public static attribute MOUSE_DRAG:MouseEvent = null;
    public static attribute MOUSE_DRAG_SCREEN:Point = null;

    private static function clamp(n:Number, min:Number, max:Number):Number{
       return Math.max(Math.min(n, max), min);
    }
    // private:

    // TODO MARK AS FINAL
    protected attribute mouseListener: SGMouseListener;

    protected function installMouseListener() {
        if (mouseListener == null) {
            selectable = true;
            var entered = false;
            mouseListener = SGMouseAdapter {
                    public function mouseClicked(e:MouseEvent, node:SGNode):Void {
                        focused = true;
                        if (onMouseClicked <> null) {
                            (onMouseClicked)(makeCanvasMouseEvent(e));
                            e.consume();
                        } else if (onMousePressed <> null) {
                            e.consume();
                        } else {
                            if (isSelectionRoot) {e.consume();}
                        }
                    }

                    public function mouseEntered(e:MouseEvent, node:SGNode):Void {
                        if (onMouseEntered <> null) {
                            (onMouseEntered)(makeCanvasMouseEvent(e));
                        }
                        if (isSelectionRoot) { e.consume(); }
                    }

                    public function mouseExited(e:MouseEvent, node:SGNode):Void {
                        if (onMouseExited <> null) {
                            (onMouseExited)(makeCanvasMouseEvent(e));
                        } 
                        if (isSelectionRoot) { e.consume(); }
                    }

                    public function mousePressed(e:MouseEvent, node:SGNode):Void {
                        focused = true;
                        Node.MOUSE_PRESS = e;        
                        var c = getCanvas();
                        //MOUSE_DRAG_SCREEN = getScreenLocation(c.getComponent());
                        if (onMousePressed <> null) {
                            (onMousePressed)(makeCanvasMouseEvent(e));
                            e.consume();
                        } else if (onMouseClicked <> null) {
                            e.consume();
                        } else {
                            if (isSelectionRoot) { e.consume(); }
                        }
                    }

                    public function mouseReleased(e:MouseEvent, node:SGNode):Void {
                        MOUSE_DRAG = null;
                        MOUSE_PRESS = null;
                        MOUSE_DRAG_SCREEN = null;
                        if (onMouseReleased <> null) {
                            (onMouseReleased)(makeCanvasMouseEvent(e));
                            e.consume();
                        } else if (onMousePressed <> null or onMouseClicked <> null) {
                            e.consume();
                        } else {
                            if (isSelectionRoot) {e.consume();}
                        }
                    }

                    public function mouseDragged(e:MouseEvent, node:SGNode):Void {
                        if (exportDrag) {
                            return;
                        }
                        var localPt = filterRoot.globalToLocal(e.getPoint(), null);

                        if (not (onMouseDragged == null and onMouseMoved == null and not isSelectionRoot)) {
                            e.consume();
                        }
                        if (onMouseDragged <> null) {
                            var dragDeltaX = 0;
                            var dragDeltaY = 0;
                            var localDragDeltaX = 0;
                            var localDragDeltaY = 0;
                            var prev = if (MOUSE_DRAG <> null)
                                            then MOUSE_DRAG
                                        else MOUSE_PRESS;
                            var screen = new java.awt.Point(0,0); //!!getScreenLocation(getCanvas().getComponent());
                            if (prev <> null) {
                               if (MOUSE_DRAG_SCREEN == null) {
                                   MOUSE_DRAG_SCREEN = screen;
                               }
                               var screenDeltaX = screen.x - MOUSE_DRAG_SCREEN.x;
                               var screenDeltaY = screen.y - MOUSE_DRAG_SCREEN.y;

                               dragDeltaX = e.getX() - prev.getX() + screenDeltaX;
                               dragDeltaY = e.getY() - prev.getY() + screenDeltaY;
                               var pt1 = new java.awt.geom.Point2D.Double(prev.getX(), prev.getY());
                               var pt2 = new java.awt.geom.Point2D.Double(prev.getX()+dragDeltaX, prev.getY()+dragDeltaY);
                               filterRoot.globalToLocal(pt1, pt1);
                               filterRoot.globalToLocal(pt2, pt2);
                               localDragDeltaX = pt2.getX().intValue() - pt1.getX().intValue();
                               localDragDeltaY = pt2.getY().intValue() - pt1.getY().intValue();
                            }
                            MOUSE_DRAG = e;
                            MOUSE_DRAG_SCREEN = screen;
                            var event = makeCanvasMouseEvent(e);
                            event.dragTranslation = XY{x: dragDeltaX, y: dragDeltaY};
                            event.localDragTranslation = XY{x: localDragDeltaX, y: localDragDeltaY};
                            if(onMouseDragged <> null)
                                (onMouseDragged)(event);
                        }
                    }

                    public function mouseMoved(e:MouseEvent, node:SGNode):Void {
                        //e.percolate = onMouseMoved == null and onMouseDragged == null and not isSelectionRoot;
                        if (onMouseMoved <> null) {
                            (onMouseMoved)(makeCanvasMouseEvent(e));
                        }
                    }
                };
            if (filterRoot <> null) {
                filterRoot.addMouseListener(mouseListener);
            }
        }
    }

    protected attribute keyListener: SGKeyListener;

    public function installKeyListener():Void { 
        if(keyListener == null) {
            selectable = true;
            var entered = false;
            keyListener = SGKeyListener {
                public function keyTyped(event:java.awt.event.KeyEvent, node:SGNode):Void {
                    if(onKeyTyped <> null) { onKeyTyped(makeKeyEvent(event)); }
                }
                public function keyPressed(event:java.awt.event.KeyEvent, node:SGNode):Void {
                    if(onKeyDown <> null) { onKeyDown(makeKeyEvent(event)); }
                }
                public function keyReleased(event:java.awt.event.KeyEvent, node:SGNode):Void {
                    if(onKeyUp <> null) { onKeyUp(makeKeyEvent(event)); }
                }
            };
            if (filterRoot <> null) {
                filterRoot.addKeyListener(keyListener);
            }

        }
    }
    protected function installFocusListener() {
        var canvas = this.getCanvas();
        if (focusListener == null and canvas.jsgpanel <> null) {
            var self = this;
            focusListener = FocusListener {
                    public function focusGained(e:FocusEvent):Void {
                        focused = true;
                    }
                    public function focusLost(e:FocusEvent):Void {
                        focused = false;
               }
            };
            canvas.jsgpanel.addFocusListener(focusListener);
        }
    }

    protected function makeKeyEvent(e:java.awt.event.KeyEvent):KeyEvent {
        var modifiers:KeyModifier[] = [
            if (e.isControlDown() ) KeyModifier.CTRL else null,
            if (e.isShiftDown()) KeyModifier.SHIFT else null,
            if (e.isMetaDown()) KeyModifier.META else
            if (e.isAltDown()) KeyModifier.ALT else null
            ];
        return KeyEvent {
            source: e
            keyStroke: KeyStroke.KEYBOARD.getKeyStroke(e.getKeyCode())
            modifiers: modifiers
            keyChar: if (e.getID() == java.awt.event.KeyEvent.KEY_TYPED) "{e.getKeyChar()}" else null
        };
    }

    protected function makeCanvasMouseEvent(e:MouseEvent) {
        //var info = MouseInfo.getPointerInfo();
        var pt = new java.awt.geom.Point2D.Double(e.getX(), e.getY());
        var c = getCanvas();
        var screenX = 0.0;
        var screenY = 0.0;
        // Temporary hack to allow Scenario3D to receive mouse events
        try {
            var loc = new java.awt.Point(0,0); //!!c.getComponent().getLocationOnScreen();
            screenX = loc.getX() + e.getX();
            screenY = loc.getY() + e.getY();
        } catch (ignore) {
        }
        var result = CanvasMouseEvent {
            modifiers: [if (e.isAltDown()) then KeyModifier.ALT else null,
                        if (e.isControlDown()) then KeyModifier.CTRL else null, 
                        if (e.isMetaDown()) then KeyModifier.META else null,
                        if (e.isShiftDown()) then KeyModifier.SHIFT else null]
            button: if (SwingUtilities.isLeftMouseButton(e)) then 1 
                    else if (SwingUtilities.isRightMouseButton(e)) then 3 else 2
            clickCount: e.getClickCount()
            localX: pt.getX()
            localY: pt.getY()
            x: e.getX()
            y: e.getY()
            screenx: screenX
            screeny: screenY
            //percolate: e.percolate // TODO: percolate not implemented
            percolate: false
            source: e
        };
        return result;
    }

    private attribute filterRoot: FXNode;
    public attribute clip: Clip;
    private attribute clipNode: VisualNode = bind if (clip == null) null else clip.shape on replace {
        if (clipNode <> null)
            filterRoot.setClip(clipNode.getVisualNode().getShape());
    };
    private attribute antialiasClip: Boolean  = bind if (clip == null) false else clip.antialias on replace {
        antialiasClipSet = true;
        if (filterRoot <> null)
            filterRoot.setClipAntialiased(antialiasClip);
    };
    private attribute antialiasClipSet = false;
    private attribute contentNode: SGNode;

    /*protected*/public attribute bounds: Rectangle2D;
    private attribute focusListener: FocusListener;
    
    private function getFX(obj:SGNode): Node {
        while (obj <> null) {
            var n:Node = obj.getAttribute("FX") as Node;
            if (n <> null) {
                return n;
            }
            obj = obj.getParent();
            // TODO: might need the code below if we support multiple parents
            /*************
            } else if (obj instanceof SGLeaf) {
                obj = (obj as SGLeaf).getParents()[0];
            } else {
                obj = null;
            }
            ********/
        }
        return null;
    }

    private static function getScreenLocation(component:java.awt.Component): java.awt.Point{
        var comp = component;
        while (comp <> null and not (comp instanceof java.awt.Frame) and 
               not (comp instanceof java.awt.Window) and
               not (comp instanceof java.applet.Applet)) {
            comp = comp.getParent();
        }
        //!!TODO what if comp is null????
        if(comp == null) {
            return component.getLocationOnScreen();
        } else {
        	return comp.getLocationOnScreen();
        }
    }

    private function updateEffect() {
        if (filterRoot <> null) {
            filterRoot.setEffect(filter.getImpl());
        }
    }

    // protected:
    public function getNode(): SGNode {
        if (filterRoot == null) {
            var canvas = this.getCanvas();
            this.onSetCanvas(canvas);
            if (focused) {
                canvas.focusedNode = this;
            }
            contentNode = this.createNode();
            if (contentNode == null) {
                throw new Exception("create node for class {this.getClass().getName()} returned null");
            }

            filterRoot = new FXNode(contentNode);
            filterRoot.putAttribute("FX", this);
            filterRoot.addNodeListener(Node.LISTENER);

            if (halign <> null) {
                filterRoot.setHorizontalAlignment(halign.id.intValue());
            }
            if (valign <> null) {
                filterRoot.setVerticalAlignment(valign.id.intValue());
            }
            if (clipNode <> null) {
                filterRoot.setClip(clipNode.getVisualNode().getShape());
            }
            if (antialiasClipSet) {
                filterRoot.setClipAntialiased(antialiasClip);
            }
            if (transform <> null and sizeof transform > 0) {
                for (t in transform) {
                    t.transformable = this;
                }
                updateTransform()
            }
            if (affineTransform <> null) {
                filterRoot.setTransform(affineTransform);
            }
            if (mouseListener <> null) {
                filterRoot.removeMouseListener(mouseListener);
                filterRoot.addMouseListener(mouseListener);
            }
            if (keyListener <> null) {
                filterRoot.removeKeyListener(keyListener);
                filterRoot.addKeyListener(keyListener);
            }
            //filterRoot.setPickable(selectable); // TODO: needed
            filterRoot.setVisible(visible);
            if (scaleToFitCanvas) {
                insert this into canvas.scaleToFitList;
            }
            if (this instanceof SizeableCanvasElement) { // hack
                var r = this as SizeableCanvasElement;
                if (r.sizeToFitCanvas) {
                    insert r into canvas.sizeToFitList;
                }
            }
            if (opacitySet and opacity <> 1.0) {
                filterRoot.setOpacity(clamp(opacity, 0, 1).floatValue());
            }
            updateEffect();
            if (toolTipText <> null) {
                //alignmentFilter.setToolTipText(toolTipText); // TODO: hmm
            }
            if (id <> null) {
                contentNode.setID(id);
            }
            var b = filterRoot.getBounds();
            currentX = b.getX();
            currentY = b.getY();
            currentWidth = b.getWidth();
            currentHeight = b.getHeight();
            
            updateCursor();
        }
        return filterRoot;
    }
    protected abstract function createNode(): SGNode;
    
    public attribute hover: Boolean;

    //TODO: implement properly...
    public attribute toolTipText: String on replace  {
    /*
        if (alignmentFilter <> null) {
            alignmentFilter.setToolTipText(value);
        }
    */
    };

    // public:
    /** Determines whether this node responds to mouse events, or other picking functions. */
    public attribute selectable: Boolean  = true on replace  {
        //alignmentFilter.setPickable(selectable);
    };
    public attribute isSelectionRoot: Boolean on replace {
        if (isSelectionRoot) {
            this.installMouseListener();
        }
    };

    public attribute exportDrag: Boolean;
    public attribute exportAsDrag: function(): CanvasDragEvent;
    /**
     * An optional Filter chain that will be applied to this node. 
     */
    public attribute filter: Filter on replace {
        updateEffect();
    };
    /** A number between 0 and 1, 0 being transparent and 1 opaque. */
    public attribute opacity: Number = 1 on replace {
        if (opacitySet == false and opacity <> 1) // ignore initial opaque setting
            opacitySet = true;
        if (filterRoot <> null) {
            filterRoot.setOpacity(clamp(opacity, 0, 1).floatValue());
        }
    }

    public attribute opacitySet = false;

    /** If true this node will be scaled to the size of its containing canvas. */
    public attribute scaleToFitCanvas: Boolean  on replace {
        if (this.parentCanvasElement <> null) {
            var canvas = this.getCanvas();
            if (scaleToFitCanvas) {
                insert this into canvas.scaleToFitList;
            } else {
                delete this from canvas.scaleToFitList;
            }
        }
    };
    /** Determines the horizontal alignment of this node relative to its origin. */
    public attribute halign: HorizontalAlignment on replace  {
        if (halign == null) {
            halign = HorizontalAlignment.LEADING;
        }
        else if (filterRoot <> null) {
            filterRoot.setHorizontalAlignment(halign.id.intValue());
        }
    }
    /** Determines the vertical alignment of this node relative to its origin. */
    public attribute valign: VerticalAlignment on replace {
        if (valign == null) {
            valign = VerticalAlignment.TOP;
        }
        else if (filterRoot <> null) {
            filterRoot.setVerticalAlignment(valign.id.intValue());
        }
    };

    /** 
     * Determines whether this node is visible in the canvas. If set to 
     * false, this node and anything it contains will not be painted and
     * will not receive events.
     */
    public attribute visible: Boolean = true on replace {
        if (filterRoot <> null) {
            filterRoot.setVisible(visible);
        }
    };

    public attribute onKeyDown: function(e:KeyEvent):Void on replace {
        installKeyListener();
    };
    public attribute onKeyUp: function(e:KeyEvent):Void on replace {
        installKeyListener();
    };
    public attribute onKeyTyped: function(e:KeyEvent):Void on replace {
        installKeyListener();
    };

    /**
     * Sets the focusable state of this Node to the specified value. This
     * value overrides the Node's default focusability, wich is false.
     */
    public attribute focusable: Boolean = false on replace {
        if (focusable) {
            installFocusListener();
            var canvas = this.getCanvas();
            canvas.jsgpanel.setFocusable(focusable);
        }
    };
    public function requestFocus() {
        if (focusable==true) {
            var canvas = this.getCanvas();
            canvas.requestFocus();
            canvas.focusedNode = this;
        }
    };
    public attribute focused: Boolean = false on replace {
        if (focusable and focused) {
            requestFocus();
        } else {
            var canvas = this.getCanvas();
            if (canvas <> null and canvas.focusedNode == this) {
                canvas.focusedNode = null;
            }
        }
    };

    attribute dragCount: Number on replace {
        if (dragCount > 0) {
            this.doDragExport();
        }
    };

    public attribute onDrop: function(e:CanvasDropEvent):Void;
    public attribute canAcceptDrop: function(e:CanvasDropEvent):Boolean;
    public attribute onDragEnter: function(e:CanvasDropEvent):Void;
    public attribute onDragExit: function(e:CanvasDropEvent):Void;

    public function handleDrop(e:CanvasDropEvent):Boolean{
        if (onDrop <> null) {
            var pt = new java.awt.Point(e.x.intValue(), e.y.intValue());
            filterRoot.globalToLocal(pt, pt);
            e.localX = pt.getX();
            e.localY = pt.getY();
            (this.onDrop)(e);
            return true;
        }
        return false;
    }
    public function handleAcceptDrop(e:CanvasDropEvent):Boolean {
        if (canAcceptDrop <> null) {
            var pt = new java.awt.Point(e.x.intValue(), e.y.intValue());
            filterRoot.globalToLocal(pt, pt);
            e.localX = pt.getX();
            e.localY = pt.getY();
            return (this.canAcceptDrop)(e);
        }
        return onDrop <> null;
    }
    public function handleDragEnter(e:CanvasDropEvent):Boolean{
        if (onDragEnter <> null) {
            var pt = new java.awt.Point(e.x.intValue(), e.y.intValue());
            filterRoot.globalToLocal(pt, pt);
            e.localX = pt.getX();
            e.localY = pt.getY();
            (this.onDragEnter)(e);
            return true;
        }
        return false;
    }
    public function handleDragExit(e:CanvasDropEvent):Boolean{
        if (onDragExit <> null) {
            var pt = new java.awt.Point(e.x.intValue(), e.y.intValue());
            filterRoot.globalToLocal(pt, pt);
            e.localX = pt.getX();
            e.localY = pt.getY();
            (this.onDragExit)(e);
            return true;
        }
        return false;
    }
    function doDragExport():Void {
        var e = (this.exportAsDrag)();
        var c = this.getCanvas();
        c.doExportAsDrag(e);
    }
    
    /** 
     * <code>attribute onMouseClicked: function(e:CanvasMouseEvent)</code><br></br>
     * Optional handler for mouse click events 
     */
    public attribute onMouseClicked: function(e:CanvasMouseEvent):Void on replace {
        this.installMouseListener();
    };
    /** 
     * <code>attribute onMouseEntered: function(e:CanvasMouseEvent)</code><br></br>
     * Optional handler for mouse enter events. 
     */
    public attribute onMouseEntered: function(e:CanvasMouseEvent):Void on replace {
        this.installMouseListener();
    };
    /** 
     * <code>attribute onMouseExited: function(e:CanvasMouseEvent)</code><br></br>
     * Optional handler for mouse exit events. 
     */
    public attribute onMouseExited: function(e:CanvasMouseEvent):Void on replace {
        this.installMouseListener();
    };
    /** 
     * <code>attribute onMousePressed: function(e:CanvasMouseEvent)</code><br></br>
     * Optional handler for mouse press events. 
     */
    public attribute onMousePressed: function(e:CanvasMouseEvent):Void on replace {
        this.installMouseListener();
    };
    /** 
     * <code>attribute onMouseReleased: function(e:CanvasMouseEvent)</code><br></br>
     * Optional handler for mouse release events. 
     */
    public attribute onMouseReleased: function(e:CanvasMouseEvent):Void on replace {
        this.installMouseListener();
    };
    /** 
     * <code>attribute onMouseDragged: function(e:CanvasMouseEvent)</code><br></br>
     * Optional handler for mouse drag events. 
     */
    public attribute onMouseDragged: function(e:CanvasMouseEvent):Void on replace {
        this.installMouseListener();
    };
    /** 
     * <code>attribute onMouseMoved: function(e:CanvasMouseEvent)</code><br></br>
     * Optional handler for mouse motion events. 
     */
    public attribute onMouseMoved: function(e:CanvasMouseEvent):Void on replace {
        this.installMouseListener();
    };
    /** Read-only attribute returning the current x coordinate of this node relative to its parent. */
    public attribute currentX: Number;
    /** Read-only attribute returning the current y coordinate of this node relative to its parent. */
    public attribute currentY: Number;
    /** Read-only attribute returning the current width of this node. */
    public attribute currentWidth: Number;
    /** Read-only attribute returning the current height of this node. */
    public attribute currentHeight: Number;

    private function updateCursor():Void {
        if (filterRoot <> null) {
            filterRoot.setCursor(if (cursor <> null) cursor.getCursor() else null);
        }
    }
    
    /** Optional cursor to use when the mouse is over this node. */
    public attribute cursor: Cursor on replace {
        updateCursor();
    };
    public function getGlobalBounds(): Rectangle2D {
        return null; // TODO: is this used anywhere?
    }
    
    public attribute id: String on replace {
        if (contentNode <> null) {
            contentNode.setID(id);
        }   
    };

    public static attribute LISTENER:FXNodeListener = FXNodeListener {};
    
    init {
        onTransformChanged = function (t:AffineTransform):Void {
            if (filterRoot <> null) {
                filterRoot.setTransform(t);
            } 
        }
    }

    // TODO: remove (this is a hack workaround, not currently used)
    /*
    function realign():Void {
        if (filterRoot <> null) {
            if (halign <> null and halign <> HorizontalAlignment.LEADING) {
                filterRoot.setHorizontalAlignment(halign.id.intValue());
            }
            if (valign <> null and valign <> VerticalAlignment.TOP) {
                filterRoot.setVerticalAlignment(valign.id.intValue());
            }
        }
    }
    */
}
