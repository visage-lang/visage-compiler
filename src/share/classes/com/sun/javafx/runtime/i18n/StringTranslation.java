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

package com.sun.javafx.runtime.i18n;

import com.sun.javafx.runtime.i18n.backport.ResourceBundle;

public class StringTranslation {
    public static String getTranslation(String className, String explicitKey, 
                                        String literal, Object... embeddedExpr) {
        String translation = literal;

        try {
            ResourceBundle rb = ResourceBundle.getBundle(className,
                    JavaFXPropertyResourceBundle.JavaFXPropertiesControl.INSTANCE);
        
            if (explicitKey.length() != 0) {
                translation = rb.getString(explicitKey);
                if (explicitKey.equals(translation)) {
                    translation = literal;
                }
            } else {
                translation = rb.getString(literal.replaceAll("\r\n|\r|\n", "\n"));
            }

            if (embeddedExpr.length != 0) {
                translation = String.format(translation, embeddedExpr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return translation;
    }
}
