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

package javafx.ui.canvas;
import java.awt.Dimension;

public class CanvasViewport {
    //TODO is readonly r/o by others and r/w by this, or is r/o only
    // allowed to be set during initialization?
    //public readonly attribute currentHeight: Number;
    public attribute currentHeight: Number;
    //TODO is readonly r/o by others and r/w by this, or is r/o only
    //public readonly attribute currentWidth: Number;
    public attribute currentWidth: Number;

    //TODO is readonly r/o by others and r/w by this, or is r/o only
    //public readonly attribute currentSize: Dimension = new Dimension(0, 0)
    public attribute currentSize: Dimension  = {new Dimension(0, 0)}
        on replace {
            currentWidth = currentSize.width;
            currentHeight = currentSize.height;
    };
    public function setSize(w:Number, h:Number){
        currentSize = new Dimension(w.intValue(), h.intValue());
    }
}

