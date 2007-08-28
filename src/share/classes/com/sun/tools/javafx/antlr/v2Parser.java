// $ANTLR 3.0 C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g 2007-08-27 21:10:54

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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "BAR", "POUND", "TYPEOF", "DOTDOT", "ABSTRACT", "AFTER", "AND", "AS", "ASSERT", "ATTRIBUTE", "BEFORE", "BIND", "BREAK", "BY", "CATCH", "CHANGE", "CLASS", "DELETE", "DO", "DUR", "EASEBOTH", "EASEIN", "EASEOUT", "TIE", "STAYS", "RETURN", "THROW", "VAR", "PACKAGE", "IMPORT", "FROM", "ON", "INSERT", "INTO", "FIRST", "LAST", "IF", "THEN", "ELSE", "THIS", "NULL", "TRUE", "FALSE", "FOR", "IN", "WHILE", "CONTINUE", "LINEAR", "MOTION", "TRY", "FINALLY", "LAZY", "WHERE", "NOT", "NEW", "PRIVATE", "PROTECTED", "PUBLIC", "FUNCTION", "READONLY", "INVERSE", "TYPE", "EXTENDS", "ORDER", "IMPLEMENTS", "INDEX", "INIT", "INSTANCEOF", "INDEXOF", "SELECT", "SUPER", "OR", "SIZEOF", "REVERSE", "LPAREN", "RPAREN", "LBRACKET", "RBRACKET", "SEMI", "COMMA", "DOT", "EQEQ", "EQ", "GT", "LT", "LTGT", "LTEQ", "GTEQ", "PLUS", "PLUSPLUS", "SUB", "SUBSUB", "STAR", "SLASH", "PERCENT", "PLUSEQ", "SUBEQ", "STAREQ", "SLASHEQ", "PERCENTEQ", "COLON", "QUES", "STRING_LITERAL", "NextIsPercent", "QUOTE_LBRACE_STRING_LITERAL", "LBRACE", "RBRACE_QUOTE_STRING_LITERAL", "RBRACE_LBRACE_STRING_LITERAL", "RBRACE", "FORMAT_STRING_LITERAL", "QUOTED_IDENTIFIER", "INTEGER_LITERAL", "Exponent", "FLOATING_POINT_LITERAL", "Letter", "JavaIDDigit", "IDENTIFIER", "WS", "COMMENT", "LINE_COMMENT"
    };
    public static final int PACKAGE=32;
    public static final int FUNCTION=62;
    public static final int LT=88;
    public static final int STAR=96;
    public static final int WHILE=49;
    public static final int EASEOUT=26;
    public static final int NEW=58;
    public static final int INDEXOF=72;
    public static final int DO=22;
    public static final int NOT=57;
    public static final int EOF=-1;
    public static final int RBRACE_QUOTE_STRING_LITERAL=110;
    public static final int BREAK=16;
    public static final int TYPE=65;
    public static final int LBRACKET=80;
    public static final int RPAREN=79;
    public static final int IMPORT=33;
    public static final int LINEAR=51;
    public static final int STRING_LITERAL=106;
    public static final int FLOATING_POINT_LITERAL=117;
    public static final int INSERT=36;
    public static final int SUBSUB=95;
    public static final int BIND=15;
    public static final int STAREQ=101;
    public static final int THIS=43;
    public static final int RETURN=29;
    public static final int VAR=31;
    public static final int SUPER=74;
    public static final int LAST=39;
    public static final int EQ=86;
    public static final int COMMENT=122;
    public static final int SELECT=73;
    public static final int INTO=37;
    public static final int QUES=105;
    public static final int EQEQ=85;
    public static final int MOTION=52;
    public static final int RBRACE=112;
    public static final int POUND=5;
    public static final int LINE_COMMENT=123;
    public static final int PRIVATE=59;
    public static final int NULL=44;
    public static final int ELSE=42;
    public static final int ON=35;
    public static final int DELETE=21;
    public static final int SLASHEQ=102;
    public static final int EASEBOTH=24;
    public static final int ASSERT=12;
    public static final int TRY=53;
    public static final int INVERSE=64;
    public static final int WS=121;
    public static final int TYPEOF=6;
    public static final int INTEGER_LITERAL=115;
    public static final int OR=75;
    public static final int JavaIDDigit=119;
    public static final int SIZEOF=76;
    public static final int GT=87;
    public static final int FROM=34;
    public static final int CATCH=18;
    public static final int REVERSE=77;
    public static final int FALSE=46;
    public static final int INIT=70;
    public static final int Letter=118;
    public static final int THROW=30;
    public static final int DUR=23;
    public static final int WHERE=56;
    public static final int PROTECTED=60;
    public static final int CLASS=20;
    public static final int ORDER=67;
    public static final int PLUSPLUS=93;
    public static final int LBRACE=109;
    public static final int ATTRIBUTE=13;
    public static final int LTEQ=90;
    public static final int SUBEQ=100;
    public static final int Exponent=116;
    public static final int FOR=47;
    public static final int SUB=94;
    public static final int DOTDOT=7;
    public static final int ABSTRACT=8;
    public static final int NextIsPercent=107;
    public static final int AND=10;
    public static final int PLUSEQ=99;
    public static final int LPAREN=78;
    public static final int IF=40;
    public static final int AS=11;
    public static final int INDEX=69;
    public static final int SLASH=97;
    public static final int THEN=41;
    public static final int IN=48;
    public static final int IMPLEMENTS=68;
    public static final int CONTINUE=50;
    public static final int COMMA=83;
    public static final int TIE=27;
    public static final int IDENTIFIER=120;
    public static final int QUOTE_LBRACE_STRING_LITERAL=108;
    public static final int PLUS=92;
    public static final int RBRACKET=81;
    public static final int DOT=84;
    public static final int RBRACE_LBRACE_STRING_LITERAL=111;
    public static final int STAYS=28;
    public static final int BY=17;
    public static final int PERCENT=98;
    public static final int LAZY=55;
    public static final int LTGT=89;
    public static final int BEFORE=14;
    public static final int INSTANCEOF=71;
    public static final int AFTER=9;
    public static final int GTEQ=91;
    public static final int READONLY=63;
    public static final int TRUE=45;
    public static final int SEMI=82;
    public static final int CHANGE=19;
    public static final int COLON=104;
    public static final int PERCENTEQ=103;
    public static final int FINALLY=54;
    public static final int FORMAT_STRING_LITERAL=113;
    public static final int EASEIN=25;
    public static final int QUOTED_IDENTIFIER=114;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:317:1: module returns [JCCompilationUnit result] : ( packageDecl )? moduleItems EOF ;
    public final JCCompilationUnit module() throws RecognitionException {
        JCCompilationUnit result = null;

        JCExpression packageDecl1 = null;

        ListBuffer<JCTree> moduleItems2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:318:8: ( ( packageDecl )? moduleItems EOF )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:318:10: ( packageDecl )? moduleItems EOF
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:318:10: ( packageDecl )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==PACKAGE) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:318:10: packageDecl
                    {
                    pushFollow(FOLLOW_packageDecl_in_module2008);
                    packageDecl1=packageDecl();
                    _fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_moduleItems_in_module2011);
            moduleItems2=moduleItems();
            _fsp--;

            match(input,EOF,FOLLOW_EOF_in_module2013); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:319:1: packageDecl returns [JCExpression value] : PACKAGE qualident SEMI ;
    public final JCExpression packageDecl() throws RecognitionException {
        JCExpression value = null;

        JCExpression qualident3 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:320:8: ( PACKAGE qualident SEMI )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:320:10: PACKAGE qualident SEMI
            {
            match(input,PACKAGE,FOLLOW_PACKAGE_in_packageDecl2035); 
            pushFollow(FOLLOW_qualident_in_packageDecl2037);
            qualident3=qualident();
            _fsp--;

            match(input,SEMI,FOLLOW_SEMI_in_packageDecl2039); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:1: moduleItems returns [ListBuffer<JCTree> items = new ListBuffer<JCTree>()] : ( moduleItem )* ;
    public final ListBuffer<JCTree> moduleItems() throws RecognitionException {
        ListBuffer<JCTree> items =  new ListBuffer<JCTree>();

        JCTree moduleItem4 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:322:9: ( ( moduleItem )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:322:11: ( moduleItem )*
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:322:11: ( moduleItem )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=POUND && LA2_0<=TYPEOF)||LA2_0==ABSTRACT||LA2_0==BREAK||LA2_0==CLASS||(LA2_0>=RETURN && LA2_0<=VAR)||LA2_0==IMPORT||LA2_0==IF||(LA2_0>=THIS && LA2_0<=FALSE)||(LA2_0>=WHILE && LA2_0<=CONTINUE)||LA2_0==TRY||(LA2_0>=NOT && LA2_0<=READONLY)||LA2_0==SUPER||(LA2_0>=SIZEOF && LA2_0<=LPAREN)||LA2_0==LBRACKET||LA2_0==SEMI||(LA2_0>=PLUSPLUS && LA2_0<=SUBSUB)||(LA2_0>=QUES && LA2_0<=STRING_LITERAL)||(LA2_0>=QUOTE_LBRACE_STRING_LITERAL && LA2_0<=LBRACE)||(LA2_0>=QUOTED_IDENTIFIER && LA2_0<=INTEGER_LITERAL)||LA2_0==FLOATING_POINT_LITERAL||LA2_0==IDENTIFIER) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:322:12: moduleItem
            	    {
            	    pushFollow(FOLLOW_moduleItem_in_moduleItems2070);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );
    public final JCTree moduleItem() throws RecognitionException {
        JCTree value = null;

        JCTree importDecl5 = null;

        JFXClassDeclaration classDefinition6 = null;

        JCStatement statement7 = null;

        JCExpression expression8 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:324:8: ( importDecl | classDefinition | statement | expression SEMI )
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
                            new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 10, input);

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
                            new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 11, input);

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
                            new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 12, input);

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
                        new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 2, input);

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
                            new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 10, input);

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
                            new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 11, input);

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
                            new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 12, input);

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
                        new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 3, input);

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
                            new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 13, input);

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
                            new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 14, input);

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
                        new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 4, input);

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
                            new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 13, input);

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
                            new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 14, input);

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
                        new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 5, input);

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
                            new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 13, input);

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
                            new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 14, input);

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
                        new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 6, input);

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
                    new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:324:10: importDecl
                    {
                    pushFollow(FOLLOW_importDecl_in_moduleItem2114);
                    importDecl5=importDecl();
                    _fsp--;

                     value = importDecl5; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:325:10: classDefinition
                    {
                    pushFollow(FOLLOW_classDefinition_in_moduleItem2130);
                    classDefinition6=classDefinition();
                    _fsp--;

                     value = classDefinition6; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:326:10: statement
                    {
                    pushFollow(FOLLOW_statement_in_moduleItem2146);
                    statement7=statement();
                    _fsp--;

                     value = statement7; 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:327:10: expression SEMI
                    {
                    pushFollow(FOLLOW_expression_in_moduleItem2168);
                    expression8=expression();
                    _fsp--;

                    match(input,SEMI,FOLLOW_SEMI_in_moduleItem2170); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:329:1: importDecl returns [JCTree value] : IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI ;
    public final JCTree importDecl() throws RecognitionException {
        JCTree value = null;

        Token STAR11=null;
        Token IMPORT12=null;
        JCIdent identifier9 = null;

        name_return name10 = null;


         JCExpression pid = null; 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:331:9: ( IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:331:11: IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI
            {
            IMPORT12=(Token)input.LT(1);
            match(input,IMPORT,FOLLOW_IMPORT_in_importDecl2207); 
            pushFollow(FOLLOW_identifier_in_importDecl2210);
            identifier9=identifier();
            _fsp--;

             pid = identifier9; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:332:18: ( DOT name )*
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
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:332:20: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_importDecl2235); 
            	    pushFollow(FOLLOW_name_in_importDecl2237);
            	    name10=name();
            	    _fsp--;

            	     pid = F.at(name10.pos).Select(pid, name10.value); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:333:18: ( DOT STAR )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==DOT) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:333:20: DOT STAR
                    {
                    match(input,DOT,FOLLOW_DOT_in_importDecl2266); 
                    STAR11=(Token)input.LT(1);
                    match(input,STAR,FOLLOW_STAR_in_importDecl2268); 
                     pid = F.at(pos(STAR11)).Select(pid, names.asterisk); 

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_importDecl2277); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:335:1: classDefinition returns [JFXClassDeclaration value] : modifierFlags CLASS name supers interfaces LBRACE classMembers RBRACE ;
    public final JFXClassDeclaration classDefinition() throws RecognitionException {
        JFXClassDeclaration value = null;

        Token CLASS13=null;
        JCModifiers modifierFlags14 = null;

        name_return name15 = null;

        ListBuffer<JCExpression> supers16 = null;

        ListBuffer<JCExpression> interfaces17 = null;

        ListBuffer<JCTree> classMembers18 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:336:2: ( modifierFlags CLASS name supers interfaces LBRACE classMembers RBRACE )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:336:4: modifierFlags CLASS name supers interfaces LBRACE classMembers RBRACE
            {
            pushFollow(FOLLOW_modifierFlags_in_classDefinition2308);
            modifierFlags14=modifierFlags();
            _fsp--;

            CLASS13=(Token)input.LT(1);
            match(input,CLASS,FOLLOW_CLASS_in_classDefinition2311); 
            pushFollow(FOLLOW_name_in_classDefinition2313);
            name15=name();
            _fsp--;

            pushFollow(FOLLOW_supers_in_classDefinition2315);
            supers16=supers();
            _fsp--;

            pushFollow(FOLLOW_interfaces_in_classDefinition2317);
            interfaces17=interfaces();
            _fsp--;

            match(input,LBRACE,FOLLOW_LBRACE_in_classDefinition2319); 
            pushFollow(FOLLOW_classMembers_in_classDefinition2321);
            classMembers18=classMembers();
            _fsp--;

            match(input,RBRACE,FOLLOW_RBRACE_in_classDefinition2323); 
             value = F.at(pos(CLASS13)).ClassDeclaration(modifierFlags14, name15.value,
            	                                	                supers16.toList(), interfaces17.toList(), 
            	                                	                classMembers18.toList()); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:340:1: supers returns [ListBuffer<JCExpression> ids = new ListBuffer<JCExpression>()] : ( EXTENDS id1= qualident ( COMMA idn= qualident )* )? ;
    public final ListBuffer<JCExpression> supers() throws RecognitionException {
        ListBuffer<JCExpression> ids =  new ListBuffer<JCExpression>();

        JCExpression id1 = null;

        JCExpression idn = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:341:2: ( ( EXTENDS id1= qualident ( COMMA idn= qualident )* )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:341:4: ( EXTENDS id1= qualident ( COMMA idn= qualident )* )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:341:4: ( EXTENDS id1= qualident ( COMMA idn= qualident )* )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==EXTENDS) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:341:5: EXTENDS id1= qualident ( COMMA idn= qualident )*
                    {
                    match(input,EXTENDS,FOLLOW_EXTENDS_in_supers2348); 
                    pushFollow(FOLLOW_qualident_in_supers2352);
                    id1=qualident();
                    _fsp--;

                     ids.append(id1); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:342:12: ( COMMA idn= qualident )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==COMMA) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:342:14: COMMA idn= qualident
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_supers2380); 
                    	    pushFollow(FOLLOW_qualident_in_supers2384);
                    	    idn=qualident();
                    	    _fsp--;

                    	     ids.append(idn); 

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
        return ids;
    }
    // $ANTLR end supers


    // $ANTLR start interfaces
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:345:1: interfaces returns [ListBuffer<JCExpression> ids = new ListBuffer<JCExpression>()] : ( IMPLEMENTS id1= qualident ( COMMA idn= qualident )* )? ;
    public final ListBuffer<JCExpression> interfaces() throws RecognitionException {
        ListBuffer<JCExpression> ids =  new ListBuffer<JCExpression>();

        JCExpression id1 = null;

        JCExpression idn = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:346:2: ( ( IMPLEMENTS id1= qualident ( COMMA idn= qualident )* )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:346:4: ( IMPLEMENTS id1= qualident ( COMMA idn= qualident )* )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:346:4: ( IMPLEMENTS id1= qualident ( COMMA idn= qualident )* )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==IMPLEMENTS) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:346:5: IMPLEMENTS id1= qualident ( COMMA idn= qualident )*
                    {
                    match(input,IMPLEMENTS,FOLLOW_IMPLEMENTS_in_interfaces2422); 
                    pushFollow(FOLLOW_qualident_in_interfaces2426);
                    id1=qualident();
                    _fsp--;

                     ids.append(id1); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:347:12: ( COMMA idn= qualident )*
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( (LA8_0==COMMA) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:347:14: COMMA idn= qualident
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_interfaces2454); 
                    	    pushFollow(FOLLOW_qualident_in_interfaces2458);
                    	    idn=qualident();
                    	    _fsp--;

                    	     ids.append(idn); 

                    	    }
                    	    break;

                    	default :
                    	    break loop8;
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
        return ids;
    }
    // $ANTLR end interfaces


    // $ANTLR start classMembers
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:350:1: classMembers returns [ListBuffer<JCTree> mems = new ListBuffer<JCTree>()] : (ad1= attributeDefinition | fd1= functionDefinition )* ( initDefinition (ad2= attributeDefinition | fd2= functionDefinition )* )? ;
    public final ListBuffer<JCTree> classMembers() throws RecognitionException {
        ListBuffer<JCTree> mems =  new ListBuffer<JCTree>();

        JFXAttributeDefinition ad1 = null;

        JFXFunctionDefinition fd1 = null;

        JFXAttributeDefinition ad2 = null;

        JFXFunctionDefinition fd2 = null;

        JFXInitDefinition initDefinition19 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:351:2: ( (ad1= attributeDefinition | fd1= functionDefinition )* ( initDefinition (ad2= attributeDefinition | fd2= functionDefinition )* )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:351:4: (ad1= attributeDefinition | fd1= functionDefinition )* ( initDefinition (ad2= attributeDefinition | fd2= functionDefinition )* )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:351:4: (ad1= attributeDefinition | fd1= functionDefinition )*
            loop10:
            do {
                int alt10=3;
                switch ( input.LA(1) ) {
                case ABSTRACT:
                    {
                    switch ( input.LA(2) ) {
                    case PUBLIC:
                        {
                        int LA10_9 = input.LA(3);

                        if ( (LA10_9==FUNCTION) ) {
                            alt10=2;
                        }
                        else if ( (LA10_9==ATTRIBUTE) ) {
                            alt10=1;
                        }


                        }
                        break;
                    case PRIVATE:
                        {
                        int LA10_10 = input.LA(3);

                        if ( (LA10_10==FUNCTION) ) {
                            alt10=2;
                        }
                        else if ( (LA10_10==ATTRIBUTE) ) {
                            alt10=1;
                        }


                        }
                        break;
                    case PROTECTED:
                        {
                        int LA10_11 = input.LA(3);

                        if ( (LA10_11==FUNCTION) ) {
                            alt10=2;
                        }
                        else if ( (LA10_11==ATTRIBUTE) ) {
                            alt10=1;
                        }


                        }
                        break;
                    case FUNCTION:
                        {
                        alt10=2;
                        }
                        break;
                    case ATTRIBUTE:
                        {
                        alt10=1;
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
                        int LA10_9 = input.LA(3);

                        if ( (LA10_9==FUNCTION) ) {
                            alt10=2;
                        }
                        else if ( (LA10_9==ATTRIBUTE) ) {
                            alt10=1;
                        }


                        }
                        break;
                    case PRIVATE:
                        {
                        int LA10_10 = input.LA(3);

                        if ( (LA10_10==FUNCTION) ) {
                            alt10=2;
                        }
                        else if ( (LA10_10==ATTRIBUTE) ) {
                            alt10=1;
                        }


                        }
                        break;
                    case PROTECTED:
                        {
                        int LA10_11 = input.LA(3);

                        if ( (LA10_11==FUNCTION) ) {
                            alt10=2;
                        }
                        else if ( (LA10_11==ATTRIBUTE) ) {
                            alt10=1;
                        }


                        }
                        break;
                    case ATTRIBUTE:
                        {
                        alt10=1;
                        }
                        break;
                    case FUNCTION:
                        {
                        alt10=2;
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
                        int LA10_12 = input.LA(3);

                        if ( (LA10_12==FUNCTION) ) {
                            alt10=2;
                        }
                        else if ( (LA10_12==ATTRIBUTE) ) {
                            alt10=1;
                        }


                        }
                        break;
                    case READONLY:
                        {
                        int LA10_13 = input.LA(3);

                        if ( (LA10_13==FUNCTION) ) {
                            alt10=2;
                        }
                        else if ( (LA10_13==ATTRIBUTE) ) {
                            alt10=1;
                        }


                        }
                        break;
                    case FUNCTION:
                        {
                        alt10=2;
                        }
                        break;
                    case ATTRIBUTE:
                        {
                        alt10=1;
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
                        int LA10_12 = input.LA(3);

                        if ( (LA10_12==FUNCTION) ) {
                            alt10=2;
                        }
                        else if ( (LA10_12==ATTRIBUTE) ) {
                            alt10=1;
                        }


                        }
                        break;
                    case READONLY:
                        {
                        int LA10_13 = input.LA(3);

                        if ( (LA10_13==FUNCTION) ) {
                            alt10=2;
                        }
                        else if ( (LA10_13==ATTRIBUTE) ) {
                            alt10=1;
                        }


                        }
                        break;
                    case FUNCTION:
                        {
                        alt10=2;
                        }
                        break;
                    case ATTRIBUTE:
                        {
                        alt10=1;
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
                        int LA10_12 = input.LA(3);

                        if ( (LA10_12==FUNCTION) ) {
                            alt10=2;
                        }
                        else if ( (LA10_12==ATTRIBUTE) ) {
                            alt10=1;
                        }


                        }
                        break;
                    case READONLY:
                        {
                        int LA10_13 = input.LA(3);

                        if ( (LA10_13==FUNCTION) ) {
                            alt10=2;
                        }
                        else if ( (LA10_13==ATTRIBUTE) ) {
                            alt10=1;
                        }


                        }
                        break;
                    case FUNCTION:
                        {
                        alt10=2;
                        }
                        break;
                    case ATTRIBUTE:
                        {
                        alt10=1;
                        }
                        break;

                    }

                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt10=1;
                    }
                    break;
                case FUNCTION:
                    {
                    alt10=2;
                    }
                    break;

                }

                switch (alt10) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:351:6: ad1= attributeDefinition
            	    {
            	    pushFollow(FOLLOW_attributeDefinition_in_classMembers2497);
            	    ad1=attributeDefinition();
            	    _fsp--;

            	     mems.append(ad1); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:352:7: fd1= functionDefinition
            	    {
            	    pushFollow(FOLLOW_functionDefinition_in_classMembers2519);
            	    fd1=functionDefinition();
            	    _fsp--;

            	     mems.append(fd1); 

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:354:4: ( initDefinition (ad2= attributeDefinition | fd2= functionDefinition )* )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==INIT) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:354:5: initDefinition (ad2= attributeDefinition | fd2= functionDefinition )*
                    {
                    pushFollow(FOLLOW_initDefinition_in_classMembers2542);
                    initDefinition19=initDefinition();
                    _fsp--;

                     mems.append(initDefinition19); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:355:6: (ad2= attributeDefinition | fd2= functionDefinition )*
                    loop11:
                    do {
                        int alt11=3;
                        switch ( input.LA(1) ) {
                        case ABSTRACT:
                            {
                            switch ( input.LA(2) ) {
                            case PUBLIC:
                                {
                                int LA11_9 = input.LA(3);

                                if ( (LA11_9==ATTRIBUTE) ) {
                                    alt11=1;
                                }
                                else if ( (LA11_9==FUNCTION) ) {
                                    alt11=2;
                                }


                                }
                                break;
                            case PRIVATE:
                                {
                                int LA11_10 = input.LA(3);

                                if ( (LA11_10==ATTRIBUTE) ) {
                                    alt11=1;
                                }
                                else if ( (LA11_10==FUNCTION) ) {
                                    alt11=2;
                                }


                                }
                                break;
                            case PROTECTED:
                                {
                                int LA11_11 = input.LA(3);

                                if ( (LA11_11==FUNCTION) ) {
                                    alt11=2;
                                }
                                else if ( (LA11_11==ATTRIBUTE) ) {
                                    alt11=1;
                                }


                                }
                                break;
                            case FUNCTION:
                                {
                                alt11=2;
                                }
                                break;
                            case ATTRIBUTE:
                                {
                                alt11=1;
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
                                int LA11_9 = input.LA(3);

                                if ( (LA11_9==ATTRIBUTE) ) {
                                    alt11=1;
                                }
                                else if ( (LA11_9==FUNCTION) ) {
                                    alt11=2;
                                }


                                }
                                break;
                            case PRIVATE:
                                {
                                int LA11_10 = input.LA(3);

                                if ( (LA11_10==ATTRIBUTE) ) {
                                    alt11=1;
                                }
                                else if ( (LA11_10==FUNCTION) ) {
                                    alt11=2;
                                }


                                }
                                break;
                            case PROTECTED:
                                {
                                int LA11_11 = input.LA(3);

                                if ( (LA11_11==FUNCTION) ) {
                                    alt11=2;
                                }
                                else if ( (LA11_11==ATTRIBUTE) ) {
                                    alt11=1;
                                }


                                }
                                break;
                            case ATTRIBUTE:
                                {
                                alt11=1;
                                }
                                break;
                            case FUNCTION:
                                {
                                alt11=2;
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
                                int LA11_12 = input.LA(3);

                                if ( (LA11_12==FUNCTION) ) {
                                    alt11=2;
                                }
                                else if ( (LA11_12==ATTRIBUTE) ) {
                                    alt11=1;
                                }


                                }
                                break;
                            case READONLY:
                                {
                                int LA11_13 = input.LA(3);

                                if ( (LA11_13==ATTRIBUTE) ) {
                                    alt11=1;
                                }
                                else if ( (LA11_13==FUNCTION) ) {
                                    alt11=2;
                                }


                                }
                                break;
                            case ATTRIBUTE:
                                {
                                alt11=1;
                                }
                                break;
                            case FUNCTION:
                                {
                                alt11=2;
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
                                int LA11_12 = input.LA(3);

                                if ( (LA11_12==FUNCTION) ) {
                                    alt11=2;
                                }
                                else if ( (LA11_12==ATTRIBUTE) ) {
                                    alt11=1;
                                }


                                }
                                break;
                            case READONLY:
                                {
                                int LA11_13 = input.LA(3);

                                if ( (LA11_13==ATTRIBUTE) ) {
                                    alt11=1;
                                }
                                else if ( (LA11_13==FUNCTION) ) {
                                    alt11=2;
                                }


                                }
                                break;
                            case FUNCTION:
                                {
                                alt11=2;
                                }
                                break;
                            case ATTRIBUTE:
                                {
                                alt11=1;
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
                                int LA11_12 = input.LA(3);

                                if ( (LA11_12==FUNCTION) ) {
                                    alt11=2;
                                }
                                else if ( (LA11_12==ATTRIBUTE) ) {
                                    alt11=1;
                                }


                                }
                                break;
                            case READONLY:
                                {
                                int LA11_13 = input.LA(3);

                                if ( (LA11_13==ATTRIBUTE) ) {
                                    alt11=1;
                                }
                                else if ( (LA11_13==FUNCTION) ) {
                                    alt11=2;
                                }


                                }
                                break;
                            case FUNCTION:
                                {
                                alt11=2;
                                }
                                break;
                            case ATTRIBUTE:
                                {
                                alt11=1;
                                }
                                break;

                            }

                            }
                            break;
                        case ATTRIBUTE:
                            {
                            alt11=1;
                            }
                            break;
                        case FUNCTION:
                            {
                            alt11=2;
                            }
                            break;

                        }

                        switch (alt11) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:355:8: ad2= attributeDefinition
                    	    {
                    	    pushFollow(FOLLOW_attributeDefinition_in_classMembers2565);
                    	    ad2=attributeDefinition();
                    	    _fsp--;

                    	     mems.append(ad2); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:356:8: fd2= functionDefinition
                    	    {
                    	    pushFollow(FOLLOW_functionDefinition_in_classMembers2588);
                    	    fd2=functionDefinition();
                    	    _fsp--;

                    	     mems.append(fd2); 

                    	    }
                    	    break;

                    	default :
                    	    break loop11;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:360:1: attributeDefinition returns [JFXAttributeDefinition def] : modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? ( ON CHANGE ocb= block )? SEMI ;
    public final JFXAttributeDefinition attributeDefinition() throws RecognitionException {
        JFXAttributeDefinition def = null;

        Token ATTRIBUTE20=null;
        JCBlock ocb = null;

        JCModifiers modifierFlags21 = null;

        name_return name22 = null;

        JFXType typeReference23 = null;

        JFXMemberSelector inverseClause24 = null;

        JavafxBindStatus bindOpt25 = null;

        JCExpression expression26 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:361:2: ( modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? ( ON CHANGE ocb= block )? SEMI )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:361:4: modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? ( ON CHANGE ocb= block )? SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_attributeDefinition2628);
            modifierFlags21=modifierFlags();
            _fsp--;

            ATTRIBUTE20=(Token)input.LT(1);
            match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeDefinition2630); 
            pushFollow(FOLLOW_name_in_attributeDefinition2632);
            name22=name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_attributeDefinition2640);
            typeReference23=typeReference();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:363:5: ( EQ bindOpt expression | inverseClause )?
            int alt13=3;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==EQ) ) {
                alt13=1;
            }
            else if ( (LA13_0==INVERSE) ) {
                alt13=2;
            }
            switch (alt13) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:363:6: EQ bindOpt expression
                    {
                    match(input,EQ,FOLLOW_EQ_in_attributeDefinition2648); 
                    pushFollow(FOLLOW_bindOpt_in_attributeDefinition2650);
                    bindOpt25=bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_attributeDefinition2652);
                    expression26=expression();
                    _fsp--;


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:363:30: inverseClause
                    {
                    pushFollow(FOLLOW_inverseClause_in_attributeDefinition2656);
                    inverseClause24=inverseClause();
                    _fsp--;


                    }
                    break;

            }

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:364:5: ( ON CHANGE ocb= block )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==ON) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:364:6: ON CHANGE ocb= block
                    {
                    match(input,ON,FOLLOW_ON_in_attributeDefinition2666); 
                    match(input,CHANGE,FOLLOW_CHANGE_in_attributeDefinition2668); 
                    pushFollow(FOLLOW_block_in_attributeDefinition2672);
                    ocb=block();
                    _fsp--;


                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_attributeDefinition2681); 
             def = F.at(pos(ATTRIBUTE20)).AttributeDefinition(modifierFlags21,
            	    						name22.value, typeReference23, inverseClause24, null, 
            	    						bindOpt25, expression26, ocb); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:369:1: inverseClause returns [JFXMemberSelector inverse = null] : INVERSE memberSelector ;
    public final JFXMemberSelector inverseClause() throws RecognitionException {
        JFXMemberSelector inverse =  null;

        JFXMemberSelector memberSelector27 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:370:2: ( INVERSE memberSelector )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:370:4: INVERSE memberSelector
            {
            match(input,INVERSE,FOLLOW_INVERSE_in_inverseClause2707); 
            pushFollow(FOLLOW_memberSelector_in_inverseClause2709);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:371:1: functionDefinition returns [JFXFunctionDefinition def] : modifierFlags FUNCTION name formalParameters typeReference blockExpression ;
    public final JFXFunctionDefinition functionDefinition() throws RecognitionException {
        JFXFunctionDefinition def = null;

        Token FUNCTION28=null;
        JCModifiers modifierFlags29 = null;

        name_return name30 = null;

        JFXType typeReference31 = null;

        ListBuffer<JCTree> formalParameters32 = null;

        JFXBlockExpression blockExpression33 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:372:2: ( modifierFlags FUNCTION name formalParameters typeReference blockExpression )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:372:4: modifierFlags FUNCTION name formalParameters typeReference blockExpression
            {
            pushFollow(FOLLOW_modifierFlags_in_functionDefinition2727);
            modifierFlags29=modifierFlags();
            _fsp--;

            FUNCTION28=(Token)input.LT(1);
            match(input,FUNCTION,FOLLOW_FUNCTION_in_functionDefinition2729); 
            pushFollow(FOLLOW_name_in_functionDefinition2731);
            name30=name();
            _fsp--;

            pushFollow(FOLLOW_formalParameters_in_functionDefinition2739);
            formalParameters32=formalParameters();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_functionDefinition2742);
            typeReference31=typeReference();
            _fsp--;

            pushFollow(FOLLOW_blockExpression_in_functionDefinition2751);
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


    // $ANTLR start initDefinition
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:378:1: initDefinition returns [JFXInitDefinition def] : INIT block ;
    public final JFXInitDefinition initDefinition() throws RecognitionException {
        JFXInitDefinition def = null;

        Token INIT34=null;
        JCBlock block35 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:379:2: ( INIT block )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:379:4: INIT block
            {
            INIT34=(Token)input.LT(1);
            match(input,INIT,FOLLOW_INIT_in_initDefinition2771); 
            pushFollow(FOLLOW_block_in_initDefinition2773);
            block35=block();
            _fsp--;

             def = F.at(pos(INIT34)).InitDefinition(block35); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:381:1: modifierFlags returns [JCModifiers mods] : (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? ;
    public final JCModifiers modifierFlags() throws RecognitionException {
        JCModifiers mods = null;

        long om1 = 0;

        long am1 = 0;

        long am2 = 0;

        long om2 = 0;


         long flags = 0; 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:383:2: ( (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:383:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:383:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
            int alt17=3;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==ABSTRACT||LA17_0==READONLY) ) {
                alt17=1;
            }
            else if ( ((LA17_0>=PRIVATE && LA17_0<=PUBLIC)) ) {
                alt17=2;
            }
            switch (alt17) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:383:6: om1= otherModifier (am1= accessModifier )?
                    {
                    pushFollow(FOLLOW_otherModifier_in_modifierFlags2802);
                    om1=otherModifier();
                    _fsp--;

                     flags |= om1; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:384:3: (am1= accessModifier )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( ((LA15_0>=PRIVATE && LA15_0<=PUBLIC)) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:384:5: am1= accessModifier
                            {
                            pushFollow(FOLLOW_accessModifier_in_modifierFlags2815);
                            am1=accessModifier();
                            _fsp--;

                             flags |= am1; 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:385:6: am2= accessModifier (om2= otherModifier )?
                    {
                    pushFollow(FOLLOW_accessModifier_in_modifierFlags2837);
                    am2=accessModifier();
                    _fsp--;

                     flags |= am2; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:386:3: (om2= otherModifier )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==ABSTRACT||LA16_0==READONLY) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:386:5: om2= otherModifier
                            {
                            pushFollow(FOLLOW_otherModifier_in_modifierFlags2850);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:389:1: accessModifier returns [long flags = 0] : ( PUBLIC | PRIVATE | PROTECTED ) ;
    public final long accessModifier() throws RecognitionException {
        long flags =  0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:390:2: ( ( PUBLIC | PRIVATE | PROTECTED ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:390:4: ( PUBLIC | PRIVATE | PROTECTED )
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:390:4: ( PUBLIC | PRIVATE | PROTECTED )
            int alt18=3;
            switch ( input.LA(1) ) {
            case PUBLIC:
                {
                alt18=1;
                }
                break;
            case PRIVATE:
                {
                alt18=2;
                }
                break;
            case PROTECTED:
                {
                alt18=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("390:4: ( PUBLIC | PRIVATE | PROTECTED )", 18, 0, input);

                throw nvae;
            }

            switch (alt18) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:390:5: PUBLIC
                    {
                    match(input,PUBLIC,FOLLOW_PUBLIC_in_accessModifier2898); 
                     flags |= Flags.PUBLIC; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:391:5: PRIVATE
                    {
                    match(input,PRIVATE,FOLLOW_PRIVATE_in_accessModifier2918); 
                     flags |= Flags.PUBLIC; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:392:5: PROTECTED
                    {
                    match(input,PROTECTED,FOLLOW_PROTECTED_in_accessModifier2937); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:393:1: otherModifier returns [long flags = 0] : ( ABSTRACT | READONLY ) ;
    public final long otherModifier() throws RecognitionException {
        long flags =  0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:394:2: ( ( ABSTRACT | READONLY ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:394:4: ( ABSTRACT | READONLY )
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:394:4: ( ABSTRACT | READONLY )
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==ABSTRACT) ) {
                alt19=1;
            }
            else if ( (LA19_0==READONLY) ) {
                alt19=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("394:4: ( ABSTRACT | READONLY )", 19, 0, input);

                throw nvae;
            }
            switch (alt19) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:394:5: ABSTRACT
                    {
                    match(input,ABSTRACT,FOLLOW_ABSTRACT_in_otherModifier2964); 
                     flags |= Flags.ABSTRACT; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:395:5: READONLY
                    {
                    match(input,READONLY,FOLLOW_READONLY_in_otherModifier2982); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:396:1: memberSelector returns [JFXMemberSelector value] : name1= name DOT name2= name ;
    public final JFXMemberSelector memberSelector() throws RecognitionException {
        JFXMemberSelector value = null;

        name_return name1 = null;

        name_return name2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:397:2: (name1= name DOT name2= name )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:397:4: name1= name DOT name2= name
            {
            pushFollow(FOLLOW_name_in_memberSelector3011);
            name1=name();
            _fsp--;

            match(input,DOT,FOLLOW_DOT_in_memberSelector3015); 
            pushFollow(FOLLOW_name_in_memberSelector3021);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:399:1: formalParameters returns [ListBuffer<JCTree> params = new ListBuffer<JCTree>()] : LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN ;
    public final ListBuffer<JCTree> formalParameters() throws RecognitionException {
        ListBuffer<JCTree> params =  new ListBuffer<JCTree>();

        JFXVar fp0 = null;

        JFXVar fpn = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:400:2: ( LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:400:4: LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_formalParameters3039); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:400:12: (fp0= formalParameter ( COMMA fpn= formalParameter )* )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==QUOTED_IDENTIFIER||LA21_0==IDENTIFIER) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:400:14: fp0= formalParameter ( COMMA fpn= formalParameter )*
                    {
                    pushFollow(FOLLOW_formalParameter_in_formalParameters3046);
                    fp0=formalParameter();
                    _fsp--;

                     params.append(fp0); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:401:12: ( COMMA fpn= formalParameter )*
                    loop20:
                    do {
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( (LA20_0==COMMA) ) {
                            alt20=1;
                        }


                        switch (alt20) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:401:14: COMMA fpn= formalParameter
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_formalParameters3064); 
                    	    pushFollow(FOLLOW_formalParameter_in_formalParameters3070);
                    	    fpn=formalParameter();
                    	    _fsp--;

                    	     params.append(fpn); 

                    	    }
                    	    break;

                    	default :
                    	    break loop20;
                        }
                    } while (true);


                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_formalParameters3081); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:403:1: formalParameter returns [JFXVar var] : name typeReference ;
    public final JFXVar formalParameter() throws RecognitionException {
        JFXVar var = null;

        name_return name36 = null;

        JFXType typeReference37 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:404:2: ( name typeReference )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:404:4: name typeReference
            {
            pushFollow(FOLLOW_name_in_formalParameter3096);
            name36=name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_formalParameter3098);
            typeReference37=typeReference();
            _fsp--;

             var = F.at(name36.pos).Var(name36.value, typeReference37, F.Modifiers(Flags.PARAMETER)); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:406:1: block returns [JCBlock value] : LBRACE ( statement | expression SEMI )* RBRACE ;
    public final JCBlock block() throws RecognitionException {
        JCBlock value = null;

        Token LBRACE40=null;
        JCStatement statement38 = null;

        JCExpression expression39 = null;


         ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>();
        	 	
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:409:2: ( LBRACE ( statement | expression SEMI )* RBRACE )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:409:4: LBRACE ( statement | expression SEMI )* RBRACE
            {
            LBRACE40=(Token)input.LT(1);
            match(input,LBRACE,FOLLOW_LBRACE_in_block3124); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:410:4: ( statement | expression SEMI )*
            loop22:
            do {
                int alt22=3;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==ABSTRACT||LA22_0==BREAK||(LA22_0>=RETURN && LA22_0<=VAR)||(LA22_0>=WHILE && LA22_0<=CONTINUE)||LA22_0==TRY||(LA22_0>=PRIVATE && LA22_0<=READONLY)||LA22_0==SEMI) ) {
                    alt22=1;
                }
                else if ( ((LA22_0>=POUND && LA22_0<=TYPEOF)||LA22_0==IF||(LA22_0>=THIS && LA22_0<=FALSE)||(LA22_0>=NOT && LA22_0<=NEW)||LA22_0==SUPER||(LA22_0>=SIZEOF && LA22_0<=LPAREN)||LA22_0==LBRACKET||(LA22_0>=PLUSPLUS && LA22_0<=SUBSUB)||(LA22_0>=QUES && LA22_0<=STRING_LITERAL)||(LA22_0>=QUOTE_LBRACE_STRING_LITERAL && LA22_0<=LBRACE)||(LA22_0>=QUOTED_IDENTIFIER && LA22_0<=INTEGER_LITERAL)||LA22_0==FLOATING_POINT_LITERAL||LA22_0==IDENTIFIER) ) {
                    alt22=2;
                }


                switch (alt22) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:410:9: statement
            	    {
            	    pushFollow(FOLLOW_statement_in_block3134);
            	    statement38=statement();
            	    _fsp--;

            	     stats.append(statement38); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:411:9: expression SEMI
            	    {
            	    pushFollow(FOLLOW_expression_in_block3149);
            	    expression39=expression();
            	    _fsp--;

            	    match(input,SEMI,FOLLOW_SEMI_in_block3151); 
            	     stats.append(F.Exec(expression39)); 

            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);

            match(input,RBRACE,FOLLOW_RBRACE_in_block3167); 
             value = F.at(pos(LBRACE40)).Block(0L, stats.toList()); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:415:1: blockExpression returns [JFXBlockExpression expr = null] : LBRACE ( statements[stats] )? RBRACE ;
    public final JFXBlockExpression blockExpression() throws RecognitionException {
        JFXBlockExpression expr =  null;

        Token LBRACE41=null;
        JCExpression statements42 = null;


         ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>(); 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:417:2: ( LBRACE ( statements[stats] )? RBRACE )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:417:4: LBRACE ( statements[stats] )? RBRACE
            {
            LBRACE41=(Token)input.LT(1);
            match(input,LBRACE,FOLLOW_LBRACE_in_blockExpression3191); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:417:11: ( statements[stats] )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( ((LA23_0>=POUND && LA23_0<=TYPEOF)||LA23_0==ABSTRACT||LA23_0==BREAK||(LA23_0>=RETURN && LA23_0<=VAR)||LA23_0==IF||(LA23_0>=THIS && LA23_0<=FALSE)||(LA23_0>=WHILE && LA23_0<=CONTINUE)||LA23_0==TRY||(LA23_0>=NOT && LA23_0<=READONLY)||LA23_0==SUPER||(LA23_0>=SIZEOF && LA23_0<=LPAREN)||LA23_0==LBRACKET||LA23_0==SEMI||(LA23_0>=PLUSPLUS && LA23_0<=SUBSUB)||(LA23_0>=QUES && LA23_0<=STRING_LITERAL)||(LA23_0>=QUOTE_LBRACE_STRING_LITERAL && LA23_0<=LBRACE)||(LA23_0>=QUOTED_IDENTIFIER && LA23_0<=INTEGER_LITERAL)||LA23_0==FLOATING_POINT_LITERAL||LA23_0==IDENTIFIER) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:417:12: statements[stats]
                    {
                    pushFollow(FOLLOW_statements_in_blockExpression3194);
                    statements42=statements(stats);
                    _fsp--;


                    }
                    break;

            }

            match(input,RBRACE,FOLLOW_RBRACE_in_blockExpression3199); 
             expr = F.at(pos(LBRACE41)).BlockExpression(0L, stats.toList(), 
            						 					     statements42); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:420:1: statements[ListBuffer<JCStatement> stats] returns [JCExpression expr = null] : ( statement (sts1= statements[stats] )? | expression ( SEMI (stsn= statements[stats] | ) | ) );
    public final JCExpression statements(ListBuffer<JCStatement> stats) throws RecognitionException {
        JCExpression expr =  null;

        JCExpression sts1 = null;

        JCExpression stsn = null;

        JCStatement statement43 = null;

        JCExpression expression44 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:421:2: ( statement (sts1= statements[stats] )? | expression ( SEMI (stsn= statements[stats] | ) | ) )
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==ABSTRACT||LA27_0==BREAK||(LA27_0>=RETURN && LA27_0<=VAR)||(LA27_0>=WHILE && LA27_0<=CONTINUE)||LA27_0==TRY||(LA27_0>=PRIVATE && LA27_0<=READONLY)||LA27_0==SEMI) ) {
                alt27=1;
            }
            else if ( ((LA27_0>=POUND && LA27_0<=TYPEOF)||LA27_0==IF||(LA27_0>=THIS && LA27_0<=FALSE)||(LA27_0>=NOT && LA27_0<=NEW)||LA27_0==SUPER||(LA27_0>=SIZEOF && LA27_0<=LPAREN)||LA27_0==LBRACKET||(LA27_0>=PLUSPLUS && LA27_0<=SUBSUB)||(LA27_0>=QUES && LA27_0<=STRING_LITERAL)||(LA27_0>=QUOTE_LBRACE_STRING_LITERAL && LA27_0<=LBRACE)||(LA27_0>=QUOTED_IDENTIFIER && LA27_0<=INTEGER_LITERAL)||LA27_0==FLOATING_POINT_LITERAL||LA27_0==IDENTIFIER) ) {
                alt27=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("420:1: statements[ListBuffer<JCStatement> stats] returns [JCExpression expr = null] : ( statement (sts1= statements[stats] )? | expression ( SEMI (stsn= statements[stats] | ) | ) );", 27, 0, input);

                throw nvae;
            }
            switch (alt27) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:421:4: statement (sts1= statements[stats] )?
                    {
                    pushFollow(FOLLOW_statement_in_statements3218);
                    statement43=statement();
                    _fsp--;

                     stats.append(statement43); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:422:3: (sts1= statements[stats] )?
                    int alt24=2;
                    int LA24_0 = input.LA(1);

                    if ( ((LA24_0>=POUND && LA24_0<=TYPEOF)||LA24_0==ABSTRACT||LA24_0==BREAK||(LA24_0>=RETURN && LA24_0<=VAR)||LA24_0==IF||(LA24_0>=THIS && LA24_0<=FALSE)||(LA24_0>=WHILE && LA24_0<=CONTINUE)||LA24_0==TRY||(LA24_0>=NOT && LA24_0<=READONLY)||LA24_0==SUPER||(LA24_0>=SIZEOF && LA24_0<=LPAREN)||LA24_0==LBRACKET||LA24_0==SEMI||(LA24_0>=PLUSPLUS && LA24_0<=SUBSUB)||(LA24_0>=QUES && LA24_0<=STRING_LITERAL)||(LA24_0>=QUOTE_LBRACE_STRING_LITERAL && LA24_0<=LBRACE)||(LA24_0>=QUOTED_IDENTIFIER && LA24_0<=INTEGER_LITERAL)||LA24_0==FLOATING_POINT_LITERAL||LA24_0==IDENTIFIER) ) {
                        alt24=1;
                    }
                    switch (alt24) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:422:4: sts1= statements[stats]
                            {
                            pushFollow(FOLLOW_statements_in_statements3230);
                            sts1=statements(stats);
                            _fsp--;

                             expr = sts1; 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:423:4: expression ( SEMI (stsn= statements[stats] | ) | )
                    {
                    pushFollow(FOLLOW_expression_in_statements3242);
                    expression44=expression();
                    _fsp--;

                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:424:10: ( SEMI (stsn= statements[stats] | ) | )
                    int alt26=2;
                    int LA26_0 = input.LA(1);

                    if ( (LA26_0==SEMI) ) {
                        alt26=1;
                    }
                    else if ( (LA26_0==RBRACE) ) {
                        alt26=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("424:10: ( SEMI (stsn= statements[stats] | ) | )", 26, 0, input);

                        throw nvae;
                    }
                    switch (alt26) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:424:11: SEMI (stsn= statements[stats] | )
                            {
                            match(input,SEMI,FOLLOW_SEMI_in_statements3254); 
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:424:17: (stsn= statements[stats] | )
                            int alt25=2;
                            int LA25_0 = input.LA(1);

                            if ( ((LA25_0>=POUND && LA25_0<=TYPEOF)||LA25_0==ABSTRACT||LA25_0==BREAK||(LA25_0>=RETURN && LA25_0<=VAR)||LA25_0==IF||(LA25_0>=THIS && LA25_0<=FALSE)||(LA25_0>=WHILE && LA25_0<=CONTINUE)||LA25_0==TRY||(LA25_0>=NOT && LA25_0<=READONLY)||LA25_0==SUPER||(LA25_0>=SIZEOF && LA25_0<=LPAREN)||LA25_0==LBRACKET||LA25_0==SEMI||(LA25_0>=PLUSPLUS && LA25_0<=SUBSUB)||(LA25_0>=QUES && LA25_0<=STRING_LITERAL)||(LA25_0>=QUOTE_LBRACE_STRING_LITERAL && LA25_0<=LBRACE)||(LA25_0>=QUOTED_IDENTIFIER && LA25_0<=INTEGER_LITERAL)||LA25_0==FLOATING_POINT_LITERAL||LA25_0==IDENTIFIER) ) {
                                alt25=1;
                            }
                            else if ( (LA25_0==RBRACE) ) {
                                alt25=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("424:17: (stsn= statements[stats] | )", 25, 0, input);

                                throw nvae;
                            }
                            switch (alt25) {
                                case 1 :
                                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:424:21: stsn= statements[stats]
                                    {
                                     stats.append(F.Exec(expression44)); 
                                    pushFollow(FOLLOW_statements_in_statements3277);
                                    stsn=statements(stats);
                                    _fsp--;

                                     expr = stsn; 

                                    }
                                    break;
                                case 2 :
                                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:426:30: 
                                    {
                                     expr = expression44; 

                                    }
                                    break;

                            }


                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:428:20: 
                            {
                             expr = expression44; 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:431:1: statement returns [JCStatement value] : ( variableDeclaration SEMI | functionDefinition | WHILE LPAREN expression RPAREN block | BREAK SEMI | CONTINUE SEMI | THROW expression SEMI | returnStatement SEMI | tryStatement | SEMI );
    public final JCStatement statement() throws RecognitionException {
        JCStatement value = null;

        Token WHILE47=null;
        Token BREAK50=null;
        Token CONTINUE51=null;
        Token THROW52=null;
        Token SEMI56=null;
        JCStatement variableDeclaration45 = null;

        JFXFunctionDefinition functionDefinition46 = null;

        JCExpression expression48 = null;

        JCBlock block49 = null;

        JCExpression expression53 = null;

        JCStatement returnStatement54 = null;

        JCStatement tryStatement55 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:432:2: ( variableDeclaration SEMI | functionDefinition | WHILE LPAREN expression RPAREN block | BREAK SEMI | CONTINUE SEMI | THROW expression SEMI | returnStatement SEMI | tryStatement | SEMI )
            int alt28=9;
            switch ( input.LA(1) ) {
            case VAR:
                {
                alt28=1;
                }
                break;
            case ABSTRACT:
            case PRIVATE:
            case PROTECTED:
            case PUBLIC:
            case FUNCTION:
            case READONLY:
                {
                alt28=2;
                }
                break;
            case WHILE:
                {
                alt28=3;
                }
                break;
            case BREAK:
                {
                alt28=4;
                }
                break;
            case CONTINUE:
                {
                alt28=5;
                }
                break;
            case THROW:
                {
                alt28=6;
                }
                break;
            case RETURN:
                {
                alt28=7;
                }
                break;
            case TRY:
                {
                alt28=8;
                }
                break;
            case SEMI:
                {
                alt28=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("431:1: statement returns [JCStatement value] : ( variableDeclaration SEMI | functionDefinition | WHILE LPAREN expression RPAREN block | BREAK SEMI | CONTINUE SEMI | THROW expression SEMI | returnStatement SEMI | tryStatement | SEMI );", 28, 0, input);

                throw nvae;
            }

            switch (alt28) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:432:4: variableDeclaration SEMI
                    {
                    pushFollow(FOLLOW_variableDeclaration_in_statement3335);
                    variableDeclaration45=variableDeclaration();
                    _fsp--;

                    match(input,SEMI,FOLLOW_SEMI_in_statement3337); 
                     value = variableDeclaration45; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:433:4: functionDefinition
                    {
                    pushFollow(FOLLOW_functionDefinition_in_statement3345);
                    functionDefinition46=functionDefinition();
                    _fsp--;

                     value = functionDefinition46; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:434:11: WHILE LPAREN expression RPAREN block
                    {
                    WHILE47=(Token)input.LT(1);
                    match(input,WHILE,FOLLOW_WHILE_in_statement3361); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_statement3363); 
                    pushFollow(FOLLOW_expression_in_statement3365);
                    expression48=expression();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_statement3367); 
                    pushFollow(FOLLOW_block_in_statement3369);
                    block49=block();
                    _fsp--;

                     value = F.at(pos(WHILE47)).WhileLoop(expression48, block49); 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:435:4: BREAK SEMI
                    {
                    BREAK50=(Token)input.LT(1);
                    match(input,BREAK,FOLLOW_BREAK_in_statement3376); 
                    match(input,SEMI,FOLLOW_SEMI_in_statement3378); 
                     value = F.at(pos(BREAK50)).Break(null); 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:436:4: CONTINUE SEMI
                    {
                    CONTINUE51=(Token)input.LT(1);
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_statement3391); 
                    match(input,SEMI,FOLLOW_SEMI_in_statement3394); 
                     value = F.at(pos(CONTINUE51)).Continue(null); 

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:437:11: THROW expression SEMI
                    {
                    THROW52=(Token)input.LT(1);
                    match(input,THROW,FOLLOW_THROW_in_statement3412); 
                    pushFollow(FOLLOW_expression_in_statement3414);
                    expression53=expression();
                    _fsp--;

                    match(input,SEMI,FOLLOW_SEMI_in_statement3416); 
                     value = F.at(pos(THROW52)).Throw(expression53); 

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:438:11: returnStatement SEMI
                    {
                    pushFollow(FOLLOW_returnStatement_in_statement3436);
                    returnStatement54=returnStatement();
                    _fsp--;

                    match(input,SEMI,FOLLOW_SEMI_in_statement3438); 
                     value = returnStatement54; 

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:439:11: tryStatement
                    {
                    pushFollow(FOLLOW_tryStatement_in_statement3454);
                    tryStatement55=tryStatement();
                    _fsp--;

                     value = tryStatement55; 

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:440:4: SEMI
                    {
                    SEMI56=(Token)input.LT(1);
                    match(input,SEMI,FOLLOW_SEMI_in_statement3465); 
                     value = F.at(pos(SEMI56)).Skip(); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:442:1: variableDeclaration returns [JCStatement value] : VAR name typeReference ( EQ bindOpt expression | ) ;
    public final JCStatement variableDeclaration() throws RecognitionException {
        JCStatement value = null;

        Token VAR57=null;
        name_return name58 = null;

        JFXType typeReference59 = null;

        JCExpression expression60 = null;

        JavafxBindStatus bindOpt61 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:443:2: ( VAR name typeReference ( EQ bindOpt expression | ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:443:4: VAR name typeReference ( EQ bindOpt expression | )
            {
            VAR57=(Token)input.LT(1);
            match(input,VAR,FOLLOW_VAR_in_variableDeclaration3495); 
            pushFollow(FOLLOW_name_in_variableDeclaration3498);
            name58=name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_variableDeclaration3501);
            typeReference59=typeReference();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:444:6: ( EQ bindOpt expression | )
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==EQ) ) {
                alt29=1;
            }
            else if ( (LA29_0==SEMI) ) {
                alt29=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("444:6: ( EQ bindOpt expression | )", 29, 0, input);

                throw nvae;
            }
            switch (alt29) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:444:8: EQ bindOpt expression
                    {
                    match(input,EQ,FOLLOW_EQ_in_variableDeclaration3512); 
                    pushFollow(FOLLOW_bindOpt_in_variableDeclaration3514);
                    bindOpt61=bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_variableDeclaration3517);
                    expression60=expression();
                    _fsp--;

                     value = F.at(pos(VAR57)).VarInit(name58.value, typeReference59, 
                    	    							expression60, bindOpt61); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:446:13: 
                    {
                     value = F.at(pos(VAR57)).VarStatement(name58.value, typeReference59); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:449:1: bindOpt returns [JavafxBindStatus status = UNBOUND] : ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? ;
    public final JavafxBindStatus bindOpt() throws RecognitionException {
        JavafxBindStatus status =  UNBOUND;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:450:2: ( ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:450:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:450:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
            int alt33=4;
            switch ( input.LA(1) ) {
                case BIND:
                    {
                    alt33=1;
                    }
                    break;
                case STAYS:
                    {
                    alt33=2;
                    }
                    break;
                case TIE:
                    {
                    alt33=3;
                    }
                    break;
            }

            switch (alt33) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:450:6: BIND ( LAZY )?
                    {
                    match(input,BIND,FOLLOW_BIND_in_bindOpt3567); 
                     status = UNIDIBIND; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:451:8: ( LAZY )?
                    int alt30=2;
                    int LA30_0 = input.LA(1);

                    if ( (LA30_0==LAZY) ) {
                        alt30=1;
                    }
                    switch (alt30) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:451:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3583); 
                             status = LAZY_UNIDIBIND; 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:452:6: STAYS ( LAZY )?
                    {
                    match(input,STAYS,FOLLOW_STAYS_in_bindOpt3598); 
                     status = UNIDIBIND; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:453:8: ( LAZY )?
                    int alt31=2;
                    int LA31_0 = input.LA(1);

                    if ( (LA31_0==LAZY) ) {
                        alt31=1;
                    }
                    switch (alt31) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:453:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3614); 
                             status = LAZY_UNIDIBIND; 

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:454:6: TIE ( LAZY )?
                    {
                    match(input,TIE,FOLLOW_TIE_in_bindOpt3629); 
                     status = BIDIBIND; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:455:8: ( LAZY )?
                    int alt32=2;
                    int LA32_0 = input.LA(1);

                    if ( (LA32_0==LAZY) ) {
                        alt32=1;
                    }
                    switch (alt32) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:455:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3645); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:457:1: returnStatement returns [JCStatement value] : RETURN ( expression )? ;
    public final JCStatement returnStatement() throws RecognitionException {
        JCStatement value = null;

        Token RETURN63=null;
        JCExpression expression62 = null;


         JCExpression expr = null; 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:459:2: ( RETURN ( expression )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:459:4: RETURN ( expression )?
            {
            RETURN63=(Token)input.LT(1);
            match(input,RETURN,FOLLOW_RETURN_in_returnStatement3679); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:459:11: ( expression )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( ((LA34_0>=POUND && LA34_0<=TYPEOF)||LA34_0==IF||(LA34_0>=THIS && LA34_0<=FALSE)||(LA34_0>=NOT && LA34_0<=NEW)||LA34_0==SUPER||(LA34_0>=SIZEOF && LA34_0<=LPAREN)||LA34_0==LBRACKET||(LA34_0>=PLUSPLUS && LA34_0<=SUBSUB)||(LA34_0>=QUES && LA34_0<=STRING_LITERAL)||(LA34_0>=QUOTE_LBRACE_STRING_LITERAL && LA34_0<=LBRACE)||(LA34_0>=QUOTED_IDENTIFIER && LA34_0<=INTEGER_LITERAL)||LA34_0==FLOATING_POINT_LITERAL||LA34_0==IDENTIFIER) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:459:12: expression
                    {
                    pushFollow(FOLLOW_expression_in_returnStatement3682);
                    expression62=expression();
                    _fsp--;

                     expr = expression62; 

                    }
                    break;

            }

             value = F.at(pos(RETURN63)).Return(expr); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:462:1: tryStatement returns [JCStatement value] : TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? ) ;
    public final JCStatement tryStatement() throws RecognitionException {
        JCStatement value = null;

        Token TRY65=null;
        JCBlock tb = null;

        JCBlock fb1 = null;

        JCBlock fb2 = null;

        JCCatch catchClause64 = null;


        	JCBlock body;
        		ListBuffer<JCCatch> catchers = new ListBuffer<JCCatch>();
        		JCBlock finalBlock = null;
        	
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:467:2: ( TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:467:4: TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )
            {
            TRY65=(Token)input.LT(1);
            match(input,TRY,FOLLOW_TRY_in_tryStatement3725); 
            pushFollow(FOLLOW_block_in_tryStatement3731);
            tb=block();
            _fsp--;

             body = tb; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:468:5: ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==FINALLY) ) {
                alt37=1;
            }
            else if ( (LA37_0==CATCH) ) {
                alt37=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("468:5: ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )", 37, 0, input);

                throw nvae;
            }
            switch (alt37) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:468:7: FINALLY fb1= block
                    {
                    match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement3744); 
                    pushFollow(FOLLOW_block_in_tryStatement3750);
                    fb1=block();
                    _fsp--;

                     finalBlock = fb1; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:469:10: ( catchClause )+ ( FINALLY fb2= block )?
                    {
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:469:10: ( catchClause )+
                    int cnt35=0;
                    loop35:
                    do {
                        int alt35=2;
                        int LA35_0 = input.LA(1);

                        if ( (LA35_0==CATCH) ) {
                            alt35=1;
                        }


                        switch (alt35) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:469:11: catchClause
                    	    {
                    	    pushFollow(FOLLOW_catchClause_in_tryStatement3766);
                    	    catchClause64=catchClause();
                    	    _fsp--;

                    	     catchers.append(catchClause64); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt35 >= 1 ) break loop35;
                                EarlyExitException eee =
                                    new EarlyExitException(35, input);
                                throw eee;
                        }
                        cnt35++;
                    } while (true);

                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:470:10: ( FINALLY fb2= block )?
                    int alt36=2;
                    int LA36_0 = input.LA(1);

                    if ( (LA36_0==FINALLY) ) {
                        alt36=1;
                    }
                    switch (alt36) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:470:11: FINALLY fb2= block
                            {
                            match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement3789); 
                            pushFollow(FOLLOW_block_in_tryStatement3794);
                            fb2=block();
                            _fsp--;

                             finalBlock = fb2; 

                            }
                            break;

                    }


                    }
                    break;

            }

             value = F.at(pos(TRY65)).Try(body, catchers.toList(), finalBlock); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:473:1: catchClause returns [JCCatch value] : CATCH LPAREN name ( COLON identifier )? RPAREN block ;
    public final JCCatch catchClause() throws RecognitionException {
        JCCatch value = null;

        Token CATCH68=null;
        name_return name66 = null;

        JCIdent identifier67 = null;

        JCBlock block69 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:474:2: ( CATCH LPAREN name ( COLON identifier )? RPAREN block )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:474:4: CATCH LPAREN name ( COLON identifier )? RPAREN block
            {
            CATCH68=(Token)input.LT(1);
            match(input,CATCH,FOLLOW_CATCH_in_catchClause3833); 
            match(input,LPAREN,FOLLOW_LPAREN_in_catchClause3836); 
            pushFollow(FOLLOW_name_in_catchClause3839);
            name66=name();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:475:4: ( COLON identifier )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==COLON) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:475:5: COLON identifier
                    {
                    match(input,COLON,FOLLOW_COLON_in_catchClause3847); 
                    pushFollow(FOLLOW_identifier_in_catchClause3849);
                    identifier67=identifier();
                    _fsp--;


                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_catchClause3858); 
            pushFollow(FOLLOW_block_in_catchClause3862);
            block69=block();
            _fsp--;

             JCModifiers mods = F.at(name66.pos).Modifiers(Flags.PARAMETER);
            	  					  JCVariableDecl formal = F.at(name66.pos).VarDef(mods, name66.value, identifier67, null);
            	  					  value = F.at(pos(CATCH68)).Catch(formal, block69); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:480:1: expression returns [JCExpression expr] : ( ifExpression | suffixedExpression );
    public final JCExpression expression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression ifExpression70 = null;

        JCExpression suffixedExpression71 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:481:9: ( ifExpression | suffixedExpression )
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==IF) ) {
                alt39=1;
            }
            else if ( ((LA39_0>=POUND && LA39_0<=TYPEOF)||(LA39_0>=THIS && LA39_0<=FALSE)||(LA39_0>=NOT && LA39_0<=NEW)||LA39_0==SUPER||(LA39_0>=SIZEOF && LA39_0<=LPAREN)||LA39_0==LBRACKET||(LA39_0>=PLUSPLUS && LA39_0<=SUBSUB)||(LA39_0>=QUES && LA39_0<=STRING_LITERAL)||(LA39_0>=QUOTE_LBRACE_STRING_LITERAL && LA39_0<=LBRACE)||(LA39_0>=QUOTED_IDENTIFIER && LA39_0<=INTEGER_LITERAL)||LA39_0==FLOATING_POINT_LITERAL||LA39_0==IDENTIFIER) ) {
                alt39=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("480:1: expression returns [JCExpression expr] : ( ifExpression | suffixedExpression );", 39, 0, input);

                throw nvae;
            }
            switch (alt39) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:481:11: ifExpression
                    {
                    pushFollow(FOLLOW_ifExpression_in_expression3890);
                    ifExpression70=ifExpression();
                    _fsp--;

                     expr = ifExpression70; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:482:11: suffixedExpression
                    {
                    pushFollow(FOLLOW_suffixedExpression_in_expression3913);
                    suffixedExpression71=suffixedExpression();
                    _fsp--;

                     expr = suffixedExpression71; 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:485:1: ifExpression returns [JCExpression expr] : IF econd= expression THEN ethen= expression ( ELSE eelse= expression )? ;
    public final JCExpression ifExpression() throws RecognitionException {
        JCExpression expr = null;

        Token IF72=null;
        JCExpression econd = null;

        JCExpression ethen = null;

        JCExpression eelse = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:486:2: ( IF econd= expression THEN ethen= expression ( ELSE eelse= expression )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:486:4: IF econd= expression THEN ethen= expression ( ELSE eelse= expression )?
            {
            IF72=(Token)input.LT(1);
            match(input,IF,FOLLOW_IF_in_ifExpression3944); 
            pushFollow(FOLLOW_expression_in_ifExpression3948);
            econd=expression();
            _fsp--;

            match(input,THEN,FOLLOW_THEN_in_ifExpression3952); 
            pushFollow(FOLLOW_expression_in_ifExpression3957);
            ethen=expression();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:487:4: ( ELSE eelse= expression )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==ELSE) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:487:5: ELSE eelse= expression
                    {
                    match(input,ELSE,FOLLOW_ELSE_in_ifExpression3966); 
                    pushFollow(FOLLOW_expression_in_ifExpression3971);
                    eelse=expression();
                    _fsp--;


                    }
                    break;

            }

             JCExpression elsepart = eelse;
            	  							  if (elsepart == null) elsepart = F.at(pos(IF72)).Literal(TypeTags.BOT, null);
            	  							  expr = F.at(pos(IF72)).Conditional(econd, ethen, elsepart); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:491:1: suffixedExpression returns [JCExpression expr] : e1= assignmentExpression ( PLUSPLUS | SUBSUB )? ;
    public final JCExpression suffixedExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:492:2: (e1= assignmentExpression ( PLUSPLUS | SUBSUB )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:492:4: e1= assignmentExpression ( PLUSPLUS | SUBSUB )?
            {
            pushFollow(FOLLOW_assignmentExpression_in_suffixedExpression3996);
            e1=assignmentExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:493:5: ( PLUSPLUS | SUBSUB )?
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( (LA41_0==PLUSPLUS||LA41_0==SUBSUB) ) {
                alt41=1;
            }
            switch (alt41) {
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
                        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_suffixedExpression4007);    throw mse;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:495:1: assignmentExpression returns [JCExpression expr] : e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? ;
    public final JCExpression assignmentExpression() throws RecognitionException {
        JCExpression expr = null;

        Token EQ73=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:496:2: (e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:496:4: e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )?
            {
            pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression4036);
            e1=assignmentOpExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:497:5: ( EQ e2= assignmentOpExpression )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==EQ) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:497:9: EQ e2= assignmentOpExpression
                    {
                    EQ73=(Token)input.LT(1);
                    match(input,EQ,FOLLOW_EQ_in_assignmentExpression4051); 
                    pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression4057);
                    e2=assignmentOpExpression();
                    _fsp--;

                     expr = F.at(pos(EQ73)).Assign(expr, e2); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:498:1: assignmentOpExpression returns [JCExpression expr] : e1= andExpression ( assignmentOperator e2= andExpression )? ;
    public final JCExpression assignmentOpExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;

        JCExpression e2 = null;

        int assignmentOperator74 = 0;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:499:2: (e1= andExpression ( assignmentOperator e2= andExpression )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:499:4: e1= andExpression ( assignmentOperator e2= andExpression )?
            {
            pushFollow(FOLLOW_andExpression_in_assignmentOpExpression4084);
            e1=andExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:500:5: ( assignmentOperator e2= andExpression )?
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( ((LA43_0>=PLUSEQ && LA43_0<=PERCENTEQ)) ) {
                alt43=1;
            }
            switch (alt43) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:500:9: assignmentOperator e2= andExpression
                    {
                    pushFollow(FOLLOW_assignmentOperator_in_assignmentOpExpression4100);
                    assignmentOperator74=assignmentOperator();
                    _fsp--;

                    pushFollow(FOLLOW_andExpression_in_assignmentOpExpression4106);
                    e2=andExpression();
                    _fsp--;

                     expr = F.Assignop(assignmentOperator74,
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:502:1: andExpression returns [JCExpression expr] : e1= orExpression ( AND e2= orExpression )* ;
    public final JCExpression andExpression() throws RecognitionException {
        JCExpression expr = null;

        Token AND75=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:503:2: (e1= orExpression ( AND e2= orExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:503:4: e1= orExpression ( AND e2= orExpression )*
            {
            pushFollow(FOLLOW_orExpression_in_andExpression4132);
            e1=orExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:504:5: ( AND e2= orExpression )*
            loop44:
            do {
                int alt44=2;
                int LA44_0 = input.LA(1);

                if ( (LA44_0==AND) ) {
                    alt44=1;
                }


                switch (alt44) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:504:9: AND e2= orExpression
            	    {
            	    AND75=(Token)input.LT(1);
            	    match(input,AND,FOLLOW_AND_in_andExpression4148); 
            	    pushFollow(FOLLOW_orExpression_in_andExpression4154);
            	    e2=orExpression();
            	    _fsp--;

            	     expr = F.at(pos(AND75)).Binary(JCTree.AND, expr, e2); 

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
    // $ANTLR end andExpression


    // $ANTLR start orExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:505:1: orExpression returns [JCExpression expr] : e1= instanceOfExpression ( OR e2= instanceOfExpression )* ;
    public final JCExpression orExpression() throws RecognitionException {
        JCExpression expr = null;

        Token OR76=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:506:2: (e1= instanceOfExpression ( OR e2= instanceOfExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:506:4: e1= instanceOfExpression ( OR e2= instanceOfExpression )*
            {
            pushFollow(FOLLOW_instanceOfExpression_in_orExpression4182);
            e1=instanceOfExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:507:5: ( OR e2= instanceOfExpression )*
            loop45:
            do {
                int alt45=2;
                int LA45_0 = input.LA(1);

                if ( (LA45_0==OR) ) {
                    alt45=1;
                }


                switch (alt45) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:507:9: OR e2= instanceOfExpression
            	    {
            	    OR76=(Token)input.LT(1);
            	    match(input,OR,FOLLOW_OR_in_orExpression4197); 
            	    pushFollow(FOLLOW_instanceOfExpression_in_orExpression4203);
            	    e2=instanceOfExpression();
            	    _fsp--;

            	     expr = F.at(pos(OR76)).Binary(JCTree.OR, expr, e2); 

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
    // $ANTLR end orExpression


    // $ANTLR start instanceOfExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:508:1: instanceOfExpression returns [JCExpression expr] : e1= relationalExpression ( INSTANCEOF identifier )? ;
    public final JCExpression instanceOfExpression() throws RecognitionException {
        JCExpression expr = null;

        Token INSTANCEOF77=null;
        JCExpression e1 = null;

        JCIdent identifier78 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:509:2: (e1= relationalExpression ( INSTANCEOF identifier )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:509:4: e1= relationalExpression ( INSTANCEOF identifier )?
            {
            pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression4231);
            e1=relationalExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:510:5: ( INSTANCEOF identifier )?
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==INSTANCEOF) ) {
                alt46=1;
            }
            switch (alt46) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:510:9: INSTANCEOF identifier
                    {
                    INSTANCEOF77=(Token)input.LT(1);
                    match(input,INSTANCEOF,FOLLOW_INSTANCEOF_in_instanceOfExpression4246); 
                    pushFollow(FOLLOW_identifier_in_instanceOfExpression4248);
                    identifier78=identifier();
                    _fsp--;

                     expr = F.at(pos(INSTANCEOF77)).Binary(JCTree.TYPETEST, expr, 
                    	   													 identifier78); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:512:1: relationalExpression returns [JCExpression expr] : e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* ;
    public final JCExpression relationalExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LTGT79=null;
        Token EQEQ80=null;
        Token LTEQ81=null;
        Token GTEQ82=null;
        Token LT83=null;
        Token GT84=null;
        Token IN85=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:513:2: (e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:513:4: e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            {
            pushFollow(FOLLOW_additiveExpression_in_relationalExpression4276);
            e1=additiveExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:514:5: ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            loop47:
            do {
                int alt47=8;
                switch ( input.LA(1) ) {
                case LTGT:
                    {
                    alt47=1;
                    }
                    break;
                case EQEQ:
                    {
                    alt47=2;
                    }
                    break;
                case LTEQ:
                    {
                    alt47=3;
                    }
                    break;
                case GTEQ:
                    {
                    alt47=4;
                    }
                    break;
                case LT:
                    {
                    alt47=5;
                    }
                    break;
                case GT:
                    {
                    alt47=6;
                    }
                    break;
                case IN:
                    {
                    alt47=7;
                    }
                    break;

                }

                switch (alt47) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:514:9: LTGT e= additiveExpression
            	    {
            	    LTGT79=(Token)input.LT(1);
            	    match(input,LTGT,FOLLOW_LTGT_in_relationalExpression4292); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4298);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LTGT79)).Binary(JCTree.NE, expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:515:9: EQEQ e= additiveExpression
            	    {
            	    EQEQ80=(Token)input.LT(1);
            	    match(input,EQEQ,FOLLOW_EQEQ_in_relationalExpression4312); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4318);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(EQEQ80)).Binary(JCTree.EQ, expr, e); 

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:516:9: LTEQ e= additiveExpression
            	    {
            	    LTEQ81=(Token)input.LT(1);
            	    match(input,LTEQ,FOLLOW_LTEQ_in_relationalExpression4332); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4338);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LTEQ81)).Binary(JCTree.LE, expr, e); 

            	    }
            	    break;
            	case 4 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:517:9: GTEQ e= additiveExpression
            	    {
            	    GTEQ82=(Token)input.LT(1);
            	    match(input,GTEQ,FOLLOW_GTEQ_in_relationalExpression4352); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4358);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(GTEQ82)).Binary(JCTree.GE, expr, e); 

            	    }
            	    break;
            	case 5 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:518:9: LT e= additiveExpression
            	    {
            	    LT83=(Token)input.LT(1);
            	    match(input,LT,FOLLOW_LT_in_relationalExpression4372); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4380);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LT83))  .Binary(JCTree.LT, expr, e); 

            	    }
            	    break;
            	case 6 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:519:9: GT e= additiveExpression
            	    {
            	    GT84=(Token)input.LT(1);
            	    match(input,GT,FOLLOW_GT_in_relationalExpression4394); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4402);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(GT84))  .Binary(JCTree.GT, expr, e); 

            	    }
            	    break;
            	case 7 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:520:9: IN e= additiveExpression
            	    {
            	    IN85=(Token)input.LT(1);
            	    match(input,IN,FOLLOW_IN_in_relationalExpression4416); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4424);
            	    e=additiveExpression();
            	    _fsp--;

            	     /* expr = F.at(pos(IN85  )).Binary(JavaFXTag.IN, expr, $e2.expr); */ 

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
    // $ANTLR end relationalExpression


    // $ANTLR start additiveExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:522:1: additiveExpression returns [JCExpression expr] : e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* ;
    public final JCExpression additiveExpression() throws RecognitionException {
        JCExpression expr = null;

        Token PLUS86=null;
        Token SUB87=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:523:2: (e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:523:4: e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            {
            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4453);
            e1=multiplicativeExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:524:5: ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            loop48:
            do {
                int alt48=3;
                int LA48_0 = input.LA(1);

                if ( (LA48_0==PLUS) ) {
                    alt48=1;
                }
                else if ( (LA48_0==SUB) ) {
                    alt48=2;
                }


                switch (alt48) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:524:9: PLUS e= multiplicativeExpression
            	    {
            	    PLUS86=(Token)input.LT(1);
            	    match(input,PLUS,FOLLOW_PLUS_in_additiveExpression4468); 
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4474);
            	    e=multiplicativeExpression();
            	    _fsp--;

            	     expr = F.at(pos(PLUS86)).Binary(JCTree.PLUS , expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:525:9: SUB e= multiplicativeExpression
            	    {
            	    SUB87=(Token)input.LT(1);
            	    match(input,SUB,FOLLOW_SUB_in_additiveExpression4487); 
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4494);
            	    e=multiplicativeExpression();
            	    _fsp--;

            	     expr = F.at(pos(SUB87)) .Binary(JCTree.MINUS, expr, e); 

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
    // $ANTLR end additiveExpression


    // $ANTLR start multiplicativeExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:527:1: multiplicativeExpression returns [JCExpression expr] : e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* ;
    public final JCExpression multiplicativeExpression() throws RecognitionException {
        JCExpression expr = null;

        Token STAR88=null;
        Token SLASH89=null;
        Token PERCENT90=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:528:2: (e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:528:4: e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            {
            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4522);
            e1=unaryExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:529:5: ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            loop49:
            do {
                int alt49=4;
                switch ( input.LA(1) ) {
                case STAR:
                    {
                    alt49=1;
                    }
                    break;
                case SLASH:
                    {
                    alt49=2;
                    }
                    break;
                case PERCENT:
                    {
                    alt49=3;
                    }
                    break;

                }

                switch (alt49) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:529:9: STAR e= unaryExpression
            	    {
            	    STAR88=(Token)input.LT(1);
            	    match(input,STAR,FOLLOW_STAR_in_multiplicativeExpression4538); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4545);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(STAR88))   .Binary(JCTree.MUL  , expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:530:9: SLASH e= unaryExpression
            	    {
            	    SLASH89=(Token)input.LT(1);
            	    match(input,SLASH,FOLLOW_SLASH_in_multiplicativeExpression4559); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4565);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(SLASH89))  .Binary(JCTree.DIV  , expr, e); 

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:531:9: PERCENT e= unaryExpression
            	    {
            	    PERCENT90=(Token)input.LT(1);
            	    match(input,PERCENT,FOLLOW_PERCENT_in_multiplicativeExpression4579); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4583);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(PERCENT90)).Binary(JCTree.MOD  , expr, e); 

            	    }
            	    break;

            	default :
            	    break loop49;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:533:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );
    public final JCExpression unaryExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression postfixExpression91 = null;

        int unaryOperator92 = 0;

        JCExpression postfixExpression93 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:534:2: ( postfixExpression | unaryOperator postfixExpression )
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( ((LA50_0>=THIS && LA50_0<=FALSE)||LA50_0==NEW||LA50_0==SUPER||LA50_0==LPAREN||LA50_0==LBRACKET||LA50_0==STRING_LITERAL||(LA50_0>=QUOTE_LBRACE_STRING_LITERAL && LA50_0<=LBRACE)||(LA50_0>=QUOTED_IDENTIFIER && LA50_0<=INTEGER_LITERAL)||LA50_0==FLOATING_POINT_LITERAL||LA50_0==IDENTIFIER) ) {
                alt50=1;
            }
            else if ( ((LA50_0>=POUND && LA50_0<=TYPEOF)||LA50_0==NOT||(LA50_0>=SIZEOF && LA50_0<=REVERSE)||(LA50_0>=PLUSPLUS && LA50_0<=SUBSUB)||LA50_0==QUES) ) {
                alt50=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("533:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );", 50, 0, input);

                throw nvae;
            }
            switch (alt50) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:534:4: postfixExpression
                    {
                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression4613);
                    postfixExpression91=postfixExpression();
                    _fsp--;

                     expr = postfixExpression91; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:535:4: unaryOperator postfixExpression
                    {
                    pushFollow(FOLLOW_unaryOperator_in_unaryExpression4624);
                    unaryOperator92=unaryOperator();
                    _fsp--;

                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression4628);
                    postfixExpression93=postfixExpression();
                    _fsp--;

                     expr = F.Unary(unaryOperator92, postfixExpression93); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:537:1: postfixExpression returns [JCExpression expr] : primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* ;
    public final JCExpression postfixExpression() throws RecognitionException {
        JCExpression expr = null;

        Token DOT95=null;
        Token LPAREN96=null;
        name_return name1 = null;

        JCExpression primaryExpression94 = null;

        ListBuffer<JCExpression> expressionListOpt97 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:538:2: ( primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:538:4: primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            {
            pushFollow(FOLLOW_primaryExpression_in_postfixExpression4648);
            primaryExpression94=primaryExpression();
            _fsp--;

             expr = primaryExpression94; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:539:5: ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            loop54:
            do {
                int alt54=3;
                int LA54_0 = input.LA(1);

                if ( (LA54_0==DOT) ) {
                    alt54=1;
                }
                else if ( (LA54_0==LBRACKET) ) {
                    alt54=2;
                }


                switch (alt54) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:539:7: DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    {
            	    DOT95=(Token)input.LT(1);
            	    match(input,DOT,FOLLOW_DOT_in_postfixExpression4663); 
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:539:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    int alt52=2;
            	    int LA52_0 = input.LA(1);

            	    if ( (LA52_0==CLASS) ) {
            	        alt52=1;
            	    }
            	    else if ( (LA52_0==QUOTED_IDENTIFIER||LA52_0==IDENTIFIER) ) {
            	        alt52=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("539:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )", 52, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt52) {
            	        case 1 :
            	            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:539:13: CLASS
            	            {
            	            match(input,CLASS,FOLLOW_CLASS_in_postfixExpression4667); 

            	            }
            	            break;
            	        case 2 :
            	            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:540:13: name1= name ( LPAREN expressionListOpt RPAREN )*
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression4691);
            	            name1=name();
            	            _fsp--;

            	             expr = F.at(pos(DOT95)).Select(expr, name1.value); 
            	            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:541:14: ( LPAREN expressionListOpt RPAREN )*
            	            loop51:
            	            do {
            	                int alt51=2;
            	                int LA51_0 = input.LA(1);

            	                if ( (LA51_0==LPAREN) ) {
            	                    alt51=1;
            	                }


            	                switch (alt51) {
            	            	case 1 :
            	            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:541:16: LPAREN expressionListOpt RPAREN
            	            	    {
            	            	    LPAREN96=(Token)input.LT(1);
            	            	    match(input,LPAREN,FOLLOW_LPAREN_in_postfixExpression4716); 
            	            	    pushFollow(FOLLOW_expressionListOpt_in_postfixExpression4718);
            	            	    expressionListOpt97=expressionListOpt();
            	            	    _fsp--;

            	            	    match(input,RPAREN,FOLLOW_RPAREN_in_postfixExpression4720); 
            	            	     expr = F.at(pos(LPAREN96)).Apply(null, expr, expressionListOpt97.toList()); 

            	            	    }
            	            	    break;

            	            	default :
            	            	    break loop51;
            	                }
            	            } while (true);


            	            }
            	            break;

            	    }


            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:543:7: LBRACKET ( name BAR )? expression RBRACKET
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_postfixExpression4752); 
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:543:16: ( name BAR )?
            	    int alt53=2;
            	    int LA53_0 = input.LA(1);

            	    if ( (LA53_0==QUOTED_IDENTIFIER||LA53_0==IDENTIFIER) ) {
            	        int LA53_1 = input.LA(2);

            	        if ( (LA53_1==BAR) ) {
            	            alt53=1;
            	        }
            	    }
            	    switch (alt53) {
            	        case 1 :
            	            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:543:17: name BAR
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression4755);
            	            name();
            	            _fsp--;

            	            match(input,BAR,FOLLOW_BAR_in_postfixExpression4757); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_expression_in_postfixExpression4761);
            	    expression();
            	    _fsp--;

            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_postfixExpression4764); 

            	    }
            	    break;

            	default :
            	    break loop54;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:545:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | bracketExpression | literal | blockExpression | LPAREN expression RPAREN );
    public final JCExpression primaryExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LBRACE99=null;
        Token THIS102=null;
        Token SUPER103=null;
        Token LPAREN105=null;
        Token LPAREN111=null;
        JCExpression newExpression98 = null;

        JCIdent identifier100 = null;

        ListBuffer<JFXStatement> objectLiteral101 = null;

        JCIdent identifier104 = null;

        ListBuffer<JCExpression> expressionListOpt106 = null;

        JCExpression stringExpression107 = null;

        JFXAbstractSequenceCreator bracketExpression108 = null;

        JCExpression literal109 = null;

        JFXBlockExpression blockExpression110 = null;

        JCExpression expression112 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:546:2: ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | bracketExpression | literal | blockExpression | LPAREN expression RPAREN )
            int alt56=10;
            switch ( input.LA(1) ) {
            case NEW:
                {
                alt56=1;
                }
                break;
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                int LA56_2 = input.LA(2);

                if ( (LA56_2==LBRACE) ) {
                    alt56=2;
                }
                else if ( ((LA56_2>=DOTDOT && LA56_2<=ABSTRACT)||LA56_2==AND||LA56_2==ATTRIBUTE||LA56_2==ON||(LA56_2>=THEN && LA56_2<=ELSE)||LA56_2==IN||(LA56_2>=PRIVATE && LA56_2<=READONLY)||LA56_2==INSTANCEOF||LA56_2==OR||(LA56_2>=LPAREN && LA56_2<=PERCENTEQ)||(LA56_2>=RBRACE_QUOTE_STRING_LITERAL && LA56_2<=RBRACE)||LA56_2==QUOTED_IDENTIFIER||LA56_2==IDENTIFIER) ) {
                    alt56=5;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("545:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | bracketExpression | literal | blockExpression | LPAREN expression RPAREN );", 56, 2, input);

                    throw nvae;
                }
                }
                break;
            case THIS:
                {
                alt56=3;
                }
                break;
            case SUPER:
                {
                alt56=4;
                }
                break;
            case QUOTE_LBRACE_STRING_LITERAL:
                {
                alt56=6;
                }
                break;
            case LBRACKET:
                {
                alt56=7;
                }
                break;
            case NULL:
            case TRUE:
            case FALSE:
            case STRING_LITERAL:
            case INTEGER_LITERAL:
            case FLOATING_POINT_LITERAL:
                {
                alt56=8;
                }
                break;
            case LBRACE:
                {
                alt56=9;
                }
                break;
            case LPAREN:
                {
                alt56=10;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("545:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | bracketExpression | literal | blockExpression | LPAREN expression RPAREN );", 56, 0, input);

                throw nvae;
            }

            switch (alt56) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:546:4: newExpression
                    {
                    pushFollow(FOLLOW_newExpression_in_primaryExpression4789);
                    newExpression98=newExpression();
                    _fsp--;

                     expr = newExpression98; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:547:4: identifier LBRACE objectLiteral RBRACE
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression4801);
                    identifier100=identifier();
                    _fsp--;

                    LBRACE99=(Token)input.LT(1);
                    match(input,LBRACE,FOLLOW_LBRACE_in_primaryExpression4803); 
                    pushFollow(FOLLOW_objectLiteral_in_primaryExpression4806);
                    objectLiteral101=objectLiteral();
                    _fsp--;

                    match(input,RBRACE,FOLLOW_RBRACE_in_primaryExpression4808); 
                     expr = F.at(pos(LBRACE99)).PureObjectLiteral(identifier100, objectLiteral101.toList()); 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:548:11: THIS
                    {
                    THIS102=(Token)input.LT(1);
                    match(input,THIS,FOLLOW_THIS_in_primaryExpression4825); 
                     expr = F.at(pos(THIS102)).Identifier(names._this); 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:549:11: SUPER
                    {
                    SUPER103=(Token)input.LT(1);
                    match(input,SUPER,FOLLOW_SUPER_in_primaryExpression4845); 
                     expr = F.at(pos(SUPER103)).Identifier(names._super); 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:550:11: identifier ( LPAREN expressionListOpt RPAREN )*
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression4865);
                    identifier104=identifier();
                    _fsp--;

                     expr = identifier104; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:551:10: ( LPAREN expressionListOpt RPAREN )*
                    loop55:
                    do {
                        int alt55=2;
                        int LA55_0 = input.LA(1);

                        if ( (LA55_0==LPAREN) ) {
                            alt55=1;
                        }


                        switch (alt55) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:551:12: LPAREN expressionListOpt RPAREN
                    	    {
                    	    LPAREN105=(Token)input.LT(1);
                    	    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression4886); 
                    	    pushFollow(FOLLOW_expressionListOpt_in_primaryExpression4890);
                    	    expressionListOpt106=expressionListOpt();
                    	    _fsp--;

                    	    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression4894); 
                    	     expr = F.at(pos(LPAREN105)).Apply(null, expr, expressionListOpt106.toList()); 

                    	    }
                    	    break;

                    	default :
                    	    break loop55;
                        }
                    } while (true);


                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:552:11: stringExpression
                    {
                    pushFollow(FOLLOW_stringExpression_in_primaryExpression4914);
                    stringExpression107=stringExpression();
                    _fsp--;

                     expr = stringExpression107; 

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:553:11: bracketExpression
                    {
                    pushFollow(FOLLOW_bracketExpression_in_primaryExpression4933);
                    bracketExpression108=bracketExpression();
                    _fsp--;

                     expr = bracketExpression108; 

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:554:11: literal
                    {
                    pushFollow(FOLLOW_literal_in_primaryExpression4952);
                    literal109=literal();
                    _fsp--;

                     expr = literal109; 

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:555:11: blockExpression
                    {
                    pushFollow(FOLLOW_blockExpression_in_primaryExpression4972);
                    blockExpression110=blockExpression();
                    _fsp--;

                     expr = blockExpression110; 

                    }
                    break;
                case 10 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:556:11: LPAREN expression RPAREN
                    {
                    LPAREN111=(Token)input.LT(1);
                    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression4990); 
                    pushFollow(FOLLOW_expression_in_primaryExpression4992);
                    expression112=expression();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression4994); 
                     expr = F.at(pos(LPAREN111)).Parens(expression112); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:558:1: newExpression returns [JCExpression expr] : NEW qualident ( LPAREN expressionListOpt RPAREN )? ;
    public final JCExpression newExpression() throws RecognitionException {
        JCExpression expr = null;

        Token NEW114=null;
        ListBuffer<JCExpression> expressionListOpt113 = null;

        JCExpression qualident115 = null;


         ListBuffer<JCExpression> args = null; 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:560:2: ( NEW qualident ( LPAREN expressionListOpt RPAREN )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:560:4: NEW qualident ( LPAREN expressionListOpt RPAREN )?
            {
            NEW114=(Token)input.LT(1);
            match(input,NEW,FOLLOW_NEW_in_newExpression5027); 
            pushFollow(FOLLOW_qualident_in_newExpression5030);
            qualident115=qualident();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:561:3: ( LPAREN expressionListOpt RPAREN )?
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( (LA57_0==LPAREN) ) {
                alt57=1;
            }
            switch (alt57) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:561:5: LPAREN expressionListOpt RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_newExpression5038); 
                    pushFollow(FOLLOW_expressionListOpt_in_newExpression5042);
                    expressionListOpt113=expressionListOpt();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_newExpression5046); 
                     args = expressionListOpt113; 

                    }
                    break;

            }

             expr = F.at(pos(NEW114)).NewClass(null, null, qualident115, 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:566:1: objectLiteral returns [ListBuffer<JFXStatement> parts = new ListBuffer<JFXStatement>()] : ( objectLiteralPart )* ;
    public final ListBuffer<JFXStatement> objectLiteral() throws RecognitionException {
        ListBuffer<JFXStatement> parts =  new ListBuffer<JFXStatement>();

        JFXStatement objectLiteralPart116 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:567:2: ( ( objectLiteralPart )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:567:4: ( objectLiteralPart )*
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:567:4: ( objectLiteralPart )*
            loop58:
            do {
                int alt58=2;
                int LA58_0 = input.LA(1);

                if ( (LA58_0==ABSTRACT||LA58_0==ATTRIBUTE||(LA58_0>=PRIVATE && LA58_0<=READONLY)||LA58_0==QUOTED_IDENTIFIER||LA58_0==IDENTIFIER) ) {
                    alt58=1;
                }


                switch (alt58) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:567:6: objectLiteralPart
            	    {
            	    pushFollow(FOLLOW_objectLiteralPart_in_objectLiteral5086);
            	    objectLiteralPart116=objectLiteralPart();
            	    _fsp--;

            	     parts.append(objectLiteralPart116); 

            	    }
            	    break;

            	default :
            	    break loop58;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );
    public final JFXStatement objectLiteralPart() throws RecognitionException {
        JFXStatement value = null;

        Token COLON117=null;
        name_return name118 = null;

        JCExpression expression119 = null;

        JavafxBindStatus bindOpt120 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:570:2: ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition )
            int alt60=3;
            switch ( input.LA(1) ) {
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                alt60=1;
                }
                break;
            case ABSTRACT:
                {
                switch ( input.LA(2) ) {
                case PUBLIC:
                    {
                    int LA60_9 = input.LA(3);

                    if ( (LA60_9==FUNCTION) ) {
                        alt60=3;
                    }
                    else if ( (LA60_9==ATTRIBUTE) ) {
                        alt60=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 9, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA60_10 = input.LA(3);

                    if ( (LA60_10==FUNCTION) ) {
                        alt60=3;
                    }
                    else if ( (LA60_10==ATTRIBUTE) ) {
                        alt60=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA60_11 = input.LA(3);

                    if ( (LA60_11==FUNCTION) ) {
                        alt60=3;
                    }
                    else if ( (LA60_11==ATTRIBUTE) ) {
                        alt60=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt60=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt60=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 2, input);

                    throw nvae;
                }

                }
                break;
            case READONLY:
                {
                switch ( input.LA(2) ) {
                case PUBLIC:
                    {
                    int LA60_9 = input.LA(3);

                    if ( (LA60_9==FUNCTION) ) {
                        alt60=3;
                    }
                    else if ( (LA60_9==ATTRIBUTE) ) {
                        alt60=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 9, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA60_10 = input.LA(3);

                    if ( (LA60_10==FUNCTION) ) {
                        alt60=3;
                    }
                    else if ( (LA60_10==ATTRIBUTE) ) {
                        alt60=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA60_11 = input.LA(3);

                    if ( (LA60_11==FUNCTION) ) {
                        alt60=3;
                    }
                    else if ( (LA60_11==ATTRIBUTE) ) {
                        alt60=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt60=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt60=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 3, input);

                    throw nvae;
                }

                }
                break;
            case PUBLIC:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA60_12 = input.LA(3);

                    if ( (LA60_12==FUNCTION) ) {
                        alt60=3;
                    }
                    else if ( (LA60_12==ATTRIBUTE) ) {
                        alt60=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA60_13 = input.LA(3);

                    if ( (LA60_13==FUNCTION) ) {
                        alt60=3;
                    }
                    else if ( (LA60_13==ATTRIBUTE) ) {
                        alt60=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case FUNCTION:
                    {
                    alt60=3;
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt60=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 4, input);

                    throw nvae;
                }

                }
                break;
            case PRIVATE:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA60_12 = input.LA(3);

                    if ( (LA60_12==FUNCTION) ) {
                        alt60=3;
                    }
                    else if ( (LA60_12==ATTRIBUTE) ) {
                        alt60=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA60_13 = input.LA(3);

                    if ( (LA60_13==FUNCTION) ) {
                        alt60=3;
                    }
                    else if ( (LA60_13==ATTRIBUTE) ) {
                        alt60=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt60=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt60=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 5, input);

                    throw nvae;
                }

                }
                break;
            case PROTECTED:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA60_12 = input.LA(3);

                    if ( (LA60_12==FUNCTION) ) {
                        alt60=3;
                    }
                    else if ( (LA60_12==ATTRIBUTE) ) {
                        alt60=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA60_13 = input.LA(3);

                    if ( (LA60_13==FUNCTION) ) {
                        alt60=3;
                    }
                    else if ( (LA60_13==ATTRIBUTE) ) {
                        alt60=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt60=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt60=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 6, input);

                    throw nvae;
                }

                }
                break;
            case ATTRIBUTE:
                {
                alt60=2;
                }
                break;
            case FUNCTION:
                {
                alt60=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("569:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 60, 0, input);

                throw nvae;
            }

            switch (alt60) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:570:4: name COLON bindOpt expression ( COMMA | SEMI )?
                    {
                    pushFollow(FOLLOW_name_in_objectLiteralPart5114);
                    name118=name();
                    _fsp--;

                    COLON117=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_objectLiteralPart5116); 
                    pushFollow(FOLLOW_bindOpt_in_objectLiteralPart5119);
                    bindOpt120=bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_objectLiteralPart5121);
                    expression119=expression();
                    _fsp--;

                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:570:35: ( COMMA | SEMI )?
                    int alt59=2;
                    int LA59_0 = input.LA(1);

                    if ( ((LA59_0>=SEMI && LA59_0<=COMMA)) ) {
                        alt59=1;
                    }
                    switch (alt59) {
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
                                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_objectLiteralPart5123);    throw mse;
                            }


                            }
                            break;

                    }

                     value = F.at(pos(COLON117)).ObjectLiteralPart(name118.value, expression119, bindOpt120); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:571:11: attributeDefinition
                    {
                    pushFollow(FOLLOW_attributeDefinition_in_objectLiteralPart5144);
                    attributeDefinition();
                    _fsp--;


                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:572:11: functionDefinition
                    {
                    pushFollow(FOLLOW_functionDefinition_in_objectLiteralPart5156);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:574:1: stringExpression returns [JCExpression expr] : ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL ;
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
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:576:2: (ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:576:4: ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL
            {
            ql=(Token)input.LT(1);
            match(input,QUOTE_LBRACE_STRING_LITERAL,FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression5187); 
             strexp.append(F.at(pos(ql)).Literal(TypeTags.CLASS, ql.getText())); 
            pushFollow(FOLLOW_formatOrNull_in_stringExpression5196);
            f1=formatOrNull();
            _fsp--;

             strexp.append(f1); 
            pushFollow(FOLLOW_expression_in_stringExpression5207);
            e1=expression();
            _fsp--;

             strexp.append(e1); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:579:4: (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )*
            loop61:
            do {
                int alt61=2;
                int LA61_0 = input.LA(1);

                if ( (LA61_0==RBRACE_LBRACE_STRING_LITERAL) ) {
                    alt61=1;
                }


                switch (alt61) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:579:7: rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression
            	    {
            	    rl=(Token)input.LT(1);
            	    match(input,RBRACE_LBRACE_STRING_LITERAL,FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression5222); 
            	     strexp.append(F.at(pos(rl)).Literal(TypeTags.CLASS, rl.getText())); 
            	    pushFollow(FOLLOW_formatOrNull_in_stringExpression5234);
            	    fn=formatOrNull();
            	    _fsp--;

            	     strexp.append(fn); 
            	    pushFollow(FOLLOW_expression_in_stringExpression5248);
            	    en=expression();
            	    _fsp--;

            	     strexp.append(en); 

            	    }
            	    break;

            	default :
            	    break loop61;
                }
            } while (true);

            rq=(Token)input.LT(1);
            match(input,RBRACE_QUOTE_STRING_LITERAL,FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression5269); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:586:1: formatOrNull returns [JCExpression expr] : (fs= FORMAT_STRING_LITERAL | );
    public final JCExpression formatOrNull() throws RecognitionException {
        JCExpression expr = null;

        Token fs=null;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:587:2: (fs= FORMAT_STRING_LITERAL | )
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==FORMAT_STRING_LITERAL) ) {
                alt62=1;
            }
            else if ( ((LA62_0>=POUND && LA62_0<=TYPEOF)||LA62_0==IF||(LA62_0>=THIS && LA62_0<=FALSE)||(LA62_0>=NOT && LA62_0<=NEW)||LA62_0==SUPER||(LA62_0>=SIZEOF && LA62_0<=LPAREN)||LA62_0==LBRACKET||(LA62_0>=PLUSPLUS && LA62_0<=SUBSUB)||(LA62_0>=QUES && LA62_0<=STRING_LITERAL)||(LA62_0>=QUOTE_LBRACE_STRING_LITERAL && LA62_0<=LBRACE)||(LA62_0>=QUOTED_IDENTIFIER && LA62_0<=INTEGER_LITERAL)||LA62_0==FLOATING_POINT_LITERAL||LA62_0==IDENTIFIER) ) {
                alt62=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("586:1: formatOrNull returns [JCExpression expr] : (fs= FORMAT_STRING_LITERAL | );", 62, 0, input);

                throw nvae;
            }
            switch (alt62) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:587:4: fs= FORMAT_STRING_LITERAL
                    {
                    fs=(Token)input.LT(1);
                    match(input,FORMAT_STRING_LITERAL,FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull5299); 
                     expr = F.at(pos(fs)).Literal(TypeTags.CLASS, fs.getText()); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:588:22: 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:590:1: bracketExpression returns [JFXAbstractSequenceCreator expr] : LBRACKET ( | e1= expression ( | COMMA e2= expression ( ( COMMA en= expression )* ) | DOTDOT dd= expression ) ) RBRACKET ;
    public final JFXAbstractSequenceCreator bracketExpression() throws RecognitionException {
        JFXAbstractSequenceCreator expr = null;

        Token LBRACKET121=null;
        JCExpression e1 = null;

        JCExpression e2 = null;

        JCExpression en = null;

        JCExpression dd = null;


         ListBuffer<JCExpression> exps = new ListBuffer<JCExpression>(); 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:592:2: ( LBRACKET ( | e1= expression ( | COMMA e2= expression ( ( COMMA en= expression )* ) | DOTDOT dd= expression ) ) RBRACKET )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:592:4: LBRACKET ( | e1= expression ( | COMMA e2= expression ( ( COMMA en= expression )* ) | DOTDOT dd= expression ) ) RBRACKET
            {
            LBRACKET121=(Token)input.LT(1);
            match(input,LBRACKET,FOLLOW_LBRACKET_in_bracketExpression5332); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:593:6: ( | e1= expression ( | COMMA e2= expression ( ( COMMA en= expression )* ) | DOTDOT dd= expression ) )
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( (LA65_0==RBRACKET) ) {
                alt65=1;
            }
            else if ( ((LA65_0>=POUND && LA65_0<=TYPEOF)||LA65_0==IF||(LA65_0>=THIS && LA65_0<=FALSE)||(LA65_0>=NOT && LA65_0<=NEW)||LA65_0==SUPER||(LA65_0>=SIZEOF && LA65_0<=LPAREN)||LA65_0==LBRACKET||(LA65_0>=PLUSPLUS && LA65_0<=SUBSUB)||(LA65_0>=QUES && LA65_0<=STRING_LITERAL)||(LA65_0>=QUOTE_LBRACE_STRING_LITERAL && LA65_0<=LBRACE)||(LA65_0>=QUOTED_IDENTIFIER && LA65_0<=INTEGER_LITERAL)||LA65_0==FLOATING_POINT_LITERAL||LA65_0==IDENTIFIER) ) {
                alt65=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("593:6: ( | e1= expression ( | COMMA e2= expression ( ( COMMA en= expression )* ) | DOTDOT dd= expression ) )", 65, 0, input);

                throw nvae;
            }
            switch (alt65) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:593:20: 
                    {
                     expr = F.at(pos(LBRACKET121)).EmptySequence(); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:594:8: e1= expression ( | COMMA e2= expression ( ( COMMA en= expression )* ) | DOTDOT dd= expression )
                    {
                    pushFollow(FOLLOW_expression_in_bracketExpression5360);
                    e1=expression();
                    _fsp--;

                     exps.append(e1); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:595:8: ( | COMMA e2= expression ( ( COMMA en= expression )* ) | DOTDOT dd= expression )
                    int alt64=3;
                    switch ( input.LA(1) ) {
                    case RBRACKET:
                        {
                        alt64=1;
                        }
                        break;
                    case COMMA:
                        {
                        alt64=2;
                        }
                        break;
                    case DOTDOT:
                        {
                        alt64=3;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("595:8: ( | COMMA e2= expression ( ( COMMA en= expression )* ) | DOTDOT dd= expression )", 64, 0, input);

                        throw nvae;
                    }

                    switch (alt64) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:595:23: 
                            {
                             expr = F.at(pos(LBRACKET121)).ExplicitSequence(exps.toList()); 

                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:596:10: COMMA e2= expression ( ( COMMA en= expression )* )
                            {
                            match(input,COMMA,FOLLOW_COMMA_in_bracketExpression5393); 
                            pushFollow(FOLLOW_expression_in_bracketExpression5397);
                            e2=expression();
                            _fsp--;

                             exps.append(e2); 
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:597:12: ( ( COMMA en= expression )* )
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:600:14: ( COMMA en= expression )*
                            {
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:600:14: ( COMMA en= expression )*
                            loop63:
                            do {
                                int alt63=2;
                                int LA63_0 = input.LA(1);

                                if ( (LA63_0==COMMA) ) {
                                    alt63=1;
                                }


                                switch (alt63) {
                            	case 1 :
                            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:600:15: COMMA en= expression
                            	    {
                            	    match(input,COMMA,FOLLOW_COMMA_in_bracketExpression5432); 
                            	    pushFollow(FOLLOW_expression_in_bracketExpression5437);
                            	    en=expression();
                            	    _fsp--;

                            	     exps.append(en); 

                            	    }
                            	    break;

                            	default :
                            	    break loop63;
                                }
                            } while (true);

                             expr = F.at(pos(LBRACKET121)).ExplicitSequence(exps.toList()); 

                            }


                            }
                            break;
                        case 3 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:603:10: DOTDOT dd= expression
                            {
                            match(input,DOTDOT,FOLLOW_DOTDOT_in_bracketExpression5483); 
                            pushFollow(FOLLOW_expression_in_bracketExpression5489);
                            dd=expression();
                            _fsp--;

                             expr = F.at(pos(LBRACKET121)).RangeSequence(e1, dd); 

                            }
                            break;

                    }


                    }
                    break;

            }

            match(input,RBRACKET,FOLLOW_RBRACKET_in_bracketExpression5515); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:608:1: expressionListOpt returns [ListBuffer<JCExpression> args = new ListBuffer<JCExpression>()] : (e1= expression ( COMMA e= expression )* )? ;
    public final ListBuffer<JCExpression> expressionListOpt() throws RecognitionException {
        ListBuffer<JCExpression> args =  new ListBuffer<JCExpression>();

        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:609:2: ( (e1= expression ( COMMA e= expression )* )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:609:4: (e1= expression ( COMMA e= expression )* )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:609:4: (e1= expression ( COMMA e= expression )* )?
            int alt67=2;
            int LA67_0 = input.LA(1);

            if ( ((LA67_0>=POUND && LA67_0<=TYPEOF)||LA67_0==IF||(LA67_0>=THIS && LA67_0<=FALSE)||(LA67_0>=NOT && LA67_0<=NEW)||LA67_0==SUPER||(LA67_0>=SIZEOF && LA67_0<=LPAREN)||LA67_0==LBRACKET||(LA67_0>=PLUSPLUS && LA67_0<=SUBSUB)||(LA67_0>=QUES && LA67_0<=STRING_LITERAL)||(LA67_0>=QUOTE_LBRACE_STRING_LITERAL && LA67_0<=LBRACE)||(LA67_0>=QUOTED_IDENTIFIER && LA67_0<=INTEGER_LITERAL)||LA67_0==FLOATING_POINT_LITERAL||LA67_0==IDENTIFIER) ) {
                alt67=1;
            }
            switch (alt67) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:609:6: e1= expression ( COMMA e= expression )*
                    {
                    pushFollow(FOLLOW_expression_in_expressionListOpt5536);
                    e1=expression();
                    _fsp--;

                     args.append(e1); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:610:6: ( COMMA e= expression )*
                    loop66:
                    do {
                        int alt66=2;
                        int LA66_0 = input.LA(1);

                        if ( (LA66_0==COMMA) ) {
                            alt66=1;
                        }


                        switch (alt66) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:610:7: COMMA e= expression
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_expressionListOpt5547); 
                    	    pushFollow(FOLLOW_expression_in_expressionListOpt5553);
                    	    e=expression();
                    	    _fsp--;

                    	     args.append(e); 

                    	    }
                    	    break;

                    	default :
                    	    break loop66;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:611:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );
    public final int unaryOperator() throws RecognitionException {
        int optag = 0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:612:2: ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB )
            int alt68=9;
            switch ( input.LA(1) ) {
            case POUND:
                {
                alt68=1;
                }
                break;
            case QUES:
                {
                alt68=2;
                }
                break;
            case SUB:
                {
                alt68=3;
                }
                break;
            case NOT:
                {
                alt68=4;
                }
                break;
            case SIZEOF:
                {
                alt68=5;
                }
                break;
            case TYPEOF:
                {
                alt68=6;
                }
                break;
            case REVERSE:
                {
                alt68=7;
                }
                break;
            case PLUSPLUS:
                {
                alt68=8;
                }
                break;
            case SUBSUB:
                {
                alt68=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("611:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );", 68, 0, input);

                throw nvae;
            }

            switch (alt68) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:612:4: POUND
                    {
                    match(input,POUND,FOLLOW_POUND_in_unaryOperator5576); 
                     optag = 0; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:613:4: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_unaryOperator5587); 
                     optag = 0; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:614:4: SUB
                    {
                    match(input,SUB,FOLLOW_SUB_in_unaryOperator5600); 
                     optag = JCTree.NEG; 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:615:4: NOT
                    {
                    match(input,NOT,FOLLOW_NOT_in_unaryOperator5613); 
                     optag = JCTree.NOT; 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:616:4: SIZEOF
                    {
                    match(input,SIZEOF,FOLLOW_SIZEOF_in_unaryOperator5626); 
                     optag = 0; 

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:617:4: TYPEOF
                    {
                    match(input,TYPEOF,FOLLOW_TYPEOF_in_unaryOperator5639); 
                     optag = 0; 

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:618:4: REVERSE
                    {
                    match(input,REVERSE,FOLLOW_REVERSE_in_unaryOperator5652); 
                     optag = 0; 

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:619:4: PLUSPLUS
                    {
                    match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_unaryOperator5665); 
                     optag = 0; 

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:620:4: SUBSUB
                    {
                    match(input,SUBSUB,FOLLOW_SUBSUB_in_unaryOperator5678); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:622:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );
    public final int assignmentOperator() throws RecognitionException {
        int optag = 0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:623:2: ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ )
            int alt69=5;
            switch ( input.LA(1) ) {
            case PLUSEQ:
                {
                alt69=1;
                }
                break;
            case SUBEQ:
                {
                alt69=2;
                }
                break;
            case STAREQ:
                {
                alt69=3;
                }
                break;
            case SLASHEQ:
                {
                alt69=4;
                }
                break;
            case PERCENTEQ:
                {
                alt69=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("622:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );", 69, 0, input);

                throw nvae;
            }

            switch (alt69) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:623:4: PLUSEQ
                    {
                    match(input,PLUSEQ,FOLLOW_PLUSEQ_in_assignmentOperator5699); 
                     optag = JCTree.PLUS_ASG; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:624:4: SUBEQ
                    {
                    match(input,SUBEQ,FOLLOW_SUBEQ_in_assignmentOperator5712); 
                     optag = JCTree.MINUS_ASG; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:625:4: STAREQ
                    {
                    match(input,STAREQ,FOLLOW_STAREQ_in_assignmentOperator5725); 
                     optag = JCTree.MUL_ASG; 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:626:4: SLASHEQ
                    {
                    match(input,SLASHEQ,FOLLOW_SLASHEQ_in_assignmentOperator5738); 
                     optag = JCTree.DIV_ASG; 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:627:4: PERCENTEQ
                    {
                    match(input,PERCENTEQ,FOLLOW_PERCENTEQ_in_assignmentOperator5751); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:629:1: typeReference returns [JFXType type] : ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? ;
    public final JFXType typeReference() throws RecognitionException {
        JFXType type = null;

        Token STAR123=null;
        int ccn = 0;

        int ccs = 0;

        name_return name122 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:630:2: ( ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:630:4: ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:630:4: ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            int alt71=2;
            int LA71_0 = input.LA(1);

            if ( (LA71_0==COLON) ) {
                alt71=1;
            }
            switch (alt71) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:630:6: COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    {
                    match(input,COLON,FOLLOW_COLON_in_typeReference5775); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:630:13: ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    int alt70=2;
                    int LA70_0 = input.LA(1);

                    if ( (LA70_0==QUOTED_IDENTIFIER||LA70_0==IDENTIFIER) ) {
                        alt70=1;
                    }
                    else if ( (LA70_0==STAR) ) {
                        alt70=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("630:13: ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )", 70, 0, input);

                        throw nvae;
                    }
                    switch (alt70) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:630:15: name ccn= cardinalityConstraint
                            {
                            pushFollow(FOLLOW_name_in_typeReference5780);
                            name122=name();
                            _fsp--;

                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference5784);
                            ccn=cardinalityConstraint();
                            _fsp--;

                             type = F.TypeClass(name122.value, ccn); 

                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:631:22: STAR ccs= cardinalityConstraint
                            {
                            STAR123=(Token)input.LT(1);
                            match(input,STAR,FOLLOW_STAR_in_typeReference5810); 
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference5814);
                            ccs=cardinalityConstraint();
                            _fsp--;

                             type = F.at(pos(STAR123)).TypeAny(ccs); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:635:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | );
    public final int cardinalityConstraint() throws RecognitionException {
        int ary = 0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:636:2: ( LBRACKET RBRACKET | )
            int alt72=2;
            int LA72_0 = input.LA(1);

            if ( (LA72_0==LBRACKET) ) {
                alt72=1;
            }
            else if ( (LA72_0==ON||LA72_0==INVERSE||LA72_0==RPAREN||(LA72_0>=SEMI && LA72_0<=COMMA)||LA72_0==EQ||LA72_0==LBRACE) ) {
                alt72=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("635:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | );", 72, 0, input);

                throw nvae;
            }
            switch (alt72) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:636:5: LBRACKET RBRACKET
                    {
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_cardinalityConstraint5876); 
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_cardinalityConstraint5880); 
                     ary = JFXType.CARDINALITY_ANY; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:637:29: 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:639:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );
    public final JCExpression literal() throws RecognitionException {
        JCExpression expr = null;

        Token t=null;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:640:2: (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL )
            int alt73=6;
            switch ( input.LA(1) ) {
            case STRING_LITERAL:
                {
                alt73=1;
                }
                break;
            case INTEGER_LITERAL:
                {
                alt73=2;
                }
                break;
            case FLOATING_POINT_LITERAL:
                {
                alt73=3;
                }
                break;
            case TRUE:
                {
                alt73=4;
                }
                break;
            case FALSE:
                {
                alt73=5;
                }
                break;
            case NULL:
                {
                alt73=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("639:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );", 73, 0, input);

                throw nvae;
            }

            switch (alt73) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:640:4: t= STRING_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_literal5934); 
                     expr = F.at(pos(t)).Literal(TypeTags.CLASS, t.getText()); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:641:4: t= INTEGER_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,INTEGER_LITERAL,FOLLOW_INTEGER_LITERAL_in_literal5944); 
                     expr = F.at(pos(t)).Literal(TypeTags.INT, Convert.string2int(t.getText(), 10)); 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:642:4: t= FLOATING_POINT_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,FLOATING_POINT_LITERAL,FOLLOW_FLOATING_POINT_LITERAL_in_literal5954); 
                     expr = F.at(pos(t)).Literal(TypeTags.DOUBLE, Double.valueOf(t.getText())); 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:643:4: t= TRUE
                    {
                    t=(Token)input.LT(1);
                    match(input,TRUE,FOLLOW_TRUE_in_literal5964); 
                     expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 1); 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:644:4: t= FALSE
                    {
                    t=(Token)input.LT(1);
                    match(input,FALSE,FOLLOW_FALSE_in_literal5978); 
                     expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 0); 

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:645:4: t= NULL
                    {
                    t=(Token)input.LT(1);
                    match(input,NULL,FOLLOW_NULL_in_literal5992); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:647:1: typeName returns [JCExpression expr] : qualident ;
    public final JCExpression typeName() throws RecognitionException {
        JCExpression expr = null;

        JCExpression qualident124 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:648:8: ( qualident )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:648:10: qualident
            {
            pushFollow(FOLLOW_qualident_in_typeName6019);
            qualident124=qualident();
            _fsp--;

             expr = qualident124; 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:650:1: qualident returns [JCExpression expr] : identifier ( DOT name )* ;
    public final JCExpression qualident() throws RecognitionException {
        JCExpression expr = null;

        JCIdent identifier125 = null;

        name_return name126 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:651:8: ( identifier ( DOT name )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:651:10: identifier ( DOT name )*
            {
            pushFollow(FOLLOW_identifier_in_qualident6061);
            identifier125=identifier();
            _fsp--;

             expr = identifier125; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:652:10: ( DOT name )*
            loop74:
            do {
                int alt74=2;
                int LA74_0 = input.LA(1);

                if ( (LA74_0==DOT) ) {
                    int LA74_2 = input.LA(2);

                    if ( (LA74_2==QUOTED_IDENTIFIER||LA74_2==IDENTIFIER) ) {
                        alt74=1;
                    }


                }


                switch (alt74) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:652:12: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_qualident6089); 
            	    pushFollow(FOLLOW_name_in_qualident6091);
            	    name126=name();
            	    _fsp--;

            	     expr = F.at(name126.pos).Select(expr, name126.value); 

            	    }
            	    break;

            	default :
            	    break loop74;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:654:1: identifier returns [JCIdent expr] : name ;
    public final JCIdent identifier() throws RecognitionException {
        JCIdent expr = null;

        name_return name127 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:655:2: ( name )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:655:4: name
            {
            pushFollow(FOLLOW_name_in_identifier6128);
            name127=name();
            _fsp--;

             expr = F.at(name127.pos).Ident(name127.value); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:657:1: name returns [Name value, int pos] : tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) ;
    public final name_return name() throws RecognitionException {
        name_return retval = new name_return();
        retval.start = input.LT(1);

        Token tokid=null;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:658:2: (tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:658:4: tokid= ( QUOTED_IDENTIFIER | IDENTIFIER )
            {
            tokid=(Token)input.LT(1);
            if ( input.LA(1)==QUOTED_IDENTIFIER||input.LA(1)==IDENTIFIER ) {
                input.consume();
                errorRecovery=false;
            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_name6162);    throw mse;
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


 

    public static final BitSet FOLLOW_packageDecl_in_module2008 = new BitSet(new long[]{0xBE267902E0110160L,0x012C3600E0057400L});
    public static final BitSet FOLLOW_moduleItems_in_module2011 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_module2013 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PACKAGE_in_packageDecl2035 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_qualident_in_packageDecl2037 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_packageDecl2039 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_moduleItem_in_moduleItems2070 = new BitSet(new long[]{0xBE267902E0110162L,0x012C3600E0057400L});
    public static final BitSet FOLLOW_importDecl_in_moduleItem2114 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDefinition_in_moduleItem2130 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_moduleItem2146 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_moduleItem2168 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_moduleItem2170 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importDecl2207 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_identifier_in_importDecl2210 = new BitSet(new long[]{0x0000000000000000L,0x0000000000140000L});
    public static final BitSet FOLLOW_DOT_in_importDecl2235 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_importDecl2237 = new BitSet(new long[]{0x0000000000000000L,0x0000000000140000L});
    public static final BitSet FOLLOW_DOT_in_importDecl2266 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000000L});
    public static final BitSet FOLLOW_STAR_in_importDecl2268 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_importDecl2277 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_classDefinition2308 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_CLASS_in_classDefinition2311 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_classDefinition2313 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000014L});
    public static final BitSet FOLLOW_supers_in_classDefinition2315 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000010L});
    public static final BitSet FOLLOW_interfaces_in_classDefinition2317 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_LBRACE_in_classDefinition2319 = new BitSet(new long[]{0xB800000000002100L,0x0001000000000040L});
    public static final BitSet FOLLOW_classMembers_in_classDefinition2321 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_classDefinition2323 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXTENDS_in_supers2348 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_qualident_in_supers2352 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_COMMA_in_supers2380 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_qualident_in_supers2384 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_IMPLEMENTS_in_interfaces2422 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_qualident_in_interfaces2426 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_COMMA_in_interfaces2454 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_qualident_in_interfaces2458 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_attributeDefinition_in_classMembers2497 = new BitSet(new long[]{0xB800000000002102L,0x0000000000000040L});
    public static final BitSet FOLLOW_functionDefinition_in_classMembers2519 = new BitSet(new long[]{0xB800000000002102L,0x0000000000000040L});
    public static final BitSet FOLLOW_initDefinition_in_classMembers2542 = new BitSet(new long[]{0xB800000000002102L});
    public static final BitSet FOLLOW_attributeDefinition_in_classMembers2565 = new BitSet(new long[]{0xB800000000002102L});
    public static final BitSet FOLLOW_functionDefinition_in_classMembers2588 = new BitSet(new long[]{0xB800000000002102L});
    public static final BitSet FOLLOW_modifierFlags_in_attributeDefinition2628 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeDefinition2630 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_attributeDefinition2632 = new BitSet(new long[]{0x0000000800000000L,0x0000010000440001L});
    public static final BitSet FOLLOW_typeReference_in_attributeDefinition2640 = new BitSet(new long[]{0x0000000800000000L,0x0000000000440001L});
    public static final BitSet FOLLOW_EQ_in_attributeDefinition2648 = new BitSet(new long[]{0x0600790018008060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_bindOpt_in_attributeDefinition2650 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_attributeDefinition2652 = new BitSet(new long[]{0x0000000800000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_inverseClause_in_attributeDefinition2656 = new BitSet(new long[]{0x0000000800000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_ON_in_attributeDefinition2666 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_CHANGE_in_attributeDefinition2668 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_block_in_attributeDefinition2672 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_attributeDefinition2681 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INVERSE_in_inverseClause2707 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_memberSelector_in_inverseClause2709 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_functionDefinition2727 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_FUNCTION_in_functionDefinition2729 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_functionDefinition2731 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_formalParameters_in_functionDefinition2739 = new BitSet(new long[]{0x0000000000000000L,0x0000210000000000L});
    public static final BitSet FOLLOW_typeReference_in_functionDefinition2742 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_blockExpression_in_functionDefinition2751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INIT_in_initDefinition2771 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_block_in_initDefinition2773 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags2802 = new BitSet(new long[]{0x3800000000000002L});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags2815 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags2837 = new BitSet(new long[]{0x8000000000000102L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags2850 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_accessModifier2898 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PRIVATE_in_accessModifier2918 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROTECTED_in_accessModifier2937 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ABSTRACT_in_otherModifier2964 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_READONLY_in_otherModifier2982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_memberSelector3011 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_DOT_in_memberSelector3015 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_memberSelector3021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_formalParameters3039 = new BitSet(new long[]{0x0000000000000000L,0x0104000000008000L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters3046 = new BitSet(new long[]{0x0000000000000000L,0x0000000000088000L});
    public static final BitSet FOLLOW_COMMA_in_formalParameters3064 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters3070 = new BitSet(new long[]{0x0000000000000000L,0x0000000000088000L});
    public static final BitSet FOLLOW_RPAREN_in_formalParameters3081 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_formalParameter3096 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
    public static final BitSet FOLLOW_typeReference_in_formalParameter3098 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_block3124 = new BitSet(new long[]{0xFE267900E0010160L,0x012D3600E0057400L});
    public static final BitSet FOLLOW_statement_in_block3134 = new BitSet(new long[]{0xFE267900E0010160L,0x012D3600E0057400L});
    public static final BitSet FOLLOW_expression_in_block3149 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_block3151 = new BitSet(new long[]{0xFE267900E0010160L,0x012D3600E0057400L});
    public static final BitSet FOLLOW_RBRACE_in_block3167 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_blockExpression3191 = new BitSet(new long[]{0xFE267900E0010160L,0x012D3600E0057400L});
    public static final BitSet FOLLOW_statements_in_blockExpression3194 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_blockExpression3199 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_statements3218 = new BitSet(new long[]{0xFE267900E0010162L,0x012C3600E0057400L});
    public static final BitSet FOLLOW_statements_in_statements3230 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_statements3242 = new BitSet(new long[]{0x0000000000000002L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statements3254 = new BitSet(new long[]{0xFE267900E0010162L,0x012C3600E0057400L});
    public static final BitSet FOLLOW_statements_in_statements3277 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclaration_in_statement3335 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement3337 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionDefinition_in_statement3345 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_statement3361 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_LPAREN_in_statement3363 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_statement3365 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_RPAREN_in_statement3367 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_block_in_statement3369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BREAK_in_statement3376 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement3378 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_statement3391 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement3394 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THROW_in_statement3412 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_statement3414 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement3416 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_returnStatement_in_statement3436 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement3438 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tryStatement_in_statement3454 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_statement3465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variableDeclaration3495 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_variableDeclaration3498 = new BitSet(new long[]{0x0000000000000002L,0x0000010000400000L});
    public static final BitSet FOLLOW_typeReference_in_variableDeclaration3501 = new BitSet(new long[]{0x0000000000000002L,0x0000000000400000L});
    public static final BitSet FOLLOW_EQ_in_variableDeclaration3512 = new BitSet(new long[]{0x0600790018008060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_bindOpt_in_variableDeclaration3514 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_variableDeclaration3517 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BIND_in_bindOpt3567 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3583 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAYS_in_bindOpt3598 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3614 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TIE_in_bindOpt3629 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3645 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnStatement3679 = new BitSet(new long[]{0x0600790000000062L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_returnStatement3682 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRY_in_tryStatement3725 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3731 = new BitSet(new long[]{0x0040000000040000L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement3744 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3750 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catchClause_in_tryStatement3766 = new BitSet(new long[]{0x0040000000040002L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement3789 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3794 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CATCH_in_catchClause3833 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_LPAREN_in_catchClause3836 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_catchClause3839 = new BitSet(new long[]{0x0000000000000000L,0x0000010000008000L});
    public static final BitSet FOLLOW_COLON_in_catchClause3847 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_identifier_in_catchClause3849 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_RPAREN_in_catchClause3858 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_block_in_catchClause3862 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifExpression_in_expression3890 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_suffixedExpression_in_expression3913 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifExpression3944 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_ifExpression3948 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_THEN_in_ifExpression3952 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_ifExpression3957 = new BitSet(new long[]{0x0000040000000002L});
    public static final BitSet FOLLOW_ELSE_in_ifExpression3966 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_ifExpression3971 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentExpression_in_suffixedExpression3996 = new BitSet(new long[]{0x0000000000000002L,0x00000000A0000000L});
    public static final BitSet FOLLOW_set_in_suffixedExpression4007 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression4036 = new BitSet(new long[]{0x0000000000000002L,0x0000000000400000L});
    public static final BitSet FOLLOW_EQ_in_assignmentExpression4051 = new BitSet(new long[]{0x0600780000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression4057 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression4084 = new BitSet(new long[]{0x0000000000000002L,0x000000F800000000L});
    public static final BitSet FOLLOW_assignmentOperator_in_assignmentOpExpression4100 = new BitSet(new long[]{0x0600780000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression4106 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orExpression_in_andExpression4132 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_AND_in_andExpression4148 = new BitSet(new long[]{0x0600780000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_orExpression_in_andExpression4154 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression4182 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000800L});
    public static final BitSet FOLLOW_OR_in_orExpression4197 = new BitSet(new long[]{0x0600780000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression4203 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000800L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression4231 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_INSTANCEOF_in_instanceOfExpression4246 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_identifier_in_instanceOfExpression4248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4276 = new BitSet(new long[]{0x0001000000000002L,0x000000000FA00000L});
    public static final BitSet FOLLOW_LTGT_in_relationalExpression4292 = new BitSet(new long[]{0x0600780000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4298 = new BitSet(new long[]{0x0001000000000002L,0x000000000FA00000L});
    public static final BitSet FOLLOW_EQEQ_in_relationalExpression4312 = new BitSet(new long[]{0x0600780000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4318 = new BitSet(new long[]{0x0001000000000002L,0x000000000FA00000L});
    public static final BitSet FOLLOW_LTEQ_in_relationalExpression4332 = new BitSet(new long[]{0x0600780000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4338 = new BitSet(new long[]{0x0001000000000002L,0x000000000FA00000L});
    public static final BitSet FOLLOW_GTEQ_in_relationalExpression4352 = new BitSet(new long[]{0x0600780000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4358 = new BitSet(new long[]{0x0001000000000002L,0x000000000FA00000L});
    public static final BitSet FOLLOW_LT_in_relationalExpression4372 = new BitSet(new long[]{0x0600780000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4380 = new BitSet(new long[]{0x0001000000000002L,0x000000000FA00000L});
    public static final BitSet FOLLOW_GT_in_relationalExpression4394 = new BitSet(new long[]{0x0600780000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4402 = new BitSet(new long[]{0x0001000000000002L,0x000000000FA00000L});
    public static final BitSet FOLLOW_IN_in_relationalExpression4416 = new BitSet(new long[]{0x0600780000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4424 = new BitSet(new long[]{0x0001000000000002L,0x000000000FA00000L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4453 = new BitSet(new long[]{0x0000000000000002L,0x0000000050000000L});
    public static final BitSet FOLLOW_PLUS_in_additiveExpression4468 = new BitSet(new long[]{0x0600780000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4474 = new BitSet(new long[]{0x0000000000000002L,0x0000000050000000L});
    public static final BitSet FOLLOW_SUB_in_additiveExpression4487 = new BitSet(new long[]{0x0600780000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4494 = new BitSet(new long[]{0x0000000000000002L,0x0000000050000000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4522 = new BitSet(new long[]{0x0000000000000002L,0x0000000700000000L});
    public static final BitSet FOLLOW_STAR_in_multiplicativeExpression4538 = new BitSet(new long[]{0x0600780000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4545 = new BitSet(new long[]{0x0000000000000002L,0x0000000700000000L});
    public static final BitSet FOLLOW_SLASH_in_multiplicativeExpression4559 = new BitSet(new long[]{0x0600780000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4565 = new BitSet(new long[]{0x0000000000000002L,0x0000000700000000L});
    public static final BitSet FOLLOW_PERCENT_in_multiplicativeExpression4579 = new BitSet(new long[]{0x0600780000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4583 = new BitSet(new long[]{0x0000000000000002L,0x0000000700000000L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression4613 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryOperator_in_unaryExpression4624 = new BitSet(new long[]{0x0400780000000000L,0x012C340000014400L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression4628 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primaryExpression_in_postfixExpression4648 = new BitSet(new long[]{0x0000000000000002L,0x0000000000110000L});
    public static final BitSet FOLLOW_DOT_in_postfixExpression4663 = new BitSet(new long[]{0x0000000000100000L,0x0104000000000000L});
    public static final BitSet FOLLOW_CLASS_in_postfixExpression4667 = new BitSet(new long[]{0x0000000000000002L,0x0000000000110000L});
    public static final BitSet FOLLOW_name_in_postfixExpression4691 = new BitSet(new long[]{0x0000000000000002L,0x0000000000114000L});
    public static final BitSet FOLLOW_LPAREN_in_postfixExpression4716 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E001F400L});
    public static final BitSet FOLLOW_expressionListOpt_in_postfixExpression4718 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_RPAREN_in_postfixExpression4720 = new BitSet(new long[]{0x0000000000000002L,0x0000000000114000L});
    public static final BitSet FOLLOW_LBRACKET_in_postfixExpression4752 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_name_in_postfixExpression4755 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_BAR_in_postfixExpression4757 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_postfixExpression4761 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_postfixExpression4764 = new BitSet(new long[]{0x0000000000000002L,0x0000000000110000L});
    public static final BitSet FOLLOW_newExpression_in_primaryExpression4789 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression4801 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_LBRACE_in_primaryExpression4803 = new BitSet(new long[]{0xB800000000002100L,0x0105000000000000L});
    public static final BitSet FOLLOW_objectLiteral_in_primaryExpression4806 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_primaryExpression4808 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THIS_in_primaryExpression4825 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_primaryExpression4845 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression4865 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004000L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression4886 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E001F400L});
    public static final BitSet FOLLOW_expressionListOpt_in_primaryExpression4890 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression4894 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004000L});
    public static final BitSet FOLLOW_stringExpression_in_primaryExpression4914 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_bracketExpression_in_primaryExpression4933 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primaryExpression4952 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_blockExpression_in_primaryExpression4972 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression4990 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_primaryExpression4992 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression4994 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_newExpression5027 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_qualident_in_newExpression5030 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004000L});
    public static final BitSet FOLLOW_LPAREN_in_newExpression5038 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E001F400L});
    public static final BitSet FOLLOW_expressionListOpt_in_newExpression5042 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_RPAREN_in_newExpression5046 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_objectLiteralPart_in_objectLiteral5086 = new BitSet(new long[]{0xB800000000002102L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_objectLiteralPart5114 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_COLON_in_objectLiteralPart5116 = new BitSet(new long[]{0x0600790018008060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_bindOpt_in_objectLiteralPart5119 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_objectLiteralPart5121 = new BitSet(new long[]{0x0000000000000002L,0x00000000000C0000L});
    public static final BitSet FOLLOW_set_in_objectLiteralPart5123 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeDefinition_in_objectLiteralPart5144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionDefinition_in_objectLiteralPart5156 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression5187 = new BitSet(new long[]{0x0600790000000060L,0x012E3600E0017400L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression5196 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_stringExpression5207 = new BitSet(new long[]{0x0000000000000000L,0x0000C00000000000L});
    public static final BitSet FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression5222 = new BitSet(new long[]{0x0600790000000060L,0x012E3600E0017400L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression5234 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_stringExpression5248 = new BitSet(new long[]{0x0000000000000000L,0x0000C00000000000L});
    public static final BitSet FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression5269 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull5299 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_bracketExpression5332 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0037400L});
    public static final BitSet FOLLOW_expression_in_bracketExpression5360 = new BitSet(new long[]{0x0000000000000080L,0x00000000000A0000L});
    public static final BitSet FOLLOW_COMMA_in_bracketExpression5393 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_bracketExpression5397 = new BitSet(new long[]{0x0000000000000000L,0x00000000000A0000L});
    public static final BitSet FOLLOW_COMMA_in_bracketExpression5432 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_bracketExpression5437 = new BitSet(new long[]{0x0000000000000000L,0x00000000000A0000L});
    public static final BitSet FOLLOW_DOTDOT_in_bracketExpression5483 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_bracketExpression5489 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_bracketExpression5515 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt5536 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_COMMA_in_expressionListOpt5547 = new BitSet(new long[]{0x0600790000000060L,0x012C3600E0017400L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt5553 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_POUND_in_unaryOperator5576 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_unaryOperator5587 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUB_in_unaryOperator5600 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_unaryOperator5613 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIZEOF_in_unaryOperator5626 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TYPEOF_in_unaryOperator5639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REVERSE_in_unaryOperator5652 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_unaryOperator5665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_unaryOperator5678 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSEQ_in_assignmentOperator5699 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBEQ_in_assignmentOperator5712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAREQ_in_assignmentOperator5725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SLASHEQ_in_assignmentOperator5738 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENTEQ_in_assignmentOperator5751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLON_in_typeReference5775 = new BitSet(new long[]{0x0000000000000000L,0x0104000100000000L});
    public static final BitSet FOLLOW_name_in_typeReference5780 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference5784 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_typeReference5810 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference5814 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_cardinalityConstraint5876 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_cardinalityConstraint5880 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_literal5934 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_LITERAL_in_literal5944 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOATING_POINT_LITERAL_in_literal5954 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRUE_in_literal5964 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALSE_in_literal5978 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_literal5992 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualident_in_typeName6019 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_qualident6061 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_DOT_in_qualident6089 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_qualident6091 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_name_in_identifier6128 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_name6162 = new BitSet(new long[]{0x0000000000000002L});

}