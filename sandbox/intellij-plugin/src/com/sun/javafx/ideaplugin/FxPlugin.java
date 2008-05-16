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
