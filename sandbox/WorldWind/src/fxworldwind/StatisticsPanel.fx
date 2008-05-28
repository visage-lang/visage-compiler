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

import javafx.ui.*;
import java.awt.Dimension;
import javax.swing.*;
import java.util.*;
import java.lang.System;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.util.PerformanceStatistic;
import gov.nasa.worldwind.event.RenderingListener;
import gov.nasa.worldwind.event.RenderingEvent;
/**
 * @author jclarke
 */

public class StatisticsPanel extends BorderPanel {
    override attribute preferredSize = new Dimension(200, 400);
    public attribute wwd:WWD on replace {
        wwd.wwd.setPerFrameStatisticsKeys(PerformanceStatistic.ALL_STATISTICS_SET);
    }
    private  attribute statsPanel:GridPanel;
    private attribute self = this;
    
    override attribute center = GridPanel {
        rows:1
        vgap:10
        border: CompoundBorder {
            borders: [
                EmptyBorder{top:9, left:9, right:9, bottom:9},
                TitledBorder{title:"Statistics"}
            ]
        }
        cells: ScrollPane {
            //preferredSize: bind self.preferredSize
            border: EmptyBorder {top:9, left:9, right:9, bottom:9}
            view: BorderPanel {
                center: statsPanel = GridPanel {
                    columns:1
                    vgap:5
                    border: EmptyBorder{top:5, left:5, right:5, bottom:5}
                }
            }

        }
    };
    
    public function update():Void {
        delete statsPanel.cells;
        // Fill the layers panel with the titles of all layers in the world window's current model.
        if (wwd.wwd.getSceneController().getPerFrameStatistics().size() > 0) {
            var pfs = new ArrayList(wwd.wwd.getSceneController().getPerFrameStatistics());
            Collections.sort(pfs);
            var iterator = pfs.iterator();
            while(iterator.hasNext()) {
                var stat = iterator.next().toString();
                insert SimpleLabel{text: stat} into statsPanel.cells;
            }
        }
        
    }
    
    init {
        wwd.wwd.addRenderingListener(RenderingListener {
            public function stageChanged(event:RenderingEvent):Void {
                if (event.getSource() instanceof WorldWindow) {
                    update();
                }
            }
        });
    }

}
