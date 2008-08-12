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
import javafx.ext.swing.*;
import javax.swing.JTextArea;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;



/**
 * @author jclarke
 */

class StdoutStream extends ByteArrayOutputStream {
    var  stdout : StdoutPane;
    
    override function flush() : Void {
       var str = new String(toByteArray());
       stdout.append(str);
    }
}

public class StdoutPane extends ScrollableComponent {
   
    public function getJTextArea() : JTextArea {
        getJComponent() as JTextArea;
    }
    public var rows: Number = getJTextArea().getRows() on replace {
        getJTextArea().setRows(rows);
    };
    public var columns: Number = getJTextArea().getColumns() on replace {
        getJTextArea().setColumns(columns);
    };
    public function clear(): Void {
        getJTextArea().setText("");
        
    }
    public function append(atext:String) : Void {
        getJTextArea().append(atext);
    }
    
    postinit {
        var out = new PrintStream(StdoutStream {stdout: this}, true);
        System.out.println("Redirecting output to FXPad console panel");
        System.setErr(out);
        System.setOut(out)        
    }
    
    override function createJComponent(): javax.swing.JComponent {
        var ta = new JTextArea();
        ta.setOpaque(false);
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.getCaret().setVisible( true );        
        ta;
    }
    
}
