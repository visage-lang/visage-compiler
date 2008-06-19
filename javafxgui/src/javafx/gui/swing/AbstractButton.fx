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

import javax.swing.JComponent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.*;
import javafx.gui.*;
import com.sun.javafx.gui.Util;

// PENDING(shannonh) - margin, gap, more icons
// PENDING_DOC_REVIEW
/**
 * The {@code AbstractButton} class defines
 * common behaviors for buttons and menu items.
 */
public abstract class AbstractButton extends Component {

   // PENDING_DOC_REVIEW
   /**
    * Represents the button's text.
    */
    public attribute text: String on replace {
        doAndIgnoreJComponentChange(function() {
            getAbstractButton().setText(text);
        });
    }

   // PENDING_DOC_REVIEW
   /**
    * Represents the button's icon.
    */
    public attribute icon: Icon = Icon.fromToolkitIcon(getAbstractButton().getIcon()) on replace {
        doAndIgnoreJComponentChange(function() {
            getAbstractButton().setIcon(icon.getToolkitIcon());
        });
    }

   // PENDING_DOC_REVIEW
   /**
    * Represents the button's pressed icon.
    */
    public attribute pressedIcon: Icon = Icon.fromToolkitIcon(getAbstractButton().getPressedIcon()) on replace {
        doAndIgnoreJComponentChange(function() {
            getAbstractButton().setPressedIcon(pressedIcon.getToolkitIcon());
        });
    }

   // PENDING_DOC_REVIEW
   /**
    * Represents the horizontal alignment of the text and icon.
    */
    public attribute horizontalAlignment: HorizontalAlignment =
        Util.SwingConstant_To_HA(getAbstractButton().getHorizontalAlignment())
        on replace {
            doAndIgnoreJComponentChange(function() {
                getAbstractButton().setHorizontalAlignment(Util.HA_To_SwingConstant(horizontalAlignment));
            });
        }

   // PENDING_DOC_REVIEW
   /**
    * Represents the vertical alignment of the text and icon.
    */
    public attribute verticalAlignment: VerticalAlignment =
        Util.SwingConstant_To_VA(getAbstractButton().getVerticalAlignment())
        on replace {
            doAndIgnoreJComponentChange(function() {
                getAbstractButton().setVerticalAlignment(Util.VA_To_SwingConstant(verticalAlignment));
            });
        }

   // PENDING_DOC_REVIEW
   /**
    * Represents the horizontal position of the text relative to the icon.
    */
    public attribute horizontalTextPosition: HorizontalAlignment =
        Util.SwingConstant_To_HA(getAbstractButton().getHorizontalTextPosition())
        on replace {
            doAndIgnoreJComponentChange(function() {
                getAbstractButton().setHorizontalTextPosition(Util.HA_To_SwingConstant(horizontalTextPosition));
            });
        }

   // PENDING_DOC_REVIEW
   /**
    * Represents the vertical position of the text relative to the icon.
    */
    public attribute verticalTextPosition: VerticalAlignment =
        Util.SwingConstant_To_VA(getAbstractButton().getVerticalTextPosition())
        on replace {
            doAndIgnoreJComponentChange(function() {
                getAbstractButton().setVerticalTextPosition(Util.VA_To_SwingConstant(verticalTextPosition));
            });
        }

   // PENDING_DOC_REVIEW
   /**
    * Represents the button's action.
    */
    public attribute action: function(): Void;

    // PENDING(shannonh)
    // A hack to prevent against init being called twice when AbstractButton is
    // inherited twice by CheckBoxMenuItem and RadioButtonMenuItem
    // Will be removed when http://openjfx.java.sun.com/jira/browse/JFXC-751 is
    // fixed.
    private attribute hack_InitCalled = false;

    postinit {
        if (not hack_InitCalled) {
            hack_InitCalled = true;

            var ab = getAbstractButton();

	    ab.addPropertyChangeListener(PropertyChangeListener {
                public function propertyChange(e: PropertyChangeEvent): Void {
                    if (ignoreJComponentChange) {
                        return;
                    }

                    var propName = e.getPropertyName();
                    if ("text".equals(propName)) {
                        text = ab.getText();
                    } else if ("icon".equals(propName)) {
                        icon = Icon.fromToolkitIcon(ab.getIcon());
                    } else if ("pressedIcon".equals(propName)) {
                        icon = Icon.fromToolkitIcon(ab.getIcon());
                    } else if ("horizontalAlignment".equals(propName)) {
                        horizontalAlignment = Util.SwingConstant_To_HA(ab.getHorizontalAlignment());
                    } else if ("verticalAlignment".equals(propName)) {
                        verticalAlignment = Util.SwingConstant_To_VA(ab.getVerticalAlignment());
                    } else if ("horizontalTextPosition".equals(propName)) {
                        horizontalTextPosition = Util.SwingConstant_To_HA(ab.getHorizontalTextPosition());
                    } else if ("verticalTextPosition".equals(propName)) {
                        verticalTextPosition = Util.SwingConstant_To_VA(ab.getVerticalTextPosition());
                    }
                }
            });

            ab.addActionListener(ActionListener {
                public function actionPerformed(e: ActionEvent): Void {
                    if (action <> null) {
                        action();
                    }
                }
            });
        }
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns the {@code AbstractButton} delegate for this component.
     */
    public /* final */ function getAbstractButton(): javax.swing.AbstractButton {
        getJComponent() as javax.swing.AbstractButton;
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Creates the {@code JComponent} delegate for this component.
     */
    protected /* final */ function createJComponent(): JComponent {
        createAbstractButton();
    }

    abstract function createAbstractButton(): javax.swing.AbstractButton;

}
