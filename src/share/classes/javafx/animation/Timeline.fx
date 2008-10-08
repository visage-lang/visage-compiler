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
import javafx.util.Sequences;
import java.lang.Object;
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
     * Defines the direction/speed at which the timeline is expected to
     * be played.
     * <p>
     * The absolute value of {@code rate} indicates the speed which the timeline
     * is to be played, while the sign of {@code rate} indicates the direction
     * in which the timelineis to be played. A postive value of {@code rate}
     * indicates forward play, a negative value indicates backward play and {@code 0.0}
     * to stop a running timeline. The default value is {@code 1.0}.
     * <p>
     * Rate {@code 1.0} is normal play, {@code 2.0} is 2 time normal,
     * {@code -1.0} is backwards, etc...
     * 
     * <p>
     * Inverting the rate of a running timeline will cause the {@code Timeline}
     * to reverse direction in place and play back over the portion of the 
     * {@code Timeline} that has alreay elapsed.
     *
     * @profile common
     */

    public var rate: Number = 1.0 on replace old {
        var newSpeed = Math.abs(rate);
        if(rate == 0) {
            stop();
        } else {
            if (old != 0) {
                // figure out if there is a direction change
                if(old * rate < 0) {
                    toggle();
                }
            } else {
                forward = rate > 0;
            }   

            if(newSpeed != Math.abs(old)) {
                speedChangePos = lastElapsed;
                speedChangeElapsedPos = curElapsed;
            }
        }

        if(currentRate != 0) {
            currentRate = if(forward) newSpeed else -newSpeed;
        }
    }
    
    /**
     * Read-only variable to indicate current direction/speed at which the 
     * timeline is being played.
     * <p>
     * {@code currentRate} is not necessary equal to {@code rate}. {@code currentRate}
     * is set to {@code 0.0} when animation is paused or stopped. Also {@code currentRate}
     * may point to different direction during some repeat cycles when {@code autoReverse} is {@code true}
     *
     * @see rate
     * @profile common
     */
    public-read var currentRate: Number = 0.0;
   
    /**
     * Defines timline's play head position.
     * <p>
     * If timeline is running, it jumps to the specified position immediately.
     * If it is not running, the {@code time} indicates from where the timeline
     * to start when next {@code play()} is called.
     * <p>
     * If user wants to bind the variable and update it simultaneously, bidirectional
     * {@code bind} is needed.
     * <p>
     *  <code>
     *      var pos: Duration;<br>
     *      var t: Timeline = Timeline { <br>
     *          time: bind pos with inverse; <br>
     *          ...<br>
     *      } 
     *  </code>
     *
     * <p> 
     *  Changes {@code time} on sub timelines are ignored.
     * 
     *  @profile common
     *
     */
    public var time: Duration = 0.0ms;
    
    /**
     * Enable/disable interpolation. The default value is {@code true}
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
     * {@code forward} indicates whether the timeline is moving
     * forward or backward.
     * <p>
     * This value is initially {@code true}, which indicates the timeline
     * is moving forward when animation is started by default.
     * <p>
     */
    var forward: Boolean = true on replace {
        if(currentRate != 0) {
            currentRate = -currentRate;
        }
    }
    
    /**
     * {@code curTime} tracks current play head position internally, so 
     * {@code Timeline} can distinguish if {@code time} has been 
     * modified externally.
     */
    var curPos: Number = 0.0;
    
    
    /**
     * Randomly seeking a {@code Timeline} may affect {@code Timeline}'s
     * duration. {@code durOffset} tracks the duration changes and
     * compensates it accordingly.
     */
    var durOffset: Number = 0.0;
    
    
    /**
     * The position timeline speed is changed.
     */
    var speedChangePos: Number = 0.0;
    
    /**
     * At what point current speed was set, without factor in timeline speed.
     */
    var speedChangeElapsedPos: Number = 0.0;
    
    /**
     * Timeline total elapsed time without factor in speed.
     */
    var curElapsed: Number = 0.0;
    
    /**
     * {@code Timeline} can be inverted in place, the inversion point needs to be
     * recorded.
     */
    var invertOffsetValid: Boolean = true;
    var invertOffsetT: Number = 0.0;
    
    /**
     * {@code isReverse} is true, {@code Timeline} needs to be unwinded.
     */
    var isReverse: Boolean = false;
    
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
     * Plays timeline from current position in the direction indicated
     * by {@code rate}. If the timeline is running, it has no effect.
     * <p>
     * When {@code rate} > 0 (forward play), if a timeline is already
     * positioned at the end, the first cycle will not be played, it is 
     * considered to have already finished. This also applies to a
     * backward ({@code rate} < 0) cycle if a timeline is positioned at the 
     * beginning. However, if the timeline has {@code repeatCount} > 1, 
     * following cycle(s) will be played as usual.
     * <p>
     * To play a {@code Timeline} backwards from the end:<br>
     * <code>
     *  timeline.rate = negative rate<br>
     *  timeline.time = overall duration of timeline<br>
     *  timeline.play()<br>
     * </code>
     * <p>
     * Note:
     *  <l>
     *      <li>{@code play()} is an asynchronous call, timeline may not start
     *          immediately.
     *      <li>It has no effect on sub timelines.
     *  </l>
     *
     * @profile common
     */
    public function play() {
        if(not subtimeline and rate != 0.0) {
            // timeline not yet started, so just start it
            if(clip == null or not clip.isRunning()) {
                start();
            } else if(paused) {
                resume();
            }
        } 
    }
    
    
    function start() {
        reset(false);
        if(not forward and duration < 0) {
            throw new UnsupportedOperationException("backward-running INDEFINITE sub-timeline is not supported");
        }
        setSubtimelineDirection(forward);        
        buildClip();
        clip.start();       
    }
    
    /**
     * Plays timeline from initial position in forward direction.
     * <p>
     * It is equivalent to
     * <p>
     *  <code>
     *      timeline.stop();<br>
     *      timeline.rate = Math.abs(timeline.rate); </br>
     *      timeline.time = 0.0s;<br>
     *      timeline.play();<br>
     *  </code>
     *
     * <p>
     * Note:
     *  <l>
     *      <li>{@code playFromStart()} is an asynchronous call, timeline may not start
     *          immediately.
     *      <li>It has no effect on sub timelines.
     *  </l>
     *  <p>
     *
     *  @profile common
     */
    public function playFromStart() {
        if(not subtimeline and rate != 0.0) {
            rate = Math.abs(rate);
            getTotalDur();
            curPos = 0.0;
            time = 0.0ms;
            start();
        }
    }
    
    /**
     * Toggle the {@code Timeline}.
     * If the {@code Timeline} is running, the {@code Timeline} will be unwound in place,
     * meaning the {@code Timeline} will reverse its direction and run backwards from
     * current position back to the original position. If the running {@code Timeline}
     * is toggled a second time, the {@code Timeline} will again reverse direction and
     * run forwards from the current position.
     * <p>
     * If the {@code timeline} is not running, it simply sets up the {@code Timeline}
     * to run in opposite direction in next {@code play()} call.
     */
    function toggle() {
        if(not subtimeline) {
            forward = not forward;
            if(running) {
                // Timeline is reversed in place, offset needs to be recorded.
                invertOffsetValid = false;
                isReverse = not isReverse;
                setSubtimelineDirection(forward);                
                frameIndex = sortedFrames.size() - frameIndex -1 ;
            } 
        }
    }
    
    var initialKeyValues: KeyValue[];

    function reset(isSubTimeline: Boolean):Void {
        if(isSubTimeline) {
            cycleIndex = 0;
            lastElapsed = 0;
            frameIndex = 0;
        } else if(not valid) {
            rebuildTargets();
        }
 
        
        for (kv in initialKeyValues) {
            kv.target.set(kv.value);
        }
        
        for (i in [0..<subtimelines.size()]) {
            var sub = subtimelines.get(i) as SubTimeline;
            sub.timeline.reset(true);
        }
    }

    /**
     * Stops the animation.  If the animation is not currently running,
     * this method has no effect.
     * <p>
     * Note:
     *  <l>
     *      <li>{@code stop()} is an asynchronous call, timeline may not stop
     *          immediately.
     *      <li>It has no effect on sub timelines.
     *  </l>
     *
     * @profile common
     */
    public function stop(): Void {
        if(not subtimeline) {
            if(clip != null) {
                clip.stop();
            }

            // also, stop all sub timelines
            for (i in [0..<subtimelines.size()]) {
                var sub = subtimelines.get(i) as SubTimeline;
                if(sub.timeline.running) {
                    sub.timeline.stop();
                    sub.timeline.running = false;
                    sub.timeline.currentRate = 0.0;
                }
            }

            forward = rate >= 0;
       }
    }

    /**
     * Pauses the animation.  If the animation is not currently running,
     * this method has no effect.
     * <p>
     * Note:
     *  <l>
     *      <li>{@code pause()} is an asynchronous call, timeline may not pause
     *          immediately.
     *      <li>It has no effect on sub timelines.
     *  </l>
     *  
     *  @profile common
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
    function resume() {
        if(clip != null) {
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
                if(subtimeline and keyFrame.timelines.size() > 0) {
                    throw new UnsupportedOperationException("Sub timeline in a sub timeline is not supported");                    
                }
                
                for (timeline in keyFrame.timelines) {
                    timeline.subtimeline = true;
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
                    timeline.forward = forward;
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
                    
                    // get current value and attach it to zero frame
                    var kv = KeyValue {
                        target: keyValue.target;
                        value: keyValue.target.get();
                    }
                        
                    insert kv into initialKeyValues;
                    var kfp = KFPair {
                        value: kv
                        frame: zeroFrame
                    }
                    pairlist.add(kfp);

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
        curElapsed = totalElapsedArg;

        var totalElapsed = totalElapsedArg;
        if(not subtimeline) {
            totalElapsed = (totalElapsed - speedChangeElapsedPos) * Math.abs(rate) + 
                            speedChangePos;            
        }
        
        totalElapsed -= durOffset;
        var needsStop = false;
        var totalDur = getTotalDur();
	// enforce minimum duration of 1 ms
        var dur = Math.max(duration, 1);

        var curT:Number;
        var cycle:Integer;

        // if position has been modified externally, reposition the playhead   
        var timeInMillis = time.toMillis();
        var playheadUpdated: Boolean = false;
        
        if(curPos != timeInMillis and not subtimeline) {
            playheadUpdated = true;
            
            if(totalDur < 0) {
                totalElapsed = timeInMillis;
            } else {
                cycle = totalElapsed / dur as Integer;
                
                var savedElapsed = totalElapsed;
                
                // pulse to end of timeline
                if(cycle >= repeatCount) {
                    totalElapsed = (repeatCount - 1) * dur + timeInMillis;
                } else {
                    totalElapsed = cycle * dur + timeInMillis;
                }
                
                durOffset += savedElapsed - totalElapsed;
                
            }
            updateFrameIndex(totalElapsed);
        }

        if(totalDur >= 0) {
            // needs to adjust offset if timeline is reversed.
            if(not invertOffsetValid) {
                invertOffsetT = if(isReverse) totalElapsed + lastElapsed else totalElapsed - lastElapsed;
                invertOffsetValid = true;
            }
            
            totalElapsed = if(isReverse) invertOffsetT - totalElapsed else totalElapsed - invertOffsetT;
            
            if(isReverse) {
                if(totalElapsed <= 0) {
                    totalElapsed = 0;
                    needsStop = true;
                }
            } else {
                if(totalElapsed >= totalDur) {
                    totalElapsed = totalDur;
                    needsStop = true;
                }
            }
            lastElapsed = totalElapsed;
        }
        
        if (duration < 0) {
            // indefinite duration (e.g. will occur when a sub-timeline
            // has indefinite repeatCount); always stay on zero cycle
            curT = totalElapsed;
            cycle = 0;
        } else {
            curT = totalElapsed mod dur;
            cycle = totalElapsed / dur as Integer;
        }
        
        if(playheadUpdated) {
            updateSubtimelinePlayhead(curT);
        }
        
        // check if passed cycle boundary
        if(isReverse) {
            while(cycle < cycleIndex and 
            (repeatCount < 0 or cycleIndex >= 0)) {
                visitCycle(cycleIndex > cycle+1);
                if(autoReverse) {
                    forward = not forward;
                    setSubtimelineDirection(forward);
                } else if(not subtimeline and not needsStop){
                    reset(false);
                }
                cycleIndex --;
            }
        } else {        
            while(cycle > cycleIndex and 
                (repeatCount < 0 or cycleIndex < repeatCount)) {
                visitCycle(cycleIndex < cycle-1);
                if(autoReverse) {
                    forward = not forward;
                    setSubtimelineDirection(forward);
                } else if(not subtimeline and not needsStop){
                    reset(false);
                }
                cycleIndex ++;
            }
        }
        var cycleForward = if(isReverse) not forward else forward;
        
        if((not needsStop) or cycleIndex < repeatCount) {
            if(not cycleForward) {
                if(duration >= 0) {
                    curT = dur - curT;
                    curPos = curT;
                    time = makeDur(curT);
                 } else {
                    curPos = totalElapsed;
                    time = makeDur(totalElapsed);
                }
            } else {
                curPos = curT;
                time = makeDur(curT);
            }

            if(not visitFrames(curT, false)) {
                return;
            }

            doInterpolate(curT);

            // look through all sub-timelines and call process()
            // on any active SubTimeline objects        
            processSubtimelines(curT);
        }
        
        if(needsStop) {
             if(clip != null) {
                clip.stop();
            }
            if(not subtimeline) {
                forward = rate >= 0;
            }
        }    
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
            if(forward and subTimeline.running ) {
                if(subDur < 0 or curT < startTime + subDur) {
                    subTimeline.process(subCurT);
                } else {
                    subTimeline.process(subDur);
                    subTimeline.running = false;
                    subTimeline.currentRate = 0.0;
                }
            } else if(not forward) {
                if(subTimeline.running) {
                    subCurT = subDur - subCurT;
                    subTimeline.process(subCurT);
                    if(subCurT <= 0) {
                        subTimeline.running = false;
                        subTimeline.currentRate = 0.0;
                    }
                } else if(subDur < 0 or (curT <= startTime + subDur and  curT > startTime)) {
                    subTimeline.running = true;
                    subTimeline.currentRate = if(subTimeline.forward) Math.abs(rate) else - Math.abs(rate);
                    if(subDur >= 0) {
                        subCurT = subDur - subCurT;
                    }
                    subTimeline.process(subCurT);
                }
            }
        }        
    }
    
    function updateSubtimelinePlayhead(curT: Number) { 
        for(i in [0..<subtimelines.size()]) {
            var sub = subtimelines.get(i) as SubTimeline;
            var subTimeline = sub.timeline;
            var subDur = subTimeline.getTotalDur();
            var startTime = sub.startTime.toMillis();
            
            if(curT >= startTime and 
               (curT <= startTime + subDur or subDur < 0)) {
               if(not subTimeline.running) {
                   subTimeline.running = true;
                   subTimeline.currentRate = if(subTimeline.forward) Math.abs(rate) else -Math.abs(rate);
               }
            } else if(curT < startTime) {
                if(subTimeline.running) {
                    subTimeline.reset(true);
                    subTimeline.running = false;
                    subTimeline.currentRate = 0.0;
                }            
            } else if(curT > startTime + subDur)
                if(subTimeline.running) {
                    subTimeline.process(subDur);
                    subTimeline.running = false;
                    subTimeline.currentRate = 0.0;
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
            var subTimeline = sub.timeline;
            var subRate = subTimeline.currentRate;
            if(subRate == 0.0) {
                subRate = subTimeline.rate;
            }
            if(forward) {
                subTimeline.forward = if(subRate >= 0) true else false;
            } else {
                subTimeline.forward = if(subRate <= 0) true else false;
            }
        }
    }
    
    
    /**
     * Once play head is repositioned, frameIndex has to be recalcuated
     * to complete repositioning
     */
    function updateFrameIndex(totalElapsed: Number): Void {
        var curT = if(duration < 0) totalElapsed else (totalElapsed mod Math.max(duration, 1));
        
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
    
    
    function visitCycle(catchingUp:Boolean): Void {
        var cycleT = if (forward) duration else 0;
        curPos = cycleT;
        time = makeDur(cycleT);
        
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
                sub.timeline.currentRate = 0.0;
            }
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
            t.currentRate = if(t.forward) Math.abs(rate) else -Math.abs(rate);
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
                t.currentRate = if(t.forward) Math.abs(rate) else -Math.abs(rate);
            }
            t.process(t.getTotalDur());
            t.running = false;
            t.currentRate = 0.0;
        }
    }
    
    /**
     * Play head can be changed inside key frame's action callback,
     * if it is the case, we want to abort and re-evaluate at next
     * pulse.
     */
    function visitFrames(curT:Number, catchingUp:Boolean) : Boolean {
        if (forward) {
            var i1 = frameIndex;
            var i2 = sortedFrames.size()-1;
            for (fi in [i1..i2]) {
                var kf = sortedFrames[fi];
                if (curT >= kf.time.toMillis()) {
                    if (not (catchingUp and kf.canSkip)) {
                        kf.visit();
                        if(time.toMillis() != curPos) {
                            return false;
                        }
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
                        if(time.toMillis() != curPos) {
                            return false;
                        }
                    }
                    frameIndex++;
                } else {
                    break;
                }
            }
        }
        return true;
    }

    
    function pauseSubtimelines() {
        for(i in [0..<subtimelines.size()]) {
            var sub = subtimelines.get(i) as SubTimeline;
            if(sub.timeline.running) {
                sub.timeline.currentRate = 0.0;
                sub.timeline.paused = true;
            }
        }
    }
    
    function resumeSubtimelines() {
        var speed = Math.abs(rate);
        for(i in [0..<subtimelines.size()]) {
            var sub = subtimelines.get(i) as SubTimeline;
            if(sub.timeline.running) {
                sub.timeline.currentRate = if(sub.timeline.forward) speed else -speed;
                sub.timeline.paused = false;
            }
        }        
    }
    
    function createAdapter():TimingTarget {
        TimingTargetAdapter {
            override function begin() : Void {
                running = true;
                paused = false;

                invertOffsetValid = true;
                isReverse = false;
                invertOffsetT = 0.0;
                durOffset = 0.0;

                cycleIndex = 0;
                durOffset = 0;
                forward = (rate >= 0);
                speedChangePos = 0.0;
                speedChangeElapsedPos = 0.0;
                var totalDur = getTotalDur();
                        
                if(forward) {
                    lastElapsed = 0;
                    /**
                     * If timeline already reaches the end before it even starts, 
                     * and intends to move forward, treat it as a completed
                     * forward cycle.
                     */
                    if(time.toMillis() == duration) {
                        durOffset = -duration;
                        curPos = duration;
                        cycleIndex ++;
                        if(autoReverse) {
                            forward = not forward;
                        }
                    }
                } else {
                    lastElapsed = totalDur;
                    /**
                     * If timeline is at initial position and intends to move backward, 
                     * treat it as a completed backward cycle.
                     */
                    if(time == 0ms) {
                        cycleIndex ++;
                        durOffset = -duration;
                        if(autoReverse) {
                            forward = not forward;
                        }
                    } else if(time.toMillis() == duration) {
                        // play backward from the end of timeline
                        curPos = 0.0;
                        time = 0.0ms;
                    }
                }
                
                frameIndex = 0;
                if(forward) {
                    currentRate = Math.abs(rate);
                } else {
                    currentRate = - Math.abs(rate);
                }
            }
            
            override function timingEvent(fraction, totalElapsed) : Void {
                process(totalElapsed as Number);
            }

            override function pause() : Void {
                paused = true;
                pauseSubtimelines();
                currentRate = 0.0;
            }

            override function resume() : Void {
                paused = false;
                resumeSubtimelines();
                if(forward) {
                    currentRate = Math.abs(rate);
                } else {
                    currentRate = - Math.abs(rate);
                }
            }

            override function end() : Void {
                running = false;
                paused = false;
                currentRate = 0.0;

                invertOffsetValid = true;
                isReverse = false;
                invertOffsetT = 0.0;
                durOffset = 0.0;
        
                curPos = 0.0;
                time = 0ms;
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
