/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.animation;

import com.sun.javafx.runtime.Pointer;
import com.sun.scenario.animation.Clip;
import com.sun.scenario.animation.Interpolators;
import com.sun.scenario.animation.TimingTarget;
import com.sun.scenario.animation.TimingTargetAdapter;
import javafx.lang.Duration;
import java.lang.Object;
import java.lang.System;
import java.util.ArrayList;

public class Timeline {

    public static attribute INDEFINITE = -1;

    public attribute repeatCount: Number = 1.0;
    public attribute autoReverse: Boolean = false;
    public attribute direction:Direction = Direction.FORWARD;
    public attribute keyFrames: KeyFrame[] on replace {
        invalidate();
    };

    // if false, indicates that the internal (optimized) data structure
    // needs to be rebuilt
    private attribute valid = false;
    function invalidate() {
        valid = false;
    }

    // duration is inferred from time of last key frame and durations
    // of any sub-timelines in rebuildTargets()
    private attribute duration: Number = -1;

    function getTotalDur():Number {
        if (not valid) {
            rebuildTargets();
        }
        if (duration < 0 or repeatCount < 0) {
            return -1;
        }
        return duration * repeatCount;
    }

    public function start():Void {
        if (clip <> null) {
            clip.stop();
        }
        buildClip();
        clip.start();
    }

    public function stop():Void {
        clip.stop();
    }

    public function pause():Void {
        clip.pause();
    }

    public function resume():Void {
        clip.resume();
    }

    private function buildClip():Void {
        if (clip <> null and clip.isRunning()) {
            clip.cancel();
        }
        clip = Clip.create(Clip.INDEFINITE, adapter);
        clip.setInterpolator(Interpolators.getLinearInstance());
    }

    private attribute clip: Clip;
    private attribute targets: ArrayList = new ArrayList();
    private attribute actions: ArrayList = new ArrayList();
    private attribute subtimelines: ArrayList = new ArrayList();
    private attribute adapter: TimingTarget = createAdapter();

    //
    // Need to revalidate everything (call rebuildTargets() again) if
    // any of the following change after construction:
    //   - Timeline.keyFrames (insert, delete, or replace)
    //   - KeyFrame.time (any)
    //   - KeyValue.target (any)
    //
    // The following should be safe to change at any time:
    //   - Timeline.repeatCount
    //   - Timeline.autoReverse
    //   - Timeline.direction
    //   - KeyValue.value
    //   - KeyValue.interpolate
    //
    private function rebuildTargets() {
        targets.clear();
        actions.clear();
        subtimelines.clear();
        duration = 0;

        for (keyFrame in keyFrames) {
            if (duration >= 0) {
                duration = java.lang.Math.max(duration, keyFrame.time.millis);
            }

            if (keyFrame.timelines <> null) {
                for (timeline in keyFrame.timelines) {
                    var subDur = timeline.getTotalDur();
                    if (duration >= 0 and subDur >= 0) {
                        duration = java.lang.Math.max(duration, keyFrame.time.millis + subDur);
                    } else {
                        duration = -1;
                    }
                    var sub = SubTimeline {
                        startTime: keyFrame.time
                        timeline: timeline
                    }
                    subtimelines.add(sub);
                }
            }

            if (keyFrame.action <> null) {
                actions.add(Action { keyFrame: keyFrame });
            }

            for (keyValue in keyFrame.values) {
                // TODO: targets should really be Map<Pointer,List<KFPair>>
                var pairlist: KFPairList;
                for (i in [0..<targets.size()]) {
                    var pl = targets.get(i) as KFPairList;
                    if (pl.target == keyValue.target) {
                        // already have a KFPairList for this target
                        pairlist = pl;
                        break;
                    }
                }
                if (pairlist == null) {
                    pairlist = KFPairList { target: keyValue.target }
                    targets.add(pairlist);
                }
                var kfpair = KFPair {
                    frame: keyFrame
                    value: keyValue
                }
                pairlist.add(kfpair);
            }
        }

        valid = true;
    }

    function process(totalElapsed:Number) : Void {
        var needsStop = false;
        var totalDur = getTotalDur();
        if (totalDur >= 0 and totalElapsed >= totalDur) {
            // process one last pulse to ensure targets reach their end values
            totalElapsed = totalDur;
            needsStop = true;
        }

        var curT:Number;
        var cycle:Integer;
        var backward = false;
        if (duration < 0) {
            // indefinite duration (e.g. will occur when a sub-timeline
            // has indefinite repeatCount); always stay on zero cycle
            curT = totalElapsed;
            cycle = 0;
        } else {
            curT = totalElapsed % duration;
            cycle = totalElapsed / duration as Integer;
            if (curT == 0 and totalElapsed <> 0) {
                // we're at the end, or exactly on a cycle boundary;
                // treat this as the "1.0" case of the previous cycle
                // instead of the "0.0" case of the current cycle
                // TODO: there's probably a better way to deal with this...
                curT = duration;
                cycle -= 1;
            }
            if (autoReverse) {
                if (cycle % 2 == 1) {
                    curT = duration - curT;
                    backward = not backward;
                }
            }
            if (direction == Direction.REVERSE) {
                curT = duration - curT;
                backward = not backward;
            }
        }

        for (i in [0..<targets.size()]) {
            var pairlist = targets.get(i) as KFPairList;
            var kfpair1 = pairlist.get(0);
            var leftT = kfpair1.frame.time.millis;
            if (curT < leftT) {
                // haven't yet reached the first key frame
                // for this target; but still, we need to make sure
                // the target reaches its start/end value on each cycle
                if (cycle > pairlist.visitedCycle) {
                    pairlist.target.set(kfpair1.value.value);
                    pairlist.visitedCycle = cycle;
                }
                continue;
            }

            var v1:KeyValue;
            var v2:KeyValue;
            var segT = 0.0;

            for (j in [1..<pairlist.size()]) {
                // find keyframes on either side of the curT value
                var kfpair2 = pairlist.get(j);
                var rightT = kfpair2.frame.time.millis;
                if (curT <= rightT) {
                    v1 = kfpair1.value;
                    v2 = kfpair2.value;
                    segT = (curT - leftT) / (rightT - leftT);
                    break;
                }

                kfpair1 = kfpair2;
                leftT = kfpair1.frame.time.millis;
            }

            if (v1 <> null and v2 <> null) {
                pairlist.target.set(v2.interpolate.interpolate(v1.value, v2.value, segT));
            }
        }

        // look through all sub-timelines and recursively call process()
        // on any active SubTimeline objects
        for (i in [0..<subtimelines.size()]) {
            var sub = subtimelines.get(i) as SubTimeline;
            if (curT >= sub.startTime.millis) {
                var subDur = sub.timeline.getTotalDur();
                if (subDur < 0 or curT < sub.startTime.millis + subDur) {
                    sub.timeline.process(curT - sub.startTime.millis);
                }
            }
        }

        // look through all KeyFrames containing non-null action() function
        // and invoke action() if necessary
        for (i in [0..<actions.size()]) {
            var action = actions.get(i) as Action;
            action.visit(curT, cycle, backward);
        }

        if (needsStop and clip <> null) {
            clip.stop();
        }
    }

    private function createAdapter():TimingTarget {
        TimingTargetAdapter {
            public function begin() : Void {
                // clear the visited flag on all Actions and KFPairLists
                for (i in [0..<actions.size()]) {
                    var action = actions.get(i) as Action;
                    action.reset();
                }
                for (i in [0..<targets.size()]) {
                    var target = targets.get(i) as KFPairList;
                    target.reset();
                }
            }
            
            public function timingEvent(fraction, totalElapsed) : Void {
                process(totalElapsed as Number);
            }
        }
    }
}

class KFPair {
    attribute frame:KeyFrame;
    attribute value:KeyValue;
}

class KFPairList {
    attribute target:Pointer;
    private attribute pairs:ArrayList = new ArrayList();

    attribute visitedCycle:Integer = 0;
    function reset() {
        visitedCycle = 0;
    }

    function size(): Integer {
        return pairs.size();
    }

    function add(pair:KFPair): Void {
        // keep list sorted chronologically
        for (i in [0..<pairs.size()]) {
            var listval = get(i);
            if (pair.frame.time < listval.frame.time) {
                pairs.add(i, pair);
                return;
            }
        }
        pairs.add(pair);
    }

    function get(i:Integer): KFPair {
        return pairs.get(i) as KFPair;
    }
}

class SubTimeline {
    attribute startTime:Duration;
    attribute timeline:Timeline;
}

class Action {
    attribute keyFrame:KeyFrame;

    private attribute visitedCycle:Integer = -1;
    function reset() {
        visitedCycle = -1;
    }

    function visit(curT:Number, cycle:Integer, backward:Boolean) {
        if (cycle > visitedCycle) {
            if (cycle > visitedCycle + 1) {
                // missed the last cycle entirely, so make up for it now
                visitedCycle = cycle;
                keyFrame.action();
            } else {
                if (backward) {
                    if (curT <= keyFrame.time.millis) {
                        visitedCycle = cycle;
                        keyFrame.action();
                    }
                } else {
                    if (curT >= keyFrame.time.millis) {
                        visitedCycle = cycle;
                        keyFrame.action();
                    }
                }
            }
        }
    }
}
