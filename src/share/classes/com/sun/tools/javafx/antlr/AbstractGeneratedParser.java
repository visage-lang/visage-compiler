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
            {"name", "an identifier"} 
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
                mb.append(tokenNames[mte.expecting]);
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
        String msg = getErrorMessage(e, tokenNames);
        //        System.err.println("ERROR: " + msg);
        log.error(pos, "javafx.generalerror", msg);
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
