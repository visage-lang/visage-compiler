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

package com.sun.javafx.runtime.util;

import java.util.Collections;
import java.util.Map;
import java.util.Locale;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import com.sun.javafx.runtime.util.backport.ResourceBundle;

public class StringLocalization {

    private static final Map<ThreadGroup, Map<String, String>> map = 
        Collections.synchronizedMap(
            new WeakHashMap<ThreadGroup, Map<String, String>>());

    public static String getLocalizedString(String scriptName, String explicitKey, 
                                        String literal, Object... embeddedExpr) {
        return getLocalizedString(
            getPropertiesName(scriptName.replaceAll("/", "\\.")), 
            explicitKey, literal, Locale.getDefault(), embeddedExpr);
    }

    public static String getLocalizedString(String propertiesName, String explicitKey, 
                        String literal, Locale locale, Object... embeddedExpr) {
        String localization = literal;

        try {
            ResourceBundle rb = ResourceBundle.getBundle(propertiesName,
                    locale, FXPropertyResourceBundle.FXPropertiesControl.INSTANCE);
        
            if (explicitKey.length() != 0) {
                localization = rb.getString(explicitKey);
                if (explicitKey.equals(localization) && 
                    !rb.keySet().contains(explicitKey)) {
                    localization = literal;
                }
            } else {
                localization = rb.getString(literal.replaceAll("\r\n|\r|\n", "\n"));
            }

            if (embeddedExpr.length != 0) {
                localization = String.format(localization, embeddedExpr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return localization;
    }

    public static void associate(String source, String properties) {
        Map<String, String> assoc = getAssociation();
        
        if (properties != null) {
            assoc.put(source, properties);
        } else {
            assoc.remove(source);
        }
    }

    public static String getPropertiesName(String scriptName) {
        String propertiesName = scriptName;
        Map<String, String> assoc = getAssociation();
        
        while (true) {
            if (assoc.containsKey(scriptName)) {
                propertiesName = assoc.get(scriptName);
                break;
            } else {
                int lastDot = scriptName.lastIndexOf('.');
                if (lastDot != -1) {
                    scriptName = scriptName.substring(0, lastDot);
                } else {
                    break;
                }
            }
        }

        return propertiesName;
    }

    private static Map<String, String> getAssociation() {
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
        Map<String, String> assoc = map.get(tg);

        if (assoc == null) {
            assoc = new ConcurrentHashMap<String, String>();
            map.put(tg, assoc);
        }

        return assoc;
    }
}
