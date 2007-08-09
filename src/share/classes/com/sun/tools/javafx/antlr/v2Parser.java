// $ANTLR 3.0 C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g 2007-08-09 09:33:49

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
    public String getGrammarFileName() { return "C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g"; }

    
            public v2Parser(Context context, CharSequence content) {
               this(new CommonTokenStream(new v2Lexer(new ANTLRStringStream(content.toString()))));
               initialize(context);
        	}



    // $ANTLR start module
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:320:1: module returns [JCCompilationUnit result] : ( packageDecl )? moduleItems EOF ;
    public final JCCompilationUnit module() throws RecognitionException {
        JCCompilationUnit result = null;

        JCExpression packageDecl1 = null;

        ListBuffer<JCTree> moduleItems2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:8: ( ( packageDecl )? moduleItems EOF )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:10: ( packageDecl )? moduleItems EOF
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:10: ( packageDecl )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==PACKAGE) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:10: packageDecl
                    {
                    pushFollow(FOLLOW_packageDecl_in_module2032);
                    packageDecl1=packageDecl();
                    _fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_moduleItems_in_module2035);
            moduleItems2=moduleItems();
            _fsp--;

            match(input,EOF,FOLLOW_EOF_in_module2037); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:322:1: packageDecl returns [JCExpression value] : PACKAGE qualident SEMI ;
    public final JCExpression packageDecl() throws RecognitionException {
        JCExpression value = null;

        JCExpression qualident3 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:323:8: ( PACKAGE qualident SEMI )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:323:10: PACKAGE qualident SEMI
            {
            match(input,PACKAGE,FOLLOW_PACKAGE_in_packageDecl2059); 
            pushFollow(FOLLOW_qualident_in_packageDecl2061);
            qualident3=qualident();
            _fsp--;

            match(input,SEMI,FOLLOW_SEMI_in_packageDecl2063); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:324:1: moduleItems returns [ListBuffer<JCTree> items = new ListBuffer<JCTree>()] : ( moduleItem )* ;
    public final ListBuffer<JCTree> moduleItems() throws RecognitionException {
        ListBuffer<JCTree> items =  new ListBuffer<JCTree>();

        JCTree moduleItem4 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:325:9: ( ( moduleItem )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:325:11: ( moduleItem )*
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:325:11: ( moduleItem )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=POUND && LA2_0<=TYPEOF)||LA2_0==ABSTRACT||LA2_0==BREAK||LA2_0==CLASS||(LA2_0>=RETURN && LA2_0<=VAR)||LA2_0==IMPORT||LA2_0==IF||(LA2_0>=THIS && LA2_0<=FALSE)||(LA2_0>=WHILE && LA2_0<=CONTINUE)||LA2_0==TRY||(LA2_0>=NOT && LA2_0<=READONLY)||LA2_0==SUPER||(LA2_0>=SIZEOF && LA2_0<=LPAREN)||LA2_0==SEMI||(LA2_0>=PLUSPLUS && LA2_0<=SUBSUB)||(LA2_0>=QUES && LA2_0<=STRING_LITERAL)||(LA2_0>=QUOTE_LBRACE_STRING_LITERAL && LA2_0<=LBRACE)||(LA2_0>=QUOTED_IDENTIFIER && LA2_0<=INTEGER_LITERAL)||LA2_0==FLOATING_POINT_LITERAL||LA2_0==IDENTIFIER) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:325:12: moduleItem
            	    {
            	    pushFollow(FOLLOW_moduleItem_in_moduleItems2094);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );
    public final JCTree moduleItem() throws RecognitionException {
        JCTree value = null;

        JCTree importDecl5 = null;

        JFXClassDeclaration classDefinition6 = null;

        JCStatement statement7 = null;

        JCExpression expression8 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:327:8: ( importDecl | classDefinition | statement | expression SEMI )
            int alt3=4;
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
                    int LA3_10 = input.LA(3);

                    if ( (LA3_10==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_10==CLASS) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA3_11 = input.LA(3);

                    if ( (LA3_11==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_11==CLASS) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA3_12 = input.LA(3);

                    if ( (LA3_12==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_12==CLASS) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 12, input);

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
                        new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 2, input);

                    throw nvae;
                }

                }
                break;
            case READONLY:
                {
                switch ( input.LA(2) ) {
                case PUBLIC:
                    {
                    int LA3_10 = input.LA(3);

                    if ( (LA3_10==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_10==CLASS) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA3_11 = input.LA(3);

                    if ( (LA3_11==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_11==CLASS) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA3_12 = input.LA(3);

                    if ( (LA3_12==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_12==CLASS) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 12, input);

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
                        new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 3, input);

                    throw nvae;
                }

                }
                break;
            case PUBLIC:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA3_13 = input.LA(3);

                    if ( (LA3_13==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_13==CLASS) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA3_14 = input.LA(3);

                    if ( (LA3_14==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_14==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 14, input);

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
                        new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 4, input);

                    throw nvae;
                }

                }
                break;
            case PRIVATE:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA3_13 = input.LA(3);

                    if ( (LA3_13==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_13==CLASS) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA3_14 = input.LA(3);

                    if ( (LA3_14==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_14==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 14, input);

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
                        new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 5, input);

                    throw nvae;
                }

                }
                break;
            case PROTECTED:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA3_13 = input.LA(3);

                    if ( (LA3_13==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_13==CLASS) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA3_14 = input.LA(3);

                    if ( (LA3_14==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_14==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 14, input);

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
                        new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 6, input);

                    throw nvae;
                }

                }
                break;
            case CLASS:
                {
                alt3=2;
                }
                break;
            case BREAK:
            case RETURN:
            case THROW:
            case VAR:
            case WHILE:
            case CONTINUE:
            case TRY:
            case FUNCTION:
            case SEMI:
                {
                alt3=3;
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
            case LBRACE:
            case QUOTED_IDENTIFIER:
            case INTEGER_LITERAL:
            case FLOATING_POINT_LITERAL:
            case IDENTIFIER:
                {
                alt3=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:327:10: importDecl
                    {
                    pushFollow(FOLLOW_importDecl_in_moduleItem2138);
                    importDecl5=importDecl();
                    _fsp--;

                     value = importDecl5; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:328:10: classDefinition
                    {
                    pushFollow(FOLLOW_classDefinition_in_moduleItem2154);
                    classDefinition6=classDefinition();
                    _fsp--;

                     value = classDefinition6; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:329:10: statement
                    {
                    pushFollow(FOLLOW_statement_in_moduleItem2170);
                    statement7=statement();
                    _fsp--;

                     value = statement7; 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:330:10: expression SEMI
                    {
                    pushFollow(FOLLOW_expression_in_moduleItem2192);
                    expression8=expression();
                    _fsp--;

                    match(input,SEMI,FOLLOW_SEMI_in_moduleItem2194); 
                     value = F.Exec(expression8); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:332:1: importDecl returns [JCTree value] : IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI ;
    public final JCTree importDecl() throws RecognitionException {
        JCTree value = null;

        Token STAR11=null;
        Token IMPORT12=null;
        JCIdent identifier9 = null;

        name_return name10 = null;


         JCExpression pid = null; 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:334:9: ( IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:334:11: IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI
            {
            IMPORT12=(Token)input.LT(1);
            match(input,IMPORT,FOLLOW_IMPORT_in_importDecl2231); 
            pushFollow(FOLLOW_identifier_in_importDecl2234);
            identifier9=identifier();
            _fsp--;

             pid = identifier9; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:335:18: ( DOT name )*
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
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:335:20: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_importDecl2259); 
            	    pushFollow(FOLLOW_name_in_importDecl2261);
            	    name10=name();
            	    _fsp--;

            	     pid = F.at(name10.pos).Select(pid, name10.value); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:336:18: ( DOT STAR )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==DOT) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:336:20: DOT STAR
                    {
                    match(input,DOT,FOLLOW_DOT_in_importDecl2290); 
                    STAR11=(Token)input.LT(1);
                    match(input,STAR,FOLLOW_STAR_in_importDecl2292); 
                     pid = F.at(pos(STAR11)).Select(pid, names.asterisk); 

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_importDecl2301); 
             value = F.at(pos(IMPORT12)).Import(pid, false); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:338:1: classDefinition returns [JFXClassDeclaration value] : modifierFlags CLASS name supers LBRACE classMembers RBRACE ;
    public final JFXClassDeclaration classDefinition() throws RecognitionException {
        JFXClassDeclaration value = null;

        Token CLASS13=null;
        JCModifiers modifierFlags14 = null;

        name_return name15 = null;

        ListBuffer<Name> supers16 = null;

        ListBuffer<JFXAbstractMember> classMembers17 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:339:2: ( modifierFlags CLASS name supers LBRACE classMembers RBRACE )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:339:4: modifierFlags CLASS name supers LBRACE classMembers RBRACE
            {
            pushFollow(FOLLOW_modifierFlags_in_classDefinition2332);
            modifierFlags14=modifierFlags();
            _fsp--;

            CLASS13=(Token)input.LT(1);
            match(input,CLASS,FOLLOW_CLASS_in_classDefinition2335); 
            pushFollow(FOLLOW_name_in_classDefinition2337);
            name15=name();
            _fsp--;

            pushFollow(FOLLOW_supers_in_classDefinition2339);
            supers16=supers();
            _fsp--;

            match(input,LBRACE,FOLLOW_LBRACE_in_classDefinition2341); 
            pushFollow(FOLLOW_classMembers_in_classDefinition2343);
            classMembers17=classMembers();
            _fsp--;

            match(input,RBRACE,FOLLOW_RBRACE_in_classDefinition2345); 
             value = F.at(pos(CLASS13)).ClassDeclaration(modifierFlags14, name15.value,
            	                                	                supers16.toList(), classMembers17.toList()); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:342:1: supers returns [ListBuffer<Name> names = new ListBuffer<Name>()] : ( EXTENDS name1= name ( COMMA namen= name )* )? ;
    public final ListBuffer<Name> supers() throws RecognitionException {
        ListBuffer<Name> names =  new ListBuffer<Name>();

        name_return name1 = null;

        name_return namen = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:343:2: ( ( EXTENDS name1= name ( COMMA namen= name )* )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:343:4: ( EXTENDS name1= name ( COMMA namen= name )* )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:343:4: ( EXTENDS name1= name ( COMMA namen= name )* )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==EXTENDS) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:343:5: EXTENDS name1= name ( COMMA namen= name )*
                    {
                    match(input,EXTENDS,FOLLOW_EXTENDS_in_supers2370); 
                    pushFollow(FOLLOW_name_in_supers2374);
                    name1=name();
                    _fsp--;

                     names.append(name1.value); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:344:12: ( COMMA namen= name )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==COMMA) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:344:14: COMMA namen= name
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_supers2402); 
                    	    pushFollow(FOLLOW_name_in_supers2406);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:346:1: classMembers returns [ListBuffer<JFXAbstractMember> mems = new ListBuffer<JFXAbstractMember>()] : ( attributeDefinition | functionDefinition )* ;
    public final ListBuffer<JFXAbstractMember> classMembers() throws RecognitionException {
        ListBuffer<JFXAbstractMember> mems =  new ListBuffer<JFXAbstractMember>();

        JFXAttributeDefinition attributeDefinition18 = null;

        JFXFunctionDefinition functionDefinition19 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:347:2: ( ( attributeDefinition | functionDefinition )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:347:3: ( attributeDefinition | functionDefinition )*
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:347:3: ( attributeDefinition | functionDefinition )*
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

                        if ( (LA8_9==FUNCTION) ) {
                            alt8=2;
                        }
                        else if ( (LA8_9==ATTRIBUTE) ) {
                            alt8=1;
                        }


                        }
                        break;
                    case PRIVATE:
                        {
                        int LA8_10 = input.LA(3);

                        if ( (LA8_10==ATTRIBUTE) ) {
                            alt8=1;
                        }
                        else if ( (LA8_10==FUNCTION) ) {
                            alt8=2;
                        }


                        }
                        break;
                    case PROTECTED:
                        {
                        int LA8_11 = input.LA(3);

                        if ( (LA8_11==FUNCTION) ) {
                            alt8=2;
                        }
                        else if ( (LA8_11==ATTRIBUTE) ) {
                            alt8=1;
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

                        if ( (LA8_9==FUNCTION) ) {
                            alt8=2;
                        }
                        else if ( (LA8_9==ATTRIBUTE) ) {
                            alt8=1;
                        }


                        }
                        break;
                    case PRIVATE:
                        {
                        int LA8_10 = input.LA(3);

                        if ( (LA8_10==ATTRIBUTE) ) {
                            alt8=1;
                        }
                        else if ( (LA8_10==FUNCTION) ) {
                            alt8=2;
                        }


                        }
                        break;
                    case PROTECTED:
                        {
                        int LA8_11 = input.LA(3);

                        if ( (LA8_11==FUNCTION) ) {
                            alt8=2;
                        }
                        else if ( (LA8_11==ATTRIBUTE) ) {
                            alt8=1;
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
                case PUBLIC:
                    {
                    switch ( input.LA(2) ) {
                    case ABSTRACT:
                        {
                        int LA8_12 = input.LA(3);

                        if ( (LA8_12==ATTRIBUTE) ) {
                            alt8=1;
                        }
                        else if ( (LA8_12==FUNCTION) ) {
                            alt8=2;
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
                case PRIVATE:
                    {
                    switch ( input.LA(2) ) {
                    case ABSTRACT:
                        {
                        int LA8_12 = input.LA(3);

                        if ( (LA8_12==ATTRIBUTE) ) {
                            alt8=1;
                        }
                        else if ( (LA8_12==FUNCTION) ) {
                            alt8=2;
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

                        if ( (LA8_12==ATTRIBUTE) ) {
                            alt8=1;
                        }
                        else if ( (LA8_12==FUNCTION) ) {
                            alt8=2;
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
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:347:5: attributeDefinition
            	    {
            	    pushFollow(FOLLOW_attributeDefinition_in_classMembers2440);
            	    attributeDefinition18=attributeDefinition();
            	    _fsp--;

            	     mems.append(attributeDefinition18); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:348:5: functionDefinition
            	    {
            	    pushFollow(FOLLOW_functionDefinition_in_classMembers2458);
            	    functionDefinition19=functionDefinition();
            	    _fsp--;

            	     mems.append(functionDefinition19); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:350:1: attributeDefinition returns [JFXAttributeDefinition def] : modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? SEMI ;
    public final JFXAttributeDefinition attributeDefinition() throws RecognitionException {
        JFXAttributeDefinition def = null;

        Token ATTRIBUTE20=null;
        JCModifiers modifierFlags21 = null;

        name_return name22 = null;

        JFXType typeReference23 = null;

        JFXMemberSelector inverseClause24 = null;

        JavafxBindStatus bindOpt25 = null;

        JCExpression expression26 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:351:2: ( modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? SEMI )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:351:4: modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_attributeDefinition2487);
            modifierFlags21=modifierFlags();
            _fsp--;

            ATTRIBUTE20=(Token)input.LT(1);
            match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeDefinition2489); 
            pushFollow(FOLLOW_name_in_attributeDefinition2491);
            name22=name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_attributeDefinition2499);
            typeReference23=typeReference();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:353:5: ( EQ bindOpt expression | inverseClause )?
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
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:353:6: EQ bindOpt expression
                    {
                    match(input,EQ,FOLLOW_EQ_in_attributeDefinition2507); 
                    pushFollow(FOLLOW_bindOpt_in_attributeDefinition2509);
                    bindOpt25=bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_attributeDefinition2511);
                    expression26=expression();
                    _fsp--;


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:353:30: inverseClause
                    {
                    pushFollow(FOLLOW_inverseClause_in_attributeDefinition2515);
                    inverseClause24=inverseClause();
                    _fsp--;


                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_attributeDefinition2525); 
             def = F.at(pos(ATTRIBUTE20)).AttributeDefinition(modifierFlags21,
            	    						name22.value, typeReference23, inverseClause24, null, 
            	    						bindOpt25, expression26); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:358:1: inverseClause returns [JFXMemberSelector inverse = null] : INVERSE memberSelector ;
    public final JFXMemberSelector inverseClause() throws RecognitionException {
        JFXMemberSelector inverse =  null;

        JFXMemberSelector memberSelector27 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:359:2: ( INVERSE memberSelector )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:359:4: INVERSE memberSelector
            {
            match(input,INVERSE,FOLLOW_INVERSE_in_inverseClause2551); 
            pushFollow(FOLLOW_memberSelector_in_inverseClause2553);
            memberSelector27=memberSelector();
            _fsp--;

             inverse = memberSelector27; 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:360:1: functionDefinition returns [JFXFunctionDefinition def] : modifierFlags FUNCTION name formalParameters typeReference blockExpression ;
    public final JFXFunctionDefinition functionDefinition() throws RecognitionException {
        JFXFunctionDefinition def = null;

        Token FUNCTION28=null;
        JCModifiers modifierFlags29 = null;

        name_return name30 = null;

        JFXType typeReference31 = null;

        ListBuffer<JCTree> formalParameters32 = null;

        JFXBlockExpression blockExpression33 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:361:2: ( modifierFlags FUNCTION name formalParameters typeReference blockExpression )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:361:4: modifierFlags FUNCTION name formalParameters typeReference blockExpression
            {
            pushFollow(FOLLOW_modifierFlags_in_functionDefinition2571);
            modifierFlags29=modifierFlags();
            _fsp--;

            FUNCTION28=(Token)input.LT(1);
            match(input,FUNCTION,FOLLOW_FUNCTION_in_functionDefinition2573); 
            pushFollow(FOLLOW_name_in_functionDefinition2575);
            name30=name();
            _fsp--;

            pushFollow(FOLLOW_formalParameters_in_functionDefinition2583);
            formalParameters32=formalParameters();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_functionDefinition2586);
            typeReference31=typeReference();
            _fsp--;

            pushFollow(FOLLOW_blockExpression_in_functionDefinition2595);
            blockExpression33=blockExpression();
            _fsp--;

             def = F.at(pos(FUNCTION28)).FunctionDefinition(modifierFlags29,
            	    						name30.value, typeReference31, 
            	    						formalParameters32.toList(), blockExpression33); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:367:1: modifierFlags returns [JCModifiers mods] : (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? ;
    public final JCModifiers modifierFlags() throws RecognitionException {
        JCModifiers mods = null;

        long om1 = 0;

        long am1 = 0;

        long am2 = 0;

        long om2 = 0;


         long flags = 0; 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:369:2: ( (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:369:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:369:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
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
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:369:6: om1= otherModifier (am1= accessModifier )?
                    {
                    pushFollow(FOLLOW_otherModifier_in_modifierFlags2623);
                    om1=otherModifier();
                    _fsp--;

                     flags |= om1; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:370:3: (am1= accessModifier )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( ((LA10_0>=PRIVATE && LA10_0<=PUBLIC)) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:370:5: am1= accessModifier
                            {
                            pushFollow(FOLLOW_accessModifier_in_modifierFlags2636);
                            am1=accessModifier();
                            _fsp--;

                             flags |= am1; 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:371:6: am2= accessModifier (om2= otherModifier )?
                    {
                    pushFollow(FOLLOW_accessModifier_in_modifierFlags2658);
                    am2=accessModifier();
                    _fsp--;

                     flags |= am2; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:372:3: (om2= otherModifier )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==ABSTRACT||LA11_0==READONLY) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:372:5: om2= otherModifier
                            {
                            pushFollow(FOLLOW_otherModifier_in_modifierFlags2671);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:375:1: accessModifier returns [long flags = 0] : ( PUBLIC | PRIVATE | PROTECTED ) ;
    public final long accessModifier() throws RecognitionException {
        long flags =  0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:376:2: ( ( PUBLIC | PRIVATE | PROTECTED ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:376:4: ( PUBLIC | PRIVATE | PROTECTED )
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:376:4: ( PUBLIC | PRIVATE | PROTECTED )
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
                    new NoViableAltException("376:4: ( PUBLIC | PRIVATE | PROTECTED )", 13, 0, input);

                throw nvae;
            }

            switch (alt13) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:376:5: PUBLIC
                    {
                    match(input,PUBLIC,FOLLOW_PUBLIC_in_accessModifier2719); 
                     flags |= Flags.PUBLIC; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:377:5: PRIVATE
                    {
                    match(input,PRIVATE,FOLLOW_PRIVATE_in_accessModifier2739); 
                     flags |= Flags.PUBLIC; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:378:5: PROTECTED
                    {
                    match(input,PROTECTED,FOLLOW_PROTECTED_in_accessModifier2758); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:379:1: otherModifier returns [long flags = 0] : ( ABSTRACT | READONLY ) ;
    public final long otherModifier() throws RecognitionException {
        long flags =  0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:380:2: ( ( ABSTRACT | READONLY ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:380:4: ( ABSTRACT | READONLY )
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:380:4: ( ABSTRACT | READONLY )
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
                    new NoViableAltException("380:4: ( ABSTRACT | READONLY )", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:380:5: ABSTRACT
                    {
                    match(input,ABSTRACT,FOLLOW_ABSTRACT_in_otherModifier2785); 
                     flags |= Flags.ABSTRACT; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:381:5: READONLY
                    {
                    match(input,READONLY,FOLLOW_READONLY_in_otherModifier2803); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:382:1: memberSelector returns [JFXMemberSelector value] : name1= name DOT name2= name ;
    public final JFXMemberSelector memberSelector() throws RecognitionException {
        JFXMemberSelector value = null;

        name_return name1 = null;

        name_return name2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:383:2: (name1= name DOT name2= name )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:383:4: name1= name DOT name2= name
            {
            pushFollow(FOLLOW_name_in_memberSelector2832);
            name1=name();
            _fsp--;

            match(input,DOT,FOLLOW_DOT_in_memberSelector2836); 
            pushFollow(FOLLOW_name_in_memberSelector2842);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:385:1: formalParameters returns [ListBuffer<JCTree> params = new ListBuffer<JCTree>()] : LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN ;
    public final ListBuffer<JCTree> formalParameters() throws RecognitionException {
        ListBuffer<JCTree> params =  new ListBuffer<JCTree>();

        JFXVar fp0 = null;

        JFXVar fpn = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:386:2: ( LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:386:4: LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_formalParameters2860); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:386:12: (fp0= formalParameter ( COMMA fpn= formalParameter )* )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==QUOTED_IDENTIFIER||LA16_0==IDENTIFIER) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:386:14: fp0= formalParameter ( COMMA fpn= formalParameter )*
                    {
                    pushFollow(FOLLOW_formalParameter_in_formalParameters2867);
                    fp0=formalParameter();
                    _fsp--;

                     params.append(fp0); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:387:12: ( COMMA fpn= formalParameter )*
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( (LA15_0==COMMA) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:387:14: COMMA fpn= formalParameter
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_formalParameters2885); 
                    	    pushFollow(FOLLOW_formalParameter_in_formalParameters2891);
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

            match(input,RPAREN,FOLLOW_RPAREN_in_formalParameters2902); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:389:1: formalParameter returns [JFXVar var] : name typeReference ;
    public final JFXVar formalParameter() throws RecognitionException {
        JFXVar var = null;

        name_return name34 = null;

        JFXType typeReference35 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:390:2: ( name typeReference )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:390:4: name typeReference
            {
            pushFollow(FOLLOW_name_in_formalParameter2917);
            name34=name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_formalParameter2919);
            typeReference35=typeReference();
            _fsp--;

             var = F.at(name34.pos).Var(name34.value, typeReference35, F.Modifiers(Flags.PARAMETER)); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:392:1: block returns [JCBlock value] : LBRACE ( statement | expression SEMI )* RBRACE ;
    public final JCBlock block() throws RecognitionException {
        JCBlock value = null;

        Token LBRACE38=null;
        JCStatement statement36 = null;

        JCExpression expression37 = null;


         ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>();
        	 	
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:395:2: ( LBRACE ( statement | expression SEMI )* RBRACE )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:395:4: LBRACE ( statement | expression SEMI )* RBRACE
            {
            LBRACE38=(Token)input.LT(1);
            match(input,LBRACE,FOLLOW_LBRACE_in_block2945); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:396:4: ( statement | expression SEMI )*
            loop17:
            do {
                int alt17=3;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==ABSTRACT||LA17_0==BREAK||(LA17_0>=RETURN && LA17_0<=VAR)||(LA17_0>=WHILE && LA17_0<=CONTINUE)||LA17_0==TRY||(LA17_0>=PRIVATE && LA17_0<=READONLY)||LA17_0==SEMI) ) {
                    alt17=1;
                }
                else if ( ((LA17_0>=POUND && LA17_0<=TYPEOF)||LA17_0==IF||(LA17_0>=THIS && LA17_0<=FALSE)||(LA17_0>=NOT && LA17_0<=NEW)||LA17_0==SUPER||(LA17_0>=SIZEOF && LA17_0<=LPAREN)||(LA17_0>=PLUSPLUS && LA17_0<=SUBSUB)||(LA17_0>=QUES && LA17_0<=STRING_LITERAL)||(LA17_0>=QUOTE_LBRACE_STRING_LITERAL && LA17_0<=LBRACE)||(LA17_0>=QUOTED_IDENTIFIER && LA17_0<=INTEGER_LITERAL)||LA17_0==FLOATING_POINT_LITERAL||LA17_0==IDENTIFIER) ) {
                    alt17=2;
                }


                switch (alt17) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:396:9: statement
            	    {
            	    pushFollow(FOLLOW_statement_in_block2955);
            	    statement36=statement();
            	    _fsp--;

            	     stats.append(statement36); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:397:9: expression SEMI
            	    {
            	    pushFollow(FOLLOW_expression_in_block2970);
            	    expression37=expression();
            	    _fsp--;

            	    match(input,SEMI,FOLLOW_SEMI_in_block2972); 
            	     stats.append(F.Exec(expression37)); 

            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

            match(input,RBRACE,FOLLOW_RBRACE_in_block2988); 
             value = F.at(pos(LBRACE38)).Block(0L, stats.toList()); 

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


    // $ANTLR start blockExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:401:1: blockExpression returns [JFXBlockExpression expr = null] : LBRACE ( statements[stats] )? RBRACE ;
    public final JFXBlockExpression blockExpression() throws RecognitionException {
        JFXBlockExpression expr =  null;

        Token LBRACE39=null;
        JCExpression statements40 = null;


         ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>(); 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:403:2: ( LBRACE ( statements[stats] )? RBRACE )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:403:4: LBRACE ( statements[stats] )? RBRACE
            {
            LBRACE39=(Token)input.LT(1);
            match(input,LBRACE,FOLLOW_LBRACE_in_blockExpression3012); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:403:11: ( statements[stats] )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( ((LA18_0>=POUND && LA18_0<=TYPEOF)||LA18_0==ABSTRACT||LA18_0==BREAK||(LA18_0>=RETURN && LA18_0<=VAR)||LA18_0==IF||(LA18_0>=THIS && LA18_0<=FALSE)||(LA18_0>=WHILE && LA18_0<=CONTINUE)||LA18_0==TRY||(LA18_0>=NOT && LA18_0<=READONLY)||LA18_0==SUPER||(LA18_0>=SIZEOF && LA18_0<=LPAREN)||LA18_0==SEMI||(LA18_0>=PLUSPLUS && LA18_0<=SUBSUB)||(LA18_0>=QUES && LA18_0<=STRING_LITERAL)||(LA18_0>=QUOTE_LBRACE_STRING_LITERAL && LA18_0<=LBRACE)||(LA18_0>=QUOTED_IDENTIFIER && LA18_0<=INTEGER_LITERAL)||LA18_0==FLOATING_POINT_LITERAL||LA18_0==IDENTIFIER) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:403:12: statements[stats]
                    {
                    pushFollow(FOLLOW_statements_in_blockExpression3015);
                    statements40=statements(stats);
                    _fsp--;


                    }
                    break;

            }

            match(input,RBRACE,FOLLOW_RBRACE_in_blockExpression3020); 
             expr = F.at(pos(LBRACE39)).BlockExpression(0L, stats.toList(), 
            						 					     statements40); 

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
    // $ANTLR end blockExpression


    // $ANTLR start statements
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:406:1: statements[ListBuffer<JCStatement> stats] returns [JCExpression expr = null] : ( statement (sts1= statements[stats] )? | expression ( SEMI (stsn= statements[stats] | ) | ) );
    public final JCExpression statements(ListBuffer<JCStatement> stats) throws RecognitionException {
        JCExpression expr =  null;

        JCExpression sts1 = null;

        JCExpression stsn = null;

        JCStatement statement41 = null;

        JCExpression expression42 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:407:2: ( statement (sts1= statements[stats] )? | expression ( SEMI (stsn= statements[stats] | ) | ) )
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==ABSTRACT||LA22_0==BREAK||(LA22_0>=RETURN && LA22_0<=VAR)||(LA22_0>=WHILE && LA22_0<=CONTINUE)||LA22_0==TRY||(LA22_0>=PRIVATE && LA22_0<=READONLY)||LA22_0==SEMI) ) {
                alt22=1;
            }
            else if ( ((LA22_0>=POUND && LA22_0<=TYPEOF)||LA22_0==IF||(LA22_0>=THIS && LA22_0<=FALSE)||(LA22_0>=NOT && LA22_0<=NEW)||LA22_0==SUPER||(LA22_0>=SIZEOF && LA22_0<=LPAREN)||(LA22_0>=PLUSPLUS && LA22_0<=SUBSUB)||(LA22_0>=QUES && LA22_0<=STRING_LITERAL)||(LA22_0>=QUOTE_LBRACE_STRING_LITERAL && LA22_0<=LBRACE)||(LA22_0>=QUOTED_IDENTIFIER && LA22_0<=INTEGER_LITERAL)||LA22_0==FLOATING_POINT_LITERAL||LA22_0==IDENTIFIER) ) {
                alt22=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("406:1: statements[ListBuffer<JCStatement> stats] returns [JCExpression expr = null] : ( statement (sts1= statements[stats] )? | expression ( SEMI (stsn= statements[stats] | ) | ) );", 22, 0, input);

                throw nvae;
            }
            switch (alt22) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:407:4: statement (sts1= statements[stats] )?
                    {
                    pushFollow(FOLLOW_statement_in_statements3039);
                    statement41=statement();
                    _fsp--;

                     stats.append(statement41); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:408:3: (sts1= statements[stats] )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( ((LA19_0>=POUND && LA19_0<=TYPEOF)||LA19_0==ABSTRACT||LA19_0==BREAK||(LA19_0>=RETURN && LA19_0<=VAR)||LA19_0==IF||(LA19_0>=THIS && LA19_0<=FALSE)||(LA19_0>=WHILE && LA19_0<=CONTINUE)||LA19_0==TRY||(LA19_0>=NOT && LA19_0<=READONLY)||LA19_0==SUPER||(LA19_0>=SIZEOF && LA19_0<=LPAREN)||LA19_0==SEMI||(LA19_0>=PLUSPLUS && LA19_0<=SUBSUB)||(LA19_0>=QUES && LA19_0<=STRING_LITERAL)||(LA19_0>=QUOTE_LBRACE_STRING_LITERAL && LA19_0<=LBRACE)||(LA19_0>=QUOTED_IDENTIFIER && LA19_0<=INTEGER_LITERAL)||LA19_0==FLOATING_POINT_LITERAL||LA19_0==IDENTIFIER) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:408:4: sts1= statements[stats]
                            {
                            pushFollow(FOLLOW_statements_in_statements3051);
                            sts1=statements(stats);
                            _fsp--;

                             expr = sts1; 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:409:4: expression ( SEMI (stsn= statements[stats] | ) | )
                    {
                    pushFollow(FOLLOW_expression_in_statements3063);
                    expression42=expression();
                    _fsp--;

                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:410:10: ( SEMI (stsn= statements[stats] | ) | )
                    int alt21=2;
                    int LA21_0 = input.LA(1);

                    if ( (LA21_0==SEMI) ) {
                        alt21=1;
                    }
                    else if ( (LA21_0==RBRACE) ) {
                        alt21=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("410:10: ( SEMI (stsn= statements[stats] | ) | )", 21, 0, input);

                        throw nvae;
                    }
                    switch (alt21) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:410:11: SEMI (stsn= statements[stats] | )
                            {
                            match(input,SEMI,FOLLOW_SEMI_in_statements3075); 
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:410:17: (stsn= statements[stats] | )
                            int alt20=2;
                            int LA20_0 = input.LA(1);

                            if ( ((LA20_0>=POUND && LA20_0<=TYPEOF)||LA20_0==ABSTRACT||LA20_0==BREAK||(LA20_0>=RETURN && LA20_0<=VAR)||LA20_0==IF||(LA20_0>=THIS && LA20_0<=FALSE)||(LA20_0>=WHILE && LA20_0<=CONTINUE)||LA20_0==TRY||(LA20_0>=NOT && LA20_0<=READONLY)||LA20_0==SUPER||(LA20_0>=SIZEOF && LA20_0<=LPAREN)||LA20_0==SEMI||(LA20_0>=PLUSPLUS && LA20_0<=SUBSUB)||(LA20_0>=QUES && LA20_0<=STRING_LITERAL)||(LA20_0>=QUOTE_LBRACE_STRING_LITERAL && LA20_0<=LBRACE)||(LA20_0>=QUOTED_IDENTIFIER && LA20_0<=INTEGER_LITERAL)||LA20_0==FLOATING_POINT_LITERAL||LA20_0==IDENTIFIER) ) {
                                alt20=1;
                            }
                            else if ( (LA20_0==RBRACE) ) {
                                alt20=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("410:17: (stsn= statements[stats] | )", 20, 0, input);

                                throw nvae;
                            }
                            switch (alt20) {
                                case 1 :
                                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:410:21: stsn= statements[stats]
                                    {
                                     stats.append(F.Exec(expression42)); 
                                    pushFollow(FOLLOW_statements_in_statements3098);
                                    stsn=statements(stats);
                                    _fsp--;

                                     expr = stsn; 

                                    }
                                    break;
                                case 2 :
                                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:412:30: 
                                    {
                                     expr = expression42; 

                                    }
                                    break;

                            }


                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:414:20: 
                            {
                             expr = expression42; 

                            }
                            break;

                    }


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
    // $ANTLR end statements


    // $ANTLR start statement
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:417:1: statement returns [JCStatement value] : ( variableDeclaration SEMI | functionDefinition | WHILE LPAREN expression RPAREN block | BREAK SEMI | CONTINUE SEMI | THROW expression SEMI | returnStatement SEMI | tryStatement | SEMI );
    public final JCStatement statement() throws RecognitionException {
        JCStatement value = null;

        Token WHILE45=null;
        Token BREAK48=null;
        Token CONTINUE49=null;
        Token THROW50=null;
        Token SEMI54=null;
        JCStatement variableDeclaration43 = null;

        JFXFunctionDefinition functionDefinition44 = null;

        JCExpression expression46 = null;

        JCBlock block47 = null;

        JCExpression expression51 = null;

        JCStatement returnStatement52 = null;

        JCStatement tryStatement53 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:418:2: ( variableDeclaration SEMI | functionDefinition | WHILE LPAREN expression RPAREN block | BREAK SEMI | CONTINUE SEMI | THROW expression SEMI | returnStatement SEMI | tryStatement | SEMI )
            int alt23=9;
            switch ( input.LA(1) ) {
            case VAR:
                {
                alt23=1;
                }
                break;
            case ABSTRACT:
            case PRIVATE:
            case PROTECTED:
            case PUBLIC:
            case FUNCTION:
            case READONLY:
                {
                alt23=2;
                }
                break;
            case WHILE:
                {
                alt23=3;
                }
                break;
            case BREAK:
                {
                alt23=4;
                }
                break;
            case CONTINUE:
                {
                alt23=5;
                }
                break;
            case THROW:
                {
                alt23=6;
                }
                break;
            case RETURN:
                {
                alt23=7;
                }
                break;
            case TRY:
                {
                alt23=8;
                }
                break;
            case SEMI:
                {
                alt23=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("417:1: statement returns [JCStatement value] : ( variableDeclaration SEMI | functionDefinition | WHILE LPAREN expression RPAREN block | BREAK SEMI | CONTINUE SEMI | THROW expression SEMI | returnStatement SEMI | tryStatement | SEMI );", 23, 0, input);

                throw nvae;
            }

            switch (alt23) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:418:4: variableDeclaration SEMI
                    {
                    pushFollow(FOLLOW_variableDeclaration_in_statement3156);
                    variableDeclaration43=variableDeclaration();
                    _fsp--;

                    match(input,SEMI,FOLLOW_SEMI_in_statement3158); 
                     value = variableDeclaration43; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:419:4: functionDefinition
                    {
                    pushFollow(FOLLOW_functionDefinition_in_statement3166);
                    functionDefinition44=functionDefinition();
                    _fsp--;

                     value = functionDefinition44; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:420:11: WHILE LPAREN expression RPAREN block
                    {
                    WHILE45=(Token)input.LT(1);
                    match(input,WHILE,FOLLOW_WHILE_in_statement3182); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_statement3184); 
                    pushFollow(FOLLOW_expression_in_statement3186);
                    expression46=expression();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_statement3188); 
                    pushFollow(FOLLOW_block_in_statement3190);
                    block47=block();
                    _fsp--;

                     value = F.at(pos(WHILE45)).WhileLoop(expression46, block47); 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:421:4: BREAK SEMI
                    {
                    BREAK48=(Token)input.LT(1);
                    match(input,BREAK,FOLLOW_BREAK_in_statement3197); 
                    match(input,SEMI,FOLLOW_SEMI_in_statement3199); 
                     value = F.at(pos(BREAK48)).Break(null); 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:422:4: CONTINUE SEMI
                    {
                    CONTINUE49=(Token)input.LT(1);
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_statement3212); 
                    match(input,SEMI,FOLLOW_SEMI_in_statement3215); 
                     value = F.at(pos(CONTINUE49)).Continue(null); 

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:423:11: THROW expression SEMI
                    {
                    THROW50=(Token)input.LT(1);
                    match(input,THROW,FOLLOW_THROW_in_statement3233); 
                    pushFollow(FOLLOW_expression_in_statement3235);
                    expression51=expression();
                    _fsp--;

                    match(input,SEMI,FOLLOW_SEMI_in_statement3237); 
                     value = F.at(pos(THROW50)).Throw(expression51); 

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:424:11: returnStatement SEMI
                    {
                    pushFollow(FOLLOW_returnStatement_in_statement3257);
                    returnStatement52=returnStatement();
                    _fsp--;

                    match(input,SEMI,FOLLOW_SEMI_in_statement3259); 
                     value = returnStatement52; 

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:425:11: tryStatement
                    {
                    pushFollow(FOLLOW_tryStatement_in_statement3275);
                    tryStatement53=tryStatement();
                    _fsp--;

                     value = tryStatement53; 

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:426:4: SEMI
                    {
                    SEMI54=(Token)input.LT(1);
                    match(input,SEMI,FOLLOW_SEMI_in_statement3286); 
                     value = F.at(pos(SEMI54)).Skip(); 

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


    // $ANTLR start variableDeclaration
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:428:1: variableDeclaration returns [JCStatement value] : VAR name typeReference ( EQ bindOpt expression | ) ;
    public final JCStatement variableDeclaration() throws RecognitionException {
        JCStatement value = null;

        Token VAR55=null;
        name_return name56 = null;

        JFXType typeReference57 = null;

        JCExpression expression58 = null;

        JavafxBindStatus bindOpt59 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:429:2: ( VAR name typeReference ( EQ bindOpt expression | ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:429:4: VAR name typeReference ( EQ bindOpt expression | )
            {
            VAR55=(Token)input.LT(1);
            match(input,VAR,FOLLOW_VAR_in_variableDeclaration3316); 
            pushFollow(FOLLOW_name_in_variableDeclaration3319);
            name56=name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_variableDeclaration3322);
            typeReference57=typeReference();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:430:6: ( EQ bindOpt expression | )
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==EQ) ) {
                alt24=1;
            }
            else if ( (LA24_0==SEMI) ) {
                alt24=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("430:6: ( EQ bindOpt expression | )", 24, 0, input);

                throw nvae;
            }
            switch (alt24) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:430:8: EQ bindOpt expression
                    {
                    match(input,EQ,FOLLOW_EQ_in_variableDeclaration3333); 
                    pushFollow(FOLLOW_bindOpt_in_variableDeclaration3335);
                    bindOpt59=bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_variableDeclaration3338);
                    expression58=expression();
                    _fsp--;

                     value = F.at(pos(VAR55)).VarInit(name56.value, typeReference57, 
                    	    							expression58, bindOpt59); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:432:13: 
                    {
                     value = F.at(pos(VAR55)).VarStatement(name56.value, typeReference57); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:435:1: bindOpt returns [JavafxBindStatus status = UNBOUND] : ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? ;
    public final JavafxBindStatus bindOpt() throws RecognitionException {
        JavafxBindStatus status =  UNBOUND;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:436:2: ( ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:436:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:436:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
            int alt28=4;
            switch ( input.LA(1) ) {
                case BIND:
                    {
                    alt28=1;
                    }
                    break;
                case STAYS:
                    {
                    alt28=2;
                    }
                    break;
                case TIE:
                    {
                    alt28=3;
                    }
                    break;
            }

            switch (alt28) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:436:6: BIND ( LAZY )?
                    {
                    match(input,BIND,FOLLOW_BIND_in_bindOpt3388); 
                     status = UNIDIBIND; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:437:8: ( LAZY )?
                    int alt25=2;
                    int LA25_0 = input.LA(1);

                    if ( (LA25_0==LAZY) ) {
                        alt25=1;
                    }
                    switch (alt25) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:437:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3404); 
                             status = LAZY_UNIDIBIND; 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:438:6: STAYS ( LAZY )?
                    {
                    match(input,STAYS,FOLLOW_STAYS_in_bindOpt3419); 
                     status = UNIDIBIND; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:439:8: ( LAZY )?
                    int alt26=2;
                    int LA26_0 = input.LA(1);

                    if ( (LA26_0==LAZY) ) {
                        alt26=1;
                    }
                    switch (alt26) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:439:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3435); 
                             status = LAZY_UNIDIBIND; 

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:440:6: TIE ( LAZY )?
                    {
                    match(input,TIE,FOLLOW_TIE_in_bindOpt3450); 
                     status = BIDIBIND; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:441:8: ( LAZY )?
                    int alt27=2;
                    int LA27_0 = input.LA(1);

                    if ( (LA27_0==LAZY) ) {
                        alt27=1;
                    }
                    switch (alt27) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:441:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3466); 
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


    // $ANTLR start returnStatement
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:443:1: returnStatement returns [JCStatement value] : RETURN ( expression )? ;
    public final JCStatement returnStatement() throws RecognitionException {
        JCStatement value = null;

        Token RETURN61=null;
        JCExpression expression60 = null;


         JCExpression expr = null; 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:445:2: ( RETURN ( expression )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:445:4: RETURN ( expression )?
            {
            RETURN61=(Token)input.LT(1);
            match(input,RETURN,FOLLOW_RETURN_in_returnStatement3500); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:445:11: ( expression )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( ((LA29_0>=POUND && LA29_0<=TYPEOF)||LA29_0==IF||(LA29_0>=THIS && LA29_0<=FALSE)||(LA29_0>=NOT && LA29_0<=NEW)||LA29_0==SUPER||(LA29_0>=SIZEOF && LA29_0<=LPAREN)||(LA29_0>=PLUSPLUS && LA29_0<=SUBSUB)||(LA29_0>=QUES && LA29_0<=STRING_LITERAL)||(LA29_0>=QUOTE_LBRACE_STRING_LITERAL && LA29_0<=LBRACE)||(LA29_0>=QUOTED_IDENTIFIER && LA29_0<=INTEGER_LITERAL)||LA29_0==FLOATING_POINT_LITERAL||LA29_0==IDENTIFIER) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:445:12: expression
                    {
                    pushFollow(FOLLOW_expression_in_returnStatement3503);
                    expression60=expression();
                    _fsp--;

                     expr = expression60; 

                    }
                    break;

            }

             value = F.at(pos(RETURN61)).Return(expr); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:448:1: tryStatement returns [JCStatement value] : TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? ) ;
    public final JCStatement tryStatement() throws RecognitionException {
        JCStatement value = null;

        Token TRY63=null;
        JCBlock tb = null;

        JCBlock fb1 = null;

        JCBlock fb2 = null;

        JCCatch catchClause62 = null;


        	JCBlock body;
        		ListBuffer<JCCatch> catchers = new ListBuffer<JCCatch>();
        		JCBlock finalBlock = null;
        	
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:453:2: ( TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:453:4: TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )
            {
            TRY63=(Token)input.LT(1);
            match(input,TRY,FOLLOW_TRY_in_tryStatement3546); 
            pushFollow(FOLLOW_block_in_tryStatement3552);
            tb=block();
            _fsp--;

             body = tb; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:454:5: ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==FINALLY) ) {
                alt32=1;
            }
            else if ( (LA32_0==CATCH) ) {
                alt32=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("454:5: ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )", 32, 0, input);

                throw nvae;
            }
            switch (alt32) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:454:7: FINALLY fb1= block
                    {
                    match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement3565); 
                    pushFollow(FOLLOW_block_in_tryStatement3571);
                    fb1=block();
                    _fsp--;

                     finalBlock = fb1; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:455:10: ( catchClause )+ ( FINALLY fb2= block )?
                    {
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:455:10: ( catchClause )+
                    int cnt30=0;
                    loop30:
                    do {
                        int alt30=2;
                        int LA30_0 = input.LA(1);

                        if ( (LA30_0==CATCH) ) {
                            alt30=1;
                        }


                        switch (alt30) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:455:11: catchClause
                    	    {
                    	    pushFollow(FOLLOW_catchClause_in_tryStatement3587);
                    	    catchClause62=catchClause();
                    	    _fsp--;

                    	     catchers.append(catchClause62); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt30 >= 1 ) break loop30;
                                EarlyExitException eee =
                                    new EarlyExitException(30, input);
                                throw eee;
                        }
                        cnt30++;
                    } while (true);

                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:456:10: ( FINALLY fb2= block )?
                    int alt31=2;
                    int LA31_0 = input.LA(1);

                    if ( (LA31_0==FINALLY) ) {
                        alt31=1;
                    }
                    switch (alt31) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:456:11: FINALLY fb2= block
                            {
                            match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement3610); 
                            pushFollow(FOLLOW_block_in_tryStatement3615);
                            fb2=block();
                            _fsp--;

                             finalBlock = fb2; 

                            }
                            break;

                    }


                    }
                    break;

            }

             value = F.at(pos(TRY63)).Try(body, catchers.toList(), finalBlock); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:459:1: catchClause returns [JCCatch value] : CATCH LPAREN name ( COLON identifier )? RPAREN block ;
    public final JCCatch catchClause() throws RecognitionException {
        JCCatch value = null;

        Token CATCH66=null;
        name_return name64 = null;

        JCIdent identifier65 = null;

        JCBlock block67 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:460:2: ( CATCH LPAREN name ( COLON identifier )? RPAREN block )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:460:4: CATCH LPAREN name ( COLON identifier )? RPAREN block
            {
            CATCH66=(Token)input.LT(1);
            match(input,CATCH,FOLLOW_CATCH_in_catchClause3654); 
            match(input,LPAREN,FOLLOW_LPAREN_in_catchClause3657); 
            pushFollow(FOLLOW_name_in_catchClause3660);
            name64=name();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:461:4: ( COLON identifier )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==COLON) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:461:5: COLON identifier
                    {
                    match(input,COLON,FOLLOW_COLON_in_catchClause3668); 
                    pushFollow(FOLLOW_identifier_in_catchClause3670);
                    identifier65=identifier();
                    _fsp--;


                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_catchClause3679); 
            pushFollow(FOLLOW_block_in_catchClause3683);
            block67=block();
            _fsp--;

             JCModifiers mods = F.at(name64.pos).Modifiers(Flags.PARAMETER);
            	  					  JCVariableDecl formal = F.at(name64.pos).VarDef(mods, name64.value, identifier65, null);
            	  					  value = F.at(pos(CATCH66)).Catch(formal, block67); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:466:1: expression returns [JCExpression expr] : ( ifExpression | suffixedExpression );
    public final JCExpression expression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression ifExpression68 = null;

        JCExpression suffixedExpression69 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:467:9: ( ifExpression | suffixedExpression )
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==IF) ) {
                alt34=1;
            }
            else if ( ((LA34_0>=POUND && LA34_0<=TYPEOF)||(LA34_0>=THIS && LA34_0<=FALSE)||(LA34_0>=NOT && LA34_0<=NEW)||LA34_0==SUPER||(LA34_0>=SIZEOF && LA34_0<=LPAREN)||(LA34_0>=PLUSPLUS && LA34_0<=SUBSUB)||(LA34_0>=QUES && LA34_0<=STRING_LITERAL)||(LA34_0>=QUOTE_LBRACE_STRING_LITERAL && LA34_0<=LBRACE)||(LA34_0>=QUOTED_IDENTIFIER && LA34_0<=INTEGER_LITERAL)||LA34_0==FLOATING_POINT_LITERAL||LA34_0==IDENTIFIER) ) {
                alt34=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("466:1: expression returns [JCExpression expr] : ( ifExpression | suffixedExpression );", 34, 0, input);

                throw nvae;
            }
            switch (alt34) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:467:11: ifExpression
                    {
                    pushFollow(FOLLOW_ifExpression_in_expression3711);
                    ifExpression68=ifExpression();
                    _fsp--;

                     expr = ifExpression68; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:468:11: suffixedExpression
                    {
                    pushFollow(FOLLOW_suffixedExpression_in_expression3734);
                    suffixedExpression69=suffixedExpression();
                    _fsp--;

                     expr = suffixedExpression69; 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:471:1: ifExpression returns [JCExpression expr] : IF econd= expression THEN ethen= expression ELSE eelse= expression ;
    public final JCExpression ifExpression() throws RecognitionException {
        JCExpression expr = null;

        Token IF70=null;
        JCExpression econd = null;

        JCExpression ethen = null;

        JCExpression eelse = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:472:2: ( IF econd= expression THEN ethen= expression ELSE eelse= expression )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:472:4: IF econd= expression THEN ethen= expression ELSE eelse= expression
            {
            IF70=(Token)input.LT(1);
            match(input,IF,FOLLOW_IF_in_ifExpression3765); 
            pushFollow(FOLLOW_expression_in_ifExpression3769);
            econd=expression();
            _fsp--;

            match(input,THEN,FOLLOW_THEN_in_ifExpression3773); 
            pushFollow(FOLLOW_expression_in_ifExpression3778);
            ethen=expression();
            _fsp--;

            match(input,ELSE,FOLLOW_ELSE_in_ifExpression3786); 
            pushFollow(FOLLOW_expression_in_ifExpression3791);
            eelse=expression();
            _fsp--;

             expr = F.at(pos(IF70)).Conditional(econd, ethen, eelse); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:475:1: suffixedExpression returns [JCExpression expr] : e1= assignmentExpression ( PLUSPLUS | SUBSUB )? ;
    public final JCExpression suffixedExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:476:2: (e1= assignmentExpression ( PLUSPLUS | SUBSUB )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:476:4: e1= assignmentExpression ( PLUSPLUS | SUBSUB )?
            {
            pushFollow(FOLLOW_assignmentExpression_in_suffixedExpression3814);
            e1=assignmentExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:477:5: ( PLUSPLUS | SUBSUB )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==PLUSPLUS||LA35_0==SUBSUB) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:
                    {
                    if ( input.LA(1)==PLUSPLUS||input.LA(1)==SUBSUB ) {
                        input.consume();
                        errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse =
                            new MismatchedSetException(null,input);
                        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_suffixedExpression3825);    throw mse;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:479:1: assignmentExpression returns [JCExpression expr] : e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? ;
    public final JCExpression assignmentExpression() throws RecognitionException {
        JCExpression expr = null;

        Token EQ71=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:480:2: (e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:480:4: e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )?
            {
            pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression3854);
            e1=assignmentOpExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:481:5: ( EQ e2= assignmentOpExpression )?
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==EQ) ) {
                alt36=1;
            }
            switch (alt36) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:481:9: EQ e2= assignmentOpExpression
                    {
                    EQ71=(Token)input.LT(1);
                    match(input,EQ,FOLLOW_EQ_in_assignmentExpression3869); 
                    pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression3875);
                    e2=assignmentOpExpression();
                    _fsp--;

                     expr = F.at(pos(EQ71)).Assign(expr, e2); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:482:1: assignmentOpExpression returns [JCExpression expr] : e1= andExpression ( assignmentOperator e2= andExpression )? ;
    public final JCExpression assignmentOpExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;

        JCExpression e2 = null;

        int assignmentOperator72 = 0;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:483:2: (e1= andExpression ( assignmentOperator e2= andExpression )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:483:4: e1= andExpression ( assignmentOperator e2= andExpression )?
            {
            pushFollow(FOLLOW_andExpression_in_assignmentOpExpression3902);
            e1=andExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:484:5: ( assignmentOperator e2= andExpression )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( ((LA37_0>=PLUSEQ && LA37_0<=PERCENTEQ)) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:484:9: assignmentOperator e2= andExpression
                    {
                    pushFollow(FOLLOW_assignmentOperator_in_assignmentOpExpression3918);
                    assignmentOperator72=assignmentOperator();
                    _fsp--;

                    pushFollow(FOLLOW_andExpression_in_assignmentOpExpression3924);
                    e2=andExpression();
                    _fsp--;

                     expr = F.Assignop(assignmentOperator72,
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:486:1: andExpression returns [JCExpression expr] : e1= orExpression ( AND e2= orExpression )* ;
    public final JCExpression andExpression() throws RecognitionException {
        JCExpression expr = null;

        Token AND73=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:487:2: (e1= orExpression ( AND e2= orExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:487:4: e1= orExpression ( AND e2= orExpression )*
            {
            pushFollow(FOLLOW_orExpression_in_andExpression3950);
            e1=orExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:488:5: ( AND e2= orExpression )*
            loop38:
            do {
                int alt38=2;
                int LA38_0 = input.LA(1);

                if ( (LA38_0==AND) ) {
                    alt38=1;
                }


                switch (alt38) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:488:9: AND e2= orExpression
            	    {
            	    AND73=(Token)input.LT(1);
            	    match(input,AND,FOLLOW_AND_in_andExpression3966); 
            	    pushFollow(FOLLOW_orExpression_in_andExpression3972);
            	    e2=orExpression();
            	    _fsp--;

            	     expr = F.at(pos(AND73)).Binary(JCTree.AND, expr, e2); 

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
    // $ANTLR end andExpression


    // $ANTLR start orExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:489:1: orExpression returns [JCExpression expr] : e1= instanceOfExpression ( OR e2= instanceOfExpression )* ;
    public final JCExpression orExpression() throws RecognitionException {
        JCExpression expr = null;

        Token OR74=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:490:2: (e1= instanceOfExpression ( OR e2= instanceOfExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:490:4: e1= instanceOfExpression ( OR e2= instanceOfExpression )*
            {
            pushFollow(FOLLOW_instanceOfExpression_in_orExpression4000);
            e1=instanceOfExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:491:5: ( OR e2= instanceOfExpression )*
            loop39:
            do {
                int alt39=2;
                int LA39_0 = input.LA(1);

                if ( (LA39_0==OR) ) {
                    alt39=1;
                }


                switch (alt39) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:491:9: OR e2= instanceOfExpression
            	    {
            	    OR74=(Token)input.LT(1);
            	    match(input,OR,FOLLOW_OR_in_orExpression4015); 
            	    pushFollow(FOLLOW_instanceOfExpression_in_orExpression4021);
            	    e2=instanceOfExpression();
            	    _fsp--;

            	     expr = F.at(pos(OR74)).Binary(JCTree.OR, expr, e2); 

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
    // $ANTLR end orExpression


    // $ANTLR start instanceOfExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:492:1: instanceOfExpression returns [JCExpression expr] : e1= relationalExpression ( INSTANCEOF identifier )? ;
    public final JCExpression instanceOfExpression() throws RecognitionException {
        JCExpression expr = null;

        Token INSTANCEOF75=null;
        JCExpression e1 = null;

        JCIdent identifier76 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:493:2: (e1= relationalExpression ( INSTANCEOF identifier )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:493:4: e1= relationalExpression ( INSTANCEOF identifier )?
            {
            pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression4049);
            e1=relationalExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:494:5: ( INSTANCEOF identifier )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==INSTANCEOF) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:494:9: INSTANCEOF identifier
                    {
                    INSTANCEOF75=(Token)input.LT(1);
                    match(input,INSTANCEOF,FOLLOW_INSTANCEOF_in_instanceOfExpression4064); 
                    pushFollow(FOLLOW_identifier_in_instanceOfExpression4066);
                    identifier76=identifier();
                    _fsp--;

                     expr = F.at(pos(INSTANCEOF75)).Binary(JCTree.TYPETEST, expr, 
                    	   													 identifier76); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:496:1: relationalExpression returns [JCExpression expr] : e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* ;
    public final JCExpression relationalExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LTGT77=null;
        Token EQEQ78=null;
        Token LTEQ79=null;
        Token GTEQ80=null;
        Token LT81=null;
        Token GT82=null;
        Token IN83=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:497:2: (e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:497:4: e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            {
            pushFollow(FOLLOW_additiveExpression_in_relationalExpression4094);
            e1=additiveExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:498:5: ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            loop41:
            do {
                int alt41=8;
                switch ( input.LA(1) ) {
                case LTGT:
                    {
                    alt41=1;
                    }
                    break;
                case EQEQ:
                    {
                    alt41=2;
                    }
                    break;
                case LTEQ:
                    {
                    alt41=3;
                    }
                    break;
                case GTEQ:
                    {
                    alt41=4;
                    }
                    break;
                case LT:
                    {
                    alt41=5;
                    }
                    break;
                case GT:
                    {
                    alt41=6;
                    }
                    break;
                case IN:
                    {
                    alt41=7;
                    }
                    break;

                }

                switch (alt41) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:498:9: LTGT e= additiveExpression
            	    {
            	    LTGT77=(Token)input.LT(1);
            	    match(input,LTGT,FOLLOW_LTGT_in_relationalExpression4110); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4116);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LTGT77)).Binary(JCTree.NE, expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:499:9: EQEQ e= additiveExpression
            	    {
            	    EQEQ78=(Token)input.LT(1);
            	    match(input,EQEQ,FOLLOW_EQEQ_in_relationalExpression4130); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4136);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(EQEQ78)).Binary(JCTree.EQ, expr, e); 

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:500:9: LTEQ e= additiveExpression
            	    {
            	    LTEQ79=(Token)input.LT(1);
            	    match(input,LTEQ,FOLLOW_LTEQ_in_relationalExpression4150); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4156);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LTEQ79)).Binary(JCTree.LE, expr, e); 

            	    }
            	    break;
            	case 4 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:501:9: GTEQ e= additiveExpression
            	    {
            	    GTEQ80=(Token)input.LT(1);
            	    match(input,GTEQ,FOLLOW_GTEQ_in_relationalExpression4170); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4176);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(GTEQ80)).Binary(JCTree.GE, expr, e); 

            	    }
            	    break;
            	case 5 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:502:9: LT e= additiveExpression
            	    {
            	    LT81=(Token)input.LT(1);
            	    match(input,LT,FOLLOW_LT_in_relationalExpression4190); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4198);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LT81))  .Binary(JCTree.LT, expr, e); 

            	    }
            	    break;
            	case 6 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:503:9: GT e= additiveExpression
            	    {
            	    GT82=(Token)input.LT(1);
            	    match(input,GT,FOLLOW_GT_in_relationalExpression4212); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4220);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(GT82))  .Binary(JCTree.GT, expr, e); 

            	    }
            	    break;
            	case 7 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:504:9: IN e= additiveExpression
            	    {
            	    IN83=(Token)input.LT(1);
            	    match(input,IN,FOLLOW_IN_in_relationalExpression4234); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4242);
            	    e=additiveExpression();
            	    _fsp--;

            	     /* expr = F.at(pos(IN83  )).Binary(JavaFXTag.IN, expr, $e2.expr); */ 

            	    }
            	    break;

            	default :
            	    break loop41;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:506:1: additiveExpression returns [JCExpression expr] : e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* ;
    public final JCExpression additiveExpression() throws RecognitionException {
        JCExpression expr = null;

        Token PLUS84=null;
        Token SUB85=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:507:2: (e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:507:4: e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            {
            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4271);
            e1=multiplicativeExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:508:5: ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            loop42:
            do {
                int alt42=3;
                int LA42_0 = input.LA(1);

                if ( (LA42_0==PLUS) ) {
                    alt42=1;
                }
                else if ( (LA42_0==SUB) ) {
                    alt42=2;
                }


                switch (alt42) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:508:9: PLUS e= multiplicativeExpression
            	    {
            	    PLUS84=(Token)input.LT(1);
            	    match(input,PLUS,FOLLOW_PLUS_in_additiveExpression4286); 
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4292);
            	    e=multiplicativeExpression();
            	    _fsp--;

            	     expr = F.at(pos(PLUS84)).Binary(JCTree.PLUS , expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:509:9: SUB e= multiplicativeExpression
            	    {
            	    SUB85=(Token)input.LT(1);
            	    match(input,SUB,FOLLOW_SUB_in_additiveExpression4305); 
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4312);
            	    e=multiplicativeExpression();
            	    _fsp--;

            	     expr = F.at(pos(SUB85)) .Binary(JCTree.MINUS, expr, e); 

            	    }
            	    break;

            	default :
            	    break loop42;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:511:1: multiplicativeExpression returns [JCExpression expr] : e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* ;
    public final JCExpression multiplicativeExpression() throws RecognitionException {
        JCExpression expr = null;

        Token STAR86=null;
        Token SLASH87=null;
        Token PERCENT88=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:512:2: (e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:512:4: e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            {
            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4340);
            e1=unaryExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:513:5: ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            loop43:
            do {
                int alt43=4;
                switch ( input.LA(1) ) {
                case STAR:
                    {
                    alt43=1;
                    }
                    break;
                case SLASH:
                    {
                    alt43=2;
                    }
                    break;
                case PERCENT:
                    {
                    alt43=3;
                    }
                    break;

                }

                switch (alt43) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:513:9: STAR e= unaryExpression
            	    {
            	    STAR86=(Token)input.LT(1);
            	    match(input,STAR,FOLLOW_STAR_in_multiplicativeExpression4356); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4363);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(STAR86))   .Binary(JCTree.MUL  , expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:514:9: SLASH e= unaryExpression
            	    {
            	    SLASH87=(Token)input.LT(1);
            	    match(input,SLASH,FOLLOW_SLASH_in_multiplicativeExpression4377); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4383);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(SLASH87))  .Binary(JCTree.DIV  , expr, e); 

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:515:9: PERCENT e= unaryExpression
            	    {
            	    PERCENT88=(Token)input.LT(1);
            	    match(input,PERCENT,FOLLOW_PERCENT_in_multiplicativeExpression4397); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4401);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(PERCENT88)).Binary(JCTree.MOD  , expr, e); 

            	    }
            	    break;

            	default :
            	    break loop43;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:517:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );
    public final JCExpression unaryExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression postfixExpression89 = null;

        int unaryOperator90 = 0;

        JCExpression postfixExpression91 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:518:2: ( postfixExpression | unaryOperator postfixExpression )
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( ((LA44_0>=THIS && LA44_0<=FALSE)||LA44_0==NEW||LA44_0==SUPER||LA44_0==LPAREN||LA44_0==STRING_LITERAL||(LA44_0>=QUOTE_LBRACE_STRING_LITERAL && LA44_0<=LBRACE)||(LA44_0>=QUOTED_IDENTIFIER && LA44_0<=INTEGER_LITERAL)||LA44_0==FLOATING_POINT_LITERAL||LA44_0==IDENTIFIER) ) {
                alt44=1;
            }
            else if ( ((LA44_0>=POUND && LA44_0<=TYPEOF)||LA44_0==NOT||(LA44_0>=SIZEOF && LA44_0<=REVERSE)||(LA44_0>=PLUSPLUS && LA44_0<=SUBSUB)||LA44_0==QUES) ) {
                alt44=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("517:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );", 44, 0, input);

                throw nvae;
            }
            switch (alt44) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:518:4: postfixExpression
                    {
                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression4431);
                    postfixExpression89=postfixExpression();
                    _fsp--;

                     expr = postfixExpression89; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:519:4: unaryOperator postfixExpression
                    {
                    pushFollow(FOLLOW_unaryOperator_in_unaryExpression4442);
                    unaryOperator90=unaryOperator();
                    _fsp--;

                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression4446);
                    postfixExpression91=postfixExpression();
                    _fsp--;

                     expr = F.Unary(unaryOperator90, postfixExpression91); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:521:1: postfixExpression returns [JCExpression expr] : primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* ;
    public final JCExpression postfixExpression() throws RecognitionException {
        JCExpression expr = null;

        Token DOT93=null;
        Token LPAREN94=null;
        name_return name1 = null;

        JCExpression primaryExpression92 = null;

        ListBuffer<JCExpression> expressionListOpt95 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:522:2: ( primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:522:4: primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            {
            pushFollow(FOLLOW_primaryExpression_in_postfixExpression4466);
            primaryExpression92=primaryExpression();
            _fsp--;

             expr = primaryExpression92; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:523:5: ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            loop48:
            do {
                int alt48=3;
                int LA48_0 = input.LA(1);

                if ( (LA48_0==DOT) ) {
                    alt48=1;
                }
                else if ( (LA48_0==LBRACKET) ) {
                    alt48=2;
                }


                switch (alt48) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:523:7: DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    {
            	    DOT93=(Token)input.LT(1);
            	    match(input,DOT,FOLLOW_DOT_in_postfixExpression4481); 
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:523:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    int alt46=2;
            	    int LA46_0 = input.LA(1);

            	    if ( (LA46_0==CLASS) ) {
            	        alt46=1;
            	    }
            	    else if ( (LA46_0==QUOTED_IDENTIFIER||LA46_0==IDENTIFIER) ) {
            	        alt46=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("523:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )", 46, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt46) {
            	        case 1 :
            	            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:523:13: CLASS
            	            {
            	            match(input,CLASS,FOLLOW_CLASS_in_postfixExpression4485); 

            	            }
            	            break;
            	        case 2 :
            	            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:524:13: name1= name ( LPAREN expressionListOpt RPAREN )*
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression4509);
            	            name1=name();
            	            _fsp--;

            	             expr = F.at(pos(DOT93)).Select(expr, name1.value); 
            	            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:525:14: ( LPAREN expressionListOpt RPAREN )*
            	            loop45:
            	            do {
            	                int alt45=2;
            	                int LA45_0 = input.LA(1);

            	                if ( (LA45_0==LPAREN) ) {
            	                    alt45=1;
            	                }


            	                switch (alt45) {
            	            	case 1 :
            	            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:525:16: LPAREN expressionListOpt RPAREN
            	            	    {
            	            	    LPAREN94=(Token)input.LT(1);
            	            	    match(input,LPAREN,FOLLOW_LPAREN_in_postfixExpression4534); 
            	            	    pushFollow(FOLLOW_expressionListOpt_in_postfixExpression4536);
            	            	    expressionListOpt95=expressionListOpt();
            	            	    _fsp--;

            	            	    match(input,RPAREN,FOLLOW_RPAREN_in_postfixExpression4538); 
            	            	     expr = F.at(pos(LPAREN94)).Apply(null, expr, expressionListOpt95.toList()); 

            	            	    }
            	            	    break;

            	            	default :
            	            	    break loop45;
            	                }
            	            } while (true);


            	            }
            	            break;

            	    }


            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:527:7: LBRACKET ( name BAR )? expression RBRACKET
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_postfixExpression4570); 
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:527:16: ( name BAR )?
            	    int alt47=2;
            	    int LA47_0 = input.LA(1);

            	    if ( (LA47_0==QUOTED_IDENTIFIER||LA47_0==IDENTIFIER) ) {
            	        int LA47_1 = input.LA(2);

            	        if ( (LA47_1==BAR) ) {
            	            alt47=1;
            	        }
            	    }
            	    switch (alt47) {
            	        case 1 :
            	            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:527:17: name BAR
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression4573);
            	            name();
            	            _fsp--;

            	            match(input,BAR,FOLLOW_BAR_in_postfixExpression4575); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_expression_in_postfixExpression4579);
            	    expression();
            	    _fsp--;

            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_postfixExpression4582); 

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
        return expr;
    }
    // $ANTLR end postfixExpression


    // $ANTLR start primaryExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:529:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | blockExpression | LPAREN expression RPAREN );
    public final JCExpression primaryExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LBRACE97=null;
        Token THIS100=null;
        Token SUPER101=null;
        Token LPAREN103=null;
        Token LPAREN108=null;
        JCExpression newExpression96 = null;

        JCIdent identifier98 = null;

        ListBuffer<JFXStatement> objectLiteral99 = null;

        JCIdent identifier102 = null;

        ListBuffer<JCExpression> expressionListOpt104 = null;

        JCExpression stringExpression105 = null;

        JCExpression literal106 = null;

        JFXBlockExpression blockExpression107 = null;

        JCExpression expression109 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:530:2: ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | blockExpression | LPAREN expression RPAREN )
            int alt50=9;
            switch ( input.LA(1) ) {
            case NEW:
                {
                alt50=1;
                }
                break;
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                int LA50_2 = input.LA(2);

                if ( (LA50_2==ABSTRACT||LA50_2==AND||LA50_2==ATTRIBUTE||(LA50_2>=THEN && LA50_2<=ELSE)||LA50_2==IN||(LA50_2>=PRIVATE && LA50_2<=READONLY)||LA50_2==INSTANCEOF||LA50_2==OR||(LA50_2>=LPAREN && LA50_2<=PERCENTEQ)||(LA50_2>=RBRACE_QUOTE_STRING_LITERAL && LA50_2<=RBRACE)||LA50_2==QUOTED_IDENTIFIER||LA50_2==IDENTIFIER) ) {
                    alt50=5;
                }
                else if ( (LA50_2==LBRACE) ) {
                    alt50=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("529:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | blockExpression | LPAREN expression RPAREN );", 50, 2, input);

                    throw nvae;
                }
                }
                break;
            case THIS:
                {
                alt50=3;
                }
                break;
            case SUPER:
                {
                alt50=4;
                }
                break;
            case QUOTE_LBRACE_STRING_LITERAL:
                {
                alt50=6;
                }
                break;
            case NULL:
            case TRUE:
            case FALSE:
            case STRING_LITERAL:
            case INTEGER_LITERAL:
            case FLOATING_POINT_LITERAL:
                {
                alt50=7;
                }
                break;
            case LBRACE:
                {
                alt50=8;
                }
                break;
            case LPAREN:
                {
                alt50=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("529:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | blockExpression | LPAREN expression RPAREN );", 50, 0, input);

                throw nvae;
            }

            switch (alt50) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:530:4: newExpression
                    {
                    pushFollow(FOLLOW_newExpression_in_primaryExpression4607);
                    newExpression96=newExpression();
                    _fsp--;

                     expr = newExpression96; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:531:4: identifier LBRACE objectLiteral RBRACE
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression4619);
                    identifier98=identifier();
                    _fsp--;

                    LBRACE97=(Token)input.LT(1);
                    match(input,LBRACE,FOLLOW_LBRACE_in_primaryExpression4621); 
                    pushFollow(FOLLOW_objectLiteral_in_primaryExpression4624);
                    objectLiteral99=objectLiteral();
                    _fsp--;

                    match(input,RBRACE,FOLLOW_RBRACE_in_primaryExpression4626); 
                     expr = F.at(pos(LBRACE97)).PureObjectLiteral(identifier98, objectLiteral99.toList()); 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:532:11: THIS
                    {
                    THIS100=(Token)input.LT(1);
                    match(input,THIS,FOLLOW_THIS_in_primaryExpression4643); 
                     expr = F.at(pos(THIS100)).Identifier(names._this); 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:533:11: SUPER
                    {
                    SUPER101=(Token)input.LT(1);
                    match(input,SUPER,FOLLOW_SUPER_in_primaryExpression4663); 
                     expr = F.at(pos(SUPER101)).Identifier(names._super); 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:534:11: identifier ( LPAREN expressionListOpt RPAREN )*
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression4683);
                    identifier102=identifier();
                    _fsp--;

                     expr = identifier102; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:535:10: ( LPAREN expressionListOpt RPAREN )*
                    loop49:
                    do {
                        int alt49=2;
                        int LA49_0 = input.LA(1);

                        if ( (LA49_0==LPAREN) ) {
                            alt49=1;
                        }


                        switch (alt49) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:535:12: LPAREN expressionListOpt RPAREN
                    	    {
                    	    LPAREN103=(Token)input.LT(1);
                    	    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression4704); 
                    	    pushFollow(FOLLOW_expressionListOpt_in_primaryExpression4708);
                    	    expressionListOpt104=expressionListOpt();
                    	    _fsp--;

                    	    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression4712); 
                    	     expr = F.at(pos(LPAREN103)).Apply(null, expr, expressionListOpt104.toList()); 

                    	    }
                    	    break;

                    	default :
                    	    break loop49;
                        }
                    } while (true);


                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:536:11: stringExpression
                    {
                    pushFollow(FOLLOW_stringExpression_in_primaryExpression4732);
                    stringExpression105=stringExpression();
                    _fsp--;

                     expr = stringExpression105; 

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:537:11: literal
                    {
                    pushFollow(FOLLOW_literal_in_primaryExpression4751);
                    literal106=literal();
                    _fsp--;

                     expr = literal106; 

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:538:11: blockExpression
                    {
                    pushFollow(FOLLOW_blockExpression_in_primaryExpression4771);
                    blockExpression107=blockExpression();
                    _fsp--;

                     expr = blockExpression107; 

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:539:11: LPAREN expression RPAREN
                    {
                    LPAREN108=(Token)input.LT(1);
                    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression4789); 
                    pushFollow(FOLLOW_expression_in_primaryExpression4791);
                    expression109=expression();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression4793); 
                     expr = F.at(pos(LPAREN108)).Parens(expression109); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:541:1: newExpression returns [JCExpression expr] : NEW identifier ( LPAREN expressionListOpt RPAREN )? ;
    public final JCExpression newExpression() throws RecognitionException {
        JCExpression expr = null;

        Token NEW111=null;
        ListBuffer<JCExpression> expressionListOpt110 = null;

        JCIdent identifier112 = null;


         ListBuffer<JCExpression> args = null; 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:543:2: ( NEW identifier ( LPAREN expressionListOpt RPAREN )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:543:4: NEW identifier ( LPAREN expressionListOpt RPAREN )?
            {
            NEW111=(Token)input.LT(1);
            match(input,NEW,FOLLOW_NEW_in_newExpression4826); 
            pushFollow(FOLLOW_identifier_in_newExpression4829);
            identifier112=identifier();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:544:3: ( LPAREN expressionListOpt RPAREN )?
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==LPAREN) ) {
                alt51=1;
            }
            switch (alt51) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:544:5: LPAREN expressionListOpt RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_newExpression4837); 
                    pushFollow(FOLLOW_expressionListOpt_in_newExpression4841);
                    expressionListOpt110=expressionListOpt();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_newExpression4845); 
                     args = expressionListOpt110; 

                    }
                    break;

            }

             expr = F.at(pos(NEW111)).NewClass(null, null, identifier112, 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:549:1: objectLiteral returns [ListBuffer<JFXStatement> parts = new ListBuffer<JFXStatement>()] : ( objectLiteralPart )* ;
    public final ListBuffer<JFXStatement> objectLiteral() throws RecognitionException {
        ListBuffer<JFXStatement> parts =  new ListBuffer<JFXStatement>();

        JFXStatement objectLiteralPart113 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:550:2: ( ( objectLiteralPart )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:550:4: ( objectLiteralPart )*
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:550:4: ( objectLiteralPart )*
            loop52:
            do {
                int alt52=2;
                int LA52_0 = input.LA(1);

                if ( (LA52_0==ABSTRACT||LA52_0==ATTRIBUTE||(LA52_0>=PRIVATE && LA52_0<=READONLY)||LA52_0==QUOTED_IDENTIFIER||LA52_0==IDENTIFIER) ) {
                    alt52=1;
                }


                switch (alt52) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:550:6: objectLiteralPart
            	    {
            	    pushFollow(FOLLOW_objectLiteralPart_in_objectLiteral4885);
            	    objectLiteralPart113=objectLiteralPart();
            	    _fsp--;

            	     parts.append(objectLiteralPart113); 

            	    }
            	    break;

            	default :
            	    break loop52;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );
    public final JFXStatement objectLiteralPart() throws RecognitionException {
        JFXStatement value = null;

        Token COLON114=null;
        name_return name115 = null;

        JCExpression expression116 = null;

        JavafxBindStatus bindOpt117 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:552:2: ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition )
            int alt54=3;
            switch ( input.LA(1) ) {
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                alt54=1;
                }
                break;
            case ABSTRACT:
                {
                switch ( input.LA(2) ) {
                case PUBLIC:
                    {
                    int LA54_9 = input.LA(3);

                    if ( (LA54_9==FUNCTION) ) {
                        alt54=3;
                    }
                    else if ( (LA54_9==ATTRIBUTE) ) {
                        alt54=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 9, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA54_10 = input.LA(3);

                    if ( (LA54_10==FUNCTION) ) {
                        alt54=3;
                    }
                    else if ( (LA54_10==ATTRIBUTE) ) {
                        alt54=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA54_11 = input.LA(3);

                    if ( (LA54_11==FUNCTION) ) {
                        alt54=3;
                    }
                    else if ( (LA54_11==ATTRIBUTE) ) {
                        alt54=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt54=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt54=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 2, input);

                    throw nvae;
                }

                }
                break;
            case READONLY:
                {
                switch ( input.LA(2) ) {
                case PUBLIC:
                    {
                    int LA54_9 = input.LA(3);

                    if ( (LA54_9==FUNCTION) ) {
                        alt54=3;
                    }
                    else if ( (LA54_9==ATTRIBUTE) ) {
                        alt54=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 9, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA54_10 = input.LA(3);

                    if ( (LA54_10==FUNCTION) ) {
                        alt54=3;
                    }
                    else if ( (LA54_10==ATTRIBUTE) ) {
                        alt54=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA54_11 = input.LA(3);

                    if ( (LA54_11==FUNCTION) ) {
                        alt54=3;
                    }
                    else if ( (LA54_11==ATTRIBUTE) ) {
                        alt54=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt54=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt54=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 3, input);

                    throw nvae;
                }

                }
                break;
            case PUBLIC:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA54_12 = input.LA(3);

                    if ( (LA54_12==FUNCTION) ) {
                        alt54=3;
                    }
                    else if ( (LA54_12==ATTRIBUTE) ) {
                        alt54=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA54_13 = input.LA(3);

                    if ( (LA54_13==ATTRIBUTE) ) {
                        alt54=2;
                    }
                    else if ( (LA54_13==FUNCTION) ) {
                        alt54=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt54=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt54=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 4, input);

                    throw nvae;
                }

                }
                break;
            case PRIVATE:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA54_12 = input.LA(3);

                    if ( (LA54_12==FUNCTION) ) {
                        alt54=3;
                    }
                    else if ( (LA54_12==ATTRIBUTE) ) {
                        alt54=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA54_13 = input.LA(3);

                    if ( (LA54_13==ATTRIBUTE) ) {
                        alt54=2;
                    }
                    else if ( (LA54_13==FUNCTION) ) {
                        alt54=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt54=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt54=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 5, input);

                    throw nvae;
                }

                }
                break;
            case PROTECTED:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA54_12 = input.LA(3);

                    if ( (LA54_12==FUNCTION) ) {
                        alt54=3;
                    }
                    else if ( (LA54_12==ATTRIBUTE) ) {
                        alt54=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA54_13 = input.LA(3);

                    if ( (LA54_13==ATTRIBUTE) ) {
                        alt54=2;
                    }
                    else if ( (LA54_13==FUNCTION) ) {
                        alt54=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt54=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt54=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 6, input);

                    throw nvae;
                }

                }
                break;
            case ATTRIBUTE:
                {
                alt54=2;
                }
                break;
            case FUNCTION:
                {
                alt54=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("551:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 54, 0, input);

                throw nvae;
            }

            switch (alt54) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:552:4: name COLON bindOpt expression ( COMMA | SEMI )?
                    {
                    pushFollow(FOLLOW_name_in_objectLiteralPart4911);
                    name115=name();
                    _fsp--;

                    COLON114=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_objectLiteralPart4913); 
                    pushFollow(FOLLOW_bindOpt_in_objectLiteralPart4916);
                    bindOpt117=bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_objectLiteralPart4918);
                    expression116=expression();
                    _fsp--;

                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:552:35: ( COMMA | SEMI )?
                    int alt53=2;
                    int LA53_0 = input.LA(1);

                    if ( ((LA53_0>=SEMI && LA53_0<=COMMA)) ) {
                        alt53=1;
                    }
                    switch (alt53) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:
                            {
                            if ( (input.LA(1)>=SEMI && input.LA(1)<=COMMA) ) {
                                input.consume();
                                errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse =
                                    new MismatchedSetException(null,input);
                                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_objectLiteralPart4920);    throw mse;
                            }


                            }
                            break;

                    }

                     value = F.at(pos(COLON114)).ObjectLiteralPart(name115.value, expression116, bindOpt117); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:553:10: attributeDefinition
                    {
                    pushFollow(FOLLOW_attributeDefinition_in_objectLiteralPart4940);
                    attributeDefinition();
                    _fsp--;


                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:554:10: functionDefinition
                    {
                    pushFollow(FOLLOW_functionDefinition_in_objectLiteralPart4951);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:555:1: stringExpression returns [JCExpression expr] : ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL ;
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
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:557:2: (ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:557:4: ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL
            {
            ql=(Token)input.LT(1);
            match(input,QUOTE_LBRACE_STRING_LITERAL,FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression4973); 
             strexp.append(F.at(pos(ql)).Literal(TypeTags.CLASS, ql.getText())); 
            pushFollow(FOLLOW_formatOrNull_in_stringExpression4982);
            f1=formatOrNull();
            _fsp--;

             strexp.append(f1); 
            pushFollow(FOLLOW_expression_in_stringExpression4993);
            e1=expression();
            _fsp--;

             strexp.append(e1); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:560:4: (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )*
            loop55:
            do {
                int alt55=2;
                int LA55_0 = input.LA(1);

                if ( (LA55_0==RBRACE_LBRACE_STRING_LITERAL) ) {
                    alt55=1;
                }


                switch (alt55) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:560:7: rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression
            	    {
            	    rl=(Token)input.LT(1);
            	    match(input,RBRACE_LBRACE_STRING_LITERAL,FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression5008); 
            	     strexp.append(F.at(pos(rl)).Literal(TypeTags.CLASS, rl.getText())); 
            	    pushFollow(FOLLOW_formatOrNull_in_stringExpression5020);
            	    fn=formatOrNull();
            	    _fsp--;

            	     strexp.append(fn); 
            	    pushFollow(FOLLOW_expression_in_stringExpression5034);
            	    en=expression();
            	    _fsp--;

            	     strexp.append(en); 

            	    }
            	    break;

            	default :
            	    break loop55;
                }
            } while (true);

            rq=(Token)input.LT(1);
            match(input,RBRACE_QUOTE_STRING_LITERAL,FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression5055); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:567:1: formatOrNull returns [JCExpression expr] : (fs= FORMAT_STRING_LITERAL | );
    public final JCExpression formatOrNull() throws RecognitionException {
        JCExpression expr = null;

        Token fs=null;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:568:2: (fs= FORMAT_STRING_LITERAL | )
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==FORMAT_STRING_LITERAL) ) {
                alt56=1;
            }
            else if ( ((LA56_0>=POUND && LA56_0<=TYPEOF)||LA56_0==IF||(LA56_0>=THIS && LA56_0<=FALSE)||(LA56_0>=NOT && LA56_0<=NEW)||LA56_0==SUPER||(LA56_0>=SIZEOF && LA56_0<=LPAREN)||(LA56_0>=PLUSPLUS && LA56_0<=SUBSUB)||(LA56_0>=QUES && LA56_0<=STRING_LITERAL)||(LA56_0>=QUOTE_LBRACE_STRING_LITERAL && LA56_0<=LBRACE)||(LA56_0>=QUOTED_IDENTIFIER && LA56_0<=INTEGER_LITERAL)||LA56_0==FLOATING_POINT_LITERAL||LA56_0==IDENTIFIER) ) {
                alt56=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("567:1: formatOrNull returns [JCExpression expr] : (fs= FORMAT_STRING_LITERAL | );", 56, 0, input);

                throw nvae;
            }
            switch (alt56) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:568:4: fs= FORMAT_STRING_LITERAL
                    {
                    fs=(Token)input.LT(1);
                    match(input,FORMAT_STRING_LITERAL,FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull5085); 
                     expr = F.at(pos(fs)).Literal(TypeTags.CLASS, fs.getText()); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:569:22: 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:571:1: expressionListOpt returns [ListBuffer<JCExpression> args = new ListBuffer<JCExpression>()] : (e1= expression ( COMMA e= expression )* )? ;
    public final ListBuffer<JCExpression> expressionListOpt() throws RecognitionException {
        ListBuffer<JCExpression> args =  new ListBuffer<JCExpression>();

        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:572:2: ( (e1= expression ( COMMA e= expression )* )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:572:4: (e1= expression ( COMMA e= expression )* )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:572:4: (e1= expression ( COMMA e= expression )* )?
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( ((LA58_0>=POUND && LA58_0<=TYPEOF)||LA58_0==IF||(LA58_0>=THIS && LA58_0<=FALSE)||(LA58_0>=NOT && LA58_0<=NEW)||LA58_0==SUPER||(LA58_0>=SIZEOF && LA58_0<=LPAREN)||(LA58_0>=PLUSPLUS && LA58_0<=SUBSUB)||(LA58_0>=QUES && LA58_0<=STRING_LITERAL)||(LA58_0>=QUOTE_LBRACE_STRING_LITERAL && LA58_0<=LBRACE)||(LA58_0>=QUOTED_IDENTIFIER && LA58_0<=INTEGER_LITERAL)||LA58_0==FLOATING_POINT_LITERAL||LA58_0==IDENTIFIER) ) {
                alt58=1;
            }
            switch (alt58) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:572:6: e1= expression ( COMMA e= expression )*
                    {
                    pushFollow(FOLLOW_expression_in_expressionListOpt5117);
                    e1=expression();
                    _fsp--;

                     args.append(e1); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:573:6: ( COMMA e= expression )*
                    loop57:
                    do {
                        int alt57=2;
                        int LA57_0 = input.LA(1);

                        if ( (LA57_0==COMMA) ) {
                            alt57=1;
                        }


                        switch (alt57) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:573:7: COMMA e= expression
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_expressionListOpt5128); 
                    	    pushFollow(FOLLOW_expression_in_expressionListOpt5134);
                    	    e=expression();
                    	    _fsp--;

                    	     args.append(e); 

                    	    }
                    	    break;

                    	default :
                    	    break loop57;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:574:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );
    public final int unaryOperator() throws RecognitionException {
        int optag = 0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:575:2: ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB )
            int alt59=9;
            switch ( input.LA(1) ) {
            case POUND:
                {
                alt59=1;
                }
                break;
            case QUES:
                {
                alt59=2;
                }
                break;
            case SUB:
                {
                alt59=3;
                }
                break;
            case NOT:
                {
                alt59=4;
                }
                break;
            case SIZEOF:
                {
                alt59=5;
                }
                break;
            case TYPEOF:
                {
                alt59=6;
                }
                break;
            case REVERSE:
                {
                alt59=7;
                }
                break;
            case PLUSPLUS:
                {
                alt59=8;
                }
                break;
            case SUBSUB:
                {
                alt59=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("574:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );", 59, 0, input);

                throw nvae;
            }

            switch (alt59) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:575:4: POUND
                    {
                    match(input,POUND,FOLLOW_POUND_in_unaryOperator5157); 
                     optag = 0; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:576:4: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_unaryOperator5168); 
                     optag = 0; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:577:4: SUB
                    {
                    match(input,SUB,FOLLOW_SUB_in_unaryOperator5181); 
                     optag = JCTree.NEG; 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:578:4: NOT
                    {
                    match(input,NOT,FOLLOW_NOT_in_unaryOperator5194); 
                     optag = JCTree.NOT; 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:579:4: SIZEOF
                    {
                    match(input,SIZEOF,FOLLOW_SIZEOF_in_unaryOperator5207); 
                     optag = 0; 

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:580:4: TYPEOF
                    {
                    match(input,TYPEOF,FOLLOW_TYPEOF_in_unaryOperator5220); 
                     optag = 0; 

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:581:4: REVERSE
                    {
                    match(input,REVERSE,FOLLOW_REVERSE_in_unaryOperator5233); 
                     optag = 0; 

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:582:4: PLUSPLUS
                    {
                    match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_unaryOperator5246); 
                     optag = 0; 

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:583:4: SUBSUB
                    {
                    match(input,SUBSUB,FOLLOW_SUBSUB_in_unaryOperator5259); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:585:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );
    public final int assignmentOperator() throws RecognitionException {
        int optag = 0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:586:2: ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ )
            int alt60=5;
            switch ( input.LA(1) ) {
            case PLUSEQ:
                {
                alt60=1;
                }
                break;
            case SUBEQ:
                {
                alt60=2;
                }
                break;
            case STAREQ:
                {
                alt60=3;
                }
                break;
            case SLASHEQ:
                {
                alt60=4;
                }
                break;
            case PERCENTEQ:
                {
                alt60=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("585:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );", 60, 0, input);

                throw nvae;
            }

            switch (alt60) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:586:4: PLUSEQ
                    {
                    match(input,PLUSEQ,FOLLOW_PLUSEQ_in_assignmentOperator5280); 
                     optag = JCTree.PLUS_ASG; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:587:4: SUBEQ
                    {
                    match(input,SUBEQ,FOLLOW_SUBEQ_in_assignmentOperator5293); 
                     optag = JCTree.MINUS_ASG; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:588:4: STAREQ
                    {
                    match(input,STAREQ,FOLLOW_STAREQ_in_assignmentOperator5306); 
                     optag = JCTree.MUL_ASG; 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:589:4: SLASHEQ
                    {
                    match(input,SLASHEQ,FOLLOW_SLASHEQ_in_assignmentOperator5319); 
                     optag = JCTree.DIV_ASG; 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:590:4: PERCENTEQ
                    {
                    match(input,PERCENTEQ,FOLLOW_PERCENTEQ_in_assignmentOperator5332); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:592:1: typeReference returns [JFXType type] : ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? ;
    public final JFXType typeReference() throws RecognitionException {
        JFXType type = null;

        Token STAR119=null;
        int ccn = 0;

        int ccs = 0;

        name_return name118 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:593:2: ( ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:593:4: ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:593:4: ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==COLON) ) {
                alt62=1;
            }
            switch (alt62) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:593:6: COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    {
                    match(input,COLON,FOLLOW_COLON_in_typeReference5356); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:593:13: ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    int alt61=2;
                    int LA61_0 = input.LA(1);

                    if ( (LA61_0==QUOTED_IDENTIFIER||LA61_0==IDENTIFIER) ) {
                        alt61=1;
                    }
                    else if ( (LA61_0==STAR) ) {
                        alt61=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("593:13: ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )", 61, 0, input);

                        throw nvae;
                    }
                    switch (alt61) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:593:15: name ccn= cardinalityConstraint
                            {
                            pushFollow(FOLLOW_name_in_typeReference5361);
                            name118=name();
                            _fsp--;

                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference5365);
                            ccn=cardinalityConstraint();
                            _fsp--;

                             type = F.TypeClass(name118.value, ccn); 

                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:594:22: STAR ccs= cardinalityConstraint
                            {
                            STAR119=(Token)input.LT(1);
                            match(input,STAR,FOLLOW_STAR_in_typeReference5391); 
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference5395);
                            ccs=cardinalityConstraint();
                            _fsp--;

                             type = F.at(pos(STAR119)).TypeAny(ccs); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:598:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );
    public final int cardinalityConstraint() throws RecognitionException {
        int ary = 0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:599:2: ( LBRACKET RBRACKET | QUES | PLUS | STAR | )
            int alt63=5;
            switch ( input.LA(1) ) {
            case LBRACKET:
                {
                alt63=1;
                }
                break;
            case QUES:
                {
                alt63=2;
                }
                break;
            case PLUS:
                {
                alt63=3;
                }
                break;
            case STAR:
                {
                alt63=4;
                }
                break;
            case INVERSE:
            case RPAREN:
            case SEMI:
            case COMMA:
            case EQ:
            case LBRACE:
                {
                alt63=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("598:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 63, 0, input);

                throw nvae;
            }

            switch (alt63) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:599:5: LBRACKET RBRACKET
                    {
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_cardinalityConstraint5457); 
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_cardinalityConstraint5461); 
                     ary = JFXType.CARDINALITY_ANY; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:600:5: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_cardinalityConstraint5473); 
                     ary = JFXType.CARDINALITY_OPTIONAL; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:601:5: PLUS
                    {
                    match(input,PLUS,FOLLOW_PLUS_in_cardinalityConstraint5500); 
                     ary = JFXType.CARDINALITY_ANY; 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:602:5: STAR
                    {
                    match(input,STAR,FOLLOW_STAR_in_cardinalityConstraint5527); 
                     ary = JFXType.CARDINALITY_ANY; 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:603:29: 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:605:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );
    public final JCExpression literal() throws RecognitionException {
        JCExpression expr = null;

        Token t=null;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:606:2: (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL )
            int alt64=6;
            switch ( input.LA(1) ) {
            case STRING_LITERAL:
                {
                alt64=1;
                }
                break;
            case INTEGER_LITERAL:
                {
                alt64=2;
                }
                break;
            case FLOATING_POINT_LITERAL:
                {
                alt64=3;
                }
                break;
            case TRUE:
                {
                alt64=4;
                }
                break;
            case FALSE:
                {
                alt64=5;
                }
                break;
            case NULL:
                {
                alt64=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("605:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );", 64, 0, input);

                throw nvae;
            }

            switch (alt64) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:606:4: t= STRING_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_literal5596); 
                     expr = F.at(pos(t)).Literal(TypeTags.CLASS, t.getText()); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:607:4: t= INTEGER_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,INTEGER_LITERAL,FOLLOW_INTEGER_LITERAL_in_literal5606); 
                     expr = F.at(pos(t)).Literal(TypeTags.INT, Convert.string2int(t.getText(), 10)); 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:608:4: t= FLOATING_POINT_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,FLOATING_POINT_LITERAL,FOLLOW_FLOATING_POINT_LITERAL_in_literal5616); 
                     expr = F.at(pos(t)).Literal(TypeTags.DOUBLE, Double.valueOf(t.getText())); 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:609:4: t= TRUE
                    {
                    t=(Token)input.LT(1);
                    match(input,TRUE,FOLLOW_TRUE_in_literal5626); 
                     expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 1); 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:610:4: t= FALSE
                    {
                    t=(Token)input.LT(1);
                    match(input,FALSE,FOLLOW_FALSE_in_literal5640); 
                     expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 0); 

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:611:4: t= NULL
                    {
                    t=(Token)input.LT(1);
                    match(input,NULL,FOLLOW_NULL_in_literal5654); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:613:1: typeName returns [JCExpression expr] : qualident ;
    public final JCExpression typeName() throws RecognitionException {
        JCExpression expr = null;

        JCExpression qualident120 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:614:8: ( qualident )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:614:10: qualident
            {
            pushFollow(FOLLOW_qualident_in_typeName5681);
            qualident120=qualident();
            _fsp--;

             expr = qualident120; 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:616:1: qualident returns [JCExpression expr] : identifier ( DOT name )* ;
    public final JCExpression qualident() throws RecognitionException {
        JCExpression expr = null;

        JCIdent identifier121 = null;

        name_return name122 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:617:8: ( identifier ( DOT name )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:617:10: identifier ( DOT name )*
            {
            pushFollow(FOLLOW_identifier_in_qualident5723);
            identifier121=identifier();
            _fsp--;

             expr = identifier121; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:618:10: ( DOT name )*
            loop65:
            do {
                int alt65=2;
                int LA65_0 = input.LA(1);

                if ( (LA65_0==DOT) ) {
                    alt65=1;
                }


                switch (alt65) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:618:12: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_qualident5751); 
            	    pushFollow(FOLLOW_name_in_qualident5753);
            	    name122=name();
            	    _fsp--;

            	     expr = F.at(name122.pos).Select(expr, name122.value); 

            	    }
            	    break;

            	default :
            	    break loop65;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:620:1: identifier returns [JCIdent expr] : name ;
    public final JCIdent identifier() throws RecognitionException {
        JCIdent expr = null;

        name_return name123 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:621:2: ( name )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:621:4: name
            {
            pushFollow(FOLLOW_name_in_identifier5790);
            name123=name();
            _fsp--;

             expr = F.at(name123.pos).Ident(name123.value); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:623:1: name returns [Name value, int pos] : tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) ;
    public final name_return name() throws RecognitionException {
        name_return retval = new name_return();
        retval.start = input.LT(1);

        Token tokid=null;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:624:2: (tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:624:4: tokid= ( QUOTED_IDENTIFIER | IDENTIFIER )
            {
            tokid=(Token)input.LT(1);
            if ( input.LA(1)==QUOTED_IDENTIFIER||input.LA(1)==IDENTIFIER ) {
                input.consume();
                errorRecovery=false;
            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_name5824);    throw mse;
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


 

    public static final BitSet FOLLOW_packageDecl_in_module2032 = new BitSet(new long[]{0xF130F205C0120260L,0x0961B001C008E805L});
    public static final BitSet FOLLOW_moduleItems_in_module2035 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_module2037 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PACKAGE_in_packageDecl2059 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_qualident_in_packageDecl2061 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_packageDecl2063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_moduleItem_in_moduleItems2094 = new BitSet(new long[]{0xF130F205C0120262L,0x0961B001C008E805L});
    public static final BitSet FOLLOW_importDecl_in_moduleItem2138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDefinition_in_moduleItem2154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_moduleItem2170 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_moduleItem2192 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_moduleItem2194 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importDecl2231 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_identifier_in_importDecl2234 = new BitSet(new long[]{0x0000000000000000L,0x0000000000280000L});
    public static final BitSet FOLLOW_DOT_in_importDecl2259 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_importDecl2261 = new BitSet(new long[]{0x0000000000000000L,0x0000000000280000L});
    public static final BitSet FOLLOW_DOT_in_importDecl2290 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
    public static final BitSet FOLLOW_STAR_in_importDecl2292 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_importDecl2301 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_classDefinition2332 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_CLASS_in_classDefinition2335 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_classDefinition2337 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000020L});
    public static final BitSet FOLLOW_supers_in_classDefinition2339 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_classDefinition2341 = new BitSet(new long[]{0xC000000000004200L,0x0008000000000005L});
    public static final BitSet FOLLOW_classMembers_in_classDefinition2343 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_classDefinition2345 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXTENDS_in_supers2370 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_supers2374 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_COMMA_in_supers2402 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_supers2406 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_attributeDefinition_in_classMembers2440 = new BitSet(new long[]{0xC000000000004202L,0x0000000000000005L});
    public static final BitSet FOLLOW_functionDefinition_in_classMembers2458 = new BitSet(new long[]{0xC000000000004202L,0x0000000000000005L});
    public static final BitSet FOLLOW_modifierFlags_in_attributeDefinition2487 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeDefinition2489 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_attributeDefinition2491 = new BitSet(new long[]{0x0000000000000000L,0x0000080000880008L});
    public static final BitSet FOLLOW_typeReference_in_attributeDefinition2499 = new BitSet(new long[]{0x0000000000000000L,0x0000000000880008L});
    public static final BitSet FOLLOW_EQ_in_attributeDefinition2507 = new BitSet(new long[]{0x3000F20030010060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_bindOpt_in_attributeDefinition2509 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_attributeDefinition2511 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_inverseClause_in_attributeDefinition2515 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_attributeDefinition2525 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INVERSE_in_inverseClause2551 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_memberSelector_in_inverseClause2553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_functionDefinition2571 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_functionDefinition2573 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_functionDefinition2575 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_formalParameters_in_functionDefinition2583 = new BitSet(new long[]{0x0000000000000000L,0x0001080000000000L});
    public static final BitSet FOLLOW_typeReference_in_functionDefinition2586 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_blockExpression_in_functionDefinition2595 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags2623 = new BitSet(new long[]{0xC000000000000002L,0x0000000000000001L});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags2636 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags2658 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000004L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags2671 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_accessModifier2719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PRIVATE_in_accessModifier2739 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROTECTED_in_accessModifier2758 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ABSTRACT_in_otherModifier2785 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_READONLY_in_otherModifier2803 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_memberSelector2832 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_DOT_in_memberSelector2836 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_memberSelector2842 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_formalParameters2860 = new BitSet(new long[]{0x0000000000000000L,0x0820000000010000L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters2867 = new BitSet(new long[]{0x0000000000000000L,0x0000000000110000L});
    public static final BitSet FOLLOW_COMMA_in_formalParameters2885 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters2891 = new BitSet(new long[]{0x0000000000000000L,0x0000000000110000L});
    public static final BitSet FOLLOW_RPAREN_in_formalParameters2902 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_formalParameter2917 = new BitSet(new long[]{0x0000000000000002L,0x0000080000000000L});
    public static final BitSet FOLLOW_typeReference_in_formalParameter2919 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_block2945 = new BitSet(new long[]{0xF130F201C0020260L,0x0969B001C008E807L});
    public static final BitSet FOLLOW_statement_in_block2955 = new BitSet(new long[]{0xF130F201C0020260L,0x0969B001C008E807L});
    public static final BitSet FOLLOW_expression_in_block2970 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_block2972 = new BitSet(new long[]{0xF130F201C0020260L,0x0969B001C008E807L});
    public static final BitSet FOLLOW_RBRACE_in_block2988 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_blockExpression3012 = new BitSet(new long[]{0xF130F201C0020260L,0x0969B001C008E807L});
    public static final BitSet FOLLOW_statements_in_blockExpression3015 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_blockExpression3020 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_statements3039 = new BitSet(new long[]{0xF130F201C0020262L,0x0961B001C008E807L});
    public static final BitSet FOLLOW_statements_in_statements3051 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_statements3063 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_statements3075 = new BitSet(new long[]{0xF130F201C0020262L,0x0961B001C008E807L});
    public static final BitSet FOLLOW_statements_in_statements3098 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclaration_in_statement3156 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_statement3158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionDefinition_in_statement3166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_statement3182 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_LPAREN_in_statement3184 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_statement3186 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_statement3188 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_block_in_statement3190 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BREAK_in_statement3197 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_statement3199 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_statement3212 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_statement3215 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THROW_in_statement3233 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_statement3235 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_statement3237 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_returnStatement_in_statement3257 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_statement3259 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tryStatement_in_statement3275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_statement3286 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variableDeclaration3316 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_variableDeclaration3319 = new BitSet(new long[]{0x0000000000000002L,0x0000080000800000L});
    public static final BitSet FOLLOW_typeReference_in_variableDeclaration3322 = new BitSet(new long[]{0x0000000000000002L,0x0000000000800000L});
    public static final BitSet FOLLOW_EQ_in_variableDeclaration3333 = new BitSet(new long[]{0x3000F20030010060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_bindOpt_in_variableDeclaration3335 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_variableDeclaration3338 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BIND_in_bindOpt3388 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAYS_in_bindOpt3419 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3435 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TIE_in_bindOpt3450 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3466 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnStatement3500 = new BitSet(new long[]{0x3000F20000000062L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_returnStatement3503 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRY_in_tryStatement3546 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3552 = new BitSet(new long[]{0x0200000000080000L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement3565 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3571 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catchClause_in_tryStatement3587 = new BitSet(new long[]{0x0200000000080002L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement3610 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3615 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CATCH_in_catchClause3654 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_LPAREN_in_catchClause3657 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_catchClause3660 = new BitSet(new long[]{0x0000000000000000L,0x0000080000010000L});
    public static final BitSet FOLLOW_COLON_in_catchClause3668 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_identifier_in_catchClause3670 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_catchClause3679 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_block_in_catchClause3683 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifExpression_in_expression3711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_suffixedExpression_in_expression3734 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifExpression3765 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_ifExpression3769 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_THEN_in_ifExpression3773 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_ifExpression3778 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_ELSE_in_ifExpression3786 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_ifExpression3791 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentExpression_in_suffixedExpression3814 = new BitSet(new long[]{0x0000000000000002L,0x0000000140000000L});
    public static final BitSet FOLLOW_set_in_suffixedExpression3825 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression3854 = new BitSet(new long[]{0x0000000000000002L,0x0000000000800000L});
    public static final BitSet FOLLOW_EQ_in_assignmentExpression3869 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression3875 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression3902 = new BitSet(new long[]{0x0000000000000002L,0x000001F000000000L});
    public static final BitSet FOLLOW_assignmentOperator_in_assignmentOpExpression3918 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression3924 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orExpression_in_andExpression3950 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_AND_in_andExpression3966 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_orExpression_in_andExpression3972 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression4000 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001000L});
    public static final BitSet FOLLOW_OR_in_orExpression4015 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression4021 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001000L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression4049 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_INSTANCEOF_in_instanceOfExpression4064 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_identifier_in_instanceOfExpression4066 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4094 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_LTGT_in_relationalExpression4110 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4116 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_EQEQ_in_relationalExpression4130 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4136 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_LTEQ_in_relationalExpression4150 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4156 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_GTEQ_in_relationalExpression4170 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4176 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_LT_in_relationalExpression4190 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4198 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_GT_in_relationalExpression4212 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4220 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_IN_in_relationalExpression4234 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4242 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4271 = new BitSet(new long[]{0x0000000000000002L,0x00000000A0000000L});
    public static final BitSet FOLLOW_PLUS_in_additiveExpression4286 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4292 = new BitSet(new long[]{0x0000000000000002L,0x00000000A0000000L});
    public static final BitSet FOLLOW_SUB_in_additiveExpression4305 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4312 = new BitSet(new long[]{0x0000000000000002L,0x00000000A0000000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4340 = new BitSet(new long[]{0x0000000000000002L,0x0000000E00000000L});
    public static final BitSet FOLLOW_STAR_in_multiplicativeExpression4356 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4363 = new BitSet(new long[]{0x0000000000000002L,0x0000000E00000000L});
    public static final BitSet FOLLOW_SLASH_in_multiplicativeExpression4377 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4383 = new BitSet(new long[]{0x0000000000000002L,0x0000000E00000000L});
    public static final BitSet FOLLOW_PERCENT_in_multiplicativeExpression4397 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4401 = new BitSet(new long[]{0x0000000000000002L,0x0000000E00000000L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression4431 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryOperator_in_unaryExpression4442 = new BitSet(new long[]{0x2000F00000000000L,0x0961A00000008800L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression4446 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primaryExpression_in_postfixExpression4466 = new BitSet(new long[]{0x0000000000000002L,0x0000000000220000L});
    public static final BitSet FOLLOW_DOT_in_postfixExpression4481 = new BitSet(new long[]{0x0000000000100000L,0x0820000000000000L});
    public static final BitSet FOLLOW_CLASS_in_postfixExpression4485 = new BitSet(new long[]{0x0000000000000002L,0x0000000000220000L});
    public static final BitSet FOLLOW_name_in_postfixExpression4509 = new BitSet(new long[]{0x0000000000000002L,0x0000000000228000L});
    public static final BitSet FOLLOW_LPAREN_in_postfixExpression4534 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C001E800L});
    public static final BitSet FOLLOW_expressionListOpt_in_postfixExpression4536 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_postfixExpression4538 = new BitSet(new long[]{0x0000000000000002L,0x0000000000228000L});
    public static final BitSet FOLLOW_LBRACKET_in_postfixExpression4570 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_name_in_postfixExpression4573 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_BAR_in_postfixExpression4575 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_postfixExpression4579 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_RBRACKET_in_postfixExpression4582 = new BitSet(new long[]{0x0000000000000002L,0x0000000000220000L});
    public static final BitSet FOLLOW_newExpression_in_primaryExpression4607 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression4619 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_primaryExpression4621 = new BitSet(new long[]{0xC000000000004200L,0x0828000000000005L});
    public static final BitSet FOLLOW_objectLiteral_in_primaryExpression4624 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_primaryExpression4626 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THIS_in_primaryExpression4643 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_primaryExpression4663 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression4683 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression4704 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C001E800L});
    public static final BitSet FOLLOW_expressionListOpt_in_primaryExpression4708 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression4712 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_stringExpression_in_primaryExpression4732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primaryExpression4751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_blockExpression_in_primaryExpression4771 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression4789 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_primaryExpression4791 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression4793 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_newExpression4826 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_identifier_in_newExpression4829 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_LPAREN_in_newExpression4837 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C001E800L});
    public static final BitSet FOLLOW_expressionListOpt_in_newExpression4841 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_newExpression4845 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_objectLiteralPart_in_objectLiteral4885 = new BitSet(new long[]{0xC000000000004202L,0x0820000000000005L});
    public static final BitSet FOLLOW_name_in_objectLiteralPart4911 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_COLON_in_objectLiteralPart4913 = new BitSet(new long[]{0x3000F20030010060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_bindOpt_in_objectLiteralPart4916 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_objectLiteralPart4918 = new BitSet(new long[]{0x0000000000000002L,0x0000000000180000L});
    public static final BitSet FOLLOW_set_in_objectLiteralPart4920 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeDefinition_in_objectLiteralPart4940 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionDefinition_in_objectLiteralPart4951 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression4973 = new BitSet(new long[]{0x3000F20000000060L,0x0971B001C000E800L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression4982 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_stringExpression4993 = new BitSet(new long[]{0x0000000000000000L,0x0006000000000000L});
    public static final BitSet FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression5008 = new BitSet(new long[]{0x3000F20000000060L,0x0971B001C000E800L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression5020 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_stringExpression5034 = new BitSet(new long[]{0x0000000000000000L,0x0006000000000000L});
    public static final BitSet FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression5055 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull5085 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt5117 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_COMMA_in_expressionListOpt5128 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt5134 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_POUND_in_unaryOperator5157 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_unaryOperator5168 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUB_in_unaryOperator5181 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_unaryOperator5194 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIZEOF_in_unaryOperator5207 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TYPEOF_in_unaryOperator5220 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REVERSE_in_unaryOperator5233 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_unaryOperator5246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_unaryOperator5259 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSEQ_in_assignmentOperator5280 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBEQ_in_assignmentOperator5293 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAREQ_in_assignmentOperator5306 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SLASHEQ_in_assignmentOperator5319 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENTEQ_in_assignmentOperator5332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLON_in_typeReference5356 = new BitSet(new long[]{0x0000000000000000L,0x0820000200000000L});
    public static final BitSet FOLLOW_name_in_typeReference5361 = new BitSet(new long[]{0x0000000000000002L,0x0000100220020000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference5365 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_typeReference5391 = new BitSet(new long[]{0x0000000000000002L,0x0000100220020000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference5395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_cardinalityConstraint5457 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_RBRACKET_in_cardinalityConstraint5461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_cardinalityConstraint5473 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUS_in_cardinalityConstraint5500 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_cardinalityConstraint5527 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_literal5596 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_LITERAL_in_literal5606 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOATING_POINT_LITERAL_in_literal5616 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRUE_in_literal5626 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALSE_in_literal5640 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_literal5654 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualident_in_typeName5681 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_qualident5723 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_DOT_in_qualident5751 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_qualident5753 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_name_in_identifier5790 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_name5824 = new BitSet(new long[]{0x0000000000000002L});

}