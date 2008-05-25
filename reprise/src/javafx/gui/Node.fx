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

import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.event.SGNodeListener;
import com.sun.scenario.scenegraph.event.SGNodeEvent;
import com.sun.scenario.scenegraph.event.SGKeyListener;
import com.sun.scenario.scenegraph.event.SGMouseListener;
import com.sun.scenario.scenegraph.event.SGFocusListener;
import com.sun.scenario.scenegraph.event.SGMouseAdapter;
import com.sun.scenario.scenegraph.fx.FXNode;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.event.FocusEvent;
import java.awt.event.MouseWheelEvent;
import java.lang.Math;
import java.math.BigInteger;
import javafx.gui.effect.Effect;
import com.sun.javafx.gui.AccessHelper;


// PENDING_DOC_REVIEW
/**
 * Base class for scene graph nodes. The {@code Node} class defines a "local"
 * coordinate system like the one used by AWT/Swing: {@code x} increases to the
 * right, {@code y} increases downwards.
 * 
 * @profile common
 */     
public abstract class Node {

    abstract function createSGNode() : SGNode;

    // The following attributes are updated each time this Node's
    // ComponentListener notifies us that the fx.FXNode's bounds
    // have changed.  See Canvas.fx for the details.  The value
    // of cachedBounds includes the Node's transform, the value
    // of cachedXYWH does not.

    attribute cachedBounds:java.awt.geom.Rectangle2D;  // getSGNode().getBounds()
    attribute cachedXYWH:java.awt.geom.Rectangle2D;    // getFXNode().getBounds()

    private function createNodeListener():SGNodeListener {
        SGNodeListener {
            public function boundsChanged(e:SGNodeEvent):Void {
                cachedBounds = null;
                cachedXYWH = null;
                requestLayout();
            }
        }
    }

    private attribute fxNode:FXNode = null;

    function getFXNode():FXNode {
        if (fxNode == null) {
            fxNode = new FXNode(createSGNode());
            fxNode.addNodeListener(createNodeListener());
        }
        fxNode
    }

    function getSGNode():SGNode {
        getFXNode().getLeaf()
    }

    attribute parent:Node = null;
    

    // PENDING_DOC_REVIEW
    /**
     * Returns the current x coordinate of the {@code Node} origin.
     * This value does not include the node's transform.
     *
     * @profile common
     */         
    public bound function getX():Number { 
        if (cachedXYWH <> null) cachedXYWH.getX() else getSGNode().getBounds().getX();
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns the current y coordinate of the {@code Node} origin.
     * This value does not include the node's transform.
     *
     * @profile common
     */         
    public bound function getY():Number { 
        if (cachedXYWH <> null) cachedXYWH.getY() else getSGNode().getBounds().getY();
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns the current width of this {@code Node}.
     * This value does not include the node's transform.
     *
     * @profile common
     */     
    public bound function getWidth():Number { 
        if (cachedXYWH <> null) cachedXYWH.getWidth() else getSGNode().getBounds().getWidth();
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns the current height of this {@code Node}.
     * This value does not include the node's transform.
     *
     * @profile common
     */     
    public bound function getHeight():Number { 
        if (cachedXYWH <> null) cachedXYWH.getHeight() else getSGNode().getBounds().getHeight();
    }

    /**
     * Returns the current x coordinate of the {@code Node} origin.
     * This value includes the node's transform.
     *
     * @profile common
     */         
    public bound function getBoundsX() {
        if (cachedBounds <> null) cachedBounds.getX() else getFXNode().getBounds().getX();
    }

    /**
     * Returns the current y coordinate of the {@code Node} origin.
     * This value includes the node's transform.
     *
     * @profile common
     */         
    public bound function getBoundsY() {
        if (cachedBounds <> null) cachedBounds.getY() else getFXNode().getBounds().getY();
    }

    /**
     * Returns the current width of this {@code Node}.
     * This value includes the node's transform.
     *
     * @profile common
     */     
    public bound function getBoundsWidth() {
        if (cachedBounds <> null) cachedBounds.getWidth() else getFXNode().getBounds().getWidth();
    }

    /**
     * Returns the current height of this {@code Node}.
     * This value includes the node's transform.
     *
     * @profile common
     */     
    public bound function getBoundsHeight() {
        if (cachedBounds <> null) cachedBounds.getHeight() else getFXNode().getBounds().getHeight();
    }


    // PENDING_DOC_REVIEW
    /**
     * Returns the parent {@code Node} of this {@code Node}. 
     * 
     * @profile common
     */         
    public function getParent():Node { parent }

    attribute layoutX:Number on replace {
        getFXNode().setTranslateX(translateX + layoutX);
    }

    attribute layoutY:Number on replace {
        getFXNode().setTranslateY(translateY + layoutY);
    }

    attribute needsLayout:Boolean = false;
    
    /* Set the needsLayout attribute of every ancestor of this Node.
     * If this Node is a Group/CustomNode then set its needsLayout
     * attribute as well.  We short-circuit when we find a node
     * who's needsLayout bit is already set.
     */
    function requestLayout():Void {
        var n:Node = this;
        while (n <> null) {
            if ((n instanceof Group) or (n instanceof CustomNode)) { 
                n.needsLayout = true; 
            }
            n = n.getParent();
            if (n <> null and n.needsLayout) { break; }
        }
        getSGNode().getPanel().revalidate();
    }

    /**
     * @profile common
     */         
    public attribute id:String on replace {
        getSGNode().setID(id);
        getSGNode().putAttribute("FXNode", this); // see Node#lookup()
    }

    /**
     * Returns this Node if its id matches.  Otherwise, if this 
     * Node is a Group, then return the first descendant whose id
     * matches.
     * 
     * @profile common
     */         
     public function lookup(id:String):Node { 
         var node:SGNode = getSGNode().lookup(id);
         if (node <> null) node.getAttribute("FXNode") as Node else null;
     }

    // PENDING_DOC_REVIEW
    /**
     * Defines if this {@code Node} is shown or hidden.
     *
     * @profile common
     */      
    public attribute visible:Boolean = true on replace {
        getSGNode().setVisible(visible);
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the mouse cursor for this {@code Node}. 
     *
     * @profile common conditional cursor
     */         
    public attribute cursor:Cursor on replace {
        getSGNode().setCursor(if (cursor <> null) cursor.getAWTCursor() else null);
    }

    /**
     * @profile common conditional groupOpacity
     * In the common profile opacity is supported unconditionally on all nodes 
     * except Group, where on some platforms the implementation might try 
     * to deliver the result with best effort.  The application should check 
     * groupOpacity feature value on the particular platform to see
     * how group opacity works. Possible values are:
     *    <ul>
     *      <li><code>FULLY_SUPPORTED</code> - for full support of the group
     *               opacity</li>
     *      <li><code>BEST_EFFORT</code> - for a support which does not guarantee
     *               an exact appearance of the group opacity (e.g. overlapping
     *               graphics nodes might be visualized differently</li>
     *    </ul>
     * 
     */         
    public attribute opacity:Number = 1.0 on replace {
        getFXNode().setOpacity(opacity.floatValue());
    }

    function updateFXNodeTransform():Void {
        var xform = new AffineTransform();
        for (t in transform) {
            xform.concatenate(t.getAffineTransform());
        }
        getFXNode().setTransform(xform);
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the array of {@link Transform} objects to be applied when 
     * this {@code Node} is shown.
     *
     * @profile common
     */         
    public attribute transform:Transform[] on replace oldElts[a..b] = newElts {
        for (elt in newElts) { elt.node = this; }
        updateFXNodeTransform();
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the distance by which coordinates are translated in the
     * X axis direction of this {@code Node}.
     *
     * @profile common
     */         
    public attribute translateX:Number on replace {
        getFXNode().setTranslateX(translateX + layoutX);
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the distance by which coordinates are translated in the
     * Y axis direction of this {@code Node}.
     *
     * @profile common
     */     
    public attribute translateY:Number on replace {
        getFXNode().setTranslateY(translateY + layoutY);
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the rotation anchor point of this {@code Node}.
     *
     * @profile common
     */     
    public attribute anchorX:Number on replace {
        getFXNode().setAnchorX(anchorX);
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the rotation anchor point of this {@code Node}.
     *
     * @profile common
     */     
    public attribute anchorY:Number on replace {
        getFXNode().setAnchorY(anchorY);
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the factor by which coordinates are scaled along the   
     * X axis direction of this {@code Node}.
     *
     * @profile common
     */     
    public attribute scaleX:Number = 1.0 on replace {
        getFXNode().setScaleX(scaleX);
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the factor by which coordinates are scaled along the   
     * Y axis direction of this {@code Node}.
     *
     * @profile common
     */     
    public attribute scaleY:Number = 1.0 on replace {
        getFXNode().setScaleY(scaleY);
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the multiplier by which coordinates are shifted in the
     * direction of the positive X axis as a factor of their Y coordinate.
     *
     * @profile common
     */     
    public attribute shearX:Number on replace {
        getFXNode().setShearX(shearX);
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the multiplier by which coordinates are shifted in the
     * direction of the positive Y axis as a factor of their X coordinate
     *
     * @profile common
     */     
    public attribute shearY:Number on replace {
        getFXNode().setShearY(shearY);
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the angle of rotation measured in degrees for this {@code Node}.
     *
     * @profile common
     */     
    public attribute rotation:Number on replace {
        getFXNode().setRotation(Math.toRadians(rotation));
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the {@link HorizontalAlignment} of this {@code Node}.
     *
     * @profile common
     */     
    public attribute horizontalAlignment:HorizontalAlignment = HorizontalAlignment.LEADING on replace {
        getFXNode().setHorizontalAlignment(horizontalAlignment.getToolkitValue());
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the {@link VerticalAlignment} of this {@code Node}.
     *
     * @profile common
     */     
    public attribute verticalAlignment:VerticalAlignment = VerticalAlignment.TOP on replace {
        getFXNode().setVerticalAlignment(verticalAlignment.getToolkitValue());
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the clipping shape of this {@code Node}. 
     *
     * @profile common conditional clip
     */     
    public attribute clip:Shape on replace {
        getFXNode().setClip(clip.getSGAbstractShape().getShape());
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines if the soft clipping is used by this {@code Node}.
     * 
     * @profile common conditional clip
     */         
    public attribute clipAntialiased:Boolean on replace {
        getFXNode().setClipAntialiased(clipAntialiased);
    }

    /**
     * @profile common
     */         
    public attribute cache:Boolean on replace {
        getFXNode().setCachedAsBitmap(cache);
    }

    public attribute effect:Effect on replace {
        getFXNode().setEffect(AccessHelper.getEffectImpl(effect));
    }

    public function toFront(): Void {
        var group:Group = if (parent instanceof Group) parent as Group else null;
        if ((group <> null) and (group.content[0] <> this)) {
            group.content = [this, group.content[n | n <> this]];
        }
    }

    public function toBack(): Void {
        var group:Group = if (parent instanceof Group) parent as Group else null;
        if ((group <> null) and (group.content[sizeof group.content -1] <> this)) {
            group.content = [group.content[n | n <> this], this];
        }
    }

    /**
     * @profile common conditional mouse
     */         
    public attribute blocksMouse:Boolean = false on replace {
        getFXNode().getLeaf().setMouseBlocker(blocksMouse);
    }
    
    private attribute mouseOver:Boolean = false;

    // PENDING_DOC_REVIEW
    /**
     * Returns {@code true} if the mouse is over this {@code Node}. 
     *
     * @profile common conditional mouse
     */     
    public bound function isMouseOver():Boolean { mouseOver }

    private attribute modifiers:Integer = 0;

    /**
     * Returns {@code true} if the specified mouse button is pressed.
     *
     * @param the nubmer of mouse button to be checked
     * 
     * @profile common conditional mouse
     */     
    public function isMouseButtonDown(buttonNumber:Integer):Boolean {
        // java.awt.InputEvent.BUTTON{1,2,3}_MASK == 16,8,4 -- bits 4,3,2
        (buttonNumber > 0) and (buttonNumber < 4) and BigInteger.valueOf(modifiers).testBit(5 - buttonNumber);
    }

    private attribute dragAnchorXY:Point;

    private function createMouseEvent(e:java.awt.event.MouseEvent) {
        MouseEvent{ awtMouseEvent:e node:this dragAnchorXY:dragAnchorXY }
    }

    private function createMouseListener():SGMouseListener {
        SGMouseAdapter {
            private function call(f:function(e:MouseEvent):Void, e:java.awt.event.MouseEvent) {
                modifiers = e.getModifiersEx();
                if (f <> null) f(createMouseEvent(e));
            }

            public function mouseClicked(e:java.awt.event.MouseEvent, node:SGNode):Void {
                call(onMouseClicked, e);
            }

            public function mouseEntered(e:java.awt.event.MouseEvent, node:SGNode):Void {
                mouseOver = true;
                call(onMouseEntered, e);
            }

            public function mouseExited(e:java.awt.event.MouseEvent, node:SGNode):Void {
                mouseOver = false;
                call(onMouseExited, e);
            }

            public function mousePressed(e:java.awt.event.MouseEvent, node:SGNode):Void {
                dragAnchorXY = e.getPoint();
                call(onMousePressed, e);
            }

            public function mouseReleased(e:java.awt.event.MouseEvent, node:SGNode):Void {
                call(onMouseReleased, e);
                dragAnchorXY = null;
            }

            public function mouseDragged(e:java.awt.event.MouseEvent, node:SGNode):Void {
                call(onMouseDragged, e);
            }

            public function mouseMoved(e:java.awt.event.MouseEvent, node:SGNode):Void {
                call(onMouseMoved, e);
            }

            public function mouseWheelMoved(e:MouseWheelEvent, node:SGNode):Void {
                call(onMouseWheelMoved, e);
            }
        }
    }

    private attribute mouseListenerInstalled:Boolean = false;

    private function iml():Void { // install mouse listener
        if (not mouseListenerInstalled) {
            getSGNode().addMouseListener(createMouseListener());
            mouseListenerInstalled = true;
        }
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines a function to be notified when a mouse button has been clicked 
     * (pressed and released) on this {@code Node}.
     */
    public attribute onMouseClicked: function(e:MouseEvent):Void on replace { iml(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines a function to be notified when a mouse button is pressed 
     * on this {@code Node} and then dragged.
     */
    public attribute onMouseDragged: function(e:MouseEvent):Void on replace { iml(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines a function to be notified when the mouse enters this {@code Node}. 
     */
    public attribute onMouseEntered: function(e:MouseEvent):Void on replace { iml(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines a function to be notified when the mouse exits this {@code Node}. 
     */
    public attribute onMouseExited: function(e:MouseEvent):Void on replace { iml(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines a function to be notified when mouse cursor has been moved onto 
     * this {@code Node} but no buttons have been pushed. 
     */
    public attribute onMouseMoved: function(e:MouseEvent):Void on replace { iml(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines a function to be notified when a mouse button 
     * has been pressed on this {@code Node}.
     */
    public attribute onMousePressed: function(e:MouseEvent):Void on replace { iml(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines a function to be notified when a mouse button 
     * has been released on this {@code Node}.
     */
    public attribute onMouseReleased: function(e:MouseEvent):Void on replace { iml(); }

    /**
     * Defines a function to be notified when the mouse scroll wheel has moved.
     */
    public attribute onMouseWheelMoved: function(e:MouseEvent):Void on replace { iml(); }


    // PENDING_DOC_REVIEW
    /**
     * Requests that this {@code Node} get the input focus, and that this
     * {@code Node}'s top-level ancestor become the focused Window.
     */
    public function requestFocus():Void {
        getSGNode().requestFocus();
    }

    private attribute focused = false;

    // PENDING_DOC_REVIEW
    /**
     * Returns {@code true} if this {@code Node} is the focus owner.
     */
    public bound function hasFocus():Boolean { focused }

    private function createKeyEvent(e:java.awt.event.KeyEvent) {
        KeyEvent{ awtKeyEvent:e node:this }
    }

    private function createKeyListener():SGKeyListener {
        SGKeyListener {
            private function call(f:function(e:KeyEvent):Void, e:java.awt.event.KeyEvent) {
                if (f <> null) f(createKeyEvent(e));
            }
            public function keyTyped(e:java.awt.event.KeyEvent, node:SGNode):Void { 
                call(onKeyTyped, e);
            }
            public function keyPressed(e:java.awt.event.KeyEvent, node:SGNode):Void { 
                call(onKeyPressed, e);
            }
            public function keyReleased(e:java.awt.event.KeyEvent, node:SGNode):Void { 
                call(onKeyReleased, e);
            }
        }
    }

    private function createFocusListener():SGFocusListener {
        SGFocusListener {
            public function focusGained(e:FocusEvent, node:SGNode):Void { }
            public function focusLost(e:FocusEvent, node:SGNode):Void { }
        }
    }

     // PENDING_DOC_REVIEW
     /**
      * Defines whether this {@code Node} can be focused.
      *
      * @profile common
      */     
    public bound function isFocusable():Boolean {
        keyListenerInstalled and visible;
    }

    private attribute keyListenerInstalled:Boolean = false;

    private function ikl():Void { // install key,focus listeners
        if (not keyListenerInstalled) {
            var node = getSGNode();
            node.addKeyListener(createKeyListener());
            node.addFocusListener(createFocusListener());
            keyListenerInstalled = true;
        }
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines a function to be notified when a key has been typed. 
     * 
     * @profile common conditional keyboard
     */     
    public attribute onKeyPressed: function(e:KeyEvent):Void on replace { ikl(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines a function to be notified when a key has been released.
     *
     * @profile common conditional keyboard
     */     
    public attribute onKeyReleased: function(e:KeyEvent):Void on replace { ikl(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines a function to be notified when a key has been typed.
     *
     * @profile common conditional keyboard
     */     
    public attribute onKeyTyped: function(e:KeyEvent):Void on replace { ikl(); }

}
