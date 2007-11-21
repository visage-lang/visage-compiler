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

import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.event.SGNodeEvent;
import com.sun.scenario.scenegraph.event.SGNodeListener;
//TODO CHange to work with compiler
/*******************************
import net.java.javafx.type.Attribute;
import net.java.javafx.type.AttributeValueAccessListener;
import net.java.javafx.type.Module;
import net.java.javafx.type.ModuleAware;
import net.java.javafx.type.Type;
import net.java.javafx.type.Value;
import net.java.javafx.type.ValueList;
************************/

//public class FXNodeListener implements SGNodeListener, ModuleAware, AttributeValueAccessListener {
public class FXNodeListener implements SGNodeListener {
/**************************************************************************
    Module mModule;

    public void setModule(Module module) {
        if (module == null) {
            mModule = null;
            return;
        }
        mModule = module;
        Type nodeType = mModule.getType("javafx.ui.canvas.Node");
        if (nodeType == null) {
            // backward compatibility: remove this later
            nodeType = mModule.getType("Node");
        }
        mBounds = nodeType.getAttribute("bounds");
        mX = nodeType.getAttribute("currentX");
        mY = nodeType.getAttribute("currentY");
        mWidth = nodeType.getAttribute("currentWidth");
        mHeight= nodeType.getAttribute("currentHeight");
        mTransformGroup = nodeType.getAttribute("transformFilter");
        mHalign = nodeType.getAttribute("halign");
        mValign = nodeType.getAttribute("valign");
    }

    public void attributeValueAccess(Value instance,
                                     Attribute attr,
                                     int index) {
    }
    
    public void attributeCardinalityAccess(Value instance,
                                           Attribute attr) {
        if (attr == mX || attr == mY || attr == mWidth || attr == mHeight) {
            //System.out.println("access: " + attr.getName());
            if (instance.getClientProperty("FX.nodelistener") != this) {
                instance.putClientProperty("FX.nodelistener", this);
                Value v = instance.getAttribute(mTransformGroup, 0);
                if (v != null) {
                    SGNode tg = (SGNode)v.get();
                    if (tg != null) {
                        tg.removeNodeListener(this);
                        tg.addNodeListener(this);
                    }
                }
            }
        }
    }

    Attribute mBounds;
    Attribute mUnderscoreBounds;
    Attribute mX;
    Attribute mY;
    Attribute mWidth;
    Attribute mHeight;
    Attribute mTransformGroup;
    Attribute mTranslateX;
    Attribute mTranslateY;
    Attribute mValign;
    Attribute mHalign;

    ValueList mTop;
    ValueList mMiddle;
    ValueList mBottom;

    ValueList mLeading;
    ValueList mCenter;
    ValueList mTrailing;
******************************************/

    Object getFX(SGNode obj) {
        Object result = null;
        while (obj != null) {
            result = obj.getAttribute("FX");
            if (result != null) {
                return result;
            }
            obj = obj.getParent();
            // TODO: maybe the following is needed when we support multiple
            // parents on SGNode?
            /*
            } else if (obj instanceof ZVisualComponent) {
                ZNode[] p = ((ZVisualComponent)obj).getParents();
                if (p.length > 0) {
                    obj = p[0];
                } else {
                    obj = null;
                }
            } else {
                obj = null;
            }
             */
        }
        return result;
    }

    public void boundsChanged(SGNodeEvent e) {
        if (e.isConsumed()) {
            //System.out.println("event was consumed");
            return;
        }
        SGNode n = e.getNode();
        if (n == null) {
            return;
        }
        final Object src = e.getSource();
        if (true) {
            if (src != n) {
                // TODO: not sure why the following was there...
                /*
                if (src instanceof ZClipGroup) {
                    //e.consume();
                }
                */
                return;
            }
        }
        // TODO: is the following needed?
        /*
        if (e.getProperty("FX.nodelistener") == this) {
            //System.out.println("already processed");
            return;
        }
        e.putProperty("FX.nodelistener", this);
         */
// TODO 
/*********************************************************
        Rectangle2D bounds = n.getBounds();
        Value value = (Value)n.getAttribute("FX");
        if (value != null) {
            Value b = (Value)value.getAttribute(mBounds, 0);
            Rectangle2D oldBounds = null;
            if (b != null) {
                oldBounds = (Rectangle2D)b.get();
            }
            if (oldBounds == null || !bounds.equals(oldBounds)) {
                value.prepare(mX);
                value.prepare(mY);
                value.prepare(mWidth);
                value.prepare(mHeight);
                value.prepare(mBounds);

                value.set(mBounds, 0, bounds);
                if (oldBounds == null || oldBounds.isEmpty()) {
                    value.setNumber(mX, 0, bounds.getX());
                    value.setNumber(mY, 0, bounds.getY());
                    value.setNumber(mWidth, 0, bounds.getWidth());
                    value.setNumber(mHeight, 0, bounds.getHeight());
                } else {
                    if (bounds.getX() != oldBounds.getX()) {
                        value.setNumber(mX, 0, bounds.getX());
                    }
                    if (bounds.getY() != oldBounds.getY()) {
                        value.setNumber(mY, 0, bounds.getY());
                    }
                    if (bounds.getWidth() != oldBounds.getWidth()) {
                        value.setNumber(mWidth, 0, bounds.getWidth());
                    }
                    if (bounds.getHeight()!= oldBounds.getHeight()) {
                        value.setNumber(mHeight, 0, bounds.getHeight());
                    }
                }
                
                value.commit(mX);
                value.commit(mY);
                value.commit(mWidth);
                value.commit(mHeight);
                value.commit(mBounds);
            }
        }
************************************************/
    }
}
