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

package com.sun.tools.javafx.ui.animation;

import java.util.*;
import javax.swing.Timer;
import java.lang.ref.*;

public class Animation {

    public static final int MIN_TIMER = 1000/30;

    Timer timer = new Timer(MIN_TIMER, new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent e) {
		flushAnimators();
	    }
	});
    {
	timer.setInitialDelay(0);
    }


    WeakHashMap animators = new WeakHashMap();
    WeakHashMap propertyMap = new WeakHashMap();
    
    void addAnimator(AnimatorImpl a) {
	animators.put(a, null);
	if (animators.size() == 1) {
	    timer.start();
	} 
	flushAnimators();
    }
    
    void removeAnimator(AnimatorImpl a) {
	animators.remove(a);
	if (animators.size() == 0) {
	    timer.stop();
	}
    }

    void flushAnimators() {
	long now = System.currentTimeMillis();
	if (animators.size() > 0) {
	    AnimatorImpl[] arr = new AnimatorImpl[animators.size()];
	    animators.keySet().toArray(arr);
	    for (int i = 0; i < arr.length; i++) {
		arr[i].timerEvent(now);
	    }
	} else {
	    timer.stop();
	}
	long end = System.currentTimeMillis();
	if (end - now > MIN_TIMER) {
	    System.out.println("FLUSH="+(end - now));
	}
    }

    public interface TimePositionListener {
	public void timePositionChanged(double from, double to);
    }

    public interface StateListener {
	public void stateChanged();
    }

    public interface Getter {
	public Object get(Object property);
    }
    
    public interface Evaluator {
	public Object evaluate(Object property,
			       Object previousValue,
			       Object nextValue,
			       double unitTimeInterval);
    }
    
    public interface KeyValue {
	public Object getProperty();
	public Object getValue();
	public Evaluator getEvaluator();
    }

    public interface ActionListener {
        public void doAction();
    }
    
    public interface KeyFrame {
	public long getKeyTime();
	public void setKeyTime(long millis);
	public List<KeyValue> getKeyValues();
        public void setKeyValues(List<KeyValue> list);
	public void setActionListener(ActionListener l);
	public ActionListener getActionListener();
    }

    public interface Reversable {
	public boolean isReverse();
    }

    public interface StoryBoard extends Reversable {
	public void setAutoReverse(boolean value);
	public boolean isAutoReverse();
	public void setRepeatCount(double f);
	public double getRepeatCount();
	public void setKeyFrames(List<KeyFrame> keyFrames);
	public long getDuration();
    }
    
    public interface Animator extends Reversable {
	public void setStartOffset(long dur);
	public long getStartOffset();
	public void setGetter(Getter getter);
	public long getDuration();
	public long getPriority();
	public void setKeyFrames(List<KeyFrame> keyFrames);
	public void setStoryBoards(List<StoryBoard> storyboards);
	public void setPosition(double unitTimeInterval);
	public double getPosition();
	public void setAutoReverse(boolean value);
	public boolean getAutoReverse();
	public double getRepeatCount();
	public void setRepeatCount(double value);
	public void start();
	public void stop();
	public void pause();
	public void resume();
	public void reverse();

	public boolean isToggle();
	public void setToggle(boolean value);
	public boolean isRunning();
	public boolean isPaused();
	public boolean isStopped();
	public StoryBoard createStoryBoard();
	public KeyFrame createKeyFrame();
	public KeyValue createKeyValue(Object property,
				       Object value,
				       Evaluator eval);
	public void addTimePositionListener(TimePositionListener l);
	public void removeTimePositionListener(TimePositionListener l);
	public void addStateListener(StateListener l);
	public void removeStateListener(StateListener l);
    }
    
    


    public class AnimatorImpl implements Animator {
	List<StoryBoard> storyBoards;
	TreeMap<Long, List<KeyFrame> > frameMap = new TreeMap<Long, List<KeyFrame> >();
	Map keyFramesByProperty = new HashMap();
	TreeMap<SpanKey, Span> spanMap = new TreeMap<SpanKey, Span>();
	WeakHashMap positionListeners = new WeakHashMap();
	WeakHashMap stateListeners = new WeakHashMap();
	long duration;
	long activeDuration;
	long currentTime = -1;
	double position;
	long startOffset;

	void startTimer() {
	    addAnimator(this);
	}

	void stopTimer() {
	    removeAnimator(this);
	}

	public void setStartOffset(long d) {
	    startOffset = d;
	}
	
	public long getStartOffset() {
	    return startOffset;
	}

	public KeyFrame createKeyFrame() {
	    return new KeyFrame() {
		long time;
		ActionListener listener;
		List<KeyValue> values = new ArrayList<KeyValue>();
		public void setKeyTime(long n) {
		    time = n;
		}
		public long getKeyTime() {
		    return time;
		}
		public List<KeyValue> getKeyValues() {
		    return values;
		}
		public void setKeyValues(List<KeyValue> list) {
                    values.clear();
		    values.addAll(list);
		}
		public void setActionListener(ActionListener l) {
		    listener = l;
		}
		public ActionListener getActionListener() {
		    return listener;
		}
	    };
	}

	public KeyValue createKeyValue(final Object p,
				       final Object value,
				       final Evaluator evaluator) {
	    return new KeyValue() {
		public Object getProperty() {return p;}
		public Object getValue() {return value;}
		public Evaluator getEvaluator() { return evaluator; }
	    };
	}

	double repeatCount = 0;
	double repeats;
	
	public void setRepeatCount(double value) {
	    repeatCount = value;
	}
	
	public double getRepeatCount() {
	    return repeatCount;
	}

	final int RUNNING = 1;
	final int PAUSED = 2;
	final int IDLE = 3;
	long priority;
	int state = IDLE;
	boolean isReverse;
	boolean autoReverse;
	boolean toggle;
	Getter getter;

	public boolean isReverse() {
	    return isReverse;
	}

	public void setToggle(boolean value) {
	    toggle = value;
	}
	
	public boolean isToggle() {
	    return toggle;
	}

	public long getPriority() { return priority; }

	public void setAutoReverse(boolean value) {
	    autoReverse = value;
	}


	public boolean getAutoReverse() {
	    return autoReverse;
	}

	public boolean isRunning() {
	    return state == RUNNING;
	}

	public boolean isPaused() {
	    return state == PAUSED;
	}

	public boolean isStopped() {
	    return state == IDLE;
	}

	public void setGetter(Getter getter) {
	    this.getter = getter;
	}

	public void start() {
	    stop();	    
	    position = -1;
	    priority = System.currentTimeMillis();
	    run();
	}

	public void stop() {
	    stopTimer();
	    state = IDLE;
	    startTime = -1;
	    currentTime = -1;
	    repeats = 0;
	    if (!toggle) {
		isReverse = false;
	    }
	    fireStateChange();
	}

	public void pause() {
	    if (state == RUNNING) {
		stopTimer();
		fireStateChange();
	    }
	    state = PAUSED;
	}

	public void resume() {
	    if (state == PAUSED || state == IDLE) {
		state = RUNNING;
		startTimer();
		fireStateChange();
	    }
	}

	public void reverse() {
	    if (isRunning()) {
		isReverse = !isReverse;
		long now = System.currentTimeMillis();
		long elapsed = now - startTime;
		startTime = now - (duration - elapsed);
		fireStateChange();
	    } else {
		priority = System.currentTimeMillis();
		isReverse = !isReverse;
		position = -1;
		System.out.println("REVERSE = " + isReverse);
		run();
	    }
	}

	long startTime;

	void timerEvent(long now) {
	    long elapsed = 0;
	    boolean force = false;
	    if (position < 0) {
		position = 0;
		force = true;
		startTime = now;
		for (Span span : spanMap.values()) {
		    span.init(getter);
		}
		if (storyBoards != null) {
		    for (StoryBoard s : storyBoards) {
			StoryBoardImpl si = (StoryBoardImpl)s;
			si.init(getter);
		    }
		}
	    } else {
		elapsed = now - startTime;
	    }
	    double t = (double)elapsed/(double)activeDuration;
	    if (isReverse) {
		t = 1-t;
	    }
	    setPositionInternal(t, force);
	    if (elapsed >= activeDuration) {
		if (repeatCount > 0) {
		    double r = ++repeats;
		    double rem = repeatCount -r;
		    if (rem >= 0) {
			if (repeatCount == Double.POSITIVE_INFINITY) {
			    rem = 0;
			}
			startTime = -1;
			currentTime = -1;
			position = -1;
			if (rem > 0) {
			    //activeDuration = (long) ((double)duration * rem);
			}
			if (autoReverse) {
			    isReverse = !isReverse;
			}
			if (storyBoards != null) {
			    for (StoryBoard s : storyBoards) {
				((StoryBoardImpl)s).restart();
			    }
			}
			return;
		    }
		}
		System.out.println("stopping: " + elapsed + " dur " + duration + " t = " + t);
		stop();
	    }
	}

	public void run() {
	    state = RUNNING;
	    repeats = 0;
	    startTime = -1;
	    currentTime = 0;
	    activeDuration = duration;
	    fireStateChange();
	    startTimer();
	}

	boolean acquire(Object property) {
	    //System.out.println("map = " + propertyMap);
	    WeakReference owner = (WeakReference)propertyMap.get(property);
	    if (owner != null) {
		Animator a = (Animator)owner.get();
		if (a != this) {
		    if (a != null) {
			if (a.getPriority() < this.getPriority()) {
			    System.out.println("You're canceled by: " + property);
			    
			    a.stop();
			} else {
			    return false;
			}
		    }
		    propertyMap.put(property, new WeakReference(this));
		}
	    } else {
		propertyMap.put(property, new WeakReference(this));
	    }
	    //System.out.println(this + " aquired " + property);
	    return true;
	}

	Object evaluate1(KeyValue kv, Object oldValue,
		       Object newValue, double t) {
	    Object p = kv.getProperty();
	    Evaluator e = kv.getEvaluator();
	    if (acquire(p)) {
		Object value = e.evaluate(p, oldValue, newValue, t);
		return value;
	    }
	    return null;
	}

	class Span {
	    Reversable owner;
	    KeyFrame start;
	    KeyFrame end;
	    List<KeyValue> keyValues = new ArrayList<KeyValue>();
	    Map<Object, Object> initialValues;
	    List<KeyFrame> startKeyFrames = new ArrayList<KeyFrame>();
	    List<KeyFrame> endKeyFrames = new ArrayList<KeyFrame>();

	    
	    Span(Reversable owner, KeyFrame kstart, KeyFrame kend) {
                this.owner = owner;
		start = kstart;
		end = kend;
		System.out.println("new span: " + 
				   (start == null ? 0 : 
				    start.getKeyTime()) + 
				   " " + end.getKeyTime());
	    }

	    void addEnd(KeyFrame k) {
		if (!endKeyFrames.contains(k)) {
		    endKeyFrames.add(k);
		}
	    }

	    void addStart(KeyFrame k) {
		if (k != null && !startKeyFrames.contains(k)) {
		    startKeyFrames.add(k);
		}
	    }
	    
	    public void init(Getter getter) {
		if (start == null) {
		    if (initialValues != null) {
			for (Object key : initialValues.keySet()) {
			    Object value = getter.get(key);
			    //System.out.println("initial value of " + key + " = " + value);
			    initialValues.put(key, value);
			}
		    }
		}
	    }
	    
	    public void add(KeyValue kv) {
		keyValues.add(kv);
		if (start == null) {
		    if (initialValues == null) {
			initialValues = new HashMap();
		    }
		    initialValues.put(kv.getProperty(), null);
		}
	    }

	    public long getStartTime() {
		long value = start == null ? 0 : start.getKeyTime() ;
		return value;
	    }

	    public long getEndTime() {
		long value = end.getKeyTime();
		return value;
	    }
	    
	    public void evaluate(long lastTime,
				 long currentTime) {
		double then = lastTime;
		double now = currentTime;
		double s = getStartTime();
		double e = getEndTime();
		double span = e-s;
		double t = 1;
		if (span != 0) {
		    t = (now - s) / span;
		} 
		// make sure we hit the key frame
		if (then < now) {
		    if (e > then && e < now) {
			//System.out.println("FORCING KEY FRAME "+then + " " + e + " " + now);
			evaluate(getEndTime(), getEndTime());
		    } 
		} else if (owner.isReverse() && now < then) {
		    if (e > now && e < then) {
			//System.out.println("FORCING KEY FRAME "+then + " " + e + " " + now);
			evaluate(getEndTime(), getEndTime());
			// hit zero also for discrete update?
			//evaluate(getStartTime(), getStartTime());
		    }
		}
		if (currentTime < s || currentTime > e) {
		    return;
		}
		if (t < 0 || t > 1) {
		    return;
		}
		if (span == 0) {
		    //System.out.println("hit zero key frame "+ t);
		}
		//System.out.println("evaluating span: " + getStartTime() + " to " + getEndTime());
		for (KeyValue kv : keyValues) {
		    Object p = kv.getProperty();
		    if (!acquire(p)) {
			System.out.println("I'm canceled by: " + p);
			stop();
			return;
		    }
		}
		for (KeyValue kv : keyValues) {
		    Object newValue = kv.getValue();
		    Object oldValue = newValue;
		    Object p = kv.getProperty();
		    boolean found = false;
		    if (startKeyFrames.size() > 0) {
			for (KeyFrame kf : startKeyFrames) {
			    for (KeyValue jv : kf.getKeyValues()) {
				if (jv.getProperty().equals(p)) {
				    oldValue = jv.getValue();
				    found = true;
				    break;
				}
			    }
			}
		    }
		    if (!found) {
			if (initialValues == null) {
			    //System.out.println("NO INITIAL VALUE FOUND FOR " + p + " in " + getStartTime() + " " + getEndTime());
			} else {
			    oldValue = initialValues.get(p);
			}
		    }
		    Object result = evaluate1(kv, oldValue, newValue, (double)t);
		    //System.out.println("evaluating " + p + " = " + result + " oldValue = " + oldValue);
		}
		if (t == 1.0) {
		    for (KeyFrame kf : endKeyFrames) {
			ActionListener l = kf.getActionListener();
			if (l != null) {
			    l.doAction();
			}
		    }
		}
	    }
	}
	
	class SpanKey implements Comparable {
	    long start;
	    long end;
	    public int compareTo(Object o) {
		SpanKey k = (SpanKey)o;
		long cmp = start - k.start;
		if (cmp != 0) return cmp < 0 ? -1 : 1;
		cmp = end - k.end;
		return cmp < 0 ? -1 : cmp > 0 ? 1 : 0;
	    }

	    public boolean equals(Object o) {
		return compareTo(o) == 0;
	    }

	    SpanKey(long start, long end) {
		this.start = start;
		this.end = end;
	    }

	    public String toString() {
		return "start="+start+", end="+end;
	    }
	}
	
	Span getSpan(Reversable owner, KeyFrame kstart, KeyFrame kend) {
	    long start = 0;
	    if (kstart != null) {
		start = kstart.getKeyTime();
	    }
	    long end = kend.getKeyTime();
	    SpanKey key = new SpanKey(start, end);
		Span span = spanMap.get(key);
	    if (span == null) {
		span = new Span(owner, kstart, kend);
		spanMap.put(key, span);
	    }
	    span.addStart(kstart);
	    span.addEnd(kend);
	    return span;
	}

	class StoryBoardImpl implements StoryBoard {

	    TreeMap<Long, List<KeyFrame> > frameMap = new TreeMap<Long, List<KeyFrame> >();
	    TreeMap<SpanKey, Span> spanMap = new TreeMap<SpanKey, Span>();
	    long duration = 0;
	    double repeatCount;
	    boolean isStoryBoardAutoReverse;
	    boolean isStoryBoardReverse;

	    public void setAutoReverse(boolean value) {
		isStoryBoardAutoReverse = value;
	    }

	    public boolean isAutoReverse() {
		return isStoryBoardAutoReverse;
	    }

	    public boolean isReverse() {
		return isStoryBoardReverse;
	    }

	    public void setRepeatCount(double v) {
		repeatCount = v;
	    }

	    public double getRepeatCount() {
		return repeatCount;
	    }

	    public void setKeyFrames(List<KeyFrame> keyFrames) {
		spanMap.clear();
		frameMap.clear();
		Map<Object,KeyFrame> map = new HashMap<Object, KeyFrame>();
		for (KeyFrame k : keyFrames) {
		    List<KeyFrame> list = frameMap.get(k.getKeyTime());
		    if (list == null) {
			list = new ArrayList<KeyFrame>();
			frameMap.put(k.getKeyTime(), list);
		    }
		    list.add(k);
		    if (k.getKeyValues().size() == 0) {
			getSpan(this, null, k);
		    } else {
			for (KeyValue v : k.getKeyValues()) {
			    Object p = v.getProperty();
			    KeyFrame prev = map.get(p);
			    Span span = getSpan(this, prev, k);
			    span.add(v);
			    map.put(p, k);
			}
		    }
		}
		if (frameMap.size() == 0) {
		    duration = 0;
		} else {
		    duration = (long)frameMap.lastKey();
		}
	    }

	    Span getSpan(Reversable owner, KeyFrame kstart, KeyFrame kend) {
		long start = 0;
		if (kstart != null) {
		    start = kstart.getKeyTime();
		}
		long end = kend.getKeyTime();
		SpanKey key = new SpanKey(start, end);
		Span span = spanMap.get(key);
		if (span == null) {
		    span = new Span(owner, kstart, kend);
		    spanMap.put(key, span);
		}
		span.addStart(kstart);
		span.addEnd(kend);
		return span;
	    }

	    long myOff = 0;
	    double myRepeats;

	    public void restart() {
		myOff += AnimatorImpl.this.getDuration(); 
	    }

	    public void evaluate(long lastTime, long currentTime) {
		lastTime += myOff;
		currentTime += myOff;
		if (lastTime > duration) {
		    if (duration != 0) {
			double repeats = (double)currentTime / (double)duration;
			repeats = Math.floor(repeats); // TBD: fractional repeats
			if (repeats >= repeatCount) {
			    return;
			}
			if (repeats > myRepeats) {
			    myRepeats = repeats;
			    if (isStoryBoardAutoReverse) {
				isStoryBoardReverse = !isStoryBoardReverse;
			    }
			}
			lastTime %= duration;
			currentTime %= duration;
		    }
		}
		if (isStoryBoardReverse) {
		    currentTime = duration - currentTime;
		    lastTime = duration - lastTime;
		}
		for (Span span: spanMap.values()) {
		    span.evaluate(lastTime, currentTime);
		}
	    }

	    public void init(Getter getter) {
		for (Span span: spanMap.values()) {
		    span.init(getter);
		}
	    }

	    public long getDuration() {
		return duration;
	    }
	}

	public void setStoryBoards(List<StoryBoard> storyBoards) {
	    this.storyBoards = storyBoards;
	    duration = 0;
	    for (StoryBoard s : storyBoards) {
		duration = Math.max(duration, s.getDuration());
	    }
	}

	public void setKeyFrames(List<KeyFrame> keyFrames) {
	    stop();
	    long max = Long.MIN_VALUE;
	    if (storyBoards != null) {
		for (StoryBoard s : storyBoards) {
		    max = Math.max(max, s.getDuration());
		}
	    }
	    spanMap.clear();
	    frameMap.clear();
	    KeyFrame last = null;
	    boolean sawNeg = false;
	    for (KeyFrame k : keyFrames) {
		long t = k.getKeyTime() - startOffset;
		if (t < 0) {
		    sawNeg = true;
		}
		if (t > max) {
		    max = t;
		    last = k;
		}
	    }
	    //System.out.println("max="+max);
	    // hack
	    if (startOffset > 0) {
		List<KeyFrame> newList = new ArrayList<KeyFrame>();
		for (KeyFrame k : keyFrames) {
		    long t = k.getKeyTime() - startOffset;
		    if (t < 0) {
			t = max + t;
		    } else if (t == 0) {
			// clone it
			KeyFrame nk = createKeyFrame();
			nk.setKeyTime(max);
			nk.getKeyValues().addAll(k.getKeyValues());
			newList.add(nk);
		    }
		    //System.out.println("reset " + k.getKeyTime() + " to " + t);
		    k.setKeyTime(t);
		    newList.add(k);
		}
		keyFrames = newList;
	    }
	    for (KeyFrame k : keyFrames) {
		List<KeyFrame> list = frameMap.get(k.getKeyTime());
		if (list == null) {
		    list = new ArrayList<KeyFrame>();
		    frameMap.put(k.getKeyTime(), list);
		}
		list.add(k);
	    }
	    Map<Object,KeyFrame> map = new HashMap<Object, KeyFrame>();
	    for (List<KeyFrame> lk : frameMap.values()) {
		for (KeyFrame k : lk) {
		    if (k.getKeyValues().size() == 0) {
			getSpan(this, null, k);
		    } else {
			for (KeyValue v : k.getKeyValues()) {
			    Object p = v.getProperty();
			    KeyFrame prev = map.get(p);
			    Span span = getSpan(this, prev, k);
			    if (prev != null) {
				//System.out.println("p="+p);
				//System.out.println("prev = " + prev.getKeyTime() + " k =" + k.getKeyTime());
				//System.out.println("span = " + span);
			    }
			    span.add(v);
			    map.put(p, k);
			}
		    }
		}
	    }
	    //System.out.println("spanMap="+spanMap.keySet());
	    duration = max;
	    activeDuration = duration;
	}

	public long getDuration() {
	    return duration;
	}

	public double getPosition() {
	    return position;
	}

	public void setPosition(double unit) {
	    if (state != IDLE && state != PAUSED) {
		stop();
	    }
	    if (position < 0) {

		    for (Span span : spanMap.values()) {
			span.init(getter);
		    }
		    if (storyBoards != null) {
			for (StoryBoard s : storyBoards) {
			    StoryBoardImpl si = (StoryBoardImpl)s;
			    si.init(getter);
			}
		    }

		position = 0;
	    }
	    setPositionInternal(unit, true);
	}
	
	void setPositionInternal(double unit, boolean force) {
	    if (!force && (position == unit)) {
		return;
	    }
	    double old = position;
	    position = unit;
	    long lastTime = (long) (old * (double)activeDuration);
	    currentTime = (long) (unit * (double)activeDuration);
	    long t = currentTime;
	    for (Span span : spanMap.values()) {
		span.evaluate(lastTime, t);
	    }
	    if (storyBoards != null) {
		for (StoryBoard s : storyBoards) {
		    StoryBoardImpl si = (StoryBoardImpl)s;
		    si.evaluate(lastTime, t);
		}
	    }
	    firePositionChange(old, unit);
	}

	public void addTimePositionListener(TimePositionListener t) {
	    positionListeners.put(t, null);
	}

	public void removeTimePositionListener(TimePositionListener t) {
	    positionListeners.remove(t);
	}

	public void addStateListener(StateListener t) {
	    stateListeners.put(t, null);
	}

	public void removeStateListener(StateListener t) {
	    stateListeners.remove(t);
	}

	void fireStateChange() {
	    if (stateListeners.size() > 0) {
		StateListener[] arr = 
		    new StateListener[stateListeners.size()];
		stateListeners.keySet().toArray(arr);
		for (int i = 0; i < arr.length; i++) {
		    arr[i].stateChanged();
		}
	    }
	}
	
	void firePositionChange(double oldValue, double newValue) {
	    if (positionListeners.size() > 0) {
		TimePositionListener[] arr = 
		    new TimePositionListener[positionListeners.size()];
		positionListeners.keySet().toArray(arr);
		for (int i = 0; i < arr.length; i++) {
		    arr[i].timePositionChanged(oldValue, newValue);
		}
	    }
	}

	public StoryBoard createStoryBoard() {
	    return new StoryBoardImpl();
	}
    }

    public Animator createAnimator() {
	return new AnimatorImpl();
    }

}
