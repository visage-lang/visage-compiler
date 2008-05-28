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

package com.sun.javafx.api.ui;

import java.awt.*;
import java.awt.event.*;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.javafx.runtime.location.BooleanLocation;
import com.sun.scenario.scenegraph.JSGPanel;
import com.sun.scenario.scenegraph.SGNode;

public class FXMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener {
    static Class targetClass = null;
    static PropertyDescriptor mHover;
    static PropertyDescriptor mContentNode;
    static PropertyDescriptor mIsSelectionRoot;
    static PropertyDescriptor mSelectable;
    static PropertyDescriptor mDraggable;
    static PropertyDescriptor mDragTrigger;

    static {
        try {
            targetClass = Class.forName("javafx.ui.canvas.Node$Intf");
            BeanInfo bi = Introspector.getBeanInfo(targetClass);
            PropertyDescriptor[] pds = bi.getPropertyDescriptors();
            for(int i = 0; i < pds.length; i++) {
                String name = pds[i].getName();
                if(name.equals("$hover")) {
                    mHover =pds[i];
                } else if(name.equals("$contentNode")) {
                    mContentNode =pds[i];
                } else if(name.equals("$isSelectionRoot")) {
                    mIsSelectionRoot =pds[i];
                } else if(name.equals("$selectable")) {
                    mSelectable =pds[i];
                } else if(name.equals("$exportDrag")) {
                    mDraggable =pds[i];
                } else if(name.equals("$dragCount")) {
                    mDragTrigger =pds[i];
                }
            }
        }catch(Exception ex) {
            Logger.getLogger(FXMouseListener.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    boolean mDragging;
    IdentityHashMap<Object, Object> mHoverSet = new IdentityHashMap<Object, Object>();

    Object getFX(SGNode obj) {
        Object result = null;
        while (obj != null) {
            result = obj.getAttribute("FX");
            if (result != null) {
                return result;
            }
            obj = obj.getParent();
        }
        return result;
    }
    
    private void setBoolean(Object obj, PropertyDescriptor desc, boolean value) {
        try {
            Method meth = desc.getReadMethod();
            BooleanLocation location = (BooleanLocation) meth.invoke(obj);
            location.setAsBoolean(value);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(FXMouseListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(FXMouseListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(FXMouseListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean getBoolean(Object obj, PropertyDescriptor desc) {
        try {
            Method meth = desc.getReadMethod();
            BooleanLocation location = (BooleanLocation) meth.invoke(obj);
            return location.getAsBoolean();
        } catch (IllegalAccessException ex) {
            Logger.getLogger(FXMouseListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(FXMouseListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(FXMouseListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private void updateHoverSet(Set hoverSet) {
        Iterator iter = mHoverSet.keySet().iterator();
        while (iter.hasNext()) {
            Object n = iter.next();
            if (n == null || hoverSet == null || !hoverSet.contains(n)) {
                if (n != null) {
                    setBoolean(n, mHover, false);
                }
                iter.remove();
            }
        }
        if (hoverSet != null) {
            iter = hoverSet.iterator();
            while (iter.hasNext()) {
                Object n = iter.next();
                mHoverSet.put(n, null);
                setBoolean(n, mHover, true);
            }
        }
    }
    
    private void updateHoverSetForEvent(MouseEvent e) {
        JSGPanel jsgpanel = (JSGPanel)e.getComponent();
        if (jsgpanel == null) {
            updateHoverSet(null);
            return;
        }
        Point pt = e.getPoint();
        SGNode root = jsgpanel.getScene();
        List<SGNode> path = root.pick(pt);
        if (path.isEmpty()) {
            updateHoverSet(null);
            return;
        }
        boolean done = false;
        Map hoverSet = new IdentityHashMap();
        Object last = null;
        for (SGNode sgnode : path) {
            Object n = getFX(sgnode);
            if (n != null && n != last) {
                if (!done) {
                    hoverSet.put(n, n);
                    done = getBoolean(n,mIsSelectionRoot);
                }
                last = n;
            }
        }
        updateHoverSet(hoverSet.keySet());
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        mDragging = false;
        mouseMoved(e);
    }

    public void mouseEntered(MouseEvent e) {
        if (mDragging) {
            //System.out.println("mouse enter during drag: "+ e.getPoint());
            //return;
        }
        updateHoverSetForEvent(e);
    }

    public void mouseExited(MouseEvent e) {
        if (mDragging) {
            //System.out.println("mouse exit during drag "+ e.getPoint());
            //return;
        }
        updateHoverSetForEvent(e);
    }
    
    public void mouseWheelMoved(MouseWheelEvent e) {
	mouseMoved(e);
    }

    public void mouseMoved(MouseEvent e) {
        updateHoverSetForEvent(e);
    }

    public void mouseDragged(MouseEvent e) {
        updateHoverSetForEvent(e);
    }
}
