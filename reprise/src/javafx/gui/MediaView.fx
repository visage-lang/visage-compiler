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

import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGComponent;

import com.sun.media.jmc.MediaProvider;
import com.sun.media.jmc.control.VideoControl;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

import java.lang.System;
import com.sun.javafx.gui.MediaHelper;

/**
 * A {@code Node} that provides a view of {@code Media} being played
 * @profile core
*/ 
public class MediaView extends Node {
    private attribute mediaProvider:MediaProvider;
    private attribute sgc:SGComponent;
    
    /**
     * Sets the viewer to view the media being played by the {@code mediaPlayer}.
     * If the {@code mediaPlayer} is already being viewed by another 
     * {@code MediaView}, this operation may fail and {@code onError} will be
     * called with a {@code MediaError}, {@code OPERATION_UNSUPPORTED}
     */
    public attribute mediaPlayer:MediaPlayer on replace {
        // get Media player's component and make it ours
        // how to fail on this?

        mediaProvider = mediaPlayer.mediaProvider;
        setComponent();
        mediaPlayer.addView(this);
        
    }
    
    /**
     * The function specified by the {@code onError} attribute is invoked whenever
     * an error occurs on this {@code MediaView}
     * @profile core
     * @see MediaError
     */
    public attribute onError:function(me:MediaError);

    /**
     * If {@code fullScreen} is {@code true}, the {@code mediaView} will extend to use
     * the full screen, otherwise it will be limited to fit within its node.
     * @profile core
     */
    public attribute fullScreen:Boolean;

   /**
     * If {@code transformable} is {@code true}, the {@code mediaView} may be
     * transformed with such operations setting its {@code Transform}, 
     * {@code ShearX}, or {@code ShearY} attributes. 
     * Otherwise, setting these attributes may have no effect.     
     * @profile core
     * @see node.transform
     */
    public attribute transformable:Boolean=true; 

    /**
     * If {@code compositable} is {@code true}, the {@code MediaView} may
     * support compositing effects such as translucency. Otherwise, the 
     * {@code MediaView} is constrained to being an opaque node
     */
    public attribute compositable:Boolean=true;

    /**
     * If {@code rotatable} is {@code true}, the {@code mediaView} may be 
     * rotated by setting its rotation attribute
     * @profile core
     * @see node.rotation
     */
    public attribute rotatable:Boolean=true; 

    /**
     * If {@code preserveAspectRatio} is {@code true}, the media is scaled to
     * fit the node, but its aspect is preserved. Otherwise, the media is
     * scaled, but will be stretched or sheared in both dimension to fit its
     * node's dimensions
     */
    public attribute preserveAspectRatio:Boolean= true;


    // viewport?

    private attribute myJPanel: JPanel;
    

    function setComponent() {
            var vc:VideoControl;
            var mComp:JComponent;
            vc = MediaHelper.getVideoControl(mediaProvider);

            if (vc <> null) {
                mComp = vc.getVideoPane();
            }

        if (myJPanel == null) { // a contaier for the media window. since
                                // we may not have it yet
            // fix this in JMC, by having provider return a component?
            myJPanel = new JPanel();
            
            myJPanel.setLayout(new BorderLayout());
            myJPanel.validate();
            sgc.setComponent(myJPanel);
        }
        if (mComp <> null) {
            myJPanel.removeAll();
            myJPanel.add(mComp);
        }
    }

    function createSGNode() : SGNode {
        sgc = new SGComponent();
        setComponent();
        return sgc;
    }
    
 
}
