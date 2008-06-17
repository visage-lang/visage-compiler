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
package javafx.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

import java.awt.event.*;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

abstract class AccumulativeRunnable<T> implements Runnable {
    private List<T> arguments = null;
    
    /**
     * Equivalent to {@code Runnable.run} method with the
     * accumulated arguments to process.
     *
     * @param args accumulated argumets to process.
     */
    protected abstract void run(List<T> args);
    
    /**
     * {@inheritDoc}
     *
     * <p>
     * This implementation calls {@code run(List<T> args)} mehtod
     * with the list of accumulated arguments.
     */
    public final void run() {
        run(flush());
    }
    
    /**
     * appends arguments and sends this {@cod Runnable} for the
     * execution if needed.
     * <p>
     * This implementation uses {@see #submit} to send this 
     * {@code Runnable} for execution. 
     * @param args the arguments to accumulate
     */
    public final synchronized void add(T... args) {
        boolean isSubmitted = true;
        if (arguments == null) {
            isSubmitted = false;
            arguments = new ArrayList<T>();
        }
        Collections.addAll(arguments, args);
        if (!isSubmitted) {
            submit();
        }
    }

    /**
     * Sends this {@code Runnable} for the execution
     *
     * <p>
     * This method is to be executed only from {@code add} method.
     *
     * <p>
     * This implementation uses {@code SwingWorker.invokeLater}.
     */
    protected void submit() {
        SwingUtilities.invokeLater(this);
    }
        
    /**
     * Returns accumulated arguments and flashes the arguments storage.
     *
     * @return accumulated arguments
     */
    private final synchronized List<T> flush() {
        List<T> list = arguments;
        arguments = null;
        return list;
    }
}



abstract class SwingWorker<T, V> implements Runnable /* implements RunnableFuture<T> see bug 6684841 */ {
    /**
     * number of worker threads.
     */
    private static final int MAX_WORKER_THREADS = 10;

    /**
     * current progress.
     */
    private volatile int progress;

    /**
     * current state.
     */
    private volatile StateValue state;

    /**
     * everything is run inside this FutureTask. Also it is used as
     * a delegatee for the Future API.
     */
    private final FutureTask<T> future;

    /**
     * all propertyChangeSupport goes through this.
     */
    private final PropertyChangeSupport propertyChangeSupport;

    /**
     * handler for {@code process} mehtod.
     */
    private AccumulativeRunnable<V> doProcess;

    /**
     * handler for progress property change notifications.
     */
    private AccumulativeRunnable<Integer> doNotifyProgressChange;

    private final AccumulativeRunnable<Runnable> doSubmit = new DoSubmitAccumulativeRunnable();

    /**
     * Values for the {@code state} bound property.
     * @since 1.6
     */
    public enum StateValue {
        /**
         * Initial {@code SwingWorker} state.
         */
        PENDING,
        /**
         * {@code SwingWorker} is {@code STARTED} 
         * before invoking {@code doInBackground}.
         */
        STARTED,

        /**
         * {@code SwingWorker} is {@code DONE}
         * after {@code doInBackground} method
         * is finished.
         */
        DONE
    };

    /**
     * Constructs this {@code SwingWorker}.
     */
    public SwingWorker() {
        Callable<T> callable = 
                new Callable<T>() {
                    public T call() throws Exception {
                        setState(StateValue.STARTED);
                        return doInBackground();
                    }
                };

        future = new FutureTask<T>(callable) {
                       @Override
                       protected void done() {
                           doneEDT();
                           setState(StateValue.DONE);
                       }
                   };

       state = StateValue.PENDING;
       propertyChangeSupport = new SwingWorkerPropertyChangeSupport(this);
       doProcess = null;
       doNotifyProgressChange = null;
    }
    
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * <p>
     * Note that this method is executed only once.
     * 
     * <p>
     * Note: this method is executed in a background thread.
     *  
     *
     * @return the computed result
     * @throws Exception if unable to compute a result
     * 
     */
    protected abstract T doInBackground() throws Exception ;
    
    /**
     * Sets this {@code Future} to the result of computation unless
     * it has been cancelled.
     */
    public final void run() {
        future.run();
    }
    
    /**
     * Sends data chunks to the {@link #process} method. This method is to be
     * used from inside the {@code doInBackground} method to deliver 
     * intermediate results
     * for processing on the <i>Event Dispatch Thread</i> inside the
     * {@code process} method.
     * 
     * <p>
     * Because the {@code process} method is invoked asynchronously on
     * the <i>Event Dispatch Thread</i>
     * multiple invocations to the {@code publish} method
     * might occur before the {@code process} method is executed. For
     * performance purposes all these invocations are coalesced into one
     * invocation with concatenated arguments.
     * 
     * <p>
     * For example:
     * 
     * <pre>
     * publish(&quot;1&quot;);
     * publish(&quot;2&quot;, &quot;3&quot;);
     * publish(&quot;4&quot;, &quot;5&quot;, &quot;6&quot;);
     * </pre>
     * 
     * might result in:
     * 
     * <pre>
     * process(&quot;1&quot;, &quot;2&quot;, &quot;3&quot;, &quot;4&quot;, &quot;5&quot;, &quot;6&quot;)
     * </pre>
     *
     * <p>
     * <b>Sample Usage</b>. This code snippet loads some tabular data and
     * updates {@code DefaultTableModel} with it. Note that it safe to mutate
     * the tableModel from inside the {@code process} method because it is 
     * invoked on the <i>Event Dispatch Thread</i>.
     * 
     * <pre>
     * class TableSwingWorker extends 
     *         SwingWorker&lt;DefaultTableModel, Object[]&gt; {
     *     private final DefaultTableModel tableModel;
     * 
     *     public TableSwingWorker(DefaultTableModel tableModel) {
     *         this.tableModel = tableModel;
     *     }
     * 
     *     {@code @Override}
     *     protected DefaultTableModel doInBackground() throws Exception {
     *         for (Object[] row = loadData(); 
     *                  ! isCancelled() &amp;&amp; row != null; 
     *                  row = loadData()) {
     *             publish((Object[]) row);
     *         }
     *         return tableModel;
     *     }
     * 
     *     {@code @Override}
     *     protected void process(List&lt;Object[]&gt; chunks) {
     *         for (Object[] row : chunks) {
     *             tableModel.addRow(row);
     *         }
     *     }
     * }
     * </pre>
     * 
     * @param chunks intermediate results to process
     * 
     * @see #process
     * 
     */
    protected final void publish(V... chunks) {
        synchronized (this) {
            if (doProcess == null) {
                doProcess = new AccumulativeRunnable<V>() {
                    @Override
                    public void run(List<V> args) {
                        process(args);
                    }
                    @Override
                    protected void submit() {
                        doSubmit.add(this);
                    }
                };
            }
        }
        doProcess.add(chunks);
    }

    /**
     * Receives data chunks from the {@code publish} method asynchronously on the
     * <i>Event Dispatch Thread</i>.
     * 
     * <p>
     * Please refer to the {@link #publish} method for more details.
     * 
     * @param chunks intermediate results to process
     * 
     * @see #publish
     * 
     */
    protected void process(List<V> chunks) {
    }

    /**
     * Executed on the <i>Event Dispatch Thread</i> after the {@code doInBackground}
     * method is finished. The default
     * implementation does nothing. Subclasses may override this method to
     * perform completion actions on the <i>Event Dispatch Thread</i>. Note
     * that you can query status inside the implementation of this method to
     * determine the result of this task or whether this task has been cancelled.
     * 
     * @see #doInBackground
     * @see #isCancelled()
     * @see #get
     */
    protected void done() {
    }

    /**
     * Sets the {@code progress} bound property.
     * The value should be from 0 to 100.
     *
     * <p>
     * Because {@code PropertyChangeListener}s are notified asynchronously on
     * the <i>Event Dispatch Thread</i> multiple invocations to the
     * {@code setProgress} method might occur before any
     * {@code PropertyChangeListeners} are invoked. For performance purposes
     * all these invocations are coalesced into one invocation with the last
     * invocation argument only.
     * 
     * <p>
     * For example, the following invokations:
     * 
     * <pre>
     * setProgress(1);
     * setProgress(2);
     * setProgress(3);
     * </pre>
     * 
     * might result in a single {@code PropertyChangeListener} notification with
     * the value {@code 3}.
     * 
     * @param progress the progress value to set
     * @throws IllegalArgumentException is value not from 0 to 100
     */
    protected final void setProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("the value should be from 0 to 100");
        }
        if (this.progress == progress) {
            return;
        }
        int oldProgress = this.progress;
        this.progress = progress;
        if (! getPropertyChangeSupport().hasListeners("progress")) {
            return;
        }
        synchronized (this) {
            if (doNotifyProgressChange == null) {
                doNotifyProgressChange = 
                    new AccumulativeRunnable<Integer>() {
                        @Override
                        public void run(List<Integer> args) {
                            firePropertyChange("progress", 
                               args.get(0), 
                               args.get(args.size() - 1));
                        }
                        @Override
                        protected void submit() {
                            doSubmit.add(this);
                        }
                    };
            }
        }
        doNotifyProgressChange.add(oldProgress, progress);
    }

    /**
     * Returns the {@code progress} bound property.
     * 
     * @return the progress bound property.
     */
    public final int getProgress() {
        return progress;
    }

    /**
     * Schedules this {@code SwingWorker} for execution on a <i>worker</i>
     * thread. There are a number of <i>worker</i> threads available. In the
     * event all <i>worker</i> threads are busy handling other
     * {@code SwingWorkers} this {@code SwingWorker} is placed in a waiting
     * queue.
     *
     * <p>
     * Note:
     * {@code SwingWorker} is only designed to be executed once.  Executing a
     * {@code SwingWorker} more than once will not result in invoking the
     * {@code doInBackground} method twice.
     */
    public final void execute() {
        getWorkersExecutorService().execute(this);
    }

    // Future methods START
    /**
     * {@inheritDoc}
     */
    public final boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isCancelled() {
        return future.isCancelled();
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isDone() {
        return future.isDone();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Note: calling {@code get} on the <i>Event Dispatch Thread</i> blocks
     * <i>all</i> events, including repaints, from being processed until this
     * {@code SwingWorker} is complete.
     * 
     * <p>
     * When you want the {@code SwingWorker} to block on the <i>Event
     * Dispatch Thread</i> we recommend that you use a <i>modal dialog</i>.
     *
     * <p>
     * For example:
     * 
     * <pre>
     * class SwingWorkerCompletionWaiter extends PropertyChangeListener {
     *     private JDialog dialog;
     * 
     *     public SwingWorkerCompletionWaiter(JDialog dialog) {
     *         this.dialog = dialog;
     *     }
     * 
     *     public void propertyChange(PropertyChangeEvent event) {
     *         if (&quot;state&quot;.equals(event.getPropertyName())
     *                 &amp;&amp; SwingWorker.StateValue.DONE == event.getNewValue()) {
     *             dialog.setVisible(false);
     *             dialog.dispose();
     *         }
     *     }
     * }
     * JDialog dialog = new JDialog(owner, true);
     * swingWorker.addPropertyChangeListener(
     *     new SwingWorkerCompletionWaiter(dialog));
     * swingWorker.execute();
     * //the dialog will be visible until the SwingWorker is done
     * dialog.setVisible(true); 
     * </pre>
     */
    public final T get() throws InterruptedException, ExecutionException {
        return future.get();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Please refer to {@link #get} for more details.
     */
    public final T get(long timeout, TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException {
        return future.get(timeout, unit);
    }

    // Future methods END

    // PropertyChangeSupports methods START
    /**
     * Adds a {@code PropertyChangeListener} to the listener list. The listener
     * is registered for all properties. The same listener object may be added
     * more than once, and will be called as many times as it is added. If
     * {@code listener} is {@code null}, no exception is thrown and no action is taken.
     * 
     * <p>
     * Note: This is merely a convenience wrapper. All work is delegated to
     * {@code PropertyChangeSupport} from {@link #getPropertyChangeSupport}.
     * 
     * @param listener the {@code PropertyChangeListener} to be added
     */
    public final void addPropertyChangeListener(PropertyChangeListener listener) {
        getPropertyChangeSupport().addPropertyChangeListener(listener);
    }

    /**
     * Removes a {@code PropertyChangeListener} from the listener list. This
     * removes a {@code PropertyChangeListener} that was registered for all
     * properties. If {@code listener} was added more than once to the same
     * event source, it will be notified one less time after being removed. If
     * {@code listener} is {@code null}, or was never added, no exception is
     * thrown and no action is taken.
     * 
     * <p>
     * Note: This is merely a convenience wrapper. All work is delegated to
     * {@code PropertyChangeSupport} from {@link #getPropertyChangeSupport}.
     * 
     * @param listener the {@code PropertyChangeListener} to be removed
     */
    public final void removePropertyChangeListener(PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(listener);
    }

    /**
     * Reports a bound property update to any registered listeners. No event is
     * fired if {@code old} and {@code new} are equal and non-null.
     * 
     * <p>
     * This {@code SwingWorker} will be the source for 
     * any generated events.
     *
     * <p>
     * When called off the <i>Event Dispatch Thread</i>
     * {@code PropertyChangeListeners} are notified asynchronously on
     * the <i>Event Dispatch Thread</i>.
     * <p>
     * Note: This is merely a convenience wrapper. All work is delegated to
     * {@code PropertyChangeSupport} from {@link #getPropertyChangeSupport}.
     * 
     * 
     * @param propertyName the programmatic name of the property that was
     *        changed
     * @param oldValue the old value of the property
     * @param newValue the new value of the property
     */
    public final void firePropertyChange(String propertyName, Object oldValue,
            Object newValue) {
        getPropertyChangeSupport().firePropertyChange(propertyName,
            oldValue, newValue);
    }

    /**
     * Returns the {@code PropertyChangeSupport} for this {@code SwingWorker}. 
     * This method is used when flexible access to bound properties support is
     * needed.
     * <p>
     * This {@code SwingWorker} will be the source for 
     * any generated events.
     * 
     * <p>
     * Note: The returned {@code PropertyChangeSupport} notifies any
     * {@code PropertyChangeListener}s asynchronously on the <i>Event Dispatch
     * Thread</i> in the event that {@code firePropertyChange} or
     * {@code fireIndexedPropertyChange} are called off the <i>Event Dispatch
     * Thread</i>.
     * 
     * @return {@code PropertyChangeSupport} for this {@code SwingWorker}
     */
    public final PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    // PropertyChangeSupports methods END

    /**
     * Returns the {@code SwingWorker} state bound property.
     * 
     * @return the current state
     */
    public final StateValue getState() {
        /*
         * DONE is a speacial case
         * to keep getState and isDone is sync
         */
        if (isDone()) {
            return StateValue.DONE;
        } else {
            return state;
        }
    }
    
    /**
     * Sets this {@code SwingWorker} state bound property.
     * @param the state state to set
     */
    private void setState(StateValue state) {
        StateValue old = this.state;
        this.state = state;
        firePropertyChange("state", old, state);
    }

    /**
     * Invokes {@code done} on the EDT.
     */
    private void doneEDT() {
        Runnable doDone = 
            new Runnable() {
                public void run() {
                    done();
                }
            };
        if (SwingUtilities.isEventDispatchThread()) {
            doDone.run();
        } else {
            doSubmit.add(doDone);
        }
    }


    /**
     * returns workersExecutorService.
     *
     * returns the service stored in the appContext or creates it if
     * necessary. If the last one it triggers autoShutdown thread to
     * get started.
     * 
     * @return ExecutorService for the {@code SwingWorkers}
     * @see #startAutoShutdownThread
     */
    static ExecutorService service;
    private static synchronized ExecutorService getWorkersExecutorService() {
        Object obj = service;
        if (obj == null) {
            //this creates non-daemon threads. 
            ThreadFactory threadFactory = 
                new ThreadFactory() {
                    final ThreadFactory defaultFactory = 
                        Executors.defaultThreadFactory();
                    public Thread newThread(final Runnable r) {
                        Thread thread = 
                            defaultFactory.newThread(r);
                        thread.setName("SwingWorker-" 
                            + thread.getName());
                        return thread;
                    }
                };

            /*
             * We want a to have no more than MAX_WORKER_THREADS
             * running threads.
             *
             * We want a worker thread to wait no longer than 1 second
             * for new tasks before terminating.
             */
            obj = new ThreadPoolExecutor(0, MAX_WORKER_THREADS,
                                         1L, TimeUnit.SECONDS,
                                         new LinkedBlockingQueue<Runnable>(),
                                         threadFactory) {

                    private final ReentrantLock pauseLock = new ReentrantLock();
                    private final Condition unpaused = pauseLock.newCondition();
                    private boolean isPaused = false;
                    private final ReentrantLock executeLock = new ReentrantLock();
                    
                    @Override
                    public void execute(Runnable command) {
                        /*
                         * ThreadPoolExecutor first tries to run task
                         * in a corePool. If all threads are busy it
                         * tries to add task to the waiting queue. If it
                         * fails it run task in maximumPool.
                         *
                         * We want corePool to be 0 and
                         * maximumPool to be MAX_WORKER_THREADS
                         * We need to change the order of the execution.
                         * First try corePool then try maximumPool
                         * pool and only then store to the waiting
                         * queue. We can not do that because we would
                         * need access to the private methods.
                         *
                         * Instead we enlarge corePool to
                         * MAX_WORKER_THREADS before the execution and
                         * shrink it back to 0 after. 
                         * It does pretty much what we need.
                         *
                         * While we changing the corePoolSize we need
                         * to stop running worker threads from accepting new
                         * tasks.
                         */
                        
                        //we need atomicity for the execute method.
                        executeLock.lock();
                        try {

                            pauseLock.lock();
                            try {
                                isPaused = true;
                            } finally {
                                pauseLock.unlock();
                            }
                            
                            setCorePoolSize(MAX_WORKER_THREADS);
                            super.execute(command);
                            setCorePoolSize(0);
                            
                            pauseLock.lock();
                            try {
                                isPaused = false;
                                unpaused.signalAll();
                            } finally {
                                pauseLock.unlock();
                            }
                        } finally {
                            executeLock.unlock();
                        }
                    }
                    @Override 
                    protected void afterExecute(Runnable r, Throwable t) { 
                        super.afterExecute(r, t);
                        pauseLock.lock();
                        try {
                            while(isPaused) {
                                unpaused.await();
                            }
                        } catch(InterruptedException ignore) {
                            
                        } finally {
                            pauseLock.unlock();
                        }
                    }
                };
            service = (ExecutorService)obj;
        }
        return service;
    }
    
    private static class DoSubmitAccumulativeRunnable 
          extends AccumulativeRunnable<Runnable> implements ActionListener {
        private final static int DELAY = (int) (1000 / 30);
        @Override
        protected void run(List<Runnable> args) {
            for (Runnable runnable : args) {
                runnable.run();
            }
        }
        @Override
        protected void submit() {
            Timer timer = new Timer(DELAY, this);
            timer.setRepeats(false);
            timer.start();
        }
        public void actionPerformed(ActionEvent event) {
            run();
        }
    }
    
    private class SwingWorkerPropertyChangeSupport 
            extends PropertyChangeSupport {
        SwingWorkerPropertyChangeSupport(Object source) {
            super(source);
        }
        @Override
        public void firePropertyChange(final PropertyChangeEvent evt) {
            if (SwingUtilities.isEventDispatchThread()) {
                super.firePropertyChange(evt);
            } else {
                doSubmit.add(
                    new Runnable() {
                        public void run() {
                            SwingWorkerPropertyChangeSupport.this
                                .firePropertyChange(evt);
                        }
                    });
            }
        }
    }
}
