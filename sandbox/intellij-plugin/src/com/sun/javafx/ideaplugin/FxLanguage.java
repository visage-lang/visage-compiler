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

import com.intellij.lang.Language;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.lang.BracePair;
import com.intellij.lang.ParserDefinition;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * FxLanguage
 *
 * @author Brian Goetz
 */
public class FxLanguage extends Language {

    private FxParserDefinition definition = new FxParserDefinition();

    public FxLanguage() {
        super(FxPlugin.FX_LANGUAGE_NAME, "text/plain");
    }

    @NotNull
    public SyntaxHighlighter getSyntaxHighlighter(Project project, final VirtualFile virtualFile) {
        return new FxHighlighter();
    }

//    @Nullable
//    public ParserDefinition getParserDefinition() {
//        return definition;
//    }

    @Nullable
    public PairedBraceMatcher getPairedBraceMatcher() {
        return new PairedBraceMatcher() { 
            private final BracePair[] PAIRS = new BracePair[] {
                    new BracePair('(',FxTokens.LPAREN.elementType, ')', FxTokens.RPAREN.elementType, false),
                    new BracePair('[',FxTokens.LBRACKET.elementType, ']', FxTokens.RBRACKET.elementType, false),
                    new BracePair('{',FxTokens.LBRACE.elementType, '}', FxTokens.RBRACE.elementType, true)
            };

            public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType iElementType, @Nullable IElementType iElementType1) {
                return true;
            }

            public BracePair[] getPairs() {
                return PAIRS;
            }
        };
    }
}
