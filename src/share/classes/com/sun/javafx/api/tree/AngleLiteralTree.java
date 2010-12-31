/*
 * Copyright 2010 Visage Project.  All Rights Reserved.
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
 */

package com.sun.javafx.api.tree;

/**
 * A tree node for a JavaFX Script angle literal.
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public interface AngleLiteralTree extends Tree {

    public enum Units {
        DEGREES("deg"),
        RADIANS("rad"),
        TURNS("turn");
        
        Units(String suffix) {
            this.suffix = suffix;
        }
        
        public String getSuffix() {
            return suffix;
        }
        
        private String suffix;
    }
    
    /**
     * @return the numeric value of this tree.
     */
    public LiteralTree getValue();
    
    /**
     * @return the units specified to declare this angle literal.
     */
    Units getUnits();
}
