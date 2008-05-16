package com.sun.javafx.ideaplugin;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * FxHighlighter
 *
 * @author Brian Goetz
 */
public class FxHighlighter extends SyntaxHighlighterBase {
    private static Map<IElementType, TextAttributesKey> keys;

    private static final TextAttributesKey FX_KEYWORD
            = TextAttributesKey.createTextAttributesKey("FX.KEYWORD", HighlighterColors.JAVA_KEYWORD.getDefaultAttributes());

    private static final TextAttributesKey FX_STRING
            = TextAttributesKey.createTextAttributesKey("FX.STRING", HighlighterColors.JAVA_STRING.getDefaultAttributes());


    static {
        keys = new HashMap<IElementType, TextAttributesKey>();

        fillMap(keys, FxTokens.KEYWORDS, FX_KEYWORD);
        keys.put(FxTokens.STRING_LITERAL.elementType, FX_STRING);
    }

    @NotNull
    public Lexer getHighlightingLexer() {
        return new FxLexer();
    }

    @NotNull
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(keys.get(tokenType));
    }
}
