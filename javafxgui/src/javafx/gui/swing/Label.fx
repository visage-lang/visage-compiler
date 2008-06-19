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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javafx.gui.*;
import com.sun.javafx.gui.Util;

// PENDING_DOC_REVIEW
/**
 * A display area for a short text string or an icon, or both.
 */
public class Label extends Component {

    // PENDING_DOC_REVIEW
    /**
     * Defines the single line of text this component will display.  If
     * the value of text is null or empty string, nothing is displayed.
     */
    public attribute text: String on replace {
        doAndIgnoreJComponentChange(function() {
            getJLabel().setText(text);
        });
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the icon this component will display.  If
     * the value of icon is null, nothing is displayed.
     */
    public attribute icon: Icon = Icon.fromToolkitIcon(getJLabel().getIcon()) on replace {
        doAndIgnoreJComponentChange(function() {
            getJLabel().setIcon(icon.getToolkitIcon());
        });
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the alignment of the label's contents along the X axis.
     */
    public attribute horizontalAlignment: HorizontalAlignment =
        Util.SwingConstant_To_HA(getJLabel().getHorizontalAlignment())
        on replace {
            doAndIgnoreJComponentChange(function() {
                getJLabel().setHorizontalAlignment(Util.HA_To_SwingConstant(horizontalAlignment));
            });
        }

    // PENDING_DOC_REVIEW
    /**
     * Defines the alignment of the label's contents along the Y axis.
     */
    public attribute verticalAlignment: VerticalAlignment =
        Util.SwingConstant_To_VA(getJLabel().getVerticalAlignment())
        on replace {
            doAndIgnoreJComponentChange(function() {
                getJLabel().setVerticalAlignment(Util.VA_To_SwingConstant(verticalAlignment));
            });
        }

    // PENDING_DOC_REVIEW
    /**
     * Defines the horizontal position of the label's text,
     * relative to its icon.
     */
    public attribute horizontalTextPosition: HorizontalAlignment =
        Util.SwingConstant_To_HA(getJLabel().getHorizontalTextPosition())
        on replace {
            doAndIgnoreJComponentChange(function() {
                getJLabel().setHorizontalTextPosition(Util.HA_To_SwingConstant(horizontalTextPosition));
            });
        }

    // PENDING_DOC_REVIEW
    /**
     * Defines the vertical position of the label's text,
     * relative to its icon.
     */
    public attribute verticalTextPosition: VerticalAlignment =
        Util.SwingConstant_To_VA(getJLabel().getVerticalTextPosition())
        on replace {
            doAndIgnoreJComponentChange(function() {
                getJLabel().setVerticalTextPosition(Util.VA_To_SwingConstant(verticalTextPosition));
            });
        }

    /**
     * Defines the component this is labelling. 
     * Can be null if this does not label a Component. 
     */
    public attribute labelFor: Component = null on replace {
        doAndIgnoreJComponentChange(function() {
            getJLabel().setLabelFor(labelFor.getJComponent());
        });
    }

    postinit {
        var lbl = getJLabel();

	lbl.addPropertyChangeListener(PropertyChangeListener {
            public function propertyChange(e: PropertyChangeEvent): Void {
                if (ignoreJComponentChange) {
                    return;
                }

                var propName = e.getPropertyName();
                if ("text".equals(propName)) {
                    text = lbl.getText();
                } else if ("icon".equals(propName)) {
                    icon = Icon.fromToolkitIcon(lbl.getIcon());
                } else if ("horizontalAlignment".equals(propName)) {
                    horizontalAlignment = Util.SwingConstant_To_HA(lbl.getHorizontalAlignment());
                } else if ("verticalAlignment".equals(propName)) {
                    verticalAlignment = Util.SwingConstant_To_VA(lbl.getVerticalAlignment());
                } else if ("horizontalTextPosition".equals(propName)) {
                    horizontalTextPosition = Util.SwingConstant_To_HA(lbl.getHorizontalTextPosition());
                } else if ("verticalTextPosition".equals(propName)) {
                    verticalTextPosition = Util.SwingConstant_To_VA(lbl.getVerticalTextPosition());
                } else if ("labelFor".equals(propName)) {
                    var comp = lbl.getLabelFor();
                    labelFor = if (comp instanceof JComponent) Component.fromJComponent(comp as JComponent) else null;
                }
            }
        });
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns the {@link JLabel} delegate for this component.
     */
    public /* final */ function getJLabel(): JLabel {
        getJComponent() as JLabel;
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates the specific {@link JComponent} delegate for this component.
     */
    protected /* final */ function createJComponent(): JComponent {
        new JLabel();
    }

}
