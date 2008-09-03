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
import java.util.Map;
import java.util.HashMap;
import java.lang.System;
import java.lang.Math;

function makeDur(millis:Number):Duration {
    return 1ms * millis;
}

class CurrentKeyValue extends KeyValue {
}

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
     * Speed control
     * 
     * @profile common
     */

    public var speed: Number = 1.0 on replace old {
        if (old != 0) {
            lastElapsed /= old;
        }
        lastElapsed *= speed;
    }

    /**
     * Enable/disable interpolation
     *
     * @profile common
     */

    public var interpolate: Boolean = true;
        

    /**
     * Defines the number of cycles in this animation.
     * The {@code repeatCount} may be {@code INDEFINITE}
     * for animations that repeat indefinitely, but must otherwise be >= 0.
     * The default value is 1.
     *
     * @profile common
     */
    public var repeatCount: Number = 1.0;


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
    public var autoReverse: Boolean = false;

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
    public var toggle: Boolean = false on replace {
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
    public var keyFrames: KeyFrame[] on replace oldValues = newValues {
	for(keyFrame: KeyFrame in newValues) {
	    keyFrame.owner = this;
	}
        invalidate();
    };

    /**
     * Read-only var that indicates whether the animation is
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
    public-read var running: Boolean = false;

    /**
     * Read-only var that indicates whether the animation is
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
    public-read var paused: Boolean = false;

    // if false, indicates that the internal (optimized) data structure
    // needs to be rebuilt
    var valid = false;
    package function invalidate() {
        valid = false;
    }

    // duration is inferred from time of last key frame and durations
    // of any sub-timelines in rebuildTargets()
    var duration: Number = -1;

    function getTotalDur():Number {
        if (not valid) {
            rebuildTargets();
        }
        if (duration < 0 or repeatCount < 0) {
            return -1;
        }
	
	// enforce minimum duration of 1 ms
	// Refer to JFXC-1399, minimum duration prevents 
	// timeline run too fast, especially when duration = 0
	// can result tight loop.
        return Math.max(duration, 1) * repeatCount;
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
        reset();
        if (toggle) {
            // change direction in place
            if (clip == null) {
                buildClip();
            }
            if (not clip.isRunning()) {
                clip.start();
            }
        } else {
            // stop current clip and restart from beginning
            buildClip();
            clip.start();
        }
    }

    var initialKeyValues: KeyValue[];

    function reset():Void {
        for (kv in initialKeyValues) {
            kv.value = kv.target.get();
        }
        if (toggle) {
            // change direction in place
            isReverse = not isReverse;
            offsetValid = false;
            frameIndex = keyFrames.size() - frameIndex;
        } else {
            // stop current clip and restart from beginning
            frameIndex = 0;
        }
        for (i in [0..<subtimelines.size()]) {
            var sub = subtimelines.get(i) as SubTimeline;
            sub.timeline.reset();
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
        if (not running) {
            start();
        } else {
            clip.resume();
        }
    }

    function buildClip() {
        if (clip != null and clip.isRunning()) {
            clip.stop();
        }
        clip = Clip.create(Clip.INDEFINITE, adapter);
        clip.setInterpolator(Interpolators.getLinearInstance());
    }

    var clip: Clip;
    var sortedFrames: KeyFrame[];
    var targets: Map = new HashMap();
    var subtimelines: ArrayList = new ArrayList();
    var adapter: TimingTarget = createAdapter();

    var cycleIndex: Integer = 0;
    var frameIndex: Integer = 0; 

    var isReverse: Boolean = true;
    var offsetT: Number = 0;
    var lastElapsed: Number = 0;
    var offsetValid: Boolean = false;

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
    function rebuildTargets():Void {
        initialKeyValues = [];
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
                var pairlist: KFPairList = targets.get(keyValue.target) as KFPairList;
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
                        
                        var initkv = KeyValue {
                            target: kv.target;
                            value: kv.value;                            
                        }
                        
                        insert initkv into initialKeyValues;
                        var kfp = KFPair {
                            value: kv
                            frame: zeroFrame
                        }
                        pairlist.add(kfp);
                    }
                    targets.put(keyValue.target, pairlist);
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

    /**
     * Sets the Timeline's play head to {@code position}
     */

    public function setPosition(position:Duration):Void {
        if (running) {
           timeOffset += position.toMillis();                      
        } else {
           timeOffset = position.toMillis();                      
        }
    }

    /**
     * Returns the current position of the Timeline's play head
     */

    public bound function getPosition():Duration {
        var pos = if (duration == 0) 0 else lastElapsed mod duration;
        return makeDur(pos);
    }


    var timeOffset: Number;

    function process(totalElapsedArg:Number):Void {
        // 1. calculate totalDur
        // 2. modify totalElapsed depending on direction
        // 3. clamp totalElapsed and set needsStop if necessary
        // 4. calculate curT and cycle based on totalElapsed
        // 5. decide whether to increment or decrement cycle/frame index, depending on direction
        // 6. visit key frames
        // 7. do interpolation between active key frames
        // 8. visit subtimelines
        // 9. stop clip if needsStop
        var totalElapsed = totalElapsedArg * speed;
        totalElapsed += timeOffset;

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
        }

        lastElapsed = totalElapsed;

        var curT:Number;
        var cycle:Integer;
        var backward = false;
        if (duration < 0) {
            // indefinite duration (e.g. will occur when a sub-timeline
            // has indefinite repeatCount); always stay on zero cycle
            curT = totalElapsed;
            cycle = 0;
        } else {
	    // enforce minimum duration of 1 ms
	    var dur = Math.max(duration, 1);
	    
            curT = totalElapsed mod dur;
            cycle = totalElapsed / dur as Integer;
            if (curT == 0 and totalElapsed != 0) {
                // we're at the end, or exactly on a cycle boundary;
                // treat this as the "1.0" case of the previous cycle
                // instead of the "0.0" case of the current cycle
                // TODO: there's probably a better way to deal with this...
                curT = dur;
                cycle -= 1;
            }
            if (autoReverse) {
                if (cycle mod 2 == 1) {
                    curT = dur - curT;
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
        if (interpolate) {
            // now handle the active interval for each target
            var iter = targets.values().iterator();
            while (iter.hasNext()) {
                var pairlist = iter.next() as KFPairList;
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
                    if (curT < rightT) {
                        v1 = kfpair1.value;
                        v2 = kfpair2.value;
                        segT = (curT - leftT) / (rightT - leftT);
                        break;
                    } 
                    kfpair1 = kfpair2;
                    leftT = kfpair1.frame.time.toMillis();
                }
                if (segT == 0.0 or segT == 1.0) {
                    continue;
                }
                if (v1 != null and v2 != null) {
                    if (v2.interpolate == null) {
                        var v = Interpolator.LINEAR.interpolate(v1.value, 
                                                                v2.value, segT);
                        pairlist.target.set(v);
                    } else {
                        pairlist.target.set(v2.interpolate.interpolate(v1.value, v2.value, segT));
                    }
                } 
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
            timeOffset = 0.0;
        }
    }

    function visitCycle(cycle:Integer, catchingUp:Boolean) {
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

    function visitFrames(curT:Number, backward:Boolean, catchingUp:Boolean) : Void {
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

    function createAdapter():TimingTarget {
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
    var frame:KeyFrame;
    var value:KeyValue;
}

class KFPairList {
    var target:KeyValueTarget;
    def pairs:ArrayList = new ArrayList();

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
