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

    private final JTextField mainClass;
    private final JComboBox moduleCombo;
    private final DefaultComboBoxModel moduleModel;

    public FxRunSettingsEditor (Project project) {
        mainClass = new JTextField ();
        moduleCombo = new JComboBox (moduleModel = new DefaultComboBoxModel ());

        moduleCombo.setRenderer (new DefaultListCellRenderer() {
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
        mainClass.setText (configuration.getMainClass ());
        moduleModel.removeAllElements ();
        for (Module module : configuration.getValidModules ())
            moduleModel.addElement (module);
        moduleModel.setSelectedItem (configuration.getConfigurationModule ().getModule ());
    }

    protected void applyEditorTo (FxRunConfiguration configuration) throws ConfigurationException {
        configuration.setMainClass (mainClass.getText ());
        configuration.getConfigurationModule ().setModule ((Module) moduleModel.getSelectedItem ());
    }

    @NotNull protected JComponent createEditor () {
        JPanel panel = new JPanel ();
        panel.setLayout (new GridBagLayout ());
        GridBagConstraints gbc = new GridBagConstraints ();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.weighty = 0.0;
        panel.add (new JLabel ("Main Class:"), gbc);
        gbc.weighty = 1.0;
        panel.add (mainClass, gbc);

        gbc.weighty = 0.0;
        panel.add (new JLabel ("Module:"), gbc);
        gbc.weighty = 1.0;
        panel.add (moduleCombo, gbc);

        return panel;
    }

    protected void disposeEditor () {
    }

}
