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

import fxpad.gui.*;
import javafx.ext.swing.*;
import javax.swing.event.ChangeListener;

/**
 * @author jclarke
 */

public class LineAnnotation {
    var changeListeners: ChangeListener[];
    var an: FXTextArea.LineAnnotation;
    function getAn(): FXTextArea.LineAnnotation {
        if (an == null) {
            an = FXTextArea.LineAnnotation {
                override function getLine():Integer {
                    return line;
                }
                override function getColumn():Integer {
                    return column;
                }
                override function getLength():Integer {
                    return length;
                }
                override function getToolTipText():String {
                    return toolTipText;
                }
                override function setBounds(x:Integer, y:Integer, w:Integer, h:Integer) {
                    currentX = x;
                    currentY = y;
                    currentWidth = w;
                    currentHeight = h;
                }
                override function getComponent():java.awt.Component {
                     return content.getJComponent() as java.awt.Component;
                }
                override function addChangeListener(l:ChangeListener):Void {
                    insert l into changeListeners;
                }
                override function removeChangeListener(l:ChangeListener):Void {
                    delete l from changeListeners;
                }
            };
        }
        return an;
    }
    function fireChange() {
        for (i in changeListeners) {
            i.stateChanged(null);
        }
    }
    public var line: Integer on replace {
        fireChange();
    };
    public var column: Integer on replace {
        fireChange();
    };
    public var length: Integer on replace {
        fireChange();
    };
    public var currentX: Number;
    public var currentY: Number;
    public var currentWidth: Number;
    public var currentHeight: Number;
    public var content: Component on replace {
        fireChange();
    };
    public var toolTipText: String;
}
