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

package javafx.input;

import java.awt.geom.Point2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import javafx.scene.Node;

/**
 * @treatasprivate implementation detail
 */
public function createMouseEvent(node: Node,
                                 awtMouseEvent: java.awt.event.MouseEvent,
                                 dragAnchorXY: Point) {

    MouseEvent{node: node
               awtMouseEvent: awtMouseEvent
               dragAnchorXY: dragAnchorXY};
}

/**
 * The mouse (pointer's) location is available relative to several
 * coordinate systems: getX(),getY() - relative to the origin of the
 * MouseEvent's node, getStageX(),getStageY() - relative to to the
 * origin of the {@code Stage} that contains the node,
 * getScreenX(),getScreenY() - relative to origin of the screen that
 * contains the mouse pointer, getDragX(), getDragY() - if the
 * MouseEvent is part of a press-drag-release gesture, then relative
 * to the location of the press event, otherwise 0.
 * 
 * @profile common conditional mouse
 */
public class MouseEvent {

    public attribute node: Node;

    private attribute localXY: Point2D = null;

    private attribute screenXY: Point = null;

    attribute awtMouseEvent: java.awt.event.MouseEvent;

    attribute dragAnchorXY: Point = null;

    private function getLocalXY():Point2D { 
        if (localXY == null) { 
            localXY = node.impl_getSGNode().globalToLocal(awtMouseEvent.getPoint(), null);
        }
        return localXY;
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns the horizontal x position of the event relative to the 
     * origin of the MouseEvent's node.
     *
     * @profile common conditional mouse
     */     
    public function getX():Number { getLocalXY().getX() }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns the vertical y position of the event relative to the 
     * origin of the MouseEvent's node.
     *
     * @profile common conditional mouse
     */         
    public function getY():Number { getLocalXY().getY() }

    private function getScreenXY():Point { 
        if (screenXY == null) { 
            screenXY = MouseInfo.getPointerInfo().getLocation();
        }
        return screenXY;
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns the absolute horizontal x position of the event.
     *
     * @profile common conditional mouse
     */     
    public function getScreenX():Number { getScreenXY().getX() }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns the absolute vertical y position of the event.
     *
     * @profile common conditional mouse
     */         
    public function getScreenY():Number { getScreenXY().getY() }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns the x offset of the event relative to the most recent press event
     * if the {@code MouseEvent} is part of a press-drag-release gesture,
     * otherwise returns {@code 0}.
     *
     * @profile common conditional mouse
     */         
    public function getDragX():Number {
        if (dragAnchorXY == null) 0 else awtMouseEvent.getX() - dragAnchorXY.getX();
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns the y offset of the event relative to the most recent press event
     * only if the {@code MouseEvent} is part of a press-drag-release gesture,
     * otherwise returns {@code 0}.
     *
     * @profile common conditional mouse
     */         
    public function getDragY():Number {
        if (dragAnchorXY == null) 0 else awtMouseEvent.getY() - dragAnchorXY.getY();
    }

    /**
     * Returns the number of clicks the mouse wheel was rotated.
     */         
    public function getWheelRotation():Number {
        if (awtMouseEvent instanceof MouseWheelEvent) (awtMouseEvent as MouseWheelEvent).getWheelRotation() else 0;
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns the horizontal x position of the event relative to the 
     * origin of the {@code Stage} that contains the MouseEvent's node.
     *
     * @profile common conditional mouse
     */         
    public function getStageX():Number { awtMouseEvent.getX() }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns the vertical y position of the event relative to the 
     * origin of the {@code Stage} that contains the MouseEvent's node.
     *
     * @profile common conditional mouse
     */         
    public function getStageY():Number { awtMouseEvent.getY() }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns which, if any, of the mouse buttons has changed state.
     *
     * @profile common conditional mouse
     */     
    public function getButton():Integer { awtMouseEvent.getButton() }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns the number of mouse clicks associated with this event.
     *
     * @profile common conditional mouse
     */     
    public function getClickCount():Integer { awtMouseEvent.getClickCount() }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns whether or not the Shift modifier is down on this event. 
     *
     * @profile common conditional mouse
     */     
    public function isShiftDown():Boolean { awtMouseEvent.isShiftDown() }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns whether or not the Control modifier is down on this event.
     *
     * @profile common conditional mouse
     */     
    public function isControlDown():Boolean { awtMouseEvent.isControlDown() }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns whether or not the Alt modifier is down on this event.
     *
     * @profile common conditional mouse
     */     
    public function isAltDown():Boolean { awtMouseEvent.isAltDown() }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns whether or not the Meta modifier is down on this event.
     *
     * @profile common conditional mouse
     */     
    public function isMetaDown():Boolean { awtMouseEvent.isMetaDown() }

    // PENDING_DOC_REVIEW
    /**
     * Returns whether or not this mouse event is the popup menu
     * trigger event for the platform.
     * <p><b>Note</b>: Popup menus are triggered differently
     * on different systems. Therefore, {@code isPopupTrigger}
     * should be checked in both {@code onMousePressed}
     * and {@code mouseReleased} for proper cross-platform functionality.
     *
     * @profile common conditional mouse
     */     
    public function isPopupTrigger():Boolean{ awtMouseEvent.isPopupTrigger() }

}
