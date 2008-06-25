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

package javafx.ext.swing;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.SwingConstants;
import java.lang.IllegalArgumentException;

public class ClusterPanel extends AbstractPanel {

    public attribute hcluster: Cluster;

    public attribute vcluster: Cluster;

    public attribute autoCreateGaps: Boolean = true on replace {
        getGroupLayout().setAutoCreateGaps(autoCreateGaps);
    }

    public attribute autoCreateContainerGaps: Boolean = true on replace {
        getGroupLayout().setAutoCreateContainerGaps(autoCreateContainerGaps);
    }

    public attribute honorsVisibility: Boolean = true on replace {
        getGroupLayout().setHonorsVisibility(honorsVisibility);
    }

    public /* final */ function linkSizes(components: Component[]) {
        getGroupLayout().linkSize(for (component in components) component.getRootJComponent());
    }

    public /* final */ function linkSizes(components: Component[], horizontal: Boolean) {
        var axis = if (horizontal) SwingConstants.HORIZONTAL else SwingConstants.VERTICAL;
        getGroupLayout().linkSize(axis, for (component in components) component.getRootJComponent());
    }

    protected /* final */ function remove(component: Component): Void {
        // PENDING(shannonh) - implement this
    }

    protected /* final */ function configureJPanel(jPanel: JPanel): Void {
        jPanel.setLayout(new GroupLayout(jPanel));
    }

    private function getGroupLayout(): GroupLayout {
        return getJPanel().getLayout() as GroupLayout;
    }

    private function setupGL() {
        if (hcluster == null or vcluster == null) {
            throw new IllegalArgumentException("must have both a horizontal and vertial cluster");
        }

        var gl = getGroupLayout();
        gl.setHorizontalGroup(hcluster.createGLGroup(true, gl));
        gl.setVerticalGroup(vcluster.createGLGroup(false, gl));
    }

    init {
        setupGL();
    }

}
