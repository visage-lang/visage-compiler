// $ANTLR 3.0 \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g 2007-08-03 10:09:36

package com.sun.tools.javafx.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
public class v2Lexer extends Lexer {
    public static final int FUNCTION=65;
    public static final int PACKAGE=33;
    public static final int LT=89;
    public static final int STAR=97;
    public static final int WHILE=52;
    public static final int EASEOUT=27;
    public static final int NEW=61;
    public static final int INDEXOF=73;
    public static final int DO=23;
    public static final int UNITINTERVAL=49;
    public static final int NOT=60;
    public static final int EOF=-1;
    public static final int GTGT=106;
    public static final int RBRACE_QUOTE_STRING_LITERAL=113;
    public static final int BREAK=17;
    public static final int TYPE=68;
    public static final int LBRACKET=81;
    public static final int RPAREN=80;
    public static final int LINEAR=54;
    public static final int IMPORT=34;
    public static final int STRING_LITERAL=109;
    public static final int INSERT=37;
    public static final int FLOATING_POINT_LITERAL=120;
    public static final int SUBSUB=96;
    public static final int BIND=16;
    public static final int STAREQ=102;
    public static final int THIS=44;
    public static final int RETURN=30;
    public static final int VAR=32;
    public static final int SUPER=75;
    public static final int EQ=87;
    public static final int LAST=40;
    public static final int COMMENT=125;
    public static final int SELECT=74;
    public static final int INTO=38;
    public static final int QUES=108;
    public static final int EQEQ=86;
    public static final int MOTION=55;
    public static final int RBRACE=115;
    public static final int POUND=5;
    public static final int LINE_COMMENT=126;
    public static final int PRIVATE=62;
    public static final int NULL=45;
    public static final int ELSE=43;
    public static final int ON=36;
    public static final int DELETE=21;
    public static final int SLASHEQ=103;
    public static final int EASEBOTH=25;
    public static final int ASSERT=13;
    public static final int TRY=56;
    public static final int INVERSE=67;
    public static final int WS=124;
    public static final int TYPEOF=6;
    public static final int INTEGER_LITERAL=118;
    public static final int OR=76;
    public static final int JavaIDDigit=122;
    public static final int SIZEOF=77;
    public static final int GT=88;
    public static final int FROM=35;
    public static final int CATCH=19;
    public static final int REVERSE=78;
    public static final int FALSE=47;
    public static final int DISTINCT=22;
    public static final int Letter=121;
    public static final int THROW=31;
    public static final int DUR=24;
    public static final int WHERE=59;
    public static final int PROTECTED=63;
    public static final int CLASS=20;
    public static final int ORDER=70;
    public static final int PLUSPLUS=94;
    public static final int LBRACE=112;
    public static final int ATTRIBUTE=14;
    public static final int LTEQ=91;
    public static final int SUBEQ=101;
    public static final int Exponent=119;
    public static final int LARROW=8;
    public static final int FOR=48;
    public static final int SUB=95;
    public static final int DOTDOT=7;
    public static final int ABSTRACT=9;
    public static final int NextIsPercent=110;
    public static final int AND=11;
    public static final int PLUSEQ=100;
    public static final int LPAREN=79;
    public static final int IF=41;
    public static final int AS=12;
    public static final int INDEX=71;
    public static final int SLASH=98;
    public static final int IN=50;
    public static final int THEN=42;
    public static final int CONTINUE=53;
    public static final int COMMA=84;
    public static final int TIE=28;
    public static final int IDENTIFIER=123;
    public static final int QUOTE_LBRACE_STRING_LITERAL=111;
    public static final int PLUS=93;
    public static final int RBRACKET=82;
    public static final int DOT=85;
    public static final int RBRACE_LBRACE_STRING_LITERAL=114;
    public static final int LTLT=105;
    public static final int STAYS=29;
    public static final int BY=18;
    public static final int PERCENT=99;
    public static final int LAZY=58;
    public static final int LTGT=90;
    public static final int BEFORE=15;
    public static final int INSTANCEOF=72;
    public static final int AFTER=10;
    public static final int GTEQ=92;
    public static final int Tokens=127;
    public static final int READONLY=66;
    public static final int SEMI=83;
    public static final int TRUE=46;
    public static final int COLON=107;
    public static final int FINALLY=57;
    public static final int PERCENTEQ=104;
    public static final int EASEIN=26;
    public static final int FORMAT_STRING_LITERAL=116;
    public static final int QUOTED_IDENTIFIER=117;
    public static final int FPS=51;
    public static final int EXTENDS=69;
    public static final int PUBLIC=64;
    public static final int BAR=4;
    public static final int FIRST=39;
    
        /** Track "He{"l{"l"}o"} world" quotes
         */
        private static class BraceQuoteTracker {
            private static BraceQuoteTracker quoteStack = null;
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
            static void enterBrace(int quote, boolean percentIsFormat) {
                if (quote == 0) {  // exisiting string expression or non string expression
                    if (quoteStack != null) {
                        ++quoteStack.braceDepth;
                        quoteStack.percentIsFormat = percentIsFormat;
                    }
                } else {
                    quoteStack = new BraceQuoteTracker(quoteStack, (char)quote, percentIsFormat); // push
                }
            }
            /** Return quote kind if we are reentering a quote
             * */
            static char leaveBrace() {
                if (quoteStack != null && --quoteStack.braceDepth == 0) {
                    return quoteStack.quote;
                }
                return 0;
            }
            static boolean rightBraceLikeQuote(int quote) {
                return quoteStack != null && quoteStack.braceDepth == 1 && (quote == 0 || quoteStack.quote == (char)quote);
            }
            static void leaveQuote() {
                assert (quoteStack != null && quoteStack.braceDepth == 0);
                quoteStack = quoteStack.next; // pop
            }
            static boolean percentIsFormat() {
                return quoteStack != null && quoteStack.percentIsFormat;
            }
            static void resetPercentIsFormat() {
                quoteStack.percentIsFormat = false;
            }
            static boolean inBraceQuote() {
                return quoteStack != null;
            }
        }
        
        void removeQuotes() {
        	setText(getText().substring(1, getText().length()-1));
        }
        
        // quote context --
        static final int CUR_QUOTE_CTX	= 0;	// 0 = use current quote context
        static final int SNG_QUOTE_CTX	= 1;	// 1 = single quote quote context
        static final int DBL_QUOTE_CTX	= 2;	// 2 = double quote quote context
     
    public v2Lexer() {;} 
    public v2Lexer(CharStream input) {
        super(input);
        ruleMemo = new HashMap[125+1];
     }
    public String getGrammarFileName() { return "\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g"; }

    // $ANTLR start BAR
    public final void mBAR() throws RecognitionException {
        try {
            int _type = BAR;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:66:5: ( '|' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:66:7: '|'
            {
            match('|'); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end BAR

    // $ANTLR start POUND
    public final void mPOUND() throws RecognitionException {
        try {
            int _type = POUND;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:67:7: ( '#' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:67:9: '#'
            {
            match('#'); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end POUND

    // $ANTLR start TYPEOF
    public final void mTYPEOF() throws RecognitionException {
        try {
            int _type = TYPEOF;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:68:8: ( 'typeof' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:68:10: 'typeof'
            {
            match("typeof"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end TYPEOF

    // $ANTLR start DOTDOT
    public final void mDOTDOT() throws RecognitionException {
        try {
            int _type = DOTDOT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:69:8: ( '..' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:69:10: '..'
            {
            match(".."); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end DOTDOT

    // $ANTLR start LARROW
    public final void mLARROW() throws RecognitionException {
        try {
            int _type = LARROW;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:70:8: ( '<-' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:70:10: '<-'
            {
            match("<-"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LARROW

    // $ANTLR start ABSTRACT
    public final void mABSTRACT() throws RecognitionException {
        try {
            int _type = ABSTRACT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:71:10: ( 'abstract' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:71:12: 'abstract'
            {
            match("abstract"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ABSTRACT

    // $ANTLR start AFTER
    public final void mAFTER() throws RecognitionException {
        try {
            int _type = AFTER;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:72:7: ( 'after' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:72:9: 'after'
            {
            match("after"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end AFTER

    // $ANTLR start AND
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:73:5: ( 'and' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:73:7: 'and'
            {
            match("and"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end AND

    // $ANTLR start AS
    public final void mAS() throws RecognitionException {
        try {
            int _type = AS;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:74:4: ( 'as' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:74:6: 'as'
            {
            match("as"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end AS

    // $ANTLR start ASSERT
    public final void mASSERT() throws RecognitionException {
        try {
            int _type = ASSERT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:75:8: ( 'assert' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:75:10: 'assert'
            {
            match("assert"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ASSERT

    // $ANTLR start ATTRIBUTE
    public final void mATTRIBUTE() throws RecognitionException {
        try {
            int _type = ATTRIBUTE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:76:11: ( 'attribute' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:76:13: 'attribute'
            {
            match("attribute"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ATTRIBUTE

    // $ANTLR start BEFORE
    public final void mBEFORE() throws RecognitionException {
        try {
            int _type = BEFORE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:77:8: ( 'before' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:77:10: 'before'
            {
            match("before"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end BEFORE

    // $ANTLR start BIND
    public final void mBIND() throws RecognitionException {
        try {
            int _type = BIND;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:78:6: ( 'bind' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:78:8: 'bind'
            {
            match("bind"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end BIND

    // $ANTLR start BREAK
    public final void mBREAK() throws RecognitionException {
        try {
            int _type = BREAK;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:79:7: ( 'break' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:79:9: 'break'
            {
            match("break"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end BREAK

    // $ANTLR start BY
    public final void mBY() throws RecognitionException {
        try {
            int _type = BY;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:80:4: ( 'by' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:80:6: 'by'
            {
            match("by"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end BY

    // $ANTLR start CATCH
    public final void mCATCH() throws RecognitionException {
        try {
            int _type = CATCH;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:81:7: ( 'catch' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:81:9: 'catch'
            {
            match("catch"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end CATCH

    // $ANTLR start CLASS
    public final void mCLASS() throws RecognitionException {
        try {
            int _type = CLASS;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:82:7: ( 'class' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:82:9: 'class'
            {
            match("class"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end CLASS

    // $ANTLR start DELETE
    public final void mDELETE() throws RecognitionException {
        try {
            int _type = DELETE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:83:8: ( 'delete' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:83:10: 'delete'
            {
            match("delete"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end DELETE

    // $ANTLR start DISTINCT
    public final void mDISTINCT() throws RecognitionException {
        try {
            int _type = DISTINCT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:84:10: ( 'distinct' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:84:12: 'distinct'
            {
            match("distinct"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end DISTINCT

    // $ANTLR start DO
    public final void mDO() throws RecognitionException {
        try {
            int _type = DO;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:85:4: ( 'do' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:85:6: 'do'
            {
            match("do"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end DO

    // $ANTLR start DUR
    public final void mDUR() throws RecognitionException {
        try {
            int _type = DUR;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:86:5: ( 'dur' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:86:7: 'dur'
            {
            match("dur"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end DUR

    // $ANTLR start EASEBOTH
    public final void mEASEBOTH() throws RecognitionException {
        try {
            int _type = EASEBOTH;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:87:10: ( 'easeboth' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:87:12: 'easeboth'
            {
            match("easeboth"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end EASEBOTH

    // $ANTLR start EASEIN
    public final void mEASEIN() throws RecognitionException {
        try {
            int _type = EASEIN;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:88:8: ( 'easein' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:88:10: 'easein'
            {
            match("easein"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end EASEIN

    // $ANTLR start EASEOUT
    public final void mEASEOUT() throws RecognitionException {
        try {
            int _type = EASEOUT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:89:9: ( 'easeout' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:89:11: 'easeout'
            {
            match("easeout"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end EASEOUT

    // $ANTLR start TIE
    public final void mTIE() throws RecognitionException {
        try {
            int _type = TIE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:90:5: ( 'tie' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:90:7: 'tie'
            {
            match("tie"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end TIE

    // $ANTLR start STAYS
    public final void mSTAYS() throws RecognitionException {
        try {
            int _type = STAYS;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:91:7: ( 'stays' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:91:9: 'stays'
            {
            match("stays"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end STAYS

    // $ANTLR start RETURN
    public final void mRETURN() throws RecognitionException {
        try {
            int _type = RETURN;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:92:8: ( 'return' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:92:10: 'return'
            {
            match("return"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RETURN

    // $ANTLR start THROW
    public final void mTHROW() throws RecognitionException {
        try {
            int _type = THROW;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:93:7: ( 'throw' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:93:9: 'throw'
            {
            match("throw"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end THROW

    // $ANTLR start VAR
    public final void mVAR() throws RecognitionException {
        try {
            int _type = VAR;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:94:5: ( 'var' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:94:7: 'var'
            {
            match("var"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end VAR

    // $ANTLR start PACKAGE
    public final void mPACKAGE() throws RecognitionException {
        try {
            int _type = PACKAGE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:95:9: ( 'package' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:95:11: 'package'
            {
            match("package"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PACKAGE

    // $ANTLR start IMPORT
    public final void mIMPORT() throws RecognitionException {
        try {
            int _type = IMPORT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:96:8: ( 'import' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:96:10: 'import'
            {
            match("import"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end IMPORT

    // $ANTLR start FROM
    public final void mFROM() throws RecognitionException {
        try {
            int _type = FROM;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:97:6: ( 'from' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:97:8: 'from'
            {
            match("from"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FROM

    // $ANTLR start ON
    public final void mON() throws RecognitionException {
        try {
            int _type = ON;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:98:4: ( 'on' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:98:6: 'on'
            {
            match("on"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ON

    // $ANTLR start INSERT
    public final void mINSERT() throws RecognitionException {
        try {
            int _type = INSERT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:99:8: ( 'insert' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:99:10: 'insert'
            {
            match("insert"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INSERT

    // $ANTLR start INTO
    public final void mINTO() throws RecognitionException {
        try {
            int _type = INTO;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:100:6: ( 'into' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:100:8: 'into'
            {
            match("into"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INTO

    // $ANTLR start FIRST
    public final void mFIRST() throws RecognitionException {
        try {
            int _type = FIRST;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:101:7: ( 'first' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:101:9: 'first'
            {
            match("first"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FIRST

    // $ANTLR start LAST
    public final void mLAST() throws RecognitionException {
        try {
            int _type = LAST;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:102:6: ( 'last' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:102:8: 'last'
            {
            match("last"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LAST

    // $ANTLR start IF
    public final void mIF() throws RecognitionException {
        try {
            int _type = IF;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:103:4: ( 'if' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:103:6: 'if'
            {
            match("if"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end IF

    // $ANTLR start THEN
    public final void mTHEN() throws RecognitionException {
        try {
            int _type = THEN;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:104:6: ( 'then' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:104:8: 'then'
            {
            match("then"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end THEN

    // $ANTLR start ELSE
    public final void mELSE() throws RecognitionException {
        try {
            int _type = ELSE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:105:6: ( 'else' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:105:8: 'else'
            {
            match("else"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ELSE

    // $ANTLR start THIS
    public final void mTHIS() throws RecognitionException {
        try {
            int _type = THIS;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:106:6: ( 'this' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:106:8: 'this'
            {
            match("this"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end THIS

    // $ANTLR start NULL
    public final void mNULL() throws RecognitionException {
        try {
            int _type = NULL;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:107:6: ( 'null' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:107:8: 'null'
            {
            match("null"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end NULL

    // $ANTLR start TRUE
    public final void mTRUE() throws RecognitionException {
        try {
            int _type = TRUE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:108:6: ( 'true' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:108:8: 'true'
            {
            match("true"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end TRUE

    // $ANTLR start FALSE
    public final void mFALSE() throws RecognitionException {
        try {
            int _type = FALSE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:109:7: ( 'false' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:109:9: 'false'
            {
            match("false"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FALSE

    // $ANTLR start FOR
    public final void mFOR() throws RecognitionException {
        try {
            int _type = FOR;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:110:5: ( 'for' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:110:7: 'for'
            {
            match("for"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FOR

    // $ANTLR start UNITINTERVAL
    public final void mUNITINTERVAL() throws RecognitionException {
        try {
            int _type = UNITINTERVAL;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:111:14: ( 'unitinterval' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:111:16: 'unitinterval'
            {
            match("unitinterval"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end UNITINTERVAL

    // $ANTLR start IN
    public final void mIN() throws RecognitionException {
        try {
            int _type = IN;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:112:4: ( 'in' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:112:6: 'in'
            {
            match("in"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end IN

    // $ANTLR start FPS
    public final void mFPS() throws RecognitionException {
        try {
            int _type = FPS;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:113:5: ( 'fps' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:113:7: 'fps'
            {
            match("fps"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FPS

    // $ANTLR start WHILE
    public final void mWHILE() throws RecognitionException {
        try {
            int _type = WHILE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:114:7: ( 'while' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:114:9: 'while'
            {
            match("while"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end WHILE

    // $ANTLR start CONTINUE
    public final void mCONTINUE() throws RecognitionException {
        try {
            int _type = CONTINUE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:115:10: ( 'continue' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:115:12: 'continue'
            {
            match("continue"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end CONTINUE

    // $ANTLR start LINEAR
    public final void mLINEAR() throws RecognitionException {
        try {
            int _type = LINEAR;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:116:8: ( 'linear' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:116:10: 'linear'
            {
            match("linear"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LINEAR

    // $ANTLR start MOTION
    public final void mMOTION() throws RecognitionException {
        try {
            int _type = MOTION;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:117:8: ( 'motion' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:117:10: 'motion'
            {
            match("motion"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end MOTION

    // $ANTLR start TRY
    public final void mTRY() throws RecognitionException {
        try {
            int _type = TRY;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:118:5: ( 'try' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:118:7: 'try'
            {
            match("try"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end TRY

    // $ANTLR start FINALLY
    public final void mFINALLY() throws RecognitionException {
        try {
            int _type = FINALLY;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:119:9: ( 'finally' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:119:11: 'finally'
            {
            match("finally"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FINALLY

    // $ANTLR start LAZY
    public final void mLAZY() throws RecognitionException {
        try {
            int _type = LAZY;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:120:6: ( 'lazy' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:120:8: 'lazy'
            {
            match("lazy"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LAZY

    // $ANTLR start WHERE
    public final void mWHERE() throws RecognitionException {
        try {
            int _type = WHERE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:121:7: ( 'where' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:121:9: 'where'
            {
            match("where"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end WHERE

    // $ANTLR start NOT
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:122:5: ( 'not' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:122:7: 'not'
            {
            match("not"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end NOT

    // $ANTLR start NEW
    public final void mNEW() throws RecognitionException {
        try {
            int _type = NEW;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:123:5: ( 'new' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:123:7: 'new'
            {
            match("new"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end NEW

    // $ANTLR start PRIVATE
    public final void mPRIVATE() throws RecognitionException {
        try {
            int _type = PRIVATE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:124:9: ( 'private' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:124:11: 'private'
            {
            match("private"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PRIVATE

    // $ANTLR start PROTECTED
    public final void mPROTECTED() throws RecognitionException {
        try {
            int _type = PROTECTED;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:125:11: ( 'protected' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:125:13: 'protected'
            {
            match("protected"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PROTECTED

    // $ANTLR start PUBLIC
    public final void mPUBLIC() throws RecognitionException {
        try {
            int _type = PUBLIC;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:126:8: ( 'public' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:126:10: 'public'
            {
            match("public"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PUBLIC

    // $ANTLR start FUNCTION
    public final void mFUNCTION() throws RecognitionException {
        try {
            int _type = FUNCTION;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:127:10: ( 'function' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:127:12: 'function'
            {
            match("function"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FUNCTION

    // $ANTLR start READONLY
    public final void mREADONLY() throws RecognitionException {
        try {
            int _type = READONLY;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:128:10: ( 'readonly' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:128:12: 'readonly'
            {
            match("readonly"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end READONLY

    // $ANTLR start INVERSE
    public final void mINVERSE() throws RecognitionException {
        try {
            int _type = INVERSE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:129:9: ( 'inverse' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:129:11: 'inverse'
            {
            match("inverse"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INVERSE

    // $ANTLR start TYPE
    public final void mTYPE() throws RecognitionException {
        try {
            int _type = TYPE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:130:6: ( 'type' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:130:8: 'type'
            {
            match("type"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end TYPE

    // $ANTLR start EXTENDS
    public final void mEXTENDS() throws RecognitionException {
        try {
            int _type = EXTENDS;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:131:9: ( 'extends' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:131:11: 'extends'
            {
            match("extends"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end EXTENDS

    // $ANTLR start ORDER
    public final void mORDER() throws RecognitionException {
        try {
            int _type = ORDER;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:132:7: ( 'order' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:132:9: 'order'
            {
            match("order"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ORDER

    // $ANTLR start INDEX
    public final void mINDEX() throws RecognitionException {
        try {
            int _type = INDEX;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:133:7: ( 'index' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:133:9: 'index'
            {
            match("index"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INDEX

    // $ANTLR start INSTANCEOF
    public final void mINSTANCEOF() throws RecognitionException {
        try {
            int _type = INSTANCEOF;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:134:12: ( 'instanceof' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:134:14: 'instanceof'
            {
            match("instanceof"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INSTANCEOF

    // $ANTLR start INDEXOF
    public final void mINDEXOF() throws RecognitionException {
        try {
            int _type = INDEXOF;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:135:9: ( 'indexof' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:135:11: 'indexof'
            {
            match("indexof"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INDEXOF

    // $ANTLR start SELECT
    public final void mSELECT() throws RecognitionException {
        try {
            int _type = SELECT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:136:8: ( 'select' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:136:10: 'select'
            {
            match("select"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SELECT

    // $ANTLR start SUPER
    public final void mSUPER() throws RecognitionException {
        try {
            int _type = SUPER;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:137:7: ( 'super' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:137:9: 'super'
            {
            match("super"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SUPER

    // $ANTLR start OR
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:138:4: ( 'or' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:138:6: 'or'
            {
            match("or"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end OR

    // $ANTLR start SIZEOF
    public final void mSIZEOF() throws RecognitionException {
        try {
            int _type = SIZEOF;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:139:8: ( 'sizeof' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:139:10: 'sizeof'
            {
            match("sizeof"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SIZEOF

    // $ANTLR start REVERSE
    public final void mREVERSE() throws RecognitionException {
        try {
            int _type = REVERSE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:140:9: ( 'reverse' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:140:11: 'reverse'
            {
            match("reverse"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end REVERSE

    // $ANTLR start LPAREN
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:141:8: ( '(' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:141:10: '('
            {
            match('('); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LPAREN

    // $ANTLR start RPAREN
    public final void mRPAREN() throws RecognitionException {
        try {
            int _type = RPAREN;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:142:8: ( ')' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:142:10: ')'
            {
            match(')'); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RPAREN

    // $ANTLR start LBRACKET
    public final void mLBRACKET() throws RecognitionException {
        try {
            int _type = LBRACKET;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:143:10: ( '[' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:143:12: '['
            {
            match('['); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LBRACKET

    // $ANTLR start RBRACKET
    public final void mRBRACKET() throws RecognitionException {
        try {
            int _type = RBRACKET;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:144:10: ( ']' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:144:12: ']'
            {
            match(']'); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RBRACKET

    // $ANTLR start SEMI
    public final void mSEMI() throws RecognitionException {
        try {
            int _type = SEMI;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:145:6: ( ';' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:145:8: ';'
            {
            match(';'); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SEMI

    // $ANTLR start COMMA
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:146:7: ( ',' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:146:9: ','
            {
            match(','); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COMMA

    // $ANTLR start DOT
    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:147:5: ( '.' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:147:7: '.'
            {
            match('.'); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end DOT

    // $ANTLR start EQEQ
    public final void mEQEQ() throws RecognitionException {
        try {
            int _type = EQEQ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:148:6: ( '==' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:148:8: '=='
            {
            match("=="); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end EQEQ

    // $ANTLR start EQ
    public final void mEQ() throws RecognitionException {
        try {
            int _type = EQ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:149:4: ( '=' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:149:6: '='
            {
            match('='); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end EQ

    // $ANTLR start GT
    public final void mGT() throws RecognitionException {
        try {
            int _type = GT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:150:4: ( '>' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:150:6: '>'
            {
            match('>'); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end GT

    // $ANTLR start LT
    public final void mLT() throws RecognitionException {
        try {
            int _type = LT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:151:4: ( '<' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:151:6: '<'
            {
            match('<'); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LT

    // $ANTLR start LTGT
    public final void mLTGT() throws RecognitionException {
        try {
            int _type = LTGT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:152:6: ( '<>' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:152:8: '<>'
            {
            match("<>"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LTGT

    // $ANTLR start LTEQ
    public final void mLTEQ() throws RecognitionException {
        try {
            int _type = LTEQ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:153:6: ( '<=' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:153:8: '<='
            {
            match("<="); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LTEQ

    // $ANTLR start GTEQ
    public final void mGTEQ() throws RecognitionException {
        try {
            int _type = GTEQ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:154:6: ( '>=' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:154:8: '>='
            {
            match(">="); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end GTEQ

    // $ANTLR start PLUS
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:155:6: ( '+' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:155:8: '+'
            {
            match('+'); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PLUS

    // $ANTLR start PLUSPLUS
    public final void mPLUSPLUS() throws RecognitionException {
        try {
            int _type = PLUSPLUS;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:156:10: ( '++' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:156:12: '++'
            {
            match("++"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PLUSPLUS

    // $ANTLR start SUB
    public final void mSUB() throws RecognitionException {
        try {
            int _type = SUB;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:157:5: ( '-' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:157:7: '-'
            {
            match('-'); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SUB

    // $ANTLR start SUBSUB
    public final void mSUBSUB() throws RecognitionException {
        try {
            int _type = SUBSUB;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:158:8: ( '--' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:158:10: '--'
            {
            match("--"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SUBSUB

    // $ANTLR start STAR
    public final void mSTAR() throws RecognitionException {
        try {
            int _type = STAR;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:159:6: ( '*' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:159:8: '*'
            {
            match('*'); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end STAR

    // $ANTLR start SLASH
    public final void mSLASH() throws RecognitionException {
        try {
            int _type = SLASH;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:160:7: ( '/' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:160:9: '/'
            {
            match('/'); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SLASH

    // $ANTLR start PERCENT
    public final void mPERCENT() throws RecognitionException {
        try {
            int _type = PERCENT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:161:9: ( '%' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:161:11: '%'
            {
            match('%'); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PERCENT

    // $ANTLR start PLUSEQ
    public final void mPLUSEQ() throws RecognitionException {
        try {
            int _type = PLUSEQ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:162:8: ( '+=' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:162:10: '+='
            {
            match("+="); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PLUSEQ

    // $ANTLR start SUBEQ
    public final void mSUBEQ() throws RecognitionException {
        try {
            int _type = SUBEQ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:163:7: ( '-=' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:163:9: '-='
            {
            match("-="); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SUBEQ

    // $ANTLR start STAREQ
    public final void mSTAREQ() throws RecognitionException {
        try {
            int _type = STAREQ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:164:8: ( '*=' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:164:10: '*='
            {
            match("*="); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end STAREQ

    // $ANTLR start SLASHEQ
    public final void mSLASHEQ() throws RecognitionException {
        try {
            int _type = SLASHEQ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:165:9: ( '/=' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:165:11: '/='
            {
            match("/="); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SLASHEQ

    // $ANTLR start PERCENTEQ
    public final void mPERCENTEQ() throws RecognitionException {
        try {
            int _type = PERCENTEQ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:166:11: ( '%=' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:166:13: '%='
            {
            match("%="); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PERCENTEQ

    // $ANTLR start LTLT
    public final void mLTLT() throws RecognitionException {
        try {
            int _type = LTLT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:167:6: ( '<<' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:167:8: '<<'
            {
            match("<<"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LTLT

    // $ANTLR start GTGT
    public final void mGTGT() throws RecognitionException {
        try {
            int _type = GTGT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:168:6: ( '>>' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:168:8: '>>'
            {
            match(">>"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end GTGT

    // $ANTLR start COLON
    public final void mCOLON() throws RecognitionException {
        try {
            int _type = COLON;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:169:7: ( ':' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:169:9: ':'
            {
            match(':'); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COLON

    // $ANTLR start QUES
    public final void mQUES() throws RecognitionException {
        try {
            int _type = QUES;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:170:6: ( '?' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:170:8: '?'
            {
            match('?'); if (failed) return ;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end QUES

    // $ANTLR start STRING_LITERAL
    public final void mSTRING_LITERAL() throws RecognitionException {
        try {
            int _type = STRING_LITERAL;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:209:19: ( '\"' (~ ( '{' | '\"' ) )* '\"' | '\\'' (~ ( '{' | '\\'' ) )* '\\'' )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='\"') ) {
                alt3=1;
            }
            else if ( (LA3_0=='\'') ) {
                alt3=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("209:1: STRING_LITERAL : ( '\"' (~ ( '{' | '\"' ) )* '\"' | '\\'' (~ ( '{' | '\\'' ) )* '\\'' );", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:209:21: '\"' (~ ( '{' | '\"' ) )* '\"'
                    {
                    match('\"'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:209:25: (~ ( '{' | '\"' ) )*
                    loop1:
                    do {
                        int alt1=2;
                        int LA1_0 = input.LA(1);

                        if ( ((LA1_0>='\u0000' && LA1_0<='!')||(LA1_0>='#' && LA1_0<='z')||(LA1_0>='|' && LA1_0<='\uFFFE')) ) {
                            alt1=1;
                        }


                        switch (alt1) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:209:26: ~ ( '{' | '\"' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='z')||(input.LA(1)>='|' && input.LA(1)<='\uFFFE') ) {
                    	        input.consume();
                    	    failed=false;
                    	    }
                    	    else {
                    	        if (backtracking>0) {failed=true; return ;}
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop1;
                        }
                    } while (true);

                    match('\"'); if (failed) return ;
                    if ( backtracking==0 ) {
                       removeQuotes(); 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:210:7: '\\'' (~ ( '{' | '\\'' ) )* '\\''
                    {
                    match('\''); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:210:12: (~ ( '{' | '\\'' ) )*
                    loop2:
                    do {
                        int alt2=2;
                        int LA2_0 = input.LA(1);

                        if ( ((LA2_0>='\u0000' && LA2_0<='&')||(LA2_0>='(' && LA2_0<='z')||(LA2_0>='|' && LA2_0<='\uFFFE')) ) {
                            alt2=1;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:210:13: ~ ( '{' | '\\'' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='z')||(input.LA(1)>='|' && input.LA(1)<='\uFFFE') ) {
                    	        input.consume();
                    	    failed=false;
                    	    }
                    	    else {
                    	        if (backtracking>0) {failed=true; return ;}
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop2;
                        }
                    } while (true);

                    match('\''); if (failed) return ;
                    if ( backtracking==0 ) {
                       removeQuotes(); 
                    }

                    }
                    break;

            }
            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end STRING_LITERAL

    // $ANTLR start QUOTE_LBRACE_STRING_LITERAL
    public final void mQUOTE_LBRACE_STRING_LITERAL() throws RecognitionException {
        try {
            int _type = QUOTE_LBRACE_STRING_LITERAL;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:213:30: ( '\"' (~ ( '{' | '\"' ) )* '{' NextIsPercent[DBL_QUOTE_CTX] | '\\'' (~ ( '{' | '\\'' ) )* '{' NextIsPercent[SNG_QUOTE_CTX] )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='\"') ) {
                alt6=1;
            }
            else if ( (LA6_0=='\'') ) {
                alt6=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("213:1: QUOTE_LBRACE_STRING_LITERAL : ( '\"' (~ ( '{' | '\"' ) )* '{' NextIsPercent[DBL_QUOTE_CTX] | '\\'' (~ ( '{' | '\\'' ) )* '{' NextIsPercent[SNG_QUOTE_CTX] );", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:213:32: '\"' (~ ( '{' | '\"' ) )* '{' NextIsPercent[DBL_QUOTE_CTX]
                    {
                    match('\"'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:213:36: (~ ( '{' | '\"' ) )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( ((LA4_0>='\u0000' && LA4_0<='!')||(LA4_0>='#' && LA4_0<='z')||(LA4_0>='|' && LA4_0<='\uFFFE')) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:213:37: ~ ( '{' | '\"' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='z')||(input.LA(1)>='|' && input.LA(1)<='\uFFFE') ) {
                    	        input.consume();
                    	    failed=false;
                    	    }
                    	    else {
                    	        if (backtracking>0) {failed=true; return ;}
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);

                    match('{'); if (failed) return ;
                    if ( backtracking==0 ) {
                       removeQuotes(); 
                    }
                    mNextIsPercent(DBL_QUOTE_CTX); if (failed) return ;

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:215:7: '\\'' (~ ( '{' | '\\'' ) )* '{' NextIsPercent[SNG_QUOTE_CTX]
                    {
                    match('\''); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:215:12: (~ ( '{' | '\\'' ) )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( ((LA5_0>='\u0000' && LA5_0<='&')||(LA5_0>='(' && LA5_0<='z')||(LA5_0>='|' && LA5_0<='\uFFFE')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:215:13: ~ ( '{' | '\\'' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='z')||(input.LA(1)>='|' && input.LA(1)<='\uFFFE') ) {
                    	        input.consume();
                    	    failed=false;
                    	    }
                    	    else {
                    	        if (backtracking>0) {failed=true; return ;}
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);

                    match('{'); if (failed) return ;
                    if ( backtracking==0 ) {
                       removeQuotes(); 
                    }
                    mNextIsPercent(SNG_QUOTE_CTX); if (failed) return ;

                    }
                    break;

            }
            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end QUOTE_LBRACE_STRING_LITERAL

    // $ANTLR start LBRACE
    public final void mLBRACE() throws RecognitionException {
        try {
            int _type = LBRACE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:218:11: ( '{' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:218:13: '{'
            {
            match('{'); if (failed) return ;
            if ( backtracking==0 ) {
               BraceQuoteTracker.enterBrace(0, false); 
            }

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LBRACE

    // $ANTLR start RBRACE_QUOTE_STRING_LITERAL
    public final void mRBRACE_QUOTE_STRING_LITERAL() throws RecognitionException {
        try {
            int _type = RBRACE_QUOTE_STRING_LITERAL;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:220:30: ({...}? => '}' (~ ( '{' | '\"' ) )* '\"' | {...}? => '}' (~ ( '{' | '\\'' ) )* '\\'' )
            int alt9=2;
            alt9 = dfa9.predict(input);
            switch (alt9) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:220:35: {...}? => '}' (~ ( '{' | '\"' ) )* '\"'
                    {
                    if ( !( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ) ) {
                        if (backtracking>0) {failed=true; return ;}
                        throw new FailedPredicateException(input, "RBRACE_QUOTE_STRING_LITERAL", " BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ");
                    }
                    match('}'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:221:11: (~ ( '{' | '\"' ) )*
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0>='\u0000' && LA7_0<='!')||(LA7_0>='#' && LA7_0<='z')||(LA7_0>='|' && LA7_0<='\uFFFE')) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:221:12: ~ ( '{' | '\"' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='z')||(input.LA(1)>='|' && input.LA(1)<='\uFFFE') ) {
                    	        input.consume();
                    	    failed=false;
                    	    }
                    	    else {
                    	        if (backtracking>0) {failed=true; return ;}
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop7;
                        }
                    } while (true);

                    match('\"'); if (failed) return ;
                    if ( backtracking==0 ) {
                       BraceQuoteTracker.leaveBrace(); 
                      				         			  BraceQuoteTracker.leaveQuote(); 
                      				         			  removeQuotes(); 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:224:10: {...}? => '}' (~ ( '{' | '\\'' ) )* '\\''
                    {
                    if ( !( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) ) ) {
                        if (backtracking>0) {failed=true; return ;}
                        throw new FailedPredicateException(input, "RBRACE_QUOTE_STRING_LITERAL", " BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) ");
                    }
                    match('}'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:225:11: (~ ( '{' | '\\'' ) )*
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( ((LA8_0>='\u0000' && LA8_0<='&')||(LA8_0>='(' && LA8_0<='z')||(LA8_0>='|' && LA8_0<='\uFFFE')) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:225:12: ~ ( '{' | '\\'' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='z')||(input.LA(1)>='|' && input.LA(1)<='\uFFFE') ) {
                    	        input.consume();
                    	    failed=false;
                    	    }
                    	    else {
                    	        if (backtracking>0) {failed=true; return ;}
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop8;
                        }
                    } while (true);

                    match('\''); if (failed) return ;
                    if ( backtracking==0 ) {
                       BraceQuoteTracker.leaveBrace(); 
                      				         			  BraceQuoteTracker.leaveQuote(); 
                      				         			  removeQuotes(); 
                    }

                    }
                    break;

            }
            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RBRACE_QUOTE_STRING_LITERAL

    // $ANTLR start RBRACE_LBRACE_STRING_LITERAL
    public final void mRBRACE_LBRACE_STRING_LITERAL() throws RecognitionException {
        try {
            int _type = RBRACE_LBRACE_STRING_LITERAL;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:229:31: ({...}? => '}' (~ ( '{' | '\"' ) )* '{' NextIsPercent[CUR_QUOTE_CTX] | {...}? => '}' (~ ( '{' | '\\'' ) )* '{' NextIsPercent[CUR_QUOTE_CTX] )
            int alt12=2;
            alt12 = dfa12.predict(input);
            switch (alt12) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:229:36: {...}? => '}' (~ ( '{' | '\"' ) )* '{' NextIsPercent[CUR_QUOTE_CTX]
                    {
                    if ( !( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ) ) {
                        if (backtracking>0) {failed=true; return ;}
                        throw new FailedPredicateException(input, "RBRACE_LBRACE_STRING_LITERAL", " BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ");
                    }
                    match('}'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:230:11: (~ ( '{' | '\"' ) )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( ((LA10_0>='\u0000' && LA10_0<='!')||(LA10_0>='#' && LA10_0<='z')||(LA10_0>='|' && LA10_0<='\uFFFE')) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:230:12: ~ ( '{' | '\"' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='z')||(input.LA(1)>='|' && input.LA(1)<='\uFFFE') ) {
                    	        input.consume();
                    	    failed=false;
                    	    }
                    	    else {
                    	        if (backtracking>0) {failed=true; return ;}
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);

                    match('{'); if (failed) return ;
                    if ( backtracking==0 ) {
                       BraceQuoteTracker.leaveBrace(); 
                      				         			  removeQuotes(); 
                    }
                    mNextIsPercent(CUR_QUOTE_CTX); if (failed) return ;

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:233:10: {...}? => '}' (~ ( '{' | '\\'' ) )* '{' NextIsPercent[CUR_QUOTE_CTX]
                    {
                    if ( !( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) ) ) {
                        if (backtracking>0) {failed=true; return ;}
                        throw new FailedPredicateException(input, "RBRACE_LBRACE_STRING_LITERAL", " BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) ");
                    }
                    match('}'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:234:11: (~ ( '{' | '\\'' ) )*
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( ((LA11_0>='\u0000' && LA11_0<='&')||(LA11_0>='(' && LA11_0<='z')||(LA11_0>='|' && LA11_0<='\uFFFE')) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:234:12: ~ ( '{' | '\\'' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='z')||(input.LA(1)>='|' && input.LA(1)<='\uFFFE') ) {
                    	        input.consume();
                    	    failed=false;
                    	    }
                    	    else {
                    	        if (backtracking>0) {failed=true; return ;}
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop11;
                        }
                    } while (true);

                    match('{'); if (failed) return ;
                    if ( backtracking==0 ) {
                       BraceQuoteTracker.leaveBrace(); 
                      				         			  removeQuotes(); 
                    }
                    mNextIsPercent(CUR_QUOTE_CTX); if (failed) return ;

                    }
                    break;

            }
            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RBRACE_LBRACE_STRING_LITERAL

    // $ANTLR start RBRACE
    public final void mRBRACE() throws RecognitionException {
        try {
            int _type = RBRACE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:238:11: ({...}? => '}' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:238:16: {...}? => '}'
            {
            if ( !( !BraceQuoteTracker.rightBraceLikeQuote(CUR_QUOTE_CTX) ) ) {
                if (backtracking>0) {failed=true; return ;}
                throw new FailedPredicateException(input, "RBRACE", " !BraceQuoteTracker.rightBraceLikeQuote(CUR_QUOTE_CTX) ");
            }
            match('}'); if (failed) return ;
            if ( backtracking==0 ) {
               BraceQuoteTracker.leaveBrace(); 
            }

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RBRACE

    // $ANTLR start NextIsPercent
    public final void mNextIsPercent(int quoteContext) throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:243:6: ( ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )* '%' )=> | )
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (synpred1()) ) {
                alt13=1;
            }
            else if ( (true) ) {
                alt13=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("241:1: fragment NextIsPercent[int quoteContext] : ( ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )* '%' )=> | );", 13, 0, input);

                throw nvae;
            }
            switch (alt13) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:243:8: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )* '%' )=>
                    {
                    if ( backtracking==0 ) {
                       BraceQuoteTracker.enterBrace(quoteContext, true); 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:245:10: 
                    {
                    if ( backtracking==0 ) {
                       BraceQuoteTracker.enterBrace(quoteContext, false); 
                    }

                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end NextIsPercent

    // $ANTLR start FORMAT_STRING_LITERAL
    public final void mFORMAT_STRING_LITERAL() throws RecognitionException {
        try {
            int _type = FORMAT_STRING_LITERAL;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:247:24: ({...}? => '%' (~ ' ' )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:247:30: {...}? => '%' (~ ' ' )*
            {
            if ( !( BraceQuoteTracker.percentIsFormat() ) ) {
                if (backtracking>0) {failed=true; return ;}
                throw new FailedPredicateException(input, "FORMAT_STRING_LITERAL", " BraceQuoteTracker.percentIsFormat() ");
            }
            match('%'); if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:248:11: (~ ' ' )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( ((LA14_0>='\u0000' && LA14_0<='\u001F')||(LA14_0>='!' && LA14_0<='\uFFFE')) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:248:12: ~ ' '
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\u001F')||(input.LA(1)>='!' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();
            	    failed=false;
            	    }
            	    else {
            	        if (backtracking>0) {failed=true; return ;}
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

            if ( backtracking==0 ) {
               BraceQuoteTracker.resetPercentIsFormat(); 
            }

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FORMAT_STRING_LITERAL

    // $ANTLR start QUOTED_IDENTIFIER
    public final void mQUOTED_IDENTIFIER() throws RecognitionException {
        try {
            int _type = QUOTED_IDENTIFIER;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:252:2: ( '<<' ( . )* '>>' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:252:4: '<<' ( . )* '>>'
            {
            match("<<"); if (failed) return ;

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:252:9: ( . )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0=='>') ) {
                    int LA15_1 = input.LA(2);

                    if ( (LA15_1=='>') ) {
                        alt15=2;
                    }
                    else if ( ((LA15_1>='\u0000' && LA15_1<='=')||(LA15_1>='?' && LA15_1<='\uFFFE')) ) {
                        alt15=1;
                    }


                }
                else if ( ((LA15_0>='\u0000' && LA15_0<='=')||(LA15_0>='?' && LA15_0<='\uFFFE')) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:252:9: .
            	    {
            	    matchAny(); if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);

            match(">>"); if (failed) return ;

            if ( backtracking==0 ) {
               setText(getText().substring(2, getText().length()-2)); 
            }

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end QUOTED_IDENTIFIER

    // $ANTLR start INTEGER_LITERAL
    public final void mINTEGER_LITERAL() throws RecognitionException {
        try {
            int _type = INTEGER_LITERAL;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:254:17: ( ( '0' | '1' .. '9' ( '0' .. '9' )* ) )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:254:19: ( '0' | '1' .. '9' ( '0' .. '9' )* )
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:254:19: ( '0' | '1' .. '9' ( '0' .. '9' )* )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0=='0') ) {
                alt17=1;
            }
            else if ( ((LA17_0>='1' && LA17_0<='9')) ) {
                alt17=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("254:19: ( '0' | '1' .. '9' ( '0' .. '9' )* )", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:254:20: '0'
                    {
                    match('0'); if (failed) return ;

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:254:26: '1' .. '9' ( '0' .. '9' )*
                    {
                    matchRange('1','9'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:254:35: ( '0' .. '9' )*
                    loop16:
                    do {
                        int alt16=2;
                        int LA16_0 = input.LA(1);

                        if ( ((LA16_0>='0' && LA16_0<='9')) ) {
                            alt16=1;
                        }


                        switch (alt16) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:254:35: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop16;
                        }
                    } while (true);


                    }
                    break;

            }


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INTEGER_LITERAL

    // $ANTLR start FLOATING_POINT_LITERAL
    public final void mFLOATING_POINT_LITERAL() throws RecognitionException {
        try {
            int _type = FLOATING_POINT_LITERAL;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:257:5: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( Exponent )? | '.' ( '0' .. '9' )+ ( Exponent )? | ( '0' .. '9' )+ Exponent )
            int alt24=3;
            alt24 = dfa24.predict(input);
            switch (alt24) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:257:9: ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( Exponent )?
                    {
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:257:9: ( '0' .. '9' )+
                    int cnt18=0;
                    loop18:
                    do {
                        int alt18=2;
                        int LA18_0 = input.LA(1);

                        if ( ((LA18_0>='0' && LA18_0<='9')) ) {
                            alt18=1;
                        }


                        switch (alt18) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:257:10: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt18 >= 1 ) break loop18;
                    	    if (backtracking>0) {failed=true; return ;}
                                EarlyExitException eee =
                                    new EarlyExitException(18, input);
                                throw eee;
                        }
                        cnt18++;
                    } while (true);

                    match('.'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:257:25: ( '0' .. '9' )*
                    loop19:
                    do {
                        int alt19=2;
                        int LA19_0 = input.LA(1);

                        if ( ((LA19_0>='0' && LA19_0<='9')) ) {
                            alt19=1;
                        }


                        switch (alt19) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:257:26: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop19;
                        }
                    } while (true);

                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:257:37: ( Exponent )?
                    int alt20=2;
                    int LA20_0 = input.LA(1);

                    if ( (LA20_0=='E'||LA20_0=='e') ) {
                        alt20=1;
                    }
                    switch (alt20) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:257:37: Exponent
                            {
                            mExponent(); if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:258:9: '.' ( '0' .. '9' )+ ( Exponent )?
                    {
                    match('.'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:258:13: ( '0' .. '9' )+
                    int cnt21=0;
                    loop21:
                    do {
                        int alt21=2;
                        int LA21_0 = input.LA(1);

                        if ( ((LA21_0>='0' && LA21_0<='9')) ) {
                            alt21=1;
                        }


                        switch (alt21) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:258:14: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt21 >= 1 ) break loop21;
                    	    if (backtracking>0) {failed=true; return ;}
                                EarlyExitException eee =
                                    new EarlyExitException(21, input);
                                throw eee;
                        }
                        cnt21++;
                    } while (true);

                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:258:25: ( Exponent )?
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( (LA22_0=='E'||LA22_0=='e') ) {
                        alt22=1;
                    }
                    switch (alt22) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:258:25: Exponent
                            {
                            mExponent(); if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:259:9: ( '0' .. '9' )+ Exponent
                    {
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:259:9: ( '0' .. '9' )+
                    int cnt23=0;
                    loop23:
                    do {
                        int alt23=2;
                        int LA23_0 = input.LA(1);

                        if ( ((LA23_0>='0' && LA23_0<='9')) ) {
                            alt23=1;
                        }


                        switch (alt23) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:259:10: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt23 >= 1 ) break loop23;
                    	    if (backtracking>0) {failed=true; return ;}
                                EarlyExitException eee =
                                    new EarlyExitException(23, input);
                                throw eee;
                        }
                        cnt23++;
                    } while (true);

                    mExponent(); if (failed) return ;

                    }
                    break;

            }
            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FLOATING_POINT_LITERAL

    // $ANTLR start Exponent
    public final void mExponent() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:263:10: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:263:12: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:263:22: ( '+' | '-' )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0=='+'||LA25_0=='-') ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();
                    failed=false;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        MismatchedSetException mse =
                            new MismatchedSetException(null,input);
                        recover(mse);    throw mse;
                    }


                    }
                    break;

            }

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:263:33: ( '0' .. '9' )+
            int cnt26=0;
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( ((LA26_0>='0' && LA26_0<='9')) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:263:34: '0' .. '9'
            	    {
            	    matchRange('0','9'); if (failed) return ;

            	    }
            	    break;

            	default :
            	    if ( cnt26 >= 1 ) break loop26;
            	    if (backtracking>0) {failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(26, input);
                        throw eee;
                }
                cnt26++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end Exponent

    // $ANTLR start IDENTIFIER
    public final void mIDENTIFIER() throws RecognitionException {
        try {
            int _type = IDENTIFIER;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:266:5: ( Letter ( Letter | JavaIDDigit )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:266:9: Letter ( Letter | JavaIDDigit )*
            {
            mLetter(); if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:266:16: ( Letter | JavaIDDigit )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0=='$'||(LA27_0>='0' && LA27_0<='9')||(LA27_0>='A' && LA27_0<='Z')||LA27_0=='_'||(LA27_0>='a' && LA27_0<='z')||(LA27_0>='\u00C0' && LA27_0<='\u00D6')||(LA27_0>='\u00D8' && LA27_0<='\u00F6')||(LA27_0>='\u00F8' && LA27_0<='\u1FFF')||(LA27_0>='\u3040' && LA27_0<='\u318F')||(LA27_0>='\u3300' && LA27_0<='\u337F')||(LA27_0>='\u3400' && LA27_0<='\u3D2D')||(LA27_0>='\u4E00' && LA27_0<='\u9FFF')||(LA27_0>='\uF900' && LA27_0<='\uFAFF')) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:
            	    {
            	    if ( input.LA(1)=='$'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u1FFF')||(input.LA(1)>='\u3040' && input.LA(1)<='\u318F')||(input.LA(1)>='\u3300' && input.LA(1)<='\u337F')||(input.LA(1)>='\u3400' && input.LA(1)<='\u3D2D')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FFF')||(input.LA(1)>='\uF900' && input.LA(1)<='\uFAFF') ) {
            	        input.consume();
            	    failed=false;
            	    }
            	    else {
            	        if (backtracking>0) {failed=true; return ;}
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop27;
                }
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end IDENTIFIER

    // $ANTLR start Letter
    public final void mLetter() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:271:5: ( '\\u0024' | '\\u0041' .. '\\u005a' | '\\u005f' | '\\u0061' .. '\\u007a' | '\\u00c0' .. '\\u00d6' | '\\u00d8' .. '\\u00f6' | '\\u00f8' .. '\\u00ff' | '\\u0100' .. '\\u1fff' | '\\u3040' .. '\\u318f' | '\\u3300' .. '\\u337f' | '\\u3400' .. '\\u3d2d' | '\\u4e00' .. '\\u9fff' | '\\uf900' .. '\\ufaff' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:
            {
            if ( input.LA(1)=='$'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u1FFF')||(input.LA(1)>='\u3040' && input.LA(1)<='\u318F')||(input.LA(1)>='\u3300' && input.LA(1)<='\u337F')||(input.LA(1)>='\u3400' && input.LA(1)<='\u3D2D')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FFF')||(input.LA(1)>='\uF900' && input.LA(1)<='\uFAFF') ) {
                input.consume();
            failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }


            }

        }
        finally {
        }
    }
    // $ANTLR end Letter

    // $ANTLR start JavaIDDigit
    public final void mJavaIDDigit() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:288:5: ( '\\u0030' .. '\\u0039' | '\\u0660' .. '\\u0669' | '\\u06f0' .. '\\u06f9' | '\\u0966' .. '\\u096f' | '\\u09e6' .. '\\u09ef' | '\\u0a66' .. '\\u0a6f' | '\\u0ae6' .. '\\u0aef' | '\\u0b66' .. '\\u0b6f' | '\\u0be7' .. '\\u0bef' | '\\u0c66' .. '\\u0c6f' | '\\u0ce6' .. '\\u0cef' | '\\u0d66' .. '\\u0d6f' | '\\u0e50' .. '\\u0e59' | '\\u0ed0' .. '\\u0ed9' | '\\u1040' .. '\\u1049' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='\u0660' && input.LA(1)<='\u0669')||(input.LA(1)>='\u06F0' && input.LA(1)<='\u06F9')||(input.LA(1)>='\u0966' && input.LA(1)<='\u096F')||(input.LA(1)>='\u09E6' && input.LA(1)<='\u09EF')||(input.LA(1)>='\u0A66' && input.LA(1)<='\u0A6F')||(input.LA(1)>='\u0AE6' && input.LA(1)<='\u0AEF')||(input.LA(1)>='\u0B66' && input.LA(1)<='\u0B6F')||(input.LA(1)>='\u0BE7' && input.LA(1)<='\u0BEF')||(input.LA(1)>='\u0C66' && input.LA(1)<='\u0C6F')||(input.LA(1)>='\u0CE6' && input.LA(1)<='\u0CEF')||(input.LA(1)>='\u0D66' && input.LA(1)<='\u0D6F')||(input.LA(1)>='\u0E50' && input.LA(1)<='\u0E59')||(input.LA(1)>='\u0ED0' && input.LA(1)<='\u0ED9')||(input.LA(1)>='\u1040' && input.LA(1)<='\u1049') ) {
                input.consume();
            failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }


            }

        }
        finally {
        }
    }
    // $ANTLR end JavaIDDigit

    // $ANTLR start WS
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:305:5: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' ) )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:305:8: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
                input.consume();
            failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            if ( backtracking==0 ) {
              channel=HIDDEN;
            }

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end WS

    // $ANTLR start COMMENT
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:309:5: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:309:9: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); if (failed) return ;

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:309:14: ( options {greedy=false; } : . )*
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( (LA28_0=='*') ) {
                    int LA28_1 = input.LA(2);

                    if ( (LA28_1=='/') ) {
                        alt28=2;
                    }
                    else if ( ((LA28_1>='\u0000' && LA28_1<='.')||(LA28_1>='0' && LA28_1<='\uFFFE')) ) {
                        alt28=1;
                    }


                }
                else if ( ((LA28_0>='\u0000' && LA28_0<=')')||(LA28_0>='+' && LA28_0<='\uFFFE')) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:309:42: .
            	    {
            	    matchAny(); if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop28;
                }
            } while (true);

            match("*/"); if (failed) return ;

            if ( backtracking==0 ) {
              channel=HIDDEN;
            }

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COMMENT

    // $ANTLR start LINE_COMMENT
    public final void mLINE_COMMENT() throws RecognitionException {
        try {
            int _type = LINE_COMMENT;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:313:5: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:313:7: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
            {
            match("//"); if (failed) return ;

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:313:12: (~ ( '\\n' | '\\r' ) )*
            loop29:
            do {
                int alt29=2;
                int LA29_0 = input.LA(1);

                if ( ((LA29_0>='\u0000' && LA29_0<='\t')||(LA29_0>='\u000B' && LA29_0<='\f')||(LA29_0>='\u000E' && LA29_0<='\uFFFE')) ) {
                    alt29=1;
                }


                switch (alt29) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:313:12: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();
            	    failed=false;
            	    }
            	    else {
            	        if (backtracking>0) {failed=true; return ;}
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop29;
                }
            } while (true);

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:313:26: ( '\\r' )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0=='\r') ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:313:26: '\\r'
                    {
                    match('\r'); if (failed) return ;

                    }
                    break;

            }

            match('\n'); if (failed) return ;
            if ( backtracking==0 ) {
              channel=HIDDEN;
            }

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LINE_COMMENT

    public void mTokens() throws RecognitionException {
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:8: ( BAR | POUND | TYPEOF | DOTDOT | LARROW | ABSTRACT | AFTER | AND | AS | ASSERT | ATTRIBUTE | BEFORE | BIND | BREAK | BY | CATCH | CLASS | DELETE | DISTINCT | DO | DUR | EASEBOTH | EASEIN | EASEOUT | TIE | STAYS | RETURN | THROW | VAR | PACKAGE | IMPORT | FROM | ON | INSERT | INTO | FIRST | LAST | IF | THEN | ELSE | THIS | NULL | TRUE | FALSE | FOR | UNITINTERVAL | IN | FPS | WHILE | CONTINUE | LINEAR | MOTION | TRY | FINALLY | LAZY | WHERE | NOT | NEW | PRIVATE | PROTECTED | PUBLIC | FUNCTION | READONLY | INVERSE | TYPE | EXTENDS | ORDER | INDEX | INSTANCEOF | INDEXOF | SELECT | SUPER | OR | SIZEOF | REVERSE | LPAREN | RPAREN | LBRACKET | RBRACKET | SEMI | COMMA | DOT | EQEQ | EQ | GT | LT | LTGT | LTEQ | GTEQ | PLUS | PLUSPLUS | SUB | SUBSUB | STAR | SLASH | PERCENT | PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ | LTLT | GTGT | COLON | QUES | STRING_LITERAL | QUOTE_LBRACE_STRING_LITERAL | LBRACE | RBRACE_QUOTE_STRING_LITERAL | RBRACE_LBRACE_STRING_LITERAL | RBRACE | FORMAT_STRING_LITERAL | QUOTED_IDENTIFIER | INTEGER_LITERAL | FLOATING_POINT_LITERAL | IDENTIFIER | WS | COMMENT | LINE_COMMENT )
        int alt31=119;
        alt31 = dfa31.predict(input);
        switch (alt31) {
            case 1 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:10: BAR
                {
                mBAR(); if (failed) return ;

                }
                break;
            case 2 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:14: POUND
                {
                mPOUND(); if (failed) return ;

                }
                break;
            case 3 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:20: TYPEOF
                {
                mTYPEOF(); if (failed) return ;

                }
                break;
            case 4 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:27: DOTDOT
                {
                mDOTDOT(); if (failed) return ;

                }
                break;
            case 5 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:34: LARROW
                {
                mLARROW(); if (failed) return ;

                }
                break;
            case 6 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:41: ABSTRACT
                {
                mABSTRACT(); if (failed) return ;

                }
                break;
            case 7 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:50: AFTER
                {
                mAFTER(); if (failed) return ;

                }
                break;
            case 8 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:56: AND
                {
                mAND(); if (failed) return ;

                }
                break;
            case 9 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:60: AS
                {
                mAS(); if (failed) return ;

                }
                break;
            case 10 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:63: ASSERT
                {
                mASSERT(); if (failed) return ;

                }
                break;
            case 11 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:70: ATTRIBUTE
                {
                mATTRIBUTE(); if (failed) return ;

                }
                break;
            case 12 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:80: BEFORE
                {
                mBEFORE(); if (failed) return ;

                }
                break;
            case 13 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:87: BIND
                {
                mBIND(); if (failed) return ;

                }
                break;
            case 14 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:92: BREAK
                {
                mBREAK(); if (failed) return ;

                }
                break;
            case 15 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:98: BY
                {
                mBY(); if (failed) return ;

                }
                break;
            case 16 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:101: CATCH
                {
                mCATCH(); if (failed) return ;

                }
                break;
            case 17 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:107: CLASS
                {
                mCLASS(); if (failed) return ;

                }
                break;
            case 18 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:113: DELETE
                {
                mDELETE(); if (failed) return ;

                }
                break;
            case 19 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:120: DISTINCT
                {
                mDISTINCT(); if (failed) return ;

                }
                break;
            case 20 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:129: DO
                {
                mDO(); if (failed) return ;

                }
                break;
            case 21 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:132: DUR
                {
                mDUR(); if (failed) return ;

                }
                break;
            case 22 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:136: EASEBOTH
                {
                mEASEBOTH(); if (failed) return ;

                }
                break;
            case 23 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:145: EASEIN
                {
                mEASEIN(); if (failed) return ;

                }
                break;
            case 24 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:152: EASEOUT
                {
                mEASEOUT(); if (failed) return ;

                }
                break;
            case 25 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:160: TIE
                {
                mTIE(); if (failed) return ;

                }
                break;
            case 26 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:164: STAYS
                {
                mSTAYS(); if (failed) return ;

                }
                break;
            case 27 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:170: RETURN
                {
                mRETURN(); if (failed) return ;

                }
                break;
            case 28 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:177: THROW
                {
                mTHROW(); if (failed) return ;

                }
                break;
            case 29 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:183: VAR
                {
                mVAR(); if (failed) return ;

                }
                break;
            case 30 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:187: PACKAGE
                {
                mPACKAGE(); if (failed) return ;

                }
                break;
            case 31 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:195: IMPORT
                {
                mIMPORT(); if (failed) return ;

                }
                break;
            case 32 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:202: FROM
                {
                mFROM(); if (failed) return ;

                }
                break;
            case 33 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:207: ON
                {
                mON(); if (failed) return ;

                }
                break;
            case 34 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:210: INSERT
                {
                mINSERT(); if (failed) return ;

                }
                break;
            case 35 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:217: INTO
                {
                mINTO(); if (failed) return ;

                }
                break;
            case 36 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:222: FIRST
                {
                mFIRST(); if (failed) return ;

                }
                break;
            case 37 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:228: LAST
                {
                mLAST(); if (failed) return ;

                }
                break;
            case 38 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:233: IF
                {
                mIF(); if (failed) return ;

                }
                break;
            case 39 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:236: THEN
                {
                mTHEN(); if (failed) return ;

                }
                break;
            case 40 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:241: ELSE
                {
                mELSE(); if (failed) return ;

                }
                break;
            case 41 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:246: THIS
                {
                mTHIS(); if (failed) return ;

                }
                break;
            case 42 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:251: NULL
                {
                mNULL(); if (failed) return ;

                }
                break;
            case 43 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:256: TRUE
                {
                mTRUE(); if (failed) return ;

                }
                break;
            case 44 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:261: FALSE
                {
                mFALSE(); if (failed) return ;

                }
                break;
            case 45 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:267: FOR
                {
                mFOR(); if (failed) return ;

                }
                break;
            case 46 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:271: UNITINTERVAL
                {
                mUNITINTERVAL(); if (failed) return ;

                }
                break;
            case 47 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:284: IN
                {
                mIN(); if (failed) return ;

                }
                break;
            case 48 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:287: FPS
                {
                mFPS(); if (failed) return ;

                }
                break;
            case 49 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:291: WHILE
                {
                mWHILE(); if (failed) return ;

                }
                break;
            case 50 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:297: CONTINUE
                {
                mCONTINUE(); if (failed) return ;

                }
                break;
            case 51 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:306: LINEAR
                {
                mLINEAR(); if (failed) return ;

                }
                break;
            case 52 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:313: MOTION
                {
                mMOTION(); if (failed) return ;

                }
                break;
            case 53 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:320: TRY
                {
                mTRY(); if (failed) return ;

                }
                break;
            case 54 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:324: FINALLY
                {
                mFINALLY(); if (failed) return ;

                }
                break;
            case 55 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:332: LAZY
                {
                mLAZY(); if (failed) return ;

                }
                break;
            case 56 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:337: WHERE
                {
                mWHERE(); if (failed) return ;

                }
                break;
            case 57 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:343: NOT
                {
                mNOT(); if (failed) return ;

                }
                break;
            case 58 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:347: NEW
                {
                mNEW(); if (failed) return ;

                }
                break;
            case 59 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:351: PRIVATE
                {
                mPRIVATE(); if (failed) return ;

                }
                break;
            case 60 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:359: PROTECTED
                {
                mPROTECTED(); if (failed) return ;

                }
                break;
            case 61 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:369: PUBLIC
                {
                mPUBLIC(); if (failed) return ;

                }
                break;
            case 62 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:376: FUNCTION
                {
                mFUNCTION(); if (failed) return ;

                }
                break;
            case 63 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:385: READONLY
                {
                mREADONLY(); if (failed) return ;

                }
                break;
            case 64 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:394: INVERSE
                {
                mINVERSE(); if (failed) return ;

                }
                break;
            case 65 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:402: TYPE
                {
                mTYPE(); if (failed) return ;

                }
                break;
            case 66 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:407: EXTENDS
                {
                mEXTENDS(); if (failed) return ;

                }
                break;
            case 67 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:415: ORDER
                {
                mORDER(); if (failed) return ;

                }
                break;
            case 68 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:421: INDEX
                {
                mINDEX(); if (failed) return ;

                }
                break;
            case 69 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:427: INSTANCEOF
                {
                mINSTANCEOF(); if (failed) return ;

                }
                break;
            case 70 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:438: INDEXOF
                {
                mINDEXOF(); if (failed) return ;

                }
                break;
            case 71 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:446: SELECT
                {
                mSELECT(); if (failed) return ;

                }
                break;
            case 72 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:453: SUPER
                {
                mSUPER(); if (failed) return ;

                }
                break;
            case 73 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:459: OR
                {
                mOR(); if (failed) return ;

                }
                break;
            case 74 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:462: SIZEOF
                {
                mSIZEOF(); if (failed) return ;

                }
                break;
            case 75 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:469: REVERSE
                {
                mREVERSE(); if (failed) return ;

                }
                break;
            case 76 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:477: LPAREN
                {
                mLPAREN(); if (failed) return ;

                }
                break;
            case 77 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:484: RPAREN
                {
                mRPAREN(); if (failed) return ;

                }
                break;
            case 78 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:491: LBRACKET
                {
                mLBRACKET(); if (failed) return ;

                }
                break;
            case 79 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:500: RBRACKET
                {
                mRBRACKET(); if (failed) return ;

                }
                break;
            case 80 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:509: SEMI
                {
                mSEMI(); if (failed) return ;

                }
                break;
            case 81 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:514: COMMA
                {
                mCOMMA(); if (failed) return ;

                }
                break;
            case 82 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:520: DOT
                {
                mDOT(); if (failed) return ;

                }
                break;
            case 83 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:524: EQEQ
                {
                mEQEQ(); if (failed) return ;

                }
                break;
            case 84 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:529: EQ
                {
                mEQ(); if (failed) return ;

                }
                break;
            case 85 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:532: GT
                {
                mGT(); if (failed) return ;

                }
                break;
            case 86 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:535: LT
                {
                mLT(); if (failed) return ;

                }
                break;
            case 87 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:538: LTGT
                {
                mLTGT(); if (failed) return ;

                }
                break;
            case 88 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:543: LTEQ
                {
                mLTEQ(); if (failed) return ;

                }
                break;
            case 89 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:548: GTEQ
                {
                mGTEQ(); if (failed) return ;

                }
                break;
            case 90 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:553: PLUS
                {
                mPLUS(); if (failed) return ;

                }
                break;
            case 91 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:558: PLUSPLUS
                {
                mPLUSPLUS(); if (failed) return ;

                }
                break;
            case 92 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:567: SUB
                {
                mSUB(); if (failed) return ;

                }
                break;
            case 93 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:571: SUBSUB
                {
                mSUBSUB(); if (failed) return ;

                }
                break;
            case 94 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:578: STAR
                {
                mSTAR(); if (failed) return ;

                }
                break;
            case 95 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:583: SLASH
                {
                mSLASH(); if (failed) return ;

                }
                break;
            case 96 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:589: PERCENT
                {
                mPERCENT(); if (failed) return ;

                }
                break;
            case 97 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:597: PLUSEQ
                {
                mPLUSEQ(); if (failed) return ;

                }
                break;
            case 98 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:604: SUBEQ
                {
                mSUBEQ(); if (failed) return ;

                }
                break;
            case 99 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:610: STAREQ
                {
                mSTAREQ(); if (failed) return ;

                }
                break;
            case 100 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:617: SLASHEQ
                {
                mSLASHEQ(); if (failed) return ;

                }
                break;
            case 101 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:625: PERCENTEQ
                {
                mPERCENTEQ(); if (failed) return ;

                }
                break;
            case 102 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:635: LTLT
                {
                mLTLT(); if (failed) return ;

                }
                break;
            case 103 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:640: GTGT
                {
                mGTGT(); if (failed) return ;

                }
                break;
            case 104 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:645: COLON
                {
                mCOLON(); if (failed) return ;

                }
                break;
            case 105 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:651: QUES
                {
                mQUES(); if (failed) return ;

                }
                break;
            case 106 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:656: STRING_LITERAL
                {
                mSTRING_LITERAL(); if (failed) return ;

                }
                break;
            case 107 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:671: QUOTE_LBRACE_STRING_LITERAL
                {
                mQUOTE_LBRACE_STRING_LITERAL(); if (failed) return ;

                }
                break;
            case 108 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:699: LBRACE
                {
                mLBRACE(); if (failed) return ;

                }
                break;
            case 109 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:706: RBRACE_QUOTE_STRING_LITERAL
                {
                mRBRACE_QUOTE_STRING_LITERAL(); if (failed) return ;

                }
                break;
            case 110 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:734: RBRACE_LBRACE_STRING_LITERAL
                {
                mRBRACE_LBRACE_STRING_LITERAL(); if (failed) return ;

                }
                break;
            case 111 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:763: RBRACE
                {
                mRBRACE(); if (failed) return ;

                }
                break;
            case 112 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:770: FORMAT_STRING_LITERAL
                {
                mFORMAT_STRING_LITERAL(); if (failed) return ;

                }
                break;
            case 113 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:792: QUOTED_IDENTIFIER
                {
                mQUOTED_IDENTIFIER(); if (failed) return ;

                }
                break;
            case 114 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:810: INTEGER_LITERAL
                {
                mINTEGER_LITERAL(); if (failed) return ;

                }
                break;
            case 115 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:826: FLOATING_POINT_LITERAL
                {
                mFLOATING_POINT_LITERAL(); if (failed) return ;

                }
                break;
            case 116 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:849: IDENTIFIER
                {
                mIDENTIFIER(); if (failed) return ;

                }
                break;
            case 117 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:860: WS
                {
                mWS(); if (failed) return ;

                }
                break;
            case 118 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:863: COMMENT
                {
                mCOMMENT(); if (failed) return ;

                }
                break;
            case 119 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:1:871: LINE_COMMENT
                {
                mLINE_COMMENT(); if (failed) return ;

                }
                break;

        }

    }

    // $ANTLR start synpred1
    public final void synpred1_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:243:8: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )* '%' )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:243:9: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )* '%'
        {
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:243:9: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )*
        loop32:
        do {
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( ((LA32_0>='\t' && LA32_0<='\n')||(LA32_0>='\f' && LA32_0<='\r')||LA32_0==' ') ) {
                alt32=1;
            }


            switch (alt32) {
        	case 1 :
        	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:
        	    {
        	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
        	        input.consume();
        	    failed=false;
        	    }
        	    else {
        	        if (backtracking>0) {failed=true; return ;}
        	        MismatchedSetException mse =
        	            new MismatchedSetException(null,input);
        	        recover(mse);    throw mse;
        	    }


        	    }
        	    break;

        	default :
        	    break loop32;
            }
        } while (true);

        match('%'); if (failed) return ;

        }
    }
    // $ANTLR end synpred1

    public final boolean synpred1() {
        backtracking++;
        int start = input.mark();
        try {
            synpred1_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }


    protected DFA9 dfa9 = new DFA9(this);
    protected DFA12 dfa12 = new DFA12(this);
    protected DFA24 dfa24 = new DFA24(this);
    protected DFA31 dfa31 = new DFA31(this);
    static final String DFA9_eotS =
        "\3\uffff\1\5\1\6\2\uffff";
    static final String DFA9_eofS =
        "\7\uffff";
    static final String DFA9_minS =
        "\1\175\4\0\2\uffff";
    static final String DFA9_maxS =
        "\1\175\4\ufffe\2\uffff";
    static final String DFA9_acceptS =
        "\5\uffff\1\2\1\1";
    static final String DFA9_specialS =
        "\1\4\1\3\1\1\1\2\1\0\2\uffff}>";
    static final String[] DFA9_transitionS = {
            "\1\1",
            "\42\2\1\4\4\2\1\3\123\2\1\uffff\uff83\2",
            "\42\2\1\4\4\2\1\3\123\2\1\uffff\uff83\2",
            "\173\6\1\uffff\uff83\6",
            "\173\5\1\uffff\uff83\5",
            "",
            ""
    };

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "220:1: RBRACE_QUOTE_STRING_LITERAL : ({...}? => '}' (~ ( '{' | '\"' ) )* '\"' | {...}? => '}' (~ ( '{' | '\\'' ) )* '\\'' );";
        }
        public int specialStateTransition(int s, IntStream input) throws NoViableAltException {
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA9_4 = input.LA(1);

                         
                        int index9_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((LA9_4>='\u0000' && LA9_4<='z')||(LA9_4>='|' && LA9_4<='\uFFFE')) && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 5;}

                        else s = 6;

                         
                        input.seek(index9_4);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA9_2 = input.LA(1);

                         
                        int index9_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA9_2=='\"') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 4;}

                        else if ( (LA9_2=='\'') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 3;}

                        else if ( ((LA9_2>='\u0000' && LA9_2<='!')||(LA9_2>='#' && LA9_2<='&')||(LA9_2>='(' && LA9_2<='z')||(LA9_2>='|' && LA9_2<='\uFFFE')) && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 2;}

                         
                        input.seek(index9_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA9_3 = input.LA(1);

                         
                        int index9_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((LA9_3>='\u0000' && LA9_3<='z')||(LA9_3>='|' && LA9_3<='\uFFFE')) && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 6;}

                        else s = 5;

                         
                        input.seek(index9_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA9_1 = input.LA(1);

                         
                        int index9_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((LA9_1>='\u0000' && LA9_1<='!')||(LA9_1>='#' && LA9_1<='&')||(LA9_1>='(' && LA9_1<='z')||(LA9_1>='|' && LA9_1<='\uFFFE')) && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 2;}

                        else if ( (LA9_1=='\'') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 3;}

                        else if ( (LA9_1=='\"') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 4;}

                         
                        input.seek(index9_1);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA9_0 = input.LA(1);

                         
                        int index9_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA9_0=='}') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 1;}

                         
                        input.seek(index9_0);
                        if ( s>=0 ) return s;
                        break;
            }
            if (backtracking>0) {failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 9, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA12_eotS =
        "\6\uffff";
    static final String DFA12_eofS =
        "\6\uffff";
    static final String DFA12_minS =
        "\1\175\3\0\2\uffff";
    static final String DFA12_maxS =
        "\1\175\2\ufffe\1\0\2\uffff";
    static final String DFA12_acceptS =
        "\4\uffff\1\1\1\2";
    static final String DFA12_specialS =
        "\1\3\1\1\1\2\1\0\2\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\1",
            "\42\2\1\5\4\2\1\4\123\2\1\3\uff83\2",
            "\42\2\1\5\4\2\1\4\123\2\1\3\uff83\2",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "229:1: RBRACE_LBRACE_STRING_LITERAL : ({...}? => '}' (~ ( '{' | '\"' ) )* '{' NextIsPercent[CUR_QUOTE_CTX] | {...}? => '}' (~ ( '{' | '\\'' ) )* '{' NextIsPercent[CUR_QUOTE_CTX] );";
        }
        public int specialStateTransition(int s, IntStream input) throws NoViableAltException {
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA12_3 = input.LA(1);

                         
                        int index12_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ) ) {s = 4;}

                        else if ( ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) ) ) {s = 5;}

                         
                        input.seek(index12_3);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA12_1 = input.LA(1);

                         
                        int index12_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((LA12_1>='\u0000' && LA12_1<='!')||(LA12_1>='#' && LA12_1<='&')||(LA12_1>='(' && LA12_1<='z')||(LA12_1>='|' && LA12_1<='\uFFFE')) && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 2;}

                        else if ( (LA12_1=='{') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 3;}

                        else if ( (LA12_1=='\'') && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 4;}

                        else if ( (LA12_1=='\"') && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 5;}

                         
                        input.seek(index12_1);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA12_2 = input.LA(1);

                         
                        int index12_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA12_2=='{') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 3;}

                        else if ( ((LA12_2>='\u0000' && LA12_2<='!')||(LA12_2>='#' && LA12_2<='&')||(LA12_2>='(' && LA12_2<='z')||(LA12_2>='|' && LA12_2<='\uFFFE')) && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 2;}

                        else if ( (LA12_2=='\"') && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 5;}

                        else if ( (LA12_2=='\'') && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 4;}

                         
                        input.seek(index12_2);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA12_0 = input.LA(1);

                         
                        int index12_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA12_0=='}') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 1;}

                         
                        input.seek(index12_0);
                        if ( s>=0 ) return s;
                        break;
            }
            if (backtracking>0) {failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 12, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA24_eotS =
        "\5\uffff";
    static final String DFA24_eofS =
        "\5\uffff";
    static final String DFA24_minS =
        "\2\56\3\uffff";
    static final String DFA24_maxS =
        "\1\71\1\145\3\uffff";
    static final String DFA24_acceptS =
        "\2\uffff\1\2\1\3\1\1";
    static final String DFA24_specialS =
        "\5\uffff}>";
    static final String[] DFA24_transitionS = {
            "\1\2\1\uffff\12\1",
            "\1\4\1\uffff\12\1\13\uffff\1\3\37\uffff\1\3",
            "",
            "",
            ""
    };

    static final short[] DFA24_eot = DFA.unpackEncodedString(DFA24_eotS);
    static final short[] DFA24_eof = DFA.unpackEncodedString(DFA24_eofS);
    static final char[] DFA24_min = DFA.unpackEncodedStringToUnsignedChars(DFA24_minS);
    static final char[] DFA24_max = DFA.unpackEncodedStringToUnsignedChars(DFA24_maxS);
    static final short[] DFA24_accept = DFA.unpackEncodedString(DFA24_acceptS);
    static final short[] DFA24_special = DFA.unpackEncodedString(DFA24_specialS);
    static final short[][] DFA24_transition;

    static {
        int numStates = DFA24_transitionS.length;
        DFA24_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA24_transition[i] = DFA.unpackEncodedString(DFA24_transitionS[i]);
        }
    }

    class DFA24 extends DFA {

        public DFA24(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 24;
            this.eot = DFA24_eot;
            this.eof = DFA24_eof;
            this.min = DFA24_min;
            this.max = DFA24_max;
            this.accept = DFA24_accept;
            this.special = DFA24_special;
            this.transition = DFA24_transition;
        }
        public String getDescription() {
            return "256:1: FLOATING_POINT_LITERAL : ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( Exponent )? | '.' ( '0' .. '9' )+ ( Exponent )? | ( '0' .. '9' )+ Exponent );";
        }
    }
    static final String DFA31_eotS =
        "\3\uffff\1\54\1\63\1\71\21\54\6\uffff\1\152\1\155\1\160\1\163\1"+
        "\165\1\171\1\174\5\uffff\1\u0083\2\u0086\2\uffff\4\54\5\uffff\1"+
        "\u0090\2\uffff\1\u0092\7\54\1\u009a\6\54\1\u00a1\14\54\1\u00b5\1"+
        "\u00b6\7\54\1\u00c0\1\u00c1\10\54\21\uffff\1\u00cc\7\uffff\1\u00ce"+
        "\2\uffff\1\u00cf\1\uffff\1\u0086\1\54\1\u00d6\3\54\1\u00da\1\54"+
        "\2\uffff\1\54\1\uffff\1\54\1\u00de\5\54\1\uffff\3\54\1\u00e7\2\54"+
        "\1\uffff\12\54\1\u00f4\10\54\2\uffff\4\54\1\u0102\1\u0103\3\54\2"+
        "\uffff\4\54\1\u010b\1\u010c\4\54\11\uffff\1\u0113\1\uffff\1\54\1"+
        "\u0115\1\u0116\1\uffff\1\u0117\2\54\1\uffff\2\54\1\u011c\5\54\1"+
        "\uffff\4\54\1\u0128\7\54\1\uffff\10\54\1\u0138\3\54\1\u013c\2\uffff"+
        "\3\54\1\u0140\1\u0141\1\54\1\u0143\2\uffff\4\54\1\uffff\1\54\1\uffff"+
        "\1\u0149\3\uffff\2\54\1\u014c\1\54\1\uffff\1\u014e\1\54\1\u0150"+
        "\1\u0151\7\54\1\uffff\2\54\1\u015b\1\u015c\12\54\1\u0168\1\uffff"+
        "\2\54\1\u016b\1\uffff\1\u016c\1\54\1\u016e\2\uffff\1\54\1\uffff"+
        "\1\54\1\u0171\1\u0172\1\54\1\u0174\1\uffff\1\u0175\1\54\1\uffff"+
        "\1\54\1\uffff\1\u0178\2\uffff\2\54\1\u017b\1\54\1\u017d\2\54\1\u0180"+
        "\1\u0181\2\uffff\1\u0182\5\54\1\u0188\1\54\1\u018a\2\54\1\uffff"+
        "\1\u018d\1\54\2\uffff\1\54\1\uffff\1\u0190\1\54\2\uffff\1\u0192"+
        "\2\uffff\2\54\1\uffff\2\54\1\uffff\1\u0197\1\uffff\1\54\1\u0199"+
        "\3\uffff\1\54\1\u019b\1\u019c\1\u019d\1\54\1\uffff\1\u019f\1\uffff"+
        "\1\54\1\u01a1\1\uffff\1\u01a2\1\54\1\uffff\1\54\1\uffff\1\54\1\u01a6"+
        "\1\u01a7\1\u01a8\1\uffff\1\u01a9\1\uffff\1\u01aa\3\uffff\1\54\1"+
        "\uffff\1\54\2\uffff\1\u01ad\1\54\1\u01af\5\uffff\1\u01b0\1\54\1"+
        "\uffff\1\54\2\uffff\1\u01b3\1\54\1\uffff\1\54\1\u01b6\1\uffff";
    static final String DFA31_eofS =
        "\u01b7\uffff";
    static final String DFA31_minS =
        "\1\11\2\uffff\1\150\1\56\1\55\1\142\1\145\1\141\1\145\1\141\2\145"+
        "\2\141\1\146\1\141\1\156\1\141\1\145\1\156\1\150\1\157\6\uffff\2"+
        "\75\1\53\1\55\1\75\1\52\1\0\2\uffff\2\0\1\uffff\1\0\2\56\2\uffff"+
        "\1\160\2\145\1\165\5\uffff\1\0\2\uffff\1\44\1\164\1\144\1\164\1"+
        "\163\1\156\1\145\1\146\1\44\1\164\1\141\1\156\1\162\1\163\1\154"+
        "\1\44\1\164\2\163\1\172\1\154\1\160\2\141\1\162\1\143\1\151\1\142"+
        "\2\44\1\160\1\156\1\157\1\163\1\162\1\154\1\156\2\44\1\163\1\156"+
        "\1\154\1\164\1\167\1\151\1\145\1\164\21\uffff\1\0\1\uffff\2\0\2"+
        "\uffff\3\0\2\uffff\1\0\1\uffff\1\56\1\145\1\44\1\157\1\156\1\163"+
        "\1\44\1\145\2\uffff\1\145\1\uffff\1\162\1\44\1\145\1\164\1\144\1"+
        "\141\1\157\1\uffff\1\143\1\163\1\164\1\44\1\164\1\145\1\uffff\6"+
        "\145\1\171\1\165\1\144\1\145\1\44\1\153\1\166\1\164\1\154\3\145"+
        "\1\157\2\uffff\1\157\1\141\1\163\1\155\2\44\1\163\1\143\1\145\2"+
        "\uffff\1\164\1\171\1\145\1\154\2\44\1\164\1\154\1\162\1\151\1\0"+
        "\3\uffff\1\0\2\uffff\1\0\1\uffff\1\44\1\uffff\1\167\2\44\1\uffff"+
        "\1\44\1\162\1\151\1\uffff\2\162\1\44\1\153\1\162\1\150\1\163\1\151"+
        "\1\uffff\1\151\1\164\1\156\1\142\1\44\1\157\1\143\1\162\1\163\1"+
        "\162\1\157\1\162\1\uffff\2\141\1\145\1\151\2\162\1\141\1\170\1\44"+
        "\1\162\1\154\1\164\1\44\2\uffff\1\145\1\164\1\162\2\44\1\141\1\44"+
        "\2\uffff\1\151\2\145\1\157\1\uffff\1\146\1\uffff\1\44\3\uffff\1"+
        "\164\1\142\1\44\1\141\1\uffff\1\44\1\145\2\44\2\156\1\145\1\144"+
        "\1\156\1\157\1\165\1\uffff\1\146\1\164\2\44\2\156\1\163\1\147\1"+
        "\164\2\143\1\163\1\164\1\156\1\44\1\uffff\1\164\1\154\1\44\1\uffff"+
        "\1\44\1\151\1\44\2\uffff\1\162\1\uffff\1\156\2\44\1\156\1\44\1\uffff"+
        "\1\44\1\165\1\uffff\1\143\1\uffff\1\44\2\uffff\1\165\1\143\1\44"+
        "\1\163\1\44\2\164\2\44\2\uffff\1\44\1\154\3\145\1\164\1\44\1\145"+
        "\1\44\1\143\1\146\1\uffff\1\44\1\171\2\uffff\1\157\1\uffff\1\44"+
        "\1\164\2\uffff\1\44\2\uffff\2\164\1\uffff\1\145\1\164\1\uffff\1"+
        "\44\1\uffff\1\150\1\44\3\uffff\1\171\3\44\1\145\1\uffff\1\44\1\uffff"+
        "\1\145\1\44\1\uffff\1\44\1\156\1\uffff\1\145\1\uffff\1\145\3\44"+
        "\1\uffff\1\44\1\uffff\1\44\3\uffff\1\144\1\uffff\1\157\2\uffff\1"+
        "\44\1\162\1\44\5\uffff\1\44\1\146\1\uffff\1\166\2\uffff\1\44\1\141"+
        "\1\uffff\1\154\1\44\1\uffff";
    static final String DFA31_maxS =
        "\1\ufaff\2\uffff\1\171\1\71\1\76\1\164\1\171\1\157\1\165\1\170\1"+
        "\165\1\145\1\141\1\165\1\156\1\165\1\162\1\151\1\165\1\156\1\150"+
        "\1\157\6\uffff\1\75\1\76\4\75\1\ufffe\2\uffff\2\ufffe\1\uffff\1"+
        "\ufffe\2\145\2\uffff\1\160\1\145\1\162\1\171\5\uffff\1\ufffe\2\uffff"+
        "\1\ufaff\1\164\1\144\1\164\1\163\1\156\1\145\1\146\1\ufaff\1\164"+
        "\1\141\1\156\1\162\1\163\1\154\1\ufaff\1\164\2\163\1\172\1\154\1"+
        "\160\1\141\1\166\1\162\1\143\1\157\1\142\2\ufaff\1\160\1\162\1\157"+
        "\1\163\1\162\1\154\1\156\2\ufaff\1\172\1\156\1\154\1\164\1\167\2"+
        "\151\1\164\21\uffff\1\ufffe\1\uffff\1\0\1\ufffe\2\uffff\3\ufffe"+
        "\2\uffff\1\ufffe\1\uffff\2\145\1\ufaff\1\157\1\156\1\163\1\ufaff"+
        "\1\145\2\uffff\1\145\1\uffff\1\162\1\ufaff\1\145\1\164\1\144\1\141"+
        "\1\157\1\uffff\1\143\1\163\1\164\1\ufaff\1\164\1\145\1\uffff\6\145"+
        "\1\171\1\165\1\144\1\145\1\ufaff\1\153\1\166\1\164\1\154\1\145\1"+
        "\164\1\145\1\157\2\uffff\1\157\1\141\1\163\1\155\2\ufaff\1\163\1"+
        "\143\1\145\2\uffff\1\164\1\171\1\145\1\154\2\ufaff\1\164\1\154\1"+
        "\162\1\151\1\0\3\uffff\1\ufffe\2\uffff\1\ufffe\1\uffff\1\ufaff\1"+
        "\uffff\1\167\2\ufaff\1\uffff\1\ufaff\1\162\1\151\1\uffff\2\162\1"+
        "\ufaff\1\153\1\162\1\150\1\163\1\151\1\uffff\1\151\1\164\1\156\1"+
        "\157\1\ufaff\1\157\1\143\1\162\1\163\1\162\1\157\1\162\1\uffff\2"+
        "\141\1\145\1\151\2\162\1\141\1\170\1\ufaff\1\162\1\154\1\164\1\ufaff"+
        "\2\uffff\1\145\1\164\1\162\2\ufaff\1\141\1\ufaff\2\uffff\1\151\2"+
        "\145\1\157\1\uffff\1\146\1\uffff\1\ufaff\3\uffff\1\164\1\142\1\ufaff"+
        "\1\141\1\uffff\1\ufaff\1\145\2\ufaff\2\156\1\145\1\144\1\156\1\157"+
        "\1\165\1\uffff\1\146\1\164\2\ufaff\2\156\1\163\1\147\1\164\2\143"+
        "\1\163\1\164\1\156\1\ufaff\1\uffff\1\164\1\154\1\ufaff\1\uffff\1"+
        "\ufaff\1\151\1\ufaff\2\uffff\1\162\1\uffff\1\156\2\ufaff\1\156\1"+
        "\ufaff\1\uffff\1\ufaff\1\165\1\uffff\1\143\1\uffff\1\ufaff\2\uffff"+
        "\1\165\1\143\1\ufaff\1\163\1\ufaff\2\164\2\ufaff\2\uffff\1\ufaff"+
        "\1\154\3\145\1\164\1\ufaff\1\145\1\ufaff\1\143\1\146\1\uffff\1\ufaff"+
        "\1\171\2\uffff\1\157\1\uffff\1\ufaff\1\164\2\uffff\1\ufaff\2\uffff"+
        "\2\164\1\uffff\1\145\1\164\1\uffff\1\ufaff\1\uffff\1\150\1\ufaff"+
        "\3\uffff\1\171\3\ufaff\1\145\1\uffff\1\ufaff\1\uffff\1\145\1\ufaff"+
        "\1\uffff\1\ufaff\1\156\1\uffff\1\145\1\uffff\1\145\3\ufaff\1\uffff"+
        "\1\ufaff\1\uffff\1\ufaff\3\uffff\1\144\1\uffff\1\157\2\uffff\1\ufaff"+
        "\1\162\1\ufaff\5\uffff\1\ufaff\1\146\1\uffff\1\166\2\uffff\1\ufaff"+
        "\1\141\1\uffff\1\154\1\ufaff\1\uffff";
    static final String DFA31_acceptS =
        "\1\uffff\1\1\1\2\24\uffff\1\114\1\115\1\116\1\117\1\120\1\121\7"+
        "\uffff\1\150\1\151\2\uffff\1\154\3\uffff\1\164\1\165\4\uffff\1\4"+
        "\1\122\1\163\1\127\1\130\1\uffff\1\5\1\126\57\uffff\1\123\1\124"+
        "\1\131\1\147\1\125\1\133\1\141\1\132\1\135\1\142\1\134\1\143\1\136"+
        "\1\144\1\167\1\166\1\137\1\uffff\1\160\2\uffff\1\153\1\152\3\uffff"+
        "\1\157\1\156\1\uffff\1\162\10\uffff\1\161\1\146\1\uffff\1\11\7\uffff"+
        "\1\17\6\uffff\1\24\23\uffff\1\57\1\46\11\uffff\1\111\1\41\13\uffff"+
        "\1\140\2\155\1\uffff\1\156\1\155\1\uffff\1\156\1\uffff\1\31\3\uffff"+
        "\1\65\3\uffff\1\10\10\uffff\1\25\14\uffff\1\35\15\uffff\1\60\1\55"+
        "\7\uffff\1\71\1\72\4\uffff\1\145\1\uffff\1\101\1\uffff\1\47\1\51"+
        "\1\53\4\uffff\1\15\13\uffff\1\50\17\uffff\1\43\3\uffff\1\40\3\uffff"+
        "\1\45\1\67\1\uffff\1\52\5\uffff\1\34\2\uffff\1\7\1\uffff\1\16\1"+
        "\uffff\1\20\1\21\11\uffff\1\110\1\32\13\uffff\1\104\2\uffff\1\44"+
        "\1\54\1\uffff\1\103\2\uffff\1\61\1\70\1\uffff\1\3\1\12\2\uffff\1"+
        "\14\2\uffff\1\22\1\uffff\1\27\2\uffff\1\112\1\107\1\33\5\uffff\1"+
        "\75\1\uffff\1\42\2\uffff\1\37\2\uffff\1\63\1\uffff\1\64\4\uffff"+
        "\1\102\1\uffff\1\30\1\uffff\1\113\1\36\1\73\1\uffff\1\100\1\uffff"+
        "\1\106\1\66\3\uffff\1\6\1\62\1\23\1\26\1\77\2\uffff\1\76\1\uffff"+
        "\1\13\1\74\2\uffff\1\105\2\uffff\1\56";
    static final String DFA31_specialS =
        "\1\2\42\uffff\1\1\5\uffff\1\10\120\uffff\1\3\1\uffff\1\4\4\uffff"+
        "\1\6\1\11\2\uffff\1\7\106\uffff\1\5\3\uffff\1\12\2\uffff\1\0\u00e3"+
        "\uffff}>";
    static final String[] DFA31_transitionS = {
            "\2\55\1\uffff\2\55\22\uffff\1\55\1\uffff\1\46\1\2\1\54\1\43"+
            "\1\uffff\1\47\1\27\1\30\1\41\1\37\1\34\1\40\1\4\1\42\1\52\11"+
            "\53\1\44\1\33\1\5\1\35\1\36\1\45\1\uffff\32\54\1\31\1\uffff"+
            "\1\32\1\uffff\1\54\1\uffff\1\6\1\7\1\10\1\11\1\12\1\20\2\54"+
            "\1\17\2\54\1\22\1\26\1\23\1\21\1\16\1\54\1\14\1\13\1\3\1\24"+
            "\1\15\1\25\3\54\1\50\1\1\1\51\102\uffff\27\54\1\uffff\37\54"+
            "\1\uffff\u1f08\54\u1040\uffff\u0150\54\u0170\uffff\u0080\54"+
            "\u0080\uffff\u092e\54\u10d2\uffff\u5200\54\u5900\uffff\u0200"+
            "\54",
            "",
            "",
            "\1\60\1\57\10\uffff\1\61\6\uffff\1\56",
            "\1\62\1\uffff\12\64",
            "\1\70\16\uffff\1\67\1\66\1\65",
            "\1\76\3\uffff\1\75\7\uffff\1\74\4\uffff\1\72\1\73",
            "\1\101\3\uffff\1\77\10\uffff\1\100\6\uffff\1\102",
            "\1\103\12\uffff\1\104\2\uffff\1\105",
            "\1\110\3\uffff\1\107\5\uffff\1\111\5\uffff\1\106",
            "\1\113\12\uffff\1\114\13\uffff\1\112",
            "\1\116\3\uffff\1\115\12\uffff\1\120\1\117",
            "\1\121",
            "\1\122",
            "\1\123\20\uffff\1\124\2\uffff\1\125",
            "\1\127\6\uffff\1\130\1\126",
            "\1\135\7\uffff\1\131\5\uffff\1\134\1\133\1\uffff\1\132\2\uffff"+
            "\1\136",
            "\1\140\3\uffff\1\137",
            "\1\141\7\uffff\1\142",
            "\1\145\11\uffff\1\144\5\uffff\1\143",
            "\1\146",
            "\1\147",
            "\1\150",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\151",
            "\1\153\1\154",
            "\1\156\21\uffff\1\157",
            "\1\161\17\uffff\1\162",
            "\1\164",
            "\1\170\4\uffff\1\167\15\uffff\1\166",
            "\40\173\1\uffff\34\173\1\172\uffc1\173",
            "",
            "",
            "\42\175\1\177\130\175\1\176\uff83\175",
            "\47\u0080\1\177\123\u0080\1\176\uff83\u0080",
            "",
            "\42\u0081\1\u0085\4\u0081\1\u0082\123\u0081\1\u0084\uff83\u0081",
            "\1\64\1\uffff\12\64\13\uffff\1\64\37\uffff\1\64",
            "\1\64\1\uffff\12\u0087\13\uffff\1\64\37\uffff\1\64",
            "",
            "",
            "\1\u0088",
            "\1\u0089",
            "\1\u008b\3\uffff\1\u008c\10\uffff\1\u008a",
            "\1\u008e\3\uffff\1\u008d",
            "",
            "",
            "",
            "",
            "",
            "\uffff\u008f",
            "",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\22\54"+
            "\1\u0091\7\54\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54"+
            "\u1040\uffff\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e"+
            "\54\u10d2\uffff\u5200\54\u5900\uffff\u0200\54",
            "\1\u0093",
            "\1\u0094",
            "\1\u0095",
            "\1\u0096",
            "\1\u0097",
            "\1\u0098",
            "\1\u0099",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u009b",
            "\1\u009c",
            "\1\u009d",
            "\1\u009e",
            "\1\u009f",
            "\1\u00a0",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u00a2",
            "\1\u00a3",
            "\1\u00a4",
            "\1\u00a5",
            "\1\u00a6",
            "\1\u00a7",
            "\1\u00a8",
            "\1\u00aa\22\uffff\1\u00a9\1\uffff\1\u00ab",
            "\1\u00ac",
            "\1\u00ad",
            "\1\u00ae\5\uffff\1\u00af",
            "\1\u00b0",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\3\54"+
            "\1\u00b3\16\54\1\u00b2\1\u00b4\1\54\1\u00b1\4\54\105\uffff\27"+
            "\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff\u0150\54\u0170"+
            "\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff\u5200\54\u5900"+
            "\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u00b7",
            "\1\u00b8\3\uffff\1\u00b9",
            "\1\u00ba",
            "\1\u00bb",
            "\1\u00bc",
            "\1\u00bd",
            "\1\u00be",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\3\54"+
            "\1\u00bf\26\54\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54"+
            "\u1040\uffff\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e"+
            "\54\u10d2\uffff\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u00c2\6\uffff\1\u00c3",
            "\1\u00c4",
            "\1\u00c5",
            "\1\u00c6",
            "\1\u00c7",
            "\1\u00c8",
            "\1\u00ca\3\uffff\1\u00c9",
            "\1\u00cb",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\40\173\1\uffff\uffde\173",
            "",
            "\1\uffff",
            "\42\175\1\177\130\175\1\176\uff83\175",
            "",
            "",
            "\47\u0080\1\177\123\u0080\1\176\uff83\u0080",
            "\42\u0081\1\u0085\4\u0081\1\u0082\123\u0081\1\u0084\uff83\u0081",
            "\42\u00d0\1\u00cf\130\u00d0\1\u00d1\uff83\u00d0",
            "",
            "",
            "\47\u00d3\1\u00d2\123\u00d3\1\u00d4\uff83\u00d3",
            "",
            "\1\64\1\uffff\12\u0087\13\uffff\1\64\37\uffff\1\64",
            "\1\u00d5",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u00d7",
            "\1\u00d8",
            "\1\u00d9",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u00db",
            "",
            "",
            "\1\u00dc",
            "",
            "\1\u00dd",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u00df",
            "\1\u00e0",
            "\1\u00e1",
            "\1\u00e2",
            "\1\u00e3",
            "",
            "\1\u00e4",
            "\1\u00e5",
            "\1\u00e6",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u00e8",
            "\1\u00e9",
            "",
            "\1\u00ea",
            "\1\u00eb",
            "\1\u00ec",
            "\1\u00ed",
            "\1\u00ee",
            "\1\u00ef",
            "\1\u00f0",
            "\1\u00f1",
            "\1\u00f2",
            "\1\u00f3",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u00f5",
            "\1\u00f6",
            "\1\u00f7",
            "\1\u00f8",
            "\1\u00f9",
            "\1\u00fa\16\uffff\1\u00fb",
            "\1\u00fc",
            "\1\u00fd",
            "",
            "",
            "\1\u00fe",
            "\1\u00ff",
            "\1\u0100",
            "\1\u0101",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0104",
            "\1\u0105",
            "\1\u0106",
            "",
            "",
            "\1\u0107",
            "\1\u0108",
            "\1\u0109",
            "\1\u010a",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u010d",
            "\1\u010e",
            "\1\u010f",
            "\1\u0110",
            "\1\uffff",
            "",
            "",
            "",
            "\42\u00d0\1\u00cf\130\u00d0\1\u00d1\uff83\u00d0",
            "",
            "",
            "\47\u00d3\1\u00d2\123\u00d3\1\u00d4\uff83\u00d3",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\16\54"+
            "\1\u0112\13\54\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54"+
            "\u1040\uffff\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e"+
            "\54\u10d2\uffff\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\u0114",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0118",
            "\1\u0119",
            "",
            "\1\u011a",
            "\1\u011b",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u011d",
            "\1\u011e",
            "\1\u011f",
            "\1\u0120",
            "\1\u0121",
            "",
            "\1\u0122",
            "\1\u0123",
            "\1\u0124",
            "\1\u0126\6\uffff\1\u0125\5\uffff\1\u0127",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0129",
            "\1\u012a",
            "\1\u012b",
            "\1\u012c",
            "\1\u012d",
            "\1\u012e",
            "\1\u012f",
            "",
            "\1\u0130",
            "\1\u0131",
            "\1\u0132",
            "\1\u0133",
            "\1\u0134",
            "\1\u0135",
            "\1\u0136",
            "\1\u0137",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0139",
            "\1\u013a",
            "\1\u013b",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "",
            "\1\u013d",
            "\1\u013e",
            "\1\u013f",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0142",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "",
            "\1\u0144",
            "\1\u0145",
            "\1\u0146",
            "\1\u0147",
            "",
            "\1\u0148",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "",
            "",
            "\1\u014a",
            "\1\u014b",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u014d",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u014f",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0152",
            "\1\u0153",
            "\1\u0154",
            "\1\u0155",
            "\1\u0156",
            "\1\u0157",
            "\1\u0158",
            "",
            "\1\u0159",
            "\1\u015a",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u015d",
            "\1\u015e",
            "\1\u015f",
            "\1\u0160",
            "\1\u0161",
            "\1\u0162",
            "\1\u0163",
            "\1\u0164",
            "\1\u0165",
            "\1\u0166",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\16\54"+
            "\1\u0167\13\54\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54"+
            "\u1040\uffff\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e"+
            "\54\u10d2\uffff\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\u0169",
            "\1\u016a",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u016d",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "",
            "\1\u016f",
            "",
            "\1\u0170",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0173",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0176",
            "",
            "\1\u0177",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "",
            "\1\u0179",
            "\1\u017a",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u017c",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u017e",
            "\1\u017f",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0183",
            "\1\u0184",
            "\1\u0185",
            "\1\u0186",
            "\1\u0187",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0189",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u018b",
            "\1\u018c",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u018e",
            "",
            "",
            "\1\u018f",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0191",
            "",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "",
            "\1\u0193",
            "\1\u0194",
            "",
            "\1\u0195",
            "\1\u0196",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\u0198",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "",
            "",
            "\1\u019a",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u019e",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\u01a0",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u01a3",
            "",
            "\1\u01a4",
            "",
            "\1\u01a5",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "",
            "",
            "\1\u01ab",
            "",
            "\1\u01ac",
            "",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u01ae",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "",
            "",
            "",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u01b1",
            "",
            "\1\u01b2",
            "",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u01b4",
            "",
            "\1\u01b5",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            ""
    };

    static final short[] DFA31_eot = DFA.unpackEncodedString(DFA31_eotS);
    static final short[] DFA31_eof = DFA.unpackEncodedString(DFA31_eofS);
    static final char[] DFA31_min = DFA.unpackEncodedStringToUnsignedChars(DFA31_minS);
    static final char[] DFA31_max = DFA.unpackEncodedStringToUnsignedChars(DFA31_maxS);
    static final short[] DFA31_accept = DFA.unpackEncodedString(DFA31_acceptS);
    static final short[] DFA31_special = DFA.unpackEncodedString(DFA31_specialS);
    static final short[][] DFA31_transition;

    static {
        int numStates = DFA31_transitionS.length;
        DFA31_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA31_transition[i] = DFA.unpackEncodedString(DFA31_transitionS[i]);
        }
    }

    class DFA31 extends DFA {

        public DFA31(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 31;
            this.eot = DFA31_eot;
            this.eof = DFA31_eof;
            this.min = DFA31_min;
            this.max = DFA31_max;
            this.accept = DFA31_accept;
            this.special = DFA31_special;
            this.transition = DFA31_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( BAR | POUND | TYPEOF | DOTDOT | LARROW | ABSTRACT | AFTER | AND | AS | ASSERT | ATTRIBUTE | BEFORE | BIND | BREAK | BY | CATCH | CLASS | DELETE | DISTINCT | DO | DUR | EASEBOTH | EASEIN | EASEOUT | TIE | STAYS | RETURN | THROW | VAR | PACKAGE | IMPORT | FROM | ON | INSERT | INTO | FIRST | LAST | IF | THEN | ELSE | THIS | NULL | TRUE | FALSE | FOR | UNITINTERVAL | IN | FPS | WHILE | CONTINUE | LINEAR | MOTION | TRY | FINALLY | LAZY | WHERE | NOT | NEW | PRIVATE | PROTECTED | PUBLIC | FUNCTION | READONLY | INVERSE | TYPE | EXTENDS | ORDER | INDEX | INSTANCEOF | INDEXOF | SELECT | SUPER | OR | SIZEOF | REVERSE | LPAREN | RPAREN | LBRACKET | RBRACKET | SEMI | COMMA | DOT | EQEQ | EQ | GT | LT | LTGT | LTEQ | GTEQ | PLUS | PLUSPLUS | SUB | SUBSUB | STAR | SLASH | PERCENT | PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ | LTLT | GTGT | COLON | QUES | STRING_LITERAL | QUOTE_LBRACE_STRING_LITERAL | LBRACE | RBRACE_QUOTE_STRING_LITERAL | RBRACE_LBRACE_STRING_LITERAL | RBRACE | FORMAT_STRING_LITERAL | QUOTED_IDENTIFIER | INTEGER_LITERAL | FLOATING_POINT_LITERAL | IDENTIFIER | WS | COMMENT | LINE_COMMENT );";
        }
        public int specialStateTransition(int s, IntStream input) throws NoViableAltException {
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA31_211 = input.LA(1);

                         
                        int index31_211 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA31_211=='\'') && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 210;}

                        else if ( ((LA31_211>='\u0000' && LA31_211<='&')||(LA31_211>='(' && LA31_211<='z')||(LA31_211>='|' && LA31_211<='\uFFFE')) && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 211;}

                        else if ( (LA31_211=='{') && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 212;}

                         
                        input.seek(index31_211);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA31_35 = input.LA(1);

                         
                        int index31_35 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA31_35=='=') ) {s = 122;}

                        else if ( ((LA31_35>='\u0000' && LA31_35<='\u001F')||(LA31_35>='!' && LA31_35<='<')||(LA31_35>='>' && LA31_35<='\uFFFE')) && ( BraceQuoteTracker.percentIsFormat() )) {s = 123;}

                        else s = 124;

                         
                        input.seek(index31_35);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA31_0 = input.LA(1);

                         
                        int index31_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA31_0=='|') ) {s = 1;}

                        else if ( (LA31_0=='#') ) {s = 2;}

                        else if ( (LA31_0=='t') ) {s = 3;}

                        else if ( (LA31_0=='.') ) {s = 4;}

                        else if ( (LA31_0=='<') ) {s = 5;}

                        else if ( (LA31_0=='a') ) {s = 6;}

                        else if ( (LA31_0=='b') ) {s = 7;}

                        else if ( (LA31_0=='c') ) {s = 8;}

                        else if ( (LA31_0=='d') ) {s = 9;}

                        else if ( (LA31_0=='e') ) {s = 10;}

                        else if ( (LA31_0=='s') ) {s = 11;}

                        else if ( (LA31_0=='r') ) {s = 12;}

                        else if ( (LA31_0=='v') ) {s = 13;}

                        else if ( (LA31_0=='p') ) {s = 14;}

                        else if ( (LA31_0=='i') ) {s = 15;}

                        else if ( (LA31_0=='f') ) {s = 16;}

                        else if ( (LA31_0=='o') ) {s = 17;}

                        else if ( (LA31_0=='l') ) {s = 18;}

                        else if ( (LA31_0=='n') ) {s = 19;}

                        else if ( (LA31_0=='u') ) {s = 20;}

                        else if ( (LA31_0=='w') ) {s = 21;}

                        else if ( (LA31_0=='m') ) {s = 22;}

                        else if ( (LA31_0=='(') ) {s = 23;}

                        else if ( (LA31_0==')') ) {s = 24;}

                        else if ( (LA31_0=='[') ) {s = 25;}

                        else if ( (LA31_0==']') ) {s = 26;}

                        else if ( (LA31_0==';') ) {s = 27;}

                        else if ( (LA31_0==',') ) {s = 28;}

                        else if ( (LA31_0=='=') ) {s = 29;}

                        else if ( (LA31_0=='>') ) {s = 30;}

                        else if ( (LA31_0=='+') ) {s = 31;}

                        else if ( (LA31_0=='-') ) {s = 32;}

                        else if ( (LA31_0=='*') ) {s = 33;}

                        else if ( (LA31_0=='/') ) {s = 34;}

                        else if ( (LA31_0=='%') ) {s = 35;}

                        else if ( (LA31_0==':') ) {s = 36;}

                        else if ( (LA31_0=='?') ) {s = 37;}

                        else if ( (LA31_0=='\"') ) {s = 38;}

                        else if ( (LA31_0=='\'') ) {s = 39;}

                        else if ( (LA31_0=='{') ) {s = 40;}

                        else if ( (LA31_0=='}') && (( !BraceQuoteTracker.rightBraceLikeQuote(CUR_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 41;}

                        else if ( (LA31_0=='0') ) {s = 42;}

                        else if ( ((LA31_0>='1' && LA31_0<='9')) ) {s = 43;}

                        else if ( (LA31_0=='$'||(LA31_0>='A' && LA31_0<='Z')||LA31_0=='_'||(LA31_0>='g' && LA31_0<='h')||(LA31_0>='j' && LA31_0<='k')||LA31_0=='q'||(LA31_0>='x' && LA31_0<='z')||(LA31_0>='\u00C0' && LA31_0<='\u00D6')||(LA31_0>='\u00D8' && LA31_0<='\u00F6')||(LA31_0>='\u00F8' && LA31_0<='\u1FFF')||(LA31_0>='\u3040' && LA31_0<='\u318F')||(LA31_0>='\u3300' && LA31_0<='\u337F')||(LA31_0>='\u3400' && LA31_0<='\u3D2D')||(LA31_0>='\u4E00' && LA31_0<='\u9FFF')||(LA31_0>='\uF900' && LA31_0<='\uFAFF')) ) {s = 44;}

                        else if ( ((LA31_0>='\t' && LA31_0<='\n')||(LA31_0>='\f' && LA31_0<='\r')||LA31_0==' ') ) {s = 45;}

                         
                        input.seek(index31_0);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA31_122 = input.LA(1);

                         
                        int index31_122 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((LA31_122>='\u0000' && LA31_122<='\u001F')||(LA31_122>='!' && LA31_122<='\uFFFE')) && ( BraceQuoteTracker.percentIsFormat() )) {s = 123;}

                        else s = 204;

                         
                        input.seek(index31_122);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA31_124 = input.LA(1);

                         
                        int index31_124 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (!( BraceQuoteTracker.percentIsFormat() )) ) {s = 205;}

                        else if ( ( BraceQuoteTracker.percentIsFormat() ) ) {s = 123;}

                         
                        input.seek(index31_124);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA31_204 = input.LA(1);

                         
                        int index31_204 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (!( BraceQuoteTracker.percentIsFormat() )) ) {s = 273;}

                        else if ( ( BraceQuoteTracker.percentIsFormat() ) ) {s = 123;}

                         
                        input.seek(index31_204);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA31_129 = input.LA(1);

                         
                        int index31_129 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA31_129=='\'') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 130;}

                        else if ( (LA31_129=='\"') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 133;}

                        else if ( (LA31_129=='{') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 132;}

                        else if ( ((LA31_129>='\u0000' && LA31_129<='!')||(LA31_129>='#' && LA31_129<='&')||(LA31_129>='(' && LA31_129<='z')||(LA31_129>='|' && LA31_129<='\uFFFE')) && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 129;}

                         
                        input.seek(index31_129);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA31_133 = input.LA(1);

                         
                        int index31_133 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA31_133=='\'') && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 210;}

                        else if ( ((LA31_133>='\u0000' && LA31_133<='&')||(LA31_133>='(' && LA31_133<='z')||(LA31_133>='|' && LA31_133<='\uFFFE')) && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 211;}

                        else if ( (LA31_133=='{') && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 212;}

                        else s = 207;

                         
                        input.seek(index31_133);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA31_41 = input.LA(1);

                         
                        int index31_41 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((LA31_41>='\u0000' && LA31_41<='!')||(LA31_41>='#' && LA31_41<='&')||(LA31_41>='(' && LA31_41<='z')||(LA31_41>='|' && LA31_41<='\uFFFE')) && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 129;}

                        else if ( (LA31_41=='\'') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 130;}

                        else if ( (LA31_41=='{') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 132;}

                        else if ( (LA31_41=='\"') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 133;}

                        else s = 131;

                         
                        input.seek(index31_41);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA31_130 = input.LA(1);

                         
                        int index31_130 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA31_130=='\"') && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 207;}

                        else if ( ((LA31_130>='\u0000' && LA31_130<='!')||(LA31_130>='#' && LA31_130<='z')||(LA31_130>='|' && LA31_130<='\uFFFE')) && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 208;}

                        else if ( (LA31_130=='{') && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 209;}

                        else s = 206;

                         
                        input.seek(index31_130);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA31_208 = input.LA(1);

                         
                        int index31_208 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA31_208=='\"') && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 207;}

                        else if ( ((LA31_208>='\u0000' && LA31_208<='!')||(LA31_208>='#' && LA31_208<='z')||(LA31_208>='|' && LA31_208<='\uFFFE')) && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 208;}

                        else if ( (LA31_208=='{') && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 209;}

                         
                        input.seek(index31_208);
                        if ( s>=0 ) return s;
                        break;
            }
            if (backtracking>0) {failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 31, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}