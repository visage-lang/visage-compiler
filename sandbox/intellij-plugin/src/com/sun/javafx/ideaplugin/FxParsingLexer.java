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

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import com.sun.tools.javafx.antlr.v3Lexer;
import org.jetbrains.annotations.Nullable;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.Token;

/**
 * FxParsingLexer
 *
 * @author Brian Goetz
 */
public class  FxParsingLexer extends LexerBase {
    private WrappedAntlrLexer lexer;
    private int bufferEnd;
    private char[] buffer;
    private int curIndex, size;
    private int[] tokenStart;
    private IElementType[] tokenType;

    @Deprecated
    public void start(char[] buffer, int startOffset, int endOffset, int initialState) {
        assert(startOffset == 0);
        this.buffer = buffer;
        this.bufferEnd = endOffset;
        lexer = new WrappedAntlrLexer(new ANTLRStringStream(buffer, endOffset));

        // Inelegant -- prepare buffers that are way too big and resize at end.  Better to resize dynamically -- tbd.
        tokenStart = new int[endOffset];
        tokenType = new IElementType[endOffset];
        curIndex = 0;
        size = 0;

        while (true) {
            tokenStart[curIndex] = lexer.getCharIndex();
            Token t;
            try {
                t = lexer.nextToken();
            } catch (RecognitionExceptionSignal s) {
                lexer.recover(s.exception);
                t = Token.INVALID_TOKEN;
            }
            if (t.getType() == v3Lexer.EOF) {
                size = curIndex + 1;
                int[] tempInts = new int[size];
                System.arraycopy(tokenStart, 0, tempInts, 0, size);
                tokenStart = tempInts;
                IElementType[] tempTokens = new IElementType[size];
                System.arraycopy(tokenType, 0, tempTokens, 0, size);
                tokenType = tempTokens;
                break;
            }
            tokenType[curIndex++] = FxTokens.getElement(t.getType());
        }

        lexer.reset();
        curIndex = 0;
    }

    public WrappedAntlrLexer detachLexer() {
        WrappedAntlrLexer retVal = lexer;
        lexer = null;
        return retVal;
    }

    public int getState() {
        return 0;
    }

    public int getIndex() {
        return curIndex;
    }

    @Nullable
    public IElementType getTokenType() {
        return (curIndex < size) ? tokenType[curIndex] : null;
    }

    public int getTokenStart() {
        return (curIndex < size) ? tokenStart[curIndex] : bufferEnd;
    }

    public int getTokenEnd() {
        return curIndex >= size ? bufferEnd : tokenStart[curIndex+1];
    }

    public void advance() {
        ++curIndex;
    }

    @Deprecated
    public char[] getBuffer() {
        return buffer;
    }

    public int getBufferEnd() {
        return bufferEnd;
    }
}
