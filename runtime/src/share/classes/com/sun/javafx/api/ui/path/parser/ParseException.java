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
package com.sun.javafx.api.ui.path.parser;


public class ParseException extends RuntimeException
{

    public ParseException(String s, int i, int j)
    {
        super(s);
        exception = null;
        lineNumber = i;
        columnNumber = j;
    }

    public ParseException(Exception exception1)
    {
        exception = exception1;
        lineNumber = -1;
        columnNumber = -1;
    }

    public ParseException(String s, Exception exception1)
    {
        super(s);
        exception = exception1;
    }

    public String getMessage()
    {
        String s = super.getMessage();
        if(s == null && exception != null)
            return exception.getMessage();
        else
            return s;
    }

    public Exception getException()
    {
        return exception;
    }

    public int getLineNumber()
    {
        return lineNumber;
    }

    public int getColumnNumber()
    {
        return columnNumber;
    }

    protected Exception exception;
    protected int lineNumber;
    protected int columnNumber;
}
