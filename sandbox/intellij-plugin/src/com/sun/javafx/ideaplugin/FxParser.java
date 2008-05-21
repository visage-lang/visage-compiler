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

import com.intellij.lang.PsiParser;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.sun.tools.javafx.antlr.v3Parser;
import org.jetbrains.annotations.NotNull;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * FxParser
 *
 * @author Brian Goetz
 */
public class FxParser implements PsiParser {
    private final FxParsingLexer lexer;

    public FxParser(FxParsingLexer lexer) {
        this.lexer = lexer;
    }

    @NotNull
    public ASTNode parse(IElementType rootElement, PsiBuilder psiBuilder) {
        final AtomicInteger errors = new AtomicInteger();
        v3Parser parser = new v3Parser(new CommonTokenStream(lexer.detachLexer())) {
            public void displayRecognitionError(String[] strings, RecognitionException e) {
                errors.incrementAndGet();
            }
        };
        try {
            v3Parser.module_return antlrParseTree = parser.module();
            if (errors.get() == 0) {
                PsiBuilder.Marker rootMarker = psiBuilder.mark();
                traverse((CommonTree) antlrParseTree.getTree(), psiBuilder);
                rootMarker.done(rootElement);
                return psiBuilder.getTreeBuilt();
            }
            else {
                PsiBuilder.Marker rootMarker = psiBuilder.mark();
                traverse((CommonTree) antlrParseTree.getTree(), psiBuilder);
                rootMarker.done(rootElement);
                return psiBuilder.getTreeBuilt();
            }
        } catch (RecognitionException e) {
            throw new RuntimeException("Unexpected exception in parsing", e);
        }
    }

    private void traverse(CommonTree tree, PsiBuilder builder) {
        System.out.printf("Token %s at %d:%d%n", tree.getToken(), tree.getTokenStartIndex(), tree.getTokenStopIndex());
        if (tree.getTokenStartIndex() > tree.getTokenStopIndex())
            return;
        if (tree.getTokenStartIndex() < 0)
            return;
        skipTo(builder, tree.getTokenStartIndex());
        PsiBuilder.Marker m = builder.mark();
        for (int i=0; i<tree.getChildCount(); i++)
            traverse((CommonTree) tree.getChild(i), builder);
        skipTo(builder, Math.min(lexer.getMaxIndex(), tree.getTokenStopIndex()));
        m.done(FxAstNodes.GENERIC_NODE.elementType);
    }

    private void skipTo(PsiBuilder builder, int skipTo) {
        System.out.println("Skipping to " + skipTo);
        while (lexer.getIndex() < skipTo) {
            builder.getTokenType();
            builder.advanceLexer();
        }
    }
}
