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

package com.sun.javafx.ideaplugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerAdapter;
import com.intellij.openapi.util.IconLoader;
import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * FXSupportLoader
 */
public class FxPlugin implements ApplicationComponent {
    public static final Language FX_LANGUAGE = new FxLanguage();
    public static final LanguageFileType FX_FILE_TYPE = new FxFileType();
    public static final String FX_FILE_EXTENSION = "fx";
    public static final String FX_LANGUAGE_NAME = "JavaFX Script";
    public static final Icon FX_CLASS_ICON = IconLoader.getIcon("/icons/fx-class.png");

    public FxPlugin() {
    }

    public void initComponent() {
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            public void run() {
                FileTypeManager.getInstance().registerFileType(FX_FILE_TYPE, FX_FILE_EXTENSION);

                ProjectManager.getInstance().addProjectManagerListener(new ProjectManagerAdapter() {
                    public void projectOpened(Project project) {
                        CompilerManager compilerManager = CompilerManager.getInstance(project);
                        compilerManager.addCompiler(new FxCompiler(project));
                        compilerManager.addCompilableFileType(FX_FILE_TYPE);
                    }
                });
            }
        });
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "FXSupportLoader";
    }
}
