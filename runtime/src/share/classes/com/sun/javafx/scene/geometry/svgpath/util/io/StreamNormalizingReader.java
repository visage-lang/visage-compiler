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

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import com.sun.javafx.scene.geometry.svgpath.util.EncodingUtilities;

public class StreamNormalizingReader extends NormalizingReader
{
    protected static class UTF16DecoderFactory
        implements CharDecoderFactory
    {

        public CharDecoder createCharDecoder(InputStream inputstream)
            throws IOException
        {
            return new UTF16Decoder(inputstream);
        }

        protected UTF16DecoderFactory()
        {
        }
    }

    protected static class UTF8DecoderFactory
        implements CharDecoderFactory
    {

        public CharDecoder createCharDecoder(InputStream inputstream)
            throws IOException
        {
            return new UTF8Decoder(inputstream);
        }

        protected UTF8DecoderFactory()
        {
        }
    }

    protected static class ISO_8859_1DecoderFactory
        implements CharDecoderFactory
    {

        public CharDecoder createCharDecoder(InputStream inputstream)
            throws IOException
        {
            return new ISO_8859_1Decoder(inputstream);
        }

        protected ISO_8859_1DecoderFactory()
        {
        }
    }

    protected static class ASCIIDecoderFactory
        implements CharDecoderFactory
    {

        public CharDecoder createCharDecoder(InputStream inputstream)
            throws IOException
        {
            return new ASCIIDecoder(inputstream);
        }

        protected ASCIIDecoderFactory()
        {
        }
    }

    protected static interface CharDecoderFactory
    {

        public abstract CharDecoder createCharDecoder(InputStream inputstream)
            throws IOException;
    }


    public StreamNormalizingReader(InputStream inputstream)
        throws IOException
    {
        this(inputstream, null);
    }

    public StreamNormalizingReader(InputStream inputstream, String s)
        throws IOException
    {
        nextChar = -1;
        line = 1;
        if(s == null)
            s = "ISO-8859-1";
        charDecoder = createCharDecoder(inputstream, s);
    }

    public StreamNormalizingReader(Reader reader)
        throws IOException
    {
        nextChar = -1;
        line = 1;
        charDecoder = new GenericDecoder(reader);
    }

    protected StreamNormalizingReader()
    {
        nextChar = -1;
        line = 1;
    }

    public int read()
        throws IOException
    {
        int i = nextChar;
        if(i != -1)
        {
            nextChar = -1;
            if(i == 13)
            {
                column = 0;
                line++;
            } else
            {
                column++;
            }
            return i;
        }
        i = charDecoder.readChar();
        switch(i)
        {
        case 13: // '\r'
            column = 0;
            line++;
            int j = charDecoder.readChar();
            if(j == 10)
            {
                return 10;
            } else
            {
                nextChar = j;
                return 10;
            }

        case 10: // '\n'
            column = 0;
            line++;
            break;
        }
        return i;
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
        charDecoder.dispose();
        charDecoder = null;
    }

    protected CharDecoder createCharDecoder(InputStream inputstream, String s)
        throws IOException
    {
        CharDecoderFactory chardecoderfactory = (CharDecoderFactory)charDecoderFactories.get(s.toUpperCase());
        if(chardecoderfactory != null)
            return chardecoderfactory.createCharDecoder(inputstream);
        String s1 = EncodingUtilities.javaEncoding(s);
        if(s1 == null)
            s1 = s;
        return new GenericDecoder(inputstream, s1);
    }

    protected CharDecoder charDecoder;
    protected int nextChar;
    protected int line;
    protected int column;
    protected static final Map charDecoderFactories;

    static 
    {
        charDecoderFactories = new HashMap(11);
        ASCIIDecoderFactory asciidecoderfactory = new ASCIIDecoderFactory();
        charDecoderFactories.put("ASCII", asciidecoderfactory);
        charDecoderFactories.put("US-ASCII", asciidecoderfactory);
        charDecoderFactories.put("ISO-8859-1", new ISO_8859_1DecoderFactory());
        charDecoderFactories.put("UTF-8", new UTF8DecoderFactory());
        charDecoderFactories.put("UTF-16", new UTF16DecoderFactory());
    }
}
