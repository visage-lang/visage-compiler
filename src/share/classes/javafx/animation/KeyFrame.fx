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

public class KeyFrame extends Comparable {
    /**
     * Defines the reference elapsed time offset within a single cycle
     * of a Timeline at which the associated values will be set and at
     * which the {@code action()} function will be called.
     */
    public attribute time: Duration;

    /**
     * The list of target variables and the desired values they should
     * interpolate at the specified time of this {@code KeyFrame}.
     */
    public attribute values: KeyValue[];

    /**
     * A list of sub-timelines that will be started when the time cursor passes
     * the specified time of this {@code KeyFrame}.
     */
    public attribute timelines: Timeline[];

    /**
     * A function that is called when the elapsed time on a cycle passes
     * the specified time of this {@code KeyFrame}.
     * The {@code action()} function will be called if the elapsed
     * time passes the indicated value, even if it never equaled the
     * time value exactly.
     */
    public attribute action: function();

    /**
     * Defines whether or not the {@code action()} function
     * can be skipped if the master timer gets behind and
     * more than one {@link Timeline} cycles are skipped
     * between time pulses.
     * If {@code true}, only one call to the {@code action()}
     * function will occur for each time pulse, regardless of
     * how many cycles have occured since the last time pulse
     * was processed.
     */
    public attribute canSkip: Boolean = false;

    /**
     * A comparison function used to sort KeyFrames by their
     * specified reference time.
     */
    public function compareTo(o:Object):Integer {
        var kf = o as KeyFrame;
        return time.compareTo(kf.time);
    }

    function visit() {
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
