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

package fxpad.gui;

import java.io.File;
import java.lang.System;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.JComponent;
import java.lang.Exception;
import javafx.ext.swing.*;

/**
 * @author jclarke
 */

public class FileChooser extends Component {
    public attribute cwd: File;
    public attribute fileFilters: FileFilter[];
    public attribute title: String;
    public attribute files: Boolean;
    public attribute directories:Boolean;
    public attribute action: function(selectedFile:File);
    public attribute selectedFile: File;
    public attribute accessory: Component;
    public attribute controlButtonsAreShown: Boolean = true;
    
    public /* final */ function getJFileChooser(): JFileChooser {
        getJComponent() as JFileChooser;
    }

    /* final */ override function createJComponent(): JComponent {
        createJFileChooser();
    }

    protected function createJFileChooser(): JFileChooser {
        var fc = new JFileChooser();
        try {
            fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        } catch (e:Exception) {
            e.printStackTrace();
        }  
        fc;
    }
    
    public function showOpenDialog(c:Component):Void{
        this.configure();
        var filechooser = getJFileChooser();
        //filechooser.addPropertyChangeListener(propertyChangeListener);
        var rc:Integer = JFileChooser.ERROR_OPTION;
        var window = if(c == null) null else SwingUtilities.getWindowAncestor(c.getJComponent());
        try {
            rc = filechooser.showOpenDialog(window);
        } finally {
            //filechooser.removePropertyChangeListener(propertyChangeListener);
        }
        if (rc == JFileChooser.APPROVE_OPTION) {
            selectedFile = filechooser.getSelectedFile();
            if(action != null) {
                action(selectedFile);
            }
            cwd = filechooser.getCurrentDirectory();
        }
    }

    public function showSaveDialog(c:Component):Void {
        configure();
        var filechooser = getJFileChooser();
        var window = if(c == null) null else SwingUtilities.getWindowAncestor(c.getJComponent());
        if (filechooser.showSaveDialog(window) == JFileChooser.APPROVE_OPTION) {
            selectedFile = filechooser.getSelectedFile();
            if(action != null) {
                action(selectedFile);
            }
            cwd = filechooser.getCurrentDirectory();
        }
    }    
    
    protected function configure():Void{
        var filechooser = getJFileChooser();
        filechooser.setSelectedFile(null);
        filechooser.setDialogTitle(title);
        filechooser.resetChoosableFileFilters();
        filechooser.setAccessory(accessory.getJComponent());
        filechooser.setControlButtonsAreShown(controlButtonsAreShown);
        var mode = if (files and directories) then JFileChooser.FILES_AND_DIRECTORIES else 
                if (directories) then JFileChooser.DIRECTORIES_ONLY else JFileChooser.FILES_ONLY;
        filechooser.setFileSelectionMode(mode);
        filechooser.setSelectedFile(null);

        var sz = sizeof fileFilters - 1;     
        for(i in [sz..0 step -1] ) {
            filechooser.addChoosableFileFilter( fileFilters[i] )
        }
        if (cwd != null) {
            filechooser.setCurrentDirectory(cwd.getCanonicalFile());
        }
    }    
    
}