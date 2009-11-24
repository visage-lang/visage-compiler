/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.javafx.animation.ClipFactory;
import com.sun.javafx.animation.Clip;
import com.sun.javafx.animation.InterpolatorFactory;
import com.sun.javafx.animation.TimingTarget;
import com.sun.javafx.animation.TimingTargetAdapter;
import com.sun.javafx.runtime.Entry;
import javafx.lang.Duration;
import javafx.util.Sequences;
import java.lang.Object;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.Math;
import java.lang.UnsupportedOperationException;
import java.lang.RuntimeException;


function makeDur(millis:Number):Duration {
    return Duration.valueOf(millis);
}

package function getClipFactory():ClipFactory {
    var animationProvider = Entry.getAnimationProvider();
    if (animationProvider == null) {
        throw new RuntimeException("Unable to getClipFactory");
    }

    animationProvider.getClipFactory();
}

package function getInterpolatorFactory():InterpolatorFactory {
    var animationProvider = Entry.getAnimationProvider();
    if (animationProvider == null) {
        throw new RuntimeException("Unable to getInterpolatorFactory");
    }

    animationProvider.getInterpolatorFactory();
}

class CurrentKeyValue extends KeyValue {
}

/**
 * Used to specify an animation that repeats indefinitely, until
 * the {@code stop()} method is called.
 *
 * @profile common
 */
public def INDEFINITE = -1;

/**
 * An animation is driven by its associated properties, such as size, location
 * and color, etc. {@code Timeline} provides the capability to update
 * the property values along the progression of time.
 * <p>
 * A {@code Timeline}, defined by one or more {@link KeyFrame}s, processes
 * individual {@link KeyFrame} sequentially, in the order specified by
 * {@code KeyFrame.time}. The animated properties, defined as key
 * values in {@code KeyFrame.values}, are interpolated (when interpolation is enabled)
 * to/from the targeted key values at the specified time of the {@link KeyFrame} to
 * {@code Timeline}'s initial position, depends on {@code Timeline}'s direction.
 * <p>
 * {@code Timeline} processes individual {@code KeyFrame} at or after specified
 * time interval elapsed, it does not guarantee the timing when {@code KeyFrame}
 * is processed.
 * <p>
 * Call {@link #play()} or {@link #playFromStart()} to play a {@code Timeline}.
 * The {@code Timeline} progresses in the direction and speed specified by
 * {@link #rate}, and stops when its duration is elasped. A {@code Timeline}
 * with indefinite duration (a {@link #repeatCount}
 * of {@link #INDEFINITE}) runs repeatedly until the {@link #stop()} method
 * is explicitly called, which will stop the running
 * {@code Timeline} and reset its play head to the initial position.
 * <p>
 * {@code Timeline} can be paused by calling {@link #pause()}, and next {@link play()}
 * call will resume the {@code Timeline} from where it was paused.
 * <p>
 * A {@code Timeline}'s play head can be randomly positioned, whether it is running or
 * not. If the {@code Timeline} is running, the play head jumps to the specified
 * position immediately and continues playing from new position. If the {@code Timeline}
 * is not running, the next {@link #play()} will start the {@code Timeline} from the
 * specified position.
 * <p>
 * Invert the value of {@link #rate} can invert {@code Timeline} play direction. Inverting
 * a running {@code Timeline} causes it to reverse direction in play and play back
 * over the portion it has elapsed.
 *
 * @profile common
 * @see KeyFrame
 *
 */

public class Timeline {
    // NOTE: We use a private instance def rather than directly using Duration.INDEFINITE
    // to workaround compiler bug JFXC-3248 which was causing a memory leak.
    def DURATION_INDEFINITE: Duration = Duration.INDEFINITE;

    /**
     * Defines the direction/speed at which the {@code Timeline} is expected to
     * be played.
     * <p>
     * The absolute value of {@code rate} indicates the speed which the {@code Timeline}
     * is to be played, while the sign of {@code rate} indicates the direction.
     * A postive value of {@code rate} indicates forward play, a negative value
     * indicates backward play and {@code 0.0} to stop a running timeline.
     * <p>
     * Rate {@code 1.0} is normal play, {@code 2.0} is 2 time normal,
     * {@code -1.0} is backwards, etc...
     *
     * <p>
     * Inverting the rate of a running {@code Timeline} will cause the {@code Timeline}
     * to reverse direction in place and play back over the portion of the
     * {@code Timeline} that has alreay elapsed.
     *
     * @profile common
     * @defaultvalue 1.0
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
        }

        if(currentRate != 0) {
            currentRate = if(forward) newSpeed else -newSpeed;
        }
        if (running) {
            rateChanged = true;
        }
    }

    /**
     * Read-only variable to indicate current direction/speed at which the
     * {@code Timeline} is being played.
     * <p>
     * {@code currentRate} is not necessary equal to {@code rate}.
     * {@code currentRate} is set to {@code 0.0} when animation is paused
     * or stopped. {@code currentRate} may also point to different direction
     * during reverse cycles when {@code autoReverse} is {@code true}
     *
     * @profile common
     * @defaultvalue 0.0
     */
    public-read var currentRate: Number = 0.0;

    /**
     * Read-only variable to indicate the duration of one cycle of this
     * {@code Timeline}: the time it takes to play from time 0 to the
     * {@code KeyFrame} with the largest time (at the default  {@code rate}
     * of 1.0).
     *
     * <p>
     * This is set to the largest time value of its keyFrames.
     *
     * @profile common
     * @defaultvalue 0ms
     */
    public-read protected var cycleDuration:Duration = 0ms;

    /**
     * Read-only variable to indicate the total duration of this
     * {@code Timeline}, including repeats. A Timeline with a
     * {@code repeatCount} of {@code Timeline.INDEFINITE} will have a
     * {@code totalDuration} of {@code Duration.INDEFINITE}.
     *
     * <p>
     * This is set to cycleDuration * repeatCount.
     *
     * @profile common
     * @defaultvalue 0ms
     */
    public-read var totalDuration:Duration = bind
        if (repeatCount == Timeline.INDEFINITE) 
            then DURATION_INDEFINITE
            else repeatCount * cycleDuration;

    /**
     * Defines the {@code Timeline}'s play head position.
     * <p>
     * If {@code Timeline} is running, it jumps to the specified position immediately.
     * If it is not running, the {@code time} indicates from where the {@code Timeline}
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
     * @profile common
     * @defaultvalue 0ms
     *
     */
    public var time: Duration = 0ms on replace old {
        if (old != time) {
            doInterpolate(time.toMillis() as Number);
        }
    }

    // inner Timeline's engine should ALWAYS use this function
    // instead of direct assignment to {@code time}
    // to avoid inconsistancy
    function movePlayhead(pos: Number) {
        if (pos != curPos) {
            curPos = pos;
            time = Duration.valueOf(curPos);
        }
    }

   /**
    * Enable/disable interpolation.
    *
    * @profile common
    * @defaultvalue true
    */
    public var interpolate: Boolean = true;


    /**
     * Defines the number of cycles in this animation.
     * The {@code repeatCount} may be {@code INDEFINITE}
     * for animations that repeat indefinitely, but must otherwise be >= 0.
     *
     * @profile common
     * @defaultvalue 1.0
     *
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
     * Otherwise, animation will loop such that each cycle proceeds
     * forward from the initial {@code KeyFrame}.
     *
     * @profile common
     * @defaultvalue false
     */
    public var autoReverse: Boolean = false;


    /**
     * Defines the sequence of {@code KeyFrame}s in this animation.
     * If a {@code KeyFrame} is not provided for the {@code time==0s}
     * instant, one will be synthesized using the target values
     * that are current at the time {@link #play()} or {@link #playFromStart()}
     * is called.
     *
     * @profile common
     */
    public var keyFrames: KeyFrame[] on replace oldValues = newValues {
        for(keyFrame: KeyFrame in newValues) {
            keyFrame.owner = this;
        }
        invalidate();
    	sortAndComputeTL(false);
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
     * The maximum framerate at which this animation will run, in frames per
     * second.  This can be used, for example, to keep particularly complex
     * Timelines from over-consuming system resources.
     * By default, a Timeline's framerate is not explicitly limited, meaning
     * the Timeline will run at an optimal framerate for the underlying
     * platform.
     *
     * @profile common
     */
    // We're gonna have to go right to ludicrous speed
    public-init var framerate: Number = Float.MAX_VALUE;

    /**
     * {@code forward} indicates whether the timeline is on
     * forward cycle.
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
     * {@code curPos} tracks the current play head position internally, so
     * {@code Timeline} can distinguish whether {@code time} has been
     * modified externally.
     */
    var curPos: Number = 0.0;


    /**
     * Timeline total elapsed time without factor in speed.
     */
    var curElapsed: Number = 0.0;


    /**
     * {@code isReverse} is true, {@code Timeline} is unwinding.
     */
    var isReverse: Boolean = false;

    // if false, indicates that the internal (optimized) data structure
    // needs to be rebuilt
    var valid = false;

    package function invalidate() {
        valid = false;
    }

    // duration is inferred from time of last key frame in rebuildTargets()
    var timelineDur: Number = -1;

    package function sortAndComputeTL(sorted: Boolean):Void {
        /*
        if (keyFrames != null) {
            if (not sorted) {
                sortedFrames = Sequences.sort(keyFrames) as KeyFrame[];
                cycleDuration = sortedFrames[sortedFrames.size()-1].time;
            }
        }
        */
        if (not sorted) {
            if (keyFrames.size() > 0) {
                sortedFrames = Sequences.sort(keyFrames) as KeyFrame[];
                cycleDuration = sortedFrames[sortedFrames.size()-1].time;
            } else {
                sortedFrames = [];
                cycleDuration = 0ms;
            }
        }
    }

    function getTotalDur():Number {
        if (not valid) {
            rebuildTargets();
        }
        if (timelineDur < 0 or repeatCount < 0) {
            return -1;
        }

        // enforce minimum timelineDur of 1 ms
        // Refer to RT-319, minimum timelineDur prevents
        // timeline from running "too fast", especially
        // when timelineDur = 0 can result tight loop.
        return Math.max(timelineDur, 1) * repeatCount;
    }


    /**
     * Plays {@code Timeline} from current position in the direction indicated
     * by {@code rate}. If the timeline is running, it has no effect.
     * <p>
     * When {@code rate} > 0 (forward play), if a {@code Timeline} is already
     * positioned at the end, the first cycle will not be played, it is
     * considered to have already finished. This also applies to a
     * backward ({@code rate} < 0) cycle if a timeline is positioned at the
     * beginning. However, if the {@code Timeline} has {@code repeatCount} > 1,
     * following cycle(s) will be played as usual.
     * <p>
     * When {@code Timeline} reaches the end, {@code Timeline} is stopped
     * and the play head remains at the end.
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
     *      <li>{@code play()} is an asynchronous call, {@code Timeline} may not start
     *          immediately.
     *  </l>
     *
     * @profile common
     */
    public function play() {
        if(rate != 0.0) {
            // timeline not yet started, so just start it
            if(clip == null or not clip.isRunning()) {
                start();
            } else if(paused) {
                resume();
            }
        }
    }


    function start() {
        stopping = false;
        validate();
        if (time == 0.0ms) {
            initKeyValues();
        }
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
     *      <li>{@code playFromStart()} is an asynchronous call, {@code Timeline} may not start
     *          immediately.
     *  </l>
     *  <p>
     *
     *  @profile common
     */
    public function playFromStart() {
        if(rate != 0.0) {
            rate = Math.abs(rate);
            getTotalDur();
            movePlayhead(0);
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
        forward = not forward;
        if(running) {
            isReverse = not isReverse;
            rateChanged = true;
        }
    }

    var initialKeyValues: KeyValue[];

    function reset():Void {
        validate();
        initKeyValues();
    }

    function validate():Void {
        if(not valid) {
            rebuildTargets();
        }
    }

    function initKeyValues():Void {
        for (kv in initialKeyValues) {
            kv.target.set(kv.value());
        }
    }

    var stopping = false;
    /**
     * Stops the animation and resets the play head to its initial position.
     * If the animation is not currently running, this method has no effect.
     * <p>
     * Note:
     *  <l>
     *      <li>{@code stop()} is an asynchronous call, timeline may not stop
     *          immediately.
     *  </l>
     *
     * @profile common
     */
    public function stop(): Void {
        stopping = true;
        if(clip != null) {
            clip.stop();
        }

        forward = rate >= 0;

        if(not running) {
            movePlayhead(0);
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
     *  </l>
     *
     *  @profile common
     */
    public function pause() {
        clip.pause();
    }

    /**
     * Resumes the animation from a paused state.  If the animation is
     * not currently running or not currently paused, this method has
     * no effect.
     *
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
        clip = getClipFactory().create(Clip.INDEFINITE, adapter);
        clip.setInterpolator(getInterpolatorFactory().getLinearInstance());
        // Leave Clip resolution alone if framerate was not set by user
        if (framerate != Float.MAX_VALUE) {
            clip.setResolution(1000 / framerate)
        }
    }

    var clip: Clip;
    var sortedFrames: KeyFrame[];
    var targets: Map = new HashMap(); // KeyValueTarget -> List<KFPair>
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
    //
    // *Should* work, may not
    //   - KeyValue.value
    //   - KeyValue.interpolate
    //
    function rebuildTargets():Void {
        initialKeyValues = [];
        targets.clear();
        if (sizeof keyFrames == 0) {
            return;
        }

	    timelineDur = sortedFrames[sortedFrames.size()-1].time.toMillis() as Number;

        var zeroFrame:KeyFrame;
        if (sortedFrames[0].time == 0s) {
            zeroFrame = sortedFrames[0];
        } else {
            zeroFrame = KeyFrame { time: 0s };
        }

        for (keyFrame in sortedFrames) {
            for (keyValue in keyFrame.values) {
                // TODO: targets should really be Map<KeyValueTarget,List<KFPair>>
                var pairlist: KFPairList = targets.get(keyValue.target) as KFPairList;
                if (pairlist == null) {
                    // New KeyValue: setup its KFPairList & 0-frame if needed
                    pairlist = KFPairList {
                        target: keyValue.target
                    }

                    if (keyFrame.time != 0ms) {
                        // This KeyValue doesn't have an entry in the 0-frame,
                        // so set one up.  Get the current value and attach it
                        // to the zero frame.
                        var kv = KeyValue {
                            target: keyValue.target;
                            var value = keyValue.target.get();
                            value: function() { value }
                        }
                        insert kv into initialKeyValues;
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

    // last processed tick
    var lastTick: Number;
    // the tick where rate or time was changed last time
    var baseTick: Number;
    // total time elapsed in this {@code Timeline} by the {@code baseTick}
    var baseElapsed: Number;
    // tracks any changes of currentRate, either sign or magnitude
    var rateChanged: Boolean;

    /**
     * This routine process all the cases except when timeline
     * is running backward indefinitely. This special case is
     * handled by process_backward_indefinitely().
     */
    function process(currentTick: Number):Void {
	// enforce minimum timelineDur of 1 ms
        var dur = Math.max(timelineDur, 1);
        var totalDur = getTotalDur();

        // if speed or direction changed, update the base values
        if (rateChanged) {
            baseTick = lastTick;
            baseElapsed = lastElapsed;
            rateChanged = false;
        }

        var timeInMillis = time.toMillis() as Number;

        // if position has been modified externally, reposition the playhead
        if(curPos != timeInMillis) {
            // external time change is limited to the current cycle's bounds
            timeInMillis = Math.min(timelineDur, Math.max(timeInMillis, 0));

            // update the base values
            baseTick = lastTick;
            var adjustedMillis = if (forward != isReverse) timeInMillis else dur - timeInMillis;
            if(totalDur < 0) {
                baseElapsed = adjustedMillis;
            } else {
                var curCycle = Math.min(repeatCount - 1, lastElapsed / dur) as Integer;
                baseElapsed = curCycle * dur + adjustedMillis;
            }
            // catch up frames between the current and new positions
            if (not visitFrames(curPos, timeInMillis, true, true)) {
                return;
            }
        }

        // isReverse rewinds elapsed time
        var timeDirection = if (isReverse) -1 else 1;
        var elapsed = baseElapsed + (currentTick - baseTick) * Math.abs(rate) * timeDirection;
        if (totalDur >= 0) {
            elapsed = Math.min(elapsed, totalDur);
        }
        elapsed = Math.max(elapsed, 0);
        lastElapsed = elapsed;
        var needsStop = playedToEnd();

        var curT: Number;
        var cycle: Integer;

        if (timelineDur < 0) {
            // indefinite duration (e.g. will occur when a sub-timeline
            // has indefinite repeatCount); always stay on zero cycle
            curT = elapsed;
            cycle = 0;
        } else {
            curT = elapsed mod dur;
            cycle = elapsed / dur as Integer;
        }

        // check if passed cycle boundary
        if(isReverse) {
            while(cycle < cycleIndex and (repeatCount < 0 or cycleIndex >= 0)) {
                if (not visitCycle(cycleIndex > cycle + 1)) {
                    return;
                }
                cycleIndex --;
            }
        } else {
            while(cycle > cycleIndex and(repeatCount < 0 or cycleIndex < repeatCount)) {
                if (not visitCycle(cycleIndex < cycle - 1)) {
                    return;
                }
                cycleIndex ++;
            }
        }

        var cycleForward = if(isReverse) not forward else forward;

        if((not needsStop) or cycleIndex < repeatCount) {
            var newPos = if(cycleForward) curT
                         else if(timelineDur < 0) elapsed else dur - curT;
            if (not visitFrames(curPos, newPos, false, true)) {
                return;
            }
        }
        lastTick = currentTick;

        if(needsStop) {
            stop();
        }
    }

    function playedToEnd(): Boolean {
        var endReached = false;
            var totalDur = getTotalDur();
        if(isReverse) {
            if(lastElapsed <= 0) {
                endReached = true;
            }
        } else {
            if(totalDur >= 0 and lastElapsed >= totalDur) {
                endReached = true;
            }
        }
        return endReached;
    }

    function doInterpolate(curT: Number) {
        if (interpolate and not targets.isEmpty()) {
            // now handle the active interval for each target
            var iter = targets.values().iterator();
            while (iter.hasNext()) {
                var pairlist = iter.next() as KFPairList;
                var kfpair1 = pairlist.get(0);
                var leftT = kfpair1.frame.time.toMillis() as Number;

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
                    var rightT = kfpair2.frame.time.toMillis() as Number;
                    if (curT < rightT) {
                        v1 = kfpair1.value;
                        v2 = kfpair2.value;
                        segT = (curT - leftT) / (rightT - leftT);
                        break;
                    }
                    kfpair1 = kfpair2;
                    leftT = kfpair1.frame.time.toMillis() as Number;
                }
                if (segT == 0.0 or segT == 1.0) {
                    continue;
                }
                if (v1 != null and v2 != null) {
                    if (v2.interpolate == null) {
                        var v = Interpolator.LINEAR.interpolate(v1.value(),
                                                                v2.value(), segT);
                        pairlist.target.set(v);
                    } else {
                        pairlist.target.set(v2.interpolate.interpolate(v1.value(),
                                                                v2.value(), segT));
                    }
                }
            }
        }

    }

    function visitCycle(catchingUp:Boolean): Boolean  {
        var cycleT = if (forward) timelineDur else 0;
        if (not visitFrames(curPos, cycleT, catchingUp, false)) {
            return false;
        }
        prepareForNextCycle(catchingUp);
        return true;
    }

    function prepareForNextCycle(catchingUp: Boolean) {
        if (not catchingUp and playedToEnd()) {
            return;
        }
        if (autoReverse) {
            forward = not forward;
        } else {
            frameIndex = if (forward) 0 else sortedFrames.size() - 1;
            lastKF = -1;
            movePlayhead(if (forward) 0 else timelineDur);
        }
    }

    // track last visited frame to avoid double visiting it on external time
    // change in the direction opposite to the current value of {@code forward}
    var lastKF = -1;
    var lastKFForward = true;
    /**
     * Play head can be changed inside key frame's action callback,
     * if it is the case, we want to abort and re-evaluate at next
     * pulse.
     */
    function visitFrames(fromTime:Number, toTime:Number, catchingUp:Boolean, visitLast: Boolean) : Boolean {
        var fwd = fromTime <= toTime;
        var aborted = false;

        if (lastKF >= 0) {
            // check if can clear lastKF
            var t = sortedFrames[lastKF].time.toMillis() as Number;
            var reversed = lastKFForward != fwd;
            var farFromLastKF =
                    fwd and (t < fromTime or t > toTime)
                    or
                    not fwd and (t > fromTime or t < toTime);
            if (not reversed and farFromLastKF) {
                // play head moved far from lastKF, can clear it
                lastKF = -1;
            }
        }
        if (fwd) {
            var fromKF = Math.max(0, frameIndex - 1);
            var toKF = sortedFrames.size() - 1;

            for (fi in [fromKF..toKF]) {
                var kf = sortedFrames[fi];
                var kfMillis = kf.time.toMillis() as Number;
                if (kfMillis >= fromTime) {
                    if (kfMillis > toTime) {
                        break;
                    }
                    if (not visitKeyFrame(toTime, fi, kf, catchingUp, visitLast)) {
                        aborted = true;
                        lastTick += Math.abs(kfMillis - fromTime);
                        break;
                    }
                }
            }
        } else {
            var fromKF = Math.min(sortedFrames.size() - 1, frameIndex + 1);
            var toKF = 0;

            for (fi in [fromKF..toKF step -1]) {
                var kf = sortedFrames[fi];
                var kfMillis = kf.time.toMillis() as Number;
                if (kfMillis <= fromTime) {
                    if (kfMillis < toTime) {
                        break;
                    }
                    if (not visitKeyFrame(toTime, fi, kf, catchingUp, visitLast)) {
                        aborted = true;
                        lastTick += Math.abs(kfMillis - fromTime);
                        break;
                    }
                }
            }
        }
        lastKFForward = fwd;
        if (not aborted) {
            movePlayhead(toTime);
        }
        return not aborted;
    }

    function visitKeyFrame(toTime: Number, kfIndex: Integer, kf: KeyFrame,
                           catchingUp: Boolean, visitLast: Boolean): Boolean {
        if (kfIndex != lastKF) { // suppress double visiting on toggle
            frameIndex = kfIndex;
            lastKF = kfIndex;
            var kfMillis = kf.time.toMillis() as Number;

            if (not (catchingUp and kf.canSkip) or visitLast and kfMillis == toTime) {
                movePlayhead(kfMillis);
                var savedCurRate = currentRate;
                kf.visit();
                var timeChanged = curPos != (time.toMillis() as Number);
                if (timeChanged or savedCurRate != currentRate or stopping) {
                    // if time, speed or direction has been changed at the kf's action,
                    // or the timeline has been stopped, abort further visiting
                    return false;
                }
            }
        }
        return true;
    }

    function updateFrameIndex() {
        var fi = frameIndex;
        if (forward) {
            for (i in [0 .. < sortedFrames.size()]) {
                fi = i;
                if (sortedFrames[i].time >= time) {
                    break;
                }
            }
        } else {
            for (i in [sortedFrames.size() - 1 .. 0 step -1]) {
                fi = i;
                if (sortedFrames[i].time <= time) {
                    break;
                }
            }
        }
        frameIndex = fi;
    }

    function updateCurrentRate() {
        currentRate = if (forward) Math.abs(rate) else -Math.abs(rate);
    }

    function createAdapter():TimingTarget {
        TimingTargetAdapter {
            override function begin() : Void {
                running = true;
                paused = false;
                isReverse = false;
                stopping = false;
                cycleIndex = 0;
                forward = (rate >= 0);
                lastKF = -1;

                lastTick = 0.0;
                baseTick = 0.0;

                movePlayhead(Math.min(timelineDur, Math.max(time.toMillis() as Number, 0)) as Number);

                if(forward) {
                    lastElapsed = curPos;
                    /**
                     * If timeline already reaches the end before it even starts,
                     * and intends to move forward, treat it as a completed
                     * forward cycle.
                     */
                    if((time.toMillis() as Number) >= timelineDur) {
                        cycleIndex ++;
                        prepareForNextCycle(false);
                        }
                } else {
                    lastElapsed = timelineDur - curPos;
                    /**
                     * If timeline is at initial position and intends to move backward,
                     * treat it as a completed backward cycle.
                     */
                    if(time <= 0ms) {
                        cycleIndex ++;
                        prepareForNextCycle(false);
                    }
                }
                baseElapsed = lastElapsed;

                updateFrameIndex();
                updateCurrentRate();
            }

            override function timingEvent(fraction, totalElapsed) : Void {
                process(totalElapsed as Number);
            }

            override function pause() : Void {
                paused = true;
                currentRate = 0.0;
            }

            override function resume() : Void {
                paused = false;
                updateCurrentRate();
                }

            override function end() : Void {
                running = false;
                paused = false;
                currentRate = 0.0;
                isReverse = false;

                var dur = getTotalDur();
                if(not playedToEnd() or
                        // INDEFINITE duration timeline can never reach to the end,
                        // must be explicit stop
                        dur < 0) {
                    movePlayhead(0);
                }
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
