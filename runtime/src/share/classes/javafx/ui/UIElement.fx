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

import java.awt.RenderingHints;
import java.lang.Object;
import java.lang.Throwable;
import java.lang.System;
import java.lang.Math;
import java.lang.Thread;
import java.awt.Toolkit;
import java.util.HashMap;
import com.sun.javafx.api.ui.UIContext;
import com.sun.javafx.api.ui.UIContextImpl;

public static function __EASEBOTH(t:Number):Number {
    var timer = TimerImpl{startTime:0, duration:1000, to:1000.0};

    timer.setEaseBoth();
    var timeDiff = t * 1000;
    return timer.calcNextValue(timeDiff.intValue(), 0).doubleValue()/1000.0;
}
public static function __EASEIN(t:Number):Number {
    var timer = TimerImpl{startTime:0, duration:1000, to:1000};
    timer.setEaseIn();
    var timeDiff = t * 1000;
    return timer.calcNextValue(timeDiff.intValue(), 0).doubleValue()/1000.0;
}
public static function __EASEOUT(t:Number):Number {
    var timer = TimerImpl{startTime:0, duration:1000, to:1000};
    timer.setEaseOut();
    var timeDiff = t * 1000;
    return timer.calcNextValue(timeDiff.intValue(), 0).doubleValue()/1000.0;
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
    //TODO JXFC-195
    //return __EASE(a, t, __EASEBOTH, __INTERPOLATE_NUM);
    //TODO JXFC-195 WORKAROUND
    t = __EASEBOTH(t);
    var off = t * (sizeof a-1);
    var i = off.intValue();
    var value1 = a[i];
    if (i + 1 == sizeof a) then {
        return value1;
    };
    var value2 = a[i+1];
    return __INTERPOLATE_NUM(value1, value2, off-i);
};

public static function EASEIN(a:Number[], t:Number):Number {
    //TODO JXFC-195
    //return __EASE(a, t, __EASEIN, __INTERPOLATE_NUM);
    //TODO JXFC-195 WORKAROUND
    t = __EASEIN(t);
    var off = t * (sizeof a-1);
    var i = off.intValue();
    var value1 = a[i];
    if (i + 1 == sizeof a) then {
        return value1;
    };
    var value2 = a[i+1];
    return __INTERPOLATE_NUM(value1, value2, off-i);
};

public static function EASEOUT(a:Number[], t:Number):Number {
    //TODO JXFC-195
    //return __EASE(a, t, __EASEOUT, __INTERPOLATE_NUM);
    //TODO JXFC-195 WORKAROUND
    t = __EASEOUT(t);
    var off = t * (sizeof a-1);
    var i = off.intValue();
    var value1 = a[i];
    if (i + 1 == sizeof a) then {
        return value1;
    };
    var value2 = a[i+1];
    return __INTERPOLATE_NUM(value1, value2, off-i);
};

public static function LINEAR(a:Number[], t:Number):Number {
    //TODO JXFC-195
    //return __EASE(a, t, function(t:Number) {return t;}, __INTERPOLATE_NUM);
    //TODO JXFC-195 WORKAROUND
    var off = t * (sizeof a-1);
    var i = off.intValue();
    var value1 = a[i];
    if (i + 1 == sizeof a) then {
        return value1;
    };
    var value2 = a[i+1];
    return __INTERPOLATE_NUM(value1, value2, off-i);
};

public static function DISCRETE(a:Number[], t:Number):Number {
    return a[t.intValue()*(sizeof a -1)];
};

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
    }
} catch (e:Throwable) {
    // ignore option setting errors
}

public function getScreenResolution():Integer {
    Toolkit.getDefaultToolkit().getScreenResolution();
}

public function cmToPixel(cm:Number):Number {
     Math.round(cm * getScreenResolution() * 100 / 254);
}
public function mmToPixel(mm:Number):Number {
     Math.round(mm * getScreenResolution() * 10 / 254);
}
public function inchToPixel(inch:Number):Number{ 
     Math.round(inch * getScreenResolution());
}

public function pointToPixel(pt:Integer):Number{
     Math.round(pt * getScreenResolution() / 72);
}

public class UIElement {
    // each applet has separate ThreadGroup, so this map provides separate UIContext for each applet
    private static attribute threadGroupContexts: HashMap 
        = new HashMap/*<ThreadGroup,Map<String,UIContext>>*/;

    // Not abstract this is the default implementation
    public bound function getWindow():java.awt.Window {return null; }
    public attribute lookAndFeel: String = null
        on replace {
	if (lookAndFeel <> null) {
            javax.swing.UIManager.setLookAndFeel(lookAndFeel);
            //javax.swing.SwingUtilities.updateComponentTreeUI(getWindow());
            var win = getWindow();
            if(win <> null){
                javax.swing.SwingUtilities.updateComponentTreeUI(win);
            }
	}
    };
  
    public static attribute context:UIContext = getUIContext();

    public static function getUIContext(): UIContext {
        var tgroup = Thread.currentThread().getThreadGroup();
        var appContext = threadGroupContexts.get(tgroup) as HashMap;
        if (appContext == null) {
            appContext = new HashMap;
            threadGroupContexts.put(tgroup, appContext);
        }
	var context = appContext.get("javafx.UIContext") as UIContext;
	if (context == null) {
	    context = new UIContextImpl();
	    appContext.put("javafx.UIContext", context);
	}
	return context;
    }
}




