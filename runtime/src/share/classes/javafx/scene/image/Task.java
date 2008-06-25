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

package javafx.scene.image;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.annotation.Target;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
// import javax.swing.SwingWorker; see bug 6684841
// import javax.swing.SwingWorker.StateValue;
import java.util.logging.Level;
import java.util.logging.Logger;


/* This class is a subset of the Task class from JSR-296, 
 * see org.jdesktop.application.Task in appframework.dev.java.net.
 */

abstract class Task extends SwingWorker<Object, Void> {

    private static final Logger logger = Logger.getLogger(Task.class.getName());

    public Task() {
        addPropertyChangeListener(new StatePCL());
    }

    /**
     * A convenience method that sets the {@code progress} property to the following
     * ratio normalized to 0 .. 100.
     * <pre>
     * value - min / max - min
     * </pre>
     * 
     * @param value a value in the range min ... max, inclusive
     * @param min the minimum value of the range
     * @param max the maximum value of the range
     * @see #setProgress(int)
     */
    protected final void setProgress(int value, int min, int max) {
	if (min >= max) {
	    throw new IllegalArgumentException("invalid range: min >= max");
	}
	if ((value < min) || (value > max)) {
	    throw new IllegalArgumentException("invalid value");
	}
	float percentage = (float)(value - min) / (float)(max - min);
	setProgress(Math.round(percentage * 100.0f));
    }

    /**
     * A convenience method that sets the {@code progress} property to 
     * <code>percentage * 100</code>.
     * 
     * @param percentage a value in the range 0.0 ... 1.0 inclusive
     * @see #setProgress(int)
     */
    protected final void setProgress(float percentage) {
	if ((percentage < 0.0) || (percentage > 1.0)) {
	    throw new IllegalArgumentException("invalid percentage");
	}
	setProgress(Math.round(percentage * 100.0f));
    }

    /**
     * A convenience method that sets the {@code progress} property to the following
     * ratio normalized to 0 .. 100.
     * <pre>
     * value - min / max - min
     * </pre>
     * 
     * @param value a value in the range min ... max, inclusive
     * @param min the minimum value of the range
     * @param max the maximum value of the range
     * @see #setProgress(int)
     */
    protected final void setProgress(float value, float min, float max) {
	if (min >= max) {
	    throw new IllegalArgumentException("invalid range: min >= max");
	}
	if ((value < min) || (value > max)) {
	    throw new IllegalArgumentException("invalid value");
	}
	float percentage = (value - min) / (max - min);
	setProgress(Math.round(percentage * 100.0f));
    }

    /**
     * Equivalent to {@code getState() == StateValue.PENDING}.  
     * <p> 
     * When a pending Task's state changes to {@code StateValue.STARTED} 
     * a PropertyChangeEvent for the "started" property is fired.  Similarly
     * when a started Task's state changes to {@code StateValue.DONE}, a
     * "done" PropertyChangeEvent is fired.
     */
    public final boolean isPending() {
	return getState() == StateValue.PENDING;
    }

    /**
     * Equivalent to {@code getState() == StateValue.STARTED}.  
     * <p> 
     * When a pending Task's state changes to {@code StateValue.STARTED} 
     * a PropertyChangeEvent for the "started" property is fired.  Similarly
     * when a started Task's state changes to {@code StateValue.DONE}, a
     * "done" PropertyChangeEvent is fired.
     */
    public final boolean isStarted() {
	return getState() == StateValue.STARTED;
    }

    @Override protected final void done() {
	try {
	    if (isCancelled()) {
		cancelled();
	    } 
	    else {
		try {
		    succeeded(get());
		} 
		catch (InterruptedException e) {
		    interrupted(e);
		}
		catch (ExecutionException e) {
		    failed(e.getCause());
		}
	    }
	}
	finally {
	    try {
		finished();
	    }
	    finally {
		// setTaskService(null);
	    }
	}
    }

    /**
     * Called when this Task has been cancelled by {@link #cancel(boolean)}.
     * <p>
     * This method runs on the EDT.  It does nothing by default.
     * 
     * @see #done
     */
    protected void cancelled() {
    }

    /**
     * Called when this Task has successfully completed, i.e. when 
     * its {@code get} method returns a value.  Tasks that compute
     * a value should override this method.
     * <p>
     * <p>
     * This method runs on the EDT.  It does nothing by default.
     *
     * @param result the value returned by the {@code get} method
     * @see #done
     * @see #get
     * @see #failed
     */
    protected void succeeded(Object result) {
    }

    /**
     * Called if the Task's Thread is interrupted but not
     * explicitly cancelled. 
     * <p>
     * This method runs on the EDT.  It does nothing by default.
     *
     * @param e the {@code InterruptedException} thrown by {@code get}
     * @see #cancel
     * @see #done
     * @see #get
     */
    protected void interrupted(InterruptedException e) {
    }

    /**
     * Called when an execution of this Task fails and an 
     * {@code ExecutionExecption} is thrown by {@code get}.
     * <p>
     * This method runs on the EDT.  It Logs an error message by default.
     *
     * @param cause the {@link Throwable#getCause cause} of the {@code ExecutionException}
     * @see #done
     * @see #get
     * @see #failed
     */
    protected void failed(Throwable cause) {
	String msg = String.format("%s failed: %s", this, cause);
	logger.log(Level.SEVERE, msg, cause);
    } 

    /**
     * Called unconditionally (in a {@code finally} clause) after one
     * of the completion methods, {@code succeeded}, {@code failed},
     * {@code cancelled}, or {@code interrupted}, runs.  Subclasses
     * can override this method to cleanup before the {@code done}
     * method returns.
     * <p>
     * This method runs on the EDT.  It does nothing by default.
     *
     * @see #done
     * @see #get
     * @see #failed
     */
    protected void finished() {
    }

    private class StatePCL implements PropertyChangeListener {
	public void propertyChange(PropertyChangeEvent e) {
	    String propertyName = e.getPropertyName();
	    if ("state".equals(propertyName)) {
		StateValue state = (StateValue)(e.getNewValue());
		Task task = (Task)(e.getSource());
		switch (state) {
		case STARTED: taskStarted(task); break;
		case DONE: taskDone(task); break;
		}
	    }
	}
	private void taskStarted(Task task) {
	    firePropertyChange("started", false, true);
	}
	private void taskDone(Task task) {
	    try {
		task.removePropertyChangeListener(this);
		firePropertyChange("done", false, true);
	    }
	    finally {
		firePropertyChange("completed", false, true);
	    }
	}
    }

}
