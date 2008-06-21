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

import javafx.ui.AbstractMenuItem;
import javafx.ui.KeyStroke;
import javafx.ui.MenuItem;
import javafx.ui.MenuSeparator;

public class Menu extends AbstractMenuItem {
    override attribute opaque = true; 

    private attribute origrollover: Boolean;
    private attribute origbackground: java.awt.Color;
    // TODO MARK AS FINAL
    private attribute jmenu:javax.swing.JMenu;
    public attribute text:String on replace {
        if (jmenu != null) {
            jmenu.setText(text);
        }
    };
    public attribute mnemonic: KeyStroke on replace  {
        jmenu.setMnemonic(mnemonic.id.intValue());
    };
    public attribute items: AbstractMenuItem[]  on replace oldValue[lo..hi]=newVals {
        this.getComponent();
        for(k in [lo..hi]) { 
            jmenu.remove(lo);
        }
        var ndx = lo;
        for(menuitem in newVals) {
            jmenu.add(menuitem.getComponent(), ndx);
            ndx++
        }
    };
    public function onSetOpaque(value:Boolean):Void {
        if (not value) {
            origrollover = jmenu.isRolloverEnabled();
            origbackground = jmenu.getBackground();
            jmenu.setRolloverEnabled(false);
            jmenu.setBackground(new java.awt.Color(0,0,0,0));
        } else {
            jmenu.setRolloverEnabled(origrollover);
            jmenu.setBackground(origbackground);
        }
    }
    public function createComponent():javax.swing.JComponent {
            jmenu = new javax.swing.JMenu();
            jmenu.setOpaque(true);
            if (text != null) {
                jmenu.setText(text);
            }
            if (mnemonic != null) {
                jmenu.setMnemonic(mnemonic.id);
            }
            if ( not opaque) {
                jmenu.setRolloverEnabled(false);
                jmenu.setBackground(new java.awt.Color(0,0,0,0));
            }
            for(i in items) {
                jmenu.add(i.getComponent());
            }
            return jmenu;
    }    
}




