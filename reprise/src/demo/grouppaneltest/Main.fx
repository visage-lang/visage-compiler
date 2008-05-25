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
package demo.grouppaneltest;

import javafx.gui.*;
import java.lang.System;

// Here's a simple Java layout with two buttons in bottom-right.
// We'll re-create it in FX.
//
//        layout.setHorizontalGroup(layout.createSequentialGroup()
//                .addContainerGap(238, Short.MAX_VALUE)
//                .addComponent(jButton2)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                .addComponent(jButton1)
//                .addContainerGap());
//
//        layout.setVerticalGroup(layout.createSequentialGroup()
//                .addContainerGap(266, Short.MAX_VALUE)
//                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                    .addComponent(jButton1)
//                    .addComponent(jButton2))
//                .addContainerGap());

var f1 = Frame {
    name: "Frame";
    content: ClusterPanel{
        var b1 = Button{text: "Hello"}
        var b2 = Button{text: "World"}

        hcluster: SequentialCluster {
                      content: [ContainerGap{pref: 200 max: Layout.UNLIMITED_SIZE},
                                b2,
                                PreferredGap{/* placement: RELATED */},
                                b1,
                                ContainerGap{}]
                  }

        vcluster: SequentialCluster {
                      content: [ContainerGap{pref: 200  max: Layout.UNLIMITED_SIZE},
                                ParallelCluster {/* alignment: BASELINE */ content: [b1, b2]},
                                ContainerGap{}]
                  }

    }

    visible: true
}
