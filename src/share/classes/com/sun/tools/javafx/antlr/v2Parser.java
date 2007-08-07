// $ANTLR 3.0 \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g 2007-08-06 18:23:03

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
    public String getGrammarFileName() { return "\\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g"; }

    
            public v2Parser(Context context, CharSequence content) {
               this(new CommonTokenStream(new v2Lexer(new ANTLRStringStream(content.toString()))));
               initialize(context);
        	}



    // $ANTLR start module
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:320:1: module returns [JCCompilationUnit result] : ( packageDecl )? moduleItems EOF ;
    public final JCCompilationUnit module() throws RecognitionException {
        JCCompilationUnit result = null;

        JCExpression packageDecl1 = null;

        ListBuffer<JCTree> moduleItems2 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:8: ( ( packageDecl )? moduleItems EOF )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:10: ( packageDecl )? moduleItems EOF
            {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:10: ( packageDecl )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==PACKAGE) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:321:10: packageDecl
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:322:1: packageDecl returns [JCExpression value] : PACKAGE qualident SEMI ;
    public final JCExpression packageDecl() throws RecognitionException {
        JCExpression value = null;

        JCExpression qualident3 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:323:8: ( PACKAGE qualident SEMI )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:323:10: PACKAGE qualident SEMI
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:324:1: moduleItems returns [ListBuffer<JCTree> items = new ListBuffer<JCTree>()] : ( moduleItem )* ;
    public final ListBuffer<JCTree> moduleItems() throws RecognitionException {
        ListBuffer<JCTree> items =  new ListBuffer<JCTree>();

        JCTree moduleItem4 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:325:9: ( ( moduleItem )* )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:325:11: ( moduleItem )*
            {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:325:11: ( moduleItem )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==ABSTRACT||LA2_0==BREAK||LA2_0==CLASS||(LA2_0>=RETURN && LA2_0<=VAR)||LA2_0==IMPORT||(LA2_0>=WHILE && LA2_0<=CONTINUE)||LA2_0==TRY||(LA2_0>=PRIVATE && LA2_0<=READONLY)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:325:12: moduleItem
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:326:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | statement );
    public final JCTree moduleItem() throws RecognitionException {
        JCTree value = null;

        JCTree importDecl5 = null;

        JFXClassDeclaration classDefinition6 = null;

        JCStatement statement7 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:327:8: ( importDecl | classDefinition | statement )
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
            case BREAK:
            case RETURN:
            case THROW:
            case VAR:
            case WHILE:
            case CONTINUE:
            case TRY:
            case FUNCTION:
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
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:327:10: importDecl
                    {
                    pushFollow(FOLLOW_importDecl_in_moduleItem2138);
                    importDecl5=importDecl();
                    _fsp--;

                     value = importDecl5; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:328:10: classDefinition
                    {
                    pushFollow(FOLLOW_classDefinition_in_moduleItem2154);
                    classDefinition6=classDefinition();
                    _fsp--;

                     value = classDefinition6; 

                    }
                    break;
                case 3 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:329:10: statement
                    {
                    pushFollow(FOLLOW_statement_in_moduleItem2170);
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:330:1: importDecl returns [JCTree value] : IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI ;
    public final JCTree importDecl() throws RecognitionException {
        JCTree value = null;

        Token STAR10=null;
        Token IMPORT11=null;
        JCIdent identifier8 = null;

        name_return name9 = null;


         JCExpression pid = null; 
        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:332:9: ( IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:332:11: IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI
            {
            IMPORT11=(Token)input.LT(1);
            match(input,IMPORT,FOLLOW_IMPORT_in_importDecl2206); 
            pushFollow(FOLLOW_identifier_in_importDecl2209);
            identifier8=identifier();
            _fsp--;

             pid = identifier8; 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:333:18: ( DOT name )*
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
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:333:20: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_importDecl2234); 
            	    pushFollow(FOLLOW_name_in_importDecl2236);
            	    name9=name();
            	    _fsp--;

            	     pid = F.at(name9.pos).Select(pid, name9.value); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:334:18: ( DOT STAR )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==DOT) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:334:20: DOT STAR
                    {
                    match(input,DOT,FOLLOW_DOT_in_importDecl2265); 
                    STAR10=(Token)input.LT(1);
                    match(input,STAR,FOLLOW_STAR_in_importDecl2267); 
                     pid = F.at(pos(STAR10)).Select(pid, names.asterisk); 

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_importDecl2276); 
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:336:1: classDefinition returns [JFXClassDeclaration value] : modifierFlags CLASS name supers LBRACE classMembers RBRACE ;
    public final JFXClassDeclaration classDefinition() throws RecognitionException {
        JFXClassDeclaration value = null;

        Token CLASS12=null;
        JCModifiers modifierFlags13 = null;

        name_return name14 = null;

        ListBuffer<Name> supers15 = null;

        ListBuffer<JFXMemberDeclaration> classMembers16 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:337:2: ( modifierFlags CLASS name supers LBRACE classMembers RBRACE )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:337:4: modifierFlags CLASS name supers LBRACE classMembers RBRACE
            {
            pushFollow(FOLLOW_modifierFlags_in_classDefinition2307);
            modifierFlags13=modifierFlags();
            _fsp--;

            CLASS12=(Token)input.LT(1);
            match(input,CLASS,FOLLOW_CLASS_in_classDefinition2310); 
            pushFollow(FOLLOW_name_in_classDefinition2312);
            name14=name();
            _fsp--;

            pushFollow(FOLLOW_supers_in_classDefinition2314);
            supers15=supers();
            _fsp--;

            match(input,LBRACE,FOLLOW_LBRACE_in_classDefinition2316); 
            pushFollow(FOLLOW_classMembers_in_classDefinition2318);
            classMembers16=classMembers();
            _fsp--;

            match(input,RBRACE,FOLLOW_RBRACE_in_classDefinition2320); 
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:340:1: supers returns [ListBuffer<Name> names = new ListBuffer<Name>()] : ( EXTENDS name1= name ( COMMA namen= name )* )? ;
    public final ListBuffer<Name> supers() throws RecognitionException {
        ListBuffer<Name> names =  new ListBuffer<Name>();

        name_return name1 = null;

        name_return namen = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:341:2: ( ( EXTENDS name1= name ( COMMA namen= name )* )? )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:341:4: ( EXTENDS name1= name ( COMMA namen= name )* )?
            {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:341:4: ( EXTENDS name1= name ( COMMA namen= name )* )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==EXTENDS) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:341:5: EXTENDS name1= name ( COMMA namen= name )*
                    {
                    match(input,EXTENDS,FOLLOW_EXTENDS_in_supers2345); 
                    pushFollow(FOLLOW_name_in_supers2349);
                    name1=name();
                    _fsp--;

                     names.append(name1.value); 
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:342:12: ( COMMA namen= name )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==COMMA) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:342:14: COMMA namen= name
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_supers2377); 
                    	    pushFollow(FOLLOW_name_in_supers2381);
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:344:1: classMembers returns [ListBuffer<JFXMemberDeclaration> mems = new ListBuffer<JFXMemberDeclaration>()] : ( attributeDefinition | functionDefinition )* ;
    public final ListBuffer<JFXMemberDeclaration> classMembers() throws RecognitionException {
        ListBuffer<JFXMemberDeclaration> mems =  new ListBuffer<JFXMemberDeclaration>();

        JFXAttributeDeclaration attributeDefinition17 = null;

        JFXFunctionMemberDeclaration functionDefinition18 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:345:2: ( ( attributeDefinition | functionDefinition )* )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:345:3: ( attributeDefinition | functionDefinition )*
            {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:345:3: ( attributeDefinition | functionDefinition )*
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
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:345:5: attributeDefinition
            	    {
            	    pushFollow(FOLLOW_attributeDefinition_in_classMembers2415);
            	    attributeDefinition17=attributeDefinition();
            	    _fsp--;

            	     mems.append(attributeDefinition17); 

            	    }
            	    break;
            	case 2 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:346:5: functionDefinition
            	    {
            	    pushFollow(FOLLOW_functionDefinition_in_classMembers2433);
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:348:1: attributeDefinition returns [JFXAttributeDeclaration def] : modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? SEMI ;
    public final JFXAttributeDeclaration attributeDefinition() throws RecognitionException {
        JFXAttributeDeclaration def = null;

        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:349:2: ( modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? SEMI )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:349:4: modifierFlags ATTRIBUTE name typeReference ( EQ bindOpt expression | inverseClause )? SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_attributeDefinition2462);
            modifierFlags();
            _fsp--;

            match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeDefinition2464); 
            pushFollow(FOLLOW_name_in_attributeDefinition2466);
            name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_attributeDefinition2468);
            typeReference();
            _fsp--;

            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:349:47: ( EQ bindOpt expression | inverseClause )?
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
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:349:48: EQ bindOpt expression
                    {
                    match(input,EQ,FOLLOW_EQ_in_attributeDefinition2471); 
                    pushFollow(FOLLOW_bindOpt_in_attributeDefinition2473);
                    bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_attributeDefinition2476);
                    expression();
                    _fsp--;


                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:349:73: inverseClause
                    {
                    pushFollow(FOLLOW_inverseClause_in_attributeDefinition2480);
                    inverseClause();
                    _fsp--;


                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_attributeDefinition2485); 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:351:1: inverseClause returns [JFXMemberSelector inverse = null] : INVERSE memberSelector ;
    public final JFXMemberSelector inverseClause() throws RecognitionException {
        JFXMemberSelector inverse =  null;

        JFXMemberSelector memberSelector19 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:352:2: ( INVERSE memberSelector )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:352:4: INVERSE memberSelector
            {
            match(input,INVERSE,FOLLOW_INVERSE_in_inverseClause2507); 
            pushFollow(FOLLOW_memberSelector_in_inverseClause2509);
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:353:1: functionDefinition returns [JFXFunctionMemberDeclaration def] : modifierFlags FUNCTION name formalParameters typeReference blockExpression ;
    public final JFXFunctionMemberDeclaration functionDefinition() throws RecognitionException {
        JFXFunctionMemberDeclaration def = null;

        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:354:2: ( modifierFlags FUNCTION name formalParameters typeReference blockExpression )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:354:4: modifierFlags FUNCTION name formalParameters typeReference blockExpression
            {
            pushFollow(FOLLOW_modifierFlags_in_functionDefinition2527);
            modifierFlags();
            _fsp--;

            match(input,FUNCTION,FOLLOW_FUNCTION_in_functionDefinition2529); 
            pushFollow(FOLLOW_name_in_functionDefinition2533);
            name();
            _fsp--;

            pushFollow(FOLLOW_formalParameters_in_functionDefinition2535);
            formalParameters();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_functionDefinition2539);
            typeReference();
            _fsp--;

            pushFollow(FOLLOW_blockExpression_in_functionDefinition2542);
            blockExpression();
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:356:1: modifierFlags returns [JCModifiers mods] : (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? ;
    public final JCModifiers modifierFlags() throws RecognitionException {
        JCModifiers mods = null;

        long om1 = 0;

        long am1 = 0;

        long am2 = 0;

        long om2 = 0;


         long flags = 0; 
        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:358:2: ( (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:358:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
            {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:358:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
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
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:358:6: om1= otherModifier (am1= accessModifier )?
                    {
                    pushFollow(FOLLOW_otherModifier_in_modifierFlags2566);
                    om1=otherModifier();
                    _fsp--;

                     flags |= om1; 
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:359:3: (am1= accessModifier )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( ((LA10_0>=PRIVATE && LA10_0<=PUBLIC)) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:359:5: am1= accessModifier
                            {
                            pushFollow(FOLLOW_accessModifier_in_modifierFlags2579);
                            am1=accessModifier();
                            _fsp--;

                             flags |= am1; 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:360:6: am2= accessModifier (om2= otherModifier )?
                    {
                    pushFollow(FOLLOW_accessModifier_in_modifierFlags2601);
                    am2=accessModifier();
                    _fsp--;

                     flags |= am2; 
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:361:3: (om2= otherModifier )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==ABSTRACT||LA11_0==READONLY) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:361:5: om2= otherModifier
                            {
                            pushFollow(FOLLOW_otherModifier_in_modifierFlags2614);
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:364:1: accessModifier returns [long flags = 0] : ( PUBLIC | PRIVATE | PROTECTED ) ;
    public final long accessModifier() throws RecognitionException {
        long flags =  0;

        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:365:2: ( ( PUBLIC | PRIVATE | PROTECTED ) )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:365:4: ( PUBLIC | PRIVATE | PROTECTED )
            {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:365:4: ( PUBLIC | PRIVATE | PROTECTED )
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
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:365:5: PUBLIC
                    {
                    match(input,PUBLIC,FOLLOW_PUBLIC_in_accessModifier2662); 
                     flags |= Flags.PUBLIC; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:366:5: PRIVATE
                    {
                    match(input,PRIVATE,FOLLOW_PRIVATE_in_accessModifier2682); 
                     flags |= Flags.PUBLIC; 

                    }
                    break;
                case 3 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:367:5: PROTECTED
                    {
                    match(input,PROTECTED,FOLLOW_PROTECTED_in_accessModifier2701); 
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:368:1: otherModifier returns [long flags = 0] : ( ABSTRACT | READONLY ) ;
    public final long otherModifier() throws RecognitionException {
        long flags =  0;

        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:369:2: ( ( ABSTRACT | READONLY ) )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:369:4: ( ABSTRACT | READONLY )
            {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:369:4: ( ABSTRACT | READONLY )
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
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:369:5: ABSTRACT
                    {
                    match(input,ABSTRACT,FOLLOW_ABSTRACT_in_otherModifier2728); 
                     flags |= Flags.ABSTRACT; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:370:5: READONLY
                    {
                    match(input,READONLY,FOLLOW_READONLY_in_otherModifier2746); 
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:371:1: memberSelector returns [JFXMemberSelector value] : name1= name DOT name2= name ;
    public final JFXMemberSelector memberSelector() throws RecognitionException {
        JFXMemberSelector value = null;

        name_return name1 = null;

        name_return name2 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:372:2: (name1= name DOT name2= name )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:372:4: name1= name DOT name2= name
            {
            pushFollow(FOLLOW_name_in_memberSelector2775);
            name1=name();
            _fsp--;

            match(input,DOT,FOLLOW_DOT_in_memberSelector2779); 
            pushFollow(FOLLOW_name_in_memberSelector2785);
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:374:1: formalParameters returns [ListBuffer<JCTree> params = new ListBuffer<JCTree>()] : LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN ;
    public final ListBuffer<JCTree> formalParameters() throws RecognitionException {
        ListBuffer<JCTree> params =  new ListBuffer<JCTree>();

        JFXVar fp0 = null;

        JFXVar fpn = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:375:2: ( LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:375:4: LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_formalParameters2803); 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:375:12: (fp0= formalParameter ( COMMA fpn= formalParameter )* )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==QUOTED_IDENTIFIER||LA16_0==IDENTIFIER) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:375:14: fp0= formalParameter ( COMMA fpn= formalParameter )*
                    {
                    pushFollow(FOLLOW_formalParameter_in_formalParameters2810);
                    fp0=formalParameter();
                    _fsp--;

                     params.append(fp0); 
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:376:12: ( COMMA fpn= formalParameter )*
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( (LA15_0==COMMA) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:376:14: COMMA fpn= formalParameter
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_formalParameters2828); 
                    	    pushFollow(FOLLOW_formalParameter_in_formalParameters2834);
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

            match(input,RPAREN,FOLLOW_RPAREN_in_formalParameters2845); 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:378:1: formalParameter returns [JFXVar var] : name typeReference ;
    public final JFXVar formalParameter() throws RecognitionException {
        JFXVar var = null;

        name_return name20 = null;

        JFXType typeReference21 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:379:2: ( name typeReference )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:379:4: name typeReference
            {
            pushFollow(FOLLOW_name_in_formalParameter2860);
            name20=name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_formalParameter2862);
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:381:1: block returns [JCBlock value] : LBRACE (s1= statement | e1= expression ) ( SEMI (sn= statement | en= expression ) )* ( SEMI )? RBRACE ;
    public final JCBlock block() throws RecognitionException {
        JCBlock value = null;

        Token LBRACE22=null;
        JCStatement s1 = null;

        JCExpression e1 = null;

        JCStatement sn = null;

        JCExpression en = null;


         ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>();
        	 	
        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:384:2: ( LBRACE (s1= statement | e1= expression ) ( SEMI (sn= statement | en= expression ) )* ( SEMI )? RBRACE )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:384:4: LBRACE (s1= statement | e1= expression ) ( SEMI (sn= statement | en= expression ) )* ( SEMI )? RBRACE
            {
            LBRACE22=(Token)input.LT(1);
            match(input,LBRACE,FOLLOW_LBRACE_in_block2888); 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:385:4: (s1= statement | e1= expression )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==ABSTRACT||LA17_0==BREAK||(LA17_0>=RETURN && LA17_0<=VAR)||(LA17_0>=WHILE && LA17_0<=CONTINUE)||LA17_0==TRY||(LA17_0>=PRIVATE && LA17_0<=READONLY)) ) {
                alt17=1;
            }
            else if ( ((LA17_0>=POUND && LA17_0<=TYPEOF)||LA17_0==IF||(LA17_0>=THIS && LA17_0<=FALSE)||(LA17_0>=NOT && LA17_0<=NEW)||LA17_0==SUPER||(LA17_0>=SIZEOF && LA17_0<=LPAREN)||(LA17_0>=PLUSPLUS && LA17_0<=SUBSUB)||(LA17_0>=QUES && LA17_0<=STRING_LITERAL)||(LA17_0>=QUOTE_LBRACE_STRING_LITERAL && LA17_0<=LBRACE)||(LA17_0>=QUOTED_IDENTIFIER && LA17_0<=INTEGER_LITERAL)||LA17_0==FLOATING_POINT_LITERAL||LA17_0==IDENTIFIER) ) {
                alt17=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("385:4: (s1= statement | e1= expression )", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:385:9: s1= statement
                    {
                    pushFollow(FOLLOW_statement_in_block2900);
                    s1=statement();
                    _fsp--;

                     stats.append(s1); 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:386:9: e1= expression
                    {
                    pushFollow(FOLLOW_expression_in_block2917);
                    e1=expression();
                    _fsp--;

                     stats.append(F.Exec(e1)); 

                    }
                    break;

            }

            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:388:4: ( SEMI (sn= statement | en= expression ) )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==SEMI) ) {
                    int LA19_1 = input.LA(2);

                    if ( ((LA19_1>=POUND && LA19_1<=TYPEOF)||LA19_1==ABSTRACT||LA19_1==BREAK||(LA19_1>=RETURN && LA19_1<=VAR)||LA19_1==IF||(LA19_1>=THIS && LA19_1<=FALSE)||(LA19_1>=WHILE && LA19_1<=CONTINUE)||LA19_1==TRY||(LA19_1>=NOT && LA19_1<=READONLY)||LA19_1==SUPER||(LA19_1>=SIZEOF && LA19_1<=LPAREN)||(LA19_1>=PLUSPLUS && LA19_1<=SUBSUB)||(LA19_1>=QUES && LA19_1<=STRING_LITERAL)||(LA19_1>=QUOTE_LBRACE_STRING_LITERAL && LA19_1<=LBRACE)||(LA19_1>=QUOTED_IDENTIFIER && LA19_1<=INTEGER_LITERAL)||LA19_1==FLOATING_POINT_LITERAL||LA19_1==IDENTIFIER) ) {
                        alt19=1;
                    }


                }


                switch (alt19) {
            	case 1 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:388:6: SEMI (sn= statement | en= expression )
            	    {
            	    match(input,SEMI,FOLLOW_SEMI_in_block2934); 
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:389:6: (sn= statement | en= expression )
            	    int alt18=2;
            	    int LA18_0 = input.LA(1);

            	    if ( (LA18_0==ABSTRACT||LA18_0==BREAK||(LA18_0>=RETURN && LA18_0<=VAR)||(LA18_0>=WHILE && LA18_0<=CONTINUE)||LA18_0==TRY||(LA18_0>=PRIVATE && LA18_0<=READONLY)) ) {
            	        alt18=1;
            	    }
            	    else if ( ((LA18_0>=POUND && LA18_0<=TYPEOF)||LA18_0==IF||(LA18_0>=THIS && LA18_0<=FALSE)||(LA18_0>=NOT && LA18_0<=NEW)||LA18_0==SUPER||(LA18_0>=SIZEOF && LA18_0<=LPAREN)||(LA18_0>=PLUSPLUS && LA18_0<=SUBSUB)||(LA18_0>=QUES && LA18_0<=STRING_LITERAL)||(LA18_0>=QUOTE_LBRACE_STRING_LITERAL && LA18_0<=LBRACE)||(LA18_0>=QUOTED_IDENTIFIER && LA18_0<=INTEGER_LITERAL)||LA18_0==FLOATING_POINT_LITERAL||LA18_0==IDENTIFIER) ) {
            	        alt18=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("389:6: (sn= statement | en= expression )", 18, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt18) {
            	        case 1 :
            	            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:389:9: sn= statement
            	            {
            	            pushFollow(FOLLOW_statement_in_block2946);
            	            sn=statement();
            	            _fsp--;

            	             stats.append(sn); 

            	            }
            	            break;
            	        case 2 :
            	            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:390:9: en= expression
            	            {
            	            pushFollow(FOLLOW_expression_in_block2963);
            	            en=expression();
            	            _fsp--;

            	             stats.append(F.Exec(en)); 

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);

            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:393:4: ( SEMI )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==SEMI) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:393:4: SEMI
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_block2986); 

                    }
                    break;

            }

            match(input,RBRACE,FOLLOW_RBRACE_in_block2989); 
             value = F.at(pos(LBRACE22)).Block(0L, stats.toList()); 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:395:1: blockExpression returns [JFXBlockExpression expr] : LBRACE (s1= statement | e1= expression ) ( SEMI (sn= statement | en= expression ) )* ( SEMI )? RBRACE ;
    public final JFXBlockExpression blockExpression() throws RecognitionException {
        JFXBlockExpression expr = null;

        Token LBRACE23=null;
        JCStatement s1 = null;

        JCExpression e1 = null;

        JCStatement sn = null;

        JCExpression en = null;


         ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>();
        	 	  JCExpression pending = null;
        	 	
        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:399:2: ( LBRACE (s1= statement | e1= expression ) ( SEMI (sn= statement | en= expression ) )* ( SEMI )? RBRACE )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:399:4: LBRACE (s1= statement | e1= expression ) ( SEMI (sn= statement | en= expression ) )* ( SEMI )? RBRACE
            {
            LBRACE23=(Token)input.LT(1);
            match(input,LBRACE,FOLLOW_LBRACE_in_blockExpression3015); 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:400:4: (s1= statement | e1= expression )
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==ABSTRACT||LA21_0==BREAK||(LA21_0>=RETURN && LA21_0<=VAR)||(LA21_0>=WHILE && LA21_0<=CONTINUE)||LA21_0==TRY||(LA21_0>=PRIVATE && LA21_0<=READONLY)) ) {
                alt21=1;
            }
            else if ( ((LA21_0>=POUND && LA21_0<=TYPEOF)||LA21_0==IF||(LA21_0>=THIS && LA21_0<=FALSE)||(LA21_0>=NOT && LA21_0<=NEW)||LA21_0==SUPER||(LA21_0>=SIZEOF && LA21_0<=LPAREN)||(LA21_0>=PLUSPLUS && LA21_0<=SUBSUB)||(LA21_0>=QUES && LA21_0<=STRING_LITERAL)||(LA21_0>=QUOTE_LBRACE_STRING_LITERAL && LA21_0<=LBRACE)||(LA21_0>=QUOTED_IDENTIFIER && LA21_0<=INTEGER_LITERAL)||LA21_0==FLOATING_POINT_LITERAL||LA21_0==IDENTIFIER) ) {
                alt21=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("400:4: (s1= statement | e1= expression )", 21, 0, input);

                throw nvae;
            }
            switch (alt21) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:400:9: s1= statement
                    {
                    pushFollow(FOLLOW_statement_in_blockExpression3027);
                    s1=statement();
                    _fsp--;

                     stats.append(s1); 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:401:9: e1= expression
                    {
                    pushFollow(FOLLOW_expression_in_blockExpression3044);
                    e1=expression();
                    _fsp--;

                     pending = e1; 

                    }
                    break;

            }

            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:403:4: ( SEMI (sn= statement | en= expression ) )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==SEMI) ) {
                    int LA23_1 = input.LA(2);

                    if ( ((LA23_1>=POUND && LA23_1<=TYPEOF)||LA23_1==ABSTRACT||LA23_1==BREAK||(LA23_1>=RETURN && LA23_1<=VAR)||LA23_1==IF||(LA23_1>=THIS && LA23_1<=FALSE)||(LA23_1>=WHILE && LA23_1<=CONTINUE)||LA23_1==TRY||(LA23_1>=NOT && LA23_1<=READONLY)||LA23_1==SUPER||(LA23_1>=SIZEOF && LA23_1<=LPAREN)||(LA23_1>=PLUSPLUS && LA23_1<=SUBSUB)||(LA23_1>=QUES && LA23_1<=STRING_LITERAL)||(LA23_1>=QUOTE_LBRACE_STRING_LITERAL && LA23_1<=LBRACE)||(LA23_1>=QUOTED_IDENTIFIER && LA23_1<=INTEGER_LITERAL)||LA23_1==FLOATING_POINT_LITERAL||LA23_1==IDENTIFIER) ) {
                        alt23=1;
                    }


                }


                switch (alt23) {
            	case 1 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:403:6: SEMI (sn= statement | en= expression )
            	    {
            	    match(input,SEMI,FOLLOW_SEMI_in_blockExpression3061); 
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:404:6: (sn= statement | en= expression )
            	    int alt22=2;
            	    int LA22_0 = input.LA(1);

            	    if ( (LA22_0==ABSTRACT||LA22_0==BREAK||(LA22_0>=RETURN && LA22_0<=VAR)||(LA22_0>=WHILE && LA22_0<=CONTINUE)||LA22_0==TRY||(LA22_0>=PRIVATE && LA22_0<=READONLY)) ) {
            	        alt22=1;
            	    }
            	    else if ( ((LA22_0>=POUND && LA22_0<=TYPEOF)||LA22_0==IF||(LA22_0>=THIS && LA22_0<=FALSE)||(LA22_0>=NOT && LA22_0<=NEW)||LA22_0==SUPER||(LA22_0>=SIZEOF && LA22_0<=LPAREN)||(LA22_0>=PLUSPLUS && LA22_0<=SUBSUB)||(LA22_0>=QUES && LA22_0<=STRING_LITERAL)||(LA22_0>=QUOTE_LBRACE_STRING_LITERAL && LA22_0<=LBRACE)||(LA22_0>=QUOTED_IDENTIFIER && LA22_0<=INTEGER_LITERAL)||LA22_0==FLOATING_POINT_LITERAL||LA22_0==IDENTIFIER) ) {
            	        alt22=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("404:6: (sn= statement | en= expression )", 22, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt22) {
            	        case 1 :
            	            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:404:9: sn= statement
            	            {
            	            pushFollow(FOLLOW_statement_in_blockExpression3073);
            	            sn=statement();
            	            _fsp--;

            	             if (pending!=null) { stats.append(F.Exec(pending)); pending = null; } 
            	            						  stats.append(sn); 

            	            }
            	            break;
            	        case 2 :
            	            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:406:9: en= expression
            	            {
            	            pushFollow(FOLLOW_expression_in_blockExpression3090);
            	            en=expression();
            	            _fsp--;

            	             if (pending!=null) { stats.append(F.Exec(pending)); } 
            	            						  pending = en; 

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop23;
                }
            } while (true);

            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:410:4: ( SEMI )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==SEMI) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:410:4: SEMI
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_blockExpression3113); 

                    }
                    break;

            }

            match(input,RBRACE,FOLLOW_RBRACE_in_blockExpression3116); 
             expr = F.at(pos(LBRACE23)).BlockExpression(0L, stats.toList(), pending); 

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


    // $ANTLR start statement
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:412:1: statement returns [JCStatement value] : ( variableDeclaration | functionDefinition | WHILE LPAREN expression RPAREN block | BREAK | CONTINUE | THROW expression | returnStatement | tryStatement );
    public final JCStatement statement() throws RecognitionException {
        JCStatement value = null;

        Token WHILE26=null;
        Token BREAK29=null;
        Token CONTINUE30=null;
        Token THROW31=null;
        JCStatement variableDeclaration24 = null;

        JFXFunctionMemberDeclaration functionDefinition25 = null;

        JCExpression expression27 = null;

        JCBlock block28 = null;

        JCExpression expression32 = null;

        JCStatement returnStatement33 = null;

        JCStatement tryStatement34 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:413:2: ( variableDeclaration | functionDefinition | WHILE LPAREN expression RPAREN block | BREAK | CONTINUE | THROW expression | returnStatement | tryStatement )
            int alt25=8;
            switch ( input.LA(1) ) {
            case VAR:
                {
                alt25=1;
                }
                break;
            case ABSTRACT:
            case PRIVATE:
            case PROTECTED:
            case PUBLIC:
            case FUNCTION:
            case READONLY:
                {
                alt25=2;
                }
                break;
            case WHILE:
                {
                alt25=3;
                }
                break;
            case BREAK:
                {
                alt25=4;
                }
                break;
            case CONTINUE:
                {
                alt25=5;
                }
                break;
            case THROW:
                {
                alt25=6;
                }
                break;
            case RETURN:
                {
                alt25=7;
                }
                break;
            case TRY:
                {
                alt25=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("412:1: statement returns [JCStatement value] : ( variableDeclaration | functionDefinition | WHILE LPAREN expression RPAREN block | BREAK | CONTINUE | THROW expression | returnStatement | tryStatement );", 25, 0, input);

                throw nvae;
            }

            switch (alt25) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:413:4: variableDeclaration
                    {
                    pushFollow(FOLLOW_variableDeclaration_in_statement3135);
                    variableDeclaration24=variableDeclaration();
                    _fsp--;

                     value = variableDeclaration24; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:414:4: functionDefinition
                    {
                    pushFollow(FOLLOW_functionDefinition_in_statement3145);
                    functionDefinition25=functionDefinition();
                    _fsp--;

                     value = functionDefinition25; 

                    }
                    break;
                case 3 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:415:11: WHILE LPAREN expression RPAREN block
                    {
                    WHILE26=(Token)input.LT(1);
                    match(input,WHILE,FOLLOW_WHILE_in_statement3161); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_statement3163); 
                    pushFollow(FOLLOW_expression_in_statement3165);
                    expression27=expression();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_statement3167); 
                    pushFollow(FOLLOW_block_in_statement3169);
                    block28=block();
                    _fsp--;

                     value = F.at(pos(WHILE26)).WhileLoop(expression27, block28); 

                    }
                    break;
                case 4 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:416:4: BREAK
                    {
                    BREAK29=(Token)input.LT(1);
                    match(input,BREAK,FOLLOW_BREAK_in_statement3176); 
                     value = F.at(pos(BREAK29)).Break(null); 

                    }
                    break;
                case 5 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:417:4: CONTINUE
                    {
                    CONTINUE30=(Token)input.LT(1);
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_statement3190); 
                     value = F.at(pos(CONTINUE30)).Continue(null); 

                    }
                    break;
                case 6 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:418:11: THROW expression
                    {
                    THROW31=(Token)input.LT(1);
                    match(input,THROW,FOLLOW_THROW_in_statement3211); 
                    pushFollow(FOLLOW_expression_in_statement3213);
                    expression32=expression();
                    _fsp--;

                     value = F.at(pos(THROW31)).Throw(expression32); 

                    }
                    break;
                case 7 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:419:11: returnStatement
                    {
                    pushFollow(FOLLOW_returnStatement_in_statement3234);
                    returnStatement33=returnStatement();
                    _fsp--;

                     value = returnStatement33; 

                    }
                    break;
                case 8 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:420:11: tryStatement
                    {
                    pushFollow(FOLLOW_tryStatement_in_statement3251);
                    tryStatement34=tryStatement();
                    _fsp--;

                     value = tryStatement34; 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:422:1: variableDeclaration returns [JCStatement value] : VAR name typeReference ( EQ bindOpt expression | ) ;
    public final JCStatement variableDeclaration() throws RecognitionException {
        JCStatement value = null;

        Token VAR35=null;
        name_return name36 = null;

        JFXType typeReference37 = null;

        JCExpression expression38 = null;

        JavafxBindStatus bindOpt39 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:423:2: ( VAR name typeReference ( EQ bindOpt expression | ) )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:423:4: VAR name typeReference ( EQ bindOpt expression | )
            {
            VAR35=(Token)input.LT(1);
            match(input,VAR,FOLLOW_VAR_in_variableDeclaration3280); 
            pushFollow(FOLLOW_name_in_variableDeclaration3283);
            name36=name();
            _fsp--;

            pushFollow(FOLLOW_typeReference_in_variableDeclaration3286);
            typeReference37=typeReference();
            _fsp--;

            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:424:6: ( EQ bindOpt expression | )
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==EQ) ) {
                alt26=1;
            }
            else if ( (LA26_0==EOF||LA26_0==ABSTRACT||LA26_0==BREAK||LA26_0==CLASS||(LA26_0>=RETURN && LA26_0<=VAR)||LA26_0==IMPORT||(LA26_0>=WHILE && LA26_0<=CONTINUE)||LA26_0==TRY||(LA26_0>=PRIVATE && LA26_0<=READONLY)||LA26_0==SEMI||LA26_0==RBRACE) ) {
                alt26=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("424:6: ( EQ bindOpt expression | )", 26, 0, input);

                throw nvae;
            }
            switch (alt26) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:424:8: EQ bindOpt expression
                    {
                    match(input,EQ,FOLLOW_EQ_in_variableDeclaration3297); 
                    pushFollow(FOLLOW_bindOpt_in_variableDeclaration3299);
                    bindOpt39=bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_variableDeclaration3302);
                    expression38=expression();
                    _fsp--;

                     value = F.at(pos(VAR35)).VarInit(name36.value, typeReference37, 
                    	    							expression38, bindOpt39); 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:426:13: 
                    {
                     value = F.at(pos(VAR35)).VarStatement(name36.value, typeReference37); 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:429:1: bindOpt returns [JavafxBindStatus status = UNBOUND] : ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? ;
    public final JavafxBindStatus bindOpt() throws RecognitionException {
        JavafxBindStatus status =  UNBOUND;

        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:430:2: ( ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:430:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
            {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:430:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
            int alt30=4;
            switch ( input.LA(1) ) {
                case BIND:
                    {
                    alt30=1;
                    }
                    break;
                case STAYS:
                    {
                    alt30=2;
                    }
                    break;
                case TIE:
                    {
                    alt30=3;
                    }
                    break;
            }

            switch (alt30) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:430:6: BIND ( LAZY )?
                    {
                    match(input,BIND,FOLLOW_BIND_in_bindOpt3352); 
                     status = UNIDIBIND; 
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:431:8: ( LAZY )?
                    int alt27=2;
                    int LA27_0 = input.LA(1);

                    if ( (LA27_0==LAZY) ) {
                        alt27=1;
                    }
                    switch (alt27) {
                        case 1 :
                            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:431:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3368); 
                             status = LAZY_UNIDIBIND; 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:432:6: STAYS ( LAZY )?
                    {
                    match(input,STAYS,FOLLOW_STAYS_in_bindOpt3383); 
                     status = UNIDIBIND; 
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:433:8: ( LAZY )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==LAZY) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:433:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3399); 
                             status = LAZY_UNIDIBIND; 

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:434:6: TIE ( LAZY )?
                    {
                    match(input,TIE,FOLLOW_TIE_in_bindOpt3414); 
                     status = BIDIBIND; 
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:435:8: ( LAZY )?
                    int alt29=2;
                    int LA29_0 = input.LA(1);

                    if ( (LA29_0==LAZY) ) {
                        alt29=1;
                    }
                    switch (alt29) {
                        case 1 :
                            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:435:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3430); 
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:437:1: returnStatement returns [JCStatement value] : RETURN ( expression )? ;
    public final JCStatement returnStatement() throws RecognitionException {
        JCStatement value = null;

        Token RETURN41=null;
        JCExpression expression40 = null;


         JCExpression expr = null; 
        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:439:2: ( RETURN ( expression )? )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:439:4: RETURN ( expression )?
            {
            RETURN41=(Token)input.LT(1);
            match(input,RETURN,FOLLOW_RETURN_in_returnStatement3464); 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:439:11: ( expression )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( ((LA31_0>=POUND && LA31_0<=TYPEOF)||LA31_0==IF||(LA31_0>=THIS && LA31_0<=FALSE)||(LA31_0>=NOT && LA31_0<=NEW)||LA31_0==SUPER||(LA31_0>=SIZEOF && LA31_0<=LPAREN)||(LA31_0>=PLUSPLUS && LA31_0<=SUBSUB)||(LA31_0>=QUES && LA31_0<=STRING_LITERAL)||(LA31_0>=QUOTE_LBRACE_STRING_LITERAL && LA31_0<=LBRACE)||(LA31_0>=QUOTED_IDENTIFIER && LA31_0<=INTEGER_LITERAL)||LA31_0==FLOATING_POINT_LITERAL||LA31_0==IDENTIFIER) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:439:12: expression
                    {
                    pushFollow(FOLLOW_expression_in_returnStatement3467);
                    expression40=expression();
                    _fsp--;

                     expr = expression40; 

                    }
                    break;

            }

             value = F.at(pos(RETURN41)).Return(expr); 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:442:1: tryStatement returns [JCStatement value] : TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? ) ;
    public final JCStatement tryStatement() throws RecognitionException {
        JCStatement value = null;

        Token TRY43=null;
        JCBlock tb = null;

        JCBlock fb1 = null;

        JCBlock fb2 = null;

        JCCatch catchClause42 = null;


        	JCBlock body;
        		ListBuffer<JCCatch> catchers = new ListBuffer<JCCatch>();
        		JCBlock finalBlock = null;
        	
        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:447:2: ( TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? ) )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:447:4: TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )
            {
            TRY43=(Token)input.LT(1);
            match(input,TRY,FOLLOW_TRY_in_tryStatement3510); 
            pushFollow(FOLLOW_block_in_tryStatement3516);
            tb=block();
            _fsp--;

             body = tb; 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:448:5: ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==FINALLY) ) {
                alt34=1;
            }
            else if ( (LA34_0==CATCH) ) {
                alt34=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("448:5: ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )", 34, 0, input);

                throw nvae;
            }
            switch (alt34) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:448:7: FINALLY fb1= block
                    {
                    match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement3529); 
                    pushFollow(FOLLOW_block_in_tryStatement3535);
                    fb1=block();
                    _fsp--;

                     finalBlock = fb1; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:449:10: ( catchClause )+ ( FINALLY fb2= block )?
                    {
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:449:10: ( catchClause )+
                    int cnt32=0;
                    loop32:
                    do {
                        int alt32=2;
                        int LA32_0 = input.LA(1);

                        if ( (LA32_0==CATCH) ) {
                            alt32=1;
                        }


                        switch (alt32) {
                    	case 1 :
                    	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:449:11: catchClause
                    	    {
                    	    pushFollow(FOLLOW_catchClause_in_tryStatement3551);
                    	    catchClause42=catchClause();
                    	    _fsp--;

                    	     catchers.append(catchClause42); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt32 >= 1 ) break loop32;
                                EarlyExitException eee =
                                    new EarlyExitException(32, input);
                                throw eee;
                        }
                        cnt32++;
                    } while (true);

                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:450:10: ( FINALLY fb2= block )?
                    int alt33=2;
                    int LA33_0 = input.LA(1);

                    if ( (LA33_0==FINALLY) ) {
                        alt33=1;
                    }
                    switch (alt33) {
                        case 1 :
                            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:450:11: FINALLY fb2= block
                            {
                            match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement3574); 
                            pushFollow(FOLLOW_block_in_tryStatement3579);
                            fb2=block();
                            _fsp--;

                             finalBlock = fb2; 

                            }
                            break;

                    }


                    }
                    break;

            }

             value = F.at(pos(TRY43)).Try(body, catchers.toList(), finalBlock); 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:453:1: catchClause returns [JCCatch value] : CATCH LPAREN name ( COLON identifier )? RPAREN block ;
    public final JCCatch catchClause() throws RecognitionException {
        JCCatch value = null;

        Token CATCH46=null;
        name_return name44 = null;

        JCIdent identifier45 = null;

        JCBlock block47 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:454:2: ( CATCH LPAREN name ( COLON identifier )? RPAREN block )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:454:4: CATCH LPAREN name ( COLON identifier )? RPAREN block
            {
            CATCH46=(Token)input.LT(1);
            match(input,CATCH,FOLLOW_CATCH_in_catchClause3618); 
            match(input,LPAREN,FOLLOW_LPAREN_in_catchClause3621); 
            pushFollow(FOLLOW_name_in_catchClause3624);
            name44=name();
            _fsp--;

            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:455:4: ( COLON identifier )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==COLON) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:455:5: COLON identifier
                    {
                    match(input,COLON,FOLLOW_COLON_in_catchClause3632); 
                    pushFollow(FOLLOW_identifier_in_catchClause3634);
                    identifier45=identifier();
                    _fsp--;


                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_catchClause3643); 
            pushFollow(FOLLOW_block_in_catchClause3647);
            block47=block();
            _fsp--;

             JCModifiers mods = F.at(name44.pos).Modifiers(Flags.PARAMETER);
            	  					  JCVariableDecl formal = F.at(name44.pos).VarDef(mods, name44.value, identifier45, null);
            	  					  value = F.at(pos(CATCH46)).Catch(formal, block47); 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:460:1: expression returns [JCExpression expr] : ( ifExpression | suffixedExpression );
    public final JCExpression expression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression ifExpression48 = null;

        JCExpression suffixedExpression49 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:461:9: ( ifExpression | suffixedExpression )
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==IF) ) {
                alt36=1;
            }
            else if ( ((LA36_0>=POUND && LA36_0<=TYPEOF)||(LA36_0>=THIS && LA36_0<=FALSE)||(LA36_0>=NOT && LA36_0<=NEW)||LA36_0==SUPER||(LA36_0>=SIZEOF && LA36_0<=LPAREN)||(LA36_0>=PLUSPLUS && LA36_0<=SUBSUB)||(LA36_0>=QUES && LA36_0<=STRING_LITERAL)||(LA36_0>=QUOTE_LBRACE_STRING_LITERAL && LA36_0<=LBRACE)||(LA36_0>=QUOTED_IDENTIFIER && LA36_0<=INTEGER_LITERAL)||LA36_0==FLOATING_POINT_LITERAL||LA36_0==IDENTIFIER) ) {
                alt36=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("460:1: expression returns [JCExpression expr] : ( ifExpression | suffixedExpression );", 36, 0, input);

                throw nvae;
            }
            switch (alt36) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:461:11: ifExpression
                    {
                    pushFollow(FOLLOW_ifExpression_in_expression3675);
                    ifExpression48=ifExpression();
                    _fsp--;

                     expr = ifExpression48; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:462:11: suffixedExpression
                    {
                    pushFollow(FOLLOW_suffixedExpression_in_expression3698);
                    suffixedExpression49=suffixedExpression();
                    _fsp--;

                     expr = suffixedExpression49; 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:464:1: ifExpression returns [JCExpression expr] : IF econd= expression THEN ethen= expression ELSE eelse= expression ;
    public final JCExpression ifExpression() throws RecognitionException {
        JCExpression expr = null;

        Token IF50=null;
        JCExpression econd = null;

        JCExpression ethen = null;

        JCExpression eelse = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:465:2: ( IF econd= expression THEN ethen= expression ELSE eelse= expression )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:465:4: IF econd= expression THEN ethen= expression ELSE eelse= expression
            {
            IF50=(Token)input.LT(1);
            match(input,IF,FOLLOW_IF_in_ifExpression3729); 
            pushFollow(FOLLOW_expression_in_ifExpression3733);
            econd=expression();
            _fsp--;

            match(input,THEN,FOLLOW_THEN_in_ifExpression3737); 
            pushFollow(FOLLOW_expression_in_ifExpression3742);
            ethen=expression();
            _fsp--;

            match(input,ELSE,FOLLOW_ELSE_in_ifExpression3750); 
            pushFollow(FOLLOW_expression_in_ifExpression3755);
            eelse=expression();
            _fsp--;

             expr = F.at(pos(IF50)).Conditional(econd, ethen, eelse); 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:468:1: suffixedExpression returns [JCExpression expr] : e1= assignmentExpression ( PLUSPLUS | SUBSUB )? ;
    public final JCExpression suffixedExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:469:2: (e1= assignmentExpression ( PLUSPLUS | SUBSUB )? )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:469:4: e1= assignmentExpression ( PLUSPLUS | SUBSUB )?
            {
            pushFollow(FOLLOW_assignmentExpression_in_suffixedExpression3778);
            e1=assignmentExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:470:5: ( PLUSPLUS | SUBSUB )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==PLUSPLUS||LA37_0==SUBSUB) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:
                    {
                    if ( input.LA(1)==PLUSPLUS||input.LA(1)==SUBSUB ) {
                        input.consume();
                        errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse =
                            new MismatchedSetException(null,input);
                        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_suffixedExpression3789);    throw mse;
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:472:1: assignmentExpression returns [JCExpression expr] : e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? ;
    public final JCExpression assignmentExpression() throws RecognitionException {
        JCExpression expr = null;

        Token EQ51=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:473:2: (e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:473:4: e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )?
            {
            pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression3818);
            e1=assignmentOpExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:474:5: ( EQ e2= assignmentOpExpression )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==EQ) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:474:9: EQ e2= assignmentOpExpression
                    {
                    EQ51=(Token)input.LT(1);
                    match(input,EQ,FOLLOW_EQ_in_assignmentExpression3833); 
                    pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression3839);
                    e2=assignmentOpExpression();
                    _fsp--;

                     expr = F.at(pos(EQ51)).Assign(expr, e2); 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:475:1: assignmentOpExpression returns [JCExpression expr] : e1= andExpression ( assignmentOperator e2= andExpression )? ;
    public final JCExpression assignmentOpExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;

        JCExpression e2 = null;

        int assignmentOperator52 = 0;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:476:2: (e1= andExpression ( assignmentOperator e2= andExpression )? )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:476:4: e1= andExpression ( assignmentOperator e2= andExpression )?
            {
            pushFollow(FOLLOW_andExpression_in_assignmentOpExpression3866);
            e1=andExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:477:5: ( assignmentOperator e2= andExpression )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( ((LA39_0>=PLUSEQ && LA39_0<=PERCENTEQ)) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:477:9: assignmentOperator e2= andExpression
                    {
                    pushFollow(FOLLOW_assignmentOperator_in_assignmentOpExpression3882);
                    assignmentOperator52=assignmentOperator();
                    _fsp--;

                    pushFollow(FOLLOW_andExpression_in_assignmentOpExpression3888);
                    e2=andExpression();
                    _fsp--;

                     expr = F.Assignop(assignmentOperator52,
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:479:1: andExpression returns [JCExpression expr] : e1= orExpression ( AND e2= orExpression )* ;
    public final JCExpression andExpression() throws RecognitionException {
        JCExpression expr = null;

        Token AND53=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:480:2: (e1= orExpression ( AND e2= orExpression )* )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:480:4: e1= orExpression ( AND e2= orExpression )*
            {
            pushFollow(FOLLOW_orExpression_in_andExpression3914);
            e1=orExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:481:5: ( AND e2= orExpression )*
            loop40:
            do {
                int alt40=2;
                int LA40_0 = input.LA(1);

                if ( (LA40_0==AND) ) {
                    alt40=1;
                }


                switch (alt40) {
            	case 1 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:481:9: AND e2= orExpression
            	    {
            	    AND53=(Token)input.LT(1);
            	    match(input,AND,FOLLOW_AND_in_andExpression3930); 
            	    pushFollow(FOLLOW_orExpression_in_andExpression3936);
            	    e2=orExpression();
            	    _fsp--;

            	     expr = F.at(pos(AND53)).Binary(JCTree.AND, expr, e2); 

            	    }
            	    break;

            	default :
            	    break loop40;
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:482:1: orExpression returns [JCExpression expr] : e1= instanceOfExpression ( OR e2= instanceOfExpression )* ;
    public final JCExpression orExpression() throws RecognitionException {
        JCExpression expr = null;

        Token OR54=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:483:2: (e1= instanceOfExpression ( OR e2= instanceOfExpression )* )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:483:4: e1= instanceOfExpression ( OR e2= instanceOfExpression )*
            {
            pushFollow(FOLLOW_instanceOfExpression_in_orExpression3964);
            e1=instanceOfExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:484:5: ( OR e2= instanceOfExpression )*
            loop41:
            do {
                int alt41=2;
                int LA41_0 = input.LA(1);

                if ( (LA41_0==OR) ) {
                    alt41=1;
                }


                switch (alt41) {
            	case 1 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:484:9: OR e2= instanceOfExpression
            	    {
            	    OR54=(Token)input.LT(1);
            	    match(input,OR,FOLLOW_OR_in_orExpression3979); 
            	    pushFollow(FOLLOW_instanceOfExpression_in_orExpression3985);
            	    e2=instanceOfExpression();
            	    _fsp--;

            	     expr = F.at(pos(OR54)).Binary(JCTree.OR, expr, e2); 

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
    // $ANTLR end orExpression


    // $ANTLR start instanceOfExpression
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:485:1: instanceOfExpression returns [JCExpression expr] : e1= relationalExpression ( INSTANCEOF identifier )? ;
    public final JCExpression instanceOfExpression() throws RecognitionException {
        JCExpression expr = null;

        Token INSTANCEOF55=null;
        JCExpression e1 = null;

        JCIdent identifier56 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:486:2: (e1= relationalExpression ( INSTANCEOF identifier )? )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:486:4: e1= relationalExpression ( INSTANCEOF identifier )?
            {
            pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression4013);
            e1=relationalExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:487:5: ( INSTANCEOF identifier )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==INSTANCEOF) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:487:9: INSTANCEOF identifier
                    {
                    INSTANCEOF55=(Token)input.LT(1);
                    match(input,INSTANCEOF,FOLLOW_INSTANCEOF_in_instanceOfExpression4028); 
                    pushFollow(FOLLOW_identifier_in_instanceOfExpression4030);
                    identifier56=identifier();
                    _fsp--;

                     expr = F.at(pos(INSTANCEOF55)).Binary(JCTree.TYPETEST, expr, 
                    	   													 identifier56); 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:489:1: relationalExpression returns [JCExpression expr] : e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* ;
    public final JCExpression relationalExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LTGT57=null;
        Token EQEQ58=null;
        Token LTEQ59=null;
        Token GTEQ60=null;
        Token LT61=null;
        Token GT62=null;
        Token IN63=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:490:2: (e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:490:4: e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            {
            pushFollow(FOLLOW_additiveExpression_in_relationalExpression4058);
            e1=additiveExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:491:5: ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            loop43:
            do {
                int alt43=8;
                switch ( input.LA(1) ) {
                case LTGT:
                    {
                    alt43=1;
                    }
                    break;
                case EQEQ:
                    {
                    alt43=2;
                    }
                    break;
                case LTEQ:
                    {
                    alt43=3;
                    }
                    break;
                case GTEQ:
                    {
                    alt43=4;
                    }
                    break;
                case LT:
                    {
                    alt43=5;
                    }
                    break;
                case GT:
                    {
                    alt43=6;
                    }
                    break;
                case IN:
                    {
                    alt43=7;
                    }
                    break;

                }

                switch (alt43) {
            	case 1 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:491:9: LTGT e= additiveExpression
            	    {
            	    LTGT57=(Token)input.LT(1);
            	    match(input,LTGT,FOLLOW_LTGT_in_relationalExpression4074); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4080);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LTGT57)).Binary(JCTree.NE, expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:492:9: EQEQ e= additiveExpression
            	    {
            	    EQEQ58=(Token)input.LT(1);
            	    match(input,EQEQ,FOLLOW_EQEQ_in_relationalExpression4094); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4100);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(EQEQ58)).Binary(JCTree.EQ, expr, e); 

            	    }
            	    break;
            	case 3 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:493:9: LTEQ e= additiveExpression
            	    {
            	    LTEQ59=(Token)input.LT(1);
            	    match(input,LTEQ,FOLLOW_LTEQ_in_relationalExpression4114); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4120);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LTEQ59)).Binary(JCTree.LE, expr, e); 

            	    }
            	    break;
            	case 4 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:494:9: GTEQ e= additiveExpression
            	    {
            	    GTEQ60=(Token)input.LT(1);
            	    match(input,GTEQ,FOLLOW_GTEQ_in_relationalExpression4134); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4140);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(GTEQ60)).Binary(JCTree.GE, expr, e); 

            	    }
            	    break;
            	case 5 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:495:9: LT e= additiveExpression
            	    {
            	    LT61=(Token)input.LT(1);
            	    match(input,LT,FOLLOW_LT_in_relationalExpression4154); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4162);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(LT61))  .Binary(JCTree.LT, expr, e); 

            	    }
            	    break;
            	case 6 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:496:9: GT e= additiveExpression
            	    {
            	    GT62=(Token)input.LT(1);
            	    match(input,GT,FOLLOW_GT_in_relationalExpression4176); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4184);
            	    e=additiveExpression();
            	    _fsp--;

            	     expr = F.at(pos(GT62))  .Binary(JCTree.GT, expr, e); 

            	    }
            	    break;
            	case 7 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:497:9: IN e= additiveExpression
            	    {
            	    IN63=(Token)input.LT(1);
            	    match(input,IN,FOLLOW_IN_in_relationalExpression4198); 
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression4206);
            	    e=additiveExpression();
            	    _fsp--;

            	     /* expr = F.at(pos(IN63  )).Binary(JavaFXTag.IN, expr, $e2.expr); */ 

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
    // $ANTLR end relationalExpression


    // $ANTLR start additiveExpression
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:499:1: additiveExpression returns [JCExpression expr] : e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* ;
    public final JCExpression additiveExpression() throws RecognitionException {
        JCExpression expr = null;

        Token PLUS64=null;
        Token SUB65=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:500:2: (e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:500:4: e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            {
            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4235);
            e1=multiplicativeExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:501:5: ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            loop44:
            do {
                int alt44=3;
                int LA44_0 = input.LA(1);

                if ( (LA44_0==PLUS) ) {
                    alt44=1;
                }
                else if ( (LA44_0==SUB) ) {
                    alt44=2;
                }


                switch (alt44) {
            	case 1 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:501:9: PLUS e= multiplicativeExpression
            	    {
            	    PLUS64=(Token)input.LT(1);
            	    match(input,PLUS,FOLLOW_PLUS_in_additiveExpression4250); 
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4256);
            	    e=multiplicativeExpression();
            	    _fsp--;

            	     expr = F.at(pos(PLUS64)).Binary(JCTree.PLUS , expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:502:9: SUB e= multiplicativeExpression
            	    {
            	    SUB65=(Token)input.LT(1);
            	    match(input,SUB,FOLLOW_SUB_in_additiveExpression4269); 
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4276);
            	    e=multiplicativeExpression();
            	    _fsp--;

            	     expr = F.at(pos(SUB65)) .Binary(JCTree.MINUS, expr, e); 

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
    // $ANTLR end additiveExpression


    // $ANTLR start multiplicativeExpression
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:504:1: multiplicativeExpression returns [JCExpression expr] : e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* ;
    public final JCExpression multiplicativeExpression() throws RecognitionException {
        JCExpression expr = null;

        Token STAR66=null;
        Token SLASH67=null;
        Token PERCENT68=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:505:2: (e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:505:4: e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            {
            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4304);
            e1=unaryExpression();
            _fsp--;

             expr = e1; 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:506:5: ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            loop45:
            do {
                int alt45=4;
                switch ( input.LA(1) ) {
                case STAR:
                    {
                    alt45=1;
                    }
                    break;
                case SLASH:
                    {
                    alt45=2;
                    }
                    break;
                case PERCENT:
                    {
                    alt45=3;
                    }
                    break;

                }

                switch (alt45) {
            	case 1 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:506:9: STAR e= unaryExpression
            	    {
            	    STAR66=(Token)input.LT(1);
            	    match(input,STAR,FOLLOW_STAR_in_multiplicativeExpression4320); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4327);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(STAR66))   .Binary(JCTree.MUL  , expr, e); 

            	    }
            	    break;
            	case 2 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:507:9: SLASH e= unaryExpression
            	    {
            	    SLASH67=(Token)input.LT(1);
            	    match(input,SLASH,FOLLOW_SLASH_in_multiplicativeExpression4341); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4347);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(SLASH67))  .Binary(JCTree.DIV  , expr, e); 

            	    }
            	    break;
            	case 3 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:508:9: PERCENT e= unaryExpression
            	    {
            	    PERCENT68=(Token)input.LT(1);
            	    match(input,PERCENT,FOLLOW_PERCENT_in_multiplicativeExpression4361); 
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4365);
            	    e=unaryExpression();
            	    _fsp--;

            	     expr = F.at(pos(PERCENT68)).Binary(JCTree.MOD  , expr, e); 

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
    // $ANTLR end multiplicativeExpression


    // $ANTLR start unaryExpression
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:510:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );
    public final JCExpression unaryExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression postfixExpression69 = null;

        int unaryOperator70 = 0;

        JCExpression postfixExpression71 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:511:2: ( postfixExpression | unaryOperator postfixExpression )
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( ((LA46_0>=THIS && LA46_0<=FALSE)||LA46_0==NEW||LA46_0==SUPER||LA46_0==LPAREN||LA46_0==STRING_LITERAL||(LA46_0>=QUOTE_LBRACE_STRING_LITERAL && LA46_0<=LBRACE)||(LA46_0>=QUOTED_IDENTIFIER && LA46_0<=INTEGER_LITERAL)||LA46_0==FLOATING_POINT_LITERAL||LA46_0==IDENTIFIER) ) {
                alt46=1;
            }
            else if ( ((LA46_0>=POUND && LA46_0<=TYPEOF)||LA46_0==NOT||(LA46_0>=SIZEOF && LA46_0<=REVERSE)||(LA46_0>=PLUSPLUS && LA46_0<=SUBSUB)||LA46_0==QUES) ) {
                alt46=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("510:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );", 46, 0, input);

                throw nvae;
            }
            switch (alt46) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:511:4: postfixExpression
                    {
                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression4395);
                    postfixExpression69=postfixExpression();
                    _fsp--;

                     expr = postfixExpression69; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:512:4: unaryOperator postfixExpression
                    {
                    pushFollow(FOLLOW_unaryOperator_in_unaryExpression4406);
                    unaryOperator70=unaryOperator();
                    _fsp--;

                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression4410);
                    postfixExpression71=postfixExpression();
                    _fsp--;

                     expr = F.Unary(unaryOperator70, postfixExpression71); 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:514:1: postfixExpression returns [JCExpression expr] : primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* ;
    public final JCExpression postfixExpression() throws RecognitionException {
        JCExpression expr = null;

        Token DOT73=null;
        Token LPAREN74=null;
        name_return name1 = null;

        JCExpression primaryExpression72 = null;

        ListBuffer<JCExpression> expressionListOpt75 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:515:2: ( primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:515:4: primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            {
            pushFollow(FOLLOW_primaryExpression_in_postfixExpression4430);
            primaryExpression72=primaryExpression();
            _fsp--;

             expr = primaryExpression72; 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:516:5: ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            loop50:
            do {
                int alt50=3;
                int LA50_0 = input.LA(1);

                if ( (LA50_0==DOT) ) {
                    alt50=1;
                }
                else if ( (LA50_0==LBRACKET) ) {
                    alt50=2;
                }


                switch (alt50) {
            	case 1 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:516:7: DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    {
            	    DOT73=(Token)input.LT(1);
            	    match(input,DOT,FOLLOW_DOT_in_postfixExpression4445); 
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:516:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    int alt48=2;
            	    int LA48_0 = input.LA(1);

            	    if ( (LA48_0==CLASS) ) {
            	        alt48=1;
            	    }
            	    else if ( (LA48_0==QUOTED_IDENTIFIER||LA48_0==IDENTIFIER) ) {
            	        alt48=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("516:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )", 48, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt48) {
            	        case 1 :
            	            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:516:13: CLASS
            	            {
            	            match(input,CLASS,FOLLOW_CLASS_in_postfixExpression4449); 

            	            }
            	            break;
            	        case 2 :
            	            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:517:13: name1= name ( LPAREN expressionListOpt RPAREN )*
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression4473);
            	            name1=name();
            	            _fsp--;

            	             expr = F.at(pos(DOT73)).Select(expr, name1.value); 
            	            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:518:14: ( LPAREN expressionListOpt RPAREN )*
            	            loop47:
            	            do {
            	                int alt47=2;
            	                int LA47_0 = input.LA(1);

            	                if ( (LA47_0==LPAREN) ) {
            	                    alt47=1;
            	                }


            	                switch (alt47) {
            	            	case 1 :
            	            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:518:16: LPAREN expressionListOpt RPAREN
            	            	    {
            	            	    LPAREN74=(Token)input.LT(1);
            	            	    match(input,LPAREN,FOLLOW_LPAREN_in_postfixExpression4498); 
            	            	    pushFollow(FOLLOW_expressionListOpt_in_postfixExpression4500);
            	            	    expressionListOpt75=expressionListOpt();
            	            	    _fsp--;

            	            	    match(input,RPAREN,FOLLOW_RPAREN_in_postfixExpression4502); 
            	            	     expr = F.at(pos(LPAREN74)).Apply(null, expr, expressionListOpt75.toList()); 

            	            	    }
            	            	    break;

            	            	default :
            	            	    break loop47;
            	                }
            	            } while (true);


            	            }
            	            break;

            	    }


            	    }
            	    break;
            	case 2 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:520:7: LBRACKET ( name BAR )? expression RBRACKET
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_postfixExpression4534); 
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:520:16: ( name BAR )?
            	    int alt49=2;
            	    int LA49_0 = input.LA(1);

            	    if ( (LA49_0==QUOTED_IDENTIFIER||LA49_0==IDENTIFIER) ) {
            	        int LA49_1 = input.LA(2);

            	        if ( (LA49_1==BAR) ) {
            	            alt49=1;
            	        }
            	    }
            	    switch (alt49) {
            	        case 1 :
            	            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:520:17: name BAR
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression4537);
            	            name();
            	            _fsp--;

            	            match(input,BAR,FOLLOW_BAR_in_postfixExpression4539); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_expression_in_postfixExpression4543);
            	    expression();
            	    _fsp--;

            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_postfixExpression4546); 

            	    }
            	    break;

            	default :
            	    break loop50;
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:522:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | blockExpression | LPAREN expression RPAREN );
    public final JCExpression primaryExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LBRACE77=null;
        Token THIS80=null;
        Token SUPER81=null;
        Token LPAREN83=null;
        Token LPAREN88=null;
        JCExpression newExpression76 = null;

        JCIdent identifier78 = null;

        ListBuffer<JFXStatement> objectLiteral79 = null;

        JCIdent identifier82 = null;

        ListBuffer<JCExpression> expressionListOpt84 = null;

        JCExpression stringExpression85 = null;

        JCExpression literal86 = null;

        JFXBlockExpression blockExpression87 = null;

        JCExpression expression89 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:523:2: ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | blockExpression | LPAREN expression RPAREN )
            int alt52=9;
            switch ( input.LA(1) ) {
            case NEW:
                {
                alt52=1;
                }
                break;
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                int LA52_2 = input.LA(2);

                if ( (LA52_2==LBRACE) ) {
                    alt52=2;
                }
                else if ( (LA52_2==EOF||LA52_2==ABSTRACT||LA52_2==AND||LA52_2==ATTRIBUTE||LA52_2==BREAK||LA52_2==CLASS||(LA52_2>=RETURN && LA52_2<=VAR)||LA52_2==IMPORT||(LA52_2>=THEN && LA52_2<=ELSE)||LA52_2==IN||(LA52_2>=WHILE && LA52_2<=CONTINUE)||LA52_2==TRY||(LA52_2>=PRIVATE && LA52_2<=READONLY)||LA52_2==INSTANCEOF||LA52_2==OR||(LA52_2>=LPAREN && LA52_2<=PERCENTEQ)||(LA52_2>=RBRACE_QUOTE_STRING_LITERAL && LA52_2<=RBRACE)||LA52_2==QUOTED_IDENTIFIER||LA52_2==IDENTIFIER) ) {
                    alt52=5;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("522:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | blockExpression | LPAREN expression RPAREN );", 52, 2, input);

                    throw nvae;
                }
                }
                break;
            case THIS:
                {
                alt52=3;
                }
                break;
            case SUPER:
                {
                alt52=4;
                }
                break;
            case QUOTE_LBRACE_STRING_LITERAL:
                {
                alt52=6;
                }
                break;
            case NULL:
            case TRUE:
            case FALSE:
            case STRING_LITERAL:
            case INTEGER_LITERAL:
            case FLOATING_POINT_LITERAL:
                {
                alt52=7;
                }
                break;
            case LBRACE:
                {
                alt52=8;
                }
                break;
            case LPAREN:
                {
                alt52=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("522:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | blockExpression | LPAREN expression RPAREN );", 52, 0, input);

                throw nvae;
            }

            switch (alt52) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:523:4: newExpression
                    {
                    pushFollow(FOLLOW_newExpression_in_primaryExpression4571);
                    newExpression76=newExpression();
                    _fsp--;

                     expr = newExpression76; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:524:4: identifier LBRACE objectLiteral RBRACE
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression4583);
                    identifier78=identifier();
                    _fsp--;

                    LBRACE77=(Token)input.LT(1);
                    match(input,LBRACE,FOLLOW_LBRACE_in_primaryExpression4585); 
                    pushFollow(FOLLOW_objectLiteral_in_primaryExpression4588);
                    objectLiteral79=objectLiteral();
                    _fsp--;

                    match(input,RBRACE,FOLLOW_RBRACE_in_primaryExpression4590); 
                     expr = F.at(pos(LBRACE77)).PureObjectLiteral(identifier78, objectLiteral79.toList()); 

                    }
                    break;
                case 3 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:525:11: THIS
                    {
                    THIS80=(Token)input.LT(1);
                    match(input,THIS,FOLLOW_THIS_in_primaryExpression4607); 
                     expr = F.at(pos(THIS80)).Identifier(names._this); 

                    }
                    break;
                case 4 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:526:11: SUPER
                    {
                    SUPER81=(Token)input.LT(1);
                    match(input,SUPER,FOLLOW_SUPER_in_primaryExpression4627); 
                     expr = F.at(pos(SUPER81)).Identifier(names._super); 

                    }
                    break;
                case 5 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:527:11: identifier ( LPAREN expressionListOpt RPAREN )*
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression4647);
                    identifier82=identifier();
                    _fsp--;

                     expr = identifier82; 
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:528:10: ( LPAREN expressionListOpt RPAREN )*
                    loop51:
                    do {
                        int alt51=2;
                        int LA51_0 = input.LA(1);

                        if ( (LA51_0==LPAREN) ) {
                            alt51=1;
                        }


                        switch (alt51) {
                    	case 1 :
                    	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:528:12: LPAREN expressionListOpt RPAREN
                    	    {
                    	    LPAREN83=(Token)input.LT(1);
                    	    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression4668); 
                    	    pushFollow(FOLLOW_expressionListOpt_in_primaryExpression4672);
                    	    expressionListOpt84=expressionListOpt();
                    	    _fsp--;

                    	    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression4676); 
                    	     expr = F.at(pos(LPAREN83)).Apply(null, expr, expressionListOpt84.toList()); 

                    	    }
                    	    break;

                    	default :
                    	    break loop51;
                        }
                    } while (true);


                    }
                    break;
                case 6 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:529:11: stringExpression
                    {
                    pushFollow(FOLLOW_stringExpression_in_primaryExpression4696);
                    stringExpression85=stringExpression();
                    _fsp--;

                     expr = stringExpression85; 

                    }
                    break;
                case 7 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:530:11: literal
                    {
                    pushFollow(FOLLOW_literal_in_primaryExpression4715);
                    literal86=literal();
                    _fsp--;

                     expr = literal86; 

                    }
                    break;
                case 8 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:531:11: blockExpression
                    {
                    pushFollow(FOLLOW_blockExpression_in_primaryExpression4735);
                    blockExpression87=blockExpression();
                    _fsp--;

                     expr = blockExpression87; 

                    }
                    break;
                case 9 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:532:11: LPAREN expression RPAREN
                    {
                    LPAREN88=(Token)input.LT(1);
                    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression4753); 
                    pushFollow(FOLLOW_expression_in_primaryExpression4755);
                    expression89=expression();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression4757); 
                     expr = F.at(pos(LPAREN88)).Parens(expression89); 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:534:1: newExpression returns [JCExpression expr] : NEW identifier ( LPAREN expressionListOpt RPAREN )? ;
    public final JCExpression newExpression() throws RecognitionException {
        JCExpression expr = null;

        Token NEW91=null;
        ListBuffer<JCExpression> expressionListOpt90 = null;

        JCIdent identifier92 = null;


         ListBuffer<JCExpression> args = null; 
        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:536:2: ( NEW identifier ( LPAREN expressionListOpt RPAREN )? )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:536:4: NEW identifier ( LPAREN expressionListOpt RPAREN )?
            {
            NEW91=(Token)input.LT(1);
            match(input,NEW,FOLLOW_NEW_in_newExpression4790); 
            pushFollow(FOLLOW_identifier_in_newExpression4793);
            identifier92=identifier();
            _fsp--;

            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:537:3: ( LPAREN expressionListOpt RPAREN )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==LPAREN) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:537:5: LPAREN expressionListOpt RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_newExpression4801); 
                    pushFollow(FOLLOW_expressionListOpt_in_newExpression4805);
                    expressionListOpt90=expressionListOpt();
                    _fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_newExpression4809); 
                     args = expressionListOpt90; 

                    }
                    break;

            }

             expr = F.at(pos(NEW91)).NewClass(null, null, identifier92, 
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:542:1: objectLiteral returns [ListBuffer<JFXStatement> parts = new ListBuffer<JFXStatement>()] : ( objectLiteralPart )* ;
    public final ListBuffer<JFXStatement> objectLiteral() throws RecognitionException {
        ListBuffer<JFXStatement> parts =  new ListBuffer<JFXStatement>();

        JFXStatement objectLiteralPart93 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:543:2: ( ( objectLiteralPart )* )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:543:4: ( objectLiteralPart )*
            {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:543:4: ( objectLiteralPart )*
            loop54:
            do {
                int alt54=2;
                int LA54_0 = input.LA(1);

                if ( (LA54_0==ABSTRACT||LA54_0==ATTRIBUTE||(LA54_0>=PRIVATE && LA54_0<=READONLY)||LA54_0==QUOTED_IDENTIFIER||LA54_0==IDENTIFIER) ) {
                    alt54=1;
                }


                switch (alt54) {
            	case 1 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:543:6: objectLiteralPart
            	    {
            	    pushFollow(FOLLOW_objectLiteralPart_in_objectLiteral4849);
            	    objectLiteralPart93=objectLiteralPart();
            	    _fsp--;

            	     parts.append(objectLiteralPart93); 

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
        return parts;
    }
    // $ANTLR end objectLiteral


    // $ANTLR start objectLiteralPart
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );
    public final JFXStatement objectLiteralPart() throws RecognitionException {
        JFXStatement value = null;

        Token COLON94=null;
        name_return name95 = null;

        JCExpression expression96 = null;

        JavafxBindStatus bindOpt97 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:545:2: ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition )
            int alt56=3;
            switch ( input.LA(1) ) {
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                alt56=1;
                }
                break;
            case ABSTRACT:
                {
                switch ( input.LA(2) ) {
                case PUBLIC:
                    {
                    int LA56_9 = input.LA(3);

                    if ( (LA56_9==ATTRIBUTE) ) {
                        alt56=2;
                    }
                    else if ( (LA56_9==FUNCTION) ) {
                        alt56=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 9, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA56_10 = input.LA(3);

                    if ( (LA56_10==FUNCTION) ) {
                        alt56=3;
                    }
                    else if ( (LA56_10==ATTRIBUTE) ) {
                        alt56=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA56_11 = input.LA(3);

                    if ( (LA56_11==ATTRIBUTE) ) {
                        alt56=2;
                    }
                    else if ( (LA56_11==FUNCTION) ) {
                        alt56=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case FUNCTION:
                    {
                    alt56=3;
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt56=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 2, input);

                    throw nvae;
                }

                }
                break;
            case READONLY:
                {
                switch ( input.LA(2) ) {
                case PUBLIC:
                    {
                    int LA56_9 = input.LA(3);

                    if ( (LA56_9==ATTRIBUTE) ) {
                        alt56=2;
                    }
                    else if ( (LA56_9==FUNCTION) ) {
                        alt56=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 9, input);

                        throw nvae;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA56_10 = input.LA(3);

                    if ( (LA56_10==FUNCTION) ) {
                        alt56=3;
                    }
                    else if ( (LA56_10==ATTRIBUTE) ) {
                        alt56=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 10, input);

                        throw nvae;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA56_11 = input.LA(3);

                    if ( (LA56_11==ATTRIBUTE) ) {
                        alt56=2;
                    }
                    else if ( (LA56_11==FUNCTION) ) {
                        alt56=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 11, input);

                        throw nvae;
                    }
                    }
                    break;
                case FUNCTION:
                    {
                    alt56=3;
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt56=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 3, input);

                    throw nvae;
                }

                }
                break;
            case PUBLIC:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA56_12 = input.LA(3);

                    if ( (LA56_12==FUNCTION) ) {
                        alt56=3;
                    }
                    else if ( (LA56_12==ATTRIBUTE) ) {
                        alt56=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA56_13 = input.LA(3);

                    if ( (LA56_13==FUNCTION) ) {
                        alt56=3;
                    }
                    else if ( (LA56_13==ATTRIBUTE) ) {
                        alt56=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt56=2;
                    }
                    break;
                case FUNCTION:
                    {
                    alt56=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 4, input);

                    throw nvae;
                }

                }
                break;
            case PRIVATE:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA56_12 = input.LA(3);

                    if ( (LA56_12==FUNCTION) ) {
                        alt56=3;
                    }
                    else if ( (LA56_12==ATTRIBUTE) ) {
                        alt56=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA56_13 = input.LA(3);

                    if ( (LA56_13==FUNCTION) ) {
                        alt56=3;
                    }
                    else if ( (LA56_13==ATTRIBUTE) ) {
                        alt56=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case FUNCTION:
                    {
                    alt56=3;
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt56=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 5, input);

                    throw nvae;
                }

                }
                break;
            case PROTECTED:
                {
                switch ( input.LA(2) ) {
                case ABSTRACT:
                    {
                    int LA56_12 = input.LA(3);

                    if ( (LA56_12==FUNCTION) ) {
                        alt56=3;
                    }
                    else if ( (LA56_12==ATTRIBUTE) ) {
                        alt56=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 12, input);

                        throw nvae;
                    }
                    }
                    break;
                case READONLY:
                    {
                    int LA56_13 = input.LA(3);

                    if ( (LA56_13==FUNCTION) ) {
                        alt56=3;
                    }
                    else if ( (LA56_13==ATTRIBUTE) ) {
                        alt56=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 13, input);

                        throw nvae;
                    }
                    }
                    break;
                case FUNCTION:
                    {
                    alt56=3;
                    }
                    break;
                case ATTRIBUTE:
                    {
                    alt56=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 6, input);

                    throw nvae;
                }

                }
                break;
            case ATTRIBUTE:
                {
                alt56=2;
                }
                break;
            case FUNCTION:
                {
                alt56=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("544:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | attributeDefinition | functionDefinition );", 56, 0, input);

                throw nvae;
            }

            switch (alt56) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:545:4: name COLON bindOpt expression ( COMMA | SEMI )?
                    {
                    pushFollow(FOLLOW_name_in_objectLiteralPart4875);
                    name95=name();
                    _fsp--;

                    COLON94=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_objectLiteralPart4877); 
                    pushFollow(FOLLOW_bindOpt_in_objectLiteralPart4880);
                    bindOpt97=bindOpt();
                    _fsp--;

                    pushFollow(FOLLOW_expression_in_objectLiteralPart4882);
                    expression96=expression();
                    _fsp--;

                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:545:35: ( COMMA | SEMI )?
                    int alt55=2;
                    int LA55_0 = input.LA(1);

                    if ( ((LA55_0>=SEMI && LA55_0<=COMMA)) ) {
                        alt55=1;
                    }
                    switch (alt55) {
                        case 1 :
                            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:
                            {
                            if ( (input.LA(1)>=SEMI && input.LA(1)<=COMMA) ) {
                                input.consume();
                                errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse =
                                    new MismatchedSetException(null,input);
                                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_objectLiteralPart4884);    throw mse;
                            }


                            }
                            break;

                    }

                     value = F.at(pos(COLON94)).ObjectLiteralPart(name95.value, expression96, bindOpt97); 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:546:10: attributeDefinition
                    {
                    pushFollow(FOLLOW_attributeDefinition_in_objectLiteralPart4904);
                    attributeDefinition();
                    _fsp--;


                    }
                    break;
                case 3 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:547:10: functionDefinition
                    {
                    pushFollow(FOLLOW_functionDefinition_in_objectLiteralPart4915);
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:548:1: stringExpression returns [JCExpression expr] : ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL ;
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
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:550:2: (ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:550:4: ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL
            {
            ql=(Token)input.LT(1);
            match(input,QUOTE_LBRACE_STRING_LITERAL,FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression4937); 
             strexp.append(F.at(pos(ql)).Literal(TypeTags.CLASS, ql.getText())); 
            pushFollow(FOLLOW_formatOrNull_in_stringExpression4946);
            f1=formatOrNull();
            _fsp--;

             strexp.append(f1); 
            pushFollow(FOLLOW_expression_in_stringExpression4957);
            e1=expression();
            _fsp--;

             strexp.append(e1); 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:553:4: (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )*
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( (LA57_0==RBRACE_LBRACE_STRING_LITERAL) ) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:553:7: rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression
            	    {
            	    rl=(Token)input.LT(1);
            	    match(input,RBRACE_LBRACE_STRING_LITERAL,FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression4972); 
            	     strexp.append(F.at(pos(rl)).Literal(TypeTags.CLASS, rl.getText())); 
            	    pushFollow(FOLLOW_formatOrNull_in_stringExpression4984);
            	    fn=formatOrNull();
            	    _fsp--;

            	     strexp.append(fn); 
            	    pushFollow(FOLLOW_expression_in_stringExpression4998);
            	    en=expression();
            	    _fsp--;

            	     strexp.append(en); 

            	    }
            	    break;

            	default :
            	    break loop57;
                }
            } while (true);

            rq=(Token)input.LT(1);
            match(input,RBRACE_QUOTE_STRING_LITERAL,FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression5019); 
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:560:1: formatOrNull returns [JCExpression expr] : (fs= FORMAT_STRING_LITERAL | );
    public final JCExpression formatOrNull() throws RecognitionException {
        JCExpression expr = null;

        Token fs=null;

        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:561:2: (fs= FORMAT_STRING_LITERAL | )
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==FORMAT_STRING_LITERAL) ) {
                alt58=1;
            }
            else if ( ((LA58_0>=POUND && LA58_0<=TYPEOF)||LA58_0==IF||(LA58_0>=THIS && LA58_0<=FALSE)||(LA58_0>=NOT && LA58_0<=NEW)||LA58_0==SUPER||(LA58_0>=SIZEOF && LA58_0<=LPAREN)||(LA58_0>=PLUSPLUS && LA58_0<=SUBSUB)||(LA58_0>=QUES && LA58_0<=STRING_LITERAL)||(LA58_0>=QUOTE_LBRACE_STRING_LITERAL && LA58_0<=LBRACE)||(LA58_0>=QUOTED_IDENTIFIER && LA58_0<=INTEGER_LITERAL)||LA58_0==FLOATING_POINT_LITERAL||LA58_0==IDENTIFIER) ) {
                alt58=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("560:1: formatOrNull returns [JCExpression expr] : (fs= FORMAT_STRING_LITERAL | );", 58, 0, input);

                throw nvae;
            }
            switch (alt58) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:561:4: fs= FORMAT_STRING_LITERAL
                    {
                    fs=(Token)input.LT(1);
                    match(input,FORMAT_STRING_LITERAL,FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull5049); 
                     expr = F.at(pos(fs)).Literal(TypeTags.CLASS, fs.getText()); 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:562:22: 
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:564:1: expressionListOpt returns [ListBuffer<JCExpression> args = new ListBuffer<JCExpression>()] : (e1= expression ( COMMA e= expression )* )? ;
    public final ListBuffer<JCExpression> expressionListOpt() throws RecognitionException {
        ListBuffer<JCExpression> args =  new ListBuffer<JCExpression>();

        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:565:2: ( (e1= expression ( COMMA e= expression )* )? )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:565:4: (e1= expression ( COMMA e= expression )* )?
            {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:565:4: (e1= expression ( COMMA e= expression )* )?
            int alt60=2;
            int LA60_0 = input.LA(1);

            if ( ((LA60_0>=POUND && LA60_0<=TYPEOF)||LA60_0==IF||(LA60_0>=THIS && LA60_0<=FALSE)||(LA60_0>=NOT && LA60_0<=NEW)||LA60_0==SUPER||(LA60_0>=SIZEOF && LA60_0<=LPAREN)||(LA60_0>=PLUSPLUS && LA60_0<=SUBSUB)||(LA60_0>=QUES && LA60_0<=STRING_LITERAL)||(LA60_0>=QUOTE_LBRACE_STRING_LITERAL && LA60_0<=LBRACE)||(LA60_0>=QUOTED_IDENTIFIER && LA60_0<=INTEGER_LITERAL)||LA60_0==FLOATING_POINT_LITERAL||LA60_0==IDENTIFIER) ) {
                alt60=1;
            }
            switch (alt60) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:565:6: e1= expression ( COMMA e= expression )*
                    {
                    pushFollow(FOLLOW_expression_in_expressionListOpt5081);
                    e1=expression();
                    _fsp--;

                     args.append(e1); 
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:566:6: ( COMMA e= expression )*
                    loop59:
                    do {
                        int alt59=2;
                        int LA59_0 = input.LA(1);

                        if ( (LA59_0==COMMA) ) {
                            alt59=1;
                        }


                        switch (alt59) {
                    	case 1 :
                    	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:566:7: COMMA e= expression
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_expressionListOpt5092); 
                    	    pushFollow(FOLLOW_expression_in_expressionListOpt5098);
                    	    e=expression();
                    	    _fsp--;

                    	     args.append(e); 

                    	    }
                    	    break;

                    	default :
                    	    break loop59;
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:567:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );
    public final int unaryOperator() throws RecognitionException {
        int optag = 0;

        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:568:2: ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB )
            int alt61=9;
            switch ( input.LA(1) ) {
            case POUND:
                {
                alt61=1;
                }
                break;
            case QUES:
                {
                alt61=2;
                }
                break;
            case SUB:
                {
                alt61=3;
                }
                break;
            case NOT:
                {
                alt61=4;
                }
                break;
            case SIZEOF:
                {
                alt61=5;
                }
                break;
            case TYPEOF:
                {
                alt61=6;
                }
                break;
            case REVERSE:
                {
                alt61=7;
                }
                break;
            case PLUSPLUS:
                {
                alt61=8;
                }
                break;
            case SUBSUB:
                {
                alt61=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("567:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );", 61, 0, input);

                throw nvae;
            }

            switch (alt61) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:568:4: POUND
                    {
                    match(input,POUND,FOLLOW_POUND_in_unaryOperator5121); 
                     optag = 0; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:569:4: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_unaryOperator5132); 
                     optag = 0; 

                    }
                    break;
                case 3 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:570:4: SUB
                    {
                    match(input,SUB,FOLLOW_SUB_in_unaryOperator5145); 
                     optag = JCTree.NEG; 

                    }
                    break;
                case 4 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:571:4: NOT
                    {
                    match(input,NOT,FOLLOW_NOT_in_unaryOperator5158); 
                     optag = JCTree.NOT; 

                    }
                    break;
                case 5 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:572:4: SIZEOF
                    {
                    match(input,SIZEOF,FOLLOW_SIZEOF_in_unaryOperator5171); 
                     optag = 0; 

                    }
                    break;
                case 6 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:573:4: TYPEOF
                    {
                    match(input,TYPEOF,FOLLOW_TYPEOF_in_unaryOperator5184); 
                     optag = 0; 

                    }
                    break;
                case 7 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:574:4: REVERSE
                    {
                    match(input,REVERSE,FOLLOW_REVERSE_in_unaryOperator5197); 
                     optag = 0; 

                    }
                    break;
                case 8 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:575:4: PLUSPLUS
                    {
                    match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_unaryOperator5210); 
                     optag = 0; 

                    }
                    break;
                case 9 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:576:4: SUBSUB
                    {
                    match(input,SUBSUB,FOLLOW_SUBSUB_in_unaryOperator5223); 
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:578:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );
    public final int assignmentOperator() throws RecognitionException {
        int optag = 0;

        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:579:2: ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ )
            int alt62=5;
            switch ( input.LA(1) ) {
            case PLUSEQ:
                {
                alt62=1;
                }
                break;
            case SUBEQ:
                {
                alt62=2;
                }
                break;
            case STAREQ:
                {
                alt62=3;
                }
                break;
            case SLASHEQ:
                {
                alt62=4;
                }
                break;
            case PERCENTEQ:
                {
                alt62=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("578:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );", 62, 0, input);

                throw nvae;
            }

            switch (alt62) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:579:4: PLUSEQ
                    {
                    match(input,PLUSEQ,FOLLOW_PLUSEQ_in_assignmentOperator5244); 
                     optag = JCTree.PLUS_ASG; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:580:4: SUBEQ
                    {
                    match(input,SUBEQ,FOLLOW_SUBEQ_in_assignmentOperator5257); 
                     optag = JCTree.MINUS_ASG; 

                    }
                    break;
                case 3 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:581:4: STAREQ
                    {
                    match(input,STAREQ,FOLLOW_STAREQ_in_assignmentOperator5270); 
                     optag = JCTree.MUL_ASG; 

                    }
                    break;
                case 4 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:582:4: SLASHEQ
                    {
                    match(input,SLASHEQ,FOLLOW_SLASHEQ_in_assignmentOperator5283); 
                     optag = JCTree.DIV_ASG; 

                    }
                    break;
                case 5 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:583:4: PERCENTEQ
                    {
                    match(input,PERCENTEQ,FOLLOW_PERCENTEQ_in_assignmentOperator5296); 
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:585:1: typeReference returns [JFXType type] : ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? ;
    public final JFXType typeReference() throws RecognitionException {
        JFXType type = null;

        Token STAR99=null;
        int ccn = 0;

        int ccs = 0;

        name_return name98 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:586:2: ( ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:586:4: ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:586:4: ( COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            int alt64=2;
            int LA64_0 = input.LA(1);

            if ( (LA64_0==COLON) ) {
                alt64=1;
            }
            switch (alt64) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:586:6: COLON ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    {
                    match(input,COLON,FOLLOW_COLON_in_typeReference5320); 
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:586:13: ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    int alt63=2;
                    int LA63_0 = input.LA(1);

                    if ( (LA63_0==QUOTED_IDENTIFIER||LA63_0==IDENTIFIER) ) {
                        alt63=1;
                    }
                    else if ( (LA63_0==STAR) ) {
                        alt63=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("586:13: ( name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )", 63, 0, input);

                        throw nvae;
                    }
                    switch (alt63) {
                        case 1 :
                            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:586:15: name ccn= cardinalityConstraint
                            {
                            pushFollow(FOLLOW_name_in_typeReference5325);
                            name98=name();
                            _fsp--;

                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference5329);
                            ccn=cardinalityConstraint();
                            _fsp--;

                             type = F.TypeClass(name98.value, ccn); 

                            }
                            break;
                        case 2 :
                            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:587:22: STAR ccs= cardinalityConstraint
                            {
                            STAR99=(Token)input.LT(1);
                            match(input,STAR,FOLLOW_STAR_in_typeReference5355); 
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference5359);
                            ccs=cardinalityConstraint();
                            _fsp--;

                             type = F.at(pos(STAR99)).TypeAny(ccs); 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:591:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );
    public final int cardinalityConstraint() throws RecognitionException {
        int ary = 0;

        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:592:2: ( LBRACKET RBRACKET | QUES | PLUS | STAR | )
            int alt65=5;
            switch ( input.LA(1) ) {
            case LBRACKET:
                {
                alt65=1;
                }
                break;
            case QUES:
                {
                alt65=2;
                }
                break;
            case PLUS:
                {
                alt65=3;
                }
                break;
            case STAR:
                {
                alt65=4;
                }
                break;
            case EOF:
            case ABSTRACT:
            case BREAK:
            case CLASS:
            case RETURN:
            case THROW:
            case VAR:
            case IMPORT:
            case WHILE:
            case CONTINUE:
            case TRY:
            case PRIVATE:
            case PROTECTED:
            case PUBLIC:
            case FUNCTION:
            case READONLY:
            case INVERSE:
            case RPAREN:
            case SEMI:
            case COMMA:
            case EQ:
            case LBRACE:
            case RBRACE:
                {
                alt65=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("591:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 65, 0, input);

                throw nvae;
            }

            switch (alt65) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:592:5: LBRACKET RBRACKET
                    {
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_cardinalityConstraint5421); 
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_cardinalityConstraint5425); 
                     ary = JFXType.CARDINALITY_ANY; 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:593:5: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_cardinalityConstraint5437); 
                     ary = JFXType.CARDINALITY_OPTIONAL; 

                    }
                    break;
                case 3 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:594:5: PLUS
                    {
                    match(input,PLUS,FOLLOW_PLUS_in_cardinalityConstraint5464); 
                     ary = JFXType.CARDINALITY_ANY; 

                    }
                    break;
                case 4 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:595:5: STAR
                    {
                    match(input,STAR,FOLLOW_STAR_in_cardinalityConstraint5491); 
                     ary = JFXType.CARDINALITY_ANY; 

                    }
                    break;
                case 5 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:596:29: 
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:598:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );
    public final JCExpression literal() throws RecognitionException {
        JCExpression expr = null;

        Token t=null;

        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:599:2: (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL )
            int alt66=6;
            switch ( input.LA(1) ) {
            case STRING_LITERAL:
                {
                alt66=1;
                }
                break;
            case INTEGER_LITERAL:
                {
                alt66=2;
                }
                break;
            case FLOATING_POINT_LITERAL:
                {
                alt66=3;
                }
                break;
            case TRUE:
                {
                alt66=4;
                }
                break;
            case FALSE:
                {
                alt66=5;
                }
                break;
            case NULL:
                {
                alt66=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("598:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );", 66, 0, input);

                throw nvae;
            }

            switch (alt66) {
                case 1 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:599:4: t= STRING_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_literal5560); 
                     expr = F.at(pos(t)).Literal(TypeTags.CLASS, t.getText()); 

                    }
                    break;
                case 2 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:600:4: t= INTEGER_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,INTEGER_LITERAL,FOLLOW_INTEGER_LITERAL_in_literal5570); 
                     expr = F.at(pos(t)).Literal(TypeTags.INT, Convert.string2int(t.getText(), 10)); 

                    }
                    break;
                case 3 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:601:4: t= FLOATING_POINT_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,FLOATING_POINT_LITERAL,FOLLOW_FLOATING_POINT_LITERAL_in_literal5580); 
                     expr = F.at(pos(t)).Literal(TypeTags.DOUBLE, Double.valueOf(t.getText())); 

                    }
                    break;
                case 4 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:602:4: t= TRUE
                    {
                    t=(Token)input.LT(1);
                    match(input,TRUE,FOLLOW_TRUE_in_literal5590); 
                     expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 1); 

                    }
                    break;
                case 5 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:603:4: t= FALSE
                    {
                    t=(Token)input.LT(1);
                    match(input,FALSE,FOLLOW_FALSE_in_literal5604); 
                     expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 0); 

                    }
                    break;
                case 6 :
                    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:604:4: t= NULL
                    {
                    t=(Token)input.LT(1);
                    match(input,NULL,FOLLOW_NULL_in_literal5618); 
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:606:1: typeName returns [JCExpression expr] : qualident ;
    public final JCExpression typeName() throws RecognitionException {
        JCExpression expr = null;

        JCExpression qualident100 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:607:8: ( qualident )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:607:10: qualident
            {
            pushFollow(FOLLOW_qualident_in_typeName5645);
            qualident100=qualident();
            _fsp--;

             expr = qualident100; 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:609:1: qualident returns [JCExpression expr] : identifier ( DOT name )* ;
    public final JCExpression qualident() throws RecognitionException {
        JCExpression expr = null;

        JCIdent identifier101 = null;

        name_return name102 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:610:8: ( identifier ( DOT name )* )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:610:10: identifier ( DOT name )*
            {
            pushFollow(FOLLOW_identifier_in_qualident5687);
            identifier101=identifier();
            _fsp--;

             expr = identifier101; 
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:611:10: ( DOT name )*
            loop67:
            do {
                int alt67=2;
                int LA67_0 = input.LA(1);

                if ( (LA67_0==DOT) ) {
                    alt67=1;
                }


                switch (alt67) {
            	case 1 :
            	    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:611:12: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_qualident5715); 
            	    pushFollow(FOLLOW_name_in_qualident5717);
            	    name102=name();
            	    _fsp--;

            	     expr = F.at(name102.pos).Select(expr, name102.value); 

            	    }
            	    break;

            	default :
            	    break loop67;
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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:613:1: identifier returns [JCIdent expr] : name ;
    public final JCIdent identifier() throws RecognitionException {
        JCIdent expr = null;

        name_return name103 = null;


        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:614:2: ( name )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:614:4: name
            {
            pushFollow(FOLLOW_name_in_identifier5754);
            name103=name();
            _fsp--;

             expr = F.at(name103.pos).Ident(name103.value); 

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
    // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:616:1: name returns [Name value, int pos] : tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) ;
    public final name_return name() throws RecognitionException {
        name_return retval = new name_return();
        retval.start = input.LT(1);

        Token tokid=null;

        try {
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:617:2: (tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) )
            // \\JavaFX\\svn\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v2.g:617:4: tokid= ( QUOTED_IDENTIFIER | IDENTIFIER )
            {
            tokid=(Token)input.LT(1);
            if ( input.LA(1)==QUOTED_IDENTIFIER||input.LA(1)==IDENTIFIER ) {
                input.consume();
                errorRecovery=false;
            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_name5788);    throw mse;
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


 

    public static final BitSet FOLLOW_packageDecl_in_module2032 = new BitSet(new long[]{0xC1300005C0120200L,0x0000000000000005L});
    public static final BitSet FOLLOW_moduleItems_in_module2035 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_module2037 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PACKAGE_in_packageDecl2059 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_qualident_in_packageDecl2061 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_packageDecl2063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_moduleItem_in_moduleItems2094 = new BitSet(new long[]{0xC1300005C0120202L,0x0000000000000005L});
    public static final BitSet FOLLOW_importDecl_in_moduleItem2138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDefinition_in_moduleItem2154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_moduleItem2170 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importDecl2206 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_identifier_in_importDecl2209 = new BitSet(new long[]{0x0000000000000000L,0x0000000000280000L});
    public static final BitSet FOLLOW_DOT_in_importDecl2234 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_importDecl2236 = new BitSet(new long[]{0x0000000000000000L,0x0000000000280000L});
    public static final BitSet FOLLOW_DOT_in_importDecl2265 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
    public static final BitSet FOLLOW_STAR_in_importDecl2267 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_importDecl2276 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_classDefinition2307 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_CLASS_in_classDefinition2310 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_classDefinition2312 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000020L});
    public static final BitSet FOLLOW_supers_in_classDefinition2314 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_classDefinition2316 = new BitSet(new long[]{0xC000000000004200L,0x0008000000000005L});
    public static final BitSet FOLLOW_classMembers_in_classDefinition2318 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_classDefinition2320 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXTENDS_in_supers2345 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_supers2349 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_COMMA_in_supers2377 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_supers2381 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_attributeDefinition_in_classMembers2415 = new BitSet(new long[]{0xC000000000004202L,0x0000000000000005L});
    public static final BitSet FOLLOW_functionDefinition_in_classMembers2433 = new BitSet(new long[]{0xC000000000004202L,0x0000000000000005L});
    public static final BitSet FOLLOW_modifierFlags_in_attributeDefinition2462 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeDefinition2464 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_attributeDefinition2466 = new BitSet(new long[]{0x0000000000000000L,0x0000080000880008L});
    public static final BitSet FOLLOW_typeReference_in_attributeDefinition2468 = new BitSet(new long[]{0x0000000000000000L,0x0000000000880008L});
    public static final BitSet FOLLOW_EQ_in_attributeDefinition2471 = new BitSet(new long[]{0x3000F20030010060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_bindOpt_in_attributeDefinition2473 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_attributeDefinition2476 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_inverseClause_in_attributeDefinition2480 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_SEMI_in_attributeDefinition2485 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INVERSE_in_inverseClause2507 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_memberSelector_in_inverseClause2509 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_functionDefinition2527 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_functionDefinition2529 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_functionDefinition2533 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_formalParameters_in_functionDefinition2535 = new BitSet(new long[]{0x0000000000000000L,0x0001080000000000L});
    public static final BitSet FOLLOW_typeReference_in_functionDefinition2539 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_blockExpression_in_functionDefinition2542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags2566 = new BitSet(new long[]{0xC000000000000002L,0x0000000000000001L});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags2579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags2601 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000004L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags2614 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_accessModifier2662 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PRIVATE_in_accessModifier2682 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROTECTED_in_accessModifier2701 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ABSTRACT_in_otherModifier2728 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_READONLY_in_otherModifier2746 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_memberSelector2775 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_DOT_in_memberSelector2779 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_memberSelector2785 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_formalParameters2803 = new BitSet(new long[]{0x0000000000000000L,0x0820000000010000L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters2810 = new BitSet(new long[]{0x0000000000000000L,0x0000000000110000L});
    public static final BitSet FOLLOW_COMMA_in_formalParameters2828 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters2834 = new BitSet(new long[]{0x0000000000000000L,0x0000000000110000L});
    public static final BitSet FOLLOW_RPAREN_in_formalParameters2845 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_formalParameter2860 = new BitSet(new long[]{0x0000000000000002L,0x0000080000000000L});
    public static final BitSet FOLLOW_typeReference_in_formalParameter2862 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_block2888 = new BitSet(new long[]{0xF130F201C0020260L,0x0961B001C000E807L});
    public static final BitSet FOLLOW_statement_in_block2900 = new BitSet(new long[]{0x0000000000000000L,0x0008000000080000L});
    public static final BitSet FOLLOW_expression_in_block2917 = new BitSet(new long[]{0x0000000000000000L,0x0008000000080000L});
    public static final BitSet FOLLOW_SEMI_in_block2934 = new BitSet(new long[]{0xF130F201C0020260L,0x0961B001C000E807L});
    public static final BitSet FOLLOW_statement_in_block2946 = new BitSet(new long[]{0x0000000000000000L,0x0008000000080000L});
    public static final BitSet FOLLOW_expression_in_block2963 = new BitSet(new long[]{0x0000000000000000L,0x0008000000080000L});
    public static final BitSet FOLLOW_SEMI_in_block2986 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_block2989 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_blockExpression3015 = new BitSet(new long[]{0xF130F201C0020260L,0x0961B001C000E807L});
    public static final BitSet FOLLOW_statement_in_blockExpression3027 = new BitSet(new long[]{0x0000000000000000L,0x0008000000080000L});
    public static final BitSet FOLLOW_expression_in_blockExpression3044 = new BitSet(new long[]{0x0000000000000000L,0x0008000000080000L});
    public static final BitSet FOLLOW_SEMI_in_blockExpression3061 = new BitSet(new long[]{0xF130F201C0020260L,0x0961B001C000E807L});
    public static final BitSet FOLLOW_statement_in_blockExpression3073 = new BitSet(new long[]{0x0000000000000000L,0x0008000000080000L});
    public static final BitSet FOLLOW_expression_in_blockExpression3090 = new BitSet(new long[]{0x0000000000000000L,0x0008000000080000L});
    public static final BitSet FOLLOW_SEMI_in_blockExpression3113 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_blockExpression3116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclaration_in_statement3135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionDefinition_in_statement3145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_statement3161 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_LPAREN_in_statement3163 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_statement3165 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_statement3167 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_block_in_statement3169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BREAK_in_statement3176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_statement3190 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THROW_in_statement3211 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_statement3213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_returnStatement_in_statement3234 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tryStatement_in_statement3251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variableDeclaration3280 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_variableDeclaration3283 = new BitSet(new long[]{0x0000000000000002L,0x0000080000800000L});
    public static final BitSet FOLLOW_typeReference_in_variableDeclaration3286 = new BitSet(new long[]{0x0000000000000002L,0x0000000000800000L});
    public static final BitSet FOLLOW_EQ_in_variableDeclaration3297 = new BitSet(new long[]{0x3000F20030010060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_bindOpt_in_variableDeclaration3299 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_variableDeclaration3302 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BIND_in_bindOpt3352 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3368 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAYS_in_bindOpt3383 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3399 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TIE_in_bindOpt3414 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3430 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnStatement3464 = new BitSet(new long[]{0x3000F20000000062L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_returnStatement3467 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRY_in_tryStatement3510 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3516 = new BitSet(new long[]{0x0200000000080000L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement3529 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3535 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catchClause_in_tryStatement3551 = new BitSet(new long[]{0x0200000000080002L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement3574 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement3579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CATCH_in_catchClause3618 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_LPAREN_in_catchClause3621 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_catchClause3624 = new BitSet(new long[]{0x0000000000000000L,0x0000080000010000L});
    public static final BitSet FOLLOW_COLON_in_catchClause3632 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_identifier_in_catchClause3634 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_catchClause3643 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_block_in_catchClause3647 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifExpression_in_expression3675 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_suffixedExpression_in_expression3698 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifExpression3729 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_ifExpression3733 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_THEN_in_ifExpression3737 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_ifExpression3742 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_ELSE_in_ifExpression3750 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_ifExpression3755 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentExpression_in_suffixedExpression3778 = new BitSet(new long[]{0x0000000000000002L,0x0000000140000000L});
    public static final BitSet FOLLOW_set_in_suffixedExpression3789 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression3818 = new BitSet(new long[]{0x0000000000000002L,0x0000000000800000L});
    public static final BitSet FOLLOW_EQ_in_assignmentExpression3833 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression3839 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression3866 = new BitSet(new long[]{0x0000000000000002L,0x000001F000000000L});
    public static final BitSet FOLLOW_assignmentOperator_in_assignmentOpExpression3882 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression3888 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orExpression_in_andExpression3914 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_AND_in_andExpression3930 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_orExpression_in_andExpression3936 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression3964 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001000L});
    public static final BitSet FOLLOW_OR_in_orExpression3979 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression3985 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001000L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression4013 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_INSTANCEOF_in_instanceOfExpression4028 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_identifier_in_instanceOfExpression4030 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4058 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_LTGT_in_relationalExpression4074 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4080 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_EQEQ_in_relationalExpression4094 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4100 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_LTEQ_in_relationalExpression4114 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4120 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_GTEQ_in_relationalExpression4134 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4140 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_LT_in_relationalExpression4154 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4162 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_GT_in_relationalExpression4176 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4184 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_IN_in_relationalExpression4198 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression4206 = new BitSet(new long[]{0x0004000000000002L,0x000000001F400000L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4235 = new BitSet(new long[]{0x0000000000000002L,0x00000000A0000000L});
    public static final BitSet FOLLOW_PLUS_in_additiveExpression4250 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4256 = new BitSet(new long[]{0x0000000000000002L,0x00000000A0000000L});
    public static final BitSet FOLLOW_SUB_in_additiveExpression4269 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4276 = new BitSet(new long[]{0x0000000000000002L,0x00000000A0000000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4304 = new BitSet(new long[]{0x0000000000000002L,0x0000000E00000000L});
    public static final BitSet FOLLOW_STAR_in_multiplicativeExpression4320 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4327 = new BitSet(new long[]{0x0000000000000002L,0x0000000E00000000L});
    public static final BitSet FOLLOW_SLASH_in_multiplicativeExpression4341 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4347 = new BitSet(new long[]{0x0000000000000002L,0x0000000E00000000L});
    public static final BitSet FOLLOW_PERCENT_in_multiplicativeExpression4361 = new BitSet(new long[]{0x3000F00000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4365 = new BitSet(new long[]{0x0000000000000002L,0x0000000E00000000L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression4395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryOperator_in_unaryExpression4406 = new BitSet(new long[]{0x2000F00000000000L,0x0961A00000008800L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression4410 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primaryExpression_in_postfixExpression4430 = new BitSet(new long[]{0x0000000000000002L,0x0000000000220000L});
    public static final BitSet FOLLOW_DOT_in_postfixExpression4445 = new BitSet(new long[]{0x0000000000100000L,0x0820000000000000L});
    public static final BitSet FOLLOW_CLASS_in_postfixExpression4449 = new BitSet(new long[]{0x0000000000000002L,0x0000000000220000L});
    public static final BitSet FOLLOW_name_in_postfixExpression4473 = new BitSet(new long[]{0x0000000000000002L,0x0000000000228000L});
    public static final BitSet FOLLOW_LPAREN_in_postfixExpression4498 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C001E800L});
    public static final BitSet FOLLOW_expressionListOpt_in_postfixExpression4500 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_postfixExpression4502 = new BitSet(new long[]{0x0000000000000002L,0x0000000000228000L});
    public static final BitSet FOLLOW_LBRACKET_in_postfixExpression4534 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_name_in_postfixExpression4537 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_BAR_in_postfixExpression4539 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_postfixExpression4543 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_RBRACKET_in_postfixExpression4546 = new BitSet(new long[]{0x0000000000000002L,0x0000000000220000L});
    public static final BitSet FOLLOW_newExpression_in_primaryExpression4571 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression4583 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_primaryExpression4585 = new BitSet(new long[]{0xC000000000004200L,0x0828000000000005L});
    public static final BitSet FOLLOW_objectLiteral_in_primaryExpression4588 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_primaryExpression4590 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THIS_in_primaryExpression4607 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_primaryExpression4627 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression4647 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression4668 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C001E800L});
    public static final BitSet FOLLOW_expressionListOpt_in_primaryExpression4672 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression4676 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_stringExpression_in_primaryExpression4696 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primaryExpression4715 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_blockExpression_in_primaryExpression4735 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression4753 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_primaryExpression4755 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression4757 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_newExpression4790 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_identifier_in_newExpression4793 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_LPAREN_in_newExpression4801 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C001E800L});
    public static final BitSet FOLLOW_expressionListOpt_in_newExpression4805 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_RPAREN_in_newExpression4809 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_objectLiteralPart_in_objectLiteral4849 = new BitSet(new long[]{0xC000000000004202L,0x0820000000000005L});
    public static final BitSet FOLLOW_name_in_objectLiteralPart4875 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_COLON_in_objectLiteralPart4877 = new BitSet(new long[]{0x3000F20030010060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_bindOpt_in_objectLiteralPart4880 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_objectLiteralPart4882 = new BitSet(new long[]{0x0000000000000002L,0x0000000000180000L});
    public static final BitSet FOLLOW_set_in_objectLiteralPart4884 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeDefinition_in_objectLiteralPart4904 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionDefinition_in_objectLiteralPart4915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression4937 = new BitSet(new long[]{0x3000F20000000060L,0x0971B001C000E800L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression4946 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_stringExpression4957 = new BitSet(new long[]{0x0000000000000000L,0x0006000000000000L});
    public static final BitSet FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression4972 = new BitSet(new long[]{0x3000F20000000060L,0x0971B001C000E800L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression4984 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_stringExpression4998 = new BitSet(new long[]{0x0000000000000000L,0x0006000000000000L});
    public static final BitSet FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression5019 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull5049 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt5081 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_COMMA_in_expressionListOpt5092 = new BitSet(new long[]{0x3000F20000000060L,0x0961B001C000E800L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt5098 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_POUND_in_unaryOperator5121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_unaryOperator5132 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUB_in_unaryOperator5145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_unaryOperator5158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIZEOF_in_unaryOperator5171 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TYPEOF_in_unaryOperator5184 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REVERSE_in_unaryOperator5197 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_unaryOperator5210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_unaryOperator5223 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSEQ_in_assignmentOperator5244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBEQ_in_assignmentOperator5257 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAREQ_in_assignmentOperator5270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SLASHEQ_in_assignmentOperator5283 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENTEQ_in_assignmentOperator5296 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLON_in_typeReference5320 = new BitSet(new long[]{0x0000000000000000L,0x0820000200000000L});
    public static final BitSet FOLLOW_name_in_typeReference5325 = new BitSet(new long[]{0x0000000000000002L,0x0000100220020000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference5329 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_typeReference5355 = new BitSet(new long[]{0x0000000000000002L,0x0000100220020000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference5359 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_cardinalityConstraint5421 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_RBRACKET_in_cardinalityConstraint5425 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_cardinalityConstraint5437 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUS_in_cardinalityConstraint5464 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_cardinalityConstraint5491 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_literal5560 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_LITERAL_in_literal5570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOATING_POINT_LITERAL_in_literal5580 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRUE_in_literal5590 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALSE_in_literal5604 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_literal5618 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualident_in_typeName5645 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_qualident5687 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_DOT_in_qualident5715 = new BitSet(new long[]{0x0000000000000000L,0x0820000000000000L});
    public static final BitSet FOLLOW_name_in_qualident5717 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_name_in_identifier5754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_name5788 = new BitSet(new long[]{0x0000000000000002L});

}