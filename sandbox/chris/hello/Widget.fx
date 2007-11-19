package hello;
import javax.swing.*;
import java.awt.event.*;
import java.lang.System;

public abstract class Widget extends AbstractWidget {

    /* flag to prevent intermediate bounds update */
    private attribute inComponentListener = false;

    private function updateBounds() {
        if (not inComponentListener and component <> null) {
            component.setBounds(x.intValue(), y.intValue(), width.intValue(), height.intValue());
        }
    }
    
    public attribute height: Number = 0 on replace { updateBounds(); }
    public attribute width: Number = 0 on replace { updateBounds(); }
    public attribute x: Number = 0 on replace { updateBounds(); }
    public attribute y: Number = 0 on replace { updateBounds(); }

    public attribute opaque: Boolean = false on replace { 
        if (component <> null) {
            component.setOpaque(opaque);
        } 
    }

    public attribute showing: Boolean = false;
 
    protected attribute component: JComponent = null;
    protected abstract function createComponent(): JComponent;
    
    private function setBounds() {
        var b = component.getBounds();
        inComponentListener = true;
	if (x.intValue() <> b.getX()) {
	    x = b.getX();
	}
	if (y.intValue() <> b.getY()) {
	    y = b.getY();
	}
	if (width.intValue() <> b.getWidth()) {
	    width = b.getWidth();
	}
	if (height.intValue() <> b.getHeight()) {
	    height = b.getHeight();
	}
        inComponentListener = false;
    }
    
    public attribute onMouseDrag: function(event:MouseEvent):Void;
    public attribute onMouseMove: function(event:MouseEvent):Void;
    
    public function getComponent(): JComponent {
        if (component == null) { 
            var comp = createComponent();
	    var size = comp.getPreferredSize();
	    if (width == 0) { 
		width = size.width;
	    }
	    if (height == 0) {
		height = size.height;
	    }
	    if (x == 0) {
		x = comp.getX();
	    }
	    if (y == 0) {
		y = comp.getY();
	    }
	    comp.setOpaque(opaque);
            comp.addMouseMotionListener(MouseMotionListener {
                public function mouseDragged(event:java.awt.event.MouseEvent):Void {
                    var e = MouseEvent { x: event.getX(), y: event.getY(), button: 1 };
		    if (onMouseDrag <> null) {
			onMouseDrag(e);
		    }
                }
                public function mouseMoved(event:java.awt.event.MouseEvent):Void {
                    var e = MouseEvent { x: event.getX(), y: event.getY(), button: 1 };
		    if (onMouseMove <> null) {
			onMouseMove(e);
		    }
                }
                
            });
            comp.addComponentListener(ComponentListener {
                public function componentMoved(event:ComponentEvent):Void {
                    setBounds();
                }
                public function componentResized(event:ComponentEvent):Void {
                    setBounds();
                }
                public function componentShown(event:ComponentEvent):Void {
                    showing = true;
                    
                }
                public function componentHidden(event:ComponentEvent):Void {
                    showing = false;
                }
            });
	    component = comp;
	    System.out.println("created component {component}");
        }
        return component;
    }
    
    public function getJComponent(): JComponent { return getComponent(); }
}
