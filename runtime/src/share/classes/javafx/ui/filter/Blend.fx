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

public class Blend extends Filter {
    private attribute blend: com.sun.scenario.effect.Blend;
        
    init {
        update();
    }

    public bound function getImpl():com.sun.scenario.effect.Effect {
        blend
    };

    private function update():Void {
        // TODO: this check is necessary due to the fact that the replace
        // trigger is invoked when topInput is initialized; there's
        // probably a better way to deal with this...
        var bot = bottomInput;
        if (bot == null) {
            bot = Source {};
        }
        var top = topInput;
        if (top == null) {
            top = Source {};
        }
        blend = new com.sun.scenario.effect.Blend(mode.toolkitValue, bot.getImpl(), top.getImpl());
        blend.setOpacity(opacity.floatValue());
    }

    public attribute mode: BlendMode = BlendMode.SRC_OVER
        on replace { blend.setMode(mode.toolkitValue); }
    public attribute opacity: Number = 1
        on replace { blend.setOpacity(opacity.floatValue()); }
    // TODO: make one of these default to Source, the other undefined?
    public attribute topInput: Filter = Source { }
        on replace { update(); }
    public attribute bottomInput: Filter = Source { }
        on replace { update(); }
}
