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

package javafx.ext.swing;

import javafx.application.Application;
import java.lang.RuntimeException;

/**
 * The Swing {@code Application} subclass uses a single {@code Component} content
 * attribute instead of the {@code stage} attribute.
 */
public class SwingApplication extends Application {

    /**
     * {@code SwingApplication} uses the {@code content} attribute instead of
     * {@code stage}. An attempt to set {@code stage} to {@code non-null}
     * results in a {@code RuntimeException}.
     */
    override attribute stage on replace {
        if (stage != null) {
            throw new RuntimeException("Use content attribute with SwingApplication. Use stage with javafx.application.Application directly.");
        }
    }
    
    /**
     * The UI part of the application - a component to be shown when the 
     * application is running.
     */
    public attribute content: Component;

}
