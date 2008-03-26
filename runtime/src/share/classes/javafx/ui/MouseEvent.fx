/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

public class MouseEvent {
    public attribute modifiers: KeyModifier[];
    public attribute clickCount: Number;
    public attribute button: Number;
    public attribute x: Number;
    public attribute y: Number;
    
    private function containsModifier(mod:KeyModifier):Boolean {
        //TODO this used to be mod in Modifiers, do this until and if
        // an alternative is implemented
        var rc = false;
        for ( m in modifiers) {
            if(m == mod) {
                rc = true;
                break;
            }
        };
        return rc;
    }
    
    public function isControlDown():Boolean {
        return this.containsModifier(KeyModifier.CTRL);
    }
    public function isAltDown():Boolean {
        return this.containsModifier(KeyModifier.ALT);
    }
    public function isShiftDown():Boolean {
        return this.containsModifier(KeyModifier.SHIFT);
    }
    public function isMetaDown():Boolean {
        return this.containsModifier(KeyModifier.META);
    }
    public function isPopupTrigger():Boolean{
        this.source.isPopupTrigger();
    }

    public attribute source: java.awt.event.MouseEvent;
}

