package com.sun.javafx.ideaplugin;

import com.intellij.lang.Language;

/**
 * FxLanguage
 *
 * @author Brian Goetz
 */
public class FxLanguage extends Language {
    public FxLanguage() {
        super(FxPlugin.FX_LANGUAGE_NAME, "text/plain");
    }
}
