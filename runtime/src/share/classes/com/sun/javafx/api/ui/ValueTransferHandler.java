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
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.dnd.*;
import javax.swing.*;
import java.io.File;
import java.util.List;

abstract public class ValueTransferHandler extends TransferHandler {

    Class type;
    public Class getType() {
        return type;
    }

    public void setType(Class t) {
        type = t;
    }
    
    public ValueTransferHandler(Component comp, Class t) {
        setType(t);
    }

    protected abstract Object exportValue(JComponent c);
    protected abstract void importValue(JComponent c, Object value);
    protected abstract void cleanup(JComponent c, boolean remove);
    
    @Override
    protected Transferable createTransferable(JComponent c) {
        return new ValueSelection(exportValue(c));
    }
    
    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean importData(JComponent c, Transferable t) {
        if (canImport(c, t.getTransferDataFlavors(), t)) {
            try {
                DataFlavor[] flavors = t.getTransferDataFlavors();
                for (int i = 0; i < flavors.length; i++) {
                    if (flavors[i].equals(DataFlavor.stringFlavor)) {
                        String s = (String)t.getTransferData(flavors[i]);
                        importValue(c, s);
                        return true;
                    } else if (flavors[i].equals(DataFlavor.javaFileListFlavor)) {
                        List list = (List)t.getTransferData(flavors[i]);
                        File[] arr = new File[list.size()];
                        list.toArray(arr);
                        importValue(c, arr);
                        return true;
                    } else {
                        Object obj = t.getTransferData(flavors[i]);
                        importValue(c, obj);
                        return true;
                    }
                }
            } catch (UnsupportedFlavorException ufe) {
                ufe.printStackTrace();
            } catch (IOException ioe) {
            }
            return true;
        }

        return false;
    }
    
    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        cleanup(c, action == MOVE);
    }

    class MyDropTargetListener implements DropTargetListener {
        public void dragEnter(DropTargetDragEvent e) {
            notifyDragEnter();
        }
        public void dragOver(DropTargetDragEvent e) {
            notifyDragOver();
        }
        public void dragExit(DropTargetEvent e) {
            notifyDragExit();
        }
        public void drop(DropTargetDropEvent e) {
	    notifyDrop(e);
        }
        public void dropActionChanged(DropTargetDragEvent dsde) {
	}

    }

    MyDropTargetListener myDropTargetListener;

    void installDropTargetListener(Component comp) {
        myDropTargetListener = new MyDropTargetListener();
        try {
            comp.getDropTarget().addDropTargetListener(myDropTargetListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canImport(TransferSupport support) {
        Transferable t = null;
        try {
            t = support.getTransferable();
            if (support.getDataFlavors().length > 0) {
                t.getTransferData(support.getDataFlavors()[0]);
            }
        } catch (UnsupportedFlavorException exc) {
        } catch (IOException exc) {
        } catch (InvalidDnDOperationException exc) {
            // hack...
            t = null;
        }
        return canImport((JComponent)support.getComponent(),
                         support.getDataFlavors(),
                         t);
    }


    protected abstract boolean canImport(Object value);

    protected abstract void notifyDragEnter();
    protected abstract void notifyDragExit();
    protected abstract void notifyDragOver();
    protected abstract void notifyDrop(DropTargetDropEvent e);
    //protected abstract void notifyDropActionChanged(DropTargetDropEvent e);
    
    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        return canImport(c, flavors, null);
    }

    public boolean canImport(JComponent c, DataFlavor[] flavors, 
                             Transferable t) {
        try {
            for (int i = 0; i < flavors.length; i++) {
                DataFlavor f = flavors[i];
                if (f.equals(DataFlavor.stringFlavor)) {
                    if (type == null || type == String.class) {
                        if (t == null) {
                            return true;
                        }
                        Object obj = t.getTransferData(f);
                        return canImport(obj);
                    }
                } else if (f.equals(DataFlavor.javaFileListFlavor)) {
                    if (type == null || type == File.class) {
                        if (t == null) {
                            return true;
                        }
                        
                        List list = (List)t.getTransferData(f);
                        return canImport(list.toArray());
                    }
                } else {
                    //System.out.println("got data flavor: " + f);
                    if (t == null) {
                        return true;
                    }
                    Object obj = t.getTransferData(f);
                    return canImport(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    static class ImageWindow extends JWindow {
        Image dragged;
        private AlphaComposite composite;
        JLabel label;
        ImageWindow(Icon dragged) {
            //composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
            
            setBackground(Color.white);
            final JLabel ilabel = new JLabel();
            ilabel.setBorder(BorderFactory.createLineBorder(Color.black));            
            ilabel.setIcon(dragged);
            add(ilabel);
            ilabel.addComponentListener(new ComponentAdapter() {
                @Override
                    public void componentResized(ComponentEvent e) {
                        ImageWindow.this.setSize(ilabel.getWidth(), ilabel.getHeight());
                    }
                });
            pack();
        }
        @Override
        public void paint(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            //g2.setComposite(composite);
            super.paint(g2);
        }
    }

    static class ComponentWindow extends JWindow {
        Component component;
        ComponentWindow(Component c) {
            component = c;
            setBackground(Color.white);
            JPanel p = new JPanel();
            p.setOpaque(true);
            p.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
            p.setLayout(new BorderLayout());
            p.add(component, BorderLayout.CENTER);
            setContentPane(p);
            pack();
        }
        @Override
        public void paint(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            super.paint(g2);
        }
    }

    static class DragHandler implements DragGestureListener, DragSourceListener ,DragSourceMotionListener {
        
        private boolean scrolls;
        JWindow win;
        boolean firstCall = true;

	// --- DragGestureListener methods -----------------------------------

	/**
	 * a Drag gesture has been recognized
	 */
        public void dragGestureRecognized(DragGestureEvent dge) {
	    JComponent c = (JComponent) dge.getComponent();
	    ValueTransferHandler th = (ValueTransferHandler)c.getTransferHandler();
	    Transferable t = th.createTransferable(c);
	    if (t != null) {
                scrolls = c.getAutoscrolls();
                c.setAutoscrolls(false);
                try {
                    Image img = null; 
                    Component comp = th.getVisualComponent(t);
                    if (comp != null) {
                        win = new ComponentWindow(comp);
                        firstCall = true;
                        dge.getDragSource().addDragSourceMotionListener(this);
                        dge.startDrag(null, t, this);
                    } else {
                        Icon icn = th.getVisualRepresentation(t) ;
                        if(icn == null) {
                            dge.startDrag(null, t, this);
                        } else {
                            win = new ImageWindow(icn);
                            firstCall = true;
                            dge.getDragSource().addDragSourceMotionListener(this);
                            dge.startDrag(null, img, 
                                          new Point(0,
                                                    -1*win.getHeight()), t, this) ;
                        }
                    }
                    return;
                } catch (RuntimeException re) {
                    re.printStackTrace();
                    c.setAutoscrolls(scrolls);
                }
	    }
            
            th.exportDone(c, t, NONE);
	}

	// --- DragSourceListener methods -----------------------------------

	/**
	 * as the hotspot enters a platform dependent drop site
	 */
        public void dragEnter(DragSourceDragEvent dsde) {

	}
  
	/**
	 * as the hotspot moves over a platform dependent drop site
	 */
        public void dragOver(DragSourceDragEvent dsde) {

	}
  
	/**
	 * as the hotspot exits a platform dependent drop site
	 */
        public void dragExit(DragSourceEvent dsde) {

	}
        
        public void dragMouseMoved(DragSourceDragEvent dsde) {
            if (win != null) {
                Point p = dsde.getLocation();
                p.translate(32, 16);
                win.setLocation(p);
                if (firstCall) {
                    win.setVisible(true);
                    firstCall = false;
                }
            }
        }
  
	/**
	 * as the operation completes
	 */
        public void dragDropEnd(DragSourceDropEvent dsde) {
            if (win != null) {
                win.dispose();
                win = null;
                firstCall = true;
            }
            DragSourceContext dsc = dsde.getDragSourceContext();
            JComponent c = (JComponent)dsc.getComponent();
            ValueTransferHandler th = (ValueTransferHandler)c.getTransferHandler();
	    if (dsde.getDropSuccess()) {
                th.exportDone(c, dsc.getTransferable(), dsde.getDropAction());
	    } else {
                th.exportDone(c, dsc.getTransferable(), NONE);
            }
            c.setAutoscrolls(scrolls);
	}
  
        public void dropActionChanged(DragSourceDragEvent dsde) {
	}

    }

    SwingDragGestureRecognizer recognizer;

    @Override
    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
        int srcActions = getSourceActions(comp);
	int dragAction = srcActions & action;
	if (! (e instanceof MouseEvent)) {
	    // only mouse events supported for drag operations
	    dragAction = NONE;
	}
	if (dragAction != NONE && !GraphicsEnvironment.isHeadless()) {
	    if (recognizer == null) {
		recognizer = new SwingDragGestureRecognizer(new DragHandler());
	    }
	    recognizer.gestured(comp, (MouseEvent)e, srcActions, dragAction);
	} else {
            exportDone(comp, null, NONE);
        }
    }

    static class SwingDragGestureRecognizer extends DragGestureRecognizer {

	SwingDragGestureRecognizer(DragGestureListener dgl) {
	    super(DragSource.getDefaultDragSource(), null, NONE, dgl);
	}

	void gestured(JComponent c, MouseEvent e, int srcActions, int action) {
	    setComponent(c);
            setSourceActions(srcActions);
	    appendEvent(e);
	    fireDragGestureRecognized(action, e.getPoint());
	}

	/**
	 * register this DragGestureRecognizer's Listeners with the Component
	 */
        protected void registerListeners() {
	}

	/**
	 * unregister this DragGestureRecognizer's Listeners with the Component
	 *
	 * subclasses must override this method
	 */
        protected void unregisterListeners() {
	}

    }

    public Component getVisualComponent(Transferable t) {
        return null;
    }

}



