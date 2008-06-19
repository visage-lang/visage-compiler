/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
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

package javafx.gui.swing;

import javax.swing.GroupLayout;

public class ParallelCluster extends Cluster {

    public attribute resizable: Boolean = true;

    public attribute childAlignment: Layout.Alignment = Layout.BASELINE;

    public attribute anchorBaselineToTop: Boolean = false;

    function createGLGroupImpl(horizontal: Boolean, gl: GroupLayout): GroupLayout.Group {
        var ca = childAlignment.getToolkitValue();

        if (ca == Layout.BASELINE.getToolkitValue()) {
            if (not horizontal) {
                return gl.createBaselineGroup(resizable, anchorBaselineToTop);
            }

            ca = Layout.LEADING.getToolkitValue();
        }

        return gl.createParallelGroup(ca, resizable);
    }

    function addClusterElement(gl: GroupLayout, group: GroupLayout.Group, horizontal: Boolean, ce: ClusterElement): Void {
        var pGroup = group as GroupLayout.ParallelGroup;

        if (ce instanceof Component) {
            var comp = ce as Component;
            var min: Integer;
            var pref: Integer;
            var max: Integer;
            var align: Layout.Alignment;

            if (horizontal) {
                min = comp.hmin;
                pref = comp.hpref;
                max = comp.hmax;
                align = comp.halign;
            } else {
                min = comp.vmin;
                pref = comp.vpref;
                max = comp.vmax;
                align = comp.valign;
            }

            if (align == null) {
                pGroup.addComponent(comp.getRootJComponent(), min, pref, max);
            } else {
                pGroup.addComponent(comp.getRootJComponent(), align.getToolkitValue(), min, pref, max);
            }
        } else if (ce instanceof Cluster) {
            var cluster = ce as Cluster;
            
            if (cluster.align == null) {
                pGroup.addGroup(cluster.createGLGroup(horizontal, gl));
            } else {
                pGroup.addGroup(cluster.align.getToolkitValue(), cluster.createGLGroup(horizontal, gl));
            }
        } else {
            Cluster.addClusterElement(gl, group, horizontal, ce);
        }
    }

}
