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

import java.lang.Class;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.view.OrbitView;
import gov.nasa.worldwind.view.FlyToOrbitViewStateIterator;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Globe;


/**
 * @author jclarke
 */

public class ClickAndGoSelectListener extends SelectListener  {
    
    public var wwd:WorldWindow;
    public var pickedObjClass:Class;    // Which picked object class do we handle
    public var elevationOffset:Number;  // Above the target position    
    
    public function selected(event: SelectEvent):Void {
        if (event.getEventAction().equals(SelectEvent.LEFT_CLICK))
        {
            // This is a left click
            if (event.hasObjects() and event.getTopPickedObject().hasPosition())
            {
                // There is a picked object with a position
                if (event.getTopObject().getClass().equals(pickedObjClass)
                        and this.wwd.getView() instanceof OrbitView)
                {
                    // This object class we handle and we have an orbit view
                    var targetPos:Position = event.getTopPickedObject().getPosition();
                    var view:OrbitView = this.wwd.getView() as OrbitView;
                    var globe:Globe = this.wwd.getModel().getGlobe();
                    if(globe != null and view != null)
                    {
                        // Use a PanToIterator to iterate view to target position
                        view.applyStateIterator(FlyToOrbitViewStateIterator.createPanToIterator(
                            view, globe, new LatLon(targetPos.getLatitude(), targetPos.getLongitude()),
                            Angle.ZERO, Angle.ZERO, targetPos.getElevation() + this.elevationOffset));
                    }
                }
            }
        }
    }

}
