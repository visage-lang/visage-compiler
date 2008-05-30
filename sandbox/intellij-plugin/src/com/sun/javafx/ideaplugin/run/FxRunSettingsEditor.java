package com.sun.javafx.ideaplugin.run;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * @author David Kaspar
 */
public class FxRunSettingsEditor extends SettingsEditor<FxRunConfiguration> {

    private final FxRunSettingsPanel panel;
    private final DefaultComboBoxModel moduleModel;

    public FxRunSettingsEditor (Project project) {
        panel = new FxRunSettingsPanel ();
        panel.moduleCombo.setModel (moduleModel = new DefaultComboBoxModel ());
        panel.moduleCombo.setRenderer (new DefaultListCellRenderer() {
            public Component getListCellRendererComponent (JList list, final Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent (list, value, index, isSelected, cellHasFocus);
                final Module module = (Module) value;
                if (module != null) {
                    setIcon (module.getModuleType ().getNodeIcon (false));
                    setText (module.getName ());
                }
                return this;
            }
        });
    }

    protected void resetEditorFrom (FxRunConfiguration configuration) {
        reset (panel.mainClassText, configuration.getMainClass ());
        reset (panel.mainClassText, configuration.getMainClass ());
        reset (panel.vmParamsText, configuration.getVmParameters ());
        reset (panel.programParamsText, configuration.getProgramParameters ());
        reset (panel.workingDirectoryText, configuration.getWorkingDirectory ());
        moduleModel.removeAllElements ();
        for (Module module : configuration.getValidModules ())
            moduleModel.addElement (module);
        moduleModel.setSelectedItem (configuration.getConfigurationModule ().getModule ());
    }

    private void reset (JTextField field, String text) {
        field.setText (text != null ? text : "");
    }

    protected void applyEditorTo (FxRunConfiguration configuration) throws ConfigurationException {
        configuration.setMainClass (panel.mainClassText.getText ());
        configuration.setVmParameters (panel.vmParamsText.getText ());
        configuration.setProgramParameters (panel.programParamsText.getText ());
        configuration.setWorkingDirectory (panel.workingDirectoryText.getText ());
        configuration.getConfigurationModule ().setModule ((Module) moduleModel.getSelectedItem ());
    }

    @NotNull protected JComponent createEditor () {
        return panel.panel;
    }

    protected void disposeEditor () {
    }

}
