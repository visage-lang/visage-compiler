// $ANTLR 3.0 \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g 2007-08-03 10:09:35

package com.sun.tools.javafx.antlr;

import java.util.HashMap;
import java.util.Map;
import java.io.OutputStreamWriter;

import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javafx.tree.*;

import com.sun.tools.javac.code.*;
import com.sun.tools.javac.util.*;
import static com.sun.tools.javac.util.ListBuffer.lb;
import com.sun.tools.javafx.code.JavafxBindStatus;
import static com.sun.tools.javafx.code.JavafxBindStatus.*;

import org.antlr.runtime.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class v2Parser extends AbstractGeneratedParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "BAR", "POUND", "TYPEOF", "DOTDOT", "LARROW", "ABSTRACT", "AFTER", "AND", "AS", "ASSERT", "ATTRIBUTE", "BEFORE", "BIND", "BREAK", "BY", "CATCH", "CLASS", "DELETE", "DISTINCT", "DO", "DUR", "EASEBOTH", "EASEIN", "EASEOUT", "TIE", "STAYS", "RETURN", "THROW", "VAR", "PACKAGE", "IMPORT", "FROM", "ON", "INSERT", "INTO", "FIRST", "LAST", "IF", "THEN", "ELSE", "THIS", "NULL", "TRUE", "FALSE", "FOR", "UNITINTERVAL", "IN", "FPS", "WHILE", "CONTINUE", "LINEAR", "MOTION", "TRY", "FINALLY", "LAZY", "WHERE", "NOT", "NEW", "PRIVATE", "PROTECTED", "PUBLIC", "FUNCTION", "READONLY", "INVERSE", "TYPE", "EXTENDS", "ORDER", "INDEX", "INSTANCEOF", "INDEXOF", "SELECT", "SUPER", "OR", "SIZEOF", "REVERSE", "LPAREN", "RPAREN", "LBRACKET", "RBRACKET", "SEMI", "COMMA", "DOT", "EQEQ", "EQ", "GT", "LT", "LTGT", "LTEQ", "GTEQ", "PLUS", "PLUSPLUS", "SUB", "SUBSUB", "STAR", "SLASH", "PERCENT", "PLUSEQ", "SUBEQ", "STAREQ", "SLASHEQ", "PERCENTEQ", "LTLT", "GTGT", "COLON", "QUES", "STRING_LITERAL", "NextIsPercent", "QUOTE_LBRACE_STRING_LITERAL", "LBRACE", "RBRACE_QUOTE_STRING_LITERAL", "RBRACE_LBRACE_STRING_LITERAL", "RBRACE", "FORMAT_STRING_LITERAL", "QUOTED_IDENTIFIER", "INTEGER_LITERAL", "Exponent", "FLOATING_POINT_LITERAL", "Letter", "JavaIDDigit", "IDENTIFIER", "WS", "COMMENT", "LINE_COMMENT"
    };
    public static final int PACKAGE=33;
    public static final int FUNCTION=65;
    public static final int LT=89;
    public static final int STAR=97;
    public static final int WHILE=52;
    public static final int EASEOUT=27;
    public static final int NEW=61;
    public static final int INDEXOF=73;
    public static final int DO=23;
    public static final int UNITINTERVAL=49;
    public static final int NOT=60;
    public static final int GTGT=106;
    public static final int EOF=-1;
    public static final int RBRACE_QUOTE_STRING_LITERAL=113;
    public static final int BREAK=17;
    public static final int TYPE=68;
    public static final int LBRACKET=81;
    public static final int RPAREN=80;
    public static final int IMPORT=34;
    public static final int LINEAR=54;
    public static final int STRING_LITERAL=109;
    public static final int FLOATING_POINT_LITERAL=120;
    public static final int INSERT=37;
    public static final int SUBSUB=96;
    public static final int BIND=16;
    public static final int STAREQ=102;
    public static final int RETURN=30;
    public static final int THIS=44;
    public static final int VAR=32;
    public static final int SUPER=75;
    public static final int LAST=40;
    public static final int EQ=87;
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
    public static final int THEN=42;
    public static final int IN=50;
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
    public static final int GTEQ=92;
    public static final int AFTER=10;
    public static final int READONLY=66;
    public static final int TRUE=46;
    public static final int SEMI=83;
    public static final int COLON=107;
    public static final int PERCENTEQ=104;
    public static final int FINALLY=57;
    public static final int FORMAT_STRING_LITERAL=116;
    public static final int EASEIN=26;
    public static final int QUOTED_IDENTIFIER=117;
    public static final int FPS=51;
    public static final int PUBLIC=64;
    public static final int EXTENDS=69;
    public static final int BAR=4;
    public static final int FIRST=39;

        public v2Parser(TokenStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g"; }

    
            public v2Parser(Context context, CharSequence content) {
               this(new CommonTokenStream(new v2Lexer(new ANTLRStringStream(content.toString()))));
               initialize(context);
        	}



    // $ANTLR start module
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:320:1: module returns [JCCompilationUnit result] : ( packageDecl )? moduleItems EOF ;
    public final JCCompilationUnit module() throws RecognitionException {
        JCCompilationUnit result = null;

        JCExpression packageDecl1 = null;

        ListBuffer<JCTree> moduleItems2 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:8: ( ( packageDecl )? moduleItems EOF )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:10: ( packageDecl )? moduleItems EOF
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:10: ( packageDecl )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==PACKAGE) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:10: packageDecl
                    {
                    pushFollow(FOLLOW_packageDecl_in_module2018);
                    packageDecl1=packageDecl();
                    _fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_moduleItems_in_module2021);
            moduleItems2=moduleItems();
            _fsp--;

            match(input,EOF,FOLLOW_EOF_in_module2023); 
             result = F.TopLevel(noJCAnnotations(), packageDecl1, moduleItems2.toList()); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end module


    // $ANTLR start packageDecl
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:322:1: packageDecl returns [JCExpression value] : PACKAGE qualident SEMI ;
    public final JCExpression packageDecl() throws RecognitionException {
        JCExpression value = null;

        JCExpression qualident3 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:323:8: ( PACKAGE qualident SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:323:10: PACKAGE qualident SEMI
            {
            match(input,PACKAGE,FOLLOW_PACKAGE_in_packageDecl2045); 
            pushFollow(FOLLOW_qualident_in_packageDecl2047);
            qualident3=qualident();
            _fsp--;

            match(input,SEMI,FOLLOW_SEMI_in_packageDecl2049); 
             value = qualident3; 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end packageDecl


    // $ANTLR start moduleItems
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:324:1: moduleItems returns [ListBuffer<JCTree> items = new ListBuffer<JCTree>()] : ( moduleItem )* ;
    public final ListBuffer<JCTree> moduleItems() throws RecognitionException {
        ListBuffer<JCTree> items =  new ListBuffer<JCTree>();

        JCTree moduleItem4 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:325:9: ( ( moduleItem )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:325:11: ( moduleItem )*
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:325:11: ( moduleItem )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=POUND && LA2_0<=TYPEOF)||LA2_0==ABSTRACT||LA2_0==BREAK||LA2_0==CLASS||(LA2_0>=RETURN && LA2_0<=VAR)||LA2_0==IMPORT||LA2_0==IF||(LA2_0>=THIS && LA2_0<=FALSE)||(LA2_0>=WHILE && LA2_0<=CONTINUE)||LA2_0==TRY||(LA2_0>=NOT && LA2_0<=READONLY)||LA2_0==SUPER||(LA2_0>=SIZEOF && LA2_0<=LPAREN)||(LA2_0>=PLUSPLUS && LA2_0<=SUBSUB)||(LA2_0>=QUES && LA2_0<=STRING_LITERAL)||LA2_0==QUOTE_LBRACE_STRING_LITERAL||(LA2_0>=QUOTED_IDENTIFIER && LA2_0<=INTEGER_LITERAL)||LA2_0==FLOATING_POINT_LITERAL||LA2_0==IDENTIFIER) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:325:12: moduleItem
            	    {
            	    pushFollow(FOLLOW_moduleItem_in_moduleItems2080);
            	    moduleItem4=moduleItem();
            	    _fsp--;

            	     items.append(moduleItem4); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return items;
    }
    // $ANTLR end moduleItems


    // $ANTLR start moduleItem
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );
    public final JCTree moduleItem() throws RecognitionException {
        JCTree value = null;

        JCTree importDecl5 = null;

        JFXClassDeclaration classDefinition6 = null;

        JCStatement statement7 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:327:8: ( importDecl | classDefinition | statement )
            int alt3=3;
            switch ( input.LA(1) ) {
            case IMPORT:
                {
                alt3=1;
                }
                break;
            case ABSTRACT:
                {
                switch ( input.LA(2) ) {
                case PUBLIC:
                    {
                    int LA3_9 = input.LA(3);

                    if ( (LA3_9==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_9==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 9, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA3_10 = input.LA(3);

                    if ( (LA3_10==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_10==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA3_11 = input.LA(3);

                    if ( (LA3_11==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_11==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case FUNCTION:
                    {
                    alt3=3;
                    }
                    break;
                case CLASS:
                    {
                    alt3=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 2, input);

                    throw nvae;
                }

                }
                break;
            case READONLY:
                {
                switch ( input.LA(2) ) {
                case PUBLIC:
                    {
                    int LA3_9 = input.LA(3);

                    if ( (LA3_9==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_9==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 9, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA3_10 = input.LA(3);

                    if ( (LA3_10==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_10==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA3_11 = input.LA(3);

                    if ( (LA3_11==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_11==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case FUNCTION:
                    {
                    alt3=3;
                    }
                    break;
                case CLASS:
                    {
                    alt3=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 3, input);

                    throw nvae;
                }

                }
                break;
            case PUBLIC:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA3_12 = input.LA(3);

                    if ( (LA3_12==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_12==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA3_13 = input.LA(3);

                    if ( (LA3_13==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_13==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case CLASS:
                    {
                    alt3=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt3=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 4, input);

                    throw nvae;
                }

                }
                break;
            case PRIVATE:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA3_12 = input.LA(3);

                    if ( (LA3_12==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_12==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA3_13 = input.LA(3);

                    if ( (LA3_13==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_13==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case CLASS:
                    {
                    alt3=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt3=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 5, input);

                    throw nvae;
                }

                }
                break;
            case PROTECTED:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA3_12 = input.LA(3);

                    if ( (LA3_12==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_12==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA3_13 = input.LA(3);

                    if ( (LA3_13==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_13==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case FUNCTION:
                    {
                    alt3=3;
                    }
                    break;
                case CLASS:
                    {
                    alt3=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 6, input);

                    throw nvae;
                }

                }
                break;
            case CLASS:
                {
                alt3=2;
                }
                break;
            case POUND:
            case TYPEOF:
            case BREAK:
            case RETURN:
            case THROW:
            case VAR:
            case IF:
            case THIS:
            case NULL:
            case TRUE:
            case FALSE:
            case WHILE:
            case CONTINUE:
            case TRY:
            case NOT:
            case NEW:
            case FUNCTION:
            case SUPER:
            case SIZEOF:
            case REVERSE:
            case LPAREN:
            case PLUSPLUS:
            case SUB:
            case SUBSUB:
            case QUES:
            case STRING_LITERAL:
            case QUOTE_LBRACE_STRING_LITERAL:
            case QUOTED_IDENTIFIER:
            case INTEGER_LITERAL:
            case FLOATING_POINT_LITERAL:
            case IDENTIFIER:
                {
                alt3=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:327:10: importDecl
                    {
                    pushFollow(FOLLOW_importDecl_in_moduleItem2124);
                    importDecl5=importDecl();
                    _fsp--;

                     value = importDecl5; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:328:10: classDefinition
                    {
                    pushFollow(FOLLOW_classDefinition_in_moduleItem2140);
                    classDefinition6=classDefinition();
                    _fsp--;

                     value = classDefinition6; 

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:329:10: statement
                    {
                    pushFollow(FOLLOW_statement_in_moduleItem2156);
                    statement7=statement();
                    _fsp--;

                     value = statement7; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end moduleItem


    // $ANTLR start importDecl
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:330:1: importDecl returns [JCTree value] : IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI ;
    public final JCTree importDecl() throws RecognitionException {
        JCTree value = null;

        Token STAR10=null;
        Token IMPORT11=null;
        JCIdent identifier8 = null;

        name_return name9 = null;


         JCExpression pid = null; 
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:332:9: ( IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:332:11: IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI
            {
            IMPORT11=(Token)input.LT(1);
            match(input,IMPORT,FOLLOW_IMPORT_in_importDecl2192); 
            pushFollow(FOLLOW_identifier_in_importDecl2195);
            identifier8=identifier();
            _fsp--;

             pid = identifier8; 
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:333:18: ( DOT name )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==DOT) ) {
                    int LA4_1 = input.LA(2);

                    if ( (LA4_1==QUOTED_IDENTIFIER||LA4_1==IDENTIFIER) ) {
                        alt4=1;
                    }


                }


                switch (alt4) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:333:20: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_importDecl2220); 
            	    pushFollow(FOLLOW_name_in_importDecl2222);
            	    name9=name();
            	    _fsp--;

            	     pid = F.at(name9.pos).Select(pid, name9.value); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:334:18: ( DOT STAR )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==DOT) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:334:20: DOT STAR
                    {
                    match(input,DOT,FOLLOW_DOT_in_importDecl2251); 
                    STAR10=(Token)input.LT(1);
                    match(input,STAR,FOLLOW_STAR_in_importDecl2253); 
                     pid = F.at(pos(STAR10)).Select(pid, names.asterisk); 

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_importDecl2262); 
             value = F.at(pos(IMPORT11)).Import(pid, false); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end importDecl


    // $ANTLR start classDefinition
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:336:1: classDefinition returns [JFXClassDeclaration value] : modifierFlags CLASS name supers LBRACE classMembers RBRACE ;
    public final JFXClassDeclaration classDefinition() throws RecognitionException {
        JFXClassDeclaration value = null;

        Token CLASS12=null;
        JCModifiers modifierFlags13 = null;

        name_return name14 = null;

        ListBuffer<Name> supers15 = null;

        ListBuffer<JFXMemberDeclaration> classMembers16 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:337:2: ( modifierFlags CLASS name supers LBRACE classMembers RBRACE )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:337:4: modifierFlags CLASS name supers LBRACE classMembers RBRACE
            {
            pushFollow(FOLLOW_modifierFlags_in_classDefinition2293);
            modifierFlags13=modifierFlags();
            _fsp--;

            CLASS12=(Token)input.LT(1);
            match(input,CLASS,FOLLOW_CLASS_in_classDefinition2296); 
            pushFollow(FOLLOW_name_in_classDefinition2298);
            name14=name();
            _fsp--;

            pushFollow(FOLLOW_supers_in_classDefinition2300);
            supers15=supers();
            _fsp--;

            match(input,LBRACE,FOLLOW_LBRACE_in_classDefinition2302); 
            pushFollow(FOLLOW_classMembers_in_classDefinition2304);
            classMembers16=classMembers();
            _fsp--;

            match(input,RBRACE,FOLLOW_RBRACE_in_classDefinition2306); 
             value = F.at(pos(CLASS12)).ClassDeclaration(modifierFlags13, name14.value,
            	                                	                supers15.toList(), classMembers16.toList()); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end classDefinition


    // $ANTLR start supers
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:340:1: supers returns [ListBuffer<Name> names = new ListBuffer<Name>()] : ( EXTENDS name1= name ( COMMA namen= name )* )? ;
    public final ListBuffer<Name> supers() throws RecognitionException {
        ListBuffer<Name> names =  new ListBuffer<Name>();

        name_return name1 = null;

        name_return namen = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:341:2: ( ( EXTENDS name1= name ( COMMA namen= name )* )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:341:4: ( EXTENDS name1= name ( COMMA namen= name )* )?
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:341:4: ( EXTENDS name1= name ( COMMA namen= name )* )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==EXTENDS) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:341:5: EXTENDS name1= name ( COMMA namen= name )*
                    {
                    match(input,EXTENDS,FOLLOW_EXTENDS_in_supers2331); 
                    pushFollow(FOLLOW_name_in_supers2335);
                    name1=name();
                    _fsp--;

                     names.append(name1.value); 
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:342:12: ( COMMA namen= name )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==COMMA) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:342:14: COMMA namen= name
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_supers2363); 
                    	    pushFollow(FOLLOW_name_in_supers2367);
                    	    namen=name();
                    	    _fsp--;

                    	     names.append(namen.value); 

                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return names;
    }
    // $ANTLR end supers


    // $ANTLR start classMembers
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:344:1: classMembers returns [ListBuffer<JFXMemberDeclaration> mems = new ListBuffer<JFXMemberDeclaration>()] : ( attributeDefinition | functionDefinition )* ;
    public final ListBuffer<JFXMemberDeclaration> classMembers() throws RecognitionException {
        ListBuffer<JFXMemberDeclaration> mems =  new ListBuffer<JFXMemberDeclaration>();

        JFXAttributeDeclaration attributeDefinition17 = null;

        JFXFunctionMemberDeclaration functionDefinition18 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:345:2: ( ( attributeDefinition | functionDefinition )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:345:3: ( attributeDefinition | functionDefinition )*
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:345:3: ( attributeDefinition | functionDefinition )*
            loop8:
            do {
                int alt8=3;
                switch ( input.LA(1) ) {
                case ABSTRACT:
                    {
                    switch ( input.LA(2) ) {
                    case PUBLIC:
                        {
                        int LA8_9 = input.LA(3);

                        if ( (LA8_9==ATTRIBUTE) ) {
                            alt8=1;
                        }
                        else if ( (LA8_9==FUNCTION) ) {
                            alt8=2;
                        }


                        }
                        break;
                    case PRIVATE:
                        {
                        int LA8_10 = input.LA(3);

                        if ( (LA8_10==FUNCTION) ) {
                            alt8=2;
                        }
                        else if ( (LA8_10==ATTRIBUTE) ) {
                            alt8=1;
                        }


                        }
                        break;
                    case PROTECTED:
                        {
                        int LA8_11 = input.LA(3);

                        if ( (LA8_11==ATTRIBUTE) ) {
                            alt8=1;
                        }
                        else if ( (LA8_11==FUNCTION) ) {
                            alt8=2;
                        }


                        }
                        break;
                    case FUNCTION:
                        {
                        alt8=2;
                        }
                        break;
                    case ATTRIBUTE:
                        {
                        alt8=1;
                        }
                        break;

                    }

                    }
                    break;
                case READONLY:
                    {
                    switch ( input.LA(2) ) {
                    case PUBLIC:
                        {
                        int LA8_9 = input.LA(3);

                        if ( (LA8_9==ATTRIBUTE) ) {
                            alt8=1;
                        }
                        else if ( (LA8_9==FUNCTION) ) {
                            alt8=2;
                        }


                        }
                        break;
                    case PRIVATE:
                        {
                        int LA8_10 = input.LA(3);

                        if ( (LA8_10==FUNCTION) ) {
                            alt8=2;
                        }
                        else if ( (LA8_10==ATTRIBUTE) ) {
                            alt8=1;
                        }


                        }
                        break;
                    case PROTECTED:
                        {
                        int LA8_11 = input.LA(3);

                        if ( (LA8_11==ATTRIBUTE) ) {
                            alt8=1;
                        }
                        else if ( (LA8_11==FUNCTION) ) {
                            alt8=2;
                        }


                        }
                        break;
                    case ATTRIBUTE:
                        {
                        alt8=1;
                        }
                        break;
                    case FUNCTION:
                        {
                        alt8=2;
                        }
                        break;

                    }

                    }
                    break;
                case PUBLIC:
                    {
                    switch ( input.LA(2) ) {
                    case ABSTRACT:
                        {
                        int LA8_12 = input.LA(3);

                        if ( (LA8_12==FUNCTION) ) {
                            alt8=2;
                        }
                        else if ( (LA8_12==ATTRIBUTE) ) {
                            alt8=1;
                        }


                        }
                        break;
                    case READONLY:
                        {
                        int LA8_13 = input.LA(3);

                        if ( (LA8_13==ATTRIBUTE) ) {
                            alt8=1;
                        }
                        else if ( (LA8_13==FUNCTION) ) {
                            alt8=2;
                        }


                        }
                        break;
                    case ATTRIBUTE:
                        {
                        alt8=1;
                        }
                        break;
                    case FUNCTION:
                        {
                        alt8=2;
                        }
                        break;

                    }

                    }
                    break;
                case PRIVATE:
                    {
                    switch ( input.LA(2) ) {
                    case ABSTRACT:
                        {
                        int LA8_12 = input.LA(3);

                        if ( (LA8_12==FUNCTION) ) {
                            alt8=2;
                        }
                        else if ( (LA8_12==ATTRIBUTE) ) {
                            alt8=1;
                        }


                        }
                        break;
                    case READONLY:
                        {
                        int LA8_13 = input.LA(3);

                        if ( (LA8_13==ATTRIBUTE) ) {
                            alt8=1;
                        }
                        else if ( (LA8_13==FUNCTION) ) {
                            alt8=2;
                        }


                        }
                        break;
                    case ATTRIBUTE:
                        {
                        alt8=1;
                        }
                        break;
                    case FUNCTION:
                        {
                        alt8=2;
                        }
                        break;

                    }

                    }
                    break;
                case PROTECTED:
                    {
                    switch ( input.LA(2) ) {
                    case ABSTRACT:
                        {
                        int LA8_12 = input.LA(3);

                        if ( (LA8_12==FUNCTION) ) {
                            alt8=2;
                        }
                        else if ( (LA8_12==ATTRIBUTE) ) {
                            alt8=1;
                        }


                        }
                        break;
                    case READONLY:
                        {
                        int LA8_13 = input.LA(3);

                        if ( (LA8_13==ATTRIBUTE) ) {
                            alt8=1;
                        }
                        else if ( (LA8_13==FUNCTION) ) {
                            alt8=2;
                        }


                        }
                        break;
                    case FUNCTION:
                        {
                        alt8=2;
                        }
                        break;
                    case ATTRIBUTE:
                        {
                        alt8=1;
                        }
                        break;

                    }

                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt8=1;
                    }
                    break;
                case FUNCTION:
                    {
                    alt8=2;
                    }
                    break;

                }

                switch (alt8) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:345:5: attributeDefinition
            	    {
            	    pushFollow(FOLLOW_attributeDefinition_in_classMembers2401);
            	    attributeDefinition17=attributeDefinition();
            	    _fsp--;

            	     mems.append(attributeDefinition17); 

            	    }
            	    break;
            	case 2 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:346:5: functionDefinition
            	    {
            	    pushFollow(FOLLOW_functionDefinition_in_classMembers2419);
            	    functionDefinition18=functionDefinition();
            	    _fsp--;

            	     mems.append(functionDefinition18); 

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return mems;
    }
    // $ANTLR end classMembers


    // $ANTLR start attributeDefinition
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:348:1: attributeDefinition returns [JFXAttributeDeclaration def] : modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? SEMI ;
    public final JFXAttributeDeclaration attributeDefinition() throws RecognitionException {
        JFXAttributeDeclaration def = null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:349:2: ( modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:349:4: modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_attributeDefinition2448);
            modifierFlags();
            _fsp--;

            match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeDefinition2450); 
            pushFollow(FOLLOW_name_in_attributeDefinition2452);
            name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_attributeDefinition2454);
            typeReference();
            _fsp--;

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:349:47: ( EQ bindOpt expression | inverseClause )?
            int alt9=3;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==EQ) ) {
                alt9=1;
            }
            else if ( (LA9_0==INVERSE) ) {
                alt9=2;
            }
            switch (alt9) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:349:48: EQ bindOpt expression
                    {
                    match(input,EQ,FOLLOW_EQ_in_attributeDefinition2457); 
                    pushFollow(FOLLOW_bindOpt_in_attributeDefinition2459);
                    bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_attributeDefinition2462);
                    expression();
                    _fsp--;


                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:349:73: inverseClause
                    {
                    pushFollow(FOLLOW_inverseClause_in_attributeDefinition2466);
                    inverseClause();
                    _fsp--;


                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_attributeDefinition2471); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return def;
    }
    // $ANTLR end attributeDefinition


    // $ANTLR start inverseClause
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:351:1: inverseClause returns [JFXMemberSelector inverse = null] : INVERSE memberSelector ;
    public final JFXMemberSelector inverseClause() throws RecognitionException {
        JFXMemberSelector inverse =  null;

        JFXMemberSelector memberSelector19 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:352:2: ( INVERSE memberSelector )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:352:4: INVERSE memberSelector
            {
            match(input,INVERSE,FOLLOW_INVERSE_in_inverseClause2493); 
            pushFollow(FOLLOW_memberSelector_in_inverseClause2495);
            memberSelector19=memberSelector();
            _fsp--;

             inverse = memberSelector19; 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return inverse;
    }
    // $ANTLR end inverseClause


    // $ANTLR start functionDefinition
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:353:1: functionDefinition returns [JFXFunctionMemberDeclaration def] : modifierFlags FUNCTION name formalParameters typeReference block ;
    public final JFXFunctionMemberDeclaration functionDefinition() throws RecognitionException {
        JFXFunctionMemberDeclaration def = null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:354:2: ( modifierFlags FUNCTION name formalParameters typeReference block )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:354:4: modifierFlags FUNCTION name formalParameters typeReference block
            {
            pushFollow(FOLLOW_modifierFlags_in_functionDefinition2513);
            modifierFlags();
            _fsp--;

            match(input,FUNCTION,FOLLOW_FUNCTION_in_functionDefinition2515); 
            pushFollow(FOLLOW_name_in_functionDefinition2519);
            name();
            _fsp--;

            pushFollow(FOLLOW_formalParameters_in_functionDefinition2521);
            formalParameters();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_functionDefinition2525);
            typeReference();
            _fsp--;

            pushFollow(FOLLOW_block_in_functionDefinition2528);
            block();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return def;
    }
    // $ANTLR end functionDefinition


    // $ANTLR start modifierFlags
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:356:1: modifierFlags returns [JCModifiers mods] : (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? ;
    public final JCModifiers modifierFlags() throws RecognitionException {
        JCModifiers mods = null;

        long om1 = 0;

        long am1 = 0;

        long am2 = 0;

        long om2 = 0;


         long flags = 0; 
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:358:2: ( (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:358:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:358:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
            int alt12=3;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ABSTRACT||LA12_0==READONLY) ) {
                alt12=1;
            }
            else if ( ((LA12_0>=PRIVATE && LA12_0<=PUBLIC)) ) {
                alt12=2;
            }
            switch (alt12) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:358:6: om1= otherModifier (am1= accessModifier )?
                    {
                    pushFollow(FOLLOW_otherModifier_in_modifierFlags2552);
                    om1=otherModifier();
                    _fsp--;

                     flags |= om1; 
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:359:3: (am1= accessModifier )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( ((LA10_0>=PRIVATE && LA10_0<=PUBLIC)) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:359:5: am1= accessModifier
                            {
                            pushFollow(FOLLOW_accessModifier_in_modifierFlags2565);
                            am1=accessModifier();
                            _fsp--;

                             flags |= am1; 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:360:6: am2= accessModifier (om2= otherModifier )?
                    {
                    pushFollow(FOLLOW_accessModifier_in_modifierFlags2587);
                    am2=accessModifier();
                    _fsp--;

                     flags |= am2; 
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:361:3: (om2= otherModifier )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==ABSTRACT||LA11_0==READONLY) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:361:5: om2= otherModifier
                            {
                            pushFollow(FOLLOW_otherModifier_in_modifierFlags2600);
                            om2=otherModifier();
                            _fsp--;

                             flags |= om2; 

                            }
                            break;

                    }


                    }
                    break;

            }

             mods = F.Modifiers(flags); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return mods;
    }
    // $ANTLR end modifierFlags


    // $ANTLR start accessModifier
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:364:1: accessModifier returns [long flags = 0] : ( PUBLIC | PRIVATE | PROTECTED ) ;
    public final long accessModifier() throws RecognitionException {
        long flags =  0;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:365:2: ( ( PUBLIC | PRIVATE | PROTECTED ) )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:365:4: ( PUBLIC | PRIVATE | PROTECTED )
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:365:4: ( PUBLIC | PRIVATE | PROTECTED )
            int alt13=3;
            switch ( input.LA(1) ) {
            case PUBLIC:
                {
                alt13=1;
                }
                break;
            case PRIVATE:
                {
                alt13=2;
                }
                break;
            case PROTECTED:
                {
                alt13=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("365:4: ( PUBLIC | PRIVATE | PROTECTED )", 13, 0, input);

                throw nvae;
            }

            switch (alt13) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:365:5: PUBLIC
                    {
                    match(input,PUBLIC,FOLLOW_PUBLIC_in_accessModifier2648); 
                     flags |= Flags.PUBLIC; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:366:5: PRIVATE
                    {
                    match(input,PRIVATE,FOLLOW_PRIVATE_in_accessModifier2668); 
                     flags |= Flags.PUBLIC; 

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:367:5: PROTECTED
                    {
                    match(input,PROTECTED,FOLLOW_PROTECTED_in_accessModifier2687); 
                     flags |= Flags.PROTECTED; 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return flags;
    }
    // $ANTLR end accessModifier


    // $ANTLR start otherModifier
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:368:1: otherModifier returns [long flags = 0] : ( ABSTRACT | READONLY ) ;
    public final long otherModifier() throws RecognitionException {
        long flags =  0;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:369:2: ( ( ABSTRACT | READONLY ) )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:369:4: ( ABSTRACT | READONLY )
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:369:4: ( ABSTRACT | READONLY )
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==ABSTRACT) ) {
                alt14=1;
            }
            else if ( (LA14_0==READONLY) ) {
                alt14=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("369:4: ( ABSTRACT | READONLY )", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:369:5: ABSTRACT
                    {
                    match(input,ABSTRACT,FOLLOW_ABSTRACT_in_otherModifier2714); 
                     flags |= Flags.ABSTRACT; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:370:5: READONLY
                    {
                    match(input,READONLY,FOLLOW_READONLY_in_otherModifier2732); 
                     flags |= Flags.FINAL; 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return flags;
    }
    // $ANTLR end otherModifier


    // $ANTLR start memberSelector
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:371:1: memberSelector returns [JFXMemberSelector value] : name1= name DOT name2= name ;
    public final JFXMemberSelector memberSelector() throws RecognitionException {
        JFXMemberSelector value = null;

        name_return name1 = null;

        name_return name2 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:372:2: (name1= name DOT name2= name )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:372:4: name1= name DOT name2= name
            {
            pushFollow(FOLLOW_name_in_memberSelector2761);
            name1=name();
            _fsp--;

            match(input,DOT,FOLLOW_DOT_in_memberSelector2765); 
            pushFollow(FOLLOW_name_in_memberSelector2771);
            name2=name();
            _fsp--;

             value = F.at(name1.pos).MemberSelector(name1.value, name2.value); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end memberSelector


    // $ANTLR start formalParameters
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:374:1: formalParameters returns [ListBuffer<JCTree> params = new ListBuffer<JCTree>()] : LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN ;
    public final ListBuffer<JCTree> formalParameters() throws RecognitionException {
        ListBuffer<JCTree> params =  new ListBuffer<JCTree>();

        JFXVar fp0 = null;

        JFXVar fpn = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:375:2: ( LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:375:4: LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_formalParameters2789); 
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:375:12: (fp0= formalParameter ( COMMA fpn= formalParameter )* )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==QUOTED_IDENTIFIER||LA16_0==IDENTIFIER) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:375:14: fp0= formalParameter ( COMMA fpn= formalParameter )*
                    {
                    pushFollow(FOLLOW_formalParameter_in_formalParameters2796);
                    fp0=formalParameter();
                    _fsp--;

                     params.append(fp0); 
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:376:12: ( COMMA fpn= formalParameter )*
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( (LA15_0==COMMA) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:376:14: COMMA fpn= formalParameter
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_formalParameters2814); 
                    	    pushFollow(FOLLOW_formalParameter_in_formalParameters2820);
                    	    fpn=formalParameter();
                    	    _fsp--;

                    	     params.append(fpn); 

                    	    }
                    	    break;

                    	default :
                    	    break loop15;
                        }
                    } while (true);


                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_formalParameters2831); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return params;
    }
    // $ANTLR end formalParameters


    // $ANTLR start formalParameter
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:378:1: formalParameter returns [JFXVar var] : name typeReference ;
    public final JFXVar formalParameter() throws RecognitionException {
        JFXVar var = null;

        name_return name20 = null;

        JFXType typeReference21 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:379:2: ( name typeReference )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:379:4: name typeReference
            {
            pushFollow(FOLLOW_name_in_formalParameter2846);
            name20=name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_formalParameter2848);
            typeReference21=typeReference();
            _fsp--;

             var = F.at(name20.pos).Var(name20.value, typeReference21, F.Modifiers(Flags.PARAMETER)); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return var;
    }
    // $ANTLR end formalParameter


    // $ANTLR start block
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:381:1: block returns [JCBlock value] : LBRACE statements RBRACE ;
    public final JCBlock block() throws RecognitionException {
        JCBlock value = null;

        Token LBRACE22=null;
        ListBuffer<JCStatement> statements23 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:382:2: ( LBRACE statements RBRACE )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:382:4: LBRACE statements RBRACE
            {
            LBRACE22=(Token)input.LT(1);
            match(input,LBRACE,FOLLOW_LBRACE_in_block2867); 
            pushFollow(FOLLOW_statements_in_block2871);
            statements23=statements();
            _fsp--;

            match(input,RBRACE,FOLLOW_RBRACE_in_block2875); 
             value = F.at(pos(LBRACE22)).Block(0L, statements23.toList()); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end block


    // $ANTLR start statements
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:384:1: statements returns [ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>()] : ( statement )* ;
    public final ListBuffer<JCStatement> statements() throws RecognitionException {
        ListBuffer<JCStatement> stats =  new ListBuffer<JCStatement>();

        JCStatement statement24 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:385:2: ( ( statement )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:385:4: ( statement )*
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:385:4: ( statement )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( ((LA17_0>=POUND && LA17_0<=TYPEOF)||LA17_0==ABSTRACT||LA17_0==BREAK||(LA17_0>=RETURN && LA17_0<=VAR)||LA17_0==IF||(LA17_0>=THIS && LA17_0<=FALSE)||(LA17_0>=WHILE && LA17_0<=CONTINUE)||LA17_0==TRY||(LA17_0>=NOT && LA17_0<=READONLY)||LA17_0==SUPER||(LA17_0>=SIZEOF && LA17_0<=LPAREN)||(LA17_0>=PLUSPLUS && LA17_0<=SUBSUB)||(LA17_0>=QUES && LA17_0<=STRING_LITERAL)||LA17_0==QUOTE_LBRACE_STRING_LITERAL||(LA17_0>=QUOTED_IDENTIFIER && LA17_0<=INTEGER_LITERAL)||LA17_0==FLOATING_POINT_LITERAL||LA17_0==IDENTIFIER) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:385:5: statement
            	    {
            	    pushFollow(FOLLOW_statement_in_statements2893);
            	    statement24=statement();
            	    _fsp--;

            	     stats.append(statement24); 

            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return stats;
    }
    // $ANTLR end statements


    // $ANTLR start statement
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:387:1: statement returns [JCStatement value] : ( variableDeclaration | functionDefinition | WHILE LPAREN expression RPAREN block | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | tryStatement );
    public final JCStatement statement() throws RecognitionException {
        JCStatement value = null;

        Token WHILE27=null;
        Token SEMI30=null;
        Token BREAK32=null;
        Token CONTINUE33=null;
        JCStatement variableDeclaration25 = null;

        JFXFunctionMemberDeclaration functionDefinition26 = null;

        JCExpression expression28 = null;

        JCBlock block29 = null;

        JCExpression expression31 = null;

        JCStatement throwStatement34 = null;

        JCStatement returnStatement35 = null;

        JCStatement tryStatement36 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:388:2: ( variableDeclaration | functionDefinition | WHILE LPAREN expression RPAREN block | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | tryStatement )
            int alt18=9;
            switch ( input.LA(1) ) {
            case VAR:
                {
                alt18=1;
                }
                break;
            case ABSTRACT:
            case PRIVATE:
            case PROTECTED:
            case PUBLIC:
            case FUNCTION:
            case READONLY:
                {
                alt18=2;
                }
                break;
            case WHILE:
                {
                alt18=3;
                }
                break;
            case POUND:
            case TYPEOF:
            case IF:
            case THIS:
            case NULL:
            case TRUE:
            case FALSE:
            case NOT:
            case NEW:
            case SUPER:
            case SIZEOF:
            case REVERSE:
            case LPAREN:
            case PLUSPLUS:
            case SUB:
            case SUBSUB:
            case QUES:
            case STRING_LITERAL:
            case QUOTE_LBRACE_STRING_LITERAL:
            case QUOTED_IDENTIFIER:
            case INTEGER_LITERAL:
            case FLOATING_POINT_LITERAL:
            case IDENTIFIER:
                {
                alt18=4;
                }
                break;
            case BREAK:
                {
                alt18=5;
                }
                break;
            case CONTINUE:
                {
                alt18=6;
                }
                break;
            case THROW:
                {
                alt18=7;
                }
                break;
            case RETURN:
                {
                alt18=8;
                }
                break;
            case TRY:
                {
                alt18=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("387:1: statement returns [JCStatement value] : ( variableDeclaration | functionDefinition | WHILE LPAREN expression RPAREN block | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | tryStatement );", 18, 0, input);

                throw nvae;
            }

            switch (alt18) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:388:4: variableDeclaration
                    {
                    pushFollow(FOLLOW_variableDeclaration_in_statement2940);
                    variableDeclaration25=variableDeclaration();
                    _fsp--;

                     value = variableDeclaration25; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:389:4: functionDefinition
                    {
                    pushFollow(FOLLOW_functionDefinition_in_statement2950);
                    functionDefinition26=functionDefinition();
                    _fsp--;

                     value = functionDefinition26; 

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:390:11: WHILE LPAREN expression RPAREN block
                    {
                    WHILE27=(Token)input.LT(1);
                    match(input,WHILE,FOLLOW_WHILE_in_statement2966); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_statement2968); 
                    pushFollow(FOLLOW_expression_in_statement2970);
                    expression28=expression();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_statement2972); 
                    pushFollow(FOLLOW_block_in_statement2974);
                    block29=block();
                    _fsp--;

                     value = F.at(pos(WHILE27)).WhileLoop(expression28, block29); 

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:391:4: expression SEMI
                    {
                    pushFollow(FOLLOW_expression_in_statement2981);
                    expression31=expression();
                    _fsp--;

                    SEMI30=(Token)input.LT(1);
                    match(input,SEMI,FOLLOW_SEMI_in_statement2985); 
                     value = F.at(pos(SEMI30)).Exec(expression31); 

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:392:4: BREAK SEMI
                    {
                    BREAK32=(Token)input.LT(1);
                    match(input,BREAK,FOLLOW_BREAK_in_statement2995); 
                    match(input,SEMI,FOLLOW_SEMI_in_statement2999); 
                     value = F.at(pos(BREAK32)).Break(null); 

                    }
                    break;
                case 6 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:393:4: CONTINUE SEMI
                    {
                    CONTINUE33=(Token)input.LT(1);
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_statement3010); 
                    match(input,SEMI,FOLLOW_SEMI_in_statement3014); 
                     value = F.at(pos(CONTINUE33)).Continue(null); 

                    }
                    break;
                case 7 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:394:11: throwStatement
                    {
                    pushFollow(FOLLOW_throwStatement_in_statement3031);
                    throwStatement34=throwStatement();
                    _fsp--;

                     value = throwStatement34; 

                    }
                    break;
                case 8 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:395:11: returnStatement
                    {
                    pushFollow(FOLLOW_returnStatement_in_statement3048);
                    returnStatement35=returnStatement();
                    _fsp--;

                     value = returnStatement35; 

                    }
                    break;
                case 9 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:396:11: tryStatement
                    {
                    pushFollow(FOLLOW_tryStatement_in_statement3065);
                    tryStatement36=tryStatement();
                    _fsp--;

                     value = tryStatement36; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end statement


    // $ANTLR start assertStatement
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:398:1: assertStatement returns [JCStatement value = null] : ASSERT expression ( COLON expression )? SEMI ;
    public final JCStatement assertStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:399:2: ( ASSERT expression ( COLON expression )? SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:399:4: ASSERT expression ( COLON expression )? SEMI
            {
            match(input,ASSERT,FOLLOW_ASSERT_in_assertStatement3093); 
            pushFollow(FOLLOW_expression_in_assertStatement3097);
            expression();
            _fsp--;

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:399:26: ( COLON expression )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==COLON) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:399:30: COLON expression
                    {
                    match(input,COLON,FOLLOW_COLON_in_assertStatement3105); 
                    pushFollow(FOLLOW_expression_in_assertStatement3109);
                    expression();
                    _fsp--;


                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_assertStatement3119); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end assertStatement


    // $ANTLR start variableDeclaration
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:400:1: variableDeclaration returns [JCStatement value] : VAR name typeReference ( EQ bindOpt expression SEMI | SEMI ) ;
    public final JCStatement variableDeclaration() throws RecognitionException {
        JCStatement value = null;

        Token VAR37=null;
        name_return name38 = null;

        JFXType typeReference39 = null;

        JCExpression expression40 = null;

        JavafxBindStatus bindOpt41 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:401:2: ( VAR name typeReference ( EQ bindOpt expression SEMI | SEMI ) )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:401:4: VAR name typeReference ( EQ bindOpt expression SEMI | SEMI )
            {
            VAR37=(Token)input.LT(1);
            match(input,VAR,FOLLOW_VAR_in_variableDeclaration3134); 
            pushFollow(FOLLOW_name_in_variableDeclaration3137);
            name38=name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_variableDeclaration3140);
            typeReference39=typeReference();
            _fsp--;

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:402:6: ( EQ bindOpt expression SEMI | SEMI )
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==EQ) ) {
                alt20=1;
            }
            else if ( (LA20_0==SEMI) ) {
                alt20=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("402:6: ( EQ bindOpt expression SEMI | SEMI )", 20, 0, input);

                throw nvae;
            }
            switch (alt20) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:402:8: EQ bindOpt expression SEMI
                    {
                    match(input,EQ,FOLLOW_EQ_in_variableDeclaration3151); 
                    pushFollow(FOLLOW_bindOpt_in_variableDeclaration3153);
                    bindOpt41=bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_variableDeclaration3156);
                    expression40=expression();
                    _fsp--;

                    match(input,SEMI,FOLLOW_SEMI_in_variableDeclaration3158); 
                     value = F.at(pos(VAR37)).VarInit(name38.value, typeReference39, 
                    	    							expression40, bindOpt41); 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:404:8: SEMI
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_variableDeclaration3169); 
                     value = F.at(pos(VAR37)).VarStatement(name38.value, typeReference39); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end variableDeclaration


    // $ANTLR start bindOpt
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:407:1: bindOpt returns [JavafxBindStatus status = UNBOUND] : ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? ;
    public final JavafxBindStatus bindOpt() throws RecognitionException {
        JavafxBindStatus status =  UNBOUND;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:408:2: ( ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:408:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:408:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
            int alt24=4;
            switch ( input.LA(1) ) {
                case BIND:
                    {
                    alt24=1;
                    }
                    break;
                case STAYS:
                    {
                    alt24=2;
                    }
                    break;
                case TIE:
                    {
                    alt24=3;
                    }
                    break;
            }

            switch (alt24) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:408:6: BIND ( LAZY )?
                    {
                    match(input,BIND,FOLLOW_BIND_in_bindOpt3206); 
                     status = UNIDIBIND; 
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:409:8: ( LAZY )?
                    int alt21=2;
                    int LA21_0 = input.LA(1);

                    if ( (LA21_0==LAZY) ) {
                        alt21=1;
                    }
                    switch (alt21) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:409:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3222); 
                             status = LAZY_UNIDIBIND; 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:410:6: STAYS ( LAZY )?
                    {
                    match(input,STAYS,FOLLOW_STAYS_in_bindOpt3237); 
                     status = UNIDIBIND; 
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:411:8: ( LAZY )?
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( (LA22_0==LAZY) ) {
                        alt22=1;
                    }
                    switch (alt22) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:411:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3253); 
                             status = LAZY_UNIDIBIND; 

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:412:6: TIE ( LAZY )?
                    {
                    match(input,TIE,FOLLOW_TIE_in_bindOpt3268); 
                     status = BIDIBIND; 
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:413:8: ( LAZY )?
                    int alt23=2;
                    int LA23_0 = input.LA(1);

                    if ( (LA23_0==LAZY) ) {
                        alt23=1;
                    }
                    switch (alt23) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:413:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3284); 
                             status = LAZY_BIDIBIND; 

                            }
                            break;

                    }


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return status;
    }
    // $ANTLR end bindOpt


    // $ANTLR start throwStatement
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:415:1: throwStatement returns [JCStatement value] : THROW expression SEMI ;
    public final JCStatement throwStatement() throws RecognitionException {
        JCStatement value = null;

        Token THROW42=null;
        JCExpression expression43 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:416:2: ( THROW expression SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:416:4: THROW expression SEMI
            {
            THROW42=(Token)input.LT(1);
            match(input,THROW,FOLLOW_THROW_in_throwStatement3313); 
            pushFollow(FOLLOW_expression_in_throwStatement3317);
            expression43=expression();
            _fsp--;

            match(input,SEMI,FOLLOW_SEMI_in_throwStatement3321); 
             value = F.at(pos(THROW42)).Throw(expression43); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end throwStatement


    // $ANTLR start returnStatement
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:418:1: returnStatement returns [JCStatement value] : RETURN ( expression )? SEMI ;
    public final JCStatement returnStatement() throws RecognitionException {
        JCStatement value = null;

        Token RETURN45=null;
        JCExpression expression44 = null;


         JCExpression expr = null; 
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:420:2: ( RETURN ( expression )? SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:420:4: RETURN ( expression )? SEMI
            {
            RETURN45=(Token)input.LT(1);
            match(input,RETURN,FOLLOW_RETURN_in_returnStatement3347); 
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:420:11: ( expression )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( ((LA25_0>=POUND && LA25_0<=TYPEOF)||LA25_0==IF||(LA25_0>=THIS && LA25_0<=FALSE)||(LA25_0>=NOT && LA25_0<=NEW)||LA25_0==SUPER||(LA25_0>=SIZEOF && LA25_0<=LPAREN)||(LA25_0>=PLUSPLUS && LA25_0<=SUBSUB)||(LA25_0>=QUES && LA25_0<=STRING_LITERAL)||LA25_0==QUOTE_LBRACE_STRING_LITERAL||(LA25_0>=QUOTED_IDENTIFIER && LA25_0<=INTEGER_LITERAL)||LA25_0==FLOATING_POINT_LITERAL||LA25_0==IDENTIFIER) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:420:12: expression
                    {
                    pushFollow(FOLLOW_expression_in_returnStatement3350);
                    expression44=expression();
                    _fsp--;

                     expr = expression44; 

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_returnStatement3366); 
             value = F.at(pos(RETURN45)).Return(expr); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end returnStatement


    // $ANTLR start tryStatement
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:423:1: tryStatement returns [JCStatement value] : TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? ) ;
    public final JCStatement tryStatement() throws RecognitionException {
        JCStatement value = null;

        Token TRY47=null;
        JCBlock tb = null;

        JCBlock fb1 = null;

        JCBlock fb2 = null;

        JCCatch catchClause46 = null;


        	JCBlock body;
        		ListBuffer<JCCatch> catchers = new ListBuffer<JCCatch>();
        		JCBlock finalBlock = null;
        	
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:428:2: ( TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? ) )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:428:4: TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )
            {
            TRY47=(Token)input.LT(1);
            match(input,TRY,FOLLOW_TRY_in_tryStatement3394); 
            pushFollow(FOLLOW_block_in_tryStatement3400);
            tb=block();
            _fsp--;

             body = tb; 
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:429:5: ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==FINALLY) ) {
                alt28=1;
            }
            else if ( (LA28_0==CATCH) ) {
                alt28=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("429:5: ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )", 28, 0, input);

                throw nvae;
            }
            switch (alt28) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:429:7: FINALLY fb1= block
                    {
                    match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement3413); 
                    pushFollow(FOLLOW_block_in_tryStatement3419);
                    fb1=block();
                    _fsp--;

                     finalBlock = fb1; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:430:10: ( catchClause )+ ( FINALLY fb2= block )?
                    {
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:430:10: ( catchClause )+
                    int cnt26=0;
                    loop26:
                    do {
                        int alt26=2;
                        int LA26_0 = input.LA(1);

                        if ( (LA26_0==CATCH) ) {
                            alt26=1;
                        }


                        switch (alt26) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:430:11: catchClause
                    	    {
                    	    pushFollow(FOLLOW_catchClause_in_tryStatement3435);
                    	    catchClause46=catchClause();
                    	    _fsp--;

                    	     catchers.append(catchClause46); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt26 >= 1 ) break loop26;
                                EarlyExitException eee =
                                    new EarlyExitException(26, input);
                                throw eee;
                        }
                        cnt26++;
                    } while (true);

                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:431:10: ( FINALLY fb2= block )?
                    int alt27=2;
                    int LA27_0 = input.LA(1);

                    if ( (LA27_0==FINALLY) ) {
                        alt27=1;
                    }
                    switch (alt27) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:431:11: FINALLY fb2= block
                            {
                            match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement3458); 
                            pushFollow(FOLLOW_block_in_tryStatement3463);
                            fb2=block();
                            _fsp--;

                             finalBlock = fb2; 

                            }
                            break;

                    }


                    }
                    break;

            }

             value = F.at(pos(TRY47)).Try(body, catchers.toList(), finalBlock); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end tryStatement


    // $ANTLR start catchClause
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:434:1: catchClause returns [JCCatch value] : CATCH LPAREN name ( COLON identifier )? RPAREN block ;
    public final JCCatch catchClause() throws RecognitionException {
        JCCatch value = null;

        Token CATCH50=null;
        name_return name48 = null;

        JCIdent identifier49 = null;

        JCBlock block51 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:435:2: ( CATCH LPAREN name ( COLON identifier )? RPAREN block )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:435:4: CATCH LPAREN name ( COLON identifier )? RPAREN block
            {
            CATCH50=(Token)input.LT(1);
            match(input,CATCH,FOLLOW_CATCH_in_catchClause3502); 
            match(input,LPAREN,FOLLOW_LPAREN_in_catchClause3505); 
            pushFollow(FOLLOW_name_in_catchClause3508);
            name48=name();
            _fsp--;

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:436:4: ( COLON identifier )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==COLON) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:436:5: COLON identifier
                    {
                    match(input,COLON,FOLLOW_COLON_in_catchClause3516); 
                    pushFollow(FOLLOW_identifier_in_catchClause3518);
                    identifier49=identifier();
                    _fsp--;


                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_catchClause3527); 
            pushFollow(FOLLOW_block_in_catchClause3531);
            block51=block();
            _fsp--;

             JCModifiers mods = F.at(name48.pos).Modifiers(Flags.PARAMETER);
            	  					  JCVariableDecl formal = F.at(name48.pos).VarDef(mods, name48.value, identifier49, null);
            	  					  value = F.at(pos(CATCH50)).Catch(formal, block51); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end catchClause


    // $ANTLR start expression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:441:1: expression returns [JCExpression expr] : ( ifExpression | suffixedExpression );
    public final JCExpression expression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression ifExpression52 = null;

        JCExpression suffixedExpression53 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:442:9: ( ifExpression | suffixedExpression )
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==IF) ) {
                alt30=1;
            }
            else if ( ((LA30_0>=POUND && LA30_0<=TYPEOF)||(LA30_0>=THIS && LA30_0<=FALSE)||(LA30_0>=NOT && LA30_0<=NEW)||LA30_0==SUPER||(LA30_0>=SIZEOF && LA30_0<=LPAREN)||(LA30_0>=PLUSPLUS && LA30_0<=SUBSUB)||(LA30_0>=QUES && LA30_0<=STRING_LITERAL)||LA30_0==QUOTE_LBRACE_STRING_LITERAL||(LA30_0>=QUOTED_IDENTIFIER && LA30_0<=INTEGER_LITERAL)||LA30_0==FLOATING_POINT_LITERAL||LA30_0==IDENTIFIER) ) {
                alt30=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("441:1: expression returns [JCExpression expr] : ( ifExpression | suffixedExpression );", 30, 0, input);

                throw nvae;
            }
            switch (alt30) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:442:11: ifExpression
                    {
                    pushFollow(FOLLOW_ifExpression_in_expression3559);
                    ifExpression52=ifExpression();
                    _fsp--;

                     expr = ifExpression52; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:443:11: suffixedExpression
                    {
                    pushFollow(FOLLOW_suffixedExpression_in_expression3582);
                    suffixedExpression53=suffixedExpression();
                    _fsp--;

                     expr = suffixedExpression53; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end expression


    // $ANTLR start ifExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:445:1: ifExpression returns [JCExpression expr] : IF econd= expression THEN ethen= expression ELSE eelse= expression ;
    public final JCExpression ifExpression() throws RecognitionException {
        JCExpression expr = null;

        Token IF54=null;
        JCExpression econd = null;

        JCExpression ethen = null;

        JCExpression eelse = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:446:2: ( IF econd= expression THEN ethen= expression ELSE eelse= expression )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:446:4: IF econd= expression THEN ethen= expression ELSE eelse= expression
            {
            IF54=(Token)input.LT(1);
            match(input,IF,FOLLOW_IF_in_ifExpression3613); 
            pushFollow(FOLLOW_expression_in_ifExpression3617);
            econd=expression();
            _fsp--;

            match(input,THEN,FOLLOW_THEN_in_ifExpression3621); 
            pushFollow(FOLLOW_expression_in_ifExpression3626);
            ethen=expression();
            _fsp--;

            match(input,ELSE,FOLLOW_ELSE_in_ifExpression3634); 
            pushFollow(FOLLOW_expression_in_ifExpression3639);
            eelse=expression();
            _fsp--;

             expr = F.at(pos(IF54)).Conditional(econd, ethen, eelse); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end ifExpression


    // $ANTLR start suffixedExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:449:1: suffixedExpression returns [JCExpression expr] : e1= assignmentExpression ( PLUSPLUS | SUBSUB )? ;
    public final JCExpression suffixedExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:450:2: (e1= assignmentExpression ( PLUSPLUS | SUBSUB )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:450:4: e1= assignmentExpression ( PLUSPLUS | SUBSUB )?
            {
            pushFollow(FOLLOW_assignmentExpression_in_suffixedExpression3662);
            e1=assignmentExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:451:5: ( PLUSPLUS | SUBSUB )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==PLUSPLUS||LA31_0==SUBSUB) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:
                    {
                    if ( input.LA(1)==PLUSPLUS||input.LA(1)==SUBSUB ) {
                        input.consume();
                        errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse =
                            new MismatchedSetException(null,input);
                        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_suffixedExpression3673);    throw mse;
                    }


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end suffixedExpression


    // $ANTLR start assignmentExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:453:1: assignmentExpression returns [JCExpression expr] : e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? ;
    public final JCExpression assignmentExpression() throws RecognitionException {
        JCExpression expr = null;

        Token EQ55=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:454:2: (e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:454:4: e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )?
            {
            pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression3702);
            e1=assignmentOpExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:455:5: ( EQ e2= assignmentOpExpression )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==EQ) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:455:9: EQ e2= assignmentOpExpression
                    {
                    EQ55=(Token)input.LT(1);
                    match(input,EQ,FOLLOW_EQ_in_assignmentExpression3717); 
                    pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression3723);
                    e2=assignmentOpExpression();
                    _fsp--;

                     expr = F.at(pos(EQ55)).Assign(expr, e2); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end assignmentExpression


    // $ANTLR start assignmentOpExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:456:1: assignmentOpExpression returns [JCExpression expr] : e1= andExpression ( assignmentOperator e2= andExpression )? ;
    public final JCExpression assignmentOpExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;

        JCExpression e2 = null;

        int assignmentOperator56 = 0;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:457:2: (e1= andExpression ( assignmentOperator e2= andExpression )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:457:4: e1= andExpression ( assignmentOperator e2= andExpression )?
            {
            pushFollow(FOLLOW_andExpression_in_assignmentOpExpression3750);
            e1=andExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:458:5: ( assignmentOperator e2= andExpression )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( ((LA33_0>=PLUSEQ && LA33_0<=PERCENTEQ)) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:458:9: assignmentOperator e2= andExpression
                    {
                    pushFollow(FOLLOW_assignmentOperator_in_assignmentOpExpression3766);
                    assignmentOperator56=assignmentOperator();
                    _fsp--;

                    pushFollow(FOLLOW_andExpression_in_assignmentOpExpression3772);
                    e2=andExpression();
                    _fsp--;

                     expr = F.Assignop(assignmentOperator56,
                    	   													expr, e2); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end assignmentOpExpression


    // $ANTLR start andExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:460:1: andExpression returns [JCExpression expr] : e1= orExpression ( AND e2= orExpression )* ;
    public final JCExpression andExpression() throws RecognitionException {
        JCExpression expr = null;

        Token AND57=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:461:2: (e1= orExpression ( AND e2= orExpression )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:461:4: e1= orExpression ( AND e2= orExpression )*
            {
            pushFollow(FOLLOW_orExpression_in_andExpression3798);
            e1=orExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:462:5: ( AND e2= orExpression )*
            loop34:
            do {
                int alt34=2;
                int LA34_0 = input.LA(1);

                if ( (LA34_0==AND) ) {
                    alt34=1;
                }


                switch (alt34) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:462:9: AND e2= orExpression
            	    {
            	    AND57=(Token)input.LT(1);
            	    match(input,AND,FOLLOW_AND_in_andExpression3814); 
            	    pushFollow(FOLLOW_orExpression_in_andExpression3820);
            	    e2=orExpression();
            	    _fsp--;

            	     expr = F.at(pos(AND57)).Binary(JCTree.AND, expr, e2); 

            	    }
            	    break;

            	default :
            	    break loop34;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end andExpression


    // $ANTLR start orExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:463:1: orExpression returns [JCExpression expr] : e1= instanceOfExpression ( OR e2= instanceOfExpression )* ;
    public final JCExpression orExpression() throws RecognitionException {
        JCExpression expr = null;

        Token OR58=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:464:2: (e1= instanceOfExpression ( OR e2= instanceOfExpression )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:464:4: e1= instanceOfExpression ( OR e2= instanceOfExpression )*
            {
            pushFollow(FOLLOW_instanceOfExpression_in_orExpression3848);
            e1=instanceOfExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:465:5: ( OR e2= instanceOfExpression )*
            loop35:
            do {
                int alt35=2;
                int LA35_0 = input.LA(1);

                if ( (LA35_0==OR) ) {
                    alt35=1;
                }


                switch (alt35) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:465:9: OR e2= instanceOfExpression
            	    {
            	    OR58=(Token)input.LT(1);
            	    match(input,OR,FOLLOW_OR_in_orExpression3863); 
            	    pushFollow(FOLLOW_instanceOfExpression_in_orExpression3869);
            	    e2=instanceOfExpression();
            	    _fsp--;

            	     expr = F.at(pos(OR58)).Binary(JCTree.OR, expr, e2); 

            	    }
            	    break;

            	default :
            	    break loop35;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end orExpression


    // $ANTLR start instanceOfExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:466:1: instanceOfExpression returns [JCExpression expr] : e1= relationalExpression ( INSTANCEOF identifier )? ;
    public final JCExpression instanceOfExpression() throws RecognitionException {
        JCExpression expr = null;

        Token INSTANCEOF59=null;
        JCExpression e1 = null;

        JCIdent identifier60 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:467:2: (e1= relationalExpression ( INSTANCEOF identifier )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:467:4: e1= relationalExpression ( INSTANCEOF identifier )?
            {
            pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression3897);
            e1=relationalExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:468:5: ( INSTANCEOF identifier )?
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==INSTANCEOF) ) {
                alt36=1;
            }
            switch (alt36) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:468:9: INSTANCEOF identifier
                    {
                    INSTANCEOF59=(Token)input.LT(1);
                    match(input,INSTANCEOF,FOLLOW_INSTANCEOF_in_instanceOfExpression3912); 
                    pushFollow(FOLLOW_identifier_in_instanceOfExpression3914);
                    identifier60=identifier();
                    _fsp--;

                     expr = F.at(pos(INSTANCEOF59)).Binary(JCTree.TYPETEST, expr, 
                    	   													 identifier60); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end instanceOfExpression


    // $ANTLR start relationalExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:470:1: relationalExpression returns [JCExpression expr] : e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* ;
    public final JCExpression relationalExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LTGT61=null;
        Token EQEQ62=null;
        Token LTEQ63=null;
        Token GTEQ64=null;
        Token LT65=null;
        Token GT66=null;
        Token IN67=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:471:2: (e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:471:4: e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            {
            pushFollow(FOLLOW_additiveExpression_in_relationalExpression3942);
            e1=additiveExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:472:5: ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            loop37:
            do {
                int alt37=8;
                switch ( input.LA(1) ) {
                case LTGT:
                    {
                    alt37=1;
                    }
                    break;
                case EQEQ:
                    {
                    alt37=2;
                    }
                    break;
                case LTEQ:
                    {
                    alt37=3;
                    }
                    break;
                case GTEQ:
                    {
                    alt37=4;
                    }
                    break;
                case LT:
                    {
                    alt37=5;
                    }
                    break;
                case GT:
                    {
                    alt37=6;
                    }
                    break;
                case IN:
                    {
                    alt37=7;
                    }
                    break;

                }

                switch (alt37) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:472:9: LTGT e= additiveExpression
            	    {
            	    LTGT61=(Token)input.LT(1);
            	    match(input,LTGT,FOLLOW_LTGT_in_relationalExpression3958); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression3964);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LTGT61)).Binary(JCTree.NE, expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:473:9: EQEQ e= additiveExpression
            	    {
            	    EQEQ62=(Token)input.LT(1);
            	    match(input,EQEQ,FOLLOW_EQEQ_in_relationalExpression3978); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression3984);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(EQEQ62)).Binary(JCTree.EQ, expr, e); 

            	    }
            	    break;
            	case 3 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:474:9: LTEQ e= additiveExpression
            	    {
            	    LTEQ63=(Token)input.LT(1);
            	    match(input,LTEQ,FOLLOW_LTEQ_in_relationalExpression3998); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4004);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LTEQ63)).Binary(JCTree.LE, expr, e); 

            	    }
            	    break;
            	case 4 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:475:9: GTEQ e= additiveExpression
            	    {
            	    GTEQ64=(Token)input.LT(1);
            	    match(input,GTEQ,FOLLOW_GTEQ_in_relationalExpression4018); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4024);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(GTEQ64)).Binary(JCTree.GE, expr, e); 

            	    }
            	    break;
            	case 5 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:476:9: LT e= additiveExpression
            	    {
            	    LT65=(Token)input.LT(1);
            	    match(input,LT,FOLLOW_LT_in_relationalExpression4038); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4046);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LT65))  .Binary(JCTree.LT, expr, e); 

            	    }
            	    break;
            	case 6 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:477:9: GT e= additiveExpression
            	    {
            	    GT66=(Token)input.LT(1);
            	    match(input,GT,FOLLOW_GT_in_relationalExpression4060); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4068);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(GT66))  .Binary(JCTree.GT, expr, e); 

            	    }
            	    break;
            	case 7 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:478:9: IN e= additiveExpression
            	    {
            	    IN67=(Token)input.LT(1);
            	    match(input,IN,FOLLOW_IN_in_relationalExpression4082); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4090);
            	    e=additiveExpression();
            	    _fsp--;

            	     /* expr = F.at(pos(IN67  )).Binary(JavaFXTag.IN, expr, $e2.expr); */ 

            	    }
            	    break;

            	default :
            	    break loop37;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end relationalExpression


    // $ANTLR start additiveExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:480:1: additiveExpression returns [JCExpression expr] : e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* ;
    public final JCExpression additiveExpression() throws RecognitionException {
        JCExpression expr = null;

        Token PLUS68=null;
        Token SUB69=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:481:2: (e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:481:4: e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            {
            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4119);
            e1=multiplicativeExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:482:5: ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            loop38:
            do {
                int alt38=3;
                int LA38_0 = input.LA(1);

                if ( (LA38_0==PLUS) ) {
                    alt38=1;
                }
                else if ( (LA38_0==SUB) ) {
                    alt38=2;
                }


                switch (alt38) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:482:9: PLUS e= multiplicativeExpression
            	    {
            	    PLUS68=(Token)input.LT(1);
            	    match(input,PLUS,FOLLOW_PLUS_in_additiveExpression4134); 
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4140);
            	    e=multiplicativeExpression();
            	    _fsp--;

            	     expr = F.at(pos(PLUS68)).Binary(JCTree.PLUS , expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:483:9: SUB e= multiplicativeExpression
            	    {
            	    SUB69=(Token)input.LT(1);
            	    match(input,SUB,FOLLOW_SUB_in_additiveExpression4153); 
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4160);
            	    e=multiplicativeExpression();
            	    _fsp--;

            	     expr = F.at(pos(SUB69)) .Binary(JCTree.MINUS, expr, e); 

            	    }
            	    break;

            	default :
            	    break loop38;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end additiveExpression


    // $ANTLR start multiplicativeExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:485:1: multiplicativeExpression returns [JCExpression expr] : e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* ;
    public final JCExpression multiplicativeExpression() throws RecognitionException {
        JCExpression expr = null;

        Token STAR70=null;
        Token SLASH71=null;
        Token PERCENT72=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:486:2: (e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:486:4: e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            {
            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4188);
            e1=unaryExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:487:5: ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            loop39:
            do {
                int alt39=4;
                switch ( input.LA(1) ) {
                case STAR:
                    {
                    alt39=1;
                    }
                    break;
                case SLASH:
                    {
                    alt39=2;
                    }
                    break;
                case PERCENT:
                    {
                    alt39=3;
                    }
                    break;

                }

                switch (alt39) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:487:9: STAR e= unaryExpression
            	    {
            	    STAR70=(Token)input.LT(1);
            	    match(input,STAR,FOLLOW_STAR_in_multiplicativeExpression4204); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4211);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(STAR70))   .Binary(JCTree.MUL  , expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:488:9: SLASH e= unaryExpression
            	    {
            	    SLASH71=(Token)input.LT(1);
            	    match(input,SLASH,FOLLOW_SLASH_in_multiplicativeExpression4225); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4231);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(SLASH71))  .Binary(JCTree.DIV  , expr, e); 

            	    }
            	    break;
            	case 3 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:489:9: PERCENT e= unaryExpression
            	    {
            	    PERCENT72=(Token)input.LT(1);
            	    match(input,PERCENT,FOLLOW_PERCENT_in_multiplicativeExpression4245); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4249);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(PERCENT72)).Binary(JCTree.MOD  , expr, e); 

            	    }
            	    break;

            	default :
            	    break loop39;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end multiplicativeExpression


    // $ANTLR start unaryExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:491:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );
    public final JCExpression unaryExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression postfixExpression73 = null;

        int unaryOperator74 = 0;

        JCExpression postfixExpression75 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:492:2: ( postfixExpression | unaryOperator postfixExpression )
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( ((LA40_0>=THIS && LA40_0<=FALSE)||LA40_0==NEW||LA40_0==SUPER||LA40_0==LPAREN||LA40_0==STRING_LITERAL||LA40_0==QUOTE_LBRACE_STRING_LITERAL||(LA40_0>=QUOTED_IDENTIFIER && LA40_0<=INTEGER_LITERAL)||LA40_0==FLOATING_POINT_LITERAL||LA40_0==IDENTIFIER) ) {
                alt40=1;
            }
            else if ( ((LA40_0>=POUND && LA40_0<=TYPEOF)||LA40_0==NOT||(LA40_0>=SIZEOF && LA40_0<=REVERSE)||(LA40_0>=PLUSPLUS && LA40_0<=SUBSUB)||LA40_0==QUES) ) {
                alt40=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("491:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );", 40, 0, input);

                throw nvae;
            }
            switch (alt40) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:492:4: postfixExpression
                    {
                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression4279);
                    postfixExpression73=postfixExpression();
                    _fsp--;

                     expr = postfixExpression73; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:493:4: unaryOperator postfixExpression
                    {
                    pushFollow(FOLLOW_unaryOperator_in_unaryExpression4290);
                    unaryOperator74=unaryOperator();
                    _fsp--;

                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression4294);
                    postfixExpression75=postfixExpression();
                    _fsp--;

                     expr = F.Unary(unaryOperator74, postfixExpression75); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end unaryExpression


    // $ANTLR start postfixExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:495:1: postfixExpression returns [JCExpression expr] : primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* ;
    public final JCExpression postfixExpression() throws RecognitionException {
        JCExpression expr = null;

        Token DOT77=null;
        Token LPAREN78=null;
        name_return name1 = null;

        JCExpression primaryExpression76 = null;

        ListBuffer<JCExpression> expressionListOpt79 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:496:2: ( primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:496:4: primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            {
            pushFollow(FOLLOW_primaryExpression_in_postfixExpression4314);
            primaryExpression76=primaryExpression();
            _fsp--;

             expr = primaryExpression76; 
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:497:5: ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            loop44:
            do {
                int alt44=3;
                int LA44_0 = input.LA(1);

                if ( (LA44_0==DOT) ) {
                    alt44=1;
                }
                else if ( (LA44_0==LBRACKET) ) {
                    alt44=2;
                }


                switch (alt44) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:497:7: DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    {
            	    DOT77=(Token)input.LT(1);
            	    match(input,DOT,FOLLOW_DOT_in_postfixExpression4329); 
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:497:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    int alt42=2;
            	    int LA42_0 = input.LA(1);

            	    if ( (LA42_0==CLASS) ) {
            	        alt42=1;
            	    }
            	    else if ( (LA42_0==QUOTED_IDENTIFIER||LA42_0==IDENTIFIER) ) {
            	        alt42=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("497:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )", 42, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt42) {
            	        case 1 :
            	            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:497:13: CLASS
            	            {
            	            match(input,CLASS,FOLLOW_CLASS_in_postfixExpression4333); 

            	            }
            	            break;
            	        case 2 :
            	            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:498:13: name1= name ( LPAREN expressionListOpt RPAREN )*
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression4357);
            	            name1=name();
            	            _fsp--;

            	             expr = F.at(pos(DOT77)).Select(expr, name1.value); 
            	            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:499:14: ( LPAREN expressionListOpt RPAREN )*
            	            loop41:
            	            do {
            	                int alt41=2;
            	                int LA41_0 = input.LA(1);

            	                if ( (LA41_0==LPAREN) ) {
            	                    alt41=1;
            	                }


            	                switch (alt41) {
            	            	case 1 :
            	            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:499:16: LPAREN expressionListOpt RPAREN
            	            	    {
            	            	    LPAREN78=(Token)input.LT(1);
            	            	    match(input,LPAREN,FOLLOW_LPAREN_in_postfixExpression4382); 
            	            	    pushFollow(FOLLOW_expressionListOpt_in_postfixExpression4384);
            	            	    expressionListOpt79=expressionListOpt();
            	            	    _fsp--;

            	            	    match(input,RPAREN,FOLLOW_RPAREN_in_postfixExpression4386); 
            	            	     expr = F.at(pos(LPAREN78)).Apply(null, expr, expressionListOpt79.toList()); 

            	            	    }
            	            	    break;

            	            	default :
            	            	    break loop41;
            	                }
            	            } while (true);


            	            }
            	            break;

            	    }


            	    }
            	    break;
            	case 2 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:501:7: LBRACKET ( name BAR )? expression RBRACKET
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_postfixExpression4418); 
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:501:16: ( name BAR )?
            	    int alt43=2;
            	    int LA43_0 = input.LA(1);

            	    if ( (LA43_0==QUOTED_IDENTIFIER||LA43_0==IDENTIFIER) ) {
            	        int LA43_1 = input.LA(2);

            	        if ( (LA43_1==BAR) ) {
            	            alt43=1;
            	        }
            	    }
            	    switch (alt43) {
            	        case 1 :
            	            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:501:17: name BAR
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression4421);
            	            name();
            	            _fsp--;

            	            match(input,BAR,FOLLOW_BAR_in_postfixExpression4423); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_expression_in_postfixExpression4427);
            	    expression();
            	    _fsp--;

            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_postfixExpression4430); 

            	    }
            	    break;

            	default :
            	    break loop44;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end postfixExpression


    // $ANTLR start primaryExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:503:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | LPAREN expression RPAREN );
    public final JCExpression primaryExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LBRACE81=null;
        Token THIS84=null;
        Token SUPER85=null;
        Token LPAREN87=null;
        Token LPAREN91=null;
        JCExpression newExpression80 = null;

        JCIdent identifier82 = null;

        ListBuffer<JFXStatement> objectLiteral83 = null;

        JCIdent identifier86 = null;

        ListBuffer<JCExpression> expressionListOpt88 = null;

        JCExpression stringExpression89 = null;

        JCExpression literal90 = null;

        JCExpression expression92 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:504:2: ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | LPAREN expression RPAREN )
            int alt46=8;
            switch ( input.LA(1) ) {
            case NEW:
                {
                alt46=1;
                }
                break;
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                int LA46_2 = input.LA(2);

                if ( (LA46_2==ABSTRACT||LA46_2==AND||LA46_2==ATTRIBUTE||(LA46_2>=THEN && LA46_2<=ELSE)||LA46_2==IN||(LA46_2>=PRIVATE && LA46_2<=READONLY)||LA46_2==INSTANCEOF||LA46_2==OR||(LA46_2>=LPAREN && LA46_2<=PERCENTEQ)||LA46_2==COLON||(LA46_2>=RBRACE_QUOTE_STRING_LITERAL && LA46_2<=RBRACE)||LA46_2==QUOTED_IDENTIFIER||LA46_2==IDENTIFIER) ) {
                    alt46=5;
                }
                else if ( (LA46_2==LBRACE) ) {
                    alt46=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("503:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | LPAREN expression RPAREN );", 46, 2, input);

                    throw nvae;
                }
                }
                break;
            case THIS:
                {
                alt46=3;
                }
                break;
            case SUPER:
                {
                alt46=4;
                }
                break;
            case QUOTE_LBRACE_STRING_LITERAL:
                {
                alt46=6;
                }
                break;
            case NULL:
            case TRUE:
            case FALSE:
            case STRING_LITERAL:
            case INTEGER_LITERAL:
            case FLOATING_POINT_LITERAL:
                {
                alt46=7;
                }
                break;
            case LPAREN:
                {
                alt46=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("503:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | LPAREN expression RPAREN );", 46, 0, input);

                throw nvae;
            }

            switch (alt46) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:504:4: newExpression
                    {
                    pushFollow(FOLLOW_newExpression_in_primaryExpression4455);
                    newExpression80=newExpression();
                    _fsp--;

                     expr = newExpression80; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:505:4: identifier LBRACE objectLiteral RBRACE
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression4467);
                    identifier82=identifier();
                    _fsp--;

                    LBRACE81=(Token)input.LT(1);
                    match(input,LBRACE,FOLLOW_LBRACE_in_primaryExpression4469); 
                    pushFollow(FOLLOW_objectLiteral_in_primaryExpression4472);
                    objectLiteral83=objectLiteral();
                    _fsp--;

                    match(input,RBRACE,FOLLOW_RBRACE_in_primaryExpression4474); 
                     expr = F.at(pos(LBRACE81)).PureObjectLiteral(identifier82, objectLiteral83.toList()); 

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:506:11: THIS
                    {
                    THIS84=(Token)input.LT(1);
                    match(input,THIS,FOLLOW_THIS_in_primaryExpression4491); 
                     expr = F.at(pos(THIS84)).Identifier(names._this); 

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:507:11: SUPER
                    {
                    SUPER85=(Token)input.LT(1);
                    match(input,SUPER,FOLLOW_SUPER_in_primaryExpression4511); 
                     expr = F.at(pos(SUPER85)).Identifier(names._super); 

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:508:11: identifier ( LPAREN expressionListOpt RPAREN )*
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression4531);
                    identifier86=identifier();
                    _fsp--;

                     expr = identifier86; 
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:509:10: ( LPAREN expressionListOpt RPAREN )*
                    loop45:
                    do {
                        int alt45=2;
                        int LA45_0 = input.LA(1);

                        if ( (LA45_0==LPAREN) ) {
                            alt45=1;
                        }


                        switch (alt45) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:509:12: LPAREN expressionListOpt RPAREN
                    	    {
                    	    LPAREN87=(Token)input.LT(1);
                    	    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression4552); 
                    	    pushFollow(FOLLOW_expressionListOpt_in_primaryExpression4556);
                    	    expressionListOpt88=expressionListOpt();
                    	    _fsp--;

                    	    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression4560); 
                    	     expr = F.at(pos(LPAREN87)).Apply(null, expr, expressionListOpt88.toList()); 

                    	    }
                    	    break;

                    	default :
                    	    break loop45;
                        }
                    } while (true);


                    }
                    break;
                case 6 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:510:11: stringExpression
                    {
                    pushFollow(FOLLOW_stringExpression_in_primaryExpression4580);
                    stringExpression89=stringExpression();
                    _fsp--;

                     expr = stringExpression89; 

                    }
                    break;
                case 7 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:511:11: literal
                    {
                    pushFollow(FOLLOW_literal_in_primaryExpression4599);
                    literal90=literal();
                    _fsp--;

                     expr = literal90; 

                    }
                    break;
                case 8 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:512:11: LPAREN expression RPAREN
                    {
                    LPAREN91=(Token)input.LT(1);
                    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression4619); 
                    pushFollow(FOLLOW_expression_in_primaryExpression4621);
                    expression92=expression();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression4623); 
                     expr = F.at(pos(LPAREN91)).Parens(expression92); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end primaryExpression


    // $ANTLR start newExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:514:1: newExpression returns [JCExpression expr] : NEW identifier ( LPAREN expressionListOpt RPAREN )? ;
    public final JCExpression newExpression() throws RecognitionException {
        JCExpression expr = null;

        Token NEW94=null;
        ListBuffer<JCExpression> expressionListOpt93 = null;

        JCIdent identifier95 = null;


         ListBuffer<JCExpression> args = null; 
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:516:2: ( NEW identifier ( LPAREN expressionListOpt RPAREN )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:516:4: NEW identifier ( LPAREN expressionListOpt RPAREN )?
            {
            NEW94=(Token)input.LT(1);
            match(input,NEW,FOLLOW_NEW_in_newExpression4656); 
            pushFollow(FOLLOW_identifier_in_newExpression4659);
            identifier95=identifier();
            _fsp--;

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:517:3: ( LPAREN expressionListOpt RPAREN )?
            int alt47=2;
            int LA47_0 = input.LA(1);

            if ( (LA47_0==LPAREN) ) {
                alt47=1;
            }
            switch (alt47) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:517:5: LPAREN expressionListOpt RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_newExpression4667); 
                    pushFollow(FOLLOW_expressionListOpt_in_newExpression4671);
                    expressionListOpt93=expressionListOpt();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_newExpression4675); 
                     args = expressionListOpt93; 

                    }
                    break;

            }

             expr = F.at(pos(NEW94)).NewClass(null, null, identifier95, 
            												(args==null? new ListBuffer<JCExpression>() : args).toList(), null); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end newExpression


    // $ANTLR start objectLiteral
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:522:1: objectLiteral returns [ListBuffer<JFXStatement> parts = new ListBuffer<JFXStatement>()] : ( objectLiteralPart )* ;
    public final ListBuffer<JFXStatement> objectLiteral() throws RecognitionException {
        ListBuffer<JFXStatement> parts =  new ListBuffer<JFXStatement>();

        JFXStatement objectLiteralPart96 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:523:2: ( ( objectLiteralPart )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:523:4: ( objectLiteralPart )*
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:523:4: ( objectLiteralPart )*
            loop48:
            do {
                int alt48=2;
                int LA48_0 = input.LA(1);

                if ( (LA48_0==ABSTRACT||LA48_0==ATTRIBUTE||(LA48_0>=PRIVATE && LA48_0<=READONLY)||LA48_0==QUOTED_IDENTIFIER||LA48_0==IDENTIFIER) ) {
                    alt48=1;
                }


                switch (alt48) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:523:6: objectLiteralPart
            	    {
            	    pushFollow(FOLLOW_objectLiteralPart_in_objectLiteral4715);
            	    objectLiteralPart96=objectLiteralPart();
            	    _fsp--;

            	     parts.append(objectLiteralPart96); 

            	    }
            	    break;

            	default :
            	    break loop48;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return parts;
    }
    // $ANTLR end objectLiteral


    // $ANTLR start objectLiteralPart
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );
    public final JFXStatement objectLiteralPart() throws RecognitionException {
        JFXStatement value = null;

        Token COLON97=null;
        name_return name98 = null;

        JCExpression expression99 = null;

        JavafxBindStatus bindOpt100 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:525:2: ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition )
            int alt50=3;
            switch ( input.LA(1) ) {
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                alt50=1;
                }
                break;
            case ABSTRACT:
                {
                switch ( input.LA(2) ) {
                case PUBLIC:
                    {
                    int LA50_9 = input.LA(3);

                    if ( (LA50_9==FUNCTION) ) {
                        alt50=3;
                    }
                    else if ( (LA50_9==ATTRIBUTE) ) {
                        alt50=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 9, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA50_10 = input.LA(3);

                    if ( (LA50_10==ATTRIBUTE) ) {
                        alt50=2;
                    }
                    else if ( (LA50_10==FUNCTION) ) {
                        alt50=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA50_11 = input.LA(3);

                    if ( (LA50_11==FUNCTION) ) {
                        alt50=3;
                    }
                    else if ( (LA50_11==ATTRIBUTE) ) {
                        alt50=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt50=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt50=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 2, input);

                    throw nvae;
                }

                }
                break;
            case READONLY:
                {
                switch ( input.LA(2) ) {
                case PUBLIC:
                    {
                    int LA50_9 = input.LA(3);

                    if ( (LA50_9==FUNCTION) ) {
                        alt50=3;
                    }
                    else if ( (LA50_9==ATTRIBUTE) ) {
                        alt50=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 9, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA50_10 = input.LA(3);

                    if ( (LA50_10==ATTRIBUTE) ) {
                        alt50=2;
                    }
                    else if ( (LA50_10==FUNCTION) ) {
                        alt50=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA50_11 = input.LA(3);

                    if ( (LA50_11==FUNCTION) ) {
                        alt50=3;
                    }
                    else if ( (LA50_11==ATTRIBUTE) ) {
                        alt50=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case FUNCTION:
                    {
                    alt50=3;
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt50=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 3, input);

                    throw nvae;
                }

                }
                break;
            case PUBLIC:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA50_12 = input.LA(3);

                    if ( (LA50_12==FUNCTION) ) {
                        alt50=3;
                    }
                    else if ( (LA50_12==ATTRIBUTE) ) {
                        alt50=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA50_13 = input.LA(3);

                    if ( (LA50_13==FUNCTION) ) {
                        alt50=3;
                    }
                    else if ( (LA50_13==ATTRIBUTE) ) {
                        alt50=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt50=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt50=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 4, input);

                    throw nvae;
                }

                }
                break;
            case PRIVATE:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA50_12 = input.LA(3);

                    if ( (LA50_12==FUNCTION) ) {
                        alt50=3;
                    }
                    else if ( (LA50_12==ATTRIBUTE) ) {
                        alt50=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA50_13 = input.LA(3);

                    if ( (LA50_13==FUNCTION) ) {
                        alt50=3;
                    }
                    else if ( (LA50_13==ATTRIBUTE) ) {
                        alt50=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case FUNCTION:
                    {
                    alt50=3;
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt50=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 5, input);

                    throw nvae;
                }

                }
                break;
            case PROTECTED:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA50_12 = input.LA(3);

                    if ( (LA50_12==FUNCTION) ) {
                        alt50=3;
                    }
                    else if ( (LA50_12==ATTRIBUTE) ) {
                        alt50=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA50_13 = input.LA(3);

                    if ( (LA50_13==FUNCTION) ) {
                        alt50=3;
                    }
                    else if ( (LA50_13==ATTRIBUTE) ) {
                        alt50=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt50=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt50=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 6, input);

                    throw nvae;
                }

                }
                break;
            case ATTRIBUTE:
                {
                alt50=2;
                }
                break;
            case FUNCTION:
                {
                alt50=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("524:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 50, 0, input);

                throw nvae;
            }

            switch (alt50) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:525:4: name COLON bindOpt expression ( COMMA | SEMI )?
                    {
                    pushFollow(FOLLOW_name_in_objectLiteralPart4741);
                    name98=name();
                    _fsp--;

                    COLON97=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_objectLiteralPart4743); 
                    pushFollow(FOLLOW_bindOpt_in_objectLiteralPart4746);
                    bindOpt100=bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_objectLiteralPart4748);
                    expression99=expression();
                    _fsp--;

                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:525:35: ( COMMA | SEMI )?
                    int alt49=2;
                    int LA49_0 = input.LA(1);

                    if ( ((LA49_0>=SEMI && LA49_0<=COMMA)) ) {
                        alt49=1;
                    }
                    switch (alt49) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:
                            {
                            if ( (input.LA(1)>=SEMI && input.LA(1)<=COMMA) ) {
                                input.consume();
                                errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse =
                                    new MismatchedSetException(null,input);
                                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_objectLiteralPart4750);    throw mse;
                            }


                            }
                            break;

                    }

                     value = F.at(pos(COLON97)).ObjectLiteralPart(name98.value, expression99, bindOpt100); 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:526:10: attributeDefinition
                    {
                    pushFollow(FOLLOW_attributeDefinition_in_objectLiteralPart4770);
                    attributeDefinition();
                    _fsp--;


                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:527:10: functionDefinition
                    {
                    pushFollow(FOLLOW_functionDefinition_in_objectLiteralPart4781);
                    functionDefinition();
                    _fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end objectLiteralPart


    // $ANTLR start stringExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:528:1: stringExpression returns [JCExpression expr] : ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL ;
    public final JCExpression stringExpression() throws RecognitionException {
        JCExpression expr = null;

        Token ql=null;
        Token rl=null;
        Token rq=null;
        JCExpression f1 = null;

        JCExpression e1 = null;

        JCExpression fn = null;

        JCExpression en = null;


         ListBuffer<JCExpression> strexp = new ListBuffer<JCExpression>(); 
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:530:2: (ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:530:4: ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL
            {
            ql=(Token)input.LT(1);
            match(input,QUOTE_LBRACE_STRING_LITERAL,FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression4803); 
             strexp.append(F.at(pos(ql)).Literal(TypeTags.CLASS, ql.getText())); 
            pushFollow(FOLLOW_formatOrNull_in_stringExpression4812);
            f1=formatOrNull();
            _fsp--;

             strexp.append(f1); 
            pushFollow(FOLLOW_expression_in_stringExpression4823);
            e1=expression();
            _fsp--;

             strexp.append(e1); 
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:533:4: (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )*
            loop51:
            do {
                int alt51=2;
                int LA51_0 = input.LA(1);

                if ( (LA51_0==RBRACE_LBRACE_STRING_LITERAL) ) {
                    alt51=1;
                }


                switch (alt51) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:533:7: rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression
            	    {
            	    rl=(Token)input.LT(1);
            	    match(input,RBRACE_LBRACE_STRING_LITERAL,FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression4838); 
            	     strexp.append(F.at(pos(rl)).Literal(TypeTags.CLASS, rl.getText())); 
            	    pushFollow(FOLLOW_formatOrNull_in_stringExpression4850);
            	    fn=formatOrNull();
            	    _fsp--;

            	     strexp.append(fn); 
            	    pushFollow(FOLLOW_expression_in_stringExpression4864);
            	    en=expression();
            	    _fsp--;

            	     strexp.append(en); 

            	    }
            	    break;

            	default :
            	    break loop51;
                }
            } while (true);

            rq=(Token)input.LT(1);
            match(input,RBRACE_QUOTE_STRING_LITERAL,FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression4885); 
             strexp.append(F.at(pos(rq)).Literal(TypeTags.CLASS, rq.getText())); 
             expr = F.at(pos(ql)).StringExpression(strexp.toList()); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end stringExpression


    // $ANTLR start formatOrNull
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:540:1: formatOrNull returns [JCExpression expr] : (fs= FORMAT_STRING_LITERAL | );
    public final JCExpression formatOrNull() throws RecognitionException {
        JCExpression expr = null;

        Token fs=null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:541:2: (fs= FORMAT_STRING_LITERAL | )
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( (LA52_0==FORMAT_STRING_LITERAL) ) {
                alt52=1;
            }
            else if ( ((LA52_0>=POUND && LA52_0<=TYPEOF)||LA52_0==IF||(LA52_0>=THIS && LA52_0<=FALSE)||(LA52_0>=NOT && LA52_0<=NEW)||LA52_0==SUPER||(LA52_0>=SIZEOF && LA52_0<=LPAREN)||(LA52_0>=PLUSPLUS && LA52_0<=SUBSUB)||(LA52_0>=QUES && LA52_0<=STRING_LITERAL)||LA52_0==QUOTE_LBRACE_STRING_LITERAL||(LA52_0>=QUOTED_IDENTIFIER && LA52_0<=INTEGER_LITERAL)||LA52_0==FLOATING_POINT_LITERAL||LA52_0==IDENTIFIER) ) {
                alt52=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("540:1: formatOrNull returns [JCExpression expr] : (fs= FORMAT_STRING_LITERAL | );", 52, 0, input);

                throw nvae;
            }
            switch (alt52) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:541:4: fs= FORMAT_STRING_LITERAL
                    {
                    fs=(Token)input.LT(1);
                    match(input,FORMAT_STRING_LITERAL,FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull4915); 
                     expr = F.at(pos(fs)).Literal(TypeTags.CLASS, fs.getText()); 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:542:22: 
                    {
                     expr = null; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end formatOrNull


    // $ANTLR start expressionListOpt
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:544:1: expressionListOpt returns [ListBuffer<JCExpression> args = new ListBuffer<JCExpression>()] : (e1= expression ( COMMA e= expression )* )? ;
    public final ListBuffer<JCExpression> expressionListOpt() throws RecognitionException {
        ListBuffer<JCExpression> args =  new ListBuffer<JCExpression>();

        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:545:2: ( (e1= expression ( COMMA e= expression )* )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:545:4: (e1= expression ( COMMA e= expression )* )?
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:545:4: (e1= expression ( COMMA e= expression )* )?
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( ((LA54_0>=POUND && LA54_0<=TYPEOF)||LA54_0==IF||(LA54_0>=THIS && LA54_0<=FALSE)||(LA54_0>=NOT && LA54_0<=NEW)||LA54_0==SUPER||(LA54_0>=SIZEOF && LA54_0<=LPAREN)||(LA54_0>=PLUSPLUS && LA54_0<=SUBSUB)||(LA54_0>=QUES && LA54_0<=STRING_LITERAL)||LA54_0==QUOTE_LBRACE_STRING_LITERAL||(LA54_0>=QUOTED_IDENTIFIER && LA54_0<=INTEGER_LITERAL)||LA54_0==FLOATING_POINT_LITERAL||LA54_0==IDENTIFIER) ) {
                alt54=1;
            }
            switch (alt54) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:545:6: e1= expression ( COMMA e= expression )*
                    {
                    pushFollow(FOLLOW_expression_in_expressionListOpt4947);
                    e1=expression();
                    _fsp--;

                     args.append(e1); 
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:546:6: ( COMMA e= expression )*
                    loop53:
                    do {
                        int alt53=2;
                        int LA53_0 = input.LA(1);

                        if ( (LA53_0==COMMA) ) {
                            alt53=1;
                        }


                        switch (alt53) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:546:7: COMMA e= expression
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_expressionListOpt4958); 
                    	    pushFollow(FOLLOW_expression_in_expressionListOpt4964);
                    	    e=expression();
                    	    _fsp--;

                    	     args.append(e); 

                    	    }
                    	    break;

                    	default :
                    	    break loop53;
                        }
                    } while (true);


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return args;
    }
    // $ANTLR end expressionListOpt


    // $ANTLR start unaryOperator
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:547:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );
    public final int unaryOperator() throws RecognitionException {
        int optag = 0;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:548:2: ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB )
            int alt55=9;
            switch ( input.LA(1) ) {
            case POUND:
                {
                alt55=1;
                }
                break;
            case QUES:
                {
                alt55=2;
                }
                break;
            case SUB:
                {
                alt55=3;
                }
                break;
            case NOT:
                {
                alt55=4;
                }
                break;
            case SIZEOF:
                {
                alt55=5;
                }
                break;
            case TYPEOF:
                {
                alt55=6;
                }
                break;
            case REVERSE:
                {
                alt55=7;
                }
                break;
            case PLUSPLUS:
                {
                alt55=8;
                }
                break;
            case SUBSUB:
                {
                alt55=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("547:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );", 55, 0, input);

                throw nvae;
            }

            switch (alt55) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:548:4: POUND
                    {
                    match(input,POUND,FOLLOW_POUND_in_unaryOperator4987); 
                     optag = 0; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:549:4: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_unaryOperator4998); 
                     optag = 0; 

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:550:4: SUB
                    {
                    match(input,SUB,FOLLOW_SUB_in_unaryOperator5011); 
                     optag = JCTree.NEG; 

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:551:4: NOT
                    {
                    match(input,NOT,FOLLOW_NOT_in_unaryOperator5024); 
                     optag = JCTree.NOT; 

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:552:4: SIZEOF
                    {
                    match(input,SIZEOF,FOLLOW_SIZEOF_in_unaryOperator5037); 
                     optag = 0; 

                    }
                    break;
                case 6 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:553:4: TYPEOF
                    {
                    match(input,TYPEOF,FOLLOW_TYPEOF_in_unaryOperator5050); 
                     optag = 0; 

                    }
                    break;
                case 7 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:554:4: REVERSE
                    {
                    match(input,REVERSE,FOLLOW_REVERSE_in_unaryOperator5063); 
                     optag = 0; 

                    }
                    break;
                case 8 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:555:4: PLUSPLUS
                    {
                    match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_unaryOperator5076); 
                     optag = 0; 

                    }
                    break;
                case 9 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:556:4: SUBSUB
                    {
                    match(input,SUBSUB,FOLLOW_SUBSUB_in_unaryOperator5089); 
                     optag = 0; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return optag;
    }
    // $ANTLR end unaryOperator


    // $ANTLR start assignmentOperator
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:558:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );
    public final int assignmentOperator() throws RecognitionException {
        int optag = 0;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:559:2: ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ )
            int alt56=5;
            switch ( input.LA(1) ) {
            case PLUSEQ:
                {
                alt56=1;
                }
                break;
            case SUBEQ:
                {
                alt56=2;
                }
                break;
            case STAREQ:
                {
                alt56=3;
                }
                break;
            case SLASHEQ:
                {
                alt56=4;
                }
                break;
            case PERCENTEQ:
                {
                alt56=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("558:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );", 56, 0, input);

                throw nvae;
            }

            switch (alt56) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:559:4: PLUSEQ
                    {
                    match(input,PLUSEQ,FOLLOW_PLUSEQ_in_assignmentOperator5110); 
                     optag = JCTree.PLUS_ASG; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:560:4: SUBEQ
                    {
                    match(input,SUBEQ,FOLLOW_SUBEQ_in_assignmentOperator5123); 
                     optag = JCTree.MINUS_ASG; 

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:561:4: STAREQ
                    {
                    match(input,STAREQ,FOLLOW_STAREQ_in_assignmentOperator5136); 
                     optag = JCTree.MUL_ASG; 

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:562:4: SLASHEQ
                    {
                    match(input,SLASHEQ,FOLLOW_SLASHEQ_in_assignmentOperator5149); 
                     optag = JCTree.DIV_ASG; 

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:563:4: PERCENTEQ
                    {
                    match(input,PERCENTEQ,FOLLOW_PERCENTEQ_in_assignmentOperator5162); 
                     optag = JCTree.MOD_ASG; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return optag;
    }
    // $ANTLR end assignmentOperator


    // $ANTLR start typeReference
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:565:1: typeReference returns [JFXType type] : ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? ;
    public final JFXType typeReference() throws RecognitionException {
        JFXType type = null;

        Token STAR102=null;
        int ccn = 0;

        int ccs = 0;

        name_return name101 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:566:2: ( ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:566:4: ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:566:4: ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==COLON) ) {
                alt58=1;
            }
            switch (alt58) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:566:6: COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    {
                    match(input,COLON,FOLLOW_COLON_in_typeReference5186); 
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:566:13: ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    int alt57=2;
                    int LA57_0 = input.LA(1);

                    if ( (LA57_0==QUOTED_IDENTIFIER||LA57_0==IDENTIFIER) ) {
                        alt57=1;
                    }
                    else if ( (LA57_0==STAR) ) {
                        alt57=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("566:13: ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )", 57, 0, input);

                        throw nvae;
                    }
                    switch (alt57) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:566:15: name ccn= cardinalityConstraint
                            {
                            pushFollow(FOLLOW_name_in_typeReference5191);
                            name101=name();
                            _fsp--;

                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference5195);
                            ccn=cardinalityConstraint();
                            _fsp--;

                             type = F.TypeClass(name101.value, ccn); 

                            }
                            break;
                        case 2 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:567:22: STAR ccs= cardinalityConstraint
                            {
                            STAR102=(Token)input.LT(1);
                            match(input,STAR,FOLLOW_STAR_in_typeReference5221); 
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference5225);
                            ccs=cardinalityConstraint();
                            _fsp--;

                             type = F.at(pos(STAR102)).TypeAny(ccs); 

                            }
                            break;

                    }


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return type;
    }
    // $ANTLR end typeReference


    // $ANTLR start cardinalityConstraint
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:571:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );
    public final int cardinalityConstraint() throws RecognitionException {
        int ary = 0;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:572:2: ( LBRACKET RBRACKET | QUES | PLUS | STAR | )
            int alt59=5;
            switch ( input.LA(1) ) {
            case LBRACKET:
                {
                alt59=1;
                }
                break;
            case QUES:
                {
                alt59=2;
                }
                break;
            case PLUS:
                {
                alt59=3;
                }
                break;
            case STAR:
                {
                alt59=4;
                }
                break;
            case INVERSE:
            case RPAREN:
            case SEMI:
            case COMMA:
            case EQ:
            case LBRACE:
                {
                alt59=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("571:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 59, 0, input);

                throw nvae;
            }

            switch (alt59) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:572:5: LBRACKET RBRACKET
                    {
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_cardinalityConstraint5287); 
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_cardinalityConstraint5291); 
                     ary = JFXType.CARDINALITY_ANY; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:573:5: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_cardinalityConstraint5303); 
                     ary = JFXType.CARDINALITY_OPTIONAL; 

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:574:5: PLUS
                    {
                    match(input,PLUS,FOLLOW_PLUS_in_cardinalityConstraint5330); 
                     ary = JFXType.CARDINALITY_ANY; 

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:575:5: STAR
                    {
                    match(input,STAR,FOLLOW_STAR_in_cardinalityConstraint5357); 
                     ary = JFXType.CARDINALITY_ANY; 

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:576:29: 
                    {
                     ary = JFXType.CARDINALITY_OPTIONAL; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ary;
    }
    // $ANTLR end cardinalityConstraint


    // $ANTLR start literal
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:578:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );
    public final JCExpression literal() throws RecognitionException {
        JCExpression expr = null;

        Token t=null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:579:2: (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL )
            int alt60=6;
            switch ( input.LA(1) ) {
            case STRING_LITERAL:
                {
                alt60=1;
                }
                break;
            case INTEGER_LITERAL:
                {
                alt60=2;
                }
                break;
            case FLOATING_POINT_LITERAL:
                {
                alt60=3;
                }
                break;
            case TRUE:
                {
                alt60=4;
                }
                break;
            case FALSE:
                {
                alt60=5;
                }
                break;
            case NULL:
                {
                alt60=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("578:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );", 60, 0, input);

                throw nvae;
            }

            switch (alt60) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:579:4: t= STRING_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_literal5426); 
                     expr = F.at(pos(t)).Literal(TypeTags.CLASS, t.getText()); 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:580:4: t= INTEGER_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,INTEGER_LITERAL,FOLLOW_INTEGER_LITERAL_in_literal5436); 
                     expr = F.at(pos(t)).Literal(TypeTags.INT, Convert.string2int(t.getText(), 10)); 

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:581:4: t= FLOATING_POINT_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,FLOATING_POINT_LITERAL,FOLLOW_FLOATING_POINT_LITERAL_in_literal5446); 
                     expr = F.at(pos(t)).Literal(TypeTags.DOUBLE, Double.valueOf(t.getText())); 

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:582:4: t= TRUE
                    {
                    t=(Token)input.LT(1);
                    match(input,TRUE,FOLLOW_TRUE_in_literal5456); 
                     expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 1); 

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:583:4: t= FALSE
                    {
                    t=(Token)input.LT(1);
                    match(input,FALSE,FOLLOW_FALSE_in_literal5470); 
                     expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 0); 

                    }
                    break;
                case 6 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:584:4: t= NULL
                    {
                    t=(Token)input.LT(1);
                    match(input,NULL,FOLLOW_NULL_in_literal5484); 
                     expr = F.at(pos(t)).Literal(TypeTags.BOT, null); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end literal


    // $ANTLR start typeName
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:586:1: typeName returns [JCExpression expr] : qualident ;
    public final JCExpression typeName() throws RecognitionException {
        JCExpression expr = null;

        JCExpression qualident103 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:587:8: ( qualident )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:587:10: qualident
            {
            pushFollow(FOLLOW_qualident_in_typeName5511);
            qualident103=qualident();
            _fsp--;

             expr = qualident103; 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end typeName


    // $ANTLR start qualident
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:589:1: qualident returns [JCExpression expr] : identifier ( DOT name )* ;
    public final JCExpression qualident() throws RecognitionException {
        JCExpression expr = null;

        JCIdent identifier104 = null;

        name_return name105 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:590:8: ( identifier ( DOT name )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:590:10: identifier ( DOT name )*
            {
            pushFollow(FOLLOW_identifier_in_qualident5553);
            identifier104=identifier();
            _fsp--;

             expr = identifier104; 
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:591:10: ( DOT name )*
            loop61:
            do {
                int alt61=2;
                int LA61_0 = input.LA(1);

                if ( (LA61_0==DOT) ) {
                    alt61=1;
                }


                switch (alt61) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:591:12: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_qualident5581); 
            	    pushFollow(FOLLOW_name_in_qualident5583);
            	    name105=name();
            	    _fsp--;

            	     expr = F.at(name105.pos).Select(expr, name105.value); 

            	    }
            	    break;

            	default :
            	    break loop61;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end qualident


    // $ANTLR start identifier
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:593:1: identifier returns [JCIdent expr] : name ;
    public final JCIdent identifier() throws RecognitionException {
        JCIdent expr = null;

        name_return name106 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:594:2: ( name )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:594:4: name
            {
            pushFollow(FOLLOW_name_in_identifier5620);
            name106=name();
            _fsp--;

             expr = F.at(name106.pos).Ident(name106.value); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end identifier

    public static class name_return extends ParserRuleReturnScope {
        public Name value;
        public int pos;
    };

    // $ANTLR start name
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:596:1: name returns [Name value, int pos] : tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) ;
    public final name_return name() throws RecognitionException {
        name_return retval = new name_return();
        retval.start = input.LT(1);

        Token tokid=null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:597:2: (tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:597:4: tokid= ( QUOTED_IDENTIFIER | IDENTIFIER )
            {
            tokid=(Token)input.LT(1);
            if ( input.LA(1)==QUOTED_IDENTIFIER||input.LA(1)==IDENTIFIER ) {
                input.consume();
                errorRecovery=false;
            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_name5654);    throw mse;
            }

             retval.value = Name.fromString(names, tokid.getText()); retval.pos = pos(tokid); 

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end name


 

    public static final BitSet FOLLOW_packageDecl_in_module2018 = new BitSet(new long[]{0xF130F205C0120260L,0x0960B001C000E805L});
    public static final BitSet FOLLOW_moduleItems_in_module2021 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_module2023 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PACKAGE_in_packageDecl2045 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_qualident_in_packageDecl2047 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_packageDecl2049 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_moduleItem_in_moduleItems2080 = new BitSet(new long[]{0xF130F205C0120262L,0x0960B001C000E805L});
    public static final BitSet FOLLOW_importDecl_in_moduleItem2124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDefinition_in_moduleItem2140 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_moduleItem2156 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importDecl2192 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_identifier_in_importDecl2195 = new BitSet(new long[]{0x0000000000000000L,0x0000000000280000L});
    public static final BitSet FOLLOW_DOT_in_importDecl2220 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_importDecl2222 = new BitSet(new long[]{0x0000000000000000L,0x0000000000280000L});
    public static final BitSet FOLLOW_DOT_in_importDecl2251 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
    public static final BitSet FOLLOW_STAR_in_importDecl2253 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_importDecl2262 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_classDefinition2293 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_CLASS_in_classDefinition2296 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_classDefinition2298 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000020L});
    public static final BitSet FOLLOW_supers_in_classDefinition2300 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_classDefinition2302 = new BitSet(new long[]{0xC000000000004200L,0x0008000000000005L});
    public static final BitSet FOLLOW_classMembers_in_classDefinition2304 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_classDefinition2306 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXTENDS_in_supers2331 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_supers2335 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_COMMA_in_supers2363 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_supers2367 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_attributeDefinition_in_classMembers2401 = new BitSet(new long[]{0xC000000000004202L,0x0000000000000005L});
    public static final BitSet FOLLOW_functionDefinition_in_classMembers2419 = new BitSet(new long[]{0xC000000000004202L,0x0000000000000005L});
    public static final BitSet FOLLOW_modifierFlags_in_attributeDefinition2448 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeDefinition2450 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_attributeDefinition2452 = new BitSet(new long[]{0x0000000000000000L,0x0000080000880008L});
    public static final BitSet FOLLOW_typeReference_in_attributeDefinition2454 = new BitSet(new long[]{0x0000000000000000L,0x0000000000880008L});
    public static final BitSet FOLLOW_EQ_in_attributeDefinition2457 = new BitSet(new long[]{0x3000F20030010060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_bindOpt_in_attributeDefinition2459 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_expression_in_attributeDefinition2462 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_inverseClause_in_attributeDefinition2466 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_attributeDefinition2471 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INVERSE_in_inverseClause2493 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_memberSelector_in_inverseClause2495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_functionDefinition2513 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_functionDefinition2515 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_functionDefinition2519 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_formalParameters_in_functionDefinition2521 = new BitSet(new long[]{0x0000000000000000L,0x0001080000000000L});
    public static final BitSet FOLLOW_typeReference_in_functionDefinition2525 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_block_in_functionDefinition2528 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags2552 = new BitSet(new long[]{0xC000000000000002L,0x0000000000000001L});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags2565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags2587 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000004L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags2600 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_accessModifier2648 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PRIVATE_in_accessModifier2668 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROTECTED_in_accessModifier2687 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ABSTRACT_in_otherModifier2714 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_READONLY_in_otherModifier2732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_memberSelector2761 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_DOT_in_memberSelector2765 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_memberSelector2771 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_formalParameters2789 = new BitSet(new long[]{0x0000000000000000L,0x0820000000010000L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters2796 = new BitSet(new long[]{0x0000000000000000L,0x0000000000110000L});
    public static final BitSet FOLLOW_COMMA_in_formalParameters2814 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters2820 = new BitSet(new long[]{0x0000000000000000L,0x0000000000110000L});
    public static final BitSet FOLLOW_RPAREN_in_formalParameters2831 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_formalParameter2846 = new BitSet(new long[]{0x0000000000000002L,0x0000080000000000L});
    public static final BitSet FOLLOW_typeReference_in_formalParameter2848 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_block2867 = new BitSet(new long[]{0xF130F201C0020260L,0x0968B001C000E807L});
    public static final BitSet FOLLOW_statements_in_block2871 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_block2875 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_statements2893 = new BitSet(new long[]{0xF130F201C0020262L,0x0960B001C000E807L});
    public static final BitSet FOLLOW_variableDeclaration_in_statement2940 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionDefinition_in_statement2950 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_statement2966 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_LPAREN_in_statement2968 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_expression_in_statement2970 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_statement2972 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_block_in_statement2974 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_statement2981 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_statement2985 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BREAK_in_statement2995 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_statement2999 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_statement3010 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_statement3014 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_throwStatement_in_statement3031 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_returnStatement_in_statement3048 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tryStatement_in_statement3065 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_assertStatement3093 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_expression_in_assertStatement3097 = new BitSet(new long[]{0x0000000000000000L,0x0000080000080000L});
    public static final BitSet FOLLOW_COLON_in_assertStatement3105 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_expression_in_assertStatement3109 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_assertStatement3119 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variableDeclaration3134 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_variableDeclaration3137 = new BitSet(new long[]{0x0000000000000000L,0x0000080000880000L});
    public static final BitSet FOLLOW_typeReference_in_variableDeclaration3140 = new BitSet(new long[]{0x0000000000000000L,0x0000000000880000L});
    public static final BitSet FOLLOW_EQ_in_variableDeclaration3151 = new BitSet(new long[]{0x3000F20030010060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_bindOpt_in_variableDeclaration3153 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_expression_in_variableDeclaration3156 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_variableDeclaration3158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_variableDeclaration3169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BIND_in_bindOpt3206 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3222 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAYS_in_bindOpt3237 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3253 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TIE_in_bindOpt3268 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3284 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THROW_in_throwStatement3313 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_expression_in_throwStatement3317 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_throwStatement3321 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnStatement3347 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C008E800L});
    public static final BitSet FOLLOW_expression_in_returnStatement3350 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_returnStatement3366 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRY_in_tryStatement3394 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3400 = new BitSet(new long[]{0x0200000000080000L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement3413 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3419 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catchClause_in_tryStatement3435 = new BitSet(new long[]{0x0200000000080002L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement3458 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3463 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CATCH_in_catchClause3502 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_LPAREN_in_catchClause3505 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_catchClause3508 = new BitSet(new long[]{0x0000000000000000L,0x0000080000010000L});
    public static final BitSet FOLLOW_COLON_in_catchClause3516 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_identifier_in_catchClause3518 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_catchClause3527 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_block_in_catchClause3531 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifExpression_in_expression3559 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_suffixedExpression_in_expression3582 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifExpression3613 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_expression_in_ifExpression3617 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_THEN_in_ifExpression3621 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_expression_in_ifExpression3626 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_ELSE_in_ifExpression3634 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_expression_in_ifExpression3639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentExpression_in_suffixedExpression3662 = new BitSet(new long[]{0x0000000000000002L,0x0000000140000000L});
    public static final BitSet FOLLOW_set_in_suffixedExpression3673 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression3702 = new BitSet(new long[]{0x0000000000000002L,0x0000000000800000L});
    public static final BitSet FOLLOW_EQ_in_assignmentExpression3717 = new BitSet(new long[]{0x3000F00000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression3723 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression3750 = new BitSet(new long[]{0x0000000000000002L,0x000001F000000000L});
    public static final BitSet FOLLOW_assignmentOperator_in_assignmentOpExpression3766 = new BitSet(new long[]{0x3000F00000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression3772 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orExpression_in_andExpression3798 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_AND_in_andExpression3814 = new BitSet(new long[]{0x3000F00000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_orExpression_in_andExpression3820 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression3848 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001000L});
    public static final BitSet FOLLOW_OR_in_orExpression3863 = new BitSet(new long[]{0x3000F00000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression3869 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001000L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression3897 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_INSTANCEOF_in_instanceOfExpression3912 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_identifier_in_instanceOfExpression3914 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression3942 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_LTGT_in_relationalExpression3958 = new BitSet(new long[]{0x3000F00000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression3964 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_EQEQ_in_relationalExpression3978 = new BitSet(new long[]{0x3000F00000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression3984 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_LTEQ_in_relationalExpression3998 = new BitSet(new long[]{0x3000F00000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4004 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_GTEQ_in_relationalExpression4018 = new BitSet(new long[]{0x3000F00000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4024 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_LT_in_relationalExpression4038 = new BitSet(new long[]{0x3000F00000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4046 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_GT_in_relationalExpression4060 = new BitSet(new long[]{0x3000F00000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4068 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_IN_in_relationalExpression4082 = new BitSet(new long[]{0x3000F00000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4090 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4119 = new BitSet(new long[]{0x0000000000000002L,0x00000000A0000000L});
    public static final BitSet FOLLOW_PLUS_in_additiveExpression4134 = new BitSet(new long[]{0x3000F00000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4140 = new BitSet(new long[]{0x0000000000000002L,0x00000000A0000000L});
    public static final BitSet FOLLOW_SUB_in_additiveExpression4153 = new BitSet(new long[]{0x3000F00000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4160 = new BitSet(new long[]{0x0000000000000002L,0x00000000A0000000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4188 = new BitSet(new long[]{0x0000000000000002L,0x0000000E00000000L});
    public static final BitSet FOLLOW_STAR_in_multiplicativeExpression4204 = new BitSet(new long[]{0x3000F00000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4211 = new BitSet(new long[]{0x0000000000000002L,0x0000000E00000000L});
    public static final BitSet FOLLOW_SLASH_in_multiplicativeExpression4225 = new BitSet(new long[]{0x3000F00000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4231 = new BitSet(new long[]{0x0000000000000002L,0x0000000E00000000L});
    public static final BitSet FOLLOW_PERCENT_in_multiplicativeExpression4245 = new BitSet(new long[]{0x3000F00000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4249 = new BitSet(new long[]{0x0000000000000002L,0x0000000E00000000L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression4279 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryOperator_in_unaryExpression4290 = new BitSet(new long[]{0x2000F00000000000L,0x0960A00000008800L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression4294 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primaryExpression_in_postfixExpression4314 = new BitSet(new long[]{0x0000000000000002L,0x0000000000220000L});
    public static final BitSet FOLLOW_DOT_in_postfixExpression4329 = new BitSet(new long[]{0x0000000000100000L,0x0820000000000000L});
    public static final BitSet FOLLOW_CLASS_in_postfixExpression4333 = new BitSet(new long[]{0x0000000000000002L,0x0000000000220000L});
    public static final BitSet FOLLOW_name_in_postfixExpression4357 = new BitSet(new long[]{0x0000000000000002L,0x0000000000228000L});
    public static final BitSet FOLLOW_LPAREN_in_postfixExpression4382 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C001E800L});
    public static final BitSet FOLLOW_expressionListOpt_in_postfixExpression4384 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_postfixExpression4386 = new BitSet(new long[]{0x0000000000000002L,0x0000000000228000L});
    public static final BitSet FOLLOW_LBRACKET_in_postfixExpression4418 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_name_in_postfixExpression4421 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_BAR_in_postfixExpression4423 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_expression_in_postfixExpression4427 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_RBRACKET_in_postfixExpression4430 = new BitSet(new long[]{0x0000000000000002L,0x0000000000220000L});
    public static final BitSet FOLLOW_newExpression_in_primaryExpression4455 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression4467 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_primaryExpression4469 = new BitSet(new long[]{0xC000000000004200L,0x0828000000000005L});
    public static final BitSet FOLLOW_objectLiteral_in_primaryExpression4472 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_primaryExpression4474 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THIS_in_primaryExpression4491 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_primaryExpression4511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression4531 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression4552 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C001E800L});
    public static final BitSet FOLLOW_expressionListOpt_in_primaryExpression4556 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression4560 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_stringExpression_in_primaryExpression4580 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primaryExpression4599 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression4619 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_expression_in_primaryExpression4621 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression4623 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_newExpression4656 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_identifier_in_newExpression4659 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_LPAREN_in_newExpression4667 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C001E800L});
    public static final BitSet FOLLOW_expressionListOpt_in_newExpression4671 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_newExpression4675 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_objectLiteralPart_in_objectLiteral4715 = new BitSet(new long[]{0xC000000000004202L,0x0820000000000005L});
    public static final BitSet FOLLOW_name_in_objectLiteralPart4741 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_COLON_in_objectLiteralPart4743 = new BitSet(new long[]{0x3000F20030010060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_bindOpt_in_objectLiteralPart4746 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_expression_in_objectLiteralPart4748 = new BitSet(new long[]{0x0000000000000002L,0x0000000000180000L});
    public static final BitSet FOLLOW_set_in_objectLiteralPart4750 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeDefinition_in_objectLiteralPart4770 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionDefinition_in_objectLiteralPart4781 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression4803 = new BitSet(new long[]{0x3000F20000000060L,0x0970B001C000E800L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression4812 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_expression_in_stringExpression4823 = new BitSet(new long[]{0x0000000000000000L,0x0006000000000000L});
    public static final BitSet FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression4838 = new BitSet(new long[]{0x3000F20000000060L,0x0970B001C000E800L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression4850 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_expression_in_stringExpression4864 = new BitSet(new long[]{0x0000000000000000L,0x0006000000000000L});
    public static final BitSet FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression4885 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull4915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt4947 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_COMMA_in_expressionListOpt4958 = new BitSet(new long[]{0x3000F20000000060L,0x0960B001C000E800L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt4964 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_POUND_in_unaryOperator4987 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_unaryOperator4998 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUB_in_unaryOperator5011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_unaryOperator5024 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIZEOF_in_unaryOperator5037 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TYPEOF_in_unaryOperator5050 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REVERSE_in_unaryOperator5063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_unaryOperator5076 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_unaryOperator5089 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSEQ_in_assignmentOperator5110 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBEQ_in_assignmentOperator5123 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAREQ_in_assignmentOperator5136 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SLASHEQ_in_assignmentOperator5149 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENTEQ_in_assignmentOperator5162 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLON_in_typeReference5186 = new BitSet(new long[]{0x0000000000000000L,0x0820000200000000L});
    public static final BitSet FOLLOW_name_in_typeReference5191 = new BitSet(new long[]{0x0000000000000002L,0x0000100220020000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference5195 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_typeReference5221 = new BitSet(new long[]{0x0000000000000002L,0x0000100220020000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference5225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_cardinalityConstraint5287 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_RBRACKET_in_cardinalityConstraint5291 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_cardinalityConstraint5303 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUS_in_cardinalityConstraint5330 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_cardinalityConstraint5357 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_literal5426 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_LITERAL_in_literal5436 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOATING_POINT_LITERAL_in_literal5446 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRUE_in_literal5456 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALSE_in_literal5470 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_literal5484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualident_in_typeName5511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_qualident5553 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_DOT_in_qualident5581 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_qualident5583 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_name_in_identifier5620 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_name5654 = new BitSet(new long[]{0x0000000000000002L});

}