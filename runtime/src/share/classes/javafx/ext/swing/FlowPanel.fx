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
import java.awt.FlowLayout;
import javafx.scene.HorizontalAlignment;
import com.sun.javafx.scene.Util;

// PENDING_DOC_REVIEW
/**
 * A {@link Panel} that lays out its children in a directional flow, 
 * much like lines of text in a paragraph.
 * <p/>
 * {@code FlowPanel} is typically used to arrange buttons in a panel.
 * It arranges buttons horizontally until no more buttons fit on the same line.
 * The line alignment is determined by the {@code HorizontalAlignment} attribute.
 * <p>Example:</p>

@example
import javafx.ext.swing.*;
import javafx.scene.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.transform.*;

FlowPanel {
    hgap: 20
    vgap: 20
    content: [Canvas {
            content: Rectangle {
                x: 0
                y: 0
                width: 200
                height: 100
                arcWidth: 20
                arcHeight: 20
                fill: Color.RED
            }
        },
        Canvas {
            content: Text {
            content: "The text besides the rectangle"
            translateX: 0
            translateY: 15
            }
        }
    ]
}
@endexample
 */
public class FlowPanel extends Panel {

    // PENDING_DOC_REVIEW
    /**
     * Defines the horizontal gap between components and
     * between the components and the borders of the {@code FlowPanel}.
     */
    public attribute hgap: Integer = getFlowLayout().getHgap() on replace {
        getFlowLayout().setHgap(hgap);
        getJPanel().revalidate();
        getJPanel().repaint();
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the vertical gap between components and
     * between the components and the borders of the {@code FlowPanel}.
     */
    public attribute vgap: Integer = getFlowLayout().getVgap() on replace {
        getFlowLayout().setVgap(vgap);
        getJPanel().revalidate();
        getJPanel().repaint();
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the {@link HorizontalAlignment} for this {@code FlowPanel}.
     */
    public attribute alignment: HorizontalAlignment = Util.FLConstant_To_HA(getFlowLayout().getAlignment())
        on replace {
            getFlowLayout().setAlignment(Util.HA_To_FLConstant(alignment));
            getJPanel().revalidate();
            getJPanel().repaint();
        }

    protected /* final */ function configureJPanel(jPanel: JPanel): Void {
        jPanel.setLayout(new FlowLayout());
    }

    private function getFlowLayout(): FlowLayout {
        return getJPanel().getLayout() as FlowLayout;
    }

}
