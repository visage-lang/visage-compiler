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

package com.sun.javafx.ideaplugin.run;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.sun.javafx.ideaplugin.FxPlugin;
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
        return "Runs a JavaFX Script application";
    }

    public Icon getIcon () {
        return FxPlugin.FX_ICON;
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
