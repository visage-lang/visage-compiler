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

import javax.swing.JComponent;
import javax.swing.JPanel;
import javafx.lang.Sequences;
import javafx.lang.FX;

/**
 * A {@code Component Container} that lays out its children absolutely,
 * based on their {@code x, y, width, height} attributes.
 */
public class Panel extends AbstractPanel {

    /**
     * The {@code Components} contained in this {@code Panel}.
     */
    public attribute content: Component[] on replace oldContent[a..b] = newSlice {
        var jPanel = getJPanel();

        for (component in oldContent[a..b]) {
            jPanel.remove(component.getRootJComponent());
            unparentFromThisContainer(component);
        }

        var index = if (a == 0) 0 else indexOfJComponentInJPanel(content[a - 1].getRootJComponent(), jPanel) + 1;
        for (component in newSlice) {
            var rootj = component.getRootJComponent();
            var oldidx = indexOfJComponentInJPanel(rootj, jPanel);
            if (oldidx <> -1 and oldidx < index) {
                jPanel.add(rootj, index - 1);
            } else {
                jPanel.add(rootj, index++);
            }
            parentToThisContainer(component);
        }

        jPanel.revalidate();
        jPanel.repaint();
        resetContentFromJPanel();
    }

    private static function indexOfJComponentInJPanel(component: JComponent, panel: JPanel): Integer {
        var children = panel.getComponents();
        return Sequences.indexByIdentity(children, component);
    }

    private function resetContentFromJPanel(): Void {
        var jPanel = getJPanel();
        var fromJPanel = for (i in [0..<jPanel.getComponentCount()],
                              j in [Component.getComponentFor(jPanel.getComponent(i) as JComponent)]
                              where j <> null) j as Component;

        if (not Sequences.isEqualByContentIdentity(content, fromJPanel)) {
            // PENDING(shannonh) - want to do this without firing the trigger
            // http://openjfx.java.sun.com/jira/browse/JFXC-1007
            content = fromJPanel;
         }
    }

    /**
     * {@inheritDoc}
     */
    protected /* final */ function remove(component: Component): Void {
        // PENDING(shannonh) - what I really want here is a deleteByIdentity operator
        // http://openjfx.java.sun.com/jira/browse/JFXC-1005
        var indices = for (c in content where FX.isSameObject(c, component)) indexof c;
        for (i in [sizeof indices - 1..0 step -1]) {
            delete content[indices[i]];
        }
    }

}
