/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
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

package com.sun.javafx.gui;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * A group of internal methods that don't have a better home.
 * Public only to allow use from other packages.
 *
 * Not part of the public API.
 */
public final class InternalHelper {
    
    private static final String METAL_LAF_CLASSNAME = "javax.swing.plaf.metal.MetalLookAndFeel";

    private InternalHelper() {}

    public static final void initDefaultLAF() {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
        } catch (ClassNotFoundException ignore) {
        } catch (InstantiationException ignore) {
        } catch (IllegalAccessException ignore) {
        } catch (UnsupportedLookAndFeelException ignore) {
        }

        try {
            if (!METAL_LAF_CLASSNAME.equals(UIManager.getLookAndFeel().getClass().getName())) {
                UIManager.setLookAndFeel(METAL_LAF_CLASSNAME);
            }
        } catch (ClassNotFoundException ignore) {
        } catch (InstantiationException ignore) {
        } catch (IllegalAccessException ignore) {
        } catch (UnsupportedLookAndFeelException ignore) {
        }
    }

}
