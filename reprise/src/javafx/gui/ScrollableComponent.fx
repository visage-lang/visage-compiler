/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
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

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

// PENDING_DOC_REVIEW
/**
 * Provides a scrollable view of a specified component.
 */
public abstract class ScrollableComponent extends Component {

    private attribute jScrollPane: JScrollPane;

    // PENDING_DOC_REVIEW
    /**
     * Determines if the horizontal and vertical scrollbars 
     * appear in the {@code ScrollableComponent}.
     */
    public attribute scrollable: Boolean = true on replace {
        var sp = getJScrollPane();
        if (scrollable) {
            sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            sp.setBorder((new JScrollPane()).getBorder());
            relayoutScrollPane();
        } else {
            sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            sp.setBorder(new EmptyBorder(0, 0, 0, 0));
            relayoutScrollPane();
        }
    }

    init {
        jScrollPane.setViewportView(getViewComponent().getJComponent());
    }

    function getViewComponent(): Component {
        this;
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns the {@code JScrollPane} delegate for this component.
     */
    public /* final */ function getJScrollPane(): JScrollPane {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
        }

        jScrollPane;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation is overridden to delegate to {@link #getJScrollPane()}.
     */
    public function getRootJComponent(): JComponent {
        getJScrollPane();
    }

    private function relayoutScrollPane(): Void {
        var parent = jScrollPane.getParent();
        if (parent instanceof JComponent) {
            (parent as JComponent).revalidate();
            (parent as JComponent).repaint();
        }
    }

}
