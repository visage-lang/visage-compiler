package com.sun.javafx.ideaplugin;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.text.CharArrayCharSequence;
import com.intellij.util.text.CharArrayUtil;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javafx.antlr.v3Lexer;
import org.antlr.runtime.ANTLRStringStream;
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

    public void start(char[] buffer, int startOffset, int endOffset, int initialState) {
        start(new CharArrayCharSequence(buffer), startOffset, endOffset, initialState);
    }

    public void start(final CharSequence buffer, int startOffset, int endOffset, final int initialState) {
        System.out.printf("reparsing from %d to %d%n", startOffset, endOffset);
        this.buffer = buffer;
        bufferStart = startOffset;
        bufferEnd = endOffset;
        lexer = new v3Lexer(new Context(), new ANTLRStringStream(buffer.toString().substring(startOffset, endOffset)));
        advance();
    }

    public int getState() {
        return 0;
    }

    @Nullable
    public IElementType getTokenType() {
        System.out.printf("Returning %d:%s @ %d:%d%n", nextToken.getType(), FxTokens.getElement(nextToken.getType()), getTokenStart(), getTokenEnd());
        if (nextToken.getType() == v3Lexer.EOF)
            return null;
        IElementType result = FxTokens.getElement(nextToken.getType());
        if (result == null) {
            System.out.println("unknown token type");
            return TokenType.BAD_CHARACTER;
        }
        return result;
    }

    public int getTokenStart() {
        return curStart;
    }

    public int getTokenEnd() {
        return curEnd;
    }

    public void advance() {
        System.out.println("advancing from " + lexer.getCharIndex());
        curStart = lexer.getCharIndex();
        nextToken = lexer.nextToken();
        curEnd = lexer.getCharIndex();
        System.out.printf("Processed %d:%s @ %d:%d%n", nextToken.getType(), FxTokens.getElement(nextToken.getType()), getTokenStart(), getTokenEnd());
    }

    @Deprecated
    public char[] getBuffer() {
        return CharArrayUtil.fromSequence(buffer);
    }

    public int getBufferEnd() {
        return bufferEnd;
    }
}
