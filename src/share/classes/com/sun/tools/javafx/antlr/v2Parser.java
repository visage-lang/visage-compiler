// $ANTLR 3.0 C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g 2007-08-14 21:33:52

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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "BAR", "POUND", "TYPEOF", "DOTDOT", "LARROW", "ABSTRACT", "AFTER", "AND", "AS", "ASSERT", "ATTRIBUTE", "BEFORE", "BIND", "BREAK", "BY", "CATCH", "CHANGE", "CLASS", "DELETE", "DO", "DUR", "EASEBOTH", "EASEIN", "EASEOUT", "TIE", "STAYS", "RETURN", "THROW", "VAR", "PACKAGE", "IMPORT", "FROM", "ON", "INSERT", "INTO", "FIRST", "LAST", "IF", "THEN", "ELSE", "THIS", "NULL", "TRUE", "FALSE", "FOR", "IN", "WHILE", "CONTINUE", "LINEAR", "MOTION", "TRY", "FINALLY", "LAZY", "WHERE", "NOT", "NEW", "PRIVATE", "PROTECTED", "PUBLIC", "FUNCTION", "READONLY", "INVERSE", "TYPE", "EXTENDS", "ORDER", "INDEX", "INIT", "INSTANCEOF", "INDEXOF", "SELECT", "SUPER", "OR", "SIZEOF", "REVERSE", "LPAREN", "RPAREN", "LBRACKET", "RBRACKET", "SEMI", "COMMA", "DOT", "EQEQ", "EQ", "GT", "LT", "LTGT", "LTEQ", "GTEQ", "PLUS", "PLUSPLUS", "SUB", "SUBSUB", "STAR", "SLASH", "PERCENT", "PLUSEQ", "SUBEQ", "STAREQ", "SLASHEQ", "PERCENTEQ", "COLON", "QUES", "STRING_LITERAL", "NextIsPercent", "QUOTE_LBRACE_STRING_LITERAL", "LBRACE", "RBRACE_QUOTE_STRING_LITERAL", "RBRACE_LBRACE_STRING_LITERAL", "RBRACE", "FORMAT_STRING_LITERAL", "QUOTED_IDENTIFIER", "INTEGER_LITERAL", "Exponent", "FLOATING_POINT_LITERAL", "Letter", "JavaIDDigit", "IDENTIFIER", "WS", "COMMENT", "LINE_COMMENT"
    };
    public static final int PACKAGE=33;
    public static final int FUNCTION=63;
    public static final int LT=88;
    public static final int STAR=96;
    public static final int WHILE=50;
    public static final int EASEOUT=27;
    public static final int NEW=59;
    public static final int INDEXOF=72;
    public static final int DO=23;
    public static final int NOT=58;
    public static final int EOF=-1;
    public static final int RBRACE_QUOTE_STRING_LITERAL=110;
    public static final int BREAK=17;
    public static final int TYPE=66;
    public static final int LBRACKET=80;
    public static final int RPAREN=79;
    public static final int IMPORT=34;
    public static final int LINEAR=52;
    public static final int STRING_LITERAL=106;
    public static final int FLOATING_POINT_LITERAL=117;
    public static final int INSERT=37;
    public static final int SUBSUB=95;
    public static final int BIND=16;
    public static final int STAREQ=101;
    public static final int RETURN=30;
    public static final int THIS=44;
    public static final int VAR=32;
    public static final int SUPER=74;
    public static final int LAST=40;
    public static final int EQ=86;
    public static final int COMMENT=122;
    public static final int SELECT=73;
    public static final int INTO=38;
    public static final int QUES=105;
    public static final int EQEQ=85;
    public static final int MOTION=53;
    public static final int RBRACE=112;
    public static final int POUND=5;
    public static final int LINE_COMMENT=123;
    public static final int PRIVATE=60;
    public static final int NULL=45;
    public static final int ELSE=43;
    public static final int ON=36;
    public static final int DELETE=22;
    public static final int SLASHEQ=102;
    public static final int EASEBOTH=25;
    public static final int ASSERT=13;
    public static final int TRY=54;
    public static final int INVERSE=65;
    public static final int WS=121;
    public static final int TYPEOF=6;
    public static final int INTEGER_LITERAL=115;
    public static final int OR=75;
    public static final int JavaIDDigit=119;
    public static final int SIZEOF=76;
    public static final int GT=87;
    public static final int FROM=35;
    public static final int CATCH=19;
    public static final int REVERSE=77;
    public static final int FALSE=47;
    public static final int INIT=70;
    public static final int Letter=118;
    public static final int THROW=31;
    public static final int DUR=24;
    public static final int WHERE=57;
    public static final int PROTECTED=61;
    public static final int CLASS=21;
    public static final int ORDER=68;
    public static final int PLUSPLUS=93;
    public static final int LBRACE=109;
    public static final int ATTRIBUTE=14;
    public static final int LTEQ=90;
    public static final int SUBEQ=100;
    public static final int Exponent=116;
    public static final int LARROW=8;
    public static final int FOR=48;
    public static final int SUB=94;
    public static final int DOTDOT=7;
    public static final int ABSTRACT=9;
    public static final int NextIsPercent=107;
    public static final int AND=11;
    public static final int PLUSEQ=99;
    public static final int LPAREN=78;
    public static final int IF=41;
    public static final int AS=12;
    public static final int INDEX=69;
    public static final int SLASH=97;
    public static final int THEN=42;
    public static final int IN=49;
    public static final int CONTINUE=51;
    public static final int COMMA=83;
    public static final int TIE=28;
    public static final int IDENTIFIER=120;
    public static final int QUOTE_LBRACE_STRING_LITERAL=108;
    public static final int PLUS=92;
    public static final int RBRACKET=81;
    public static final int DOT=84;
    public static final int RBRACE_LBRACE_STRING_LITERAL=111;
    public static final int STAYS=29;
    public static final int BY=18;
    public static final int PERCENT=98;
    public static final int LAZY=56;
    public static final int LTGT=89;
    public static final int BEFORE=15;
    public static final int INSTANCEOF=71;
    public static final int AFTER=10;
    public static final int GTEQ=91;
    public static final int READONLY=64;
    public static final int TRUE=46;
    public static final int SEMI=82;
    public static final int CHANGE=20;
    public static final int COLON=104;
    public static final int PERCENTEQ=103;
    public static final int FINALLY=55;
    public static final int FORMAT_STRING_LITERAL=113;
    public static final int EASEIN=26;
    public static final int QUOTED_IDENTIFIER=114;
    public static final int PUBLIC=62;
    public static final int EXTENDS=67;
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

                if ( ((LA2_0>=POUND && LA2_0<=TYPEOF)||LA2_0==ABSTRACT||LA2_0==BREAK||LA2_0==CLASS||(LA2_0>=RETURN && LA2_0<=VAR)||LA2_0==IMPORT||LA2_0==IF||(LA2_0>=THIS && LA2_0<=FALSE)||(LA2_0>=WHILE && LA2_0<=CONTINUE)||LA2_0==TRY||(LA2_0>=NOT && LA2_0<=READONLY)||LA2_0==SUPER||(LA2_0>=SIZEOF && LA2_0<=LPAREN)||LA2_0==SEMI||(LA2_0>=PLUSPLUS && LA2_0<=SUBSUB)||(LA2_0>=QUES && LA2_0<=STRING_LITERAL)||(LA2_0>=QUOTE_LBRACE_STRING_LITERAL && LA2_0<=LBRACE)||(LA2_0>=QUOTED_IDENTIFIER && LA2_0<=INTEGER_LITERAL)||LA2_0==FLOATING_POINT_LITERAL||LA2_0==IDENTIFIER) ) {
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

                    if ( (LA3_11==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_11==CLASS) ) {
                        alt3=2;
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

                    if ( (LA3_12==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_12==CLASS) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 12, input);

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

                    if ( (LA3_11==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_11==CLASS) ) {
                        alt3=2;
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

                    if ( (LA3_12==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_12==CLASS) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("323:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement | expression SEMI );", 3, 12, input);

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

                    if ( (LA3_13==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_13==CLASS) ) {
                        alt3=2;
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

                    if ( (LA3_13==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_13==CLASS) ) {
                        alt3=2;
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

                    if ( (LA3_13==FUNCTION) ) {
                        alt3=3;
                    }
                    else if ( (LA3_13==CLASS) ) {
                        alt3=2;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:335:1: classDefinition returns [JFXClassDeclaration value] : modifierFlags CLASS name supers LBRACE classMembers RBRACE ;
    public final JFXClassDeclaration classDefinition() throws RecognitionException {
        JFXClassDeclaration value = null;

        Token CLASS13=null;
        JCModifiers modifierFlags14 = null;

        name_return name15 = null;

        ListBuffer<Name> supers16 = null;

        ListBuffer<JCTree> classMembers17 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:336:2: ( modifierFlags CLASS name supers LBRACE classMembers RBRACE )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:336:4: modifierFlags CLASS name supers LBRACE classMembers RBRACE
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

            match(input,LBRACE,FOLLOW_LBRACE_in_classDefinition2317); 
            pushFollow(FOLLOW_classMembers_in_classDefinition2319);
            classMembers17=classMembers();
            _fsp--;

            match(input,RBRACE,FOLLOW_RBRACE_in_classDefinition2321); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:339:1: supers returns [ListBuffer<Name> names = new ListBuffer<Name>()] : ( EXTENDS name1= name ( COMMA namen= name )* )? ;
    public final ListBuffer<Name> supers() throws RecognitionException {
        ListBuffer<Name> names =  new ListBuffer<Name>();

        name_return name1 = null;

        name_return namen = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:340:2: ( ( EXTENDS name1= name ( COMMA namen= name )* )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:340:4: ( EXTENDS name1= name ( COMMA namen= name )* )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:340:4: ( EXTENDS name1= name ( COMMA namen= name )* )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==EXTENDS) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:340:5: EXTENDS name1= name ( COMMA namen= name )*
                    {
                    match(input,EXTENDS,FOLLOW_EXTENDS_in_supers2346); 
                    pushFollow(FOLLOW_name_in_supers2350);
                    name1=name();
                    _fsp--;

                     names.append(name1.value); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:341:12: ( COMMA namen= name )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==COMMA) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:341:14: COMMA namen= name
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_supers2378); 
                    	    pushFollow(FOLLOW_name_in_supers2382);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:343:1: classMembers returns [ListBuffer<JCTree> mems = new ListBuffer<JCTree>()] : (ad1= attributeDefinition | fd1= functionDefinition )* ( initDefinition (ad2= attributeDefinition | fd2= functionDefinition )* )? ;
    public final ListBuffer<JCTree> classMembers() throws RecognitionException {
        ListBuffer<JCTree> mems =  new ListBuffer<JCTree>();

        JFXAttributeDefinition ad1 = null;

        JFXFunctionDefinition fd1 = null;

        JFXAttributeDefinition ad2 = null;

        JFXFunctionDefinition fd2 = null;

        JFXInitDefinition initDefinition18 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:344:2: ( (ad1= attributeDefinition | fd1= functionDefinition )* ( initDefinition (ad2= attributeDefinition | fd2= functionDefinition )* )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:344:4: (ad1= attributeDefinition | fd1= functionDefinition )* ( initDefinition (ad2= attributeDefinition | fd2= functionDefinition )* )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:344:4: (ad1= attributeDefinition | fd1= functionDefinition )*
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
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:344:6: ad1= attributeDefinition
            	    {
            	    pushFollow(FOLLOW_attributeDefinition_in_classMembers2419);
            	    ad1=attributeDefinition();
            	    _fsp--;

            	     mems.append(ad1); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:345:7: fd1= functionDefinition
            	    {
            	    pushFollow(FOLLOW_functionDefinition_in_classMembers2441);
            	    fd1=functionDefinition();
            	    _fsp--;

            	     mems.append(fd1); 

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:347:4: ( initDefinition (ad2= attributeDefinition | fd2= functionDefinition )* )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==INIT) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:347:5: initDefinition (ad2= attributeDefinition | fd2= functionDefinition )*
                    {
                    pushFollow(FOLLOW_initDefinition_in_classMembers2464);
                    initDefinition18=initDefinition();
                    _fsp--;

                     mems.append(initDefinition18); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:348:6: (ad2= attributeDefinition | fd2= functionDefinition )*
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

                                if ( (LA9_9==FUNCTION) ) {
                                    alt9=2;
                                }
                                else if ( (LA9_9==ATTRIBUTE) ) {
                                    alt9=1;
                                }


                                }
                                break;
                            case PRIVATE:
                                {
                                int LA9_10 = input.LA(3);

                                if ( (LA9_10==FUNCTION) ) {
                                    alt9=2;
                                }
                                else if ( (LA9_10==ATTRIBUTE) ) {
                                    alt9=1;
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
                        case READONLY:
                            {
                            switch ( input.LA(2) ) {
                            case PUBLIC:
                                {
                                int LA9_9 = input.LA(3);

                                if ( (LA9_9==FUNCTION) ) {
                                    alt9=2;
                                }
                                else if ( (LA9_9==ATTRIBUTE) ) {
                                    alt9=1;
                                }


                                }
                                break;
                            case PRIVATE:
                                {
                                int LA9_10 = input.LA(3);

                                if ( (LA9_10==FUNCTION) ) {
                                    alt9=2;
                                }
                                else if ( (LA9_10==ATTRIBUTE) ) {
                                    alt9=1;
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
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:348:8: ad2= attributeDefinition
                    	    {
                    	    pushFollow(FOLLOW_attributeDefinition_in_classMembers2487);
                    	    ad2=attributeDefinition();
                    	    _fsp--;

                    	     mems.append(ad2); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:349:8: fd2= functionDefinition
                    	    {
                    	    pushFollow(FOLLOW_functionDefinition_in_classMembers2510);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:353:1: attributeDefinition returns [JFXAttributeDefinition def] : modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? ( ON CHANGE ocb= block )? SEMI ;
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
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:354:2: ( modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? ( ON CHANGE ocb= block )? SEMI )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:354:4: modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? ( ON CHANGE ocb= block )? SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_attributeDefinition2550);
            modifierFlags20=modifierFlags();
            _fsp--;

            ATTRIBUTE19=(Token)input.LT(1);
            match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeDefinition2552); 
            pushFollow(FOLLOW_name_in_attributeDefinition2554);
            name21=name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_attributeDefinition2562);
            typeReference22=typeReference();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:356:5: ( EQ bindOpt expression | inverseClause )?
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
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:356:6: EQ bindOpt expression
                    {
                    match(input,EQ,FOLLOW_EQ_in_attributeDefinition2570); 
                    pushFollow(FOLLOW_bindOpt_in_attributeDefinition2572);
                    bindOpt24=bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_attributeDefinition2574);
                    expression25=expression();
                    _fsp--;


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:356:30: inverseClause
                    {
                    pushFollow(FOLLOW_inverseClause_in_attributeDefinition2578);
                    inverseClause23=inverseClause();
                    _fsp--;


                    }
                    break;

            }

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:357:5: ( ON CHANGE ocb= block )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ON) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:357:6: ON CHANGE ocb= block
                    {
                    match(input,ON,FOLLOW_ON_in_attributeDefinition2588); 
                    match(input,CHANGE,FOLLOW_CHANGE_in_attributeDefinition2590); 
                    pushFollow(FOLLOW_block_in_attributeDefinition2594);
                    ocb=block();
                    _fsp--;


                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_attributeDefinition2603); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:362:1: inverseClause returns [JFXMemberSelector inverse = null] : INVERSE memberSelector ;
    public final JFXMemberSelector inverseClause() throws RecognitionException {
        JFXMemberSelector inverse =  null;

        JFXMemberSelector memberSelector26 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:363:2: ( INVERSE memberSelector )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:363:4: INVERSE memberSelector
            {
            match(input,INVERSE,FOLLOW_INVERSE_in_inverseClause2629); 
            pushFollow(FOLLOW_memberSelector_in_inverseClause2631);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:364:1: functionDefinition returns [JFXFunctionDefinition def] : modifierFlags FUNCTION name formalParameters typeReference blockExpression ;
    public final JFXFunctionDefinition functionDefinition() throws RecognitionException {
        JFXFunctionDefinition def = null;

        Token FUNCTION27=null;
        JCModifiers modifierFlags28 = null;

        name_return name29 = null;

        JFXType typeReference30 = null;

        ListBuffer<JCTree> formalParameters31 = null;

        JFXBlockExpression blockExpression32 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:365:2: ( modifierFlags FUNCTION name formalParameters typeReference blockExpression )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:365:4: modifierFlags FUNCTION name formalParameters typeReference blockExpression
            {
            pushFollow(FOLLOW_modifierFlags_in_functionDefinition2649);
            modifierFlags28=modifierFlags();
            _fsp--;

            FUNCTION27=(Token)input.LT(1);
            match(input,FUNCTION,FOLLOW_FUNCTION_in_functionDefinition2651); 
            pushFollow(FOLLOW_name_in_functionDefinition2653);
            name29=name();
            _fsp--;

            pushFollow(FOLLOW_formalParameters_in_functionDefinition2661);
            formalParameters31=formalParameters();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_functionDefinition2664);
            typeReference30=typeReference();
            _fsp--;

            pushFollow(FOLLOW_blockExpression_in_functionDefinition2673);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:371:1: initDefinition returns [JFXInitDefinition def] : INIT block ;
    public final JFXInitDefinition initDefinition() throws RecognitionException {
        JFXInitDefinition def = null;

        Token INIT33=null;
        JCBlock block34 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:372:2: ( INIT block )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:372:4: INIT block
            {
            INIT33=(Token)input.LT(1);
            match(input,INIT,FOLLOW_INIT_in_initDefinition2693); 
            pushFollow(FOLLOW_block_in_initDefinition2695);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:374:1: modifierFlags returns [JCModifiers mods] : (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? ;
    public final JCModifiers modifierFlags() throws RecognitionException {
        JCModifiers mods = null;

        long om1 = 0;

        long am1 = 0;

        long am2 = 0;

        long om2 = 0;


         long flags = 0; 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:376:2: ( (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:376:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:376:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
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
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:376:6: om1= otherModifier (am1= accessModifier )?
                    {
                    pushFollow(FOLLOW_otherModifier_in_modifierFlags2724);
                    om1=otherModifier();
                    _fsp--;

                     flags |= om1; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:377:3: (am1= accessModifier )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( ((LA13_0>=PRIVATE && LA13_0<=PUBLIC)) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:377:5: am1= accessModifier
                            {
                            pushFollow(FOLLOW_accessModifier_in_modifierFlags2737);
                            am1=accessModifier();
                            _fsp--;

                             flags |= am1; 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:378:6: am2= accessModifier (om2= otherModifier )?
                    {
                    pushFollow(FOLLOW_accessModifier_in_modifierFlags2759);
                    am2=accessModifier();
                    _fsp--;

                     flags |= am2; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:379:3: (om2= otherModifier )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==ABSTRACT||LA14_0==READONLY) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:379:5: om2= otherModifier
                            {
                            pushFollow(FOLLOW_otherModifier_in_modifierFlags2772);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:382:1: accessModifier returns [long flags = 0] : ( PUBLIC | PRIVATE | PROTECTED ) ;
    public final long accessModifier() throws RecognitionException {
        long flags =  0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:383:2: ( ( PUBLIC | PRIVATE | PROTECTED ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:383:4: ( PUBLIC | PRIVATE | PROTECTED )
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:383:4: ( PUBLIC | PRIVATE | PROTECTED )
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
                    new NoViableAltException("383:4: ( PUBLIC | PRIVATE | PROTECTED )", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:383:5: PUBLIC
                    {
                    match(input,PUBLIC,FOLLOW_PUBLIC_in_accessModifier2820); 
                     flags |= Flags.PUBLIC; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:384:5: PRIVATE
                    {
                    match(input,PRIVATE,FOLLOW_PRIVATE_in_accessModifier2840); 
                     flags |= Flags.PUBLIC; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:385:5: PROTECTED
                    {
                    match(input,PROTECTED,FOLLOW_PROTECTED_in_accessModifier2859); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:386:1: otherModifier returns [long flags = 0] : ( ABSTRACT | READONLY ) ;
    public final long otherModifier() throws RecognitionException {
        long flags =  0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:387:2: ( ( ABSTRACT | READONLY ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:387:4: ( ABSTRACT | READONLY )
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:387:4: ( ABSTRACT | READONLY )
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
                    new NoViableAltException("387:4: ( ABSTRACT | READONLY )", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:387:5: ABSTRACT
                    {
                    match(input,ABSTRACT,FOLLOW_ABSTRACT_in_otherModifier2886); 
                     flags |= Flags.ABSTRACT; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:388:5: READONLY
                    {
                    match(input,READONLY,FOLLOW_READONLY_in_otherModifier2904); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:389:1: memberSelector returns [JFXMemberSelector value] : name1= name DOT name2= name ;
    public final JFXMemberSelector memberSelector() throws RecognitionException {
        JFXMemberSelector value = null;

        name_return name1 = null;

        name_return name2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:390:2: (name1= name DOT name2= name )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:390:4: name1= name DOT name2= name
            {
            pushFollow(FOLLOW_name_in_memberSelector2933);
            name1=name();
            _fsp--;

            match(input,DOT,FOLLOW_DOT_in_memberSelector2937); 
            pushFollow(FOLLOW_name_in_memberSelector2943);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:392:1: formalParameters returns [ListBuffer<JCTree> params = new ListBuffer<JCTree>()] : LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN ;
    public final ListBuffer<JCTree> formalParameters() throws RecognitionException {
        ListBuffer<JCTree> params =  new ListBuffer<JCTree>();

        JFXVar fp0 = null;

        JFXVar fpn = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:393:2: ( LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:393:4: LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_formalParameters2961); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:393:12: (fp0= formalParameter ( COMMA fpn= formalParameter )* )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==QUOTED_IDENTIFIER||LA19_0==IDENTIFIER) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:393:14: fp0= formalParameter ( COMMA fpn= formalParameter )*
                    {
                    pushFollow(FOLLOW_formalParameter_in_formalParameters2968);
                    fp0=formalParameter();
                    _fsp--;

                     params.append(fp0); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:394:12: ( COMMA fpn= formalParameter )*
                    loop18:
                    do {
                        int alt18=2;
                        int LA18_0 = input.LA(1);

                        if ( (LA18_0==COMMA) ) {
                            alt18=1;
                        }


                        switch (alt18) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:394:14: COMMA fpn= formalParameter
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_formalParameters2986); 
                    	    pushFollow(FOLLOW_formalParameter_in_formalParameters2992);
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

            match(input,RPAREN,FOLLOW_RPAREN_in_formalParameters3003); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:396:1: formalParameter returns [JFXVar var] : name typeReference ;
    public final JFXVar formalParameter() throws RecognitionException {
        JFXVar var = null;

        name_return name35 = null;

        JFXType typeReference36 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:397:2: ( name typeReference )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:397:4: name typeReference
            {
            pushFollow(FOLLOW_name_in_formalParameter3018);
            name35=name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_formalParameter3020);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:399:1: block returns [JCBlock value] : LBRACE ( statement | expression SEMI )* RBRACE ;
    public final JCBlock block() throws RecognitionException {
        JCBlock value = null;

        Token LBRACE39=null;
        JCStatement statement37 = null;

        JCExpression expression38 = null;


         ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>();
        	 	
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:402:2: ( LBRACE ( statement | expression SEMI )* RBRACE )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:402:4: LBRACE ( statement | expression SEMI )* RBRACE
            {
            LBRACE39=(Token)input.LT(1);
            match(input,LBRACE,FOLLOW_LBRACE_in_block3046); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:403:4: ( statement | expression SEMI )*
            loop20:
            do {
                int alt20=3;
                int LA20_0 = input.LA(1);

                if ( (LA20_0==ABSTRACT||LA20_0==BREAK||(LA20_0>=RETURN && LA20_0<=VAR)||(LA20_0>=WHILE && LA20_0<=CONTINUE)||LA20_0==TRY||(LA20_0>=PRIVATE && LA20_0<=READONLY)||LA20_0==SEMI) ) {
                    alt20=1;
                }
                else if ( ((LA20_0>=POUND && LA20_0<=TYPEOF)||LA20_0==IF||(LA20_0>=THIS && LA20_0<=FALSE)||(LA20_0>=NOT && LA20_0<=NEW)||LA20_0==SUPER||(LA20_0>=SIZEOF && LA20_0<=LPAREN)||(LA20_0>=PLUSPLUS && LA20_0<=SUBSUB)||(LA20_0>=QUES && LA20_0<=STRING_LITERAL)||(LA20_0>=QUOTE_LBRACE_STRING_LITERAL && LA20_0<=LBRACE)||(LA20_0>=QUOTED_IDENTIFIER && LA20_0<=INTEGER_LITERAL)||LA20_0==FLOATING_POINT_LITERAL||LA20_0==IDENTIFIER) ) {
                    alt20=2;
                }


                switch (alt20) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:403:9: statement
            	    {
            	    pushFollow(FOLLOW_statement_in_block3056);
            	    statement37=statement();
            	    _fsp--;

            	     stats.append(statement37); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:404:9: expression SEMI
            	    {
            	    pushFollow(FOLLOW_expression_in_block3071);
            	    expression38=expression();
            	    _fsp--;

            	    match(input,SEMI,FOLLOW_SEMI_in_block3073); 
            	     stats.append(F.Exec(expression38)); 

            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);

            match(input,RBRACE,FOLLOW_RBRACE_in_block3089); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:408:1: blockExpression returns [JFXBlockExpression expr = null] : LBRACE ( statements[stats] )? RBRACE ;
    public final JFXBlockExpression blockExpression() throws RecognitionException {
        JFXBlockExpression expr =  null;

        Token LBRACE40=null;
        JCExpression statements41 = null;


         ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>(); 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:410:2: ( LBRACE ( statements[stats] )? RBRACE )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:410:4: LBRACE ( statements[stats] )? RBRACE
            {
            LBRACE40=(Token)input.LT(1);
            match(input,LBRACE,FOLLOW_LBRACE_in_blockExpression3113); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:410:11: ( statements[stats] )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( ((LA21_0>=POUND && LA21_0<=TYPEOF)||LA21_0==ABSTRACT||LA21_0==BREAK||(LA21_0>=RETURN && LA21_0<=VAR)||LA21_0==IF||(LA21_0>=THIS && LA21_0<=FALSE)||(LA21_0>=WHILE && LA21_0<=CONTINUE)||LA21_0==TRY||(LA21_0>=NOT && LA21_0<=READONLY)||LA21_0==SUPER||(LA21_0>=SIZEOF && LA21_0<=LPAREN)||LA21_0==SEMI||(LA21_0>=PLUSPLUS && LA21_0<=SUBSUB)||(LA21_0>=QUES && LA21_0<=STRING_LITERAL)||(LA21_0>=QUOTE_LBRACE_STRING_LITERAL && LA21_0<=LBRACE)||(LA21_0>=QUOTED_IDENTIFIER && LA21_0<=INTEGER_LITERAL)||LA21_0==FLOATING_POINT_LITERAL||LA21_0==IDENTIFIER) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:410:12: statements[stats]
                    {
                    pushFollow(FOLLOW_statements_in_blockExpression3116);
                    statements41=statements(stats);
                    _fsp--;


                    }
                    break;

            }

            match(input,RBRACE,FOLLOW_RBRACE_in_blockExpression3121); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:413:1: statements[ListBuffer<JCStatement> stats] returns [JCExpression expr = null] : ( statement (sts1= statements[stats] )? | expression ( SEMI (stsn= statements[stats] | ) | ) );
    public final JCExpression statements(ListBuffer<JCStatement> stats) throws RecognitionException {
        JCExpression expr =  null;

        JCExpression sts1 = null;

        JCExpression stsn = null;

        JCStatement statement42 = null;

        JCExpression expression43 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:414:2: ( statement (sts1= statements[stats] )? | expression ( SEMI (stsn= statements[stats] | ) | ) )
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==ABSTRACT||LA25_0==BREAK||(LA25_0>=RETURN && LA25_0<=VAR)||(LA25_0>=WHILE && LA25_0<=CONTINUE)||LA25_0==TRY||(LA25_0>=PRIVATE && LA25_0<=READONLY)||LA25_0==SEMI) ) {
                alt25=1;
            }
            else if ( ((LA25_0>=POUND && LA25_0<=TYPEOF)||LA25_0==IF||(LA25_0>=THIS && LA25_0<=FALSE)||(LA25_0>=NOT && LA25_0<=NEW)||LA25_0==SUPER||(LA25_0>=SIZEOF && LA25_0<=LPAREN)||(LA25_0>=PLUSPLUS && LA25_0<=SUBSUB)||(LA25_0>=QUES && LA25_0<=STRING_LITERAL)||(LA25_0>=QUOTE_LBRACE_STRING_LITERAL && LA25_0<=LBRACE)||(LA25_0>=QUOTED_IDENTIFIER && LA25_0<=INTEGER_LITERAL)||LA25_0==FLOATING_POINT_LITERAL||LA25_0==IDENTIFIER) ) {
                alt25=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("413:1: statements[ListBuffer<JCStatement> stats] returns [JCExpression expr = null] : ( statement (sts1= statements[stats] )? | expression ( SEMI (stsn= statements[stats] | ) | ) );", 25, 0, input);

                throw nvae;
            }
            switch (alt25) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:414:4: statement (sts1= statements[stats] )?
                    {
                    pushFollow(FOLLOW_statement_in_statements3140);
                    statement42=statement();
                    _fsp--;

                     stats.append(statement42); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:415:3: (sts1= statements[stats] )?
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( ((LA22_0>=POUND && LA22_0<=TYPEOF)||LA22_0==ABSTRACT||LA22_0==BREAK||(LA22_0>=RETURN && LA22_0<=VAR)||LA22_0==IF||(LA22_0>=THIS && LA22_0<=FALSE)||(LA22_0>=WHILE && LA22_0<=CONTINUE)||LA22_0==TRY||(LA22_0>=NOT && LA22_0<=READONLY)||LA22_0==SUPER||(LA22_0>=SIZEOF && LA22_0<=LPAREN)||LA22_0==SEMI||(LA22_0>=PLUSPLUS && LA22_0<=SUBSUB)||(LA22_0>=QUES && LA22_0<=STRING_LITERAL)||(LA22_0>=QUOTE_LBRACE_STRING_LITERAL && LA22_0<=LBRACE)||(LA22_0>=QUOTED_IDENTIFIER && LA22_0<=INTEGER_LITERAL)||LA22_0==FLOATING_POINT_LITERAL||LA22_0==IDENTIFIER) ) {
                        alt22=1;
                    }
                    switch (alt22) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:415:4: sts1= statements[stats]
                            {
                            pushFollow(FOLLOW_statements_in_statements3152);
                            sts1=statements(stats);
                            _fsp--;

                             expr = sts1; 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:416:4: expression ( SEMI (stsn= statements[stats] | ) | )
                    {
                    pushFollow(FOLLOW_expression_in_statements3164);
                    expression43=expression();
                    _fsp--;

                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:417:10: ( SEMI (stsn= statements[stats] | ) | )
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
                            new NoViableAltException("417:10: ( SEMI (stsn= statements[stats] | ) | )", 24, 0, input);

                        throw nvae;
                    }
                    switch (alt24) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:417:11: SEMI (stsn= statements[stats] | )
                            {
                            match(input,SEMI,FOLLOW_SEMI_in_statements3176); 
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:417:17: (stsn= statements[stats] | )
                            int alt23=2;
                            int LA23_0 = input.LA(1);

                            if ( ((LA23_0>=POUND && LA23_0<=TYPEOF)||LA23_0==ABSTRACT||LA23_0==BREAK||(LA23_0>=RETURN && LA23_0<=VAR)||LA23_0==IF||(LA23_0>=THIS && LA23_0<=FALSE)||(LA23_0>=WHILE && LA23_0<=CONTINUE)||LA23_0==TRY||(LA23_0>=NOT && LA23_0<=READONLY)||LA23_0==SUPER||(LA23_0>=SIZEOF && LA23_0<=LPAREN)||LA23_0==SEMI||(LA23_0>=PLUSPLUS && LA23_0<=SUBSUB)||(LA23_0>=QUES && LA23_0<=STRING_LITERAL)||(LA23_0>=QUOTE_LBRACE_STRING_LITERAL && LA23_0<=LBRACE)||(LA23_0>=QUOTED_IDENTIFIER && LA23_0<=INTEGER_LITERAL)||LA23_0==FLOATING_POINT_LITERAL||LA23_0==IDENTIFIER) ) {
                                alt23=1;
                            }
                            else if ( (LA23_0==RBRACE) ) {
                                alt23=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("417:17: (stsn= statements[stats] | )", 23, 0, input);

                                throw nvae;
                            }
                            switch (alt23) {
                                case 1 :
                                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:417:21: stsn= statements[stats]
                                    {
                                     stats.append(F.Exec(expression43)); 
                                    pushFollow(FOLLOW_statements_in_statements3199);
                                    stsn=statements(stats);
                                    _fsp--;

                                     expr = stsn; 

                                    }
                                    break;
                                case 2 :
                                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:419:30: 
                                    {
                                     expr = expression43; 

                                    }
                                    break;

                            }


                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:421:20: 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:424:1: statement returns [JCStatement value] : ( variableDeclaration SEMI | functionDefinition | WHILE LPAREN expression RPAREN block | BREAK SEMI | CONTINUE SEMI | THROW expression SEMI | returnStatement SEMI | tryStatement | SEMI );
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
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:425:2: ( variableDeclaration SEMI | functionDefinition | WHILE LPAREN expression RPAREN block | BREAK SEMI | CONTINUE SEMI | THROW expression SEMI | returnStatement SEMI | tryStatement | SEMI )
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
                    new NoViableAltException("424:1: statement returns [JCStatement value] : ( variableDeclaration SEMI | functionDefinition | WHILE LPAREN expression RPAREN block | BREAK SEMI | CONTINUE SEMI | THROW expression SEMI | returnStatement SEMI | tryStatement | SEMI );", 26, 0, input);

                throw nvae;
            }

            switch (alt26) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:425:4: variableDeclaration SEMI
                    {
                    pushFollow(FOLLOW_variableDeclaration_in_statement3257);
                    variableDeclaration44=variableDeclaration();
                    _fsp--;

                    match(input,SEMI,FOLLOW_SEMI_in_statement3259); 
                     value = variableDeclaration44; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:426:4: functionDefinition
                    {
                    pushFollow(FOLLOW_functionDefinition_in_statement3267);
                    functionDefinition45=functionDefinition();
                    _fsp--;

                     value = functionDefinition45; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:427:11: WHILE LPAREN expression RPAREN block
                    {
                    WHILE46=(Token)input.LT(1);
                    match(input,WHILE,FOLLOW_WHILE_in_statement3283); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_statement3285); 
                    pushFollow(FOLLOW_expression_in_statement3287);
                    expression47=expression();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_statement3289); 
                    pushFollow(FOLLOW_block_in_statement3291);
                    block48=block();
                    _fsp--;

                     value = F.at(pos(WHILE46)).WhileLoop(expression47, block48); 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:428:4: BREAK SEMI
                    {
                    BREAK49=(Token)input.LT(1);
                    match(input,BREAK,FOLLOW_BREAK_in_statement3298); 
                    match(input,SEMI,FOLLOW_SEMI_in_statement3300); 
                     value = F.at(pos(BREAK49)).Break(null); 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:429:4: CONTINUE SEMI
                    {
                    CONTINUE50=(Token)input.LT(1);
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_statement3313); 
                    match(input,SEMI,FOLLOW_SEMI_in_statement3316); 
                     value = F.at(pos(CONTINUE50)).Continue(null); 

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:430:11: THROW expression SEMI
                    {
                    THROW51=(Token)input.LT(1);
                    match(input,THROW,FOLLOW_THROW_in_statement3334); 
                    pushFollow(FOLLOW_expression_in_statement3336);
                    expression52=expression();
                    _fsp--;

                    match(input,SEMI,FOLLOW_SEMI_in_statement3338); 
                     value = F.at(pos(THROW51)).Throw(expression52); 

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:431:11: returnStatement SEMI
                    {
                    pushFollow(FOLLOW_returnStatement_in_statement3358);
                    returnStatement53=returnStatement();
                    _fsp--;

                    match(input,SEMI,FOLLOW_SEMI_in_statement3360); 
                     value = returnStatement53; 

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:432:11: tryStatement
                    {
                    pushFollow(FOLLOW_tryStatement_in_statement3376);
                    tryStatement54=tryStatement();
                    _fsp--;

                     value = tryStatement54; 

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:433:4: SEMI
                    {
                    SEMI55=(Token)input.LT(1);
                    match(input,SEMI,FOLLOW_SEMI_in_statement3387); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:435:1: variableDeclaration returns [JCStatement value] : VAR name typeReference ( EQ bindOpt expression | ) ;
    public final JCStatement variableDeclaration() throws RecognitionException {
        JCStatement value = null;

        Token VAR56=null;
        name_return name57 = null;

        JFXType typeReference58 = null;

        JCExpression expression59 = null;

        JavafxBindStatus bindOpt60 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:436:2: ( VAR name typeReference ( EQ bindOpt expression | ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:436:4: VAR name typeReference ( EQ bindOpt expression | )
            {
            VAR56=(Token)input.LT(1);
            match(input,VAR,FOLLOW_VAR_in_variableDeclaration3417); 
            pushFollow(FOLLOW_name_in_variableDeclaration3420);
            name57=name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_variableDeclaration3423);
            typeReference58=typeReference();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:437:6: ( EQ bindOpt expression | )
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
                    new NoViableAltException("437:6: ( EQ bindOpt expression | )", 27, 0, input);

                throw nvae;
            }
            switch (alt27) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:437:8: EQ bindOpt expression
                    {
                    match(input,EQ,FOLLOW_EQ_in_variableDeclaration3434); 
                    pushFollow(FOLLOW_bindOpt_in_variableDeclaration3436);
                    bindOpt60=bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_variableDeclaration3439);
                    expression59=expression();
                    _fsp--;

                     value = F.at(pos(VAR56)).VarInit(name57.value, typeReference58, 
                    	    							expression59, bindOpt60); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:439:13: 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:442:1: bindOpt returns [JavafxBindStatus status = UNBOUND] : ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? ;
    public final JavafxBindStatus bindOpt() throws RecognitionException {
        JavafxBindStatus status =  UNBOUND;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:443:2: ( ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:443:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:443:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
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
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:443:6: BIND ( LAZY )?
                    {
                    match(input,BIND,FOLLOW_BIND_in_bindOpt3489); 
                     status = UNIDIBIND; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:444:8: ( LAZY )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==LAZY) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:444:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3505); 
                             status = LAZY_UNIDIBIND; 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:445:6: STAYS ( LAZY )?
                    {
                    match(input,STAYS,FOLLOW_STAYS_in_bindOpt3520); 
                     status = UNIDIBIND; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:446:8: ( LAZY )?
                    int alt29=2;
                    int LA29_0 = input.LA(1);

                    if ( (LA29_0==LAZY) ) {
                        alt29=1;
                    }
                    switch (alt29) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:446:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3536); 
                             status = LAZY_UNIDIBIND; 

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:447:6: TIE ( LAZY )?
                    {
                    match(input,TIE,FOLLOW_TIE_in_bindOpt3551); 
                     status = BIDIBIND; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:448:8: ( LAZY )?
                    int alt30=2;
                    int LA30_0 = input.LA(1);

                    if ( (LA30_0==LAZY) ) {
                        alt30=1;
                    }
                    switch (alt30) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:448:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3567); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:450:1: returnStatement returns [JCStatement value] : RETURN ( expression )? ;
    public final JCStatement returnStatement() throws RecognitionException {
        JCStatement value = null;

        Token RETURN62=null;
        JCExpression expression61 = null;


         JCExpression expr = null; 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:452:2: ( RETURN ( expression )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:452:4: RETURN ( expression )?
            {
            RETURN62=(Token)input.LT(1);
            match(input,RETURN,FOLLOW_RETURN_in_returnStatement3601); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:452:11: ( expression )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( ((LA32_0>=POUND && LA32_0<=TYPEOF)||LA32_0==IF||(LA32_0>=THIS && LA32_0<=FALSE)||(LA32_0>=NOT && LA32_0<=NEW)||LA32_0==SUPER||(LA32_0>=SIZEOF && LA32_0<=LPAREN)||(LA32_0>=PLUSPLUS && LA32_0<=SUBSUB)||(LA32_0>=QUES && LA32_0<=STRING_LITERAL)||(LA32_0>=QUOTE_LBRACE_STRING_LITERAL && LA32_0<=LBRACE)||(LA32_0>=QUOTED_IDENTIFIER && LA32_0<=INTEGER_LITERAL)||LA32_0==FLOATING_POINT_LITERAL||LA32_0==IDENTIFIER) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:452:12: expression
                    {
                    pushFollow(FOLLOW_expression_in_returnStatement3604);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:455:1: tryStatement returns [JCStatement value] : TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? ) ;
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
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:460:2: ( TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:460:4: TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )
            {
            TRY64=(Token)input.LT(1);
            match(input,TRY,FOLLOW_TRY_in_tryStatement3647); 
            pushFollow(FOLLOW_block_in_tryStatement3653);
            tb=block();
            _fsp--;

             body = tb; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:461:5: ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )
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
                    new NoViableAltException("461:5: ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )", 35, 0, input);

                throw nvae;
            }
            switch (alt35) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:461:7: FINALLY fb1= block
                    {
                    match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement3666); 
                    pushFollow(FOLLOW_block_in_tryStatement3672);
                    fb1=block();
                    _fsp--;

                     finalBlock = fb1; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:462:10: ( catchClause )+ ( FINALLY fb2= block )?
                    {
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:462:10: ( catchClause )+
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
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:462:11: catchClause
                    	    {
                    	    pushFollow(FOLLOW_catchClause_in_tryStatement3688);
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

                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:463:10: ( FINALLY fb2= block )?
                    int alt34=2;
                    int LA34_0 = input.LA(1);

                    if ( (LA34_0==FINALLY) ) {
                        alt34=1;
                    }
                    switch (alt34) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:463:11: FINALLY fb2= block
                            {
                            match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement3711); 
                            pushFollow(FOLLOW_block_in_tryStatement3716);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:466:1: catchClause returns [JCCatch value] : CATCH LPAREN name ( COLON identifier )? RPAREN block ;
    public final JCCatch catchClause() throws RecognitionException {
        JCCatch value = null;

        Token CATCH67=null;
        name_return name65 = null;

        JCIdent identifier66 = null;

        JCBlock block68 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:467:2: ( CATCH LPAREN name ( COLON identifier )? RPAREN block )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:467:4: CATCH LPAREN name ( COLON identifier )? RPAREN block
            {
            CATCH67=(Token)input.LT(1);
            match(input,CATCH,FOLLOW_CATCH_in_catchClause3755); 
            match(input,LPAREN,FOLLOW_LPAREN_in_catchClause3758); 
            pushFollow(FOLLOW_name_in_catchClause3761);
            name65=name();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:468:4: ( COLON identifier )?
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==COLON) ) {
                alt36=1;
            }
            switch (alt36) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:468:5: COLON identifier
                    {
                    match(input,COLON,FOLLOW_COLON_in_catchClause3769); 
                    pushFollow(FOLLOW_identifier_in_catchClause3771);
                    identifier66=identifier();
                    _fsp--;


                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_catchClause3780); 
            pushFollow(FOLLOW_block_in_catchClause3784);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:473:1: expression returns [JCExpression expr] : ( ifExpression | suffixedExpression );
    public final JCExpression expression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression ifExpression69 = null;

        JCExpression suffixedExpression70 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:474:9: ( ifExpression | suffixedExpression )
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==IF) ) {
                alt37=1;
            }
            else if ( ((LA37_0>=POUND && LA37_0<=TYPEOF)||(LA37_0>=THIS && LA37_0<=FALSE)||(LA37_0>=NOT && LA37_0<=NEW)||LA37_0==SUPER||(LA37_0>=SIZEOF && LA37_0<=LPAREN)||(LA37_0>=PLUSPLUS && LA37_0<=SUBSUB)||(LA37_0>=QUES && LA37_0<=STRING_LITERAL)||(LA37_0>=QUOTE_LBRACE_STRING_LITERAL && LA37_0<=LBRACE)||(LA37_0>=QUOTED_IDENTIFIER && LA37_0<=INTEGER_LITERAL)||LA37_0==FLOATING_POINT_LITERAL||LA37_0==IDENTIFIER) ) {
                alt37=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("473:1: expression returns [JCExpression expr] : ( ifExpression | suffixedExpression );", 37, 0, input);

                throw nvae;
            }
            switch (alt37) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:474:11: ifExpression
                    {
                    pushFollow(FOLLOW_ifExpression_in_expression3812);
                    ifExpression69=ifExpression();
                    _fsp--;

                     expr = ifExpression69; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:475:11: suffixedExpression
                    {
                    pushFollow(FOLLOW_suffixedExpression_in_expression3835);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:478:1: ifExpression returns [JCExpression expr] : IF econd= expression THEN ethen= expression ELSE eelse= expression ;
    public final JCExpression ifExpression() throws RecognitionException {
        JCExpression expr = null;

        Token IF71=null;
        JCExpression econd = null;

        JCExpression ethen = null;

        JCExpression eelse = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:479:2: ( IF econd= expression THEN ethen= expression ELSE eelse= expression )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:479:4: IF econd= expression THEN ethen= expression ELSE eelse= expression
            {
            IF71=(Token)input.LT(1);
            match(input,IF,FOLLOW_IF_in_ifExpression3866); 
            pushFollow(FOLLOW_expression_in_ifExpression3870);
            econd=expression();
            _fsp--;

            match(input,THEN,FOLLOW_THEN_in_ifExpression3874); 
            pushFollow(FOLLOW_expression_in_ifExpression3879);
            ethen=expression();
            _fsp--;

            match(input,ELSE,FOLLOW_ELSE_in_ifExpression3887); 
            pushFollow(FOLLOW_expression_in_ifExpression3892);
            eelse=expression();
            _fsp--;

             expr = F.at(pos(IF71)).Conditional(econd, ethen, eelse); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:482:1: suffixedExpression returns [JCExpression expr] : e1= assignmentExpression ( PLUSPLUS | SUBSUB )? ;
    public final JCExpression suffixedExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:483:2: (e1= assignmentExpression ( PLUSPLUS | SUBSUB )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:483:4: e1= assignmentExpression ( PLUSPLUS | SUBSUB )?
            {
            pushFollow(FOLLOW_assignmentExpression_in_suffixedExpression3915);
            e1=assignmentExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:484:5: ( PLUSPLUS | SUBSUB )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==PLUSPLUS||LA38_0==SUBSUB) ) {
                alt38=1;
            }
            switch (alt38) {
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
                        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_suffixedExpression3926);    throw mse;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:486:1: assignmentExpression returns [JCExpression expr] : e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? ;
    public final JCExpression assignmentExpression() throws RecognitionException {
        JCExpression expr = null;

        Token EQ72=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:487:2: (e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:487:4: e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )?
            {
            pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression3955);
            e1=assignmentOpExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:488:5: ( EQ e2= assignmentOpExpression )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==EQ) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:488:9: EQ e2= assignmentOpExpression
                    {
                    EQ72=(Token)input.LT(1);
                    match(input,EQ,FOLLOW_EQ_in_assignmentExpression3970); 
                    pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression3976);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:489:1: assignmentOpExpression returns [JCExpression expr] : e1= andExpression ( assignmentOperator e2= andExpression )? ;
    public final JCExpression assignmentOpExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;

        JCExpression e2 = null;

        int assignmentOperator73 = 0;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:490:2: (e1= andExpression ( assignmentOperator e2= andExpression )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:490:4: e1= andExpression ( assignmentOperator e2= andExpression )?
            {
            pushFollow(FOLLOW_andExpression_in_assignmentOpExpression4003);
            e1=andExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:491:5: ( assignmentOperator e2= andExpression )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( ((LA40_0>=PLUSEQ && LA40_0<=PERCENTEQ)) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:491:9: assignmentOperator e2= andExpression
                    {
                    pushFollow(FOLLOW_assignmentOperator_in_assignmentOpExpression4019);
                    assignmentOperator73=assignmentOperator();
                    _fsp--;

                    pushFollow(FOLLOW_andExpression_in_assignmentOpExpression4025);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:493:1: andExpression returns [JCExpression expr] : e1= orExpression ( AND e2= orExpression )* ;
    public final JCExpression andExpression() throws RecognitionException {
        JCExpression expr = null;

        Token AND74=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:494:2: (e1= orExpression ( AND e2= orExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:494:4: e1= orExpression ( AND e2= orExpression )*
            {
            pushFollow(FOLLOW_orExpression_in_andExpression4051);
            e1=orExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:495:5: ( AND e2= orExpression )*
            loop41:
            do {
                int alt41=2;
                int LA41_0 = input.LA(1);

                if ( (LA41_0==AND) ) {
                    alt41=1;
                }


                switch (alt41) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:495:9: AND e2= orExpression
            	    {
            	    AND74=(Token)input.LT(1);
            	    match(input,AND,FOLLOW_AND_in_andExpression4067); 
            	    pushFollow(FOLLOW_orExpression_in_andExpression4073);
            	    e2=orExpression();
            	    _fsp--;

            	     expr = F.at(pos(AND74)).Binary(JCTree.AND, expr, e2); 

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
    // $ANTLR end andExpression


    // $ANTLR start orExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:496:1: orExpression returns [JCExpression expr] : e1= instanceOfExpression ( OR e2= instanceOfExpression )* ;
    public final JCExpression orExpression() throws RecognitionException {
        JCExpression expr = null;

        Token OR75=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:497:2: (e1= instanceOfExpression ( OR e2= instanceOfExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:497:4: e1= instanceOfExpression ( OR e2= instanceOfExpression )*
            {
            pushFollow(FOLLOW_instanceOfExpression_in_orExpression4101);
            e1=instanceOfExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:498:5: ( OR e2= instanceOfExpression )*
            loop42:
            do {
                int alt42=2;
                int LA42_0 = input.LA(1);

                if ( (LA42_0==OR) ) {
                    alt42=1;
                }


                switch (alt42) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:498:9: OR e2= instanceOfExpression
            	    {
            	    OR75=(Token)input.LT(1);
            	    match(input,OR,FOLLOW_OR_in_orExpression4116); 
            	    pushFollow(FOLLOW_instanceOfExpression_in_orExpression4122);
            	    e2=instanceOfExpression();
            	    _fsp--;

            	     expr = F.at(pos(OR75)).Binary(JCTree.OR, expr, e2); 

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
    // $ANTLR end orExpression


    // $ANTLR start instanceOfExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:499:1: instanceOfExpression returns [JCExpression expr] : e1= relationalExpression ( INSTANCEOF identifier )? ;
    public final JCExpression instanceOfExpression() throws RecognitionException {
        JCExpression expr = null;

        Token INSTANCEOF76=null;
        JCExpression e1 = null;

        JCIdent identifier77 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:500:2: (e1= relationalExpression ( INSTANCEOF identifier )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:500:4: e1= relationalExpression ( INSTANCEOF identifier )?
            {
            pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression4150);
            e1=relationalExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:501:5: ( INSTANCEOF identifier )?
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==INSTANCEOF) ) {
                alt43=1;
            }
            switch (alt43) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:501:9: INSTANCEOF identifier
                    {
                    INSTANCEOF76=(Token)input.LT(1);
                    match(input,INSTANCEOF,FOLLOW_INSTANCEOF_in_instanceOfExpression4165); 
                    pushFollow(FOLLOW_identifier_in_instanceOfExpression4167);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:503:1: relationalExpression returns [JCExpression expr] : e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* ;
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
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:504:2: (e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:504:4: e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            {
            pushFollow(FOLLOW_additiveExpression_in_relationalExpression4195);
            e1=additiveExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:505:5: ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            loop44:
            do {
                int alt44=8;
                switch ( input.LA(1) ) {
                case LTGT:
                    {
                    alt44=1;
                    }
                    break;
                case EQEQ:
                    {
                    alt44=2;
                    }
                    break;
                case LTEQ:
                    {
                    alt44=3;
                    }
                    break;
                case GTEQ:
                    {
                    alt44=4;
                    }
                    break;
                case LT:
                    {
                    alt44=5;
                    }
                    break;
                case GT:
                    {
                    alt44=6;
                    }
                    break;
                case IN:
                    {
                    alt44=7;
                    }
                    break;

                }

                switch (alt44) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:505:9: LTGT e= additiveExpression
            	    {
            	    LTGT78=(Token)input.LT(1);
            	    match(input,LTGT,FOLLOW_LTGT_in_relationalExpression4211); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4217);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LTGT78)).Binary(JCTree.NE, expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:506:9: EQEQ e= additiveExpression
            	    {
            	    EQEQ79=(Token)input.LT(1);
            	    match(input,EQEQ,FOLLOW_EQEQ_in_relationalExpression4231); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4237);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(EQEQ79)).Binary(JCTree.EQ, expr, e); 

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:507:9: LTEQ e= additiveExpression
            	    {
            	    LTEQ80=(Token)input.LT(1);
            	    match(input,LTEQ,FOLLOW_LTEQ_in_relationalExpression4251); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4257);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LTEQ80)).Binary(JCTree.LE, expr, e); 

            	    }
            	    break;
            	case 4 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:508:9: GTEQ e= additiveExpression
            	    {
            	    GTEQ81=(Token)input.LT(1);
            	    match(input,GTEQ,FOLLOW_GTEQ_in_relationalExpression4271); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4277);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(GTEQ81)).Binary(JCTree.GE, expr, e); 

            	    }
            	    break;
            	case 5 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:509:9: LT e= additiveExpression
            	    {
            	    LT82=(Token)input.LT(1);
            	    match(input,LT,FOLLOW_LT_in_relationalExpression4291); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4299);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LT82))  .Binary(JCTree.LT, expr, e); 

            	    }
            	    break;
            	case 6 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:510:9: GT e= additiveExpression
            	    {
            	    GT83=(Token)input.LT(1);
            	    match(input,GT,FOLLOW_GT_in_relationalExpression4313); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4321);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(GT83))  .Binary(JCTree.GT, expr, e); 

            	    }
            	    break;
            	case 7 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:511:9: IN e= additiveExpression
            	    {
            	    IN84=(Token)input.LT(1);
            	    match(input,IN,FOLLOW_IN_in_relationalExpression4335); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4343);
            	    e=additiveExpression();
            	    _fsp--;

            	     /* expr = F.at(pos(IN84  )).Binary(JavaFXTag.IN, expr, $e2.expr); */ 

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
    // $ANTLR end relationalExpression


    // $ANTLR start additiveExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:513:1: additiveExpression returns [JCExpression expr] : e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* ;
    public final JCExpression additiveExpression() throws RecognitionException {
        JCExpression expr = null;

        Token PLUS85=null;
        Token SUB86=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:514:2: (e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:514:4: e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            {
            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4372);
            e1=multiplicativeExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:515:5: ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            loop45:
            do {
                int alt45=3;
                int LA45_0 = input.LA(1);

                if ( (LA45_0==PLUS) ) {
                    alt45=1;
                }
                else if ( (LA45_0==SUB) ) {
                    alt45=2;
                }


                switch (alt45) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:515:9: PLUS e= multiplicativeExpression
            	    {
            	    PLUS85=(Token)input.LT(1);
            	    match(input,PLUS,FOLLOW_PLUS_in_additiveExpression4387); 
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4393);
            	    e=multiplicativeExpression();
            	    _fsp--;

            	     expr = F.at(pos(PLUS85)).Binary(JCTree.PLUS , expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:516:9: SUB e= multiplicativeExpression
            	    {
            	    SUB86=(Token)input.LT(1);
            	    match(input,SUB,FOLLOW_SUB_in_additiveExpression4406); 
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4413);
            	    e=multiplicativeExpression();
            	    _fsp--;

            	     expr = F.at(pos(SUB86)) .Binary(JCTree.MINUS, expr, e); 

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
    // $ANTLR end additiveExpression


    // $ANTLR start multiplicativeExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:518:1: multiplicativeExpression returns [JCExpression expr] : e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* ;
    public final JCExpression multiplicativeExpression() throws RecognitionException {
        JCExpression expr = null;

        Token STAR87=null;
        Token SLASH88=null;
        Token PERCENT89=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:519:2: (e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:519:4: e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            {
            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4441);
            e1=unaryExpression();
            _fsp--;

             expr = e1; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:520:5: ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            loop46:
            do {
                int alt46=4;
                switch ( input.LA(1) ) {
                case STAR:
                    {
                    alt46=1;
                    }
                    break;
                case SLASH:
                    {
                    alt46=2;
                    }
                    break;
                case PERCENT:
                    {
                    alt46=3;
                    }
                    break;

                }

                switch (alt46) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:520:9: STAR e= unaryExpression
            	    {
            	    STAR87=(Token)input.LT(1);
            	    match(input,STAR,FOLLOW_STAR_in_multiplicativeExpression4457); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4464);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(STAR87))   .Binary(JCTree.MUL  , expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:521:9: SLASH e= unaryExpression
            	    {
            	    SLASH88=(Token)input.LT(1);
            	    match(input,SLASH,FOLLOW_SLASH_in_multiplicativeExpression4478); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4484);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(SLASH88))  .Binary(JCTree.DIV  , expr, e); 

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:522:9: PERCENT e= unaryExpression
            	    {
            	    PERCENT89=(Token)input.LT(1);
            	    match(input,PERCENT,FOLLOW_PERCENT_in_multiplicativeExpression4498); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4502);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(PERCENT89)).Binary(JCTree.MOD  , expr, e); 

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
    // $ANTLR end multiplicativeExpression


    // $ANTLR start unaryExpression
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:524:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );
    public final JCExpression unaryExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression postfixExpression90 = null;

        int unaryOperator91 = 0;

        JCExpression postfixExpression92 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:525:2: ( postfixExpression | unaryOperator postfixExpression )
            int alt47=2;
            int LA47_0 = input.LA(1);

            if ( ((LA47_0>=THIS && LA47_0<=FALSE)||LA47_0==NEW||LA47_0==SUPER||LA47_0==LPAREN||LA47_0==STRING_LITERAL||(LA47_0>=QUOTE_LBRACE_STRING_LITERAL && LA47_0<=LBRACE)||(LA47_0>=QUOTED_IDENTIFIER && LA47_0<=INTEGER_LITERAL)||LA47_0==FLOATING_POINT_LITERAL||LA47_0==IDENTIFIER) ) {
                alt47=1;
            }
            else if ( ((LA47_0>=POUND && LA47_0<=TYPEOF)||LA47_0==NOT||(LA47_0>=SIZEOF && LA47_0<=REVERSE)||(LA47_0>=PLUSPLUS && LA47_0<=SUBSUB)||LA47_0==QUES) ) {
                alt47=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("524:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );", 47, 0, input);

                throw nvae;
            }
            switch (alt47) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:525:4: postfixExpression
                    {
                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression4532);
                    postfixExpression90=postfixExpression();
                    _fsp--;

                     expr = postfixExpression90; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:526:4: unaryOperator postfixExpression
                    {
                    pushFollow(FOLLOW_unaryOperator_in_unaryExpression4543);
                    unaryOperator91=unaryOperator();
                    _fsp--;

                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression4547);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:528:1: postfixExpression returns [JCExpression expr] : primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* ;
    public final JCExpression postfixExpression() throws RecognitionException {
        JCExpression expr = null;

        Token DOT94=null;
        Token LPAREN95=null;
        name_return name1 = null;

        JCExpression primaryExpression93 = null;

        ListBuffer<JCExpression> expressionListOpt96 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:529:2: ( primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:529:4: primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            {
            pushFollow(FOLLOW_primaryExpression_in_postfixExpression4567);
            primaryExpression93=primaryExpression();
            _fsp--;

             expr = primaryExpression93; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:530:5: ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            loop51:
            do {
                int alt51=3;
                int LA51_0 = input.LA(1);

                if ( (LA51_0==DOT) ) {
                    alt51=1;
                }
                else if ( (LA51_0==LBRACKET) ) {
                    alt51=2;
                }


                switch (alt51) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:530:7: DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    {
            	    DOT94=(Token)input.LT(1);
            	    match(input,DOT,FOLLOW_DOT_in_postfixExpression4582); 
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:530:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    int alt49=2;
            	    int LA49_0 = input.LA(1);

            	    if ( (LA49_0==CLASS) ) {
            	        alt49=1;
            	    }
            	    else if ( (LA49_0==QUOTED_IDENTIFIER||LA49_0==IDENTIFIER) ) {
            	        alt49=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("530:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )", 49, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt49) {
            	        case 1 :
            	            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:530:13: CLASS
            	            {
            	            match(input,CLASS,FOLLOW_CLASS_in_postfixExpression4586); 

            	            }
            	            break;
            	        case 2 :
            	            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:531:13: name1= name ( LPAREN expressionListOpt RPAREN )*
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression4610);
            	            name1=name();
            	            _fsp--;

            	             expr = F.at(pos(DOT94)).Select(expr, name1.value); 
            	            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:532:14: ( LPAREN expressionListOpt RPAREN )*
            	            loop48:
            	            do {
            	                int alt48=2;
            	                int LA48_0 = input.LA(1);

            	                if ( (LA48_0==LPAREN) ) {
            	                    alt48=1;
            	                }


            	                switch (alt48) {
            	            	case 1 :
            	            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:532:16: LPAREN expressionListOpt RPAREN
            	            	    {
            	            	    LPAREN95=(Token)input.LT(1);
            	            	    match(input,LPAREN,FOLLOW_LPAREN_in_postfixExpression4635); 
            	            	    pushFollow(FOLLOW_expressionListOpt_in_postfixExpression4637);
            	            	    expressionListOpt96=expressionListOpt();
            	            	    _fsp--;

            	            	    match(input,RPAREN,FOLLOW_RPAREN_in_postfixExpression4639); 
            	            	     expr = F.at(pos(LPAREN95)).Apply(null, expr, expressionListOpt96.toList()); 

            	            	    }
            	            	    break;

            	            	default :
            	            	    break loop48;
            	                }
            	            } while (true);


            	            }
            	            break;

            	    }


            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:534:7: LBRACKET ( name BAR )? expression RBRACKET
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_postfixExpression4671); 
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:534:16: ( name BAR )?
            	    int alt50=2;
            	    int LA50_0 = input.LA(1);

            	    if ( (LA50_0==QUOTED_IDENTIFIER||LA50_0==IDENTIFIER) ) {
            	        int LA50_1 = input.LA(2);

            	        if ( (LA50_1==BAR) ) {
            	            alt50=1;
            	        }
            	    }
            	    switch (alt50) {
            	        case 1 :
            	            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:534:17: name BAR
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression4674);
            	            name();
            	            _fsp--;

            	            match(input,BAR,FOLLOW_BAR_in_postfixExpression4676); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_expression_in_postfixExpression4680);
            	    expression();
            	    _fsp--;

            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_postfixExpression4683); 

            	    }
            	    break;

            	default :
            	    break loop51;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:536:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | blockExpression | LPAREN expression RPAREN );
    public final JCExpression primaryExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LBRACE98=null;
        Token THIS101=null;
        Token SUPER102=null;
        Token LPAREN104=null;
        Token LPAREN109=null;
        JCExpression newExpression97 = null;

        JCIdent identifier99 = null;

        ListBuffer<JFXStatement> objectLiteral100 = null;

        JCIdent identifier103 = null;

        ListBuffer<JCExpression> expressionListOpt105 = null;

        JCExpression stringExpression106 = null;

        JCExpression literal107 = null;

        JFXBlockExpression blockExpression108 = null;

        JCExpression expression110 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:537:2: ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | blockExpression | LPAREN expression RPAREN )
            int alt53=9;
            switch ( input.LA(1) ) {
            case NEW:
                {
                alt53=1;
                }
                break;
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                int LA53_2 = input.LA(2);

                if ( (LA53_2==LBRACE) ) {
                    alt53=2;
                }
                else if ( (LA53_2==ABSTRACT||LA53_2==AND||LA53_2==ATTRIBUTE||LA53_2==ON||(LA53_2>=THEN && LA53_2<=ELSE)||LA53_2==IN||(LA53_2>=PRIVATE && LA53_2<=READONLY)||LA53_2==INSTANCEOF||LA53_2==OR||(LA53_2>=LPAREN && LA53_2<=PERCENTEQ)||(LA53_2>=RBRACE_QUOTE_STRING_LITERAL && LA53_2<=RBRACE)||LA53_2==QUOTED_IDENTIFIER||LA53_2==IDENTIFIER) ) {
                    alt53=5;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("536:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | blockExpression | LPAREN expression RPAREN );", 53, 2, input);

                    throw nvae;
                }
                }
                break;
            case THIS:
                {
                alt53=3;
                }
                break;
            case SUPER:
                {
                alt53=4;
                }
                break;
            case QUOTE_LBRACE_STRING_LITERAL:
                {
                alt53=6;
                }
                break;
            case NULL:
            case TRUE:
            case FALSE:
            case STRING_LITERAL:
            case INTEGER_LITERAL:
            case FLOATING_POINT_LITERAL:
                {
                alt53=7;
                }
                break;
            case LBRACE:
                {
                alt53=8;
                }
                break;
            case LPAREN:
                {
                alt53=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("536:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | blockExpression | LPAREN expression RPAREN );", 53, 0, input);

                throw nvae;
            }

            switch (alt53) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:537:4: newExpression
                    {
                    pushFollow(FOLLOW_newExpression_in_primaryExpression4708);
                    newExpression97=newExpression();
                    _fsp--;

                     expr = newExpression97; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:538:4: identifier LBRACE objectLiteral RBRACE
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression4720);
                    identifier99=identifier();
                    _fsp--;

                    LBRACE98=(Token)input.LT(1);
                    match(input,LBRACE,FOLLOW_LBRACE_in_primaryExpression4722); 
                    pushFollow(FOLLOW_objectLiteral_in_primaryExpression4725);
                    objectLiteral100=objectLiteral();
                    _fsp--;

                    match(input,RBRACE,FOLLOW_RBRACE_in_primaryExpression4727); 
                     expr = F.at(pos(LBRACE98)).PureObjectLiteral(identifier99, objectLiteral100.toList()); 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:539:11: THIS
                    {
                    THIS101=(Token)input.LT(1);
                    match(input,THIS,FOLLOW_THIS_in_primaryExpression4744); 
                     expr = F.at(pos(THIS101)).Identifier(names._this); 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:540:11: SUPER
                    {
                    SUPER102=(Token)input.LT(1);
                    match(input,SUPER,FOLLOW_SUPER_in_primaryExpression4764); 
                     expr = F.at(pos(SUPER102)).Identifier(names._super); 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:541:11: identifier ( LPAREN expressionListOpt RPAREN )*
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression4784);
                    identifier103=identifier();
                    _fsp--;

                     expr = identifier103; 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:542:10: ( LPAREN expressionListOpt RPAREN )*
                    loop52:
                    do {
                        int alt52=2;
                        int LA52_0 = input.LA(1);

                        if ( (LA52_0==LPAREN) ) {
                            alt52=1;
                        }


                        switch (alt52) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:542:12: LPAREN expressionListOpt RPAREN
                    	    {
                    	    LPAREN104=(Token)input.LT(1);
                    	    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression4805); 
                    	    pushFollow(FOLLOW_expressionListOpt_in_primaryExpression4809);
                    	    expressionListOpt105=expressionListOpt();
                    	    _fsp--;

                    	    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression4813); 
                    	     expr = F.at(pos(LPAREN104)).Apply(null, expr, expressionListOpt105.toList()); 

                    	    }
                    	    break;

                    	default :
                    	    break loop52;
                        }
                    } while (true);


                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:543:11: stringExpression
                    {
                    pushFollow(FOLLOW_stringExpression_in_primaryExpression4833);
                    stringExpression106=stringExpression();
                    _fsp--;

                     expr = stringExpression106; 

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:544:11: literal
                    {
                    pushFollow(FOLLOW_literal_in_primaryExpression4852);
                    literal107=literal();
                    _fsp--;

                     expr = literal107; 

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:545:11: blockExpression
                    {
                    pushFollow(FOLLOW_blockExpression_in_primaryExpression4872);
                    blockExpression108=blockExpression();
                    _fsp--;

                     expr = blockExpression108; 

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:546:11: LPAREN expression RPAREN
                    {
                    LPAREN109=(Token)input.LT(1);
                    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression4890); 
                    pushFollow(FOLLOW_expression_in_primaryExpression4892);
                    expression110=expression();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression4894); 
                     expr = F.at(pos(LPAREN109)).Parens(expression110); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:548:1: newExpression returns [JCExpression expr] : NEW identifier ( LPAREN expressionListOpt RPAREN )? ;
    public final JCExpression newExpression() throws RecognitionException {
        JCExpression expr = null;

        Token NEW112=null;
        ListBuffer<JCExpression> expressionListOpt111 = null;

        JCIdent identifier113 = null;


         ListBuffer<JCExpression> args = null; 
        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:550:2: ( NEW identifier ( LPAREN expressionListOpt RPAREN )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:550:4: NEW identifier ( LPAREN expressionListOpt RPAREN )?
            {
            NEW112=(Token)input.LT(1);
            match(input,NEW,FOLLOW_NEW_in_newExpression4927); 
            pushFollow(FOLLOW_identifier_in_newExpression4930);
            identifier113=identifier();
            _fsp--;

            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:551:3: ( LPAREN expressionListOpt RPAREN )?
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==LPAREN) ) {
                alt54=1;
            }
            switch (alt54) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:551:5: LPAREN expressionListOpt RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_newExpression4938); 
                    pushFollow(FOLLOW_expressionListOpt_in_newExpression4942);
                    expressionListOpt111=expressionListOpt();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_newExpression4946); 
                     args = expressionListOpt111; 

                    }
                    break;

            }

             expr = F.at(pos(NEW112)).NewClass(null, null, identifier113, 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:556:1: objectLiteral returns [ListBuffer<JFXStatement> parts = new ListBuffer<JFXStatement>()] : ( objectLiteralPart )* ;
    public final ListBuffer<JFXStatement> objectLiteral() throws RecognitionException {
        ListBuffer<JFXStatement> parts =  new ListBuffer<JFXStatement>();

        JFXStatement objectLiteralPart114 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:557:2: ( ( objectLiteralPart )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:557:4: ( objectLiteralPart )*
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:557:4: ( objectLiteralPart )*
            loop55:
            do {
                int alt55=2;
                int LA55_0 = input.LA(1);

                if ( (LA55_0==ABSTRACT||LA55_0==ATTRIBUTE||(LA55_0>=PRIVATE && LA55_0<=READONLY)||LA55_0==QUOTED_IDENTIFIER||LA55_0==IDENTIFIER) ) {
                    alt55=1;
                }


                switch (alt55) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:557:6: objectLiteralPart
            	    {
            	    pushFollow(FOLLOW_objectLiteralPart_in_objectLiteral4986);
            	    objectLiteralPart114=objectLiteralPart();
            	    _fsp--;

            	     parts.append(objectLiteralPart114); 

            	    }
            	    break;

            	default :
            	    break loop55;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );
    public final JFXStatement objectLiteralPart() throws RecognitionException {
        JFXStatement value = null;

        Token COLON115=null;
        name_return name116 = null;

        JCExpression expression117 = null;

        JavafxBindStatus bindOpt118 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:559:2: ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition )
            int alt57=3;
            switch ( input.LA(1) ) {
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                alt57=1;
                }
                break;
            case ABSTRACT:
                {
                switch ( input.LA(2) ) {
                case PUBLIC:
                    {
                    int LA57_9 = input.LA(3);

                    if ( (LA57_9==ATTRIBUTE) ) {
                        alt57=2;
                    }
                    else if ( (LA57_9==FUNCTION) ) {
                        alt57=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 9, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA57_10 = input.LA(3);

                    if ( (LA57_10==FUNCTION) ) {
                        alt57=3;
                    }
                    else if ( (LA57_10==ATTRIBUTE) ) {
                        alt57=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA57_11 = input.LA(3);

                    if ( (LA57_11==FUNCTION) ) {
                        alt57=3;
                    }
                    else if ( (LA57_11==ATTRIBUTE) ) {
                        alt57=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt57=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt57=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 2, input);

                    throw nvae;
                }

                }
                break;
            case READONLY:
                {
                switch ( input.LA(2) ) {
                case PUBLIC:
                    {
                    int LA57_9 = input.LA(3);

                    if ( (LA57_9==ATTRIBUTE) ) {
                        alt57=2;
                    }
                    else if ( (LA57_9==FUNCTION) ) {
                        alt57=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 9, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA57_10 = input.LA(3);

                    if ( (LA57_10==FUNCTION) ) {
                        alt57=3;
                    }
                    else if ( (LA57_10==ATTRIBUTE) ) {
                        alt57=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA57_11 = input.LA(3);

                    if ( (LA57_11==FUNCTION) ) {
                        alt57=3;
                    }
                    else if ( (LA57_11==ATTRIBUTE) ) {
                        alt57=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt57=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt57=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 3, input);

                    throw nvae;
                }

                }
                break;
            case PUBLIC:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA57_12 = input.LA(3);

                    if ( (LA57_12==FUNCTION) ) {
                        alt57=3;
                    }
                    else if ( (LA57_12==ATTRIBUTE) ) {
                        alt57=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA57_13 = input.LA(3);

                    if ( (LA57_13==FUNCTION) ) {
                        alt57=3;
                    }
                    else if ( (LA57_13==ATTRIBUTE) ) {
                        alt57=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt57=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt57=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 4, input);

                    throw nvae;
                }

                }
                break;
            case PRIVATE:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA57_12 = input.LA(3);

                    if ( (LA57_12==FUNCTION) ) {
                        alt57=3;
                    }
                    else if ( (LA57_12==ATTRIBUTE) ) {
                        alt57=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA57_13 = input.LA(3);

                    if ( (LA57_13==FUNCTION) ) {
                        alt57=3;
                    }
                    else if ( (LA57_13==ATTRIBUTE) ) {
                        alt57=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt57=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt57=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 5, input);

                    throw nvae;
                }

                }
                break;
            case PROTECTED:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA57_12 = input.LA(3);

                    if ( (LA57_12==FUNCTION) ) {
                        alt57=3;
                    }
                    else if ( (LA57_12==ATTRIBUTE) ) {
                        alt57=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA57_13 = input.LA(3);

                    if ( (LA57_13==FUNCTION) ) {
                        alt57=3;
                    }
                    else if ( (LA57_13==ATTRIBUTE) ) {
                        alt57=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt57=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt57=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 6, input);

                    throw nvae;
                }

                }
                break;
            case ATTRIBUTE:
                {
                alt57=2;
                }
                break;
            case FUNCTION:
                {
                alt57=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("558:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 57, 0, input);

                throw nvae;
            }

            switch (alt57) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:559:4: name COLON bindOpt expression ( COMMA | SEMI )?
                    {
                    pushFollow(FOLLOW_name_in_objectLiteralPart5012);
                    name116=name();
                    _fsp--;

                    COLON115=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_objectLiteralPart5014); 
                    pushFollow(FOLLOW_bindOpt_in_objectLiteralPart5017);
                    bindOpt118=bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_objectLiteralPart5019);
                    expression117=expression();
                    _fsp--;

                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:559:35: ( COMMA | SEMI )?
                    int alt56=2;
                    int LA56_0 = input.LA(1);

                    if ( ((LA56_0>=SEMI && LA56_0<=COMMA)) ) {
                        alt56=1;
                    }
                    switch (alt56) {
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
                                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_objectLiteralPart5021);    throw mse;
                            }


                            }
                            break;

                    }

                     value = F.at(pos(COLON115)).ObjectLiteralPart(name116.value, expression117, bindOpt118); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:560:10: attributeDefinition
                    {
                    pushFollow(FOLLOW_attributeDefinition_in_objectLiteralPart5041);
                    attributeDefinition();
                    _fsp--;


                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:561:10: functionDefinition
                    {
                    pushFollow(FOLLOW_functionDefinition_in_objectLiteralPart5052);
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:562:1: stringExpression returns [JCExpression expr] : ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL ;
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
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:564:2: (ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:564:4: ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL
            {
            ql=(Token)input.LT(1);
            match(input,QUOTE_LBRACE_STRING_LITERAL,FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression5074); 
             strexp.append(F.at(pos(ql)).Literal(TypeTags.CLASS, ql.getText())); 
            pushFollow(FOLLOW_formatOrNull_in_stringExpression5083);
            f1=formatOrNull();
            _fsp--;

             strexp.append(f1); 
            pushFollow(FOLLOW_expression_in_stringExpression5094);
            e1=expression();
            _fsp--;

             strexp.append(e1); 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:567:4: (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )*
            loop58:
            do {
                int alt58=2;
                int LA58_0 = input.LA(1);

                if ( (LA58_0==RBRACE_LBRACE_STRING_LITERAL) ) {
                    alt58=1;
                }


                switch (alt58) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:567:7: rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression
            	    {
            	    rl=(Token)input.LT(1);
            	    match(input,RBRACE_LBRACE_STRING_LITERAL,FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression5109); 
            	     strexp.append(F.at(pos(rl)).Literal(TypeTags.CLASS, rl.getText())); 
            	    pushFollow(FOLLOW_formatOrNull_in_stringExpression5121);
            	    fn=formatOrNull();
            	    _fsp--;

            	     strexp.append(fn); 
            	    pushFollow(FOLLOW_expression_in_stringExpression5135);
            	    en=expression();
            	    _fsp--;

            	     strexp.append(en); 

            	    }
            	    break;

            	default :
            	    break loop58;
                }
            } while (true);

            rq=(Token)input.LT(1);
            match(input,RBRACE_QUOTE_STRING_LITERAL,FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression5156); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:574:1: formatOrNull returns [JCExpression expr] : (fs= FORMAT_STRING_LITERAL | );
    public final JCExpression formatOrNull() throws RecognitionException {
        JCExpression expr = null;

        Token fs=null;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:575:2: (fs= FORMAT_STRING_LITERAL | )
            int alt59=2;
            int LA59_0 = input.LA(1);

            if ( (LA59_0==FORMAT_STRING_LITERAL) ) {
                alt59=1;
            }
            else if ( ((LA59_0>=POUND && LA59_0<=TYPEOF)||LA59_0==IF||(LA59_0>=THIS && LA59_0<=FALSE)||(LA59_0>=NOT && LA59_0<=NEW)||LA59_0==SUPER||(LA59_0>=SIZEOF && LA59_0<=LPAREN)||(LA59_0>=PLUSPLUS && LA59_0<=SUBSUB)||(LA59_0>=QUES && LA59_0<=STRING_LITERAL)||(LA59_0>=QUOTE_LBRACE_STRING_LITERAL && LA59_0<=LBRACE)||(LA59_0>=QUOTED_IDENTIFIER && LA59_0<=INTEGER_LITERAL)||LA59_0==FLOATING_POINT_LITERAL||LA59_0==IDENTIFIER) ) {
                alt59=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("574:1: formatOrNull returns [JCExpression expr] : (fs= FORMAT_STRING_LITERAL | );", 59, 0, input);

                throw nvae;
            }
            switch (alt59) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:575:4: fs= FORMAT_STRING_LITERAL
                    {
                    fs=(Token)input.LT(1);
                    match(input,FORMAT_STRING_LITERAL,FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull5186); 
                     expr = F.at(pos(fs)).Literal(TypeTags.CLASS, fs.getText()); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:576:22: 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:578:1: expressionListOpt returns [ListBuffer<JCExpression> args = new ListBuffer<JCExpression>()] : (e1= expression ( COMMA e= expression )* )? ;
    public final ListBuffer<JCExpression> expressionListOpt() throws RecognitionException {
        ListBuffer<JCExpression> args =  new ListBuffer<JCExpression>();

        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:579:2: ( (e1= expression ( COMMA e= expression )* )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:579:4: (e1= expression ( COMMA e= expression )* )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:579:4: (e1= expression ( COMMA e= expression )* )?
            int alt61=2;
            int LA61_0 = input.LA(1);

            if ( ((LA61_0>=POUND && LA61_0<=TYPEOF)||LA61_0==IF||(LA61_0>=THIS && LA61_0<=FALSE)||(LA61_0>=NOT && LA61_0<=NEW)||LA61_0==SUPER||(LA61_0>=SIZEOF && LA61_0<=LPAREN)||(LA61_0>=PLUSPLUS && LA61_0<=SUBSUB)||(LA61_0>=QUES && LA61_0<=STRING_LITERAL)||(LA61_0>=QUOTE_LBRACE_STRING_LITERAL && LA61_0<=LBRACE)||(LA61_0>=QUOTED_IDENTIFIER && LA61_0<=INTEGER_LITERAL)||LA61_0==FLOATING_POINT_LITERAL||LA61_0==IDENTIFIER) ) {
                alt61=1;
            }
            switch (alt61) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:579:6: e1= expression ( COMMA e= expression )*
                    {
                    pushFollow(FOLLOW_expression_in_expressionListOpt5218);
                    e1=expression();
                    _fsp--;

                     args.append(e1); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:580:6: ( COMMA e= expression )*
                    loop60:
                    do {
                        int alt60=2;
                        int LA60_0 = input.LA(1);

                        if ( (LA60_0==COMMA) ) {
                            alt60=1;
                        }


                        switch (alt60) {
                    	case 1 :
                    	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:580:7: COMMA e= expression
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_expressionListOpt5229); 
                    	    pushFollow(FOLLOW_expression_in_expressionListOpt5235);
                    	    e=expression();
                    	    _fsp--;

                    	     args.append(e); 

                    	    }
                    	    break;

                    	default :
                    	    break loop60;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:581:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );
    public final int unaryOperator() throws RecognitionException {
        int optag = 0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:582:2: ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB )
            int alt62=9;
            switch ( input.LA(1) ) {
            case POUND:
                {
                alt62=1;
                }
                break;
            case QUES:
                {
                alt62=2;
                }
                break;
            case SUB:
                {
                alt62=3;
                }
                break;
            case NOT:
                {
                alt62=4;
                }
                break;
            case SIZEOF:
                {
                alt62=5;
                }
                break;
            case TYPEOF:
                {
                alt62=6;
                }
                break;
            case REVERSE:
                {
                alt62=7;
                }
                break;
            case PLUSPLUS:
                {
                alt62=8;
                }
                break;
            case SUBSUB:
                {
                alt62=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("581:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );", 62, 0, input);

                throw nvae;
            }

            switch (alt62) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:582:4: POUND
                    {
                    match(input,POUND,FOLLOW_POUND_in_unaryOperator5258); 
                     optag = 0; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:583:4: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_unaryOperator5269); 
                     optag = 0; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:584:4: SUB
                    {
                    match(input,SUB,FOLLOW_SUB_in_unaryOperator5282); 
                     optag = JCTree.NEG; 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:585:4: NOT
                    {
                    match(input,NOT,FOLLOW_NOT_in_unaryOperator5295); 
                     optag = JCTree.NOT; 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:586:4: SIZEOF
                    {
                    match(input,SIZEOF,FOLLOW_SIZEOF_in_unaryOperator5308); 
                     optag = 0; 

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:587:4: TYPEOF
                    {
                    match(input,TYPEOF,FOLLOW_TYPEOF_in_unaryOperator5321); 
                     optag = 0; 

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:588:4: REVERSE
                    {
                    match(input,REVERSE,FOLLOW_REVERSE_in_unaryOperator5334); 
                     optag = 0; 

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:589:4: PLUSPLUS
                    {
                    match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_unaryOperator5347); 
                     optag = 0; 

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:590:4: SUBSUB
                    {
                    match(input,SUBSUB,FOLLOW_SUBSUB_in_unaryOperator5360); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:592:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );
    public final int assignmentOperator() throws RecognitionException {
        int optag = 0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:593:2: ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ )
            int alt63=5;
            switch ( input.LA(1) ) {
            case PLUSEQ:
                {
                alt63=1;
                }
                break;
            case SUBEQ:
                {
                alt63=2;
                }
                break;
            case STAREQ:
                {
                alt63=3;
                }
                break;
            case SLASHEQ:
                {
                alt63=4;
                }
                break;
            case PERCENTEQ:
                {
                alt63=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("592:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );", 63, 0, input);

                throw nvae;
            }

            switch (alt63) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:593:4: PLUSEQ
                    {
                    match(input,PLUSEQ,FOLLOW_PLUSEQ_in_assignmentOperator5381); 
                     optag = JCTree.PLUS_ASG; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:594:4: SUBEQ
                    {
                    match(input,SUBEQ,FOLLOW_SUBEQ_in_assignmentOperator5394); 
                     optag = JCTree.MINUS_ASG; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:595:4: STAREQ
                    {
                    match(input,STAREQ,FOLLOW_STAREQ_in_assignmentOperator5407); 
                     optag = JCTree.MUL_ASG; 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:596:4: SLASHEQ
                    {
                    match(input,SLASHEQ,FOLLOW_SLASHEQ_in_assignmentOperator5420); 
                     optag = JCTree.DIV_ASG; 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:597:4: PERCENTEQ
                    {
                    match(input,PERCENTEQ,FOLLOW_PERCENTEQ_in_assignmentOperator5433); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:599:1: typeReference returns [JFXType type] : ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? ;
    public final JFXType typeReference() throws RecognitionException {
        JFXType type = null;

        Token STAR120=null;
        int ccn = 0;

        int ccs = 0;

        name_return name119 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:600:2: ( ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:600:4: ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:600:4: ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( (LA65_0==COLON) ) {
                alt65=1;
            }
            switch (alt65) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:600:6: COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    {
                    match(input,COLON,FOLLOW_COLON_in_typeReference5457); 
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:600:13: ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    int alt64=2;
                    int LA64_0 = input.LA(1);

                    if ( (LA64_0==QUOTED_IDENTIFIER||LA64_0==IDENTIFIER) ) {
                        alt64=1;
                    }
                    else if ( (LA64_0==STAR) ) {
                        alt64=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("600:13: ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )", 64, 0, input);

                        throw nvae;
                    }
                    switch (alt64) {
                        case 1 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:600:15: name ccn= cardinalityConstraint
                            {
                            pushFollow(FOLLOW_name_in_typeReference5462);
                            name119=name();
                            _fsp--;

                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference5466);
                            ccn=cardinalityConstraint();
                            _fsp--;

                             type = F.TypeClass(name119.value, ccn); 

                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:601:22: STAR ccs= cardinalityConstraint
                            {
                            STAR120=(Token)input.LT(1);
                            match(input,STAR,FOLLOW_STAR_in_typeReference5492); 
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference5496);
                            ccs=cardinalityConstraint();
                            _fsp--;

                             type = F.at(pos(STAR120)).TypeAny(ccs); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:605:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );
    public final int cardinalityConstraint() throws RecognitionException {
        int ary = 0;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:606:2: ( LBRACKET RBRACKET | QUES | PLUS | STAR | )
            int alt66=5;
            switch ( input.LA(1) ) {
            case LBRACKET:
                {
                alt66=1;
                }
                break;
            case QUES:
                {
                alt66=2;
                }
                break;
            case PLUS:
                {
                alt66=3;
                }
                break;
            case STAR:
                {
                alt66=4;
                }
                break;
            case ON:
            case INVERSE:
            case RPAREN:
            case SEMI:
            case COMMA:
            case EQ:
            case LBRACE:
                {
                alt66=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("605:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 66, 0, input);

                throw nvae;
            }

            switch (alt66) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:606:5: LBRACKET RBRACKET
                    {
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_cardinalityConstraint5558); 
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_cardinalityConstraint5562); 
                     ary = JFXType.CARDINALITY_ANY; 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:607:5: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_cardinalityConstraint5574); 
                     ary = JFXType.CARDINALITY_OPTIONAL; 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:608:5: PLUS
                    {
                    match(input,PLUS,FOLLOW_PLUS_in_cardinalityConstraint5601); 
                     ary = JFXType.CARDINALITY_ANY; 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:609:5: STAR
                    {
                    match(input,STAR,FOLLOW_STAR_in_cardinalityConstraint5628); 
                     ary = JFXType.CARDINALITY_ANY; 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:610:29: 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:612:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );
    public final JCExpression literal() throws RecognitionException {
        JCExpression expr = null;

        Token t=null;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:613:2: (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL )
            int alt67=6;
            switch ( input.LA(1) ) {
            case STRING_LITERAL:
                {
                alt67=1;
                }
                break;
            case INTEGER_LITERAL:
                {
                alt67=2;
                }
                break;
            case FLOATING_POINT_LITERAL:
                {
                alt67=3;
                }
                break;
            case TRUE:
                {
                alt67=4;
                }
                break;
            case FALSE:
                {
                alt67=5;
                }
                break;
            case NULL:
                {
                alt67=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("612:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );", 67, 0, input);

                throw nvae;
            }

            switch (alt67) {
                case 1 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:613:4: t= STRING_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_literal5697); 
                     expr = F.at(pos(t)).Literal(TypeTags.CLASS, t.getText()); 

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:614:4: t= INTEGER_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,INTEGER_LITERAL,FOLLOW_INTEGER_LITERAL_in_literal5707); 
                     expr = F.at(pos(t)).Literal(TypeTags.INT, Convert.string2int(t.getText(), 10)); 

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:615:4: t= FLOATING_POINT_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,FLOATING_POINT_LITERAL,FOLLOW_FLOATING_POINT_LITERAL_in_literal5717); 
                     expr = F.at(pos(t)).Literal(TypeTags.DOUBLE, Double.valueOf(t.getText())); 

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:616:4: t= TRUE
                    {
                    t=(Token)input.LT(1);
                    match(input,TRUE,FOLLOW_TRUE_in_literal5727); 
                     expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 1); 

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:617:4: t= FALSE
                    {
                    t=(Token)input.LT(1);
                    match(input,FALSE,FOLLOW_FALSE_in_literal5741); 
                     expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 0); 

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:618:4: t= NULL
                    {
                    t=(Token)input.LT(1);
                    match(input,NULL,FOLLOW_NULL_in_literal5755); 
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:620:1: typeName returns [JCExpression expr] : qualident ;
    public final JCExpression typeName() throws RecognitionException {
        JCExpression expr = null;

        JCExpression qualident121 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:621:8: ( qualident )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:621:10: qualident
            {
            pushFollow(FOLLOW_qualident_in_typeName5782);
            qualident121=qualident();
            _fsp--;

             expr = qualident121; 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:623:1: qualident returns [JCExpression expr] : identifier ( DOT name )* ;
    public final JCExpression qualident() throws RecognitionException {
        JCExpression expr = null;

        JCIdent identifier122 = null;

        name_return name123 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:624:8: ( identifier ( DOT name )* )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:624:10: identifier ( DOT name )*
            {
            pushFollow(FOLLOW_identifier_in_qualident5824);
            identifier122=identifier();
            _fsp--;

             expr = identifier122; 
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:625:10: ( DOT name )*
            loop68:
            do {
                int alt68=2;
                int LA68_0 = input.LA(1);

                if ( (LA68_0==DOT) ) {
                    alt68=1;
                }


                switch (alt68) {
            	case 1 :
            	    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:625:12: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_qualident5852); 
            	    pushFollow(FOLLOW_name_in_qualident5854);
            	    name123=name();
            	    _fsp--;

            	     expr = F.at(name123.pos).Select(expr, name123.value); 

            	    }
            	    break;

            	default :
            	    break loop68;
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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:627:1: identifier returns [JCIdent expr] : name ;
    public final JCIdent identifier() throws RecognitionException {
        JCIdent expr = null;

        name_return name124 = null;


        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:628:2: ( name )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:628:4: name
            {
            pushFollow(FOLLOW_name_in_identifier5891);
            name124=name();
            _fsp--;

             expr = F.at(name124.pos).Ident(name124.value); 

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
    // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:630:1: name returns [Name value, int pos] : tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) ;
    public final name_return name() throws RecognitionException {
        name_return retval = new name_return();
        retval.start = input.LT(1);

        Token tokid=null;

        try {
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:631:2: (tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) )
            // C:\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:631:4: tokid= ( QUOTED_IDENTIFIER | IDENTIFIER )
            {
            tokid=(Token)input.LT(1);
            if ( input.LA(1)==QUOTED_IDENTIFIER||input.LA(1)==IDENTIFIER ) {
                input.consume();
                errorRecovery=false;
            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_name5925);    throw mse;
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


 

    public static final BitSet FOLLOW_packageDecl_in_module2008 = new BitSet(new long[]{0x7C4CF205C0220260L,0x012C3600E0047401L});
    public static final BitSet FOLLOW_moduleItems_in_module2011 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_module2013 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PACKAGE_in_packageDecl2035 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_qualident_in_packageDecl2037 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_packageDecl2039 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_moduleItem_in_moduleItems2070 = new BitSet(new long[]{0x7C4CF205C0220262L,0x012C3600E0047401L});
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
    public static final BitSet FOLLOW_modifierFlags_in_classDefinition2308 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_CLASS_in_classDefinition2311 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_classDefinition2313 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000008L});
    public static final BitSet FOLLOW_supers_in_classDefinition2315 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_LBRACE_in_classDefinition2317 = new BitSet(new long[]{0x7000000000004200L,0x0001000000000041L});
    public static final BitSet FOLLOW_classMembers_in_classDefinition2319 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_classDefinition2321 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXTENDS_in_supers2346 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_supers2350 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_COMMA_in_supers2378 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_supers2382 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_attributeDefinition_in_classMembers2419 = new BitSet(new long[]{0x7000000000004202L,0x0000000000000041L});
    public static final BitSet FOLLOW_functionDefinition_in_classMembers2441 = new BitSet(new long[]{0x7000000000004202L,0x0000000000000041L});
    public static final BitSet FOLLOW_initDefinition_in_classMembers2464 = new BitSet(new long[]{0x7000000000004202L,0x0000000000000001L});
    public static final BitSet FOLLOW_attributeDefinition_in_classMembers2487 = new BitSet(new long[]{0x7000000000004202L,0x0000000000000001L});
    public static final BitSet FOLLOW_functionDefinition_in_classMembers2510 = new BitSet(new long[]{0x7000000000004202L,0x0000000000000001L});
    public static final BitSet FOLLOW_modifierFlags_in_attributeDefinition2550 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeDefinition2552 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_attributeDefinition2554 = new BitSet(new long[]{0x0000001000000000L,0x0000010000440002L});
    public static final BitSet FOLLOW_typeReference_in_attributeDefinition2562 = new BitSet(new long[]{0x0000001000000000L,0x0000000000440002L});
    public static final BitSet FOLLOW_EQ_in_attributeDefinition2570 = new BitSet(new long[]{0x0C00F20030010060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_bindOpt_in_attributeDefinition2572 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_expression_in_attributeDefinition2574 = new BitSet(new long[]{0x0000001000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_inverseClause_in_attributeDefinition2578 = new BitSet(new long[]{0x0000001000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_ON_in_attributeDefinition2588 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_CHANGE_in_attributeDefinition2590 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_block_in_attributeDefinition2594 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_attributeDefinition2603 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INVERSE_in_inverseClause2629 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_memberSelector_in_inverseClause2631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_functionDefinition2649 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_FUNCTION_in_functionDefinition2651 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_functionDefinition2653 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_formalParameters_in_functionDefinition2661 = new BitSet(new long[]{0x0000000000000000L,0x0000210000000000L});
    public static final BitSet FOLLOW_typeReference_in_functionDefinition2664 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_blockExpression_in_functionDefinition2673 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INIT_in_initDefinition2693 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_block_in_initDefinition2695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags2724 = new BitSet(new long[]{0x7000000000000002L});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags2737 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags2759 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000001L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags2772 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_accessModifier2820 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PRIVATE_in_accessModifier2840 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROTECTED_in_accessModifier2859 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ABSTRACT_in_otherModifier2886 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_READONLY_in_otherModifier2904 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_memberSelector2933 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_DOT_in_memberSelector2937 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_memberSelector2943 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_formalParameters2961 = new BitSet(new long[]{0x0000000000000000L,0x0104000000008000L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters2968 = new BitSet(new long[]{0x0000000000000000L,0x0000000000088000L});
    public static final BitSet FOLLOW_COMMA_in_formalParameters2986 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters2992 = new BitSet(new long[]{0x0000000000000000L,0x0000000000088000L});
    public static final BitSet FOLLOW_RPAREN_in_formalParameters3003 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_formalParameter3018 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
    public static final BitSet FOLLOW_typeReference_in_formalParameter3020 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_block3046 = new BitSet(new long[]{0xFC4CF201C0020260L,0x012D3600E0047401L});
    public static final BitSet FOLLOW_statement_in_block3056 = new BitSet(new long[]{0xFC4CF201C0020260L,0x012D3600E0047401L});
    public static final BitSet FOLLOW_expression_in_block3071 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_block3073 = new BitSet(new long[]{0xFC4CF201C0020260L,0x012D3600E0047401L});
    public static final BitSet FOLLOW_RBRACE_in_block3089 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_blockExpression3113 = new BitSet(new long[]{0xFC4CF201C0020260L,0x012D3600E0047401L});
    public static final BitSet FOLLOW_statements_in_blockExpression3116 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_blockExpression3121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_statements3140 = new BitSet(new long[]{0xFC4CF201C0020262L,0x012C3600E0047401L});
    public static final BitSet FOLLOW_statements_in_statements3152 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_statements3164 = new BitSet(new long[]{0x0000000000000002L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statements3176 = new BitSet(new long[]{0xFC4CF201C0020262L,0x012C3600E0047401L});
    public static final BitSet FOLLOW_statements_in_statements3199 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclaration_in_statement3257 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement3259 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionDefinition_in_statement3267 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_statement3283 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_LPAREN_in_statement3285 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_expression_in_statement3287 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_RPAREN_in_statement3289 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_block_in_statement3291 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BREAK_in_statement3298 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement3300 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_statement3313 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement3316 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THROW_in_statement3334 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_expression_in_statement3336 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement3338 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_returnStatement_in_statement3358 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement3360 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tryStatement_in_statement3376 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_statement3387 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variableDeclaration3417 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_variableDeclaration3420 = new BitSet(new long[]{0x0000000000000002L,0x0000010000400000L});
    public static final BitSet FOLLOW_typeReference_in_variableDeclaration3423 = new BitSet(new long[]{0x0000000000000002L,0x0000000000400000L});
    public static final BitSet FOLLOW_EQ_in_variableDeclaration3434 = new BitSet(new long[]{0x0C00F20030010060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_bindOpt_in_variableDeclaration3436 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_expression_in_variableDeclaration3439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BIND_in_bindOpt3489 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3505 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAYS_in_bindOpt3520 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3536 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TIE_in_bindOpt3551 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnStatement3601 = new BitSet(new long[]{0x0C00F20000000062L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_expression_in_returnStatement3604 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRY_in_tryStatement3647 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3653 = new BitSet(new long[]{0x0080000000080000L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement3666 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3672 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catchClause_in_tryStatement3688 = new BitSet(new long[]{0x0080000000080002L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement3711 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3716 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CATCH_in_catchClause3755 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_LPAREN_in_catchClause3758 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_catchClause3761 = new BitSet(new long[]{0x0000000000000000L,0x0000010000008000L});
    public static final BitSet FOLLOW_COLON_in_catchClause3769 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_identifier_in_catchClause3771 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_RPAREN_in_catchClause3780 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_block_in_catchClause3784 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifExpression_in_expression3812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_suffixedExpression_in_expression3835 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifExpression3866 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_expression_in_ifExpression3870 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_THEN_in_ifExpression3874 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_expression_in_ifExpression3879 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_ELSE_in_ifExpression3887 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_expression_in_ifExpression3892 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentExpression_in_suffixedExpression3915 = new BitSet(new long[]{0x0000000000000002L,0x00000000A0000000L});
    public static final BitSet FOLLOW_set_in_suffixedExpression3926 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression3955 = new BitSet(new long[]{0x0000000000000002L,0x0000000000400000L});
    public static final BitSet FOLLOW_EQ_in_assignmentExpression3970 = new BitSet(new long[]{0x0C00F00000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression3976 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression4003 = new BitSet(new long[]{0x0000000000000002L,0x000000F800000000L});
    public static final BitSet FOLLOW_assignmentOperator_in_assignmentOpExpression4019 = new BitSet(new long[]{0x0C00F00000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression4025 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orExpression_in_andExpression4051 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_AND_in_andExpression4067 = new BitSet(new long[]{0x0C00F00000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_orExpression_in_andExpression4073 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression4101 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000800L});
    public static final BitSet FOLLOW_OR_in_orExpression4116 = new BitSet(new long[]{0x0C00F00000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression4122 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000800L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression4150 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_INSTANCEOF_in_instanceOfExpression4165 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_identifier_in_instanceOfExpression4167 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4195 = new BitSet(new long[]{0x0002000000000002L,0x000000000FA00000L});
    public static final BitSet FOLLOW_LTGT_in_relationalExpression4211 = new BitSet(new long[]{0x0C00F00000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4217 = new BitSet(new long[]{0x0002000000000002L,0x000000000FA00000L});
    public static final BitSet FOLLOW_EQEQ_in_relationalExpression4231 = new BitSet(new long[]{0x0C00F00000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4237 = new BitSet(new long[]{0x0002000000000002L,0x000000000FA00000L});
    public static final BitSet FOLLOW_LTEQ_in_relationalExpression4251 = new BitSet(new long[]{0x0C00F00000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4257 = new BitSet(new long[]{0x0002000000000002L,0x000000000FA00000L});
    public static final BitSet FOLLOW_GTEQ_in_relationalExpression4271 = new BitSet(new long[]{0x0C00F00000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4277 = new BitSet(new long[]{0x0002000000000002L,0x000000000FA00000L});
    public static final BitSet FOLLOW_LT_in_relationalExpression4291 = new BitSet(new long[]{0x0C00F00000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4299 = new BitSet(new long[]{0x0002000000000002L,0x000000000FA00000L});
    public static final BitSet FOLLOW_GT_in_relationalExpression4313 = new BitSet(new long[]{0x0C00F00000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4321 = new BitSet(new long[]{0x0002000000000002L,0x000000000FA00000L});
    public static final BitSet FOLLOW_IN_in_relationalExpression4335 = new BitSet(new long[]{0x0C00F00000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4343 = new BitSet(new long[]{0x0002000000000002L,0x000000000FA00000L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4372 = new BitSet(new long[]{0x0000000000000002L,0x0000000050000000L});
    public static final BitSet FOLLOW_PLUS_in_additiveExpression4387 = new BitSet(new long[]{0x0C00F00000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4393 = new BitSet(new long[]{0x0000000000000002L,0x0000000050000000L});
    public static final BitSet FOLLOW_SUB_in_additiveExpression4406 = new BitSet(new long[]{0x0C00F00000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4413 = new BitSet(new long[]{0x0000000000000002L,0x0000000050000000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4441 = new BitSet(new long[]{0x0000000000000002L,0x0000000700000000L});
    public static final BitSet FOLLOW_STAR_in_multiplicativeExpression4457 = new BitSet(new long[]{0x0C00F00000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4464 = new BitSet(new long[]{0x0000000000000002L,0x0000000700000000L});
    public static final BitSet FOLLOW_SLASH_in_multiplicativeExpression4478 = new BitSet(new long[]{0x0C00F00000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4484 = new BitSet(new long[]{0x0000000000000002L,0x0000000700000000L});
    public static final BitSet FOLLOW_PERCENT_in_multiplicativeExpression4498 = new BitSet(new long[]{0x0C00F00000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4502 = new BitSet(new long[]{0x0000000000000002L,0x0000000700000000L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression4532 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryOperator_in_unaryExpression4543 = new BitSet(new long[]{0x0800F00000000000L,0x012C340000004400L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression4547 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primaryExpression_in_postfixExpression4567 = new BitSet(new long[]{0x0000000000000002L,0x0000000000110000L});
    public static final BitSet FOLLOW_DOT_in_postfixExpression4582 = new BitSet(new long[]{0x0000000000200000L,0x0104000000000000L});
    public static final BitSet FOLLOW_CLASS_in_postfixExpression4586 = new BitSet(new long[]{0x0000000000000002L,0x0000000000110000L});
    public static final BitSet FOLLOW_name_in_postfixExpression4610 = new BitSet(new long[]{0x0000000000000002L,0x0000000000114000L});
    public static final BitSet FOLLOW_LPAREN_in_postfixExpression4635 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E000F400L});
    public static final BitSet FOLLOW_expressionListOpt_in_postfixExpression4637 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_RPAREN_in_postfixExpression4639 = new BitSet(new long[]{0x0000000000000002L,0x0000000000114000L});
    public static final BitSet FOLLOW_LBRACKET_in_postfixExpression4671 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_name_in_postfixExpression4674 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_BAR_in_postfixExpression4676 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_expression_in_postfixExpression4680 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_postfixExpression4683 = new BitSet(new long[]{0x0000000000000002L,0x0000000000110000L});
    public static final BitSet FOLLOW_newExpression_in_primaryExpression4708 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression4720 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_LBRACE_in_primaryExpression4722 = new BitSet(new long[]{0x7000000000004200L,0x0105000000000001L});
    public static final BitSet FOLLOW_objectLiteral_in_primaryExpression4725 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_primaryExpression4727 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THIS_in_primaryExpression4744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_primaryExpression4764 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression4784 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004000L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression4805 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E000F400L});
    public static final BitSet FOLLOW_expressionListOpt_in_primaryExpression4809 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression4813 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004000L});
    public static final BitSet FOLLOW_stringExpression_in_primaryExpression4833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primaryExpression4852 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_blockExpression_in_primaryExpression4872 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression4890 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_expression_in_primaryExpression4892 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression4894 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_newExpression4927 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_identifier_in_newExpression4930 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004000L});
    public static final BitSet FOLLOW_LPAREN_in_newExpression4938 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E000F400L});
    public static final BitSet FOLLOW_expressionListOpt_in_newExpression4942 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_RPAREN_in_newExpression4946 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_objectLiteralPart_in_objectLiteral4986 = new BitSet(new long[]{0x7000000000004202L,0x0104000000000001L});
    public static final BitSet FOLLOW_name_in_objectLiteralPart5012 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_COLON_in_objectLiteralPart5014 = new BitSet(new long[]{0x0C00F20030010060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_bindOpt_in_objectLiteralPart5017 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_expression_in_objectLiteralPart5019 = new BitSet(new long[]{0x0000000000000002L,0x00000000000C0000L});
    public static final BitSet FOLLOW_set_in_objectLiteralPart5021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeDefinition_in_objectLiteralPart5041 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionDefinition_in_objectLiteralPart5052 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression5074 = new BitSet(new long[]{0x0C00F20000000060L,0x012E3600E0007400L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression5083 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_expression_in_stringExpression5094 = new BitSet(new long[]{0x0000000000000000L,0x0000C00000000000L});
    public static final BitSet FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression5109 = new BitSet(new long[]{0x0C00F20000000060L,0x012E3600E0007400L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression5121 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_expression_in_stringExpression5135 = new BitSet(new long[]{0x0000000000000000L,0x0000C00000000000L});
    public static final BitSet FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression5156 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull5186 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt5218 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_COMMA_in_expressionListOpt5229 = new BitSet(new long[]{0x0C00F20000000060L,0x012C3600E0007400L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt5235 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_POUND_in_unaryOperator5258 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_unaryOperator5269 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUB_in_unaryOperator5282 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_unaryOperator5295 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIZEOF_in_unaryOperator5308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TYPEOF_in_unaryOperator5321 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REVERSE_in_unaryOperator5334 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_unaryOperator5347 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_unaryOperator5360 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSEQ_in_assignmentOperator5381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBEQ_in_assignmentOperator5394 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAREQ_in_assignmentOperator5407 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SLASHEQ_in_assignmentOperator5420 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENTEQ_in_assignmentOperator5433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLON_in_typeReference5457 = new BitSet(new long[]{0x0000000000000000L,0x0104000100000000L});
    public static final BitSet FOLLOW_name_in_typeReference5462 = new BitSet(new long[]{0x0000000000000002L,0x0000020110010000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference5466 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_typeReference5492 = new BitSet(new long[]{0x0000000000000002L,0x0000020110010000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference5496 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_cardinalityConstraint5558 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_cardinalityConstraint5562 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_cardinalityConstraint5574 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUS_in_cardinalityConstraint5601 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_cardinalityConstraint5628 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_literal5697 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_LITERAL_in_literal5707 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOATING_POINT_LITERAL_in_literal5717 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRUE_in_literal5727 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALSE_in_literal5741 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_literal5755 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualident_in_typeName5782 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_qualident5824 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_DOT_in_qualident5852 = new BitSet(new long[]{0x0000000000000000L,0x0104000000000000L});
    public static final BitSet FOLLOW_name_in_qualident5854 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_name_in_identifier5891 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_name5925 = new BitSet(new long[]{0x0000000000000002L});

}