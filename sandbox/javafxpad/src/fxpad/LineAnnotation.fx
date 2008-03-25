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

import javafx.ui.*;
import javafx.ui.canvas.*;
import javax.swing.event.ChangeListener;

/**
 * @author jclarke
 */

public class LineAnnotation {
    private attribute changeListeners: ChangeListener[];
    private attribute an: com.sun.javafx.api.ui.fxkit.FXTextArea.LineAnnotation;
    function getAn():com.sun.javafx.api.ui.fxkit.FXTextArea.LineAnnotation {
        if (an == null) {
            an = com.sun.javafx.api.ui.fxkit.FXTextArea.LineAnnotation {
                function getLine():Integer {
                    return line;
                }
                function getColumn():Integer {
                    return column;
                }
                function getLength():Integer {
                    return length;
                }
                function getToolTipText():String {
                    return toolTipText;
                }
                function setBounds(x:Integer, y:Integer, w:Integer, h:Integer) {
                    currentX = x;
                    currentY = y;
                    currentWidth = w;
                    currentHeight = h;
                }
                function getComponent():java.awt.Component {
                     return content.getComponent();
                }
                function addChangeListener(l:ChangeListener):Void {
                    insert l into changeListeners;
                }
                function removeChangeListener(l:ChangeListener):Void {
                    delete l from changeListeners;
                }
            };
        }
        return an;
    }
    private function fireChange() {
        for (i in changeListeners) {
            i.stateChanged(null);
        }
    }
    public attribute line: Integer on replace {
        fireChange();
    };
    public attribute column: Integer on replace {
        fireChange();
    };
    public attribute length: Integer on replace {
        fireChange();
    };
    public attribute currentX: Number;
    public attribute currentY: Number;
    public attribute currentWidth: Number;
    public attribute currentHeight: Number;
    public attribute content: Widget on replace {
        fireChange();
    };
    public attribute toolTipText: String;
}