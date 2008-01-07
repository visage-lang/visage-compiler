/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * A/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 NY WARRANTY; without even the implied warranty of MERCHANTABILITY or
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

package com.sun.javafx.api.ui;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JApplet;

/**
 * Intialization helper for javafx.ui.Applet
 * @author tball
 */
public class FXApplet extends JApplet {
    private static final String[][] paramInfo = {
        { "AppletClass", "String", "applet classname" }
    };

    @Override
    public String[][] getParameterInfo() {
        return paramInfo;
    }

    public FXApplet() {
        super();

        // invoke the applet's JavaFX Script runtime entry point
        try {
            Class<?> appletClass = getAppletClass();
            if (appletClass != null) {
                Method main = appletClass.getMethod("javafx$run$");
                Object result = main.invoke(null);
                if (result != null)
                    setContentAttribute(result);
            }
        } catch (Throwable t) {
            Logger.getLogger(FXApplet.class.getName()).log(Level.SEVERE, null, t);
        }
    }
    
    /**
     * Returns the applet class name.  There are two scenarios this supports:
     * <ul><li>Specify <code>com.sun.javafx.api.ui.FXApplet</code> for the
     * <code>code</code> parameter and the JavaFX Script applet class with a
     * <code>AppletClass</code> parameter.  In this case, the applet needs to
     * return a <code>javafx.ui.Applet</code> or <code>javafx.ui.Widget</code>
     * instance, which is installed as the applet's content.</li>
     * <li>Subclass <code>javafx.ui.Applet</code> and specify that class name
     * for the applet's <code>code</code> parameter.
     * 
     * @return  the name of the JavaFX Script class which is the applet class
     * @throws java.lang.ClassNotFoundException
     */
    private Class<?> getAppletClass() throws ClassNotFoundException {
        String clsname;
        try {
            clsname = getParameter("AppletClass");
        } catch (NullPointerException e) {
            // true if applet class is instantiated from test
            return null;
        }
        return clsname != null ? Class.forName(clsname) : getClass();
        
    }
    
    protected void setContentAttribute(Object o) {}
}
