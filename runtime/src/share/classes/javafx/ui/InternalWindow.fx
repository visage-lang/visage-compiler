/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
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
 
package javafx.ui; 

import javax.swing.JInternalFrame;
import com.sun.javafx.api.ui.XInternalFrame;
import javax.swing.JPanel;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.InternalFrameAdapter;
import com.sun.javafx.api.ui.ScrollablePanel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JViewport;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import javax.swing.JRootPane;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JApplet;
import javax.swing.JWindow;
import javax.swing.JLayeredPane;
import javax.swing.event.InternalFrameEvent;



class InternalWindow extends UIElement {
    // private:
    private function getRootPane(e:UIElement):JRootPane {
        var result: javax.swing.JRootPane;
        if (e instanceof Applet) {
            return (e as Applet).getRootPane();
        }
        if (e instanceof Widget) {
            var c = (e as Widget).getComponent() as java.awt.Component;
            while (c <> null) {
                if (c instanceof JApplet) {
                    result = (c as JApplet).getRootPane();
                    return result;
                } else if (c instanceof JDialog) {
                    result = (c as JDialog).getRootPane();
                    return result;
                } else if (c instanceof JWindow) {
                    result = (c as JWindow).getRootPane();
                    return result;
                } else if (c instanceof JFrame) {
                    result = (c as JFrame).getRootPane();
                    return result;
                }
                c = c.getParent();
            }
        }
        
        var w = e.getWindow();
        if (w instanceof JDialog) {
            result = (w as JDialog).getRootPane();
        } else if (w instanceof JFrame) {
            result = (w as JFrame).getRootPane();
        } else if (w instanceof JWindow) {
            result = (w as JWindow).getRootPane();
        } else {
        }
        return result;
    }

    private attribute contentComponent:JComponent;
    private attribute dx: Number;
    private attribute dy: Number;
    private attribute inBounds: Boolean;
    private attribute frame: XInternalFrame;
    private attribute glass: JPanel;
    private attribute frameBounds: java.awt.Rectangle;
    private attribute oldGlass: java.awt.Component;
    private attribute oldGlassVisible: Boolean;
    private attribute sp:ScrollablePanel;
    private attribute vp:JViewport;

    // public:
    public attribute modal:Boolean;
    public attribute background: Color on replace {
        frame.getContentPane().setBackground(background.getColor());
    };
    public attribute anchor: Anchor;
    public attribute icon: Icon = Icon { icon: UIManager.getIcon("InternalFrame.icon") }
         on replace {
            if (icon == null) {
                var defaultIcon = UIManager.getIcon("InternalFrame.icon");
                var emptyIcon = new ImageIcon(UIElement.context.getTransparentImage(defaultIcon.getIconWidth(),
                                                                            defaultIcon.getIconHeight()));
                frame.setFrameIcon(emptyIcon);
            } else {
                frame.setFrameIcon(icon.getIcon());
            }
        };
    public attribute owner: UIElement;
    public attribute title: String on replace {
        frame.setTitle(title);
    };
    public attribute visible: Boolean on replace {
        //TODO DO LATER - this is a work around until a more permanent solution is provided
        javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                  public function run():Void {
                    if (visible) {
                        if (glass <> null) {
                            return;
                        }
                    } else {
                        if (glass == null) {
                            return;
                        }
                    }
                    if (visible) {
                        dx = x;
                        dy = y;
                        var root = getRootPane(owner);
                        oldGlass = root.getGlassPane();
                        oldGlassVisible = oldGlass.isVisible();
                        frame = UIElement.context.createInternalFrame();
                        glass = new JPanel();
                        glass.setBounds(0, 0, 5000, 5000); // fix me
                        glass.setOpaque(false);
                        glass.addMouseListener( MouseAdapter{ 
                                public function mouseClicked(e:MouseEvent):Void {} });
                        frame.setClosable(closable);
                        frame.setIconifiable(false);
                        frame.setMaximizable(false);
                        frame.setResizable(resizable);
                        frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
                        sp = new ScrollablePanel();
                        sp.setLayout(new BorderLayout());
                        sp.setOpaque(false);
                        vp = new JViewport();
                        vp.setView(sp);
                        vp.setOpaque(false);
                        var p = new JPanel();
                        p.setOpaque(false);
                        if (background <> null) {
                            p.setBackground(background.getColor());
                        }
                        p.setLayout(new BorderLayout());
                        p.add(vp, BorderLayout.CENTER);
                        frame.setOpaque(false);
                        frame.setVisible(false);
                        frame.setContentPane(p);
                        frame.setTitle(title);
                        if (icon == null) {
                            var defaultIcon = UIManager.getIcon("InternalFrame.icon");
                            var emptyIcon = new ImageIcon(UIElement.context.getTransparentImage(defaultIcon.getIconWidth(),
                                                                                        defaultIcon.getIconHeight()));
                            frame.setFrameIcon(emptyIcon);
                        } else {
                           frame.setFrameIcon(icon.getIcon());
                        }
                        if (contentComponent <> null) {
                            sp.add(contentComponent);
                        }
                        if (modal) {
                            root.getLayeredPane().add(glass, JLayeredPane.MODAL_LAYER, 0);
                        }
                        root.getLayeredPane().add(frame, JLayeredPane.MODAL_LAYER, 0);
                        frame.pack();
                        glass.setVisible(modal);
                        if (fade) {
                            frame.setOpacity(0.1.floatValue());
                        }
                        //TODO JFXC-333 
                        /************************
                        frame.addComponentListener(ComponentAdapter {
                                public function componentMoved(e:ComponentEvent):Void {
                                    inBounds = true;
                                    var b = frame.getBounds();
                                    x = b.x;
                                    y = b.y;
                                    inBounds = false;
                                }
                                public function componentResized(e:ComponentEvent):Void {
                                    inBounds = true;
                                    var b = frame.getBounds();
                                    width = b.width;
                                    height = b.height;
                                    inBounds = false;
                                }
                            } as ComponentListener );
                         ****** END JFXC-333 *******/
                        //TODO JFXC-333 
                        /************************
                        frame.addInternalFrameListener(InternalFrameAdapter {
                                public function internalFrameClosing(e:InternalFrameEvent):Void {
                                    if (onClose <> null) {
                                        (onClose)();
                                    } else {
                                        visible = false;
                                    }
                                }
                            } as InternalFrameListener);
                        ****** END JFXC-333 *******/
                        if (height == 0 or width == 0) {
                            var b = frame.getBounds();
                            inBounds = true;
                            if (width == 0) {
                                width = b.width;
                            }
                            if (height == 0) {
                                height = b.height;
                            }
                            inBounds = false;
                        } 
                        if (anchor <> null) {
                            inBounds = true;
                            var r = root.getBounds();
                            if (anchor == Anchor.CENTER) {
                                x = r.width/2 - width/2;
                                y = r.height/2 - height/2;
                            } else if (anchor == Anchor.NORTH) {
                                x = r.width/2 - width/2;
                                y =0;
                            } else if (anchor == Anchor.SOUTH) {
                                x = r.width/2 - width/2;
                                y = r.height - height;
                            } else if (anchor == Anchor.SOUTHWEST) {
                                x = 0;
                                y = r.height - height;
                            } else if (anchor == Anchor.SOUTHEAST) {
                                x = r.width-width;
                                y = r.height - height;
                            } else if (anchor == Anchor.NORTHEAST) {
                                x = r.width - width;
                                y = 0;
                            } else if (anchor == Anchor.NORTHWEST) {
                                x = 0;
                                y = 0;
                            }
                            x += dx;
                            y += dy;
                            x = x + dx;
                            y = y + dy;
                            inBounds = false;
                        }
                    } 
                    if (not slide and not fade) {
                        if (not visible) {
                            var root = getRootPane(owner);
                            frame.getParent().remove(frame);
                            glass.getParent().remove(glass);
                            root.validate();
                            root.repaint();
                            oldGlass = null;
                            glass = null;
                            frame = null;
                            if (anchor <> null) {
                                x = dx;
                                y = dy;
                            }
                        } else {
                            var rect = new java.awt.Rectangle(x.intValue(), 
                                y.intValue(), width.intValue(), height.intValue());
                            sp.setScrollableTracksViewportHeight(true);
                            sp.setScrollableTracksViewportWidth(true);
                            sp.setPreferredScrollableViewportSize(null);
                            frame.pack();
                            frame.setBounds(rect);
                            frame.setSelected(true);
                        }
                        if (not fade) {
                            return;
                        }
                    }
                    if (visible) {
                        frame.setVisible(not fade);
                        sp.setScrollableTracksViewportHeight(not slide);
                        sp.setScrollableTracksViewportWidth(not slide);
                        if (slide) {
                            sp.setPreferredScrollableViewportSize(
                                new Dimension((width-10).intValue(), 
                                              (height-35).intValue()));
                        } else {
                            sp.setPreferredScrollableViewportSize(null);
                        }
                        if (anchor == null or anchor == Anchor.NORTH
                                or anchor == Anchor.NORTHEAST
                                or anchor == Anchor.NORTHWEST
                                or anchor == Anchor.CENTER) {
                            var h = height.intValue();
                            var rect = new java.awt.Rectangle(
                                                            x.intValue(), 
                                                            y.intValue(), 
                                                            width.intValue(), 
                                                            height.intValue());
                            frame.setBounds(x.intValue(), y.intValue(), width.intValue(), if( slide) then 0 else height.intValue());
                            //TODO DUR Animation
                            for (i in [0..h]) {//TODO DUR ANIMATION(dur animationDuration easeboth) 
                                if (fade) {
                                    if (i > 0) {
                                            var opacity = (i/h).floatValue();
                                            frame.setOpacity(opacity);
                                            if (not frame.isVisible()) {
                                                frame.setVisible(true);
                                            }
                                    }
                                }
                                if (slide) {
                                    frame.setBounds(x.intValue(), y.intValue(), width.intValue(), i.intValue());
                                }
                                animating = i < h;
                                if (i == h and slide) {
                                    if (not frame.isSelected()) {
                                        frame.setSelected(true);
                                    }
                                    sp.setScrollableTracksViewportHeight(true);
                                    sp.setScrollableTracksViewportWidth(true);
                                    sp.setPreferredScrollableViewportSize(null);
                                }
                            }
                        } else {
                            var h = height.intValue();
                            var y1 = y;
                            frame.reshape(x.intValue(), h.intValue(), width.intValue(), if (slide) then 0 else height.intValue());

                            for (i in [h..0 step -1]) {//TODO DUR ANIMATION(dur animationDuration easeboth) 
                                if (fade) {
                                    frame.setOpacity((1-i/h).floatValue());
                                }
                                if (slide) {
                                    frame.setBounds(x.intValue(), y1.intValue()+i.intValue(), width.intValue(), h.intValue()-i.intValue());
                                }
                                if (not frame.isSelected()) {
                                    frame.setSelected(true);
                                }
                                animating = i > 0;
                                if (i == 0) {
                                    sp.setScrollableTracksViewportHeight(true);
                                    sp.setScrollableTracksViewportWidth(true);
                                    sp.setPreferredScrollableViewportSize(null);
                                }

                            }
                        }
                    } else {
                        if (oldGlass == null) {
                            return;
                        }
                        sp.setScrollableTracksViewportHeight(false);
                        sp.setScrollableTracksViewportWidth(false);
                        sp.setPreferredScrollableViewportSize(new Dimension((width-10).intValue(), (height-35).intValue()));
                        if (anchor == null or anchor == Anchor.NORTH
                                    or anchor ==Anchor.NORTHEAST
                                    or anchor ==Anchor.NORTHWEST
                                    or anchor ==Anchor.CENTER ) {
                            var h = frame.getHeight();
                            (glass.getParent() as JLayeredPane).moveToFront(glass);
                            for (i in [h..0 step -1]) {//TODO DUR ANIMATION(dur animationDuration easeboth) 
                                if (frame <> null) {
                                    if (slide) {
                                        frame.setBounds(x.intValue(), y.intValue(), width.intValue(), i);
                                    }
                                    if (fade) {
                                        frame.setOpacity(i/h);
                                    }
                                    animating = i > 0;
                                    if (i == 0) {
                                        var root = getRootPane(owner);
                                        glass.getParent().remove(glass);
                                        frame.getParent().remove(frame);
                                        oldGlass = null;
                                        glass = null;
                                        frame = null;
                                        height = h;
                                        if (anchor <> null) {
                                            x = dx;
                                            y = dy;
                                        }
                                    }
                                }
                            }
                        } else {
                            var h = frame.getHeight();
                            var y1 = y;
                            for (i in [0..h]) {//TODO DUR ANIMATION(dur animationDuration easeboth) 
                                if (frame <> null) {
                                    if (slide) {
                                        frame.setBounds(x.intValue(), y1.intValue() + i, width.intValue(), h.intValue() - i);
                                    }
                                    if (fade) {
                                        frame.setOpacity(1-i/h);
                                    }
                                    animating = i < h;
                                    if (i == h) {
                                        var root = getRootPane(owner);
                                        glass.getParent().remove(glass);
                                        frame.getParent().remove(frame);
                                        oldGlass = null;
                                        glass = null;
                                        frame = null;
                                        height = h;
                                        if (anchor <> null) {
                                            x = dx;
                                            y = dy;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            })
        };

    public attribute content: Widget on replace {
        this.onSetContent(content);
    };
    public attribute closable: Boolean = true on replace {
        frame.setClosable(closable);
    };
    public attribute resizable: Boolean = true on replace {
        frame.setResizable(resizable);
    };
    public attribute x: Number on replace {
        if (not inBounds) {
            frame.reshape(x.intValue(), y.intValue(), width.intValue(), height.intValue());
            frame.repaint();
        }
    };
    public attribute y: Number on replace {
        if (not inBounds) {
            frame.reshape(x.intValue(), y.intValue(), width.intValue(), height.intValue());
            frame.repaint();
        }
    };
    public attribute width: Number on replace {
        if (not inBounds) {
            frame.reshape(x.intValue(), y.intValue(), width.intValue(), height.intValue());
            frame.repaint();
        }
    };
    public attribute height: Number on replace {
        if (not inBounds) {
            frame.reshape(x.intValue(), y.intValue(), width.intValue(), height.intValue());
            frame.repaint();
        }
    };

    public attribute onClose: function():Void;
    public attribute fade: Boolean;
    public attribute slide: Boolean = false;
    public attribute animationDuration: Number = 200;
    public attribute animating: Boolean;
    public function onSetContent(value:Widget):Void{
        setContent(value.getComponent());
    }
    public function setContent(value:javax.swing.JComponent) {
        contentComponent = value;
        var container = sp;
        if (container <> null) {
            if (container.getComponentCount() > 0) {
                container.remove(0);
            }
            if (value <> null) {
                container.add(value);
            }
            frame.validate();
        }
    }
    public bound function getWindow():java.awt.Window  {
        return owner.getWindow();
    }
}



