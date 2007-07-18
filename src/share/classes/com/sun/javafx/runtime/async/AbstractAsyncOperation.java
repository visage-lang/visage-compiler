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

package com.sun.javafx.runtime.async;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

/**
 * AbstractAsyncOperation.   Base class for result-bearing, asynchronous operations. Some operations are asynchronous
 * because they would potentially block the EDT for unacceptably long. Since JFX lacks a clear concurrency model,
 * allowing users to execute arbitrary JFX code in background threads would invariably cause problems.  Therefore,
 * we provide a number of Java classes for async operations, which will execute in a background thread, such as
 * "fetch a resource over the web".  Async operations should not access any JFX state except the immutable parameters
 * passed in, and should not have side effects other than those managed by thread-safe Java classes.
 *
 * @author Brian Goetz
 */
public abstract class AbstractAsyncOperation<V> implements Callable<V> {

    private final FutureTask<V> future;
    protected final AsyncOperationListener listener;

    private int progressGranularity = 100;
    private int progressMax, lastProgress, progressIncrement, nextProgress, bytesRead;

    protected AbstractAsyncOperation(final AsyncOperationListener<V> listener) {
        this.listener = listener;

        Callable<V> callable = new Callable<V>() {
            public V call() throws Exception {
                return AbstractAsyncOperation.this.call();
            }
        };

        final Runnable completionRunnable = new Runnable() {
            public void run() {
                if (future.isCancelled())
                    listener.onCancel();
                else
                    try {
                        listener.onCompletion(future.get());
                    } catch (InterruptedException e) {
                        listener.onCancel();
                    } catch (ExecutionException e) {
                        listener.onException(e);
                    }
            }
        };

        future = new FutureTask<V>(callable) {
            protected void done() {
                try {
                    SwingUtilities.invokeLater(completionRunnable);
                } finally {
                    super.done();
                }
            }
        };
    }

    public boolean isCancelled() {
        return future.isCancelled();
    }

    public boolean isDone() {
        return future.isDone();
    }

    public void cancel() {
        future.cancel(true);
    }

    public void start() {
        ExecutorService es = BackgroundExecutor.getExecutor();
        es.execute(future);
    }

    protected void notifyProgress() {
        final int last = lastProgress;
        final int max = progressMax;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                listener.onProgress(last, max);
            }
        });
    }

    private void addProgress(int amount) {
        bytesRead  += amount;
        if (bytesRead > nextProgress) {
            lastProgress = bytesRead;
            notifyProgress();
            nextProgress = ((lastProgress / progressIncrement) + 1) * progressIncrement;
        }
    }

    protected int getProgressMax() {
        return progressMax;
    }

    protected void setProgressMax(int progressMax) {
        this.progressMax = progressMax;
        progressIncrement = progressMax / progressGranularity;
        nextProgress = ((lastProgress / progressIncrement) + 1) * progressIncrement;
        notifyProgress();
    }

    protected int getProgressGranularity() {
        return progressGranularity;
    }

    protected void setProgressGranularity(int progressGranularity) {
        this.progressGranularity = progressGranularity;
        progressIncrement = progressMax / progressGranularity;
        nextProgress = ((lastProgress / progressIncrement) + 1) * progressIncrement;
        notifyProgress();
    }

    protected class ProgressInputStream extends BufferedInputStream {
        public ProgressInputStream(InputStream in) {
            super(in);
        }

        public synchronized int read() throws IOException {
            if (Thread.currentThread().isInterrupted())
                throw new InterruptedIOException();
            int ch = super.read();
            addProgress(1);
            return ch;
        }

        public synchronized int read(byte b[], int off, int len) throws IOException {
            if (Thread.currentThread().isInterrupted())
                throw new InterruptedIOException();
            int bytes = super.read(b, off, len);
            addProgress(bytes);
            return bytes;
        }

        public int read(byte b[]) throws IOException {
            if (Thread.currentThread().isInterrupted())
                throw new InterruptedIOException();
            int bytes = super.read(b);
            addProgress(bytes);
            return bytes;
        }
    }
}
