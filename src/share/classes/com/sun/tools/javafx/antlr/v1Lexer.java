// $ANTLR 3.0 \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g 2007-08-03 11:46:22

package com.sun.tools.javafx.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
public class v1Lexer extends Lexer {
    public static final int FUNCTION=70;
    public static final int PACKAGE=34;
    public static final int LT=95;
    public static final int STAR=103;
    public static final int LATER=37;
    public static final int WHILE=55;
    public static final int EASEOUT=28;
    public static final int TRIGGER=38;
    public static final int NEW=65;
    public static final int INDEXOF=78;
    public static final int DO=24;
    public static final int UNITINTERVAL=52;
    public static final int NOT=64;
    public static final int EOF=-1;
    public static final int GTGT=112;
    public static final int RBRACE_QUOTE_STRING_LITERAL=119;
    public static final int BREAK=18;
    public static final int TYPE=73;
    public static final int LBRACKET=87;
    public static final int RPAREN=86;
    public static final int LINEAR=57;
    public static final int IMPORT=35;
    public static final int STRING_LITERAL=115;
    public static final int INSERT=40;
    public static final int FLOATING_POINT_LITERAL=126;
    public static final int SUBSUB=102;
    public static final int BIND=16;
    public static final int STAREQ=108;
    public static final int THIS=47;
    public static final int RETURN=31;
    public static final int VAR=33;
    public static final int SUPER=80;
    public static final int LAST=43;
    public static final int EQ=93;
    public static final int COMMENT=131;
    public static final int SELECT=79;
    public static final int INTO=41;
    public static final int QUES=114;
    public static final int EQEQ=92;
    public static final int MOTION=58;
    public static final int RBRACE=121;
    public static final int POUND=5;
    public static final int LINE_COMMENT=132;
    public static final int PRIVATE=66;
    public static final int NULL=48;
    public static final int ELSE=46;
    public static final int ON=39;
    public static final int DELETE=22;
    public static final int SLASHEQ=109;
    public static final int EASEBOTH=26;
    public static final int ASSERT=13;
    public static final int TRY=59;
    public static final int INVERSE=72;
    public static final int WS=130;
    public static final int TYPEOF=6;
    public static final int INTEGER_LITERAL=124;
    public static final int OR=81;
    public static final int JavaIDDigit=128;
    public static final int SIZEOF=82;
    public static final int GT=94;
    public static final int FOREACH=62;
    public static final int FROM=36;
    public static final int CATCH=20;
    public static final int OPERATION=69;
    public static final int REVERSE=83;
    public static final int FALSE=50;
    public static final int DISTINCT=23;
    public static final int Letter=127;
    public static final int THROW=32;
    public static final int DUR=25;
    public static final int WHERE=63;
    public static final int PROTECTED=67;
    public static final int CLASS=21;
    public static final int ORDER=75;
    public static final int PLUSPLUS=100;
    public static final int LBRACE=118;
    public static final int LTEQ=97;
    public static final int ATTRIBUTE=14;
    public static final int SUBEQ=107;
    public static final int BIBIND=17;
    public static final int Exponent=125;
    public static final int LARROW=8;
    public static final int FOR=51;
    public static final int SUB=101;
    public static final int DOTDOT=7;
    public static final int ABSTRACT=9;
    public static final int NextIsPercent=116;
    public static final int AND=11;
    public static final int PLUSEQ=106;
    public static final int LPAREN=85;
    public static final int IF=44;
    public static final int INDEX=76;
    public static final int AS=12;
    public static final int SLASH=104;
    public static final int IN=53;
    public static final int THEN=45;
    public static final int CONTINUE=56;
    public static final int COMMA=90;
    public static final int TIE=29;
    public static final int IDENTIFIER=129;
    public static final int QUOTE_LBRACE_STRING_LITERAL=117;
    public static final int PLUS=99;
    public static final int RBRACKET=88;
    public static final int DOT=91;
    public static final int RBRACE_LBRACE_STRING_LITERAL=120;
    public static final int LTLT=111;
    public static final int STAYS=30;
    public static final int BY=19;
    public static final int XOR=84;
    public static final int PERCENT=105;
    public static final int LAZY=61;
    public static final int LTGT=96;
    public static final int BEFORE=15;
    public static final int INSTANCEOF=77;
    public static final int AFTER=10;
    public static final int GTEQ=98;
    public static final int Tokens=133;
    public static final int READONLY=71;
    public static final int SEMI=89;
    public static final int TRUE=49;
    public static final int COLON=113;
    public static final int FINALLY=60;
    public static final int PERCENTEQ=110;
    public static final int EASEIN=27;
    public static final int FORMAT_STRING_LITERAL=122;
    public static final int QUOTED_IDENTIFIER=123;
    public static final int FPS=54;
    public static final int EXTENDS=74;
    public static final int PUBLIC=68;
    public static final int BAR=4;
    public static final int FIRST=42;
    
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
     
    public v1Lexer() {;} 
    public v1Lexer(CharStream input) {
        super(input);
        ruleMemo = new HashMap[131+1];
     }
    public String getGrammarFileName() { return "\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g"; }

    // $ANTLR start BAR
    public final void mBAR() throws RecognitionException {
        try {
            int _type = BAR;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:66:5: ( '|' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:66:7: '|'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:67:7: ( '#' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:67:9: '#'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:68:8: ( 'typeof' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:68:10: 'typeof'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:69:8: ( '..' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:69:10: '..'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:70:8: ( '<-' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:70:10: '<-'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:71:10: ( 'abstract' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:71:12: 'abstract'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:72:7: ( 'after' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:72:9: 'after'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:73:5: ( 'and' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:73:7: 'and'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:74:4: ( 'as' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:74:6: 'as'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:75:8: ( 'assert' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:75:10: 'assert'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:76:11: ( 'attribute' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:76:13: 'attribute'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:77:8: ( 'before' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:77:10: 'before'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:78:6: ( 'bind' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:78:8: 'bind'
            {
            match("bind"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end BIND

    // $ANTLR start BIBIND
    public final void mBIBIND() throws RecognitionException {
        try {
            int _type = BIBIND;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:79:8: ( 'bibind' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:79:10: 'bibind'
            {
            match("bibind"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end BIBIND

    // $ANTLR start BREAK
    public final void mBREAK() throws RecognitionException {
        try {
            int _type = BREAK;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:80:7: ( 'break' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:80:9: 'break'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:81:4: ( 'by' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:81:6: 'by'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:82:7: ( 'catch' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:82:9: 'catch'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:83:7: ( 'class' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:83:9: 'class'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:84:8: ( 'delete' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:84:10: 'delete'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:85:10: ( 'distinct' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:85:12: 'distinct'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:86:4: ( 'do' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:86:6: 'do'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:87:5: ( 'dur' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:87:7: 'dur'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:88:10: ( 'easeboth' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:88:12: 'easeboth'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:89:8: ( 'easein' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:89:10: 'easein'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:90:9: ( 'easeout' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:90:11: 'easeout'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:91:5: ( 'tie' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:91:7: 'tie'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:92:7: ( 'stays' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:92:9: 'stays'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:93:8: ( 'return' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:93:10: 'return'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:94:7: ( 'throw' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:94:9: 'throw'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:95:5: ( 'var' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:95:7: 'var'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:96:9: ( 'package' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:96:11: 'package'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:97:8: ( 'import' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:97:10: 'import'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:98:6: ( 'from' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:98:8: 'from'
            {
            match("from"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FROM

    // $ANTLR start LATER
    public final void mLATER() throws RecognitionException {
        try {
            int _type = LATER;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:99:7: ( 'later' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:99:9: 'later'
            {
            match("later"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LATER

    // $ANTLR start TRIGGER
    public final void mTRIGGER() throws RecognitionException {
        try {
            int _type = TRIGGER;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:100:9: ( 'trigger' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:100:11: 'trigger'
            {
            match("trigger"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end TRIGGER

    // $ANTLR start ON
    public final void mON() throws RecognitionException {
        try {
            int _type = ON;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:101:4: ( 'on' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:101:6: 'on'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:102:8: ( 'insert' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:102:10: 'insert'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:103:6: ( 'into' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:103:8: 'into'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:104:7: ( 'first' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:104:9: 'first'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:105:6: ( 'last' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:105:8: 'last'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:106:4: ( 'if' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:106:6: 'if'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:107:6: ( 'then' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:107:8: 'then'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:108:6: ( 'else' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:108:8: 'else'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:109:6: ( 'this' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:109:8: 'this'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:110:6: ( 'null' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:110:8: 'null'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:111:6: ( 'true' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:111:8: 'true'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:112:7: ( 'false' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:112:9: 'false'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:113:5: ( 'for' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:113:7: 'for'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:114:14: ( 'unitinterval' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:114:16: 'unitinterval'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:115:4: ( 'in' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:115:6: 'in'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:116:5: ( 'fps' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:116:7: 'fps'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:117:7: ( 'while' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:117:9: 'while'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:118:10: ( 'continue' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:118:12: 'continue'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:119:8: ( 'linear' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:119:10: 'linear'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:120:8: ( 'motion' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:120:10: 'motion'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:121:5: ( 'try' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:121:7: 'try'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:122:9: ( 'finally' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:122:11: 'finally'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:123:6: ( 'lazy' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:123:8: 'lazy'
            {
            match("lazy"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LAZY

    // $ANTLR start FOREACH
    public final void mFOREACH() throws RecognitionException {
        try {
            int _type = FOREACH;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:124:9: ( 'foreach' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:124:11: 'foreach'
            {
            match("foreach"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FOREACH

    // $ANTLR start WHERE
    public final void mWHERE() throws RecognitionException {
        try {
            int _type = WHERE;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:125:7: ( 'where' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:125:9: 'where'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:126:5: ( 'not' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:126:7: 'not'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:127:5: ( 'new' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:127:7: 'new'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:128:9: ( 'private' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:128:11: 'private'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:129:11: ( 'protected' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:129:13: 'protected'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:130:8: ( 'public' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:130:10: 'public'
            {
            match("public"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PUBLIC

    // $ANTLR start OPERATION
    public final void mOPERATION() throws RecognitionException {
        try {
            int _type = OPERATION;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:131:11: ( 'operation' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:131:13: 'operation'
            {
            match("operation"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end OPERATION

    // $ANTLR start FUNCTION
    public final void mFUNCTION() throws RecognitionException {
        try {
            int _type = FUNCTION;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:132:10: ( 'function' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:132:12: 'function'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:133:10: ( 'readonly' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:133:12: 'readonly'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:134:9: ( 'inverse' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:134:11: 'inverse'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:135:6: ( 'type' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:135:8: 'type'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:136:9: ( 'extends' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:136:11: 'extends'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:137:7: ( 'order' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:137:9: 'order'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:138:7: ( 'index' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:138:9: 'index'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:139:12: ( 'instanceof' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:139:14: 'instanceof'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:140:9: ( 'indexof' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:140:11: 'indexof'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:141:8: ( 'select' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:141:10: 'select'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:142:7: ( 'super' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:142:9: 'super'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:143:4: ( 'or' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:143:6: 'or'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:144:8: ( 'sizeof' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:144:10: 'sizeof'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:145:9: ( 'reverse' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:145:11: 'reverse'
            {
            match("reverse"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end REVERSE

    // $ANTLR start XOR
    public final void mXOR() throws RecognitionException {
        try {
            int _type = XOR;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:146:5: ( 'xor' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:146:7: 'xor'
            {
            match("xor"); if (failed) return ;


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end XOR

    // $ANTLR start LPAREN
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:147:8: ( '(' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:147:10: '('
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:148:8: ( ')' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:148:10: ')'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:149:10: ( '[' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:149:12: '['
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:150:10: ( ']' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:150:12: ']'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:151:6: ( ';' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:151:8: ';'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:152:7: ( ',' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:152:9: ','
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:153:5: ( '.' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:153:7: '.'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:154:6: ( '==' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:154:8: '=='
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:155:4: ( '=' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:155:6: '='
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:156:4: ( '>' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:156:6: '>'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:157:4: ( '<' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:157:6: '<'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:158:6: ( '<>' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:158:8: '<>'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:159:6: ( '<=' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:159:8: '<='
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:160:6: ( '>=' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:160:8: '>='
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:161:6: ( '+' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:161:8: '+'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:162:10: ( '++' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:162:12: '++'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:163:5: ( '-' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:163:7: '-'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:164:8: ( '--' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:164:10: '--'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:165:6: ( '*' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:165:8: '*'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:166:7: ( '/' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:166:9: '/'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:167:9: ( '%' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:167:11: '%'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:168:8: ( '+=' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:168:10: '+='
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:169:7: ( '-=' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:169:9: '-='
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:170:8: ( '*=' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:170:10: '*='
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:171:9: ( '/=' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:171:11: '/='
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:172:11: ( '%=' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:172:13: '%='
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:173:6: ( '<<' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:173:8: '<<'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:174:6: ( '>>' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:174:8: '>>'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:175:7: ( ':' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:175:9: ':'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:176:6: ( '?' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:176:8: '?'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:215:19: ( '\"' (~ ( '{' | '\"' ) )* '\"' | '\\'' (~ ( '{' | '\\'' ) )* '\\'' )
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
                    new NoViableAltException("215:1: STRING_LITERAL : ( '\"' (~ ( '{' | '\"' ) )* '\"' | '\\'' (~ ( '{' | '\\'' ) )* '\\'' );", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:215:21: '\"' (~ ( '{' | '\"' ) )* '\"'
                    {
                    match('\"'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:215:25: (~ ( '{' | '\"' ) )*
                    loop1:
                    do {
                        int alt1=2;
                        int LA1_0 = input.LA(1);

                        if ( ((LA1_0>='\u0000' && LA1_0<='!')||(LA1_0>='#' && LA1_0<='z')||(LA1_0>='|' && LA1_0<='\uFFFE')) ) {
                            alt1=1;
                        }


                        switch (alt1) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:215:26: ~ ( '{' | '\"' )
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
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:216:7: '\\'' (~ ( '{' | '\\'' ) )* '\\''
                    {
                    match('\''); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:216:12: (~ ( '{' | '\\'' ) )*
                    loop2:
                    do {
                        int alt2=2;
                        int LA2_0 = input.LA(1);

                        if ( ((LA2_0>='\u0000' && LA2_0<='&')||(LA2_0>='(' && LA2_0<='z')||(LA2_0>='|' && LA2_0<='\uFFFE')) ) {
                            alt2=1;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:216:13: ~ ( '{' | '\\'' )
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:219:30: ( '\"' (~ ( '{' | '\"' ) )* '{' NextIsPercent[DBL_QUOTE_CTX] | '\\'' (~ ( '{' | '\\'' ) )* '{' NextIsPercent[SNG_QUOTE_CTX] )
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
                    new NoViableAltException("219:1: QUOTE_LBRACE_STRING_LITERAL : ( '\"' (~ ( '{' | '\"' ) )* '{' NextIsPercent[DBL_QUOTE_CTX] | '\\'' (~ ( '{' | '\\'' ) )* '{' NextIsPercent[SNG_QUOTE_CTX] );", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:219:32: '\"' (~ ( '{' | '\"' ) )* '{' NextIsPercent[DBL_QUOTE_CTX]
                    {
                    match('\"'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:219:36: (~ ( '{' | '\"' ) )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( ((LA4_0>='\u0000' && LA4_0<='!')||(LA4_0>='#' && LA4_0<='z')||(LA4_0>='|' && LA4_0<='\uFFFE')) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:219:37: ~ ( '{' | '\"' )
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
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:221:7: '\\'' (~ ( '{' | '\\'' ) )* '{' NextIsPercent[SNG_QUOTE_CTX]
                    {
                    match('\''); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:221:12: (~ ( '{' | '\\'' ) )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( ((LA5_0>='\u0000' && LA5_0<='&')||(LA5_0>='(' && LA5_0<='z')||(LA5_0>='|' && LA5_0<='\uFFFE')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:221:13: ~ ( '{' | '\\'' )
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:224:11: ( '{' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:224:13: '{'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:226:30: ({...}? => '}' (~ ( '{' | '\"' ) )* '\"' | {...}? => '}' (~ ( '{' | '\\'' ) )* '\\'' )
            int alt9=2;
            alt9 = dfa9.predict(input);
            switch (alt9) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:226:35: {...}? => '}' (~ ( '{' | '\"' ) )* '\"'
                    {
                    if ( !( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ) ) {
                        if (backtracking>0) {failed=true; return ;}
                        throw new FailedPredicateException(input, "RBRACE_QUOTE_STRING_LITERAL", " BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ");
                    }
                    match('}'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:227:11: (~ ( '{' | '\"' ) )*
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0>='\u0000' && LA7_0<='!')||(LA7_0>='#' && LA7_0<='z')||(LA7_0>='|' && LA7_0<='\uFFFE')) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:227:12: ~ ( '{' | '\"' )
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
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:230:10: {...}? => '}' (~ ( '{' | '\\'' ) )* '\\''
                    {
                    if ( !( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) ) ) {
                        if (backtracking>0) {failed=true; return ;}
                        throw new FailedPredicateException(input, "RBRACE_QUOTE_STRING_LITERAL", " BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) ");
                    }
                    match('}'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:231:11: (~ ( '{' | '\\'' ) )*
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( ((LA8_0>='\u0000' && LA8_0<='&')||(LA8_0>='(' && LA8_0<='z')||(LA8_0>='|' && LA8_0<='\uFFFE')) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:231:12: ~ ( '{' | '\\'' )
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:235:31: ({...}? => '}' (~ ( '{' | '\"' ) )* '{' NextIsPercent[CUR_QUOTE_CTX] | {...}? => '}' (~ ( '{' | '\\'' ) )* '{' NextIsPercent[CUR_QUOTE_CTX] )
            int alt12=2;
            alt12 = dfa12.predict(input);
            switch (alt12) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:235:36: {...}? => '}' (~ ( '{' | '\"' ) )* '{' NextIsPercent[CUR_QUOTE_CTX]
                    {
                    if ( !( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ) ) {
                        if (backtracking>0) {failed=true; return ;}
                        throw new FailedPredicateException(input, "RBRACE_LBRACE_STRING_LITERAL", " BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ");
                    }
                    match('}'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:236:11: (~ ( '{' | '\"' ) )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( ((LA10_0>='\u0000' && LA10_0<='!')||(LA10_0>='#' && LA10_0<='z')||(LA10_0>='|' && LA10_0<='\uFFFE')) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:236:12: ~ ( '{' | '\"' )
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
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:239:10: {...}? => '}' (~ ( '{' | '\\'' ) )* '{' NextIsPercent[CUR_QUOTE_CTX]
                    {
                    if ( !( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) ) ) {
                        if (backtracking>0) {failed=true; return ;}
                        throw new FailedPredicateException(input, "RBRACE_LBRACE_STRING_LITERAL", " BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) ");
                    }
                    match('}'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:240:11: (~ ( '{' | '\\'' ) )*
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( ((LA11_0>='\u0000' && LA11_0<='&')||(LA11_0>='(' && LA11_0<='z')||(LA11_0>='|' && LA11_0<='\uFFFE')) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:240:12: ~ ( '{' | '\\'' )
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:244:11: ({...}? => '}' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:244:16: {...}? => '}'
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:249:6: ( ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )* '%' )=> | )
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
                    new NoViableAltException("247:1: fragment NextIsPercent[int quoteContext] : ( ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )* '%' )=> | );", 13, 0, input);

                throw nvae;
            }
            switch (alt13) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:249:8: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )* '%' )=>
                    {
                    if ( backtracking==0 ) {
                       BraceQuoteTracker.enterBrace(quoteContext, true); 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:251:10: 
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:253:24: ({...}? => '%' (~ ' ' )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:253:30: {...}? => '%' (~ ' ' )*
            {
            if ( !( BraceQuoteTracker.percentIsFormat() ) ) {
                if (backtracking>0) {failed=true; return ;}
                throw new FailedPredicateException(input, "FORMAT_STRING_LITERAL", " BraceQuoteTracker.percentIsFormat() ");
            }
            match('%'); if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:254:11: (~ ' ' )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( ((LA14_0>='\u0000' && LA14_0<='\u001F')||(LA14_0>='!' && LA14_0<='\uFFFE')) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:254:12: ~ ' '
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:258:2: ( '<<' (~ '>' | '>' ~ '>' )* ( '>' )* '>>' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:258:4: '<<' (~ '>' | '>' ~ '>' )* ( '>' )* '>>'
            {
            match("<<"); if (failed) return ;

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:258:9: (~ '>' | '>' ~ '>' )*
            loop15:
            do {
                int alt15=3;
                int LA15_0 = input.LA(1);

                if ( (LA15_0=='>') ) {
                    int LA15_1 = input.LA(2);

                    if ( ((LA15_1>='\u0000' && LA15_1<='=')||(LA15_1>='?' && LA15_1<='\uFFFE')) ) {
                        alt15=2;
                    }


                }
                else if ( ((LA15_0>='\u0000' && LA15_0<='=')||(LA15_0>='?' && LA15_0<='\uFFFE')) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:258:10: ~ '>'
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='=')||(input.LA(1)>='?' && input.LA(1)<='\uFFFE') ) {
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
            	case 2 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:258:16: '>' ~ '>'
            	    {
            	    match('>'); if (failed) return ;
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='=')||(input.LA(1)>='?' && input.LA(1)<='\uFFFE') ) {
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
            	    break loop15;
                }
            } while (true);

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:258:27: ( '>' )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0=='>') ) {
                    int LA16_1 = input.LA(2);

                    if ( (LA16_1=='>') ) {
                        int LA16_2 = input.LA(3);

                        if ( (LA16_2=='>') ) {
                            alt16=1;
                        }


                    }


                }


                switch (alt16) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:258:27: '>'
            	    {
            	    match('>'); if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop16;
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:260:17: ( ( '0' | '1' .. '9' ( '0' .. '9' )* ) )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:260:19: ( '0' | '1' .. '9' ( '0' .. '9' )* )
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:260:19: ( '0' | '1' .. '9' ( '0' .. '9' )* )
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0=='0') ) {
                alt18=1;
            }
            else if ( ((LA18_0>='1' && LA18_0<='9')) ) {
                alt18=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("260:19: ( '0' | '1' .. '9' ( '0' .. '9' )* )", 18, 0, input);

                throw nvae;
            }
            switch (alt18) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:260:20: '0'
                    {
                    match('0'); if (failed) return ;

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:260:26: '1' .. '9' ( '0' .. '9' )*
                    {
                    matchRange('1','9'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:260:35: ( '0' .. '9' )*
                    loop17:
                    do {
                        int alt17=2;
                        int LA17_0 = input.LA(1);

                        if ( ((LA17_0>='0' && LA17_0<='9')) ) {
                            alt17=1;
                        }


                        switch (alt17) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:260:35: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop17;
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:263:5: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( Exponent )? | '.' ( '0' .. '9' )+ ( Exponent )? | ( '0' .. '9' )+ Exponent )
            int alt25=3;
            alt25 = dfa25.predict(input);
            switch (alt25) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:263:9: ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( Exponent )?
                    {
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:263:9: ( '0' .. '9' )+
                    int cnt19=0;
                    loop19:
                    do {
                        int alt19=2;
                        int LA19_0 = input.LA(1);

                        if ( ((LA19_0>='0' && LA19_0<='9')) ) {
                            alt19=1;
                        }


                        switch (alt19) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:263:10: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt19 >= 1 ) break loop19;
                    	    if (backtracking>0) {failed=true; return ;}
                                EarlyExitException eee =
                                    new EarlyExitException(19, input);
                                throw eee;
                        }
                        cnt19++;
                    } while (true);

                    match('.'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:263:25: ( '0' .. '9' )*
                    loop20:
                    do {
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( ((LA20_0>='0' && LA20_0<='9')) ) {
                            alt20=1;
                        }


                        switch (alt20) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:263:26: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop20;
                        }
                    } while (true);

                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:263:37: ( Exponent )?
                    int alt21=2;
                    int LA21_0 = input.LA(1);

                    if ( (LA21_0=='E'||LA21_0=='e') ) {
                        alt21=1;
                    }
                    switch (alt21) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:263:37: Exponent
                            {
                            mExponent(); if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:264:9: '.' ( '0' .. '9' )+ ( Exponent )?
                    {
                    match('.'); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:264:13: ( '0' .. '9' )+
                    int cnt22=0;
                    loop22:
                    do {
                        int alt22=2;
                        int LA22_0 = input.LA(1);

                        if ( ((LA22_0>='0' && LA22_0<='9')) ) {
                            alt22=1;
                        }


                        switch (alt22) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:264:14: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt22 >= 1 ) break loop22;
                    	    if (backtracking>0) {failed=true; return ;}
                                EarlyExitException eee =
                                    new EarlyExitException(22, input);
                                throw eee;
                        }
                        cnt22++;
                    } while (true);

                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:264:25: ( Exponent )?
                    int alt23=2;
                    int LA23_0 = input.LA(1);

                    if ( (LA23_0=='E'||LA23_0=='e') ) {
                        alt23=1;
                    }
                    switch (alt23) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:264:25: Exponent
                            {
                            mExponent(); if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:265:9: ( '0' .. '9' )+ Exponent
                    {
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:265:9: ( '0' .. '9' )+
                    int cnt24=0;
                    loop24:
                    do {
                        int alt24=2;
                        int LA24_0 = input.LA(1);

                        if ( ((LA24_0>='0' && LA24_0<='9')) ) {
                            alt24=1;
                        }


                        switch (alt24) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:265:10: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt24 >= 1 ) break loop24;
                    	    if (backtracking>0) {failed=true; return ;}
                                EarlyExitException eee =
                                    new EarlyExitException(24, input);
                                throw eee;
                        }
                        cnt24++;
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:269:10: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:269:12: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
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

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:269:22: ( '+' | '-' )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0=='+'||LA26_0=='-') ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
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

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:269:33: ( '0' .. '9' )+
            int cnt27=0;
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( ((LA27_0>='0' && LA27_0<='9')) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:269:34: '0' .. '9'
            	    {
            	    matchRange('0','9'); if (failed) return ;

            	    }
            	    break;

            	default :
            	    if ( cnt27 >= 1 ) break loop27;
            	    if (backtracking>0) {failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(27, input);
                        throw eee;
                }
                cnt27++;
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:272:5: ( Letter ( Letter | JavaIDDigit )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:272:9: Letter ( Letter | JavaIDDigit )*
            {
            mLetter(); if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:272:16: ( Letter | JavaIDDigit )*
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( (LA28_0=='$'||(LA28_0>='0' && LA28_0<='9')||(LA28_0>='A' && LA28_0<='Z')||LA28_0=='_'||(LA28_0>='a' && LA28_0<='z')||(LA28_0>='\u00C0' && LA28_0<='\u00D6')||(LA28_0>='\u00D8' && LA28_0<='\u00F6')||(LA28_0>='\u00F8' && LA28_0<='\u1FFF')||(LA28_0>='\u3040' && LA28_0<='\u318F')||(LA28_0>='\u3300' && LA28_0<='\u337F')||(LA28_0>='\u3400' && LA28_0<='\u3D2D')||(LA28_0>='\u4E00' && LA28_0<='\u9FFF')||(LA28_0>='\uF900' && LA28_0<='\uFAFF')) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
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
            	    break loop28;
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:277:5: ( '\\u0024' | '\\u0041' .. '\\u005a' | '\\u005f' | '\\u0061' .. '\\u007a' | '\\u00c0' .. '\\u00d6' | '\\u00d8' .. '\\u00f6' | '\\u00f8' .. '\\u00ff' | '\\u0100' .. '\\u1fff' | '\\u3040' .. '\\u318f' | '\\u3300' .. '\\u337f' | '\\u3400' .. '\\u3d2d' | '\\u4e00' .. '\\u9fff' | '\\uf900' .. '\\ufaff' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:294:5: ( '\\u0030' .. '\\u0039' | '\\u0660' .. '\\u0669' | '\\u06f0' .. '\\u06f9' | '\\u0966' .. '\\u096f' | '\\u09e6' .. '\\u09ef' | '\\u0a66' .. '\\u0a6f' | '\\u0ae6' .. '\\u0aef' | '\\u0b66' .. '\\u0b6f' | '\\u0be7' .. '\\u0bef' | '\\u0c66' .. '\\u0c6f' | '\\u0ce6' .. '\\u0cef' | '\\u0d66' .. '\\u0d6f' | '\\u0e50' .. '\\u0e59' | '\\u0ed0' .. '\\u0ed9' | '\\u1040' .. '\\u1049' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:311:5: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' ) )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:311:8: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:315:5: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:315:9: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); if (failed) return ;

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:315:14: ( options {greedy=false; } : . )*
            loop29:
            do {
                int alt29=2;
                int LA29_0 = input.LA(1);

                if ( (LA29_0=='*') ) {
                    int LA29_1 = input.LA(2);

                    if ( (LA29_1=='/') ) {
                        alt29=2;
                    }
                    else if ( ((LA29_1>='\u0000' && LA29_1<='.')||(LA29_1>='0' && LA29_1<='\uFFFE')) ) {
                        alt29=1;
                    }


                }
                else if ( ((LA29_0>='\u0000' && LA29_0<=')')||(LA29_0>='+' && LA29_0<='\uFFFE')) ) {
                    alt29=1;
                }


                switch (alt29) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:315:42: .
            	    {
            	    matchAny(); if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop29;
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:319:5: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:319:7: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
            {
            match("//"); if (failed) return ;

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:319:12: (~ ( '\\n' | '\\r' ) )*
            loop30:
            do {
                int alt30=2;
                int LA30_0 = input.LA(1);

                if ( ((LA30_0>='\u0000' && LA30_0<='\t')||(LA30_0>='\u000B' && LA30_0<='\f')||(LA30_0>='\u000E' && LA30_0<='\uFFFE')) ) {
                    alt30=1;
                }


                switch (alt30) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:319:12: ~ ( '\\n' | '\\r' )
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
            	    break loop30;
                }
            } while (true);

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:319:26: ( '\\r' )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0=='\r') ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:319:26: '\\r'
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
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:8: ( BAR | POUND | TYPEOF | DOTDOT | LARROW | ABSTRACT | AFTER | AND | AS | ASSERT | ATTRIBUTE | BEFORE | BIND | BIBIND | BREAK | BY | CATCH | CLASS | DELETE | DISTINCT | DO | DUR | EASEBOTH | EASEIN | EASEOUT | TIE | STAYS | RETURN | THROW | VAR | PACKAGE | IMPORT | FROM | LATER | TRIGGER | ON | INSERT | INTO | FIRST | LAST | IF | THEN | ELSE | THIS | NULL | TRUE | FALSE | FOR | UNITINTERVAL | IN | FPS | WHILE | CONTINUE | LINEAR | MOTION | TRY | FINALLY | LAZY | FOREACH | WHERE | NOT | NEW | PRIVATE | PROTECTED | PUBLIC | OPERATION | FUNCTION | READONLY | INVERSE | TYPE | EXTENDS | ORDER | INDEX | INSTANCEOF | INDEXOF | SELECT | SUPER | OR | SIZEOF | REVERSE | XOR | LPAREN | RPAREN | LBRACKET | RBRACKET | SEMI | COMMA | DOT | EQEQ | EQ | GT | LT | LTGT | LTEQ | GTEQ | PLUS | PLUSPLUS | SUB | SUBSUB | STAR | SLASH | PERCENT | PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ | LTLT | GTGT | COLON | QUES | STRING_LITERAL | QUOTE_LBRACE_STRING_LITERAL | LBRACE | RBRACE_QUOTE_STRING_LITERAL | RBRACE_LBRACE_STRING_LITERAL | RBRACE | FORMAT_STRING_LITERAL | QUOTED_IDENTIFIER | INTEGER_LITERAL | FLOATING_POINT_LITERAL | IDENTIFIER | WS | COMMENT | LINE_COMMENT )
        int alt32=125;
        alt32 = dfa32.predict(input);
        switch (alt32) {
            case 1 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:10: BAR
                {
                mBAR(); if (failed) return ;

                }
                break;
            case 2 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:14: POUND
                {
                mPOUND(); if (failed) return ;

                }
                break;
            case 3 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:20: TYPEOF
                {
                mTYPEOF(); if (failed) return ;

                }
                break;
            case 4 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:27: DOTDOT
                {
                mDOTDOT(); if (failed) return ;

                }
                break;
            case 5 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:34: LARROW
                {
                mLARROW(); if (failed) return ;

                }
                break;
            case 6 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:41: ABSTRACT
                {
                mABSTRACT(); if (failed) return ;

                }
                break;
            case 7 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:50: AFTER
                {
                mAFTER(); if (failed) return ;

                }
                break;
            case 8 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:56: AND
                {
                mAND(); if (failed) return ;

                }
                break;
            case 9 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:60: AS
                {
                mAS(); if (failed) return ;

                }
                break;
            case 10 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:63: ASSERT
                {
                mASSERT(); if (failed) return ;

                }
                break;
            case 11 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:70: ATTRIBUTE
                {
                mATTRIBUTE(); if (failed) return ;

                }
                break;
            case 12 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:80: BEFORE
                {
                mBEFORE(); if (failed) return ;

                }
                break;
            case 13 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:87: BIND
                {
                mBIND(); if (failed) return ;

                }
                break;
            case 14 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:92: BIBIND
                {
                mBIBIND(); if (failed) return ;

                }
                break;
            case 15 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:99: BREAK
                {
                mBREAK(); if (failed) return ;

                }
                break;
            case 16 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:105: BY
                {
                mBY(); if (failed) return ;

                }
                break;
            case 17 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:108: CATCH
                {
                mCATCH(); if (failed) return ;

                }
                break;
            case 18 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:114: CLASS
                {
                mCLASS(); if (failed) return ;

                }
                break;
            case 19 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:120: DELETE
                {
                mDELETE(); if (failed) return ;

                }
                break;
            case 20 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:127: DISTINCT
                {
                mDISTINCT(); if (failed) return ;

                }
                break;
            case 21 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:136: DO
                {
                mDO(); if (failed) return ;

                }
                break;
            case 22 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:139: DUR
                {
                mDUR(); if (failed) return ;

                }
                break;
            case 23 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:143: EASEBOTH
                {
                mEASEBOTH(); if (failed) return ;

                }
                break;
            case 24 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:152: EASEIN
                {
                mEASEIN(); if (failed) return ;

                }
                break;
            case 25 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:159: EASEOUT
                {
                mEASEOUT(); if (failed) return ;

                }
                break;
            case 26 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:167: TIE
                {
                mTIE(); if (failed) return ;

                }
                break;
            case 27 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:171: STAYS
                {
                mSTAYS(); if (failed) return ;

                }
                break;
            case 28 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:177: RETURN
                {
                mRETURN(); if (failed) return ;

                }
                break;
            case 29 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:184: THROW
                {
                mTHROW(); if (failed) return ;

                }
                break;
            case 30 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:190: VAR
                {
                mVAR(); if (failed) return ;

                }
                break;
            case 31 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:194: PACKAGE
                {
                mPACKAGE(); if (failed) return ;

                }
                break;
            case 32 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:202: IMPORT
                {
                mIMPORT(); if (failed) return ;

                }
                break;
            case 33 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:209: FROM
                {
                mFROM(); if (failed) return ;

                }
                break;
            case 34 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:214: LATER
                {
                mLATER(); if (failed) return ;

                }
                break;
            case 35 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:220: TRIGGER
                {
                mTRIGGER(); if (failed) return ;

                }
                break;
            case 36 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:228: ON
                {
                mON(); if (failed) return ;

                }
                break;
            case 37 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:231: INSERT
                {
                mINSERT(); if (failed) return ;

                }
                break;
            case 38 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:238: INTO
                {
                mINTO(); if (failed) return ;

                }
                break;
            case 39 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:243: FIRST
                {
                mFIRST(); if (failed) return ;

                }
                break;
            case 40 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:249: LAST
                {
                mLAST(); if (failed) return ;

                }
                break;
            case 41 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:254: IF
                {
                mIF(); if (failed) return ;

                }
                break;
            case 42 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:257: THEN
                {
                mTHEN(); if (failed) return ;

                }
                break;
            case 43 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:262: ELSE
                {
                mELSE(); if (failed) return ;

                }
                break;
            case 44 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:267: THIS
                {
                mTHIS(); if (failed) return ;

                }
                break;
            case 45 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:272: NULL
                {
                mNULL(); if (failed) return ;

                }
                break;
            case 46 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:277: TRUE
                {
                mTRUE(); if (failed) return ;

                }
                break;
            case 47 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:282: FALSE
                {
                mFALSE(); if (failed) return ;

                }
                break;
            case 48 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:288: FOR
                {
                mFOR(); if (failed) return ;

                }
                break;
            case 49 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:292: UNITINTERVAL
                {
                mUNITINTERVAL(); if (failed) return ;

                }
                break;
            case 50 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:305: IN
                {
                mIN(); if (failed) return ;

                }
                break;
            case 51 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:308: FPS
                {
                mFPS(); if (failed) return ;

                }
                break;
            case 52 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:312: WHILE
                {
                mWHILE(); if (failed) return ;

                }
                break;
            case 53 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:318: CONTINUE
                {
                mCONTINUE(); if (failed) return ;

                }
                break;
            case 54 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:327: LINEAR
                {
                mLINEAR(); if (failed) return ;

                }
                break;
            case 55 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:334: MOTION
                {
                mMOTION(); if (failed) return ;

                }
                break;
            case 56 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:341: TRY
                {
                mTRY(); if (failed) return ;

                }
                break;
            case 57 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:345: FINALLY
                {
                mFINALLY(); if (failed) return ;

                }
                break;
            case 58 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:353: LAZY
                {
                mLAZY(); if (failed) return ;

                }
                break;
            case 59 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:358: FOREACH
                {
                mFOREACH(); if (failed) return ;

                }
                break;
            case 60 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:366: WHERE
                {
                mWHERE(); if (failed) return ;

                }
                break;
            case 61 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:372: NOT
                {
                mNOT(); if (failed) return ;

                }
                break;
            case 62 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:376: NEW
                {
                mNEW(); if (failed) return ;

                }
                break;
            case 63 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:380: PRIVATE
                {
                mPRIVATE(); if (failed) return ;

                }
                break;
            case 64 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:388: PROTECTED
                {
                mPROTECTED(); if (failed) return ;

                }
                break;
            case 65 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:398: PUBLIC
                {
                mPUBLIC(); if (failed) return ;

                }
                break;
            case 66 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:405: OPERATION
                {
                mOPERATION(); if (failed) return ;

                }
                break;
            case 67 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:415: FUNCTION
                {
                mFUNCTION(); if (failed) return ;

                }
                break;
            case 68 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:424: READONLY
                {
                mREADONLY(); if (failed) return ;

                }
                break;
            case 69 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:433: INVERSE
                {
                mINVERSE(); if (failed) return ;

                }
                break;
            case 70 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:441: TYPE
                {
                mTYPE(); if (failed) return ;

                }
                break;
            case 71 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:446: EXTENDS
                {
                mEXTENDS(); if (failed) return ;

                }
                break;
            case 72 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:454: ORDER
                {
                mORDER(); if (failed) return ;

                }
                break;
            case 73 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:460: INDEX
                {
                mINDEX(); if (failed) return ;

                }
                break;
            case 74 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:466: INSTANCEOF
                {
                mINSTANCEOF(); if (failed) return ;

                }
                break;
            case 75 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:477: INDEXOF
                {
                mINDEXOF(); if (failed) return ;

                }
                break;
            case 76 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:485: SELECT
                {
                mSELECT(); if (failed) return ;

                }
                break;
            case 77 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:492: SUPER
                {
                mSUPER(); if (failed) return ;

                }
                break;
            case 78 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:498: OR
                {
                mOR(); if (failed) return ;

                }
                break;
            case 79 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:501: SIZEOF
                {
                mSIZEOF(); if (failed) return ;

                }
                break;
            case 80 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:508: REVERSE
                {
                mREVERSE(); if (failed) return ;

                }
                break;
            case 81 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:516: XOR
                {
                mXOR(); if (failed) return ;

                }
                break;
            case 82 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:520: LPAREN
                {
                mLPAREN(); if (failed) return ;

                }
                break;
            case 83 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:527: RPAREN
                {
                mRPAREN(); if (failed) return ;

                }
                break;
            case 84 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:534: LBRACKET
                {
                mLBRACKET(); if (failed) return ;

                }
                break;
            case 85 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:543: RBRACKET
                {
                mRBRACKET(); if (failed) return ;

                }
                break;
            case 86 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:552: SEMI
                {
                mSEMI(); if (failed) return ;

                }
                break;
            case 87 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:557: COMMA
                {
                mCOMMA(); if (failed) return ;

                }
                break;
            case 88 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:563: DOT
                {
                mDOT(); if (failed) return ;

                }
                break;
            case 89 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:567: EQEQ
                {
                mEQEQ(); if (failed) return ;

                }
                break;
            case 90 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:572: EQ
                {
                mEQ(); if (failed) return ;

                }
                break;
            case 91 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:575: GT
                {
                mGT(); if (failed) return ;

                }
                break;
            case 92 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:578: LT
                {
                mLT(); if (failed) return ;

                }
                break;
            case 93 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:581: LTGT
                {
                mLTGT(); if (failed) return ;

                }
                break;
            case 94 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:586: LTEQ
                {
                mLTEQ(); if (failed) return ;

                }
                break;
            case 95 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:591: GTEQ
                {
                mGTEQ(); if (failed) return ;

                }
                break;
            case 96 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:596: PLUS
                {
                mPLUS(); if (failed) return ;

                }
                break;
            case 97 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:601: PLUSPLUS
                {
                mPLUSPLUS(); if (failed) return ;

                }
                break;
            case 98 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:610: SUB
                {
                mSUB(); if (failed) return ;

                }
                break;
            case 99 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:614: SUBSUB
                {
                mSUBSUB(); if (failed) return ;

                }
                break;
            case 100 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:621: STAR
                {
                mSTAR(); if (failed) return ;

                }
                break;
            case 101 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:626: SLASH
                {
                mSLASH(); if (failed) return ;

                }
                break;
            case 102 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:632: PERCENT
                {
                mPERCENT(); if (failed) return ;

                }
                break;
            case 103 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:640: PLUSEQ
                {
                mPLUSEQ(); if (failed) return ;

                }
                break;
            case 104 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:647: SUBEQ
                {
                mSUBEQ(); if (failed) return ;

                }
                break;
            case 105 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:653: STAREQ
                {
                mSTAREQ(); if (failed) return ;

                }
                break;
            case 106 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:660: SLASHEQ
                {
                mSLASHEQ(); if (failed) return ;

                }
                break;
            case 107 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:668: PERCENTEQ
                {
                mPERCENTEQ(); if (failed) return ;

                }
                break;
            case 108 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:678: LTLT
                {
                mLTLT(); if (failed) return ;

                }
                break;
            case 109 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:683: GTGT
                {
                mGTGT(); if (failed) return ;

                }
                break;
            case 110 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:688: COLON
                {
                mCOLON(); if (failed) return ;

                }
                break;
            case 111 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:694: QUES
                {
                mQUES(); if (failed) return ;

                }
                break;
            case 112 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:699: STRING_LITERAL
                {
                mSTRING_LITERAL(); if (failed) return ;

                }
                break;
            case 113 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:714: QUOTE_LBRACE_STRING_LITERAL
                {
                mQUOTE_LBRACE_STRING_LITERAL(); if (failed) return ;

                }
                break;
            case 114 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:742: LBRACE
                {
                mLBRACE(); if (failed) return ;

                }
                break;
            case 115 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:749: RBRACE_QUOTE_STRING_LITERAL
                {
                mRBRACE_QUOTE_STRING_LITERAL(); if (failed) return ;

                }
                break;
            case 116 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:777: RBRACE_LBRACE_STRING_LITERAL
                {
                mRBRACE_LBRACE_STRING_LITERAL(); if (failed) return ;

                }
                break;
            case 117 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:806: RBRACE
                {
                mRBRACE(); if (failed) return ;

                }
                break;
            case 118 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:813: FORMAT_STRING_LITERAL
                {
                mFORMAT_STRING_LITERAL(); if (failed) return ;

                }
                break;
            case 119 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:835: QUOTED_IDENTIFIER
                {
                mQUOTED_IDENTIFIER(); if (failed) return ;

                }
                break;
            case 120 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:853: INTEGER_LITERAL
                {
                mINTEGER_LITERAL(); if (failed) return ;

                }
                break;
            case 121 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:869: FLOATING_POINT_LITERAL
                {
                mFLOATING_POINT_LITERAL(); if (failed) return ;

                }
                break;
            case 122 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:892: IDENTIFIER
                {
                mIDENTIFIER(); if (failed) return ;

                }
                break;
            case 123 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:903: WS
                {
                mWS(); if (failed) return ;

                }
                break;
            case 124 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:906: COMMENT
                {
                mCOMMENT(); if (failed) return ;

                }
                break;
            case 125 :
                // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:914: LINE_COMMENT
                {
                mLINE_COMMENT(); if (failed) return ;

                }
                break;

        }

    }

    // $ANTLR start synpred1
    public final void synpred1_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:249:8: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )* '%' )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:249:9: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )* '%'
        {
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:249:9: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )*
        loop33:
        do {
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( ((LA33_0>='\t' && LA33_0<='\n')||(LA33_0>='\f' && LA33_0<='\r')||LA33_0==' ') ) {
                alt33=1;
            }


            switch (alt33) {
        	case 1 :
        	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
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
        	    break loop33;
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
    protected DFA25 dfa25 = new DFA25(this);
    protected DFA32 dfa32 = new DFA32(this);
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
        "\1\0\1\2\1\4\1\1\1\3\2\uffff}>";
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
            return "226:1: RBRACE_QUOTE_STRING_LITERAL : ({...}? => '}' (~ ( '{' | '\"' ) )* '\"' | {...}? => '}' (~ ( '{' | '\\'' ) )* '\\'' );";
        }
        public int specialStateTransition(int s, IntStream input) throws NoViableAltException {
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA9_0 = input.LA(1);

                         
                        int index9_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA9_0=='}') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 1;}

                         
                        input.seek(index9_0);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA9_3 = input.LA(1);

                         
                        int index9_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((LA9_3>='\u0000' && LA9_3<='z')||(LA9_3>='|' && LA9_3<='\uFFFE')) && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 6;}

                        else s = 5;

                         
                        input.seek(index9_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
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
                    case 3 : 
                        int LA9_4 = input.LA(1);

                         
                        int index9_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((LA9_4>='\u0000' && LA9_4<='z')||(LA9_4>='|' && LA9_4<='\uFFFE')) && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 5;}

                        else s = 6;

                         
                        input.seek(index9_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA9_2 = input.LA(1);

                         
                        int index9_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA9_2=='\'') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 3;}

                        else if ( (LA9_2=='\"') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 4;}

                        else if ( ((LA9_2>='\u0000' && LA9_2<='!')||(LA9_2>='#' && LA9_2<='&')||(LA9_2>='(' && LA9_2<='z')||(LA9_2>='|' && LA9_2<='\uFFFE')) && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 2;}

                         
                        input.seek(index9_2);
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
        "\4\uffff\1\2\1\1";
    static final String DFA12_specialS =
        "\1\1\1\3\1\0\1\2\2\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\1",
            "\42\2\1\4\4\2\1\5\123\2\1\3\uff83\2",
            "\42\2\1\4\4\2\1\5\123\2\1\3\uff83\2",
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
            return "235:1: RBRACE_LBRACE_STRING_LITERAL : ({...}? => '}' (~ ( '{' | '\"' ) )* '{' NextIsPercent[CUR_QUOTE_CTX] | {...}? => '}' (~ ( '{' | '\\'' ) )* '{' NextIsPercent[CUR_QUOTE_CTX] );";
        }
        public int specialStateTransition(int s, IntStream input) throws NoViableAltException {
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA12_2 = input.LA(1);

                         
                        int index12_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA12_2=='{') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 3;}

                        else if ( ((LA12_2>='\u0000' && LA12_2<='!')||(LA12_2>='#' && LA12_2<='&')||(LA12_2>='(' && LA12_2<='z')||(LA12_2>='|' && LA12_2<='\uFFFE')) && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 2;}

                        else if ( (LA12_2=='\"') && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 4;}

                        else if ( (LA12_2=='\'') && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 5;}

                         
                        input.seek(index12_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA12_0 = input.LA(1);

                         
                        int index12_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA12_0=='}') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 1;}

                         
                        input.seek(index12_0);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA12_3 = input.LA(1);

                         
                        int index12_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ) ) {s = 5;}

                        else if ( ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) ) ) {s = 4;}

                         
                        input.seek(index12_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA12_1 = input.LA(1);

                         
                        int index12_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((LA12_1>='\u0000' && LA12_1<='!')||(LA12_1>='#' && LA12_1<='&')||(LA12_1>='(' && LA12_1<='z')||(LA12_1>='|' && LA12_1<='\uFFFE')) && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 2;}

                        else if ( (LA12_1=='{') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 3;}

                        else if ( (LA12_1=='\"') && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 4;}

                        else if ( (LA12_1=='\'') && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 5;}

                         
                        input.seek(index12_1);
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
    static final String DFA25_eotS =
        "\5\uffff";
    static final String DFA25_eofS =
        "\5\uffff";
    static final String DFA25_minS =
        "\2\56\3\uffff";
    static final String DFA25_maxS =
        "\1\71\1\145\3\uffff";
    static final String DFA25_acceptS =
        "\2\uffff\1\2\1\1\1\3";
    static final String DFA25_specialS =
        "\5\uffff}>";
    static final String[] DFA25_transitionS = {
            "\1\2\1\uffff\12\1",
            "\1\3\1\uffff\12\1\13\uffff\1\4\37\uffff\1\4",
            "",
            "",
            ""
    };

    static final short[] DFA25_eot = DFA.unpackEncodedString(DFA25_eotS);
    static final short[] DFA25_eof = DFA.unpackEncodedString(DFA25_eofS);
    static final char[] DFA25_min = DFA.unpackEncodedStringToUnsignedChars(DFA25_minS);
    static final char[] DFA25_max = DFA.unpackEncodedStringToUnsignedChars(DFA25_maxS);
    static final short[] DFA25_accept = DFA.unpackEncodedString(DFA25_acceptS);
    static final short[] DFA25_special = DFA.unpackEncodedString(DFA25_specialS);
    static final short[][] DFA25_transition;

    static {
        int numStates = DFA25_transitionS.length;
        DFA25_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA25_transition[i] = DFA.unpackEncodedString(DFA25_transitionS[i]);
        }
    }

    class DFA25 extends DFA {

        public DFA25(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 25;
            this.eot = DFA25_eot;
            this.eof = DFA25_eof;
            this.min = DFA25_min;
            this.max = DFA25_max;
            this.accept = DFA25_accept;
            this.special = DFA25_special;
            this.transition = DFA25_transition;
        }
        public String getDescription() {
            return "262:1: FLOATING_POINT_LITERAL : ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( Exponent )? | '.' ( '0' .. '9' )+ ( Exponent )? | ( '0' .. '9' )+ Exponent );";
        }
    }
    static final String DFA32_eotS =
        "\3\uffff\1\55\1\64\1\72\22\55\6\uffff\1\155\1\160\1\163\1\166\1"+
        "\170\1\174\1\176\5\uffff\1\u0086\2\u0089\2\uffff\4\55\3\uffff\1"+
        "\u0094\4\uffff\1\55\1\u0097\3\55\1\u009b\11\55\1\u00a6\14\55\1\u00ba"+
        "\1\u00bb\11\55\1\u00c9\1\55\1\u00cb\7\55\21\uffff\1\u00d4\7\uffff"+
        "\1\u00d6\2\uffff\1\u00d9\1\uffff\1\u0089\4\55\1\u00e1\1\u00e2\2"+
        "\55\2\uffff\2\55\1\uffff\2\55\1\u00e9\1\uffff\7\55\1\u00f1\2\55"+
        "\1\uffff\12\55\1\u00fe\10\55\2\uffff\1\55\1\u010a\4\55\1\u010f\6"+
        "\55\1\uffff\1\55\1\uffff\1\u0117\1\u0118\5\55\1\u011e\11\uffff\1"+
        "\u0121\1\u0122\1\55\1\u0124\2\uffff\1\55\1\u0126\4\55\1\uffff\1"+
        "\u012b\6\55\1\uffff\4\55\1\u0138\7\55\1\uffff\10\55\1\u0148\2\55"+
        "\1\uffff\3\55\1\u014e\1\uffff\2\55\1\u0151\1\u0152\3\55\2\uffff"+
        "\1\u0156\4\55\2\uffff\1\55\2\uffff\1\u015c\1\uffff\1\55\1\uffff"+
        "\3\55\1\u0161\1\uffff\1\55\1\u0163\1\55\1\u0165\1\u0166\7\55\1\uffff"+
        "\1\u016e\2\55\1\u0171\11\55\1\u017c\1\55\1\uffff\4\55\1\u0182\1"+
        "\uffff\1\u0183\1\u0184\2\uffff\1\55\1\u0186\1\55\1\uffff\1\55\1"+
        "\u0189\1\u018a\1\55\1\u018c\1\uffff\2\55\1\u018f\1\55\1\uffff\1"+
        "\u0191\1\uffff\1\u0192\2\uffff\1\55\1\u0194\4\55\1\u0199\1\uffff"+
        "\1\u019a\1\u019b\1\uffff\1\55\1\u019d\3\55\1\u01a1\2\55\1\u01a4"+
        "\1\55\1\uffff\1\55\1\u01a7\3\55\3\uffff\1\u01ab\1\uffff\2\55\2\uffff"+
        "\1\u01ae\1\uffff\1\u01af\1\55\1\uffff\1\55\2\uffff\1\55\1\uffff"+
        "\1\55\1\u01b4\1\u01b5\1\55\3\uffff\1\55\1\uffff\1\u01b8\1\u01b9"+
        "\1\55\1\uffff\1\u01bb\1\55\1\uffff\1\u01bd\1\u01be\1\uffff\1\u01bf"+
        "\1\55\1\u01c1\1\uffff\2\55\2\uffff\1\u01c4\1\55\1\u01c6\1\u01c7"+
        "\2\uffff\1\u01c8\1\u01c9\2\uffff\1\55\1\uffff\1\55\3\uffff\1\u01cc"+
        "\1\uffff\2\55\1\uffff\1\u01cf\4\uffff\1\u01d0\1\55\1\uffff\1\u01d2"+
        "\1\55\2\uffff\1\u01d4\1\uffff\1\55\1\uffff\1\55\1\u01d7\1\uffff";
    static final String DFA32_eofS =
        "\u01d8\uffff";
    static final String DFA32_minS =
        "\1\11\2\uffff\1\150\1\56\1\55\1\142\1\145\1\141\1\145\1\141\2\145"+
        "\2\141\1\146\2\141\1\156\1\145\1\156\1\150\2\157\6\uffff\2\75\1"+
        "\53\1\55\1\75\1\52\1\0\2\uffff\2\0\1\uffff\1\0\2\56\2\uffff\1\160"+
        "\2\145\1\151\3\uffff\1\0\4\uffff\1\163\1\44\2\164\1\144\1\44\1\142"+
        "\1\145\1\146\1\164\1\141\1\156\1\162\1\154\1\163\1\44\1\164\2\163"+
        "\1\160\1\154\1\172\2\141\1\162\1\151\1\142\1\143\2\44\1\160\1\162"+
        "\2\156\1\157\1\163\1\154\1\163\1\156\1\44\1\145\1\44\1\164\1\167"+
        "\1\154\1\151\1\145\1\164\1\162\21\uffff\2\0\1\uffff\1\0\2\uffff"+
        "\3\0\2\uffff\1\0\1\uffff\1\56\1\145\1\163\1\157\1\156\2\44\1\147"+
        "\1\145\2\uffff\1\164\1\145\1\uffff\1\162\1\145\1\44\1\uffff\1\144"+
        "\1\151\1\141\1\157\1\143\1\163\1\164\1\44\1\145\1\164\1\uffff\6"+
        "\145\1\171\1\144\1\165\1\145\1\44\1\166\1\164\1\154\1\153\3\145"+
        "\1\157\2\uffff\1\157\1\44\1\143\1\141\1\163\1\155\1\44\1\163\1\145"+
        "\1\164\1\171\2\145\1\uffff\1\162\1\uffff\2\44\1\154\1\164\1\162"+
        "\1\154\1\151\1\44\1\0\3\uffff\1\0\2\uffff\1\0\1\uffff\2\44\1\167"+
        "\1\44\2\uffff\1\147\1\44\2\162\1\151\1\162\1\uffff\1\44\1\156\1"+
        "\153\1\162\1\150\1\163\1\151\1\uffff\1\164\1\151\1\156\1\142\1\44"+
        "\1\162\1\143\1\157\1\163\1\157\2\162\1\uffff\1\141\1\145\1\151\2"+
        "\141\1\162\1\170\1\162\1\44\1\162\1\141\1\uffff\1\164\1\154\1\164"+
        "\1\44\1\uffff\1\145\1\162\2\44\1\141\1\162\1\141\2\uffff\1\44\1"+
        "\151\2\145\1\157\2\uffff\1\146\2\uffff\1\44\1\uffff\1\145\1\uffff"+
        "\1\141\1\164\1\142\1\44\1\uffff\1\144\1\44\1\145\2\44\1\156\1\145"+
        "\1\156\1\144\1\165\1\157\1\156\1\uffff\1\44\1\164\1\146\1\44\2\156"+
        "\1\163\1\164\2\143\1\147\1\156\1\164\1\44\1\163\1\uffff\1\164\1"+
        "\143\1\151\1\154\1\44\1\uffff\2\44\2\uffff\1\162\1\44\1\164\1\uffff"+
        "\1\156\2\44\1\156\1\44\1\uffff\1\162\1\143\1\44\1\165\1\uffff\1"+
        "\44\1\uffff\1\44\2\uffff\1\165\1\44\1\143\1\163\2\164\1\44\1\uffff"+
        "\2\44\1\uffff\1\154\1\44\2\145\1\164\1\44\1\145\1\143\1\44\1\146"+
        "\1\uffff\1\145\1\44\1\150\1\157\1\171\3\uffff\1\44\1\uffff\1\151"+
        "\1\164\2\uffff\1\44\1\uffff\1\44\1\164\1\uffff\1\164\2\uffff\1\145"+
        "\1\uffff\1\164\2\44\1\150\3\uffff\1\171\1\uffff\2\44\1\145\1\uffff"+
        "\1\44\1\145\1\uffff\2\44\1\uffff\1\44\1\156\1\44\1\uffff\1\157\1"+
        "\145\2\uffff\1\44\1\145\2\44\2\uffff\2\44\2\uffff\1\144\1\uffff"+
        "\1\157\3\uffff\1\44\1\uffff\1\156\1\162\1\uffff\1\44\4\uffff\1\44"+
        "\1\146\1\uffff\1\44\1\166\2\uffff\1\44\1\uffff\1\141\1\uffff\1\154"+
        "\1\44\1\uffff";
    static final String DFA32_maxS =
        "\1\ufaff\2\uffff\1\171\1\71\1\76\1\164\1\171\1\157\1\165\1\170\1"+
        "\165\1\145\1\141\1\165\1\156\1\165\1\151\1\162\1\165\1\156\1\150"+
        "\2\157\6\uffff\1\75\1\76\4\75\1\ufffe\2\uffff\2\ufffe\1\uffff\1"+
        "\ufffe\2\145\2\uffff\1\160\1\162\1\145\1\171\3\uffff\1\ufffe\4\uffff"+
        "\1\163\1\ufaff\2\164\1\144\1\ufaff\1\156\1\145\1\146\1\164\1\141"+
        "\1\156\1\162\1\154\1\163\1\ufaff\1\164\2\163\1\160\1\154\1\172\1"+
        "\141\1\166\1\162\1\157\1\142\1\143\2\ufaff\1\160\1\162\1\156\1\162"+
        "\1\157\1\163\1\154\1\172\1\156\1\ufaff\1\145\1\ufaff\1\164\1\167"+
        "\1\154\2\151\1\164\1\162\21\uffff\1\ufffe\1\0\1\uffff\1\ufffe\2"+
        "\uffff\3\ufffe\2\uffff\1\ufffe\1\uffff\2\145\1\163\1\157\1\156\2"+
        "\ufaff\1\147\1\145\2\uffff\1\164\1\145\1\uffff\1\162\1\145\1\ufaff"+
        "\1\uffff\1\144\1\151\1\141\1\157\1\143\1\163\1\164\1\ufaff\1\145"+
        "\1\164\1\uffff\6\145\1\171\1\144\1\165\1\145\1\ufaff\1\166\1\164"+
        "\1\154\1\153\1\164\2\145\1\157\2\uffff\1\157\1\ufaff\1\143\1\141"+
        "\1\163\1\155\1\ufaff\1\163\1\145\1\164\1\171\2\145\1\uffff\1\162"+
        "\1\uffff\2\ufaff\1\154\1\164\1\162\1\154\1\151\1\ufaff\1\0\3\uffff"+
        "\1\ufffe\2\uffff\1\ufffe\1\uffff\2\ufaff\1\167\1\ufaff\2\uffff\1"+
        "\147\1\ufaff\2\162\1\151\1\162\1\uffff\1\ufaff\1\156\1\153\1\162"+
        "\1\150\1\163\1\151\1\uffff\1\164\1\151\1\156\1\157\1\ufaff\1\162"+
        "\1\143\1\157\1\163\1\157\2\162\1\uffff\1\141\1\145\1\151\2\141\1"+
        "\162\1\170\1\162\1\ufaff\1\162\1\141\1\uffff\1\164\1\154\1\164\1"+
        "\ufaff\1\uffff\1\145\1\162\2\ufaff\1\141\1\162\1\141\2\uffff\1\ufaff"+
        "\1\151\2\145\1\157\2\uffff\1\146\2\uffff\1\ufaff\1\uffff\1\145\1"+
        "\uffff\1\141\1\164\1\142\1\ufaff\1\uffff\1\144\1\ufaff\1\145\2\ufaff"+
        "\1\156\1\145\1\156\1\144\1\165\1\157\1\156\1\uffff\1\ufaff\1\164"+
        "\1\146\1\ufaff\2\156\1\163\1\164\2\143\1\147\1\156\1\164\1\ufaff"+
        "\1\163\1\uffff\1\164\1\143\1\151\1\154\1\ufaff\1\uffff\2\ufaff\2"+
        "\uffff\1\162\1\ufaff\1\164\1\uffff\1\156\2\ufaff\1\156\1\ufaff\1"+
        "\uffff\1\162\1\143\1\ufaff\1\165\1\uffff\1\ufaff\1\uffff\1\ufaff"+
        "\2\uffff\1\165\1\ufaff\1\143\1\163\2\164\1\ufaff\1\uffff\2\ufaff"+
        "\1\uffff\1\154\1\ufaff\2\145\1\164\1\ufaff\1\145\1\143\1\ufaff\1"+
        "\146\1\uffff\1\145\1\ufaff\1\150\1\157\1\171\3\uffff\1\ufaff\1\uffff"+
        "\1\151\1\164\2\uffff\1\ufaff\1\uffff\1\ufaff\1\164\1\uffff\1\164"+
        "\2\uffff\1\145\1\uffff\1\164\2\ufaff\1\150\3\uffff\1\171\1\uffff"+
        "\2\ufaff\1\145\1\uffff\1\ufaff\1\145\1\uffff\2\ufaff\1\uffff\1\ufaff"+
        "\1\156\1\ufaff\1\uffff\1\157\1\145\2\uffff\1\ufaff\1\145\2\ufaff"+
        "\2\uffff\2\ufaff\2\uffff\1\144\1\uffff\1\157\3\uffff\1\ufaff\1\uffff"+
        "\1\156\1\162\1\uffff\1\ufaff\4\uffff\1\ufaff\1\146\1\uffff\1\ufaff"+
        "\1\166\2\uffff\1\ufaff\1\uffff\1\141\1\uffff\1\154\1\ufaff\1\uffff";
    static final String DFA32_acceptS =
        "\1\uffff\1\1\1\2\25\uffff\1\122\1\123\1\124\1\125\1\126\1\127\7"+
        "\uffff\1\156\1\157\2\uffff\1\162\3\uffff\1\172\1\173\4\uffff\1\4"+
        "\1\130\1\171\1\uffff\1\5\1\135\1\136\1\134\61\uffff\1\131\1\132"+
        "\1\155\1\137\1\133\1\147\1\141\1\140\1\150\1\143\1\142\1\151\1\144"+
        "\1\152\1\174\1\175\1\145\2\uffff\1\166\1\uffff\1\161\1\160\3\uffff"+
        "\1\165\1\164\1\uffff\1\170\11\uffff\1\167\1\154\2\uffff\1\11\3\uffff"+
        "\1\20\12\uffff\1\25\23\uffff\1\62\1\51\15\uffff\1\116\1\uffff\1"+
        "\44\11\uffff\1\146\1\163\1\164\1\uffff\2\163\1\uffff\1\164\4\uffff"+
        "\1\32\1\70\6\uffff\1\10\7\uffff\1\26\14\uffff\1\36\13\uffff\1\60"+
        "\4\uffff\1\63\7\uffff\1\75\1\76\5\uffff\1\121\1\153\1\uffff\1\106"+
        "\1\54\1\uffff\1\52\1\uffff\1\56\4\uffff\1\15\14\uffff\1\53\17\uffff"+
        "\1\46\5\uffff\1\41\2\uffff\1\50\1\72\3\uffff\1\55\5\uffff\1\35\4"+
        "\uffff\1\7\1\uffff\1\17\1\uffff\1\21\1\22\7\uffff\1\115\2\uffff"+
        "\1\33\12\uffff\1\111\5\uffff\1\47\1\57\1\42\1\uffff\1\110\2\uffff"+
        "\1\74\1\64\1\uffff\1\3\2\uffff\1\12\1\uffff\1\16\1\14\1\uffff\1"+
        "\23\4\uffff\1\30\1\114\1\117\1\uffff\1\34\3\uffff\1\101\2\uffff"+
        "\1\45\2\uffff\1\40\3\uffff\1\66\2\uffff\1\67\1\43\4\uffff\1\107"+
        "\1\31\2\uffff\1\120\1\77\1\uffff\1\37\1\uffff\1\113\1\105\1\73\1"+
        "\uffff\1\71\2\uffff\1\6\1\uffff\1\65\1\24\1\27\1\104\2\uffff\1\103"+
        "\2\uffff\1\13\1\100\1\uffff\1\102\1\uffff\1\112\2\uffff\1\61";
    static final String DFA32_specialS =
        "\1\2\43\uffff\1\10\5\uffff\1\1\122\uffff\1\4\1\6\5\uffff\1\11\1"+
        "\0\2\uffff\1\12\113\uffff\1\7\3\uffff\1\3\2\uffff\1\5\u00fc\uffff}>";
    static final String[] DFA32_transitionS = {
            "\2\56\1\uffff\2\56\22\uffff\1\56\1\uffff\1\47\1\2\1\55\1\44"+
            "\1\uffff\1\50\1\30\1\31\1\42\1\40\1\35\1\41\1\4\1\43\1\53\11"+
            "\54\1\45\1\34\1\5\1\36\1\37\1\46\1\uffff\32\55\1\32\1\uffff"+
            "\1\33\1\uffff\1\55\1\uffff\1\6\1\7\1\10\1\11\1\12\1\20\2\55"+
            "\1\17\2\55\1\21\1\26\1\23\1\22\1\16\1\55\1\14\1\13\1\3\1\24"+
            "\1\15\1\25\1\27\2\55\1\51\1\1\1\52\102\uffff\27\55\1\uffff\37"+
            "\55\1\uffff\u1f08\55\u1040\uffff\u0150\55\u0170\uffff\u0080"+
            "\55\u0080\uffff\u092e\55\u10d2\uffff\u5200\55\u5900\uffff\u0200"+
            "\55",
            "",
            "",
            "\1\60\1\61\10\uffff\1\62\6\uffff\1\57",
            "\1\63\1\uffff\12\65",
            "\1\67\16\uffff\1\66\1\71\1\70",
            "\1\73\3\uffff\1\76\7\uffff\1\77\4\uffff\1\74\1\75",
            "\1\103\3\uffff\1\101\10\uffff\1\102\6\uffff\1\100",
            "\1\104\12\uffff\1\105\2\uffff\1\106",
            "\1\110\3\uffff\1\111\5\uffff\1\112\5\uffff\1\107",
            "\1\114\12\uffff\1\115\13\uffff\1\113",
            "\1\117\3\uffff\1\120\12\uffff\1\121\1\116",
            "\1\122",
            "\1\123",
            "\1\126\20\uffff\1\124\2\uffff\1\125",
            "\1\130\6\uffff\1\131\1\127",
            "\1\137\7\uffff\1\134\5\uffff\1\132\1\136\1\uffff\1\135\2\uffff"+
            "\1\133",
            "\1\140\7\uffff\1\141",
            "\1\144\1\uffff\1\143\1\uffff\1\142",
            "\1\146\11\uffff\1\145\5\uffff\1\147",
            "\1\150",
            "\1\151",
            "\1\152",
            "\1\153",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\154",
            "\1\157\1\156",
            "\1\162\21\uffff\1\161",
            "\1\165\17\uffff\1\164",
            "\1\167",
            "\1\172\4\uffff\1\173\15\uffff\1\171",
            "\40\177\1\uffff\34\177\1\175\uffc1\177",
            "",
            "",
            "\42\u0080\1\u0082\130\u0080\1\u0081\uff83\u0080",
            "\47\u0083\1\u0082\123\u0083\1\u0081\uff83\u0083",
            "",
            "\42\u0084\1\u0088\4\u0084\1\u0085\123\u0084\1\u0087\uff83\u0084",
            "\1\65\1\uffff\12\65\13\uffff\1\65\37\uffff\1\65",
            "\1\65\1\uffff\12\u008a\13\uffff\1\65\37\uffff\1\65",
            "",
            "",
            "\1\u008b",
            "\1\u008e\3\uffff\1\u008c\10\uffff\1\u008d",
            "\1\u008f",
            "\1\u0091\13\uffff\1\u0092\3\uffff\1\u0090",
            "",
            "",
            "",
            "\uffff\u0093",
            "",
            "",
            "",
            "",
            "\1\u0095",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\22\55"+
            "\1\u0096\7\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55"+
            "\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e"+
            "\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\u0098",
            "\1\u0099",
            "\1\u009a",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u009d\13\uffff\1\u009c",
            "\1\u009e",
            "\1\u009f",
            "\1\u00a0",
            "\1\u00a1",
            "\1\u00a2",
            "\1\u00a3",
            "\1\u00a4",
            "\1\u00a5",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u00a7",
            "\1\u00a8",
            "\1\u00a9",
            "\1\u00aa",
            "\1\u00ab",
            "\1\u00ac",
            "\1\u00ad",
            "\1\u00ae\22\uffff\1\u00af\1\uffff\1\u00b0",
            "\1\u00b1",
            "\1\u00b2\5\uffff\1\u00b3",
            "\1\u00b4",
            "\1\u00b5",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\3\55"+
            "\1\u00b7\16\55\1\u00b6\1\u00b9\1\55\1\u00b8\4\55\105\uffff\27"+
            "\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff\u0150\55\u0170"+
            "\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff\u5200\55\u5900"+
            "\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u00bc",
            "\1\u00bd",
            "\1\u00be",
            "\1\u00bf\3\uffff\1\u00c0",
            "\1\u00c1",
            "\1\u00c2",
            "\1\u00c3",
            "\1\u00c5\1\u00c4\5\uffff\1\u00c6",
            "\1\u00c7",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\3\55"+
            "\1\u00c8\26\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55"+
            "\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e"+
            "\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\u00ca",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u00cc",
            "\1\u00cd",
            "\1\u00ce",
            "\1\u00cf",
            "\1\u00d0\3\uffff\1\u00d1",
            "\1\u00d2",
            "\1\u00d3",
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
            "\40\177\1\uffff\uffde\177",
            "\1\uffff",
            "",
            "\42\u0080\1\u0082\130\u0080\1\u0081\uff83\u0080",
            "",
            "",
            "\47\u0083\1\u0082\123\u0083\1\u0081\uff83\u0083",
            "\42\u0084\1\u0088\4\u0084\1\u0085\123\u0084\1\u0087\uff83\u0084",
            "\42\u00d8\1\u00d9\130\u00d8\1\u00d7\uff83\u00d8",
            "",
            "",
            "\47\u00db\1\u00da\123\u00db\1\u00dc\uff83\u00db",
            "",
            "\1\65\1\uffff\12\u008a\13\uffff\1\65\37\uffff\1\65",
            "\1\u00dd",
            "\1\u00de",
            "\1\u00df",
            "\1\u00e0",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u00e3",
            "\1\u00e4",
            "",
            "",
            "\1\u00e5",
            "\1\u00e6",
            "",
            "\1\u00e7",
            "\1\u00e8",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u00ea",
            "\1\u00eb",
            "\1\u00ec",
            "\1\u00ed",
            "\1\u00ee",
            "\1\u00ef",
            "\1\u00f0",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u00f2",
            "\1\u00f3",
            "",
            "\1\u00f4",
            "\1\u00f5",
            "\1\u00f6",
            "\1\u00f7",
            "\1\u00f8",
            "\1\u00f9",
            "\1\u00fa",
            "\1\u00fb",
            "\1\u00fc",
            "\1\u00fd",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u00ff",
            "\1\u0100",
            "\1\u0101",
            "\1\u0102",
            "\1\u0104\16\uffff\1\u0103",
            "\1\u0105",
            "\1\u0106",
            "\1\u0107",
            "",
            "",
            "\1\u0108",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\4\55"+
            "\1\u0109\25\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55"+
            "\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e"+
            "\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\u010b",
            "\1\u010c",
            "\1\u010d",
            "\1\u010e",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0110",
            "\1\u0111",
            "\1\u0112",
            "\1\u0113",
            "\1\u0114",
            "\1\u0115",
            "",
            "\1\u0116",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0119",
            "\1\u011a",
            "\1\u011b",
            "\1\u011c",
            "\1\u011d",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\uffff",
            "",
            "",
            "",
            "\42\u00d8\1\u00d9\130\u00d8\1\u00d7\uff83\u00d8",
            "",
            "",
            "\47\u00db\1\u00da\123\u00db\1\u00dc\uff83\u00db",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\16\55"+
            "\1\u0120\13\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55"+
            "\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e"+
            "\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0123",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "",
            "\1\u0125",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0127",
            "\1\u0128",
            "\1\u0129",
            "\1\u012a",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u012c",
            "\1\u012d",
            "\1\u012e",
            "\1\u012f",
            "\1\u0130",
            "\1\u0131",
            "",
            "\1\u0132",
            "\1\u0133",
            "\1\u0134",
            "\1\u0136\6\uffff\1\u0137\5\uffff\1\u0135",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0139",
            "\1\u013a",
            "\1\u013b",
            "\1\u013c",
            "\1\u013d",
            "\1\u013e",
            "\1\u013f",
            "",
            "\1\u0140",
            "\1\u0141",
            "\1\u0142",
            "\1\u0143",
            "\1\u0144",
            "\1\u0145",
            "\1\u0146",
            "\1\u0147",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0149",
            "\1\u014a",
            "",
            "\1\u014b",
            "\1\u014c",
            "\1\u014d",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u014f",
            "\1\u0150",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0153",
            "\1\u0154",
            "\1\u0155",
            "",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0157",
            "\1\u0158",
            "\1\u0159",
            "\1\u015a",
            "",
            "",
            "\1\u015b",
            "",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u015d",
            "",
            "\1\u015e",
            "\1\u015f",
            "\1\u0160",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u0162",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0164",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0167",
            "\1\u0168",
            "\1\u0169",
            "\1\u016a",
            "\1\u016b",
            "\1\u016c",
            "\1\u016d",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u016f",
            "\1\u0170",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0172",
            "\1\u0173",
            "\1\u0174",
            "\1\u0175",
            "\1\u0176",
            "\1\u0177",
            "\1\u0178",
            "\1\u0179",
            "\1\u017a",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\16\55"+
            "\1\u017b\13\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55"+
            "\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e"+
            "\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\u017d",
            "",
            "\1\u017e",
            "\1\u017f",
            "\1\u0180",
            "\1\u0181",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "",
            "\1\u0185",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0187",
            "",
            "\1\u0188",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u018b",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u018d",
            "\1\u018e",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0190",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "",
            "\1\u0193",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0195",
            "\1\u0196",
            "\1\u0197",
            "\1\u0198",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u019c",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u019e",
            "\1\u019f",
            "\1\u01a0",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01a2",
            "\1\u01a3",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01a5",
            "",
            "\1\u01a6",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01a8",
            "\1\u01a9",
            "\1\u01aa",
            "",
            "",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u01ac",
            "\1\u01ad",
            "",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01b0",
            "",
            "\1\u01b1",
            "",
            "",
            "\1\u01b2",
            "",
            "\1\u01b3",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01b6",
            "",
            "",
            "",
            "\1\u01b7",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01ba",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01bc",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01c0",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u01c2",
            "\1\u01c3",
            "",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01c5",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "",
            "\1\u01ca",
            "",
            "\1\u01cb",
            "",
            "",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u01cd",
            "\1\u01ce",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "",
            "",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01d1",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01d3",
            "",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u01d5",
            "",
            "\1\u01d6",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32\55"+
            "\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            ""
    };

    static final short[] DFA32_eot = DFA.unpackEncodedString(DFA32_eotS);
    static final short[] DFA32_eof = DFA.unpackEncodedString(DFA32_eofS);
    static final char[] DFA32_min = DFA.unpackEncodedStringToUnsignedChars(DFA32_minS);
    static final char[] DFA32_max = DFA.unpackEncodedStringToUnsignedChars(DFA32_maxS);
    static final short[] DFA32_accept = DFA.unpackEncodedString(DFA32_acceptS);
    static final short[] DFA32_special = DFA.unpackEncodedString(DFA32_specialS);
    static final short[][] DFA32_transition;

    static {
        int numStates = DFA32_transitionS.length;
        DFA32_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA32_transition[i] = DFA.unpackEncodedString(DFA32_transitionS[i]);
        }
    }

    class DFA32 extends DFA {

        public DFA32(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 32;
            this.eot = DFA32_eot;
            this.eof = DFA32_eof;
            this.min = DFA32_min;
            this.max = DFA32_max;
            this.accept = DFA32_accept;
            this.special = DFA32_special;
            this.transition = DFA32_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( BAR | POUND | TYPEOF | DOTDOT | LARROW | ABSTRACT | AFTER | AND | AS | ASSERT | ATTRIBUTE | BEFORE | BIND | BIBIND | BREAK | BY | CATCH | CLASS | DELETE | DISTINCT | DO | DUR | EASEBOTH | EASEIN | EASEOUT | TIE | STAYS | RETURN | THROW | VAR | PACKAGE | IMPORT | FROM | LATER | TRIGGER | ON | INSERT | INTO | FIRST | LAST | IF | THEN | ELSE | THIS | NULL | TRUE | FALSE | FOR | UNITINTERVAL | IN | FPS | WHILE | CONTINUE | LINEAR | MOTION | TRY | FINALLY | LAZY | FOREACH | WHERE | NOT | NEW | PRIVATE | PROTECTED | PUBLIC | OPERATION | FUNCTION | READONLY | INVERSE | TYPE | EXTENDS | ORDER | INDEX | INSTANCEOF | INDEXOF | SELECT | SUPER | OR | SIZEOF | REVERSE | XOR | LPAREN | RPAREN | LBRACKET | RBRACKET | SEMI | COMMA | DOT | EQEQ | EQ | GT | LT | LTGT | LTEQ | GTEQ | PLUS | PLUSPLUS | SUB | SUBSUB | STAR | SLASH | PERCENT | PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ | LTLT | GTGT | COLON | QUES | STRING_LITERAL | QUOTE_LBRACE_STRING_LITERAL | LBRACE | RBRACE_QUOTE_STRING_LITERAL | RBRACE_LBRACE_STRING_LITERAL | RBRACE | FORMAT_STRING_LITERAL | QUOTED_IDENTIFIER | INTEGER_LITERAL | FLOATING_POINT_LITERAL | IDENTIFIER | WS | COMMENT | LINE_COMMENT );";
        }
        public int specialStateTransition(int s, IntStream input) throws NoViableAltException {
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA32_133 = input.LA(1);

                         
                        int index32_133 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_133=='{') && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 215;}

                        else if ( ((LA32_133>='\u0000' && LA32_133<='!')||(LA32_133>='#' && LA32_133<='z')||(LA32_133>='|' && LA32_133<='\uFFFE')) && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 216;}

                        else if ( (LA32_133=='\"') && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 217;}

                        else s = 214;

                         
                        input.seek(index32_133);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA32_42 = input.LA(1);

                         
                        int index32_42 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((LA32_42>='\u0000' && LA32_42<='!')||(LA32_42>='#' && LA32_42<='&')||(LA32_42>='(' && LA32_42<='z')||(LA32_42>='|' && LA32_42<='\uFFFE')) && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 132;}

                        else if ( (LA32_42=='\'') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 133;}

                        else if ( (LA32_42=='{') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 135;}

                        else if ( (LA32_42=='\"') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 136;}

                        else s = 134;

                         
                        input.seek(index32_42);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA32_0 = input.LA(1);

                         
                        int index32_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_0=='|') ) {s = 1;}

                        else if ( (LA32_0=='#') ) {s = 2;}

                        else if ( (LA32_0=='t') ) {s = 3;}

                        else if ( (LA32_0=='.') ) {s = 4;}

                        else if ( (LA32_0=='<') ) {s = 5;}

                        else if ( (LA32_0=='a') ) {s = 6;}

                        else if ( (LA32_0=='b') ) {s = 7;}

                        else if ( (LA32_0=='c') ) {s = 8;}

                        else if ( (LA32_0=='d') ) {s = 9;}

                        else if ( (LA32_0=='e') ) {s = 10;}

                        else if ( (LA32_0=='s') ) {s = 11;}

                        else if ( (LA32_0=='r') ) {s = 12;}

                        else if ( (LA32_0=='v') ) {s = 13;}

                        else if ( (LA32_0=='p') ) {s = 14;}

                        else if ( (LA32_0=='i') ) {s = 15;}

                        else if ( (LA32_0=='f') ) {s = 16;}

                        else if ( (LA32_0=='l') ) {s = 17;}

                        else if ( (LA32_0=='o') ) {s = 18;}

                        else if ( (LA32_0=='n') ) {s = 19;}

                        else if ( (LA32_0=='u') ) {s = 20;}

                        else if ( (LA32_0=='w') ) {s = 21;}

                        else if ( (LA32_0=='m') ) {s = 22;}

                        else if ( (LA32_0=='x') ) {s = 23;}

                        else if ( (LA32_0=='(') ) {s = 24;}

                        else if ( (LA32_0==')') ) {s = 25;}

                        else if ( (LA32_0=='[') ) {s = 26;}

                        else if ( (LA32_0==']') ) {s = 27;}

                        else if ( (LA32_0==';') ) {s = 28;}

                        else if ( (LA32_0==',') ) {s = 29;}

                        else if ( (LA32_0=='=') ) {s = 30;}

                        else if ( (LA32_0=='>') ) {s = 31;}

                        else if ( (LA32_0=='+') ) {s = 32;}

                        else if ( (LA32_0=='-') ) {s = 33;}

                        else if ( (LA32_0=='*') ) {s = 34;}

                        else if ( (LA32_0=='/') ) {s = 35;}

                        else if ( (LA32_0=='%') ) {s = 36;}

                        else if ( (LA32_0==':') ) {s = 37;}

                        else if ( (LA32_0=='?') ) {s = 38;}

                        else if ( (LA32_0=='\"') ) {s = 39;}

                        else if ( (LA32_0=='\'') ) {s = 40;}

                        else if ( (LA32_0=='{') ) {s = 41;}

                        else if ( (LA32_0=='}') && (( !BraceQuoteTracker.rightBraceLikeQuote(CUR_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 42;}

                        else if ( (LA32_0=='0') ) {s = 43;}

                        else if ( ((LA32_0>='1' && LA32_0<='9')) ) {s = 44;}

                        else if ( (LA32_0=='$'||(LA32_0>='A' && LA32_0<='Z')||LA32_0=='_'||(LA32_0>='g' && LA32_0<='h')||(LA32_0>='j' && LA32_0<='k')||LA32_0=='q'||(LA32_0>='y' && LA32_0<='z')||(LA32_0>='\u00C0' && LA32_0<='\u00D6')||(LA32_0>='\u00D8' && LA32_0<='\u00F6')||(LA32_0>='\u00F8' && LA32_0<='\u1FFF')||(LA32_0>='\u3040' && LA32_0<='\u318F')||(LA32_0>='\u3300' && LA32_0<='\u337F')||(LA32_0>='\u3400' && LA32_0<='\u3D2D')||(LA32_0>='\u4E00' && LA32_0<='\u9FFF')||(LA32_0>='\uF900' && LA32_0<='\uFAFF')) ) {s = 45;}

                        else if ( ((LA32_0>='\t' && LA32_0<='\n')||(LA32_0>='\f' && LA32_0<='\r')||LA32_0==' ') ) {s = 46;}

                         
                        input.seek(index32_0);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA32_216 = input.LA(1);

                         
                        int index32_216 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_216=='{') && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 215;}

                        else if ( ((LA32_216>='\u0000' && LA32_216<='!')||(LA32_216>='#' && LA32_216<='z')||(LA32_216>='|' && LA32_216<='\uFFFE')) && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 216;}

                        else if ( (LA32_216=='\"') && ( BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) )) {s = 217;}

                         
                        input.seek(index32_216);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA32_125 = input.LA(1);

                         
                        int index32_125 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((LA32_125>='\u0000' && LA32_125<='\u001F')||(LA32_125>='!' && LA32_125<='\uFFFE')) && ( BraceQuoteTracker.percentIsFormat() )) {s = 127;}

                        else s = 212;

                         
                        input.seek(index32_125);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA32_219 = input.LA(1);

                         
                        int index32_219 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_219=='\'') && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 218;}

                        else if ( ((LA32_219>='\u0000' && LA32_219<='&')||(LA32_219>='(' && LA32_219<='z')||(LA32_219>='|' && LA32_219<='\uFFFE')) && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 219;}

                        else if ( (LA32_219=='{') && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 220;}

                         
                        input.seek(index32_219);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA32_126 = input.LA(1);

                         
                        int index32_126 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (!( BraceQuoteTracker.percentIsFormat() )) ) {s = 213;}

                        else if ( ( BraceQuoteTracker.percentIsFormat() ) ) {s = 127;}

                         
                        input.seek(index32_126);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA32_212 = input.LA(1);

                         
                        int index32_212 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (!( BraceQuoteTracker.percentIsFormat() )) ) {s = 287;}

                        else if ( ( BraceQuoteTracker.percentIsFormat() ) ) {s = 127;}

                         
                        input.seek(index32_212);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA32_36 = input.LA(1);

                         
                        int index32_36 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_36=='=') ) {s = 125;}

                        else if ( ((LA32_36>='\u0000' && LA32_36<='\u001F')||(LA32_36>='!' && LA32_36<='<')||(LA32_36>='>' && LA32_36<='\uFFFE')) && ( BraceQuoteTracker.percentIsFormat() )) {s = 127;}

                        else s = 126;

                         
                        input.seek(index32_36);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA32_132 = input.LA(1);

                         
                        int index32_132 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_132=='{') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 135;}

                        else if ( (LA32_132=='\'') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 133;}

                        else if ( (LA32_132=='\"') && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 136;}

                        else if ( ((LA32_132>='\u0000' && LA32_132<='!')||(LA32_132>='#' && LA32_132<='&')||(LA32_132>='(' && LA32_132<='z')||(LA32_132>='|' && LA32_132<='\uFFFE')) && (( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) || BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) ))) {s = 132;}

                         
                        input.seek(index32_132);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA32_136 = input.LA(1);

                         
                        int index32_136 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_136=='\'') && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 218;}

                        else if ( ((LA32_136>='\u0000' && LA32_136<='&')||(LA32_136>='(' && LA32_136<='z')||(LA32_136>='|' && LA32_136<='\uFFFE')) && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 219;}

                        else if ( (LA32_136=='{') && ( BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) )) {s = 220;}

                        else s = 217;

                         
                        input.seek(index32_136);
                        if ( s>=0 ) return s;
                        break;
            }
            if (backtracking>0) {failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 32, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}