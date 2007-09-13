// $ANTLR 3.0.1 C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g 2007-09-06 08:27:50

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
            ruleMemo = new HashMap[299+1];
         }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g"; }

    
            public v1Parser(Context context, CharSequence content) {
               this(new CommonTokenStream(new v1Lexer(new ANTLRStringStream(content.toString()))));
               initialize(context);
        	}



    // $ANTLR start module
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:326:1: module returns [JCCompilationUnit result] : ( packageDecl )? moduleItems EOF ;
    public final JCCompilationUnit module() throws RecognitionException {
        JCCompilationUnit result = null;

        JCExpression packageDecl1 = null;

        ListBuffer<JCTree> moduleItems2 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:327:8: ( ( packageDecl )? moduleItems EOF )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:327:10: ( packageDecl )? moduleItems EOF
            {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:327:10: ( packageDecl )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==PACKAGE) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: packageDecl
                    {
                    pushFollow(FOLLOW_packageDecl_in_module2085);
                    packageDecl1=packageDecl();
                    _fsp--;
                    if (failed) return result;

                    }
                    break;

            }

            pushFollow(FOLLOW_moduleItems_in_module2088);
            moduleItems2=moduleItems();
            _fsp--;
            if (failed) return result;
            match(input,EOF,FOLLOW_EOF_in_module2090); if (failed) return result;
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:329:1: packageDecl returns [JCExpression value] : PACKAGE qualident SEMI ;
    public final JCExpression packageDecl() throws RecognitionException {
        JCExpression value = null;

        JCExpression qualident3 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:330:8: ( PACKAGE qualident SEMI )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:330:10: PACKAGE qualident SEMI
            {
            match(input,PACKAGE,FOLLOW_PACKAGE_in_packageDecl2123); if (failed) return value;
            pushFollow(FOLLOW_qualident_in_packageDecl2125);
            qualident3=qualident();
            _fsp--;
            if (failed) return value;
            match(input,SEMI,FOLLOW_SEMI_in_packageDecl2127); if (failed) return value;
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:331:1: moduleItems returns [ListBuffer<JCTree> items = new ListBuffer<JCTree>()] : ( moduleItem )* ;
    public final ListBuffer<JCTree> moduleItems() throws RecognitionException {
        ListBuffer<JCTree> items =  new ListBuffer<JCTree>();

        JCTree moduleItem4 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:332:9: ( ( moduleItem )* )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:332:11: ( moduleItem )*
            {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:332:11: ( moduleItem )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=POUND && LA2_0<=TYPEOF)||LA2_0==ABSTRACT||LA2_0==ATTRIBUTE||LA2_0==BREAK||(LA2_0>=CLASS && LA2_0<=DELETE)||LA2_0==DO||(LA2_0>=RETURN && LA2_0<=VAR)||LA2_0==IMPORT||LA2_0==TRIGGER||LA2_0==INSERT||LA2_0==IF||(LA2_0>=THIS && LA2_0<=UNITINTERVAL)||(LA2_0>=WHILE && LA2_0<=CONTINUE)||LA2_0==TRY||LA2_0==FOREACH||(LA2_0>=NOT && LA2_0<=READONLY)||(LA2_0>=INDEXOF && LA2_0<=SUPER)||(LA2_0>=SIZEOF && LA2_0<=REVERSE)||LA2_0==LPAREN||LA2_0==LBRACKET||LA2_0==DOT||(LA2_0>=PLUSPLUS && LA2_0<=SUBSUB)||(LA2_0>=QUES && LA2_0<=STRING_LITERAL)||LA2_0==QUOTE_LBRACE_STRING_LITERAL||(LA2_0>=QUOTED_IDENTIFIER && LA2_0<=INTEGER_LITERAL)||LA2_0==FLOATING_POINT_LITERAL||LA2_0==IDENTIFIER) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:332:12: moduleItem
            	    {
            	    pushFollow(FOLLOW_moduleItem_in_moduleItems2156);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:333:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );
    public final JCTree moduleItem() throws RecognitionException {
        JCTree value = null;

        JCTree importDecl5 = null;

        JFXClassDeclaration classDefinition6 = null;

        JCStatement statementExcept7 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:334:8: ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept )
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

                    if ( (LA3_8==DOT) ) {
                        alt3=4;
                    }
                    else if ( (LA3_8==LPAREN) ) {
                        alt3=7;
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

                if ( (LA3_5==QUOTED_IDENTIFIER||LA3_5==IDENTIFIER) ) {
                    int LA3_9 = input.LA(3);

                    if ( (LA3_9==DOT) ) {
                        alt3=5;
                    }
                    else if ( (LA3_9==LPAREN) ) {
                        alt3=7;
                    }
                    else {
                        if (backtracking>0) {failed=true; return value;}
                        NoViableAltException nvae =
                            new NoViableAltException("333:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );", 3, 9, input);

                        throw nvae;
                    }
                }
                else if ( (LA3_5==LPAREN) ) {
                    alt3=7;
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:334:10: importDecl
                    {
                    pushFollow(FOLLOW_importDecl_in_moduleItem2198);
                    importDecl5=importDecl();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = importDecl5; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:335:10: classDefinition
                    {
                    pushFollow(FOLLOW_classDefinition_in_moduleItem2213);
                    classDefinition6=classDefinition();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = classDefinition6; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:336:10: attributeDefinition
                    {
                    pushFollow(FOLLOW_attributeDefinition_in_moduleItem2228);
                    attributeDefinition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:337:10: memberOperationDefinition
                    {
                    pushFollow(FOLLOW_memberOperationDefinition_in_moduleItem2242);
                    memberOperationDefinition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:338:10: memberFunctionDefinition
                    {
                    pushFollow(FOLLOW_memberFunctionDefinition_in_moduleItem2255);
                    memberFunctionDefinition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:339:10: TRIGGER ON changeRule
                    {
                    match(input,TRIGGER,FOLLOW_TRIGGER_in_moduleItem2268); if (failed) return value;
                    match(input,ON,FOLLOW_ON_in_moduleItem2270); if (failed) return value;
                    pushFollow(FOLLOW_changeRule_in_moduleItem2272);
                    changeRule();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:340:10: statementExcept
                    {
                    pushFollow(FOLLOW_statementExcept_in_moduleItem2285);
                    statementExcept7=statementExcept();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = statementExcept7; 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:341:1: importDecl returns [JCTree value] : IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI ;
    public final JCTree importDecl() throws RecognitionException {
        JCTree value = null;

        Token STAR10=null;
        Token IMPORT11=null;
        JCIdent identifier8 = null;

        name_return name9 = null;


         JCExpression pid = null; 
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:343:9: ( IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:343:11: IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI
            {
            IMPORT11=(Token)input.LT(1);
            match(input,IMPORT,FOLLOW_IMPORT_in_importDecl2314); if (failed) return value;
            pushFollow(FOLLOW_identifier_in_importDecl2317);
            identifier8=identifier();
            _fsp--;
            if (failed) return value;
            if ( backtracking==0 ) {
               pid = identifier8; 
            }
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:344:18: ( DOT name )*
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
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:344:20: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_importDecl2341); if (failed) return value;
            	    pushFollow(FOLLOW_name_in_importDecl2343);
            	    name9=name();
            	    _fsp--;
            	    if (failed) return value;
            	    if ( backtracking==0 ) {
            	       pid = F.at(name9.pos).Select(pid, name9.value); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:345:18: ( DOT STAR )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==DOT) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:345:20: DOT STAR
                    {
                    match(input,DOT,FOLLOW_DOT_in_importDecl2371); if (failed) return value;
                    STAR10=(Token)input.LT(1);
                    match(input,STAR,FOLLOW_STAR_in_importDecl2373); if (failed) return value;
                    if ( backtracking==0 ) {
                       pid = F.at(pos(STAR10)).Select(pid, names.asterisk); 
                    }

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_importDecl2381); if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(pos(IMPORT11)).Import(pid, false); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:347:1: classDefinition returns [JFXClassDeclaration value] : modifierFlags CLASS name supers LBRACE classMembers RBRACE ;
    public final JFXClassDeclaration classDefinition() throws RecognitionException {
        JFXClassDeclaration value = null;

        Token CLASS12=null;
        JCModifiers modifierFlags13 = null;

        name_return name14 = null;

        ListBuffer<JCExpression> supers15 = null;

        ListBuffer<JCTree> classMembers16 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:348:2: ( modifierFlags CLASS name supers LBRACE classMembers RBRACE )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:348:4: modifierFlags CLASS name supers LBRACE classMembers RBRACE
            {
            pushFollow(FOLLOW_modifierFlags_in_classDefinition2407);
            modifierFlags13=modifierFlags();
            _fsp--;
            if (failed) return value;
            CLASS12=(Token)input.LT(1);
            match(input,CLASS,FOLLOW_CLASS_in_classDefinition2410); if (failed) return value;
            pushFollow(FOLLOW_name_in_classDefinition2412);
            name14=name();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_supers_in_classDefinition2414);
            supers15=supers();
            _fsp--;
            if (failed) return value;
            match(input,LBRACE,FOLLOW_LBRACE_in_classDefinition2416); if (failed) return value;
            pushFollow(FOLLOW_classMembers_in_classDefinition2418);
            classMembers16=classMembers();
            _fsp--;
            if (failed) return value;
            match(input,RBRACE,FOLLOW_RBRACE_in_classDefinition2420); if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(pos(CLASS12)).ClassDeclaration(modifierFlags13, 
              	  							name14.value,
              	                                	                supers15.toList(), 
              	                                	                classMembers16.toList()); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:354:1: supers returns [ListBuffer<JCExpression> ids = new ListBuffer<JCExpression>()] : ( EXTENDS id1= qualident ( COMMA idn= qualident )* )? ;
    public final ListBuffer<JCExpression> supers() throws RecognitionException {
        ListBuffer<JCExpression> ids =  new ListBuffer<JCExpression>();

        JCExpression id1 = null;

        JCExpression idn = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:355:2: ( ( EXTENDS id1= qualident ( COMMA idn= qualident )* )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:355:4: ( EXTENDS id1= qualident ( COMMA idn= qualident )* )?
            {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:355:4: ( EXTENDS id1= qualident ( COMMA idn= qualident )* )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==EXTENDS) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:355:5: EXTENDS id1= qualident ( COMMA idn= qualident )*
                    {
                    match(input,EXTENDS,FOLLOW_EXTENDS_in_supers2446); if (failed) return ids;
                    pushFollow(FOLLOW_qualident_in_supers2450);
                    id1=qualident();
                    _fsp--;
                    if (failed) return ids;
                    if ( backtracking==0 ) {
                       ids.append(id1); 
                    }
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:356:12: ( COMMA idn= qualident )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==COMMA) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:356:14: COMMA idn= qualident
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_supers2474); if (failed) return ids;
                    	    pushFollow(FOLLOW_qualident_in_supers2478);
                    	    idn=qualident();
                    	    _fsp--;
                    	    if (failed) return ids;
                    	    if ( backtracking==0 ) {
                    	       ids.append(idn); 
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
        return ids;
    }
    // $ANTLR end supers


    // $ANTLR start classMembers
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:359:1: classMembers returns [ListBuffer<JCTree> mems = new ListBuffer<JCTree>()] : ( attributeDecl | functionDecl | operationDecl )* ;
    public final ListBuffer<JCTree> classMembers() throws RecognitionException {
        ListBuffer<JCTree> mems =  new ListBuffer<JCTree>();

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:360:2: ( ( attributeDecl | functionDecl | operationDecl )* )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:360:3: ( attributeDecl | functionDecl | operationDecl )*
            {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:360:3: ( attributeDecl | functionDecl | operationDecl )*
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
                    case PRIVATE:
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
                    case PROTECTED:
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

                    }

                    }
                    break;
                case READONLY:
                    {
                    switch ( input.LA(2) ) {
                    case PUBLIC:
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
                    case PRIVATE:
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
                    case PROTECTED:
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

                        }

                        }
                        break;
                    case READONLY:
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

                        }

                        }
                        break;
                    case READONLY:
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
                        switch ( input.LA(3) ) {
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
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:360:5: attributeDecl
            	    {
            	    pushFollow(FOLLOW_attributeDecl_in_classMembers2512);
            	    attributeDecl();
            	    _fsp--;
            	    if (failed) return mems;

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:361:5: functionDecl
            	    {
            	    pushFollow(FOLLOW_functionDecl_in_classMembers2530);
            	    functionDecl();
            	    _fsp--;
            	    if (failed) return mems;

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:362:5: operationDecl
            	    {
            	    pushFollow(FOLLOW_operationDecl_in_classMembers2549);
            	    operationDecl();
            	    _fsp--;
            	    if (failed) return mems;

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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:364:1: attributeDecl : modifierFlags ATTRIBUTE name typeReference inverseClause ( orderBy | indexOn )? SEMI ;
    public final void attributeDecl() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:365:2: ( modifierFlags ATTRIBUTE name typeReference inverseClause ( orderBy | indexOn )? SEMI )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:365:4: modifierFlags ATTRIBUTE name typeReference inverseClause ( orderBy | indexOn )? SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_attributeDecl2577);
            modifierFlags();
            _fsp--;
            if (failed) return ;
            match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeDecl2579); if (failed) return ;
            pushFollow(FOLLOW_name_in_attributeDecl2581);
            name();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_typeReference_in_attributeDecl2583);
            typeReference();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_inverseClause_in_attributeDecl2585);
            inverseClause();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:365:62: ( orderBy | indexOn )?
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:365:63: orderBy
                    {
                    pushFollow(FOLLOW_orderBy_in_attributeDecl2589);
                    orderBy();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:365:73: indexOn
                    {
                    pushFollow(FOLLOW_indexOn_in_attributeDecl2593);
                    indexOn();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_attributeDecl2597); if (failed) return ;

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
    // $ANTLR end attributeDecl


    // $ANTLR start inverseClause
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:367:1: inverseClause returns [JFXMemberSelector inverse = null] : ( INVERSE memberSelector )? ;
    public final JFXMemberSelector inverseClause() throws RecognitionException {
        JFXMemberSelector inverse =  null;

        JFXMemberSelector memberSelector17 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:368:2: ( ( INVERSE memberSelector )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:368:4: ( INVERSE memberSelector )?
            {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:368:4: ( INVERSE memberSelector )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==INVERSE) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:368:5: INVERSE memberSelector
                    {
                    match(input,INVERSE,FOLLOW_INVERSE_in_inverseClause2615); if (failed) return inverse;
                    pushFollow(FOLLOW_memberSelector_in_inverseClause2617);
                    memberSelector17=memberSelector();
                    _fsp--;
                    if (failed) return inverse;
                    if ( backtracking==0 ) {
                       inverse = memberSelector17; 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:369:1: functionDecl : modifierFlags FUNCTION name formalParameters typeReference SEMI ;
    public final void functionDecl() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:370:2: ( modifierFlags FUNCTION name formalParameters typeReference SEMI )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:370:4: modifierFlags FUNCTION name formalParameters typeReference SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_functionDecl2632);
            modifierFlags();
            _fsp--;
            if (failed) return ;
            match(input,FUNCTION,FOLLOW_FUNCTION_in_functionDecl2634); if (failed) return ;
            pushFollow(FOLLOW_name_in_functionDecl2636);
            name();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_formalParameters_in_functionDecl2638);
            formalParameters();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_typeReference_in_functionDecl2640);
            typeReference();
            _fsp--;
            if (failed) return ;
            match(input,SEMI,FOLLOW_SEMI_in_functionDecl2642); if (failed) return ;

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
    // $ANTLR end functionDecl


    // $ANTLR start operationDecl
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:373:1: operationDecl : modifierFlags OPERATION name formalParameters typeReference SEMI ;
    public final void operationDecl() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:374:2: ( modifierFlags OPERATION name formalParameters typeReference SEMI )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:374:4: modifierFlags OPERATION name formalParameters typeReference SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_operationDecl2657);
            modifierFlags();
            _fsp--;
            if (failed) return ;
            match(input,OPERATION,FOLLOW_OPERATION_in_operationDecl2661); if (failed) return ;
            pushFollow(FOLLOW_name_in_operationDecl2665);
            name();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_formalParameters_in_operationDecl2669);
            formalParameters();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_typeReference_in_operationDecl2673);
            typeReference();
            _fsp--;
            if (failed) return ;
            match(input,SEMI,FOLLOW_SEMI_in_operationDecl2678); if (failed) return ;

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
    // $ANTLR end operationDecl


    // $ANTLR start attributeDefinition
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:376:1: attributeDefinition : ATTRIBUTE memberSelector EQ bindOpt expression SEMI ;
    public final void attributeDefinition() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:377:2: ( ATTRIBUTE memberSelector EQ bindOpt expression SEMI )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:377:4: ATTRIBUTE memberSelector EQ bindOpt expression SEMI
            {
            match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeDefinition2693); if (failed) return ;
            pushFollow(FOLLOW_memberSelector_in_attributeDefinition2697);
            memberSelector();
            _fsp--;
            if (failed) return ;
            match(input,EQ,FOLLOW_EQ_in_attributeDefinition2701); if (failed) return ;
            pushFollow(FOLLOW_bindOpt_in_attributeDefinition2703);
            bindOpt();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_expression_in_attributeDefinition2706);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,SEMI,FOLLOW_SEMI_in_attributeDefinition2710); if (failed) return ;

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
    // $ANTLR end attributeDefinition


    // $ANTLR start memberOperationDefinition
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:379:1: memberOperationDefinition : OPERATION memberSelector formalParameters typeReference block ;
    public final void memberOperationDefinition() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:380:2: ( OPERATION memberSelector formalParameters typeReference block )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:380:4: OPERATION memberSelector formalParameters typeReference block
            {
            match(input,OPERATION,FOLLOW_OPERATION_in_memberOperationDefinition2724); if (failed) return ;
            pushFollow(FOLLOW_memberSelector_in_memberOperationDefinition2728);
            memberSelector();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_formalParameters_in_memberOperationDefinition2732);
            formalParameters();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_typeReference_in_memberOperationDefinition2736);
            typeReference();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_block_in_memberOperationDefinition2739);
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
    // $ANTLR end memberOperationDefinition


    // $ANTLR start memberFunctionDefinition
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:382:1: memberFunctionDefinition : FUNCTION memberSelector formalParameters typeReference block ;
    public final void memberFunctionDefinition() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:383:2: ( FUNCTION memberSelector formalParameters typeReference block )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:383:4: FUNCTION memberSelector formalParameters typeReference block
            {
            match(input,FUNCTION,FOLLOW_FUNCTION_in_memberFunctionDefinition2754); if (failed) return ;
            pushFollow(FOLLOW_memberSelector_in_memberFunctionDefinition2758);
            memberSelector();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_formalParameters_in_memberFunctionDefinition2762);
            formalParameters();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_typeReference_in_memberFunctionDefinition2766);
            typeReference();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_block_in_memberFunctionDefinition2769);
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
    // $ANTLR end memberFunctionDefinition


    // $ANTLR start functionBody
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:385:1: functionBody : ( EQ expression ( whereVarDecls )? SEMI | LBRACE ( variableDefinition | localFunctionDefinition | localOperationDefinition )* RETURN expression ( SEMI )? RBRACE );
    public final void functionBody() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:386:2: ( EQ expression ( whereVarDecls )? SEMI | LBRACE ( variableDefinition | localFunctionDefinition | localOperationDefinition )* RETURN expression ( SEMI )? RBRACE )
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
                    new NoViableAltException("385:1: functionBody : ( EQ expression ( whereVarDecls )? SEMI | LBRACE ( variableDefinition | localFunctionDefinition | localOperationDefinition )* RETURN expression ( SEMI )? RBRACE );", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:386:4: EQ expression ( whereVarDecls )? SEMI
                    {
                    match(input,EQ,FOLLOW_EQ_in_functionBody2784); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_functionBody2788);
                    expression();
                    _fsp--;
                    if (failed) return ;
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:386:22: ( whereVarDecls )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==WHERE) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: whereVarDecls
                            {
                            pushFollow(FOLLOW_whereVarDecls_in_functionBody2792);
                            whereVarDecls();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }

                    match(input,SEMI,FOLLOW_SEMI_in_functionBody2798); if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:387:11: LBRACE ( variableDefinition | localFunctionDefinition | localOperationDefinition )* RETURN expression ( SEMI )? RBRACE
                    {
                    match(input,LBRACE,FOLLOW_LBRACE_in_functionBody2814); if (failed) return ;
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:387:20: ( variableDefinition | localFunctionDefinition | localOperationDefinition )*
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
                    	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:387:24: variableDefinition
                    	    {
                    	    pushFollow(FOLLOW_variableDefinition_in_functionBody2822);
                    	    variableDefinition();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;
                    	case 2 :
                    	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:387:49: localFunctionDefinition
                    	    {
                    	    pushFollow(FOLLOW_localFunctionDefinition_in_functionBody2830);
                    	    localFunctionDefinition();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;
                    	case 3 :
                    	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:387:79: localOperationDefinition
                    	    {
                    	    pushFollow(FOLLOW_localOperationDefinition_in_functionBody2838);
                    	    localOperationDefinition();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);

                    match(input,RETURN,FOLLOW_RETURN_in_functionBody2848); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_functionBody2852);
                    expression();
                    _fsp--;
                    if (failed) return ;
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:387:134: ( SEMI )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==SEMI) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: SEMI
                            {
                            match(input,SEMI,FOLLOW_SEMI_in_functionBody2856); if (failed) return ;

                            }
                            break;

                    }

                    match(input,RBRACE,FOLLOW_RBRACE_in_functionBody2862); if (failed) return ;

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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:388:1: whereVarDecls : WHERE whereVarDecl ( COMMA whereVarDecl )* ;
    public final void whereVarDecls() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:388:15: ( WHERE whereVarDecl ( COMMA whereVarDecl )* )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:388:17: WHERE whereVarDecl ( COMMA whereVarDecl )*
            {
            match(input,WHERE,FOLLOW_WHERE_in_whereVarDecls2870); if (failed) return ;
            pushFollow(FOLLOW_whereVarDecl_in_whereVarDecls2874);
            whereVarDecl();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:388:40: ( COMMA whereVarDecl )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==COMMA) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:388:44: COMMA whereVarDecl
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_whereVarDecls2882); if (failed) return ;
            	    pushFollow(FOLLOW_whereVarDecl_in_whereVarDecls2886);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:389:1: whereVarDecl : ( localFunctionDefinition | name typeReference EQ expression );
    public final void whereVarDecl() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:389:14: ( localFunctionDefinition | name typeReference EQ expression )
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==FUNCTION) ) {
                alt16=1;
            }
            else if ( (LA16_0==QUOTED_IDENTIFIER||LA16_0==IDENTIFIER) ) {
                int LA16_2 = input.LA(2);

                if ( (LA16_2==EQ||LA16_2==COLON) ) {
                    alt16=2;
                }
                else if ( (LA16_2==LPAREN) ) {
                    alt16=1;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("389:1: whereVarDecl : ( localFunctionDefinition | name typeReference EQ expression );", 16, 2, input);

                    throw nvae;
                }
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("389:1: whereVarDecl : ( localFunctionDefinition | name typeReference EQ expression );", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:389:16: localFunctionDefinition
                    {
                    pushFollow(FOLLOW_localFunctionDefinition_in_whereVarDecl2900);
                    localFunctionDefinition();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:390:10: name typeReference EQ expression
                    {
                    pushFollow(FOLLOW_name_in_whereVarDecl2912);
                    name();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_typeReference_in_whereVarDecl2916);
                    typeReference();
                    _fsp--;
                    if (failed) return ;
                    match(input,EQ,FOLLOW_EQ_in_whereVarDecl2920); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_whereVarDecl2924);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:391:1: variableDefinition : VAR name typeReference EQ expression SEMI ;
    public final void variableDefinition() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:391:20: ( VAR name typeReference EQ expression SEMI )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:391:22: VAR name typeReference EQ expression SEMI
            {
            match(input,VAR,FOLLOW_VAR_in_variableDefinition2932); if (failed) return ;
            pushFollow(FOLLOW_name_in_variableDefinition2936);
            name();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_typeReference_in_variableDefinition2940);
            typeReference();
            _fsp--;
            if (failed) return ;
            match(input,EQ,FOLLOW_EQ_in_variableDefinition2943); if (failed) return ;
            pushFollow(FOLLOW_expression_in_variableDefinition2947);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,SEMI,FOLLOW_SEMI_in_variableDefinition2951); if (failed) return ;

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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:392:1: changeRule : ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );
    public final void changeRule() throws RecognitionException {
        JCIdent id1 = null;

        JCIdent id2 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:393:2: ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block )
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
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("392:1: changeRule : ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 7, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("392:1: changeRule : ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 4, input);

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

                            if ( (LA17_11==LBRACKET) ) {
                                alt17=4;
                            }
                            else if ( (LA17_11==EQ) ) {
                                alt17=2;
                            }
                            else {
                                if (backtracking>0) {failed=true; return ;}
                                NoViableAltException nvae =
                                    new NoViableAltException("392:1: changeRule : ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 11, input);

                                throw nvae;
                            }
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("392:1: changeRule : ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 8, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("392:1: changeRule : ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 6, input);

                        throw nvae;
                    }
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("392:1: changeRule : ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA17_0==QUOTED_IDENTIFIER||LA17_0==IDENTIFIER) ) {
                alt17=3;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("392:1: changeRule : ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:393:4: LPAREN NEW typeName RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule2962); if (failed) return ;
                    match(input,NEW,FOLLOW_NEW_in_changeRule2966); if (failed) return ;
                    pushFollow(FOLLOW_typeName_in_changeRule2970);
                    typeName();
                    _fsp--;
                    if (failed) return ;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule2973); if (failed) return ;
                    pushFollow(FOLLOW_block_in_changeRule2976);
                    block();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:394:4: LPAREN memberSelector EQ identifier RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule2981); if (failed) return ;
                    pushFollow(FOLLOW_memberSelector_in_changeRule2985);
                    memberSelector();
                    _fsp--;
                    if (failed) return ;
                    match(input,EQ,FOLLOW_EQ_in_changeRule2988); if (failed) return ;
                    pushFollow(FOLLOW_identifier_in_changeRule2990);
                    identifier();
                    _fsp--;
                    if (failed) return ;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule2994); if (failed) return ;
                    pushFollow(FOLLOW_block_in_changeRule2997);
                    block();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:395:4: memberSelector EQ identifier block
                    {
                    pushFollow(FOLLOW_memberSelector_in_changeRule3002);
                    memberSelector();
                    _fsp--;
                    if (failed) return ;
                    match(input,EQ,FOLLOW_EQ_in_changeRule3005); if (failed) return ;
                    pushFollow(FOLLOW_identifier_in_changeRule3007);
                    identifier();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_block_in_changeRule3009);
                    block();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:396:4: LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule3014); if (failed) return ;
                    pushFollow(FOLLOW_memberSelector_in_changeRule3018);
                    memberSelector();
                    _fsp--;
                    if (failed) return ;
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_changeRule3022); if (failed) return ;
                    pushFollow(FOLLOW_identifier_in_changeRule3028);
                    id1=identifier();
                    _fsp--;
                    if (failed) return ;
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_changeRule3032); if (failed) return ;
                    match(input,EQ,FOLLOW_EQ_in_changeRule3036); if (failed) return ;
                    pushFollow(FOLLOW_identifier_in_changeRule3040);
                    id2=identifier();
                    _fsp--;
                    if (failed) return ;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule3044); if (failed) return ;
                    pushFollow(FOLLOW_block_in_changeRule3047);
                    block();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:397:4: LPAREN INSERT identifier INTO memberSelector RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule3052); if (failed) return ;
                    match(input,INSERT,FOLLOW_INSERT_in_changeRule3056); if (failed) return ;
                    pushFollow(FOLLOW_identifier_in_changeRule3060);
                    identifier();
                    _fsp--;
                    if (failed) return ;
                    match(input,INTO,FOLLOW_INTO_in_changeRule3064); if (failed) return ;
                    pushFollow(FOLLOW_memberSelector_in_changeRule3068);
                    memberSelector();
                    _fsp--;
                    if (failed) return ;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule3072); if (failed) return ;
                    pushFollow(FOLLOW_block_in_changeRule3074);
                    block();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:398:4: LPAREN DELETE identifier FROM memberSelector RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule3081); if (failed) return ;
                    match(input,DELETE,FOLLOW_DELETE_in_changeRule3085); if (failed) return ;
                    pushFollow(FOLLOW_identifier_in_changeRule3089);
                    identifier();
                    _fsp--;
                    if (failed) return ;
                    match(input,FROM,FOLLOW_FROM_in_changeRule3093); if (failed) return ;
                    pushFollow(FOLLOW_memberSelector_in_changeRule3097);
                    memberSelector();
                    _fsp--;
                    if (failed) return ;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule3101); if (failed) return ;
                    pushFollow(FOLLOW_block_in_changeRule3103);
                    block();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:399:4: LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule3110); if (failed) return ;
                    match(input,DELETE,FOLLOW_DELETE_in_changeRule3114); if (failed) return ;
                    pushFollow(FOLLOW_memberSelector_in_changeRule3117);
                    memberSelector();
                    _fsp--;
                    if (failed) return ;
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_changeRule3121); if (failed) return ;
                    pushFollow(FOLLOW_identifier_in_changeRule3125);
                    identifier();
                    _fsp--;
                    if (failed) return ;
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_changeRule3129); if (failed) return ;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule3133); if (failed) return ;
                    pushFollow(FOLLOW_block_in_changeRule3135);
                    block();
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
    // $ANTLR end changeRule


    // $ANTLR start modifierFlags
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:401:1: modifierFlags returns [JCModifiers mods] : (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? ;
    public final JCModifiers modifierFlags() throws RecognitionException {
        JCModifiers mods = null;

        long om1 = 0;

        long am1 = 0;

        long am2 = 0;

        long om2 = 0;


         long flags = 0; 
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:403:2: ( (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:403:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
            {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:403:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:403:6: om1= otherModifier (am1= accessModifier )?
                    {
                    pushFollow(FOLLOW_otherModifier_in_modifierFlags3158);
                    om1=otherModifier();
                    _fsp--;
                    if (failed) return mods;
                    if ( backtracking==0 ) {
                       flags |= om1; 
                    }
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:404:3: (am1= accessModifier )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( ((LA18_0>=PRIVATE && LA18_0<=PUBLIC)) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:404:5: am1= accessModifier
                            {
                            pushFollow(FOLLOW_accessModifier_in_modifierFlags3171);
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:405:6: am2= accessModifier (om2= otherModifier )?
                    {
                    pushFollow(FOLLOW_accessModifier_in_modifierFlags3193);
                    am2=accessModifier();
                    _fsp--;
                    if (failed) return mods;
                    if ( backtracking==0 ) {
                       flags |= am2; 
                    }
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:406:3: (om2= otherModifier )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==ABSTRACT||LA19_0==READONLY) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:406:5: om2= otherModifier
                            {
                            pushFollow(FOLLOW_otherModifier_in_modifierFlags3206);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:409:1: accessModifier returns [long flags = 0] : ( PUBLIC | PRIVATE | PROTECTED ) ;
    public final long accessModifier() throws RecognitionException {
        long flags =  0;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:410:2: ( ( PUBLIC | PRIVATE | PROTECTED ) )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:410:4: ( PUBLIC | PRIVATE | PROTECTED )
            {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:410:4: ( PUBLIC | PRIVATE | PROTECTED )
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
                    new NoViableAltException("410:4: ( PUBLIC | PRIVATE | PROTECTED )", 21, 0, input);

                throw nvae;
            }

            switch (alt21) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:410:5: PUBLIC
                    {
                    match(input,PUBLIC,FOLLOW_PUBLIC_in_accessModifier3254); if (failed) return flags;
                    if ( backtracking==0 ) {
                       flags |= Flags.PUBLIC; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:5: PRIVATE
                    {
                    match(input,PRIVATE,FOLLOW_PRIVATE_in_accessModifier3271); if (failed) return flags;
                    if ( backtracking==0 ) {
                       flags |= Flags.PUBLIC; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:412:5: PROTECTED
                    {
                    match(input,PROTECTED,FOLLOW_PROTECTED_in_accessModifier3287); if (failed) return flags;
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:413:1: otherModifier returns [long flags = 0] : ( ABSTRACT | READONLY ) ;
    public final long otherModifier() throws RecognitionException {
        long flags =  0;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:414:2: ( ( ABSTRACT | READONLY ) )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:414:4: ( ABSTRACT | READONLY )
            {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:414:4: ( ABSTRACT | READONLY )
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
                    new NoViableAltException("414:4: ( ABSTRACT | READONLY )", 22, 0, input);

                throw nvae;
            }
            switch (alt22) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:414:5: ABSTRACT
                    {
                    match(input,ABSTRACT,FOLLOW_ABSTRACT_in_otherModifier3311); if (failed) return flags;
                    if ( backtracking==0 ) {
                       flags |= Flags.ABSTRACT; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:5: READONLY
                    {
                    match(input,READONLY,FOLLOW_READONLY_in_otherModifier3326); if (failed) return flags;
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:416:1: memberSelector returns [JFXMemberSelector value] : name1= name DOT name2= name ;
    public final JFXMemberSelector memberSelector() throws RecognitionException {
        JFXMemberSelector value = null;

        name_return name1 = null;

        name_return name2 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:417:2: (name1= name DOT name2= name )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:417:4: name1= name DOT name2= name
            {
            pushFollow(FOLLOW_name_in_memberSelector3352);
            name1=name();
            _fsp--;
            if (failed) return value;
            match(input,DOT,FOLLOW_DOT_in_memberSelector3356); if (failed) return value;
            pushFollow(FOLLOW_name_in_memberSelector3362);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:418:1: formalParameters returns [ListBuffer<JCTree> params = new ListBuffer<JCTree>()] : LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN ;
    public final ListBuffer<JCTree> formalParameters() throws RecognitionException {
        ListBuffer<JCTree> params =  new ListBuffer<JCTree>();

        JFXVar fp0 = null;

        JFXVar fpn = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:419:2: ( LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:419:4: LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_formalParameters3378); if (failed) return params;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:419:13: (fp0= formalParameter ( COMMA fpn= formalParameter )* )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==QUOTED_IDENTIFIER||LA24_0==IDENTIFIER) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:419:15: fp0= formalParameter ( COMMA fpn= formalParameter )*
                    {
                    pushFollow(FOLLOW_formalParameter_in_formalParameters3386);
                    fp0=formalParameter();
                    _fsp--;
                    if (failed) return params;
                    if ( backtracking==0 ) {
                       params.append(fp0); 
                    }
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:420:13: ( COMMA fpn= formalParameter )*
                    loop23:
                    do {
                        int alt23=2;
                        int LA23_0 = input.LA(1);

                        if ( (LA23_0==COMMA) ) {
                            alt23=1;
                        }


                        switch (alt23) {
                    	case 1 :
                    	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:420:15: COMMA fpn= formalParameter
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_formalParameters3405); if (failed) return params;
                    	    pushFollow(FOLLOW_formalParameter_in_formalParameters3411);
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

            match(input,RPAREN,FOLLOW_RPAREN_in_formalParameters3422); if (failed) return params;

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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:421:1: formalParameter returns [JFXVar var] : name typeReference ;
    public final JFXVar formalParameter() throws RecognitionException {
        JFXVar var = null;

        name_return name18 = null;

        JFXType typeReference19 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:422:2: ( name typeReference )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:422:4: name typeReference
            {
            pushFollow(FOLLOW_name_in_formalParameter3435);
            name18=name();
            _fsp--;
            if (failed) return var;
            pushFollow(FOLLOW_typeReference_in_formalParameter3437);
            typeReference19=typeReference();
            _fsp--;
            if (failed) return var;
            if ( backtracking==0 ) {
               var = F.at(name18.pos).Var(name18.value, typeReference19, F.Modifiers(Flags.PARAMETER), null, null); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:423:1: block returns [JCBlock value] : LBRACE statements RBRACE ;
    public final JCBlock block() throws RecognitionException {
        JCBlock value = null;

        Token LBRACE20=null;
        ListBuffer<JCStatement> statements21 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:424:2: ( LBRACE statements RBRACE )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:424:4: LBRACE statements RBRACE
            {
            LBRACE20=(Token)input.LT(1);
            match(input,LBRACE,FOLLOW_LBRACE_in_block3454); if (failed) return value;
            pushFollow(FOLLOW_statements_in_block3458);
            statements21=statements();
            _fsp--;
            if (failed) return value;
            match(input,RBRACE,FOLLOW_RBRACE_in_block3462); if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(pos(LBRACE20)).Block(0L, statements21.toList()); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:426:1: statements returns [ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>()] : ( statement )* ;
    public final ListBuffer<JCStatement> statements() throws RecognitionException {
        ListBuffer<JCStatement> stats =  new ListBuffer<JCStatement>();

        JCStatement statement22 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:427:2: ( ( statement )* )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:427:4: ( statement )*
            {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:427:4: ( statement )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( ((LA25_0>=POUND && LA25_0<=TYPEOF)||LA25_0==BREAK||LA25_0==DELETE||LA25_0==DO||(LA25_0>=RETURN && LA25_0<=VAR)||LA25_0==TRIGGER||LA25_0==INSERT||LA25_0==IF||(LA25_0>=THIS && LA25_0<=UNITINTERVAL)||(LA25_0>=WHILE && LA25_0<=CONTINUE)||LA25_0==TRY||LA25_0==FOREACH||(LA25_0>=NOT && LA25_0<=NEW)||(LA25_0>=OPERATION && LA25_0<=FUNCTION)||(LA25_0>=INDEXOF && LA25_0<=SUPER)||(LA25_0>=SIZEOF && LA25_0<=REVERSE)||LA25_0==LPAREN||LA25_0==LBRACKET||LA25_0==DOT||(LA25_0>=PLUSPLUS && LA25_0<=SUBSUB)||(LA25_0>=QUES && LA25_0<=STRING_LITERAL)||LA25_0==QUOTE_LBRACE_STRING_LITERAL||(LA25_0>=QUOTED_IDENTIFIER && LA25_0<=INTEGER_LITERAL)||LA25_0==FLOATING_POINT_LITERAL||LA25_0==IDENTIFIER) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:427:5: statement
            	    {
            	    pushFollow(FOLLOW_statement_in_statements3480);
            	    statement22=statement();
            	    _fsp--;
            	    if (failed) return stats;
            	    if ( backtracking==0 ) {
            	       stats.append(statement22); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:428:1: statement returns [JCStatement value] : ( statementExcept | localTriggerStatement );
    public final JCStatement statement() throws RecognitionException {
        JCStatement value = null;

        JCStatement statementExcept23 = null;

        JCStatement localTriggerStatement24 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:429:8: ( statementExcept | localTriggerStatement )
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
                    new NoViableAltException("428:1: statement returns [JCStatement value] : ( statementExcept | localTriggerStatement );", 26, 0, input);

                throw nvae;
            }
            switch (alt26) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:429:10: statementExcept
                    {
                    pushFollow(FOLLOW_statementExcept_in_statement3531);
                    statementExcept23=statementExcept();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = statementExcept23; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:430:10: localTriggerStatement
                    {
                    pushFollow(FOLLOW_localTriggerStatement_in_statement3547);
                    localTriggerStatement24=localTriggerStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = localTriggerStatement24; 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:431:1: statementExcept returns [JCStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );
    public final JCStatement statementExcept() throws RecognitionException {
        JCStatement value = null;

        Token WHILE28=null;
        Token SEMI34=null;
        Token BREAK36=null;
        Token CONTINUE37=null;
        JCStatement variableDeclaration25 = null;

        JCStatement localFunctionDefinition26 = null;

        JCStatement localOperationDefinition27 = null;

        JCExpression expression29 = null;

        JCBlock block30 = null;

        JCStatement ifStatement31 = null;

        JCStatement insertStatement32 = null;

        JCStatement deleteStatement33 = null;

        JCExpression expression35 = null;

        JCStatement throwStatement38 = null;

        JCStatement returnStatement39 = null;

        JCStatement forAlphaStatement40 = null;

        JCStatement forJoinStatement41 = null;

        JCStatement tryStatement42 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:2: ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement )
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
                        new NoViableAltException("431:1: statementExcept returns [JCStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 2, input);

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
                        new NoViableAltException("431:1: statementExcept returns [JCStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 3, input);

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
                        new NoViableAltException("431:1: statementExcept returns [JCStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 4, input);

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
                        new NoViableAltException("431:1: statementExcept returns [JCStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 5, input);

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
                        new NoViableAltException("431:1: statementExcept returns [JCStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 7, input);

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
                        new NoViableAltException("431:1: statementExcept returns [JCStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 40, input);

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
                    new NoViableAltException("431:1: statementExcept returns [JCStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 0, input);

                throw nvae;
            }

            switch (alt27) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:4: variableDeclaration
                    {
                    pushFollow(FOLLOW_variableDeclaration_in_statementExcept3565);
                    variableDeclaration25=variableDeclaration();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = variableDeclaration25; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:433:4: localFunctionDefinition
                    {
                    pushFollow(FOLLOW_localFunctionDefinition_in_statementExcept3575);
                    localFunctionDefinition26=localFunctionDefinition();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = localFunctionDefinition26; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:434:4: localOperationDefinition
                    {
                    pushFollow(FOLLOW_localOperationDefinition_in_statementExcept3583);
                    localOperationDefinition27=localOperationDefinition();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = localOperationDefinition27; 
                    }

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:435:10: backgroundStatement
                    {
                    pushFollow(FOLLOW_backgroundStatement_in_statementExcept3597);
                    backgroundStatement();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:436:10: laterStatement
                    {
                    pushFollow(FOLLOW_laterStatement_in_statementExcept3612);
                    laterStatement();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:437:10: WHILE LPAREN expression RPAREN block
                    {
                    WHILE28=(Token)input.LT(1);
                    match(input,WHILE,FOLLOW_WHILE_in_statementExcept3627); if (failed) return value;
                    match(input,LPAREN,FOLLOW_LPAREN_in_statementExcept3629); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_statementExcept3631);
                    expression29=expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_statementExcept3633); if (failed) return value;
                    pushFollow(FOLLOW_block_in_statementExcept3635);
                    block30=block();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(WHILE28)).WhileLoop(expression29, block30); 
                    }

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:438:10: ifStatement
                    {
                    pushFollow(FOLLOW_ifStatement_in_statementExcept3648);
                    ifStatement31=ifStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = ifStatement31; 
                    }

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:439:10: insertStatement
                    {
                    pushFollow(FOLLOW_insertStatement_in_statementExcept3665);
                    insertStatement32=insertStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = insertStatement32; 
                    }

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:440:10: deleteStatement
                    {
                    pushFollow(FOLLOW_deleteStatement_in_statementExcept3681);
                    deleteStatement33=deleteStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = deleteStatement33; 
                    }

                    }
                    break;
                case 10 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:441:4: expression SEMI
                    {
                    pushFollow(FOLLOW_expression_in_statementExcept3691);
                    expression35=expression();
                    _fsp--;
                    if (failed) return value;
                    SEMI34=(Token)input.LT(1);
                    match(input,SEMI,FOLLOW_SEMI_in_statementExcept3695); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(SEMI34)).Exec(expression35); 
                    }

                    }
                    break;
                case 11 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:442:4: BREAK SEMI
                    {
                    BREAK36=(Token)input.LT(1);
                    match(input,BREAK,FOLLOW_BREAK_in_statementExcept3705); if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_statementExcept3709); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(BREAK36)).Break(null); 
                    }

                    }
                    break;
                case 12 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:443:4: CONTINUE SEMI
                    {
                    CONTINUE37=(Token)input.LT(1);
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_statementExcept3720); if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_statementExcept3724); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(CONTINUE37)).Continue(null); 
                    }

                    }
                    break;
                case 13 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:444:10: throwStatement
                    {
                    pushFollow(FOLLOW_throwStatement_in_statementExcept3740);
                    throwStatement38=throwStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = throwStatement38; 
                    }

                    }
                    break;
                case 14 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:445:10: returnStatement
                    {
                    pushFollow(FOLLOW_returnStatement_in_statementExcept3756);
                    returnStatement39=returnStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = returnStatement39; 
                    }

                    }
                    break;
                case 15 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:446:10: forAlphaStatement
                    {
                    pushFollow(FOLLOW_forAlphaStatement_in_statementExcept3772);
                    forAlphaStatement40=forAlphaStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = forAlphaStatement40; 
                    }

                    }
                    break;
                case 16 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:447:10: forJoinStatement
                    {
                    pushFollow(FOLLOW_forJoinStatement_in_statementExcept3788);
                    forJoinStatement41=forJoinStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = forJoinStatement41; 
                    }

                    }
                    break;
                case 17 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:448:10: tryStatement
                    {
                    pushFollow(FOLLOW_tryStatement_in_statementExcept3804);
                    tryStatement42=tryStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = tryStatement42; 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:449:1: assertStatement returns [JCStatement value = null] : ASSERT expression ( COLON expression )? SEMI ;
    public final JCStatement assertStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:450:2: ( ASSERT expression ( COLON expression )? SEMI )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:450:4: ASSERT expression ( COLON expression )? SEMI
            {
            match(input,ASSERT,FOLLOW_ASSERT_in_assertStatement3823); if (failed) return value;
            pushFollow(FOLLOW_expression_in_assertStatement3827);
            expression();
            _fsp--;
            if (failed) return value;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:450:26: ( COLON expression )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==COLON) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:450:30: COLON expression
                    {
                    match(input,COLON,FOLLOW_COLON_in_assertStatement3835); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_assertStatement3839);
                    expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_assertStatement3849); if (failed) return value;

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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:451:1: localOperationDefinition returns [JCStatement value] : OPERATION name formalParameters typeReference block ;
    public final JCStatement localOperationDefinition() throws RecognitionException {
        JCStatement value = null;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:452:2: ( OPERATION name formalParameters typeReference block )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:452:4: OPERATION name formalParameters typeReference block
            {
            match(input,OPERATION,FOLLOW_OPERATION_in_localOperationDefinition3864); if (failed) return value;
            pushFollow(FOLLOW_name_in_localOperationDefinition3868);
            name();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_formalParameters_in_localOperationDefinition3872);
            formalParameters();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_typeReference_in_localOperationDefinition3876);
            typeReference();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_block_in_localOperationDefinition3879);
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
    // $ANTLR end localOperationDefinition


    // $ANTLR start localFunctionDefinition
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:454:1: localFunctionDefinition returns [JCStatement value] : ( FUNCTION )? name formalParameters typeReference block ;
    public final JCStatement localFunctionDefinition() throws RecognitionException {
        JCStatement value = null;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:455:2: ( ( FUNCTION )? name formalParameters typeReference block )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:455:4: ( FUNCTION )? name formalParameters typeReference block
            {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:455:4: ( FUNCTION )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==FUNCTION) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: FUNCTION
                    {
                    match(input,FUNCTION,FOLLOW_FUNCTION_in_localFunctionDefinition3898); if (failed) return value;

                    }
                    break;

            }

            pushFollow(FOLLOW_name_in_localFunctionDefinition3904);
            name();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_formalParameters_in_localFunctionDefinition3908);
            formalParameters();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_typeReference_in_localFunctionDefinition3912);
            typeReference();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_block_in_localFunctionDefinition3915);
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
    // $ANTLR end localFunctionDefinition


    // $ANTLR start variableDeclaration
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:457:1: variableDeclaration returns [JCStatement value] : VAR name typeReference ( EQ bindOpt expression SEMI | SEMI ) ;
    public final JCStatement variableDeclaration() throws RecognitionException {
        JCStatement value = null;

        Token VAR43=null;
        name_return name44 = null;

        JFXType typeReference45 = null;

        JCExpression expression46 = null;

        JavafxBindStatus bindOpt47 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:458:2: ( VAR name typeReference ( EQ bindOpt expression SEMI | SEMI ) )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:458:4: VAR name typeReference ( EQ bindOpt expression SEMI | SEMI )
            {
            VAR43=(Token)input.LT(1);
            match(input,VAR,FOLLOW_VAR_in_variableDeclaration3934); if (failed) return value;
            pushFollow(FOLLOW_name_in_variableDeclaration3937);
            name44=name();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_typeReference_in_variableDeclaration3940);
            typeReference45=typeReference();
            _fsp--;
            if (failed) return value;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:459:6: ( EQ bindOpt expression SEMI | SEMI )
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
                    new NoViableAltException("459:6: ( EQ bindOpt expression SEMI | SEMI )", 30, 0, input);

                throw nvae;
            }
            switch (alt30) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:459:8: EQ bindOpt expression SEMI
                    {
                    match(input,EQ,FOLLOW_EQ_in_variableDeclaration3951); if (failed) return value;
                    pushFollow(FOLLOW_bindOpt_in_variableDeclaration3953);
                    bindOpt47=bindOpt();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_expression_in_variableDeclaration3956);
                    expression46=expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_variableDeclaration3958); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(VAR43)).Var(name44.value, typeReference45, F.Modifiers(Flags.PARAMETER),
                      	    							expression46, bindOpt47); 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:461:8: SEMI
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_variableDeclaration3969); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(VAR43)).Var(name44.value, typeReference45, F.Modifiers(Flags.PARAMETER),
                      	    							expression46, bindOpt47); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:465:1: bindOpt returns [JavafxBindStatus status = UNBOUND] : ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? ;
    public final JavafxBindStatus bindOpt() throws RecognitionException {
        JavafxBindStatus status =  UNBOUND;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:466:2: ( ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:466:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
            {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:466:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:466:6: BIND ( LAZY )?
                    {
                    match(input,BIND,FOLLOW_BIND_in_bindOpt4006); if (failed) return status;
                    if ( backtracking==0 ) {
                       status = UNIDIBIND; 
                    }
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:467:8: ( LAZY )?
                    int alt31=2;
                    int LA31_0 = input.LA(1);

                    if ( (LA31_0==LAZY) ) {
                        alt31=1;
                    }
                    switch (alt31) {
                        case 1 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:467:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt4022); if (failed) return status;
                            if ( backtracking==0 ) {
                               status = LAZY_UNIDIBIND; 
                            }

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:468:6: STAYS ( LAZY )?
                    {
                    match(input,STAYS,FOLLOW_STAYS_in_bindOpt4037); if (failed) return status;
                    if ( backtracking==0 ) {
                       status = UNIDIBIND; 
                    }
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:469:8: ( LAZY )?
                    int alt32=2;
                    int LA32_0 = input.LA(1);

                    if ( (LA32_0==LAZY) ) {
                        alt32=1;
                    }
                    switch (alt32) {
                        case 1 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:469:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt4053); if (failed) return status;
                            if ( backtracking==0 ) {
                               status = LAZY_UNIDIBIND; 
                            }

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:470:6: TIE ( LAZY )?
                    {
                    match(input,TIE,FOLLOW_TIE_in_bindOpt4068); if (failed) return status;
                    if ( backtracking==0 ) {
                       status = BIDIBIND; 
                    }
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:471:8: ( LAZY )?
                    int alt33=2;
                    int LA33_0 = input.LA(1);

                    if ( (LA33_0==LAZY) ) {
                        alt33=1;
                    }
                    switch (alt33) {
                        case 1 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:471:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt4084); if (failed) return status;
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:473:1: backgroundStatement : DO block ;
    public final void backgroundStatement() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:474:2: ( DO block )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:474:4: DO block
            {
            match(input,DO,FOLLOW_DO_in_backgroundStatement4110); if (failed) return ;
            pushFollow(FOLLOW_block_in_backgroundStatement4114);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:475:1: laterStatement : DO LATER block ;
    public final void laterStatement() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:476:2: ( DO LATER block )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:476:4: DO LATER block
            {
            match(input,DO,FOLLOW_DO_in_laterStatement4130); if (failed) return ;
            match(input,LATER,FOLLOW_LATER_in_laterStatement4134); if (failed) return ;
            pushFollow(FOLLOW_block_in_laterStatement4138);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:477:1: ifStatement returns [JCStatement value] : IF LPAREN expression RPAREN s1= block ( ELSE s2= block )? ;
    public final JCStatement ifStatement() throws RecognitionException {
        JCStatement value = null;

        Token IF48=null;
        JCBlock s1 = null;

        JCBlock s2 = null;

        JCExpression expression49 = null;


         JCStatement elsepart = null; 
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:479:2: ( IF LPAREN expression RPAREN s1= block ( ELSE s2= block )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:479:4: IF LPAREN expression RPAREN s1= block ( ELSE s2= block )?
            {
            IF48=(Token)input.LT(1);
            match(input,IF,FOLLOW_IF_in_ifStatement4158); if (failed) return value;
            match(input,LPAREN,FOLLOW_LPAREN_in_ifStatement4162); if (failed) return value;
            pushFollow(FOLLOW_expression_in_ifStatement4166);
            expression49=expression();
            _fsp--;
            if (failed) return value;
            match(input,RPAREN,FOLLOW_RPAREN_in_ifStatement4170); if (failed) return value;
            pushFollow(FOLLOW_block_in_ifStatement4176);
            s1=block();
            _fsp--;
            if (failed) return value;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:479:49: ( ELSE s2= block )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==ELSE) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:479:50: ELSE s2= block
                    {
                    match(input,ELSE,FOLLOW_ELSE_in_ifStatement4179); if (failed) return value;
                    pushFollow(FOLLOW_block_in_ifStatement4184);
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
               value = F.at(pos(IF48)).If(expression49, s1, elsepart); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:481:1: insertStatement returns [JCStatement value = null] : INSERT ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) ) SEMI ;
    public final JCStatement insertStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:482:2: ( INSERT ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) ) SEMI )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:482:4: INSERT ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) ) SEMI
            {
            match(input,INSERT,FOLLOW_INSERT_in_insertStatement4213); if (failed) return value;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:482:13: ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) )
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
                    new NoViableAltException("482:13: ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) )", 38, 0, input);

                throw nvae;
            }
            switch (alt38) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:482:17: DISTINCT expression INTO expression
                    {
                    match(input,DISTINCT,FOLLOW_DISTINCT_in_insertStatement4221); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_insertStatement4225);
                    expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,INTO,FOLLOW_INTO_in_insertStatement4229); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_insertStatement4233);
                    expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:482:65: expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression )
                    {
                    pushFollow(FOLLOW_expression_in_insertStatement4241);
                    expression();
                    _fsp--;
                    if (failed) return value;
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:482:78: ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression )
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
                            new NoViableAltException("482:78: ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression )", 37, 0, input);

                        throw nvae;
                    }

                    switch (alt37) {
                        case 1 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:482:82: ( ( AS ( FIRST | LAST ) )? INTO expression )
                            {
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:482:82: ( ( AS ( FIRST | LAST ) )? INTO expression )
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:482:86: ( AS ( FIRST | LAST ) )? INTO expression
                            {
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:482:86: ( AS ( FIRST | LAST ) )?
                            int alt36=2;
                            int LA36_0 = input.LA(1);

                            if ( (LA36_0==AS) ) {
                                alt36=1;
                            }
                            switch (alt36) {
                                case 1 :
                                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:482:90: AS ( FIRST | LAST )
                                    {
                                    match(input,AS,FOLLOW_AS_in_insertStatement4257); if (failed) return value;
                                    if ( (input.LA(1)>=FIRST && input.LA(1)<=LAST) ) {
                                        input.consume();
                                        errorRecovery=false;failed=false;
                                    }
                                    else {
                                        if (backtracking>0) {failed=true; return value;}
                                        MismatchedSetException mse =
                                            new MismatchedSetException(null,input);
                                        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_insertStatement4261);    throw mse;
                                    }


                                    }
                                    break;

                            }

                            match(input,INTO,FOLLOW_INTO_in_insertStatement4287); if (failed) return value;
                            pushFollow(FOLLOW_expression_in_insertStatement4291);
                            expression();
                            _fsp--;
                            if (failed) return value;

                            }


                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:482:156: AFTER expression
                            {
                            match(input,AFTER,FOLLOW_AFTER_in_insertStatement4303); if (failed) return value;
                            pushFollow(FOLLOW_expression_in_insertStatement4307);
                            expression();
                            _fsp--;
                            if (failed) return value;

                            }
                            break;
                        case 3 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:482:181: BEFORE expression
                            {
                            match(input,BEFORE,FOLLOW_BEFORE_in_insertStatement4315); if (failed) return value;
                            pushFollow(FOLLOW_expression_in_insertStatement4319);
                            expression();
                            _fsp--;
                            if (failed) return value;

                            }
                            break;

                    }


                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_insertStatement4333); if (failed) return value;

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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:483:1: deleteStatement returns [JCStatement value = null] : DELETE expression SEMI ;
    public final JCStatement deleteStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:484:2: ( DELETE expression SEMI )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:484:4: DELETE expression SEMI
            {
            match(input,DELETE,FOLLOW_DELETE_in_deleteStatement4348); if (failed) return value;
            pushFollow(FOLLOW_expression_in_deleteStatement4352);
            expression();
            _fsp--;
            if (failed) return value;
            match(input,SEMI,FOLLOW_SEMI_in_deleteStatement4356); if (failed) return value;

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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:485:1: throwStatement returns [JCStatement value = null] : THROW expression SEMI ;
    public final JCStatement throwStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:486:2: ( THROW expression SEMI )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:486:4: THROW expression SEMI
            {
            match(input,THROW,FOLLOW_THROW_in_throwStatement4371); if (failed) return value;
            pushFollow(FOLLOW_expression_in_throwStatement4375);
            expression();
            _fsp--;
            if (failed) return value;
            match(input,SEMI,FOLLOW_SEMI_in_throwStatement4379); if (failed) return value;

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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:487:1: returnStatement returns [JCStatement value] : RETURN ( expression )? SEMI ;
    public final JCStatement returnStatement() throws RecognitionException {
        JCStatement value = null;

        Token RETURN51=null;
        JCExpression expression50 = null;


         JCExpression expr = null; 
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:489:2: ( RETURN ( expression )? SEMI )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:489:4: RETURN ( expression )? SEMI
            {
            RETURN51=(Token)input.LT(1);
            match(input,RETURN,FOLLOW_RETURN_in_returnStatement4399); if (failed) return value;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:489:11: ( expression )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( ((LA39_0>=POUND && LA39_0<=TYPEOF)||LA39_0==IF||(LA39_0>=THIS && LA39_0<=FALSE)||LA39_0==UNITINTERVAL||LA39_0==FOREACH||(LA39_0>=NOT && LA39_0<=NEW)||(LA39_0>=OPERATION && LA39_0<=FUNCTION)||(LA39_0>=INDEXOF && LA39_0<=SUPER)||(LA39_0>=SIZEOF && LA39_0<=REVERSE)||LA39_0==LPAREN||LA39_0==LBRACKET||LA39_0==DOT||(LA39_0>=PLUSPLUS && LA39_0<=SUBSUB)||(LA39_0>=QUES && LA39_0<=STRING_LITERAL)||LA39_0==QUOTE_LBRACE_STRING_LITERAL||(LA39_0>=QUOTED_IDENTIFIER && LA39_0<=INTEGER_LITERAL)||LA39_0==FLOATING_POINT_LITERAL||LA39_0==IDENTIFIER) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:489:12: expression
                    {
                    pushFollow(FOLLOW_expression_in_returnStatement4402);
                    expression50=expression();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       expr = expression50; 
                    }

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_returnStatement4409); if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(pos(RETURN51)).Return(expr); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:492:1: localTriggerStatement returns [JCStatement value = null] : TRIGGER ON ( localTriggerCondition | LPAREN localTriggerCondition RPAREN ) block ;
    public final JCStatement localTriggerStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:493:2: ( TRIGGER ON ( localTriggerCondition | LPAREN localTriggerCondition RPAREN ) block )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:493:4: TRIGGER ON ( localTriggerCondition | LPAREN localTriggerCondition RPAREN ) block
            {
            match(input,TRIGGER,FOLLOW_TRIGGER_in_localTriggerStatement4435); if (failed) return value;
            match(input,ON,FOLLOW_ON_in_localTriggerStatement4439); if (failed) return value;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:493:20: ( localTriggerCondition | LPAREN localTriggerCondition RPAREN )
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
                    new NoViableAltException("493:20: ( localTriggerCondition | LPAREN localTriggerCondition RPAREN )", 40, 0, input);

                throw nvae;
            }
            switch (alt40) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:493:22: localTriggerCondition
                    {
                    pushFollow(FOLLOW_localTriggerCondition_in_localTriggerStatement4446);
                    localTriggerCondition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:493:46: LPAREN localTriggerCondition RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_localTriggerStatement4450); if (failed) return value;
                    pushFollow(FOLLOW_localTriggerCondition_in_localTriggerStatement4454);
                    localTriggerCondition();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_localTriggerStatement4458); if (failed) return value;

                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_localTriggerStatement4462);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:494:1: localTriggerCondition returns [JCStatement value = null] : ( name ( LBRACKET name RBRACKET )? EQ expression | INSERT name INTO ( name EQ ) expression | DELETE name FROM ( name EQ ) expression );
    public final JCStatement localTriggerCondition() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:495:2: ( name ( LBRACKET name RBRACKET )? EQ expression | INSERT name INTO ( name EQ ) expression | DELETE name FROM ( name EQ ) expression )
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
                    new NoViableAltException("494:1: localTriggerCondition returns [JCStatement value = null] : ( name ( LBRACKET name RBRACKET )? EQ expression | INSERT name INTO ( name EQ ) expression | DELETE name FROM ( name EQ ) expression );", 42, 0, input);

                throw nvae;
            }

            switch (alt42) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:495:4: name ( LBRACKET name RBRACKET )? EQ expression
                    {
                    pushFollow(FOLLOW_name_in_localTriggerCondition4477);
                    name();
                    _fsp--;
                    if (failed) return value;
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:495:11: ( LBRACKET name RBRACKET )?
                    int alt41=2;
                    int LA41_0 = input.LA(1);

                    if ( (LA41_0==LBRACKET) ) {
                        alt41=1;
                    }
                    switch (alt41) {
                        case 1 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:495:15: LBRACKET name RBRACKET
                            {
                            match(input,LBRACKET,FOLLOW_LBRACKET_in_localTriggerCondition4485); if (failed) return value;
                            pushFollow(FOLLOW_name_in_localTriggerCondition4489);
                            name();
                            _fsp--;
                            if (failed) return value;
                            match(input,RBRACKET,FOLLOW_RBRACKET_in_localTriggerCondition4493); if (failed) return value;

                            }
                            break;

                    }

                    match(input,EQ,FOLLOW_EQ_in_localTriggerCondition4503); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_localTriggerCondition4507);
                    expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:496:10: INSERT name INTO ( name EQ ) expression
                    {
                    match(input,INSERT,FOLLOW_INSERT_in_localTriggerCondition4519); if (failed) return value;
                    pushFollow(FOLLOW_name_in_localTriggerCondition4523);
                    name();
                    _fsp--;
                    if (failed) return value;
                    match(input,INTO,FOLLOW_INTO_in_localTriggerCondition4527); if (failed) return value;
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:496:33: ( name EQ )
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:496:35: name EQ
                    {
                    pushFollow(FOLLOW_name_in_localTriggerCondition4533);
                    name();
                    _fsp--;
                    if (failed) return value;
                    match(input,EQ,FOLLOW_EQ_in_localTriggerCondition4537); if (failed) return value;

                    }

                    pushFollow(FOLLOW_expression_in_localTriggerCondition4545);
                    expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:497:10: DELETE name FROM ( name EQ ) expression
                    {
                    match(input,DELETE,FOLLOW_DELETE_in_localTriggerCondition4557); if (failed) return value;
                    pushFollow(FOLLOW_name_in_localTriggerCondition4561);
                    name();
                    _fsp--;
                    if (failed) return value;
                    match(input,FROM,FOLLOW_FROM_in_localTriggerCondition4565); if (failed) return value;
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:497:33: ( name EQ )
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:497:35: name EQ
                    {
                    pushFollow(FOLLOW_name_in_localTriggerCondition4571);
                    name();
                    _fsp--;
                    if (failed) return value;
                    match(input,EQ,FOLLOW_EQ_in_localTriggerCondition4575); if (failed) return value;

                    }

                    pushFollow(FOLLOW_expression_in_localTriggerCondition4583);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:498:1: forAlphaStatement returns [JCStatement value = null] : FOR LPAREN alphaExpression RPAREN block ;
    public final JCStatement forAlphaStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:499:2: ( FOR LPAREN alphaExpression RPAREN block )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:499:4: FOR LPAREN alphaExpression RPAREN block
            {
            match(input,FOR,FOLLOW_FOR_in_forAlphaStatement4598); if (failed) return value;
            match(input,LPAREN,FOLLOW_LPAREN_in_forAlphaStatement4602); if (failed) return value;
            pushFollow(FOLLOW_alphaExpression_in_forAlphaStatement4606);
            alphaExpression();
            _fsp--;
            if (failed) return value;
            match(input,RPAREN,FOLLOW_RPAREN_in_forAlphaStatement4610); if (failed) return value;
            pushFollow(FOLLOW_block_in_forAlphaStatement4614);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:500:1: alphaExpression : UNITINTERVAL IN DUR expression ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )? ;
    public final void alphaExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:500:17: ( UNITINTERVAL IN DUR expression ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:500:19: UNITINTERVAL IN DUR expression ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )?
            {
            match(input,UNITINTERVAL,FOLLOW_UNITINTERVAL_in_alphaExpression4622); if (failed) return ;
            match(input,IN,FOLLOW_IN_in_alphaExpression4626); if (failed) return ;
            match(input,DUR,FOLLOW_DUR_in_alphaExpression4630); if (failed) return ;
            pushFollow(FOLLOW_expression_in_alphaExpression4634);
            expression();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:500:58: ( FPS expression )?
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:500:62: FPS expression
                    {
                    match(input,FPS,FOLLOW_FPS_in_alphaExpression4642); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_alphaExpression4646);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:500:87: ( WHILE expression )?
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:500:91: WHILE expression
                    {
                    match(input,WHILE,FOLLOW_WHILE_in_alphaExpression4660); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_alphaExpression4664);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:500:118: ( CONTINUE IF expression )?
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:500:122: CONTINUE IF expression
                    {
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_alphaExpression4678); if (failed) return ;
                    match(input,IF,FOLLOW_IF_in_alphaExpression4682); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_alphaExpression4686);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:501:1: forJoinStatement returns [JCStatement value = null] : FOR LPAREN joinClause RPAREN ( LPAREN durClause RPAREN )? block ;
    public final JCStatement forJoinStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:502:2: ( FOR LPAREN joinClause RPAREN ( LPAREN durClause RPAREN )? block )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:502:4: FOR LPAREN joinClause RPAREN ( LPAREN durClause RPAREN )? block
            {
            match(input,FOR,FOLLOW_FOR_in_forJoinStatement4707); if (failed) return value;
            match(input,LPAREN,FOLLOW_LPAREN_in_forJoinStatement4711); if (failed) return value;
            pushFollow(FOLLOW_joinClause_in_forJoinStatement4715);
            joinClause();
            _fsp--;
            if (failed) return value;
            match(input,RPAREN,FOLLOW_RPAREN_in_forJoinStatement4719); if (failed) return value;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:502:41: ( LPAREN durClause RPAREN )?
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==LPAREN) ) {
                alt46=1;
            }
            switch (alt46) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:502:45: LPAREN durClause RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_forJoinStatement4727); if (failed) return value;
                    pushFollow(FOLLOW_durClause_in_forJoinStatement4731);
                    durClause();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_forJoinStatement4735); if (failed) return value;

                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_forJoinStatement4745);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:503:1: joinClause : name IN expression ( COMMA name IN expression )* ( WHERE expression )? ;
    public final void joinClause() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:503:12: ( name IN expression ( COMMA name IN expression )* ( WHERE expression )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:503:14: name IN expression ( COMMA name IN expression )* ( WHERE expression )?
            {
            pushFollow(FOLLOW_name_in_joinClause4753);
            name();
            _fsp--;
            if (failed) return ;
            match(input,IN,FOLLOW_IN_in_joinClause4757); if (failed) return ;
            pushFollow(FOLLOW_expression_in_joinClause4761);
            expression();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:503:39: ( COMMA name IN expression )*
            loop47:
            do {
                int alt47=2;
                int LA47_0 = input.LA(1);

                if ( (LA47_0==COMMA) ) {
                    alt47=1;
                }


                switch (alt47) {
            	case 1 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:503:43: COMMA name IN expression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_joinClause4769); if (failed) return ;
            	    pushFollow(FOLLOW_name_in_joinClause4773);
            	    name();
            	    _fsp--;
            	    if (failed) return ;
            	    match(input,IN,FOLLOW_IN_in_joinClause4777); if (failed) return ;
            	    pushFollow(FOLLOW_expression_in_joinClause4781);
            	    expression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop47;
                }
            } while (true);

            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:503:82: ( WHERE expression )?
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==WHERE) ) {
                alt48=1;
            }
            switch (alt48) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:503:86: WHERE expression
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_joinClause4795); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_joinClause4799);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:1: durClause : DUR expression ( LINEAR | EASEIN | EASEOUT | EASEBOTH | MOTION expression )? ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )? ;
    public final void durClause() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:11: ( DUR expression ( LINEAR | EASEIN | EASEOUT | EASEBOTH | MOTION expression )? ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:13: DUR expression ( LINEAR | EASEIN | EASEOUT | EASEBOTH | MOTION expression )? ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )?
            {
            match(input,DUR,FOLLOW_DUR_in_durClause4813); if (failed) return ;
            pushFollow(FOLLOW_expression_in_durClause4817);
            expression();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:32: ( LINEAR | EASEIN | EASEOUT | EASEBOTH | MOTION expression )?
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:36: LINEAR
                    {
                    match(input,LINEAR,FOLLOW_LINEAR_in_durClause4825); if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:49: EASEIN
                    {
                    match(input,EASEIN,FOLLOW_EASEIN_in_durClause4833); if (failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:62: EASEOUT
                    {
                    match(input,EASEOUT,FOLLOW_EASEOUT_in_durClause4841); if (failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:76: EASEBOTH
                    {
                    match(input,EASEBOTH,FOLLOW_EASEBOTH_in_durClause4849); if (failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:91: MOTION expression
                    {
                    match(input,MOTION,FOLLOW_MOTION_in_durClause4857); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_durClause4861);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:119: ( FPS expression )?
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:123: FPS expression
                    {
                    match(input,FPS,FOLLOW_FPS_in_durClause4875); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_durClause4879);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:146: ( WHILE expression )?
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:150: WHILE expression
                    {
                    match(input,WHILE,FOLLOW_WHILE_in_durClause4891); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_durClause4895);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:177: ( CONTINUE IF expression )?
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:181: CONTINUE IF expression
                    {
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_durClause4909); if (failed) return ;
                    match(input,IF,FOLLOW_IF_in_durClause4913); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_durClause4917);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:505:1: tryStatement returns [JCStatement value = null] : TRY block ( FINALLY block | ( catchClause )+ ( FINALLY block )? ) ;
    public final JCStatement tryStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:2: ( TRY block ( FINALLY block | ( catchClause )+ ( FINALLY block )? ) )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:4: TRY block ( FINALLY block | ( catchClause )+ ( FINALLY block )? )
            {
            match(input,TRY,FOLLOW_TRY_in_tryStatement4938); if (failed) return value;
            pushFollow(FOLLOW_block_in_tryStatement4942);
            block();
            _fsp--;
            if (failed) return value;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:18: ( FINALLY block | ( catchClause )+ ( FINALLY block )? )
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
                    new NoViableAltException("506:18: ( FINALLY block | ( catchClause )+ ( FINALLY block )? )", 55, 0, input);

                throw nvae;
            }
            switch (alt55) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:22: FINALLY block
                    {
                    match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement4950); if (failed) return value;
                    pushFollow(FOLLOW_block_in_tryStatement4954);
                    block();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:46: ( catchClause )+ ( FINALLY block )?
                    {
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:46: ( catchClause )+
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
                    	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: catchClause
                    	    {
                    	    pushFollow(FOLLOW_catchClause_in_tryStatement4964);
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

                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:62: ( FINALLY block )?
                    int alt54=2;
                    int LA54_0 = input.LA(1);

                    if ( (LA54_0==FINALLY) ) {
                        alt54=1;
                    }
                    switch (alt54) {
                        case 1 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:506:66: FINALLY block
                            {
                            match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement4974); if (failed) return value;
                            pushFollow(FOLLOW_block_in_tryStatement4978);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:507:1: catchClause : CATCH LPAREN name ( typeReference )? ( IF expression )? RPAREN block ;
    public final void catchClause() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:507:13: ( CATCH LPAREN name ( typeReference )? ( IF expression )? RPAREN block )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:507:15: CATCH LPAREN name ( typeReference )? ( IF expression )? RPAREN block
            {
            match(input,CATCH,FOLLOW_CATCH_in_catchClause4996); if (failed) return ;
            match(input,LPAREN,FOLLOW_LPAREN_in_catchClause5000); if (failed) return ;
            pushFollow(FOLLOW_name_in_catchClause5004);
            name();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:507:39: ( typeReference )?
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: typeReference
                    {
                    pushFollow(FOLLOW_typeReference_in_catchClause5008);
                    typeReference();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:507:57: ( IF expression )?
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( (LA57_0==IF) ) {
                alt57=1;
            }
            switch (alt57) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:507:61: IF expression
                    {
                    match(input,IF,FOLLOW_IF_in_catchClause5018); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_catchClause5022);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_catchClause5032); if (failed) return ;
            pushFollow(FOLLOW_block_in_catchClause5036);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:508:1: expression returns [JCExpression expr] : ( foreach | functionExpression | operationExpression | alphaExpression | ifExpression | selectExpression | LPAREN typeName RPAREN suffixedExpression | suffixedExpression );
    public final JCExpression expression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression suffixedExpression52 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:509:2: ( foreach | functionExpression | operationExpression | alphaExpression | ifExpression | selectExpression | LPAREN typeName RPAREN suffixedExpression | suffixedExpression )
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
                        new NoViableAltException("508:1: expression returns [JCExpression expr] : ( foreach | functionExpression | operationExpression | alphaExpression | ifExpression | selectExpression | LPAREN typeName RPAREN suffixedExpression | suffixedExpression );", 58, 7, input);

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
                    new NoViableAltException("508:1: expression returns [JCExpression expr] : ( foreach | functionExpression | operationExpression | alphaExpression | ifExpression | selectExpression | LPAREN typeName RPAREN suffixedExpression | suffixedExpression );", 58, 0, input);

                throw nvae;
            }

            switch (alt58) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:509:4: foreach
                    {
                    pushFollow(FOLLOW_foreach_in_expression5050);
                    foreach();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:11: functionExpression
                    {
                    pushFollow(FOLLOW_functionExpression_in_expression5063);
                    functionExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:511:11: operationExpression
                    {
                    pushFollow(FOLLOW_operationExpression_in_expression5076);
                    operationExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:512:11: alphaExpression
                    {
                    pushFollow(FOLLOW_alphaExpression_in_expression5089);
                    alphaExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:513:11: ifExpression
                    {
                    pushFollow(FOLLOW_ifExpression_in_expression5102);
                    ifExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:514:11: selectExpression
                    {
                    pushFollow(FOLLOW_selectExpression_in_expression5118);
                    selectExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:515:11: LPAREN typeName RPAREN suffixedExpression
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_expression5131); if (failed) return expr;
                    pushFollow(FOLLOW_typeName_in_expression5137);
                    typeName();
                    _fsp--;
                    if (failed) return expr;
                    match(input,RPAREN,FOLLOW_RPAREN_in_expression5140); if (failed) return expr;
                    pushFollow(FOLLOW_suffixedExpression_in_expression5144);
                    suffixedExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:516:11: suffixedExpression
                    {
                    pushFollow(FOLLOW_suffixedExpression_in_expression5159);
                    suffixedExpression52=suffixedExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = suffixedExpression52; 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:517:1: foreach : FOREACH LPAREN name IN expression ( COMMA name IN expression )* ( WHERE expression )? RPAREN expression ;
    public final void foreach() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:517:9: ( FOREACH LPAREN name IN expression ( COMMA name IN expression )* ( WHERE expression )? RPAREN expression )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:517:11: FOREACH LPAREN name IN expression ( COMMA name IN expression )* ( WHERE expression )? RPAREN expression
            {
            match(input,FOREACH,FOLLOW_FOREACH_in_foreach5171); if (failed) return ;
            match(input,LPAREN,FOLLOW_LPAREN_in_foreach5175); if (failed) return ;
            pushFollow(FOLLOW_name_in_foreach5179);
            name();
            _fsp--;
            if (failed) return ;
            match(input,IN,FOLLOW_IN_in_foreach5183); if (failed) return ;
            pushFollow(FOLLOW_expression_in_foreach5187);
            expression();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:517:55: ( COMMA name IN expression )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( (LA59_0==COMMA) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:517:59: COMMA name IN expression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_foreach5195); if (failed) return ;
            	    pushFollow(FOLLOW_name_in_foreach5199);
            	    name();
            	    _fsp--;
            	    if (failed) return ;
            	    match(input,IN,FOLLOW_IN_in_foreach5203); if (failed) return ;
            	    pushFollow(FOLLOW_expression_in_foreach5207);
            	    expression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop59;
                }
            } while (true);

            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:517:98: ( WHERE expression )?
            int alt60=2;
            int LA60_0 = input.LA(1);

            if ( (LA60_0==WHERE) ) {
                alt60=1;
            }
            switch (alt60) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:517:102: WHERE expression
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_foreach5221); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_foreach5225);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_foreach5235); if (failed) return ;
            pushFollow(FOLLOW_expression_in_foreach5239);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:1: functionExpression : FUNCTION formalParameters ( typeReference )? functionBody ;
    public final void functionExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:20: ( FUNCTION formalParameters ( typeReference )? functionBody )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:22: FUNCTION formalParameters ( typeReference )? functionBody
            {
            match(input,FUNCTION,FOLLOW_FUNCTION_in_functionExpression5247); if (failed) return ;
            pushFollow(FOLLOW_formalParameters_in_functionExpression5251);
            formalParameters();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:52: ( typeReference )?
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: typeReference
                    {
                    pushFollow(FOLLOW_typeReference_in_functionExpression5255);
                    typeReference();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_functionBody_in_functionExpression5261);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:519:1: operationExpression : OPERATION formalParameters ( typeReference )? block ;
    public final void operationExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:519:21: ( OPERATION formalParameters ( typeReference )? block )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:519:23: OPERATION formalParameters ( typeReference )? block
            {
            match(input,OPERATION,FOLLOW_OPERATION_in_operationExpression5269); if (failed) return ;
            pushFollow(FOLLOW_formalParameters_in_operationExpression5273);
            formalParameters();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:519:54: ( typeReference )?
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: typeReference
                    {
                    pushFollow(FOLLOW_typeReference_in_operationExpression5277);
                    typeReference();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_operationExpression5283);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:520:1: ifExpression : IF expression THEN expression ELSE expression ;
    public final void ifExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:520:14: ( IF expression THEN expression ELSE expression )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:520:16: IF expression THEN expression ELSE expression
            {
            match(input,IF,FOLLOW_IF_in_ifExpression5291); if (failed) return ;
            pushFollow(FOLLOW_expression_in_ifExpression5295);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,THEN,FOLLOW_THEN_in_ifExpression5299); if (failed) return ;
            pushFollow(FOLLOW_expression_in_ifExpression5303);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,ELSE,FOLLOW_ELSE_in_ifExpression5307); if (failed) return ;
            pushFollow(FOLLOW_expression_in_ifExpression5311);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:1: selectExpression : SELECT ( DISTINCT )? expression FROM selectionVar ( COMMA selectionVar )* ( WHERE expression )? ;
    public final void selectExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:18: ( SELECT ( DISTINCT )? expression FROM selectionVar ( COMMA selectionVar )* ( WHERE expression )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:20: SELECT ( DISTINCT )? expression FROM selectionVar ( COMMA selectionVar )* ( WHERE expression )?
            {
            match(input,SELECT,FOLLOW_SELECT_in_selectExpression5319); if (failed) return ;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:29: ( DISTINCT )?
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==DISTINCT) ) {
                alt63=1;
            }
            switch (alt63) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: DISTINCT
                    {
                    match(input,DISTINCT,FOLLOW_DISTINCT_in_selectExpression5323); if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_expression_in_selectExpression5331);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,FROM,FOLLOW_FROM_in_selectExpression5335); if (failed) return ;
            pushFollow(FOLLOW_selectionVar_in_selectExpression5339);
            selectionVar();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:79: ( COMMA selectionVar )*
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
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:83: COMMA selectionVar
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_selectExpression5347); if (failed) return ;
            	    pushFollow(FOLLOW_selectionVar_in_selectExpression5351);
            	    selectionVar();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop64;
                }
            } while (true);

            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:112: ( WHERE expression )?
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:116: WHERE expression
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_selectExpression5365); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_selectExpression5369);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:522:1: selectionVar : name ( IN expression )? ;
    public final void selectionVar() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:522:14: ( name ( IN expression )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:522:16: name ( IN expression )?
            {
            pushFollow(FOLLOW_name_in_selectionVar5383);
            name();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:522:23: ( IN expression )?
            int alt66=2;
            int LA66_0 = input.LA(1);

            if ( (LA66_0==IN) ) {
                alt66=1;
            }
            switch (alt66) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:522:27: IN expression
                    {
                    match(input,IN,FOLLOW_IN_in_selectionVar5391); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_selectionVar5395);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:523:1: suffixedExpression returns [JCExpression expr] : e1= assignmentExpression ( indexOn | orderBy | durClause | PLUSPLUS | SUBSUB )? ;
    public final JCExpression suffixedExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:524:2: (e1= assignmentExpression ( indexOn | orderBy | durClause | PLUSPLUS | SUBSUB )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:524:4: e1= assignmentExpression ( indexOn | orderBy | durClause | PLUSPLUS | SUBSUB )?
            {
            pushFollow(FOLLOW_assignmentExpression_in_suffixedExpression5418);
            e1=assignmentExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:5: ( indexOn | orderBy | durClause | PLUSPLUS | SUBSUB )?
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:6: indexOn
                    {
                    pushFollow(FOLLOW_indexOn_in_suffixedExpression5430);
                    indexOn();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:16: orderBy
                    {
                    pushFollow(FOLLOW_orderBy_in_suffixedExpression5434);
                    orderBy();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:26: durClause
                    {
                    pushFollow(FOLLOW_durClause_in_suffixedExpression5438);
                    durClause();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:38: PLUSPLUS
                    {
                    match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_suffixedExpression5442); if (failed) return expr;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:49: SUBSUB
                    {
                    match(input,SUBSUB,FOLLOW_SUBSUB_in_suffixedExpression5446); if (failed) return expr;

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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:526:1: assignmentExpression returns [JCExpression expr] : e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? ;
    public final JCExpression assignmentExpression() throws RecognitionException {
        JCExpression expr = null;

        Token EQ53=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:527:2: (e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:527:4: e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )?
            {
            pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression5468);
            e1=assignmentOpExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:528:5: ( EQ e2= assignmentOpExpression )?
            int alt68=2;
            int LA68_0 = input.LA(1);

            if ( (LA68_0==EQ) ) {
                alt68=1;
            }
            switch (alt68) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:528:9: EQ e2= assignmentOpExpression
                    {
                    EQ53=(Token)input.LT(1);
                    match(input,EQ,FOLLOW_EQ_in_assignmentExpression5483); if (failed) return expr;
                    pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression5489);
                    e2=assignmentOpExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(EQ53)).Assign(expr, e2); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:529:1: assignmentOpExpression returns [JCExpression expr] : e1= andExpression ( assignmentOperator e2= andExpression )? ;
    public final JCExpression assignmentOpExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;

        JCExpression e2 = null;

        int assignmentOperator54 = 0;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:530:2: (e1= andExpression ( assignmentOperator e2= andExpression )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:530:4: e1= andExpression ( assignmentOperator e2= andExpression )?
            {
            pushFollow(FOLLOW_andExpression_in_assignmentOpExpression5516);
            e1=andExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:531:5: ( assignmentOperator e2= andExpression )?
            int alt69=2;
            int LA69_0 = input.LA(1);

            if ( ((LA69_0>=PLUSEQ && LA69_0<=PERCENTEQ)) ) {
                alt69=1;
            }
            switch (alt69) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:531:9: assignmentOperator e2= andExpression
                    {
                    pushFollow(FOLLOW_assignmentOperator_in_assignmentOpExpression5532);
                    assignmentOperator54=assignmentOperator();
                    _fsp--;
                    if (failed) return expr;
                    pushFollow(FOLLOW_andExpression_in_assignmentOpExpression5538);
                    e2=andExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.Assignop(assignmentOperator54,
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:533:1: andExpression returns [JCExpression expr] : e1= orExpression ( AND e2= orExpression )* ;
    public final JCExpression andExpression() throws RecognitionException {
        JCExpression expr = null;

        Token AND55=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:534:2: (e1= orExpression ( AND e2= orExpression )* )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:534:4: e1= orExpression ( AND e2= orExpression )*
            {
            pushFollow(FOLLOW_orExpression_in_andExpression5564);
            e1=orExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:535:5: ( AND e2= orExpression )*
            loop70:
            do {
                int alt70=2;
                int LA70_0 = input.LA(1);

                if ( (LA70_0==AND) ) {
                    alt70=1;
                }


                switch (alt70) {
            	case 1 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:535:9: AND e2= orExpression
            	    {
            	    AND55=(Token)input.LT(1);
            	    match(input,AND,FOLLOW_AND_in_andExpression5580); if (failed) return expr;
            	    pushFollow(FOLLOW_orExpression_in_andExpression5586);
            	    e2=orExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(AND55)).Binary(JCTree.AND, expr, e2); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:536:1: orExpression returns [JCExpression expr] : e1= instanceOfExpression ( OR e2= instanceOfExpression )* ;
    public final JCExpression orExpression() throws RecognitionException {
        JCExpression expr = null;

        Token OR56=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:537:2: (e1= instanceOfExpression ( OR e2= instanceOfExpression )* )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:537:4: e1= instanceOfExpression ( OR e2= instanceOfExpression )*
            {
            pushFollow(FOLLOW_instanceOfExpression_in_orExpression5614);
            e1=instanceOfExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:538:5: ( OR e2= instanceOfExpression )*
            loop71:
            do {
                int alt71=2;
                int LA71_0 = input.LA(1);

                if ( (LA71_0==OR) ) {
                    alt71=1;
                }


                switch (alt71) {
            	case 1 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:538:9: OR e2= instanceOfExpression
            	    {
            	    OR56=(Token)input.LT(1);
            	    match(input,OR,FOLLOW_OR_in_orExpression5629); if (failed) return expr;
            	    pushFollow(FOLLOW_instanceOfExpression_in_orExpression5635);
            	    e2=instanceOfExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(OR56)).Binary(JCTree.OR, expr, e2); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:539:1: instanceOfExpression returns [JCExpression expr] : e1= relationalExpression ( INSTANCEOF identifier )? ;
    public final JCExpression instanceOfExpression() throws RecognitionException {
        JCExpression expr = null;

        Token INSTANCEOF57=null;
        JCExpression e1 = null;

        JCIdent identifier58 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:540:2: (e1= relationalExpression ( INSTANCEOF identifier )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:540:4: e1= relationalExpression ( INSTANCEOF identifier )?
            {
            pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression5663);
            e1=relationalExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:541:5: ( INSTANCEOF identifier )?
            int alt72=2;
            int LA72_0 = input.LA(1);

            if ( (LA72_0==INSTANCEOF) ) {
                alt72=1;
            }
            switch (alt72) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:541:9: INSTANCEOF identifier
                    {
                    INSTANCEOF57=(Token)input.LT(1);
                    match(input,INSTANCEOF,FOLLOW_INSTANCEOF_in_instanceOfExpression5678); if (failed) return expr;
                    pushFollow(FOLLOW_identifier_in_instanceOfExpression5680);
                    identifier58=identifier();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(INSTANCEOF57)).Binary(JCTree.TYPETEST, expr, 
                      	   													 identifier58); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:543:1: relationalExpression returns [JCExpression expr] : e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* ;
    public final JCExpression relationalExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LTGT59=null;
        Token EQEQ60=null;
        Token LTEQ61=null;
        Token GTEQ62=null;
        Token LT63=null;
        Token GT64=null;
        Token IN65=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:544:2: (e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:544:4: e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            {
            pushFollow(FOLLOW_additiveExpression_in_relationalExpression5708);
            e1=additiveExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:545:5: ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
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
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:545:9: LTGT e= additiveExpression
            	    {
            	    LTGT59=(Token)input.LT(1);
            	    match(input,LTGT,FOLLOW_LTGT_in_relationalExpression5724); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5730);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(LTGT59)).Binary(JCTree.NE, expr, e); 
            	    }

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:546:9: EQEQ e= additiveExpression
            	    {
            	    EQEQ60=(Token)input.LT(1);
            	    match(input,EQEQ,FOLLOW_EQEQ_in_relationalExpression5744); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5750);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(EQEQ60)).Binary(JCTree.EQ, expr, e); 
            	    }

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:547:9: LTEQ e= additiveExpression
            	    {
            	    LTEQ61=(Token)input.LT(1);
            	    match(input,LTEQ,FOLLOW_LTEQ_in_relationalExpression5764); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5770);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(LTEQ61)).Binary(JCTree.LE, expr, e); 
            	    }

            	    }
            	    break;
            	case 4 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:548:9: GTEQ e= additiveExpression
            	    {
            	    GTEQ62=(Token)input.LT(1);
            	    match(input,GTEQ,FOLLOW_GTEQ_in_relationalExpression5784); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5790);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(GTEQ62)).Binary(JCTree.GE, expr, e); 
            	    }

            	    }
            	    break;
            	case 5 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:549:9: LT e= additiveExpression
            	    {
            	    LT63=(Token)input.LT(1);
            	    match(input,LT,FOLLOW_LT_in_relationalExpression5804); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5812);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(LT63))  .Binary(JCTree.LT, expr, e); 
            	    }

            	    }
            	    break;
            	case 6 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:550:9: GT e= additiveExpression
            	    {
            	    GT64=(Token)input.LT(1);
            	    match(input,GT,FOLLOW_GT_in_relationalExpression5826); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5834);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(GT64))  .Binary(JCTree.GT, expr, e); 
            	    }

            	    }
            	    break;
            	case 7 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:551:9: IN e= additiveExpression
            	    {
            	    IN65=(Token)input.LT(1);
            	    match(input,IN,FOLLOW_IN_in_relationalExpression5848); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5856);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       /* expr = F.at(pos(IN65  )).Binary(JavaFXTag.IN, expr, $e2.expr); */ 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:553:1: additiveExpression returns [JCExpression expr] : e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* ;
    public final JCExpression additiveExpression() throws RecognitionException {
        JCExpression expr = null;

        Token PLUS66=null;
        Token SUB67=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:554:2: (e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:554:4: e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            {
            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression5885);
            e1=multiplicativeExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:555:5: ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
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
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:555:9: PLUS e= multiplicativeExpression
            	    {
            	    PLUS66=(Token)input.LT(1);
            	    match(input,PLUS,FOLLOW_PLUS_in_additiveExpression5900); if (failed) return expr;
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression5906);
            	    e=multiplicativeExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(PLUS66)).Binary(JCTree.PLUS , expr, e); 
            	    }

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:556:9: SUB e= multiplicativeExpression
            	    {
            	    SUB67=(Token)input.LT(1);
            	    match(input,SUB,FOLLOW_SUB_in_additiveExpression5919); if (failed) return expr;
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression5926);
            	    e=multiplicativeExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(SUB67)) .Binary(JCTree.MINUS, expr, e); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:558:1: multiplicativeExpression returns [JCExpression expr] : e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* ;
    public final JCExpression multiplicativeExpression() throws RecognitionException {
        JCExpression expr = null;

        Token STAR68=null;
        Token SLASH69=null;
        Token PERCENT70=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:559:2: (e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:559:4: e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            {
            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression5954);
            e1=unaryExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:560:5: ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
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
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:560:9: STAR e= unaryExpression
            	    {
            	    STAR68=(Token)input.LT(1);
            	    match(input,STAR,FOLLOW_STAR_in_multiplicativeExpression5970); if (failed) return expr;
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression5977);
            	    e=unaryExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(STAR68))   .Binary(JCTree.MUL  , expr, e); 
            	    }

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:561:9: SLASH e= unaryExpression
            	    {
            	    SLASH69=(Token)input.LT(1);
            	    match(input,SLASH,FOLLOW_SLASH_in_multiplicativeExpression5991); if (failed) return expr;
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression5997);
            	    e=unaryExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(SLASH69))  .Binary(JCTree.DIV  , expr, e); 
            	    }

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:562:9: PERCENT e= unaryExpression
            	    {
            	    PERCENT70=(Token)input.LT(1);
            	    match(input,PERCENT,FOLLOW_PERCENT_in_multiplicativeExpression6011); if (failed) return expr;
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression6015);
            	    e=unaryExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(PERCENT70)).Binary(JCTree.MOD  , expr, e); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:564:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );
    public final JCExpression unaryExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression postfixExpression71 = null;

        int unaryOperator72 = 0;

        JCExpression postfixExpression73 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:565:2: ( postfixExpression | unaryOperator postfixExpression )
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
                    new NoViableAltException("564:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );", 76, 0, input);

                throw nvae;
            }
            switch (alt76) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:565:4: postfixExpression
                    {
                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression6045);
                    postfixExpression71=postfixExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = postfixExpression71; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:566:4: unaryOperator postfixExpression
                    {
                    pushFollow(FOLLOW_unaryOperator_in_unaryExpression6056);
                    unaryOperator72=unaryOperator();
                    _fsp--;
                    if (failed) return expr;
                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression6060);
                    postfixExpression73=postfixExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.Unary(unaryOperator72, postfixExpression73); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:568:1: postfixExpression returns [JCExpression expr] : primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* ;
    public final JCExpression postfixExpression() throws RecognitionException {
        JCExpression expr = null;

        Token DOT75=null;
        Token LPAREN76=null;
        name_return name1 = null;

        JCExpression primaryExpression74 = null;

        ListBuffer<JCExpression> expressionListOpt77 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:569:2: ( primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:569:4: primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            {
            pushFollow(FOLLOW_primaryExpression_in_postfixExpression6080);
            primaryExpression74=primaryExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = primaryExpression74; 
            }
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:570:5: ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
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
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:570:7: DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    {
            	    DOT75=(Token)input.LT(1);
            	    match(input,DOT,FOLLOW_DOT_in_postfixExpression6095); if (failed) return expr;
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:570:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
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
            	            new NoViableAltException("570:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )", 78, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt78) {
            	        case 1 :
            	            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:570:13: CLASS
            	            {
            	            match(input,CLASS,FOLLOW_CLASS_in_postfixExpression6099); if (failed) return expr;

            	            }
            	            break;
            	        case 2 :
            	            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:571:13: name1= name ( LPAREN expressionListOpt RPAREN )*
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression6123);
            	            name1=name();
            	            _fsp--;
            	            if (failed) return expr;
            	            if ( backtracking==0 ) {
            	               expr = F.at(pos(DOT75)).Select(expr, name1.value); 
            	            }
            	            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:572:14: ( LPAREN expressionListOpt RPAREN )*
            	            loop77:
            	            do {
            	                int alt77=2;
            	                int LA77_0 = input.LA(1);

            	                if ( (LA77_0==LPAREN) ) {
            	                    alt77=1;
            	                }


            	                switch (alt77) {
            	            	case 1 :
            	            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:572:16: LPAREN expressionListOpt RPAREN
            	            	    {
            	            	    LPAREN76=(Token)input.LT(1);
            	            	    match(input,LPAREN,FOLLOW_LPAREN_in_postfixExpression6148); if (failed) return expr;
            	            	    pushFollow(FOLLOW_expressionListOpt_in_postfixExpression6150);
            	            	    expressionListOpt77=expressionListOpt();
            	            	    _fsp--;
            	            	    if (failed) return expr;
            	            	    match(input,RPAREN,FOLLOW_RPAREN_in_postfixExpression6152); if (failed) return expr;
            	            	    if ( backtracking==0 ) {
            	            	       expr = F.at(pos(LPAREN76)).Apply(null, expr, expressionListOpt77.toList()); 
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
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:574:7: LBRACKET ( name BAR )? expression RBRACKET
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_postfixExpression6184); if (failed) return expr;
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:574:16: ( name BAR )?
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
            	            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:574:17: name BAR
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression6187);
            	            name();
            	            _fsp--;
            	            if (failed) return expr;
            	            match(input,BAR,FOLLOW_BAR_in_postfixExpression6189); if (failed) return expr;

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_expression_in_postfixExpression6193);
            	    expression();
            	    _fsp--;
            	    if (failed) return expr;
            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_postfixExpression6196); if (failed) return expr;

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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:576:1: primaryExpression returns [JCExpression expr] : ( newExpression | typeName LBRACE objectLiteral RBRACE | bracketExpression | ordinalExpression | contextExpression | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | LPAREN expression RPAREN );
    public final JCExpression primaryExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LBRACE79=null;
        Token THIS82=null;
        Token SUPER83=null;
        Token LPAREN85=null;
        Token LPAREN89=null;
        JCExpression newExpression78 = null;

        JCExpression typeName80 = null;

        ListBuffer<JCStatement> objectLiteral81 = null;

        JCIdent identifier84 = null;

        ListBuffer<JCExpression> expressionListOpt86 = null;

        JCExpression stringExpression87 = null;

        JCExpression literal88 = null;

        JCExpression expression90 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:577:2: ( newExpression | typeName LBRACE objectLiteral RBRACE | bracketExpression | ordinalExpression | contextExpression | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | LPAREN expression RPAREN )
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
                        new NoViableAltException("576:1: primaryExpression returns [JCExpression expr] : ( newExpression | typeName LBRACE objectLiteral RBRACE | bracketExpression | ordinalExpression | contextExpression | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | LPAREN expression RPAREN );", 82, 2, input);

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
                    new NoViableAltException("576:1: primaryExpression returns [JCExpression expr] : ( newExpression | typeName LBRACE objectLiteral RBRACE | bracketExpression | ordinalExpression | contextExpression | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | LPAREN expression RPAREN );", 82, 0, input);

                throw nvae;
            }

            switch (alt82) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:577:4: newExpression
                    {
                    pushFollow(FOLLOW_newExpression_in_primaryExpression6221);
                    newExpression78=newExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = newExpression78; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:578:4: typeName LBRACE objectLiteral RBRACE
                    {
                    pushFollow(FOLLOW_typeName_in_primaryExpression6233);
                    typeName80=typeName();
                    _fsp--;
                    if (failed) return expr;
                    LBRACE79=(Token)input.LT(1);
                    match(input,LBRACE,FOLLOW_LBRACE_in_primaryExpression6235); if (failed) return expr;
                    pushFollow(FOLLOW_objectLiteral_in_primaryExpression6238);
                    objectLiteral81=objectLiteral();
                    _fsp--;
                    if (failed) return expr;
                    match(input,RBRACE,FOLLOW_RBRACE_in_primaryExpression6240); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(LBRACE79)).PureObjectLiteral(typeName80, objectLiteral81.toList()); 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:579:4: bracketExpression
                    {
                    pushFollow(FOLLOW_bracketExpression_in_primaryExpression6250);
                    bracketExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:581:4: ordinalExpression
                    {
                    pushFollow(FOLLOW_ordinalExpression_in_primaryExpression6265);
                    ordinalExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:582:10: contextExpression
                    {
                    pushFollow(FOLLOW_contextExpression_in_primaryExpression6277);
                    contextExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:583:10: THIS
                    {
                    THIS82=(Token)input.LT(1);
                    match(input,THIS,FOLLOW_THIS_in_primaryExpression6289); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(THIS82)).Identifier(names._this); 
                    }

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:584:10: SUPER
                    {
                    SUPER83=(Token)input.LT(1);
                    match(input,SUPER,FOLLOW_SUPER_in_primaryExpression6308); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(SUPER83)).Identifier(names._super); 
                    }

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:585:10: identifier ( LPAREN expressionListOpt RPAREN )*
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression6327);
                    identifier84=identifier();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = identifier84; 
                    }
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:586:10: ( LPAREN expressionListOpt RPAREN )*
                    loop81:
                    do {
                        int alt81=2;
                        int LA81_0 = input.LA(1);

                        if ( (LA81_0==LPAREN) ) {
                            alt81=1;
                        }


                        switch (alt81) {
                    	case 1 :
                    	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:586:12: LPAREN expressionListOpt RPAREN
                    	    {
                    	    LPAREN85=(Token)input.LT(1);
                    	    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression6348); if (failed) return expr;
                    	    pushFollow(FOLLOW_expressionListOpt_in_primaryExpression6352);
                    	    expressionListOpt86=expressionListOpt();
                    	    _fsp--;
                    	    if (failed) return expr;
                    	    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression6356); if (failed) return expr;
                    	    if ( backtracking==0 ) {
                    	       expr = F.at(pos(LPAREN85)).Apply(null, expr, expressionListOpt86.toList()); 
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
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:587:10: stringExpression
                    {
                    pushFollow(FOLLOW_stringExpression_in_primaryExpression6375);
                    stringExpression87=stringExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = stringExpression87; 
                    }

                    }
                    break;
                case 10 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:588:10: literal
                    {
                    pushFollow(FOLLOW_literal_in_primaryExpression6393);
                    literal88=literal();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = literal88; 
                    }

                    }
                    break;
                case 11 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:589:10: LPAREN expression RPAREN
                    {
                    LPAREN89=(Token)input.LT(1);
                    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression6412); if (failed) return expr;
                    pushFollow(FOLLOW_expression_in_primaryExpression6414);
                    expression90=expression();
                    _fsp--;
                    if (failed) return expr;
                    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression6416); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(LPAREN89)).Parens(expression90); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:591:1: newExpression returns [JCExpression expr] : NEW typeName ( LPAREN expressionListOpt RPAREN )? ;
    public final JCExpression newExpression() throws RecognitionException {
        JCExpression expr = null;

        Token NEW92=null;
        ListBuffer<JCExpression> expressionListOpt91 = null;

        JCExpression typeName93 = null;


         ListBuffer<JCExpression> args = null; 
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:593:2: ( NEW typeName ( LPAREN expressionListOpt RPAREN )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:593:4: NEW typeName ( LPAREN expressionListOpt RPAREN )?
            {
            NEW92=(Token)input.LT(1);
            match(input,NEW,FOLLOW_NEW_in_newExpression6448); if (failed) return expr;
            pushFollow(FOLLOW_typeName_in_newExpression6451);
            typeName93=typeName();
            _fsp--;
            if (failed) return expr;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:594:3: ( LPAREN expressionListOpt RPAREN )?
            int alt83=2;
            int LA83_0 = input.LA(1);

            if ( (LA83_0==LPAREN) ) {
                alt83=1;
            }
            switch (alt83) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:594:5: LPAREN expressionListOpt RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_newExpression6459); if (failed) return expr;
                    pushFollow(FOLLOW_expressionListOpt_in_newExpression6463);
                    expressionListOpt91=expressionListOpt();
                    _fsp--;
                    if (failed) return expr;
                    match(input,RPAREN,FOLLOW_RPAREN_in_newExpression6467); if (failed) return expr;
                    if ( backtracking==0 ) {
                       args = expressionListOpt91; 
                    }

                    }
                    break;

            }

            if ( backtracking==0 ) {
               expr = F.at(pos(NEW92)).NewClass(null, null, typeName93, 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:599:1: objectLiteral returns [ListBuffer<JCStatement> parts = new ListBuffer<JCStatement>()] : ( objectLiteralPart )* ;
    public final ListBuffer<JCStatement> objectLiteral() throws RecognitionException {
        ListBuffer<JCStatement> parts =  new ListBuffer<JCStatement>();

        JCStatement objectLiteralPart94 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:600:2: ( ( objectLiteralPart )* )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:600:4: ( objectLiteralPart )*
            {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:600:4: ( objectLiteralPart )*
            loop84:
            do {
                int alt84=2;
                int LA84_0 = input.LA(1);

                if ( (LA84_0==ATTRIBUTE||LA84_0==VAR||LA84_0==TRIGGER||(LA84_0>=OPERATION && LA84_0<=FUNCTION)||LA84_0==QUOTED_IDENTIFIER||LA84_0==IDENTIFIER) ) {
                    alt84=1;
                }


                switch (alt84) {
            	case 1 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:600:6: objectLiteralPart
            	    {
            	    pushFollow(FOLLOW_objectLiteralPart_in_objectLiteral6507);
            	    objectLiteralPart94=objectLiteralPart();
            	    _fsp--;
            	    if (failed) return parts;
            	    if ( backtracking==0 ) {
            	       parts.append(objectLiteralPart94); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:601:1: objectLiteralPart returns [JCStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | ATTRIBUTE name typeReference EQ bindOpt expression SEMI | localOperationDefinition | localFunctionDefinition | localTriggerStatement | variableDefinition );
    public final JCStatement objectLiteralPart() throws RecognitionException {
        JCStatement value = null;

        Token COLON95=null;
        name_return name96 = null;

        JCExpression expression97 = null;

        JavafxBindStatus bindOpt98 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:602:2: ( name COLON bindOpt expression ( COMMA | SEMI )? | ATTRIBUTE name typeReference EQ bindOpt expression SEMI | localOperationDefinition | localFunctionDefinition | localTriggerStatement | variableDefinition )
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
                        new NoViableAltException("601:1: objectLiteralPart returns [JCStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | ATTRIBUTE name typeReference EQ bindOpt expression SEMI | localOperationDefinition | localFunctionDefinition | localTriggerStatement | variableDefinition );", 86, 1, input);

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
                    new NoViableAltException("601:1: objectLiteralPart returns [JCStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | ATTRIBUTE name typeReference EQ bindOpt expression SEMI | localOperationDefinition | localFunctionDefinition | localTriggerStatement | variableDefinition );", 86, 0, input);

                throw nvae;
            }

            switch (alt86) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:602:4: name COLON bindOpt expression ( COMMA | SEMI )?
                    {
                    pushFollow(FOLLOW_name_in_objectLiteralPart6533);
                    name96=name();
                    _fsp--;
                    if (failed) return value;
                    COLON95=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_objectLiteralPart6535); if (failed) return value;
                    pushFollow(FOLLOW_bindOpt_in_objectLiteralPart6538);
                    bindOpt98=bindOpt();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_expression_in_objectLiteralPart6540);
                    expression97=expression();
                    _fsp--;
                    if (failed) return value;
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:602:35: ( COMMA | SEMI )?
                    int alt85=2;
                    int LA85_0 = input.LA(1);

                    if ( ((LA85_0>=SEMI && LA85_0<=COMMA)) ) {
                        alt85=1;
                    }
                    switch (alt85) {
                        case 1 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
                            {
                            if ( (input.LA(1)>=SEMI && input.LA(1)<=COMMA) ) {
                                input.consume();
                                errorRecovery=false;failed=false;
                            }
                            else {
                                if (backtracking>0) {failed=true; return value;}
                                MismatchedSetException mse =
                                    new MismatchedSetException(null,input);
                                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_objectLiteralPart6542);    throw mse;
                            }


                            }
                            break;

                    }

                    if ( backtracking==0 ) {
                       value = F.at(pos(COLON95)).ObjectLiteralPart(name96.value, expression97, bindOpt98); 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:603:10: ATTRIBUTE name typeReference EQ bindOpt expression SEMI
                    {
                    match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_objectLiteralPart6562); if (failed) return value;
                    pushFollow(FOLLOW_name_in_objectLiteralPart6566);
                    name();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_typeReference_in_objectLiteralPart6570);
                    typeReference();
                    _fsp--;
                    if (failed) return value;
                    match(input,EQ,FOLLOW_EQ_in_objectLiteralPart6574); if (failed) return value;
                    pushFollow(FOLLOW_bindOpt_in_objectLiteralPart6577);
                    bindOpt();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_expression_in_objectLiteralPart6579);
                    expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_objectLiteralPart6583); if (failed) return value;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:604:10: localOperationDefinition
                    {
                    pushFollow(FOLLOW_localOperationDefinition_in_objectLiteralPart6595);
                    localOperationDefinition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:605:10: localFunctionDefinition
                    {
                    pushFollow(FOLLOW_localFunctionDefinition_in_objectLiteralPart6607);
                    localFunctionDefinition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:606:10: localTriggerStatement
                    {
                    pushFollow(FOLLOW_localTriggerStatement_in_objectLiteralPart6619);
                    localTriggerStatement();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:607:10: variableDefinition
                    {
                    pushFollow(FOLLOW_variableDefinition_in_objectLiteralPart6631);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:608:1: bracketExpression : LBRACKET ( | expression ( | COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* ) | BAR generator ( COMMA ( generator | expression ) )* | DOTDOT ( LT )? expression ) ) RBRACKET ;
    public final void bracketExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:609:2: ( LBRACKET ( | expression ( | COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* ) | BAR generator ( COMMA ( generator | expression ) )* | DOTDOT ( LT )? expression ) ) RBRACKET )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:609:4: LBRACKET ( | expression ( | COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* ) | BAR generator ( COMMA ( generator | expression ) )* | DOTDOT ( LT )? expression ) ) RBRACKET
            {
            match(input,LBRACKET,FOLLOW_LBRACKET_in_bracketExpression6641); if (failed) return ;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:610:3: ( | expression ( | COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* ) | BAR generator ( COMMA ( generator | expression ) )* | DOTDOT ( LT )? expression ) )
            int alt94=2;
            int LA94_0 = input.LA(1);

            if ( (LA94_0==RBRACKET) ) {
                alt94=1;
            }
            else if ( ((LA94_0>=POUND && LA94_0<=TYPEOF)||LA94_0==IF||(LA94_0>=THIS && LA94_0<=FALSE)||LA94_0==UNITINTERVAL||LA94_0==FOREACH||(LA94_0>=NOT && LA94_0<=NEW)||(LA94_0>=OPERATION && LA94_0<=FUNCTION)||(LA94_0>=INDEXOF && LA94_0<=SUPER)||(LA94_0>=SIZEOF && LA94_0<=REVERSE)||LA94_0==LPAREN||LA94_0==LBRACKET||LA94_0==DOT||(LA94_0>=PLUSPLUS && LA94_0<=SUBSUB)||(LA94_0>=QUES && LA94_0<=STRING_LITERAL)||LA94_0==QUOTE_LBRACE_STRING_LITERAL||(LA94_0>=QUOTED_IDENTIFIER && LA94_0<=INTEGER_LITERAL)||LA94_0==FLOATING_POINT_LITERAL||LA94_0==IDENTIFIER) ) {
                alt94=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("610:3: ( | expression ( | COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* ) | BAR generator ( COMMA ( generator | expression ) )* | DOTDOT ( LT )? expression ) )", 94, 0, input);

                throw nvae;
            }
            switch (alt94) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:611:3: 
                    {
                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:611:5: expression ( | COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* ) | BAR generator ( COMMA ( generator | expression ) )* | DOTDOT ( LT )? expression )
                    {
                    pushFollow(FOLLOW_expression_in_bracketExpression6656);
                    expression();
                    _fsp--;
                    if (failed) return ;
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:612:9: ( | COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* ) | BAR generator ( COMMA ( generator | expression ) )* | DOTDOT ( LT )? expression )
                    int alt93=4;
                    switch ( input.LA(1) ) {
                    case RBRACKET:
                        {
                        alt93=1;
                        }
                        break;
                    case COMMA:
                        {
                        alt93=2;
                        }
                        break;
                    case BAR:
                        {
                        alt93=3;
                        }
                        break;
                    case DOTDOT:
                        {
                        alt93=4;
                        }
                        break;
                    default:
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("612:9: ( | COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* ) | BAR generator ( COMMA ( generator | expression ) )* | DOTDOT ( LT )? expression )", 93, 0, input);

                        throw nvae;
                    }

                    switch (alt93) {
                        case 1 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:613:9: 
                            {
                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:613:11: COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* )
                            {
                            match(input,COMMA,FOLLOW_COMMA_in_bracketExpression6683); if (failed) return ;
                            pushFollow(FOLLOW_expression_in_bracketExpression6685);
                            expression();
                            _fsp--;
                            if (failed) return ;
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:614:10: ( DOTDOT ( LT )? expression | ( COMMA expression )* )
                            int alt89=2;
                            int LA89_0 = input.LA(1);

                            if ( (LA89_0==DOTDOT) ) {
                                alt89=1;
                            }
                            else if ( (LA89_0==RBRACKET||LA89_0==COMMA) ) {
                                alt89=2;
                            }
                            else {
                                if (backtracking>0) {failed=true; return ;}
                                NoViableAltException nvae =
                                    new NoViableAltException("614:10: ( DOTDOT ( LT )? expression | ( COMMA expression )* )", 89, 0, input);

                                throw nvae;
                            }
                            switch (alt89) {
                                case 1 :
                                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:614:12: DOTDOT ( LT )? expression
                                    {
                                    match(input,DOTDOT,FOLLOW_DOTDOT_in_bracketExpression6699); if (failed) return ;
                                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:614:21: ( LT )?
                                    int alt87=2;
                                    int LA87_0 = input.LA(1);

                                    if ( (LA87_0==LT) ) {
                                        alt87=1;
                                    }
                                    switch (alt87) {
                                        case 1 :
                                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: LT
                                            {
                                            match(input,LT,FOLLOW_LT_in_bracketExpression6703); if (failed) return ;

                                            }
                                            break;

                                    }

                                    pushFollow(FOLLOW_expression_in_bracketExpression6706);
                                    expression();
                                    _fsp--;
                                    if (failed) return ;

                                    }
                                    break;
                                case 2 :
                                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:615:12: ( COMMA expression )*
                                    {
                                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:615:12: ( COMMA expression )*
                                    loop88:
                                    do {
                                        int alt88=2;
                                        int LA88_0 = input.LA(1);

                                        if ( (LA88_0==COMMA) ) {
                                            alt88=1;
                                        }


                                        switch (alt88) {
                                    	case 1 :
                                    	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:615:13: COMMA expression
                                    	    {
                                    	    match(input,COMMA,FOLLOW_COMMA_in_bracketExpression6720); if (failed) return ;
                                    	    pushFollow(FOLLOW_expression_in_bracketExpression6724);
                                    	    expression();
                                    	    _fsp--;
                                    	    if (failed) return ;

                                    	    }
                                    	    break;

                                    	default :
                                    	    break loop88;
                                        }
                                    } while (true);


                                    }
                                    break;

                            }


                            }
                            break;
                        case 3 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:617:11: BAR generator ( COMMA ( generator | expression ) )*
                            {
                            match(input,BAR,FOLLOW_BAR_in_bracketExpression6751); if (failed) return ;
                            pushFollow(FOLLOW_generator_in_bracketExpression6755);
                            generator();
                            _fsp--;
                            if (failed) return ;
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:617:29: ( COMMA ( generator | expression ) )*
                            loop91:
                            do {
                                int alt91=2;
                                int LA91_0 = input.LA(1);

                                if ( (LA91_0==COMMA) ) {
                                    alt91=1;
                                }


                                switch (alt91) {
                            	case 1 :
                            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:617:30: COMMA ( generator | expression )
                            	    {
                            	    match(input,COMMA,FOLLOW_COMMA_in_bracketExpression6760); if (failed) return ;
                            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:617:38: ( generator | expression )
                            	    int alt90=2;
                            	    int LA90_0 = input.LA(1);

                            	    if ( (LA90_0==QUOTED_IDENTIFIER||LA90_0==IDENTIFIER) ) {
                            	        int LA90_1 = input.LA(2);

                            	        if ( (LA90_1==LARROW) ) {
                            	            alt90=1;
                            	        }
                            	        else if ( (LA90_1==AND||LA90_1==DUR||LA90_1==IN||(LA90_1>=ORDER && LA90_1<=INSTANCEOF)||LA90_1==OR||LA90_1==LPAREN||(LA90_1>=LBRACKET && LA90_1<=RBRACKET)||(LA90_1>=COMMA && LA90_1<=PERCENTEQ)||LA90_1==LBRACE) ) {
                            	            alt90=2;
                            	        }
                            	        else {
                            	            if (backtracking>0) {failed=true; return ;}
                            	            NoViableAltException nvae =
                            	                new NoViableAltException("617:38: ( generator | expression )", 90, 1, input);

                            	            throw nvae;
                            	        }
                            	    }
                            	    else if ( ((LA90_0>=POUND && LA90_0<=TYPEOF)||LA90_0==IF||(LA90_0>=THIS && LA90_0<=FALSE)||LA90_0==UNITINTERVAL||LA90_0==FOREACH||(LA90_0>=NOT && LA90_0<=NEW)||(LA90_0>=OPERATION && LA90_0<=FUNCTION)||(LA90_0>=INDEXOF && LA90_0<=SUPER)||(LA90_0>=SIZEOF && LA90_0<=REVERSE)||LA90_0==LPAREN||LA90_0==LBRACKET||LA90_0==DOT||(LA90_0>=PLUSPLUS && LA90_0<=SUBSUB)||(LA90_0>=QUES && LA90_0<=STRING_LITERAL)||LA90_0==QUOTE_LBRACE_STRING_LITERAL||LA90_0==INTEGER_LITERAL||LA90_0==FLOATING_POINT_LITERAL) ) {
                            	        alt90=2;
                            	    }
                            	    else {
                            	        if (backtracking>0) {failed=true; return ;}
                            	        NoViableAltException nvae =
                            	            new NoViableAltException("617:38: ( generator | expression )", 90, 0, input);

                            	        throw nvae;
                            	    }
                            	    switch (alt90) {
                            	        case 1 :
                            	            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:617:39: generator
                            	            {
                            	            pushFollow(FOLLOW_generator_in_bracketExpression6765);
                            	            generator();
                            	            _fsp--;
                            	            if (failed) return ;

                            	            }
                            	            break;
                            	        case 2 :
                            	            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:617:51: expression
                            	            {
                            	            pushFollow(FOLLOW_expression_in_bracketExpression6769);
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
                            break;
                        case 4 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:618:11: DOTDOT ( LT )? expression
                            {
                            match(input,DOTDOT,FOLLOW_DOTDOT_in_bracketExpression6788); if (failed) return ;
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:618:20: ( LT )?
                            int alt92=2;
                            int LA92_0 = input.LA(1);

                            if ( (LA92_0==LT) ) {
                                alt92=1;
                            }
                            switch (alt92) {
                                case 1 :
                                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: LT
                                    {
                                    match(input,LT,FOLLOW_LT_in_bracketExpression6792); if (failed) return ;

                                    }
                                    break;

                            }

                            pushFollow(FOLLOW_expression_in_bracketExpression6796);
                            expression();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }

            match(input,RBRACKET,FOLLOW_RBRACKET_in_bracketExpression6818); if (failed) return ;

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


    // $ANTLR start generator
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:623:1: generator : name LARROW expression ;
    public final void generator() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:623:11: ( name LARROW expression )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:623:13: name LARROW expression
            {
            pushFollow(FOLLOW_name_in_generator6828);
            name();
            _fsp--;
            if (failed) return ;
            match(input,LARROW,FOLLOW_LARROW_in_generator6832); if (failed) return ;
            pushFollow(FOLLOW_expression_in_generator6836);
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:624:1: ordinalExpression : INDEXOF ( name | DOT ) ;
    public final void ordinalExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:624:19: ( INDEXOF ( name | DOT ) )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:624:21: INDEXOF ( name | DOT )
            {
            match(input,INDEXOF,FOLLOW_INDEXOF_in_ordinalExpression6844); if (failed) return ;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:624:31: ( name | DOT )
            int alt95=2;
            int LA95_0 = input.LA(1);

            if ( (LA95_0==QUOTED_IDENTIFIER||LA95_0==IDENTIFIER) ) {
                alt95=1;
            }
            else if ( (LA95_0==DOT) ) {
                alt95=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("624:31: ( name | DOT )", 95, 0, input);

                throw nvae;
            }
            switch (alt95) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:624:35: name
                    {
                    pushFollow(FOLLOW_name_in_ordinalExpression6852);
                    name();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:624:46: DOT
                    {
                    match(input,DOT,FOLLOW_DOT_in_ordinalExpression6860); if (failed) return ;

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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:625:1: contextExpression : DOT ;
    public final void contextExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:625:19: ( DOT )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:625:21: DOT
            {
            match(input,DOT,FOLLOW_DOT_in_contextExpression6872); if (failed) return ;

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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:626:1: stringExpression returns [JCExpression expr] : ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL ;
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
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:628:2: (ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:628:4: ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL
            {
            ql=(Token)input.LT(1);
            match(input,QUOTE_LBRACE_STRING_LITERAL,FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression6894); if (failed) return expr;
            if ( backtracking==0 ) {
               strexp.append(F.at(pos(ql)).Literal(TypeTags.CLASS, ql.getText())); 
            }
            pushFollow(FOLLOW_formatOrNull_in_stringExpression6903);
            f1=formatOrNull();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               strexp.append(f1); 
            }
            pushFollow(FOLLOW_expression_in_stringExpression6914);
            e1=expression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               strexp.append(e1); 
            }
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:631:4: (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )*
            loop96:
            do {
                int alt96=2;
                int LA96_0 = input.LA(1);

                if ( (LA96_0==RBRACE_LBRACE_STRING_LITERAL) ) {
                    alt96=1;
                }


                switch (alt96) {
            	case 1 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:631:7: rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression
            	    {
            	    rl=(Token)input.LT(1);
            	    match(input,RBRACE_LBRACE_STRING_LITERAL,FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression6929); if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       strexp.append(F.at(pos(rl)).Literal(TypeTags.CLASS, rl.getText())); 
            	    }
            	    pushFollow(FOLLOW_formatOrNull_in_stringExpression6941);
            	    fn=formatOrNull();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       strexp.append(fn); 
            	    }
            	    pushFollow(FOLLOW_expression_in_stringExpression6955);
            	    en=expression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       strexp.append(en); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop96;
                }
            } while (true);

            rq=(Token)input.LT(1);
            match(input,RBRACE_QUOTE_STRING_LITERAL,FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression6976); if (failed) return expr;
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:638:1: formatOrNull returns [JCExpression expr] : (fs= FORMAT_STRING_LITERAL | );
    public final JCExpression formatOrNull() throws RecognitionException {
        JCExpression expr = null;

        Token fs=null;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:639:2: (fs= FORMAT_STRING_LITERAL | )
            int alt97=2;
            int LA97_0 = input.LA(1);

            if ( (LA97_0==FORMAT_STRING_LITERAL) ) {
                alt97=1;
            }
            else if ( ((LA97_0>=POUND && LA97_0<=TYPEOF)||LA97_0==IF||(LA97_0>=THIS && LA97_0<=FALSE)||LA97_0==UNITINTERVAL||LA97_0==FOREACH||(LA97_0>=NOT && LA97_0<=NEW)||(LA97_0>=OPERATION && LA97_0<=FUNCTION)||(LA97_0>=INDEXOF && LA97_0<=SUPER)||(LA97_0>=SIZEOF && LA97_0<=REVERSE)||LA97_0==LPAREN||LA97_0==LBRACKET||LA97_0==DOT||(LA97_0>=PLUSPLUS && LA97_0<=SUBSUB)||(LA97_0>=QUES && LA97_0<=STRING_LITERAL)||LA97_0==QUOTE_LBRACE_STRING_LITERAL||(LA97_0>=QUOTED_IDENTIFIER && LA97_0<=INTEGER_LITERAL)||LA97_0==FLOATING_POINT_LITERAL||LA97_0==IDENTIFIER) ) {
                alt97=2;
            }
            else {
                if (backtracking>0) {failed=true; return expr;}
                NoViableAltException nvae =
                    new NoViableAltException("638:1: formatOrNull returns [JCExpression expr] : (fs= FORMAT_STRING_LITERAL | );", 97, 0, input);

                throw nvae;
            }
            switch (alt97) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:639:4: fs= FORMAT_STRING_LITERAL
                    {
                    fs=(Token)input.LT(1);
                    match(input,FORMAT_STRING_LITERAL,FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull7006); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(fs)).Literal(TypeTags.CLASS, fs.getText()); 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:640:22: 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:642:1: expressionListOpt returns [ListBuffer<JCExpression> args = new ListBuffer<JCExpression>()] : (e1= expression ( COMMA e= expression )* )? ;
    public final ListBuffer<JCExpression> expressionListOpt() throws RecognitionException {
        ListBuffer<JCExpression> args =  new ListBuffer<JCExpression>();

        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:643:2: ( (e1= expression ( COMMA e= expression )* )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:643:4: (e1= expression ( COMMA e= expression )* )?
            {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:643:4: (e1= expression ( COMMA e= expression )* )?
            int alt99=2;
            int LA99_0 = input.LA(1);

            if ( ((LA99_0>=POUND && LA99_0<=TYPEOF)||LA99_0==IF||(LA99_0>=THIS && LA99_0<=FALSE)||LA99_0==UNITINTERVAL||LA99_0==FOREACH||(LA99_0>=NOT && LA99_0<=NEW)||(LA99_0>=OPERATION && LA99_0<=FUNCTION)||(LA99_0>=INDEXOF && LA99_0<=SUPER)||(LA99_0>=SIZEOF && LA99_0<=REVERSE)||LA99_0==LPAREN||LA99_0==LBRACKET||LA99_0==DOT||(LA99_0>=PLUSPLUS && LA99_0<=SUBSUB)||(LA99_0>=QUES && LA99_0<=STRING_LITERAL)||LA99_0==QUOTE_LBRACE_STRING_LITERAL||(LA99_0>=QUOTED_IDENTIFIER && LA99_0<=INTEGER_LITERAL)||LA99_0==FLOATING_POINT_LITERAL||LA99_0==IDENTIFIER) ) {
                alt99=1;
            }
            switch (alt99) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:643:6: e1= expression ( COMMA e= expression )*
                    {
                    pushFollow(FOLLOW_expression_in_expressionListOpt7038);
                    e1=expression();
                    _fsp--;
                    if (failed) return args;
                    if ( backtracking==0 ) {
                       args.append(e1); 
                    }
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:644:6: ( COMMA e= expression )*
                    loop98:
                    do {
                        int alt98=2;
                        int LA98_0 = input.LA(1);

                        if ( (LA98_0==COMMA) ) {
                            alt98=1;
                        }


                        switch (alt98) {
                    	case 1 :
                    	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:644:7: COMMA e= expression
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_expressionListOpt7049); if (failed) return args;
                    	    pushFollow(FOLLOW_expression_in_expressionListOpt7055);
                    	    e=expression();
                    	    _fsp--;
                    	    if (failed) return args;
                    	    if ( backtracking==0 ) {
                    	       args.append(e); 
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop98;
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:645:1: orderBy returns [JCExpression expr] : ORDER BY expression ;
    public final JCExpression orderBy() throws RecognitionException {
        JCExpression expr = null;

        JCExpression expression99 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:646:2: ( ORDER BY expression )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:646:4: ORDER BY expression
            {
            match(input,ORDER,FOLLOW_ORDER_in_orderBy7077); if (failed) return expr;
            match(input,BY,FOLLOW_BY_in_orderBy7081); if (failed) return expr;
            pushFollow(FOLLOW_expression_in_orderBy7085);
            expression99=expression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = expression99; 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:647:1: indexOn returns [JCExpression expr = null] : INDEX ON name ( COMMA name )* ;
    public final JCExpression indexOn() throws RecognitionException {
        JCExpression expr =  null;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:648:2: ( INDEX ON name ( COMMA name )* )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:648:4: INDEX ON name ( COMMA name )*
            {
            match(input,INDEX,FOLLOW_INDEX_in_indexOn7100); if (failed) return expr;
            match(input,ON,FOLLOW_ON_in_indexOn7104); if (failed) return expr;
            pushFollow(FOLLOW_name_in_indexOn7108);
            name();
            _fsp--;
            if (failed) return expr;
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:648:24: ( COMMA name )*
            loop100:
            do {
                int alt100=2;
                int LA100_0 = input.LA(1);

                if ( (LA100_0==COMMA) ) {
                    int LA100_2 = input.LA(2);

                    if ( (LA100_2==QUOTED_IDENTIFIER||LA100_2==IDENTIFIER) ) {
                        int LA100_3 = input.LA(3);

                        if ( (synpred177()) ) {
                            alt100=1;
                        }


                    }


                }


                switch (alt100) {
            	case 1 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:648:28: COMMA name
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_indexOn7116); if (failed) return expr;
            	    pushFollow(FOLLOW_name_in_indexOn7120);
            	    name();
            	    _fsp--;
            	    if (failed) return expr;

            	    }
            	    break;

            	default :
            	    break loop100;
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:649:1: multiplyOperator : ( STAR | SLASH | PERCENT );
    public final void multiplyOperator() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:649:18: ( STAR | SLASH | PERCENT )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:650:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );
    public final int unaryOperator() throws RecognitionException {
        int optag = 0;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:651:2: ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB )
            int alt101=9;
            switch ( input.LA(1) ) {
            case POUND:
                {
                alt101=1;
                }
                break;
            case QUES:
                {
                alt101=2;
                }
                break;
            case SUB:
                {
                alt101=3;
                }
                break;
            case NOT:
                {
                alt101=4;
                }
                break;
            case SIZEOF:
                {
                alt101=5;
                }
                break;
            case TYPEOF:
                {
                alt101=6;
                }
                break;
            case REVERSE:
                {
                alt101=7;
                }
                break;
            case PLUSPLUS:
                {
                alt101=8;
                }
                break;
            case SUBSUB:
                {
                alt101=9;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return optag;}
                NoViableAltException nvae =
                    new NoViableAltException("650:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );", 101, 0, input);

                throw nvae;
            }

            switch (alt101) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:651:4: POUND
                    {
                    match(input,POUND,FOLLOW_POUND_in_unaryOperator7164); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:652:4: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_unaryOperator7175); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:653:4: SUB
                    {
                    match(input,SUB,FOLLOW_SUB_in_unaryOperator7188); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.NEG; 
                    }

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:654:4: NOT
                    {
                    match(input,NOT,FOLLOW_NOT_in_unaryOperator7201); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.NOT; 
                    }

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:655:4: SIZEOF
                    {
                    match(input,SIZEOF,FOLLOW_SIZEOF_in_unaryOperator7214); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:656:4: TYPEOF
                    {
                    match(input,TYPEOF,FOLLOW_TYPEOF_in_unaryOperator7227); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:657:4: REVERSE
                    {
                    match(input,REVERSE,FOLLOW_REVERSE_in_unaryOperator7240); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:658:4: PLUSPLUS
                    {
                    match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_unaryOperator7253); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:659:4: SUBSUB
                    {
                    match(input,SUBSUB,FOLLOW_SUBSUB_in_unaryOperator7266); if (failed) return optag;
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:661:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );
    public final int assignmentOperator() throws RecognitionException {
        int optag = 0;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:662:2: ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ )
            int alt102=5;
            switch ( input.LA(1) ) {
            case PLUSEQ:
                {
                alt102=1;
                }
                break;
            case SUBEQ:
                {
                alt102=2;
                }
                break;
            case STAREQ:
                {
                alt102=3;
                }
                break;
            case SLASHEQ:
                {
                alt102=4;
                }
                break;
            case PERCENTEQ:
                {
                alt102=5;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return optag;}
                NoViableAltException nvae =
                    new NoViableAltException("661:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );", 102, 0, input);

                throw nvae;
            }

            switch (alt102) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:662:4: PLUSEQ
                    {
                    match(input,PLUSEQ,FOLLOW_PLUSEQ_in_assignmentOperator7287); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.PLUS_ASG; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:663:4: SUBEQ
                    {
                    match(input,SUBEQ,FOLLOW_SUBEQ_in_assignmentOperator7300); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.MINUS_ASG; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:664:4: STAREQ
                    {
                    match(input,STAREQ,FOLLOW_STAREQ_in_assignmentOperator7313); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.MUL_ASG; 
                    }

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:665:4: SLASHEQ
                    {
                    match(input,SLASHEQ,FOLLOW_SLASHEQ_in_assignmentOperator7326); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.DIV_ASG; 
                    }

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:666:4: PERCENTEQ
                    {
                    match(input,PERCENTEQ,FOLLOW_PERCENTEQ_in_assignmentOperator7339); if (failed) return optag;
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:668:1: typeReference returns [JFXType type] : ( COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | typeName ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? ;
    public final JFXType typeReference() throws RecognitionException {
        JFXType type = null;

        Token STAR102=null;
        int ccf = 0;

        int ccn = 0;

        int ccs = 0;

        ListBuffer<JCTree> formalParameters100 = null;

        JCExpression typeName101 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:669:2: ( ( COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | typeName ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:669:4: ( COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | typeName ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:669:4: ( COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | typeName ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            int alt105=2;
            int LA105_0 = input.LA(1);

            if ( (LA105_0==COLON) ) {
                alt105=1;
            }
            switch (alt105) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:669:6: COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | typeName ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    {
                    match(input,COLON,FOLLOW_COLON_in_typeReference7363); if (failed) return type;
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:669:13: ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | typeName ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    int alt104=3;
                    switch ( input.LA(1) ) {
                    case OPERATION:
                    case FUNCTION:
                    case LPAREN:
                        {
                        alt104=1;
                        }
                        break;
                    case QUOTED_IDENTIFIER:
                    case IDENTIFIER:
                        {
                        alt104=2;
                        }
                        break;
                    case STAR:
                        {
                        alt104=3;
                        }
                        break;
                    default:
                        if (backtracking>0) {failed=true; return type;}
                        NoViableAltException nvae =
                            new NoViableAltException("669:13: ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | typeName ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )", 104, 0, input);

                        throw nvae;
                    }

                    switch (alt104) {
                        case 1 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:670:22: ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint )
                            {
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:670:22: ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint )
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:670:55: ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint
                            {
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:670:55: ( FUNCTION | OPERATION )?
                            int alt103=2;
                            int LA103_0 = input.LA(1);

                            if ( ((LA103_0>=OPERATION && LA103_0<=FUNCTION)) ) {
                                alt103=1;
                            }
                            switch (alt103) {
                                case 1 :
                                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
                                    {
                                    if ( (input.LA(1)>=OPERATION && input.LA(1)<=FUNCTION) ) {
                                        input.consume();
                                        errorRecovery=false;failed=false;
                                    }
                                    else {
                                        if (backtracking>0) {failed=true; return type;}
                                        MismatchedSetException mse =
                                            new MismatchedSetException(null,input);
                                        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_typeReference7403);    throw mse;
                                    }


                                    }
                                    break;

                            }

                            pushFollow(FOLLOW_formalParameters_in_typeReference7412);
                            formalParameters100=formalParameters();
                            _fsp--;
                            if (failed) return type;
                            pushFollow(FOLLOW_typeReference_in_typeReference7414);
                            typeReference();
                            _fsp--;
                            if (failed) return type;
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference7418);
                            ccf=cardinalityConstraint();
                            _fsp--;
                            if (failed) return type;

                            }

                            if ( backtracking==0 ) {
                               type = F.TypeFunctional(formalParameters100.toList(), 
                                                                                                                     type, ccf); 
                            }

                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:673:22: typeName ccn= cardinalityConstraint
                            {
                            pushFollow(FOLLOW_typeName_in_typeReference7473);
                            typeName101=typeName();
                            _fsp--;
                            if (failed) return type;
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference7477);
                            ccn=cardinalityConstraint();
                            _fsp--;
                            if (failed) return type;
                            if ( backtracking==0 ) {
                               type = F.TypeClass(typeName101, ccn); 
                            }

                            }
                            break;
                        case 3 :
                            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:674:22: STAR ccs= cardinalityConstraint
                            {
                            STAR102=(Token)input.LT(1);
                            match(input,STAR,FOLLOW_STAR_in_typeReference7503); if (failed) return type;
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference7507);
                            ccs=cardinalityConstraint();
                            _fsp--;
                            if (failed) return type;
                            if ( backtracking==0 ) {
                               type = F.at(pos(STAR102)).TypeAny(ccs); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:676:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );
    public final int cardinalityConstraint() throws RecognitionException {
        int ary = 0;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:677:2: ( LBRACKET RBRACKET | QUES | PLUS | STAR | )
            int alt106=5;
            switch ( input.LA(1) ) {
            case LBRACKET:
                {
                int LA106_1 = input.LA(2);

                if ( (synpred197()) ) {
                    alt106=1;
                }
                else if ( (true) ) {
                    alt106=5;
                }
                else {
                    if (backtracking>0) {failed=true; return ary;}
                    NoViableAltException nvae =
                        new NoViableAltException("676:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 106, 1, input);

                    throw nvae;
                }
                }
                break;
            case QUES:
                {
                int LA106_2 = input.LA(2);

                if ( (synpred198()) ) {
                    alt106=2;
                }
                else if ( (true) ) {
                    alt106=5;
                }
                else {
                    if (backtracking>0) {failed=true; return ary;}
                    NoViableAltException nvae =
                        new NoViableAltException("676:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 106, 2, input);

                    throw nvae;
                }
                }
                break;
            case PLUS:
                {
                int LA106_3 = input.LA(2);

                if ( (synpred199()) ) {
                    alt106=3;
                }
                else if ( (true) ) {
                    alt106=5;
                }
                else {
                    if (backtracking>0) {failed=true; return ary;}
                    NoViableAltException nvae =
                        new NoViableAltException("676:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 106, 3, input);

                    throw nvae;
                }
                }
                break;
            case STAR:
                {
                int LA106_4 = input.LA(2);

                if ( (synpred200()) ) {
                    alt106=4;
                }
                else if ( (true) ) {
                    alt106=5;
                }
                else {
                    if (backtracking>0) {failed=true; return ary;}
                    NoViableAltException nvae =
                        new NoViableAltException("676:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 106, 4, input);

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
                alt106=5;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ary;}
                NoViableAltException nvae =
                    new NoViableAltException("676:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 106, 0, input);

                throw nvae;
            }

            switch (alt106) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:677:5: LBRACKET RBRACKET
                    {
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_cardinalityConstraint7538); if (failed) return ary;
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_cardinalityConstraint7542); if (failed) return ary;
                    if ( backtracking==0 ) {
                       ary = JFXType.CARDINALITY_ANY; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:678:5: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_cardinalityConstraint7554); if (failed) return ary;
                    if ( backtracking==0 ) {
                       ary = JFXType.CARDINALITY_OPTIONAL; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:679:5: PLUS
                    {
                    match(input,PLUS,FOLLOW_PLUS_in_cardinalityConstraint7581); if (failed) return ary;
                    if ( backtracking==0 ) {
                       ary = JFXType.CARDINALITY_ANY; 
                    }

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:680:5: STAR
                    {
                    match(input,STAR,FOLLOW_STAR_in_cardinalityConstraint7608); if (failed) return ary;
                    if ( backtracking==0 ) {
                       ary = JFXType.CARDINALITY_ANY; 
                    }

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:681:29: 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:683:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );
    public final JCExpression literal() throws RecognitionException {
        JCExpression expr = null;

        Token t=null;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:684:2: (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL )
            int alt107=6;
            switch ( input.LA(1) ) {
            case STRING_LITERAL:
                {
                alt107=1;
                }
                break;
            case INTEGER_LITERAL:
                {
                alt107=2;
                }
                break;
            case FLOATING_POINT_LITERAL:
                {
                alt107=3;
                }
                break;
            case TRUE:
                {
                alt107=4;
                }
                break;
            case FALSE:
                {
                alt107=5;
                }
                break;
            case NULL:
                {
                alt107=6;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return expr;}
                NoViableAltException nvae =
                    new NoViableAltException("683:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );", 107, 0, input);

                throw nvae;
            }

            switch (alt107) {
                case 1 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:684:4: t= STRING_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_literal7677); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.CLASS, t.getText()); 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:685:4: t= INTEGER_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,INTEGER_LITERAL,FOLLOW_INTEGER_LITERAL_in_literal7687); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.INT, Convert.string2int(t.getText(), 10)); 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:686:4: t= FLOATING_POINT_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,FLOATING_POINT_LITERAL,FOLLOW_FLOATING_POINT_LITERAL_in_literal7697); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.DOUBLE, Double.valueOf(t.getText())); 
                    }

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:687:4: t= TRUE
                    {
                    t=(Token)input.LT(1);
                    match(input,TRUE,FOLLOW_TRUE_in_literal7707); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 1); 
                    }

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:688:4: t= FALSE
                    {
                    t=(Token)input.LT(1);
                    match(input,FALSE,FOLLOW_FALSE_in_literal7721); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 0); 
                    }

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:689:4: t= NULL
                    {
                    t=(Token)input.LT(1);
                    match(input,NULL,FOLLOW_NULL_in_literal7735); if (failed) return expr;
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:691:1: typeName returns [JCExpression expr] : qualident ;
    public final JCExpression typeName() throws RecognitionException {
        JCExpression expr = null;

        JCExpression qualident103 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:692:8: ( qualident )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:692:10: qualident
            {
            pushFollow(FOLLOW_qualident_in_typeName7762);
            qualident103=qualident();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = qualident103; 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:694:1: qualident returns [JCExpression expr] : n1= name ( DOT nn= name )* ;
    public final JCExpression qualident() throws RecognitionException {
        JCExpression expr = null;

        name_return n1 = null;

        name_return nn = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:695:8: (n1= name ( DOT nn= name )* )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:695:10: n1= name ( DOT nn= name )*
            {
            pushFollow(FOLLOW_name_in_qualident7806);
            n1=name();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = F.at(n1.pos).Identifier(n1.value); 
            }
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:696:10: ( DOT nn= name )*
            loop108:
            do {
                int alt108=2;
                int LA108_0 = input.LA(1);

                if ( (LA108_0==DOT) ) {
                    int LA108_2 = input.LA(2);

                    if ( (LA108_2==QUOTED_IDENTIFIER||LA108_2==IDENTIFIER) ) {
                        int LA108_3 = input.LA(3);

                        if ( (synpred206()) ) {
                            alt108=1;
                        }


                    }


                }


                switch (alt108) {
            	case 1 :
            	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:696:12: DOT nn= name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_qualident7835); if (failed) return expr;
            	    pushFollow(FOLLOW_name_in_qualident7839);
            	    nn=name();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(nn.pos).Select(expr, nn.value); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop108;
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:698:1: identifier returns [JCIdent expr] : name ;
    public final JCIdent identifier() throws RecognitionException {
        JCIdent expr = null;

        name_return name104 = null;


        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:699:2: ( name )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:699:4: name
            {
            pushFollow(FOLLOW_name_in_identifier7876);
            name104=name();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = F.at(name104.pos).Ident(name104.value); 
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
    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:701:1: name returns [Name value, int pos] : tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) ;
    public final name_return name() throws RecognitionException {
        name_return retval = new name_return();
        retval.start = input.LT(1);

        Token tokid=null;

        try {
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:702:2: (tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) )
            // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:702:4: tokid= ( QUOTED_IDENTIFIER | IDENTIFIER )
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
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_name7910);    throw mse;
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
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:433:4: ( localFunctionDefinition )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:433:4: localFunctionDefinition
        {
        pushFollow(FOLLOW_localFunctionDefinition_in_synpred453575);
        localFunctionDefinition();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred45

    // $ANTLR start synpred46
    public final void synpred46_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:434:4: ( localOperationDefinition )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:434:4: localOperationDefinition
        {
        pushFollow(FOLLOW_localOperationDefinition_in_synpred463583);
        localOperationDefinition();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred46

    // $ANTLR start synpred47
    public final void synpred47_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:435:10: ( backgroundStatement )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:435:10: backgroundStatement
        {
        pushFollow(FOLLOW_backgroundStatement_in_synpred473597);
        backgroundStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred47

    // $ANTLR start synpred48
    public final void synpred48_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:436:10: ( laterStatement )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:436:10: laterStatement
        {
        pushFollow(FOLLOW_laterStatement_in_synpred483612);
        laterStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred48

    // $ANTLR start synpred50
    public final void synpred50_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:438:10: ( ifStatement )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:438:10: ifStatement
        {
        pushFollow(FOLLOW_ifStatement_in_synpred503648);
        ifStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred50

    // $ANTLR start synpred53
    public final void synpred53_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:441:4: ( expression SEMI )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:441:4: expression SEMI
        {
        pushFollow(FOLLOW_expression_in_synpred533691);
        expression();
        _fsp--;
        if (failed) return ;
        match(input,SEMI,FOLLOW_SEMI_in_synpred533695); if (failed) return ;

        }
    }
    // $ANTLR end synpred53

    // $ANTLR start synpred58
    public final void synpred58_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:446:10: ( forAlphaStatement )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:446:10: forAlphaStatement
        {
        pushFollow(FOLLOW_forAlphaStatement_in_synpred583772);
        forAlphaStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred58

    // $ANTLR start synpred59
    public final void synpred59_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:447:10: ( forJoinStatement )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:447:10: forJoinStatement
        {
        pushFollow(FOLLOW_forJoinStatement_in_synpred593788);
        forJoinStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred59

    // $ANTLR start synpred80
    public final void synpred80_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:500:62: ( FPS expression )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:500:62: FPS expression
        {
        match(input,FPS,FOLLOW_FPS_in_synpred804642); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred804646);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred80

    // $ANTLR start synpred81
    public final void synpred81_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:500:91: ( WHILE expression )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:500:91: WHILE expression
        {
        match(input,WHILE,FOLLOW_WHILE_in_synpred814660); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred814664);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred81

    // $ANTLR start synpred82
    public final void synpred82_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:500:122: ( CONTINUE IF expression )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:500:122: CONTINUE IF expression
        {
        match(input,CONTINUE,FOLLOW_CONTINUE_in_synpred824678); if (failed) return ;
        match(input,IF,FOLLOW_IF_in_synpred824682); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred824686);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred82

    // $ANTLR start synpred86
    public final void synpred86_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:36: ( LINEAR )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:36: LINEAR
        {
        match(input,LINEAR,FOLLOW_LINEAR_in_synpred864825); if (failed) return ;

        }
    }
    // $ANTLR end synpred86

    // $ANTLR start synpred87
    public final void synpred87_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:49: ( EASEIN )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:49: EASEIN
        {
        match(input,EASEIN,FOLLOW_EASEIN_in_synpred874833); if (failed) return ;

        }
    }
    // $ANTLR end synpred87

    // $ANTLR start synpred88
    public final void synpred88_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:62: ( EASEOUT )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:62: EASEOUT
        {
        match(input,EASEOUT,FOLLOW_EASEOUT_in_synpred884841); if (failed) return ;

        }
    }
    // $ANTLR end synpred88

    // $ANTLR start synpred89
    public final void synpred89_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:76: ( EASEBOTH )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:76: EASEBOTH
        {
        match(input,EASEBOTH,FOLLOW_EASEBOTH_in_synpred894849); if (failed) return ;

        }
    }
    // $ANTLR end synpred89

    // $ANTLR start synpred90
    public final void synpred90_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:91: ( MOTION expression )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:91: MOTION expression
        {
        match(input,MOTION,FOLLOW_MOTION_in_synpred904857); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred904861);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred90

    // $ANTLR start synpred91
    public final void synpred91_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:123: ( FPS expression )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:123: FPS expression
        {
        match(input,FPS,FOLLOW_FPS_in_synpred914875); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred914879);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred91

    // $ANTLR start synpred92
    public final void synpred92_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:150: ( WHILE expression )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:150: WHILE expression
        {
        match(input,WHILE,FOLLOW_WHILE_in_synpred924891); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred924895);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred92

    // $ANTLR start synpred93
    public final void synpred93_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:181: ( CONTINUE IF expression )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:181: CONTINUE IF expression
        {
        match(input,CONTINUE,FOLLOW_CONTINUE_in_synpred934909); if (failed) return ;
        match(input,IF,FOLLOW_IF_in_synpred934913); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred934917);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred93

    // $ANTLR start synpred97
    public final void synpred97_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:507:39: ( typeReference )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:507:39: typeReference
        {
        pushFollow(FOLLOW_typeReference_in_synpred975008);
        typeReference();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred97

    // $ANTLR start synpred105
    public final void synpred105_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:515:11: ( LPAREN typeName RPAREN suffixedExpression )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:515:11: LPAREN typeName RPAREN suffixedExpression
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred1055131); if (failed) return ;
        pushFollow(FOLLOW_typeName_in_synpred1055137);
        typeName();
        _fsp--;
        if (failed) return ;
        match(input,RPAREN,FOLLOW_RPAREN_in_synpred1055140); if (failed) return ;
        pushFollow(FOLLOW_suffixedExpression_in_synpred1055144);
        suffixedExpression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred105

    // $ANTLR start synpred108
    public final void synpred108_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:52: ( typeReference )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:52: typeReference
        {
        pushFollow(FOLLOW_typeReference_in_synpred1085255);
        typeReference();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred108

    // $ANTLR start synpred109
    public final void synpred109_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:519:54: ( typeReference )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:519:54: typeReference
        {
        pushFollow(FOLLOW_typeReference_in_synpred1095277);
        typeReference();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred109

    // $ANTLR start synpred111
    public final void synpred111_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:83: ( COMMA selectionVar )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:83: COMMA selectionVar
        {
        match(input,COMMA,FOLLOW_COMMA_in_synpred1115347); if (failed) return ;
        pushFollow(FOLLOW_selectionVar_in_synpred1115351);
        selectionVar();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred111

    // $ANTLR start synpred112
    public final void synpred112_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:116: ( WHERE expression )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:116: WHERE expression
        {
        match(input,WHERE,FOLLOW_WHERE_in_synpred1125365); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred1125369);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred112

    // $ANTLR start synpred143
    public final void synpred143_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:578:4: ( typeName LBRACE objectLiteral RBRACE )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:578:4: typeName LBRACE objectLiteral RBRACE
        {
        pushFollow(FOLLOW_typeName_in_synpred1436233);
        typeName();
        _fsp--;
        if (failed) return ;
        match(input,LBRACE,FOLLOW_LBRACE_in_synpred1436235); if (failed) return ;
        pushFollow(FOLLOW_objectLiteral_in_synpred1436238);
        objectLiteral();
        _fsp--;
        if (failed) return ;
        match(input,RBRACE,FOLLOW_RBRACE_in_synpred1436240); if (failed) return ;

        }
    }
    // $ANTLR end synpred143

    // $ANTLR start synpred150
    public final void synpred150_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:585:10: ( identifier ( LPAREN expressionListOpt RPAREN )* )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:585:10: identifier ( LPAREN expressionListOpt RPAREN )*
        {
        pushFollow(FOLLOW_identifier_in_synpred1506327);
        identifier();
        _fsp--;
        if (failed) return ;
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:586:10: ( LPAREN expressionListOpt RPAREN )*
        loop122:
        do {
            int alt122=2;
            int LA122_0 = input.LA(1);

            if ( (LA122_0==LPAREN) ) {
                alt122=1;
            }


            switch (alt122) {
        	case 1 :
        	    // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:586:12: LPAREN expressionListOpt RPAREN
        	    {
        	    match(input,LPAREN,FOLLOW_LPAREN_in_synpred1506348); if (failed) return ;
        	    pushFollow(FOLLOW_expressionListOpt_in_synpred1506352);
        	    expressionListOpt();
        	    _fsp--;
        	    if (failed) return ;
        	    match(input,RPAREN,FOLLOW_RPAREN_in_synpred1506356); if (failed) return ;

        	    }
        	    break;

        	default :
        	    break loop122;
            }
        } while (true);


        }
    }
    // $ANTLR end synpred150

    // $ANTLR start synpred177
    public final void synpred177_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:648:28: ( COMMA name )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:648:28: COMMA name
        {
        match(input,COMMA,FOLLOW_COMMA_in_synpred1777116); if (failed) return ;
        pushFollow(FOLLOW_name_in_synpred1777120);
        name();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred177

    // $ANTLR start synpred197
    public final void synpred197_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:677:5: ( LBRACKET RBRACKET )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:677:5: LBRACKET RBRACKET
        {
        match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred1977538); if (failed) return ;
        match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred1977542); if (failed) return ;

        }
    }
    // $ANTLR end synpred197

    // $ANTLR start synpred198
    public final void synpred198_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:678:5: ( QUES )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:678:5: QUES
        {
        match(input,QUES,FOLLOW_QUES_in_synpred1987554); if (failed) return ;

        }
    }
    // $ANTLR end synpred198

    // $ANTLR start synpred199
    public final void synpred199_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:679:5: ( PLUS )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:679:5: PLUS
        {
        match(input,PLUS,FOLLOW_PLUS_in_synpred1997581); if (failed) return ;

        }
    }
    // $ANTLR end synpred199

    // $ANTLR start synpred200
    public final void synpred200_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:680:5: ( STAR )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:680:5: STAR
        {
        match(input,STAR,FOLLOW_STAR_in_synpred2007608); if (failed) return ;

        }
    }
    // $ANTLR end synpred200

    // $ANTLR start synpred206
    public final void synpred206_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:696:12: ( DOT name )
        // C:\\JavaFX\\3\\branches\\visitor-reorg\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:696:12: DOT name
        {
        match(input,DOT,FOLLOW_DOT_in_synpred2067835); if (failed) return ;
        pushFollow(FOLLOW_name_in_synpred2067839);
        name();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred206

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
    public final boolean synpred206() {
        backtracking++;
        int start = input.mark();
        try {
            synpred206_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred199() {
        backtracking++;
        int start = input.mark();
        try {
            synpred199_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred200() {
        backtracking++;
        int start = input.mark();
        try {
            synpred200_fragment(); // can never throw exception
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
    public final boolean synpred177() {
        backtracking++;
        int start = input.mark();
        try {
            synpred177_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred197() {
        backtracking++;
        int start = input.mark();
        try {
            synpred197_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred198() {
        backtracking++;
        int start = input.mark();
        try {
            synpred198_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_packageDecl_in_module2085 = new BitSet(new long[]{0x499F914B81644260L,0x582C007008ADC0FFL,0x0000000000000002L});
    public static final BitSet FOLLOW_moduleItems_in_module2088 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_module2090 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PACKAGE_in_packageDecl2123 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_qualident_in_packageDecl2125 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_packageDecl2127 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_moduleItem_in_moduleItems2156 = new BitSet(new long[]{0x499F914B81644262L,0x582C007008ADC0FFL,0x0000000000000002L});
    public static final BitSet FOLLOW_importDecl_in_moduleItem2198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDefinition_in_moduleItem2213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeDefinition_in_moduleItem2228 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberOperationDefinition_in_moduleItem2242 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberFunctionDefinition_in_moduleItem2255 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRIGGER_in_moduleItem2268 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ON_in_moduleItem2270 = new BitSet(new long[]{0x0000000000000000L,0x0800000000200000L,0x0000000000000002L});
    public static final BitSet FOLLOW_changeRule_in_moduleItem2272 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statementExcept_in_moduleItem2285 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importDecl2314 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_importDecl2317 = new BitSet(new long[]{0x0000000000000000L,0x000000000A000000L});
    public static final BitSet FOLLOW_DOT_in_importDecl2341 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_importDecl2343 = new BitSet(new long[]{0x0000000000000000L,0x000000000A000000L});
    public static final BitSet FOLLOW_DOT_in_importDecl2371 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_STAR_in_importDecl2373 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_importDecl2381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_classDefinition2407 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_CLASS_in_classDefinition2410 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_classDefinition2412 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000400L});
    public static final BitSet FOLLOW_supers_in_classDefinition2414 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_classDefinition2416 = new BitSet(new long[]{0x0000000000004200L,0x020000000000009CL});
    public static final BitSet FOLLOW_classMembers_in_classDefinition2418 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_classDefinition2420 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXTENDS_in_supers2446 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_qualident_in_supers2450 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_supers2474 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_qualident_in_supers2478 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_attributeDecl_in_classMembers2512 = new BitSet(new long[]{0x0000000000004202L,0x000000000000009CL});
    public static final BitSet FOLLOW_functionDecl_in_classMembers2530 = new BitSet(new long[]{0x0000000000004202L,0x000000000000009CL});
    public static final BitSet FOLLOW_operationDecl_in_classMembers2549 = new BitSet(new long[]{0x0000000000004202L,0x000000000000009CL});
    public static final BitSet FOLLOW_modifierFlags_in_attributeDecl2577 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeDecl2579 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_attributeDecl2581 = new BitSet(new long[]{0x0000000000000000L,0x0002000002001900L});
    public static final BitSet FOLLOW_typeReference_in_attributeDecl2583 = new BitSet(new long[]{0x0000000000000000L,0x0000000002001900L});
    public static final BitSet FOLLOW_inverseClause_in_attributeDecl2585 = new BitSet(new long[]{0x0000000000000000L,0x0000000002001800L});
    public static final BitSet FOLLOW_orderBy_in_attributeDecl2589 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_indexOn_in_attributeDecl2593 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_attributeDecl2597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INVERSE_in_inverseClause2615 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_inverseClause2617 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_functionDecl2632 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_FUNCTION_in_functionDecl2634 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_functionDecl2636 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_functionDecl2638 = new BitSet(new long[]{0x0000000000000000L,0x0002000002000000L});
    public static final BitSet FOLLOW_typeReference_in_functionDecl2640 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_functionDecl2642 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_operationDecl2657 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_OPERATION_in_operationDecl2661 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_operationDecl2665 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_operationDecl2669 = new BitSet(new long[]{0x0000000000000000L,0x0002000002000000L});
    public static final BitSet FOLLOW_typeReference_in_operationDecl2673 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_operationDecl2678 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeDefinition2693 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_attributeDefinition2697 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_attributeDefinition2701 = new BitSet(new long[]{0x4017900060010060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_bindOpt_in_attributeDefinition2703 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_attributeDefinition2706 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_attributeDefinition2710 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATION_in_memberOperationDefinition2724 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_memberOperationDefinition2728 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_memberOperationDefinition2732 = new BitSet(new long[]{0x0000000000000000L,0x0042000000000000L});
    public static final BitSet FOLLOW_typeReference_in_memberOperationDefinition2736 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_memberOperationDefinition2739 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_memberFunctionDefinition2754 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_memberFunctionDefinition2758 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_memberFunctionDefinition2762 = new BitSet(new long[]{0x0000000000000000L,0x0042000000000000L});
    public static final BitSet FOLLOW_typeReference_in_memberFunctionDefinition2766 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_memberFunctionDefinition2769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQ_in_functionBody2784 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_functionBody2788 = new BitSet(new long[]{0x8000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_whereVarDecls_in_functionBody2792 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_functionBody2798 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_functionBody2814 = new BitSet(new long[]{0x0000000280000000L,0x0800000000000060L,0x0000000000000002L});
    public static final BitSet FOLLOW_variableDefinition_in_functionBody2822 = new BitSet(new long[]{0x0000000280000000L,0x0800000000000060L,0x0000000000000002L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_functionBody2830 = new BitSet(new long[]{0x0000000280000000L,0x0800000000000060L,0x0000000000000002L});
    public static final BitSet FOLLOW_localOperationDefinition_in_functionBody2838 = new BitSet(new long[]{0x0000000280000000L,0x0800000000000060L,0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_functionBody2848 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_functionBody2852 = new BitSet(new long[]{0x0000000000000000L,0x0200000002000000L});
    public static final BitSet FOLLOW_SEMI_in_functionBody2856 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_functionBody2862 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_whereVarDecls2870 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000040L,0x0000000000000002L});
    public static final BitSet FOLLOW_whereVarDecl_in_whereVarDecls2874 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_whereVarDecls2882 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000040L,0x0000000000000002L});
    public static final BitSet FOLLOW_whereVarDecl_in_whereVarDecls2886 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_whereVarDecl2900 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_whereVarDecl2912 = new BitSet(new long[]{0x0000000000000000L,0x0002000020000000L});
    public static final BitSet FOLLOW_typeReference_in_whereVarDecl2916 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_whereVarDecl2920 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_whereVarDecl2924 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variableDefinition2932 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_variableDefinition2936 = new BitSet(new long[]{0x0000000000000000L,0x0002000020000000L});
    public static final BitSet FOLLOW_typeReference_in_variableDefinition2940 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_variableDefinition2943 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_variableDefinition2947 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_variableDefinition2951 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule2962 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_changeRule2966 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_typeName_in_changeRule2970 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule2973 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule2976 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule2981 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule2985 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_changeRule2988 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_changeRule2990 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule2994 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule2997 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3002 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_changeRule3005 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_changeRule3007 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3009 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule3014 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3018 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_LBRACKET_in_changeRule3022 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_changeRule3028 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_changeRule3032 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_changeRule3036 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_changeRule3040 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule3044 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3047 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule3052 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_INSERT_in_changeRule3056 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_changeRule3060 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_INTO_in_changeRule3064 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3068 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule3072 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3074 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule3081 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_DELETE_in_changeRule3085 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_changeRule3089 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_FROM_in_changeRule3093 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3097 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule3101 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule3110 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_DELETE_in_changeRule3114 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3117 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_LBRACKET_in_changeRule3121 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_changeRule3125 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_changeRule3129 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule3133 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags3158 = new BitSet(new long[]{0x0000000000000002L,0x000000000000001CL});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags3171 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags3193 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000080L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags3206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_accessModifier3254 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PRIVATE_in_accessModifier3271 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROTECTED_in_accessModifier3287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ABSTRACT_in_otherModifier3311 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_READONLY_in_otherModifier3326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_memberSelector3352 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_DOT_in_memberSelector3356 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_memberSelector3362 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_formalParameters3378 = new BitSet(new long[]{0x0000000000000000L,0x0800000000400000L,0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters3386 = new BitSet(new long[]{0x0000000000000000L,0x0000000004400000L});
    public static final BitSet FOLLOW_COMMA_in_formalParameters3405 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters3411 = new BitSet(new long[]{0x0000000000000000L,0x0000000004400000L});
    public static final BitSet FOLLOW_RPAREN_in_formalParameters3422 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_formalParameter3435 = new BitSet(new long[]{0x0000000000000002L,0x0002000000000000L});
    public static final BitSet FOLLOW_typeReference_in_formalParameter3437 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_block3454 = new BitSet(new long[]{0x499F914381440060L,0x5A2C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_statements_in_block3458 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_block3462 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_statements3480 = new BitSet(new long[]{0x499F914381440062L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_statementExcept_in_statement3531 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localTriggerStatement_in_statement3547 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclaration_in_statementExcept3565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_statementExcept3575 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localOperationDefinition_in_statementExcept3583 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_backgroundStatement_in_statementExcept3597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_laterStatement_in_statementExcept3612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_statementExcept3627 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_statementExcept3629 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_statementExcept3631 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_statementExcept3633 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_statementExcept3635 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifStatement_in_statementExcept3648 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_insertStatement_in_statementExcept3665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_deleteStatement_in_statementExcept3681 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_statementExcept3691 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_statementExcept3695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BREAK_in_statementExcept3705 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_statementExcept3709 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_statementExcept3720 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_statementExcept3724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_throwStatement_in_statementExcept3740 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_returnStatement_in_statementExcept3756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forAlphaStatement_in_statementExcept3772 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forJoinStatement_in_statementExcept3788 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tryStatement_in_statementExcept3804 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_assertStatement3823 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_assertStatement3827 = new BitSet(new long[]{0x0000000000000000L,0x0002000002000000L});
    public static final BitSet FOLLOW_COLON_in_assertStatement3835 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_assertStatement3839 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_assertStatement3849 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATION_in_localOperationDefinition3864 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localOperationDefinition3868 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_localOperationDefinition3872 = new BitSet(new long[]{0x0000000000000000L,0x0042000000000000L});
    public static final BitSet FOLLOW_typeReference_in_localOperationDefinition3876 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_localOperationDefinition3879 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_localFunctionDefinition3898 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localFunctionDefinition3904 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_localFunctionDefinition3908 = new BitSet(new long[]{0x0000000000000000L,0x0042000000000000L});
    public static final BitSet FOLLOW_typeReference_in_localFunctionDefinition3912 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_localFunctionDefinition3915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variableDeclaration3934 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_variableDeclaration3937 = new BitSet(new long[]{0x0000000000000000L,0x0002000022000000L});
    public static final BitSet FOLLOW_typeReference_in_variableDeclaration3940 = new BitSet(new long[]{0x0000000000000000L,0x0000000022000000L});
    public static final BitSet FOLLOW_EQ_in_variableDeclaration3951 = new BitSet(new long[]{0x4017900060010060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_bindOpt_in_variableDeclaration3953 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_variableDeclaration3956 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_variableDeclaration3958 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_variableDeclaration3969 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BIND_in_bindOpt4006 = new BitSet(new long[]{0x2000000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt4022 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAYS_in_bindOpt4037 = new BitSet(new long[]{0x2000000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt4053 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TIE_in_bindOpt4068 = new BitSet(new long[]{0x2000000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt4084 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DO_in_backgroundStatement4110 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_backgroundStatement4114 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DO_in_laterStatement4130 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LATER_in_laterStatement4134 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_laterStatement4138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifStatement4158 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_ifStatement4162 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_ifStatement4166 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_ifStatement4170 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_ifStatement4176 = new BitSet(new long[]{0x0000400000000002L});
    public static final BitSet FOLLOW_ELSE_in_ifStatement4179 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_ifStatement4184 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INSERT_in_insertStatement4213 = new BitSet(new long[]{0x4017900000800060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_DISTINCT_in_insertStatement4221 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_insertStatement4225 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_INTO_in_insertStatement4229 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_insertStatement4233 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_expression_in_insertStatement4241 = new BitSet(new long[]{0x0000020000009400L});
    public static final BitSet FOLLOW_AS_in_insertStatement4257 = new BitSet(new long[]{0x00000C0000000000L});
    public static final BitSet FOLLOW_set_in_insertStatement4261 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_INTO_in_insertStatement4287 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_insertStatement4291 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_AFTER_in_insertStatement4303 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_insertStatement4307 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_BEFORE_in_insertStatement4315 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_insertStatement4319 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_insertStatement4333 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DELETE_in_deleteStatement4348 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_deleteStatement4352 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_deleteStatement4356 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THROW_in_throwStatement4371 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_throwStatement4375 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_throwStatement4379 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnStatement4399 = new BitSet(new long[]{0x4017900000000060L,0x582C00700AADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_returnStatement4402 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_returnStatement4409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRIGGER_in_localTriggerStatement4435 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ON_in_localTriggerStatement4439 = new BitSet(new long[]{0x0000010000400000L,0x0800000000200000L,0x0000000000000002L});
    public static final BitSet FOLLOW_localTriggerCondition_in_localTriggerStatement4446 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_LPAREN_in_localTriggerStatement4450 = new BitSet(new long[]{0x0000010000400000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_localTriggerCondition_in_localTriggerStatement4454 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_localTriggerStatement4458 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_localTriggerStatement4462 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4477 = new BitSet(new long[]{0x0000000000000000L,0x0000000020800000L});
    public static final BitSet FOLLOW_LBRACKET_in_localTriggerCondition4485 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4489 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_localTriggerCondition4493 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_localTriggerCondition4503 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_localTriggerCondition4507 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INSERT_in_localTriggerCondition4519 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4523 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_INTO_in_localTriggerCondition4527 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4533 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_localTriggerCondition4537 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_localTriggerCondition4545 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DELETE_in_localTriggerCondition4557 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4561 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_FROM_in_localTriggerCondition4565 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4571 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_localTriggerCondition4575 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_localTriggerCondition4583 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_forAlphaStatement4598 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_forAlphaStatement4602 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_alphaExpression_in_forAlphaStatement4606 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_forAlphaStatement4610 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_forAlphaStatement4614 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNITINTERVAL_in_alphaExpression4622 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_alphaExpression4626 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_DUR_in_alphaExpression4630 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_alphaExpression4634 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_FPS_in_alphaExpression4642 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_alphaExpression4646 = new BitSet(new long[]{0x0180000000000002L});
    public static final BitSet FOLLOW_WHILE_in_alphaExpression4660 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_alphaExpression4664 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_alphaExpression4678 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_IF_in_alphaExpression4682 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_alphaExpression4686 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_forJoinStatement4707 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_forJoinStatement4711 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_joinClause_in_forJoinStatement4715 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_forJoinStatement4719 = new BitSet(new long[]{0x0000000000000000L,0x0040000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_forJoinStatement4727 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_durClause_in_forJoinStatement4731 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_forJoinStatement4735 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_forJoinStatement4745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_joinClause4753 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_joinClause4757 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_joinClause4761 = new BitSet(new long[]{0x8000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_joinClause4769 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_joinClause4773 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_joinClause4777 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_joinClause4781 = new BitSet(new long[]{0x8000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_WHERE_in_joinClause4795 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_joinClause4799 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DUR_in_durClause4813 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_durClause4817 = new BitSet(new long[]{0x07C000001C000002L});
    public static final BitSet FOLLOW_LINEAR_in_durClause4825 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_EASEIN_in_durClause4833 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_EASEOUT_in_durClause4841 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_EASEBOTH_in_durClause4849 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_MOTION_in_durClause4857 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_durClause4861 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_FPS_in_durClause4875 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_durClause4879 = new BitSet(new long[]{0x0180000000000002L});
    public static final BitSet FOLLOW_WHILE_in_durClause4891 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_durClause4895 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_durClause4909 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_IF_in_durClause4913 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_durClause4917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRY_in_tryStatement4938 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement4942 = new BitSet(new long[]{0x1000000000100000L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement4950 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement4954 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catchClause_in_tryStatement4964 = new BitSet(new long[]{0x1000000000100002L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement4974 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement4978 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CATCH_in_catchClause4996 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_catchClause5000 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_catchClause5004 = new BitSet(new long[]{0x0000100000000000L,0x0002000000400000L});
    public static final BitSet FOLLOW_typeReference_in_catchClause5008 = new BitSet(new long[]{0x0000100000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_IF_in_catchClause5018 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_catchClause5022 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_catchClause5032 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_catchClause5036 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_foreach_in_expression5050 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionExpression_in_expression5063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_operationExpression_in_expression5076 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_alphaExpression_in_expression5089 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifExpression_in_expression5102 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_selectExpression_in_expression5118 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_expression5131 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_typeName_in_expression5137 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_expression5140 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_suffixedExpression_in_expression5144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_suffixedExpression_in_expression5159 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOREACH_in_foreach5171 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_foreach5175 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_foreach5179 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_foreach5183 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_foreach5187 = new BitSet(new long[]{0x8000000000000000L,0x0000000004400000L});
    public static final BitSet FOLLOW_COMMA_in_foreach5195 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_foreach5199 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_foreach5203 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_foreach5207 = new BitSet(new long[]{0x8000000000000000L,0x0000000004400000L});
    public static final BitSet FOLLOW_WHERE_in_foreach5221 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_foreach5225 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_foreach5235 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_foreach5239 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_functionExpression5247 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_functionExpression5251 = new BitSet(new long[]{0x0000000000000000L,0x0042000020000000L});
    public static final BitSet FOLLOW_typeReference_in_functionExpression5255 = new BitSet(new long[]{0x0000000000000000L,0x0040000020000000L});
    public static final BitSet FOLLOW_functionBody_in_functionExpression5261 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATION_in_operationExpression5269 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_operationExpression5273 = new BitSet(new long[]{0x0000000000000000L,0x0042000000000000L});
    public static final BitSet FOLLOW_typeReference_in_operationExpression5277 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_operationExpression5283 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifExpression5291 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_ifExpression5295 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_THEN_in_ifExpression5299 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_ifExpression5303 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ELSE_in_ifExpression5307 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_ifExpression5311 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SELECT_in_selectExpression5319 = new BitSet(new long[]{0x4017900000800060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_DISTINCT_in_selectExpression5323 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_selectExpression5331 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_FROM_in_selectExpression5335 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_selectionVar_in_selectExpression5339 = new BitSet(new long[]{0x8000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_selectExpression5347 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_selectionVar_in_selectExpression5351 = new BitSet(new long[]{0x8000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_WHERE_in_selectExpression5365 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_selectExpression5369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_selectionVar5383 = new BitSet(new long[]{0x0020000000000002L});
    public static final BitSet FOLLOW_IN_in_selectionVar5391 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_selectionVar5395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentExpression_in_suffixedExpression5418 = new BitSet(new long[]{0x0000000002000002L,0x0000005000001800L});
    public static final BitSet FOLLOW_indexOn_in_suffixedExpression5430 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orderBy_in_suffixedExpression5434 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_durClause_in_suffixedExpression5438 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_suffixedExpression5442 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_suffixedExpression5446 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression5468 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_assignmentExpression5483 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression5489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression5516 = new BitSet(new long[]{0x0000000000000002L,0x00007C0000000000L});
    public static final BitSet FOLLOW_assignmentOperator_in_assignmentOpExpression5532 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression5538 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orExpression_in_andExpression5564 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_AND_in_andExpression5580 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_orExpression_in_andExpression5586 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression5614 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_OR_in_orExpression5629 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression5635 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression5663 = new BitSet(new long[]{0x0000000000000002L,0x0000000000002000L});
    public static final BitSet FOLLOW_INSTANCEOF_in_instanceOfExpression5678 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_instanceOfExpression5680 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5708 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_LTGT_in_relationalExpression5724 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5730 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_EQEQ_in_relationalExpression5744 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5750 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_LTEQ_in_relationalExpression5764 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5770 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_GTEQ_in_relationalExpression5784 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5790 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_LT_in_relationalExpression5804 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5812 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_GT_in_relationalExpression5826 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5834 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_IN_in_relationalExpression5848 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5856 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression5885 = new BitSet(new long[]{0x0000000000000002L,0x0000002800000000L});
    public static final BitSet FOLLOW_PLUS_in_additiveExpression5900 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression5906 = new BitSet(new long[]{0x0000000000000002L,0x0000002800000000L});
    public static final BitSet FOLLOW_SUB_in_additiveExpression5919 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression5926 = new BitSet(new long[]{0x0000000000000002L,0x0000002800000000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression5954 = new BitSet(new long[]{0x0000000000000002L,0x0000038000000000L});
    public static final BitSet FOLLOW_STAR_in_multiplicativeExpression5970 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression5977 = new BitSet(new long[]{0x0000000000000002L,0x0000038000000000L});
    public static final BitSet FOLLOW_SLASH_in_multiplicativeExpression5991 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression5997 = new BitSet(new long[]{0x0000000000000002L,0x0000038000000000L});
    public static final BitSet FOLLOW_PERCENT_in_multiplicativeExpression6011 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression6015 = new BitSet(new long[]{0x0000000000000002L,0x0000038000000000L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression6045 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryOperator_in_unaryExpression6056 = new BitSet(new long[]{0x0007800000000000L,0x5828000008A14002L,0x0000000000000002L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression6060 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primaryExpression_in_postfixExpression6080 = new BitSet(new long[]{0x0000000000000002L,0x0000000008800000L});
    public static final BitSet FOLLOW_DOT_in_postfixExpression6095 = new BitSet(new long[]{0x0000000000200000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_CLASS_in_postfixExpression6099 = new BitSet(new long[]{0x0000000000000002L,0x0000000008800000L});
    public static final BitSet FOLLOW_name_in_postfixExpression6123 = new BitSet(new long[]{0x0000000000000002L,0x0000000008A00000L});
    public static final BitSet FOLLOW_LPAREN_in_postfixExpression6148 = new BitSet(new long[]{0x4017900000000060L,0x582C007008EDC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expressionListOpt_in_postfixExpression6150 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_postfixExpression6152 = new BitSet(new long[]{0x0000000000000002L,0x0000000008A00000L});
    public static final BitSet FOLLOW_LBRACKET_in_postfixExpression6184 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_postfixExpression6187 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_BAR_in_postfixExpression6189 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_postfixExpression6193 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_postfixExpression6196 = new BitSet(new long[]{0x0000000000000002L,0x0000000008800000L});
    public static final BitSet FOLLOW_newExpression_in_primaryExpression6221 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeName_in_primaryExpression6233 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_primaryExpression6235 = new BitSet(new long[]{0x0000004200004000L,0x0A00000000000060L,0x0000000000000002L});
    public static final BitSet FOLLOW_objectLiteral_in_primaryExpression6238 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_primaryExpression6240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_bracketExpression_in_primaryExpression6250 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ordinalExpression_in_primaryExpression6265 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_contextExpression_in_primaryExpression6277 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THIS_in_primaryExpression6289 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_primaryExpression6308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression6327 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression6348 = new BitSet(new long[]{0x4017900000000060L,0x582C007008EDC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expressionListOpt_in_primaryExpression6352 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression6356 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_stringExpression_in_primaryExpression6375 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primaryExpression6393 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression6412 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_primaryExpression6414 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression6416 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_newExpression6448 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_typeName_in_newExpression6451 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_newExpression6459 = new BitSet(new long[]{0x4017900000000060L,0x582C007008EDC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expressionListOpt_in_newExpression6463 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_newExpression6467 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_objectLiteralPart_in_objectLiteral6507 = new BitSet(new long[]{0x0000004200004002L,0x0800000000000060L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_objectLiteralPart6533 = new BitSet(new long[]{0x0000000000000000L,0x0002000000000000L});
    public static final BitSet FOLLOW_COLON_in_objectLiteralPart6535 = new BitSet(new long[]{0x4017900060010060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_bindOpt_in_objectLiteralPart6538 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_objectLiteralPart6540 = new BitSet(new long[]{0x0000000000000002L,0x0000000006000000L});
    public static final BitSet FOLLOW_set_in_objectLiteralPart6542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_objectLiteralPart6562 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_objectLiteralPart6566 = new BitSet(new long[]{0x0000000000000000L,0x0002000020000000L});
    public static final BitSet FOLLOW_typeReference_in_objectLiteralPart6570 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_objectLiteralPart6574 = new BitSet(new long[]{0x4017900060010060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_bindOpt_in_objectLiteralPart6577 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_objectLiteralPart6579 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_objectLiteralPart6583 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localOperationDefinition_in_objectLiteralPart6595 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_objectLiteralPart6607 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localTriggerStatement_in_objectLiteralPart6619 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDefinition_in_objectLiteralPart6631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_bracketExpression6641 = new BitSet(new long[]{0x4017900000000060L,0x582C007009ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_bracketExpression6656 = new BitSet(new long[]{0x0000000000000090L,0x0000000005000000L});
    public static final BitSet FOLLOW_COMMA_in_bracketExpression6683 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_bracketExpression6685 = new BitSet(new long[]{0x0000000000000080L,0x0000000005000000L});
    public static final BitSet FOLLOW_DOTDOT_in_bracketExpression6699 = new BitSet(new long[]{0x4017900000000060L,0x582C007088ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_bracketExpression6703 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_bracketExpression6706 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_COMMA_in_bracketExpression6720 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_bracketExpression6724 = new BitSet(new long[]{0x0000000000000000L,0x0000000005000000L});
    public static final BitSet FOLLOW_BAR_in_bracketExpression6751 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_generator_in_bracketExpression6755 = new BitSet(new long[]{0x0000000000000000L,0x0000000005000000L});
    public static final BitSet FOLLOW_COMMA_in_bracketExpression6760 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_generator_in_bracketExpression6765 = new BitSet(new long[]{0x0000000000000000L,0x0000000005000000L});
    public static final BitSet FOLLOW_expression_in_bracketExpression6769 = new BitSet(new long[]{0x0000000000000000L,0x0000000005000000L});
    public static final BitSet FOLLOW_DOTDOT_in_bracketExpression6788 = new BitSet(new long[]{0x4017900000000060L,0x582C007088ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_bracketExpression6792 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_bracketExpression6796 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_bracketExpression6818 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_generator6828 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_LARROW_in_generator6832 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_generator6836 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INDEXOF_in_ordinalExpression6844 = new BitSet(new long[]{0x0000000000000000L,0x0800000008000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_ordinalExpression6852 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_ordinalExpression6860 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_contextExpression6872 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression6894 = new BitSet(new long[]{0x4017900000000060L,0x5C2C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression6903 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_stringExpression6914 = new BitSet(new long[]{0x0000000000000000L,0x0180000000000000L});
    public static final BitSet FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression6929 = new BitSet(new long[]{0x4017900000000060L,0x5C2C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression6941 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_stringExpression6955 = new BitSet(new long[]{0x0000000000000000L,0x0180000000000000L});
    public static final BitSet FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression6976 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull7006 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt7038 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_expressionListOpt7049 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt7055 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_ORDER_in_orderBy7077 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_BY_in_orderBy7081 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_orderBy7085 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INDEX_in_indexOn7100 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ON_in_indexOn7104 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_indexOn7108 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_indexOn7116 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_indexOn7120 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_set_in_multiplyOperator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_POUND_in_unaryOperator7164 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_unaryOperator7175 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUB_in_unaryOperator7188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_unaryOperator7201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIZEOF_in_unaryOperator7214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TYPEOF_in_unaryOperator7227 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REVERSE_in_unaryOperator7240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_unaryOperator7253 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_unaryOperator7266 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSEQ_in_assignmentOperator7287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBEQ_in_assignmentOperator7300 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAREQ_in_assignmentOperator7313 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SLASHEQ_in_assignmentOperator7326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENTEQ_in_assignmentOperator7339 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLON_in_typeReference7363 = new BitSet(new long[]{0x0000000000000000L,0x0800008000200060L,0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_typeReference7403 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_typeReference7412 = new BitSet(new long[]{0x0000000000000000L,0x0006008800800000L});
    public static final BitSet FOLLOW_typeReference_in_typeReference7414 = new BitSet(new long[]{0x0000000000000002L,0x0004008800800000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference7418 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeName_in_typeReference7473 = new BitSet(new long[]{0x0000000000000002L,0x0004008800800000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference7477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_typeReference7503 = new BitSet(new long[]{0x0000000000000002L,0x0004008800800000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference7507 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_cardinalityConstraint7538 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_cardinalityConstraint7542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_cardinalityConstraint7554 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUS_in_cardinalityConstraint7581 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_cardinalityConstraint7608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_literal7677 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_LITERAL_in_literal7687 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOATING_POINT_LITERAL_in_literal7697 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRUE_in_literal7707 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALSE_in_literal7721 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_literal7735 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualident_in_typeName7762 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_qualident7806 = new BitSet(new long[]{0x0000000000000002L,0x0000000008000000L});
    public static final BitSet FOLLOW_DOT_in_qualident7835 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_qualident7839 = new BitSet(new long[]{0x0000000000000002L,0x0000000008000000L});
    public static final BitSet FOLLOW_name_in_identifier7876 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_name7910 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_synpred453575 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localOperationDefinition_in_synpred463583 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_backgroundStatement_in_synpred473597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_laterStatement_in_synpred483612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifStatement_in_synpred503648 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred533691 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_synpred533695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forAlphaStatement_in_synpred583772 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forJoinStatement_in_synpred593788 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FPS_in_synpred804642 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred804646 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_synpred814660 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred814664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_synpred824678 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_IF_in_synpred824682 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred824686 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LINEAR_in_synpred864825 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EASEIN_in_synpred874833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EASEOUT_in_synpred884841 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EASEBOTH_in_synpred894849 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MOTION_in_synpred904857 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred904861 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FPS_in_synpred914875 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred914879 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_synpred924891 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred924895 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_synpred934909 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_IF_in_synpred934913 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred934917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeReference_in_synpred975008 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1055131 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_typeName_in_synpred1055137 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1055140 = new BitSet(new long[]{0x0007800000000060L,0x582C007008AD4003L,0x0000000000000002L});
    public static final BitSet FOLLOW_suffixedExpression_in_synpred1055144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeReference_in_synpred1085255 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeReference_in_synpred1095277 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMA_in_synpred1115347 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_selectionVar_in_synpred1115351 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_synpred1125365 = new BitSet(new long[]{0x4017900000000060L,0x582C007008ADC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred1125369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeName_in_synpred1436233 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_synpred1436235 = new BitSet(new long[]{0x0000004200004000L,0x0A00000000000060L,0x0000000000000002L});
    public static final BitSet FOLLOW_objectLiteral_in_synpred1436238 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_synpred1436240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_synpred1506327 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1506348 = new BitSet(new long[]{0x4017900000000060L,0x582C007008EDC063L,0x0000000000000002L});
    public static final BitSet FOLLOW_expressionListOpt_in_synpred1506352 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1506356 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_COMMA_in_synpred1777116 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_synpred1777120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred1977538 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred1977542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_synpred1987554 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUS_in_synpred1997581 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_synpred2007608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_synpred2067835 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_synpred2067839 = new BitSet(new long[]{0x0000000000000002L});

}