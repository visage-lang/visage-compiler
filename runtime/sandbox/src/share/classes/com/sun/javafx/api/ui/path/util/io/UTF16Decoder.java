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

public class UTF16Decoder extends AbstractCharDecoder
{

    public UTF16Decoder(InputStream inputstream)
        throws IOException
    {
        super(inputstream);
        int i = inputstream.read();
        if(i == -1)
            endOfStreamError("UTF-16");
        int j = inputstream.read();
        if(j == -1)
            endOfStreamError("UTF-16");
        int k = (i & 0xff) << 8 | j & 0xff;
        switch(k)
        {
        case 65279: 
            bigEndian = true;
            break;

        default:
            charError("UTF-16");
            break;

        case 65534: 
            break;
        }
    }

    public UTF16Decoder(InputStream inputstream, boolean flag)
    {
        super(inputstream);
        bigEndian = flag;
    }

    public int readChar()
        throws IOException
    {
        if(super.position == super.count)
            fillBuffer();
        if(super.count == -1)
            return -1;
        byte byte0 = super.buffer[super.position++];
        if(super.position == super.count)
            fillBuffer();
        if(super.count == -1)
            endOfStreamError("UTF-16");
        byte byte1 = super.buffer[super.position++];
        int i = bigEndian ? (byte0 & 0xff) << 8 | byte1 & 0xff : (byte1 & 0xff) << 8 | byte0 & 0xff;
        if(i == 65534)
            charError("UTF-16");
        return i;
    }

    protected boolean bigEndian;
}
