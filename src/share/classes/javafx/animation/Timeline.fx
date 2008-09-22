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
import java.lang.Double;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.Math;
import java.lang.UnsupportedOperationException;

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
    public var repeatCount: Number = 1 on replace = newVal {
        if (newVal < INDEFINITE) {
            repeatCount = INDEFINITE;
        }
    }


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
    
    /**
     * Read-only var that indicates whether the timeline is moving
     * forward or backward.
     * <p>
     * This value is initially {@code true}, which indicates the timeline
     * is moving forward when animation is started by default.
     * <p>
     * The default behavior can be overriden by calling <code>startBackward</code> 
     */
    public-read var forward: Boolean = true;
    
    /**
     * Defines whether this animation reverses direction in place
     * each time {@code start()} is called.  
     * If {@code true}, the animation will reverse its direction
     * when it is restarted.
     * The default value is {@code false}, indicating that the
     * animation will restart with previous direction.
     *
     * @profile common
     */
    public-init var toggle: Boolean = false on replace {
        forwardStart = not toggle;
    };

    
    
    /**
     * It indicates previous direction is forward or backward.
     */
    var forwardStart: Boolean = true;
    
    /**
     * Current timeline position
     */
    var curPosition: Number = 0.0;
    
    
    /**
     * Indicates if it is a sub timeline
     */
     var subtimeline: Boolean = false;
    
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
	// timeline from running "too fast", especially 
        // when duration = 0 can result tight loop.
        return Math.max(duration, 1) * repeatCount;
    }

    /**
     * Starts (or restarts) the animation with forward direction.
     *
     * @profile common
     */
    public function startForward() {
        if(not subtimeline) {
            reset(false);

            forward = true;
            forwardStart = true;

            setSubtimelineDirection(forward);

            curPosition = 0.0;

            // stop current clip and restart from beginning
            buildClip();
            clip.start();
        }
    }

    /**
     * Starts (or restarts) the animation with forward direction.
     *
     * @profile common
     */
    public function startBackward() {
        if(not subtimeline) {
            reset(false);

            forward = false;
            forwardStart = false;

            setSubtimelineDirection(forward);

            if(duration < 0) {
                curPosition = -1;
            } else {
                curPosition = duration;
            }
            // stop current clip and restart from beginning
            buildClip();
            clip.start();        
       }
    }

    /**
     * Starts (or restarts) the animation.
     *
     * @profile common
     */
    public function start() {
        if(not subtimeline) {
            
            reset(false);

            if(toggle) {
                forwardStart = not forwardStart;
                forward = forwardStart;
                setSubtimelineDirection(forward);
            } else {
                forward = forwardStart;
                setSubtimelineDirection(forward);
            }
            if(forward) {
                curPosition = 0;
            } else {
                if(duration < 0) {
                    curPosition = -1;
                } else {
                    curPosition = duration;
                }
            }
            buildClip();
            clip.start();
        }
    }
    
    var initialKeyValues: KeyValue[];

    function reset(isSubTimeline: Boolean):Void {
        if(isSubTimeline) {
            cycleIndex = 0;
            lastElapsed = 0;
            frameIndex = 0;
        }
        
        
        for (kv in initialKeyValues) {
            kv.value = kv.target.get();
        }

        for (i in [0..<subtimelines.size()]) {
            var sub = subtimelines.get(i) as SubTimeline;
            sub.timeline.reset(true);
        }
    }

    /**
     * Stops the animation.  If the animation is not currently running,
     * this method has no effect.
     *
     * @profile common
     */
    public function stop(): Void {
        if(clip != null) {
            clip.stop();
        }
        
        // also, stop all sub timelines
        for (i in [0..<subtimelines.size()]) {
            var sub = subtimelines.get(i) as SubTimeline;
            if(sub.timeline.running) {
                sub.timeline.stop();
                sub.timeline.running = false;
            }
        }
    }

    /**
     * Pauses the animation.  If the animation is not currently running,
     * this method has no effect.
     */
    public function pause() {
        if(not subtimeline) {
            clip.pause();
        }
    }

    /**
     * Resumes the animation from a paused state.  If the animation is
     * not currently running or not currently paused, this method has
     * no effect.
     *
     * @profile common
     */
    public function resume() {
        if(not subtimeline) {
            if (not running) {
                startForward();
            } else {
                if(clip != null) {
                    clip.resume();
                }
            }
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

    var lastElapsed: Number = 0;

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
                    timeline.forwardStart = forwardStart;
                    timeline.forward = forward;
                    timeline.subtimeline = true;
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
     * <p>
     * This method has no effect if the timeline is a sub timeline.
     */

    public function setPosition(position:Duration):Void {
        if(not subtimeline) {
            timeOffset = position.toMillis();                      
        }
    }

    /**
     * Returns the current position of the Timeline's play head
     */
    public bound function getPosition():Duration {        
        return makeDur(curPosition);
    }


    var timeOffset: Number;

    /**
     * This routine process all the cases except when timeline
     * is running backward indefinitely. This special case is 
     * handled by process_backward_indefinitely().
     */
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
        var totalElapsed = if(subtimeline) totalElapsedArg else totalElapsedArg * speed;
        
        var needsStop = false;
        var totalDur = getTotalDur();
        // if timeOffset > 0, there is pending request to change play head position        
        if(timeOffset > 0) {
            if(duration < 0) {
                totalElapsed = timeOffset;
            } else {
                totalElapsed = ((totalElapsed / duration) as Integer) * duration + timeOffset;
            }
            updateFrameIndex(totalElapsed);
            timeOffset = 0.0;
        }


        lastElapsed = totalElapsed;

        var curT:Number;
        var cycle:Integer;

        if(totalDur >= 0 and totalElapsed >= totalDur) {
            totalElapsed = totalDur;
            needsStop = true;
        }

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
        }
 
        // check if passed cycle boundary
        while(cycle > cycleIndex and 
            (repeatCount < 0 or cycleIndex < repeatCount)) {
            visitCycle(cycleIndex < cycle-1);
            if(autoReverse) {
                forward = not forward;
                setSubtimelineDirection(forward);
            }
            cycleIndex ++;
        }
  
        
        if((not needsStop) or cycleIndex < repeatCount) {
            if(not forward) {
                if(duration >= 0) {
                    curT = Math.max(duration, 1) - curT;
                    curPosition = curT;
                 } else {
                    curPosition = -1;
                }
            } else {
                // update current position
                curPosition = curT;            
            }

            visitFrames(curT, false);

            doInterpolate(curT);

            // look through all sub-timelines and recursively call process()
            // on any active SubTimeline objects        
            processSubtimelines(curT);
        }
        
        if(needsStop) {
             if(clip != null) {
                clip.stop();
            }
        }    
    }
    
    /**
     *  This routine handles a special case:
     *  <l>
     *      <li> duration < 0
     *      <li> forward = false
     *  <l>
     *  <p>
     *   
     */
    function process_backward_indefinitely(totalElapsedArg:Number):Void {
        var totalElapsed = if(subtimeline) totalElapsedArg else totalElapsedArg * speed;
        
    }
    
    
    function doInterpolate(curT: Number) {
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
        
    }

    /**
     * parent timeline always delivers forward timing to sub timelines, but
     * sub timelines' direction can be altered by parent timeline.
     */
    function processSubtimelines(curT: Number): Void {
        for (i in [0..<subtimelines.size()]) {
            var sub = subtimelines.get(i) as SubTimeline;
            var subTimeline = sub.timeline;
            var subDur = subTimeline.getTotalDur();
                        
            var startTime = sub.startTime.toMillis();
            var subCurT = curT - startTime;
            if(subTimeline.forward and subTimeline.running ) {
                if(subDur < 0 or curT <= startTime + subDur) {
                    subTimeline.process(subCurT);
                } else {
                    subTimeline.process(subDur);
                    subTimeline.running = false;
                }
            } else if(not subTimeline.forward) {
                if(subTimeline.running) {
                    subTimeline.process(subCurT);
                } else if(subDur < 0 or (curT <= startTime + subDur and  curT > startTime)) {
                    subTimeline.running = true;
                    subTimeline.process(subCurT);
                }
            }
        }        
    }
    
    /**
     * Parent timeline has altered its direction, sub timelines' direction
     * needs to be altered accordingly.
     */
    function setSubtimelineDirection(forward: Boolean): Void {
        for (i in [0..<subtimelines.size()]) {
            var sub = subtimelines.get(i) as SubTimeline;
            sub.timeline.forwardStart = forward;
            sub.timeline.forward = forward;
            sub.timeline.setSubtimelineDirection(forward);
        }
    }
    
    
    /**
     * Once play head is repositioned, frameIndex has to be recalcuated
     * to complete repositioning
     */
    function updateFrameIndex(totalElapsed: Number): Void {
        var curT = if(duration < 0) totalElapsed else (totalElapsed mod duration);
        

        // now we need to recalculate frameIndex
        frameIndex = 0;
        for(kf: KeyFrame in sortedFrames) {
            if(curT <= kf.time.toMillis()) {
                if(not forward and curT == kf.time.toMillis()) {
                frameIndex ++;
                }
                break;
            }
            frameIndex ++;
        }
        if(not forward) {
            frameIndex = sortedFrames.size() - frameIndex;
        }
    }
    
    
    function visitCycle(catchingUp:Boolean) {
        var cycleT = if (forward) duration else 0;
        curPosition = cycleT;
        visitFrames(cycleT, catchingUp);
        
        /**
         * Ignore sub timelines during catching up cycles
         */
        if(not catchingUp) {
            // closeup sub timelines at cycle boundary
            handleCycleBoundary(cycleT);
        }
        
        // avoid repeated visits to terminals in autoReverse case
        frameIndex = if (autoReverse) 1 else 0;
    }

    /**
     * At the cycle boundary, stops all running sub timelines.
     */
    function handleCycleBoundary(curT: Number) {
        for (i in [0..<subtimelines.size()]) {
            var sub = subtimelines.get(i) as SubTimeline;
            if(sub.timeline.running) {
                sub.timeline.process(curT - sub.startTime.toMillis());
                sub.timeline.running = false;
            }
            sub.timeline.reset(true);
        }
    }
    
    /**
     * During timeline forward cycle, starts all sub timelines
     * of just visited key frame
     */
    function startKeyFrameSubtimelines(kf: KeyFrame, curT: Number) {
        for(t: Timeline in kf.timelines) {
            t.reset(true);
            t.running = true;
        }
    }
    
    /**
     * During timeline backward cycle, stops all sub timelines
     * of just visited key frame
     */
    function stopKeyFrameSubtimelines(kf: KeyFrame, curT: Number) {
        for(t: Timeline in kf.timelines) {
            if(not t.running) {
                t.running = true;
            }
            t.process(t.getTotalDur());
            t.running = false;
        }
    }
    
    function visitFrames(curT:Number, catchingUp:Boolean) : Void {
        if (forward) {
            var i1 = frameIndex;
            var i2 = sortedFrames.size()-1;
            for (fi in [i1..i2]) {
                var kf = sortedFrames[fi];
                if (curT >= kf.time.toMillis()) {
                    if (not (catchingUp and kf.canSkip)) {
                        kf.visit();
                        startKeyFrameSubtimelines(kf, curT);
                    } 
                    frameIndex++;
                } else {
                    break;
                }
            }
        } else {
            var i1 = sortedFrames.size()-1-frameIndex;
            var i2 = 0;
            for (fi in [i1..i2 step -1]) {
                var kf = sortedFrames[fi];
                if (curT <= kf.time.toMillis()) {
                    if (not (catchingUp and kf.canSkip)) {
                        stopKeyFrameSubtimelines(kf, curT);
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

                cycleIndex = 0;
                if(forwardStart) {
                    lastElapsed = 0;                   
                    curPosition = 0;
                } else {
                    lastElapsed = getTotalDur();
                    if(duration < 0) {
                        throw new UnsupportedOperationException("backward-running INDEFINITE sub-timeline is not supported");
                    } else {
                        curPosition = duration;
                    }
                }
                
                frameIndex = 0;
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
