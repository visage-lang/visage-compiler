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
 
package javafx.ui; 

import javax.swing.JSlider;
public class Slider extends Widget {

    override attribute focusable = false;

    // TODO MARK AS FINAL
    private attribute jslider:JSlider;

    public attribute min: Number = 0 on replace {
        if(jslider <> null) {
            jslider.setMinimum(min.intValue());
        }
    };
    public attribute max: Number = 100 on replace {
        if(jslider <> null) {
            jslider.setMaximum(max.intValue());
        }
    };
    public attribute value: Number = 50 on replace {
        if(jslider <> null) {
            jslider.setValue(value.intValue());
        }
    };
    public attribute extent: Number = -1 on replace {
        if(jslider <> null) {
            jslider.setExtent(extent.intValue());
        }
    };
    public attribute orientation: Orientation = Orientation.HORIZONTAL on replace {
        if(jslider <> null) {
             jslider.setOrientation(if (orientation == Orientation.VERTICAL) 
                               then JSlider.VERTICAL
                               else JSlider.HORIZONTAL);
        }
    };
    public attribute majorTickSpacing: Number = 0 on replace {
        if(jslider <> null) {
            jslider.setMajorTickSpacing(majorTickSpacing.intValue());
        }
    };
    public attribute minorTickSpacing: Number = 0 on replace {
        if(jslider <> null) {
            jslider.setMinorTickSpacing(minorTickSpacing.intValue());
        }
    };
    public attribute snapToTicks: Boolean = false on replace {
        if(jslider <> null) {
            jslider.setSnapToTicks(snapToTicks);
        }
    };
    public attribute paintTicks: Boolean = false on replace {
        if(jslider <> null) {
            jslider.setPaintTicks(paintTicks);
        }
    };
    public attribute paintTrack: Boolean = true on replace {
        if(jslider <> null) {
            jslider.setPaintTrack(paintTrack);
        }
    };
    public attribute paintLabels: Boolean = false on replace {
        if(jslider <> null) {
            jslider.setPaintLabels(paintLabels);
        }
    };
    public attribute labels: SliderLabel[] on replace oldValue[lo..hi]=newVals {
        if(jslider <> null) {
            var dict = new java.util.Hashtable();
            for (i in labels) {
                dict.put(i.value.intValue(), i.label.getComponent());
            }
            paintLabels = true;
            jslider.setLabelTable(dict);
        }
    };

    public attribute filled: Boolean = false on replace {
        if(jslider <> null) {
            jslider.putClientProperty("JSlider.isFilled", filled);
        }
    };

    public function createComponent():javax.swing.JComponent {
        try {
            jslider = new javax.swing.JSlider(
                if (orientation == Orientation.VERTICAL) 
                    then javax.swing.JSlider.VERTICAL 
                    else javax.swing.JSlider.HORIZONTAL,
                min.intValue(), max.intValue(), value.intValue());
        } catch (e) {
            throw new java.lang.Exception("{e} min = {min} max={max} value={value}");
        }
        jslider.setOpaque(false);
        jslider.setSnapToTicks(snapToTicks);
        jslider.setPaintTicks(paintTicks);
        jslider.setPaintLabels(paintLabels);
        jslider.setMajorTickSpacing(majorTickSpacing.intValue());
        jslider.setMinorTickSpacing(minorTickSpacing.intValue());
        jslider.setPaintTrack(paintTrack);
        if (extent <> -1) {
            jslider.setExtent(extent.intValue());
        }
        if (sizeof labels > 0) {
            var dict = new java.util.Hashtable();
            for (i in labels) {
                dict.put(i.value.intValue(), i.label.getComponent());
            }
            jslider.setLabelTable(dict);
        }
        if (filled) {
            jslider.putClientProperty("JSlider.isFilled", true);
        }
        jslider.addChangeListener(javax.swing.event.ChangeListener {
                                      public function stateChanged(e:javax.swing.event.ChangeEvent):Void {
                                          value = jslider.getValue();
                                      }
                                  });
        return jslider;
    }
}
