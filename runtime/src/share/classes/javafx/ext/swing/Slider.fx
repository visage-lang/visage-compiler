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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javafx.scene.Orientation;
import com.sun.javafx.scene.Util;

// PENDING_DOC_REVIEW
/**
 * A component that lets the user graphically select a value by sliding
 * a knob within a bounded interval.
 */
public class Slider extends Component {

    // PENDING_DOC_REVIEW
    /**
     * Represents the slider's {@code minimum} value.
     * <p>
     * The other two properties may be changed as well, to ensure that:
     * <pre>
     * minimum &lt;= value &lt;= maximum
     * </pre>
     */
    public attribute minimum: Integer = getJSlider().getMinimum() on replace {
        doAndIgnoreJComponentChange(function() {
            getJSlider().setMinimum(minimum);
        });
    }

    // PENDING_DOC_REVIEW
    /**
     * Represents the slider's {@code maximum} value.
     * <p>
     * The other two properties may be changed as well, to ensure that:
     * <pre>
     * minimum &lt;= value &lt;= maximum
     * </pre>
     */
    public attribute maximum: Integer = getJSlider().getMaximum() on replace {
        doAndIgnoreJComponentChange(function() {
            getJSlider().setMaximum(maximum);
        });
    }

    // PENDING_DOC_REVIEW
    /**
     * Represents the slider's current value.
     * <p>
     * The other two properties may be changed as well, to ensure that:
     * <pre>
     * minimum &lt;= value &lt;= maximum
     * </pre>
     */
    public attribute value: Integer = getJSlider().getValue() on replace {
        doAndIgnoreJComponentChange(function() {
            getJSlider().setValue(value);
        });
    }

    // PENDING_DOC_REVIEW
    /**
     * Represents the slider's orientation.
     */
    public attribute orientation: Orientation =
        Util.SwingConstant_To_Orientation(getJSlider().getOrientation())
        on replace {
            doAndIgnoreJComponentChange(function() {
                getJSlider().setOrientation(Util.Orientation_To_SwingConstant(orientation));
            });
        }

    postinit {
        var jSlider = getJSlider();

        jSlider.addChangeListener(ChangeListener {
            public function stateChanged(e:ChangeEvent):Void {
                if (ignoreJComponentChange) {
                    return;
                }

                if (value <> jSlider.getValue()) {
                    value = jSlider.getValue();
                }
            }
        });

        jSlider.addPropertyChangeListener(PropertyChangeListener {
            public function propertyChange(e:PropertyChangeEvent): Void {
                if (ignoreJComponentChange) {
                    return;
                }

                var propName = e.getPropertyName();
                if ("minimum".equals(propName)) {
                    minimum = jSlider.getMinimum();
                } else if ("maximum".equals(propName)) {
                    maximum = jSlider.getMaximum();
                } else if ("orientation".equals(propName)) {
                    orientation = Util.SwingConstant_To_Orientation(jSlider.getOrientation());
                }
            }
        });
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns the {@code JSlider} delegate for this component.
     */
    public /* final */ function getJSlider(): JSlider {
        getJComponent() as JSlider;
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates the {@code JComponent} delegate for this component.
     */
    protected /* final */ function createJComponent(): JComponent {
        new JSlider();
    }

}
