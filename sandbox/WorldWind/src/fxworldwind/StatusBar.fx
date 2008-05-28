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
import javax.swing.*;
import java.lang.Math;
import java.lang.System;

import gov.nasa.worldwind.event.PositionListener;
import gov.nasa.worldwind.event.PositionEvent;
import gov.nasa.worldwind.event.RenderingListener;
import gov.nasa.worldwind.event.RenderingEvent;
import gov.nasa.worldwind.WorldWindow;

/**
 * @author jclarke
 */

public class StatusBar extends GridPanel, PositionListener, RenderingListener {
    override attribute rows = 1;
    override attribute columns = 5;
    public attribute eventSource:WorldWindow on replace(old) {
        if (old <> null)
        {
            old.removePositionListener(this);
            old.removeRenderingListener(this);
        }

        if (this.eventSource <> null)
        {
            this.eventSource.addPositionListener(this);
            this.eventSource.addRenderingListener(this);
        }        
    };
    public attribute showNetworkStatus:Boolean = true;
    
    private attribute latDisplay: SimpleLabel = SimpleLabel{horizontalAlignment: HorizontalAlignment.CENTER};
    private attribute lonDisplay: SimpleLabel = SimpleLabel{
            horizontalAlignment: HorizontalAlignment.CENTER
            text: "Off globe"
        };
    private attribute altDisplay: SimpleLabel = SimpleLabel{horizontalAlignment: HorizontalAlignment.CENTER};
    private attribute eleDisplay: SimpleLabel = SimpleLabel{horizontalAlignment: HorizontalAlignment.CENTER};
    private attribute heartBeat: SimpleLabel = SimpleLabel{
            horizontalAlignment: HorizontalAlignment.CENTER
            foreground: Color.rgba(255, 0, 0, 0)
        };
    
    override attribute cells = bind [
        altDisplay,
        latDisplay,
        lonDisplay,
        eleDisplay,
        heartBeat
    ];
    public function moved(event:PositionEvent):Void {
        var newPos = event.getPosition();
        if (newPos <> null)
        {
            var elev = 
                (eventSource.getModel().getGlobe().getElevation(newPos.getLatitude(), newPos.getLongitude())).intValue();
            latDisplay.text = "Lat {%7.4f newPos.getLatitude().getDegrees()}\u00B0";
            lonDisplay.text = "Lon {%7.4f newPos.getLongitude().getDegrees()}\u00B0";
            eleDisplay.text = "Elev {%,7d  elev} meters";
        }
        else
        {
            latDisplay.text = "";
            lonDisplay.text = "Off globe";
            eleDisplay.text = "";
        }  
    }
    
    public function stageChanged(event:RenderingEvent):Void {
        if (eventSource.getView() <> null and eventSource.getView().getEyePosition() <> null) {
            var elev:Number = eventSource.getView().getEyePosition().getElevation();
            var div:Number = 1e3.doubleValue();
            var alt = Math.round(elev / div);
            altDisplay.text = "Altitude {%,7d alt} km";
        } else {
            altDisplay.text = "Altitude";   
        }
    }
    
}    
