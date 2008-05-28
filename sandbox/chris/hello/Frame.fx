package hello;
import java.lang.Runnable;
import java.lang.System;

public class Frame extends UIElement {        

    public function getWindow(): java.awt.Window {
	return getFrame();
    }

    private attribute windowListener: java.awt.event.WindowListener;
    private attribute componentListener: java.awt.event.ComponentListener;
    private attribute frame: javax.swing.JFrame;
    private attribute inListener: Boolean;

    public attribute owner: UIElement;

    public attribute opaque: Boolean;
    public attribute opacity: Number;
    public attribute disposeOnClose: Boolean = false;
    public attribute hideOnClose: Boolean = false;
    public attribute content: Widget;
    /**
     * This is the title of the frame.  It can be changed
     * at any time.  
     */
    public attribute title: String;

    public attribute height: Number on replace { setSize(); }
    public attribute width: Number on replace { setSize(); }

    private function setSize() {
	size = new java.awt.Dimension(width.intValue(), height.intValue());
	if (frame <> null and not inListener) {
	    frame.setSize(size);
	}
    }

    private attribute size: java.awt.Dimension = null;

    public attribute x: Number = 0 on replace { setLocation(); }
    public attribute y: Number = 0 on replace { setLocation(); }

    private function setLocation() {
	if (frame <> null and not inListener) {
	    var location = new java.awt.Point(x.intValue(), y.intValue());
	    frame.setLocation(location);
	}
    }

    //private attribute location: java.awt.Point = null;

    public function toFront(): Frame {
	getFrame().toFront();
	return this;
    }
    public function toBack(): Frame {
	getFrame().toBack();
	return this;
    }

    public function hide(): Frame {
	visible = false;
	return this;
    }

    public attribute onOpen: function();
    public attribute onClose: function();
    public attribute centerOnScreen: Boolean;
    //public attribute background: Color;
    /**
     * Makes the frame visible or invisible. Frame's are initially invisible.
     * You must explicitly assign <code>true</code> to this attribute to make the frame
     * visible on the screen.
     */
    public attribute visible: Boolean on replace {
	if (not inListener) {
	    if (visible) {
		javax.swing.SwingUtilities.invokeLater(Runnable {
			public function run():Void {
			    show();
			}
		    });
	    } else if (frame <> null) {
		frame.hide();
	    }
	}
    }
    /**
     * This field indicates whether the frame is resizable.
     * This property can be changed at any time.
     * <code>resizable</code> will be true if the frame is
     * resizable, otherwise it will be false.
     */
    public attribute resizable: Boolean = true;
    /**
     * The icon image for this frame, or <code>null</code> 
     * if this frame doesn't have an icon image.
     */
    public attribute iconImage: Image;
    /**
     * Disables or enables decorations for this frame.
     * This attribute can only be set while the frame is not displayable.
     */
    public attribute undecorated: Boolean = false;
    public attribute showing: Boolean = false;
    public attribute iconified: Boolean = false;
    public attribute active: Boolean;

    protected function createFrame():javax.swing.JFrame {
	return new javax.swing.JFrame();
    }

    public function getFrame(): javax.swing.JFrame {
	if (frame == null) {
	    frame = createFrame();
	    frame.setUndecorated(undecorated);
	    frame.setResizable(resizable);
	    //if (background <> null)  {
	    //frame.setBackground(background.getColor());
	    //}
	    if (content <> null) {
		frame.getContentPane().add(content.getComponent());
	    }
	    if (iconImage <> null) {
		frame.setIconImage(iconImage.getImage());
	    }
	    frame.setTitle(title);
	    windowListener = java.awt.event.WindowListener {
		public function windowClosing(event:java.awt.event.WindowEvent): Void {
		    if (onClose <> null) {
			onClose();
		    }
		    if (hideOnClose) {
			visible = false;
			showing = false;
		    }
		}
		public function windowClosed(event:java.awt.event.WindowEvent): Void { 
		    showing = false;
		}
		public function windowOpened(event:java.awt.event.WindowEvent): Void {
		    inListener = true;
		    iconified = false;
		    inListener = false;
		    if (onOpen <> null) {
			onOpen();
		    }
		}
		public function windowIconified(event:java.awt.event.WindowEvent): Void {
		    inListener = true;
		    iconified = true;
		    inListener = false;
		}
		public function windowDeiconified(event:java.awt.event.WindowEvent): Void {
		    inListener = true;
		    iconified = false;
		    inListener = false;
		}
		public function windowActivated(event:java.awt.event.WindowEvent): Void {
		    inListener = true;
		    active = true;
		    inListener = false;
		}
		public function windowDeactivated(event:java.awt.event.WindowEvent): Void {
		    inListener = true;
		    active = false;
		    inListener = false;
		}
	    };
	    frame.addWindowListener(windowListener);
	    componentListener = java.awt.event.ComponentListener {
		public function componentResized(e:java.awt.event.ComponentEvent):Void {
                    if (resizable) {
                        inListener = true;
                        height = frame.getHeight();
                        width = frame.getWidth();
                        inListener = false;
                    }
                }
                public function componentShown(e:java.awt.event.ComponentEvent):Void {
		    inListener = true;
		    visible = true;
		    showing = true;
		    inListener = false;
                }
                public function componentHidden(e:java.awt.event.ComponentEvent):Void {
                    inListener = true;
                    visible = false;
                    showing = false;
                    inListener = false;
                }
                public function componentMoved(e:java.awt.event.ComponentEvent):Void {
		    inListener = true;
		    var pt = frame.getLocation();
		    x = pt.getX();
		    y = pt.getY();
		    inListener = false;
                }
	    };
	    frame.addComponentListener(componentListener);
	}
	return frame;
    }

    init {
	System.out.println("init");
    }

    public function show(): Frame {
	System.out.println("show");
	getFrame();
	if (size == null or height == 0 or width == 0) {
	    frame.pack();
	    size = frame.getSize();
	    if (height <> 0) {
		size.height = height.intValue();
	    }
	    if (width <> 0) {
		size.width = width.intValue();
	    }
	    if (size.height <> 0 or size.width <> 0) {
		frame.setSize(size);
	    }
	} else {
	    frame.pack();
	    frame.setSize(size);
	}
	if (owner <> null) {
	    frame.setLocationRelativeTo(owner.getWindow());
	} else {
	    if (x <> 0 or y <> 0) {
		var location = new java.awt.Point(x.intValue(), y.intValue());
		frame.setLocation(location);
	    } else if (centerOnScreen) {
		var d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();                 
		var s = frame.getSize();
		frame.setLocation(d.width/2 - s.width/2, d.height/2 - s.height/2);
	    } else {
		var location = frame.getLocation();
	    }
	}
	javax.swing.SwingUtilities.invokeLater(Runnable {
		public function run():Void {
		    if (visible) {
			frame.setVisible(true);
			frame.toFront();
			inListener = true;
			var location = frame.getLocation();
			x = location.getX();
			y = location.getY();
			size = frame.getSize();
			height = size.height;
			width = size.width;
			inListener = false;
		    }
		}
	    });
	return this;
    }

    public function getJFrame(): javax.swing.JFrame {
	return getFrame();
    }
}
