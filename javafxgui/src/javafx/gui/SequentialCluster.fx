/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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
package javafx.gui;

import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;

public class SequentialCluster extends Cluster {

    function createGLGroupImpl(horizontal: Boolean, gl: GroupLayout): GroupLayout.Group {
        return gl.createSequentialGroup();
    }

    function addClusterElement(gl: GroupLayout, group: GroupLayout.Group, horizontal: Boolean, ce: ClusterElement): Void {
        var sGroup = group as GroupLayout.SequentialGroup;

        if (ce instanceof Component) {
            var comp = ce as Component;
            var min: Integer;
            var pref: Integer;
            var max: Integer;
            var isbaseline: Boolean;

            if (horizontal) {
                min = comp.hmin;
                pref = comp.hpref;
                max = comp.hmax;
                isbaseline = comp.hisbaseline;
            } else {
                min = comp.vmin;
                pref = comp.vpref;
                max = comp.vmax;
                isbaseline = comp.visbaseline;
            }

            sGroup.addComponent(isbaseline, comp.getRootJComponent(), min, pref, max);
        } else if (ce instanceof Cluster) {
            var cluster = ce as Cluster;
            sGroup.addGroup(cluster.useAsBaseline, cluster.createGLGroup(horizontal, gl));
        } else if (ce instanceof ContainerGap) {
            var cg = ce as ContainerGap;
            sGroup.addContainerGap(cg.pref, cg.max);
        } else if (ce instanceof PreferredGap) {
            var pg = ce as PreferredGap;
            sGroup.addPreferredGap(pg.type.getToolkitValue(), pg.pref, pg.max);
        } else {
            Cluster.addClusterElement(gl, group, horizontal, ce);
        }
    }

}
