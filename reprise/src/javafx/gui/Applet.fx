/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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
package javafx.gui;

import javafx.lang.DeferredTask;
import java.lang.Class;
import java.lang.Error;
import java.lang.Throwable;
import javax.swing.JApplet;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;
import com.sun.javafx.runtime.Entry;
import com.sun.javafx.gui.InternalHelper;

// PENDING_DOC_REVIEW
/**
 * The {@code Applet} class provides
 * support for the JFC/Swing component architecture.
 */
public class Applet extends JApplet {

    private attribute app:Application;

    private function launchApplication() {
        InternalHelper.initDefaultLAF();

        var appClassName = getParameter("ApplicationClass");
        var errorPrefix = "Couldn't launch FX Application";
        if (appClassName <> null) {
            try {
                var appClass:Class = Class.forName(appClassName);
                var name = Entry.entryMethodName();
                var args = Sequences.make(java.lang.String.<<class>>) as java.lang.Object;
                var appObject = appClass.getMethod(name, Sequence.<<class>>).invoke(null, args);
                app = appObject as Application;
            }
            catch (e:Throwable) {
                throw new Error("{errorPrefix} {appClassName}", e);
            }
        }
        else {
            throw new Error("{errorPrefix}: no ApplicationClass applet param specified");
        }
        if (app <> null) {
            setContentPane(app.content.getJComponent());
            if (app.onStart <> null) app.onStart();
        }
    }

    public function <<init>>() {
        DeferredTask {
            action: launchApplication
        }
    }

    public function start():Void {
        if (app.resume <> null) app.resume();
    }
    
    public function stop():Void {
        if (app.suspend <> null) app.suspend();
    }
}

