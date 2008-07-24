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
package com.sun.javafx.api.ui.path.util.io;

import java.io.IOException;
import java.io.InputStream;

public class UTF8Decoder extends AbstractCharDecoder
{

    public UTF8Decoder(InputStream inputstream)
    {
        super(inputstream);
        nextChar = -1;
    }

    public int readChar()
        throws IOException
    {
        if(nextChar != -1)
        {
            int i = nextChar;
            nextChar = -1;
            return i;
        }
        if(super.position == super.count)
            fillBuffer();
        if(super.count == -1)
            return -1;
        int j = super.buffer[super.position++] & 0xff;
        switch(UTF8_BYTES[j])
        {
        default:
            charError("UTF-8");
            // fall through

        case 1: // '\001'
            return j;

        case 2: // '\002'
            if(super.position == super.count)
                fillBuffer();
            if(super.count == -1)
                endOfStreamError("UTF-8");
            return (j & 0x1f) << 6 | super.buffer[super.position++] & 0x3f;

        case 3: // '\003'
            if(super.position == super.count)
                fillBuffer();
            if(super.count == -1)
                endOfStreamError("UTF-8");
            byte byte0 = super.buffer[super.position++];
            if(super.position == super.count)
                fillBuffer();
            if(super.count == -1)
                endOfStreamError("UTF-8");
            byte byte2 = super.buffer[super.position++];
            if((byte0 & 0xc0) != 128 || (byte2 & 0xc0) != 128)
                charError("UTF-8");
            return (j & 0x1f) << 12 | (byte0 & 0x3f) << 6 | byte2 & 0x1f;

        case 4: // '\004'
            break;
        }
        if(super.position == super.count)
            fillBuffer();
        if(super.count == -1)
            endOfStreamError("UTF-8");
        byte byte1 = super.buffer[super.position++];
        if(super.position == super.count)
            fillBuffer();
        if(super.count == -1)
            endOfStreamError("UTF-8");
        byte byte3 = super.buffer[super.position++];
        if(super.position == super.count)
            fillBuffer();
        if(super.count == -1)
            endOfStreamError("UTF-8");
        byte byte4 = super.buffer[super.position++];
        if((byte1 & 0xc0) != 128 || (byte3 & 0xc0) != 128 || (byte4 & 0xc0) != 128)
            charError("UTF-8");
        int k = (j & 0x1f) << 18 | (byte1 & 0x3f) << 12 | (byte3 & 0x1f) << 6 | byte4 & 0x1f;
        nextChar = (k - 0x10000) % 1024 + 56320;
        return (k - 0x10000) / 1024 + 55296;
    }

    protected static final byte UTF8_BYTES[] = {
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
        1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
        2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
        4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 
        0, 0, 0, 0, 0, 0
    };
    protected int nextChar;

}
