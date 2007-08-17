// $ANTLR 3.0 C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g 2007-08-16 22:54:10

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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "BAR", "POUND", "TYPEOF", "DOTDOT", "ABSTRACT", "AFTER", "AND", "AS", "ASSERT", "ATTRIBUTE", "BEFORE", "BIND", "BREAK", "BY", "CATCH", "CHANGE", "CLASS", "DELETE", "DO", "DUR", "EASEBOTH", "EASEIN", "EASEOUT", "TIE", "STAYS", "RETURN", "THROW", "VAR", "PACKAGE", "IMPORT", "FROM", "ON", "INSERT", "INTO", "FIRST", "LAST", "IF", "THEN", "ELSE", "THIS", "NULL", "TRUE", "FALSE", "FOR", "IN", "WHILE", "CONTINUE", "LINEAR", "MOTION", "TRY", "FINALLY", "LAZY", "WHERE", "NOT", "NEW", "PRIVATE", "PROTECTED", "PUBLIC", "FUNCTION", "READONLY", "INVERSE", "TYPE", "EXTENDS", "ORDER", "INDEX", "INIT", "INSTANCEOF", "INDEXOF", "SELECT", "SUPER", "OR", "SIZEOF", "REVERSE", "LPAREN", "RPAREN", "LBRACKET", "RBRACKET", "SEMI", "COMMA", "DOT", "EQEQ", "EQ", "GT", "LT", "LTGT", "LTEQ", "GTEQ", "PLUS", "PLUSPLUS", "SUB", "SUBSUB", "STAR", "SLASH", "PERCENT", "PLUSEQ", "SUBEQ", "STAREQ", "SLASHEQ", "PERCENTEQ", "COLON", "QUES", "STRING_LITERAL", "NextIsPercent", "QUOTE_LBRACE_STRING_LITERAL", "LBRACE", "RBRACE_QUOTE_STRING_LITERAL", "RBRACE_LBRACE_STRING_LITERAL", "RBRACE", "FORMAT_STRING_LITERAL", "QUOTED_IDENTIFIER", "INTEGER_LITERAL", "Exponent", "FLOATING_POINT_LITERAL", "Letter", "JavaIDDigit", "IDENTIFIER", "WS", "COMMENT", "LINE_COMMENT"
    };
    public static final int PACKAGE=32;
    public static final int FUNCTION=62;
    public static final int LT=87;
    public static final int STAR=95;
    public static final int WHILE=49;
    public static final int EASEOUT=26;
    public static final int NEW=58;
    public static final int INDEXOF=71;
    public static final int DO=22;
    public static final int NOT=57;
    public static final int EOF=-1;
    public static final int RBRACE_QUOTE_STRING_LITERAL=109;
    public static final int BREAK=16;
    public static final int TYPE=65;
    public static final int LBRACKET=79;
    public static final int RPAREN=78;
    public static final int IMPORT=33;
    public static final int LINEAR=51;
    public static final int STRING_LITERAL=105;
    public static final int FLOATING_POINT_LITERAL=116;
    public static final int INSERT=36;
    public static final int SUBSUB=94;
    public static final int BIND=15;
    public static final int STAREQ=100;
    public static final int THIS=43;
    public static final int RETURN=29;
    public static final int VAR=31;
    public static final int SUPER=73;
    public static final int LAST=39;
    public static final int EQ=85;
    public static final int COMMENT=121;
    public static final int SELECT=72;
    public static final int INTO=37;
    public static final int QUES=104;
    public static final int EQEQ=84;
    public static final int MOTION=52;
    public static final int RBRACE=111;
    public static final int POUND=5;
    public static final int LINE_COMMENT=122;
    public static final int PRIVATE=59;
    public static final int NULL=44;
    public static final int ELSE=42;
    public static final int ON=35;
    public static final int DELETE=21;
    public static final int SLASHEQ=101;
    public static final int EASEBOTH=24;
    public static final int ASSERT=12;
    public static final int TRY=53;
    public static final int INVERSE=64;
    public static final int WS=120;
    public static final int TYPEOF=6;
    public static final int INTEGER_LITERAL=114;
    public static final int OR=74;
    public static final int JavaIDDigit=118;
    public static final int SIZEOF=75;
    public static final int GT=86;
    public static final int FROM=34;
    public static final int CATCH=18;
    public static final int REVERSE=76;
    public static final int FALSE=46;
    public static final int INIT=69;
    public static final int Letter=117;
    public static final int THROW=30;
    public static final int DUR=23;
    public static final int WHERE=56;
    public static final int PROTECTED=60;
    public static final int CLASS=20;
    public static final int ORDER=67;
    public static final int PLUSPLUS=92;
    public static final int LBRACE=108;
    public static final int ATTRIBUTE=13;
    public static final int LTEQ=89;
    public static final int SUBEQ=99;
    public static final int Exponent=115;
    public static final int FOR=47;
    public static final int SUB=93;
    public static final int DOTDOT=7;
    public static final int ABSTRACT=8;
    public static final int NextIsPercent=106;
    public static final int AND=10;
    public static final int PLUSEQ=98;
    public static final int LPAREN=77;
    public static final int IF=40;
    public static final int AS=11;
    public static final int INDEX=68;
    public static final int SLASH=96;
    public static final int THEN=41;
    public static final int IN=48;
    public static final int CONTINUE=50;
    public static final int COMMA=82;
    public static final int TIE=27;
    public static final int IDENTIFIER=119;
    public static final int QUOTE_LBRACE_STRING_LITERAL=107;
    public static final int PLUS=91;
    public static final int RBRACKET=80;
    public static final int DOT=83;
    public static final int RBRACE_LBRACE_STRING_LITERAL=110;
    public static final int STAYS=28;
    public static final int BY=17;
    public static final int PERCENT=97;
    public static final int LAZY=55;
    public static final int LTGT=88;
    public static final int BEFORE=14;
    public static final int INSTANCEOF=70;
    public static final int AFTER=9;
    public static final int GTEQ=90;
    public static final int READONLY=63;
    public static final int TRUE=45;
    public static final int SEMI=81;
    public static final int CHANGE=19;
    public static final int COLON=103;
    public static final int PERCENTEQ=102;
    public static final int FINALLY=54;
    public static final int FORMAT_STRING_LITERAL=112;
    public static final int EASEIN=25;
    public static final int QUOTED_IDENTIFIER=113;
    public static final int PUBLIC=61;
    public static final int EXTENDS=66;
    public static final int BAR=4;
    public static final int FIRST=38;

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:316:1: module returns [JCCompilationUnit result] : ( packageDecl )? moduleItems EOF ;
    public final JCCompilationUnit module() throws RecognitionException {
        JCCompilationUnit result = null;

        JCExpression packageDecl1 = null;

        ListBuffer<JCTree> moduleItems2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:317:8: ( ( packageDecl )? moduleItems EOF )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:317:10: ( packageDecl )? moduleItems EOF
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:317:10: ( packageDecl )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==PACKAGE) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:317:10: packageDecl
                    {
                    pushFollow(FOLLOW_packageDecl_in_module2000);
                    packageDecl1=packageDecl();
                    _fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_moduleItems_in_module2003);
            moduleItems2=moduleItems();
            _fsp--;

            match(input,EOF,FOLLOW_EOF_in_module2005); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:318:1: packageDecl returns [JCExpression value] : PACKAGE qualident SEMI ;
    public final JCExpression packageDecl() throws RecognitionException {
        JCExpression value = null;

        JCExpression qualident3 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:319:8: ( PACKAGE qualident SEMI )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:319:10: PACKAGE qualident SEMI
            {
            match(input,PACKAGE,FOLLOW_PACKAGE_in_packageDecl2027); 
            pushFollow(FOLLOW_qualident_in_packageDecl2029);
            qualident3=qualident();
            _fsp--;

            match(input,SEMI,FOLLOW_SEMI_in_packageDecl2031); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:320:1: moduleItems returns [ListBuffer<JCTree> items = new ListBuffer<JCTree>()] : ( moduleItem )* ;
    public final ListBuffer<JCTree> moduleItems() throws RecognitionException {
        ListBuffer<JCTree> items =  new ListBuffer<JCTree>();

        JCTree moduleItem4 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:9: ( ( moduleItem )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:11: ( moduleItem )*
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:11: ( moduleItem )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=POUND && LA2_0<=TYPEOF)||LA2_0==ABSTRACT||LA2_0==BREAK||LA2_0==CLASS||(LA2_0>=RETURN && LA2_0<=VAR)||LA2_0==IMPORT||LA2_0==IF||(LA2_0>=THIS && LA2_0<=FALSE)||(LA2_0>=WHILE && LA2_0<=CONTINUE)||LA2_0==TRY||(LA2_0>=NOT && LA2_0<=READONLY)||LA2_0==SUPER||(LA2_0>=SIZEOF && LA2_0<=LPAREN)||LA2_0==LBRACKET||LA2_0==SEMI||(LA2_0>=PLUSPLUS && LA2_0<=SUBSUB)||(LA2_0>=QUES && LA2_0<=STRING_LITERAL)||(LA2_0>=QUOTE_LBRACE_STRING_LITERAL && LA2_0<=LBRACE)||(LA2_0>=QUOTED_IDENTIFIER && LA2_0<=INTEGER_LITERAL)||LA2_0==FLOATING_POINT_LITERAL||LA2_0==IDENTIFIER) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:12: moduleItem
            	    {
            	    pushFollow(FOLLOW_moduleItem_in_moduleItems2062);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );
    public final JCTree moduleItem() throws RecognitionException {
        JCTree value = null;

        JCTree importDecl5 = null;

        JFXClassDeclaration classDefinition6 = null;

        JCStatement statement7 = null;

        JCExpression expression8 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:323:8: ( importDecl | classDefinition | statement | expression SEMI )
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

                    if ( (LA3_10==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_10==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
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
                            new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
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
                            new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 12, input);

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
                        new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 2, input);

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

                    if ( (LA3_10==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_10==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
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
                            new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
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
                            new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 12, input);

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
                        new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 3, input);

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

                    if ( (LA3_13==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_13==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA3_14 = input.LA(3);

                    if ( (LA3_14==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_14==CLASS) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 14, input);

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
                        new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 4, input);

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

                    if ( (LA3_13==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_13==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA3_14 = input.LA(3);

                    if ( (LA3_14==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_14==CLASS) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 14, input);

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
                        new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 5, input);

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

                    if ( (LA3_13==CLASS) ) {
                        alt3=2;
                    }
                    else if ( (LA3_13==FUNCTION) ) {
                        alt3=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA3_14 = input.LA(3);

                    if ( (LA3_14==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_14==CLASS) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 14, input);

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
                        new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 6, input);

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
            case LBRACKET:
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
                    new NoViableAltException("322:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:323:10: importDecl
                    {
                    pushFollow(FOLLOW_importDecl_in_moduleItem2106);
                    importDecl5=importDecl();
                    _fsp--;

                     value = importDecl5; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:324:10: classDefinition
                    {
                    pushFollow(FOLLOW_classDefinition_in_moduleItem2122);
                    classDefinition6=classDefinition();
                    _fsp--;

                     value = classDefinition6; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:325:10: statement
                    {
                    pushFollow(FOLLOW_statement_in_moduleItem2138);
                    statement7=statement();
                    _fsp--;

                     value = statement7; 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:326:10: expression SEMI
                    {
                    pushFollow(FOLLOW_expression_in_moduleItem2160);
                    expression8=expression();
                    _fsp--;

                    match(input,SEMI,FOLLOW_SEMI_in_moduleItem2162); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:328:1: importDecl returns [JCTree value] : IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI ;
    public final JCTree importDecl() throws RecognitionException {
        JCTree value = null;

        Token STAR11=null;
        Token IMPORT12=null;
        JCIdent identifier9 = null;

        name_return name10 = null;


         JCExpression pid = null; 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:330:9: ( IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:330:11: IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI
            {
            IMPORT12=(Token)input.LT(1);
            match(input,IMPORT,FOLLOW_IMPORT_in_importDecl2199); 
            pushFollow(FOLLOW_identifier_in_importDecl2202);
            identifier9=identifier();
            _fsp--;

             pid = identifier9; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:331:18: ( DOT name )*
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
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:331:20: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_importDecl2227); 
            	    pushFollow(FOLLOW_name_in_importDecl2229);
            	    name10=name();
            	    _fsp--;

            	     pid = F.at(name10.pos).Select(pid, name10.value); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:332:18: ( DOT STAR )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==DOT) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:332:20: DOT STAR
                    {
                    match(input,DOT,FOLLOW_DOT_in_importDecl2258); 
                    STAR11=(Token)input.LT(1);
                    match(input,STAR,FOLLOW_STAR_in_importDecl2260); 
                     pid = F.at(pos(STAR11)).Select(pid, names.asterisk); 

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_importDecl2269); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:334:1: classDefinition returns [JFXClassDeclaration value] : modifierFlags CLASS name supers LBRACE classMembers RBRACE ;
    public final JFXClassDeclaration classDefinition() throws RecognitionException {
        JFXClassDeclaration value = null;

        Token CLASS13=null;
        JCModifiers modifierFlags14 = null;

        name_return name15 = null;

        ListBuffer<Name> supers16 = null;

        ListBuffer<JCTree> classMembers17 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:335:2: ( modifierFlags CLASS name supers LBRACE classMembers RBRACE )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:335:4: modifierFlags CLASS name supers LBRACE classMembers RBRACE
            {
            pushFollow(FOLLOW_modifierFlags_in_classDefinition2300);
            modifierFlags14=modifierFlags();
            _fsp--;

            CLASS13=(Token)input.LT(1);
            match(input,CLASS,FOLLOW_CLASS_in_classDefinition2303); 
            pushFollow(FOLLOW_name_in_classDefinition2305);
            name15=name();
            _fsp--;

            pushFollow(FOLLOW_supers_in_classDefinition2307);
            supers16=supers();
            _fsp--;

            match(input,LBRACE,FOLLOW_LBRACE_in_classDefinition2309); 
            pushFollow(FOLLOW_classMembers_in_classDefinition2311);
            classMembers17=classMembers();
            _fsp--;

            match(input,RBRACE,FOLLOW_RBRACE_in_classDefinition2313); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:338:1: supers returns [ListBuffer<Name> names = new ListBuffer<Name>()] : ( EXTENDS name1= name ( COMMA namen= name )* )? ;
    public final ListBuffer<Name> supers() throws RecognitionException {
        ListBuffer<Name> names =  new ListBuffer<Name>();

        name_return name1 = null;

        name_return namen = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:339:2: ( ( EXTENDS name1= name ( COMMA namen= name )* )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:339:4: ( EXTENDS name1= name ( COMMA namen= name )* )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:339:4: ( EXTENDS name1= name ( COMMA namen= name )* )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==EXTENDS) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:339:5: EXTENDS name1= name ( COMMA namen= name )*
                    {
                    match(input,EXTENDS,FOLLOW_EXTENDS_in_supers2338); 
                    pushFollow(FOLLOW_name_in_supers2342);
                    name1=name();
                    _fsp--;

                     names.append(name1.value); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:340:12: ( COMMA namen= name )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==COMMA) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:340:14: COMMA namen= name
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_supers2370); 
                    	    pushFollow(FOLLOW_name_in_supers2374);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:342:1: classMembers returns [ListBuffer<JCTree> mems = new ListBuffer<JCTree>()] : (ad1= attributeDefinition | fd1= functionDefinition )* ( initDefinition (ad2= attributeDefinition | fd2= functionDefinition )* )? ;
    public final ListBuffer<JCTree> classMembers() throws RecognitionException {
        ListBuffer<JCTree> mems =  new ListBuffer<JCTree>();

        JFXAttributeDefinition ad1 = null;

        JFXFunctionDefinition fd1 = null;

        JFXAttributeDefinition ad2 = null;

        JFXFunctionDefinition fd2 = null;

        JFXInitDefinition initDefinition18 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:343:2: ( (ad1= attributeDefinition | fd1= functionDefinition )* ( initDefinition (ad2= attributeDefinition | fd2= functionDefinition )* )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:343:4: (ad1= attributeDefinition | fd1= functionDefinition )* ( initDefinition (ad2= attributeDefinition | fd2= functionDefinition )* )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:343:4: (ad1= attributeDefinition | fd1= functionDefinition )*
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

                        if ( (LA8_13==FUNCTION) ) {
                            alt8=2;
                        }
                        else if ( (LA8_13==ATTRIBUTE) ) {
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

                        if ( (LA8_13==FUNCTION) ) {
                            alt8=2;
                        }
                        else if ( (LA8_13==ATTRIBUTE) ) {
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

                        if ( (LA8_13==FUNCTION) ) {
                            alt8=2;
                        }
                        else if ( (LA8_13==ATTRIBUTE) ) {
                            alt8=1;
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
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:343:6: ad1= attributeDefinition
            	    {
            	    pushFollow(FOLLOW_attributeDefinition_in_classMembers2411);
            	    ad1=attributeDefinition();
            	    _fsp--;

            	     mems.append(ad1); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:344:7: fd1= functionDefinition
            	    {
            	    pushFollow(FOLLOW_functionDefinition_in_classMembers2433);
            	    fd1=functionDefinition();
            	    _fsp--;

            	     mems.append(fd1); 

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:346:4: ( initDefinition (ad2= attributeDefinition | fd2= functionDefinition )* )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==INIT) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:346:5: initDefinition (ad2= attributeDefinition | fd2= functionDefinition )*
                    {
                    pushFollow(FOLLOW_initDefinition_in_classMembers2456);
                    initDefinition18=initDefinition();
                    _fsp--;

                     mems.append(initDefinition18); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:347:6: (ad2= attributeDefinition | fd2= functionDefinition )*
                    loop9:
                    do {
                        int alt9=3;
                        switch ( input.LA(1) ) {
                        case ABSTRACT:
                            {
                            switch ( input.LA(2) ) {
                            case PUBLIC:
                                {
                                int LA9_9 = input.LA(3);

                                if ( (LA9_9==ATTRIBUTE) ) {
                                    alt9=1;
                                }
                                else if ( (LA9_9==FUNCTION) ) {
                                    alt9=2;
                                }


                                }
                                break;
                            case PRIVATE:
                                {
                                int LA9_10 = input.LA(3);

                                if ( (LA9_10==ATTRIBUTE) ) {
                                    alt9=1;
                                }
                                else if ( (LA9_10==FUNCTION) ) {
                                    alt9=2;
                                }


                                }
                                break;
                            case PROTECTED:
                                {
                                int LA9_11 = input.LA(3);

                                if ( (LA9_11==ATTRIBUTE) ) {
                                    alt9=1;
                                }
                                else if ( (LA9_11==FUNCTION) ) {
                                    alt9=2;
                                }


                                }
                                break;
                            case FUNCTION:
                                {
                                alt9=2;
                                }
                                break;
                            case ATTRIBUTE:
                                {
                                alt9=1;
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
                                int LA9_9 = input.LA(3);

                                if ( (LA9_9==ATTRIBUTE) ) {
                                    alt9=1;
                                }
                                else if ( (LA9_9==FUNCTION) ) {
                                    alt9=2;
                                }


                                }
                                break;
                            case PRIVATE:
                                {
                                int LA9_10 = input.LA(3);

                                if ( (LA9_10==ATTRIBUTE) ) {
                                    alt9=1;
                                }
                                else if ( (LA9_10==FUNCTION) ) {
                                    alt9=2;
                                }


                                }
                                break;
                            case PROTECTED:
                                {
                                int LA9_11 = input.LA(3);

                                if ( (LA9_11==ATTRIBUTE) ) {
                                    alt9=1;
                                }
                                else if ( (LA9_11==FUNCTION) ) {
                                    alt9=2;
                                }


                                }
                                break;
                            case ATTRIBUTE:
                                {
                                alt9=1;
                                }
                                break;
                            case FUNCTION:
                                {
                                alt9=2;
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
                                int LA9_12 = input.LA(3);

                                if ( (LA9_12==ATTRIBUTE) ) {
                                    alt9=1;
                                }
                                else if ( (LA9_12==FUNCTION) ) {
                                    alt9=2;
                                }


                                }
                                break;
                            case READONLY:
                                {
                                int LA9_13 = input.LA(3);

                                if ( (LA9_13==ATTRIBUTE) ) {
                                    alt9=1;
                                }
                                else if ( (LA9_13==FUNCTION) ) {
                                    alt9=2;
                                }


                                }
                                break;
                            case FUNCTION:
                                {
                                alt9=2;
                                }
                                break;
                            case ATTRIBUTE:
                                {
                                alt9=1;
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
                                int LA9_12 = input.LA(3);

                                if ( (LA9_12==ATTRIBUTE) ) {
                                    alt9=1;
                                }
                                else if ( (LA9_12==FUNCTION) ) {
                                    alt9=2;
                                }


                                }
                                break;
                            case READONLY:
                                {
                                int LA9_13 = input.LA(3);

                                if ( (LA9_13==ATTRIBUTE) ) {
                                    alt9=1;
                                }
                                else if ( (LA9_13==FUNCTION) ) {
                                    alt9=2;
                                }


                                }
                                break;
                            case FUNCTION:
                                {
                                alt9=2;
                                }
                                break;
                            case ATTRIBUTE:
                                {
                                alt9=1;
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
                                int LA9_12 = input.LA(3);

                                if ( (LA9_12==ATTRIBUTE) ) {
                                    alt9=1;
                                }
                                else if ( (LA9_12==FUNCTION) ) {
                                    alt9=2;
                                }


                                }
                                break;
                            case READONLY:
                                {
                                int LA9_13 = input.LA(3);

                                if ( (LA9_13==ATTRIBUTE) ) {
                                    alt9=1;
                                }
                                else if ( (LA9_13==FUNCTION) ) {
                                    alt9=2;
                                }


                                }
                                break;
                            case FUNCTION:
                                {
                                alt9=2;
                                }
                                break;
                            case ATTRIBUTE:
                                {
                                alt9=1;
                                }
                                break;

                            }

                            }
                            break;
                        case ATTRIBUTE:
                            {
                            alt9=1;
                            }
                            break;
                        case FUNCTION:
                            {
                            alt9=2;
                            }
                            break;

                        }

                        switch (alt9) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:347:8: ad2= attributeDefinition
                    	    {
                    	    pushFollow(FOLLOW_attributeDefinition_in_classMembers2479);
                    	    ad2=attributeDefinition();
                    	    _fsp--;

                    	     mems.append(ad2); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:348:8: fd2= functionDefinition
                    	    {
                    	    pushFollow(FOLLOW_functionDefinition_in_classMembers2502);
                    	    fd2=functionDefinition();
                    	    _fsp--;

                    	     mems.append(fd2); 

                    	    }
                    	    break;

                    	default :
                    	    break loop9;
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
        return mems;
    }
    // $ANTLR end classMembers


    // $ANTLR start attributeDefinition
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:352:1: attributeDefinition returns [JFXAttributeDefinition def] : modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? ( ON CHANGE ocb= block )? SEMI ;
    public final JFXAttributeDefinition attributeDefinition() throws RecognitionException {
        JFXAttributeDefinition def = null;

        Token ATTRIBUTE19=null;
        JCBlock ocb = null;

        JCModifiers modifierFlags20 = null;

        name_return name21 = null;

        JFXType typeReference22 = null;

        JFXMemberSelector inverseClause23 = null;

        JavafxBindStatus bindOpt24 = null;

        JCExpression expression25 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:353:2: ( modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? ( ON CHANGE ocb= block )? SEMI )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:353:4: modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? ( ON CHANGE ocb= block )? SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_attributeDefinition2542);
            modifierFlags20=modifierFlags();
            _fsp--;

            ATTRIBUTE19=(Token)input.LT(1);
            match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeDefinition2544); 
            pushFollow(FOLLOW_name_in_attributeDefinition2546);
            name21=name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_attributeDefinition2554);
            typeReference22=typeReference();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:355:5: ( EQ bindOpt expression | inverseClause )?
            int alt11=3;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==EQ) ) {
                alt11=1;
            }
            else if ( (LA11_0==INVERSE) ) {
                alt11=2;
            }
            switch (alt11) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:355:6: EQ bindOpt expression
                    {
                    match(input,EQ,FOLLOW_EQ_in_attributeDefinition2562); 
                    pushFollow(FOLLOW_bindOpt_in_attributeDefinition2564);
                    bindOpt24=bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_attributeDefinition2566);
                    expression25=expression();
                    _fsp--;


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:355:30: inverseClause
                    {
                    pushFollow(FOLLOW_inverseClause_in_attributeDefinition2570);
                    inverseClause23=inverseClause();
                    _fsp--;


                    }
                    break;

            }

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:356:5: ( ON CHANGE ocb= block )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ON) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:356:6: ON CHANGE ocb= block
                    {
                    match(input,ON,FOLLOW_ON_in_attributeDefinition2580); 
                    match(input,CHANGE,FOLLOW_CHANGE_in_attributeDefinition2582); 
                    pushFollow(FOLLOW_block_in_attributeDefinition2586);
                    ocb=block();
                    _fsp--;


                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_attributeDefinition2595); 
             def = F.at(pos(ATTRIBUTE19)).AttributeDefinition(modifierFlags20,
            	    						name21.value, typeReference22, inverseClause23, null, 
            	    						bindOpt24, expression25, ocb); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:361:1: inverseClause returns [JFXMemberSelector inverse = null] : INVERSE memberSelector ;
    public final JFXMemberSelector inverseClause() throws RecognitionException {
        JFXMemberSelector inverse =  null;

        JFXMemberSelector memberSelector26 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:362:2: ( INVERSE memberSelector )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:362:4: INVERSE memberSelector
            {
            match(input,INVERSE,FOLLOW_INVERSE_in_inverseClause2621); 
            pushFollow(FOLLOW_memberSelector_in_inverseClause2623);
            memberSelector26=memberSelector();
            _fsp--;

             inverse = memberSelector26; 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:363:1: functionDefinition returns [JFXFunctionDefinition def] : modifierFlags FUNCTION name formalParameters typeReference blockExpression ;
    public final JFXFunctionDefinition functionDefinition() throws RecognitionException {
        JFXFunctionDefinition def = null;

        Token FUNCTION27=null;
        JCModifiers modifierFlags28 = null;

        name_return name29 = null;

        JFXType typeReference30 = null;

        ListBuffer<JCTree> formalParameters31 = null;

        JFXBlockExpression blockExpression32 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:364:2: ( modifierFlags FUNCTION name formalParameters typeReference blockExpression )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:364:4: modifierFlags FUNCTION name formalParameters typeReference blockExpression
            {
            pushFollow(FOLLOW_modifierFlags_in_functionDefinition2641);
            modifierFlags28=modifierFlags();
            _fsp--;

            FUNCTION27=(Token)input.LT(1);
            match(input,FUNCTION,FOLLOW_FUNCTION_in_functionDefinition2643); 
            pushFollow(FOLLOW_name_in_functionDefinition2645);
            name29=name();
            _fsp--;

            pushFollow(FOLLOW_formalParameters_in_functionDefinition2653);
            formalParameters31=formalParameters();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_functionDefinition2656);
            typeReference30=typeReference();
            _fsp--;

            pushFollow(FOLLOW_blockExpression_in_functionDefinition2665);
            blockExpression32=blockExpression();
            _fsp--;

             def = F.at(pos(FUNCTION27)).FunctionDefinition(modifierFlags28,
            	    						name29.value, typeReference30, 
            	    						formalParameters31.toList(), blockExpression32); 

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


    // $ANTLR start initDefinition
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:370:1: initDefinition returns [JFXInitDefinition def] : INIT block ;
    public final JFXInitDefinition initDefinition() throws RecognitionException {
        JFXInitDefinition def = null;

        Token INIT33=null;
        JCBlock block34 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:371:2: ( INIT block )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:371:4: INIT block
            {
            INIT33=(Token)input.LT(1);
            match(input,INIT,FOLLOW_INIT_in_initDefinition2685); 
            pushFollow(FOLLOW_block_in_initDefinition2687);
            block34=block();
            _fsp--;

             def = F.at(pos(INIT33)).InitDefinition(block34); 

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
    // $ANTLR end initDefinition


    // $ANTLR start modifierFlags
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:373:1: modifierFlags returns [JCModifiers mods] : (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? ;
    public final JCModifiers modifierFlags() throws RecognitionException {
        JCModifiers mods = null;

        long om1 = 0;

        long am1 = 0;

        long am2 = 0;

        long om2 = 0;


         long flags = 0; 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:375:2: ( (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:375:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:375:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
            int alt15=3;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==ABSTRACT||LA15_0==READONLY) ) {
                alt15=1;
            }
            else if ( ((LA15_0>=PRIVATE && LA15_0<=PUBLIC)) ) {
                alt15=2;
            }
            switch (alt15) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:375:6: om1= otherModifier (am1= accessModifier )?
                    {
                    pushFollow(FOLLOW_otherModifier_in_modifierFlags2716);
                    om1=otherModifier();
                    _fsp--;

                     flags |= om1; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:376:3: (am1= accessModifier )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( ((LA13_0>=PRIVATE && LA13_0<=PUBLIC)) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:376:5: am1= accessModifier
                            {
                            pushFollow(FOLLOW_accessModifier_in_modifierFlags2729);
                            am1=accessModifier();
                            _fsp--;

                             flags |= am1; 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:377:6: am2= accessModifier (om2= otherModifier )?
                    {
                    pushFollow(FOLLOW_accessModifier_in_modifierFlags2751);
                    am2=accessModifier();
                    _fsp--;

                     flags |= am2; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:378:3: (om2= otherModifier )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==ABSTRACT||LA14_0==READONLY) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:378:5: om2= otherModifier
                            {
                            pushFollow(FOLLOW_otherModifier_in_modifierFlags2764);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:381:1: accessModifier returns [long flags = 0] : ( PUBLIC | PRIVATE | PROTECTED ) ;
    public final long accessModifier() throws RecognitionException {
        long flags =  0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:382:2: ( ( PUBLIC | PRIVATE | PROTECTED ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:382:4: ( PUBLIC | PRIVATE | PROTECTED )
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:382:4: ( PUBLIC | PRIVATE | PROTECTED )
            int alt16=3;
            switch ( input.LA(1) ) {
            case PUBLIC:
                {
                alt16=1;
                }
                break;
            case PRIVATE:
                {
                alt16=2;
                }
                break;
            case PROTECTED:
                {
                alt16=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("382:4: ( PUBLIC | PRIVATE | PROTECTED )", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:382:5: PUBLIC
                    {
                    match(input,PUBLIC,FOLLOW_PUBLIC_in_accessModifier2812); 
                     flags |= Flags.PUBLIC; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:383:5: PRIVATE
                    {
                    match(input,PRIVATE,FOLLOW_PRIVATE_in_accessModifier2832); 
                     flags |= Flags.PUBLIC; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:384:5: PROTECTED
                    {
                    match(input,PROTECTED,FOLLOW_PROTECTED_in_accessModifier2851); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:385:1: otherModifier returns [long flags = 0] : ( ABSTRACT | READONLY ) ;
    public final long otherModifier() throws RecognitionException {
        long flags =  0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:386:2: ( ( ABSTRACT | READONLY ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:386:4: ( ABSTRACT | READONLY )
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:386:4: ( ABSTRACT | READONLY )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==ABSTRACT) ) {
                alt17=1;
            }
            else if ( (LA17_0==READONLY) ) {
                alt17=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("386:4: ( ABSTRACT | READONLY )", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:386:5: ABSTRACT
                    {
                    match(input,ABSTRACT,FOLLOW_ABSTRACT_in_otherModifier2878); 
                     flags |= Flags.ABSTRACT; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:387:5: READONLY
                    {
                    match(input,READONLY,FOLLOW_READONLY_in_otherModifier2896); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:388:1: memberSelector returns [JFXMemberSelector value] : name1= name DOT name2= name ;
    public final JFXMemberSelector memberSelector() throws RecognitionException {
        JFXMemberSelector value = null;

        name_return name1 = null;

        name_return name2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:389:2: (name1= name DOT name2= name )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:389:4: name1= name DOT name2= name
            {
            pushFollow(FOLLOW_name_in_memberSelector2925);
            name1=name();
            _fsp--;

            match(input,DOT,FOLLOW_DOT_in_memberSelector2929); 
            pushFollow(FOLLOW_name_in_memberSelector2935);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:391:1: formalParameters returns [ListBuffer<JCTree> params = new ListBuffer<JCTree>()] : LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN ;
    public final ListBuffer<JCTree> formalParameters() throws RecognitionException {
        ListBuffer<JCTree> params =  new ListBuffer<JCTree>();

        JFXVar fp0 = null;

        JFXVar fpn = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:392:2: ( LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:392:4: LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_formalParameters2953); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:392:12: (fp0= formalParameter ( COMMA fpn= formalParameter )* )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==QUOTED_IDENTIFIER||LA19_0==IDENTIFIER) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:392:14: fp0= formalParameter ( COMMA fpn= formalParameter )*
                    {
                    pushFollow(FOLLOW_formalParameter_in_formalParameters2960);
                    fp0=formalParameter();
                    _fsp--;

                     params.append(fp0); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:393:12: ( COMMA fpn= formalParameter )*
                    loop18:
                    do {
                        int alt18=2;
                        int LA18_0 = input.LA(1);

                        if ( (LA18_0==COMMA) ) {
                            alt18=1;
                        }


                        switch (alt18) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:393:14: COMMA fpn= formalParameter
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_formalParameters2978); 
                    	    pushFollow(FOLLOW_formalParameter_in_formalParameters2984);
                    	    fpn=formalParameter();
                    	    _fsp--;

                    	     params.append(fpn); 

                    	    }
                    	    break;

                    	default :
                    	    break loop18;
                        }
                    } while (true);


                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_formalParameters2995); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:395:1: formalParameter returns [JFXVar var] : name typeReference ;
    public final JFXVar formalParameter() throws RecognitionException {
        JFXVar var = null;

        name_return name35 = null;

        JFXType typeReference36 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:396:2: ( name typeReference )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:396:4: name typeReference
            {
            pushFollow(FOLLOW_name_in_formalParameter3010);
            name35=name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_formalParameter3012);
            typeReference36=typeReference();
            _fsp--;

             var = F.at(name35.pos).Var(name35.value, typeReference36, F.Modifiers(Flags.PARAMETER)); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:398:1: block returns [JCBlock value] : LBRACE ( statement | expression SEMI )* RBRACE ;
    public final JCBlock block() throws RecognitionException {
        JCBlock value = null;

        Token LBRACE39=null;
        JCStatement statement37 = null;

        JCExpression expression38 = null;


         ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>();
        	 	
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:401:2: ( LBRACE ( statement | expression SEMI )* RBRACE )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:401:4: LBRACE ( statement | expression SEMI )* RBRACE
            {
            LBRACE39=(Token)input.LT(1);
            match(input,LBRACE,FOLLOW_LBRACE_in_block3038); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:402:4: ( statement | expression SEMI )*
            loop20:
            do {
                int alt20=3;
                int LA20_0 = input.LA(1);

                if ( (LA20_0==ABSTRACT||LA20_0==BREAK||(LA20_0>=RETURN && LA20_0<=VAR)||(LA20_0>=WHILE && LA20_0<=CONTINUE)||LA20_0==TRY||(LA20_0>=PRIVATE && LA20_0<=READONLY)||LA20_0==SEMI) ) {
                    alt20=1;
                }
                else if ( ((LA20_0>=POUND && LA20_0<=TYPEOF)||LA20_0==IF||(LA20_0>=THIS && LA20_0<=FALSE)||(LA20_0>=NOT && LA20_0<=NEW)||LA20_0==SUPER||(LA20_0>=SIZEOF && LA20_0<=LPAREN)||LA20_0==LBRACKET||(LA20_0>=PLUSPLUS && LA20_0<=SUBSUB)||(LA20_0>=QUES && LA20_0<=STRING_LITERAL)||(LA20_0>=QUOTE_LBRACE_STRING_LITERAL && LA20_0<=LBRACE)||(LA20_0>=QUOTED_IDENTIFIER && LA20_0<=INTEGER_LITERAL)||LA20_0==FLOATING_POINT_LITERAL||LA20_0==IDENTIFIER) ) {
                    alt20=2;
                }


                switch (alt20) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:402:9: statement
            	    {
            	    pushFollow(FOLLOW_statement_in_block3048);
            	    statement37=statement();
            	    _fsp--;

            	     stats.append(statement37); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:403:9: expression SEMI
            	    {
            	    pushFollow(FOLLOW_expression_in_block3063);
            	    expression38=expression();
            	    _fsp--;

            	    match(input,SEMI,FOLLOW_SEMI_in_block3065); 
            	     stats.append(F.Exec(expression38)); 

            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);

            match(input,RBRACE,FOLLOW_RBRACE_in_block3081); 
             value = F.at(pos(LBRACE39)).Block(0L, stats.toList()); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:407:1: blockExpression returns [JFXBlockExpression expr = null] : LBRACE ( statements[stats] )? RBRACE ;
    public final JFXBlockExpression blockExpression() throws RecognitionException {
        JFXBlockExpression expr =  null;

        Token LBRACE40=null;
        JCExpression statements41 = null;


         ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>(); 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:409:2: ( LBRACE ( statements[stats] )? RBRACE )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:409:4: LBRACE ( statements[stats] )? RBRACE
            {
            LBRACE40=(Token)input.LT(1);
            match(input,LBRACE,FOLLOW_LBRACE_in_blockExpression3105); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:409:11: ( statements[stats] )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( ((LA21_0>=POUND && LA21_0<=TYPEOF)||LA21_0==ABSTRACT||LA21_0==BREAK||(LA21_0>=RETURN && LA21_0<=VAR)||LA21_0==IF||(LA21_0>=THIS && LA21_0<=FALSE)||(LA21_0>=WHILE && LA21_0<=CONTINUE)||LA21_0==TRY||(LA21_0>=NOT && LA21_0<=READONLY)||LA21_0==SUPER||(LA21_0>=SIZEOF && LA21_0<=LPAREN)||LA21_0==LBRACKET||LA21_0==SEMI||(LA21_0>=PLUSPLUS && LA21_0<=SUBSUB)||(LA21_0>=QUES && LA21_0<=STRING_LITERAL)||(LA21_0>=QUOTE_LBRACE_STRING_LITERAL && LA21_0<=LBRACE)||(LA21_0>=QUOTED_IDENTIFIER && LA21_0<=INTEGER_LITERAL)||LA21_0==FLOATING_POINT_LITERAL||LA21_0==IDENTIFIER) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:409:12: statements[stats]
                    {
                    pushFollow(FOLLOW_statements_in_blockExpression3108);
                    statements41=statements(stats);
                    _fsp--;


                    }
                    break;

            }

            match(input,RBRACE,FOLLOW_RBRACE_in_blockExpression3113); 
             expr = F.at(pos(LBRACE40)).BlockExpression(0L, stats.toList(), 
            						 					     statements41); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:412:1: statements[ListBuffer<JCStatement> stats] returns [JCExpression expr = null] : ( statement (sts1= statements[stats] )? | expression ( SEMI (stsn= statements[stats] | ) | ) );
    public final JCExpression statements(ListBuffer<JCStatement> stats) throws RecognitionException {
        JCExpression expr =  null;

        JCExpression sts1 = null;

        JCExpression stsn = null;

        JCStatement statement42 = null;

        JCExpression expression43 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:413:2: ( statement (sts1= statements[stats] )? | expression ( SEMI (stsn= statements[stats] | ) | ) )
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==ABSTRACT||LA25_0==BREAK||(LA25_0>=RETURN && LA25_0<=VAR)||(LA25_0>=WHILE && LA25_0<=CONTINUE)||LA25_0==TRY||(LA25_0>=PRIVATE && LA25_0<=READONLY)||LA25_0==SEMI) ) {
                alt25=1;
            }
            else if ( ((LA25_0>=POUND && LA25_0<=TYPEOF)||LA25_0==IF||(LA25_0>=THIS && LA25_0<=FALSE)||(LA25_0>=NOT && LA25_0<=NEW)||LA25_0==SUPER||(LA25_0>=SIZEOF && LA25_0<=LPAREN)||LA25_0==LBRACKET||(LA25_0>=PLUSPLUS && LA25_0<=SUBSUB)||(LA25_0>=QUES && LA25_0<=STRING_LITERAL)||(LA25_0>=QUOTE_LBRACE_STRING_LITERAL && LA25_0<=LBRACE)||(LA25_0>=QUOTED_IDENTIFIER && LA25_0<=INTEGER_LITERAL)||LA25_0==FLOATING_POINT_LITERAL||LA25_0==IDENTIFIER) ) {
                alt25=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("412:1: statements[ListBuffer<JCStatement> stats] returns [JCExpression expr = null] : ( statement (sts1= statements[stats] )? | expression ( SEMI (stsn= statements[stats] | ) | ) );", 25, 0, input);

                throw nvae;
            }
            switch (alt25) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:413:4: statement (sts1= statements[stats] )?
                    {
                    pushFollow(FOLLOW_statement_in_statements3132);
                    statement42=statement();
                    _fsp--;

                     stats.append(statement42); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:414:3: (sts1= statements[stats] )?
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( ((LA22_0>=POUND && LA22_0<=TYPEOF)||LA22_0==ABSTRACT||LA22_0==BREAK||(LA22_0>=RETURN && LA22_0<=VAR)||LA22_0==IF||(LA22_0>=THIS && LA22_0<=FALSE)||(LA22_0>=WHILE && LA22_0<=CONTINUE)||LA22_0==TRY||(LA22_0>=NOT && LA22_0<=READONLY)||LA22_0==SUPER||(LA22_0>=SIZEOF && LA22_0<=LPAREN)||LA22_0==LBRACKET||LA22_0==SEMI||(LA22_0>=PLUSPLUS && LA22_0<=SUBSUB)||(LA22_0>=QUES && LA22_0<=STRING_LITERAL)||(LA22_0>=QUOTE_LBRACE_STRING_LITERAL && LA22_0<=LBRACE)||(LA22_0>=QUOTED_IDENTIFIER && LA22_0<=INTEGER_LITERAL)||LA22_0==FLOATING_POINT_LITERAL||LA22_0==IDENTIFIER) ) {
                        alt22=1;
                    }
                    switch (alt22) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:414:4: sts1= statements[stats]
                            {
                            pushFollow(FOLLOW_statements_in_statements3144);
                            sts1=statements(stats);
                            _fsp--;

                             expr = sts1; 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:415:4: expression ( SEMI (stsn= statements[stats] | ) | )
                    {
                    pushFollow(FOLLOW_expression_in_statements3156);
                    expression43=expression();
                    _fsp--;

                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:416:10: ( SEMI (stsn= statements[stats] | ) | )
                    int alt24=2;
                    int LA24_0 = input.LA(1);

                    if ( (LA24_0==SEMI) ) {
                        alt24=1;
                    }
                    else if ( (LA24_0==RBRACE) ) {
                        alt24=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("416:10: ( SEMI (stsn= statements[stats] | ) | )", 24, 0, input);

                        throw nvae;
                    }
                    switch (alt24) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:416:11: SEMI (stsn= statements[stats] | )
                            {
                            match(input,SEMI,FOLLOW_SEMI_in_statements3168); 
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:416:17: (stsn= statements[stats] | )
                            int alt23=2;
                            int LA23_0 = input.LA(1);

                            if ( ((LA23_0>=POUND && LA23_0<=TYPEOF)||LA23_0==ABSTRACT||LA23_0==BREAK||(LA23_0>=RETURN && LA23_0<=VAR)||LA23_0==IF||(LA23_0>=THIS && LA23_0<=FALSE)||(LA23_0>=WHILE && LA23_0<=CONTINUE)||LA23_0==TRY||(LA23_0>=NOT && LA23_0<=READONLY)||LA23_0==SUPER||(LA23_0>=SIZEOF && LA23_0<=LPAREN)||LA23_0==LBRACKET||LA23_0==SEMI||(LA23_0>=PLUSPLUS && LA23_0<=SUBSUB)||(LA23_0>=QUES && LA23_0<=STRING_LITERAL)||(LA23_0>=QUOTE_LBRACE_STRING_LITERAL && LA23_0<=LBRACE)||(LA23_0>=QUOTED_IDENTIFIER && LA23_0<=INTEGER_LITERAL)||LA23_0==FLOATING_POINT_LITERAL||LA23_0==IDENTIFIER) ) {
                                alt23=1;
                            }
                            else if ( (LA23_0==RBRACE) ) {
                                alt23=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("416:17: (stsn= statements[stats] | )", 23, 0, input);

                                throw nvae;
                            }
                            switch (alt23) {
                                case 1 :
                                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:416:21: stsn= statements[stats]
                                    {
                                     stats.append(F.Exec(expression43)); 
                                    pushFollow(FOLLOW_statements_in_statements3191);
                                    stsn=statements(stats);
                                    _fsp--;

                                     expr = stsn; 

                                    }
                                    break;
                                case 2 :
                                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:418:30: 
                                    {
                                     expr = expression43; 

                                    }
                                    break;

                            }


                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:420:20: 
                            {
                             expr = expression43; 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:423:1: statement returns [JCStatement value] : ( variableDeclaration SEMI | functionDefinition | WHILE LPAREN expression RPAREN block | BREAK SEMI | CONTINUE SEMI | THROW expression SEMI | returnStatement SEMI | tryStatement | SEMI );
    public final JCStatement statement() throws RecognitionException {
        JCStatement value = null;

        Token WHILE46=null;
        Token BREAK49=null;
        Token CONTINUE50=null;
        Token THROW51=null;
        Token SEMI55=null;
        JCStatement variableDeclaration44 = null;

        JFXFunctionDefinition functionDefinition45 = null;

        JCExpression expression47 = null;

        JCBlock block48 = null;

        JCExpression expression52 = null;

        JCStatement returnStatement53 = null;

        JCStatement tryStatement54 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:424:2: ( variableDeclaration SEMI | functionDefinition | WHILE LPAREN expression RPAREN block | BREAK SEMI | CONTINUE SEMI | THROW expression SEMI | returnStatement SEMI | tryStatement | SEMI )
            int alt26=9;
            switch ( input.LA(1) ) {
            case VAR:
                {
                alt26=1;
                }
                break;
            case ABSTRACT:
            case PRIVATE:
            case PROTECTED:
            case PUBLIC:
            case FUNCTION:
            case READONLY:
                {
                alt26=2;
                }
                break;
            case WHILE:
                {
                alt26=3;
                }
                break;
            case BREAK:
                {
                alt26=4;
                }
                break;
            case CONTINUE:
                {
                alt26=5;
                }
                break;
            case THROW:
                {
                alt26=6;
                }
                break;
            case RETURN:
                {
                alt26=7;
                }
                break;
            case TRY:
                {
                alt26=8;
                }
                break;
            case SEMI:
                {
                alt26=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("423:1: statement returns [JCStatement value] : ( variableDeclaration SEMI | functionDefinition | WHILE LPAREN expression RPAREN block | BREAK SEMI | CONTINUE SEMI | THROW expression SEMI | returnStatement SEMI | tryStatement | SEMI );", 26, 0, input);

                throw nvae;
            }

            switch (alt26) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:424:4: variableDeclaration SEMI
                    {
                    pushFollow(FOLLOW_variableDeclaration_in_statement3249);
                    variableDeclaration44=variableDeclaration();
                    _fsp--;

                    match(input,SEMI,FOLLOW_SEMI_in_statement3251); 
                     value = variableDeclaration44; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:425:4: functionDefinition
                    {
                    pushFollow(FOLLOW_functionDefinition_in_statement3259);
                    functionDefinition45=functionDefinition();
                    _fsp--;

                     value = functionDefinition45; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:426:11: WHILE LPAREN expression RPAREN block
                    {
                    WHILE46=(Token)input.LT(1);
                    match(input,WHILE,FOLLOW_WHILE_in_statement3275); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_statement3277); 
                    pushFollow(FOLLOW_expression_in_statement3279);
                    expression47=expression();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_statement3281); 
                    pushFollow(FOLLOW_block_in_statement3283);
                    block48=block();
                    _fsp--;

                     value = F.at(pos(WHILE46)).WhileLoop(expression47, block48); 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:427:4: BREAK SEMI
                    {
                    BREAK49=(Token)input.LT(1);
                    match(input,BREAK,FOLLOW_BREAK_in_statement3290); 
                    match(input,SEMI,FOLLOW_SEMI_in_statement3292); 
                     value = F.at(pos(BREAK49)).Break(null); 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:428:4: CONTINUE SEMI
                    {
                    CONTINUE50=(Token)input.LT(1);
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_statement3305); 
                    match(input,SEMI,FOLLOW_SEMI_in_statement3308); 
                     value = F.at(pos(CONTINUE50)).Continue(null); 

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:429:11: THROW expression SEMI
                    {
                    THROW51=(Token)input.LT(1);
                    match(input,THROW,FOLLOW_THROW_in_statement3326); 
                    pushFollow(FOLLOW_expression_in_statement3328);
                    expression52=expression();
                    _fsp--;

                    match(input,SEMI,FOLLOW_SEMI_in_statement3330); 
                     value = F.at(pos(THROW51)).Throw(expression52); 

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:430:11: returnStatement SEMI
                    {
                    pushFollow(FOLLOW_returnStatement_in_statement3350);
                    returnStatement53=returnStatement();
                    _fsp--;

                    match(input,SEMI,FOLLOW_SEMI_in_statement3352); 
                     value = returnStatement53; 

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:431:11: tryStatement
                    {
                    pushFollow(FOLLOW_tryStatement_in_statement3368);
                    tryStatement54=tryStatement();
                    _fsp--;

                     value = tryStatement54; 

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:432:4: SEMI
                    {
                    SEMI55=(Token)input.LT(1);
                    match(input,SEMI,FOLLOW_SEMI_in_statement3379); 
                     value = F.at(pos(SEMI55)).Skip(); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:434:1: variableDeclaration returns [JCStatement value] : VAR name typeReference ( EQ bindOpt expression | ) ;
    public final JCStatement variableDeclaration() throws RecognitionException {
        JCStatement value = null;

        Token VAR56=null;
        name_return name57 = null;

        JFXType typeReference58 = null;

        JCExpression expression59 = null;

        JavafxBindStatus bindOpt60 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:435:2: ( VAR name typeReference ( EQ bindOpt expression | ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:435:4: VAR name typeReference ( EQ bindOpt expression | )
            {
            VAR56=(Token)input.LT(1);
            match(input,VAR,FOLLOW_VAR_in_variableDeclaration3409); 
            pushFollow(FOLLOW_name_in_variableDeclaration3412);
            name57=name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_variableDeclaration3415);
            typeReference58=typeReference();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:436:6: ( EQ bindOpt expression | )
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==EQ) ) {
                alt27=1;
            }
            else if ( (LA27_0==SEMI) ) {
                alt27=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("436:6: ( EQ bindOpt expression | )", 27, 0, input);

                throw nvae;
            }
            switch (alt27) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:436:8: EQ bindOpt expression
                    {
                    match(input,EQ,FOLLOW_EQ_in_variableDeclaration3426); 
                    pushFollow(FOLLOW_bindOpt_in_variableDeclaration3428);
                    bindOpt60=bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_variableDeclaration3431);
                    expression59=expression();
                    _fsp--;

                     value = F.at(pos(VAR56)).VarInit(name57.value, typeReference58, 
                    	    							expression59, bindOpt60); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:438:13: 
                    {
                     value = F.at(pos(VAR56)).VarStatement(name57.value, typeReference58); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:441:1: bindOpt returns [JavafxBindStatus status = UNBOUND] : ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? ;
    public final JavafxBindStatus bindOpt() throws RecognitionException {
        JavafxBindStatus status =  UNBOUND;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:442:2: ( ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:442:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:442:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
            int alt31=4;
            switch ( input.LA(1) ) {
                case BIND:
                    {
                    alt31=1;
                    }
                    break;
                case STAYS:
                    {
                    alt31=2;
                    }
                    break;
                case TIE:
                    {
                    alt31=3;
                    }
                    break;
            }

            switch (alt31) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:442:6: BIND ( LAZY )?
                    {
                    match(input,BIND,FOLLOW_BIND_in_bindOpt3481); 
                     status = UNIDIBIND; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:443:8: ( LAZY )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==LAZY) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:443:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3497); 
                             status = LAZY_UNIDIBIND; 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:444:6: STAYS ( LAZY )?
                    {
                    match(input,STAYS,FOLLOW_STAYS_in_bindOpt3512); 
                     status = UNIDIBIND; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:445:8: ( LAZY )?
                    int alt29=2;
                    int LA29_0 = input.LA(1);

                    if ( (LA29_0==LAZY) ) {
                        alt29=1;
                    }
                    switch (alt29) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:445:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3528); 
                             status = LAZY_UNIDIBIND; 

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:446:6: TIE ( LAZY )?
                    {
                    match(input,TIE,FOLLOW_TIE_in_bindOpt3543); 
                     status = BIDIBIND; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:447:8: ( LAZY )?
                    int alt30=2;
                    int LA30_0 = input.LA(1);

                    if ( (LA30_0==LAZY) ) {
                        alt30=1;
                    }
                    switch (alt30) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:447:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3559); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:449:1: returnStatement returns [JCStatement value] : RETURN ( expression )? ;
    public final JCStatement returnStatement() throws RecognitionException {
        JCStatement value = null;

        Token RETURN62=null;
        JCExpression expression61 = null;


         JCExpression expr = null; 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:451:2: ( RETURN ( expression )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:451:4: RETURN ( expression )?
            {
            RETURN62=(Token)input.LT(1);
            match(input,RETURN,FOLLOW_RETURN_in_returnStatement3593); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:451:11: ( expression )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( ((LA32_0>=POUND && LA32_0<=TYPEOF)||LA32_0==IF||(LA32_0>=THIS && LA32_0<=FALSE)||(LA32_0>=NOT && LA32_0<=NEW)||LA32_0==SUPER||(LA32_0>=SIZEOF && LA32_0<=LPAREN)||LA32_0==LBRACKET||(LA32_0>=PLUSPLUS && LA32_0<=SUBSUB)||(LA32_0>=QUES && LA32_0<=STRING_LITERAL)||(LA32_0>=QUOTE_LBRACE_STRING_LITERAL && LA32_0<=LBRACE)||(LA32_0>=QUOTED_IDENTIFIER && LA32_0<=INTEGER_LITERAL)||LA32_0==FLOATING_POINT_LITERAL||LA32_0==IDENTIFIER) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:451:12: expression
                    {
                    pushFollow(FOLLOW_expression_in_returnStatement3596);
                    expression61=expression();
                    _fsp--;

                     expr = expression61; 

                    }
                    break;

            }

             value = F.at(pos(RETURN62)).Return(expr); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:454:1: tryStatement returns [JCStatement value] : TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? ) ;
    public final JCStatement tryStatement() throws RecognitionException {
        JCStatement value = null;

        Token TRY64=null;
        JCBlock tb = null;

        JCBlock fb1 = null;

        JCBlock fb2 = null;

        JCCatch catchClause63 = null;


        	JCBlock body;
        		ListBuffer<JCCatch> catchers = new ListBuffer<JCCatch>();
        		JCBlock finalBlock = null;
        	
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:459:2: ( TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:459:4: TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )
            {
            TRY64=(Token)input.LT(1);
            match(input,TRY,FOLLOW_TRY_in_tryStatement3639); 
            pushFollow(FOLLOW_block_in_tryStatement3645);
            tb=block();
            _fsp--;

             body = tb; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:460:5: ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==FINALLY) ) {
                alt35=1;
            }
            else if ( (LA35_0==CATCH) ) {
                alt35=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("460:5: ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )", 35, 0, input);

                throw nvae;
            }
            switch (alt35) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:460:7: FINALLY fb1= block
                    {
                    match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement3658); 
                    pushFollow(FOLLOW_block_in_tryStatement3664);
                    fb1=block();
                    _fsp--;

                     finalBlock = fb1; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:461:10: ( catchClause )+ ( FINALLY fb2= block )?
                    {
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:461:10: ( catchClause )+
                    int cnt33=0;
                    loop33:
                    do {
                        int alt33=2;
                        int LA33_0 = input.LA(1);

                        if ( (LA33_0==CATCH) ) {
                            alt33=1;
                        }


                        switch (alt33) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:461:11: catchClause
                    	    {
                    	    pushFollow(FOLLOW_catchClause_in_tryStatement3680);
                    	    catchClause63=catchClause();
                    	    _fsp--;

                    	     catchers.append(catchClause63); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt33 >= 1 ) break loop33;
                                EarlyExitException eee =
                                    new EarlyExitException(33, input);
                                throw eee;
                        }
                        cnt33++;
                    } while (true);

                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:462:10: ( FINALLY fb2= block )?
                    int alt34=2;
                    int LA34_0 = input.LA(1);

                    if ( (LA34_0==FINALLY) ) {
                        alt34=1;
                    }
                    switch (alt34) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:462:11: FINALLY fb2= block
                            {
                            match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement3703); 
                            pushFollow(FOLLOW_block_in_tryStatement3708);
                            fb2=block();
                            _fsp--;

                             finalBlock = fb2; 

                            }
                            break;

                    }


                    }
                    break;

            }

             value = F.at(pos(TRY64)).Try(body, catchers.toList(), finalBlock); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:465:1: catchClause returns [JCCatch value] : CATCH LPAREN name ( COLON identifier )? RPAREN block ;
    public final JCCatch catchClause() throws RecognitionException {
        JCCatch value = null;

        Token CATCH67=null;
        name_return name65 = null;

        JCIdent identifier66 = null;

        JCBlock block68 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:466:2: ( CATCH LPAREN name ( COLON identifier )? RPAREN block )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:466:4: CATCH LPAREN name ( COLON identifier )? RPAREN block
            {
            CATCH67=(Token)input.LT(1);
            match(input,CATCH,FOLLOW_CATCH_in_catchClause3747); 
            match(input,LPAREN,FOLLOW_LPAREN_in_catchClause3750); 
            pushFollow(FOLLOW_name_in_catchClause3753);
            name65=name();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:467:4: ( COLON identifier )?
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==COLON) ) {
                alt36=1;
            }
            switch (alt36) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:467:5: COLON identifier
                    {
                    match(input,COLON,FOLLOW_COLON_in_catchClause3761); 
                    pushFollow(FOLLOW_identifier_in_catchClause3763);
                    identifier66=identifier();
                    _fsp--;


                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_catchClause3772); 
            pushFollow(FOLLOW_block_in_catchClause3776);
            block68=block();
            _fsp--;

             JCModifiers mods = F.at(name65.pos).Modifiers(Flags.PARAMETER);
            	  					  JCVariableDecl formal = F.at(name65.pos).VarDef(mods, name65.value, identifier66, null);
            	  					  value = F.at(pos(CATCH67)).Catch(formal, block68); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:472:1: expression returns [JCExpression expr] : ( ifExpression | suffixedExpression );
    public final JCExpression expression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression ifExpression69 = null;

        JCExpression suffixedExpression70 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:473:9: ( ifExpression | suffixedExpression )
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==IF) ) {
                alt37=1;
            }
            else if ( ((LA37_0>=POUND && LA37_0<=TYPEOF)||(LA37_0>=THIS && LA37_0<=FALSE)||(LA37_0>=NOT && LA37_0<=NEW)||LA37_0==SUPER||(LA37_0>=SIZEOF && LA37_0<=LPAREN)||LA37_0==LBRACKET||(LA37_0>=PLUSPLUS && LA37_0<=SUBSUB)||(LA37_0>=QUES && LA37_0<=STRING_LITERAL)||(LA37_0>=QUOTE_LBRACE_STRING_LITERAL && LA37_0<=LBRACE)||(LA37_0>=QUOTED_IDENTIFIER && LA37_0<=INTEGER_LITERAL)||LA37_0==FLOATING_POINT_LITERAL||LA37_0==IDENTIFIER) ) {
                alt37=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("472:1: expression returns [JCExpression expr] : ( ifExpression | suffixedExpression );", 37, 0, input);

                throw nvae;
            }
            switch (alt37) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:473:11: ifExpression
                    {
                    pushFollow(FOLLOW_ifExpression_in_expression3804);
                    ifExpression69=ifExpression();
                    _fsp--;

                     expr = ifExpression69; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:474:11: suffixedExpression
                    {
                    pushFollow(FOLLOW_suffixedExpression_in_expression3827);
                    suffixedExpression70=suffixedExpression();
                    _fsp--;

                     expr = suffixedExpression70; 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:477:1: ifExpression returns [JCExpression expr] : IF econd= expression THEN ethen= expression ( ELSE eelse= expression )? ;
    public final JCExpression ifExpression() throws RecognitionException {
        JCExpression expr = null;

        Token IF71=null;
        JCExpression econd = null;

        JCExpression ethen = null;

        JCExpression eelse = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:478:2: ( IF econd= expression THEN ethen= expression ( ELSE eelse= expression )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:478:4: IF econd= expression THEN ethen= expression ( ELSE eelse= expression )?
            {
            IF71=(Token)input.LT(1);
            match(input,IF,FOLLOW_IF_in_ifExpression3858); 
            pushFollow(FOLLOW_expression_in_ifExpression3862);
            econd=expression();
            _fsp--;

            match(input,THEN,FOLLOW_THEN_in_ifExpression3866); 
            pushFollow(FOLLOW_expression_in_ifExpression3871);
            ethen=expression();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:479:4: ( ELSE eelse= expression )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==ELSE) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:479:5: ELSE eelse= expression
                    {
                    match(input,ELSE,FOLLOW_ELSE_in_ifExpression3880); 
                    pushFollow(FOLLOW_expression_in_ifExpression3885);
                    eelse=expression();
                    _fsp--;


                    }
                    break;

            }

             JCExpression elsepart = eelse;
            	  							  if (elsepart == null) elsepart = F.at(pos(IF71)).Literal(TypeTags.BOT, null);
            	  							  expr = F.at(pos(IF71)).Conditional(econd, ethen, elsepart); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:483:1: suffixedExpression returns [JCExpression expr] : e1= assignmentExpression ( PLUSPLUS | SUBSUB )? ;
    public final JCExpression suffixedExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:484:2: (e1= assignmentExpression ( PLUSPLUS | SUBSUB )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:484:4: e1= assignmentExpression ( PLUSPLUS | SUBSUB )?
            {
            pushFollow(FOLLOW_assignmentExpression_in_suffixedExpression3910);
            e1=assignmentExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:485:5: ( PLUSPLUS | SUBSUB )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==PLUSPLUS||LA39_0==SUBSUB) ) {
                alt39=1;
            }
            switch (alt39) {
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
                        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_suffixedExpression3921);    throw mse;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:487:1: assignmentExpression returns [JCExpression expr] : e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? ;
    public final JCExpression assignmentExpression() throws RecognitionException {
        JCExpression expr = null;

        Token EQ72=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:488:2: (e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:488:4: e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )?
            {
            pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression3950);
            e1=assignmentOpExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:489:5: ( EQ e2= assignmentOpExpression )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==EQ) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:489:9: EQ e2= assignmentOpExpression
                    {
                    EQ72=(Token)input.LT(1);
                    match(input,EQ,FOLLOW_EQ_in_assignmentExpression3965); 
                    pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression3971);
                    e2=assignmentOpExpression();
                    _fsp--;

                     expr = F.at(pos(EQ72)).Assign(expr, e2); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:490:1: assignmentOpExpression returns [JCExpression expr] : e1= andExpression ( assignmentOperator e2= andExpression )? ;
    public final JCExpression assignmentOpExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;

        JCExpression e2 = null;

        int assignmentOperator73 = 0;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:491:2: (e1= andExpression ( assignmentOperator e2= andExpression )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:491:4: e1= andExpression ( assignmentOperator e2= andExpression )?
            {
            pushFollow(FOLLOW_andExpression_in_assignmentOpExpression3998);
            e1=andExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:492:5: ( assignmentOperator e2= andExpression )?
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( ((LA41_0>=PLUSEQ && LA41_0<=PERCENTEQ)) ) {
                alt41=1;
            }
            switch (alt41) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:492:9: assignmentOperator e2= andExpression
                    {
                    pushFollow(FOLLOW_assignmentOperator_in_assignmentOpExpression4014);
                    assignmentOperator73=assignmentOperator();
                    _fsp--;

                    pushFollow(FOLLOW_andExpression_in_assignmentOpExpression4020);
                    e2=andExpression();
                    _fsp--;

                     expr = F.Assignop(assignmentOperator73,
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:494:1: andExpression returns [JCExpression expr] : e1= orExpression ( AND e2= orExpression )* ;
    public final JCExpression andExpression() throws RecognitionException {
        JCExpression expr = null;

        Token AND74=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:495:2: (e1= orExpression ( AND e2= orExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:495:4: e1= orExpression ( AND e2= orExpression )*
            {
            pushFollow(FOLLOW_orExpression_in_andExpression4046);
            e1=orExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:496:5: ( AND e2= orExpression )*
            loop42:
            do {
                int alt42=2;
                int LA42_0 = input.LA(1);

                if ( (LA42_0==AND) ) {
                    alt42=1;
                }


                switch (alt42) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:496:9: AND e2= orExpression
            	    {
            	    AND74=(Token)input.LT(1);
            	    match(input,AND,FOLLOW_AND_in_andExpression4062); 
            	    pushFollow(FOLLOW_orExpression_in_andExpression4068);
            	    e2=orExpression();
            	    _fsp--;

            	     expr = F.at(pos(AND74)).Binary(JCTree.AND, expr, e2); 

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
    // $ANTLR end andExpression


    // $ANTLR start orExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:497:1: orExpression returns [JCExpression expr] : e1= instanceOfExpression ( OR e2= instanceOfExpression )* ;
    public final JCExpression orExpression() throws RecognitionException {
        JCExpression expr = null;

        Token OR75=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:498:2: (e1= instanceOfExpression ( OR e2= instanceOfExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:498:4: e1= instanceOfExpression ( OR e2= instanceOfExpression )*
            {
            pushFollow(FOLLOW_instanceOfExpression_in_orExpression4096);
            e1=instanceOfExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:499:5: ( OR e2= instanceOfExpression )*
            loop43:
            do {
                int alt43=2;
                int LA43_0 = input.LA(1);

                if ( (LA43_0==OR) ) {
                    alt43=1;
                }


                switch (alt43) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:499:9: OR e2= instanceOfExpression
            	    {
            	    OR75=(Token)input.LT(1);
            	    match(input,OR,FOLLOW_OR_in_orExpression4111); 
            	    pushFollow(FOLLOW_instanceOfExpression_in_orExpression4117);
            	    e2=instanceOfExpression();
            	    _fsp--;

            	     expr = F.at(pos(OR75)).Binary(JCTree.OR, expr, e2); 

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
    // $ANTLR end orExpression


    // $ANTLR start instanceOfExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:500:1: instanceOfExpression returns [JCExpression expr] : e1= relationalExpression ( INSTANCEOF identifier )? ;
    public final JCExpression instanceOfExpression() throws RecognitionException {
        JCExpression expr = null;

        Token INSTANCEOF76=null;
        JCExpression e1 = null;

        JCIdent identifier77 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:501:2: (e1= relationalExpression ( INSTANCEOF identifier )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:501:4: e1= relationalExpression ( INSTANCEOF identifier )?
            {
            pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression4145);
            e1=relationalExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:502:5: ( INSTANCEOF identifier )?
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==INSTANCEOF) ) {
                alt44=1;
            }
            switch (alt44) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:502:9: INSTANCEOF identifier
                    {
                    INSTANCEOF76=(Token)input.LT(1);
                    match(input,INSTANCEOF,FOLLOW_INSTANCEOF_in_instanceOfExpression4160); 
                    pushFollow(FOLLOW_identifier_in_instanceOfExpression4162);
                    identifier77=identifier();
                    _fsp--;

                     expr = F.at(pos(INSTANCEOF76)).Binary(JCTree.TYPETEST, expr, 
                    	   													 identifier77); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:504:1: relationalExpression returns [JCExpression expr] : e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* ;
    public final JCExpression relationalExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LTGT78=null;
        Token EQEQ79=null;
        Token LTEQ80=null;
        Token GTEQ81=null;
        Token LT82=null;
        Token GT83=null;
        Token IN84=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:505:2: (e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:505:4: e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            {
            pushFollow(FOLLOW_additiveExpression_in_relationalExpression4190);
            e1=additiveExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:506:5: ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            loop45:
            do {
                int alt45=8;
                switch ( input.LA(1) ) {
                case LTGT:
                    {
                    alt45=1;
                    }
                    break;
                case EQEQ:
                    {
                    alt45=2;
                    }
                    break;
                case LTEQ:
                    {
                    alt45=3;
                    }
                    break;
                case GTEQ:
                    {
                    alt45=4;
                    }
                    break;
                case LT:
                    {
                    alt45=5;
                    }
                    break;
                case GT:
                    {
                    alt45=6;
                    }
                    break;
                case IN:
                    {
                    alt45=7;
                    }
                    break;

                }

                switch (alt45) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:506:9: LTGT e= additiveExpression
            	    {
            	    LTGT78=(Token)input.LT(1);
            	    match(input,LTGT,FOLLOW_LTGT_in_relationalExpression4206); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4212);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LTGT78)).Binary(JCTree.NE, expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:507:9: EQEQ e= additiveExpression
            	    {
            	    EQEQ79=(Token)input.LT(1);
            	    match(input,EQEQ,FOLLOW_EQEQ_in_relationalExpression4226); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4232);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(EQEQ79)).Binary(JCTree.EQ, expr, e); 

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:508:9: LTEQ e= additiveExpression
            	    {
            	    LTEQ80=(Token)input.LT(1);
            	    match(input,LTEQ,FOLLOW_LTEQ_in_relationalExpression4246); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4252);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LTEQ80)).Binary(JCTree.LE, expr, e); 

            	    }
            	    break;
            	case 4 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:509:9: GTEQ e= additiveExpression
            	    {
            	    GTEQ81=(Token)input.LT(1);
            	    match(input,GTEQ,FOLLOW_GTEQ_in_relationalExpression4266); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4272);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(GTEQ81)).Binary(JCTree.GE, expr, e); 

            	    }
            	    break;
            	case 5 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:510:9: LT e= additiveExpression
            	    {
            	    LT82=(Token)input.LT(1);
            	    match(input,LT,FOLLOW_LT_in_relationalExpression4286); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4294);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LT82))  .Binary(JCTree.LT, expr, e); 

            	    }
            	    break;
            	case 6 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:511:9: GT e= additiveExpression
            	    {
            	    GT83=(Token)input.LT(1);
            	    match(input,GT,FOLLOW_GT_in_relationalExpression4308); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4316);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(GT83))  .Binary(JCTree.GT, expr, e); 

            	    }
            	    break;
            	case 7 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:512:9: IN e= additiveExpression
            	    {
            	    IN84=(Token)input.LT(1);
            	    match(input,IN,FOLLOW_IN_in_relationalExpression4330); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4338);
            	    e=additiveExpression();
            	    _fsp--;

            	     /* expr = F.at(pos(IN84  )).Binary(JavaFXTag.IN, expr, $e2.expr); */ 

            	    }
            	    break;

            	default :
            	    break loop45;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:514:1: additiveExpression returns [JCExpression expr] : e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* ;
    public final JCExpression additiveExpression() throws RecognitionException {
        JCExpression expr = null;

        Token PLUS85=null;
        Token SUB86=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:515:2: (e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:515:4: e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            {
            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4367);
            e1=multiplicativeExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:516:5: ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            loop46:
            do {
                int alt46=3;
                int LA46_0 = input.LA(1);

                if ( (LA46_0==PLUS) ) {
                    alt46=1;
                }
                else if ( (LA46_0==SUB) ) {
                    alt46=2;
                }


                switch (alt46) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:516:9: PLUS e= multiplicativeExpression
            	    {
            	    PLUS85=(Token)input.LT(1);
            	    match(input,PLUS,FOLLOW_PLUS_in_additiveExpression4382); 
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4388);
            	    e=multiplicativeExpression();
            	    _fsp--;

            	     expr = F.at(pos(PLUS85)).Binary(JCTree.PLUS , expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:517:9: SUB e= multiplicativeExpression
            	    {
            	    SUB86=(Token)input.LT(1);
            	    match(input,SUB,FOLLOW_SUB_in_additiveExpression4401); 
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4408);
            	    e=multiplicativeExpression();
            	    _fsp--;

            	     expr = F.at(pos(SUB86)) .Binary(JCTree.MINUS, expr, e); 

            	    }
            	    break;

            	default :
            	    break loop46;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:519:1: multiplicativeExpression returns [JCExpression expr] : e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* ;
    public final JCExpression multiplicativeExpression() throws RecognitionException {
        JCExpression expr = null;

        Token STAR87=null;
        Token SLASH88=null;
        Token PERCENT89=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:520:2: (e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:520:4: e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            {
            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4436);
            e1=unaryExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:521:5: ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            loop47:
            do {
                int alt47=4;
                switch ( input.LA(1) ) {
                case STAR:
                    {
                    alt47=1;
                    }
                    break;
                case SLASH:
                    {
                    alt47=2;
                    }
                    break;
                case PERCENT:
                    {
                    alt47=3;
                    }
                    break;

                }

                switch (alt47) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:521:9: STAR e= unaryExpression
            	    {
            	    STAR87=(Token)input.LT(1);
            	    match(input,STAR,FOLLOW_STAR_in_multiplicativeExpression4452); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4459);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(STAR87))   .Binary(JCTree.MUL  , expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:522:9: SLASH e= unaryExpression
            	    {
            	    SLASH88=(Token)input.LT(1);
            	    match(input,SLASH,FOLLOW_SLASH_in_multiplicativeExpression4473); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4479);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(SLASH88))  .Binary(JCTree.DIV  , expr, e); 

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:523:9: PERCENT e= unaryExpression
            	    {
            	    PERCENT89=(Token)input.LT(1);
            	    match(input,PERCENT,FOLLOW_PERCENT_in_multiplicativeExpression4493); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4497);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(PERCENT89)).Binary(JCTree.MOD  , expr, e); 

            	    }
            	    break;

            	default :
            	    break loop47;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:525:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );
    public final JCExpression unaryExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression postfixExpression90 = null;

        int unaryOperator91 = 0;

        JCExpression postfixExpression92 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:526:2: ( postfixExpression | unaryOperator postfixExpression )
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( ((LA48_0>=THIS && LA48_0<=FALSE)||LA48_0==NEW||LA48_0==SUPER||LA48_0==LPAREN||LA48_0==LBRACKET||LA48_0==STRING_LITERAL||(LA48_0>=QUOTE_LBRACE_STRING_LITERAL && LA48_0<=LBRACE)||(LA48_0>=QUOTED_IDENTIFIER && LA48_0<=INTEGER_LITERAL)||LA48_0==FLOATING_POINT_LITERAL||LA48_0==IDENTIFIER) ) {
                alt48=1;
            }
            else if ( ((LA48_0>=POUND && LA48_0<=TYPEOF)||LA48_0==NOT||(LA48_0>=SIZEOF && LA48_0<=REVERSE)||(LA48_0>=PLUSPLUS && LA48_0<=SUBSUB)||LA48_0==QUES) ) {
                alt48=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("525:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );", 48, 0, input);

                throw nvae;
            }
            switch (alt48) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:526:4: postfixExpression
                    {
                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression4527);
                    postfixExpression90=postfixExpression();
                    _fsp--;

                     expr = postfixExpression90; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:527:4: unaryOperator postfixExpression
                    {
                    pushFollow(FOLLOW_unaryOperator_in_unaryExpression4538);
                    unaryOperator91=unaryOperator();
                    _fsp--;

                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression4542);
                    postfixExpression92=postfixExpression();
                    _fsp--;

                     expr = F.Unary(unaryOperator91, postfixExpression92); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:529:1: postfixExpression returns [JCExpression expr] : primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* ;
    public final JCExpression postfixExpression() throws RecognitionException {
        JCExpression expr = null;

        Token DOT94=null;
        Token LPAREN95=null;
        name_return name1 = null;

        JCExpression primaryExpression93 = null;

        ListBuffer<JCExpression> expressionListOpt96 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:530:2: ( primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:530:4: primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            {
            pushFollow(FOLLOW_primaryExpression_in_postfixExpression4562);
            primaryExpression93=primaryExpression();
            _fsp--;

             expr = primaryExpression93; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:531:5: ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            loop52:
            do {
                int alt52=3;
                int LA52_0 = input.LA(1);

                if ( (LA52_0==DOT) ) {
                    alt52=1;
                }
                else if ( (LA52_0==LBRACKET) ) {
                    alt52=2;
                }


                switch (alt52) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:531:7: DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    {
            	    DOT94=(Token)input.LT(1);
            	    match(input,DOT,FOLLOW_DOT_in_postfixExpression4577); 
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:531:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    int alt50=2;
            	    int LA50_0 = input.LA(1);

            	    if ( (LA50_0==CLASS) ) {
            	        alt50=1;
            	    }
            	    else if ( (LA50_0==QUOTED_IDENTIFIER||LA50_0==IDENTIFIER) ) {
            	        alt50=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("531:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )", 50, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt50) {
            	        case 1 :
            	            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:531:13: CLASS
            	            {
            	            match(input,CLASS,FOLLOW_CLASS_in_postfixExpression4581); 

            	            }
            	            break;
            	        case 2 :
            	            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:532:13: name1= name ( LPAREN expressionListOpt RPAREN )*
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression4605);
            	            name1=name();
            	            _fsp--;

            	             expr = F.at(pos(DOT94)).Select(expr, name1.value); 
            	            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:533:14: ( LPAREN expressionListOpt RPAREN )*
            	            loop49:
            	            do {
            	                int alt49=2;
            	                int LA49_0 = input.LA(1);

            	                if ( (LA49_0==LPAREN) ) {
            	                    alt49=1;
            	                }


            	                switch (alt49) {
            	            	case 1 :
            	            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:533:16: LPAREN expressionListOpt RPAREN
            	            	    {
            	            	    LPAREN95=(Token)input.LT(1);
            	            	    match(input,LPAREN,FOLLOW_LPAREN_in_postfixExpression4630); 
            	            	    pushFollow(FOLLOW_expressionListOpt_in_postfixExpression4632);
            	            	    expressionListOpt96=expressionListOpt();
            	            	    _fsp--;

            	            	    match(input,RPAREN,FOLLOW_RPAREN_in_postfixExpression4634); 
            	            	     expr = F.at(pos(LPAREN95)).Apply(null, expr, expressionListOpt96.toList()); 

            	            	    }
            	            	    break;

            	            	default :
            	            	    break loop49;
            	                }
            	            } while (true);


            	            }
            	            break;

            	    }


            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:535:7: LBRACKET ( name BAR )? expression RBRACKET
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_postfixExpression4666); 
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:535:16: ( name BAR )?
            	    int alt51=2;
            	    int LA51_0 = input.LA(1);

            	    if ( (LA51_0==QUOTED_IDENTIFIER||LA51_0==IDENTIFIER) ) {
            	        int LA51_1 = input.LA(2);

            	        if ( (LA51_1==BAR) ) {
            	            alt51=1;
            	        }
            	    }
            	    switch (alt51) {
            	        case 1 :
            	            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:535:17: name BAR
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression4669);
            	            name();
            	            _fsp--;

            	            match(input,BAR,FOLLOW_BAR_in_postfixExpression4671); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_expression_in_postfixExpression4675);
            	    expression();
            	    _fsp--;

            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_postfixExpression4678); 

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
        return expr;
    }
    // $ANTLR end postfixExpression


    // $ANTLR start primaryExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:537:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | bracketExpression | literal | blockExpression | LPAREN expression RPAREN );
    public final JCExpression primaryExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LBRACE98=null;
        Token THIS101=null;
        Token SUPER102=null;
        Token LPAREN104=null;
        Token LPAREN110=null;
        JCExpression newExpression97 = null;

        JCIdent identifier99 = null;

        ListBuffer<JFXStatement> objectLiteral100 = null;

        JCIdent identifier103 = null;

        ListBuffer<JCExpression> expressionListOpt105 = null;

        JCExpression stringExpression106 = null;

        JFXAbstractSequenceCreator bracketExpression107 = null;

        JCExpression literal108 = null;

        JFXBlockExpression blockExpression109 = null;

        JCExpression expression111 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:538:2: ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | bracketExpression | literal | blockExpression | LPAREN expression RPAREN )
            int alt54=10;
            switch ( input.LA(1) ) {
            case NEW:
                {
                alt54=1;
                }
                break;
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                int LA54_2 = input.LA(2);

                if ( ((LA54_2>=DOTDOT && LA54_2<=ABSTRACT)||LA54_2==AND||LA54_2==ATTRIBUTE||LA54_2==ON||(LA54_2>=THEN && LA54_2<=ELSE)||LA54_2==IN||(LA54_2>=PRIVATE && LA54_2<=READONLY)||LA54_2==INSTANCEOF||LA54_2==OR||(LA54_2>=LPAREN && LA54_2<=PERCENTEQ)||(LA54_2>=RBRACE_QUOTE_STRING_LITERAL && LA54_2<=RBRACE)||LA54_2==QUOTED_IDENTIFIER||LA54_2==IDENTIFIER) ) {
                    alt54=5;
                }
                else if ( (LA54_2==LBRACE) ) {
                    alt54=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("537:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | bracketExpression | literal | blockExpression | LPAREN expression RPAREN );", 54, 2, input);

                    throw nvae;
                }
                }
                break;
            case THIS:
                {
                alt54=3;
                }
                break;
            case SUPER:
                {
                alt54=4;
                }
                break;
            case QUOTE_LBRACE_STRING_LITERAL:
                {
                alt54=6;
                }
                break;
            case LBRACKET:
                {
                alt54=7;
                }
                break;
            case NULL:
            case TRUE:
            case FALSE:
            case STRING_LITERAL:
            case INTEGER_LITERAL:
            case FLOATING_POINT_LITERAL:
                {
                alt54=8;
                }
                break;
            case LBRACE:
                {
                alt54=9;
                }
                break;
            case LPAREN:
                {
                alt54=10;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("537:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | bracketExpression | literal | blockExpression | LPAREN expression RPAREN );", 54, 0, input);

                throw nvae;
            }

            switch (alt54) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:538:4: newExpression
                    {
                    pushFollow(FOLLOW_newExpression_in_primaryExpression4703);
                    newExpression97=newExpression();
                    _fsp--;

                     expr = newExpression97; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:539:4: identifier LBRACE objectLiteral RBRACE
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression4715);
                    identifier99=identifier();
                    _fsp--;

                    LBRACE98=(Token)input.LT(1);
                    match(input,LBRACE,FOLLOW_LBRACE_in_primaryExpression4717); 
                    pushFollow(FOLLOW_objectLiteral_in_primaryExpression4720);
                    objectLiteral100=objectLiteral();
                    _fsp--;

                    match(input,RBRACE,FOLLOW_RBRACE_in_primaryExpression4722); 
                     expr = F.at(pos(LBRACE98)).PureObjectLiteral(identifier99, objectLiteral100.toList()); 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:540:11: THIS
                    {
                    THIS101=(Token)input.LT(1);
                    match(input,THIS,FOLLOW_THIS_in_primaryExpression4739); 
                     expr = F.at(pos(THIS101)).Identifier(names._this); 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:541:11: SUPER
                    {
                    SUPER102=(Token)input.LT(1);
                    match(input,SUPER,FOLLOW_SUPER_in_primaryExpression4759); 
                     expr = F.at(pos(SUPER102)).Identifier(names._super); 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:542:11: identifier ( LPAREN expressionListOpt RPAREN )*
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression4779);
                    identifier103=identifier();
                    _fsp--;

                     expr = identifier103; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:543:10: ( LPAREN expressionListOpt RPAREN )*
                    loop53:
                    do {
                        int alt53=2;
                        int LA53_0 = input.LA(1);

                        if ( (LA53_0==LPAREN) ) {
                            alt53=1;
                        }


                        switch (alt53) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:543:12: LPAREN expressionListOpt RPAREN
                    	    {
                    	    LPAREN104=(Token)input.LT(1);
                    	    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression4800); 
                    	    pushFollow(FOLLOW_expressionListOpt_in_primaryExpression4804);
                    	    expressionListOpt105=expressionListOpt();
                    	    _fsp--;

                    	    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression4808); 
                    	     expr = F.at(pos(LPAREN104)).Apply(null, expr, expressionListOpt105.toList()); 

                    	    }
                    	    break;

                    	default :
                    	    break loop53;
                        }
                    } while (true);


                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:544:11: stringExpression
                    {
                    pushFollow(FOLLOW_stringExpression_in_primaryExpression4828);
                    stringExpression106=stringExpression();
                    _fsp--;

                     expr = stringExpression106; 

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:545:11: bracketExpression
                    {
                    pushFollow(FOLLOW_bracketExpression_in_primaryExpression4847);
                    bracketExpression107=bracketExpression();
                    _fsp--;

                     expr = bracketExpression107; 

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:546:11: literal
                    {
                    pushFollow(FOLLOW_literal_in_primaryExpression4866);
                    literal108=literal();
                    _fsp--;

                     expr = literal108; 

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:547:11: blockExpression
                    {
                    pushFollow(FOLLOW_blockExpression_in_primaryExpression4886);
                    blockExpression109=blockExpression();
                    _fsp--;

                     expr = blockExpression109; 

                    }
                    break;
                case 10 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:548:11: LPAREN expression RPAREN
                    {
                    LPAREN110=(Token)input.LT(1);
                    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression4904); 
                    pushFollow(FOLLOW_expression_in_primaryExpression4906);
                    expression111=expression();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression4908); 
                     expr = F.at(pos(LPAREN110)).Parens(expression111); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:550:1: newExpression returns [JCExpression expr] : NEW identifier ( LPAREN expressionListOpt RPAREN )? ;
    public final JCExpression newExpression() throws RecognitionException {
        JCExpression expr = null;

        Token NEW113=null;
        ListBuffer<JCExpression> expressionListOpt112 = null;

        JCIdent identifier114 = null;


         ListBuffer<JCExpression> args = null; 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:552:2: ( NEW identifier ( LPAREN expressionListOpt RPAREN )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:552:4: NEW identifier ( LPAREN expressionListOpt RPAREN )?
            {
            NEW113=(Token)input.LT(1);
            match(input,NEW,FOLLOW_NEW_in_newExpression4941); 
            pushFollow(FOLLOW_identifier_in_newExpression4944);
            identifier114=identifier();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:553:3: ( LPAREN expressionListOpt RPAREN )?
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==LPAREN) ) {
                alt55=1;
            }
            switch (alt55) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:553:5: LPAREN expressionListOpt RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_newExpression4952); 
                    pushFollow(FOLLOW_expressionListOpt_in_newExpression4956);
                    expressionListOpt112=expressionListOpt();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_newExpression4960); 
                     args = expressionListOpt112; 

                    }
                    break;

            }

             expr = F.at(pos(NEW113)).NewClass(null, null, identifier114, 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:558:1: objectLiteral returns [ListBuffer<JFXStatement> parts = new ListBuffer<JFXStatement>()] : ( objectLiteralPart )* ;
    public final ListBuffer<JFXStatement> objectLiteral() throws RecognitionException {
        ListBuffer<JFXStatement> parts =  new ListBuffer<JFXStatement>();

        JFXStatement objectLiteralPart115 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:559:2: ( ( objectLiteralPart )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:559:4: ( objectLiteralPart )*
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:559:4: ( objectLiteralPart )*
            loop56:
            do {
                int alt56=2;
                int LA56_0 = input.LA(1);

                if ( (LA56_0==ABSTRACT||LA56_0==ATTRIBUTE||(LA56_0>=PRIVATE && LA56_0<=READONLY)||LA56_0==QUOTED_IDENTIFIER||LA56_0==IDENTIFIER) ) {
                    alt56=1;
                }


                switch (alt56) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:559:6: objectLiteralPart
            	    {
            	    pushFollow(FOLLOW_objectLiteralPart_in_objectLiteral5000);
            	    objectLiteralPart115=objectLiteralPart();
            	    _fsp--;

            	     parts.append(objectLiteralPart115); 

            	    }
            	    break;

            	default :
            	    break loop56;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );
    public final JFXStatement objectLiteralPart() throws RecognitionException {
        JFXStatement value = null;

        Token COLON116=null;
        name_return name117 = null;

        JCExpression expression118 = null;

        JavafxBindStatus bindOpt119 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:562:2: ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition )
            int alt58=3;
            switch ( input.LA(1) ) {
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                alt58=1;
                }
                break;
            case ABSTRACT:
                {
                switch ( input.LA(2) ) {
                case PUBLIC:
                    {
                    int LA58_9 = input.LA(3);

                    if ( (LA58_9==FUNCTION) ) {
                        alt58=3;
                    }
                    else if ( (LA58_9==ATTRIBUTE) ) {
                        alt58=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 9, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA58_10 = input.LA(3);

                    if ( (LA58_10==ATTRIBUTE) ) {
                        alt58=2;
                    }
                    else if ( (LA58_10==FUNCTION) ) {
                        alt58=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA58_11 = input.LA(3);

                    if ( (LA58_11==FUNCTION) ) {
                        alt58=3;
                    }
                    else if ( (LA58_11==ATTRIBUTE) ) {
                        alt58=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case FUNCTION:
                    {
                    alt58=3;
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt58=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 2, input);

                    throw nvae;
                }

                }
                break;
            case READONLY:
                {
                switch ( input.LA(2) ) {
                case PUBLIC:
                    {
                    int LA58_9 = input.LA(3);

                    if ( (LA58_9==FUNCTION) ) {
                        alt58=3;
                    }
                    else if ( (LA58_9==ATTRIBUTE) ) {
                        alt58=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 9, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA58_10 = input.LA(3);

                    if ( (LA58_10==ATTRIBUTE) ) {
                        alt58=2;
                    }
                    else if ( (LA58_10==FUNCTION) ) {
                        alt58=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA58_11 = input.LA(3);

                    if ( (LA58_11==FUNCTION) ) {
                        alt58=3;
                    }
                    else if ( (LA58_11==ATTRIBUTE) ) {
                        alt58=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt58=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt58=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 3, input);

                    throw nvae;
                }

                }
                break;
            case PUBLIC:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA58_12 = input.LA(3);

                    if ( (LA58_12==ATTRIBUTE) ) {
                        alt58=2;
                    }
                    else if ( (LA58_12==FUNCTION) ) {
                        alt58=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA58_13 = input.LA(3);

                    if ( (LA58_13==ATTRIBUTE) ) {
                        alt58=2;
                    }
                    else if ( (LA58_13==FUNCTION) ) {
                        alt58=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case FUNCTION:
                    {
                    alt58=3;
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt58=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 4, input);

                    throw nvae;
                }

                }
                break;
            case PRIVATE:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA58_12 = input.LA(3);

                    if ( (LA58_12==ATTRIBUTE) ) {
                        alt58=2;
                    }
                    else if ( (LA58_12==FUNCTION) ) {
                        alt58=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA58_13 = input.LA(3);

                    if ( (LA58_13==ATTRIBUTE) ) {
                        alt58=2;
                    }
                    else if ( (LA58_13==FUNCTION) ) {
                        alt58=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case FUNCTION:
                    {
                    alt58=3;
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt58=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 5, input);

                    throw nvae;
                }

                }
                break;
            case PROTECTED:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA58_12 = input.LA(3);

                    if ( (LA58_12==ATTRIBUTE) ) {
                        alt58=2;
                    }
                    else if ( (LA58_12==FUNCTION) ) {
                        alt58=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA58_13 = input.LA(3);

                    if ( (LA58_13==ATTRIBUTE) ) {
                        alt58=2;
                    }
                    else if ( (LA58_13==FUNCTION) ) {
                        alt58=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case FUNCTION:
                    {
                    alt58=3;
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt58=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 6, input);

                    throw nvae;
                }

                }
                break;
            case ATTRIBUTE:
                {
                alt58=2;
                }
                break;
            case FUNCTION:
                {
                alt58=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("561:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 58, 0, input);

                throw nvae;
            }

            switch (alt58) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:562:4: name COLON bindOpt expression ( COMMA | SEMI )?
                    {
                    pushFollow(FOLLOW_name_in_objectLiteralPart5028);
                    name117=name();
                    _fsp--;

                    COLON116=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_objectLiteralPart5030); 
                    pushFollow(FOLLOW_bindOpt_in_objectLiteralPart5033);
                    bindOpt119=bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_objectLiteralPart5035);
                    expression118=expression();
                    _fsp--;

                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:562:35: ( COMMA | SEMI )?
                    int alt57=2;
                    int LA57_0 = input.LA(1);

                    if ( ((LA57_0>=SEMI && LA57_0<=COMMA)) ) {
                        alt57=1;
                    }
                    switch (alt57) {
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
                                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_objectLiteralPart5037);    throw mse;
                            }


                            }
                            break;

                    }

                     value = F.at(pos(COLON116)).ObjectLiteralPart(name117.value, expression118, bindOpt119); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:563:11: attributeDefinition
                    {
                    pushFollow(FOLLOW_attributeDefinition_in_objectLiteralPart5058);
                    attributeDefinition();
                    _fsp--;


                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:564:11: functionDefinition
                    {
                    pushFollow(FOLLOW_functionDefinition_in_objectLiteralPart5070);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:566:1: stringExpression returns [JCExpression expr] : ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL ;
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
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:568:2: (ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:568:4: ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL
            {
            ql=(Token)input.LT(1);
            match(input,QUOTE_LBRACE_STRING_LITERAL,FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression5101); 
             strexp.append(F.at(pos(ql)).Literal(TypeTags.CLASS, ql.getText())); 
            pushFollow(FOLLOW_formatOrNull_in_stringExpression5110);
            f1=formatOrNull();
            _fsp--;

             strexp.append(f1); 
            pushFollow(FOLLOW_expression_in_stringExpression5121);
            e1=expression();
            _fsp--;

             strexp.append(e1); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:571:4: (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( (LA59_0==RBRACE_LBRACE_STRING_LITERAL) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:571:7: rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression
            	    {
            	    rl=(Token)input.LT(1);
            	    match(input,RBRACE_LBRACE_STRING_LITERAL,FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression5136); 
            	     strexp.append(F.at(pos(rl)).Literal(TypeTags.CLASS, rl.getText())); 
            	    pushFollow(FOLLOW_formatOrNull_in_stringExpression5148);
            	    fn=formatOrNull();
            	    _fsp--;

            	     strexp.append(fn); 
            	    pushFollow(FOLLOW_expression_in_stringExpression5162);
            	    en=expression();
            	    _fsp--;

            	     strexp.append(en); 

            	    }
            	    break;

            	default :
            	    break loop59;
                }
            } while (true);

            rq=(Token)input.LT(1);
            match(input,RBRACE_QUOTE_STRING_LITERAL,FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression5183); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:578:1: formatOrNull returns [JCExpression expr] : (fs= FORMAT_STRING_LITERAL | );
    public final JCExpression formatOrNull() throws RecognitionException {
        JCExpression expr = null;

        Token fs=null;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:579:2: (fs= FORMAT_STRING_LITERAL | )
            int alt60=2;
            int LA60_0 = input.LA(1);

            if ( (LA60_0==FORMAT_STRING_LITERAL) ) {
                alt60=1;
            }
            else if ( ((LA60_0>=POUND && LA60_0<=TYPEOF)||LA60_0==IF||(LA60_0>=THIS && LA60_0<=FALSE)||(LA60_0>=NOT && LA60_0<=NEW)||LA60_0==SUPER||(LA60_0>=SIZEOF && LA60_0<=LPAREN)||LA60_0==LBRACKET||(LA60_0>=PLUSPLUS && LA60_0<=SUBSUB)||(LA60_0>=QUES && LA60_0<=STRING_LITERAL)||(LA60_0>=QUOTE_LBRACE_STRING_LITERAL && LA60_0<=LBRACE)||(LA60_0>=QUOTED_IDENTIFIER && LA60_0<=INTEGER_LITERAL)||LA60_0==FLOATING_POINT_LITERAL||LA60_0==IDENTIFIER) ) {
                alt60=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("578:1: formatOrNull returns [JCExpression expr] : (fs= FORMAT_STRING_LITERAL | );", 60, 0, input);

                throw nvae;
            }
            switch (alt60) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:579:4: fs= FORMAT_STRING_LITERAL
                    {
                    fs=(Token)input.LT(1);
                    match(input,FORMAT_STRING_LITERAL,FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull5213); 
                     expr = F.at(pos(fs)).Literal(TypeTags.CLASS, fs.getText()); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:580:22: 
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


    // $ANTLR start bracketExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:582:1: bracketExpression returns [JFXAbstractSequenceCreator expr] : LBRACKET ( | e1= expression ( | COMMA e2= expression ( ( COMMA en= expression )* ) | DOTDOT dd= expression ) ) RBRACKET ;
    public final JFXAbstractSequenceCreator bracketExpression() throws RecognitionException {
        JFXAbstractSequenceCreator expr = null;

        Token LBRACKET120=null;
        JCExpression e1 = null;

        JCExpression e2 = null;

        JCExpression en = null;

        JCExpression dd = null;


         ListBuffer<JCExpression> exps = new ListBuffer<JCExpression>(); 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:584:2: ( LBRACKET ( | e1= expression ( | COMMA e2= expression ( ( COMMA en= expression )* ) | DOTDOT dd= expression ) ) RBRACKET )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:584:4: LBRACKET ( | e1= expression ( | COMMA e2= expression ( ( COMMA en= expression )* ) | DOTDOT dd= expression ) ) RBRACKET
            {
            LBRACKET120=(Token)input.LT(1);
            match(input,LBRACKET,FOLLOW_LBRACKET_in_bracketExpression5246); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:585:6: ( | e1= expression ( | COMMA e2= expression ( ( COMMA en= expression )* ) | DOTDOT dd= expression ) )
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==RBRACKET) ) {
                alt63=1;
            }
            else if ( ((LA63_0>=POUND && LA63_0<=TYPEOF)||LA63_0==IF||(LA63_0>=THIS && LA63_0<=FALSE)||(LA63_0>=NOT && LA63_0<=NEW)||LA63_0==SUPER||(LA63_0>=SIZEOF && LA63_0<=LPAREN)||LA63_0==LBRACKET||(LA63_0>=PLUSPLUS && LA63_0<=SUBSUB)||(LA63_0>=QUES && LA63_0<=STRING_LITERAL)||(LA63_0>=QUOTE_LBRACE_STRING_LITERAL && LA63_0<=LBRACE)||(LA63_0>=QUOTED_IDENTIFIER && LA63_0<=INTEGER_LITERAL)||LA63_0==FLOATING_POINT_LITERAL||LA63_0==IDENTIFIER) ) {
                alt63=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("585:6: ( | e1= expression ( | COMMA e2= expression ( ( COMMA en= expression )* ) | DOTDOT dd= expression ) )", 63, 0, input);

                throw nvae;
            }
            switch (alt63) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:585:20: 
                    {
                     expr = F.at(pos(LBRACKET120)).EmptySequence(); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:586:8: e1= expression ( | COMMA e2= expression ( ( COMMA en= expression )* ) | DOTDOT dd= expression )
                    {
                    pushFollow(FOLLOW_expression_in_bracketExpression5274);
                    e1=expression();
                    _fsp--;

                     exps.append(e1); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:587:8: ( | COMMA e2= expression ( ( COMMA en= expression )* ) | DOTDOT dd= expression )
                    int alt62=3;
                    switch ( input.LA(1) ) {
                    case RBRACKET:
                        {
                        alt62=1;
                        }
                        break;
                    case COMMA:
                        {
                        alt62=2;
                        }
                        break;
                    case DOTDOT:
                        {
                        alt62=3;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("587:8: ( | COMMA e2= expression ( ( COMMA en= expression )* ) | DOTDOT dd= expression )", 62, 0, input);

                        throw nvae;
                    }

                    switch (alt62) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:587:23: 
                            {
                             expr = F.at(pos(LBRACKET120)).ExplicitSequence(exps.toList()); 

                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:588:10: COMMA e2= expression ( ( COMMA en= expression )* )
                            {
                            match(input,COMMA,FOLLOW_COMMA_in_bracketExpression5307); 
                            pushFollow(FOLLOW_expression_in_bracketExpression5311);
                            e2=expression();
                            _fsp--;

                             exps.append(e2); 
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:589:12: ( ( COMMA en= expression )* )
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:592:14: ( COMMA en= expression )*
                            {
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:592:14: ( COMMA en= expression )*
                            loop61:
                            do {
                                int alt61=2;
                                int LA61_0 = input.LA(1);

                                if ( (LA61_0==COMMA) ) {
                                    alt61=1;
                                }


                                switch (alt61) {
                            	case 1 :
                            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:592:15: COMMA en= expression
                            	    {
                            	    match(input,COMMA,FOLLOW_COMMA_in_bracketExpression5346); 
                            	    pushFollow(FOLLOW_expression_in_bracketExpression5351);
                            	    en=expression();
                            	    _fsp--;

                            	     exps.append(en); 

                            	    }
                            	    break;

                            	default :
                            	    break loop61;
                                }
                            } while (true);

                             expr = F.at(pos(LBRACKET120)).ExplicitSequence(exps.toList()); 

                            }


                            }
                            break;
                        case 3 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:595:10: DOTDOT dd= expression
                            {
                            match(input,DOTDOT,FOLLOW_DOTDOT_in_bracketExpression5397); 
                            pushFollow(FOLLOW_expression_in_bracketExpression5403);
                            dd=expression();
                            _fsp--;

                             expr = F.at(pos(LBRACKET120)).RangeSequence(e1, dd); 

                            }
                            break;

                    }


                    }
                    break;

            }

            match(input,RBRACKET,FOLLOW_RBRACKET_in_bracketExpression5429); 

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
    // $ANTLR end bracketExpression


    // $ANTLR start expressionListOpt
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:600:1: expressionListOpt returns [ListBuffer<JCExpression> args = new ListBuffer<JCExpression>()] : (e1= expression ( COMMA e= expression )* )? ;
    public final ListBuffer<JCExpression> expressionListOpt() throws RecognitionException {
        ListBuffer<JCExpression> args =  new ListBuffer<JCExpression>();

        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:601:2: ( (e1= expression ( COMMA e= expression )* )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:601:4: (e1= expression ( COMMA e= expression )* )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:601:4: (e1= expression ( COMMA e= expression )* )?
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( ((LA65_0>=POUND && LA65_0<=TYPEOF)||LA65_0==IF||(LA65_0>=THIS && LA65_0<=FALSE)||(LA65_0>=NOT && LA65_0<=NEW)||LA65_0==SUPER||(LA65_0>=SIZEOF && LA65_0<=LPAREN)||LA65_0==LBRACKET||(LA65_0>=PLUSPLUS && LA65_0<=SUBSUB)||(LA65_0>=QUES && LA65_0<=STRING_LITERAL)||(LA65_0>=QUOTE_LBRACE_STRING_LITERAL && LA65_0<=LBRACE)||(LA65_0>=QUOTED_IDENTIFIER && LA65_0<=INTEGER_LITERAL)||LA65_0==FLOATING_POINT_LITERAL||LA65_0==IDENTIFIER) ) {
                alt65=1;
            }
            switch (alt65) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:601:6: e1= expression ( COMMA e= expression )*
                    {
                    pushFollow(FOLLOW_expression_in_expressionListOpt5450);
                    e1=expression();
                    _fsp--;

                     args.append(e1); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:602:6: ( COMMA e= expression )*
                    loop64:
                    do {
                        int alt64=2;
                        int LA64_0 = input.LA(1);

                        if ( (LA64_0==COMMA) ) {
                            alt64=1;
                        }


                        switch (alt64) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:602:7: COMMA e= expression
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_expressionListOpt5461); 
                    	    pushFollow(FOLLOW_expression_in_expressionListOpt5467);
                    	    e=expression();
                    	    _fsp--;

                    	     args.append(e); 

                    	    }
                    	    break;

                    	default :
                    	    break loop64;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:603:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );
    public final int unaryOperator() throws RecognitionException {
        int optag = 0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:604:2: ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB )
            int alt66=9;
            switch ( input.LA(1) ) {
            case POUND:
                {
                alt66=1;
                }
                break;
            case QUES:
                {
                alt66=2;
                }
                break;
            case SUB:
                {
                alt66=3;
                }
                break;
            case NOT:
                {
                alt66=4;
                }
                break;
            case SIZEOF:
                {
                alt66=5;
                }
                break;
            case TYPEOF:
                {
                alt66=6;
                }
                break;
            case REVERSE:
                {
                alt66=7;
                }
                break;
            case PLUSPLUS:
                {
                alt66=8;
                }
                break;
            case SUBSUB:
                {
                alt66=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("603:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );", 66, 0, input);

                throw nvae;
            }

            switch (alt66) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:604:4: POUND
                    {
                    match(input,POUND,FOLLOW_POUND_in_unaryOperator5490); 
                     optag = 0; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:605:4: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_unaryOperator5501); 
                     optag = 0; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:606:4: SUB
                    {
                    match(input,SUB,FOLLOW_SUB_in_unaryOperator5514); 
                     optag = JCTree.NEG; 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:607:4: NOT
                    {
                    match(input,NOT,FOLLOW_NOT_in_unaryOperator5527); 
                     optag = JCTree.NOT; 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:608:4: SIZEOF
                    {
                    match(input,SIZEOF,FOLLOW_SIZEOF_in_unaryOperator5540); 
                     optag = 0; 

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:609:4: TYPEOF
                    {
                    match(input,TYPEOF,FOLLOW_TYPEOF_in_unaryOperator5553); 
                     optag = 0; 

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:610:4: REVERSE
                    {
                    match(input,REVERSE,FOLLOW_REVERSE_in_unaryOperator5566); 
                     optag = 0; 

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:611:4: PLUSPLUS
                    {
                    match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_unaryOperator5579); 
                     optag = 0; 

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:612:4: SUBSUB
                    {
                    match(input,SUBSUB,FOLLOW_SUBSUB_in_unaryOperator5592); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:614:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );
    public final int assignmentOperator() throws RecognitionException {
        int optag = 0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:615:2: ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ )
            int alt67=5;
            switch ( input.LA(1) ) {
            case PLUSEQ:
                {
                alt67=1;
                }
                break;
            case SUBEQ:
                {
                alt67=2;
                }
                break;
            case STAREQ:
                {
                alt67=3;
                }
                break;
            case SLASHEQ:
                {
                alt67=4;
                }
                break;
            case PERCENTEQ:
                {
                alt67=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("614:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );", 67, 0, input);

                throw nvae;
            }

            switch (alt67) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:615:4: PLUSEQ
                    {
                    match(input,PLUSEQ,FOLLOW_PLUSEQ_in_assignmentOperator5613); 
                     optag = JCTree.PLUS_ASG; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:616:4: SUBEQ
                    {
                    match(input,SUBEQ,FOLLOW_SUBEQ_in_assignmentOperator5626); 
                     optag = JCTree.MINUS_ASG; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:617:4: STAREQ
                    {
                    match(input,STAREQ,FOLLOW_STAREQ_in_assignmentOperator5639); 
                     optag = JCTree.MUL_ASG; 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:618:4: SLASHEQ
                    {
                    match(input,SLASHEQ,FOLLOW_SLASHEQ_in_assignmentOperator5652); 
                     optag = JCTree.DIV_ASG; 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:619:4: PERCENTEQ
                    {
                    match(input,PERCENTEQ,FOLLOW_PERCENTEQ_in_assignmentOperator5665); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:621:1: typeReference returns [JFXType type] : ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? ;
    public final JFXType typeReference() throws RecognitionException {
        JFXType type = null;

        Token STAR122=null;
        int ccn = 0;

        int ccs = 0;

        name_return name121 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:622:2: ( ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:622:4: ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:622:4: ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            int alt69=2;
            int LA69_0 = input.LA(1);

            if ( (LA69_0==COLON) ) {
                alt69=1;
            }
            switch (alt69) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:622:6: COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    {
                    match(input,COLON,FOLLOW_COLON_in_typeReference5689); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:622:13: ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    int alt68=2;
                    int LA68_0 = input.LA(1);

                    if ( (LA68_0==QUOTED_IDENTIFIER||LA68_0==IDENTIFIER) ) {
                        alt68=1;
                    }
                    else if ( (LA68_0==STAR) ) {
                        alt68=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("622:13: ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )", 68, 0, input);

                        throw nvae;
                    }
                    switch (alt68) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:622:15: name ccn= cardinalityConstraint
                            {
                            pushFollow(FOLLOW_name_in_typeReference5694);
                            name121=name();
                            _fsp--;

                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference5698);
                            ccn=cardinalityConstraint();
                            _fsp--;

                             type = F.TypeClass(name121.value, ccn); 

                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:623:22: STAR ccs= cardinalityConstraint
                            {
                            STAR122=(Token)input.LT(1);
                            match(input,STAR,FOLLOW_STAR_in_typeReference5724); 
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference5728);
                            ccs=cardinalityConstraint();
                            _fsp--;

                             type = F.at(pos(STAR122)).TypeAny(ccs); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:627:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | );
    public final int cardinalityConstraint() throws RecognitionException {
        int ary = 0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:628:2: ( LBRACKET RBRACKET | )
            int alt70=2;
            int LA70_0 = input.LA(1);

            if ( (LA70_0==LBRACKET) ) {
                alt70=1;
            }
            else if ( (LA70_0==ON||LA70_0==INVERSE||LA70_0==RPAREN||(LA70_0>=SEMI && LA70_0<=COMMA)||LA70_0==EQ||LA70_0==LBRACE) ) {
                alt70=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("627:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | );", 70, 0, input);

                throw nvae;
            }
            switch (alt70) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:628:5: LBRACKET RBRACKET
                    {
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_cardinalityConstraint5790); 
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_cardinalityConstraint5794); 
                     ary = JFXType.CARDINALITY_ANY; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:629:29: 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:631:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );
    public final JCExpression literal() throws RecognitionException {
        JCExpression expr = null;

        Token t=null;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:632:2: (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL )
            int alt71=6;
            switch ( input.LA(1) ) {
            case STRING_LITERAL:
                {
                alt71=1;
                }
                break;
            case INTEGER_LITERAL:
                {
                alt71=2;
                }
                break;
            case FLOATING_POINT_LITERAL:
                {
                alt71=3;
                }
                break;
            case TRUE:
                {
                alt71=4;
                }
                break;
            case FALSE:
                {
                alt71=5;
                }
                break;
            case NULL:
                {
                alt71=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("631:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );", 71, 0, input);

                throw nvae;
            }

            switch (alt71) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:632:4: t= STRING_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_literal5848); 
                     expr = F.at(pos(t)).Literal(TypeTags.CLASS, t.getText()); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:633:4: t= INTEGER_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,INTEGER_LITERAL,FOLLOW_INTEGER_LITERAL_in_literal5858); 
                     expr = F.at(pos(t)).Literal(TypeTags.INT, Convert.string2int(t.getText(), 10)); 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:634:4: t= FLOATING_POINT_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,FLOATING_POINT_LITERAL,FOLLOW_FLOATING_POINT_LITERAL_in_literal5868); 
                     expr = F.at(pos(t)).Literal(TypeTags.DOUBLE, Double.valueOf(t.getText())); 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:635:4: t= TRUE
                    {
                    t=(Token)input.LT(1);
                    match(input,TRUE,FOLLOW_TRUE_in_literal5878); 
                     expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 1); 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:636:4: t= FALSE
                    {
                    t=(Token)input.LT(1);
                    match(input,FALSE,FOLLOW_FALSE_in_literal5892); 
                     expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 0); 

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:637:4: t= NULL
                    {
                    t=(Token)input.LT(1);
                    match(input,NULL,FOLLOW_NULL_in_literal5906); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:639:1: typeName returns [JCExpression expr] : qualident ;
    public final JCExpression typeName() throws RecognitionException {
        JCExpression expr = null;

        JCExpression qualident123 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:640:8: ( qualident )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:640:10: qualident
            {
            pushFollow(FOLLOW_qualident_in_typeName5933);
            qualident123=qualident();
            _fsp--;

             expr = qualident123; 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:642:1: qualident returns [JCExpression expr] : identifier ( DOT name )* ;
    public final JCExpression qualident() throws RecognitionException {
        JCExpression expr = null;

        JCIdent identifier124 = null;

        name_return name125 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:643:8: ( identifier ( DOT name )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:643:10: identifier ( DOT name )*
            {
            pushFollow(FOLLOW_identifier_in_qualident5975);
            identifier124=identifier();
            _fsp--;

             expr = identifier124; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:644:10: ( DOT name )*
            loop72:
            do {
                int alt72=2;
                int LA72_0 = input.LA(1);

                if ( (LA72_0==DOT) ) {
                    alt72=1;
                }


                switch (alt72) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:644:12: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_qualident6003); 
            	    pushFollow(FOLLOW_name_in_qualident6005);
            	    name125=name();
            	    _fsp--;

            	     expr = F.at(name125.pos).Select(expr, name125.value); 

            	    }
            	    break;

            	default :
            	    break loop72;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:646:1: identifier returns [JCIdent expr] : name ;
    public final JCIdent identifier() throws RecognitionException {
        JCIdent expr = null;

        name_return name126 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:647:2: ( name )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:647:4: name
            {
            pushFollow(FOLLOW_name_in_identifier6042);
            name126=name();
            _fsp--;

             expr = F.at(name126.pos).Ident(name126.value); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:649:1: name returns [Name value, int pos] : tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) ;
    public final name_return name() throws RecognitionException {
        name_return retval = new name_return();
        retval.start = input.LT(1);

        Token tokid=null;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:650:2: (tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:650:4: tokid= ( QUOTED_IDENTIFIER | IDENTIFIER )
            {
            tokid=(Token)input.LT(1);
            if ( input.LA(1)==QUOTED_IDENTIFIER||input.LA(1)==IDENTIFIER ) {
                input.consume();
                errorRecovery=false;
            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_name6076);    throw mse;
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


 

    public static final BitSet FOLLOW_packageDecl_in_module2000 = new BitSet(new long[]{0xBE267902E0110160L,0x00961B007002BA00L});
    public static final BitSet FOLLOW_moduleItems_in_module2003 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_module2005 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PACKAGE_in_packageDecl2027 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_qualident_in_packageDecl2029 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_SEMI_in_packageDecl2031 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_moduleItem_in_moduleItems2062 = new BitSet(new long[]{0xBE267902E0110162L,0x00961B007002BA00L});
    public static final BitSet FOLLOW_importDecl_in_moduleItem2106 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDefinition_in_moduleItem2122 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_moduleItem2138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_moduleItem2160 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_SEMI_in_moduleItem2162 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importDecl2199 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_identifier_in_importDecl2202 = new BitSet(new long[]{0x0000000000000000L,0x00000000000A0000L});
    public static final BitSet FOLLOW_DOT_in_importDecl2227 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_name_in_importDecl2229 = new BitSet(new long[]{0x0000000000000000L,0x00000000000A0000L});
    public static final BitSet FOLLOW_DOT_in_importDecl2258 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_STAR_in_importDecl2260 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_SEMI_in_importDecl2269 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_classDefinition2300 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_CLASS_in_classDefinition2303 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_name_in_classDefinition2305 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000004L});
    public static final BitSet FOLLOW_supers_in_classDefinition2307 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_LBRACE_in_classDefinition2309 = new BitSet(new long[]{0xB800000000002100L,0x0000800000000020L});
    public static final BitSet FOLLOW_classMembers_in_classDefinition2311 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RBRACE_in_classDefinition2313 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXTENDS_in_supers2338 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_name_in_supers2342 = new BitSet(new long[]{0x0000000000000002L,0x0000000000040000L});
    public static final BitSet FOLLOW_COMMA_in_supers2370 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_name_in_supers2374 = new BitSet(new long[]{0x0000000000000002L,0x0000000000040000L});
    public static final BitSet FOLLOW_attributeDefinition_in_classMembers2411 = new BitSet(new long[]{0xB800000000002102L,0x0000000000000020L});
    public static final BitSet FOLLOW_functionDefinition_in_classMembers2433 = new BitSet(new long[]{0xB800000000002102L,0x0000000000000020L});
    public static final BitSet FOLLOW_initDefinition_in_classMembers2456 = new BitSet(new long[]{0xB800000000002102L});
    public static final BitSet FOLLOW_attributeDefinition_in_classMembers2479 = new BitSet(new long[]{0xB800000000002102L});
    public static final BitSet FOLLOW_functionDefinition_in_classMembers2502 = new BitSet(new long[]{0xB800000000002102L});
    public static final BitSet FOLLOW_modifierFlags_in_attributeDefinition2542 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeDefinition2544 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_name_in_attributeDefinition2546 = new BitSet(new long[]{0x0000000800000000L,0x0000008000220001L});
    public static final BitSet FOLLOW_typeReference_in_attributeDefinition2554 = new BitSet(new long[]{0x0000000800000000L,0x0000000000220001L});
    public static final BitSet FOLLOW_EQ_in_attributeDefinition2562 = new BitSet(new long[]{0x0600790018008060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_bindOpt_in_attributeDefinition2564 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_attributeDefinition2566 = new BitSet(new long[]{0x0000000800000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_inverseClause_in_attributeDefinition2570 = new BitSet(new long[]{0x0000000800000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_ON_in_attributeDefinition2580 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_CHANGE_in_attributeDefinition2582 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_attributeDefinition2586 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_SEMI_in_attributeDefinition2595 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INVERSE_in_inverseClause2621 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_memberSelector_in_inverseClause2623 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_functionDefinition2641 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_FUNCTION_in_functionDefinition2643 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_name_in_functionDefinition2645 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_formalParameters_in_functionDefinition2653 = new BitSet(new long[]{0x0000000000000000L,0x0000108000000000L});
    public static final BitSet FOLLOW_typeReference_in_functionDefinition2656 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_blockExpression_in_functionDefinition2665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INIT_in_initDefinition2685 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_initDefinition2687 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags2716 = new BitSet(new long[]{0x3800000000000002L});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags2729 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags2751 = new BitSet(new long[]{0x8000000000000102L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags2764 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_accessModifier2812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PRIVATE_in_accessModifier2832 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROTECTED_in_accessModifier2851 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ABSTRACT_in_otherModifier2878 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_READONLY_in_otherModifier2896 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_memberSelector2925 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_DOT_in_memberSelector2929 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_name_in_memberSelector2935 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_formalParameters2953 = new BitSet(new long[]{0x0000000000000000L,0x0082000000004000L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters2960 = new BitSet(new long[]{0x0000000000000000L,0x0000000000044000L});
    public static final BitSet FOLLOW_COMMA_in_formalParameters2978 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters2984 = new BitSet(new long[]{0x0000000000000000L,0x0000000000044000L});
    public static final BitSet FOLLOW_RPAREN_in_formalParameters2995 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_formalParameter3010 = new BitSet(new long[]{0x0000000000000002L,0x0000008000000000L});
    public static final BitSet FOLLOW_typeReference_in_formalParameter3012 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_block3038 = new BitSet(new long[]{0xFE267900E0010160L,0x00969B007002BA00L});
    public static final BitSet FOLLOW_statement_in_block3048 = new BitSet(new long[]{0xFE267900E0010160L,0x00969B007002BA00L});
    public static final BitSet FOLLOW_expression_in_block3063 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_SEMI_in_block3065 = new BitSet(new long[]{0xFE267900E0010160L,0x00969B007002BA00L});
    public static final BitSet FOLLOW_RBRACE_in_block3081 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_blockExpression3105 = new BitSet(new long[]{0xFE267900E0010160L,0x00969B007002BA00L});
    public static final BitSet FOLLOW_statements_in_blockExpression3108 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RBRACE_in_blockExpression3113 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_statements3132 = new BitSet(new long[]{0xFE267900E0010162L,0x00961B007002BA00L});
    public static final BitSet FOLLOW_statements_in_statements3144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_statements3156 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_SEMI_in_statements3168 = new BitSet(new long[]{0xFE267900E0010162L,0x00961B007002BA00L});
    public static final BitSet FOLLOW_statements_in_statements3191 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclaration_in_statement3249 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_SEMI_in_statement3251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionDefinition_in_statement3259 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_statement3275 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_LPAREN_in_statement3277 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_statement3279 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_RPAREN_in_statement3281 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_statement3283 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BREAK_in_statement3290 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_SEMI_in_statement3292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_statement3305 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_SEMI_in_statement3308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THROW_in_statement3326 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_statement3328 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_SEMI_in_statement3330 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_returnStatement_in_statement3350 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_SEMI_in_statement3352 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tryStatement_in_statement3368 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_statement3379 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variableDeclaration3409 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_name_in_variableDeclaration3412 = new BitSet(new long[]{0x0000000000000002L,0x0000008000200000L});
    public static final BitSet FOLLOW_typeReference_in_variableDeclaration3415 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_EQ_in_variableDeclaration3426 = new BitSet(new long[]{0x0600790018008060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_bindOpt_in_variableDeclaration3428 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_variableDeclaration3431 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BIND_in_bindOpt3481 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3497 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAYS_in_bindOpt3512 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3528 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TIE_in_bindOpt3543 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3559 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnStatement3593 = new BitSet(new long[]{0x0600790000000062L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_returnStatement3596 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRY_in_tryStatement3639 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3645 = new BitSet(new long[]{0x0040000000040000L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement3658 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catchClause_in_tryStatement3680 = new BitSet(new long[]{0x0040000000040002L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement3703 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3708 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CATCH_in_catchClause3747 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_LPAREN_in_catchClause3750 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_name_in_catchClause3753 = new BitSet(new long[]{0x0000000000000000L,0x0000008000004000L});
    public static final BitSet FOLLOW_COLON_in_catchClause3761 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_identifier_in_catchClause3763 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_RPAREN_in_catchClause3772 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_catchClause3776 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifExpression_in_expression3804 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_suffixedExpression_in_expression3827 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifExpression3858 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_ifExpression3862 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_THEN_in_ifExpression3866 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_ifExpression3871 = new BitSet(new long[]{0x0000040000000002L});
    public static final BitSet FOLLOW_ELSE_in_ifExpression3880 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_ifExpression3885 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentExpression_in_suffixedExpression3910 = new BitSet(new long[]{0x0000000000000002L,0x0000000050000000L});
    public static final BitSet FOLLOW_set_in_suffixedExpression3921 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression3950 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_EQ_in_assignmentExpression3965 = new BitSet(new long[]{0x0600780000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression3971 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression3998 = new BitSet(new long[]{0x0000000000000002L,0x0000007C00000000L});
    public static final BitSet FOLLOW_assignmentOperator_in_assignmentOpExpression4014 = new BitSet(new long[]{0x0600780000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression4020 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orExpression_in_andExpression4046 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_AND_in_andExpression4062 = new BitSet(new long[]{0x0600780000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_orExpression_in_andExpression4068 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression4096 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_OR_in_orExpression4111 = new BitSet(new long[]{0x0600780000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression4117 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression4145 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000040L});
    public static final BitSet FOLLOW_INSTANCEOF_in_instanceOfExpression4160 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_identifier_in_instanceOfExpression4162 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4190 = new BitSet(new long[]{0x0001000000000002L,0x0000000007D00000L});
    public static final BitSet FOLLOW_LTGT_in_relationalExpression4206 = new BitSet(new long[]{0x0600780000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4212 = new BitSet(new long[]{0x0001000000000002L,0x0000000007D00000L});
    public static final BitSet FOLLOW_EQEQ_in_relationalExpression4226 = new BitSet(new long[]{0x0600780000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4232 = new BitSet(new long[]{0x0001000000000002L,0x0000000007D00000L});
    public static final BitSet FOLLOW_LTEQ_in_relationalExpression4246 = new BitSet(new long[]{0x0600780000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4252 = new BitSet(new long[]{0x0001000000000002L,0x0000000007D00000L});
    public static final BitSet FOLLOW_GTEQ_in_relationalExpression4266 = new BitSet(new long[]{0x0600780000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4272 = new BitSet(new long[]{0x0001000000000002L,0x0000000007D00000L});
    public static final BitSet FOLLOW_LT_in_relationalExpression4286 = new BitSet(new long[]{0x0600780000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4294 = new BitSet(new long[]{0x0001000000000002L,0x0000000007D00000L});
    public static final BitSet FOLLOW_GT_in_relationalExpression4308 = new BitSet(new long[]{0x0600780000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4316 = new BitSet(new long[]{0x0001000000000002L,0x0000000007D00000L});
    public static final BitSet FOLLOW_IN_in_relationalExpression4330 = new BitSet(new long[]{0x0600780000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4338 = new BitSet(new long[]{0x0001000000000002L,0x0000000007D00000L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4367 = new BitSet(new long[]{0x0000000000000002L,0x0000000028000000L});
    public static final BitSet FOLLOW_PLUS_in_additiveExpression4382 = new BitSet(new long[]{0x0600780000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4388 = new BitSet(new long[]{0x0000000000000002L,0x0000000028000000L});
    public static final BitSet FOLLOW_SUB_in_additiveExpression4401 = new BitSet(new long[]{0x0600780000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4408 = new BitSet(new long[]{0x0000000000000002L,0x0000000028000000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4436 = new BitSet(new long[]{0x0000000000000002L,0x0000000380000000L});
    public static final BitSet FOLLOW_STAR_in_multiplicativeExpression4452 = new BitSet(new long[]{0x0600780000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4459 = new BitSet(new long[]{0x0000000000000002L,0x0000000380000000L});
    public static final BitSet FOLLOW_SLASH_in_multiplicativeExpression4473 = new BitSet(new long[]{0x0600780000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4479 = new BitSet(new long[]{0x0000000000000002L,0x0000000380000000L});
    public static final BitSet FOLLOW_PERCENT_in_multiplicativeExpression4493 = new BitSet(new long[]{0x0600780000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4497 = new BitSet(new long[]{0x0000000000000002L,0x0000000380000000L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression4527 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryOperator_in_unaryExpression4538 = new BitSet(new long[]{0x0400780000000000L,0x00961A000000A200L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression4542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primaryExpression_in_postfixExpression4562 = new BitSet(new long[]{0x0000000000000002L,0x0000000000088000L});
    public static final BitSet FOLLOW_DOT_in_postfixExpression4577 = new BitSet(new long[]{0x0000000000100000L,0x0082000000000000L});
    public static final BitSet FOLLOW_CLASS_in_postfixExpression4581 = new BitSet(new long[]{0x0000000000000002L,0x0000000000088000L});
    public static final BitSet FOLLOW_name_in_postfixExpression4605 = new BitSet(new long[]{0x0000000000000002L,0x000000000008A000L});
    public static final BitSet FOLLOW_LPAREN_in_postfixExpression4630 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000FA00L});
    public static final BitSet FOLLOW_expressionListOpt_in_postfixExpression4632 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_RPAREN_in_postfixExpression4634 = new BitSet(new long[]{0x0000000000000002L,0x000000000008A000L});
    public static final BitSet FOLLOW_LBRACKET_in_postfixExpression4666 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_name_in_postfixExpression4669 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_BAR_in_postfixExpression4671 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_postfixExpression4675 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RBRACKET_in_postfixExpression4678 = new BitSet(new long[]{0x0000000000000002L,0x0000000000088000L});
    public static final BitSet FOLLOW_newExpression_in_primaryExpression4703 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression4715 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_LBRACE_in_primaryExpression4717 = new BitSet(new long[]{0xB800000000002100L,0x0082800000000000L});
    public static final BitSet FOLLOW_objectLiteral_in_primaryExpression4720 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RBRACE_in_primaryExpression4722 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THIS_in_primaryExpression4739 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_primaryExpression4759 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression4779 = new BitSet(new long[]{0x0000000000000002L,0x0000000000002000L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression4800 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000FA00L});
    public static final BitSet FOLLOW_expressionListOpt_in_primaryExpression4804 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression4808 = new BitSet(new long[]{0x0000000000000002L,0x0000000000002000L});
    public static final BitSet FOLLOW_stringExpression_in_primaryExpression4828 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_bracketExpression_in_primaryExpression4847 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primaryExpression4866 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_blockExpression_in_primaryExpression4886 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression4904 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_primaryExpression4906 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression4908 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_newExpression4941 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_identifier_in_newExpression4944 = new BitSet(new long[]{0x0000000000000002L,0x0000000000002000L});
    public static final BitSet FOLLOW_LPAREN_in_newExpression4952 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000FA00L});
    public static final BitSet FOLLOW_expressionListOpt_in_newExpression4956 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_RPAREN_in_newExpression4960 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_objectLiteralPart_in_objectLiteral5000 = new BitSet(new long[]{0xB800000000002102L,0x0082000000000000L});
    public static final BitSet FOLLOW_name_in_objectLiteralPart5028 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_COLON_in_objectLiteralPart5030 = new BitSet(new long[]{0x0600790018008060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_bindOpt_in_objectLiteralPart5033 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_objectLiteralPart5035 = new BitSet(new long[]{0x0000000000000002L,0x0000000000060000L});
    public static final BitSet FOLLOW_set_in_objectLiteralPart5037 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeDefinition_in_objectLiteralPart5058 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionDefinition_in_objectLiteralPart5070 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression5101 = new BitSet(new long[]{0x0600790000000060L,0x00971B007000BA00L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression5110 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_stringExpression5121 = new BitSet(new long[]{0x0000000000000000L,0x0000600000000000L});
    public static final BitSet FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression5136 = new BitSet(new long[]{0x0600790000000060L,0x00971B007000BA00L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression5148 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_stringExpression5162 = new BitSet(new long[]{0x0000000000000000L,0x0000600000000000L});
    public static final BitSet FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression5183 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull5213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_bracketExpression5246 = new BitSet(new long[]{0x0600790000000060L,0x00961B007001BA00L});
    public static final BitSet FOLLOW_expression_in_bracketExpression5274 = new BitSet(new long[]{0x0000000000000080L,0x0000000000050000L});
    public static final BitSet FOLLOW_COMMA_in_bracketExpression5307 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_bracketExpression5311 = new BitSet(new long[]{0x0000000000000000L,0x0000000000050000L});
    public static final BitSet FOLLOW_COMMA_in_bracketExpression5346 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_bracketExpression5351 = new BitSet(new long[]{0x0000000000000000L,0x0000000000050000L});
    public static final BitSet FOLLOW_DOTDOT_in_bracketExpression5397 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_bracketExpression5403 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RBRACKET_in_bracketExpression5429 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt5450 = new BitSet(new long[]{0x0000000000000002L,0x0000000000040000L});
    public static final BitSet FOLLOW_COMMA_in_expressionListOpt5461 = new BitSet(new long[]{0x0600790000000060L,0x00961B007000BA00L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt5467 = new BitSet(new long[]{0x0000000000000002L,0x0000000000040000L});
    public static final BitSet FOLLOW_POUND_in_unaryOperator5490 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_unaryOperator5501 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUB_in_unaryOperator5514 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_unaryOperator5527 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIZEOF_in_unaryOperator5540 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TYPEOF_in_unaryOperator5553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REVERSE_in_unaryOperator5566 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_unaryOperator5579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_unaryOperator5592 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSEQ_in_assignmentOperator5613 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBEQ_in_assignmentOperator5626 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAREQ_in_assignmentOperator5639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SLASHEQ_in_assignmentOperator5652 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENTEQ_in_assignmentOperator5665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLON_in_typeReference5689 = new BitSet(new long[]{0x0000000000000000L,0x0082000080000000L});
    public static final BitSet FOLLOW_name_in_typeReference5694 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference5698 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_typeReference5724 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference5728 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_cardinalityConstraint5790 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RBRACKET_in_cardinalityConstraint5794 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_literal5848 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_LITERAL_in_literal5858 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOATING_POINT_LITERAL_in_literal5868 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRUE_in_literal5878 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALSE_in_literal5892 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_literal5906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualident_in_typeName5933 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_qualident5975 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_DOT_in_qualident6003 = new BitSet(new long[]{0x0000000000000000L,0x0082000000000000L});
    public static final BitSet FOLLOW_name_in_qualident6005 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_name_in_identifier6042 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_name6076 = new BitSet(new long[]{0x0000000000000002L});

}