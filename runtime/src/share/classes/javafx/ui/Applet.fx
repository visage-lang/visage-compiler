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

import com.sun.javafx.api.ui.FXApplet;
import java.lang.IllegalArgumentException;
import javafx.ui.Widget;
import javafx.ui.MenuBar;
import javax.swing.*;

public class Applet extends FXApplet {
    
    public attribute menubar: MenuBar on replace {
        setJMenuBar(menubar.getComponent() as JMenuBar);
    }
    
    public attribute content: Widget on replace {
        if (content <> null) {
            setContentPane(content.getComponent());
        }
    }
    
    public attribute glassPane: Widget on replace {
        if (glassPane <> null) {
            setGlassPane(glassPane.getComponent());
        }
    }
    
    public function showDocument(url:String){
        this.showDocumentInFrame(url, "_self");
    }
    
    public function showDocumentInFrame(url:String, frameName:String){
        getAppletContext().showDocument(new java.net.URL(url),
                                                   frameName);
    }
    
    public function getWindow():java.awt.Window {
        return SwingUtilities.getWindowAncestor(this);
    }

    public function createComponent():JComponent{
        return null;
    }
    
    public function start(): Void {}
    
    public function stop(): Void {}
    
    protected function setContentAttribute(object: java.lang.Object): Void {
        if (object instanceof Applet) {
            var applet = object as Applet;
            content = applet.content;
        } else if (object instanceof Widget) {
            var widget = object as Widget;
            content = widget;
        } else {
            throw new IllegalArgumentException("bad content type: {object.getClass()}");
        }
    }
}
