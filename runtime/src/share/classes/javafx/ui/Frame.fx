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

import javafx.ui.AbstractFrame;
import javafx.ui.UIElement;
import javafx.ui.MenuBar;
import javafx.ui.Widget;
import javafx.ui.Dialog;
import javafx.ui.Image;
import java.awt.*;
import java.awt.Dimension;
//TODO SHAPE
//import javafx.ui.canvas.Shape;
import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentEvent;
import java.lang.System;
/**
 * A <code>Frame</code> is a top-level window with a title and a border,
 * and an optional menu bar.
 */
public class Frame extends AbstractFrame {
    private attribute UNSET: Integer = java.lang.Integer.MIN_VALUE;

    private attribute winListener: java.awt.event.WindowListener;
    private attribute compListener: java.awt.event.ComponentListener;
    private attribute frame: javax.swing.JFrame = new javax.swing.JFrame;
; 
    private attribute inListener: Boolean;
//TODO SHAPE
//    public attribute shape: Shape;
    public attribute disposeOnClose: Boolean = true;
    public attribute hideOnClose: Boolean;
    public attribute owner: UIElement;
    public attribute screenx: Number = UNSET on replace  {
        if (frame <> null and not inListener and screenx <> UNSET and screeny <> UNSET) {
            frame.setLocation(new java.awt.Point(screenx.intValue(), screeny.intValue()));
        }
        
    };
    public attribute screeny: Number = UNSET on replace  {
        if (frame <> null and not inListener and screeny <> UNSET and screenx <> UNSET) {
            frame.setLocation(new java.awt.Point(screenx.intValue(), screeny.intValue()));
        }
        
    };
    public attribute menubar: MenuBar on replace {
	if (frame <> null) {
	    frame.setJMenuBar(if (menubar == null) null else menubar.getComponent() as javax.swing.JMenuBar);
	}
    };
    public attribute content: Widget on replace  {
        this.setContentPane(content);
    };

    public attribute dispose: Boolean on replace {
       if (dispose) {
            this.close();
       }
    };
    /**
     * This is the title of the frame.  It can be changed
     * at any time.  
     */
    public attribute title: String on replace {
        if (frame <> null) frame.setTitle(title);
    };
    public attribute height: Number = UNSET on replace  {
        if (frame <> null and not inListener and height <> UNSET) {
            
            var dim = frame.getSize();
            dim.height = height.intValue();
            frame.setSize(dim);
        }
    };
    public attribute width: Number = UNSET on replace  {
        if (frame <> null and not inListener and width <> UNSET) {
            var dim = frame.getSize();
            dim.width = width.intValue();
            frame.setSize(dim);
        }
    };
    public function show(){
        if (visible) {
	    if (height == UNSET or width == UNSET) {
                content.getComponent().getPreferredSize();
		this.pack();
                frame.getPreferredSize();
		var dim = frame.getSize();
		if (height <> UNSET) {
		    dim.height = height.intValue();
		}
		if (width <> UNSET) {
		    dim.width = width.intValue();
		}
		if (height <> UNSET or width <> UNSET) {
		    System.out.println("setting size to { dim }");
		    frame.setSize(dim);
		}
	    } else {
		frame.pack();
		frame.setSize(new Dimension(width.intValue(), height.intValue()));
	    }
	    if (owner <> null) {
		frame.setLocationRelativeTo(owner.getWindow());
	    } else {
		if (screenx <> UNSET and screeny <> UNSET) {
		    frame.setLocation(screenx.intValue(), screeny.intValue());
		} else if (centerOnScreen) {
		    var d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();                 
		    var s = frame.getSize();
		    frame.setLocation(d.width/2 - s.width/2, d.height/2 - s.height/2);
		}
	    }
	    //TODO DO LATER - this is a work around until a more permanent solution is provided
	    javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
		   public function run():Void {
		       if (not visible) {
			   return;
		       }
                       frame.validate();
		       frame.setVisible(true);
		       frame.toFront();
		       var loc = frame.getLocation();
		       inListener = true;
		       screenx = loc.getX();
		       screeny = loc.getY();
		       var size = frame.getSize();
		       height = size.height;
		       width = size.width;
		       inListener = false;
		   }
	   });
        }
    }

    public function toFront() {
        //TODO DO LATER - this is a work around until a more permanent solution is provided
        javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                public function run():Void {
		    if (frame <> null) {
			frame.toFront();
		    }
                }
            });
    }

    public function toBack() {
        //TODO DO LATER - this is a work around until a more permanent solution is provided
        javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                  public function run():Void {
		      if (frame <> null) {
                        frame.toBack();
		      }
                  }
            });
    }
    public function hide(){
        frame.setVisible(false);
        showing = false;
    }
    //TODO is this needed?
    public function showDialog(d:Dialog) { 
        //empty 
    }
    public attribute onOpen: function():Void;
    public attribute onClose: function():Void;
    public attribute centerOnScreen: Boolean;
    public attribute background: AbstractColor on replace {
	if (frame <> null) {
	    frame.setBackground(if (background == null) null else background.getColor());
	}
    };
    /**
     * Makes the frame visible or invisible. Frame's are initially invisible.
     * You must explicitly assign <code>true</code> to this attribute to make the frame
     * visible on the screen.
     */
    public attribute visible: Boolean on replace  {
        if (not inListener) {
            if (visible) {
                //TODO DO LATER - this is a work around until a more permanent solution is provided
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                          public function run():Void {
                               show();
                          }
                });
            } else {
                if (disposeOnClose) {
                    disposeOnClose = false;
                    this.close();
                } else {
		    if (frame <> null) {
			frame.setVisible(false);
		    }
                }
            }
        }
    };
    /**
     * This field indicates whether the frame is resizable.
     * This property can be changed at any time.
     * <code>resizable</code> will be true if the frame is
     * resizable, otherwise it will be false.
     */
    public attribute resizable: Boolean = true on replace {
	if (frame <> null) {
	    frame.setResizable(resizable);
	}
    };
    /**
     * The icon image for this frame, or <code>null</code> 
     * if this frame doesn't have an icon image.
     */
    public attribute iconImage: Image on replace {
	if (frame <> null) {
	    frame.setIconImage(if (iconImage == null) then null else iconImage.getImage());
	}
    };
    /**
     * Disables or enables decorations for this frame.
     * This attribute can only be set while the frame is not displayable.
     */
    public attribute undecorated: Boolean = false on replace {
	if (frame <> null and frame.isUndecorated() <> undecorated) {
	    frame.setUndecorated(undecorated);
	}
    };
    public attribute showing: Boolean;
    public attribute iconified: Boolean = false on replace {
	if (frame <> null) {
	    var state = frame.getExtendedState();
	    var newState = 0;
	    if (iconified) {
		newState = UIElement.getUIContext().setBit(state, frame.ICONIFIED);
	    } else {
		newState = UIElement.getUIContext().clearBit(state, frame.ICONIFIED);
	    }
	    if (state <> newState) {
		frame.setExtendedState(newState);
	    }
	}
    };
    public attribute active: Boolean on replace {
        if (inListener == false) {
            if (active == true) {
                toFront();
            } else {
                toBack();
            }
        }
    };
    public function move(dx: Number, dy: Number){
        var loc = frame.getLocation();
        frame.setLocation(loc.x + dx.intValue(), loc.y + dy.intValue());
        inListener = true;
        //TODO +=
        screenx = screenx + dx;
        screeny = screeny + dy;
        inListener = false;
    }
    public function setLocation(x: Number, y: Number) {
        frame.setLocation(x.intValue(), y.intValue());
        inListener = true;
        screenx = x.intValue();
        screeny = y.intValue();
        inListener = false;
    }
    public function resize(dx: Number, dy: Number){
        var size = frame.getSize();
        frame.setSize(size.width + dx.intValue(), size.height + dy.intValue());
        inListener = true;
        //TODO +=
        width = width + dx;
        height = height + dy;
        inListener = false;
    }
    public function setSize(width: Number, height: Number) {
        inListener = true;
        frame.setSize(width.intValue(), height.intValue());
        this.width = width;
        this.height = height;
        inListener = false;
    }
    protected function setContentPane(widget:Widget){
        if (widget <> null) {
            frame.setContentPane(widget.getComponent());
            frame.validate();
        }
    }
    public function pack(){
        frame.pack();
    }
    public function close(){
        disposeOnClose = false;
	if (frame <> null) {
	    frame.dispose();
	}
        showing = false;
        frame = null;
    }
    public function getFrame(): JFrame {
         return frame;
    }

    init {
        if (background <> null) {
            frame.setBackground(background.getColor());
        }
        frame.getRootPane().putClientProperty("apple.awt.draggableWindowBackground", false);
        frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
        //frame.getRootPane().getGlassPane().setVisible(true);
        if(menubar <> null) {
            frame.setJMenuBar(menubar.getComponent() as javax.swing.JMenuBar);
        }
        win = frame;
        winListener = java.awt.event.WindowListener {
                                  public function windowClosing(e:WindowEvent):Void {
                                      if (disposeOnClose) {
                                           frame.removeWindowListener(winListener);
                                           frame.removeComponentListener(compListener);
                                           frame.dispose(); // fix me...
                                      }
                                      if(onClose <> null) {
                                        onClose();
                                      }
                                      if (hideOnClose) {
                                          visible = false;
                                          showing = false;
                                      }
                                  }
                                  public function windowClosed(e:WindowEvent):Void {
                                    //empty
                                  }
                                  public function windowOpened(e:WindowEvent):Void {
                                        if(onOpen <> null) {
                                            onOpen();
                                        }
                                    }
                                  public function windowIconified(e:WindowEvent):Void {
                                          iconified = true;
                                  }
                                  public function windowDeiconified(e:WindowEvent):Void {
                                          iconified = false;
                                  }
                                  public function windowActivated(e:WindowEvent):Void {
                                          inListener = true;
                                          active = true;
                                          inListener = false;
                                  }
                                  public function windowDeactivated(e:WindowEvent):Void {
                                          inListener = true;
                                          active = false;
                                          inListener = false;
                                  }
                              };
        compListener = java.awt.event.ComponentListener {
                    public function componentResized(e:ComponentEvent):Void {
                        if (resizable) {
                            inListener = true;
                            height = frame.getHeight();
                            width = frame.getWidth();
                            inListener = false;
                        }
                    }
                    public function componentShown(e:ComponentEvent):Void {
                        inListener = true;
                        visible = true;
                        inListener = false;
                        showing = true;
                    }
                    public function componentHidden(e:ComponentEvent):Void {
                        inListener = true;
                        visible = false;
                        inListener = false;
                        showing = false;
                    }
                    public function componentMoved(e:ComponentEvent):Void {
                        if (true) {
                            inListener = true;
                            var pt = frame.getLocation();
                            screenx = pt.getX();
                            screeny = pt.getY();
                            inListener = false;
                        }
                    }
            };
       win.addWindowListener(winListener);
       win.addComponentListener(compListener);

    }
}

