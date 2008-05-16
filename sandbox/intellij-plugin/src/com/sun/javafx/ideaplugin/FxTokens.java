package com.sun.javafx.ideaplugin;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.TokenType;
import com.sun.tools.javafx.antlr.v3Lexer;

/**
 * FxTokens
 *
 * @author Brian Goetz
 */
public enum FxTokens {
    // Simple keywords
    ABSTRACT(v3Lexer.ABSTRACT),
    ASSERT(v3Lexer.ASSERT),
    AT(v3Lexer.AT),
    ATTRIBUTE(v3Lexer.ATTRIBUTE),
    BIND(v3Lexer.BIND),
    BOUND(v3Lexer.BOUND),
    BREAK(v3Lexer.BREAK),
    CLASS(v3Lexer.CLASS),
    CONTINUE(v3Lexer.CONTINUE),
    DELETE(v3Lexer.DELETE),
    FALSE(v3Lexer.FALSE),
    FOR(v3Lexer.FOR),
    FUNCTION(v3Lexer.FUNCTION),
    IF(v3Lexer.IF),
    IMPORT(v3Lexer.IMPORT),
    INDEXOF(v3Lexer.INDEXOF),
    INIT(v3Lexer.INIT),
    INSERT(v3Lexer.INSERT),
    LET(v3Lexer.LET),
    NEW(v3Lexer.NEW),
    NOT(v3Lexer.NOT),
    NULL(v3Lexer.NULL),
    OVERRIDE(v3Lexer.OVERRIDE),
    PACKAGE(v3Lexer.PACKAGE),
    POSTINIT(v3Lexer.POSTINIT),
    PRIVATE(v3Lexer.PRIVATE),
    PROTECTED(v3Lexer.PROTECTED),
    PUBLIC(v3Lexer.PUBLIC),
    READONLY(v3Lexer.READONLY),
    RETURN(v3Lexer.RETURN),
    REVERSE(v3Lexer.REVERSE),
    SUPER(v3Lexer.SUPER),
    SIZEOF(v3Lexer.SIZEOF),
    STATIC(v3Lexer.STATIC),
    THIS(v3Lexer.THIS),
    THROW(v3Lexer.THROW),
    TRY(v3Lexer.TRY),
    TRUE(v3Lexer.TRUE),
    TYPEOF(v3Lexer.TYPEOF),
    VAR(v3Lexer.VAR),
    WHILE(v3Lexer.WHILE),

    // Subsidiary keywords

    AFTER(v3Lexer.AFTER),
    AND(v3Lexer.AND),
    AS(v3Lexer.AS),
    BEFORE(v3Lexer.BEFORE),
    CATCH(v3Lexer.CATCH),
    ELSE(v3Lexer.ELSE),
    EXCLUSIVE(v3Lexer.EXCLUSIVE),
    EXTENDS(v3Lexer.EXTENDS),
    FINALLY(v3Lexer.FINALLY),
    FIRST(v3Lexer.FIRST),
    FROM(v3Lexer.FROM),
    IN(v3Lexer.IN),
    INSTANCEOF(v3Lexer.INSTANCEOF),
    INTO(v3Lexer.INTO),
    INVERSE(v3Lexer.INVERSE),
    LAST(v3Lexer.LAST),
    LAZY(v3Lexer.LAZY),
    ON(v3Lexer.ON),
    OR(v3Lexer.OR),
    REPLACE(v3Lexer.REPLACE),
    STEP(v3Lexer.STEP),
    THEN(v3Lexer.THEN),
    TRIGGER(v3Lexer.TRIGGER),
    WITH(v3Lexer.WITH),
    WHERE(v3Lexer.WHERE),

    // Punctuation

    POUND(v3Lexer.POUND),
    LPAREN(v3Lexer.LPAREN),
    LBRACKET(v3Lexer.LBRACKET),
    LBRACE(v3Lexer.LBRACE),
    PLUSPLUS(v3Lexer.PLUSPLUS),
    SUBSUB(v3Lexer.SUBSUB),
    PIPE(v3Lexer.PIPE),
    DOTDOT(v3Lexer.DOTDOT),
    RPAREN(v3Lexer.RPAREN),
    RBRACKET(v3Lexer.RBRACKET),
    RBRACE(v3Lexer.RBRACE),
    SEMI(v3Lexer.SEMI),
    COMMA(v3Lexer.COMMA),
    DOT(v3Lexer.DOT),
    EQEQ(v3Lexer.EQEQ),
    EQ(v3Lexer.EQ),
    GT(v3Lexer.GT),
    LT(v3Lexer.LT),
    LTGT(v3Lexer.LTGT),
    LTEQ(v3Lexer.LTEQ),
    GTEQ(v3Lexer.GTEQ),
    PLUS(v3Lexer.PLUS),
    SUB(v3Lexer.SUB),
    STAR(v3Lexer.STAR),
    SLASH(v3Lexer.SLASH),
    PERCENT(v3Lexer.PERCENT),
    PLUSEQ(v3Lexer.PLUSEQ),
    SUBEQ(v3Lexer.SUBEQ),
    STAREQ(v3Lexer.STAREQ),
    SLASHEQ(v3Lexer.SLASHEQ),
    PERCENTEQ(v3Lexer.PERCENTEQ),
    COLON(v3Lexer.COLON),
    QUES(v3Lexer.QUES),
    TWEEN(v3Lexer.TWEEN),
    SUCHTHAT(v3Lexer.SUCHTHAT),

    // Literal types

    DECIMAL_LITERAL(v3Lexer.DECIMAL_LITERAL),
    STRING_LITERAL(v3Lexer.STRING_LITERAL),

    // Other

    IDENTIFIER(v3Lexer.IDENTIFIER),
    COMMENT(v3Lexer.COMMENT);

    public static TokenSet KEYWORDS = createTokenSet(ABSTRACT, ASSERT, AT, ATTRIBUTE, BIND, BOUND, BREAK,
            CLASS, CONTINUE, DELETE, FALSE, FOR, FUNCTION, IF, IMPORT, INDEXOF, INIT, INSERT, LET,
            NEW, NOT, NULL, OVERRIDE, PACKAGE, POSTINIT, PRIVATE, PROTECTED, PUBLIC, READONLY, RETURN,
            REVERSE, SUPER, SIZEOF, STATIC, THIS, THROW, TRY, TRUE, TYPEOF, VAR, WHILE);

    public static TokenSet DEPENDENT_KEYWORDS = createTokenSet(AFTER, AND, AS, BEFORE, CATCH, ELSE, EXCLUSIVE,
            EXTENDS, FINALLY, FIRST, FROM, IN, INSTANCEOF, INTO, INVERSE, LAST, LAZY, ON, OR, REPLACE, STEP,
            THEN, TRIGGER, WITH, WHERE);

    public static TokenSet OPERATORS = createTokenSet(POUND, LPAREN, LBRACKET, LBRACE, PLUSPLUS, SUBSUB, PIPE,
            DOTDOT, RPAREN, RBRACKET, RBRACE, SEMI, COMMA, DOT, EQEQ, EQ, GT, LT, LTGT, LTEQ, GTEQ, PLUS, SUB,
            STAR, SLASH, PERCENT, PLUSEQ, SUBEQ, STAREQ, SLASHEQ, PERCENTEQ, COLON, QUES, TWEEN, SUCHTHAT);

    public static TokenSet LITERALS = createTokenSet(DECIMAL_LITERAL, STRING_LITERAL);

    public final int tokenValue;
    public final FxElementType elementType;

    private static FxElementType[] tokenArray;

    static {
        int max = 0;
        for (FxTokens t : FxTokens.values())
            max = Math.max(max, t.tokenValue);
        tokenArray = new FxElementType[max+1];
        for (FxTokens t : FxTokens.values())
            tokenArray[t.tokenValue] = t.elementType;
    }

    FxTokens(int tokenValue) {
        this.tokenValue = tokenValue;
        this.elementType = new FxElementType(name(), FxPlugin.FX_LANGUAGE, tokenValue);
    }

    public IElementType asElementType() {
        return elementType;
    }

    public static TokenSet createTokenSet(FxTokens... tokens) {
        IElementType[] elements = new IElementType[tokens.length];
        for (int i = 0; i < tokens.length; i++)
            elements[i] = tokens[i].asElementType();
        return TokenSet.create(elements);
    }

    public static IElementType getElement(int tokenType) {
        if (tokenType < 0)
            return null; // avoid AIOOBE
        else if (tokenType == v3Lexer.WS)
            return TokenType.WHITE_SPACE;
        else
            return tokenArray[tokenType];
    }
}
