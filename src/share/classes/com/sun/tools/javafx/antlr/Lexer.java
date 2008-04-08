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
import com.sun.tools.javac.util.Position;

import org.antlr.runtime.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Base class for ANTLR generated parsers 
 * 
 * @author Robert Field
 * @author Zhiqun Chen
 */
public abstract class Lexer extends org.antlr.runtime.Lexer {
    
    /** The log to be used for error diagnostics.
     */
    protected Log log;
    protected int previousTokenType = -1;
    List<Token> tokens = new ArrayList<Token>();
    private final BraceQuoteTracker NULL_BQT = new BraceQuoteTracker(null, '\'', false);
    private BraceQuoteTracker quoteStack = NULL_BQT;
      
    protected Lexer(){};
    
    protected Lexer (CharStream input) {
        super(input);
    }
    
    protected Lexer (CharStream input, RecognizerSharedState state) {
        super(input, state);
    }
    
    /** Allow emitting more than one token from a lexer rule
     */
    public void emit(Token token) {
        int ttype = token.getType();
        //System.err.println("::: " + ttype + " --- " + token.getText());
        if (verifyPreviousType(ttype, previousTokenType)) {
            Token syntheticSemi = new CommonToken(token);
            syntheticSemi.setType(getSyntheticSemiType());
            syntheticSemi.setText("beginning of new statement");
            state.token = syntheticSemi;
            tokens.add(syntheticSemi);
            //System.err.println("INSERTING in front of: " + ttype);
        } else {
            state.token = token;
        }
        tokens.add(token);

        if (verifyCurrentType(ttype)) {
            previousTokenType = ttype;
        }
    }

    protected abstract int getSyntheticSemiType();

    protected abstract boolean verifyCurrentType(int ttype);

    protected abstract boolean verifyPreviousType(int ttype, int previousTokenType);

    public Token nextToken() {
        if ( tokens.size() > 0 ) {
            return tokens.remove(0);
        }
        super.nextToken();
        if ( tokens.size()==0 ) {
            emit(Token.EOF_TOKEN);
        }
        return tokens.remove(0);
    }

    void processString() {
        setText(StringLiteralProcessor.convert( log, getCharIndex(), getText() ));
    }

    void processFormatString() {
        // Add quote characters and adjust the index to invoke StringLiteralProcessor.convert().
        StringBuilder sb = new StringBuilder();
        sb.append('"').append(getText()).append('"');
        setText(StringLiteralProcessor.convert(log, getCharIndex() + 1, sb.toString()));
    }

    void processTranslationKey() {
        String text = getText().substring(2); // remove '##'
        if (text.length() > 0) {
            text = StringLiteralProcessor.convert( log, getCharIndex(), text );
        }
        setText(text);
    }


    protected void enterBrace(int quote, boolean nextIsPercent) {
        quoteStack.enterBrace(quote, nextIsPercent);
    }

    protected void leaveQuote() {
        quoteStack.leaveQuote();
    }

    protected boolean rightBraceLikeQuote(int quote) {
        return quoteStack.rightBraceLikeQuote(quote);
    }

    protected void leaveBrace() {
        quoteStack.leaveBrace();
    }

    protected boolean percentIsFormat() {
        return quoteStack.percentIsFormat();
    }

    protected void resetPercentIsFormat() {
        quoteStack.resetPercentIsFormat();
    }

    
    
    public String getErrorMessage(RecognitionException e, String[] tokenNames) {
        
        StringBuffer mb = new StringBuffer();
        if (e instanceof NoViableAltException) {
            NoViableAltException nvae = (NoViableAltException) e;
            if (e.c == Token.EOF) {
                mb.append("Sorry, I reached to the end of file. ");
                mb.append("Perhaps you are having a mismatched " + "'" + "\"" + "' or '{'");
            } else {
                mb.append("Sorry, " + getCharErrorDisplay(e.c));          
                mb.append(" is not supported in JavaFX");
            }
        } else if (e instanceof FailedPredicateException) {
             mb.append("Sorry, I was trying to understand a " + getCharErrorDisplay(e.c) + ". ");
             mb.append("Perhaps you are having a mismatched " + "'" + "\"" + "' or '{'");
            recover(e);
        } else {
            mb.append( super.getErrorMessage(e, tokenNames) );
        }
      
        return  mb.toString();
    }
    

    @Override
    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
   
        String msg = getErrorMessage(e, tokenNames);
//        log.error(Position.NOPOS, "javafx.generalerror", msg);
        log.error(getCharIndex(), "javafx.generalerror", msg);
    }
    
    
     /** Track "He{"l{"l"}o"} world" quotes
     */
    protected class BraceQuoteTracker {
        private int braceDepth;
        private char quote;
        private boolean percentIsFormat;
        private BraceQuoteTracker next;
        private BraceQuoteTracker(BraceQuoteTracker prev, char quote, boolean percentIsFormat) {
            this.quote = quote;
            this.percentIsFormat = percentIsFormat;
            this.braceDepth = 1;
            this.next = prev;
        }

        void enterBrace(int quote, boolean percentIsFormat) {
            if (quote == 0) {  // exisiting string expression or non string expression
                if (quoteStack != NULL_BQT) {
                    ++quoteStack.braceDepth;
                    quoteStack.percentIsFormat = percentIsFormat;
                }
            } else {
                quoteStack = new BraceQuoteTracker(quoteStack, (char)quote, percentIsFormat); // push
            }
        }
        /**
         * Return quote kind if we are reentering a quote
         *
         * @return leaving quote
         */
        char leaveBrace() {
            if (quoteStack != NULL_BQT && --quoteStack.braceDepth == 0) {
                return quoteStack.quote;
            }
            return 0;
        }

        boolean rightBraceLikeQuote(int quote) {
            return quoteStack != NULL_BQT && quoteStack.braceDepth == 1 && (quote == 0 || quoteStack.quote == (char)quote);
        }

        void leaveQuote() {
            assert (quoteStack != NULL_BQT && quoteStack.braceDepth == 0);
            quoteStack = quoteStack.next; // pop
        }

        boolean percentIsFormat() {
            return quoteStack != NULL_BQT && quoteStack.percentIsFormat;
        }

        void resetPercentIsFormat() {
            quoteStack.percentIsFormat = false;
        }

        boolean inBraceQuote() {
            return quoteStack != NULL_BQT;
        }
    }
}
