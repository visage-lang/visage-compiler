package com.sun.javafx.ideaplugin;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * FxElementType
 *
 * @author Brian Goetz
 */
public class FxElementType extends IElementType {
    public final int antlrToken;

    public FxElementType(@NotNull @NonNls String debugName, @Nullable Language language, int antlrToken) {
        super(debugName, language);
        this.antlrToken = antlrToken;
    }
}
