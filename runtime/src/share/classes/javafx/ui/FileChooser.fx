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
 
package javafx.ui; 

import java.io.File;
import javax.swing.JFileChooser;

public class FileChooser extends Widget {
    private attribute propertyChangeListener: java.beans.PropertyChangeListener;
    private attribute filechooser: JFileChooser;
    public attribute cwd: File;
    public attribute fileFilters: FileFilter[];
    public attribute title: String;
    public attribute files: Boolean;
    public attribute directories:Boolean;
    public attribute action: function(selectedFile:File);
    public attribute selectedFile: File;
    public attribute accessory: Widget;
    public attribute controlButtonsAreShown: Boolean = true;
    public function showOpenDialog(w:UIElement):Void{
        this.configure();
        filechooser.addPropertyChangeListener(propertyChangeListener);
        var rc:Integer = JFileChooser.ERROR_OPTION;
        try {
            rc = filechooser.showOpenDialog(w.getWindow());
        } finally {
            filechooser.removePropertyChangeListener(propertyChangeListener);
        }
        if (rc == JFileChooser.APPROVE_OPTION) {
            if(action <> null) {
                action(filechooser.getSelectedFile());
            }
            cwd = filechooser.getCurrentDirectory();
        }
    }

    public function showSaveDialog(w:UIElement):Void {
        configure();
        if (filechooser.showSaveDialog(w.getWindow()) == JFileChooser.APPROVE_OPTION) {
            if(action <> null) {
                action(filechooser.getSelectedFile());
            }
            cwd = filechooser.getCurrentDirectory();
        }
    }

    protected function createFileChooser():javax.swing.JFileChooser{
        return UIElement.context.createFileChooser();
    }

    protected function configure():Void{
        filechooser.setSelectedFile(null);
        filechooser.setDialogTitle(title);
        filechooser.resetChoosableFileFilters();
        filechooser.setAccessory(accessory.getComponent());
        filechooser.setControlButtonsAreShown(controlButtonsAreShown);
        var mode = if (files and directories) then JFileChooser.FILES_AND_DIRECTORIES else 
                if (directories) then JFileChooser.DIRECTORIES_ONLY else JFileChooser.FILES_ONLY;
        filechooser.setFileSelectionMode(mode);
        filechooser.setSelectedFile(null);

        var sz = sizeof fileFilters - 1;     
        for(i in [sz..0] ) {
            UIElement.context.addChoosableFileFilter(filechooser, 
                            com.sun.javafx.api.ui.FileFilter {
                                                   public function equals(obj:java.lang.Object):Boolean {
                                                      return obj == this;
                                                   }
                                                   public function accept(f:File):Boolean {
                                                       return (fileFilters[i].filter)(f);
                                                   }
                                                   public function getDescription():String {
                                                       return fileFilters[i].description;
                                                   }
                                               });
        }

        if (cwd <> null) {
            filechooser.setCurrentDirectory(cwd.getCanonicalFile());
        }
    }

    public function createComponent():javax.swing.JComponent {
        return getComponent();
    }

    public function getComponent():javax.swing.JComponent {
        if(filechooser == null) {
            filechooser = this.createFileChooser();
            propertyChangeListener = java.beans.PropertyChangeListener {
                public function propertyChange(e:java.beans.PropertyChangeEvent):Void {
                    var prop = e.getPropertyName();
                    if (prop == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
                        selectedFile = e.getNewValue() as File;
                    }
                }
            };
        }
        this.configure();
        return filechooser;
    }

}




