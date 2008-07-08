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

import java.math.BigInteger;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.LayoutManager;
import java.awt.Container;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import com.sun.scenario.scenegraph.JSGPanel;
import com.sun.scenario.scenegraph.SGGroup;
import javax.swing.JWindow;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javafx.lang.FX;
import javafx.ext.swing.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.image.Image;

// PENDING_DOC_REVIEW
/**
 * A {@code Frame} object is a top-level window.
 */
public class Frame extends Window{

    // PENDING(shannonh) - make bindable
    public attribute iconified: Boolean = false on replace {
        var frame:JFrame = window as JFrame;
        var oldState = BigInteger.valueOf(frame.getExtendedState());
        var newState = if (iconified) oldState.setBit(0) else oldState.clearBit(0);
        frame.setExtendedState(newState.intValue());
    }

    function setWindowTitle(title:String): Void {
        doAndIgnoreWindowChange(function() {
            (window as JFrame).setTitle(title);
        });
    }

    function isWindowResizable(): Boolean {
        (window as JFrame).isResizable();
    }

    function setWindowResizable(resizable:Boolean): Void {
        doAndIgnoreWindowChange(function() {
            (window as JFrame).setResizable(resizable);
        });
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Creates the {@link java.awt.Window} delegate for this component. Must implement RootPaneContainer, ie be a
     * JWindow,JDialog or JFrame.
     */
    function createWindow(): java.awt.Window {
        WindowImpl.createJFrame();
    }

}
