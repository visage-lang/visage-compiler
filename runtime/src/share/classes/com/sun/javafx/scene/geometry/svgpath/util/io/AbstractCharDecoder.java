/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package com.sun.javafx.scene.geometry.svgpath.util.io;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractCharDecoder
    implements CharDecoder
{

    protected AbstractCharDecoder(InputStream inputstream)
    {
        buffer = new byte[8192];
        inputStream = inputstream;
    }

    public void dispose()
        throws IOException
    {
        inputStream.close();
        inputStream = null;
    }

    protected void fillBuffer()
        throws IOException
    {
        count = inputStream.read(buffer, 0, 8192);
        position = 0;
    }

    protected void charError(String s)
        throws IOException
    {
        throw new IOException(Messages.formatMessage("invalid.char", new Object[] {
            s
        }));
    }

    protected void endOfStreamError(String s)
        throws IOException
    {
        throw new IOException(Messages.formatMessage("end.of.stream", new Object[] {
            s
        }));
    }

    public abstract int readChar()
        throws IOException;

    protected static final int BUFFER_SIZE = 8192;
    protected InputStream inputStream;
    protected byte buffer[];
    protected int position;
    protected int count;
}
