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

package com.sun.javafx.runtime.async;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Abstract base class for representing remote resources identified by a URL.  Subclasses may plug in arbitrary
 * post-processing on the stream to turn it into the desired result.  Manages progress indication if the remote resource
 * provides a content-length header.
 *
 * @author Brian Goetz
 */
public abstract class AbstractRemoteResource<T> extends AbstractAsyncOperation<T> {

    protected final String url;
    protected final String method;
    protected int fileSize;

    protected AbstractRemoteResource(String url, AsyncOperationListener<T> listener) {
        this(url, "GET", listener);
    }

    protected AbstractRemoteResource(String url, String method, AsyncOperationListener<T> listener) {
        super(listener);
        this.url = url;
        this.method = method;
    }

    protected abstract T processStream(InputStream stream) throws IOException;

    public T call() throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod(method);
        fileSize = conn.getContentLength();
        setProgressMax(fileSize);

        InputStream stream = new ProgressInputStream(conn.getInputStream());
        try {
            return processStream(stream);
        }
        finally {
            stream.close();
        }
    }

    protected class ProgressInputStream extends BufferedInputStream {
        public ProgressInputStream(InputStream in) {
            super(in);
        }

        @Override
        public synchronized int read() throws IOException {
            if (Thread.currentThread().isInterrupted())
                throw new InterruptedIOException();
            int ch = super.read();
            addProgress(1);
            return ch;
        }

        @Override
        public synchronized int read(byte b[], int off, int len) throws IOException {
            if (Thread.currentThread().isInterrupted())
                throw new InterruptedIOException();
            int bytes = super.read(b, off, len);
            addProgress(bytes);
            return bytes;
        }

        @Override
        public int read(byte b[]) throws IOException {
            if (Thread.currentThread().isInterrupted())
                throw new InterruptedIOException();
            int bytes = super.read(b);
            addProgress(bytes);
            return bytes;
        }
    }
}
