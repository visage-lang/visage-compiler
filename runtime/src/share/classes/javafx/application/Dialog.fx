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

package javafx.application;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JDialog;
import javafx.lang.FX;

// PENDING_DOC_REVIEW
/**
 * A {@code Dialog} object is a top-level window.
 * <p/>
 * A dialog may have window defined as its owner when it's constructed.
 */
public class Dialog extends Window {

    /*
     * This must be defined before createWindow() is called, so that the window can be created with the owner.
     */
    public /* set-once */ var owner : Window = null;

    // PENDING_DOC_REVIEW
    /**
     * Creates the {@link java.awt.Window} delegate for this component. Must implement RootPaneContainer.
     * ie. be a JWindow, JDialog or JFrame.
     */
    override function createWindow(): java.awt.Window {
        WindowImpl.createJDialog(owner.window);
    }

    override function setLocation(){
        if (not window.isLocationByPlatform() and (window.getX() == 0) and (window.getY() == 0)) {
            window.setLocationRelativeTo(if (owner != null) owner.window else null);
        }
    }

    override function setWindowTitle(title:String): Void {
        (window as JDialog).setTitle(title);
    }

    override function setWindowResizable(resizable:Boolean): Void {
        (window as JDialog).setResizable(resizable);
    }

    override function setUndecorated(undecorated:Boolean): Void{
        (window as JDialog).setUndecorated(undecorated);
    }

}
