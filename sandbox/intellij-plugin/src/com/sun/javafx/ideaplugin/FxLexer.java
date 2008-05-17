package com.sun.javafx.ideaplugin;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.text.CharArrayCharSequence;
import com.intellij.util.text.CharArrayUtil;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javafx.antlr.v3Lexer;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.jetbrains.annotations.Nullable;

/**
 * FxLexer
 *
 * @author Brian Goetz
 */
public class FxLexer extends LexerBase {
    private int bufferStart, bufferEnd;
    private Token nextToken;
    private int curStart, curEnd;
    private CharSequence buffer;
    private v3Lexer lexer;
    private RecognitionException stashedException;

    public void start(char[] buffer, int startOffset, int endOffset, int initialState) {
        start(new CharArrayCharSequence(buffer), startOffset, endOffset, initialState);
    }

    public void start(final CharSequence buffer, int startOffset, int endOffset, final int initialState) {
        System.out.printf("start %d:%d/%d%n", startOffset, endOffset, buffer.length());
        this.buffer = buffer;
        bufferStart = startOffset;
        bufferEnd = endOffset;
        lexer = new v3Lexer(new Context(), new ANTLRStringStream(buffer.toString().substring(startOffset, endOffset))) {
            // Workaround IAE exception in creating diagnostic
            public void displayRecognitionError(String[] strings, RecognitionException recognitionException) {
                stashedException = recognitionException;
            }
        };
        advance();
    }

    public int getState() {
        return 0;
    }

    @Nullable
    public IElementType getTokenType() {
        if (nextToken.getType() == v3Lexer.EOF)
            return null;
        IElementType result = FxTokens.getElement(nextToken.getType());
        if (result == null) {
            System.out.printf("unknown token type %d%n", nextToken.getType());
            return TokenType.BAD_CHARACTER;
        }
        return result;
    }

    public int getTokenStart() {
        return bufferStart + curStart;
    }

    public int getTokenEnd() {
        return bufferStart + curEnd;
    }

    public void advance() {
        curStart = lexer.getCharIndex();
        nextToken = lexer.nextToken();
        if (stashedException != null) {
            lexer.recover(stashedException);
            nextToken = Token.INVALID_TOKEN;
            stashedException = null;
        }
        curEnd = lexer.getCharIndex();
        System.out.printf("Processed %d:%s @ %d:%d/%d%n", nextToken.getType(), FxTokens.getElement(nextToken.getType()), getTokenStart(), getTokenEnd(), bufferEnd);
        if (curEnd == curStart && nextToken.getType() != v3Lexer.EOF)
            System.out.printf("Failed to advance position: %d:%d%n", curStart, curEnd);
    }

    @Deprecated
    public char[] getBuffer() {
        return CharArrayUtil.fromSequence(buffer);
    }

    public int getBufferEnd() {
        return bufferEnd;
    }
}

