/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;


abstract class RotatableWidget {
    public abstract function setRotation(n:Number);
    
    attribute rotation: Number
        on replace { setRotation(rotation); };


}
