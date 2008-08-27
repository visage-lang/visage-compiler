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
    protected JavafxTreeMaker F;
    
    /** The log to be used for error diagnostics.
     */
    protected Log log;
    
    /** The Source language setting. */
    protected Source source;
    
    /** The token id for white space */
    protected int whiteSpaceToken;
    
    /** Should the parser generate an end positions map? */
    protected boolean genEndPos;

    /** The end positions map. */
    HashMap<JCTree,Integer> endPositions;

    /** The doc comments map */
    HashMap<JCTree,String> docComments;

    private JavafxTreeInfo treeInfo;
    
    /** The name table. */
    protected Name.Table names;
    
    protected java.util.Map<String, String> tokenMap = new java.util.HashMap<String, String>(); 

    {
        /*
        tokenMap.put("<invalid>", "<invalid>");
        tokenMap.put("<EOR>","<EOR>");
        tokenMap.put("<DOWN>", "<DOWN>");
        tokenMap.put("<UP>", "<UP>");
        tokenMap.put("SEMI_INSERT_START", "SEMI_INSERT_START");
        */
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
        /*
        tokenMap.put("SEMI_INSERT_END", "SEMI_INSERT_END");
        */
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
        /* imaginary tokens
        tokenMap.put("MODULE", "MODULE");
        tokenMap.put("MODIFIER", "MODIFIER");
        tokenMap.put("CLASS_MEMBERS", "CLASS_MEMBERS");
        tokenMap.put("PARAM", "PRAM");
        tokenMap.put("FUNC_EXPR", "FUNC_EXPR");
        tokenMap.put("STATEMENT", "STATEMENT");
        tokenMap.put("EXPRESSION", "EXPRESSION");
        tokenMap.put("BLOCK", "BLOCK");
        tokenMap.put("MISSING_NAME", "MISSING_NAME");
        tokenMap.put("SLICE_CLAUSE", "SLICE_CLAUSE");
        tokenMap.put("ON_REPLACE_SLICE", "ON_REPLACE_SLICE");
        tokenMap.put("ON_REPLACE", "ON_REPLACE");
        tokenMap.put("ON_REPLACE_ELEMENT", "ON_REPLACE_ELEMENT");
        tokenMap.put("ON_INSERT_ELEMENT", "ON_INSERT_ELEMENT");
        tokenMap.put("ON_DELETE_ELEMENT", "ON_DELETE_ELEMENT");
        tokenMap.put("EXPR_LIST", "EXPR_LIST");
        tokenMap.put("FUNC_APPLY", "FUNC_APPLY");
        tokenMap.put("NEGATIVE", "NEGATIVE");
        tokenMap.put("POSTINCR", "POSTINCR");
        tokenMap.put("POSTDECR", "POSTDECR");
        tokenMap.put("SEQ_INDEX", "SEQ_INDEX");
        tokenMap.put("SEQ_SLICE", "SEQ_SLICE");
        tokenMap.put("SEQ_SLICE_EXCLUSIVE", "SEQ_SLICE_EXCLUSIVE");
        tokenMap.put("OBJECT_LIT", "OBJECT_LIT");
        tokenMap.put("OBJECT_LIT_PART", "OBJECT_LIT_PART");
        tokenMap.put("SEQ_EMPTY", "SEQ_EMPTY");
        tokenMap.put("SEQ_EXPLICIT","SEQ_EXPLICIT");
        tokenMap.put("EMPTY_FORMAT_STRING", "EMPTY_FORMAT_STRING");
        tokenMap.put("TYPE_NAMED", "TYPE_NAMED");
        tokenMap.put("TYPE_FUNCTION", "TYPE_FUNCTION");
        tokenMap.put("TYPE_ANY", "TYPE_ANY");
        tokenMap.put("TYPE_UNKNOWN", "TYPE_UNKNOWN");
        tokenMap.put("TYPE_ARG", "TYPE_ARG");
        tokenMap.put("TYPED_ARG_LIST", "TYPED_ARG_LIST");
        tokenMap.put("DOC_COMMENT", "DOC_COMMENT");
        */
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
    
    // this field should not be accessed using the getFXTokenNames method
    protected String[] fxTokenNames = null;
    
    protected String[][] ruleMap = { 
            {"script", "the script contents"},
            {"scriptItems", "the script contents"},
            {"packageDecl", "a package declaration"},
            {"importDecl", "an import declaration"},
            {"importId", "an import declaration"},
            {"classDefinition", "a class declaration"},
            {"overrideDeclaration", "an overriden variable"},
            {"supers", "the 'extends' part of a class declaration"},
            {"classMembers", "the members of a class declaration"},
            {"classMember", "the members of a class declaration"},
            {"functionDefinition", "a function declaration"},
            {"initDefinition", "an 'init' block"},
            {"postInitDefinition", "a 'postinit' block"},
            {"modifers", " the modifiers for a declaration (function, var, class, etc)"},
            {"modiferFlag", "an access modifier"},
            {"formalParameters", " the parameters of a function declaration"},
            {"formalParameter", " a formal parameter"},
            {"block", "a block"},
            {"blockComponent", "a component of a block"},
            {"variableDeclaration", "a variable declaration"},
            {"variableLabel", "a variable declaration"},
            {"boundExpression", "an expression"},
            {"inClause", "the 'in' clause of a 'for' expression"},
            {"elseClause", "the 'else' clause of an 'if' expression"},
            {"assignmentOpExpression", "an operator assignment expression"},
            {"primaryExpression", "an expression"},
            {"stringExpressionInner", "a string expression"},
            {"bracketExpression", "a sequence creation expression"},
            {"expressionList", "a list of expressions"},
            {"expressionListOpt", "a list of expressions"},
            {"type", "a type specification"},
            {"typeArgList", "a type specification"},
            {"typeArg", "a type specification"},
            {"typeReference", "a type specification"},
            {"cardinality", "a type specification"},
            {"typeName", "a type specification"},
            {"genericArgument", "a type specification"},
            {"qualident", "an identifier"},
            {"name", "an identifier"},
            {"paramNameOpt", "an optional identifier"} 
    };
    
    
    /* ---------- error recovery -------------- */
    
    protected JFXErroneous errorTree;
    
    /** initializes a new instance of GeneratedParser */
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
    
    protected AbstractGeneratedParserV4(TokenStream input) {
        super(input);
    }
    
    protected AbstractGeneratedParserV4(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }
   
    @Override
    protected void mismatch(IntStream input, int ttype, BitSet follow)
            throws RecognitionException {
        //System.err.println("Mismatch: " + ttype  + ", Set: " + follow);
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
                return pair[1];
            }
        }
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
        UNKNOWN {
            String forHumans() {
                return "a token";
            }
        };
        abstract String forHumans();
    };

    protected TokenClassification[] tokenClassMap = new TokenClassification[v4Parser.LAST_TOKEN + 1];
    {
        // Initiailization block.
        //     First, set them all to UNKNOWN
        for (int index = 0; index <= v4Parser.LAST_TOKEN; index += 1) {
            tokenClassMap[index] = TokenClassification.UNKNOWN;
        }
        //     Then set them appropriately.
        //     If a token is added to the grammar, it will show up as UNKNOWN.
        //     If a token is removed from the grammar, the corresponding initialization 
        //       will fail to compile (which is the earliest we could detect the problem).
        // Keywords:
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
        tokenClassMap[v4Parser.NON_WRITABLE]        = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.NOT]                 = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.NULL]                = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.OVERRIDE]            = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.PACKAGE]             = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.POSTINIT]            = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.PRIVATE]             = TokenClassification.DEPRECATED_KEYWORD;
        tokenClassMap[v4Parser.PROTECTED]           = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.PUBLIC]              = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.PUBLIC_READABLE]     = TokenClassification.KEYWORD;
        tokenClassMap[v4Parser.READABLE]            = TokenClassification.DEPRECATED_KEYWORD;
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
        tokenClassMap[v4Parser.POUND]               = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.LPAREN]              = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.LBRACKET]            = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.PLUSPLUS]            = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.SUBSUB]              = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.PIPE]                = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.DOTDOT]              = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.RPAREN]              = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.RBRACKET]            = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.SEMI]                = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.COMMA]               = TokenClassification.OPERATOR;
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
        tokenClassMap[v4Parser.COLON]               = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.QUES]                = TokenClassification.OPERATOR;
        tokenClassMap[v4Parser.SUCHTHAT]            = TokenClassification.OPERATOR;
        // Others:
        tokenClassMap[v4Parser.IDENTIFIER]          = TokenClassification.IDENTIFIER;
    }

    private TokenClassification classifyToken(Token t) {
        TokenClassification result = TokenClassification.UNKNOWN;
        int tokenType = t.getType();
        if ((tokenType >= 0) && tokenType < tokenClassMap.length) {
            result = tokenClassMap[tokenType];
        }
        return result;
    }

    protected String getParserName() {
        return this.getClass().getName();
    }

    @Override
    public String getErrorMessage(RecognitionException e, String[] tokenNames) {
        java.util.List stack = getRuleInvocationStack(e, getParserName());
        String stackTop = stack.get(stack.size()-1).toString();
        String posDescription = stackPositionDescription(stackTop);
        StringBuffer mb = new StringBuffer();
        if (e instanceof MismatchedTokenException) {
            MismatchedTokenException mte = (MismatchedTokenException) e;
           
            mb.append("Sorry, I was trying to understand ");
            mb.append(posDescription);
            mb.append(" but I got confused when I saw ");
            mb.append(getTokenErrorDisplay(e.token));
            TokenClassification tokenClass = classifyToken(e.token);
            if (tokenClass != TokenClassification.UNKNOWN && tokenClass != TokenClassification.OPERATOR) {
                mb.append(" which is ");
                mb.append(tokenClass.forHumans());
            }
            if (mte.expecting != Token.EOF) {
                mb.append(".\n Perhaps you are missing a ");
                mb.append("'" + tokenNames[mte.expecting]+"'");
            }
        } else if (e instanceof NoViableAltException) {
            NoViableAltException nvae = (NoViableAltException) e;
            
            mb.append("Sorry, I was trying to understand ");
            mb.append(posDescription);
            mb.append(" but I got confused when I saw ");
            mb.append(getTokenErrorDisplay(e.token));
            TokenClassification tokenClass = classifyToken(e.token);
            if (tokenClass != TokenClassification.UNKNOWN && tokenClass != TokenClassification.OPERATOR) {
                mb.append(" which is ");
                mb.append(tokenClass.forHumans());
            }
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
    
    /** What is the error header, normally line/character position information? */
    @Override
    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
        int pos = ((CommonToken)(e.token)).getStartIndex();
        // String msg = getErrorMessage(e, tokenNames);
        //        System.err.println("ERROR: " + msg);
        
        String msg = getErrorMessage(e, getFXTokenNames(tokenNames));
        log.error(pos, MsgSym.MESSAGE_JAVAFX_GENERALERROR, msg);
    }
    
    protected String[] getFXTokenNames(String[] tokenNames) {
        
        if (fxTokenNames != null) {
            return fxTokenNames;
        } else {
            
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
     * @return The character position of the next non-whitespace token
     * 
     */
    protected int pos() {
        //System.out.println("TOKEN: line: " + tok.getLine() + " char: " + tok.getCharPositionInLine() + " pos: " + ((CommonToken)tok).getStartIndex());
        return ((CommonToken)(input.LT(1))).getStartIndex();
    }
    
    protected int pos(Token tok) {
        //System.out.println("TOKEN: line: " + tok.getLine() + " char: " + tok.getCharPositionInLine() + " pos: " + ((CommonToken)tok).getStartIndex());
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
     * Create the end position map for the given JCTree at the supplied\
     * character index.
     * @param tree The tree for which we are mapping the endpoint
     * @param end The character positoin in the input stream that matches the end of the tree
     */
    void endPos(JCTree tree, int end) {
        if (genEndPos)
            endPositions.put(tree, end);
    }

    protected List noJFXTrees() {
        return List.<JFXTree>nil();
    }
    
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
