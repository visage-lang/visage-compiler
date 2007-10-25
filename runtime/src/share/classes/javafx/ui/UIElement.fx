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


import java.lang.Object;
import java.lang.Throwable;
import java.lang.System;
// Default Animation Functions
// TODO Animation changes 

public static function __EASEBOTH(t:Number):Number {
    //TODO Animation timer
    /******
    var timer = TimerImpl{startTime:0, duration:1000, to:1000};
    timer.setEaseBoth();
    var timeDiff = t * 1000;
    return timer.calcNextValue(timeDiff, 0)/1000;
    *******/
    return 0.1; //TODO WORKAROUND
}
public static function __EASEIN(t:Number):Number {
    //TODO Animation timer
    /******
    var timer = TimerImpl{startTime:0, duration:1000, to:1000};
    timer.setEaseIn();
    var timeDiff = t * 1000;
    return timer.calcNextValue(timeDiff, 0)/1000;
    *******/
    return 0.1; //TODO WORKAROUND
}
public static function __EASEOUT(t:Number):Number {
    //TODO Animation timer
    /******
    var timer = TimerImpl{startTime:0, duration:1000, to:1000};
    timer.setEaseOut();
    var timeDiff = t * 1000;
    return timer.calcNextValue(timeDiff, 0)/1000;
    *******/
    return 0.1; //TODO WORKAROUND
}

public static function __EASE(a:Object[], t:Number, f: function(:Number):Number, 
    interpolate: function(:Object, :Object, :Number):Object) : Object {
    
    t = f(t);
    var off = t * (sizeof a-1);
    var i = off.intValue();
    var value1 = a[i];
    if (i + 1 == sizeof a) then {
        return value1;
    };
    var value2 = a[i+1];
    return interpolate(value1, value2, off-i);
}

public static function __INTERPOLATE_NUM(value1:Number, value2: Number, t:Number):Number {
    return value1 + (value2-value1)*t;
}

public static function EASEBOTH(a:Number[], t:Number):Number {
    //TODO Animation timer
    //return __EASE(a, t, __EASEBOTH, __INTERPOLATE_NUM);
    return 0.1; //TODO WORKAROUND
};

public static function EASEIN(a:Number[], t:Number):Number {
    //TODO Animation timer
    //return __EASE(a, t, __EASEIN, __INTERPOLATE_NUM);
    return 0.1; //TODO WORKAROUND
};

public static function EASEOUT(a:Number[], t:Number):Number {
    //TODO Animation timer
    //return __EASE(a, t, __EASEOUT, __INTERPOLATE_NUM);
    return 0.1; //TODO WORKAROUND
};

public static function LINEAR(a:Number[], t:Number):Number {
    //TODO Animation timer
    //return __EASE(a, t, function(t:Number) {return t;}, __INTERPOLATE_NUM);
    return 0.1; //TODO WORKAROUND
};

public static function DISCRETE(a:Number[], t:Number):Number {
    return a[t.intValue()*(sizeof a -1)];
};



//TODO UIContext
/**************************



// net.java.javafx.ui.UIContext is a helper class providing shared cell renderers,
// image caching, and a few other hacks to work around current Swing
// limitations. The same package also contains a few non-Swing components:
// Splitter layout, Stack layout, InfiniteProgressPanel, ShadowedBorder

try {
    //javax.swing.JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    javax.swing.UIManager.put("swing.boldMetal", false);
    var osName = System.getProperty("os.name").toLowerCase();
    if (osName.contains("win") or osName.contains("mac")) then {
       javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
    }else {;};
} catch (e:Throwable) {
}


*****************/



public class UIElement {
    // Not abstract this is the default implementation
    public function getWindow():java.awt.Window {return null; }
    public attribute lookAndFeel: String
        on replace {
            javax.swing.UIManager.setLookAndFeel(lookAndFeel);
            javax.swing.SwingUtilities.updateComponentTreeUI(getWindow());
    };
    //TODO UIContext
    /****
    public static attribute context:net.java.javafx.ui.UIContext = 
        new net.java.javafx.ui.UIContextImpl();
    public static function cmToPixel(n:Number):Number{ return context.centimeterToPixel(n);}
    public static function mmToPixel(n:Number):Number{ return context.millimeterToPixel(n);}
    public static function inchToPixel(n:Number):Number{ return context.inchToPixel(n);}
    public static function pointToPixel(n:Integer):Number{ return context.pointToPixel(n);}
    init {
        context.getModule().disableUndo();
        context.getModule().disableExtent();
        //context.getModule().enableExtent();
        //context.debugFocus();        
        }
     *********/
}




