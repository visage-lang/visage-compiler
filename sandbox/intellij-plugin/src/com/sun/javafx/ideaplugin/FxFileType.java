package com.sun.javafx.ideaplugin;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * FxFileType
 *
 * @author Brian Goetz
 */
public class FxFileType extends LanguageFileType {

    public FxFileType() {
        super(FxPlugin.FX_LANGUAGE);
    }

    @NotNull
    @NonNls
    public String getName() {
        return FxPlugin.FX_LANGUAGE_NAME;
    }

    @NotNull
    public String getDescription() {
        return FxPlugin.FX_LANGUAGE_NAME;
    }

    @NotNull
    @NonNls
    public String getDefaultExtension() {
        return FxPlugin.FX_FILE_EXTENSION;
    }

    @Nullable
    public Icon getIcon() {
        return FxPlugin.FX_CLASS_ICON;
    }
}
