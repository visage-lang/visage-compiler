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
    
    public String getErrorMessage(RecognitionException e, String[] tokenNames) {
        java.util.List stack = getRuleInvocationStack(e, this.getClass().getName());
        String msg = null;
        if (e instanceof NoViableAltException) {
            NoViableAltException nvae = (NoViableAltException) e;
            msg = " no viable alt; token=" + e.token + " (decision=" 
                    + nvae.decisionNumber + " state " + nvae.stateNumber + ")" 
                    + " decision=<<" + nvae.grammarDecisionDescription + ">>";
        } else {
            msg = super.getErrorMessage(e, tokenNames);
        }
        return stack + " " + msg;
    }

/**
    public String getTokenErrorDisplay(Token t) {
        return t.toString();
    }
**/

    public abstract JCCompilationUnit module() throws RecognitionException;
    
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
