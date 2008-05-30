package com.sun.javafx.ideaplugin.run;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author David Kaspar
 */
public final class FxRunSettingsPanel {

    JTextField mainClassText;
    JTextField vmParamsText;
    JTextField programParamsText;
    JTextField workingDirectoryText;
    JButton workingDirectoryBrowseButton;
    JComboBox moduleCombo;
    JPanel panel;

    public FxRunSettingsPanel () {
        workingDirectoryBrowseButton.addActionListener (new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
                descriptor.setTitle("Choose working directory");
                VirtualFile avirtualfile[] = FileChooser.chooseFiles(workingDirectoryBrowseButton, descriptor);
                if (avirtualfile.length != 0)
                    workingDirectoryText.setText (avirtualfile[0].getPath());
            }
        });
    }

}
