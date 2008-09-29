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

package javafx.animation;

import java.lang.Comparable;
import java.lang.Object;
import javafx.lang.Duration;

/**
 * Defines a key timing and values that are interpolated along the {@code Timeline}.
 *
 * <p>
 * The developer can control the timing and/or motion behavior
 * for a particular interval by providing target values and {@code Interpolator}
 * associated to each value. The values are interpolated along the particular
 * interval and reach the target value at specified timing. Also, {@code action}
 * function will be invoked at the particular timing.
 *
 * @profile common
 * @see Timeline
 * @see Interpolator
 */
public class KeyFrame extends Comparable {
    /**
     * Defines the reference elapsed time offset within a single cycle
     * of a Timeline at which the associated values will be set and at
     * which the {@code action()} function will be called.
     * 
     * @profile common
     */
    public var time: Duration on replace {
	if(owner != null) {
	    owner.invalidate();
	}
    };

    /**
     * The list of target variables and the desired values they should
     * interpolate at the specified time of this {@code KeyFrame}.
     * 
     * @profile common
     */
    public var values: KeyValue[];

    /**
     * A list of sub-timelines that will be started when the time cursor passes
     * the specified time of this {@code KeyFrame}.
     * <p>
     * Sub timelines inside another sub timeline is not supported, a 
     * <code>java.lang.UnsupportedOperationException</code> will be thrown
     * as the result.
     * 
     * @profile common
     */
    public var timelines: Timeline[];

    /**
     * A function that is called when the elapsed time on a cycle passes
     * the specified time of this {@code KeyFrame}.
     * The {@code action()} function will be called if the elapsed
     * time passes the indicated value, even if it never equaled the
     * time value exactly.
     * 
     * @profile common
     */
    public var action: function();

    /**
     * Defines whether or not the {@code action()} function
     * can be skipped if the master timer gets behind and
     * more than one {@link Timeline} cycles are skipped
     * between time pulses.
     * If {@code true}, only one call to the {@code action()}
     * function will occur for each time pulse, regardless of
     * how many cycles have occured since the last time pulse
     * was processed.
     * 
     * @profile common
     */
    public var canSkip: Boolean = false;

    /**
     * The timeline in which this key frame to be executed. It 
     * provides feedback to timeline if there is any var
     * change that timeline needs to be invalidated.
     */
    package var owner: Timeline;
    
    /**
     * A comparison function used to sort KeyFrames by their
     * specified reference time.
     * 
     * @param o the {@code KeyFrame} to compare to
     * @return  an Integer value<br>
                > 0 if specified {@code KeyFrame} timing is ahead of this <br>
                = 0 if they have the same timing<br>
                < 0 if specified {@code KeyFrame} timing is behind this<br>
     * @profile common
     */
    public override function compareTo(o:Object):Integer {
        var kf = o as KeyFrame;
        return time.compareTo(kf.time);
    }

    package function visit() {
        for (kv in values) {
            if (kv.target != null and kv.value != null) {
                kv.target.set(kv.value);
            }
        }
        if (action != null) {
            action();
        }
    }
}
