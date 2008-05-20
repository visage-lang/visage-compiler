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

import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * FxParserDefinition
 *
 * @author Brian Goetz
 */
public class FxParserDefinition implements ParserDefinition {

    private final Map<Project, FxParsingLexer> lexerMap = Collections.synchronizedMap(new IdentityHashMap<Project, FxParsingLexer>());

    @NotNull
    public Lexer createLexer(Project project) {
        FxParsingLexer lex = lexerMap.get(project);
        if (lex == null) {
            lex = new FxParsingLexer();
            lexerMap.put(project, lex);
        }
        return lex;
    }

    public PsiParser createParser(Project project) {
        return new FxParser(lexerMap.get(project));
    }

    public IFileElementType getFileNodeType() {
        return new IFileElementType(Language.findInstance(FxLanguage.class));
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return FxTokens.WHITESPACE;
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return FxTokens.COMMENTS;
    }

    public PsiFile createFile(FileViewProvider fileViewProvider) {
        return new FxFile(fileViewProvider);
    }

    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode astNode, ASTNode astNode1) {
        return SpaceRequirements.MAY;
    }

    @NotNull
    public PsiElement createElement(ASTNode astNode) {
        return new ASTWrapperPsiElement(astNode);
    }
}
