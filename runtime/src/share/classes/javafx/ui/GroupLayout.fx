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

/**
 * Interface for elements that perform group layout.
 */

public class GroupLayout extends GroupElement {
    /** Vertical alignment of the contained elements. Defaults to BASELINE.*/
    public attribute valign: Alignment = Alignment.BASELINE;

    /** Horizontal alignment of the contained elements. Defaults to LEADING.*/
    public attribute halign: Alignment = Alignment.LEADING;

    /** The row definitions for this layout. */
    public attribute rows: Row[];

    /** The column definitions for this layout. */
    public attribute columns: Column[];

    /** The contents of this layout. */
    public attribute content: GroupElement[];

    public function addRows(host:java.awt.Container,
            layout:org.jdesktop.layout.GroupLayout, 
            vgroup:org.jdesktop.layout.GroupLayout.SequentialGroup) : Void {
            //TODO this had content.row, not sure what that means, I assume subscript 0
        if (rows == null and content[0].row == null) {
            rows = [Row {}];
            foreach (i in content) {
                i.row = rows[0];
            }
        }
        foreach (i in rows) {
            var pgroup = layout.createParallelGroup(i.alignment.id.intValue(), i.resizable);
            vgroup.add(pgroup);
            var comps:java.awt.Component[] = [];
            //TODO JXFC-244
            //foreach (j in content where j.row == i) {
            foreach(j in content) { // Workaround
                if (j instanceof Widget) {
                    var w =  j as Widget;
                    if (w.sizeToFitRow) {
                        insert w.getComponent() into comps;
                    }
                    var c = w.getComponent();
                    if (c == null) {
                        throw new java.lang.Exception("Component was null for Widget {w}");
                    }
                    if (j.vertical <> null) {
                        var spring = j.vertical;
                        pgroup.add(c, spring.min.intValue(), spring.pref.intValue(), spring.max.intValue());
                    } else if (i.resizable) {
                        pgroup.add(c,  
                                   0,
                                   org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                   java.lang.Integer.MAX_VALUE);
                    } else {
                        pgroup.add(c, 
                                   org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                   org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                   org.jdesktop.layout.GroupLayout.PREFERRED_SIZE);
                    }
                } else if (j instanceof Gap) {
                    var spring = j.vertical;
                    pgroup.add(spring.min.intValue(), spring.pref.intValue(), spring.max.intValue());
                } else if (j instanceof GroupLayout) {
                    var sg = layout.createSequentialGroup(); 
                    var g = j as GroupLayout;
                    g.addRows(host, layout, sg);
                    pgroup.add(i.alignment.id.intValue(), sg);
                }
            }
            if (sizeof comps > 0) {
                layout.linkSize(comps, layout.VERTICAL);
            }
        }
    }
    public function addColumns(host:java.awt.Container, 
            layout:org.jdesktop.layout.GroupLayout, 
            hgroup:org.jdesktop.layout.GroupLayout.SequentialGroup):Void {
//TODO this had content.column, not sure what that means, I assume subscript 0
        if (columns == null and content[0].column == null) {
           columns = [Column {}];
           foreach (i in content) {
               i.column = columns[0];
           }
        }
        foreach (i in columns) {
            var pgroup = layout.createParallelGroup(i.alignment.id.intValue(), i.resizable);
            hgroup.add(pgroup);
            var comps:java.awt.Component[] = [];
            //TODO JXFC-244
            //foreach (j in content where j.column == i) {
            foreach(j in content) { // Workaround
                if (j instanceof Widget) {
                    var w = j as Widget;
                    var c = w.getComponent();
                    if (c == null) {
                        throw new java.lang.Exception("Component was null for Widget {w}");
                    }
                    if (w.sizeToFitColumn) {
                        insert c into comps;
                    }
                    if (j.horizontal <> null) {
                        var spring = j.horizontal;
                        pgroup.add(c, spring.min.intValue(), spring.pref.intValue(), spring.max.intValue());
                    } else if (i.resizable) {
                        pgroup.add(c,  
                                   0,
                                   org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                   java.lang.Integer.MAX_VALUE);
                    } else {
                        pgroup.add(c, 
                                   org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                   org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                   org.jdesktop.layout.GroupLayout.PREFERRED_SIZE);
                    }
                } else if (j instanceof Gap) {
                    var spring = j.horizontal;
                    pgroup.add(spring.min.intValue(), spring.pref.intValue(), spring.max.intValue());
                } else if (j instanceof GroupLayout) {
                    var g = j as GroupLayout;
                    var sg = layout.createSequentialGroup(); 
                    g.addColumns(host, layout, sg);
                    pgroup.add(i.alignment.id.intValue(), sg);
                }
            }
            if (sizeof comps > 0) {
                layout.linkSize(comps, layout.HORIZONTAL);
            }
        }
    }

    public function addComponents(host:java.awt.Container) : Void {
        foreach (e in content where e instanceof GroupLayout) {
            (e as GroupLayout).addComponents(host);
        }
        foreach (e in content where e instanceof Widget) {
            var comp = (e as Widget).getComponent();
            host.add(comp);
        }
    }
}


