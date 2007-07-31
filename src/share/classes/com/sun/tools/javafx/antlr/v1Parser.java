// $ANTLR 3.0 \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g 2007-07-30 18:11:04

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
import java.util.Map;
import java.util.HashMap;
public class v1Parser extends AbstractGeneratedParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "BAR", "POUND", "TYPEOF", "DOTDOT", "LARROW", "ABSTRACT", "AFTER", "AND", "AS", "ASSERT", "ATTRIBUTE", "BEFORE", "BIND", "BIBIND", "BREAK", "BY", "CATCH", "CLASS", "DELETE", "DISTINCT", "DO", "DUR", "EASEBOTH", "EASEIN", "EASEOUT", "TIE", "STAYS", "RETURN", "THROW", "VAR", "PACKAGE", "IMPORT", "FROM", "LATER", "TRIGGER", "ON", "INSERT", "INTO", "FIRST", "LAST", "IF", "THEN", "ELSE", "THIS", "NULL", "TRUE", "FALSE", "FOR", "UNITINTERVAL", "IN", "FPS", "WHILE", "CONTINUE", "LINEAR", "MOTION", "TRY", "FINALLY", "LAZY", "FOREACH", "WHERE", "NOT", "NEW", "PRIVATE", "PROTECTED", "PUBLIC", "OPERATION", "FUNCTION", "READONLY", "INVERSE", "TYPE", "EXTENDS", "ORDER", "INDEX", "INSTANCEOF", "INDEXOF", "SELECT", "SUPER", "OR", "SIZEOF", "REVERSE", "XOR", "LPAREN", "RPAREN", "LBRACKET", "RBRACKET", "SEMI", "COMMA", "DOT", "EQEQ", "EQ", "GT", "LT", "LTGT", "LTEQ", "GTEQ", "PLUS", "PLUSPLUS", "SUB", "SUBSUB", "STAR", "SLASH", "PERCENT", "PLUSEQ", "SUBEQ", "STAREQ", "SLASHEQ", "PERCENTEQ", "LTLT", "GTGT", "COLON", "QUES", "STRING_LITERAL", "NextIsPercent", "QUOTE_LBRACE_STRING_LITERAL", "LBRACE", "RBRACE_QUOTE_STRING_LITERAL", "RBRACE_LBRACE_STRING_LITERAL", "RBRACE", "FORMAT_STRING_LITERAL", "QUOTED_IDENTIFIER", "INTEGER_LITERAL", "Exponent", "FLOATING_POINT_LITERAL", "Letter", "JavaIDDigit", "IDENTIFIER", "WS", "COMMENT", "LINE_COMMENT"
    };
    public static final int PACKAGE=34;
    public static final int FUNCTION=70;
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
    public static final int GTGT=112;
    public static final int EOF=-1;
    public static final int RBRACE_QUOTE_STRING_LITERAL=119;
    public static final int BREAK=18;
    public static final int TYPE=73;
    public static final int LBRACKET=87;
    public static final int RPAREN=86;
    public static final int IMPORT=35;
    public static final int LINEAR=57;
    public static final int STRING_LITERAL=115;
    public static final int FLOATING_POINT_LITERAL=126;
    public static final int INSERT=40;
    public static final int SUBSUB=102;
    public static final int BIND=16;
    public static final int STAREQ=108;
    public static final int RETURN=31;
    public static final int THIS=47;
    public static final int VAR=33;
    public static final int SUPER=80;
    public static final int EQ=93;
    public static final int LAST=43;
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
    public static final int AS=12;
    public static final int INDEX=76;
    public static final int SLASH=104;
    public static final int THEN=45;
    public static final int IN=53;
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
    public static final int GTEQ=98;
    public static final int AFTER=10;
    public static final int READONLY=71;
    public static final int TRUE=49;
    public static final int SEMI=89;
    public static final int COLON=113;
    public static final int PERCENTEQ=110;
    public static final int FINALLY=60;
    public static final int FORMAT_STRING_LITERAL=122;
    public static final int EASEIN=27;
    public static final int QUOTED_IDENTIFIER=123;
    public static final int FPS=54;
    public static final int PUBLIC=68;
    public static final int EXTENDS=74;
    public static final int BAR=4;
    public static final int FIRST=42;

        public v1Parser(TokenStream input) {
            super(input);
            ruleMemo = new HashMap[297+1];
         }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "\\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g"; }

    
            public v1Parser(Context context, CharSequence content) {
               this(new CommonTokenStream(new v1Lexer(new ANTLRStringStream(content.toString()))));
               initialize(context);
        	}



    // $ANTLR start module
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:326:1: module returns [JCCompilationUnit result] : ( packageDecl )? moduleItems EOF ;
    public final JCCompilationUnit module() throws RecognitionException {
        JCCompilationUnit result = null;

        JCExpression packageDecl1 = null;

        ListBuffer<JCTree> moduleItems2 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:327:10: ( ( packageDecl )? moduleItems EOF )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:327:10: ( packageDecl )? moduleItems EOF
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:327:10: ( packageDecl )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==PACKAGE) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: packageDecl
                    {
                    pushFollow(FOLLOW_packageDecl_in_module2072);
                    packageDecl1=packageDecl();
                    _fsp--;
                    if (failed) return result;

                    }
                    break;

            }

            pushFollow(FOLLOW_moduleItems_in_module2075);
            moduleItems2=moduleItems();
            _fsp--;
            if (failed) return result;
            match(input,EOF,FOLLOW_EOF_in_module2077); if (failed) return result;
            if ( backtracking==0 ) {
               result = F.TopLevel(noJCAnnotations(), packageDecl1, moduleItems2.toList()); 
            }

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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:329:1: packageDecl returns [JCExpression value] : PACKAGE qualident SEMI ;
    public final JCExpression packageDecl() throws RecognitionException {
        JCExpression value = null;

        JCExpression qualident3 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:330:10: ( PACKAGE qualident SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:330:10: PACKAGE qualident SEMI
            {
            match(input,PACKAGE,FOLLOW_PACKAGE_in_packageDecl2110); if (failed) return value;
            pushFollow(FOLLOW_qualident_in_packageDecl2112);
            qualident3=qualident();
            _fsp--;
            if (failed) return value;
            match(input,SEMI,FOLLOW_SEMI_in_packageDecl2114); if (failed) return value;
            if ( backtracking==0 ) {
               value = qualident3; 
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
    // $ANTLR end packageDecl


    // $ANTLR start moduleItems
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:331:1: moduleItems returns [ListBuffer<JCTree> items = new ListBuffer<JCTree>()] : ( moduleItem )* ;
    public final ListBuffer<JCTree> moduleItems() throws RecognitionException {
        ListBuffer<JCTree> items =  new ListBuffer<JCTree>();

        JCTree moduleItem4 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:332:11: ( ( moduleItem )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:332:11: ( moduleItem )*
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:332:11: ( moduleItem )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=POUND && LA2_0<=TYPEOF)||LA2_0==ABSTRACT||LA2_0==ATTRIBUTE||LA2_0==BREAK||(LA2_0>=CLASS && LA2_0<=DELETE)||LA2_0==DO||(LA2_0>=RETURN && LA2_0<=VAR)||LA2_0==IMPORT||LA2_0==TRIGGER||LA2_0==INSERT||LA2_0==IF||(LA2_0>=THIS && LA2_0<=UNITINTERVAL)||(LA2_0>=WHILE && LA2_0<=CONTINUE)||LA2_0==TRY||LA2_0==FOREACH||(LA2_0>=NOT && LA2_0<=READONLY)||(LA2_0>=INDEXOF && LA2_0<=SUPER)||(LA2_0>=SIZEOF && LA2_0<=REVERSE)||LA2_0==LPAREN||LA2_0==LBRACKET||LA2_0==DOT||(LA2_0>=PLUSPLUS && LA2_0<=SUBSUB)||(LA2_0>=QUES && LA2_0<=STRING_LITERAL)||LA2_0==QUOTE_LBRACE_STRING_LITERAL||(LA2_0>=QUOTED_IDENTIFIER && LA2_0<=INTEGER_LITERAL)||LA2_0==FLOATING_POINT_LITERAL||LA2_0==IDENTIFIER) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:332:12: moduleItem
            	    {
            	    pushFollow(FOLLOW_moduleItem_in_moduleItems2143);
            	    moduleItem4=moduleItem();
            	    _fsp--;
            	    if (failed) return items;
            	    if ( backtracking==0 ) {
            	       items.append(moduleItem4); 
            	    }

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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:333:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );
    public final JCTree moduleItem() throws RecognitionException {
        JCTree value = null;

        JCTree importDecl5 = null;

        JFXClassDeclaration classDefinition6 = null;

        JFXAttributeDefinition attributeDefinition7 = null;

        JFXOperationMemberDefinition memberOperationDefinition8 = null;

        JFXFunctionMemberDefinition memberFunctionDefinition9 = null;

        JFXAbstractTriggerOn changeRule10 = null;

        JCStatement statementExcept11 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:334:10: ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept )
            int alt3=7;
            switch ( input.LA(1) ) {
            case IMPORT:
                {
                alt3=1;
                }
                break;
            case ABSTRACT:
            case CLASS:
            case PRIVATE:
            case PROTECTED:
            case PUBLIC:
            case READONLY:
                {
                alt3=2;
                }
                break;
            case ATTRIBUTE:
                {
                alt3=3;
                }
                break;
            case OPERATION:
                {
                int LA3_4 = input.LA(2);

                if ( (LA3_4==QUOTED_IDENTIFIER||LA3_4==IDENTIFIER) ) {
                    int LA3_8 = input.LA(3);

                    if ( (LA3_8==LPAREN) ) {
                        alt3=7;
                    }
                    else if ( (LA3_8==DOT) ) {
                        alt3=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return value;}
                        NoViableAltException nvae =
                            new NoViableAltException("333:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );", 3, 8, input);

                        throw nvae;
                    }
                }
                else if ( (LA3_4==LPAREN) ) {
                    alt3=7;
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("333:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );", 3, 4, input);

                    throw nvae;
                }
                }
                break;
            case FUNCTION:
                {
                int LA3_5 = input.LA(2);

                if ( (LA3_5==LPAREN) ) {
                    alt3=7;
                }
                else if ( (LA3_5==QUOTED_IDENTIFIER||LA3_5==IDENTIFIER) ) {
                    int LA3_9 = input.LA(3);

                    if ( (LA3_9==LPAREN) ) {
                        alt3=7;
                    }
                    else if ( (LA3_9==DOT) ) {
                        alt3=5;
                    }
                    else {
                        if (backtracking>0) {failed=true; return value;}
                        NoViableAltException nvae =
                            new NoViableAltException("333:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );", 3, 9, input);

                        throw nvae;
                    }
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("333:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );", 3, 5, input);

                    throw nvae;
                }
                }
                break;
            case TRIGGER:
                {
                alt3=6;
                }
                break;
            case POUND:
            case TYPEOF:
            case BREAK:
            case DELETE:
            case DO:
            case RETURN:
            case THROW:
            case VAR:
            case INSERT:
            case IF:
            case THIS:
            case NULL:
            case TRUE:
            case FALSE:
            case FOR:
            case UNITINTERVAL:
            case WHILE:
            case CONTINUE:
            case TRY:
            case FOREACH:
            case NOT:
            case NEW:
            case INDEXOF:
            case SELECT:
            case SUPER:
            case SIZEOF:
            case REVERSE:
            case LPAREN:
            case LBRACKET:
            case DOT:
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
                alt3=7;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("333:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:334:10: importDecl
                    {
                    pushFollow(FOLLOW_importDecl_in_moduleItem2185);
                    importDecl5=importDecl();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = importDecl5; 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:335:10: classDefinition
                    {
                    pushFollow(FOLLOW_classDefinition_in_moduleItem2200);
                    classDefinition6=classDefinition();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = classDefinition6; 
                    }

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:336:10: attributeDefinition
                    {
                    pushFollow(FOLLOW_attributeDefinition_in_moduleItem2215);
                    attributeDefinition7=attributeDefinition();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = attributeDefinition7; 
                    }

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:337:10: memberOperationDefinition
                    {
                    pushFollow(FOLLOW_memberOperationDefinition_in_moduleItem2230);
                    memberOperationDefinition8=memberOperationDefinition();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = memberOperationDefinition8; 
                    }

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:338:10: memberFunctionDefinition
                    {
                    pushFollow(FOLLOW_memberFunctionDefinition_in_moduleItem2244);
                    memberFunctionDefinition9=memberFunctionDefinition();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = memberFunctionDefinition9; 
                    }

                    }
                    break;
                case 6 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:339:10: TRIGGER ON changeRule
                    {
                    match(input,TRIGGER,FOLLOW_TRIGGER_in_moduleItem2258); if (failed) return value;
                    match(input,ON,FOLLOW_ON_in_moduleItem2260); if (failed) return value;
                    pushFollow(FOLLOW_changeRule_in_moduleItem2262);
                    changeRule10=changeRule();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = changeRule10; 
                    }

                    }
                    break;
                case 7 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:340:10: statementExcept
                    {
                    pushFollow(FOLLOW_statementExcept_in_moduleItem2276);
                    statementExcept11=statementExcept();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = statementExcept11; 
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
        return value;
    }
    // $ANTLR end moduleItem


    // $ANTLR start importDecl
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:341:1: importDecl returns [JCTree value] : IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI ;
    public final JCTree importDecl() throws RecognitionException {
        JCTree value = null;

        Token STAR14=null;
        Token IMPORT15=null;
        JCIdent identifier12 = null;

        name_return name13 = null;


         JCExpression pid = null; 
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:343:11: ( IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:343:11: IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI
            {
            IMPORT15=(Token)input.LT(1);
            match(input,IMPORT,FOLLOW_IMPORT_in_importDecl2305); if (failed) return value;
            pushFollow(FOLLOW_identifier_in_importDecl2308);
            identifier12=identifier();
            _fsp--;
            if (failed) return value;
            if ( backtracking==0 ) {
               pid = identifier12; 
            }
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:344:18: ( DOT name )*
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
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:344:20: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_importDecl2332); if (failed) return value;
            	    pushFollow(FOLLOW_name_in_importDecl2334);
            	    name13=name();
            	    _fsp--;
            	    if (failed) return value;
            	    if ( backtracking==0 ) {
            	       pid = F.at(name13.pos).Select(pid, name13.value); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:345:18: ( DOT STAR )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==DOT) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:345:20: DOT STAR
                    {
                    match(input,DOT,FOLLOW_DOT_in_importDecl2362); if (failed) return value;
                    STAR14=(Token)input.LT(1);
                    match(input,STAR,FOLLOW_STAR_in_importDecl2364); if (failed) return value;
                    if ( backtracking==0 ) {
                       pid = F.at(pos(STAR14)).Select(pid, names.asterisk); 
                    }

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_importDecl2372); if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(pos(IMPORT15)).Import(pid, false); 
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
    // $ANTLR end importDecl


    // $ANTLR start classDefinition
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:347:1: classDefinition returns [JFXClassDeclaration value] : modifierFlags CLASS name supers LBRACE classMembers RBRACE ;
    public final JFXClassDeclaration classDefinition() throws RecognitionException {
        JFXClassDeclaration value = null;

        Token CLASS16=null;
        JCModifiers modifierFlags17 = null;

        name_return name18 = null;

        ListBuffer<Name> supers19 = null;

        ListBuffer<JFXMemberDeclaration> classMembers20 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:348:4: ( modifierFlags CLASS name supers LBRACE classMembers RBRACE )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:348:4: modifierFlags CLASS name supers LBRACE classMembers RBRACE
            {
            pushFollow(FOLLOW_modifierFlags_in_classDefinition2398);
            modifierFlags17=modifierFlags();
            _fsp--;
            if (failed) return value;
            CLASS16=(Token)input.LT(1);
            match(input,CLASS,FOLLOW_CLASS_in_classDefinition2401); if (failed) return value;
            pushFollow(FOLLOW_name_in_classDefinition2403);
            name18=name();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_supers_in_classDefinition2405);
            supers19=supers();
            _fsp--;
            if (failed) return value;
            match(input,LBRACE,FOLLOW_LBRACE_in_classDefinition2407); if (failed) return value;
            pushFollow(FOLLOW_classMembers_in_classDefinition2409);
            classMembers20=classMembers();
            _fsp--;
            if (failed) return value;
            match(input,RBRACE,FOLLOW_RBRACE_in_classDefinition2411); if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(pos(CLASS16)).ClassDeclaration(modifierFlags17, name18.value,
              	                                supers19.toList(), classMembers20.toList()); 
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
    // $ANTLR end classDefinition


    // $ANTLR start supers
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:351:1: supers returns [ListBuffer<Name> names = new ListBuffer<Name>()] : ( EXTENDS name1= name ( COMMA namen= name )* )? ;
    public final ListBuffer<Name> supers() throws RecognitionException {
        ListBuffer<Name> names =  new ListBuffer<Name>();

        name_return name1 = null;

        name_return namen = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:352:4: ( ( EXTENDS name1= name ( COMMA namen= name )* )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:352:4: ( EXTENDS name1= name ( COMMA namen= name )* )?
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:352:4: ( EXTENDS name1= name ( COMMA namen= name )* )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==EXTENDS) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:352:5: EXTENDS name1= name ( COMMA namen= name )*
                    {
                    match(input,EXTENDS,FOLLOW_EXTENDS_in_supers2431); if (failed) return names;
                    pushFollow(FOLLOW_name_in_supers2435);
                    name1=name();
                    _fsp--;
                    if (failed) return names;
                    if ( backtracking==0 ) {
                       names.append(name1.value); 
                    }
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:353:12: ( COMMA namen= name )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==COMMA) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:353:14: COMMA namen= name
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_supers2458); if (failed) return names;
                    	    pushFollow(FOLLOW_name_in_supers2462);
                    	    namen=name();
                    	    _fsp--;
                    	    if (failed) return names;
                    	    if ( backtracking==0 ) {
                    	       names.append(namen.value); 
                    	    }

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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:355:1: classMembers returns [ListBuffer<JFXMemberDeclaration> mems = new ListBuffer<JFXMemberDeclaration>()] : ( attributeDecl | functionDecl | operationDecl )* ;
    public final ListBuffer<JFXMemberDeclaration> classMembers() throws RecognitionException {
        ListBuffer<JFXMemberDeclaration> mems =  new ListBuffer<JFXMemberDeclaration>();

        JFXAttributeDeclaration attributeDecl21 = null;

        JFXFunctionMemberDeclaration functionDecl22 = null;

        JFXOperationMemberDeclaration operationDecl23 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:356:3: ( ( attributeDecl | functionDecl | operationDecl )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:356:3: ( attributeDecl | functionDecl | operationDecl )*
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:356:3: ( attributeDecl | functionDecl | operationDecl )*
            loop8:
            do {
                int alt8=4;
                switch ( input.LA(1) ) {
                case ABSTRACT:
                    {
                    switch ( input.LA(2) ) {
                    case PUBLIC:
                        {
                        switch ( input.LA(3) ) {
                        case OPERATION:
                            {
                            alt8=3;
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
                        switch ( input.LA(3) ) {
                        case OPERATION:
                            {
                            alt8=3;
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
                        switch ( input.LA(3) ) {
                        case OPERATION:
                            {
                            alt8=3;
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
                    case OPERATION:
                        {
                        alt8=3;
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
                        switch ( input.LA(3) ) {
                        case OPERATION:
                            {
                            alt8=3;
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
                        switch ( input.LA(3) ) {
                        case OPERATION:
                            {
                            alt8=3;
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
                        switch ( input.LA(3) ) {
                        case OPERATION:
                            {
                            alt8=3;
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
                    case OPERATION:
                        {
                        alt8=3;
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
                        switch ( input.LA(3) ) {
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
                        case OPERATION:
                            {
                            alt8=3;
                            }
                            break;

                        }

                        }
                        break;
                    case READONLY:
                        {
                        switch ( input.LA(3) ) {
                        case ATTRIBUTE:
                            {
                            alt8=1;
                            }
                            break;
                        case OPERATION:
                            {
                            alt8=3;
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
                    case OPERATION:
                        {
                        alt8=3;
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
                        switch ( input.LA(3) ) {
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
                        case OPERATION:
                            {
                            alt8=3;
                            }
                            break;

                        }

                        }
                        break;
                    case READONLY:
                        {
                        switch ( input.LA(3) ) {
                        case ATTRIBUTE:
                            {
                            alt8=1;
                            }
                            break;
                        case OPERATION:
                            {
                            alt8=3;
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
                    case OPERATION:
                        {
                        alt8=3;
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
                        switch ( input.LA(3) ) {
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
                        case OPERATION:
                            {
                            alt8=3;
                            }
                            break;

                        }

                        }
                        break;
                    case READONLY:
                        {
                        switch ( input.LA(3) ) {
                        case ATTRIBUTE:
                            {
                            alt8=1;
                            }
                            break;
                        case OPERATION:
                            {
                            alt8=3;
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
                    case OPERATION:
                        {
                        alt8=3;
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
                case OPERATION:
                    {
                    alt8=3;
                    }
                    break;

                }

                switch (alt8) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:356:5: attributeDecl
            	    {
            	    pushFollow(FOLLOW_attributeDecl_in_classMembers2491);
            	    attributeDecl21=attributeDecl();
            	    _fsp--;
            	    if (failed) return mems;
            	    if ( backtracking==0 ) {
            	       mems.append(attributeDecl21); 
            	    }

            	    }
            	    break;
            	case 2 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:357:5: functionDecl
            	    {
            	    pushFollow(FOLLOW_functionDecl_in_classMembers2514);
            	    functionDecl22=functionDecl();
            	    _fsp--;
            	    if (failed) return mems;
            	    if ( backtracking==0 ) {
            	       mems.append(functionDecl22); 
            	    }

            	    }
            	    break;
            	case 3 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:358:5: operationDecl
            	    {
            	    pushFollow(FOLLOW_operationDecl_in_classMembers2538);
            	    operationDecl23=operationDecl();
            	    _fsp--;
            	    if (failed) return mems;
            	    if ( backtracking==0 ) {
            	       mems.append(operationDecl23); 
            	    }

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


    // $ANTLR start attributeDecl
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:360:1: attributeDecl returns [JFXAttributeDeclaration decl] : modifierFlags ATTRIBUTE name typeReference inverseClause ( orderBy | indexOn )? SEMI ;
    public final JFXAttributeDeclaration attributeDecl() throws RecognitionException {
        JFXAttributeDeclaration decl = null;

        Token ATTRIBUTE24=null;
        JCModifiers modifierFlags25 = null;

        name_return name26 = null;

        JFXType typeReference27 = null;

        JFXMemberSelector inverseClause28 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:361:4: ( modifierFlags ATTRIBUTE name typeReference inverseClause ( orderBy | indexOn )? SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:361:4: modifierFlags ATTRIBUTE name typeReference inverseClause ( orderBy | indexOn )? SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_attributeDecl2575);
            modifierFlags25=modifierFlags();
            _fsp--;
            if (failed) return decl;
            ATTRIBUTE24=(Token)input.LT(1);
            match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeDecl2577); if (failed) return decl;
            pushFollow(FOLLOW_name_in_attributeDecl2579);
            name26=name();
            _fsp--;
            if (failed) return decl;
            pushFollow(FOLLOW_typeReference_in_attributeDecl2581);
            typeReference27=typeReference();
            _fsp--;
            if (failed) return decl;
            pushFollow(FOLLOW_inverseClause_in_attributeDecl2583);
            inverseClause28=inverseClause();
            _fsp--;
            if (failed) return decl;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:361:62: ( orderBy | indexOn )?
            int alt9=3;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==ORDER) ) {
                alt9=1;
            }
            else if ( (LA9_0==INDEX) ) {
                alt9=2;
            }
            switch (alt9) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:361:63: orderBy
                    {
                    pushFollow(FOLLOW_orderBy_in_attributeDecl2587);
                    orderBy();
                    _fsp--;
                    if (failed) return decl;

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:361:73: indexOn
                    {
                    pushFollow(FOLLOW_indexOn_in_attributeDecl2591);
                    indexOn();
                    _fsp--;
                    if (failed) return decl;

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_attributeDecl2595); if (failed) return decl;
            if ( backtracking==0 ) {
               decl = F.at(pos(ATTRIBUTE24)).AttributeDeclaration(modifierFlags25, name26.value, typeReference27,
              	                                    inverseClause28, null/*order/index*/); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return decl;
    }
    // $ANTLR end attributeDecl


    // $ANTLR start inverseClause
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:364:1: inverseClause returns [JFXMemberSelector inverse = null] : ( INVERSE memberSelector )? ;
    public final JFXMemberSelector inverseClause() throws RecognitionException {
        JFXMemberSelector inverse =  null;

        JFXMemberSelector memberSelector29 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:365:4: ( ( INVERSE memberSelector )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:365:4: ( INVERSE memberSelector )?
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:365:4: ( INVERSE memberSelector )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==INVERSE) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:365:5: INVERSE memberSelector
                    {
                    match(input,INVERSE,FOLLOW_INVERSE_in_inverseClause2614); if (failed) return inverse;
                    pushFollow(FOLLOW_memberSelector_in_inverseClause2616);
                    memberSelector29=memberSelector();
                    _fsp--;
                    if (failed) return inverse;
                    if ( backtracking==0 ) {
                       inverse = memberSelector29; 
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
        return inverse;
    }
    // $ANTLR end inverseClause


    // $ANTLR start functionDecl
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:366:1: functionDecl returns [JFXFunctionMemberDeclaration decl] : modifierFlags FUNCTION name formalParameters typeReference SEMI ;
    public final JFXFunctionMemberDeclaration functionDecl() throws RecognitionException {
        JFXFunctionMemberDeclaration decl = null;

        name_return name30 = null;

        JCModifiers modifierFlags31 = null;

        JFXType typeReference32 = null;

        ListBuffer<JCTree> formalParameters33 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:367:4: ( modifierFlags FUNCTION name formalParameters typeReference SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:367:4: modifierFlags FUNCTION name formalParameters typeReference SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_functionDecl2634);
            modifierFlags31=modifierFlags();
            _fsp--;
            if (failed) return decl;
            match(input,FUNCTION,FOLLOW_FUNCTION_in_functionDecl2636); if (failed) return decl;
            pushFollow(FOLLOW_name_in_functionDecl2638);
            name30=name();
            _fsp--;
            if (failed) return decl;
            pushFollow(FOLLOW_formalParameters_in_functionDecl2640);
            formalParameters33=formalParameters();
            _fsp--;
            if (failed) return decl;
            pushFollow(FOLLOW_typeReference_in_functionDecl2642);
            typeReference32=typeReference();
            _fsp--;
            if (failed) return decl;
            match(input,SEMI,FOLLOW_SEMI_in_functionDecl2644); if (failed) return decl;
            if ( backtracking==0 ) {
               decl =  F.at(name30.pos).FunctionDeclaration(modifierFlags31, name30.value, typeReference32,
              	                                            formalParameters33.toList()); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return decl;
    }
    // $ANTLR end functionDecl


    // $ANTLR start operationDecl
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:371:1: operationDecl returns [JFXOperationMemberDeclaration decl] : modifierFlags OPERATION name formalParameters typeReference SEMI ;
    public final JFXOperationMemberDeclaration operationDecl() throws RecognitionException {
        JFXOperationMemberDeclaration decl = null;

        Token OPERATION34=null;
        JCModifiers modifierFlags35 = null;

        name_return name36 = null;

        JFXType typeReference37 = null;

        ListBuffer<JCTree> formalParameters38 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:372:4: ( modifierFlags OPERATION name formalParameters typeReference SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:372:4: modifierFlags OPERATION name formalParameters typeReference SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_operationDecl2663);
            modifierFlags35=modifierFlags();
            _fsp--;
            if (failed) return decl;
            OPERATION34=(Token)input.LT(1);
            match(input,OPERATION,FOLLOW_OPERATION_in_operationDecl2667); if (failed) return decl;
            pushFollow(FOLLOW_name_in_operationDecl2671);
            name36=name();
            _fsp--;
            if (failed) return decl;
            pushFollow(FOLLOW_formalParameters_in_operationDecl2675);
            formalParameters38=formalParameters();
            _fsp--;
            if (failed) return decl;
            pushFollow(FOLLOW_typeReference_in_operationDecl2679);
            typeReference37=typeReference();
            _fsp--;
            if (failed) return decl;
            match(input,SEMI,FOLLOW_SEMI_in_operationDecl2684); if (failed) return decl;
            if ( backtracking==0 ) {
               decl = F.at(pos(OPERATION34)).OperationDeclaration(modifierFlags35, name36.value, typeReference37,
              	                                            formalParameters38.toList()); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return decl;
    }
    // $ANTLR end operationDecl


    // $ANTLR start attributeDefinition
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:375:1: attributeDefinition returns [JFXAttributeDefinition def] : ATTRIBUTE memberSelector EQ bindOpt expression SEMI ;
    public final JFXAttributeDefinition attributeDefinition() throws RecognitionException {
        JFXAttributeDefinition def = null;

        Token ATTRIBUTE39=null;
        JFXMemberSelector memberSelector40 = null;

        JCExpression expression41 = null;

        JavafxBindStatus bindOpt42 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:376:4: ( ATTRIBUTE memberSelector EQ bindOpt expression SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:376:4: ATTRIBUTE memberSelector EQ bindOpt expression SEMI
            {
            ATTRIBUTE39=(Token)input.LT(1);
            match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeDefinition2703); if (failed) return def;
            pushFollow(FOLLOW_memberSelector_in_attributeDefinition2707);
            memberSelector40=memberSelector();
            _fsp--;
            if (failed) return def;
            match(input,EQ,FOLLOW_EQ_in_attributeDefinition2711); if (failed) return def;
            pushFollow(FOLLOW_bindOpt_in_attributeDefinition2713);
            bindOpt42=bindOpt();
            _fsp--;
            if (failed) return def;
            pushFollow(FOLLOW_expression_in_attributeDefinition2716);
            expression41=expression();
            _fsp--;
            if (failed) return def;
            match(input,SEMI,FOLLOW_SEMI_in_attributeDefinition2720); if (failed) return def;
            if ( backtracking==0 ) {
               def = F.at(pos(ATTRIBUTE39)).AttributeDefinition(memberSelector40, expression41, bindOpt42); 
            }

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


    // $ANTLR start memberOperationDefinition
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:378:1: memberOperationDefinition returns [JFXOperationMemberDefinition def] : OPERATION memberSelector formalParameters typeReference block ;
    public final JFXOperationMemberDefinition memberOperationDefinition() throws RecognitionException {
        JFXOperationMemberDefinition def = null;

        Token OPERATION43=null;
        JFXMemberSelector memberSelector44 = null;

        JFXType typeReference45 = null;

        ListBuffer<JCTree> formalParameters46 = null;

        JCBlock block47 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:379:4: ( OPERATION memberSelector formalParameters typeReference block )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:379:4: OPERATION memberSelector formalParameters typeReference block
            {
            OPERATION43=(Token)input.LT(1);
            match(input,OPERATION,FOLLOW_OPERATION_in_memberOperationDefinition2739); if (failed) return def;
            pushFollow(FOLLOW_memberSelector_in_memberOperationDefinition2743);
            memberSelector44=memberSelector();
            _fsp--;
            if (failed) return def;
            pushFollow(FOLLOW_formalParameters_in_memberOperationDefinition2747);
            formalParameters46=formalParameters();
            _fsp--;
            if (failed) return def;
            pushFollow(FOLLOW_typeReference_in_memberOperationDefinition2751);
            typeReference45=typeReference();
            _fsp--;
            if (failed) return def;
            pushFollow(FOLLOW_block_in_memberOperationDefinition2754);
            block47=block();
            _fsp--;
            if (failed) return def;
            if ( backtracking==0 ) {
               def = F.at(pos(OPERATION43)).OperationDefinition(memberSelector44, typeReference45, 
              		              formalParameters46.toList(), block47); 
            }

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
    // $ANTLR end memberOperationDefinition


    // $ANTLR start memberFunctionDefinition
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:382:1: memberFunctionDefinition returns [JFXFunctionMemberDefinition def] : FUNCTION memberSelector formalParameters typeReference block ;
    public final JFXFunctionMemberDefinition memberFunctionDefinition() throws RecognitionException {
        JFXFunctionMemberDefinition def = null;

        Token FUNCTION48=null;
        JFXMemberSelector memberSelector49 = null;

        JFXType typeReference50 = null;

        ListBuffer<JCTree> formalParameters51 = null;

        JCBlock block52 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:383:4: ( FUNCTION memberSelector formalParameters typeReference block )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:383:4: FUNCTION memberSelector formalParameters typeReference block
            {
            FUNCTION48=(Token)input.LT(1);
            match(input,FUNCTION,FOLLOW_FUNCTION_in_memberFunctionDefinition2773); if (failed) return def;
            pushFollow(FOLLOW_memberSelector_in_memberFunctionDefinition2777);
            memberSelector49=memberSelector();
            _fsp--;
            if (failed) return def;
            pushFollow(FOLLOW_formalParameters_in_memberFunctionDefinition2781);
            formalParameters51=formalParameters();
            _fsp--;
            if (failed) return def;
            pushFollow(FOLLOW_typeReference_in_memberFunctionDefinition2785);
            typeReference50=typeReference();
            _fsp--;
            if (failed) return def;
            pushFollow(FOLLOW_block_in_memberFunctionDefinition2788);
            block52=block();
            _fsp--;
            if (failed) return def;
            if ( backtracking==0 ) {
               def = F.at(pos(FUNCTION48)).FunctionDefinition(memberSelector49, typeReference50, 
              		              formalParameters51.toList(), block52); 
            }

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
    // $ANTLR end memberFunctionDefinition


    // $ANTLR start functionBody
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:386:1: functionBody : ( EQ expression ( whereVarDecls )? SEMI | LBRACE ( variableDefinition | localFunctionDefinition | localOperationDefinition )* RETURN expression ( SEMI )? RBRACE );
    public final void functionBody() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:387:4: ( EQ expression ( whereVarDecls )? SEMI | LBRACE ( variableDefinition | localFunctionDefinition | localOperationDefinition )* RETURN expression ( SEMI )? RBRACE )
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==EQ) ) {
                alt14=1;
            }
            else if ( (LA14_0==LBRACE) ) {
                alt14=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("386:1: functionBody : ( EQ expression ( whereVarDecls )? SEMI | LBRACE ( variableDefinition | localFunctionDefinition | localOperationDefinition )* RETURN expression ( SEMI )? RBRACE );", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:387:4: EQ expression ( whereVarDecls )? SEMI
                    {
                    match(input,EQ,FOLLOW_EQ_in_functionBody2804); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_functionBody2808);
                    expression();
                    _fsp--;
                    if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:387:22: ( whereVarDecls )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==WHERE) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: whereVarDecls
                            {
                            pushFollow(FOLLOW_whereVarDecls_in_functionBody2812);
                            whereVarDecls();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }

                    match(input,SEMI,FOLLOW_SEMI_in_functionBody2818); if (failed) return ;

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:388:11: LBRACE ( variableDefinition | localFunctionDefinition | localOperationDefinition )* RETURN expression ( SEMI )? RBRACE
                    {
                    match(input,LBRACE,FOLLOW_LBRACE_in_functionBody2834); if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:388:20: ( variableDefinition | localFunctionDefinition | localOperationDefinition )*
                    loop12:
                    do {
                        int alt12=4;
                        switch ( input.LA(1) ) {
                        case VAR:
                            {
                            alt12=1;
                            }
                            break;
                        case FUNCTION:
                        case QUOTED_IDENTIFIER:
                        case IDENTIFIER:
                            {
                            alt12=2;
                            }
                            break;
                        case OPERATION:
                            {
                            alt12=3;
                            }
                            break;

                        }

                        switch (alt12) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:388:24: variableDefinition
                    	    {
                    	    pushFollow(FOLLOW_variableDefinition_in_functionBody2842);
                    	    variableDefinition();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;
                    	case 2 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:388:49: localFunctionDefinition
                    	    {
                    	    pushFollow(FOLLOW_localFunctionDefinition_in_functionBody2850);
                    	    localFunctionDefinition();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;
                    	case 3 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:388:79: localOperationDefinition
                    	    {
                    	    pushFollow(FOLLOW_localOperationDefinition_in_functionBody2858);
                    	    localOperationDefinition();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);

                    match(input,RETURN,FOLLOW_RETURN_in_functionBody2868); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_functionBody2872);
                    expression();
                    _fsp--;
                    if (failed) return ;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:388:134: ( SEMI )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==SEMI) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: SEMI
                            {
                            match(input,SEMI,FOLLOW_SEMI_in_functionBody2876); if (failed) return ;

                            }
                            break;

                    }

                    match(input,RBRACE,FOLLOW_RBRACE_in_functionBody2882); if (failed) return ;

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
        return ;
    }
    // $ANTLR end functionBody


    // $ANTLR start whereVarDecls
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:389:1: whereVarDecls : WHERE whereVarDecl ( COMMA whereVarDecl )* ;
    public final void whereVarDecls() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:389:17: ( WHERE whereVarDecl ( COMMA whereVarDecl )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:389:17: WHERE whereVarDecl ( COMMA whereVarDecl )*
            {
            match(input,WHERE,FOLLOW_WHERE_in_whereVarDecls2890); if (failed) return ;
            pushFollow(FOLLOW_whereVarDecl_in_whereVarDecls2894);
            whereVarDecl();
            _fsp--;
            if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:389:40: ( COMMA whereVarDecl )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==COMMA) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:389:44: COMMA whereVarDecl
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_whereVarDecls2902); if (failed) return ;
            	    pushFollow(FOLLOW_whereVarDecl_in_whereVarDecls2906);
            	    whereVarDecl();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop15;
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
        return ;
    }
    // $ANTLR end whereVarDecls


    // $ANTLR start whereVarDecl
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:390:1: whereVarDecl : ( localFunctionDefinition | name typeReference EQ expression );
    public final void whereVarDecl() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:390:16: ( localFunctionDefinition | name typeReference EQ expression )
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==FUNCTION) ) {
                alt16=1;
            }
            else if ( (LA16_0==QUOTED_IDENTIFIER||LA16_0==IDENTIFIER) ) {
                int LA16_2 = input.LA(2);

                if ( (LA16_2==LPAREN) ) {
                    alt16=1;
                }
                else if ( (LA16_2==EQ||LA16_2==COLON) ) {
                    alt16=2;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("390:1: whereVarDecl : ( localFunctionDefinition | name typeReference EQ expression );", 16, 2, input);

                    throw nvae;
                }
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("390:1: whereVarDecl : ( localFunctionDefinition | name typeReference EQ expression );", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:390:16: localFunctionDefinition
                    {
                    pushFollow(FOLLOW_localFunctionDefinition_in_whereVarDecl2920);
                    localFunctionDefinition();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:391:10: name typeReference EQ expression
                    {
                    pushFollow(FOLLOW_name_in_whereVarDecl2932);
                    name();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_typeReference_in_whereVarDecl2936);
                    typeReference();
                    _fsp--;
                    if (failed) return ;
                    match(input,EQ,FOLLOW_EQ_in_whereVarDecl2940); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_whereVarDecl2944);
                    expression();
                    _fsp--;
                    if (failed) return ;

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
        return ;
    }
    // $ANTLR end whereVarDecl


    // $ANTLR start variableDefinition
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:392:1: variableDefinition : VAR name typeReference EQ expression SEMI ;
    public final void variableDefinition() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:392:22: ( VAR name typeReference EQ expression SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:392:22: VAR name typeReference EQ expression SEMI
            {
            match(input,VAR,FOLLOW_VAR_in_variableDefinition2952); if (failed) return ;
            pushFollow(FOLLOW_name_in_variableDefinition2956);
            name();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_typeReference_in_variableDefinition2960);
            typeReference();
            _fsp--;
            if (failed) return ;
            match(input,EQ,FOLLOW_EQ_in_variableDefinition2963); if (failed) return ;
            pushFollow(FOLLOW_expression_in_variableDefinition2967);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,SEMI,FOLLOW_SEMI_in_variableDefinition2971); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end variableDefinition


    // $ANTLR start changeRule
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:393:1: changeRule returns [JFXAbstractTriggerOn value] : ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );
    public final JFXAbstractTriggerOn changeRule() throws RecognitionException {
        JFXAbstractTriggerOn value = null;

        Token LPAREN53=null;
        Token LPAREN56=null;
        Token EQ60=null;
        Token LPAREN64=null;
        JCIdent id1 = null;

        JCIdent id2 = null;

        JCIdent identifier54 = null;

        JCBlock block55 = null;

        JFXMemberSelector memberSelector57 = null;

        JCIdent identifier58 = null;

        JCBlock block59 = null;

        JFXMemberSelector memberSelector61 = null;

        JCIdent identifier62 = null;

        JCBlock block63 = null;

        JFXMemberSelector memberSelector65 = null;

        JCBlock block66 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:394:4: ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block )
            int alt17=7;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==LPAREN) ) {
                switch ( input.LA(2) ) {
                case INSERT:
                    {
                    alt17=5;
                    }
                    break;
                case DELETE:
                    {
                    int LA17_4 = input.LA(3);

                    if ( (LA17_4==QUOTED_IDENTIFIER||LA17_4==IDENTIFIER) ) {
                        int LA17_7 = input.LA(4);

                        if ( (LA17_7==FROM) ) {
                            alt17=6;
                        }
                        else if ( (LA17_7==DOT) ) {
                            alt17=7;
                        }
                        else {
                            if (backtracking>0) {failed=true; return value;}
                            NoViableAltException nvae =
                                new NoViableAltException("393:1: changeRule returns [JFXAbstractTriggerOn value] : ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 7, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (backtracking>0) {failed=true; return value;}
                        NoViableAltException nvae =
                            new NoViableAltException("393:1: changeRule returns [JFXAbstractTriggerOn value] : ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 4, input);

                        throw nvae;
                    }
                    }
                    break;
                case NEW:
                    {
                    alt17=1;
                    }
                    break;
                case QUOTED_IDENTIFIER:
                case IDENTIFIER:
                    {
                    int LA17_6 = input.LA(3);

                    if ( (LA17_6==DOT) ) {
                        int LA17_8 = input.LA(4);

                        if ( (LA17_8==QUOTED_IDENTIFIER||LA17_8==IDENTIFIER) ) {
                            int LA17_11 = input.LA(5);

                            if ( (LA17_11==EQ) ) {
                                alt17=2;
                            }
                            else if ( (LA17_11==LBRACKET) ) {
                                alt17=4;
                            }
                            else {
                                if (backtracking>0) {failed=true; return value;}
                                NoViableAltException nvae =
                                    new NoViableAltException("393:1: changeRule returns [JFXAbstractTriggerOn value] : ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 11, input);

                                throw nvae;
                            }
                        }
                        else {
                            if (backtracking>0) {failed=true; return value;}
                            NoViableAltException nvae =
                                new NoViableAltException("393:1: changeRule returns [JFXAbstractTriggerOn value] : ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 8, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (backtracking>0) {failed=true; return value;}
                        NoViableAltException nvae =
                            new NoViableAltException("393:1: changeRule returns [JFXAbstractTriggerOn value] : ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 6, input);

                        throw nvae;
                    }
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("393:1: changeRule returns [JFXAbstractTriggerOn value] : ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA17_0==QUOTED_IDENTIFIER||LA17_0==IDENTIFIER) ) {
                alt17=3;
            }
            else {
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("393:1: changeRule returns [JFXAbstractTriggerOn value] : ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:394:4: LPAREN NEW identifier RPAREN block
                    {
                    LPAREN53=(Token)input.LT(1);
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule2985); if (failed) return value;
                    match(input,NEW,FOLLOW_NEW_in_changeRule2989); if (failed) return value;
                    pushFollow(FOLLOW_identifier_in_changeRule2993);
                    identifier54=identifier();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule2996); if (failed) return value;
                    pushFollow(FOLLOW_block_in_changeRule2999);
                    block55=block();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(LPAREN53)).TriggerOnNew(identifier54, null, block55); 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:396:4: LPAREN memberSelector EQ identifier RPAREN block
                    {
                    LPAREN56=(Token)input.LT(1);
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule3015); if (failed) return value;
                    pushFollow(FOLLOW_memberSelector_in_changeRule3019);
                    memberSelector57=memberSelector();
                    _fsp--;
                    if (failed) return value;
                    match(input,EQ,FOLLOW_EQ_in_changeRule3022); if (failed) return value;
                    pushFollow(FOLLOW_identifier_in_changeRule3024);
                    identifier58=identifier();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule3028); if (failed) return value;
                    pushFollow(FOLLOW_block_in_changeRule3031);
                    block59=block();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(LPAREN56)).TriggerOnReplace(memberSelector57, identifier58, block59); 
                    }

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:398:4: memberSelector EQ identifier block
                    {
                    pushFollow(FOLLOW_memberSelector_in_changeRule3045);
                    memberSelector61=memberSelector();
                    _fsp--;
                    if (failed) return value;
                    EQ60=(Token)input.LT(1);
                    match(input,EQ,FOLLOW_EQ_in_changeRule3048); if (failed) return value;
                    pushFollow(FOLLOW_identifier_in_changeRule3050);
                    identifier62=identifier();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_block_in_changeRule3052);
                    block63=block();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(EQ60)).TriggerOnReplace(memberSelector61, identifier62, block63); 
                    }

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:400:4: LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block
                    {
                    LPAREN64=(Token)input.LT(1);
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule3066); if (failed) return value;
                    pushFollow(FOLLOW_memberSelector_in_changeRule3070);
                    memberSelector65=memberSelector();
                    _fsp--;
                    if (failed) return value;
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_changeRule3074); if (failed) return value;
                    pushFollow(FOLLOW_identifier_in_changeRule3080);
                    id1=identifier();
                    _fsp--;
                    if (failed) return value;
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_changeRule3084); if (failed) return value;
                    match(input,EQ,FOLLOW_EQ_in_changeRule3088); if (failed) return value;
                    pushFollow(FOLLOW_identifier_in_changeRule3092);
                    id2=identifier();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule3096); if (failed) return value;
                    pushFollow(FOLLOW_block_in_changeRule3099);
                    block66=block();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(LPAREN64)).TriggerOnReplaceElement(memberSelector65, id1, id2, block66); 
                    }

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:402:4: LPAREN INSERT identifier INTO memberSelector RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule3113); if (failed) return value;
                    match(input,INSERT,FOLLOW_INSERT_in_changeRule3117); if (failed) return value;
                    pushFollow(FOLLOW_identifier_in_changeRule3121);
                    identifier();
                    _fsp--;
                    if (failed) return value;
                    match(input,INTO,FOLLOW_INTO_in_changeRule3125); if (failed) return value;
                    pushFollow(FOLLOW_memberSelector_in_changeRule3129);
                    memberSelector();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule3133); if (failed) return value;
                    pushFollow(FOLLOW_block_in_changeRule3135);
                    block();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 6 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:403:4: LPAREN DELETE identifier FROM memberSelector RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule3142); if (failed) return value;
                    match(input,DELETE,FOLLOW_DELETE_in_changeRule3146); if (failed) return value;
                    pushFollow(FOLLOW_identifier_in_changeRule3150);
                    identifier();
                    _fsp--;
                    if (failed) return value;
                    match(input,FROM,FOLLOW_FROM_in_changeRule3154); if (failed) return value;
                    pushFollow(FOLLOW_memberSelector_in_changeRule3158);
                    memberSelector();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule3162); if (failed) return value;
                    pushFollow(FOLLOW_block_in_changeRule3164);
                    block();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 7 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:404:4: LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule3171); if (failed) return value;
                    match(input,DELETE,FOLLOW_DELETE_in_changeRule3175); if (failed) return value;
                    pushFollow(FOLLOW_memberSelector_in_changeRule3178);
                    memberSelector();
                    _fsp--;
                    if (failed) return value;
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_changeRule3182); if (failed) return value;
                    pushFollow(FOLLOW_identifier_in_changeRule3186);
                    identifier();
                    _fsp--;
                    if (failed) return value;
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_changeRule3190); if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule3194); if (failed) return value;
                    pushFollow(FOLLOW_block_in_changeRule3196);
                    block();
                    _fsp--;
                    if (failed) return value;

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
    // $ANTLR end changeRule


    // $ANTLR start modifierFlags
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:406:1: modifierFlags returns [JCModifiers mods] : (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? ;
    public final JCModifiers modifierFlags() throws RecognitionException {
        JCModifiers mods = null;

        long om1 = 0;

        long am1 = 0;

        long am2 = 0;

        long om2 = 0;


         long flags = 0; 
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:408:4: ( (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:408:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:408:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
            int alt20=3;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==ABSTRACT||LA20_0==READONLY) ) {
                alt20=1;
            }
            else if ( ((LA20_0>=PRIVATE && LA20_0<=PUBLIC)) ) {
                alt20=2;
            }
            switch (alt20) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:408:6: om1= otherModifier (am1= accessModifier )?
                    {
                    pushFollow(FOLLOW_otherModifier_in_modifierFlags3219);
                    om1=otherModifier();
                    _fsp--;
                    if (failed) return mods;
                    if ( backtracking==0 ) {
                       flags |= om1; 
                    }
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:409:3: (am1= accessModifier )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( ((LA18_0>=PRIVATE && LA18_0<=PUBLIC)) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:409:5: am1= accessModifier
                            {
                            pushFollow(FOLLOW_accessModifier_in_modifierFlags3232);
                            am1=accessModifier();
                            _fsp--;
                            if (failed) return mods;
                            if ( backtracking==0 ) {
                               flags |= am1; 
                            }

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:410:6: am2= accessModifier (om2= otherModifier )?
                    {
                    pushFollow(FOLLOW_accessModifier_in_modifierFlags3254);
                    am2=accessModifier();
                    _fsp--;
                    if (failed) return mods;
                    if ( backtracking==0 ) {
                       flags |= am2; 
                    }
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:3: (om2= otherModifier )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==ABSTRACT||LA19_0==READONLY) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:5: om2= otherModifier
                            {
                            pushFollow(FOLLOW_otherModifier_in_modifierFlags3267);
                            om2=otherModifier();
                            _fsp--;
                            if (failed) return mods;
                            if ( backtracking==0 ) {
                               flags |= om2; 
                            }

                            }
                            break;

                    }


                    }
                    break;

            }

            if ( backtracking==0 ) {
               mods = F.Modifiers(flags); 
            }

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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:414:1: accessModifier returns [long flags = 0] : ( PUBLIC | PRIVATE | PROTECTED ) ;
    public final long accessModifier() throws RecognitionException {
        long flags =  0;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:4: ( ( PUBLIC | PRIVATE | PROTECTED ) )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:4: ( PUBLIC | PRIVATE | PROTECTED )
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:4: ( PUBLIC | PRIVATE | PROTECTED )
            int alt21=3;
            switch ( input.LA(1) ) {
            case PUBLIC:
                {
                alt21=1;
                }
                break;
            case PRIVATE:
                {
                alt21=2;
                }
                break;
            case PROTECTED:
                {
                alt21=3;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return flags;}
                NoViableAltException nvae =
                    new NoViableAltException("415:4: ( PUBLIC | PRIVATE | PROTECTED )", 21, 0, input);

                throw nvae;
            }

            switch (alt21) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:5: PUBLIC
                    {
                    match(input,PUBLIC,FOLLOW_PUBLIC_in_accessModifier3315); if (failed) return flags;
                    if ( backtracking==0 ) {
                       flags |= Flags.PUBLIC; 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:416:5: PRIVATE
                    {
                    match(input,PRIVATE,FOLLOW_PRIVATE_in_accessModifier3332); if (failed) return flags;
                    if ( backtracking==0 ) {
                       flags |= Flags.PUBLIC; 
                    }

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:417:5: PROTECTED
                    {
                    match(input,PROTECTED,FOLLOW_PROTECTED_in_accessModifier3348); if (failed) return flags;
                    if ( backtracking==0 ) {
                       flags |= Flags.PROTECTED; 
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
        return flags;
    }
    // $ANTLR end accessModifier


    // $ANTLR start otherModifier
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:418:1: otherModifier returns [long flags = 0] : ( ABSTRACT | READONLY ) ;
    public final long otherModifier() throws RecognitionException {
        long flags =  0;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:419:4: ( ( ABSTRACT | READONLY ) )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:419:4: ( ABSTRACT | READONLY )
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:419:4: ( ABSTRACT | READONLY )
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==ABSTRACT) ) {
                alt22=1;
            }
            else if ( (LA22_0==READONLY) ) {
                alt22=2;
            }
            else {
                if (backtracking>0) {failed=true; return flags;}
                NoViableAltException nvae =
                    new NoViableAltException("419:4: ( ABSTRACT | READONLY )", 22, 0, input);

                throw nvae;
            }
            switch (alt22) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:419:5: ABSTRACT
                    {
                    match(input,ABSTRACT,FOLLOW_ABSTRACT_in_otherModifier3372); if (failed) return flags;
                    if ( backtracking==0 ) {
                       flags |= Flags.ABSTRACT; 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:420:5: READONLY
                    {
                    match(input,READONLY,FOLLOW_READONLY_in_otherModifier3387); if (failed) return flags;
                    if ( backtracking==0 ) {
                       flags |= Flags.FINAL; 
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
        return flags;
    }
    // $ANTLR end otherModifier


    // $ANTLR start memberSelector
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:421:1: memberSelector returns [JFXMemberSelector value] : name1= name DOT name2= name ;
    public final JFXMemberSelector memberSelector() throws RecognitionException {
        JFXMemberSelector value = null;

        name_return name1 = null;

        name_return name2 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:422:4: (name1= name DOT name2= name )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:422:4: name1= name DOT name2= name
            {
            pushFollow(FOLLOW_name_in_memberSelector3413);
            name1=name();
            _fsp--;
            if (failed) return value;
            match(input,DOT,FOLLOW_DOT_in_memberSelector3417); if (failed) return value;
            pushFollow(FOLLOW_name_in_memberSelector3423);
            name2=name();
            _fsp--;
            if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(name1.pos).MemberSelector(name1.value, name2.value); 
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
    // $ANTLR end memberSelector


    // $ANTLR start formalParameters
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:423:1: formalParameters returns [ListBuffer<JCTree> params = new ListBuffer<JCTree>()] : LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN ;
    public final ListBuffer<JCTree> formalParameters() throws RecognitionException {
        ListBuffer<JCTree> params =  new ListBuffer<JCTree>();

        JFXVar fp0 = null;

        JFXVar fpn = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:424:4: ( LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:424:4: LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_formalParameters3439); if (failed) return params;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:424:13: (fp0= formalParameter ( COMMA fpn= formalParameter )* )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==QUOTED_IDENTIFIER||LA24_0==IDENTIFIER) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:424:15: fp0= formalParameter ( COMMA fpn= formalParameter )*
                    {
                    pushFollow(FOLLOW_formalParameter_in_formalParameters3447);
                    fp0=formalParameter();
                    _fsp--;
                    if (failed) return params;
                    if ( backtracking==0 ) {
                       params.append(fp0); 
                    }
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:425:13: ( COMMA fpn= formalParameter )*
                    loop23:
                    do {
                        int alt23=2;
                        int LA23_0 = input.LA(1);

                        if ( (LA23_0==COMMA) ) {
                            alt23=1;
                        }


                        switch (alt23) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:425:15: COMMA fpn= formalParameter
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_formalParameters3466); if (failed) return params;
                    	    pushFollow(FOLLOW_formalParameter_in_formalParameters3472);
                    	    fpn=formalParameter();
                    	    _fsp--;
                    	    if (failed) return params;
                    	    if ( backtracking==0 ) {
                    	       params.append(fpn); 
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop23;
                        }
                    } while (true);


                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_formalParameters3483); if (failed) return params;

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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:426:1: formalParameter returns [JFXVar var] : name typeReference ;
    public final JFXVar formalParameter() throws RecognitionException {
        JFXVar var = null;

        name_return name67 = null;

        JFXType typeReference68 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:427:4: ( name typeReference )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:427:4: name typeReference
            {
            pushFollow(FOLLOW_name_in_formalParameter3496);
            name67=name();
            _fsp--;
            if (failed) return var;
            pushFollow(FOLLOW_typeReference_in_formalParameter3498);
            typeReference68=typeReference();
            _fsp--;
            if (failed) return var;
            if ( backtracking==0 ) {
               var = F.at(name67.pos).Var(name67.value, typeReference68); 
            }

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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:428:1: block returns [JCBlock value] : LBRACE statements RBRACE ;
    public final JCBlock block() throws RecognitionException {
        JCBlock value = null;

        Token LBRACE69=null;
        ListBuffer<JCStatement> statements70 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:429:4: ( LBRACE statements RBRACE )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:429:4: LBRACE statements RBRACE
            {
            LBRACE69=(Token)input.LT(1);
            match(input,LBRACE,FOLLOW_LBRACE_in_block3515); if (failed) return value;
            pushFollow(FOLLOW_statements_in_block3519);
            statements70=statements();
            _fsp--;
            if (failed) return value;
            match(input,RBRACE,FOLLOW_RBRACE_in_block3523); if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(pos(LBRACE69)).Block(0L, statements70.toList()); 
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
    // $ANTLR end block


    // $ANTLR start statements
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:431:1: statements returns [ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>()] : ( statement )* ;
    public final ListBuffer<JCStatement> statements() throws RecognitionException {
        ListBuffer<JCStatement> stats =  new ListBuffer<JCStatement>();

        JCStatement statement71 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:4: ( ( statement )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:4: ( statement )*
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:4: ( statement )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( ((LA25_0>=POUND && LA25_0<=TYPEOF)||LA25_0==BREAK||LA25_0==DELETE||LA25_0==DO||(LA25_0>=RETURN && LA25_0<=VAR)||LA25_0==TRIGGER||LA25_0==INSERT||LA25_0==IF||(LA25_0>=THIS && LA25_0<=UNITINTERVAL)||(LA25_0>=WHILE && LA25_0<=CONTINUE)||LA25_0==TRY||LA25_0==FOREACH||(LA25_0>=NOT && LA25_0<=NEW)||(LA25_0>=OPERATION && LA25_0<=FUNCTION)||(LA25_0>=INDEXOF && LA25_0<=SUPER)||(LA25_0>=SIZEOF && LA25_0<=REVERSE)||LA25_0==LPAREN||LA25_0==LBRACKET||LA25_0==DOT||(LA25_0>=PLUSPLUS && LA25_0<=SUBSUB)||(LA25_0>=QUES && LA25_0<=STRING_LITERAL)||LA25_0==QUOTE_LBRACE_STRING_LITERAL||(LA25_0>=QUOTED_IDENTIFIER && LA25_0<=INTEGER_LITERAL)||LA25_0==FLOATING_POINT_LITERAL||LA25_0==IDENTIFIER) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:5: statement
            	    {
            	    pushFollow(FOLLOW_statement_in_statements3541);
            	    statement71=statement();
            	    _fsp--;
            	    if (failed) return stats;
            	    if ( backtracking==0 ) {
            	       stats.append(statement71); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop25;
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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:433:1: statement returns [JCStatement value] : ( statementExcept | localTriggerStatement );
    public final JCStatement statement() throws RecognitionException {
        JCStatement value = null;

        JCStatement statementExcept72 = null;

        JCStatement localTriggerStatement73 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:434:10: ( statementExcept | localTriggerStatement )
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( ((LA26_0>=POUND && LA26_0<=TYPEOF)||LA26_0==BREAK||LA26_0==DELETE||LA26_0==DO||(LA26_0>=RETURN && LA26_0<=VAR)||LA26_0==INSERT||LA26_0==IF||(LA26_0>=THIS && LA26_0<=UNITINTERVAL)||(LA26_0>=WHILE && LA26_0<=CONTINUE)||LA26_0==TRY||LA26_0==FOREACH||(LA26_0>=NOT && LA26_0<=NEW)||(LA26_0>=OPERATION && LA26_0<=FUNCTION)||(LA26_0>=INDEXOF && LA26_0<=SUPER)||(LA26_0>=SIZEOF && LA26_0<=REVERSE)||LA26_0==LPAREN||LA26_0==LBRACKET||LA26_0==DOT||(LA26_0>=PLUSPLUS && LA26_0<=SUBSUB)||(LA26_0>=QUES && LA26_0<=STRING_LITERAL)||LA26_0==QUOTE_LBRACE_STRING_LITERAL||(LA26_0>=QUOTED_IDENTIFIER && LA26_0<=INTEGER_LITERAL)||LA26_0==FLOATING_POINT_LITERAL||LA26_0==IDENTIFIER) ) {
                alt26=1;
            }
            else if ( (LA26_0==TRIGGER) ) {
                alt26=2;
            }
            else {
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("433:1: statement returns [JCStatement value] : ( statementExcept | localTriggerStatement );", 26, 0, input);

                throw nvae;
            }
            switch (alt26) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:434:10: statementExcept
                    {
                    pushFollow(FOLLOW_statementExcept_in_statement3592);
                    statementExcept72=statementExcept();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = statementExcept72; 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:435:10: localTriggerStatement
                    {
                    pushFollow(FOLLOW_localTriggerStatement_in_statement3608);
                    localTriggerStatement73=localTriggerStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = localTriggerStatement73; 
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
        return value;
    }
    // $ANTLR end statement


    // $ANTLR start statementExcept
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:436:1: statementExcept returns [JCStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );
    public final JCStatement statementExcept() throws RecognitionException {
        JCStatement value = null;

        Token WHILE77=null;
        Token SEMI83=null;
        Token BREAK85=null;
        Token CONTINUE86=null;
        JCStatement variableDeclaration74 = null;

        JCStatement localFunctionDefinition75 = null;

        JCStatement localOperationDefinition76 = null;

        JCExpression expression78 = null;

        JCBlock block79 = null;

        JCStatement ifStatement80 = null;

        JCStatement insertStatement81 = null;

        JCStatement deleteStatement82 = null;

        JCExpression expression84 = null;

        JCStatement throwStatement87 = null;

        JCStatement returnStatement88 = null;

        JCStatement forAlphaStatement89 = null;

        JCStatement forJoinStatement90 = null;

        JCStatement tryStatement91 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:437:4: ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement )
            int alt27=17;
            switch ( input.LA(1) ) {
            case VAR:
                {
                alt27=1;
                }
                break;
            case FUNCTION:
                {
                int LA27_2 = input.LA(2);

                if ( (synpred45()) ) {
                    alt27=2;
                }
                else if ( (synpred53()) ) {
                    alt27=10;
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("436:1: statementExcept returns [JCStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 2, input);

                    throw nvae;
                }
                }
                break;
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                int LA27_3 = input.LA(2);

                if ( (synpred45()) ) {
                    alt27=2;
                }
                else if ( (synpred53()) ) {
                    alt27=10;
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("436:1: statementExcept returns [JCStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 3, input);

                    throw nvae;
                }
                }
                break;
            case OPERATION:
                {
                int LA27_4 = input.LA(2);

                if ( (synpred46()) ) {
                    alt27=3;
                }
                else if ( (synpred53()) ) {
                    alt27=10;
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("436:1: statementExcept returns [JCStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 4, input);

                    throw nvae;
                }
                }
                break;
            case DO:
                {
                int LA27_5 = input.LA(2);

                if ( (synpred47()) ) {
                    alt27=4;
                }
                else if ( (synpred48()) ) {
                    alt27=5;
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("436:1: statementExcept returns [JCStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 5, input);

                    throw nvae;
                }
                }
                break;
            case WHILE:
                {
                alt27=6;
                }
                break;
            case IF:
                {
                int LA27_7 = input.LA(2);

                if ( (synpred50()) ) {
                    alt27=7;
                }
                else if ( (synpred53()) ) {
                    alt27=10;
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("436:1: statementExcept returns [JCStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 7, input);

                    throw nvae;
                }
                }
                break;
            case INSERT:
                {
                alt27=8;
                }
                break;
            case DELETE:
                {
                alt27=9;
                }
                break;
            case POUND:
            case TYPEOF:
            case THIS:
            case NULL:
            case TRUE:
            case FALSE:
            case UNITINTERVAL:
            case FOREACH:
            case NOT:
            case NEW:
            case INDEXOF:
            case SELECT:
            case SUPER:
            case SIZEOF:
            case REVERSE:
            case LPAREN:
            case LBRACKET:
            case DOT:
            case PLUSPLUS:
            case SUB:
            case SUBSUB:
            case QUES:
            case STRING_LITERAL:
            case QUOTE_LBRACE_STRING_LITERAL:
            case INTEGER_LITERAL:
            case FLOATING_POINT_LITERAL:
                {
                alt27=10;
                }
                break;
            case BREAK:
                {
                alt27=11;
                }
                break;
            case CONTINUE:
                {
                alt27=12;
                }
                break;
            case THROW:
                {
                alt27=13;
                }
                break;
            case RETURN:
                {
                alt27=14;
                }
                break;
            case FOR:
                {
                int LA27_40 = input.LA(2);

                if ( (synpred58()) ) {
                    alt27=15;
                }
                else if ( (synpred59()) ) {
                    alt27=16;
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("436:1: statementExcept returns [JCStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 40, input);

                    throw nvae;
                }
                }
                break;
            case TRY:
                {
                alt27=17;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("436:1: statementExcept returns [JCStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 0, input);

                throw nvae;
            }

            switch (alt27) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:437:4: variableDeclaration
                    {
                    pushFollow(FOLLOW_variableDeclaration_in_statementExcept3626);
                    variableDeclaration74=variableDeclaration();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = variableDeclaration74; 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:438:4: localFunctionDefinition
                    {
                    pushFollow(FOLLOW_localFunctionDefinition_in_statementExcept3636);
                    localFunctionDefinition75=localFunctionDefinition();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = localFunctionDefinition75; 
                    }

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:439:4: localOperationDefinition
                    {
                    pushFollow(FOLLOW_localOperationDefinition_in_statementExcept3644);
                    localOperationDefinition76=localOperationDefinition();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = localOperationDefinition76; 
                    }

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:440:10: backgroundStatement
                    {
                    pushFollow(FOLLOW_backgroundStatement_in_statementExcept3658);
                    backgroundStatement();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:441:10: laterStatement
                    {
                    pushFollow(FOLLOW_laterStatement_in_statementExcept3673);
                    laterStatement();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 6 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:442:10: WHILE LPAREN expression RPAREN block
                    {
                    WHILE77=(Token)input.LT(1);
                    match(input,WHILE,FOLLOW_WHILE_in_statementExcept3688); if (failed) return value;
                    match(input,LPAREN,FOLLOW_LPAREN_in_statementExcept3690); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_statementExcept3692);
                    expression78=expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_statementExcept3694); if (failed) return value;
                    pushFollow(FOLLOW_block_in_statementExcept3696);
                    block79=block();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(WHILE77)).WhileLoop(expression78, block79); 
                    }

                    }
                    break;
                case 7 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:443:10: ifStatement
                    {
                    pushFollow(FOLLOW_ifStatement_in_statementExcept3709);
                    ifStatement80=ifStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = ifStatement80; 
                    }

                    }
                    break;
                case 8 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:444:10: insertStatement
                    {
                    pushFollow(FOLLOW_insertStatement_in_statementExcept3726);
                    insertStatement81=insertStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = insertStatement81; 
                    }

                    }
                    break;
                case 9 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:445:10: deleteStatement
                    {
                    pushFollow(FOLLOW_deleteStatement_in_statementExcept3742);
                    deleteStatement82=deleteStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = deleteStatement82; 
                    }

                    }
                    break;
                case 10 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:446:4: expression SEMI
                    {
                    pushFollow(FOLLOW_expression_in_statementExcept3752);
                    expression84=expression();
                    _fsp--;
                    if (failed) return value;
                    SEMI83=(Token)input.LT(1);
                    match(input,SEMI,FOLLOW_SEMI_in_statementExcept3756); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(SEMI83)).Exec(expression84); 
                    }

                    }
                    break;
                case 11 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:447:4: BREAK SEMI
                    {
                    BREAK85=(Token)input.LT(1);
                    match(input,BREAK,FOLLOW_BREAK_in_statementExcept3766); if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_statementExcept3770); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(BREAK85)).Break(null); 
                    }

                    }
                    break;
                case 12 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:448:4: CONTINUE SEMI
                    {
                    CONTINUE86=(Token)input.LT(1);
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_statementExcept3781); if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_statementExcept3785); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(CONTINUE86)).Continue(null); 
                    }

                    }
                    break;
                case 13 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:449:10: throwStatement
                    {
                    pushFollow(FOLLOW_throwStatement_in_statementExcept3801);
                    throwStatement87=throwStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = throwStatement87; 
                    }

                    }
                    break;
                case 14 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:450:10: returnStatement
                    {
                    pushFollow(FOLLOW_returnStatement_in_statementExcept3817);
                    returnStatement88=returnStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = returnStatement88; 
                    }

                    }
                    break;
                case 15 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:451:10: forAlphaStatement
                    {
                    pushFollow(FOLLOW_forAlphaStatement_in_statementExcept3833);
                    forAlphaStatement89=forAlphaStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = forAlphaStatement89; 
                    }

                    }
                    break;
                case 16 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:452:10: forJoinStatement
                    {
                    pushFollow(FOLLOW_forJoinStatement_in_statementExcept3849);
                    forJoinStatement90=forJoinStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = forJoinStatement90; 
                    }

                    }
                    break;
                case 17 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:453:10: tryStatement
                    {
                    pushFollow(FOLLOW_tryStatement_in_statementExcept3865);
                    tryStatement91=tryStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = tryStatement91; 
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
        return value;
    }
    // $ANTLR end statementExcept


    // $ANTLR start assertStatement
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:454:1: assertStatement returns [JCStatement value = null] : ASSERT expression ( COLON expression )? SEMI ;
    public final JCStatement assertStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:455:4: ( ASSERT expression ( COLON expression )? SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:455:4: ASSERT expression ( COLON expression )? SEMI
            {
            match(input,ASSERT,FOLLOW_ASSERT_in_assertStatement3884); if (failed) return value;
            pushFollow(FOLLOW_expression_in_assertStatement3888);
            expression();
            _fsp--;
            if (failed) return value;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:455:26: ( COLON expression )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==COLON) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:455:30: COLON expression
                    {
                    match(input,COLON,FOLLOW_COLON_in_assertStatement3896); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_assertStatement3900);
                    expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_assertStatement3910); if (failed) return value;

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


    // $ANTLR start localOperationDefinition
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:456:1: localOperationDefinition returns [JCStatement value] : OPERATION name formalParameters typeReference block ;
    public final JCStatement localOperationDefinition() throws RecognitionException {
        JCStatement value = null;

        Token OPERATION92=null;
        name_return name93 = null;

        JFXType typeReference94 = null;

        ListBuffer<JCTree> formalParameters95 = null;

        JCBlock block96 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:457:4: ( OPERATION name formalParameters typeReference block )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:457:4: OPERATION name formalParameters typeReference block
            {
            OPERATION92=(Token)input.LT(1);
            match(input,OPERATION,FOLLOW_OPERATION_in_localOperationDefinition3925); if (failed) return value;
            pushFollow(FOLLOW_name_in_localOperationDefinition3929);
            name93=name();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_formalParameters_in_localOperationDefinition3933);
            formalParameters95=formalParameters();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_typeReference_in_localOperationDefinition3937);
            typeReference94=typeReference();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_block_in_localOperationDefinition3940);
            block96=block();
            _fsp--;
            if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(pos(OPERATION92)).OperationLocalDefinition(name93.value, typeReference94, 
              									formalParameters95.toList(), block96); 
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
    // $ANTLR end localOperationDefinition


    // $ANTLR start localFunctionDefinition
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:460:1: localFunctionDefinition returns [JCStatement value] : ( FUNCTION )? name formalParameters typeReference block ;
    public final JCStatement localFunctionDefinition() throws RecognitionException {
        JCStatement value = null;

        name_return name97 = null;

        JFXType typeReference98 = null;

        ListBuffer<JCTree> formalParameters99 = null;

        JCBlock block100 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:461:4: ( ( FUNCTION )? name formalParameters typeReference block )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:461:4: ( FUNCTION )? name formalParameters typeReference block
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:461:4: ( FUNCTION )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==FUNCTION) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: FUNCTION
                    {
                    match(input,FUNCTION,FOLLOW_FUNCTION_in_localFunctionDefinition3960); if (failed) return value;

                    }
                    break;

            }

            pushFollow(FOLLOW_name_in_localFunctionDefinition3966);
            name97=name();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_formalParameters_in_localFunctionDefinition3970);
            formalParameters99=formalParameters();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_typeReference_in_localFunctionDefinition3974);
            typeReference98=typeReference();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_block_in_localFunctionDefinition3977);
            block100=block();
            _fsp--;
            if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(name97.pos).FunctionLocalDefinition(name97.value, typeReference98, 
              									formalParameters99.toList(), block100); 
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
    // $ANTLR end localFunctionDefinition


    // $ANTLR start variableDeclaration
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:464:1: variableDeclaration returns [JCStatement value] : VAR name typeReference ( EQ bindOpt expression SEMI | SEMI ) ;
    public final JCStatement variableDeclaration() throws RecognitionException {
        JCStatement value = null;

        Token VAR101=null;
        name_return name102 = null;

        JFXType typeReference103 = null;

        JCExpression expression104 = null;

        JavafxBindStatus bindOpt105 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:465:4: ( VAR name typeReference ( EQ bindOpt expression SEMI | SEMI ) )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:465:4: VAR name typeReference ( EQ bindOpt expression SEMI | SEMI )
            {
            VAR101=(Token)input.LT(1);
            match(input,VAR,FOLLOW_VAR_in_variableDeclaration3997); if (failed) return value;
            pushFollow(FOLLOW_name_in_variableDeclaration4000);
            name102=name();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_typeReference_in_variableDeclaration4003);
            typeReference103=typeReference();
            _fsp--;
            if (failed) return value;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:466:6: ( EQ bindOpt expression SEMI | SEMI )
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==EQ) ) {
                alt30=1;
            }
            else if ( (LA30_0==SEMI) ) {
                alt30=2;
            }
            else {
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("466:6: ( EQ bindOpt expression SEMI | SEMI )", 30, 0, input);

                throw nvae;
            }
            switch (alt30) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:466:8: EQ bindOpt expression SEMI
                    {
                    match(input,EQ,FOLLOW_EQ_in_variableDeclaration4014); if (failed) return value;
                    pushFollow(FOLLOW_bindOpt_in_variableDeclaration4016);
                    bindOpt105=bindOpt();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_expression_in_variableDeclaration4019);
                    expression104=expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_variableDeclaration4021); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(VAR101)).VarInit(name102.value, typeReference103, 
                      	    							expression104, bindOpt105); 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:468:8: SEMI
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_variableDeclaration4032); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(VAR101)).VarStatement(name102.value, typeReference103); 
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
        return value;
    }
    // $ANTLR end variableDeclaration


    // $ANTLR start bindOpt
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:471:1: bindOpt returns [JavafxBindStatus status = UNBOUND] : ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? ;
    public final JavafxBindStatus bindOpt() throws RecognitionException {
        JavafxBindStatus status =  UNBOUND;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:472:4: ( ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:472:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:472:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
            int alt34=4;
            switch ( input.LA(1) ) {
                case BIND:
                    {
                    alt34=1;
                    }
                    break;
                case STAYS:
                    {
                    alt34=2;
                    }
                    break;
                case TIE:
                    {
                    alt34=3;
                    }
                    break;
            }

            switch (alt34) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:472:6: BIND ( LAZY )?
                    {
                    match(input,BIND,FOLLOW_BIND_in_bindOpt4069); if (failed) return status;
                    if ( backtracking==0 ) {
                       status = UNIDIBIND; 
                    }
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:473:8: ( LAZY )?
                    int alt31=2;
                    int LA31_0 = input.LA(1);

                    if ( (LA31_0==LAZY) ) {
                        alt31=1;
                    }
                    switch (alt31) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:473:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt4085); if (failed) return status;
                            if ( backtracking==0 ) {
                               status = LAZY_UNIDIBIND; 
                            }

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:474:6: STAYS ( LAZY )?
                    {
                    match(input,STAYS,FOLLOW_STAYS_in_bindOpt4100); if (failed) return status;
                    if ( backtracking==0 ) {
                       status = UNIDIBIND; 
                    }
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:475:8: ( LAZY )?
                    int alt32=2;
                    int LA32_0 = input.LA(1);

                    if ( (LA32_0==LAZY) ) {
                        alt32=1;
                    }
                    switch (alt32) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:475:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt4116); if (failed) return status;
                            if ( backtracking==0 ) {
                               status = LAZY_UNIDIBIND; 
                            }

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:476:6: TIE ( LAZY )?
                    {
                    match(input,TIE,FOLLOW_TIE_in_bindOpt4131); if (failed) return status;
                    if ( backtracking==0 ) {
                       status = BIDIBIND; 
                    }
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:477:8: ( LAZY )?
                    int alt33=2;
                    int LA33_0 = input.LA(1);

                    if ( (LA33_0==LAZY) ) {
                        alt33=1;
                    }
                    switch (alt33) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:477:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt4147); if (failed) return status;
                            if ( backtracking==0 ) {
                               status = LAZY_BIDIBIND; 
                            }

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


    // $ANTLR start backgroundStatement
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:479:1: backgroundStatement : DO block ;
    public final void backgroundStatement() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:480:4: ( DO block )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:480:4: DO block
            {
            match(input,DO,FOLLOW_DO_in_backgroundStatement4173); if (failed) return ;
            pushFollow(FOLLOW_block_in_backgroundStatement4177);
            block();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end backgroundStatement


    // $ANTLR start laterStatement
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:481:1: laterStatement : DO LATER block ;
    public final void laterStatement() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:482:4: ( DO LATER block )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:482:4: DO LATER block
            {
            match(input,DO,FOLLOW_DO_in_laterStatement4193); if (failed) return ;
            match(input,LATER,FOLLOW_LATER_in_laterStatement4197); if (failed) return ;
            pushFollow(FOLLOW_block_in_laterStatement4201);
            block();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end laterStatement


    // $ANTLR start ifStatement
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:483:1: ifStatement returns [JCStatement value] : IF LPAREN expression RPAREN s1= block ( ELSE s2= block )? ;
    public final JCStatement ifStatement() throws RecognitionException {
        JCStatement value = null;

        Token IF106=null;
        JCBlock s1 = null;

        JCBlock s2 = null;

        JCExpression expression107 = null;


         JCStatement elsepart = null; 
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:485:4: ( IF LPAREN expression RPAREN s1= block ( ELSE s2= block )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:485:4: IF LPAREN expression RPAREN s1= block ( ELSE s2= block )?
            {
            IF106=(Token)input.LT(1);
            match(input,IF,FOLLOW_IF_in_ifStatement4221); if (failed) return value;
            match(input,LPAREN,FOLLOW_LPAREN_in_ifStatement4225); if (failed) return value;
            pushFollow(FOLLOW_expression_in_ifStatement4229);
            expression107=expression();
            _fsp--;
            if (failed) return value;
            match(input,RPAREN,FOLLOW_RPAREN_in_ifStatement4233); if (failed) return value;
            pushFollow(FOLLOW_block_in_ifStatement4239);
            s1=block();
            _fsp--;
            if (failed) return value;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:485:49: ( ELSE s2= block )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==ELSE) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:485:50: ELSE s2= block
                    {
                    match(input,ELSE,FOLLOW_ELSE_in_ifStatement4242); if (failed) return value;
                    pushFollow(FOLLOW_block_in_ifStatement4247);
                    s2=block();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       elsepart = s2; 
                    }

                    }
                    break;

            }

            if ( backtracking==0 ) {
               value = F.at(pos(IF106)).If(expression107, s1, elsepart); 
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
    // $ANTLR end ifStatement


    // $ANTLR start insertStatement
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:487:1: insertStatement returns [JCStatement value = null] : INSERT ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) ) SEMI ;
    public final JCStatement insertStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:488:4: ( INSERT ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) ) SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:488:4: INSERT ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) ) SEMI
            {
            match(input,INSERT,FOLLOW_INSERT_in_insertStatement4276); if (failed) return value;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:488:13: ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) )
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==DISTINCT) ) {
                alt38=1;
            }
            else if ( ((LA38_0>=POUND && LA38_0<=TYPEOF)||LA38_0==IF||(LA38_0>=THIS && LA38_0<=FALSE)||LA38_0==UNITINTERVAL||LA38_0==FOREACH||(LA38_0>=NOT && LA38_0<=NEW)||(LA38_0>=OPERATION && LA38_0<=FUNCTION)||(LA38_0>=INDEXOF && LA38_0<=SUPER)||(LA38_0>=SIZEOF && LA38_0<=REVERSE)||LA38_0==LPAREN||LA38_0==LBRACKET||LA38_0==DOT||(LA38_0>=PLUSPLUS && LA38_0<=SUBSUB)||(LA38_0>=QUES && LA38_0<=STRING_LITERAL)||LA38_0==QUOTE_LBRACE_STRING_LITERAL||(LA38_0>=QUOTED_IDENTIFIER && LA38_0<=INTEGER_LITERAL)||LA38_0==FLOATING_POINT_LITERAL||LA38_0==IDENTIFIER) ) {
                alt38=2;
            }
            else {
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("488:13: ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) )", 38, 0, input);

                throw nvae;
            }
            switch (alt38) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:488:17: DISTINCT expression INTO expression
                    {
                    match(input,DISTINCT,FOLLOW_DISTINCT_in_insertStatement4284); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_insertStatement4288);
                    expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,INTO,FOLLOW_INTO_in_insertStatement4292); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_insertStatement4296);
                    expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:488:65: expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression )
                    {
                    pushFollow(FOLLOW_expression_in_insertStatement4304);
                    expression();
                    _fsp--;
                    if (failed) return value;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:488:78: ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression )
                    int alt37=3;
                    switch ( input.LA(1) ) {
                    case AS:
                    case INTO:
                        {
                        alt37=1;
                        }
                        break;
                    case AFTER:
                        {
                        alt37=2;
                        }
                        break;
                    case BEFORE:
                        {
                        alt37=3;
                        }
                        break;
                    default:
                        if (backtracking>0) {failed=true; return value;}
                        NoViableAltException nvae =
                            new NoViableAltException("488:78: ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression )", 37, 0, input);

                        throw nvae;
                    }

                    switch (alt37) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:488:82: ( ( AS ( FIRST | LAST ) )? INTO expression )
                            {
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:488:82: ( ( AS ( FIRST | LAST ) )? INTO expression )
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:488:86: ( AS ( FIRST | LAST ) )? INTO expression
                            {
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:488:86: ( AS ( FIRST | LAST ) )?
                            int alt36=2;
                            int LA36_0 = input.LA(1);

                            if ( (LA36_0==AS) ) {
                                alt36=1;
                            }
                            switch (alt36) {
                                case 1 :
                                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:488:90: AS ( FIRST | LAST )
                                    {
                                    match(input,AS,FOLLOW_AS_in_insertStatement4320); if (failed) return value;
                                    if ( (input.LA(1)>=FIRST && input.LA(1)<=LAST) ) {
                                        input.consume();
                                        errorRecovery=false;failed=false;
                                    }
                                    else {
                                        if (backtracking>0) {failed=true; return value;}
                                        MismatchedSetException mse =
                                            new MismatchedSetException(null,input);
                                        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_insertStatement4324);    throw mse;
                                    }


                                    }
                                    break;

                            }

                            match(input,INTO,FOLLOW_INTO_in_insertStatement4350); if (failed) return value;
                            pushFollow(FOLLOW_expression_in_insertStatement4354);
                            expression();
                            _fsp--;
                            if (failed) return value;

                            }


                            }
                            break;
                        case 2 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:488:156: AFTER expression
                            {
                            match(input,AFTER,FOLLOW_AFTER_in_insertStatement4366); if (failed) return value;
                            pushFollow(FOLLOW_expression_in_insertStatement4370);
                            expression();
                            _fsp--;
                            if (failed) return value;

                            }
                            break;
                        case 3 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:488:181: BEFORE expression
                            {
                            match(input,BEFORE,FOLLOW_BEFORE_in_insertStatement4378); if (failed) return value;
                            pushFollow(FOLLOW_expression_in_insertStatement4382);
                            expression();
                            _fsp--;
                            if (failed) return value;

                            }
                            break;

                    }


                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_insertStatement4396); if (failed) return value;

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
    // $ANTLR end insertStatement


    // $ANTLR start deleteStatement
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:489:1: deleteStatement returns [JCStatement value = null] : DELETE expression SEMI ;
    public final JCStatement deleteStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:490:4: ( DELETE expression SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:490:4: DELETE expression SEMI
            {
            match(input,DELETE,FOLLOW_DELETE_in_deleteStatement4411); if (failed) return value;
            pushFollow(FOLLOW_expression_in_deleteStatement4415);
            expression();
            _fsp--;
            if (failed) return value;
            match(input,SEMI,FOLLOW_SEMI_in_deleteStatement4419); if (failed) return value;

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
    // $ANTLR end deleteStatement


    // $ANTLR start throwStatement
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:491:1: throwStatement returns [JCStatement value = null] : THROW expression SEMI ;
    public final JCStatement throwStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:492:4: ( THROW expression SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:492:4: THROW expression SEMI
            {
            match(input,THROW,FOLLOW_THROW_in_throwStatement4434); if (failed) return value;
            pushFollow(FOLLOW_expression_in_throwStatement4438);
            expression();
            _fsp--;
            if (failed) return value;
            match(input,SEMI,FOLLOW_SEMI_in_throwStatement4442); if (failed) return value;

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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:493:1: returnStatement returns [JCStatement value] : RETURN ( expression )? SEMI ;
    public final JCStatement returnStatement() throws RecognitionException {
        JCStatement value = null;

        Token RETURN109=null;
        JCExpression expression108 = null;


         JCExpression expr = null; 
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:495:4: ( RETURN ( expression )? SEMI )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:495:4: RETURN ( expression )? SEMI
            {
            RETURN109=(Token)input.LT(1);
            match(input,RETURN,FOLLOW_RETURN_in_returnStatement4462); if (failed) return value;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:495:11: ( expression )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( ((LA39_0>=POUND && LA39_0<=TYPEOF)||LA39_0==IF||(LA39_0>=THIS && LA39_0<=FALSE)||LA39_0==UNITINTERVAL||LA39_0==FOREACH||(LA39_0>=NOT && LA39_0<=NEW)||(LA39_0>=OPERATION && LA39_0<=FUNCTION)||(LA39_0>=INDEXOF && LA39_0<=SUPER)||(LA39_0>=SIZEOF && LA39_0<=REVERSE)||LA39_0==LPAREN||LA39_0==LBRACKET||LA39_0==DOT||(LA39_0>=PLUSPLUS && LA39_0<=SUBSUB)||(LA39_0>=QUES && LA39_0<=STRING_LITERAL)||LA39_0==QUOTE_LBRACE_STRING_LITERAL||(LA39_0>=QUOTED_IDENTIFIER && LA39_0<=INTEGER_LITERAL)||LA39_0==FLOATING_POINT_LITERAL||LA39_0==IDENTIFIER) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:495:12: expression
                    {
                    pushFollow(FOLLOW_expression_in_returnStatement4465);
                    expression108=expression();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       expr = expression108; 
                    }

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_returnStatement4472); if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(pos(RETURN109)).Return(expr); 
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
    // $ANTLR end returnStatement


    // $ANTLR start localTriggerStatement
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:498:1: localTriggerStatement returns [JCStatement value = null] : TRIGGER ON ( localTriggerCondition | LPAREN localTriggerCondition RPAREN ) block ;
    public final JCStatement localTriggerStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:499:4: ( TRIGGER ON ( localTriggerCondition | LPAREN localTriggerCondition RPAREN ) block )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:499:4: TRIGGER ON ( localTriggerCondition | LPAREN localTriggerCondition RPAREN ) block
            {
            match(input,TRIGGER,FOLLOW_TRIGGER_in_localTriggerStatement4498); if (failed) return value;
            match(input,ON,FOLLOW_ON_in_localTriggerStatement4502); if (failed) return value;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:499:20: ( localTriggerCondition | LPAREN localTriggerCondition RPAREN )
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==DELETE||LA40_0==INSERT||LA40_0==QUOTED_IDENTIFIER||LA40_0==IDENTIFIER) ) {
                alt40=1;
            }
            else if ( (LA40_0==LPAREN) ) {
                alt40=2;
            }
            else {
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("499:20: ( localTriggerCondition | LPAREN localTriggerCondition RPAREN )", 40, 0, input);

                throw nvae;
            }
            switch (alt40) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:499:22: localTriggerCondition
                    {
                    pushFollow(FOLLOW_localTriggerCondition_in_localTriggerStatement4509);
                    localTriggerCondition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:499:46: LPAREN localTriggerCondition RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_localTriggerStatement4513); if (failed) return value;
                    pushFollow(FOLLOW_localTriggerCondition_in_localTriggerStatement4517);
                    localTriggerCondition();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_localTriggerStatement4521); if (failed) return value;

                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_localTriggerStatement4525);
            block();
            _fsp--;
            if (failed) return value;

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
    // $ANTLR end localTriggerStatement


    // $ANTLR start localTriggerCondition
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:500:1: localTriggerCondition returns [JCStatement value = null] : ( name ( LBRACKET name RBRACKET )? EQ expression | INSERT name INTO ( name EQ ) expression | DELETE name FROM ( name EQ ) expression );
    public final JCStatement localTriggerCondition() throws RecognitionException {
        JCStatement value =  null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:501:4: ( name ( LBRACKET name RBRACKET )? EQ expression | INSERT name INTO ( name EQ ) expression | DELETE name FROM ( name EQ ) expression )
            int alt42=3;
            switch ( input.LA(1) ) {
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                alt42=1;
                }
                break;
            case INSERT:
                {
                alt42=2;
                }
                break;
            case DELETE:
                {
                alt42=3;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("500:1: localTriggerCondition returns [JCStatement value = null] : ( name ( LBRACKET name RBRACKET )? EQ expression | INSERT name INTO ( name EQ ) expression | DELETE name FROM ( name EQ ) expression );", 42, 0, input);

                throw nvae;
            }

            switch (alt42) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:501:4: name ( LBRACKET name RBRACKET )? EQ expression
                    {
                    pushFollow(FOLLOW_name_in_localTriggerCondition4540);
                    name();
                    _fsp--;
                    if (failed) return value;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:501:11: ( LBRACKET name RBRACKET )?
                    int alt41=2;
                    int LA41_0 = input.LA(1);

                    if ( (LA41_0==LBRACKET) ) {
                        alt41=1;
                    }
                    switch (alt41) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:501:15: LBRACKET name RBRACKET
                            {
                            match(input,LBRACKET,FOLLOW_LBRACKET_in_localTriggerCondition4548); if (failed) return value;
                            pushFollow(FOLLOW_name_in_localTriggerCondition4552);
                            name();
                            _fsp--;
                            if (failed) return value;
                            match(input,RBRACKET,FOLLOW_RBRACKET_in_localTriggerCondition4556); if (failed) return value;

                            }
                            break;

                    }

                    match(input,EQ,FOLLOW_EQ_in_localTriggerCondition4566); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_localTriggerCondition4570);
                    expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:502:10: INSERT name INTO ( name EQ ) expression
                    {
                    match(input,INSERT,FOLLOW_INSERT_in_localTriggerCondition4582); if (failed) return value;
                    pushFollow(FOLLOW_name_in_localTriggerCondition4586);
                    name();
                    _fsp--;
                    if (failed) return value;
                    match(input,INTO,FOLLOW_INTO_in_localTriggerCondition4590); if (failed) return value;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:502:33: ( name EQ )
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:502:35: name EQ
                    {
                    pushFollow(FOLLOW_name_in_localTriggerCondition4596);
                    name();
                    _fsp--;
                    if (failed) return value;
                    match(input,EQ,FOLLOW_EQ_in_localTriggerCondition4600); if (failed) return value;

                    }

                    pushFollow(FOLLOW_expression_in_localTriggerCondition4608);
                    expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:503:10: DELETE name FROM ( name EQ ) expression
                    {
                    match(input,DELETE,FOLLOW_DELETE_in_localTriggerCondition4620); if (failed) return value;
                    pushFollow(FOLLOW_name_in_localTriggerCondition4624);
                    name();
                    _fsp--;
                    if (failed) return value;
                    match(input,FROM,FOLLOW_FROM_in_localTriggerCondition4628); if (failed) return value;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:503:33: ( name EQ )
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:503:35: name EQ
                    {
                    pushFollow(FOLLOW_name_in_localTriggerCondition4634);
                    name();
                    _fsp--;
                    if (failed) return value;
                    match(input,EQ,FOLLOW_EQ_in_localTriggerCondition4638); if (failed) return value;

                    }

                    pushFollow(FOLLOW_expression_in_localTriggerCondition4646);
                    expression();
                    _fsp--;
                    if (failed) return value;

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
    // $ANTLR end localTriggerCondition


    // $ANTLR start forAlphaStatement
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:1: forAlphaStatement returns [JCStatement value = null] : FOR LPAREN alphaExpression RPAREN block ;
    public final JCStatement forAlphaStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:505:4: ( FOR LPAREN alphaExpression RPAREN block )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:505:4: FOR LPAREN alphaExpression RPAREN block
            {
            match(input,FOR,FOLLOW_FOR_in_forAlphaStatement4661); if (failed) return value;
            match(input,LPAREN,FOLLOW_LPAREN_in_forAlphaStatement4665); if (failed) return value;
            pushFollow(FOLLOW_alphaExpression_in_forAlphaStatement4669);
            alphaExpression();
            _fsp--;
            if (failed) return value;
            match(input,RPAREN,FOLLOW_RPAREN_in_forAlphaStatement4673); if (failed) return value;
            pushFollow(FOLLOW_block_in_forAlphaStatement4677);
            block();
            _fsp--;
            if (failed) return value;

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
    // $ANTLR end forAlphaStatement


    // $ANTLR start alphaExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:1: alphaExpression : UNITINTERVAL IN DUR expression ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )? ;
    public final void alphaExpression() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:19: ( UNITINTERVAL IN DUR expression ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:19: UNITINTERVAL IN DUR expression ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )?
            {
            match(input,UNITINTERVAL,FOLLOW_UNITINTERVAL_in_alphaExpression4685); if (failed) return ;
            match(input,IN,FOLLOW_IN_in_alphaExpression4689); if (failed) return ;
            match(input,DUR,FOLLOW_DUR_in_alphaExpression4693); if (failed) return ;
            pushFollow(FOLLOW_expression_in_alphaExpression4697);
            expression();
            _fsp--;
            if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:58: ( FPS expression )?
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==FPS) ) {
                int LA43_1 = input.LA(2);

                if ( (synpred80()) ) {
                    alt43=1;
                }
            }
            switch (alt43) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:62: FPS expression
                    {
                    match(input,FPS,FOLLOW_FPS_in_alphaExpression4705); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_alphaExpression4709);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:87: ( WHILE expression )?
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==WHILE) ) {
                int LA44_1 = input.LA(2);

                if ( (synpred81()) ) {
                    alt44=1;
                }
            }
            switch (alt44) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:91: WHILE expression
                    {
                    match(input,WHILE,FOLLOW_WHILE_in_alphaExpression4723); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_alphaExpression4727);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:118: ( CONTINUE IF expression )?
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==CONTINUE) ) {
                int LA45_1 = input.LA(2);

                if ( (synpred82()) ) {
                    alt45=1;
                }
            }
            switch (alt45) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:122: CONTINUE IF expression
                    {
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_alphaExpression4741); if (failed) return ;
                    match(input,IF,FOLLOW_IF_in_alphaExpression4745); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_alphaExpression4749);
                    expression();
                    _fsp--;
                    if (failed) return ;

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
        return ;
    }
    // $ANTLR end alphaExpression


    // $ANTLR start forJoinStatement
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:507:1: forJoinStatement returns [JCStatement value = null] : FOR LPAREN joinClause RPAREN ( LPAREN durClause RPAREN )? block ;
    public final JCStatement forJoinStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:508:4: ( FOR LPAREN joinClause RPAREN ( LPAREN durClause RPAREN )? block )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:508:4: FOR LPAREN joinClause RPAREN ( LPAREN durClause RPAREN )? block
            {
            match(input,FOR,FOLLOW_FOR_in_forJoinStatement4770); if (failed) return value;
            match(input,LPAREN,FOLLOW_LPAREN_in_forJoinStatement4774); if (failed) return value;
            pushFollow(FOLLOW_joinClause_in_forJoinStatement4778);
            joinClause();
            _fsp--;
            if (failed) return value;
            match(input,RPAREN,FOLLOW_RPAREN_in_forJoinStatement4782); if (failed) return value;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:508:41: ( LPAREN durClause RPAREN )?
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==LPAREN) ) {
                alt46=1;
            }
            switch (alt46) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:508:45: LPAREN durClause RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_forJoinStatement4790); if (failed) return value;
                    pushFollow(FOLLOW_durClause_in_forJoinStatement4794);
                    durClause();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_forJoinStatement4798); if (failed) return value;

                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_forJoinStatement4808);
            block();
            _fsp--;
            if (failed) return value;

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
    // $ANTLR end forJoinStatement


    // $ANTLR start joinClause
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:509:1: joinClause : name IN expression ( COMMA name IN expression )* ( WHERE expression )? ;
    public final void joinClause() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:509:14: ( name IN expression ( COMMA name IN expression )* ( WHERE expression )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:509:14: name IN expression ( COMMA name IN expression )* ( WHERE expression )?
            {
            pushFollow(FOLLOW_name_in_joinClause4816);
            name();
            _fsp--;
            if (failed) return ;
            match(input,IN,FOLLOW_IN_in_joinClause4820); if (failed) return ;
            pushFollow(FOLLOW_expression_in_joinClause4824);
            expression();
            _fsp--;
            if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:509:39: ( COMMA name IN expression )*
            loop47:
            do {
                int alt47=2;
                int LA47_0 = input.LA(1);

                if ( (LA47_0==COMMA) ) {
                    alt47=1;
                }


                switch (alt47) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:509:43: COMMA name IN expression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_joinClause4832); if (failed) return ;
            	    pushFollow(FOLLOW_name_in_joinClause4836);
            	    name();
            	    _fsp--;
            	    if (failed) return ;
            	    match(input,IN,FOLLOW_IN_in_joinClause4840); if (failed) return ;
            	    pushFollow(FOLLOW_expression_in_joinClause4844);
            	    expression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop47;
                }
            } while (true);

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:509:82: ( WHERE expression )?
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==WHERE) ) {
                alt48=1;
            }
            switch (alt48) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:509:86: WHERE expression
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_joinClause4858); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_joinClause4862);
                    expression();
                    _fsp--;
                    if (failed) return ;

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
        return ;
    }
    // $ANTLR end joinClause


    // $ANTLR start durClause
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:1: durClause : DUR expression ( LINEAR | EASEIN | EASEOUT | EASEBOTH | MOTION expression )? ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )? ;
    public final void durClause() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:13: ( DUR expression ( LINEAR | EASEIN | EASEOUT | EASEBOTH | MOTION expression )? ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:13: DUR expression ( LINEAR | EASEIN | EASEOUT | EASEBOTH | MOTION expression )? ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )?
            {
            match(input,DUR,FOLLOW_DUR_in_durClause4876); if (failed) return ;
            pushFollow(FOLLOW_expression_in_durClause4880);
            expression();
            _fsp--;
            if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:32: ( LINEAR | EASEIN | EASEOUT | EASEBOTH | MOTION expression )?
            int alt49=6;
            switch ( input.LA(1) ) {
                case LINEAR:
                    {
                    int LA49_1 = input.LA(2);

                    if ( (synpred86()) ) {
                        alt49=1;
                    }
                    }
                    break;
                case EASEIN:
                    {
                    int LA49_2 = input.LA(2);

                    if ( (synpred87()) ) {
                        alt49=2;
                    }
                    }
                    break;
                case EASEOUT:
                    {
                    int LA49_3 = input.LA(2);

                    if ( (synpred88()) ) {
                        alt49=3;
                    }
                    }
                    break;
                case EASEBOTH:
                    {
                    int LA49_4 = input.LA(2);

                    if ( (synpred89()) ) {
                        alt49=4;
                    }
                    }
                    break;
                case MOTION:
                    {
                    int LA49_5 = input.LA(2);

                    if ( (synpred90()) ) {
                        alt49=5;
                    }
                    }
                    break;
            }

            switch (alt49) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:36: LINEAR
                    {
                    match(input,LINEAR,FOLLOW_LINEAR_in_durClause4888); if (failed) return ;

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:49: EASEIN
                    {
                    match(input,EASEIN,FOLLOW_EASEIN_in_durClause4896); if (failed) return ;

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:62: EASEOUT
                    {
                    match(input,EASEOUT,FOLLOW_EASEOUT_in_durClause4904); if (failed) return ;

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:76: EASEBOTH
                    {
                    match(input,EASEBOTH,FOLLOW_EASEBOTH_in_durClause4912); if (failed) return ;

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:91: MOTION expression
                    {
                    match(input,MOTION,FOLLOW_MOTION_in_durClause4920); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_durClause4924);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:119: ( FPS expression )?
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==FPS) ) {
                int LA50_1 = input.LA(2);

                if ( (synpred91()) ) {
                    alt50=1;
                }
            }
            switch (alt50) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:123: FPS expression
                    {
                    match(input,FPS,FOLLOW_FPS_in_durClause4938); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_durClause4942);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:146: ( WHILE expression )?
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==WHILE) ) {
                int LA51_1 = input.LA(2);

                if ( (synpred92()) ) {
                    alt51=1;
                }
            }
            switch (alt51) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:150: WHILE expression
                    {
                    match(input,WHILE,FOLLOW_WHILE_in_durClause4954); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_durClause4958);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:177: ( CONTINUE IF expression )?
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( (LA52_0==CONTINUE) ) {
                int LA52_1 = input.LA(2);

                if ( (synpred93()) ) {
                    alt52=1;
                }
            }
            switch (alt52) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:181: CONTINUE IF expression
                    {
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_durClause4972); if (failed) return ;
                    match(input,IF,FOLLOW_IF_in_durClause4976); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_durClause4980);
                    expression();
                    _fsp--;
                    if (failed) return ;

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
        return ;
    }
    // $ANTLR end durClause


    // $ANTLR start tryStatement
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:511:1: tryStatement returns [JCStatement value = null] : TRY block ( FINALLY block | ( catchClause )+ ( FINALLY block )? ) ;
    public final JCStatement tryStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:512:4: ( TRY block ( FINALLY block | ( catchClause )+ ( FINALLY block )? ) )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:512:4: TRY block ( FINALLY block | ( catchClause )+ ( FINALLY block )? )
            {
            match(input,TRY,FOLLOW_TRY_in_tryStatement5001); if (failed) return value;
            pushFollow(FOLLOW_block_in_tryStatement5005);
            block();
            _fsp--;
            if (failed) return value;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:512:18: ( FINALLY block | ( catchClause )+ ( FINALLY block )? )
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==FINALLY) ) {
                alt55=1;
            }
            else if ( (LA55_0==CATCH) ) {
                alt55=2;
            }
            else {
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("512:18: ( FINALLY block | ( catchClause )+ ( FINALLY block )? )", 55, 0, input);

                throw nvae;
            }
            switch (alt55) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:512:22: FINALLY block
                    {
                    match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement5013); if (failed) return value;
                    pushFollow(FOLLOW_block_in_tryStatement5017);
                    block();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:512:46: ( catchClause )+ ( FINALLY block )?
                    {
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:512:46: ( catchClause )+
                    int cnt53=0;
                    loop53:
                    do {
                        int alt53=2;
                        int LA53_0 = input.LA(1);

                        if ( (LA53_0==CATCH) ) {
                            alt53=1;
                        }


                        switch (alt53) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: catchClause
                    	    {
                    	    pushFollow(FOLLOW_catchClause_in_tryStatement5027);
                    	    catchClause();
                    	    _fsp--;
                    	    if (failed) return value;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt53 >= 1 ) break loop53;
                    	    if (backtracking>0) {failed=true; return value;}
                                EarlyExitException eee =
                                    new EarlyExitException(53, input);
                                throw eee;
                        }
                        cnt53++;
                    } while (true);

                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:512:62: ( FINALLY block )?
                    int alt54=2;
                    int LA54_0 = input.LA(1);

                    if ( (LA54_0==FINALLY) ) {
                        alt54=1;
                    }
                    switch (alt54) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:512:66: FINALLY block
                            {
                            match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement5037); if (failed) return value;
                            pushFollow(FOLLOW_block_in_tryStatement5041);
                            block();
                            _fsp--;
                            if (failed) return value;

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
        return value;
    }
    // $ANTLR end tryStatement


    // $ANTLR start catchClause
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:513:1: catchClause : CATCH LPAREN name ( typeReference )? ( IF expression )? RPAREN block ;
    public final void catchClause() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:513:15: ( CATCH LPAREN name ( typeReference )? ( IF expression )? RPAREN block )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:513:15: CATCH LPAREN name ( typeReference )? ( IF expression )? RPAREN block
            {
            match(input,CATCH,FOLLOW_CATCH_in_catchClause5059); if (failed) return ;
            match(input,LPAREN,FOLLOW_LPAREN_in_catchClause5063); if (failed) return ;
            pushFollow(FOLLOW_name_in_catchClause5067);
            name();
            _fsp--;
            if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:513:39: ( typeReference )?
            int alt56=2;
            switch ( input.LA(1) ) {
                case COLON:
                    {
                    alt56=1;
                    }
                    break;
                case IF:
                    {
                    int LA56_2 = input.LA(2);

                    if ( (synpred97()) ) {
                        alt56=1;
                    }
                    }
                    break;
                case RPAREN:
                    {
                    int LA56_3 = input.LA(2);

                    if ( (synpred97()) ) {
                        alt56=1;
                    }
                    }
                    break;
            }

            switch (alt56) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: typeReference
                    {
                    pushFollow(FOLLOW_typeReference_in_catchClause5071);
                    typeReference();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:513:57: ( IF expression )?
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( (LA57_0==IF) ) {
                alt57=1;
            }
            switch (alt57) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:513:61: IF expression
                    {
                    match(input,IF,FOLLOW_IF_in_catchClause5081); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_catchClause5085);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_catchClause5095); if (failed) return ;
            pushFollow(FOLLOW_block_in_catchClause5099);
            block();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end catchClause


    // $ANTLR start expression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:514:1: expression returns [JCExpression expr] : ( foreach | functionExpression | operationExpression | alphaExpression | ifExpression | selectExpression | LPAREN typeName RPAREN suffixedExpression | suffixedExpression );
    public final JCExpression expression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression suffixedExpression110 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:515:4: ( foreach | functionExpression | operationExpression | alphaExpression | ifExpression | selectExpression | LPAREN typeName RPAREN suffixedExpression | suffixedExpression )
            int alt58=8;
            switch ( input.LA(1) ) {
            case FOREACH:
                {
                alt58=1;
                }
                break;
            case FUNCTION:
                {
                alt58=2;
                }
                break;
            case OPERATION:
                {
                alt58=3;
                }
                break;
            case UNITINTERVAL:
                {
                alt58=4;
                }
                break;
            case IF:
                {
                alt58=5;
                }
                break;
            case SELECT:
                {
                alt58=6;
                }
                break;
            case LPAREN:
                {
                int LA58_7 = input.LA(2);

                if ( (synpred105()) ) {
                    alt58=7;
                }
                else if ( (true) ) {
                    alt58=8;
                }
                else {
                    if (backtracking>0) {failed=true; return expr;}
                    NoViableAltException nvae =
                        new NoViableAltException("514:1: expression returns [JCExpression expr] : ( foreach | functionExpression | operationExpression | alphaExpression | ifExpression | selectExpression | LPAREN typeName RPAREN suffixedExpression | suffixedExpression );", 58, 7, input);

                    throw nvae;
                }
                }
                break;
            case POUND:
            case TYPEOF:
            case THIS:
            case NULL:
            case TRUE:
            case FALSE:
            case NOT:
            case NEW:
            case INDEXOF:
            case SUPER:
            case SIZEOF:
            case REVERSE:
            case LBRACKET:
            case DOT:
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
                alt58=8;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return expr;}
                NoViableAltException nvae =
                    new NoViableAltException("514:1: expression returns [JCExpression expr] : ( foreach | functionExpression | operationExpression | alphaExpression | ifExpression | selectExpression | LPAREN typeName RPAREN suffixedExpression | suffixedExpression );", 58, 0, input);

                throw nvae;
            }

            switch (alt58) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:515:4: foreach
                    {
                    pushFollow(FOLLOW_foreach_in_expression5113);
                    foreach();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:516:11: functionExpression
                    {
                    pushFollow(FOLLOW_functionExpression_in_expression5126);
                    functionExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:517:11: operationExpression
                    {
                    pushFollow(FOLLOW_operationExpression_in_expression5139);
                    operationExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:11: alphaExpression
                    {
                    pushFollow(FOLLOW_alphaExpression_in_expression5152);
                    alphaExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:519:11: ifExpression
                    {
                    pushFollow(FOLLOW_ifExpression_in_expression5165);
                    ifExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 6 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:520:11: selectExpression
                    {
                    pushFollow(FOLLOW_selectExpression_in_expression5181);
                    selectExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 7 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:11: LPAREN typeName RPAREN suffixedExpression
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_expression5194); if (failed) return expr;
                    pushFollow(FOLLOW_typeName_in_expression5200);
                    typeName();
                    _fsp--;
                    if (failed) return expr;
                    match(input,RPAREN,FOLLOW_RPAREN_in_expression5203); if (failed) return expr;
                    pushFollow(FOLLOW_suffixedExpression_in_expression5207);
                    suffixedExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 8 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:522:11: suffixedExpression
                    {
                    pushFollow(FOLLOW_suffixedExpression_in_expression5222);
                    suffixedExpression110=suffixedExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = suffixedExpression110; 
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
    // $ANTLR end expression


    // $ANTLR start foreach
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:523:1: foreach : FOREACH LPAREN name IN expression ( COMMA name IN expression )* ( WHERE expression )? RPAREN expression ;
    public final void foreach() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:523:11: ( FOREACH LPAREN name IN expression ( COMMA name IN expression )* ( WHERE expression )? RPAREN expression )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:523:11: FOREACH LPAREN name IN expression ( COMMA name IN expression )* ( WHERE expression )? RPAREN expression
            {
            match(input,FOREACH,FOLLOW_FOREACH_in_foreach5234); if (failed) return ;
            match(input,LPAREN,FOLLOW_LPAREN_in_foreach5238); if (failed) return ;
            pushFollow(FOLLOW_name_in_foreach5242);
            name();
            _fsp--;
            if (failed) return ;
            match(input,IN,FOLLOW_IN_in_foreach5246); if (failed) return ;
            pushFollow(FOLLOW_expression_in_foreach5250);
            expression();
            _fsp--;
            if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:523:55: ( COMMA name IN expression )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( (LA59_0==COMMA) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:523:59: COMMA name IN expression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_foreach5258); if (failed) return ;
            	    pushFollow(FOLLOW_name_in_foreach5262);
            	    name();
            	    _fsp--;
            	    if (failed) return ;
            	    match(input,IN,FOLLOW_IN_in_foreach5266); if (failed) return ;
            	    pushFollow(FOLLOW_expression_in_foreach5270);
            	    expression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop59;
                }
            } while (true);

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:523:98: ( WHERE expression )?
            int alt60=2;
            int LA60_0 = input.LA(1);

            if ( (LA60_0==WHERE) ) {
                alt60=1;
            }
            switch (alt60) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:523:102: WHERE expression
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_foreach5284); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_foreach5288);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_foreach5298); if (failed) return ;
            pushFollow(FOLLOW_expression_in_foreach5302);
            expression();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end foreach


    // $ANTLR start functionExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:524:1: functionExpression : FUNCTION formalParameters ( typeReference )? functionBody ;
    public final void functionExpression() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:524:22: ( FUNCTION formalParameters ( typeReference )? functionBody )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:524:22: FUNCTION formalParameters ( typeReference )? functionBody
            {
            match(input,FUNCTION,FOLLOW_FUNCTION_in_functionExpression5310); if (failed) return ;
            pushFollow(FOLLOW_formalParameters_in_functionExpression5314);
            formalParameters();
            _fsp--;
            if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:524:52: ( typeReference )?
            int alt61=2;
            switch ( input.LA(1) ) {
                case COLON:
                    {
                    alt61=1;
                    }
                    break;
                case EQ:
                    {
                    int LA61_2 = input.LA(2);

                    if ( (synpred108()) ) {
                        alt61=1;
                    }
                    }
                    break;
                case LBRACE:
                    {
                    int LA61_3 = input.LA(2);

                    if ( (synpred108()) ) {
                        alt61=1;
                    }
                    }
                    break;
            }

            switch (alt61) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: typeReference
                    {
                    pushFollow(FOLLOW_typeReference_in_functionExpression5318);
                    typeReference();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_functionBody_in_functionExpression5324);
            functionBody();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end functionExpression


    // $ANTLR start operationExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:1: operationExpression : OPERATION formalParameters ( typeReference )? block ;
    public final void operationExpression() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:23: ( OPERATION formalParameters ( typeReference )? block )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:23: OPERATION formalParameters ( typeReference )? block
            {
            match(input,OPERATION,FOLLOW_OPERATION_in_operationExpression5332); if (failed) return ;
            pushFollow(FOLLOW_formalParameters_in_operationExpression5336);
            formalParameters();
            _fsp--;
            if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:54: ( typeReference )?
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==COLON) ) {
                alt62=1;
            }
            else if ( (LA62_0==LBRACE) ) {
                int LA62_2 = input.LA(2);

                if ( (synpred109()) ) {
                    alt62=1;
                }
            }
            switch (alt62) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: typeReference
                    {
                    pushFollow(FOLLOW_typeReference_in_operationExpression5340);
                    typeReference();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_operationExpression5346);
            block();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end operationExpression


    // $ANTLR start ifExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:526:1: ifExpression : IF expression THEN expression ELSE expression ;
    public final void ifExpression() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:526:16: ( IF expression THEN expression ELSE expression )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:526:16: IF expression THEN expression ELSE expression
            {
            match(input,IF,FOLLOW_IF_in_ifExpression5354); if (failed) return ;
            pushFollow(FOLLOW_expression_in_ifExpression5358);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,THEN,FOLLOW_THEN_in_ifExpression5362); if (failed) return ;
            pushFollow(FOLLOW_expression_in_ifExpression5366);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,ELSE,FOLLOW_ELSE_in_ifExpression5370); if (failed) return ;
            pushFollow(FOLLOW_expression_in_ifExpression5374);
            expression();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end ifExpression


    // $ANTLR start selectExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:527:1: selectExpression : SELECT ( DISTINCT )? expression FROM selectionVar ( COMMA selectionVar )* ( WHERE expression )? ;
    public final void selectExpression() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:527:20: ( SELECT ( DISTINCT )? expression FROM selectionVar ( COMMA selectionVar )* ( WHERE expression )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:527:20: SELECT ( DISTINCT )? expression FROM selectionVar ( COMMA selectionVar )* ( WHERE expression )?
            {
            match(input,SELECT,FOLLOW_SELECT_in_selectExpression5382); if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:527:29: ( DISTINCT )?
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==DISTINCT) ) {
                alt63=1;
            }
            switch (alt63) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: DISTINCT
                    {
                    match(input,DISTINCT,FOLLOW_DISTINCT_in_selectExpression5386); if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_expression_in_selectExpression5394);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,FROM,FOLLOW_FROM_in_selectExpression5398); if (failed) return ;
            pushFollow(FOLLOW_selectionVar_in_selectExpression5402);
            selectionVar();
            _fsp--;
            if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:527:79: ( COMMA selectionVar )*
            loop64:
            do {
                int alt64=2;
                int LA64_0 = input.LA(1);

                if ( (LA64_0==COMMA) ) {
                    int LA64_2 = input.LA(2);

                    if ( (synpred111()) ) {
                        alt64=1;
                    }


                }


                switch (alt64) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:527:83: COMMA selectionVar
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_selectExpression5410); if (failed) return ;
            	    pushFollow(FOLLOW_selectionVar_in_selectExpression5414);
            	    selectionVar();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop64;
                }
            } while (true);

            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:527:112: ( WHERE expression )?
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( (LA65_0==WHERE) ) {
                int LA65_1 = input.LA(2);

                if ( (synpred112()) ) {
                    alt65=1;
                }
            }
            switch (alt65) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:527:116: WHERE expression
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_selectExpression5428); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_selectExpression5432);
                    expression();
                    _fsp--;
                    if (failed) return ;

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
        return ;
    }
    // $ANTLR end selectExpression


    // $ANTLR start selectionVar
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:528:1: selectionVar : name ( IN expression )? ;
    public final void selectionVar() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:528:16: ( name ( IN expression )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:528:16: name ( IN expression )?
            {
            pushFollow(FOLLOW_name_in_selectionVar5446);
            name();
            _fsp--;
            if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:528:23: ( IN expression )?
            int alt66=2;
            int LA66_0 = input.LA(1);

            if ( (LA66_0==IN) ) {
                alt66=1;
            }
            switch (alt66) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:528:27: IN expression
                    {
                    match(input,IN,FOLLOW_IN_in_selectionVar5454); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_selectionVar5458);
                    expression();
                    _fsp--;
                    if (failed) return ;

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
        return ;
    }
    // $ANTLR end selectionVar


    // $ANTLR start suffixedExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:529:1: suffixedExpression returns [JCExpression expr] : e1= assignmentExpression ( indexOn | orderBy | durClause | PLUSPLUS | SUBSUB )? ;
    public final JCExpression suffixedExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:530:4: (e1= assignmentExpression ( indexOn | orderBy | durClause | PLUSPLUS | SUBSUB )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:530:4: e1= assignmentExpression ( indexOn | orderBy | durClause | PLUSPLUS | SUBSUB )?
            {
            pushFollow(FOLLOW_assignmentExpression_in_suffixedExpression5481);
            e1=assignmentExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:531:5: ( indexOn | orderBy | durClause | PLUSPLUS | SUBSUB )?
            int alt67=6;
            switch ( input.LA(1) ) {
                case INDEX:
                    {
                    alt67=1;
                    }
                    break;
                case ORDER:
                    {
                    alt67=2;
                    }
                    break;
                case DUR:
                    {
                    alt67=3;
                    }
                    break;
                case PLUSPLUS:
                    {
                    alt67=4;
                    }
                    break;
                case SUBSUB:
                    {
                    alt67=5;
                    }
                    break;
            }

            switch (alt67) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:531:6: indexOn
                    {
                    pushFollow(FOLLOW_indexOn_in_suffixedExpression5493);
                    indexOn();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:531:16: orderBy
                    {
                    pushFollow(FOLLOW_orderBy_in_suffixedExpression5497);
                    orderBy();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:531:26: durClause
                    {
                    pushFollow(FOLLOW_durClause_in_suffixedExpression5501);
                    durClause();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:531:38: PLUSPLUS
                    {
                    match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_suffixedExpression5505); if (failed) return expr;

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:531:49: SUBSUB
                    {
                    match(input,SUBSUB,FOLLOW_SUBSUB_in_suffixedExpression5509); if (failed) return expr;

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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:532:1: assignmentExpression returns [JCExpression expr] : e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? ;
    public final JCExpression assignmentExpression() throws RecognitionException {
        JCExpression expr = null;

        Token EQ111=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:533:4: (e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:533:4: e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )?
            {
            pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression5531);
            e1=assignmentOpExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:534:5: ( EQ e2= assignmentOpExpression )?
            int alt68=2;
            int LA68_0 = input.LA(1);

            if ( (LA68_0==EQ) ) {
                alt68=1;
            }
            switch (alt68) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:534:9: EQ e2= assignmentOpExpression
                    {
                    EQ111=(Token)input.LT(1);
                    match(input,EQ,FOLLOW_EQ_in_assignmentExpression5546); if (failed) return expr;
                    pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression5552);
                    e2=assignmentOpExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(EQ111)).Assign(expr, e2); 
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
    // $ANTLR end assignmentExpression


    // $ANTLR start assignmentOpExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:535:1: assignmentOpExpression returns [JCExpression expr] : e1= andExpression ( assignmentOperator e2= andExpression )? ;
    public final JCExpression assignmentOpExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;

        JCExpression e2 = null;

        int assignmentOperator112 = 0;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:536:4: (e1= andExpression ( assignmentOperator e2= andExpression )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:536:4: e1= andExpression ( assignmentOperator e2= andExpression )?
            {
            pushFollow(FOLLOW_andExpression_in_assignmentOpExpression5579);
            e1=andExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:537:5: ( assignmentOperator e2= andExpression )?
            int alt69=2;
            int LA69_0 = input.LA(1);

            if ( ((LA69_0>=PLUSEQ && LA69_0<=PERCENTEQ)) ) {
                alt69=1;
            }
            switch (alt69) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:537:9: assignmentOperator e2= andExpression
                    {
                    pushFollow(FOLLOW_assignmentOperator_in_assignmentOpExpression5595);
                    assignmentOperator112=assignmentOperator();
                    _fsp--;
                    if (failed) return expr;
                    pushFollow(FOLLOW_andExpression_in_assignmentOpExpression5601);
                    e2=andExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.Assignop(assignmentOperator112,
                      	   													expr, e2); 
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
    // $ANTLR end assignmentOpExpression


    // $ANTLR start andExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:539:1: andExpression returns [JCExpression expr] : e1= orExpression ( AND e2= orExpression )* ;
    public final JCExpression andExpression() throws RecognitionException {
        JCExpression expr = null;

        Token AND113=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:540:4: (e1= orExpression ( AND e2= orExpression )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:540:4: e1= orExpression ( AND e2= orExpression )*
            {
            pushFollow(FOLLOW_orExpression_in_andExpression5627);
            e1=orExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:541:5: ( AND e2= orExpression )*
            loop70:
            do {
                int alt70=2;
                int LA70_0 = input.LA(1);

                if ( (LA70_0==AND) ) {
                    alt70=1;
                }


                switch (alt70) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:541:9: AND e2= orExpression
            	    {
            	    AND113=(Token)input.LT(1);
            	    match(input,AND,FOLLOW_AND_in_andExpression5643); if (failed) return expr;
            	    pushFollow(FOLLOW_orExpression_in_andExpression5649);
            	    e2=orExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(AND113)).Binary(JCTree.AND, expr, e2); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop70;
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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:542:1: orExpression returns [JCExpression expr] : e1= instanceOfExpression ( OR e2= instanceOfExpression )* ;
    public final JCExpression orExpression() throws RecognitionException {
        JCExpression expr = null;

        Token OR114=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:543:4: (e1= instanceOfExpression ( OR e2= instanceOfExpression )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:543:4: e1= instanceOfExpression ( OR e2= instanceOfExpression )*
            {
            pushFollow(FOLLOW_instanceOfExpression_in_orExpression5677);
            e1=instanceOfExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:544:5: ( OR e2= instanceOfExpression )*
            loop71:
            do {
                int alt71=2;
                int LA71_0 = input.LA(1);

                if ( (LA71_0==OR) ) {
                    alt71=1;
                }


                switch (alt71) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:544:9: OR e2= instanceOfExpression
            	    {
            	    OR114=(Token)input.LT(1);
            	    match(input,OR,FOLLOW_OR_in_orExpression5692); if (failed) return expr;
            	    pushFollow(FOLLOW_instanceOfExpression_in_orExpression5698);
            	    e2=instanceOfExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(OR114)).Binary(JCTree.OR, expr, e2); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop71;
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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:545:1: instanceOfExpression returns [JCExpression expr] : e1= relationalExpression ( INSTANCEOF identifier )? ;
    public final JCExpression instanceOfExpression() throws RecognitionException {
        JCExpression expr = null;

        Token INSTANCEOF115=null;
        JCExpression e1 = null;

        JCIdent identifier116 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:546:4: (e1= relationalExpression ( INSTANCEOF identifier )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:546:4: e1= relationalExpression ( INSTANCEOF identifier )?
            {
            pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression5726);
            e1=relationalExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:547:5: ( INSTANCEOF identifier )?
            int alt72=2;
            int LA72_0 = input.LA(1);

            if ( (LA72_0==INSTANCEOF) ) {
                alt72=1;
            }
            switch (alt72) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:547:9: INSTANCEOF identifier
                    {
                    INSTANCEOF115=(Token)input.LT(1);
                    match(input,INSTANCEOF,FOLLOW_INSTANCEOF_in_instanceOfExpression5741); if (failed) return expr;
                    pushFollow(FOLLOW_identifier_in_instanceOfExpression5743);
                    identifier116=identifier();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(INSTANCEOF115)).Binary(JCTree.TYPETEST, expr, 
                      	   													 identifier116); 
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
    // $ANTLR end instanceOfExpression


    // $ANTLR start relationalExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:549:1: relationalExpression returns [JCExpression expr] : e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* ;
    public final JCExpression relationalExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LTGT117=null;
        Token EQEQ118=null;
        Token LTEQ119=null;
        Token GTEQ120=null;
        Token LT121=null;
        Token GT122=null;
        Token IN123=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:550:4: (e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:550:4: e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            {
            pushFollow(FOLLOW_additiveExpression_in_relationalExpression5771);
            e1=additiveExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:551:5: ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            loop73:
            do {
                int alt73=8;
                switch ( input.LA(1) ) {
                case LTGT:
                    {
                    alt73=1;
                    }
                    break;
                case EQEQ:
                    {
                    alt73=2;
                    }
                    break;
                case LTEQ:
                    {
                    alt73=3;
                    }
                    break;
                case GTEQ:
                    {
                    alt73=4;
                    }
                    break;
                case LT:
                    {
                    alt73=5;
                    }
                    break;
                case GT:
                    {
                    alt73=6;
                    }
                    break;
                case IN:
                    {
                    alt73=7;
                    }
                    break;

                }

                switch (alt73) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:551:9: LTGT e= additiveExpression
            	    {
            	    LTGT117=(Token)input.LT(1);
            	    match(input,LTGT,FOLLOW_LTGT_in_relationalExpression5787); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5793);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(LTGT117)).Binary(JCTree.NE, expr, e); 
            	    }

            	    }
            	    break;
            	case 2 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:552:9: EQEQ e= additiveExpression
            	    {
            	    EQEQ118=(Token)input.LT(1);
            	    match(input,EQEQ,FOLLOW_EQEQ_in_relationalExpression5807); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5813);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(EQEQ118)).Binary(JCTree.EQ, expr, e); 
            	    }

            	    }
            	    break;
            	case 3 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:553:9: LTEQ e= additiveExpression
            	    {
            	    LTEQ119=(Token)input.LT(1);
            	    match(input,LTEQ,FOLLOW_LTEQ_in_relationalExpression5827); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5833);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(LTEQ119)).Binary(JCTree.LE, expr, e); 
            	    }

            	    }
            	    break;
            	case 4 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:554:9: GTEQ e= additiveExpression
            	    {
            	    GTEQ120=(Token)input.LT(1);
            	    match(input,GTEQ,FOLLOW_GTEQ_in_relationalExpression5847); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5853);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(GTEQ120)).Binary(JCTree.GE, expr, e); 
            	    }

            	    }
            	    break;
            	case 5 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:555:9: LT e= additiveExpression
            	    {
            	    LT121=(Token)input.LT(1);
            	    match(input,LT,FOLLOW_LT_in_relationalExpression5867); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5875);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(LT121))  .Binary(JCTree.LT, expr, e); 
            	    }

            	    }
            	    break;
            	case 6 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:556:9: GT e= additiveExpression
            	    {
            	    GT122=(Token)input.LT(1);
            	    match(input,GT,FOLLOW_GT_in_relationalExpression5889); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5897);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(GT122))  .Binary(JCTree.GT, expr, e); 
            	    }

            	    }
            	    break;
            	case 7 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:557:9: IN e= additiveExpression
            	    {
            	    IN123=(Token)input.LT(1);
            	    match(input,IN,FOLLOW_IN_in_relationalExpression5911); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5919);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       /* expr = F.at(pos(IN123  )).Binary(JavaFXTag.IN, expr, $e2.expr); */ 
            	    }

            	    }
            	    break;

            	default :
            	    break loop73;
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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:559:1: additiveExpression returns [JCExpression expr] : e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* ;
    public final JCExpression additiveExpression() throws RecognitionException {
        JCExpression expr = null;

        Token PLUS124=null;
        Token SUB125=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:560:4: (e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:560:4: e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            {
            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression5948);
            e1=multiplicativeExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:561:5: ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            loop74:
            do {
                int alt74=3;
                int LA74_0 = input.LA(1);

                if ( (LA74_0==PLUS) ) {
                    alt74=1;
                }
                else if ( (LA74_0==SUB) ) {
                    alt74=2;
                }


                switch (alt74) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:561:9: PLUS e= multiplicativeExpression
            	    {
            	    PLUS124=(Token)input.LT(1);
            	    match(input,PLUS,FOLLOW_PLUS_in_additiveExpression5963); if (failed) return expr;
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression5969);
            	    e=multiplicativeExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(PLUS124)).Binary(JCTree.PLUS , expr, e); 
            	    }

            	    }
            	    break;
            	case 2 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:562:9: SUB e= multiplicativeExpression
            	    {
            	    SUB125=(Token)input.LT(1);
            	    match(input,SUB,FOLLOW_SUB_in_additiveExpression5982); if (failed) return expr;
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression5989);
            	    e=multiplicativeExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(SUB125)) .Binary(JCTree.MINUS, expr, e); 
            	    }

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
    // $ANTLR end additiveExpression


    // $ANTLR start multiplicativeExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:564:1: multiplicativeExpression returns [JCExpression expr] : e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* ;
    public final JCExpression multiplicativeExpression() throws RecognitionException {
        JCExpression expr = null;

        Token STAR126=null;
        Token SLASH127=null;
        Token PERCENT128=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:565:4: (e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:565:4: e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            {
            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression6017);
            e1=unaryExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:566:5: ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            loop75:
            do {
                int alt75=4;
                switch ( input.LA(1) ) {
                case STAR:
                    {
                    alt75=1;
                    }
                    break;
                case SLASH:
                    {
                    alt75=2;
                    }
                    break;
                case PERCENT:
                    {
                    alt75=3;
                    }
                    break;

                }

                switch (alt75) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:566:9: STAR e= unaryExpression
            	    {
            	    STAR126=(Token)input.LT(1);
            	    match(input,STAR,FOLLOW_STAR_in_multiplicativeExpression6033); if (failed) return expr;
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression6040);
            	    e=unaryExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(STAR126))   .Binary(JCTree.MUL  , expr, e); 
            	    }

            	    }
            	    break;
            	case 2 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:567:9: SLASH e= unaryExpression
            	    {
            	    SLASH127=(Token)input.LT(1);
            	    match(input,SLASH,FOLLOW_SLASH_in_multiplicativeExpression6054); if (failed) return expr;
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression6060);
            	    e=unaryExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(SLASH127))  .Binary(JCTree.DIV  , expr, e); 
            	    }

            	    }
            	    break;
            	case 3 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:568:9: PERCENT e= unaryExpression
            	    {
            	    PERCENT128=(Token)input.LT(1);
            	    match(input,PERCENT,FOLLOW_PERCENT_in_multiplicativeExpression6074); if (failed) return expr;
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression6078);
            	    e=unaryExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(PERCENT128)).Binary(JCTree.MOD  , expr, e); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop75;
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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:570:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );
    public final JCExpression unaryExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression postfixExpression129 = null;

        int unaryOperator130 = 0;

        JCExpression postfixExpression131 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:571:4: ( postfixExpression | unaryOperator postfixExpression )
            int alt76=2;
            int LA76_0 = input.LA(1);

            if ( ((LA76_0>=THIS && LA76_0<=FALSE)||LA76_0==NEW||LA76_0==INDEXOF||LA76_0==SUPER||LA76_0==LPAREN||LA76_0==LBRACKET||LA76_0==DOT||LA76_0==STRING_LITERAL||LA76_0==QUOTE_LBRACE_STRING_LITERAL||(LA76_0>=QUOTED_IDENTIFIER && LA76_0<=INTEGER_LITERAL)||LA76_0==FLOATING_POINT_LITERAL||LA76_0==IDENTIFIER) ) {
                alt76=1;
            }
            else if ( ((LA76_0>=POUND && LA76_0<=TYPEOF)||LA76_0==NOT||(LA76_0>=SIZEOF && LA76_0<=REVERSE)||(LA76_0>=PLUSPLUS && LA76_0<=SUBSUB)||LA76_0==QUES) ) {
                alt76=2;
            }
            else {
                if (backtracking>0) {failed=true; return expr;}
                NoViableAltException nvae =
                    new NoViableAltException("570:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );", 76, 0, input);

                throw nvae;
            }
            switch (alt76) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:571:4: postfixExpression
                    {
                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression6108);
                    postfixExpression129=postfixExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = postfixExpression129; 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:572:4: unaryOperator postfixExpression
                    {
                    pushFollow(FOLLOW_unaryOperator_in_unaryExpression6119);
                    unaryOperator130=unaryOperator();
                    _fsp--;
                    if (failed) return expr;
                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression6123);
                    postfixExpression131=postfixExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.Unary(unaryOperator130, postfixExpression131); 
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
    // $ANTLR end unaryExpression


    // $ANTLR start postfixExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:574:1: postfixExpression returns [JCExpression expr] : primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* ;
    public final JCExpression postfixExpression() throws RecognitionException {
        JCExpression expr = null;

        Token DOT133=null;
        Token LPAREN134=null;
        name_return name1 = null;

        JCExpression primaryExpression132 = null;

        ListBuffer<JCExpression> expressionListOpt135 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:575:4: ( primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:575:4: primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            {
            pushFollow(FOLLOW_primaryExpression_in_postfixExpression6143);
            primaryExpression132=primaryExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = primaryExpression132; 
            }
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:576:5: ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            loop80:
            do {
                int alt80=3;
                int LA80_0 = input.LA(1);

                if ( (LA80_0==DOT) ) {
                    alt80=1;
                }
                else if ( (LA80_0==LBRACKET) ) {
                    alt80=2;
                }


                switch (alt80) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:576:7: DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    {
            	    DOT133=(Token)input.LT(1);
            	    match(input,DOT,FOLLOW_DOT_in_postfixExpression6158); if (failed) return expr;
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:576:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    int alt78=2;
            	    int LA78_0 = input.LA(1);

            	    if ( (LA78_0==CLASS) ) {
            	        alt78=1;
            	    }
            	    else if ( (LA78_0==QUOTED_IDENTIFIER||LA78_0==IDENTIFIER) ) {
            	        alt78=2;
            	    }
            	    else {
            	        if (backtracking>0) {failed=true; return expr;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("576:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )", 78, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt78) {
            	        case 1 :
            	            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:576:13: CLASS
            	            {
            	            match(input,CLASS,FOLLOW_CLASS_in_postfixExpression6162); if (failed) return expr;

            	            }
            	            break;
            	        case 2 :
            	            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:577:13: name1= name ( LPAREN expressionListOpt RPAREN )*
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression6186);
            	            name1=name();
            	            _fsp--;
            	            if (failed) return expr;
            	            if ( backtracking==0 ) {
            	               expr = F.at(pos(DOT133)).Select(expr, name1.value); 
            	            }
            	            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:578:14: ( LPAREN expressionListOpt RPAREN )*
            	            loop77:
            	            do {
            	                int alt77=2;
            	                int LA77_0 = input.LA(1);

            	                if ( (LA77_0==LPAREN) ) {
            	                    alt77=1;
            	                }


            	                switch (alt77) {
            	            	case 1 :
            	            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:578:16: LPAREN expressionListOpt RPAREN
            	            	    {
            	            	    LPAREN134=(Token)input.LT(1);
            	            	    match(input,LPAREN,FOLLOW_LPAREN_in_postfixExpression6211); if (failed) return expr;
            	            	    pushFollow(FOLLOW_expressionListOpt_in_postfixExpression6213);
            	            	    expressionListOpt135=expressionListOpt();
            	            	    _fsp--;
            	            	    if (failed) return expr;
            	            	    match(input,RPAREN,FOLLOW_RPAREN_in_postfixExpression6215); if (failed) return expr;
            	            	    if ( backtracking==0 ) {
            	            	       expr = F.at(pos(LPAREN134)).Apply(null, expr, expressionListOpt135.toList()); 
            	            	    }

            	            	    }
            	            	    break;

            	            	default :
            	            	    break loop77;
            	                }
            	            } while (true);


            	            }
            	            break;

            	    }


            	    }
            	    break;
            	case 2 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:580:7: LBRACKET ( name BAR )? expression RBRACKET
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_postfixExpression6247); if (failed) return expr;
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:580:16: ( name BAR )?
            	    int alt79=2;
            	    int LA79_0 = input.LA(1);

            	    if ( (LA79_0==QUOTED_IDENTIFIER||LA79_0==IDENTIFIER) ) {
            	        int LA79_1 = input.LA(2);

            	        if ( (LA79_1==BAR) ) {
            	            alt79=1;
            	        }
            	    }
            	    switch (alt79) {
            	        case 1 :
            	            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:580:17: name BAR
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression6250);
            	            name();
            	            _fsp--;
            	            if (failed) return expr;
            	            match(input,BAR,FOLLOW_BAR_in_postfixExpression6252); if (failed) return expr;

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_expression_in_postfixExpression6256);
            	    expression();
            	    _fsp--;
            	    if (failed) return expr;
            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_postfixExpression6259); if (failed) return expr;

            	    }
            	    break;

            	default :
            	    break loop80;
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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:582:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | bracketExpression | ordinalExpression | contextExpression | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | LPAREN expression RPAREN );
    public final JCExpression primaryExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LBRACE137=null;
        Token THIS140=null;
        Token SUPER141=null;
        Token LPAREN143=null;
        Token LPAREN147=null;
        JCExpression newExpression136 = null;

        JCIdent identifier138 = null;

        ListBuffer<JFXStatement> objectLiteral139 = null;

        JCIdent identifier142 = null;

        ListBuffer<JCExpression> expressionListOpt144 = null;

        JCExpression stringExpression145 = null;

        JCExpression literal146 = null;

        JCExpression expression148 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:583:4: ( newExpression | identifier LBRACE objectLiteral RBRACE | bracketExpression | ordinalExpression | contextExpression | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | LPAREN expression RPAREN )
            int alt82=11;
            switch ( input.LA(1) ) {
            case NEW:
                {
                alt82=1;
                }
                break;
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                int LA82_2 = input.LA(2);

                if ( (synpred143()) ) {
                    alt82=2;
                }
                else if ( (synpred150()) ) {
                    alt82=8;
                }
                else {
                    if (backtracking>0) {failed=true; return expr;}
                    NoViableAltException nvae =
                        new NoViableAltException("582:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | bracketExpression | ordinalExpression | contextExpression | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | LPAREN expression RPAREN );", 82, 2, input);

                    throw nvae;
                }
                }
                break;
            case LBRACKET:
                {
                alt82=3;
                }
                break;
            case INDEXOF:
                {
                alt82=4;
                }
                break;
            case DOT:
                {
                alt82=5;
                }
                break;
            case THIS:
                {
                alt82=6;
                }
                break;
            case SUPER:
                {
                alt82=7;
                }
                break;
            case QUOTE_LBRACE_STRING_LITERAL:
                {
                alt82=9;
                }
                break;
            case NULL:
            case TRUE:
            case FALSE:
            case STRING_LITERAL:
            case INTEGER_LITERAL:
            case FLOATING_POINT_LITERAL:
                {
                alt82=10;
                }
                break;
            case LPAREN:
                {
                alt82=11;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return expr;}
                NoViableAltException nvae =
                    new NoViableAltException("582:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | bracketExpression | ordinalExpression | contextExpression | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | LPAREN expression RPAREN );", 82, 0, input);

                throw nvae;
            }

            switch (alt82) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:583:4: newExpression
                    {
                    pushFollow(FOLLOW_newExpression_in_primaryExpression6284);
                    newExpression136=newExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = newExpression136; 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:584:4: identifier LBRACE objectLiteral RBRACE
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression6296);
                    identifier138=identifier();
                    _fsp--;
                    if (failed) return expr;
                    LBRACE137=(Token)input.LT(1);
                    match(input,LBRACE,FOLLOW_LBRACE_in_primaryExpression6298); if (failed) return expr;
                    pushFollow(FOLLOW_objectLiteral_in_primaryExpression6301);
                    objectLiteral139=objectLiteral();
                    _fsp--;
                    if (failed) return expr;
                    match(input,RBRACE,FOLLOW_RBRACE_in_primaryExpression6303); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(LBRACE137)).PureObjectLiteral(identifier138, objectLiteral139.toList()); 
                    }

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:585:4: bracketExpression
                    {
                    pushFollow(FOLLOW_bracketExpression_in_primaryExpression6313);
                    bracketExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:587:4: ordinalExpression
                    {
                    pushFollow(FOLLOW_ordinalExpression_in_primaryExpression6328);
                    ordinalExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:588:10: contextExpression
                    {
                    pushFollow(FOLLOW_contextExpression_in_primaryExpression6340);
                    contextExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 6 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:589:10: THIS
                    {
                    THIS140=(Token)input.LT(1);
                    match(input,THIS,FOLLOW_THIS_in_primaryExpression6352); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(THIS140)).Identifier(names._this); 
                    }

                    }
                    break;
                case 7 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:590:10: SUPER
                    {
                    SUPER141=(Token)input.LT(1);
                    match(input,SUPER,FOLLOW_SUPER_in_primaryExpression6371); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(SUPER141)).Identifier(names._super); 
                    }

                    }
                    break;
                case 8 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:591:10: identifier ( LPAREN expressionListOpt RPAREN )*
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression6390);
                    identifier142=identifier();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = identifier142; 
                    }
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:592:10: ( LPAREN expressionListOpt RPAREN )*
                    loop81:
                    do {
                        int alt81=2;
                        int LA81_0 = input.LA(1);

                        if ( (LA81_0==LPAREN) ) {
                            alt81=1;
                        }


                        switch (alt81) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:592:12: LPAREN expressionListOpt RPAREN
                    	    {
                    	    LPAREN143=(Token)input.LT(1);
                    	    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression6411); if (failed) return expr;
                    	    pushFollow(FOLLOW_expressionListOpt_in_primaryExpression6415);
                    	    expressionListOpt144=expressionListOpt();
                    	    _fsp--;
                    	    if (failed) return expr;
                    	    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression6419); if (failed) return expr;
                    	    if ( backtracking==0 ) {
                    	       expr = F.at(pos(LPAREN143)).Apply(null, expr, expressionListOpt144.toList()); 
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop81;
                        }
                    } while (true);


                    }
                    break;
                case 9 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:593:10: stringExpression
                    {
                    pushFollow(FOLLOW_stringExpression_in_primaryExpression6438);
                    stringExpression145=stringExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = stringExpression145; 
                    }

                    }
                    break;
                case 10 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:594:10: literal
                    {
                    pushFollow(FOLLOW_literal_in_primaryExpression6456);
                    literal146=literal();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = literal146; 
                    }

                    }
                    break;
                case 11 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:595:10: LPAREN expression RPAREN
                    {
                    LPAREN147=(Token)input.LT(1);
                    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression6475); if (failed) return expr;
                    pushFollow(FOLLOW_expression_in_primaryExpression6477);
                    expression148=expression();
                    _fsp--;
                    if (failed) return expr;
                    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression6479); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(LPAREN147)).Parens(expression148); 
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
    // $ANTLR end primaryExpression


    // $ANTLR start newExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:597:1: newExpression returns [JCExpression expr] : NEW identifier ( LPAREN expressionListOpt RPAREN )? ;
    public final JCExpression newExpression() throws RecognitionException {
        JCExpression expr = null;

        Token NEW150=null;
        ListBuffer<JCExpression> expressionListOpt149 = null;

        JCIdent identifier151 = null;


         ListBuffer<JCExpression> args = null; 
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:599:4: ( NEW identifier ( LPAREN expressionListOpt RPAREN )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:599:4: NEW identifier ( LPAREN expressionListOpt RPAREN )?
            {
            NEW150=(Token)input.LT(1);
            match(input,NEW,FOLLOW_NEW_in_newExpression6511); if (failed) return expr;
            pushFollow(FOLLOW_identifier_in_newExpression6514);
            identifier151=identifier();
            _fsp--;
            if (failed) return expr;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:600:3: ( LPAREN expressionListOpt RPAREN )?
            int alt83=2;
            int LA83_0 = input.LA(1);

            if ( (LA83_0==LPAREN) ) {
                alt83=1;
            }
            switch (alt83) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:600:5: LPAREN expressionListOpt RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_newExpression6522); if (failed) return expr;
                    pushFollow(FOLLOW_expressionListOpt_in_newExpression6526);
                    expressionListOpt149=expressionListOpt();
                    _fsp--;
                    if (failed) return expr;
                    match(input,RPAREN,FOLLOW_RPAREN_in_newExpression6530); if (failed) return expr;
                    if ( backtracking==0 ) {
                       args = expressionListOpt149; 
                    }

                    }
                    break;

            }

            if ( backtracking==0 ) {
               expr = F.at(pos(NEW150)).NewClass(null, null, identifier151, 
              												(args==null? new ListBuffer<JCExpression>() : args).toList(), null); 
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
    // $ANTLR end newExpression


    // $ANTLR start objectLiteral
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:605:1: objectLiteral returns [ListBuffer<JFXStatement> parts = new ListBuffer<JFXStatement>()] : ( objectLiteralPart )* ;
    public final ListBuffer<JFXStatement> objectLiteral() throws RecognitionException {
        ListBuffer<JFXStatement> parts =  new ListBuffer<JFXStatement>();

        JFXStatement objectLiteralPart152 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:606:4: ( ( objectLiteralPart )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:606:4: ( objectLiteralPart )*
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:606:4: ( objectLiteralPart )*
            loop84:
            do {
                int alt84=2;
                int LA84_0 = input.LA(1);

                if ( (LA84_0==ATTRIBUTE||LA84_0==VAR||LA84_0==TRIGGER||(LA84_0>=OPERATION && LA84_0<=FUNCTION)||LA84_0==QUOTED_IDENTIFIER||LA84_0==IDENTIFIER) ) {
                    alt84=1;
                }


                switch (alt84) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:606:6: objectLiteralPart
            	    {
            	    pushFollow(FOLLOW_objectLiteralPart_in_objectLiteral6570);
            	    objectLiteralPart152=objectLiteralPart();
            	    _fsp--;
            	    if (failed) return parts;
            	    if ( backtracking==0 ) {
            	       parts.append(objectLiteralPart152); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop84;
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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:607:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | ATTRIBUTE name typeReference EQ bindOpt expression SEMI | localOperationDefinition | localFunctionDefinition | localTriggerStatement | variableDefinition );
    public final JFXStatement objectLiteralPart() throws RecognitionException {
        JFXStatement value = null;

        Token COLON153=null;
        name_return name154 = null;

        JCExpression expression155 = null;

        JavafxBindStatus bindOpt156 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:608:4: ( name COLON bindOpt expression ( COMMA | SEMI )? | ATTRIBUTE name typeReference EQ bindOpt expression SEMI | localOperationDefinition | localFunctionDefinition | localTriggerStatement | variableDefinition )
            int alt86=6;
            switch ( input.LA(1) ) {
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                int LA86_1 = input.LA(2);

                if ( (LA86_1==COLON) ) {
                    alt86=1;
                }
                else if ( (LA86_1==LPAREN) ) {
                    alt86=4;
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("607:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | ATTRIBUTE name typeReference EQ bindOpt expression SEMI | localOperationDefinition | localFunctionDefinition | localTriggerStatement | variableDefinition );", 86, 1, input);

                    throw nvae;
                }
                }
                break;
            case ATTRIBUTE:
                {
                alt86=2;
                }
                break;
            case OPERATION:
                {
                alt86=3;
                }
                break;
            case FUNCTION:
                {
                alt86=4;
                }
                break;
            case TRIGGER:
                {
                alt86=5;
                }
                break;
            case VAR:
                {
                alt86=6;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("607:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | ATTRIBUTE name typeReference EQ bindOpt expression SEMI | localOperationDefinition | localFunctionDefinition | localTriggerStatement | variableDefinition );", 86, 0, input);

                throw nvae;
            }

            switch (alt86) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:608:4: name COLON bindOpt expression ( COMMA | SEMI )?
                    {
                    pushFollow(FOLLOW_name_in_objectLiteralPart6596);
                    name154=name();
                    _fsp--;
                    if (failed) return value;
                    COLON153=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_objectLiteralPart6598); if (failed) return value;
                    pushFollow(FOLLOW_bindOpt_in_objectLiteralPart6601);
                    bindOpt156=bindOpt();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_expression_in_objectLiteralPart6603);
                    expression155=expression();
                    _fsp--;
                    if (failed) return value;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:608:35: ( COMMA | SEMI )?
                    int alt85=2;
                    int LA85_0 = input.LA(1);

                    if ( ((LA85_0>=SEMI && LA85_0<=COMMA)) ) {
                        alt85=1;
                    }
                    switch (alt85) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
                            {
                            if ( (input.LA(1)>=SEMI && input.LA(1)<=COMMA) ) {
                                input.consume();
                                errorRecovery=false;failed=false;
                            }
                            else {
                                if (backtracking>0) {failed=true; return value;}
                                MismatchedSetException mse =
                                    new MismatchedSetException(null,input);
                                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_objectLiteralPart6605);    throw mse;
                            }


                            }
                            break;

                    }

                    if ( backtracking==0 ) {
                       value = F.at(pos(COLON153)).ObjectLiteralPart(name154.value, expression155, bindOpt156); 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:609:10: ATTRIBUTE name typeReference EQ bindOpt expression SEMI
                    {
                    match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_objectLiteralPart6625); if (failed) return value;
                    pushFollow(FOLLOW_name_in_objectLiteralPart6629);
                    name();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_typeReference_in_objectLiteralPart6633);
                    typeReference();
                    _fsp--;
                    if (failed) return value;
                    match(input,EQ,FOLLOW_EQ_in_objectLiteralPart6637); if (failed) return value;
                    pushFollow(FOLLOW_bindOpt_in_objectLiteralPart6640);
                    bindOpt();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_expression_in_objectLiteralPart6642);
                    expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_objectLiteralPart6646); if (failed) return value;

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:610:10: localOperationDefinition
                    {
                    pushFollow(FOLLOW_localOperationDefinition_in_objectLiteralPart6658);
                    localOperationDefinition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:611:10: localFunctionDefinition
                    {
                    pushFollow(FOLLOW_localFunctionDefinition_in_objectLiteralPart6670);
                    localFunctionDefinition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:612:10: localTriggerStatement
                    {
                    pushFollow(FOLLOW_localTriggerStatement_in_objectLiteralPart6682);
                    localTriggerStatement();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 6 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:613:10: variableDefinition
                    {
                    pushFollow(FOLLOW_variableDefinition_in_objectLiteralPart6694);
                    variableDefinition();
                    _fsp--;
                    if (failed) return value;

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


    // $ANTLR start bracketExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:614:1: bracketExpression : LBRACKET ( generatorClause | dotDotClause | expressionListOpt ) RBRACKET ;
    public final void bracketExpression() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:614:21: ( LBRACKET ( generatorClause | dotDotClause | expressionListOpt ) RBRACKET )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:614:21: LBRACKET ( generatorClause | dotDotClause | expressionListOpt ) RBRACKET
            {
            match(input,LBRACKET,FOLLOW_LBRACKET_in_bracketExpression6702); if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:614:32: ( generatorClause | dotDotClause | expressionListOpt )
            int alt87=3;
            switch ( input.LA(1) ) {
            case FOREACH:
                {
                int LA87_1 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 1, input);

                    throw nvae;
                }
                }
                break;
            case FUNCTION:
                {
                int LA87_2 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 2, input);

                    throw nvae;
                }
                }
                break;
            case OPERATION:
                {
                int LA87_3 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 3, input);

                    throw nvae;
                }
                }
                break;
            case UNITINTERVAL:
                {
                int LA87_4 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 4, input);

                    throw nvae;
                }
                }
                break;
            case IF:
                {
                int LA87_5 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 5, input);

                    throw nvae;
                }
                }
                break;
            case SELECT:
                {
                int LA87_6 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 6, input);

                    throw nvae;
                }
                }
                break;
            case LPAREN:
                {
                int LA87_7 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 7, input);

                    throw nvae;
                }
                }
                break;
            case NEW:
                {
                int LA87_8 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 8, input);

                    throw nvae;
                }
                }
                break;
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                int LA87_9 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 9, input);

                    throw nvae;
                }
                }
                break;
            case LBRACKET:
                {
                int LA87_10 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 10, input);

                    throw nvae;
                }
                }
                break;
            case INDEXOF:
                {
                int LA87_11 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 11, input);

                    throw nvae;
                }
                }
                break;
            case DOT:
                {
                int LA87_12 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 12, input);

                    throw nvae;
                }
                }
                break;
            case THIS:
                {
                int LA87_13 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 13, input);

                    throw nvae;
                }
                }
                break;
            case SUPER:
                {
                int LA87_14 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 14, input);

                    throw nvae;
                }
                }
                break;
            case QUOTE_LBRACE_STRING_LITERAL:
                {
                int LA87_15 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 15, input);

                    throw nvae;
                }
                }
                break;
            case STRING_LITERAL:
                {
                int LA87_16 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 16, input);

                    throw nvae;
                }
                }
                break;
            case INTEGER_LITERAL:
                {
                int LA87_17 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 17, input);

                    throw nvae;
                }
                }
                break;
            case FLOATING_POINT_LITERAL:
                {
                int LA87_18 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 18, input);

                    throw nvae;
                }
                }
                break;
            case TRUE:
                {
                int LA87_19 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 19, input);

                    throw nvae;
                }
                }
                break;
            case FALSE:
                {
                int LA87_20 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 20, input);

                    throw nvae;
                }
                }
                break;
            case NULL:
                {
                int LA87_21 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 21, input);

                    throw nvae;
                }
                }
                break;
            case POUND:
                {
                int LA87_22 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 22, input);

                    throw nvae;
                }
                }
                break;
            case QUES:
                {
                int LA87_23 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 23, input);

                    throw nvae;
                }
                }
                break;
            case SUB:
                {
                int LA87_24 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 24, input);

                    throw nvae;
                }
                }
                break;
            case NOT:
                {
                int LA87_25 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 25, input);

                    throw nvae;
                }
                }
                break;
            case SIZEOF:
                {
                int LA87_26 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 26, input);

                    throw nvae;
                }
                }
                break;
            case TYPEOF:
                {
                int LA87_27 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 27, input);

                    throw nvae;
                }
                }
                break;
            case REVERSE:
                {
                int LA87_28 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 28, input);

                    throw nvae;
                }
                }
                break;
            case PLUSPLUS:
                {
                int LA87_29 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 29, input);

                    throw nvae;
                }
                }
                break;
            case SUBSUB:
                {
                int LA87_30 = input.LA(2);

                if ( (synpred162()) ) {
                    alt87=1;
                }
                else if ( (synpred163()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 30, input);

                    throw nvae;
                }
                }
                break;
            case RBRACKET:
                {
                alt87=3;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("614:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 0, input);

                throw nvae;
            }

            switch (alt87) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:614:36: generatorClause
                    {
                    pushFollow(FOLLOW_generatorClause_in_bracketExpression6710);
                    generatorClause();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:614:58: dotDotClause
                    {
                    pushFollow(FOLLOW_dotDotClause_in_bracketExpression6718);
                    dotDotClause();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:614:77: expressionListOpt
                    {
                    pushFollow(FOLLOW_expressionListOpt_in_bracketExpression6726);
                    expressionListOpt();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,RBRACKET,FOLLOW_RBRACKET_in_bracketExpression6734); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end bracketExpression


    // $ANTLR start dotDotClause
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:615:1: dotDotClause : expression ( COMMA expression )? DOTDOT ( LT )? expression ;
    public final void dotDotClause() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:615:16: ( expression ( COMMA expression )? DOTDOT ( LT )? expression )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:615:16: expression ( COMMA expression )? DOTDOT ( LT )? expression
            {
            pushFollow(FOLLOW_expression_in_dotDotClause6742);
            expression();
            _fsp--;
            if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:615:29: ( COMMA expression )?
            int alt88=2;
            int LA88_0 = input.LA(1);

            if ( (LA88_0==COMMA) ) {
                alt88=1;
            }
            switch (alt88) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:615:33: COMMA expression
                    {
                    match(input,COMMA,FOLLOW_COMMA_in_dotDotClause6750); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_dotDotClause6754);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,DOTDOT,FOLLOW_DOTDOT_in_dotDotClause6764); if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:615:69: ( LT )?
            int alt89=2;
            int LA89_0 = input.LA(1);

            if ( (LA89_0==LT) ) {
                alt89=1;
            }
            switch (alt89) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: LT
                    {
                    match(input,LT,FOLLOW_LT_in_dotDotClause6768); if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_expression_in_dotDotClause6774);
            expression();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end dotDotClause


    // $ANTLR start generatorClause
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:616:1: generatorClause : expression BAR generator ( COMMA ( generator | expression ) )* ;
    public final void generatorClause() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:616:19: ( expression BAR generator ( COMMA ( generator | expression ) )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:616:19: expression BAR generator ( COMMA ( generator | expression ) )*
            {
            pushFollow(FOLLOW_expression_in_generatorClause6782);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,BAR,FOLLOW_BAR_in_generatorClause6786); if (failed) return ;
            pushFollow(FOLLOW_generator_in_generatorClause6790);
            generator();
            _fsp--;
            if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:616:50: ( COMMA ( generator | expression ) )*
            loop91:
            do {
                int alt91=2;
                int LA91_0 = input.LA(1);

                if ( (LA91_0==COMMA) ) {
                    alt91=1;
                }


                switch (alt91) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:616:56: COMMA ( generator | expression )
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_generatorClause6800); if (failed) return ;
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:616:64: ( generator | expression )
            	    int alt90=2;
            	    int LA90_0 = input.LA(1);

            	    if ( (LA90_0==QUOTED_IDENTIFIER||LA90_0==IDENTIFIER) ) {
            	        int LA90_1 = input.LA(2);

            	        if ( (LA90_1==LARROW) ) {
            	            alt90=1;
            	        }
            	        else if ( (LA90_1==EOF||LA90_1==AND||LA90_1==DUR||LA90_1==IN||(LA90_1>=ORDER && LA90_1<=INSTANCEOF)||LA90_1==OR||LA90_1==LPAREN||(LA90_1>=LBRACKET && LA90_1<=RBRACKET)||(LA90_1>=COMMA && LA90_1<=PERCENTEQ)||LA90_1==LBRACE) ) {
            	            alt90=2;
            	        }
            	        else {
            	            if (backtracking>0) {failed=true; return ;}
            	            NoViableAltException nvae =
            	                new NoViableAltException("616:64: ( generator | expression )", 90, 1, input);

            	            throw nvae;
            	        }
            	    }
            	    else if ( ((LA90_0>=POUND && LA90_0<=TYPEOF)||LA90_0==IF||(LA90_0>=THIS && LA90_0<=FALSE)||LA90_0==UNITINTERVAL||LA90_0==FOREACH||(LA90_0>=NOT && LA90_0<=NEW)||(LA90_0>=OPERATION && LA90_0<=FUNCTION)||(LA90_0>=INDEXOF && LA90_0<=SUPER)||(LA90_0>=SIZEOF && LA90_0<=REVERSE)||LA90_0==LPAREN||LA90_0==LBRACKET||LA90_0==DOT||(LA90_0>=PLUSPLUS && LA90_0<=SUBSUB)||(LA90_0>=QUES && LA90_0<=STRING_LITERAL)||LA90_0==QUOTE_LBRACE_STRING_LITERAL||LA90_0==INTEGER_LITERAL||LA90_0==FLOATING_POINT_LITERAL) ) {
            	        alt90=2;
            	    }
            	    else {
            	        if (backtracking>0) {failed=true; return ;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("616:64: ( generator | expression )", 90, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt90) {
            	        case 1 :
            	            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:616:68: generator
            	            {
            	            pushFollow(FOLLOW_generator_in_generatorClause6808);
            	            generator();
            	            _fsp--;
            	            if (failed) return ;

            	            }
            	            break;
            	        case 2 :
            	            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:616:84: expression
            	            {
            	            pushFollow(FOLLOW_expression_in_generatorClause6816);
            	            expression();
            	            _fsp--;
            	            if (failed) return ;

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop91;
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
        return ;
    }
    // $ANTLR end generatorClause


    // $ANTLR start generator
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:617:1: generator : name LARROW expression ;
    public final void generator() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:617:13: ( name LARROW expression )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:617:13: name LARROW expression
            {
            pushFollow(FOLLOW_name_in_generator6836);
            name();
            _fsp--;
            if (failed) return ;
            match(input,LARROW,FOLLOW_LARROW_in_generator6840); if (failed) return ;
            pushFollow(FOLLOW_expression_in_generator6844);
            expression();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end generator


    // $ANTLR start ordinalExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:618:1: ordinalExpression : INDEXOF ( name | DOT ) ;
    public final void ordinalExpression() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:618:21: ( INDEXOF ( name | DOT ) )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:618:21: INDEXOF ( name | DOT )
            {
            match(input,INDEXOF,FOLLOW_INDEXOF_in_ordinalExpression6852); if (failed) return ;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:618:31: ( name | DOT )
            int alt92=2;
            int LA92_0 = input.LA(1);

            if ( (LA92_0==QUOTED_IDENTIFIER||LA92_0==IDENTIFIER) ) {
                alt92=1;
            }
            else if ( (LA92_0==DOT) ) {
                alt92=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("618:31: ( name | DOT )", 92, 0, input);

                throw nvae;
            }
            switch (alt92) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:618:35: name
                    {
                    pushFollow(FOLLOW_name_in_ordinalExpression6860);
                    name();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:618:46: DOT
                    {
                    match(input,DOT,FOLLOW_DOT_in_ordinalExpression6868); if (failed) return ;

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
        return ;
    }
    // $ANTLR end ordinalExpression


    // $ANTLR start contextExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:619:1: contextExpression : DOT ;
    public final void contextExpression() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:619:21: ( DOT )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:619:21: DOT
            {
            match(input,DOT,FOLLOW_DOT_in_contextExpression6880); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end contextExpression


    // $ANTLR start stringExpression
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:620:1: stringExpression returns [JCExpression expr] : ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL ;
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
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:622:4: (ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:622:4: ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL
            {
            ql=(Token)input.LT(1);
            match(input,QUOTE_LBRACE_STRING_LITERAL,FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression6902); if (failed) return expr;
            if ( backtracking==0 ) {
               strexp.append(F.at(pos(ql)).Literal(TypeTags.CLASS, ql.getText())); 
            }
            pushFollow(FOLLOW_formatOrNull_in_stringExpression6911);
            f1=formatOrNull();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               strexp.append(f1); 
            }
            pushFollow(FOLLOW_expression_in_stringExpression6922);
            e1=expression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               strexp.append(e1); 
            }
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:625:4: (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )*
            loop93:
            do {
                int alt93=2;
                int LA93_0 = input.LA(1);

                if ( (LA93_0==RBRACE_LBRACE_STRING_LITERAL) ) {
                    alt93=1;
                }


                switch (alt93) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:625:7: rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression
            	    {
            	    rl=(Token)input.LT(1);
            	    match(input,RBRACE_LBRACE_STRING_LITERAL,FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression6937); if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       strexp.append(F.at(pos(rl)).Literal(TypeTags.CLASS, rl.getText())); 
            	    }
            	    pushFollow(FOLLOW_formatOrNull_in_stringExpression6949);
            	    fn=formatOrNull();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       strexp.append(fn); 
            	    }
            	    pushFollow(FOLLOW_expression_in_stringExpression6963);
            	    en=expression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       strexp.append(en); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop93;
                }
            } while (true);

            rq=(Token)input.LT(1);
            match(input,RBRACE_QUOTE_STRING_LITERAL,FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression6984); if (failed) return expr;
            if ( backtracking==0 ) {
               strexp.append(F.at(pos(rq)).Literal(TypeTags.CLASS, rq.getText())); 
            }
            if ( backtracking==0 ) {
               expr = F.at(pos(ql)).StringExpression(strexp.toList()); 
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
    // $ANTLR end stringExpression


    // $ANTLR start formatOrNull
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:632:1: formatOrNull returns [JCExpression expr] : (fs= FORMAT_STRING_LITERAL | );
    public final JCExpression formatOrNull() throws RecognitionException {
        JCExpression expr = null;

        Token fs=null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:633:4: (fs= FORMAT_STRING_LITERAL | )
            int alt94=2;
            int LA94_0 = input.LA(1);

            if ( (LA94_0==FORMAT_STRING_LITERAL) ) {
                alt94=1;
            }
            else if ( ((LA94_0>=POUND && LA94_0<=TYPEOF)||LA94_0==IF||(LA94_0>=THIS && LA94_0<=FALSE)||LA94_0==UNITINTERVAL||LA94_0==FOREACH||(LA94_0>=NOT && LA94_0<=NEW)||(LA94_0>=OPERATION && LA94_0<=FUNCTION)||(LA94_0>=INDEXOF && LA94_0<=SUPER)||(LA94_0>=SIZEOF && LA94_0<=REVERSE)||LA94_0==LPAREN||LA94_0==LBRACKET||LA94_0==DOT||(LA94_0>=PLUSPLUS && LA94_0<=SUBSUB)||(LA94_0>=QUES && LA94_0<=STRING_LITERAL)||LA94_0==QUOTE_LBRACE_STRING_LITERAL||(LA94_0>=QUOTED_IDENTIFIER && LA94_0<=INTEGER_LITERAL)||LA94_0==FLOATING_POINT_LITERAL||LA94_0==IDENTIFIER) ) {
                alt94=2;
            }
            else {
                if (backtracking>0) {failed=true; return expr;}
                NoViableAltException nvae =
                    new NoViableAltException("632:1: formatOrNull returns [JCExpression expr] : (fs= FORMAT_STRING_LITERAL | );", 94, 0, input);

                throw nvae;
            }
            switch (alt94) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:633:4: fs= FORMAT_STRING_LITERAL
                    {
                    fs=(Token)input.LT(1);
                    match(input,FORMAT_STRING_LITERAL,FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull7014); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(fs)).Literal(TypeTags.CLASS, fs.getText()); 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:634:22: 
                    {
                    if ( backtracking==0 ) {
                       expr = null; 
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
    // $ANTLR end formatOrNull


    // $ANTLR start expressionListOpt
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:636:1: expressionListOpt returns [ListBuffer<JCExpression> args = new ListBuffer<JCExpression>()] : (e1= expression ( COMMA e= expression )* )? ;
    public final ListBuffer<JCExpression> expressionListOpt() throws RecognitionException {
        ListBuffer<JCExpression> args =  new ListBuffer<JCExpression>();

        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:637:4: ( (e1= expression ( COMMA e= expression )* )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:637:4: (e1= expression ( COMMA e= expression )* )?
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:637:4: (e1= expression ( COMMA e= expression )* )?
            int alt96=2;
            int LA96_0 = input.LA(1);

            if ( ((LA96_0>=POUND && LA96_0<=TYPEOF)||LA96_0==IF||(LA96_0>=THIS && LA96_0<=FALSE)||LA96_0==UNITINTERVAL||LA96_0==FOREACH||(LA96_0>=NOT && LA96_0<=NEW)||(LA96_0>=OPERATION && LA96_0<=FUNCTION)||(LA96_0>=INDEXOF && LA96_0<=SUPER)||(LA96_0>=SIZEOF && LA96_0<=REVERSE)||LA96_0==LPAREN||LA96_0==LBRACKET||LA96_0==DOT||(LA96_0>=PLUSPLUS && LA96_0<=SUBSUB)||(LA96_0>=QUES && LA96_0<=STRING_LITERAL)||LA96_0==QUOTE_LBRACE_STRING_LITERAL||(LA96_0>=QUOTED_IDENTIFIER && LA96_0<=INTEGER_LITERAL)||LA96_0==FLOATING_POINT_LITERAL||LA96_0==IDENTIFIER) ) {
                alt96=1;
            }
            switch (alt96) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:637:6: e1= expression ( COMMA e= expression )*
                    {
                    pushFollow(FOLLOW_expression_in_expressionListOpt7046);
                    e1=expression();
                    _fsp--;
                    if (failed) return args;
                    if ( backtracking==0 ) {
                       args.append(e1); 
                    }
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:638:6: ( COMMA e= expression )*
                    loop95:
                    do {
                        int alt95=2;
                        int LA95_0 = input.LA(1);

                        if ( (LA95_0==COMMA) ) {
                            alt95=1;
                        }


                        switch (alt95) {
                    	case 1 :
                    	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:638:7: COMMA e= expression
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_expressionListOpt7057); if (failed) return args;
                    	    pushFollow(FOLLOW_expression_in_expressionListOpt7063);
                    	    e=expression();
                    	    _fsp--;
                    	    if (failed) return args;
                    	    if ( backtracking==0 ) {
                    	       args.append(e); 
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop95;
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


    // $ANTLR start orderBy
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:639:1: orderBy returns [JCExpression expr] : ORDER BY expression ;
    public final JCExpression orderBy() throws RecognitionException {
        JCExpression expr = null;

        JCExpression expression157 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:640:4: ( ORDER BY expression )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:640:4: ORDER BY expression
            {
            match(input,ORDER,FOLLOW_ORDER_in_orderBy7085); if (failed) return expr;
            match(input,BY,FOLLOW_BY_in_orderBy7089); if (failed) return expr;
            pushFollow(FOLLOW_expression_in_orderBy7093);
            expression157=expression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = expression157; 
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
    // $ANTLR end orderBy


    // $ANTLR start indexOn
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:641:1: indexOn returns [JCExpression expr = null] : INDEX ON name ( COMMA name )* ;
    public final JCExpression indexOn() throws RecognitionException {
        JCExpression expr =  null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:642:4: ( INDEX ON name ( COMMA name )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:642:4: INDEX ON name ( COMMA name )*
            {
            match(input,INDEX,FOLLOW_INDEX_in_indexOn7108); if (failed) return expr;
            match(input,ON,FOLLOW_ON_in_indexOn7112); if (failed) return expr;
            pushFollow(FOLLOW_name_in_indexOn7116);
            name();
            _fsp--;
            if (failed) return expr;
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:642:24: ( COMMA name )*
            loop97:
            do {
                int alt97=2;
                int LA97_0 = input.LA(1);

                if ( (LA97_0==COMMA) ) {
                    int LA97_2 = input.LA(2);

                    if ( (LA97_2==QUOTED_IDENTIFIER||LA97_2==IDENTIFIER) ) {
                        int LA97_3 = input.LA(3);

                        if ( (synpred173()) ) {
                            alt97=1;
                        }


                    }


                }


                switch (alt97) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:642:28: COMMA name
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_indexOn7124); if (failed) return expr;
            	    pushFollow(FOLLOW_name_in_indexOn7128);
            	    name();
            	    _fsp--;
            	    if (failed) return expr;

            	    }
            	    break;

            	default :
            	    break loop97;
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
    // $ANTLR end indexOn


    // $ANTLR start multiplyOperator
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:643:1: multiplyOperator : ( STAR | SLASH | PERCENT );
    public final void multiplyOperator() throws RecognitionException {
        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:643:20: ( STAR | SLASH | PERCENT )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
            {
            if ( (input.LA(1)>=STAR && input.LA(1)<=PERCENT) ) {
                input.consume();
                errorRecovery=false;failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_multiplyOperator0);    throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end multiplyOperator


    // $ANTLR start unaryOperator
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:644:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );
    public final int unaryOperator() throws RecognitionException {
        int optag = 0;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:645:4: ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB )
            int alt98=9;
            switch ( input.LA(1) ) {
            case POUND:
                {
                alt98=1;
                }
                break;
            case QUES:
                {
                alt98=2;
                }
                break;
            case SUB:
                {
                alt98=3;
                }
                break;
            case NOT:
                {
                alt98=4;
                }
                break;
            case SIZEOF:
                {
                alt98=5;
                }
                break;
            case TYPEOF:
                {
                alt98=6;
                }
                break;
            case REVERSE:
                {
                alt98=7;
                }
                break;
            case PLUSPLUS:
                {
                alt98=8;
                }
                break;
            case SUBSUB:
                {
                alt98=9;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return optag;}
                NoViableAltException nvae =
                    new NoViableAltException("644:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );", 98, 0, input);

                throw nvae;
            }

            switch (alt98) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:645:4: POUND
                    {
                    match(input,POUND,FOLLOW_POUND_in_unaryOperator7172); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:646:4: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_unaryOperator7183); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:647:4: SUB
                    {
                    match(input,SUB,FOLLOW_SUB_in_unaryOperator7196); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.NEG; 
                    }

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:648:4: NOT
                    {
                    match(input,NOT,FOLLOW_NOT_in_unaryOperator7209); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.NOT; 
                    }

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:649:4: SIZEOF
                    {
                    match(input,SIZEOF,FOLLOW_SIZEOF_in_unaryOperator7222); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 6 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:650:4: TYPEOF
                    {
                    match(input,TYPEOF,FOLLOW_TYPEOF_in_unaryOperator7235); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 7 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:651:4: REVERSE
                    {
                    match(input,REVERSE,FOLLOW_REVERSE_in_unaryOperator7248); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 8 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:652:4: PLUSPLUS
                    {
                    match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_unaryOperator7261); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 9 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:653:4: SUBSUB
                    {
                    match(input,SUBSUB,FOLLOW_SUBSUB_in_unaryOperator7274); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
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
        return optag;
    }
    // $ANTLR end unaryOperator


    // $ANTLR start assignmentOperator
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:655:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );
    public final int assignmentOperator() throws RecognitionException {
        int optag = 0;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:656:4: ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ )
            int alt99=5;
            switch ( input.LA(1) ) {
            case PLUSEQ:
                {
                alt99=1;
                }
                break;
            case SUBEQ:
                {
                alt99=2;
                }
                break;
            case STAREQ:
                {
                alt99=3;
                }
                break;
            case SLASHEQ:
                {
                alt99=4;
                }
                break;
            case PERCENTEQ:
                {
                alt99=5;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return optag;}
                NoViableAltException nvae =
                    new NoViableAltException("655:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );", 99, 0, input);

                throw nvae;
            }

            switch (alt99) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:656:4: PLUSEQ
                    {
                    match(input,PLUSEQ,FOLLOW_PLUSEQ_in_assignmentOperator7295); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.PLUS_ASG; 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:657:4: SUBEQ
                    {
                    match(input,SUBEQ,FOLLOW_SUBEQ_in_assignmentOperator7308); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.MINUS_ASG; 
                    }

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:658:4: STAREQ
                    {
                    match(input,STAREQ,FOLLOW_STAREQ_in_assignmentOperator7321); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.MUL_ASG; 
                    }

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:659:4: SLASHEQ
                    {
                    match(input,SLASHEQ,FOLLOW_SLASHEQ_in_assignmentOperator7334); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.DIV_ASG; 
                    }

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:660:4: PERCENTEQ
                    {
                    match(input,PERCENTEQ,FOLLOW_PERCENTEQ_in_assignmentOperator7347); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.MOD_ASG; 
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
        return optag;
    }
    // $ANTLR end assignmentOperator


    // $ANTLR start typeReference
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:662:1: typeReference returns [JFXType type] : ( COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? ;
    public final JFXType typeReference() throws RecognitionException {
        JFXType type = null;

        Token STAR160=null;
        int ccf = 0;

        int ccn = 0;

        int ccs = 0;

        ListBuffer<JCTree> formalParameters158 = null;

        name_return name159 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:663:4: ( ( COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:663:4: ( COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:663:4: ( COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            int alt102=2;
            int LA102_0 = input.LA(1);

            if ( (LA102_0==COLON) ) {
                alt102=1;
            }
            switch (alt102) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:663:6: COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    {
                    match(input,COLON,FOLLOW_COLON_in_typeReference7371); if (failed) return type;
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:663:13: ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    int alt101=3;
                    switch ( input.LA(1) ) {
                    case OPERATION:
                    case FUNCTION:
                    case LPAREN:
                        {
                        alt101=1;
                        }
                        break;
                    case QUOTED_IDENTIFIER:
                    case IDENTIFIER:
                        {
                        alt101=2;
                        }
                        break;
                    case STAR:
                        {
                        alt101=3;
                        }
                        break;
                    default:
                        if (backtracking>0) {failed=true; return type;}
                        NoViableAltException nvae =
                            new NoViableAltException("663:13: ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )", 101, 0, input);

                        throw nvae;
                    }

                    switch (alt101) {
                        case 1 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:664:22: ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint )
                            {
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:664:22: ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint )
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:664:55: ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint
                            {
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:664:55: ( FUNCTION | OPERATION )?
                            int alt100=2;
                            int LA100_0 = input.LA(1);

                            if ( ((LA100_0>=OPERATION && LA100_0<=FUNCTION)) ) {
                                alt100=1;
                            }
                            switch (alt100) {
                                case 1 :
                                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
                                    {
                                    if ( (input.LA(1)>=OPERATION && input.LA(1)<=FUNCTION) ) {
                                        input.consume();
                                        errorRecovery=false;failed=false;
                                    }
                                    else {
                                        if (backtracking>0) {failed=true; return type;}
                                        MismatchedSetException mse =
                                            new MismatchedSetException(null,input);
                                        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_typeReference7411);    throw mse;
                                    }


                                    }
                                    break;

                            }

                            pushFollow(FOLLOW_formalParameters_in_typeReference7420);
                            formalParameters158=formalParameters();
                            _fsp--;
                            if (failed) return type;
                            pushFollow(FOLLOW_typeReference_in_typeReference7422);
                            typeReference();
                            _fsp--;
                            if (failed) return type;
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference7426);
                            ccf=cardinalityConstraint();
                            _fsp--;
                            if (failed) return type;

                            }

                            if ( backtracking==0 ) {
                               type = F.TypeFunctional(formalParameters158.toList(), 
                                                                                                                     type, ccf); 
                            }

                            }
                            break;
                        case 2 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:667:22: name ccn= cardinalityConstraint
                            {
                            pushFollow(FOLLOW_name_in_typeReference7481);
                            name159=name();
                            _fsp--;
                            if (failed) return type;
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference7485);
                            ccn=cardinalityConstraint();
                            _fsp--;
                            if (failed) return type;
                            if ( backtracking==0 ) {
                               type = F.TypeClass(name159.value, ccn); 
                            }

                            }
                            break;
                        case 3 :
                            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:668:22: STAR ccs= cardinalityConstraint
                            {
                            STAR160=(Token)input.LT(1);
                            match(input,STAR,FOLLOW_STAR_in_typeReference7511); if (failed) return type;
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference7515);
                            ccs=cardinalityConstraint();
                            _fsp--;
                            if (failed) return type;
                            if ( backtracking==0 ) {
                               type = F.at(pos(STAR160)).TypeAny(ccs); 
                            }

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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:670:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );
    public final int cardinalityConstraint() throws RecognitionException {
        int ary = 0;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:671:5: ( LBRACKET RBRACKET | QUES | PLUS | STAR | )
            int alt103=5;
            switch ( input.LA(1) ) {
            case LBRACKET:
                {
                int LA103_1 = input.LA(2);

                if ( (synpred193()) ) {
                    alt103=1;
                }
                else if ( (true) ) {
                    alt103=5;
                }
                else {
                    if (backtracking>0) {failed=true; return ary;}
                    NoViableAltException nvae =
                        new NoViableAltException("670:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 103, 1, input);

                    throw nvae;
                }
                }
                break;
            case QUES:
                {
                int LA103_2 = input.LA(2);

                if ( (synpred194()) ) {
                    alt103=2;
                }
                else if ( (true) ) {
                    alt103=5;
                }
                else {
                    if (backtracking>0) {failed=true; return ary;}
                    NoViableAltException nvae =
                        new NoViableAltException("670:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 103, 2, input);

                    throw nvae;
                }
                }
                break;
            case PLUS:
                {
                int LA103_3 = input.LA(2);

                if ( (synpred195()) ) {
                    alt103=3;
                }
                else if ( (true) ) {
                    alt103=5;
                }
                else {
                    if (backtracking>0) {failed=true; return ary;}
                    NoViableAltException nvae =
                        new NoViableAltException("670:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 103, 3, input);

                    throw nvae;
                }
                }
                break;
            case STAR:
                {
                int LA103_4 = input.LA(2);

                if ( (synpred196()) ) {
                    alt103=4;
                }
                else if ( (true) ) {
                    alt103=5;
                }
                else {
                    if (backtracking>0) {failed=true; return ary;}
                    NoViableAltException nvae =
                        new NoViableAltException("670:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 103, 4, input);

                    throw nvae;
                }
                }
                break;
            case EOF:
            case IF:
            case INVERSE:
            case ORDER:
            case INDEX:
            case RPAREN:
            case SEMI:
            case COMMA:
            case EQ:
            case LBRACE:
                {
                alt103=5;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ary;}
                NoViableAltException nvae =
                    new NoViableAltException("670:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 103, 0, input);

                throw nvae;
            }

            switch (alt103) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:671:5: LBRACKET RBRACKET
                    {
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_cardinalityConstraint7546); if (failed) return ary;
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_cardinalityConstraint7550); if (failed) return ary;
                    if ( backtracking==0 ) {
                       ary = JFXType.CARDINALITY_ANY; 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:672:5: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_cardinalityConstraint7562); if (failed) return ary;
                    if ( backtracking==0 ) {
                       ary = JFXType.CARDINALITY_OPTIONAL; 
                    }

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:673:5: PLUS
                    {
                    match(input,PLUS,FOLLOW_PLUS_in_cardinalityConstraint7589); if (failed) return ary;
                    if ( backtracking==0 ) {
                       ary = JFXType.CARDINALITY_ANY; 
                    }

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:674:5: STAR
                    {
                    match(input,STAR,FOLLOW_STAR_in_cardinalityConstraint7616); if (failed) return ary;
                    if ( backtracking==0 ) {
                       ary = JFXType.CARDINALITY_ANY; 
                    }

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:675:29: 
                    {
                    if ( backtracking==0 ) {
                       ary = JFXType.CARDINALITY_OPTIONAL; 
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
        return ary;
    }
    // $ANTLR end cardinalityConstraint


    // $ANTLR start literal
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:677:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );
    public final JCExpression literal() throws RecognitionException {
        JCExpression expr = null;

        Token t=null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:678:4: (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL )
            int alt104=6;
            switch ( input.LA(1) ) {
            case STRING_LITERAL:
                {
                alt104=1;
                }
                break;
            case INTEGER_LITERAL:
                {
                alt104=2;
                }
                break;
            case FLOATING_POINT_LITERAL:
                {
                alt104=3;
                }
                break;
            case TRUE:
                {
                alt104=4;
                }
                break;
            case FALSE:
                {
                alt104=5;
                }
                break;
            case NULL:
                {
                alt104=6;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return expr;}
                NoViableAltException nvae =
                    new NoViableAltException("677:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );", 104, 0, input);

                throw nvae;
            }

            switch (alt104) {
                case 1 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:678:4: t= STRING_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_literal7685); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.CLASS, t.getText()); 
                    }

                    }
                    break;
                case 2 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:679:4: t= INTEGER_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,INTEGER_LITERAL,FOLLOW_INTEGER_LITERAL_in_literal7695); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.INT, Convert.string2int(t.getText(), 10)); 
                    }

                    }
                    break;
                case 3 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:680:4: t= FLOATING_POINT_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,FLOATING_POINT_LITERAL,FOLLOW_FLOATING_POINT_LITERAL_in_literal7705); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.DOUBLE, Double.valueOf(t.getText())); 
                    }

                    }
                    break;
                case 4 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:681:4: t= TRUE
                    {
                    t=(Token)input.LT(1);
                    match(input,TRUE,FOLLOW_TRUE_in_literal7715); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 1); 
                    }

                    }
                    break;
                case 5 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:682:4: t= FALSE
                    {
                    t=(Token)input.LT(1);
                    match(input,FALSE,FOLLOW_FALSE_in_literal7729); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 0); 
                    }

                    }
                    break;
                case 6 :
                    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:683:4: t= NULL
                    {
                    t=(Token)input.LT(1);
                    match(input,NULL,FOLLOW_NULL_in_literal7743); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.BOT, null); 
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
    // $ANTLR end literal


    // $ANTLR start typeName
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:685:1: typeName returns [JCExpression expr] : qualident ;
    public final JCExpression typeName() throws RecognitionException {
        JCExpression expr = null;

        JCExpression qualident161 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:686:10: ( qualident )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:686:10: qualident
            {
            pushFollow(FOLLOW_qualident_in_typeName7770);
            qualident161=qualident();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = qualident161; 
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
    // $ANTLR end typeName


    // $ANTLR start qualident
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:688:1: qualident returns [JCExpression expr] : identifier ( DOT name )* ;
    public final JCExpression qualident() throws RecognitionException {
        JCExpression expr = null;

        JCIdent identifier162 = null;

        name_return name163 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:689:10: ( identifier ( DOT name )* )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:689:10: identifier ( DOT name )*
            {
            pushFollow(FOLLOW_identifier_in_qualident7812);
            identifier162=identifier();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = identifier162; 
            }
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:690:10: ( DOT name )*
            loop105:
            do {
                int alt105=2;
                int LA105_0 = input.LA(1);

                if ( (LA105_0==DOT) ) {
                    alt105=1;
                }


                switch (alt105) {
            	case 1 :
            	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:690:12: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_qualident7840); if (failed) return expr;
            	    pushFollow(FOLLOW_name_in_qualident7842);
            	    name163=name();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(name163.pos).Select(expr, name163.value); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop105;
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
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:692:1: identifier returns [JCIdent expr] : name ;
    public final JCIdent identifier() throws RecognitionException {
        JCIdent expr = null;

        name_return name164 = null;


        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:693:4: ( name )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:693:4: name
            {
            pushFollow(FOLLOW_name_in_identifier7879);
            name164=name();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = F.at(name164.pos).Ident(name164.value); 
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
    // $ANTLR end identifier

    public static class name_return extends ParserRuleReturnScope {
        public Name value;
        public int pos;
    };

    // $ANTLR start name
    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:695:1: name returns [Name value, int pos] : tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) ;
    public final name_return name() throws RecognitionException {
        name_return retval = new name_return();
        retval.start = input.LT(1);

        Token tokid=null;

        try {
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:696:4: (tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) )
            // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:696:4: tokid= ( QUOTED_IDENTIFIER | IDENTIFIER )
            {
            tokid=(Token)input.LT(1);
            if ( input.LA(1)==QUOTED_IDENTIFIER||input.LA(1)==IDENTIFIER ) {
                input.consume();
                errorRecovery=false;failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return retval;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_name7913);    throw mse;
            }

            if ( backtracking==0 ) {
               retval.value = Name.fromString(names, tokid.getText()); retval.pos = pos(tokid); 
            }

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

    // $ANTLR start synpred45
    public final void synpred45_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:438:4: ( localFunctionDefinition )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:438:4: localFunctionDefinition
        {
        pushFollow(FOLLOW_localFunctionDefinition_in_synpred453636);
        localFunctionDefinition();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred45

    // $ANTLR start synpred46
    public final void synpred46_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:439:4: ( localOperationDefinition )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:439:4: localOperationDefinition
        {
        pushFollow(FOLLOW_localOperationDefinition_in_synpred463644);
        localOperationDefinition();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred46

    // $ANTLR start synpred47
    public final void synpred47_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:440:10: ( backgroundStatement )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:440:10: backgroundStatement
        {
        pushFollow(FOLLOW_backgroundStatement_in_synpred473658);
        backgroundStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred47

    // $ANTLR start synpred48
    public final void synpred48_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:441:10: ( laterStatement )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:441:10: laterStatement
        {
        pushFollow(FOLLOW_laterStatement_in_synpred483673);
        laterStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred48

    // $ANTLR start synpred50
    public final void synpred50_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:443:10: ( ifStatement )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:443:10: ifStatement
        {
        pushFollow(FOLLOW_ifStatement_in_synpred503709);
        ifStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred50

    // $ANTLR start synpred53
    public final void synpred53_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:446:4: ( expression SEMI )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:446:4: expression SEMI
        {
        pushFollow(FOLLOW_expression_in_synpred533752);
        expression();
        _fsp--;
        if (failed) return ;
        match(input,SEMI,FOLLOW_SEMI_in_synpred533756); if (failed) return ;

        }
    }
    // $ANTLR end synpred53

    // $ANTLR start synpred58
    public final void synpred58_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:451:10: ( forAlphaStatement )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:451:10: forAlphaStatement
        {
        pushFollow(FOLLOW_forAlphaStatement_in_synpred583833);
        forAlphaStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred58

    // $ANTLR start synpred59
    public final void synpred59_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:452:10: ( forJoinStatement )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:452:10: forJoinStatement
        {
        pushFollow(FOLLOW_forJoinStatement_in_synpred593849);
        forJoinStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred59

    // $ANTLR start synpred80
    public final void synpred80_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:62: ( FPS expression )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:62: FPS expression
        {
        match(input,FPS,FOLLOW_FPS_in_synpred804705); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred804709);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred80

    // $ANTLR start synpred81
    public final void synpred81_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:91: ( WHILE expression )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:91: WHILE expression
        {
        match(input,WHILE,FOLLOW_WHILE_in_synpred814723); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred814727);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred81

    // $ANTLR start synpred82
    public final void synpred82_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:122: ( CONTINUE IF expression )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:122: CONTINUE IF expression
        {
        match(input,CONTINUE,FOLLOW_CONTINUE_in_synpred824741); if (failed) return ;
        match(input,IF,FOLLOW_IF_in_synpred824745); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred824749);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred82

    // $ANTLR start synpred86
    public final void synpred86_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:36: ( LINEAR )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:36: LINEAR
        {
        match(input,LINEAR,FOLLOW_LINEAR_in_synpred864888); if (failed) return ;

        }
    }
    // $ANTLR end synpred86

    // $ANTLR start synpred87
    public final void synpred87_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:49: ( EASEIN )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:49: EASEIN
        {
        match(input,EASEIN,FOLLOW_EASEIN_in_synpred874896); if (failed) return ;

        }
    }
    // $ANTLR end synpred87

    // $ANTLR start synpred88
    public final void synpred88_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:62: ( EASEOUT )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:62: EASEOUT
        {
        match(input,EASEOUT,FOLLOW_EASEOUT_in_synpred884904); if (failed) return ;

        }
    }
    // $ANTLR end synpred88

    // $ANTLR start synpred89
    public final void synpred89_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:76: ( EASEBOTH )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:76: EASEBOTH
        {
        match(input,EASEBOTH,FOLLOW_EASEBOTH_in_synpred894912); if (failed) return ;

        }
    }
    // $ANTLR end synpred89

    // $ANTLR start synpred90
    public final void synpred90_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:91: ( MOTION expression )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:91: MOTION expression
        {
        match(input,MOTION,FOLLOW_MOTION_in_synpred904920); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred904924);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred90

    // $ANTLR start synpred91
    public final void synpred91_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:123: ( FPS expression )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:123: FPS expression
        {
        match(input,FPS,FOLLOW_FPS_in_synpred914938); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred914942);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred91

    // $ANTLR start synpred92
    public final void synpred92_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:150: ( WHILE expression )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:150: WHILE expression
        {
        match(input,WHILE,FOLLOW_WHILE_in_synpred924954); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred924958);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred92

    // $ANTLR start synpred93
    public final void synpred93_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:181: ( CONTINUE IF expression )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:181: CONTINUE IF expression
        {
        match(input,CONTINUE,FOLLOW_CONTINUE_in_synpred934972); if (failed) return ;
        match(input,IF,FOLLOW_IF_in_synpred934976); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred934980);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred93

    // $ANTLR start synpred97
    public final void synpred97_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:513:39: ( typeReference )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:513:39: typeReference
        {
        pushFollow(FOLLOW_typeReference_in_synpred975071);
        typeReference();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred97

    // $ANTLR start synpred105
    public final void synpred105_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:11: ( LPAREN typeName RPAREN suffixedExpression )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:11: LPAREN typeName RPAREN suffixedExpression
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred1055194); if (failed) return ;
        pushFollow(FOLLOW_typeName_in_synpred1055200);
        typeName();
        _fsp--;
        if (failed) return ;
        match(input,RPAREN,FOLLOW_RPAREN_in_synpred1055203); if (failed) return ;
        pushFollow(FOLLOW_suffixedExpression_in_synpred1055207);
        suffixedExpression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred105

    // $ANTLR start synpred108
    public final void synpred108_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:524:52: ( typeReference )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:524:52: typeReference
        {
        pushFollow(FOLLOW_typeReference_in_synpred1085318);
        typeReference();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred108

    // $ANTLR start synpred109
    public final void synpred109_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:54: ( typeReference )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:54: typeReference
        {
        pushFollow(FOLLOW_typeReference_in_synpred1095340);
        typeReference();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred109

    // $ANTLR start synpred111
    public final void synpred111_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:527:83: ( COMMA selectionVar )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:527:83: COMMA selectionVar
        {
        match(input,COMMA,FOLLOW_COMMA_in_synpred1115410); if (failed) return ;
        pushFollow(FOLLOW_selectionVar_in_synpred1115414);
        selectionVar();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred111

    // $ANTLR start synpred112
    public final void synpred112_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:527:116: ( WHERE expression )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:527:116: WHERE expression
        {
        match(input,WHERE,FOLLOW_WHERE_in_synpred1125428); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred1125432);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred112

    // $ANTLR start synpred143
    public final void synpred143_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:584:4: ( identifier LBRACE objectLiteral RBRACE )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:584:4: identifier LBRACE objectLiteral RBRACE
        {
        pushFollow(FOLLOW_identifier_in_synpred1436296);
        identifier();
        _fsp--;
        if (failed) return ;
        match(input,LBRACE,FOLLOW_LBRACE_in_synpred1436298); if (failed) return ;
        pushFollow(FOLLOW_objectLiteral_in_synpred1436301);
        objectLiteral();
        _fsp--;
        if (failed) return ;
        match(input,RBRACE,FOLLOW_RBRACE_in_synpred1436303); if (failed) return ;

        }
    }
    // $ANTLR end synpred143

    // $ANTLR start synpred150
    public final void synpred150_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:591:10: ( identifier ( LPAREN expressionListOpt RPAREN )* )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:591:10: identifier ( LPAREN expressionListOpt RPAREN )*
        {
        pushFollow(FOLLOW_identifier_in_synpred1506390);
        identifier();
        _fsp--;
        if (failed) return ;
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:592:10: ( LPAREN expressionListOpt RPAREN )*
        loop119:
        do {
            int alt119=2;
            int LA119_0 = input.LA(1);

            if ( (LA119_0==LPAREN) ) {
                alt119=1;
            }


            switch (alt119) {
        	case 1 :
        	    // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:592:12: LPAREN expressionListOpt RPAREN
        	    {
        	    match(input,LPAREN,FOLLOW_LPAREN_in_synpred1506411); if (failed) return ;
        	    pushFollow(FOLLOW_expressionListOpt_in_synpred1506415);
        	    expressionListOpt();
        	    _fsp--;
        	    if (failed) return ;
        	    match(input,RPAREN,FOLLOW_RPAREN_in_synpred1506419); if (failed) return ;

        	    }
        	    break;

        	default :
        	    break loop119;
            }
        } while (true);


        }
    }
    // $ANTLR end synpred150

    // $ANTLR start synpred162
    public final void synpred162_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:614:36: ( generatorClause )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:614:36: generatorClause
        {
        pushFollow(FOLLOW_generatorClause_in_synpred1626710);
        generatorClause();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred162

    // $ANTLR start synpred163
    public final void synpred163_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:614:58: ( dotDotClause )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:614:58: dotDotClause
        {
        pushFollow(FOLLOW_dotDotClause_in_synpred1636718);
        dotDotClause();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred163

    // $ANTLR start synpred173
    public final void synpred173_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:642:28: ( COMMA name )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:642:28: COMMA name
        {
        match(input,COMMA,FOLLOW_COMMA_in_synpred1737124); if (failed) return ;
        pushFollow(FOLLOW_name_in_synpred1737128);
        name();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred173

    // $ANTLR start synpred193
    public final void synpred193_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:671:5: ( LBRACKET RBRACKET )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:671:5: LBRACKET RBRACKET
        {
        match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred1937546); if (failed) return ;
        match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred1937550); if (failed) return ;

        }
    }
    // $ANTLR end synpred193

    // $ANTLR start synpred194
    public final void synpred194_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:672:5: ( QUES )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:672:5: QUES
        {
        match(input,QUES,FOLLOW_QUES_in_synpred1947562); if (failed) return ;

        }
    }
    // $ANTLR end synpred194

    // $ANTLR start synpred195
    public final void synpred195_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:673:5: ( PLUS )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:673:5: PLUS
        {
        match(input,PLUS,FOLLOW_PLUS_in_synpred1957589); if (failed) return ;

        }
    }
    // $ANTLR end synpred195

    // $ANTLR start synpred196
    public final void synpred196_fragment() throws RecognitionException {   
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:674:5: ( STAR )
        // \\JavaFX\\openjfx\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:674:5: STAR
        {
        match(input,STAR,FOLLOW_STAR_in_synpred1967616); if (failed) return ;

        }
    }
    // $ANTLR end synpred196

    public final boolean synpred80() {
        backtracking++;
        int start = input.mark();
        try {
            synpred80_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred81() {
        backtracking++;
        int start = input.mark();
        try {
            synpred81_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred46() {
        backtracking++;
        int start = input.mark();
        try {
            synpred46_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred82() {
        backtracking++;
        int start = input.mark();
        try {
            synpred82_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred45() {
        backtracking++;
        int start = input.mark();
        try {
            synpred45_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred48() {
        backtracking++;
        int start = input.mark();
        try {
            synpred48_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred47() {
        backtracking++;
        int start = input.mark();
        try {
            synpred47_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred86() {
        backtracking++;
        int start = input.mark();
        try {
            synpred86_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred87() {
        backtracking++;
        int start = input.mark();
        try {
            synpred87_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred88() {
        backtracking++;
        int start = input.mark();
        try {
            synpred88_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred89() {
        backtracking++;
        int start = input.mark();
        try {
            synpred89_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred105() {
        backtracking++;
        int start = input.mark();
        try {
            synpred105_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred143() {
        backtracking++;
        int start = input.mark();
        try {
            synpred143_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred108() {
        backtracking++;
        int start = input.mark();
        try {
            synpred108_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred109() {
        backtracking++;
        int start = input.mark();
        try {
            synpred109_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred163() {
        backtracking++;
        int start = input.mark();
        try {
            synpred163_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred162() {
        backtracking++;
        int start = input.mark();
        try {
            synpred162_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred97() {
        backtracking++;
        int start = input.mark();
        try {
            synpred97_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred59() {
        backtracking++;
        int start = input.mark();
        try {
            synpred59_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred58() {
        backtracking++;
        int start = input.mark();
        try {
            synpred58_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred92() {
        backtracking++;
        int start = input.mark();
        try {
            synpred92_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred93() {
        backtracking++;
        int start = input.mark();
        try {
            synpred93_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred90() {
        backtracking++;
        int start = input.mark();
        try {
            synpred90_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred111() {
        backtracking++;
        int start = input.mark();
        try {
            synpred111_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred91() {
        backtracking++;
        int start = input.mark();
        try {
            synpred91_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred112() {
        backtracking++;
        int start = input.mark();
        try {
            synpred112_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred53() {
        backtracking++;
        int start = input.mark();
        try {
            synpred53_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred50() {
        backtracking++;
        int start = input.mark();
        try {
            synpred50_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred173() {
        backtracking++;
        int start = input.mark();
        try {
            synpred173_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred150() {
        backtracking++;
        int start = input.mark();
        try {
            synpred150_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred195() {
        backtracking++;
        int start = input.mark();
        try {
            synpred195_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred196() {
        backtracking++;
        int start = input.mark();
        try {
            synpred196_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred193() {
        backtracking++;
        int start = input.mark();
        try {
            synpred193_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred194() {
        backtracking++;
        int start = input.mark();
        try {
            synpred194_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_packageDecl_in_module2072 = new BitSet(new long[]{0x499F914B81644260L,0x582C007008ADC0FFL,0x0000000000000002L});
    public static final BitSet FOLLOW_moduleItems_in_module2075 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_module2077 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PACKAGE_in_packageDecl2110 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_qualident_in_packageDecl2112 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_packageDecl2114 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_moduleItem_in_moduleItems2143 = new BitSet(new long[]{0x499F914B81644262L,0x582C007008ADC0FFL,0x0000000000000002L});
    public static final BitSet FOLLOW_importDecl_in_moduleItem2185 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDefinition_in_moduleItem2200 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeDefinition_in_moduleItem2215 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberOperationDefinition_in_moduleItem2230 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberFunctionDefinition_in_moduleItem2244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRIGGER_in_moduleItem2258 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ON_in_moduleItem2260 = new BitSet(new long[]{0x0000000000000000L,0x0800000000200000L,0x0000000000000002L});
    public static final BitSet FOLLOW_changeRule_in_moduleItem2262 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statementExcept_in_moduleItem2276 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importDecl2305 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_importDecl2308 = new BitSet(new long[]{0x0000000000000000L,0x000000000A000000L});
    public static final BitSet FOLLOW_DOT_in_importDecl2332 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_importDecl2334 = new BitSet(new long[]{0x0000000000000000L,0x000000000A000000L});
    public static final BitSet FOLLOW_DOT_in_importDecl2362 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_STAR_in_importDecl2364 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_importDecl2372 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_classDefinition2398 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_CLASS_in_classDefinition2401 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_classDefinition2403 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000400L});
    public static final BitSet FOLLOW_supers_in_classDefinition2405 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_classDefinition2407 = new BitSet(new long[]{0x0000000000004200L,0x020000000000009CL});
    public static final BitSet FOLLOW_classMembers_in_classDefinition2409 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_classDefinition2411 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXTENDS_in_supers2431 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_supers2435 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_supers2458 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_supers2462 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_attributeDecl_in_classMembers2491 = new BitSet(new long[]{0x0000000000004202L,0x000000000000009CL});
    public static final BitSet FOLLOW_functionDecl_in_classMembers2514 = new BitSet(new long[]{0x0000000000004202L,0x000000000000009CL});
    public static final BitSet FOLLOW_operationDecl_in_classMembers2538 = new BitSet(new long[]{0x0000000000004202L,0x000000000000009CL});
    public static final BitSet FOLLOW_modifierFlags_in_attributeDecl2575 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeDecl2577 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_attributeDecl2579 = new BitSet(new long[]{0x0000000000000000L,0x0002000002001900L});
    public static final BitSet FOLLOW_typeReference_in_attributeDecl2581 = new BitSet(new long[]{0x0000000000000000L,0x0000000002001900L});
    public static final BitSet FOLLOW_inverseClause_in_attributeDecl2583 = new BitSet(new long[]{0x0000000000000000L,0x0000000002001800L});
    public static final BitSet FOLLOW_orderBy_in_attributeDecl2587 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_indexOn_in_attributeDecl2591 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_attributeDecl2595 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INVERSE_in_inverseClause2614 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_inverseClause2616 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_functionDecl2634 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_FUNCTION_in_functionDecl2636 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_functionDecl2638 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_functionDecl2640 = new BitSet(new long[]{0x0000000000000000L,0x0002000002000000L});
    public static final BitSet FOLLOW_typeReference_in_functionDecl2642 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_functionDecl2644 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_operationDecl2663 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_OPERATION_in_operationDecl2667 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_operationDecl2671 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_operationDecl2675 = new BitSet(new long[]{0x0000000000000000L,0x0002000002000000L});
    public static final BitSet FOLLOW_typeReference_in_operationDecl2679 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_operationDecl2684 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeDefinition2703 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_attributeDefinition2707 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_attributeDefinition2711 = new BitSet(new long[]{0x4017900060010060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_bindOpt_in_attributeDefinition2713 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_attributeDefinition2716 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_attributeDefinition2720 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATION_in_memberOperationDefinition2739 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_memberOperationDefinition2743 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_memberOperationDefinition2747 = new BitSet(new long[]{0x0000000000000000L,0x0042000000000000L});
    public static final BitSet FOLLOW_typeReference_in_memberOperationDefinition2751 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_memberOperationDefinition2754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_memberFunctionDefinition2773 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_memberFunctionDefinition2777 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_memberFunctionDefinition2781 = new BitSet(new long[]{0x0000000000000000L,0x0042000000000000L});
    public static final BitSet FOLLOW_typeReference_in_memberFunctionDefinition2785 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_memberFunctionDefinition2788 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQ_in_functionBody2804 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_functionBody2808 = new BitSet(new long[]{0x8000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_whereVarDecls_in_functionBody2812 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_functionBody2818 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_functionBody2834 = new BitSet(new long[]{0x0000000280000000L,0x0800000000000060L,0x0000000000000002L});
    public static final BitSet FOLLOW_variableDefinition_in_functionBody2842 = new BitSet(new long[]{0x0000000280000000L,0x0800000000000060L,0x0000000000000002L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_functionBody2850 = new BitSet(new long[]{0x0000000280000000L,0x0800000000000060L,0x0000000000000002L});
    public static final BitSet FOLLOW_localOperationDefinition_in_functionBody2858 = new BitSet(new long[]{0x0000000280000000L,0x0800000000000060L,0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_functionBody2868 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_functionBody2872 = new BitSet(new long[]{0x0000000000000000L,0x0200000002000000L});
    public static final BitSet FOLLOW_SEMI_in_functionBody2876 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_functionBody2882 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_whereVarDecls2890 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000040L,0x0000000000000002L});
    public static final BitSet FOLLOW_whereVarDecl_in_whereVarDecls2894 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_whereVarDecls2902 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000040L,0x0000000000000002L});
    public static final BitSet FOLLOW_whereVarDecl_in_whereVarDecls2906 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_whereVarDecl2920 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_whereVarDecl2932 = new BitSet(new long[]{0x0000000000000000L,0x0002000020000000L});
    public static final BitSet FOLLOW_typeReference_in_whereVarDecl2936 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_whereVarDecl2940 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_whereVarDecl2944 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variableDefinition2952 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_variableDefinition2956 = new BitSet(new long[]{0x0000000000000000L,0x0002000020000000L});
    public static final BitSet FOLLOW_typeReference_in_variableDefinition2960 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_variableDefinition2963 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_variableDefinition2967 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_variableDefinition2971 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule2985 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_changeRule2989 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_changeRule2993 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule2996 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule2999 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule3015 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3019 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_changeRule3022 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_changeRule3024 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule3028 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3031 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3045 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_changeRule3048 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_changeRule3050 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3052 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule3066 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3070 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_LBRACKET_in_changeRule3074 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_changeRule3080 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_changeRule3084 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_changeRule3088 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_changeRule3092 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule3096 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3099 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule3113 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_INSERT_in_changeRule3117 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_changeRule3121 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_INTO_in_changeRule3125 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3129 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule3133 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule3142 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_DELETE_in_changeRule3146 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_changeRule3150 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_FROM_in_changeRule3154 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3158 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule3162 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3164 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule3171 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_DELETE_in_changeRule3175 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3178 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_LBRACKET_in_changeRule3182 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_changeRule3186 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_changeRule3190 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule3194 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3196 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags3219 = new BitSet(new long[]{0x0000000000000002L,0x000000000000001CL});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags3232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags3254 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000080L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags3267 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_accessModifier3315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PRIVATE_in_accessModifier3332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROTECTED_in_accessModifier3348 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ABSTRACT_in_otherModifier3372 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_READONLY_in_otherModifier3387 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_memberSelector3413 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_DOT_in_memberSelector3417 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_memberSelector3423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_formalParameters3439 = new BitSet(new long[]{0x0000000000000000L,0x0800000000400000L,0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters3447 = new BitSet(new long[]{0x0000000000000000L,0x0000000004400000L});
    public static final BitSet FOLLOW_COMMA_in_formalParameters3466 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters3472 = new BitSet(new long[]{0x0000000000000000L,0x0000000004400000L});
    public static final BitSet FOLLOW_RPAREN_in_formalParameters3483 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_formalParameter3496 = new BitSet(new long[]{0x0000000000000002L,0x0002000000000000L});
    public static final BitSet FOLLOW_typeReference_in_formalParameter3498 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_block3515 = new BitSet(new long[]{0x499F914381440060L,0x5A2C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_statements_in_block3519 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_block3523 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_statements3541 = new BitSet(new long[]{0x499F914381440062L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_statementExcept_in_statement3592 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localTriggerStatement_in_statement3608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclaration_in_statementExcept3626 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_statementExcept3636 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localOperationDefinition_in_statementExcept3644 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_backgroundStatement_in_statementExcept3658 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_laterStatement_in_statementExcept3673 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_statementExcept3688 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_statementExcept3690 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_statementExcept3692 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_statementExcept3694 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_statementExcept3696 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifStatement_in_statementExcept3709 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_insertStatement_in_statementExcept3726 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_deleteStatement_in_statementExcept3742 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_statementExcept3752 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_statementExcept3756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BREAK_in_statementExcept3766 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_statementExcept3770 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_statementExcept3781 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_statementExcept3785 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_throwStatement_in_statementExcept3801 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_returnStatement_in_statementExcept3817 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forAlphaStatement_in_statementExcept3833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forJoinStatement_in_statementExcept3849 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tryStatement_in_statementExcept3865 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_assertStatement3884 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_assertStatement3888 = new BitSet(new long[]{0x0000000000000000L,0x0002000002000000L});
    public static final BitSet FOLLOW_COLON_in_assertStatement3896 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_assertStatement3900 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_assertStatement3910 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATION_in_localOperationDefinition3925 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localOperationDefinition3929 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_localOperationDefinition3933 = new BitSet(new long[]{0x0000000000000000L,0x0042000000000000L});
    public static final BitSet FOLLOW_typeReference_in_localOperationDefinition3937 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_localOperationDefinition3940 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_localFunctionDefinition3960 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localFunctionDefinition3966 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_localFunctionDefinition3970 = new BitSet(new long[]{0x0000000000000000L,0x0042000000000000L});
    public static final BitSet FOLLOW_typeReference_in_localFunctionDefinition3974 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_localFunctionDefinition3977 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variableDeclaration3997 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_variableDeclaration4000 = new BitSet(new long[]{0x0000000000000000L,0x0002000022000000L});
    public static final BitSet FOLLOW_typeReference_in_variableDeclaration4003 = new BitSet(new long[]{0x0000000000000000L,0x0000000022000000L});
    public static final BitSet FOLLOW_EQ_in_variableDeclaration4014 = new BitSet(new long[]{0x4017900060010060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_bindOpt_in_variableDeclaration4016 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_variableDeclaration4019 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_variableDeclaration4021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_variableDeclaration4032 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BIND_in_bindOpt4069 = new BitSet(new long[]{0x2000000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt4085 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAYS_in_bindOpt4100 = new BitSet(new long[]{0x2000000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt4116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TIE_in_bindOpt4131 = new BitSet(new long[]{0x2000000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt4147 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DO_in_backgroundStatement4173 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_backgroundStatement4177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DO_in_laterStatement4193 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LATER_in_laterStatement4197 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_laterStatement4201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifStatement4221 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_ifStatement4225 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_ifStatement4229 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_ifStatement4233 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_ifStatement4239 = new BitSet(new long[]{0x0000400000000002L});
    public static final BitSet FOLLOW_ELSE_in_ifStatement4242 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_ifStatement4247 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INSERT_in_insertStatement4276 = new BitSet(new long[]{0x4017900000800060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_DISTINCT_in_insertStatement4284 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_insertStatement4288 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_INTO_in_insertStatement4292 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_insertStatement4296 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_expression_in_insertStatement4304 = new BitSet(new long[]{0x0000020000009400L});
    public static final BitSet FOLLOW_AS_in_insertStatement4320 = new BitSet(new long[]{0x00000C0000000000L});
    public static final BitSet FOLLOW_set_in_insertStatement4324 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_INTO_in_insertStatement4350 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_insertStatement4354 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_AFTER_in_insertStatement4366 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_insertStatement4370 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_BEFORE_in_insertStatement4378 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_insertStatement4382 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_insertStatement4396 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DELETE_in_deleteStatement4411 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_deleteStatement4415 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_deleteStatement4419 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THROW_in_throwStatement4434 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_throwStatement4438 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_throwStatement4442 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnStatement4462 = new BitSet(new long[]{0x4017900000000060L,0x582C00700AADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_returnStatement4465 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_returnStatement4472 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRIGGER_in_localTriggerStatement4498 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ON_in_localTriggerStatement4502 = new BitSet(new long[]{0x0000010000400000L,0x0800000000200000L,0x0000000000000002L});
    public static final BitSet FOLLOW_localTriggerCondition_in_localTriggerStatement4509 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_LPAREN_in_localTriggerStatement4513 = new BitSet(new long[]{0x0000010000400000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_localTriggerCondition_in_localTriggerStatement4517 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_localTriggerStatement4521 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_localTriggerStatement4525 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4540 = new BitSet(new long[]{0x0000000000000000L,0x0000000020800000L});
    public static final BitSet FOLLOW_LBRACKET_in_localTriggerCondition4548 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4552 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_localTriggerCondition4556 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_localTriggerCondition4566 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_localTriggerCondition4570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INSERT_in_localTriggerCondition4582 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4586 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_INTO_in_localTriggerCondition4590 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4596 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_localTriggerCondition4600 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_localTriggerCondition4608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DELETE_in_localTriggerCondition4620 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4624 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_FROM_in_localTriggerCondition4628 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4634 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_localTriggerCondition4638 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_localTriggerCondition4646 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_forAlphaStatement4661 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_forAlphaStatement4665 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_alphaExpression_in_forAlphaStatement4669 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_forAlphaStatement4673 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_forAlphaStatement4677 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNITINTERVAL_in_alphaExpression4685 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_alphaExpression4689 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_DUR_in_alphaExpression4693 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_alphaExpression4697 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_FPS_in_alphaExpression4705 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_alphaExpression4709 = new BitSet(new long[]{0x0180000000000002L});
    public static final BitSet FOLLOW_WHILE_in_alphaExpression4723 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_alphaExpression4727 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_alphaExpression4741 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_IF_in_alphaExpression4745 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_alphaExpression4749 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_forJoinStatement4770 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_forJoinStatement4774 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_joinClause_in_forJoinStatement4778 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_forJoinStatement4782 = new BitSet(new long[]{0x0000000000000000L,0x0040000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_forJoinStatement4790 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_durClause_in_forJoinStatement4794 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_forJoinStatement4798 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_forJoinStatement4808 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_joinClause4816 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_joinClause4820 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_joinClause4824 = new BitSet(new long[]{0x8000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_joinClause4832 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_joinClause4836 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_joinClause4840 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_joinClause4844 = new BitSet(new long[]{0x8000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_WHERE_in_joinClause4858 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_joinClause4862 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DUR_in_durClause4876 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_durClause4880 = new BitSet(new long[]{0x07C000001C000002L});
    public static final BitSet FOLLOW_LINEAR_in_durClause4888 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_EASEIN_in_durClause4896 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_EASEOUT_in_durClause4904 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_EASEBOTH_in_durClause4912 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_MOTION_in_durClause4920 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_durClause4924 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_FPS_in_durClause4938 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_durClause4942 = new BitSet(new long[]{0x0180000000000002L});
    public static final BitSet FOLLOW_WHILE_in_durClause4954 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_durClause4958 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_durClause4972 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_IF_in_durClause4976 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_durClause4980 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRY_in_tryStatement5001 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement5005 = new BitSet(new long[]{0x1000000000100000L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement5013 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement5017 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catchClause_in_tryStatement5027 = new BitSet(new long[]{0x1000000000100002L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement5037 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement5041 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CATCH_in_catchClause5059 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_catchClause5063 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_catchClause5067 = new BitSet(new long[]{0x0000100000000000L,0x0002000000400000L});
    public static final BitSet FOLLOW_typeReference_in_catchClause5071 = new BitSet(new long[]{0x0000100000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_IF_in_catchClause5081 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_catchClause5085 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_catchClause5095 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_catchClause5099 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_foreach_in_expression5113 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionExpression_in_expression5126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_operationExpression_in_expression5139 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_alphaExpression_in_expression5152 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifExpression_in_expression5165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_selectExpression_in_expression5181 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_expression5194 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_typeName_in_expression5200 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_expression5203 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_suffixedExpression_in_expression5207 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_suffixedExpression_in_expression5222 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOREACH_in_foreach5234 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_foreach5238 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_foreach5242 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_foreach5246 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_foreach5250 = new BitSet(new long[]{0x8000000000000000L,0x0000000004400000L});
    public static final BitSet FOLLOW_COMMA_in_foreach5258 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_foreach5262 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_foreach5266 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_foreach5270 = new BitSet(new long[]{0x8000000000000000L,0x0000000004400000L});
    public static final BitSet FOLLOW_WHERE_in_foreach5284 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_foreach5288 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_foreach5298 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_foreach5302 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_functionExpression5310 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_functionExpression5314 = new BitSet(new long[]{0x0000000000000000L,0x0042000020000000L});
    public static final BitSet FOLLOW_typeReference_in_functionExpression5318 = new BitSet(new long[]{0x0000000000000000L,0x0040000020000000L});
    public static final BitSet FOLLOW_functionBody_in_functionExpression5324 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATION_in_operationExpression5332 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_operationExpression5336 = new BitSet(new long[]{0x0000000000000000L,0x0042000000000000L});
    public static final BitSet FOLLOW_typeReference_in_operationExpression5340 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_operationExpression5346 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifExpression5354 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_ifExpression5358 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_THEN_in_ifExpression5362 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_ifExpression5366 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ELSE_in_ifExpression5370 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_ifExpression5374 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SELECT_in_selectExpression5382 = new BitSet(new long[]{0x4017900000800060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_DISTINCT_in_selectExpression5386 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_selectExpression5394 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_FROM_in_selectExpression5398 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_selectionVar_in_selectExpression5402 = new BitSet(new long[]{0x8000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_selectExpression5410 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_selectionVar_in_selectExpression5414 = new BitSet(new long[]{0x8000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_WHERE_in_selectExpression5428 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_selectExpression5432 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_selectionVar5446 = new BitSet(new long[]{0x0020000000000002L});
    public static final BitSet FOLLOW_IN_in_selectionVar5454 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_selectionVar5458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentExpression_in_suffixedExpression5481 = new BitSet(new long[]{0x0000000002000002L,0x0000005000001800L});
    public static final BitSet FOLLOW_indexOn_in_suffixedExpression5493 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orderBy_in_suffixedExpression5497 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_durClause_in_suffixedExpression5501 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_suffixedExpression5505 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_suffixedExpression5509 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression5531 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_assignmentExpression5546 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression5552 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression5579 = new BitSet(new long[]{0x0000000000000002L,0x00007C0000000000L});
    public static final BitSet FOLLOW_assignmentOperator_in_assignmentOpExpression5595 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression5601 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orExpression_in_andExpression5627 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_AND_in_andExpression5643 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_orExpression_in_andExpression5649 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression5677 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_OR_in_orExpression5692 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression5698 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression5726 = new BitSet(new long[]{0x0000000000000002L,0x0000000000002000L});
    public static final BitSet FOLLOW_INSTANCEOF_in_instanceOfExpression5741 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_instanceOfExpression5743 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5771 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_LTGT_in_relationalExpression5787 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5793 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_EQEQ_in_relationalExpression5807 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5813 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_LTEQ_in_relationalExpression5827 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5833 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_GTEQ_in_relationalExpression5847 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5853 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_LT_in_relationalExpression5867 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5875 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_GT_in_relationalExpression5889 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5897 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_IN_in_relationalExpression5911 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5919 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression5948 = new BitSet(new long[]{0x0000000000000002L,0x0000002800000000L});
    public static final BitSet FOLLOW_PLUS_in_additiveExpression5963 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression5969 = new BitSet(new long[]{0x0000000000000002L,0x0000002800000000L});
    public static final BitSet FOLLOW_SUB_in_additiveExpression5982 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression5989 = new BitSet(new long[]{0x0000000000000002L,0x0000002800000000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression6017 = new BitSet(new long[]{0x0000000000000002L,0x0000038000000000L});
    public static final BitSet FOLLOW_STAR_in_multiplicativeExpression6033 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression6040 = new BitSet(new long[]{0x0000000000000002L,0x0000038000000000L});
    public static final BitSet FOLLOW_SLASH_in_multiplicativeExpression6054 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression6060 = new BitSet(new long[]{0x0000000000000002L,0x0000038000000000L});
    public static final BitSet FOLLOW_PERCENT_in_multiplicativeExpression6074 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression6078 = new BitSet(new long[]{0x0000000000000002L,0x0000038000000000L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression6108 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryOperator_in_unaryExpression6119 = new BitSet(new long[]{0x0007800000000000L,0x5828000008A14002L,0x0000000000000002L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression6123 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primaryExpression_in_postfixExpression6143 = new BitSet(new long[]{0x0000000000000002L,0x0000000008800000L});
    public static final BitSet FOLLOW_DOT_in_postfixExpression6158 = new BitSet(new long[]{0x0000000000200000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_CLASS_in_postfixExpression6162 = new BitSet(new long[]{0x0000000000000002L,0x0000000008800000L});
    public static final BitSet FOLLOW_name_in_postfixExpression6186 = new BitSet(new long[]{0x0000000000000002L,0x0000000008A00000L});
    public static final BitSet FOLLOW_LPAREN_in_postfixExpression6211 = new BitSet(new long[]{0x4017900000000060L,0x582C007008EDC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expressionListOpt_in_postfixExpression6213 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_postfixExpression6215 = new BitSet(new long[]{0x0000000000000002L,0x0000000008A00000L});
    public static final BitSet FOLLOW_LBRACKET_in_postfixExpression6247 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_postfixExpression6250 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_BAR_in_postfixExpression6252 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_postfixExpression6256 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_postfixExpression6259 = new BitSet(new long[]{0x0000000000000002L,0x0000000008800000L});
    public static final BitSet FOLLOW_newExpression_in_primaryExpression6284 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression6296 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_primaryExpression6298 = new BitSet(new long[]{0x0000004200004000L,0x0A00000000000060L,0x0000000000000002L});
    public static final BitSet FOLLOW_objectLiteral_in_primaryExpression6301 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_primaryExpression6303 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_bracketExpression_in_primaryExpression6313 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ordinalExpression_in_primaryExpression6328 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_contextExpression_in_primaryExpression6340 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THIS_in_primaryExpression6352 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_primaryExpression6371 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression6390 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression6411 = new BitSet(new long[]{0x4017900000000060L,0x582C007008EDC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expressionListOpt_in_primaryExpression6415 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression6419 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_stringExpression_in_primaryExpression6438 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primaryExpression6456 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression6475 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_primaryExpression6477 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression6479 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_newExpression6511 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_newExpression6514 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_newExpression6522 = new BitSet(new long[]{0x4017900000000060L,0x582C007008EDC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expressionListOpt_in_newExpression6526 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_newExpression6530 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_objectLiteralPart_in_objectLiteral6570 = new BitSet(new long[]{0x0000004200004002L,0x0800000000000060L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_objectLiteralPart6596 = new BitSet(new long[]{0x0000000000000000L,0x0002000000000000L});
    public static final BitSet FOLLOW_COLON_in_objectLiteralPart6598 = new BitSet(new long[]{0x4017900060010060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_bindOpt_in_objectLiteralPart6601 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_objectLiteralPart6603 = new BitSet(new long[]{0x0000000000000002L,0x0000000006000000L});
    public static final BitSet FOLLOW_set_in_objectLiteralPart6605 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_objectLiteralPart6625 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_objectLiteralPart6629 = new BitSet(new long[]{0x0000000000000000L,0x0002000020000000L});
    public static final BitSet FOLLOW_typeReference_in_objectLiteralPart6633 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_objectLiteralPart6637 = new BitSet(new long[]{0x4017900060010060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_bindOpt_in_objectLiteralPart6640 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_objectLiteralPart6642 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_objectLiteralPart6646 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localOperationDefinition_in_objectLiteralPart6658 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_objectLiteralPart6670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localTriggerStatement_in_objectLiteralPart6682 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDefinition_in_objectLiteralPart6694 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_bracketExpression6702 = new BitSet(new long[]{0x4017900000000060L,0x582C007009ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_generatorClause_in_bracketExpression6710 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_dotDotClause_in_bracketExpression6718 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_expressionListOpt_in_bracketExpression6726 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_bracketExpression6734 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_dotDotClause6742 = new BitSet(new long[]{0x0000000000000080L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_dotDotClause6750 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_dotDotClause6754 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_DOTDOT_in_dotDotClause6764 = new BitSet(new long[]{0x4017900000000060L,0x582C007088ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_dotDotClause6768 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_dotDotClause6774 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_generatorClause6782 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_BAR_in_generatorClause6786 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_generator_in_generatorClause6790 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_generatorClause6800 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_generator_in_generatorClause6808 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_expression_in_generatorClause6816 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_name_in_generator6836 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_LARROW_in_generator6840 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_generator6844 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INDEXOF_in_ordinalExpression6852 = new BitSet(new long[]{0x0000000000000000L,0x0800000008000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_ordinalExpression6860 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_ordinalExpression6868 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_contextExpression6880 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression6902 = new BitSet(new long[]{0x4017900000000060L,0x5C2C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression6911 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_stringExpression6922 = new BitSet(new long[]{0x0000000000000000L,0x0180000000000000L});
    public static final BitSet FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression6937 = new BitSet(new long[]{0x4017900000000060L,0x5C2C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression6949 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_stringExpression6963 = new BitSet(new long[]{0x0000000000000000L,0x0180000000000000L});
    public static final BitSet FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression6984 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull7014 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt7046 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_expressionListOpt7057 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt7063 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_ORDER_in_orderBy7085 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_BY_in_orderBy7089 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_orderBy7093 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INDEX_in_indexOn7108 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ON_in_indexOn7112 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_indexOn7116 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_indexOn7124 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_indexOn7128 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_set_in_multiplyOperator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_POUND_in_unaryOperator7172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_unaryOperator7183 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUB_in_unaryOperator7196 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_unaryOperator7209 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIZEOF_in_unaryOperator7222 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TYPEOF_in_unaryOperator7235 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REVERSE_in_unaryOperator7248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_unaryOperator7261 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_unaryOperator7274 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSEQ_in_assignmentOperator7295 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBEQ_in_assignmentOperator7308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAREQ_in_assignmentOperator7321 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SLASHEQ_in_assignmentOperator7334 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENTEQ_in_assignmentOperator7347 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLON_in_typeReference7371 = new BitSet(new long[]{0x0000000000000000L,0x0800008000200060L,0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_typeReference7411 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_typeReference7420 = new BitSet(new long[]{0x0000000000000000L,0x0006008800800000L});
    public static final BitSet FOLLOW_typeReference_in_typeReference7422 = new BitSet(new long[]{0x0000000000000002L,0x0004008800800000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference7426 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_typeReference7481 = new BitSet(new long[]{0x0000000000000002L,0x0004008800800000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference7485 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_typeReference7511 = new BitSet(new long[]{0x0000000000000002L,0x0004008800800000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference7515 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_cardinalityConstraint7546 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_cardinalityConstraint7550 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_cardinalityConstraint7562 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUS_in_cardinalityConstraint7589 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_cardinalityConstraint7616 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_literal7685 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_LITERAL_in_literal7695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOATING_POINT_LITERAL_in_literal7705 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRUE_in_literal7715 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALSE_in_literal7729 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_literal7743 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualident_in_typeName7770 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_qualident7812 = new BitSet(new long[]{0x0000000000000002L,0x0000000008000000L});
    public static final BitSet FOLLOW_DOT_in_qualident7840 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_qualident7842 = new BitSet(new long[]{0x0000000000000002L,0x0000000008000000L});
    public static final BitSet FOLLOW_name_in_identifier7879 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_name7913 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_synpred453636 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localOperationDefinition_in_synpred463644 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_backgroundStatement_in_synpred473658 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_laterStatement_in_synpred483673 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifStatement_in_synpred503709 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred533752 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_synpred533756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forAlphaStatement_in_synpred583833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forJoinStatement_in_synpred593849 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FPS_in_synpred804705 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred804709 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_synpred814723 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred814727 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_synpred824741 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_IF_in_synpred824745 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred824749 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LINEAR_in_synpred864888 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EASEIN_in_synpred874896 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EASEOUT_in_synpred884904 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EASEBOTH_in_synpred894912 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MOTION_in_synpred904920 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred904924 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FPS_in_synpred914938 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred914942 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_synpred924954 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred924958 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_synpred934972 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_IF_in_synpred934976 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred934980 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeReference_in_synpred975071 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1055194 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_typeName_in_synpred1055200 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1055203 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_suffixedExpression_in_synpred1055207 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeReference_in_synpred1085318 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeReference_in_synpred1095340 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMA_in_synpred1115410 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_selectionVar_in_synpred1115414 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_synpred1125428 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred1125432 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_synpred1436296 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_synpred1436298 = new BitSet(new long[]{0x0000004200004000L,0x0A00000000000060L,0x0000000000000002L});
    public static final BitSet FOLLOW_objectLiteral_in_synpred1436301 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_synpred1436303 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_synpred1506390 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1506411 = new BitSet(new long[]{0x4017900000000060L,0x582C007008EDC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expressionListOpt_in_synpred1506415 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1506419 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_generatorClause_in_synpred1626710 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dotDotClause_in_synpred1636718 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMA_in_synpred1737124 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_synpred1737128 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred1937546 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred1937550 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_synpred1947562 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUS_in_synpred1957589 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_synpred1967616 = new BitSet(new long[]{0x0000000000000002L});

}