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

//TODO JXFC-284 NPE
import javafx.ui.InternalFrameDragMode;
import javafx.ui.InternalFrame;

public class DesktopPane extends Widget {
    private attribute jdesk: javax.swing.JDesktopPane;

    public attribute dragMode: InternalFrameDragMode = InternalFrameDragMode.LIVE on replace {
        jdesk.setDragMode(dragMode.id.intValue());
    };
    public attribute frames: InternalFrame[]
        on insert [ndx] (frame) {
            frame.desk = this;
            var i = frame;
            var b = new java.awt.Rectangle(i.x.intValue(), i.y.intValue(), i.width.intValue(), i.height.intValue());
            jdesk.add(frame.getComponent(), frame.layer.intValue(), 0);
            i.setBounds(b);
            frame.getComponent().setVisible(true);
        }
        on delete [ndx] (frame) {
            jdesk.remove(frame.getComponent());
        }
    public attribute cascaded: Boolean on replace {

            if (cascaded) {
                var dframes = jdesk.getAllFrames();
                var x = 0.0;
                var y = 0.0;
                var w = 0.0;
                var h = jdesk.getHeight();
                var d = (h / sizeof dframes).intValue();
                if (d < 15) {
                    d = 15;
                }
                //foreach (f in reverse frames) {

                var n = sizeof dframes - 1;
                foreach (i in [n..0]) {
                    var f = dframes[i];
                    try {
                        f.setVisible(true);
                        f.setIcon(false);
                    } catch (ignore:java.lang.Exception) {
                    }
                    try {
                        f.setMaximum(false);
                    } catch (ignore1:java.lang.Exception) {
                    }
                    var dim = f.getSize();
                    w = dim.width;
                    h = dim.height;
                    jdesk.getDesktopManager().setBoundsForFrame(f, x.intValue(), y.intValue(), w.intValue(), h.intValue());
                    x += d;
                    y += d;
                }

                cascaded = false;
            }

        };
    public attribute tiled: Boolean on replace {
            if (tiled) {
                //TODO JFXC-344
                /*********************
                var dframes = jdesk.getAllFrames();
                var rows = java.lang.Math.sqrt(sizeof dframes).intValue();
                var cols = rows;
                if (rows * cols < sizeof dframes) {
                    cols += 1;
                    if (rows * cols < sizeof dframes) {
                        rows += 1;
                    }
                }
                var size = jdesk.getSize();
                var w = (size.width / cols).intValue();
                var h = (size.height/ rows).intValue();
                var x = 0;
                var y = 0;
                foreach (i in [0..rows exclusive]) {
                    foreach (j in [0..cols exclusive]) {
                        var cell = (i * cols) + j;
                        if (cell >= sizeof dframes) {
                            break;
                        }
                        var f = dframes[cell];
                        try {
                            f.setVisible(true);
                            f.setIcon(false);
                        } catch (ignore:java.lang.Exception) {
                        }
                        try {
                            f.setMaximum(false);
                        } catch (ignore1:java.lang.Exception) {
                        }
                        jdesk.getDesktopManager().setBoundsForFrame(f, x, y, w, h);
                        x += w;
                    }
                    y += h;
                    x = 0;
                }
                ************ END OF JFXC-344 ********/
                tiled = false;
            }
        };
    public attribute focusable: Boolean = false;

    public function createComponent():javax.swing.JComponent {
    /**********
        jdesk = new javax.swing.JDesktopPane();
        jdesk.setOpaque(false);
        jdesk.setDragMode(dragMode.id);
        foreach (i in frames) {
            var count = jdesk.getComponentCountInLayer(i.layer);
            var b = new java.awt.Rectangle(i.x, i.y, i.width, i.height);
            jdesk.add(i.getComponent(), i.layer.intValue(), 0);
            i.setBounds(b);
            i.getComponent().setVisible(true);
        }
*********/
        return jdesk;
    }
}

