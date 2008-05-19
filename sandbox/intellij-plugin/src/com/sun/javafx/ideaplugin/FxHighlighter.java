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
