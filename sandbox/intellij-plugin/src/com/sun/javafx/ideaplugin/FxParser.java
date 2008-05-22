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

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.sun.tools.javafx.antlr.v3Parser;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

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
        List<StreamAction> actions = new ArrayList<StreamAction>();
        final List<ParseError> errors = new ArrayList<ParseError>();

        System.out.printf("%s/%s: starting parsing %d tokens, %d chars %n", Thread.currentThread(), lexer, lexer.getSize(), lexer.getBufferEnd());
        // Potentially inefficient; creating a new ANTLR lexer instead of reusing the one we have
        WrappedAntlrLexer antlrLexer = new WrappedAntlrLexer(new ANTLRStringStream(lexer.getBufferSequence().toString().substring(0, lexer.getBufferEnd())), false);
        v3Parser parser = new v3Parser(new CommonTokenStream(antlrLexer)) {
            protected String getParserName() {
                return "com.sun.tools.javafx.antlr.v3Parser";
            }

            public void displayRecognitionError(String[] strings, RecognitionException e) {
                errors.add(new ParseError(e, getErrorMessage(e, strings)));
            }
        };
        try {
            v3Parser.module_return antlrParseTree = parser.module();
            scrubTree((CommonTree) antlrParseTree.getTree());
            System.out.printf("finished parsing %d:%d%n", ((CommonTree) antlrParseTree.getTree()).getTokenStartIndex(), ((CommonTree) antlrParseTree.getTree()).getTokenStopIndex());
            BeginMark beginMark = new BeginMark(0);
            actions.add(beginMark);
            if (errors.size() == 0) {
                // @@@ Do the same with v3Walker, so we can get real structural information
                traverse((CommonTree) antlrParseTree.getTree(), actions);
            } else {
                for (ParseError error : errors) {
                    BeginMark beginErrorMark = new BeginMark(error.exception.token.getTokenIndex());
                    actions.add(beginErrorMark);
                    actions.add(new ErrorMark(error.exception.token.getTokenIndex() + 1, beginErrorMark, error.errorString));
                }
            }
            actions.add(new EndMark(lexer.getSize(), beginMark, rootElement));
            applyActions(actions, psiBuilder);
            return psiBuilder.getTreeBuilt();
        } catch (RecognitionException e) {
            throw new RuntimeException("Unexpected exception in parsing", e);
        }
    }

    private void scrubTree(Tree tree) {
        // The top-level tree often has wrong position info, so start one down from the top
        for (int i = tree.getChildCount() - 1; i >= 0; i--) {
            Tree child = tree.getChild(i);
            if (child.getTokenStartIndex() > child.getTokenStopIndex()) {
                System.out.println("Deleting node " + child);
                tree.deleteChild(i);
                continue;
            }
            if (child.getTokenStartIndex() < 0) {
//                System.out.println("Deleting node " + child);
//                tree.deleteChild(i);
                continue;
            }
            scrubTree(child);
            if (child.getTokenStartIndex() < tree.getTokenStartIndex()) {
                tree.setTokenStartIndex(child.getTokenStartIndex());
            }
            if (child.getTokenStopIndex() > tree.getTokenStopIndex()) {
                tree.setTokenStopIndex(child.getTokenStopIndex());
            }
        }
    }

    private void traverse(CommonTree tree, List<StreamAction> actions) {
        System.out.printf("Token %s at %d:%d%n", tree.getToken(), tree.getTokenStartIndex(), tree.getTokenStopIndex());
        if (tree.getTokenStartIndex() > tree.getTokenStopIndex())
            return;
        if (tree.getTokenStartIndex() < 0)
            return;
        BeginMark beginMark = new BeginMark(tree.getTokenStartIndex());
        actions.add(beginMark);
        for (int i = 0; i < tree.getChildCount(); i++)
            traverse((CommonTree) tree.getChild(i), actions);
        actions.add(new EndMark(tree.getTokenStopIndex() + 1, beginMark, FxAstNodes.GENERIC_NODE.elementType));
    }


    private void applyActions(List<StreamAction> actions, PsiBuilder builder) {
        for (StreamAction action : actions) {
            while (lexer.getIndex() < action.position && !builder.eof()) {
                builder.getTokenType();
                builder.advanceLexer();
            }
            action.action(builder);
        }
        while (!builder.eof()) {
            builder.getTokenType();
            builder.advanceLexer();
        }
    }

    private static class ParseError {
        public final RecognitionException exception;
        public final String errorString;

        private ParseError(RecognitionException exception, String errorString) {
            this.exception = exception;
            this.errorString = errorString;
        }
    }

    private static abstract class StreamAction {
        protected final int position;

        protected StreamAction(int position) {
            this.position = position;
        }

        public abstract void action(PsiBuilder builder);
    }

    private static class BeginMark extends StreamAction {
        public PsiBuilder.Marker marker;

        public BeginMark(int position) {
            super(position);
        }

        public void action(PsiBuilder builder) {
            marker = builder.mark();
        }
    }

    private static class EndMark extends StreamAction {
        private final BeginMark marker;
        private final IElementType elementType;

        public EndMark(int position, BeginMark marker, IElementType elementType) {
            super(position);
            this.marker = marker;
            this.elementType = elementType;
        }

        public void action(PsiBuilder builder) {
            marker.marker.done(elementType);
        }
    }

    private static class ErrorMark extends StreamAction {
        private final BeginMark marker;
        private final String errorString;

        public ErrorMark(int position, BeginMark marker, String errorString) {
            super(position);
            this.marker = marker;
            this.errorString = errorString;
        }

        public void action(PsiBuilder builder) {
            marker.marker.error(errorString);
        }
    }
}
