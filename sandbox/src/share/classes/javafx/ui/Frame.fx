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
//TODO: import javafx.ui.MenuBar;
import javafx.ui.Widget;
//TODO: import javafx.ui.Dialog;
//TODO: import javafx.ui.Image;
import java.awt.*;
import java.awt.event.*;
import java.awt.Dimension;
//TODO: import javafx.ui.canvas.Shape;
import javax.swing.JFrame;

/**
 * A <code>Frame</code> is a top-level window with a title and a border,
 * and an optional menu bar.
 */
public class Frame extends AbstractFrame {
        
    private attribute winListener: WindowListener;
    private attribute compListener: ComponentListener;
    private attribute frame: JFrame;
    private attribute inListener: Boolean;
    //TODO: public attribute shape: Shape;
    public attribute disposeOnClose: Boolean = true;
    public attribute hideOnClose: Boolean = true;
    public attribute owner: UIElement;

    //TODO: workaround for no support for unset variables yet
    private attribute UNSET: Integer = <<java.lang.Integer.MIN_VALUE>>;

    public attribute screenx: Integer = UNSET on replace {
        if (not inListener) {
            frame.setLocation(new <<java.awt.Point>>(screenx, screeny));
        }
    }

    public attribute screeny: Integer = UNSET on replace {
        if (not inListener) {
            frame.setLocation(new <<java.awt.Point>>(screenx, screeny));
        }
    }

/*//TODO: 
    public attribute menubar: MenuBar on replace {
        frame.setJMenuBar(menubar.jmenubar);
    }
*/

    public attribute content: Widget on replace {
        this.setContentPane(content);
    }

    public attribute dispose: Boolean on replace {
       if (dispose) {
            this.close();
       }
    }

    /**
     * This is the title of the frame.  It can be changed
     * at any time.  
     */
    public attribute title: String on replace {
        frame.setTitle(title);
    }

    public attribute height: Integer = UNSET on replace {
        if (not inListener) {
            if (height <> UNSET) {
                var dim = frame.getSize();
                dim.height = height;
                frame.setSize(dim);
            }
        }
    }

    public attribute width: Integer = UNSET on replace {
        if (not inListener) {
            if (width <> UNSET) {
                var dim = frame.getSize();
                dim.width = width;
                frame.setSize(dim);
            }
        }
    }

    public operation show(): Void {
        if (not visible) {
            return;
        }
        if (height == UNSET or width == UNSET) {
            this.pack();
            var dim = frame.getSize();
            if (height <> UNSET) {
                dim.height = height;
            }
            if (width <> UNSET) {
                dim.width = width;
            }
            if (height <> UNSET or width <> UNSET) {
                frame.setSize(dim);
            }
        } else {
            frame.pack();
            frame.setSize(new Dimension(width, height));
        }
        if (owner <> null) {
            frame.setLocationRelativeTo(owner.getWindow());
        } else {
            if (screenx <> UNSET and screeny <> UNSET) {
                frame.setLocation(screenx, screeny);
            } else if (centerOnScreen) {
                var d = <<java.awt.Toolkit>>.getDefaultToolkit().getScreenSize();                 
                var s = frame.getSize();
                frame.setLocation(d.width/2 - s.width/2, d.height/2 - s.height/2);
            }
        }
        //TODO DO LATER - this is a work around until a more permanent solution is provided
        javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
            public function run():Void{
                if (not visible) {
                    return;
                }
                /*
                if (not UIElement.context.isActive()) {
                   disposeOnClose = false;
                   frame.dispose();
                   return;
                }
                 */
                frame.setVisible(true);
                frame.toFront();
                var loc = frame.getLocation();
                inListener = true;
                screenx = loc.getX().intValue();
                screeny = loc.getY().intValue();
                var size = frame.getSize();
                height = size.height;
                width = size.width;
                inListener = false;
            }
        });
    }

    public operation toFront() {
        //TODO DO LATER - this is a work around until a more permanent solution is provided
        javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
            public function run():Void {
                frame.toFront();
            }
        });
    }

    public operation toBack() {
        //TODO DO LATER - this is a work around until a more permanent solution is provided
        javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
            public function run():Void {
                frame.toBack();
           }
        });
    }

    public operation hide() {
        frame.setVisible(false);
        showing = false;
    }

    public attribute onOpen: function();
    public attribute onClose: function();
    public attribute centerOnScreen: Boolean;
    public attribute background: AbstractColor on replace {
        frame.setBackground(background.getColor());
    }

    /**
     * Makes the frame visible or invisible. Frame's are initially invisible.
     * You must explicitly assign <code>true</code> to this attribute to make the frame
     * visible on the screen.
     */
    public attribute visible: Boolean = false on replace {
        if (not inListener) {
            if (visible) {
                var self = this;
                //TODO DO LATER - this is a work around until a more permanent solution is provided
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                    public function run():Void {
                        self.show();
                    }
                });
            } else {
                if (disposeOnClose) {
                    disposeOnClose = false;
                    this.close();
                } else {
                    frame.setVisible(false);
                }
            }
        }
    }

    /**
     * This field indicates whether the frame is resizable.
     * This property can be changed at any time.
     * <code>resizable</code> will be true if the frame is
     * resizable, otherwise it will be false.
     */
    public attribute resizable: Boolean = true on replace {
        frame.setResizable(resizable);
    }

    /**
     * The icon image for this frame, or <code>null</code> 
     * if this frame doesn't have an icon image.
     */
    /*TODO:
    public attribute iconImage: Image on replace {
        frame.setIconImage(iconImage.getImage());
    }
     */

    /**
     * Disables or enables decorations for this frame.
     * This attribute can only be set while the frame is not displayable.
     */
    public attribute undecorated: Boolean on replace {
        frame.setUndecorated(undecorated);
    }

    public attribute showing: Boolean;
    public attribute iconified: Boolean = false on replace {
        var state = frame.getExtendedState();
        var newState = 0;
        if (iconified) {
            newState = UIElement.context.setBit(state, frame.ICONIFIED);
        } else {
            newState = UIElement.context.clearBit(state, frame.ICONIFIED);
        }
        if (state <> newState) {
            frame.setExtendedState(newState);
        }
    }

    public attribute active: Boolean on replace {
        if (inListener == false) {
            if (active == true) {
                toFront();
            } else {
                toBack();
            }
        }
    }

    public operation move(dx: Integer, dy: Integer) {
        var loc = frame.getLocation();
        frame.setLocation((loc.x + dx).intValue(), (loc.y + dy).intValue());
        inListener = true;
        screenx += dx;
        screeny += dy;
        inListener = false;
    }

    public operation setLocation(x: Integer, y: Integer) {
        frame.setLocation(x.intValue(), y.intValue());
        inListener = true;
        screenx = x;
        screeny = y;
        inListener = false;
    }

    public operation resize(dx: Integer, dy: Integer) {
        var size = frame.getSize();
        frame.setSize(size.width + dx.intValue(), size.height + dy.intValue());
        inListener = true;
        width += dx.intValue();
        height += dy.intValue();
        inListener = false;
    }

    public operation setSize(w: Integer, h: Integer) {
        inListener = true;
        frame.setSize(w.intValue(), h.intValue());
        width = w;
        height = h;
        inListener = false;
    }

    protected operation setContentPane(widget:Widget) {
        if (widget <> null) {
            frame.setContentPane(widget.getComponent());
            frame.validate();
        }
    }

    public operation pack() {
        frame.pack();
    }

    public operation close() {
        // UIElement.context.unregisterWindow(frame);
        disposeOnClose = false;
        frame.dispose();
        showing = false;
        frame = null;
    }

    public operation getFrame(): JFrame {
         return frame;
    }

    init {
        /*
        if (not UIElement.context.isActive()) {
            return;
        }
         */
        frame = new <<javax.swing.JFrame>>;
        if (background <> null) {
            frame.setBackground(background.getColor());
        }
        frame.getRootPane().putClientProperty("apple.awt.draggableWindowBackground", false);
        frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
        //frame.getRootPane().getGlassPane().setVisible(true);
        // UIElement.context.registerWindow(frame);
        win = frame;
        var self = this;
        winListener = WindowListener {
            operation windowClosing(e) {
                if (self.disposeOnClose) {
                     self.frame.removeWindowListener(self.winListener);
                     self.frame.removeComponentListener(self.compListener);
                     // UIElement.context.unregisterWindow(self.frame);
                     self.frame.dispose(); // fix me...
                }
                (self.onClose)();
                if (self.hideOnClose) {
                    self.visible = false;
                    self.showing = false;
                }
            }
            operation windowOpened(e) {
                (self.onOpen)();
            }
            operation windowIconified(e) {
                self.iconified = true;
            }
            operation windowDeiconified(e) {
                self.iconified = false;
            }
            operation windowActivated(e) {
                self.inListener = true;
                self.active = true;
                self.inListener = false;
            }
            operation windowDeactivated(e) {
                self.inListener = true;
                self.active = false;
                self.inListener = false;
            }
        };
        compListener = ComponentListener {
            operation componentResized(e) {
                if (self.resizable) {
                    self.inListener = true;
                    self.height = self.frame.getHeight();
                    self.width = self.frame.getWidth();
                    self.inListener = false;
                }
            }
            operation componentShown(e) {
                self.inListener = true;
                self.visible = true;
                self.inListener = false;
                self.showing = true;
            }
            operation componentHidden(e) {
                self.inListener = true;
                self.visible = false;
                self.inListener = false;
                self.showing = false;
            }
            operation componentMoved(e) {
                if (true) {
                    self.inListener = true;
                    var pt = self.frame.getLocation();
                    self.screenx = pt.getX();
                    self.screeny = pt.getY();
                    self.inListener = false;
                }
            }
        };
        win.addWindowListener(winListener);
        win.addComponentListener(compListener);
    }
}






