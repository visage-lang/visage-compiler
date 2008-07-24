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

import java.util.Locale;
import java.util.MissingResourceException;
import com.sun.javafx.api.ui.path.i18n.LocalizableSupport;

public class Messages
{

    protected Messages()
    {
    }

    public static void setLocale(Locale locale)
    {
        localizableSupport.setLocale(locale);
    }

    public static Locale getLocale()
    {
        return localizableSupport.getLocale();
    }

    public static String formatMessage(String s, Object aobj[])
        throws MissingResourceException
    {
        return localizableSupport.formatMessage(s, aobj);
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    protected static final String RESOURCES = "com.sun.javafx.api.ui.path.util.io.Messages";
    protected static LocalizableSupport localizableSupport;

    static 
    {
        localizableSupport = new LocalizableSupport("com.sun.javafx.api.ui.path.util.io.Messages", (com.sun.javafx.api.ui.path.util.io.Messages.class).getClassLoader());
    }
}
