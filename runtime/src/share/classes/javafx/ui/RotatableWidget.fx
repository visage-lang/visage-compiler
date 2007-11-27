/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javafx.ui;

public abstract class  RotatableWidget extends Widget {
    public attribute rotation: Number on replace {
        setRotation(rotation);
    };
    public abstract function setRotation(n:Number):Void;
}

