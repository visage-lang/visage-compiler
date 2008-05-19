package com.sun.javafx.ideaplugin;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
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

    private static final TextAttributesKey FX_NUMERIC
            = TextAttributesKey.createTextAttributesKey("FX.NUMERIC", HighlighterColors.JAVA_NUMBER.getDefaultAttributes());

    private static final TextAttributesKey FX_BAD_CHARACTER
            = TextAttributesKey.createTextAttributesKey("FX.BADCHARACTER", HighlighterColors.BAD_CHARACTER.getDefaultAttributes());

    private static final TextAttributesKey FX_LINE_COMMENT
            = TextAttributesKey.createTextAttributesKey("FX.LINE_COMMENT", HighlighterColors.JAVA_LINE_COMMENT.getDefaultAttributes());

    private static final TextAttributesKey FX_BLOCK_COMMENT
            = TextAttributesKey.createTextAttributesKey("FX.BLOCK_COMMENT", HighlighterColors.JAVA_BLOCK_COMMENT.getDefaultAttributes());

    static {
        keys = new HashMap<IElementType, TextAttributesKey>();

        fillMap(keys, FxTokens.KEYWORDS, FX_KEYWORD);
        fillMap(keys, FxTokens.STRING_LITERALS, FX_STRING);
        fillMap(keys, FxTokens.NUMERIC_LITERALS, FX_NUMERIC);
        keys.put(TokenType.BAD_CHARACTER, FX_BAD_CHARACTER);
        keys.put(FxTokens.COMMENT.elementType, FX_BLOCK_COMMENT);
        keys.put(FxTokens.LINE_COMMENT.elementType, FX_LINE_COMMENT);
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
