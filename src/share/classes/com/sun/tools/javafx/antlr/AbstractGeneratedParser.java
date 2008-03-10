/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.antlr;

import com.sun.tools.javafx.tree.JavafxTreeMaker;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;

import com.sun.tools.javac.code.*;
import com.sun.tools.javac.util.*;
import static com.sun.tools.javac.util.ListBuffer.lb;

import org.antlr.runtime.*;

/**
 * Base class for ANTLR generated parsers 
 * 
 * @author Robert Field
 */
public abstract class AbstractGeneratedParser extends Parser {
    
    /** The factory to be used for abstract syntax tree construction.
     */
    protected JavafxTreeMaker F;
    
    /** The log to be used for error diagnostics.
     */
    protected Log log;
    
    /** The Source language setting. */
    protected Source source;
    
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
            {"module", "the module contents"},
            {"moduleItems", "the module contents"},
            {"moduleItem", "the module contents"},
            {"packageDecl", "a package declaration"},
            {"importDecl", "an import declaration"},
            {"importId", "an import declaration"},
            {"classDefinition", "a class declaration"},
            {"supers", "the 'extends' part of a class declaration"},
            {"classMembers", "the members of a class declaration"},
            {"classMember", "the members of a class declaration"},
            {"functionDefinition", "a function declaration"},
            {"initDefinition", "an 'init' block"},
            {"postInitDefinition", "a 'postinit' block"},
            {"functionModifierFlags", " the modifiers on a function declaration"},
            {"functionModifier", " the modifiers on a function declaration"},
            {"varModifierFlags", " the modifiers on an attribute/var declaration"},
            {"varModifier", " the modifiers on an attribute/var declaration"},
            {"classModifierFlags", " the modifiers on a class declaration"},
            {"classModifier", " the modifiers on a class declaration"},
            {"accessModifier", "an access modifier"},
            {"formalParameters", " the parameters of a function declaration"},
            {"formalParameter", " a formal parameter"},
            {"blockExpression", "a block expression"},
            {"blockComponent", "a component of a block"},
            {"variableDeclaration", "an attribute/variable declaration"},
            {"variableLabel", "an attribute/variable declaration"},
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
    
    protected JCErroneous errorTree;
    
    /** initializes a new instance of GeneratedParser */
    protected void initialize(Context context) {
        this.F = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        this.log = Log.instance(context);
        this.names = Name.Table.instance(context);
        this.source = Source.instance(context);
    }
    
    protected AbstractGeneratedParser(TokenStream input) {
        super(input);
    }
    
    protected AbstractGeneratedParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }
   
    protected void mismatch(IntStream input, int ttype, BitSet follow)
            throws RecognitionException {
        //System.err.println("Mismatch: " + ttype  + ", Set: " + follow);
        super.mismatch(input, ttype, follow);
    }

protected String stackPositionDescription(String ruleName) {
        // optimize for the non-error case: do sequential search
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
    
    public String getErrorMessage(RecognitionException e, String[] tokenNames) {
        java.util.List stack = getRuleInvocationStack(e, this.getClass().getName());
        String stackTop = stack.get(stack.size()-1).toString();
        String posDescription = stackPositionDescription(stackTop);
        StringBuffer mb = new StringBuffer();
        if (e instanceof MismatchedTokenException) {
            MismatchedTokenException mte = (MismatchedTokenException) e;
           
            mb.append("Sorry, I was trying to understand ");
            mb.append(posDescription);
            mb.append(" but I got confused when I saw ");
            mb.append(getTokenErrorDisplay(e.token));
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
        log.error(pos, "javafx.generalerror", msg);
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
    
    protected int pos(Token tok) {
        //System.out.println("TOKEN: line: " + tok.getLine() + " char: " + tok.getCharPositionInLine() + " pos: " + ((CommonToken)tok).getStartIndex());
        return ((CommonToken)tok).getStartIndex();
    }
    
    protected List noJCTrees() {
        return List.<JCTree>nil();
    }
    
    protected List<JCAnnotation> noJCAnnotations() {
        return List.<JCAnnotation>nil();
    }
}
