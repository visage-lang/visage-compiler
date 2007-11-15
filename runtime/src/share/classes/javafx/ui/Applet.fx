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
import javafx.ui.Widget;
import javafx.ui.MenuBar;


public class Applet extends Widget {
    private attribute applet:javax.swing.JApplet;
    public attribute menubar: MenuBar on replace {
        applet.setJMenuBar(menubar.jmenubar);
    }
    public attribute content: Widget on replace  {
        applet.setContentPane(content.getComponent());
    }
    public function showDocument(url:String){
        this.showDocumentInFrame(url, "_self");
    }
    
    public function showDocumentInFrame(url:String, frameName:String){
        if (applet <> null) {
            applet.getAppletContext().showDocument(new java.net.URL(url),
                                                   frameName);
        }
    }
    public function getWindow():java.awt.Window {
        return javax.swing.SwingUtilities.getWindowAncestor(applet);
    }
    public static attribute APPLICATION = Applet{};
    public function createComponent():javax.swing.JComponent{
        return null;
    }
    
    init {
            applet = UIElement.context.getApplet();
            Applet.APPLICATION = this;
    }
}


