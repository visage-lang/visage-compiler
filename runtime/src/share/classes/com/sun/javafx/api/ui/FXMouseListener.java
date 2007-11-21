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

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.IdentityHashMap;
import java.util.Set;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.event.SGMouseAdapter;

public class FXMouseListener extends SGMouseAdapter {

    boolean mDragging;
    IdentityHashMap mHoverSet = new IdentityHashMap();

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

    private void updateHoverSet(Set hoverSet) {
    /* TODO:
        Iterator iter = mHoverSet.keySet().iterator();
        while (iter.hasNext()) {
            Value n = (Value)iter.next();
            if (n == null || hoverSet == null || !hoverSet.contains(n)) {
                if (n != null) {
                    n.prepare(mHover);
                    n.setBoolean(mHover, 0, false);
                    n.commit(mHover);
                }
                iter.remove();
            }
        }
        if (hoverSet != null) {
            iter = hoverSet.iterator();
            while (iter.hasNext()) {
                Value n = (Value)iter.next();
                mHoverSet.put(n, null);
                n.prepare(mHover);
                n.setBoolean(mHover, 0, true);
                n.commit(mHover);
            }
        }
     */
    }
    
    private void updateHoverSetForEvent(MouseEvent e) {
/* TODO:        
        // TODO: ideally e.getComponent() would return the originating
        // JSGPanel, but for events originating from a node or embedded
        // JSGPanel, e.getComponent() will return null (since SGNode calls
        // MouseEvent.setSource(), which will overwrite the original source
        // Component); the following is more of a workaround than a fix...
        Object source = e.getSource();
        JSGPanel jsgpanel = null;
        if (source instanceof JSGPanel) {
            jsgpanel = (JSGPanel)source;
        } else if (source instanceof SGNode) {
            jsgpanel = ((SGNode)source).getPanel();
        }
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
        Value last = null;
        for (SGNode sgnode : path) {
            Value n = getF3(sgnode);
            if (n != null && n != last) {
                if (!done) {
                    hoverSet.put(n, n);
                    done = n.getBoolean(mIsSelectionRoot, 0).booleanValue();
                }
                last = n;
            }
        }
        updateHoverSet(hoverSet.keySet());
 */
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mDragging = false;
        mouseMoved(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (mDragging) {
            //System.out.println("mouse enter during drag: "+ e.getPoint());
            //return;
        }
        updateHoverSetForEvent(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (mDragging) {
            //System.out.println("mouse exit during drag "+ e.getPoint());
            //return;
        }
        updateHoverSetForEvent(e);
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
	mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        updateHoverSetForEvent(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO: implement
        /*
        if (mModule == null) {
            return;
        }
        //if (true) return;
        Point pt = e.getPoint();
        ZSceneGraphPath path = e.getCurrentPath();
        if (path == null || path.getObject() == null) {
            mDragging = true;
            updateHoverSet(null);
            return;
        }
        if (!mDragging) {
            ZNode zn = e.getGrabNode();
            Value target = getF3(zn);
            boolean exportDrag = false;
            if (target != null) {
                boolean draggable = ((Boolean)target.getAttribute(mDraggable, 0).get()).booleanValue();
                boolean done = target.getBoolean(mIsSelectionRoot, 0).booleanValue();
                if (!draggable && !done) {
                    ZSceneGraphPath grabpath = e.getGrabPath();
                    ZSceneGraphObject zobj = null;
                    for (int i = grabpath.getNumParents()-1; i >=0 ; --i) {
                        zobj = grabpath.getParent(i);
                        target = (Value)getF3(zobj);
                        if (target != null) {
                            draggable = ((Boolean)target.getAttribute(mDraggable, 0).get()).booleanValue();
                            if (draggable) {
                                exportDrag = true;
                                break;
                            }
                            done = target.getBoolean(mIsSelectionRoot, 0).booleanValue();
                            if (done) {
                                break;
                            }
                        }
                    }
                }
            }
            if (exportDrag) {
                int num = target.getNumber(mDragTrigger, 0).intValue();
                num++;
                target.prepare(mDragTrigger);
                target.setNumber(mDragTrigger, 0, new Integer(num));
                target.commit(mDragTrigger);
                //System.out.println("invoking transfer handler");
                JComponent c = (JComponent)e.getSource();
                MouseEvent me = e;
                //If they are holding down the control key, COPY rather than MOVE
                int ctrlMask = InputEvent.CTRL_DOWN_MASK;
                int action = ((e.getModifiersEx() & ctrlMask) == ctrlMask) ?
                    TransferHandler.COPY : TransferHandler.MOVE;
                
                TransferHandler handler = c.getTransferHandler();
                //Tell the transfer handler to initiate the drag.
                if (handler != null) {
                    updateHoverSet(null);
                    //((ZCanvas)c).setExportAsDrag(true); // TODO: no equivalent in Scenario
                    handler.exportAsDrag(c, me, action);
                    return;
                }
            }
        }
        Value n = getF3(path.getObject());
        Map hoverSet = new IdentityHashMap();
        if (n != null) {
            ZNode zn = (ZNode)n.getAttribute(mContentNode, 0).get();
            //n.setBoolean(mHover, 0, zn.isShowing() && 
            //zn.getGlobalShape().contains(pt));
            boolean hover = zn.isShowing() && 
                zn.containsGlobalPoint(pt);
            if (hover) {
                hoverSet.put(n, n);
            } else {
            }
        }
        boolean done = false;
        Value last  = n;
        for (int i = path.getNumParents()-1; i >=0; i--) {
            ZSceneGraphObject p = path.getParent(i);
            n = getF3(p);
            if (n != null && n != last) {
                if (!done) {
                    ZNode zn = (ZNode)n.getAttribute(mContentNode, 0).get();
                    //                    n.setBoolean(mHover, 0, 
                    //                                 zn.isShowing() &&
                    //                                 zn.getGlobalShape().contains(pt));
                    boolean hover = zn.isShowing() &&
                        zn.containsGlobalPoint(pt);
                    if (hover) {
                        hoverSet.put(n, n);
                        done = n.getBoolean(mIsSelectionRoot, 0).booleanValue();
                    } 
                } else {
                }
                last = n;
            }
        }
        updateHoverSet(hoverSet.keySet());
         */
    }
}
