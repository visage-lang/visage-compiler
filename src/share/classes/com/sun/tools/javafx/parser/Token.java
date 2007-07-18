/*
 * Copyright 1999-2005 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.parser;

import com.sun.tools.javac.util.Version;

/** An interface that defines codes for Java source tokens
 *  returned from lexical analysis.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
@Version("@(#)Token.java	1.29 07/05/05")
public enum Token {
    EOF,
    ERROR,
    IDENTIFIER,
    ABSTRACT("abstract"),
    AFTER("after"),
    AND("and"),
    AS("as"),
    ASSERT("assert"),
    ATTRIBUTE("attribute"),
    BEFORE("before"),
    BIND("bind"),
    BIBIND("bibind"),
    BREAK("break"),
    BY("by"),
    CATCH("catch"),
    CLASS("class"),
    DELETE("delete"),
    DISTINCT("distinct"),
    DO("do"),
    DUR("dur"),
    EASEBOTH("easeboth"),
    EASEIN("easein"),
    EASEOUT("easeout"),
    
    TIE("tie"),
    STAYS("stays"),
    RETURN("return"),
    THROW("throw"),
    VAR("var"),
    PACKAGE("package"),
    IMPORT("import"),
    FROM("from"),
    LATER("later"),
    TRIGGER("trigger"),
    ON("on"),
    INSERT("insert"),
    INTO("into"),
    FIRST("first"),
    LAST("last"),
    IF("if"),
    THEN("then"),
    ELSE("else"),
    THIS("this"),
    NULL("null"),
    TRUE("true"),
    FALSE("false"),
    FOR("for"),
    UNITINTERVAL("unitinterval"),
    IN("in"),
    FPS("fps"),
    WHILE("while"),
    CONTINUE("continue"),
    LINEAR("linear"),
    MOTION("motion"),
    TRY("try"),
    FINALLY("finally"),
    LAZY("lazy"),
    FOREACH("foreach"),
    WHERE("where"),
    NOT("not"),
    NEW("new"),
    PRIVATE("private"),
    PROTECTED("protected"),
    PUBLIC("public"),
    OPERATION("operation"),
    FUNCTION("function"),
    READONLY("readonly"),
    INVERSE("inverse"),
    TYPE("type"),
    SUPERTYPE("supertype"),
    ORDER("order"),
    INDEX("index"),
    INSTANCEOF("instanceof"),
    INDEXOF("indexof"),
    SELECT("select"),
    SUPER("super"),
    OR("or"),
    SIZEOF("sizeof"),
    REVERSE("reverse"),
    XOR("xor"),
    NUMBERLITERAL,
    INTEGERLITERAL,
    BOOLEANLITERAL,
    INTLITERAL,
    LONGLITERAL,
    FLOATLITERAL,
    DOUBLELITERAL,
    CHARLITERAL,
    STRINGLITERAL,
    QUOTE_LBRACE_STRINGLITERAL,
    RBRACE_QUOTE_STRINGLITERAL,
    RBRACE_LBRACE_STRINGLITERAL,
    FORMAT_STRINGLITERAL,
    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),
    LBRACKET("["),
    RBRACKET("]"),
    SEMI(";"),
    COMMA(","),
    DOT("."),
    EQEQ("=="),
    EQ("="),
    GT(">"),
    LT("<"),
    LTGT("<>"),
    LTEQ("<="),
    GTEQ(">="),
    PLUS("+"),
    PLUSPLUS("++"),
    SUB("-"),
    SUBSUB("--"),
    STAR("*"),
    SLASH("/"),
    PERCENT("%"),
    PLUSEQ("+="),
    SUBEQ("-="),
    STAREQ("*="),
    SLASHEQ("/="),
    PERCENTEQ("%="),
    LTLT("<<"),
    GTGT(">>"),
    COLON(":"),
    QUES("?"),
    COMMENT,
    CUSTOM;
    
    Token() {
        this(null);
    }
    Token(String name) {
        this.name = name;
    }
    
    public final String name;
}

