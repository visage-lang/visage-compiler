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

package javafx.ui.filter;

import com.sun.scenario.effect.Blend.Mode;

public class BlendMode {
    attribute toolkitValue: Mode = Mode.SRC_OVER;
    
    public static attribute SRC_OVER = BlendMode {
    }

    public static attribute SRC_IN = BlendMode {
        toolkitValue: Mode.SRC_IN
    }
    
    public static attribute SRC_OUT = BlendMode {
        toolkitValue: Mode.SRC_OUT
    }

    public static attribute SRC_ATOP = BlendMode {
        toolkitValue: Mode.SRC_ATOP
    }

    public static attribute ADD = BlendMode {
        toolkitValue: Mode.ADD
    }

    public static attribute MULTIPLY = BlendMode {
        toolkitValue: Mode.MULTIPLY
    }

    public static attribute SCREEN = BlendMode {
        toolkitValue: Mode.SCREEN
    }

    public static attribute OVERLAY = BlendMode {
        toolkitValue: Mode.OVERLAY
    }

    public static attribute DARKEN = BlendMode {
        toolkitValue: Mode.DARKEN
    }

    public static attribute LIGHTEN = BlendMode {
        toolkitValue: Mode.LIGHTEN
    }

    public static attribute COLOR_DODGE = BlendMode {
        toolkitValue: Mode.COLOR_DODGE
    }

    public static attribute COLOR_BURN = BlendMode {
        toolkitValue: Mode.COLOR_BURN
    }

    public static attribute HARD_LIGHT = BlendMode {
        toolkitValue: Mode.HARD_LIGHT
    }

    public static attribute SOFT_LIGHT = BlendMode {
        toolkitValue: Mode.SOFT_LIGHT
    }

    public static attribute DIFFERENCE = BlendMode {
        toolkitValue: Mode.DIFFERENCE
    }

    public static attribute EXCLUSION = BlendMode {
        toolkitValue: Mode.EXCLUSION
    }

    public static attribute RED = BlendMode {
        toolkitValue: Mode.RED
    }

    public static attribute GREEN = BlendMode {
        toolkitValue: Mode.GREEN
    }

    public static attribute BLUE = BlendMode {
        toolkitValue: Mode.BLUE
    }
}
