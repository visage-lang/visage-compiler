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

public class StringNormalizingReader extends NormalizingReader
{

    public StringNormalizingReader(String s)
    {
        line = 1;
        string = s;
        length = s.length();
    }

    public int read()
        throws IOException
    {
        char c = length != next ? string.charAt(next++) : '\uFFFF';
        if(c <= '\r')
            switch(c)
            {
            default:
                break;

            case 13: // '\r'
                column = 0;
                line++;
                char c1 = length != next ? string.charAt(next) : '\uFFFF';
                if(c1 == '\n')
                    next++;
                return 10;

            case 10: // '\n'
                column = 0;
                line++;
                break;
            }
        return c;
    }

    public int getLine()
    {
        return line;
    }

    public int getColumn()
    {
        return column;
    }

    public void close()
        throws IOException
    {
        string = null;
    }

    protected String string;
    protected int length;
    protected int next;
    protected int line;
    protected int column;
}
