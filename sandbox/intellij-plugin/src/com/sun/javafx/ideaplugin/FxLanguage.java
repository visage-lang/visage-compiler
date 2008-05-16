package com.sun.javafx.ideaplugin;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * FxLanguage
 *
 * @author Brian Goetz
 */
public class FxLanguage extends Language {
    public FxLanguage() {
        super(FxPlugin.FX_LANGUAGE_NAME, "text/plain");
    }

    @NotNull
    public SyntaxHighlighter getSyntaxHighlighter(Project project, final VirtualFile virtualFile) {
        return new FxHighlighter();
    }
}
