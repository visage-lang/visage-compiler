// $ANTLR 3.0 C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g 2007-07-27 20:53:06

package com.sun.tools.javafx.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class v1Lexer extends Lexer {
    public static final int FUNCTION=70;
    public static final int PACKAGE=34;
    public static final int LT=97;
    public static final int STAR=105;
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
    public static final int GTGT=114;
    public static final int RBRACE_QUOTE_STRING_LITERAL=119;
    public static final int BREAK=18;
    public static final int TYPE=73;
    public static final int LBRACKET=89;
    public static final int RPAREN=86;
    public static final int LINEAR=57;
    public static final int IMPORT=35;
    public static final int STRING_LITERAL=117;
    public static final int INSERT=40;
    public static final int FLOATING_POINT_LITERAL=125;
    public static final int SUBSUB=104;
    public static final int BIND=16;
    public static final int STAREQ=110;
    public static final int THIS=47;
    public static final int RETURN=31;
    public static final int VAR=33;
    public static final int SUPER=80;
    public static final int LAST=43;
    public static final int EQ=95;
    public static final int COMMENT=130;
    public static final int SELECT=79;
    public static final int INTO=41;
    public static final int QUES=116;
    public static final int EQEQ=94;
    public static final int MOTION=58;
    public static final int RBRACE=88;
    public static final int POUND=5;
    public static final int LINE_COMMENT=131;
    public static final int PRIVATE=66;
    public static final int NULL=48;
    public static final int ELSE=46;
    public static final int ON=39;
    public static final int DELETE=22;
    public static final int SLASHEQ=111;
    public static final int EASEBOTH=26;
    public static final int ASSERT=13;
    public static final int TRY=59;
    public static final int INVERSE=72;
    public static final int WS=129;
    public static final int TYPEOF=6;
    public static final int INTEGER_LITERAL=123;
    public static final int OR=81;
    public static final int JavaIDDigit=127;
    public static final int SIZEOF=82;
    public static final int GT=96;
    public static final int FOREACH=62;
    public static final int FROM=36;
    public static final int CATCH=20;
    public static final int OPERATION=69;
    public static final int REVERSE=83;
    public static final int FALSE=50;
    public static final int DISTINCT=23;
    public static final int Letter=126;
    public static final int THROW=32;
    public static final int DUR=25;
    public static final int WHERE=63;
    public static final int PROTECTED=67;
    public static final int CLASS=21;
    public static final int ORDER=75;
    public static final int PLUSPLUS=102;
    public static final int LBRACE=87;
    public static final int LTEQ=99;
    public static final int ATTRIBUTE=14;
    public static final int SUBEQ=109;
    public static final int BIBIND=17;
    public static final int Exponent=124;
    public static final int LARROW=8;
    public static final int FOR=51;
    public static final int SUB=103;
    public static final int DOTDOT=7;
    public static final int ABSTRACT=9;
    public static final int AND=11;
    public static final int PLUSEQ=108;
    public static final int LPAREN=85;
    public static final int IF=44;
    public static final int INDEX=76;
    public static final int AS=12;
    public static final int SLASH=106;
    public static final int IN=53;
    public static final int THEN=45;
    public static final int CONTINUE=56;
    public static final int COMMA=92;
    public static final int TIE=29;
    public static final int IDENTIFIER=128;
    public static final int QUOTE_LBRACE_STRING_LITERAL=118;
    public static final int PLUS=101;
    public static final int RBRACKET=90;
    public static final int DOT=93;
    public static final int RBRACE_LBRACE_STRING_LITERAL=120;
    public static final int LTLT=113;
    public static final int STAYS=30;
    public static final int BY=19;
    public static final int XOR=84;
    public static final int PERCENT=107;
    public static final int LAZY=61;
    public static final int LTGT=98;
    public static final int BEFORE=15;
    public static final int INSTANCEOF=77;
    public static final int AFTER=10;
    public static final int GTEQ=100;
    public static final int Tokens=132;
    public static final int READONLY=71;
    public static final int SEMI=91;
    public static final int TRUE=49;
    public static final int COLON=115;
    public static final int FINALLY=60;
    public static final int PERCENTEQ=112;
    public static final int EASEIN=27;
    public static final int FORMAT_STRING_LITERAL=121;
    public static final int QUOTED_IDENTIFIER=122;
    public static final int FPS=54;
    public static final int EXTENDS=74;
    public static final int PUBLIC=68;
    public static final int BAR=4;
    public static final int FIRST=42;
    public v1Lexer() {;} 
    public v1Lexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g"; }

    // $ANTLR start BAR
    public final void mBAR() throws RecognitionException {
        try {
            int _type = BAR;
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:6:5: ( '|' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:6:7: '|'
            {
            match('|'); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:7:7: ( '#' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:7:9: '#'
            {
            match('#'); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:8:8: ( 'typeof' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:8:10: 'typeof'
            {
            match("typeof"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:9:8: ( '..' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:9:10: '..'
            {
            match(".."); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:10:8: ( '<-' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:10:10: '<-'
            {
            match("<-"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:11:10: ( 'abstract' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:11:12: 'abstract'
            {
            match("abstract"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:12:7: ( 'after' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:12:9: 'after'
            {
            match("after"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:13:5: ( 'and' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:13:7: 'and'
            {
            match("and"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:14:4: ( 'as' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:14:6: 'as'
            {
            match("as"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:15:8: ( 'assert' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:15:10: 'assert'
            {
            match("assert"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:16:11: ( 'attribute' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:16:13: 'attribute'
            {
            match("attribute"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:17:8: ( 'before' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:17:10: 'before'
            {
            match("before"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:18:6: ( 'bind' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:18:8: 'bind'
            {
            match("bind"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:19:8: ( 'bibind' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:19:10: 'bibind'
            {
            match("bibind"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:20:7: ( 'break' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:20:9: 'break'
            {
            match("break"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:21:4: ( 'by' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:21:6: 'by'
            {
            match("by"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:22:7: ( 'catch' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:22:9: 'catch'
            {
            match("catch"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:23:7: ( 'class' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:23:9: 'class'
            {
            match("class"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:24:8: ( 'delete' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:24:10: 'delete'
            {
            match("delete"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:25:10: ( 'distinct' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:25:12: 'distinct'
            {
            match("distinct"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:26:4: ( 'do' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:26:6: 'do'
            {
            match("do"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:27:5: ( 'dur' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:27:7: 'dur'
            {
            match("dur"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:28:10: ( 'easeboth' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:28:12: 'easeboth'
            {
            match("easeboth"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:29:8: ( 'easein' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:29:10: 'easein'
            {
            match("easein"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:30:9: ( 'easeout' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:30:11: 'easeout'
            {
            match("easeout"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:31:5: ( 'tie' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:31:7: 'tie'
            {
            match("tie"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:32:7: ( 'stays' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:32:9: 'stays'
            {
            match("stays"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:33:8: ( 'return' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:33:10: 'return'
            {
            match("return"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:34:7: ( 'throw' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:34:9: 'throw'
            {
            match("throw"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:35:5: ( 'var' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:35:7: 'var'
            {
            match("var"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:36:9: ( 'package' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:36:11: 'package'
            {
            match("package"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:37:8: ( 'import' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:37:10: 'import'
            {
            match("import"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:38:6: ( 'from' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:38:8: 'from'
            {
            match("from"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:39:7: ( 'later' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:39:9: 'later'
            {
            match("later"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:40:9: ( 'trigger' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:40:11: 'trigger'
            {
            match("trigger"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:41:4: ( 'on' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:41:6: 'on'
            {
            match("on"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:42:8: ( 'insert' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:42:10: 'insert'
            {
            match("insert"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:43:6: ( 'into' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:43:8: 'into'
            {
            match("into"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:44:7: ( 'first' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:44:9: 'first'
            {
            match("first"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:45:6: ( 'last' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:45:8: 'last'
            {
            match("last"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:46:4: ( 'if' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:46:6: 'if'
            {
            match("if"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:47:6: ( 'then' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:47:8: 'then'
            {
            match("then"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:48:6: ( 'else' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:48:8: 'else'
            {
            match("else"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:49:6: ( 'this' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:49:8: 'this'
            {
            match("this"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:50:6: ( 'null' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:50:8: 'null'
            {
            match("null"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:51:6: ( 'true' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:51:8: 'true'
            {
            match("true"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:52:7: ( 'false' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:52:9: 'false'
            {
            match("false"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:53:5: ( 'for' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:53:7: 'for'
            {
            match("for"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:54:14: ( 'unitinterval' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:54:16: 'unitinterval'
            {
            match("unitinterval"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:55:4: ( 'in' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:55:6: 'in'
            {
            match("in"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:56:5: ( 'fps' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:56:7: 'fps'
            {
            match("fps"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:57:7: ( 'while' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:57:9: 'while'
            {
            match("while"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:58:10: ( 'continue' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:58:12: 'continue'
            {
            match("continue"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:59:8: ( 'linear' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:59:10: 'linear'
            {
            match("linear"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:60:8: ( 'motion' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:60:10: 'motion'
            {
            match("motion"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:61:5: ( 'try' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:61:7: 'try'
            {
            match("try"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:62:9: ( 'finally' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:62:11: 'finally'
            {
            match("finally"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:63:6: ( 'lazy' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:63:8: 'lazy'
            {
            match("lazy"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:64:9: ( 'foreach' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:64:11: 'foreach'
            {
            match("foreach"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:65:7: ( 'where' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:65:9: 'where'
            {
            match("where"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:66:5: ( 'not' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:66:7: 'not'
            {
            match("not"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:67:5: ( 'new' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:67:7: 'new'
            {
            match("new"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:68:9: ( 'private' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:68:11: 'private'
            {
            match("private"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:69:11: ( 'protected' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:69:13: 'protected'
            {
            match("protected"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:70:8: ( 'public' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:70:10: 'public'
            {
            match("public"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:71:11: ( 'operation' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:71:13: 'operation'
            {
            match("operation"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:72:10: ( 'function' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:72:12: 'function'
            {
            match("function"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:73:10: ( 'readonly' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:73:12: 'readonly'
            {
            match("readonly"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:74:9: ( 'inverse' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:74:11: 'inverse'
            {
            match("inverse"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:75:6: ( 'type' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:75:8: 'type'
            {
            match("type"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:76:9: ( 'extends' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:76:11: 'extends'
            {
            match("extends"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:77:7: ( 'order' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:77:9: 'order'
            {
            match("order"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:78:7: ( 'index' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:78:9: 'index'
            {
            match("index"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:79:12: ( 'instanceof' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:79:14: 'instanceof'
            {
            match("instanceof"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:80:9: ( 'indexof' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:80:11: 'indexof'
            {
            match("indexof"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:81:8: ( 'select' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:81:10: 'select'
            {
            match("select"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:82:7: ( 'super' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:82:9: 'super'
            {
            match("super"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:83:4: ( 'or' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:83:6: 'or'
            {
            match("or"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:84:8: ( 'sizeof' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:84:10: 'sizeof'
            {
            match("sizeof"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:85:9: ( 'reverse' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:85:11: 'reverse'
            {
            match("reverse"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:86:5: ( 'xor' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:86:7: 'xor'
            {
            match("xor"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:87:8: ( '(' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:87:10: '('
            {
            match('('); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:88:8: ( ')' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:88:10: ')'
            {
            match(')'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RPAREN

    // $ANTLR start LBRACE
    public final void mLBRACE() throws RecognitionException {
        try {
            int _type = LBRACE;
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:89:8: ( '{' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:89:10: '{'
            {
            match('{'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LBRACE

    // $ANTLR start RBRACE
    public final void mRBRACE() throws RecognitionException {
        try {
            int _type = RBRACE;
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:90:8: ( '}' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:90:10: '}'
            {
            match('}'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RBRACE

    // $ANTLR start LBRACKET
    public final void mLBRACKET() throws RecognitionException {
        try {
            int _type = LBRACKET;
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:91:10: ( '[' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:91:12: '['
            {
            match('['); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:92:10: ( ']' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:92:12: ']'
            {
            match(']'); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:93:6: ( ';' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:93:8: ';'
            {
            match(';'); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:94:7: ( ',' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:94:9: ','
            {
            match(','); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:95:5: ( '.' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:95:7: '.'
            {
            match('.'); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:96:6: ( '==' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:96:8: '=='
            {
            match("=="); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:97:4: ( '=' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:97:6: '='
            {
            match('='); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:98:4: ( '>' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:98:6: '>'
            {
            match('>'); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:99:4: ( '<' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:99:6: '<'
            {
            match('<'); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:100:6: ( '<>' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:100:8: '<>'
            {
            match("<>"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:101:6: ( '<=' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:101:8: '<='
            {
            match("<="); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:102:6: ( '>=' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:102:8: '>='
            {
            match(">="); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:103:6: ( '+' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:103:8: '+'
            {
            match('+'); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:104:10: ( '++' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:104:12: '++'
            {
            match("++"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:105:5: ( '-' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:105:7: '-'
            {
            match('-'); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:106:8: ( '--' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:106:10: '--'
            {
            match("--"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:107:6: ( '*' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:107:8: '*'
            {
            match('*'); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:108:7: ( '/' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:108:9: '/'
            {
            match('/'); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:109:9: ( '%' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:109:11: '%'
            {
            match('%'); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:110:8: ( '+=' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:110:10: '+='
            {
            match("+="); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:111:7: ( '-=' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:111:9: '-='
            {
            match("-="); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:112:8: ( '*=' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:112:10: '*='
            {
            match("*="); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:113:9: ( '/=' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:113:11: '/='
            {
            match("/="); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:114:11: ( '%=' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:114:13: '%='
            {
            match("%="); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:115:6: ( '<<' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:115:8: '<<'
            {
            match("<<"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:116:6: ( '>>' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:116:8: '>>'
            {
            match(">>"); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:117:7: ( ':' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:117:9: ':'
            {
            match(':'); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:118:6: ( '?' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:118:8: '?'
            {
            match('?'); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:161:17: ( '\"' ( . )* '\"' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:161:19: '\"' ( . )* '\"'
            {
            match('\"'); 
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:161:23: ( . )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='\"') ) {
                    alt1=2;
                }
                else if ( ((LA1_0>='\u0000' && LA1_0<='!')||(LA1_0>='#' && LA1_0<='\uFFFE')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:161:23: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match('\"'); 
             setText(getText().substring(1, getText().length()-1)); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:164:29: ( '\"{' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:164:31: '\"{'
            {
            match("\"{"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end QUOTE_LBRACE_STRING_LITERAL

    // $ANTLR start RBRACE_QUOTE_STRING_LITERAL
    public final void mRBRACE_QUOTE_STRING_LITERAL() throws RecognitionException {
        try {
            int _type = RBRACE_QUOTE_STRING_LITERAL;
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:165:29: ( '}\"' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:165:31: '}\"'
            {
            match("}\""); 


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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:166:30: ( '}{' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:166:32: '}{'
            {
            match("}{"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RBRACE_LBRACE_STRING_LITERAL

    // $ANTLR start FORMAT_STRING_LITERAL
    public final void mFORMAT_STRING_LITERAL() throws RecognitionException {
        try {
            int _type = FORMAT_STRING_LITERAL;
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:167:23: ( '%' (~ ' ' )* )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:167:25: '%' (~ ' ' )*
            {
            match('%'); 
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:167:29: (~ ' ' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='\u0000' && LA2_0<='\u001F')||(LA2_0>='!' && LA2_0<='\uFFFE')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:167:30: ~ ' '
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\u001F')||(input.LA(1)>='!' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:170:2: ( '<<' ( . )* '>>' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:170:4: '<<' ( . )* '>>'
            {
            match("<<"); 

            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:170:9: ( . )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='>') ) {
                    int LA3_1 = input.LA(2);

                    if ( (LA3_1=='>') ) {
                        alt3=2;
                    }
                    else if ( ((LA3_1>='\u0000' && LA3_1<='=')||(LA3_1>='?' && LA3_1<='\uFFFE')) ) {
                        alt3=1;
                    }


                }
                else if ( ((LA3_0>='\u0000' && LA3_0<='=')||(LA3_0>='?' && LA3_0<='\uFFFE')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:170:9: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            match(">>"); 

             setText(getText().substring(2, getText().length()-2)); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:172:17: ( ( '0' | '1' .. '9' ( '0' .. '9' )* ) )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:172:19: ( '0' | '1' .. '9' ( '0' .. '9' )* )
            {
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:172:19: ( '0' | '1' .. '9' ( '0' .. '9' )* )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='0') ) {
                alt5=1;
            }
            else if ( ((LA5_0>='1' && LA5_0<='9')) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("172:19: ( '0' | '1' .. '9' ( '0' .. '9' )* )", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:172:20: '0'
                    {
                    match('0'); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:172:26: '1' .. '9' ( '0' .. '9' )*
                    {
                    matchRange('1','9'); 
                    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:172:35: ( '0' .. '9' )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:172:35: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop4;
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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:175:5: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( Exponent )? | '.' ( '0' .. '9' )+ ( Exponent )? | ( '0' .. '9' )+ Exponent )
            int alt12=3;
            alt12 = dfa12.predict(input);
            switch (alt12) {
                case 1 :
                    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:175:9: ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( Exponent )?
                    {
                    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:175:9: ( '0' .. '9' )+
                    int cnt6=0;
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:175:10: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt6 >= 1 ) break loop6;
                                EarlyExitException eee =
                                    new EarlyExitException(6, input);
                                throw eee;
                        }
                        cnt6++;
                    } while (true);

                    match('.'); 
                    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:175:25: ( '0' .. '9' )*
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0>='0' && LA7_0<='9')) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:175:26: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop7;
                        }
                    } while (true);

                    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:175:37: ( Exponent )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0=='E'||LA8_0=='e') ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:175:37: Exponent
                            {
                            mExponent(); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:176:9: '.' ( '0' .. '9' )+ ( Exponent )?
                    {
                    match('.'); 
                    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:176:13: ( '0' .. '9' )+
                    int cnt9=0;
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( ((LA9_0>='0' && LA9_0<='9')) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:176:14: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt9 >= 1 ) break loop9;
                                EarlyExitException eee =
                                    new EarlyExitException(9, input);
                                throw eee;
                        }
                        cnt9++;
                    } while (true);

                    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:176:25: ( Exponent )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0=='E'||LA10_0=='e') ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:176:25: Exponent
                            {
                            mExponent(); 

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:177:9: ( '0' .. '9' )+ Exponent
                    {
                    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:177:9: ( '0' .. '9' )+
                    int cnt11=0;
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( ((LA11_0>='0' && LA11_0<='9')) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:177:10: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt11 >= 1 ) break loop11;
                                EarlyExitException eee =
                                    new EarlyExitException(11, input);
                                throw eee;
                        }
                        cnt11++;
                    } while (true);

                    mExponent(); 

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:181:10: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:181:12: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:181:22: ( '+' | '-' )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0=='+'||LA13_0=='-') ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse =
                            new MismatchedSetException(null,input);
                        recover(mse);    throw mse;
                    }


                    }
                    break;

            }

            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:181:33: ( '0' .. '9' )+
            int cnt14=0;
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( ((LA14_0>='0' && LA14_0<='9')) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:181:34: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt14 >= 1 ) break loop14;
                        EarlyExitException eee =
                            new EarlyExitException(14, input);
                        throw eee;
                }
                cnt14++;
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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:184:5: ( Letter ( Letter | JavaIDDigit )* )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:184:9: Letter ( Letter | JavaIDDigit )*
            {
            mLetter(); 
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:184:16: ( Letter | JavaIDDigit )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0=='$'||(LA15_0>='0' && LA15_0<='9')||(LA15_0>='A' && LA15_0<='Z')||LA15_0=='_'||(LA15_0>='a' && LA15_0<='z')||(LA15_0>='\u00C0' && LA15_0<='\u00D6')||(LA15_0>='\u00D8' && LA15_0<='\u00F6')||(LA15_0>='\u00F8' && LA15_0<='\u1FFF')||(LA15_0>='\u3040' && LA15_0<='\u318F')||(LA15_0>='\u3300' && LA15_0<='\u337F')||(LA15_0>='\u3400' && LA15_0<='\u3D2D')||(LA15_0>='\u4E00' && LA15_0<='\u9FFF')||(LA15_0>='\uF900' && LA15_0<='\uFAFF')) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
            	    {
            	    if ( input.LA(1)=='$'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u1FFF')||(input.LA(1)>='\u3040' && input.LA(1)<='\u318F')||(input.LA(1)>='\u3300' && input.LA(1)<='\u337F')||(input.LA(1)>='\u3400' && input.LA(1)<='\u3D2D')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FFF')||(input.LA(1)>='\uF900' && input.LA(1)<='\uFAFF') ) {
            	        input.consume();

            	    }
            	    else {
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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:189:5: ( '\\u0024' | '\\u0041' .. '\\u005a' | '\\u005f' | '\\u0061' .. '\\u007a' | '\\u00c0' .. '\\u00d6' | '\\u00d8' .. '\\u00f6' | '\\u00f8' .. '\\u00ff' | '\\u0100' .. '\\u1fff' | '\\u3040' .. '\\u318f' | '\\u3300' .. '\\u337f' | '\\u3400' .. '\\u3d2d' | '\\u4e00' .. '\\u9fff' | '\\uf900' .. '\\ufaff' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
            {
            if ( input.LA(1)=='$'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u1FFF')||(input.LA(1)>='\u3040' && input.LA(1)<='\u318F')||(input.LA(1)>='\u3300' && input.LA(1)<='\u337F')||(input.LA(1)>='\u3400' && input.LA(1)<='\u3D2D')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FFF')||(input.LA(1)>='\uF900' && input.LA(1)<='\uFAFF') ) {
                input.consume();

            }
            else {
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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:206:5: ( '\\u0030' .. '\\u0039' | '\\u0660' .. '\\u0669' | '\\u06f0' .. '\\u06f9' | '\\u0966' .. '\\u096f' | '\\u09e6' .. '\\u09ef' | '\\u0a66' .. '\\u0a6f' | '\\u0ae6' .. '\\u0aef' | '\\u0b66' .. '\\u0b6f' | '\\u0be7' .. '\\u0bef' | '\\u0c66' .. '\\u0c6f' | '\\u0ce6' .. '\\u0cef' | '\\u0d66' .. '\\u0d6f' | '\\u0e50' .. '\\u0e59' | '\\u0ed0' .. '\\u0ed9' | '\\u1040' .. '\\u1049' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='\u0660' && input.LA(1)<='\u0669')||(input.LA(1)>='\u06F0' && input.LA(1)<='\u06F9')||(input.LA(1)>='\u0966' && input.LA(1)<='\u096F')||(input.LA(1)>='\u09E6' && input.LA(1)<='\u09EF')||(input.LA(1)>='\u0A66' && input.LA(1)<='\u0A6F')||(input.LA(1)>='\u0AE6' && input.LA(1)<='\u0AEF')||(input.LA(1)>='\u0B66' && input.LA(1)<='\u0B6F')||(input.LA(1)>='\u0BE7' && input.LA(1)<='\u0BEF')||(input.LA(1)>='\u0C66' && input.LA(1)<='\u0C6F')||(input.LA(1)>='\u0CE6' && input.LA(1)<='\u0CEF')||(input.LA(1)>='\u0D66' && input.LA(1)<='\u0D6F')||(input.LA(1)>='\u0E50' && input.LA(1)<='\u0E59')||(input.LA(1)>='\u0ED0' && input.LA(1)<='\u0ED9')||(input.LA(1)>='\u1040' && input.LA(1)<='\u1049') ) {
                input.consume();

            }
            else {
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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:223:5: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' ) )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:223:8: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            channel=HIDDEN;

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:227:5: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:227:9: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:227:14: ( options {greedy=false; } : . )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0=='*') ) {
                    int LA16_1 = input.LA(2);

                    if ( (LA16_1=='/') ) {
                        alt16=2;
                    }
                    else if ( ((LA16_1>='\u0000' && LA16_1<='.')||(LA16_1>='0' && LA16_1<='\uFFFE')) ) {
                        alt16=1;
                    }


                }
                else if ( ((LA16_0>='\u0000' && LA16_0<=')')||(LA16_0>='+' && LA16_0<='\uFFFE')) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:227:42: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

            match("*/"); 

            channel=HIDDEN;

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
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:231:5: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:231:7: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
            {
            match("//"); 

            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:231:12: (~ ( '\\n' | '\\r' ) )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( ((LA17_0>='\u0000' && LA17_0<='\t')||(LA17_0>='\u000B' && LA17_0<='\f')||(LA17_0>='\u000E' && LA17_0<='\uFFFE')) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:231:12: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

            // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:231:26: ( '\\r' )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0=='\r') ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:231:26: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 
            channel=HIDDEN;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LINE_COMMENT

    public void mTokens() throws RecognitionException {
        // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:8: ( BAR | POUND | TYPEOF | DOTDOT | LARROW | ABSTRACT | AFTER | AND | AS | ASSERT | ATTRIBUTE | BEFORE | BIND | BIBIND | BREAK | BY | CATCH | CLASS | DELETE | DISTINCT | DO | DUR | EASEBOTH | EASEIN | EASEOUT | TIE | STAYS | RETURN | THROW | VAR | PACKAGE | IMPORT | FROM | LATER | TRIGGER | ON | INSERT | INTO | FIRST | LAST | IF | THEN | ELSE | THIS | NULL | TRUE | FALSE | FOR | UNITINTERVAL | IN | FPS | WHILE | CONTINUE | LINEAR | MOTION | TRY | FINALLY | LAZY | FOREACH | WHERE | NOT | NEW | PRIVATE | PROTECTED | PUBLIC | OPERATION | FUNCTION | READONLY | INVERSE | TYPE | EXTENDS | ORDER | INDEX | INSTANCEOF | INDEXOF | SELECT | SUPER | OR | SIZEOF | REVERSE | XOR | LPAREN | RPAREN | LBRACE | RBRACE | LBRACKET | RBRACKET | SEMI | COMMA | DOT | EQEQ | EQ | GT | LT | LTGT | LTEQ | GTEQ | PLUS | PLUSPLUS | SUB | SUBSUB | STAR | SLASH | PERCENT | PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ | LTLT | GTGT | COLON | QUES | STRING_LITERAL | QUOTE_LBRACE_STRING_LITERAL | RBRACE_QUOTE_STRING_LITERAL | RBRACE_LBRACE_STRING_LITERAL | FORMAT_STRING_LITERAL | QUOTED_IDENTIFIER | INTEGER_LITERAL | FLOATING_POINT_LITERAL | IDENTIFIER | WS | COMMENT | LINE_COMMENT )
        int alt19=125;
        alt19 = dfa19.predict(input);
        switch (alt19) {
            case 1 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:10: BAR
                {
                mBAR(); 

                }
                break;
            case 2 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:14: POUND
                {
                mPOUND(); 

                }
                break;
            case 3 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:20: TYPEOF
                {
                mTYPEOF(); 

                }
                break;
            case 4 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:27: DOTDOT
                {
                mDOTDOT(); 

                }
                break;
            case 5 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:34: LARROW
                {
                mLARROW(); 

                }
                break;
            case 6 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:41: ABSTRACT
                {
                mABSTRACT(); 

                }
                break;
            case 7 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:50: AFTER
                {
                mAFTER(); 

                }
                break;
            case 8 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:56: AND
                {
                mAND(); 

                }
                break;
            case 9 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:60: AS
                {
                mAS(); 

                }
                break;
            case 10 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:63: ASSERT
                {
                mASSERT(); 

                }
                break;
            case 11 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:70: ATTRIBUTE
                {
                mATTRIBUTE(); 

                }
                break;
            case 12 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:80: BEFORE
                {
                mBEFORE(); 

                }
                break;
            case 13 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:87: BIND
                {
                mBIND(); 

                }
                break;
            case 14 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:92: BIBIND
                {
                mBIBIND(); 

                }
                break;
            case 15 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:99: BREAK
                {
                mBREAK(); 

                }
                break;
            case 16 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:105: BY
                {
                mBY(); 

                }
                break;
            case 17 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:108: CATCH
                {
                mCATCH(); 

                }
                break;
            case 18 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:114: CLASS
                {
                mCLASS(); 

                }
                break;
            case 19 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:120: DELETE
                {
                mDELETE(); 

                }
                break;
            case 20 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:127: DISTINCT
                {
                mDISTINCT(); 

                }
                break;
            case 21 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:136: DO
                {
                mDO(); 

                }
                break;
            case 22 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:139: DUR
                {
                mDUR(); 

                }
                break;
            case 23 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:143: EASEBOTH
                {
                mEASEBOTH(); 

                }
                break;
            case 24 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:152: EASEIN
                {
                mEASEIN(); 

                }
                break;
            case 25 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:159: EASEOUT
                {
                mEASEOUT(); 

                }
                break;
            case 26 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:167: TIE
                {
                mTIE(); 

                }
                break;
            case 27 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:171: STAYS
                {
                mSTAYS(); 

                }
                break;
            case 28 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:177: RETURN
                {
                mRETURN(); 

                }
                break;
            case 29 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:184: THROW
                {
                mTHROW(); 

                }
                break;
            case 30 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:190: VAR
                {
                mVAR(); 

                }
                break;
            case 31 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:194: PACKAGE
                {
                mPACKAGE(); 

                }
                break;
            case 32 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:202: IMPORT
                {
                mIMPORT(); 

                }
                break;
            case 33 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:209: FROM
                {
                mFROM(); 

                }
                break;
            case 34 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:214: LATER
                {
                mLATER(); 

                }
                break;
            case 35 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:220: TRIGGER
                {
                mTRIGGER(); 

                }
                break;
            case 36 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:228: ON
                {
                mON(); 

                }
                break;
            case 37 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:231: INSERT
                {
                mINSERT(); 

                }
                break;
            case 38 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:238: INTO
                {
                mINTO(); 

                }
                break;
            case 39 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:243: FIRST
                {
                mFIRST(); 

                }
                break;
            case 40 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:249: LAST
                {
                mLAST(); 

                }
                break;
            case 41 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:254: IF
                {
                mIF(); 

                }
                break;
            case 42 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:257: THEN
                {
                mTHEN(); 

                }
                break;
            case 43 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:262: ELSE
                {
                mELSE(); 

                }
                break;
            case 44 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:267: THIS
                {
                mTHIS(); 

                }
                break;
            case 45 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:272: NULL
                {
                mNULL(); 

                }
                break;
            case 46 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:277: TRUE
                {
                mTRUE(); 

                }
                break;
            case 47 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:282: FALSE
                {
                mFALSE(); 

                }
                break;
            case 48 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:288: FOR
                {
                mFOR(); 

                }
                break;
            case 49 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:292: UNITINTERVAL
                {
                mUNITINTERVAL(); 

                }
                break;
            case 50 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:305: IN
                {
                mIN(); 

                }
                break;
            case 51 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:308: FPS
                {
                mFPS(); 

                }
                break;
            case 52 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:312: WHILE
                {
                mWHILE(); 

                }
                break;
            case 53 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:318: CONTINUE
                {
                mCONTINUE(); 

                }
                break;
            case 54 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:327: LINEAR
                {
                mLINEAR(); 

                }
                break;
            case 55 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:334: MOTION
                {
                mMOTION(); 

                }
                break;
            case 56 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:341: TRY
                {
                mTRY(); 

                }
                break;
            case 57 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:345: FINALLY
                {
                mFINALLY(); 

                }
                break;
            case 58 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:353: LAZY
                {
                mLAZY(); 

                }
                break;
            case 59 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:358: FOREACH
                {
                mFOREACH(); 

                }
                break;
            case 60 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:366: WHERE
                {
                mWHERE(); 

                }
                break;
            case 61 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:372: NOT
                {
                mNOT(); 

                }
                break;
            case 62 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:376: NEW
                {
                mNEW(); 

                }
                break;
            case 63 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:380: PRIVATE
                {
                mPRIVATE(); 

                }
                break;
            case 64 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:388: PROTECTED
                {
                mPROTECTED(); 

                }
                break;
            case 65 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:398: PUBLIC
                {
                mPUBLIC(); 

                }
                break;
            case 66 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:405: OPERATION
                {
                mOPERATION(); 

                }
                break;
            case 67 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:415: FUNCTION
                {
                mFUNCTION(); 

                }
                break;
            case 68 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:424: READONLY
                {
                mREADONLY(); 

                }
                break;
            case 69 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:433: INVERSE
                {
                mINVERSE(); 

                }
                break;
            case 70 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:441: TYPE
                {
                mTYPE(); 

                }
                break;
            case 71 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:446: EXTENDS
                {
                mEXTENDS(); 

                }
                break;
            case 72 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:454: ORDER
                {
                mORDER(); 

                }
                break;
            case 73 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:460: INDEX
                {
                mINDEX(); 

                }
                break;
            case 74 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:466: INSTANCEOF
                {
                mINSTANCEOF(); 

                }
                break;
            case 75 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:477: INDEXOF
                {
                mINDEXOF(); 

                }
                break;
            case 76 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:485: SELECT
                {
                mSELECT(); 

                }
                break;
            case 77 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:492: SUPER
                {
                mSUPER(); 

                }
                break;
            case 78 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:498: OR
                {
                mOR(); 

                }
                break;
            case 79 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:501: SIZEOF
                {
                mSIZEOF(); 

                }
                break;
            case 80 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:508: REVERSE
                {
                mREVERSE(); 

                }
                break;
            case 81 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:516: XOR
                {
                mXOR(); 

                }
                break;
            case 82 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:520: LPAREN
                {
                mLPAREN(); 

                }
                break;
            case 83 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:527: RPAREN
                {
                mRPAREN(); 

                }
                break;
            case 84 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:534: LBRACE
                {
                mLBRACE(); 

                }
                break;
            case 85 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:541: RBRACE
                {
                mRBRACE(); 

                }
                break;
            case 86 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:548: LBRACKET
                {
                mLBRACKET(); 

                }
                break;
            case 87 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:557: RBRACKET
                {
                mRBRACKET(); 

                }
                break;
            case 88 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:566: SEMI
                {
                mSEMI(); 

                }
                break;
            case 89 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:571: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 90 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:577: DOT
                {
                mDOT(); 

                }
                break;
            case 91 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:581: EQEQ
                {
                mEQEQ(); 

                }
                break;
            case 92 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:586: EQ
                {
                mEQ(); 

                }
                break;
            case 93 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:589: GT
                {
                mGT(); 

                }
                break;
            case 94 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:592: LT
                {
                mLT(); 

                }
                break;
            case 95 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:595: LTGT
                {
                mLTGT(); 

                }
                break;
            case 96 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:600: LTEQ
                {
                mLTEQ(); 

                }
                break;
            case 97 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:605: GTEQ
                {
                mGTEQ(); 

                }
                break;
            case 98 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:610: PLUS
                {
                mPLUS(); 

                }
                break;
            case 99 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:615: PLUSPLUS
                {
                mPLUSPLUS(); 

                }
                break;
            case 100 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:624: SUB
                {
                mSUB(); 

                }
                break;
            case 101 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:628: SUBSUB
                {
                mSUBSUB(); 

                }
                break;
            case 102 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:635: STAR
                {
                mSTAR(); 

                }
                break;
            case 103 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:640: SLASH
                {
                mSLASH(); 

                }
                break;
            case 104 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:646: PERCENT
                {
                mPERCENT(); 

                }
                break;
            case 105 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:654: PLUSEQ
                {
                mPLUSEQ(); 

                }
                break;
            case 106 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:661: SUBEQ
                {
                mSUBEQ(); 

                }
                break;
            case 107 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:667: STAREQ
                {
                mSTAREQ(); 

                }
                break;
            case 108 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:674: SLASHEQ
                {
                mSLASHEQ(); 

                }
                break;
            case 109 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:682: PERCENTEQ
                {
                mPERCENTEQ(); 

                }
                break;
            case 110 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:692: LTLT
                {
                mLTLT(); 

                }
                break;
            case 111 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:697: GTGT
                {
                mGTGT(); 

                }
                break;
            case 112 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:702: COLON
                {
                mCOLON(); 

                }
                break;
            case 113 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:708: QUES
                {
                mQUES(); 

                }
                break;
            case 114 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:713: STRING_LITERAL
                {
                mSTRING_LITERAL(); 

                }
                break;
            case 115 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:728: QUOTE_LBRACE_STRING_LITERAL
                {
                mQUOTE_LBRACE_STRING_LITERAL(); 

                }
                break;
            case 116 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:756: RBRACE_QUOTE_STRING_LITERAL
                {
                mRBRACE_QUOTE_STRING_LITERAL(); 

                }
                break;
            case 117 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:784: RBRACE_LBRACE_STRING_LITERAL
                {
                mRBRACE_LBRACE_STRING_LITERAL(); 

                }
                break;
            case 118 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:813: FORMAT_STRING_LITERAL
                {
                mFORMAT_STRING_LITERAL(); 

                }
                break;
            case 119 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:835: QUOTED_IDENTIFIER
                {
                mQUOTED_IDENTIFIER(); 

                }
                break;
            case 120 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:853: INTEGER_LITERAL
                {
                mINTEGER_LITERAL(); 

                }
                break;
            case 121 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:869: FLOATING_POINT_LITERAL
                {
                mFLOATING_POINT_LITERAL(); 

                }
                break;
            case 122 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:892: IDENTIFIER
                {
                mIDENTIFIER(); 

                }
                break;
            case 123 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:903: WS
                {
                mWS(); 

                }
                break;
            case 124 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:906: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 125 :
                // C:\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:1:914: LINE_COMMENT
                {
                mLINE_COMMENT(); 

                }
                break;

        }

    }


    protected DFA12 dfa12 = new DFA12(this);
    protected DFA19 dfa19 = new DFA19(this);
    static final String DFA12_eotS =
        "\5\uffff";
    static final String DFA12_eofS =
        "\5\uffff";
    static final String DFA12_minS =
        "\2\56\3\uffff";
    static final String DFA12_maxS =
        "\1\71\1\145\3\uffff";
    static final String DFA12_acceptS =
        "\2\uffff\1\2\1\1\1\3";
    static final String DFA12_specialS =
        "\5\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\2\1\uffff\12\1",
            "\1\3\1\uffff\12\1\13\uffff\1\4\37\uffff\1\4",
            "",
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
            return "174:1: FLOATING_POINT_LITERAL : ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( Exponent )? | '.' ( '0' .. '9' )+ ( Exponent )? | ( '0' .. '9' )+ Exponent );";
        }
    }
    static final String DFA19_eotS =
        "\3\uffff\1\54\1\63\1\71\22\54\3\uffff\1\155\4\uffff\1\157\1\162"+
        "\1\165\1\170\1\172\1\176\1\u0080\3\uffff\2\u0084\2\uffff\4\54\5"+
        "\uffff\1\u008f\2\uffff\2\54\1\u0093\4\54\1\u0099\7\54\1\u00a1\14"+
        "\54\1\u00b5\1\u00b6\11\54\1\u00c4\1\u00c5\10\54\24\uffff\1\u00cf"+
        "\2\uffff\1\u00d0\2\uffff\1\u0084\4\54\1\u00d5\1\54\1\u00d7\1\54"+
        "\2\uffff\1\54\1\u00da\1\54\1\uffff\5\54\1\uffff\6\54\1\u00e7\1\uffff"+
        "\12\54\1\u00f2\10\54\2\uffff\4\54\1\u0100\1\u0102\7\54\2\uffff\2"+
        "\54\1\u010c\1\u010d\4\54\1\u0112\2\uffff\1\u0114\1\u0115\1\54\1"+
        "\u0117\1\uffff\1\54\1\uffff\1\u0119\1\54\1\uffff\4\54\1\u011f\7"+
        "\54\1\uffff\2\54\1\u012b\7\54\1\uffff\4\54\1\u0137\7\54\1\u013f"+
        "\1\uffff\1\54\1\uffff\2\54\1\u0143\1\u0144\4\54\1\u0149\2\uffff"+
        "\4\54\1\uffff\1\54\2\uffff\1\u014f\1\uffff\1\54\1\uffff\2\54\1\u0153"+
        "\1\54\1\u0155\1\uffff\2\54\1\u0158\1\u0159\7\54\1\uffff\1\u0161"+
        "\2\54\1\u0164\7\54\1\uffff\2\54\1\u016f\3\54\1\u0173\1\uffff\1\54"+
        "\1\u0175\1\54\2\uffff\1\u0177\1\54\1\u0179\1\54\1\uffff\1\54\1\u017c"+
        "\1\u017d\1\54\1\u017f\1\uffff\2\54\1\u0182\1\uffff\1\54\1\uffff"+
        "\1\u0184\1\u0185\2\uffff\1\54\1\u0187\4\54\1\u018c\1\uffff\1\u018d"+
        "\1\u018e\1\uffff\2\54\1\u0191\1\54\1\u0193\3\54\1\u0197\1\54\1\uffff"+
        "\1\54\1\u019a\1\54\1\uffff\1\54\1\uffff\1\54\1\uffff\1\u019e\1\uffff"+
        "\2\54\2\uffff\1\u01a1\1\uffff\1\u01a2\1\54\1\uffff\1\54\2\uffff"+
        "\1\54\1\uffff\1\54\1\u01a7\1\54\1\u01a9\3\uffff\1\u01aa\1\54\1\uffff"+
        "\1\u01ac\1\uffff\1\u01ad\2\54\1\uffff\1\u01b0\1\u01b1\1\uffff\1"+
        "\u01b2\1\u01b3\1\54\1\uffff\2\54\2\uffff\1\u01b7\1\54\1\u01b9\1"+
        "\u01ba\1\uffff\1\u01bb\2\uffff\1\u01bc\2\uffff\2\54\4\uffff\1\u01bf"+
        "\2\54\1\uffff\1\u01c2\4\uffff\1\u01c3\1\54\1\uffff\1\u01c5\1\54"+
        "\2\uffff\1\u01c7\1\uffff\1\54\1\uffff\1\54\1\u01ca\1\uffff";
    static final String DFA19_eofS =
        "\u01cb\uffff";
    static final String DFA19_minS =
        "\1\11\2\uffff\1\150\1\56\1\55\1\142\1\145\1\141\1\145\1\141\2\145"+
        "\2\141\1\146\2\141\1\156\1\145\1\156\1\150\2\157\3\uffff\1\42\4"+
        "\uffff\2\75\1\53\1\55\1\75\1\52\1\0\2\uffff\1\0\2\56\2\uffff\1\160"+
        "\2\145\1\151\5\uffff\1\0\2\uffff\1\163\1\144\1\44\2\164\1\145\1"+
        "\142\1\44\1\146\1\141\1\164\1\156\1\154\1\163\1\162\1\44\1\164\2"+
        "\163\1\160\1\154\1\172\2\141\1\162\1\143\1\142\1\151\2\44\1\160"+
        "\1\156\1\157\1\163\1\162\1\154\1\156\1\163\1\156\2\44\1\145\1\154"+
        "\1\164\1\167\1\151\1\145\1\164\1\162\24\uffff\1\0\2\uffff\1\0\2"+
        "\uffff\1\56\1\145\1\156\1\157\1\163\1\44\1\147\1\44\1\145\2\uffff"+
        "\1\164\1\44\1\145\1\uffff\1\145\1\162\1\141\1\144\1\151\1\uffff"+
        "\1\157\1\163\1\143\1\164\1\145\1\164\1\44\1\uffff\6\145\1\171\1"+
        "\145\1\144\1\165\1\44\1\153\1\154\1\166\1\164\1\157\3\145\2\uffff"+
        "\1\157\1\141\1\163\1\155\2\44\1\163\1\143\1\171\1\164\3\145\2\uffff"+
        "\1\162\1\154\2\44\1\164\1\162\1\154\1\151\1\44\2\uffff\2\44\1\167"+
        "\1\44\1\uffff\1\147\1\uffff\1\44\1\162\1\uffff\2\162\1\151\1\153"+
        "\1\44\1\156\1\162\1\163\1\150\1\151\1\164\1\151\1\uffff\1\156\1"+
        "\142\1\44\1\162\1\143\1\157\1\163\1\162\1\157\1\162\1\uffff\1\141"+
        "\1\151\1\141\1\145\1\44\1\141\1\162\1\170\2\162\1\154\1\164\1\44"+
        "\1\uffff\1\141\1\uffff\1\145\1\164\2\44\1\162\1\141\1\162\1\141"+
        "\1\44\2\uffff\1\151\2\145\1\157\1\uffff\1\146\2\uffff\1\44\1\uffff"+
        "\1\145\1\uffff\1\141\1\164\1\44\1\142\1\44\1\uffff\1\144\1\145\2"+
        "\44\1\156\1\145\1\156\1\144\1\157\1\165\1\156\1\uffff\1\44\1\164"+
        "\1\146\1\44\1\163\2\156\1\147\1\143\1\164\1\143\1\uffff\1\156\1"+
        "\164\1\44\1\163\1\164\1\154\1\44\1\uffff\1\143\1\44\1\151\2\uffff"+
        "\1\44\1\162\1\44\1\164\1\uffff\1\156\2\44\1\156\1\44\1\uffff\1\162"+
        "\1\143\1\44\1\uffff\1\165\1\uffff\2\44\2\uffff\1\165\1\44\1\143"+
        "\1\163\2\164\1\44\1\uffff\2\44\1\uffff\1\145\1\154\1\44\1\145\1"+
        "\44\1\145\1\164\1\143\1\44\1\146\1\uffff\1\145\1\44\1\171\1\uffff"+
        "\1\150\1\uffff\1\157\1\uffff\1\44\1\uffff\1\151\1\164\2\uffff\1"+
        "\44\1\uffff\1\44\1\164\1\uffff\1\164\2\uffff\1\145\1\uffff\1\164"+
        "\1\44\1\150\1\44\3\uffff\1\44\1\171\1\uffff\1\44\1\uffff\1\44\2"+
        "\145\1\uffff\2\44\1\uffff\2\44\1\156\1\uffff\1\157\1\145\2\uffff"+
        "\1\44\1\145\2\44\1\uffff\1\44\2\uffff\1\44\2\uffff\1\144\1\157\4"+
        "\uffff\1\44\1\156\1\162\1\uffff\1\44\4\uffff\1\44\1\146\1\uffff"+
        "\1\44\1\166\2\uffff\1\44\1\uffff\1\141\1\uffff\1\154\1\44\1\uffff";
    static final String DFA19_maxS =
        "\1\ufaff\2\uffff\1\171\1\71\1\76\1\164\1\171\1\157\1\165\1\170\1"+
        "\165\1\145\1\141\1\165\1\156\1\165\1\151\1\162\1\165\1\156\1\150"+
        "\2\157\3\uffff\1\173\4\uffff\1\75\1\76\4\75\1\ufffe\2\uffff\1\ufffe"+
        "\2\145\2\uffff\1\160\1\162\1\145\1\171\5\uffff\1\ufffe\2\uffff\1"+
        "\163\1\144\1\ufaff\2\164\1\145\1\156\1\ufaff\1\146\1\141\1\164\1"+
        "\156\1\154\1\163\1\162\1\ufaff\1\164\2\163\1\160\1\154\1\172\1\141"+
        "\1\166\1\162\1\143\1\142\1\157\2\ufaff\1\160\1\162\1\157\1\163\1"+
        "\162\1\154\1\156\1\172\1\156\2\ufaff\1\145\1\154\1\164\1\167\2\151"+
        "\1\164\1\162\24\uffff\1\ufffe\2\uffff\1\ufffe\2\uffff\2\145\1\156"+
        "\1\157\1\163\1\ufaff\1\147\1\ufaff\1\145\2\uffff\1\164\1\ufaff\1"+
        "\145\1\uffff\1\145\1\162\1\141\1\144\1\151\1\uffff\1\157\1\163\1"+
        "\143\1\164\1\145\1\164\1\ufaff\1\uffff\6\145\1\171\1\145\1\144\1"+
        "\165\1\ufaff\1\153\1\154\1\166\1\164\1\157\1\164\2\145\2\uffff\1"+
        "\157\1\141\1\163\1\155\2\ufaff\1\163\1\143\1\171\1\164\3\145\2\uffff"+
        "\1\162\1\154\2\ufaff\1\164\1\162\1\154\1\151\1\ufaff\2\uffff\2\ufaff"+
        "\1\167\1\ufaff\1\uffff\1\147\1\uffff\1\ufaff\1\162\1\uffff\2\162"+
        "\1\151\1\153\1\ufaff\1\156\1\162\1\163\1\150\1\151\1\164\1\151\1"+
        "\uffff\1\156\1\157\1\ufaff\1\162\1\143\1\157\1\163\1\162\1\157\1"+
        "\162\1\uffff\1\141\1\151\1\141\1\145\1\ufaff\1\141\1\162\1\170\2"+
        "\162\1\154\1\164\1\ufaff\1\uffff\1\141\1\uffff\1\145\1\164\2\ufaff"+
        "\1\162\1\141\1\162\1\141\1\ufaff\2\uffff\1\151\2\145\1\157\1\uffff"+
        "\1\146\2\uffff\1\ufaff\1\uffff\1\145\1\uffff\1\141\1\164\1\ufaff"+
        "\1\142\1\ufaff\1\uffff\1\144\1\145\2\ufaff\1\156\1\145\1\156\1\144"+
        "\1\157\1\165\1\156\1\uffff\1\ufaff\1\164\1\146\1\ufaff\1\163\2\156"+
        "\1\147\1\143\1\164\1\143\1\uffff\1\156\1\164\1\ufaff\1\163\1\164"+
        "\1\154\1\ufaff\1\uffff\1\143\1\ufaff\1\151\2\uffff\1\ufaff\1\162"+
        "\1\ufaff\1\164\1\uffff\1\156\2\ufaff\1\156\1\ufaff\1\uffff\1\162"+
        "\1\143\1\ufaff\1\uffff\1\165\1\uffff\2\ufaff\2\uffff\1\165\1\ufaff"+
        "\1\143\1\163\2\164\1\ufaff\1\uffff\2\ufaff\1\uffff\1\145\1\154\1"+
        "\ufaff\1\145\1\ufaff\1\145\1\164\1\143\1\ufaff\1\146\1\uffff\1\145"+
        "\1\ufaff\1\171\1\uffff\1\150\1\uffff\1\157\1\uffff\1\ufaff\1\uffff"+
        "\1\151\1\164\2\uffff\1\ufaff\1\uffff\1\ufaff\1\164\1\uffff\1\164"+
        "\2\uffff\1\145\1\uffff\1\164\1\ufaff\1\150\1\ufaff\3\uffff\1\ufaff"+
        "\1\171\1\uffff\1\ufaff\1\uffff\1\ufaff\2\145\1\uffff\2\ufaff\1\uffff"+
        "\2\ufaff\1\156\1\uffff\1\157\1\145\2\uffff\1\ufaff\1\145\2\ufaff"+
        "\1\uffff\1\ufaff\2\uffff\1\ufaff\2\uffff\1\144\1\157\4\uffff\1\ufaff"+
        "\1\156\1\162\1\uffff\1\ufaff\4\uffff\1\ufaff\1\146\1\uffff\1\ufaff"+
        "\1\166\2\uffff\1\ufaff\1\uffff\1\141\1\uffff\1\154\1\ufaff\1\uffff";
    static final String DFA19_acceptS =
        "\1\uffff\1\1\1\2\25\uffff\1\122\1\123\1\124\1\uffff\1\126\1\127"+
        "\1\130\1\131\7\uffff\1\160\1\161\3\uffff\1\172\1\173\4\uffff\1\4"+
        "\1\132\1\171\1\137\1\140\1\uffff\1\5\1\136\61\uffff\1\164\1\165"+
        "\1\125\1\133\1\134\1\141\1\157\1\135\1\143\1\151\1\142\1\145\1\152"+
        "\1\144\1\153\1\146\1\174\1\154\1\175\1\147\1\uffff\1\150\1\166\1"+
        "\uffff\1\162\1\170\11\uffff\1\167\1\156\3\uffff\1\11\5\uffff\1\20"+
        "\7\uffff\1\25\23\uffff\1\62\1\51\15\uffff\1\116\1\44\11\uffff\1"+
        "\155\1\163\4\uffff\1\32\1\uffff\1\70\2\uffff\1\10\14\uffff\1\26"+
        "\12\uffff\1\36\15\uffff\1\63\1\uffff\1\60\11\uffff\1\75\1\76\4\uffff"+
        "\1\121\1\uffff\1\106\1\52\1\uffff\1\54\1\uffff\1\56\5\uffff\1\15"+
        "\13\uffff\1\53\13\uffff\1\46\7\uffff\1\41\3\uffff\1\72\1\50\4\uffff"+
        "\1\55\5\uffff\1\35\3\uffff\1\7\1\uffff\1\17\2\uffff\1\22\1\21\7"+
        "\uffff\1\115\2\uffff\1\33\12\uffff\1\111\3\uffff\1\47\1\uffff\1"+
        "\57\1\uffff\1\42\1\uffff\1\110\2\uffff\1\74\1\64\1\uffff\1\3\2\uffff"+
        "\1\12\1\uffff\1\16\1\14\1\uffff\1\23\4\uffff\1\30\1\114\1\117\2"+
        "\uffff\1\34\1\uffff\1\101\3\uffff\1\45\2\uffff\1\40\3\uffff\1\66"+
        "\2\uffff\1\67\1\43\4\uffff\1\107\1\uffff\1\31\1\120\1\uffff\1\37"+
        "\1\77\2\uffff\1\113\1\105\1\71\1\73\3\uffff\1\6\1\uffff\1\65\1\24"+
        "\1\27\1\104\2\uffff\1\103\2\uffff\1\13\1\100\1\uffff\1\102\1\uffff"+
        "\1\112\2\uffff\1\61";
    static final String DFA19_specialS =
        "\u01cb\uffff}>";
    static final String[] DFA19_transitionS = {
            "\2\55\1\uffff\2\55\22\uffff\1\55\1\uffff\1\51\1\2\1\54\1\46"+
            "\2\uffff\1\30\1\31\1\44\1\42\1\37\1\43\1\4\1\45\1\52\11\53\1"+
            "\47\1\36\1\5\1\40\1\41\1\50\1\uffff\32\54\1\34\1\uffff\1\35"+
            "\1\uffff\1\54\1\uffff\1\6\1\7\1\10\1\11\1\12\1\20\2\54\1\17"+
            "\2\54\1\21\1\26\1\23\1\22\1\16\1\54\1\14\1\13\1\3\1\24\1\15"+
            "\1\25\1\27\2\54\1\32\1\1\1\33\102\uffff\27\54\1\uffff\37\54"+
            "\1\uffff\u1f08\54\u1040\uffff\u0150\54\u0170\uffff\u0080\54"+
            "\u0080\uffff\u092e\54\u10d2\uffff\u5200\54\u5900\uffff\u0200"+
            "\54",
            "",
            "",
            "\1\57\1\60\10\uffff\1\61\6\uffff\1\56",
            "\1\62\1\uffff\12\64",
            "\1\70\16\uffff\1\67\1\66\1\65",
            "\1\72\3\uffff\1\75\7\uffff\1\73\4\uffff\1\74\1\76",
            "\1\102\3\uffff\1\100\10\uffff\1\77\6\uffff\1\101",
            "\1\104\12\uffff\1\103\2\uffff\1\105",
            "\1\106\3\uffff\1\107\5\uffff\1\111\5\uffff\1\110",
            "\1\113\12\uffff\1\114\13\uffff\1\112",
            "\1\116\3\uffff\1\117\12\uffff\1\120\1\115",
            "\1\121",
            "\1\122",
            "\1\123\20\uffff\1\125\2\uffff\1\124",
            "\1\127\6\uffff\1\130\1\126",
            "\1\135\7\uffff\1\131\5\uffff\1\134\1\133\1\uffff\1\132\2\uffff"+
            "\1\136",
            "\1\137\7\uffff\1\140",
            "\1\142\1\uffff\1\143\1\uffff\1\141",
            "\1\146\11\uffff\1\145\5\uffff\1\144",
            "\1\147",
            "\1\150",
            "\1\151",
            "\1\152",
            "",
            "",
            "",
            "\1\153\130\uffff\1\154",
            "",
            "",
            "",
            "",
            "\1\156",
            "\1\160\1\161",
            "\1\163\21\uffff\1\164",
            "\1\166\17\uffff\1\167",
            "\1\171",
            "\1\173\4\uffff\1\175\15\uffff\1\174",
            "\40\u0081\1\uffff\34\u0081\1\177\uffc1\u0081",
            "",
            "",
            "\173\u0083\1\u0082\uff83\u0083",
            "\1\64\1\uffff\12\64\13\uffff\1\64\37\uffff\1\64",
            "\1\64\1\uffff\12\u0085\13\uffff\1\64\37\uffff\1\64",
            "",
            "",
            "\1\u0086",
            "\1\u0087\3\uffff\1\u0089\10\uffff\1\u0088",
            "\1\u008a",
            "\1\u008b\13\uffff\1\u008d\3\uffff\1\u008c",
            "",
            "",
            "",
            "",
            "",
            "\uffff\u008e",
            "",
            "",
            "\1\u0090",
            "\1\u0091",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\22\54"+
            "\1\u0092\7\54\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54"+
            "\u1040\uffff\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e"+
            "\54\u10d2\uffff\u5200\54\u5900\uffff\u0200\54",
            "\1\u0094",
            "\1\u0095",
            "\1\u0096",
            "\1\u0098\13\uffff\1\u0097",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u009a",
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
            "\1\u00aa\22\uffff\1\u00ab\1\uffff\1\u00a9",
            "\1\u00ac",
            "\1\u00ad",
            "\1\u00ae",
            "\1\u00af\5\uffff\1\u00b0",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\3\54"+
            "\1\u00b3\16\54\1\u00b2\1\u00b1\1\54\1\u00b4\4\54\105\uffff\27"+
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
            "\1\u00c0\1\u00c1\5\uffff\1\u00bf",
            "\1\u00c2",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\3\54"+
            "\1\u00c3\26\54\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54"+
            "\u1040\uffff\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e"+
            "\54\u10d2\uffff\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u00c6",
            "\1\u00c7",
            "\1\u00c8",
            "\1\u00c9",
            "\1\u00ca",
            "\1\u00cb\3\uffff\1\u00cc",
            "\1\u00cd",
            "\1\u00ce",
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
            "",
            "",
            "",
            "\40\u0081\1\uffff\uffde\u0081",
            "",
            "",
            "\uffff\u0083",
            "",
            "",
            "\1\64\1\uffff\12\u0085\13\uffff\1\64\37\uffff\1\64",
            "\1\u00d1",
            "\1\u00d2",
            "\1\u00d3",
            "\1\u00d4",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u00d6",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u00d8",
            "",
            "",
            "\1\u00d9",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u00db",
            "",
            "\1\u00dc",
            "\1\u00dd",
            "\1\u00de",
            "\1\u00df",
            "\1\u00e0",
            "",
            "\1\u00e1",
            "\1\u00e2",
            "\1\u00e3",
            "\1\u00e4",
            "\1\u00e5",
            "\1\u00e6",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\u00e8",
            "\1\u00e9",
            "\1\u00ea",
            "\1\u00eb",
            "\1\u00ec",
            "\1\u00ed",
            "\1\u00ee",
            "\1\u00ef",
            "\1\u00f0",
            "\1\u00f1",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u00f3",
            "\1\u00f4",
            "\1\u00f5",
            "\1\u00f6",
            "\1\u00f7",
            "\1\u00f9\16\uffff\1\u00f8",
            "\1\u00fa",
            "\1\u00fb",
            "",
            "",
            "\1\u00fc",
            "\1\u00fd",
            "\1\u00fe",
            "\1\u00ff",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\4\54"+
            "\1\u0101\25\54\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54"+
            "\u1040\uffff\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e"+
            "\54\u10d2\uffff\u5200\54\u5900\uffff\u0200\54",
            "\1\u0103",
            "\1\u0104",
            "\1\u0105",
            "\1\u0106",
            "\1\u0107",
            "\1\u0108",
            "\1\u0109",
            "",
            "",
            "\1\u010a",
            "\1\u010b",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u010e",
            "\1\u010f",
            "\1\u0110",
            "\1\u0111",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\16\54"+
            "\1\u0113\13\54\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54"+
            "\u1040\uffff\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e"+
            "\54\u10d2\uffff\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0116",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\u0118",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u011a",
            "",
            "\1\u011b",
            "\1\u011c",
            "\1\u011d",
            "\1\u011e",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0120",
            "\1\u0121",
            "\1\u0122",
            "\1\u0123",
            "\1\u0124",
            "\1\u0125",
            "\1\u0126",
            "",
            "\1\u0127",
            "\1\u0128\6\uffff\1\u012a\5\uffff\1\u0129",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u012c",
            "\1\u012d",
            "\1\u012e",
            "\1\u012f",
            "\1\u0130",
            "\1\u0131",
            "\1\u0132",
            "",
            "\1\u0133",
            "\1\u0134",
            "\1\u0135",
            "\1\u0136",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0138",
            "\1\u0139",
            "\1\u013a",
            "\1\u013b",
            "\1\u013c",
            "\1\u013d",
            "\1\u013e",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\u0140",
            "",
            "\1\u0141",
            "\1\u0142",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0145",
            "\1\u0146",
            "\1\u0147",
            "\1\u0148",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "",
            "\1\u014a",
            "\1\u014b",
            "\1\u014c",
            "\1\u014d",
            "",
            "\1\u014e",
            "",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\u0150",
            "",
            "\1\u0151",
            "\1\u0152",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0154",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\u0156",
            "\1\u0157",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u015a",
            "\1\u015b",
            "\1\u015c",
            "\1\u015d",
            "\1\u015e",
            "\1\u015f",
            "\1\u0160",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0162",
            "\1\u0163",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0165",
            "\1\u0166",
            "\1\u0167",
            "\1\u0168",
            "\1\u0169",
            "\1\u016a",
            "\1\u016b",
            "",
            "\1\u016c",
            "\1\u016d",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\16\54"+
            "\1\u016e\13\54\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54"+
            "\u1040\uffff\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e"+
            "\54\u10d2\uffff\u5200\54\u5900\uffff\u0200\54",
            "\1\u0170",
            "\1\u0171",
            "\1\u0172",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\u0174",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0176",
            "",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0178",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u017a",
            "",
            "\1\u017b",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u017e",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\u0180",
            "\1\u0181",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\u0183",
            "",
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
            "\1\u0186",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0188",
            "\1\u0189",
            "\1\u018a",
            "\1\u018b",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\u018f",
            "\1\u0190",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0192",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0194",
            "\1\u0195",
            "\1\u0196",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u0198",
            "",
            "\1\u0199",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u019b",
            "",
            "\1\u019c",
            "",
            "\1\u019d",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\u019f",
            "\1\u01a0",
            "",
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
            "\1\u01a3",
            "",
            "\1\u01a4",
            "",
            "",
            "\1\u01a5",
            "",
            "\1\u01a6",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u01a8",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u01ab",
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
            "\1\u01ae",
            "\1\u01af",
            "",
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
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u01b4",
            "",
            "\1\u01b5",
            "\1\u01b6",
            "",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u01b8",
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
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "",
            "\1\u01bd",
            "\1\u01be",
            "",
            "",
            "",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u01c0",
            "\1\u01c1",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "",
            "",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u01c4",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "\1\u01c6",
            "",
            "",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            "",
            "\1\u01c8",
            "",
            "\1\u01c9",
            "\1\54\13\uffff\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54"+
            "\105\uffff\27\54\1\uffff\37\54\1\uffff\u1f08\54\u1040\uffff"+
            "\u0150\54\u0170\uffff\u0080\54\u0080\uffff\u092e\54\u10d2\uffff"+
            "\u5200\54\u5900\uffff\u0200\54",
            ""
    };

    static final short[] DFA19_eot = DFA.unpackEncodedString(DFA19_eotS);
    static final short[] DFA19_eof = DFA.unpackEncodedString(DFA19_eofS);
    static final char[] DFA19_min = DFA.unpackEncodedStringToUnsignedChars(DFA19_minS);
    static final char[] DFA19_max = DFA.unpackEncodedStringToUnsignedChars(DFA19_maxS);
    static final short[] DFA19_accept = DFA.unpackEncodedString(DFA19_acceptS);
    static final short[] DFA19_special = DFA.unpackEncodedString(DFA19_specialS);
    static final short[][] DFA19_transition;

    static {
        int numStates = DFA19_transitionS.length;
        DFA19_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA19_transition[i] = DFA.unpackEncodedString(DFA19_transitionS[i]);
        }
    }

    class DFA19 extends DFA {

        public DFA19(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 19;
            this.eot = DFA19_eot;
            this.eof = DFA19_eof;
            this.min = DFA19_min;
            this.max = DFA19_max;
            this.accept = DFA19_accept;
            this.special = DFA19_special;
            this.transition = DFA19_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( BAR | POUND | TYPEOF | DOTDOT | LARROW | ABSTRACT | AFTER | AND | AS | ASSERT | ATTRIBUTE | BEFORE | BIND | BIBIND | BREAK | BY | CATCH | CLASS | DELETE | DISTINCT | DO | DUR | EASEBOTH | EASEIN | EASEOUT | TIE | STAYS | RETURN | THROW | VAR | PACKAGE | IMPORT | FROM | LATER | TRIGGER | ON | INSERT | INTO | FIRST | LAST | IF | THEN | ELSE | THIS | NULL | TRUE | FALSE | FOR | UNITINTERVAL | IN | FPS | WHILE | CONTINUE | LINEAR | MOTION | TRY | FINALLY | LAZY | FOREACH | WHERE | NOT | NEW | PRIVATE | PROTECTED | PUBLIC | OPERATION | FUNCTION | READONLY | INVERSE | TYPE | EXTENDS | ORDER | INDEX | INSTANCEOF | INDEXOF | SELECT | SUPER | OR | SIZEOF | REVERSE | XOR | LPAREN | RPAREN | LBRACE | RBRACE | LBRACKET | RBRACKET | SEMI | COMMA | DOT | EQEQ | EQ | GT | LT | LTGT | LTEQ | GTEQ | PLUS | PLUSPLUS | SUB | SUBSUB | STAR | SLASH | PERCENT | PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ | LTLT | GTGT | COLON | QUES | STRING_LITERAL | QUOTE_LBRACE_STRING_LITERAL | RBRACE_QUOTE_STRING_LITERAL | RBRACE_LBRACE_STRING_LITERAL | FORMAT_STRING_LITERAL | QUOTED_IDENTIFIER | INTEGER_LITERAL | FLOATING_POINT_LITERAL | IDENTIFIER | WS | COMMENT | LINE_COMMENT );";
        }
    }
 

}