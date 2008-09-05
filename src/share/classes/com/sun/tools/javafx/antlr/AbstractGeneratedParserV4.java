/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
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

package com.sun.tools.javafx.antlr;

import java.util.HashMap;

import com.sun.tools.javac.code.Source;

import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.tree.JCTree;

import com.sun.tools.javac.util.Options;
import com.sun.tools.javafx.tree.JFXInterpolateValue;
import com.sun.tools.javafx.tree.JFXTree;
import com.sun.tools.javafx.tree.JFXErroneous;
import com.sun.tools.javafx.tree.JavafxTreeInfo;
import com.sun.tools.javafx.tree.JavafxTreeMaker;

import com.sun.tools.javafx.util.MsgSym;
import javax.tools.DiagnosticListener;
import org.antlr.runtime.*;

/**
 * Base class for ANTLR generated parsers.
 * This version incorporates error reporting and recovery changes
 * enabled by using ANTLR 3.1.
 * 
 * @author Robert Field
 * @author Jim Idle
 */
public abstract class AbstractGeneratedParserV4 extends Parser {
    
    /** The factory to be used for abstract syntax tree construction.
     */
    protected JavafxTreeMaker  F;
    
    /** The log to be used for error diagnostics.
     */
    protected Log              log;
    
    /** 
     * The Source language setting. 
     */
    protected Source           source;
    
    /** 
     * The token id for white space 
     */
    protected int whiteSpaceToken;
    
    /** 
     * Should the parser generate an end positions map? 
     */
    protected boolean genEndPos;

    /** 
     * The end positions map. 
     * End positions are built by the parser such that each entry in the map
     * is keyed by a JFX tree node built by the parser and the value is
     * the token number in the token stream that correponds to the end position
     * of the node.
     */
    HashMap<JCTree,Integer> endPositions;

    /** 
     * The doc comments map.
     * The documentation comments are comments starting
     * with '/**'. Built by the parser, this map is keyed by the AST
     * node that a comment belongs to and the value is the full text
     * of the comment, including the enclosing '/**' and comment end sequence 
     */
    HashMap<JCTree,String> docComments;

    /**
     * 
     */
    private JavafxTreeInfo treeInfo;
    
    /** 
     * The name table.
     * Keeps track of all the identifiers discovered by the parser in any particular
     * context.
     */
    protected Name.Table names;
    
    /**
     * Defines the human readable names of all the tokens that the lexer
     * can produce for use by error messages and utilities that interact with
     * the user/author.
     */
    protected java.util.Map<String, String> tokenMap = new java.util.HashMap<String, String>(); 
    {
        tokenMap.put("ABSTRACT", "abstract");
        tokenMap.put("ASSERT", "assert");
        tokenMap.put("ATTRIBUTE","attribute");
        tokenMap.put("BIND", "bind");
        tokenMap.put("BOUND", "bound");
        tokenMap.put("BREAK", "break");
        tokenMap.put("CLASS", "class");
        tokenMap.put("CONTINUE", "continue");
        tokenMap.put("DELETE", "delete");
        tokenMap.put("FALSE", "false");
        tokenMap.put("FOR", "for");
        tokenMap.put("FUNCTION", "function");
        tokenMap.put("IF", "if");
        tokenMap.put("IMPORT", "import");
        tokenMap.put("INIT", "init");
        tokenMap.put("INSERT", "insert");
        tokenMap.put("LET", "let");
        tokenMap.put("NEW", "new");
        tokenMap.put("NOT", "not");
        tokenMap.put("NULL", "null");
        tokenMap.put("OVERRIDE", "override");
        tokenMap.put("PACKAGE", "package");
        tokenMap.put("POSTINIT", "postinit");
        tokenMap.put("PRIVATE", "private");
        tokenMap.put("PROTECTED", "protected");
        tokenMap.put("PUBLIC", "public");
        tokenMap.put("READONLY", "readonly");
        tokenMap.put("RETURN", "return");
        tokenMap.put("SUPER", "super");
        tokenMap.put("SIZEOF", "sizeof");
        tokenMap.put("STATIC", "static");
        tokenMap.put("THIS", "this");
        tokenMap.put("THROW", "throw");
        tokenMap.put("TRY", "try");
        tokenMap.put("TRUE", "true");
        tokenMap.put("VAR", "var");
        tokenMap.put("WHILE", "while");
        tokenMap.put("POUND", "#");
        tokenMap.put("LPAREN", "(");
        tokenMap.put("LBRACKET", "[");
        tokenMap.put("PLUSPLUS", "++");
        tokenMap.put("SUBSUB", "--");
        tokenMap.put("PIPE", "|");
        tokenMap.put("AFTER", "after");
        tokenMap.put("AND", "and");
        tokenMap.put("AS", "as");
        tokenMap.put("BEFORE", "before");
        tokenMap.put("CATCH", "catch");
        tokenMap.put("ELSE", "else");
        tokenMap.put("EXCLUSIVE", "exclusive");
        tokenMap.put("EXTENDS", "extends");
        tokenMap.put("FINALLY", "finally");
        tokenMap.put("FIRST", "first");
        tokenMap.put("FROM", "from");
        tokenMap.put("IN", "in");
        tokenMap.put("INDEXOF", "indexof");
        tokenMap.put("INSTANCEOF", "instanceof");
        tokenMap.put("INTO", "into");
        tokenMap.put("INVERSE", "inverse");
        tokenMap.put("LAST", "last");
        tokenMap.put("LAZY", "lazy");
        tokenMap.put("ON", "on");
        tokenMap.put("OR", "or");
        tokenMap.put("REPLACE", "replace");
        tokenMap.put("REVERSE", "reverse");
        tokenMap.put("STEP", "step");
        tokenMap.put("THEN", "then");
        tokenMap.put("TYPEOF", "typeof");
        tokenMap.put("WITH", "with");
        tokenMap.put("WHERE", "where");
        tokenMap.put("DOTDOT", "..");
        tokenMap.put("RPAREN", ")");
        tokenMap.put("RBRACKET", "]");
        tokenMap.put("SEMI", ";");
        tokenMap.put("COMMA", ",");
        tokenMap.put("DOT", ".");
        tokenMap.put("EQEQ", "==");
        tokenMap.put("EQ", "=");
        tokenMap.put("GT", ">");
        tokenMap.put("LT", "<");
        tokenMap.put("LTGT", "<>");
        tokenMap.put("NOTEQ", "!=");
        tokenMap.put("LTEQ", "<=");
        tokenMap.put("GTEQ", ">=");
        tokenMap.put("PLUS", "+");
        tokenMap.put("SUB", "-");
        tokenMap.put("STAR", "*");
        tokenMap.put("SLASH", "/");
        tokenMap.put("PERCENT", "%");
        tokenMap.put("PLUSEQ", "+=");
        tokenMap.put("SUBEQ", "-=");
        tokenMap.put("STAREQ", "*=");
        tokenMap.put("SLASHEQ", "/=");
        tokenMap.put("PERCENTEQ", "%=");
        tokenMap.put("COLON", ":");
        tokenMap.put("QUES", "?");
        tokenMap.put("DoubleQuoteBody", "double quote string literal");
        tokenMap.put("SingleQuoteBody", "single quote string literal");
        tokenMap.put("STRING_LITERAL", "string literal");
        tokenMap.put("NextIsPercent", "%");
        tokenMap.put("QUOTE_LBRACE_STRING_LITERAL", "\" { string literal");
        tokenMap.put("LBRACE", "{");
        tokenMap.put("RBRACE_QUOTE_STRING_LITERAL", "} \" string literal");
        tokenMap.put("RBRACE_LBRACE_STRING_LITERAL", "} { string literal");
        tokenMap.put("RBRACE", "}");
        tokenMap.put("FORMAT_STRING_LITERAL", "format string literal");
        tokenMap.put("TranslationKeyBody", "translation key body");
        tokenMap.put("TRANSLATION_KEY", "translation key");
        tokenMap.put("DECIMAL_LITERAL", "decimal literal");
        tokenMap.put("Digits", "digits");
        tokenMap.put("Exponent", "exponent");
        tokenMap.put("TIME_LITERAL", "time literal");
        tokenMap.put("OCTAL_LITERAL", "octal literal");
        tokenMap.put("HexDigit", "hex digit");
        tokenMap.put("HEX_LITERAL", "hex literal");
        tokenMap.put("RangeDots", "..");
        tokenMap.put("FLOATING_POINT_LITERAL", "floating point literal");
        tokenMap.put("Letter", "letter");
        tokenMap.put("JavaIDDigit", "java ID digit");
        tokenMap.put("IDENTIFIER", "identifier");
        tokenMap.put("WS", "white space");
        tokenMap.put("COMMENT", "comment");
        tokenMap.put("LINE_COMMENT", "line comment");
        tokenMap.put("LAST_TOKEN", "last token");
    } 
    
    /**
     * An array of the human readable names of all the tokens the 
     * lexer can provide to the parser.
     * 
     * This field should be accessed using the getFXTokenNames method
     * @see #getFXTokenNames
     */
    protected String[] fxTokenNames = null;
    
    /**
     * Provides a human readable name for each of the parser grammar rules
     * for use by error messages or any tool that interacts with the user/author
     */
    protected String[][] ruleMap = { 
            {"script",                      "the script contents"},
            {"scriptItems",                 "the script contents"},
            {"scriptItem",                  "the script contents"},
            {"modifers",                    "the modifiers for a declaration ('function', 'var', 'class', etc)"},
            {"modiferFlag",                 "an access modifier"},
            {"packageDecl",                 "a 'package' declaration"},
            {"importDecl",                  "an 'import' declaration"},
            {"importId",                    "an 'import' declaration"},
            {"classDefinition",             "a 'class' declaration"},
            {"supers",                      "the 'extends' part of a 'class' declaration"},
            {"classMembers",                "the members of a 'class' declaration"},
            {"classMember",                 "a 'class' declaration member"},
            {"functionDefinition",          "a function declaration"},
            {"overrideDeclaration",         "an overriden variable"},
            {"initDefinition",              "an 'init' block"},
            {"postInitDefinition",          "a 'postinit' block"},
            {"variableDeclaration",         "a variable declaration"},
            {"formalParameters",            "the parameters of a function declaration"},
            {"formalParameter",             "a parameter"},
            {"block",                       "a block"},
            {"statement",                   "a statement"},
            {"onReplaceClause",             "an 'on replace' clause"},
            {"paramNameOpt",                "an optional parameter name"},
            {"paramName",                   "a parameter name"},
            {"variableLabel",               "a variable declaration"},
            {"throwStatement",              "a 'throw' statement"},
            {"whileStatement",              "a 'while' statement"},
            {"insertStatement",             "an 'insert' statement"},
            {"indexedSequenceForInsert",    "an indexed sequence in an insert statement"},
            {"deleteStatement",             "a 'delete' statement"},
            {"returnStatement",             "a 'return' statement"},
            {"tryStatement",                "a 'try' statement"},
            {"finallyClause",               "a 'finally' clause"},
            {"catchClause",                 "a 'catch' clause"},
            {"boundExpression",             "an expression"},
            {"expression",                  "an expression"},
            {"forExpression",               "a 'for' statement or expression"},
            {"inClause",                    "the 'in' clause of a 'for' expression"},
            {"ifExpression",                "an if statement or expression"},
            {"elseClause",                  "the 'else' clause of an 'if' expression"},
            {"assignmentExpression",        "an assignment"},
            {"assignmentOpExpression",      "an operator assignment expression"},
            {"assignOp",                    "an assignment operator"},
            {"andExpression",               "an expression"},
            {"orExpression",                "an expression"},
            {"typeExpression",              "an expression"},
            {"relationalExpression",        "an expression"},
            {"relOps",                      "a relational operator"},
            {"additiveExpression",          "an expression"},
            {"arithOps",                    "an arithmetic operator"},
            {"multiplicativeExpression",    "an expression"},
            {"multOps",                     "an arithmetic operator"},
            {"unaryExpression",             "an expression"},
            {"unaryOps",                    "a unary operator"},
            {"suffixedExpression",          "an expression"},
            {"postfixExpression",           "an expression"},
            {"primaryExpression",           "an expression"},
            {"keyFrameLiteralPart",         "a frame value expression"},
            {"functionExpression",          "an anonymous function definition"},
            {"newExpression",               "a 'new' expression"},
            {"objectLiteral",               "an object literal definition"},
            {"objectLiteralPart",           "a member of an object literal"},
            {"objectLiteralInit",           "an object literal initializer"},
            {"stringExpression",            "a string expression"},
            {"strCompoundElement",          "a compound string element"},
            {"stringLiteral",               "a string literal"},
            {"qlsl",                        "a compound string element"},
            {"stringExpressionInner",       "an embedded string expression"},
            {"stringFormat",                "a string formatting specification"},
            {"bracketExpression",           "a sequence creation expression"},
            {"expressionList",              "a list of expressions"},
            {"expressionListOpt",           "an optional list of expressions"},
            {"type",                        "a type specification"},
            {"typeArgList",                 "a type specification"},
            {"typeArg",                     "a type specification"},
            {"typeReference",               "a type specification"},
            {"cardinality",                 "a type specification"},
            {"typeName",                    "a type specification"},
            {"genericArgument",             "a type specification"},
            {"literal",                     "a literal constant"},
            {"qualname",                    "a qualified identifier"},
            {"identifier",                  "an identifier"},
            {"name",                        "an identifier"},
    };
    
    /**
     * Local JFX tree node used to build an error node in to the AST
     * when a syntax or semantic error is detected while parsing the
     * script. Error nodes are used by downstream tools such as IDEs
     * so that they can navigate source code even while it is not,
     * strictly speaking, valid code.
     */
    protected JFXErroneous errorTree;
    
    /** 
     * Initializes a new instance of GeneratedParser 
     */
    protected void initialize(Context context) {
       
        this.F          = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        this.log        = Log.instance(context);
        this.names      = Name.Table.instance(context);
        this.source     = Source.instance(context);
        Options options = Options.instance(context);
        this.genEndPos  =    options.get("-Xjcov") != null 
                          || context.get(DiagnosticListener.class) != null 
                          || Boolean.getBoolean("JavafxModuleBuilder.debugBadPositions");
        
        this.treeInfo = (JavafxTreeInfo) JavafxTreeInfo.instance(context);
        
    }
    
    /**
     * Create a new parser instance, pre-supplying the input token stream.
     * @param input The stream of tokens that will be pulled from the lexer
     */
    protected AbstractGeneratedParserV4(TokenStream input) {
        super(input);
    }
    
    /**
     * Create a new parser instance, pre-supplying the input token stream
     * and the shared state.
     * This is only used when a grammar is imported into another grammar.
     * 
     * @param input The stream of tokesn that will be pulled from the lexer
     * @param state The shared state object created by an interconnectd grammar
     */
    protected AbstractGeneratedParserV4(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }
   
    /**
     * An override for the standard ANTLR mismatch method, which is called when
     * the token type predicted for the current input position is not the expected
     * token type.
     * 
     * @param input The input stream whence the token came
     * @param ttype The token type that was found
     * @param follow The bitset that indicates all the tokens that would be valid at this point
     *               in the input stream.
     * @throws org.antlr.runtime.RecognitionException
     */
    @Override
    protected void mismatch(IntStream input, int ttype, BitSet follow)
            throws RecognitionException 
    {
        // Currently this is just a hook back in to the ANTLR
        // default implementation.
        //
        super.mismatch(input, ttype, follow);
    }

    /**
     * Using the supplied grammar rule name, search the rule map
     * and return a user friendly description of the what the
     * rule indicates we must have been parsing at the time of
     * error.
     * 
     * @param ruleName The grammar rule name as supplied by ANTLR error routines
     * @return Friendly form of the rule name for use in messages
     */
    protected String stackPositionDescription(String ruleName) {
        
        // optimize for the non-error case: do sequential search
        //
        for (String[] pair : ruleMap) {
            if (pair[0].equals(ruleName)) {
                
                // We found a rule name that matched where we are on the stack
                // so we can use the description associated with it.
                //
                return pair[1];
            }
        }
        
        // If here then we did not suppyl a specific description
        // for this rule, so we attempt to formulate it into something
        // readable by humans. We wplit the rule name on camel case
        // and predict if this is 'an' or 'a'
        //
        StringBuffer sb = new StringBuffer(ruleName.length()+1);
        switch (ruleName.charAt(0)) {
            case 'a': case 'e': case 'i': case 'o': case 'u': 
                 sb.append("an ");
                break;
            default:
                sb.append("a ");
                break;
        }
        for (char ch : ruleName.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                sb.append(' ');
                sb.append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
    
    /**
     * A translation matrix for converting a particular token classification
     * into a human readable description.
     */
    protected enum TokenClassification {
        KEYWORD {
            String forHumans() {
                return "a keyword";
            }
        },
        DEPRECATED_KEYWORD {
            String forHumans() {
                return "a no longer supported keyword";
            }
        },
        OPERATOR {
            String forHumans() {
                return "an operator";
            }
        }, 
        IDENTIFIER {
            String forHumans() {
                return "an identifier";
            }
        },  
        PUNCTUATION {
              String forHumans() {
                return "a punctuation character";
            }
        },
        UNKNOWN {
            String forHumans() {
                return "a token";
            }
        };
        abstract String forHumans();
    };

    /**
     * 
     */
    protected TokenClassification[] tokenClassMap = new TokenClassification[v4Parser.LAST_TOKEN + 1];
    
    /**
     * Initializer is used to initalize our token class map, which tells
     * error messages and so on how to describe the token to human beings.
     */
    {
        
        // First, set all the token types to UNKNOWN. LAST_TOKEN is an artifical
        // token generated by the parser, so that it is assigned a token number
        // higher than all the lexer defined tokens and we can use it as size
        //
        for (int index = 0; index <= v4Parser.LAST_TOKEN; index += 1) {
            tokenClassMap[index] = TokenClassification.UNKNOWN;
        }
        // Now set the type ourselves, leaving anythign we don't know about yet
        // to show up as UNKNOWN.
        // If a token is removed from the grammar, the corresponding initialization 
        // will fail to compile (which is the earliest we could detect the problem).
        //
        // Keywords:
        //
        tokenClassMap[v4Parser.ABSTRACT]            = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.ASSERT]              = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.ATTRIBUTE]           = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.BIND]                = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.BOUND]               = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.BREAK]               = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.CLASS]               = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.CONTINUE]            = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.DELETE]              = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.FALSE]               = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.FOR]                 = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.FUNCTION]            = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.IF]                  = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.IMPORT]              = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.INIT]                = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.INSERT]              = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.DEF]                 = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.NEW]                 = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.NOT]                 = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.NULL]                = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.OVERRIDE]            = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.PACKAGE]             = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.POSTINIT]            = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.PRIVATE]             = TokenClassification.DEPRECATED_KEYWORD;
        tokenClassMap[v4Parser.PROTECTED]           = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.PUBLIC]              = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.RETURN]              = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.SUPER]               = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.SIZEOF]              = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.STATIC]              = TokenClassification.DEPRECATED_KEYWORD;
        tokenClassMap[v4Parser.THIS]                = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.THROW]               = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.TRY]                 = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.TRUE]                = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.VAR]                 = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.WHILE]               = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.AFTER]               = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.AND]                 = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.AS]                  = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.BEFORE]              = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.CATCH]               = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.ELSE]                = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.EXCLUSIVE]           = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.EXTENDS]             = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.FINALLY]             = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.FIRST]               = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.FROM]                = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.IN]                  = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.INDEXOF]             = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.INSTANCEOF]          = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.INTO]                = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.INVERSE]             = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.LAST]                = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.LAZY]                = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.ON]                  = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.OR]                  = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.REPLACE]             = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.REVERSE]             = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.STEP]                = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.THEN]                = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.TYPEOF]              = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.WITH]                = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.WHERE]               = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.TWEEN]               = TokenClassification.KEYWORD;
        
        // Operators:
        //
        tokenClassMap[v4Parser.PLUSPLUS]            = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.SUBSUB]              = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.PIPE]                = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.DOTDOT]              = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.DOT]                 = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.EQEQ]                = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.EQ]                  = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.GT]                  = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.LT]                  = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.LTGT]                = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.NOTEQ]               = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.LTEQ]                = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.GTEQ]                = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.PLUS]                = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.SUB]                 = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.STAR]                = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.SLASH]               = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.PERCENT]             = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.PLUSEQ]              = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.SUBEQ]               = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.STAREQ]              = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.SLASHEQ]             = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.PERCENTEQ]           = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.QUES]                = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.SUCHTHAT]            = TokenClassification.OPERATOR;
        
        // Punctuation/syntactic sugar:
        //
        tokenClassMap[v4Parser.COLON]               = TokenClassification.PUNCTUATION;
        tokenClassMap[v4Parser.RPAREN]              = TokenClassification.PUNCTUATION;
        tokenClassMap[v4Parser.RBRACKET]            = TokenClassification.PUNCTUATION;
        tokenClassMap[v4Parser.SEMI]                = TokenClassification.PUNCTUATION;
        tokenClassMap[v4Parser.COMMA]               = TokenClassification.PUNCTUATION;
        tokenClassMap[v4Parser.POUND]               = TokenClassification.PUNCTUATION;
        tokenClassMap[v4Parser.LPAREN]              = TokenClassification.PUNCTUATION;
        tokenClassMap[v4Parser.LBRACKET]            = TokenClassification.PUNCTUATION;
        
        
        // Others:
        //
        tokenClassMap[v4Parser.IDENTIFIER]          = TokenClassification.IDENTIFIER;
    }
    
    /**
     * Returns the classification (OPERATOR, PUNCTUATION, etc) of the
     * supplied token. 
     * @param t The token to classify
     * @return The token classification 
     */
    private TokenClassification classifyToken(Token t) {
               
        // Ask ANTLR what the type is
        //
        int tokenType = t.getType();
        
        // And work out what we have
        //
        return classifyToken(tokenType);
    }

    /**
     * Returns the classification (OPERATOR, PUNCTUATION, etc) of the
     * supplied token type
     * @param t The token to classify
     * @return The token classification 
     */
    private TokenClassification classifyToken(int tokenType) {
        
        // Assume that we don't know what this token is
        //
        TokenClassification result = TokenClassification.UNKNOWN;
              
        // And if it is wihtin the range that we know about, then
        // return the classification that we hard coded.
        //
        if ((tokenType >= 0) && tokenType < tokenClassMap.length) {
            result = tokenClassMap[tokenType];
        }
        return result;
    }
    
    /**
     * Returns the parser name, which is really only useful fdor debugging scenarios.
     * @return The name of the parser class
     */
    protected String getParserName() {
        return this.getClass().getName();
    }

    /**
     * Using the given exception generated by the parser, produce an error
     * message string that is geared towards the JAvaFX script author/user.
     * @param e The exception generated by the parser. 
     * @param tokenNames The names of the tokens as generated by ANTLR (unused by this method).
     * @return The human readable error message string.
     */
    @Override
    public String getErrorMessage(RecognitionException e, String[] tokenNames) {
        
        // The rule invocation stack tells us where we are in terms of
        // LL parse and the path throguh the rules that got us to this point.
        // 
        java.util.List stack = getRuleInvocationStack(e, getParserName());
        
        // The top of the stack is the rule that actaully generated the 
        // exception.
        //
        String stackTop = stack.get(stack.size()-1).toString();
        
        // Now we know where we are, we can pick out the human oriented
        // description of what we were tryig to parse.
        //
        String posDescription = stackPositionDescription(stackTop);
        
        // Where we will build the error message string
        //
        StringBuffer mb = new StringBuffer();
        
        // The exact error message we will construct depends on the
        // exception type that was generated. We will be given one of 
        // the following exceptions:
        //
        // UnwantedTokenException   - There was an extra token in the stream that
        //                            we can see was extra because the next token after it
        //                            is the one that would have matched correctly.
        // 
        // MissingTokenException    - There was a missing token in the stream that we see 
        //                            was missing because the token we actually saw was one
        //                            that is a member of the followset had the token been
        //                            present.
        //
        // MismatchedTokenException - The token we received was not one we were expecting, but
        //                            we could neither identify a missing token that would have made it
        //                            something we can deal with, nor that it was just an
        //                            accidental extra token that we can throw away. Something like
        //                            A B C D and we got to B but the token we got was neither 
        //                            C, D nor anything following.
        //
        // NoViableAltException     - The token we saw isn't predicted by any alternative
        //                            path available at this point in the current rule.
        //                            something like:  ... (B|C|D|E) but we got Z which does
        //                            not follow from anywhere.
        //
        // EarlyExitException       - The parser wants one or more of some construct but there
        //                            were none at all in the input stream. Something like
        //                            X SEMI+
        //
        // MismatchedSetException   - The parser would have accepted any one of two or more
        //                            tokens, but the actual token was not in that set and
        //                            was not a token that we could determine was spurious or
        //                            from which we could determine that we just had a token missing.
        //
        // Other exceptions, and some of the above, are dealt with as generic RecognitionExceptions
        //
        
        // Leadin is always the same apology
        //
        mb.append("Sorry, I was trying to understand ");
        mb.append(posDescription);
            
        if (e instanceof UnwantedTokenException) {
         
            // We had an extraneous token in the stream, so we have discarded it
            // for error recovery but still need to report it.
            //
            UnwantedTokenException ute = (UnwantedTokenException) e;
            
            // Inveigh about the extra token
            //
            mb.append(" but I got confused when I found an extra ");
            
            mb.append(getTokenErrorDisplay(ute.getUnexpectedToken()));
            TokenClassification tokenClass = classifyToken(e.token);
            if (tokenClass != TokenClassification.UNKNOWN && tokenClass != TokenClassification.OPERATOR) {
                mb.append(" which is ");
                mb.append(tokenClass.forHumans());
            }
            
            mb.append(" that should not be there");
            
        } else if (e instanceof MissingTokenException) {
            
            // We were able to work out that there was just a single token missing
            // and need to report this like that.
            //
            MissingTokenException mte = (MissingTokenException) e;
            
            // Say what we think is missing
            //
            mb.append(" but I got confused because ");
            
            if  (mte.expecting == Token.EOF)
            {
                mb.append("I was looking for the end of the script here");
                
            } else {
                
                mb.append("you seem to have missed out '");
                mb.append(tokenNames[mte.expecting]);
                mb.append("'");
                TokenClassification tokenClass = classifyToken(mte.expecting);
                if (tokenClass != TokenClassification.UNKNOWN && tokenClass != TokenClassification.OPERATOR) {
                    mb.append(" which is ");
                    mb.append(tokenClass.forHumans());
                }
            
                mb.append(" that should be there");
            }
            
            
        } else if (e instanceof MismatchedTokenException) {
            
            
            MismatchedTokenException mte = (MismatchedTokenException) e;
           
            mb.append(" but I got confused when I saw ");
            mb.append(getTokenErrorDisplay(e.token));
            
            TokenClassification tokenClass = classifyToken(e.token);
            if (tokenClass != TokenClassification.UNKNOWN && tokenClass != TokenClassification.OPERATOR) {
                mb.append(" which is ");
                mb.append(tokenClass.forHumans());
            }
            
            if (tokenClass == TokenClassification.KEYWORD && mte.expecting == v4Parser.IDENTIFIER) {
                
                mb.append(".\n Perhaps you tried to use a keyword as the name of a variable");
                
            } else if (mte.expecting != Token.EOF) {
                mb.append(".\n Perhaps you are missing a ");
                mb.append("'" + tokenNames[mte.expecting]+"'");
            }
            else
            {
                mb.append(".\n I was looking for the end of the script here");
            }
            
        } else if (e instanceof NoViableAltException) {
            
            NoViableAltException nvae = (NoViableAltException) e;
            
            mb.append(" but I got confused when I saw ");
            mb.append(getTokenErrorDisplay(e.token));
            TokenClassification tokenClass = classifyToken(e.token);
            if (tokenClass != TokenClassification.UNKNOWN && tokenClass != TokenClassification.OPERATOR) {
                mb.append(" which is ");
                mb.append(tokenClass.forHumans());
            }
            
        } else if (e instanceof MismatchedSetException) {

            MismatchedSetException mse = (MismatchedSetException)e;
            
            mb.append(" but I got confused when I saw ");
            mb.append(getTokenErrorDisplay(e.token));
            TokenClassification tokenClass = classifyToken(e.token);
            if (tokenClass != TokenClassification.UNKNOWN && tokenClass != TokenClassification.OPERATOR) {
                mb.append(" which is ");
                mb.append(tokenClass.forHumans());
            }
            mb.append(".\n I was looking for one of: "+ mse.expecting);
                    
        } else {
            mb.append( super.getErrorMessage(e, tokenNames) );
        }
        return  mb.toString();
    }

/**
    public String getTokenErrorDisplay(Token t) {
        return t.toString();
    }
**/
    
    /** 
     * Creates the error/warning message that we need to show users/IDEs when
     * ANTLR has found a parsing error, has recovered from it and is now
     * telling us that a parsing exception occurred.
     * 
     * We call our own override of getErrorMessage, and this will build the
     * a string that is geared towards the JavaFX script author. Then we work out
     * where we are in the character stream and record the error using the
     * JavaFX infrastructure.
     */
    @Override
    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {

        // First, where are we. We can use the token that is in error
        // to find out where it is in the input stream as we want to 
        // hightlight the error with respect to that.
        //
        int pos = ((CommonToken)(e.token)).getStartIndex();

        // Now we build the appropriate error message
        //
        String msg = getErrorMessage(e, getFXTokenNames(tokenNames));
        
        // And record the information using hte JavaFX error sink.
        //
        log.error(pos, MsgSym.MESSAGE_JAVAFX_GENERALERROR, msg);
    }
    
    /**
     * Provides a reference to the array of human readable descriptions
     * of each token that the lexer can generate.
     * @param tokenNames The names of the tokens as ANTLR sees them
     * @return An array of human readable descriptions indexewd by the ANTLR generated token type (integer)
     */
    protected String[] getFXTokenNames(String[] tokenNames) {
        
        // If we have already generated this array, then we jsut return the
        // reference to it.
        //
        if (fxTokenNames != null) {
            return fxTokenNames;
        } else {
          
            // This is the first request for the array, so we build it
            // on the fly.
            //
            fxTokenNames = new String[tokenNames.length];
            int count = 0;
            for (String tokenName:tokenNames) {
                String fxTokenName = tokenMap.get(tokenName); 
                if (fxTokenName == null) {
                    fxTokenNames[count] = tokenName;
                } else {
                    fxTokenNames[count] = fxTokenName;
                }
                count++;
            }
            
            return fxTokenNames;
        }
    
    }
    /**
     * Calculates the current character position in the input stream.
     * This method skips whitespace tokens by virtue of using LT(1)
     * which automatically skips off channel tokens. Use when there is
     * no token yet exmined in a rule.
     * 
     * @return The character position of the next non-whitespace token in the input stream
     * 
     */
    protected int pos() {
        
        return pos(input.LT(1));
    }
    
    /**
     * Calculates the character position of the first character of the text
     * in the input stream that the supplied token represents.
     * @param tok The token to locate in the input stream
     * @return The character position of the next non-whitespace token in the input stream
     */
    protected int pos(Token tok) {
        
        return ((CommonToken)tok).getStartIndex();
    }
    
    /**
     * Associate a documentation comment with a particular AST.
     * 
     * The parser keeps a map off all the AST fragements which it has
     * identified has having a documentation comment. This is the
     * method that creates and builds that list as the parser rules
     * find out associations.
     * 
     * @param tree  The tree or tree fragment with which the documentation comment should be associated.
     * @param comment The comment that has been identified as the documentation comment for this tree.
     */
    void setDocComment(JCTree tree, CommonToken comment) {
        if (comment != null) {

            if (docComments == null) {
                docComments = new HashMap<JCTree,String>();
            }
            docComments.put(tree, comment.getText());
        }
    }
    
    
    /***
     * Given a specific starting token, locate the first non-whitespace token
     * that preceeds it, returning it if it is a comment.
     *
     * A number of syntactical constructs can be preceded by a documentatin COMMENT which 
     * is assocaitaed with the construct and should be placed in the AST. Such comments
     * must begin with the introduceer '/**'.
     * This method scans backwards from the supplied token until it finds a token that is 
     * not considered to be WHITESPACE. If the token is a qualifying COMMENT then it is
     * deemed to belong to the construct that asked to locate the comment and is
     * returned to the caller.
     *
     * @param start The token from whence to search backwards in the token stream.
     * @return null if there is no associated comment, the token that contains the
     *         comment, if there is.
     */
    protected CommonToken getDocComment(Token start) {

        // Locate the position of the token before this one in the input stream
        //
        int index = start.getTokenIndex() - 1;

        // Loop backwards through the token stream until
        // we find a token that is not considered to be whitespace
        // or we reach the start of the token stream.
        //
        while (index >= 0) {
            
            Token tok = input.get(index);
            int type;

            // Because modifiers are dealt with uniformly now, we must ignore
            // them when running backwards looking for comments.
            //
            type = tok.getType();
            if (    type == v4Parser.WS         || type == v4Parser.ABSTRACT    || type == v4Parser.BOUND 
                 || type == v4Parser.OVERRIDE   || type == v4Parser.PACKAGE     || type == v4Parser.PROTECTED 
                 || type == v4Parser.PUBLIC     || type == v4Parser.PUBLIC_READ || type == v4Parser.PUBLIC_INIT 
                 
                 //TODO: deprecated -- remove this at some point
                 //
                 || type == v4Parser.STATIC) {
                
                --index;
                
            } else {
                
                break;
            }
        }

        // Assuming that we have found a valid token (not reached the
        // the token stream start, check to see if that token is a COMMENT
        // and return null if it is not.
        //
        if (index < 0 || input.get(index).getType() != v4Parser.COMMENT) {

            return null;
        }

        // We have a valid token, see if it is a documentation
        // comment, rather than just a normal comment.
        //
        CommonToken token = (CommonToken) (input.get(index));
        if (token.getText().startsWith("/**")) {

            // It was a documentation comment so change its type
            // to reflect this, then return it.
            //
            // TODO: JI - Move this type changing into the lexer 
            //
            token.setType(v4Parser.DOC_COMMENT);
            return token;
        }

        // The token was either not a comment or was not a documentation
        // comment.
        //
        return null;
    }

    /**
     * Given a list of interpolation values, create an entry for the supplied AST node
     * in the end position map using the end position of the last AST node in the interpolation
     * value list.
     * 
     * @param tree The AST node that we wish to create an endpos for
     * @param list A list of interpolation value AST nodes.
     */
    void endPos(JCTree tree, com.sun.tools.javac.util.List<JFXInterpolateValue> list) {
        if (genEndPos) {
            int endLast = endPositions.get(list.last());
            endPositions.put(tree, endLast);
        }
    }

	/** Using the current token stream position as the start point
	 *  search back through the input token stream and set the end point
	 *  of the supplied tree object to the first non-whitespace token
	 *  we find.
     * 
     * Note that this version of endPos() is called when all elements of a
     * construct have been parsed. Hence we traverse back from one token
     * before the current index.
	 */
    void endPos(JCTree tree) {

		CommonToken tok;
        
		int index = input.index();
		int end = 0;
		
        // Unless we are at the very start (this should not
        // happen, but is coded for anyway), then the token that
        // ended whatever AST fragment we are constructing was the 
        // one before the one at the current index and so we need
        // to start at that token.
        //
        if  (index > 1) index--;
        
		if	(genEndPos && index > 0)
		{ 
			for(;;)
			{
				// Find the current token
				//
				tok = (CommonToken)input.get(index);
			
				if	(tok == null)
				{	
					// Not much we can do about that - should not
					// happen. 
					//
					break;
				}
				
				if	(tok.getType() == whiteSpaceToken && tok.getType() != Token.EOF)
				{
					index--;
				}
				else
				{
					// We have found a token that is non-whitespace and is not EOF
					//
					endPos(tree, tok.getStopIndex() + 1);
					break;
				}
			}
		
		}
    }
    
    /**
     * Create the end position map entry for the given JCTree at the supplied
     * character inde, which is the offset into the script source.
     * 
     * @param tree The tree for which we are mapping the endpoint
     * @param end The character position in the input stream that matches the end of the tree
     */
    void endPos(JCTree tree, int end) {
        if (genEndPos)
            endPositions.put(tree, end);
    }

    /**
     * 
     * @return
     */
    protected List noJFXTrees() {
        return List.<JFXTree>nil();
    }
    
    /**
     * If the parser is able to recover from the fact that a single token
     * is missing from the input stream, then it will call this method
     * to manufacture a token for use by actions in the grammar.
     * @param input The token stream where we are normally drawing tokens from
     * @param e The exception that was raised by the parser
     * @param expectedTokenType The type of the token that the parser was expecting to see next
     * @param follow The followset of tokens that can follow on from here
     * @return A newly manufactured token of the required type
     */
    @Override
    protected Object getMissingSymbol(IntStream input,
                                      RecognitionException e, 
                                      int expectedTokenType,
                                      BitSet follow) {
        
        if (expectedTokenType == Token.EOF) {
            String tokenText = "<missing EOF>";
            CommonToken t = new CommonToken(expectedTokenType, tokenText);
            Token current = ((TokenStream)input).LT(1);
            if ( current.getType() == Token.EOF ) {
                current = ((TokenStream)input).LT(-1);
            }
            t.setLine(current.getLine());
            t.setCharPositionInLine(current.getCharPositionInLine());
            t.setChannel(DEFAULT_TOKEN_CHANNEL);
            return t;
        } else {
            return super.getMissingSymbol(input, e, expectedTokenType, follow);
        }
    }
}
