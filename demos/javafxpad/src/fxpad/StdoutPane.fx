/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package fxpad;

import java.lang.System;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javax.swing.JTextArea;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;



/**
 * @author jclarke
 */

class StdoutStream extends ByteArrayOutputStream {
    attribute  stdout : StdoutPane;
    
    public function flush() : Void {
       var str = new String(toByteArray());
       stdout.append(str);
    }
}

public class StdoutPane extends ScrollableWidget {
   
    private attribute jtextarea: JTextArea = new JTextArea() on replace {
        jtextarea.setOpaque(false);
        jtextarea.setEditable(false);
        jtextarea.setLineWrap(true);
        jtextarea.getCaret().setVisible( true );
    }
    private attribute menu:PopupMenu = PopupMenu {
        owner: this
        items: MenuItem {
            text: "Clear"
            action: function():Void {
                clear();
                menu.visible = false;
            }
        }
        
    };     
    public attribute rows: Number = jtextarea.getRows() on replace {
        jtextarea.setRows(rows);
    };
    public attribute columns: Number = jtextarea.getColumns() on replace {
        jtextarea.setColumns(columns);
    };
    public function clear(): Void {
        jtextarea.setText("");
        
    }
    public function append(atext:String) : Void {
        jtextarea.append(atext);
    }
    
    postinit {
        var out = new PrintStream(StdoutStream {stdout: this}, true);
        System.out.println("Redirecting output to FXPad console panel");
        System.setErr(out);
        System.setOut(out)        
    }
    
    public function createView(): javax.swing.JComponent {
        jtextarea;
    }
    
}