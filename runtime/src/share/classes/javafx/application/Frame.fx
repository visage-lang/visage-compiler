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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowStateListener;
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
    public var iconified: Boolean = false on replace {
        var frame:JFrame = window as JFrame;
        var oldState = BigInteger.valueOf(frame.getExtendedState());
        var newState = if (iconified) oldState.setBit(0) else oldState.clearBit(0);
        frame.setExtendedState(newState.intValue());
    }

    override function setWindowTitle(title:String): Void {
        (window as JFrame).setTitle(title);
    }

    override function setWindowResizable(resizable:Boolean): Void {
        (window as JFrame).setResizable(resizable);
    }

    override function setUndecorated(undecorated:Boolean): Void{
        (window as JFrame).setUndecorated(undecorated);
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates the {@link java.awt.Window} delegate for this component. Must implement RootPaneContainer, ie be a
     * JWindow,JDialog or JFrame.
     */
    override function createWindow(): java.awt.Window {
        WindowImpl.createJFrame();
    }

    postinit {
        var jFrame =  (window as JFrame);

        jFrame.addWindowStateListener(WindowStateListener {
            public override function windowStateChanged(e: WindowEvent): Void {
                var state = BigInteger.valueOf(e.getNewState());
                iconified = state.testBit(0); // Frame.ICONIFIED = 1
            }
        });
    }

}
