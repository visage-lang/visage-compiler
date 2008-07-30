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

import com.sun.scenario.animation.Clip;
import com.sun.scenario.animation.Interpolators;
import com.sun.scenario.animation.TimingTarget;
import com.sun.scenario.animation.TimingTargetAdapter;
import javafx.lang.Duration;
import javafx.lang.Sequences;
import java.lang.Object;
import java.lang.System;
import java.util.ArrayList;
import java.lang.System;

/**
 * Used to specify an animation that repeats indefinitely (until
 * the {@code stop()} method is called).
 *
 * @profile common
 */
public def INDEFINITE = -1;

/**
 * Represents an animation, defined by one or more {@code KeyFrame}s.
 *
 * @profile common
 */
public class Timeline {

    /**
     * Defines the number of cycles in this animation.
     * The {@code repeatCount} may be {@code INDEFINITE}
     * for animations that repeat indefinitely, but must otherwise be >= 0.
     * The default value is 1.
     *
     * @profile common
     */
    public attribute repeatCount: Number = 1.0;

    /**
     * Defines whether this animation reverses direction on alternating
     * cycles.
     * If {@code true}, the animation will proceed forward on
     * the first cycle, then reverses on the second cycle, and so on.
     * The default value is {@code false}, indicating that the
     * animation will loop such that each cycle proceeds
     * forward from the initial {@code KeyFrame}.
     *
     * @profile common
     */
    public attribute autoReverse: Boolean = false;

    /**
     * Defines whether this animation reverses direction in place
     * each time {@code start()} is called.  
     * If {@code true}, the animation will initially proceed forward,
     * then restarts in place except heading in opposite direction.
     * The default value is {@code false}, indicating that the
     * animation will restart from the initial {@code KeyFrame}
     * each time {@code start()} is called.
     *
     * @profile common
     */
    public attribute toggle: Boolean = false on replace {
        isReverse = true;
    };

    /**
     * Defines the sequence of {@code KeyFrame}s in this animation.
     * If a {@code KeyFrame} is not provided for the {@code time==0s}
     * instant, one will be synthesized using the target values
     * that are current at the time {@code start()} is called.
     *
     * @profile common
     */
    public attribute keyFrames: KeyFrame[] on replace {
        invalidate();
    };

    /**
     * Read-only attribute that indicates whether the animation is
     * currently running.
     * <p>
     * This value is initially {@code false}.
     * It will become {@code true} after {@code start()} has been called,
     * and then becomes {@code false} again after the animation ends
     * naturally, or after an explicit call to {@code stop()}.
     * <p>
     * Note that {@code running} will remain {@code true} even when
     * {@code paused==true}.
     *
     * @profile common
     */
    readable var running: Boolean = false;

    /**
     * Read-only attribute that indicates whether the animation is
     * currently paused.  
     * <p>
     * This value is initially {@code false}.
     * It will become {@code true} after {@code pause()} has been called
     * on a running animation, and then becomes {@code false} again after
     * an explicit call to {@code resume()} or {@code stop()}.
     * <p>
     * Note that {@code running} will remain {@code true} even when
     * {@code paused==true}.
     *
     * @profile common
     */
    readable var paused: Boolean = false;

    // if false, indicates that the internal (optimized) data structure
    // needs to be rebuilt
    private var valid = false;
    function invalidate() {
        valid = false;
    }

    // duration is inferred from time of last key frame and durations
    // of any sub-timelines in rebuildTargets()
    private var duration: Number = -1;

    function getTotalDur():Number {
        if (not valid) {
            rebuildTargets();
        }
        if (duration < 0 or repeatCount < 0) {
            return -1;
        }
        return duration * repeatCount;
    }

    /**
     * Starts (or restarts) the animation.
     * <p>
     * If {@code toggle==false} and the animation is currently running,
     * the animation will be restarted from its initial position.
     * <p>
     * If {@code toggle==true} and the animation is currently running,
     * the animation will immediately change direction in place and
     * continue on in that new direction.  When the animation finishes
     * in one direction, calling {@code start()} again will restart the
     * animation in the opposite direction.
     *
     * @profile common
     */
    public function start() {
        if (toggle) {
            // change direction in place
            if (clip == null) {
                buildClip();
            }
            isReverse = not isReverse;
            offsetValid = false;
            frameIndex = keyFrames.size() - frameIndex;
            if (not clip.isRunning()) {
            clip.start();
            }
        } else {
            // stop current clip and restart from beginning
            buildClip();
            clip.start();
        }
    }

    /**
     * Stops the animation.  If the animation is not currently running,
     * this method has no effect.
     *
     * @profile common
     */
    public function stop() {
        clip.stop();
    }

    /**
     * Pauses the animation.  If the animation is not currently running,
     * this method has no effect.
     */
    public function pause() {
        clip.pause();
    }

    /**
     * Resumes the animation from a paused state.  If the animation is
     * not currently running or not currently paused, this method has
     * no effect.
     *
     * @profile common
     */
    public function resume() {
        clip.resume();
    }

    private function buildClip() {
        if (clip != null and clip.isRunning()) {
            clip.stop();
        }
        clip = Clip.create(Clip.INDEFINITE, adapter);
        clip.setInterpolator(Interpolators.getLinearInstance());
    }

    private attribute clip: Clip;
    private attribute sortedFrames: KeyFrame[];
    private attribute targets: ArrayList = new ArrayList();
    private attribute subtimelines: ArrayList = new ArrayList();
    private attribute adapter: TimingTarget = createAdapter();

    private attribute cycleIndex: Integer = 0;
    private attribute frameIndex: Integer = 0;

    private attribute isReverse: Boolean = true;
    private attribute offsetT: Number = 0;
    private attribute lastElapsed: Number = 0;
    private attribute offsetValid: Boolean = false;

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
    //   - Timeline.toggle
    //   - KeyValue.value
    //   - KeyValue.interpolate
    //
    private function rebuildTargets():Void {
        targets.clear();
        subtimelines.clear();
        duration = 0;
        if (sizeof keyFrames == 0) {
            return;
        }

        sortedFrames = Sequences.sort(keyFrames) as KeyFrame[];

        var zeroFrame:KeyFrame;
        if (sortedFrames[0].time == 0s) {
            zeroFrame = sortedFrames[0];
        } else {
            zeroFrame = KeyFrame { time: 0s };
        }

        for (keyFrame in keyFrames) {
            if (duration >= 0) {
                duration = java.lang.Math.max(duration, keyFrame.time.toMillis());
            }

            if (keyFrame.timelines != null) {
                for (timeline in keyFrame.timelines) {
                    var subDur = timeline.getTotalDur();
                    if (duration >= 0 and subDur >= 0) {
                        duration = java.lang.Math.max(duration, keyFrame.time.toMillis() + subDur);
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

            for (keyValue in keyFrame.values) {
                // TODO: targets should really be Map<KeyValueTarget,List<KFPair>>
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
                    pairlist = KFPairList { 
                        target: keyValue.target 
                    }
                    if (keyFrame.time != 0s) {
                        // get current value and attach it to zero frame
                        var kv = KeyValue {
                            target: keyValue.target;
                            value: keyValue.target.get();
                        }
                        var kfp = KFPair {
                            value: kv
                            frame: zeroFrame
                        }
                        pairlist.add(kfp);
                    }
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

    function process(totalElapsed:Number):Void {
        // 1. calculate totalDur
        // 2. modify totalElapsed depending on direction
        // 3. clamp totalElapsed and set needsStop if necessary
        // 4. calculate curT and cycle based on totalElapsed
        // 5. decide whether to increment or decrement cycle/frame index, depending on direction
        // 6. visit key frames
        // 7. do interpolation between active key frames
        // 8. visit subtimelines
        // 9. stop clip if needsStop

        var needsStop = false;
        var totalDur = getTotalDur();

        if (totalDur >= 0) {
            if (toggle) {
                if (not offsetValid) {
                    if (isReverse) {
                        offsetT = totalElapsed + lastElapsed;
                    } else {
                        offsetT = totalElapsed - lastElapsed;
                    }
                    offsetValid = true;
                }

                // adjust totalElapsed to account for direction (the
                // incoming totalElapsed value will continue to increase
                // monotonically regardless of how many times the direction
                // has been reversed, so here we just massage it back into
                // the range [0,totalDur] so that other calculations below
                // will work as usual)
                if (isReverse) {
                    totalElapsed = offsetT - totalElapsed;
                } else {
                    totalElapsed = totalElapsed - offsetT;
                }
            }

            // process one last pulse to ensure targets reach their end values
            if (toggle and isReverse) {
                if (totalElapsed <= 0) {
                    totalElapsed = 0;
                    needsStop = true;
                }
            } else {
                if (totalElapsed >= totalDur) {
                    totalElapsed = totalDur;
                    needsStop = true;
                }
            }

            // capture last adjusted totalElapsed value (used in toggle case)
            lastElapsed = totalElapsed;
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
            curT = totalElapsed mod duration;
            cycle = totalElapsed / duration as Integer;
            if (curT == 0 and totalElapsed != 0) {
                // we're at the end, or exactly on a cycle boundary;
                // treat this as the "1.0" case of the previous cycle
                // instead of the "0.0" case of the current cycle
                // TODO: there's probably a better way to deal with this...
                curT = duration;
                cycle -= 1;
            }
            if (autoReverse) {
                if (cycle mod 2 == 1) {
                    curT = duration - curT;
                    backward = true;
                }
            }
        }

        // look through each KeyFrame and see if we need to visit its
        // key values and its action function
        if (toggle and isReverse) {
            backward = not backward;
            while (cycleIndex > cycle) {
                // we're on a new cycle; visit any key frames that we may
                // have missed along the way
                visitCycle(cycleIndex, cycleIndex > cycle+1);
                cycleIndex--;
            }
        } else {
            while (cycleIndex < cycle) {
                // we're on a new cycle; visit any key frames that we may
                // have missed along the way
                visitCycle(cycleIndex, cycleIndex < cycle-1);
                cycleIndex++;
            }
        }
        visitFrames(curT, backward, false);

        // now handle the active interval for each target
        for (i in [0..<targets.size()]) {
            var pairlist = targets.get(i) as KFPairList;
            var kfpair1 = pairlist.get(0);
            var leftT = kfpair1.frame.time.toMillis();

            if (curT < leftT) {
                // haven't yet reached the first key frame
                // for this target
                continue;
            }

            var v1:KeyValue;
            var v2:KeyValue;
            var segT = 0.0;

            for (j in [1..<pairlist.size()]) {
                // find keyframes on either side of the curT value
                var kfpair2 = pairlist.get(j);
                var rightT = kfpair2.frame.time.toMillis();
                if (curT <= rightT) {
                    v1 = kfpair1.value;
                    v2 = kfpair2.value;
                    segT = (curT - leftT) / (rightT - leftT);
                    break;
                }

                kfpair1 = kfpair2;
                leftT = kfpair1.frame.time.toMillis();
            }

            if (v1 != null and v2 != null) {
                pairlist.target.set(v2.interpolate.interpolate(v1.value, v2.value, segT));
            } 
        }

        // look through all sub-timelines and recursively call process()
        // on any active SubTimeline objects
        for (i in [0..<subtimelines.size()]) {
            var sub = subtimelines.get(i) as SubTimeline;
            if (curT >= sub.startTime.toMillis()) {
                var subDur = sub.timeline.getTotalDur();
                if (subDur < 0 or curT <= sub.startTime.toMillis() + subDur) {
                    sub.timeline.process(curT - sub.startTime.toMillis());
                }
            }
        }

        if (needsStop and clip != null) {
            clip.stop();
        }
    }

    private function visitCycle(cycle:Integer, catchingUp:Boolean) {
        var cycleBackward = false;
        if (autoReverse) {
            if (cycle mod 2 == 1) {
                cycleBackward = true;
            }
        }
        if (toggle and isReverse) {
            cycleBackward = not cycleBackward;
        }
        var cycleT = if (cycleBackward) 0 else duration;
        visitFrames(cycleT, cycleBackward, catchingUp);
        // avoid repeated visits to terminals in autoReverse case
        frameIndex = if (autoReverse) 1 else 0;
    }

    private function visitFrames(curT:Number, backward:Boolean, catchingUp:Boolean) : Void {
        if (backward) {
            var i1 = sortedFrames.size()-1-frameIndex;
            var i2 = 0;
            for (fi in [i1..i2 step -1]) {
                var kf = sortedFrames[fi];
                if (curT <= kf.time.toMillis()) {
                    if (not (catchingUp and kf.canSkip)) {
                        kf.visit();
                    }
                    frameIndex++;
                } else {
                    break;
                }
            }
        } else {
            var i1 = frameIndex;
            var i2 = sortedFrames.size()-1;
            for (fi in [i1..i2]) {
                var kf = sortedFrames[fi];
                if (curT >= kf.time.toMillis()) {
                    if (not (catchingUp and kf.canSkip)) {
                        kf.visit();
                    }
                    frameIndex++;
                } else {
                    break;
                }
            }
        }
    }

    private function createAdapter():TimingTarget {
        TimingTargetAdapter {
            override function begin() : Void {
                running = true;
                paused = false;

                if (toggle and isReverse) {
                    cycleIndex = (repeatCount-1) as Integer;
                    lastElapsed = getTotalDur();
                } else {
                    cycleIndex = 0;
                    lastElapsed = 0;
                }
                frameIndex = 0;
                offsetT = 0;
                offsetValid = false;
            }
            
            override function timingEvent(fraction, totalElapsed) : Void {
                process(totalElapsed as Number);
            }

            override function pause() : Void {
                paused = true;
            }

            override function resume() : Void {
                paused = false;
            }

            override function end() : Void {
                running = false;
                paused = false;
            }
        }
    }
}

class KFPair {
    attribute frame:KeyFrame;
    attribute value:KeyValue;
}

class KFPairList {
    var target:KeyValueTarget;
    private def pairs:ArrayList = new ArrayList();

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
    var startTime:Duration;
    var timeline:Timeline;
}
