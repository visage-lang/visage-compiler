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

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

// PENDING_DOC_REVIEW
/**
 * Abstract base class for {@code Panels}.
 */
public abstract class AbstractPanel extends Component, Container {

   // PENDING_DOC_REVIEW
   /**
    * Defines a {@code Paint} color pattern to be applied to the background.  
    */
    public attribute background: Paint = Color.fromAWTColor(getJPanel().getBackground()) on replace {
        doAndIgnoreJComponentChange(function() {
            getJPanelImpl().setBackgroundPaint(background.getAWTPaint());
        });
    }

    private /* final */ function getJPanelImpl(): JPanelImpl {
        getJPanel() as JPanelImpl;
    }

   // PENDING_DOC_REVIEW
   /**
    * Returns the {@link JPanel} delegate for this component
    * 
    * @see javax.swing.JPanel 
    */
    public /* final */ function getJPanel(): JPanel {
        getJComponent() as JPanel;
    }

   // PENDING_DOC_REVIEW
   /**
    * Creates the specific {@link JComponent} delegate for this component.
    * @see javax.swing.JComponent 
    */
    protected /* final */ function createJComponent(): JComponent {
        var jPanel = new JPanelImpl();
        configureJPanel(jPanel);
        return jPanel;
    }

    protected function configureJPanel(jPanel: JPanel): Void {}

    postinit {
        var jPanel = getJPanel();

        jPanel.addPropertyChangeListener(BackgroundSupport.BACKGROUND_PAINT_PROPERTY,
                                         PropertyChangeListener {
            public function propertyChange(e: PropertyChangeEvent): Void {
                if (ignoreJComponentChange) {
                    return;
                }

                var bg = getJPanelImpl().getBackgroundPaint();
                if (bg == null or bg instanceof java.awt.Color) {
                    background = Color.fromAWTColor(bg as java.awt.Color);
                }
            }
        });
    }

}
