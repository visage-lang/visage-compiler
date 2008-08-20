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

package fxworldwind;

import java.awt.Dimension;
import javax.swing.*;
import java.awt.event.ActionListener;
import javafx.ui.*;
import java.lang.System;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.Layer;

/**
 * @author jclarke
 */

public class LayerPanel extends BorderPanel {
    override var preferredSize = new Dimension(200, 400);
    public var wwd:WWD on replace {
        this.fill();
    }
    var self = this;
    var layersPanel:GridPanel;
    var defaultFont:Font;
    var atMaxFont:Font;
    
    override var center = GridPanel {
        rows: 1
        vgap: 10
        border: CompoundBorder {
            borders: [
                EmptyBorder{top:9, left:9, right:9, bottom:9},
                TitledBorder{title:"Layers"}
            ]
        }  
        cells: ScrollPane {
            preferredSize: bind self.preferredSize
            border: EmptyBorder {top:9, left:9, right:9, bottom:9}
            view: BorderPanel {
                top: layersPanel = GridPanel {
                    columns:1
                    vgap:10
                    border: EmptyBorder{top:5, left:5, right:5, bottom:5}
                }
            }

        }        
    };
    
    function fill():Void
    {
        // Fill the layers panel with the titles of all layers in the world window's current model.
        var iterator = wwd.wwd.getModel().getLayers().iterator();
        while(iterator.hasNext())
        {
            var layer:Layer = iterator.next() as Layer;
            var action = LayerAction{layer:layer, wwd:wwd.wwd, selected:layer.isEnabled()};
            var jcb = CheckBox {};
            (jcb.getComponent() as JCheckBox).setAction(action);
            jcb.selected = action.selected;
            insert jcb into layersPanel.cells;

            if (defaultFont == null)
            {
                this.defaultFont = Font.fromAwtFont(jcb.getComponent().getFont());
                this.atMaxFont = this.defaultFont.italic();
            }

        }
    }    
    
    function updateStatus():Void
    {
        for( layerItem in this.layersPanel.cells)
        {
            if (layerItem instanceof CheckBox) {
                var action = (layerItem.getComponent() as JCheckBox).getAction() as LayerAction;
                if (action.layer.isMultiResolution()) {
                    if (action.layer.isAtMaxResolution()) {
                        layerItem.font = this.atMaxFont;
                    } else {
                        layerItem.font = this.defaultFont;
                    }
                }
            }
        }
    }  
    
    postinit {
        var statusTimer = new Timer(500, ActionListener 
        {
            public function actionPerformed(actionEvent):Void
            {
                updateStatus();
            }
        });
        statusTimer.start();
    }
    
}
