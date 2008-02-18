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


public class KeyModifier {
    public attribute id: Integer;
    
    public static attribute SHIFT = KeyModifier {
        id: java.awt.event.InputEvent.SHIFT_MASK
    };

    public static attribute CTRL = KeyModifier {
        id: java.awt.event.InputEvent.CTRL_MASK
    };

    public static attribute CONTROL = KeyModifier {
        id: java.awt.event.InputEvent.CTRL_MASK
    };

    public static attribute META = KeyModifier {
        id: java.awt.event.InputEvent.META_MASK
    };

    public static attribute ALT = KeyModifier {
        id: java.awt.event.InputEvent.ALT_MASK
    };

    public static attribute COMMAND = KeyModifier {
        id: if (java.lang.System.getProperty("os.name").toLowerCase().contains("mac")) 
            then java.awt.event.InputEvent.META_MASK 
            else java.awt.event.InputEvent.CTRL_MASK
            
        id: java.awt.event.InputEvent.CTRL_MASK
    };
    
    public static function isCTRL(modifiers:KeyModifier[]):Boolean {
        var matches = for(m in modifiers where m == CTRL) m;
        return sizeof matches > 0;
    }
    public static function isCONTROL(modifiers:KeyModifier[]):Boolean {
        var matches = for(m in modifiers where m == CONTROL) m;
        return sizeof matches > 0;
    }    
    
    public static function isSHIFT(modifiers:KeyModifier[]):Boolean {
        var matches = for(m in modifiers where m == SHIFT) m;
        return sizeof matches > 0;
    }  
    
    public static function isMETA(modifiers:KeyModifier[]):Boolean {
        var matches = for(m in modifiers where m == META) m;
        return sizeof matches > 0;
    } 
    
    public static function isALT(modifiers:KeyModifier[]):Boolean {
        var matches = for(m in modifiers where m == ALT) m;
        return sizeof matches > 0;
    } 
    
    public static function isCOMMAND(modifiers:KeyModifier[]):Boolean {
        var matches = for(m in modifiers where m == COMMAND) m;
        return sizeof matches > 0;
    }     
               
}
