package javafx.application;

import java.awt.geom.Point2D;
import java.lang.System;
import javafx.fxunit.*;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGRectangle;
import com.sun.scenario.scenegraph.event.SGNodeListener;
import com.sun.scenario.scenegraph.event.SGNodeEvent;
import com.sun.scenario.scenegraph.event.SGKeyListener;
import com.sun.scenario.scenegraph.event.SGMouseListener;
import com.sun.scenario.scenegraph.event.SGFocusListener;
import com.sun.scenario.scenegraph.event.SGMouseAdapter;
import com.sun.scenario.scenegraph.fx.FXNode;

/**
 * @test/fxunit
 */
public class WindowTest extends FXTestCase {
    
    /**
     * Tests that setting the name updates the java.awt.Window backing
     * the fx Window (and that setting the name works in the first place)
     */
    function testName() {
        var win = MockWindow { name:"Foo" };
        assertEquals("Foo", win.window.getName());
        assertEquals(win.name, win.window.getName());
        
        win.name = "Bar";
        assertEquals("Bar", win.window.getName());
        assertEquals(win.name, win.window.getName());
    }
    
    /**
     * Tests that setting the title updates the java.awt.Window backing this
     * window.
     */
    function testTitle() {
        var win = MockWindow { title:"Foo" };
        assertEquals("Foo", (win.window as javax.swing.JFrame).getTitle());
        assertEquals(win.title, (win.window as javax.swing.JFrame).getTitle());
        assertEquals(win.mockTitle, win.title);
        
        win.title = "Bar";
        assertEquals("Bar", (win.window as javax.swing.JFrame).getTitle());
        assertEquals(win.title, (win.window as javax.swing.JFrame).getTitle());
        assertEquals(win.mockTitle, win.title);
    }
    
    /**
     * Tests that if you attempt to change the "resizable" attribute of a
     * window that allows you to toggle its resizable property, that everything
     * works
     */
    function testResizable() {
        var win = MockWindow { resizable:true };
        assertEquals(true, win.resizable);
        assertEquals(true, (win.window as javax.swing.JFrame).isResizable());
        win.resizable = false;
        assertEquals(false, win.resizable);
        assertEquals(false, (win.window as javax.swing.JFrame).isResizable());
        win.resizable = true;
        assertEquals(true, win.resizable);
        assertEquals(true, (win.window as javax.swing.JFrame).isResizable());
    }
    
    function testResizeOfResizableWindow() {
    }
    
    function testResizeOfNonResizableWindow() {
        
    }
    
    // x/y/width/height
    
    // visible
    
    // stage
    
    // opacity
    
    // toFront/toBack
    function testCloseAction() {
        
    }
    
    // close()
}

class MockWindow extends Window {
    attribute mockTitle:String;
    attribute mockResizable:Boolean;
    
    function setWindowTitle(t:String): Void {
        mockTitle = t;
        (window as javax.swing.JFrame).setTitle(t);
    }
    
    function isWindowResizable(): Boolean { resizable }
    
    function setWindowResizable(r:Boolean): Void {
        mockResizable = r;
        (window as javax.swing.JFrame).setResizable(r);
    }

    function setUndecorated(b:Boolean) : Void {
        (window as javax.swing.JFrame).setUndecorated(b);
    }
    
    // Note: Window makes the assumption that the java.awt.Window returned will
    // be a root pane container.
    function createWindow(): java.awt.Window {
        new javax.swing.JFrame()
    }
}
