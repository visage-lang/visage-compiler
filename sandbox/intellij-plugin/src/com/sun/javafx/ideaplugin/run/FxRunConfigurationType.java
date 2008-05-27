package com.sun.javafx.ideaplugin.run;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.application.ApplicationManager;
import com.sun.javafx.ideaplugin.project.FxSdkType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author David Kaspar
 */
public class FxRunConfigurationType implements ConfigurationType {

    public static FxRunConfigurationType getInstance () {
        return ApplicationManager.getApplication ().getComponent (FxRunConfigurationType.class);
    }

    public String getDisplayName () {
        return "JavaFX Script";
    }

    public String getConfigurationTypeDescription () {
        return "Runs a JavaFX Script class";
    }

    public Icon getIcon () {
        return FxSdkType.ICON;
    }

    public ConfigurationFactory[] getConfigurationFactories () {
        return new ConfigurationFactory[] {
            new ConfigurationFactory (this) {
                public RunConfiguration createTemplateConfiguration (Project project) {
                    return new FxRunConfiguration ("JavaFX Script Application", this, project);
                }
            }
        };
    }

    @NonNls @NotNull
    public String getComponentName () {
        return "FxRunConfigurationType";
    }

    public void initComponent () {
    }

    public void disposeComponent () {
    }

}
