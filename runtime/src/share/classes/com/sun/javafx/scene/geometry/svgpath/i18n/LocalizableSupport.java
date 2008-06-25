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
package com.sun.javafx.scene.geometry.svgpath.i18n;


import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizableSupport
    implements Localizable
{

    public LocalizableSupport(String s)
    {
        this(s, null);
    }

    public LocalizableSupport(String s, ClassLoader classloader)
    {
        localeGroup = LocaleGroup.DEFAULT;
        bundleName = s;
        classLoader = classloader;
    }

    public void setLocale(Locale locale1)
    {
        if(locale != locale1)
        {
            locale = locale1;
            resourceBundle = null;
        }
    }

    public Locale getLocale()
    {
        return locale;
    }

    public void setLocaleGroup(LocaleGroup localegroup)
    {
        localeGroup = localegroup;
    }

    public LocaleGroup getLocaleGroup()
    {
        return localeGroup;
    }

    public void setDefaultLocale(Locale locale1)
    {
        localeGroup.setLocale(locale1);
    }

    public Locale getDefaultLocale()
    {
        return localeGroup.getLocale();
    }

    public String formatMessage(String s, Object aobj[])
    {
        getResourceBundle();
        return MessageFormat.format(resourceBundle.getString(s), aobj);
    }

    public ResourceBundle getResourceBundle()
    {
        Locale locale2;
        if(resourceBundle == null)
        {
            Locale locale1;
            if(locale == null)
            {
                if((locale1 = localeGroup.getLocale()) == null)
                    usedLocale = Locale.getDefault();
                else
                    usedLocale = locale1;
            } else
            {
                usedLocale = locale;
            }
            if(classLoader == null)
                resourceBundle = ResourceBundle.getBundle(bundleName, usedLocale);
            else
                resourceBundle = ResourceBundle.getBundle(bundleName, usedLocale, classLoader);
        } else
        if(locale == null)
            if((locale2 = localeGroup.getLocale()) == null)
            {
                if(usedLocale != (locale2 = Locale.getDefault()))
                {
                    usedLocale = locale2;
                    if(classLoader == null)
                        resourceBundle = ResourceBundle.getBundle(bundleName, usedLocale);
                    else
                        resourceBundle = ResourceBundle.getBundle(bundleName, usedLocale, classLoader);
                }
            } else
            if(usedLocale != locale2)
            {
                usedLocale = locale2;
                if(classLoader == null)
                    resourceBundle = ResourceBundle.getBundle(bundleName, usedLocale);
                else
                    resourceBundle = ResourceBundle.getBundle(bundleName, usedLocale, classLoader);
            }
        return resourceBundle;
    }

    protected LocaleGroup localeGroup;
    protected String bundleName;
    protected ClassLoader classLoader;
    protected Locale locale;
    protected Locale usedLocale;
    protected ResourceBundle resourceBundle;
}
