// $ANTLR 3.0.1 C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g 2007-10-12 11:03:39

package com.sun.tools.migrator.antlr;

import java.util.HashMap;
import java.util.Map;
import java.io.OutputStreamWriter;

import com.sun.tools.migrator.tree.MTTree.*;
import com.sun.tools.migrator.tree.*;

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
public class interpParser extends AbstractGeneratedParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "BAR", "POUND", "TYPEOF", "DOTDOT", "LARROW", "ABSTRACT", "AFTER", "AND", "AS", "ASSERT", "ATTRIBUTE", "BEFORE", "BIND", "BIBIND", "BREAK", "BY", "CATCH", "CLASS", "DELETE", "DISTINCT", "DO", "DUR", "EASEBOTH", "EASEIN", "EASEOUT", "TIE", "STAYS", "RETURN", "THROW", "VAR", "PACKAGE", "IMPORT", "FROM", "LATER", "TRIGGER", "ON", "INSERT", "INTO", "FIRST", "LAST", "IF", "THEN", "ELSE", "THIS", "NULL", "TRUE", "FALSE", "FOR", "UNITINTERVAL", "IN", "FPS", "WHILE", "CONTINUE", "LINEAR", "MOTION", "TRY", "FINALLY", "LAZY", "FOREACH", "WHERE", "NOT", "NEW", "PRIVATE", "PROTECTED", "PUBLIC", "OPERATION", "FUNCTION", "READONLY", "INVERSE", "TYPE", "EXTENDS", "ORDER", "INDEX", "INSTANCEOF", "INDEXOF", "SELECT", "SUPER", "OR", "SIZEOF", "REVERSE", "XOR", "LPAREN", "RPAREN", "LBRACKET", "RBRACKET", "SEMI", "COMMA", "DOT", "EQEQ", "EQ", "GT", "LT", "LTGT", "LTEQ", "GTEQ", "PLUS", "PLUSPLUS", "SUB", "SUBSUB", "STAR", "SLASH", "PERCENT", "PLUSEQ", "SUBEQ", "STAREQ", "SLASHEQ", "PERCENTEQ", "LTLT", "GTGT", "COLON", "QUES", "STRING_LITERAL", "NextIsPercent", "QUOTE_LBRACE_STRING_LITERAL", "LBRACE", "RBRACE_QUOTE_STRING_LITERAL", "RBRACE_LBRACE_STRING_LITERAL", "RBRACE", "FORMAT_STRING_LITERAL", "QUOTED_IDENTIFIER", "DECIMAL_LITERAL", "OCTAL_LITERAL", "HexDigit", "HEX_LITERAL", "RangeDots", "Digits", "Exponent", "FLOATING_POINT_LITERAL", "Letter", "JavaIDDigit", "IDENTIFIER", "WS", "COMMENT", "LINE_COMMENT"
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
    public static final int FLOATING_POINT_LITERAL=131;
    public static final int INSERT=40;
    public static final int SUBSUB=102;
    public static final int Digits=129;
    public static final int BIND=16;
    public static final int STAREQ=108;
    public static final int RETURN=31;
    public static final int THIS=47;
    public static final int VAR=33;
    public static final int SUPER=80;
    public static final int EQ=93;
    public static final int LAST=43;
    public static final int COMMENT=136;
    public static final int SELECT=79;
    public static final int INTO=41;
    public static final int QUES=114;
    public static final int EQEQ=92;
    public static final int MOTION=58;
    public static final int RBRACE=121;
    public static final int POUND=5;
    public static final int LINE_COMMENT=137;
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
    public static final int WS=135;
    public static final int RangeDots=128;
    public static final int TYPEOF=6;
    public static final int OR=81;
    public static final int JavaIDDigit=133;
    public static final int SIZEOF=82;
    public static final int GT=94;
    public static final int FOREACH=62;
    public static final int FROM=36;
    public static final int CATCH=20;
    public static final int OPERATION=69;
    public static final int REVERSE=83;
    public static final int FALSE=50;
    public static final int DISTINCT=23;
    public static final int Letter=132;
    public static final int DECIMAL_LITERAL=124;
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
    public static final int Exponent=130;
    public static final int LARROW=8;
    public static final int FOR=51;
    public static final int SUB=101;
    public static final int DOTDOT=7;
    public static final int ABSTRACT=9;
    public static final int NextIsPercent=116;
    public static final int AND=11;
    public static final int HexDigit=126;
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
    public static final int IDENTIFIER=134;
    public static final int QUOTE_LBRACE_STRING_LITERAL=117;
    public static final int PLUS=99;
    public static final int HEX_LITERAL=127;
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
    public static final int OCTAL_LITERAL=125;
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

        public interpParser(TokenStream input) {
            super(input);
            ruleMemo = new HashMap[302+1];
         }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g"; }

    
            public interpParser(Context context, char[] content) {
               this(new CommonTokenStream(new interpLexer(new ANTLRStringStream(content.toString()))));
               initialize(context);
        	}



    // $ANTLR start module
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:378:1: module returns [MTCompilationUnit result] : ( packageDecl )? moduleItems EOF ;
    public final MTCompilationUnit module() throws RecognitionException {
        MTCompilationUnit result = null;

        MTExpression packageDecl1 = null;

        ListBuffer<MTTree> moduleItems2 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:379:8: ( ( packageDecl )? moduleItems EOF )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:379:10: ( packageDecl )? moduleItems EOF
            {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:379:10: ( packageDecl )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==PACKAGE) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:0:0: packageDecl
                    {
                    pushFollow(FOLLOW_packageDecl_in_module2233);
                    packageDecl1=packageDecl();
                    _fsp--;
                    if (failed) return result;

                    }
                    break;

            }

            pushFollow(FOLLOW_moduleItems_in_module2236);
            moduleItems2=moduleItems();
            _fsp--;
            if (failed) return result;
            match(input,EOF,FOLLOW_EOF_in_module2238); if (failed) return result;
            if ( backtracking==0 ) {
               result = F.TopLevel(packageDecl1, moduleItems2.toList()); 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:381:1: packageDecl returns [MTExpression value] : PACKAGE qualident SEMI ;
    public final MTExpression packageDecl() throws RecognitionException {
        MTExpression value = null;

        MTExpression qualident3 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:382:8: ( PACKAGE qualident SEMI )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:382:10: PACKAGE qualident SEMI
            {
            match(input,PACKAGE,FOLLOW_PACKAGE_in_packageDecl2271); if (failed) return value;
            pushFollow(FOLLOW_qualident_in_packageDecl2273);
            qualident3=qualident();
            _fsp--;
            if (failed) return value;
            match(input,SEMI,FOLLOW_SEMI_in_packageDecl2275); if (failed) return value;
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:383:1: moduleItems returns [ListBuffer<MTTree> items = new ListBuffer<MTTree>()] : ( moduleItem )* ;
    public final ListBuffer<MTTree> moduleItems() throws RecognitionException {
        ListBuffer<MTTree> items =  new ListBuffer<MTTree>();

        MTTree moduleItem4 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:384:9: ( ( moduleItem )* )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:384:11: ( moduleItem )*
            {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:384:11: ( moduleItem )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=POUND && LA2_0<=TYPEOF)||LA2_0==ABSTRACT||LA2_0==ATTRIBUTE||LA2_0==BREAK||(LA2_0>=CLASS && LA2_0<=DELETE)||LA2_0==DO||(LA2_0>=RETURN && LA2_0<=VAR)||LA2_0==IMPORT||LA2_0==TRIGGER||LA2_0==INSERT||LA2_0==IF||(LA2_0>=THIS && LA2_0<=UNITINTERVAL)||(LA2_0>=WHILE && LA2_0<=CONTINUE)||LA2_0==TRY||LA2_0==FOREACH||(LA2_0>=NOT && LA2_0<=READONLY)||(LA2_0>=INDEXOF && LA2_0<=SUPER)||(LA2_0>=SIZEOF && LA2_0<=REVERSE)||LA2_0==LPAREN||LA2_0==LBRACKET||LA2_0==DOT||(LA2_0>=PLUSPLUS && LA2_0<=SUBSUB)||(LA2_0>=QUES && LA2_0<=STRING_LITERAL)||LA2_0==QUOTE_LBRACE_STRING_LITERAL||(LA2_0>=QUOTED_IDENTIFIER && LA2_0<=OCTAL_LITERAL)||LA2_0==HEX_LITERAL||LA2_0==FLOATING_POINT_LITERAL||LA2_0==IDENTIFIER) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:384:12: moduleItem
            	    {
            	    pushFollow(FOLLOW_moduleItem_in_moduleItems2304);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:385:1: moduleItem returns [MTTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );
    public final MTTree moduleItem() throws RecognitionException {
        MTTree value = null;

        MTTree importDecl5 = null;

        MTClassDeclaration classDefinition6 = null;

        MTStatement statementExcept7 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:386:8: ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept )
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

                if ( (LA3_4==LPAREN) ) {
                    alt3=7;
                }
                else if ( (LA3_4==QUOTED_IDENTIFIER||LA3_4==IDENTIFIER) ) {
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
                            new NoViableAltException("385:1: moduleItem returns [MTTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );", 3, 8, input);

                        throw nvae;
                    }
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("385:1: moduleItem returns [MTTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );", 3, 4, input);

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
                            new NoViableAltException("385:1: moduleItem returns [MTTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );", 3, 9, input);

                        throw nvae;
                    }
                }
                else if ( (LA3_5==LPAREN) ) {
                    alt3=7;
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("385:1: moduleItem returns [MTTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );", 3, 5, input);

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
            case DECIMAL_LITERAL:
            case OCTAL_LITERAL:
            case HEX_LITERAL:
            case FLOATING_POINT_LITERAL:
            case IDENTIFIER:
                {
                alt3=7;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("385:1: moduleItem returns [MTTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:386:10: importDecl
                    {
                    pushFollow(FOLLOW_importDecl_in_moduleItem2346);
                    importDecl5=importDecl();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = importDecl5; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:387:10: classDefinition
                    {
                    pushFollow(FOLLOW_classDefinition_in_moduleItem2361);
                    classDefinition6=classDefinition();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = classDefinition6; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:388:10: attributeDefinition
                    {
                    pushFollow(FOLLOW_attributeDefinition_in_moduleItem2376);
                    attributeDefinition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:389:10: memberOperationDefinition
                    {
                    pushFollow(FOLLOW_memberOperationDefinition_in_moduleItem2390);
                    memberOperationDefinition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:390:10: memberFunctionDefinition
                    {
                    pushFollow(FOLLOW_memberFunctionDefinition_in_moduleItem2403);
                    memberFunctionDefinition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:391:10: TRIGGER ON changeRule
                    {
                    match(input,TRIGGER,FOLLOW_TRIGGER_in_moduleItem2416); if (failed) return value;
                    match(input,ON,FOLLOW_ON_in_moduleItem2418); if (failed) return value;
                    pushFollow(FOLLOW_changeRule_in_moduleItem2420);
                    changeRule();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:392:10: statementExcept
                    {
                    pushFollow(FOLLOW_statementExcept_in_moduleItem2433);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:393:1: importDecl returns [MTTree value] : IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI ;
    public final MTTree importDecl() throws RecognitionException {
        MTTree value = null;

        Token STAR10=null;
        Token IMPORT11=null;
        MTIdent identifier8 = null;

        name_return name9 = null;


         MTExpression pid = null; 
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:395:9: ( IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:395:11: IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI
            {
            IMPORT11=(Token)input.LT(1);
            match(input,IMPORT,FOLLOW_IMPORT_in_importDecl2462); if (failed) return value;
            pushFollow(FOLLOW_identifier_in_importDecl2465);
            identifier8=identifier();
            _fsp--;
            if (failed) return value;
            if ( backtracking==0 ) {
               pid = identifier8; 
            }
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:396:18: ( DOT name )*
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
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:396:20: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_importDecl2489); if (failed) return value;
            	    pushFollow(FOLLOW_name_in_importDecl2491);
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

            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:397:18: ( DOT STAR )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==DOT) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:397:20: DOT STAR
                    {
                    match(input,DOT,FOLLOW_DOT_in_importDecl2519); if (failed) return value;
                    STAR10=(Token)input.LT(1);
                    match(input,STAR,FOLLOW_STAR_in_importDecl2521); if (failed) return value;
                    if ( backtracking==0 ) {
                       pid = F.at(pos(STAR10)).Select(pid, names.asterisk); 
                    }

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_importDecl2529); if (failed) return value;
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:399:1: classDefinition returns [MTClassDeclaration value] : modifierFlags CLASS name supers LBRACE classMembers RBRACE ;
    public final MTClassDeclaration classDefinition() throws RecognitionException {
        MTClassDeclaration value = null;

        Token CLASS12=null;
        MTModifiers modifierFlags13 = null;

        name_return name14 = null;

        ListBuffer<MTExpression> supers15 = null;

        ListBuffer<MTTree> classMembers16 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:400:2: ( modifierFlags CLASS name supers LBRACE classMembers RBRACE )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:400:4: modifierFlags CLASS name supers LBRACE classMembers RBRACE
            {
            pushFollow(FOLLOW_modifierFlags_in_classDefinition2555);
            modifierFlags13=modifierFlags();
            _fsp--;
            if (failed) return value;
            CLASS12=(Token)input.LT(1);
            match(input,CLASS,FOLLOW_CLASS_in_classDefinition2558); if (failed) return value;
            pushFollow(FOLLOW_name_in_classDefinition2560);
            name14=name();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_supers_in_classDefinition2562);
            supers15=supers();
            _fsp--;
            if (failed) return value;
            match(input,LBRACE,FOLLOW_LBRACE_in_classDefinition2564); if (failed) return value;
            pushFollow(FOLLOW_classMembers_in_classDefinition2566);
            classMembers16=classMembers();
            _fsp--;
            if (failed) return value;
            match(input,RBRACE,FOLLOW_RBRACE_in_classDefinition2568); if (failed) return value;
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:406:1: supers returns [ListBuffer<MTExpression> ids = new ListBuffer<MTExpression>()] : ( EXTENDS id1= qualident ( COMMA idn= qualident )* )? ;
    public final ListBuffer<MTExpression> supers() throws RecognitionException {
        ListBuffer<MTExpression> ids =  new ListBuffer<MTExpression>();

        MTExpression id1 = null;

        MTExpression idn = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:407:2: ( ( EXTENDS id1= qualident ( COMMA idn= qualident )* )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:407:4: ( EXTENDS id1= qualident ( COMMA idn= qualident )* )?
            {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:407:4: ( EXTENDS id1= qualident ( COMMA idn= qualident )* )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==EXTENDS) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:407:5: EXTENDS id1= qualident ( COMMA idn= qualident )*
                    {
                    match(input,EXTENDS,FOLLOW_EXTENDS_in_supers2594); if (failed) return ids;
                    pushFollow(FOLLOW_qualident_in_supers2598);
                    id1=qualident();
                    _fsp--;
                    if (failed) return ids;
                    if ( backtracking==0 ) {
                       ids.append(id1); 
                    }
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:408:12: ( COMMA idn= qualident )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==COMMA) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:408:14: COMMA idn= qualident
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_supers2622); if (failed) return ids;
                    	    pushFollow(FOLLOW_qualident_in_supers2626);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:411:1: classMembers returns [ListBuffer<MTTree> mems = new ListBuffer<MTTree>()] : ( attributeDecl | functionDecl | operationDecl )* ;
    public final ListBuffer<MTTree> classMembers() throws RecognitionException {
        ListBuffer<MTTree> mems =  new ListBuffer<MTTree>();

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:412:2: ( ( attributeDecl | functionDecl | operationDecl )* )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:412:3: ( attributeDecl | functionDecl | operationDecl )*
            {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:412:3: ( attributeDecl | functionDecl | operationDecl )*
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
                    case PRIVATE:
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
                    case PROTECTED:
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
                case READONLY:
                    {
                    switch ( input.LA(2) ) {
                    case PUBLIC:
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
                    case PRIVATE:
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
                    case PROTECTED:
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
                case PUBLIC:
                    {
                    switch ( input.LA(2) ) {
                    case ABSTRACT:
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
                    case READONLY:
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
                case PRIVATE:
                    {
                    switch ( input.LA(2) ) {
                    case ABSTRACT:
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
                    case READONLY:
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
                    switch ( input.LA(2) ) {
                    case ABSTRACT:
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
                    case READONLY:
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
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:412:5: attributeDecl
            	    {
            	    pushFollow(FOLLOW_attributeDecl_in_classMembers2660);
            	    attributeDecl();
            	    _fsp--;
            	    if (failed) return mems;

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:413:5: functionDecl
            	    {
            	    pushFollow(FOLLOW_functionDecl_in_classMembers2678);
            	    functionDecl();
            	    _fsp--;
            	    if (failed) return mems;

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:414:5: operationDecl
            	    {
            	    pushFollow(FOLLOW_operationDecl_in_classMembers2697);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:416:1: attributeDecl : modifierFlags ATTRIBUTE name typeReference inverseClause ( orderBy | indexOn )? SEMI ;
    public final void attributeDecl() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:417:2: ( modifierFlags ATTRIBUTE name typeReference inverseClause ( orderBy | indexOn )? SEMI )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:417:4: modifierFlags ATTRIBUTE name typeReference inverseClause ( orderBy | indexOn )? SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_attributeDecl2725);
            modifierFlags();
            _fsp--;
            if (failed) return ;
            match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeDecl2727); if (failed) return ;
            pushFollow(FOLLOW_name_in_attributeDecl2729);
            name();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_typeReference_in_attributeDecl2731);
            typeReference();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_inverseClause_in_attributeDecl2733);
            inverseClause();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:417:62: ( orderBy | indexOn )?
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
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:417:63: orderBy
                    {
                    pushFollow(FOLLOW_orderBy_in_attributeDecl2737);
                    orderBy();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:417:73: indexOn
                    {
                    pushFollow(FOLLOW_indexOn_in_attributeDecl2741);
                    indexOn();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_attributeDecl2745); if (failed) return ;

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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:419:1: inverseClause returns [MTMemberSelector inverse = null] : ( INVERSE memberSelector )? ;
    public final MTMemberSelector inverseClause() throws RecognitionException {
        MTMemberSelector inverse =  null;

        MTMemberSelector memberSelector17 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:420:2: ( ( INVERSE memberSelector )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:420:4: ( INVERSE memberSelector )?
            {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:420:4: ( INVERSE memberSelector )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==INVERSE) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:420:5: INVERSE memberSelector
                    {
                    match(input,INVERSE,FOLLOW_INVERSE_in_inverseClause2763); if (failed) return inverse;
                    pushFollow(FOLLOW_memberSelector_in_inverseClause2765);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:421:1: functionDecl : modifierFlags FUNCTION name formalParameters typeReference SEMI ;
    public final void functionDecl() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:422:2: ( modifierFlags FUNCTION name formalParameters typeReference SEMI )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:422:4: modifierFlags FUNCTION name formalParameters typeReference SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_functionDecl2780);
            modifierFlags();
            _fsp--;
            if (failed) return ;
            match(input,FUNCTION,FOLLOW_FUNCTION_in_functionDecl2782); if (failed) return ;
            pushFollow(FOLLOW_name_in_functionDecl2784);
            name();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_formalParameters_in_functionDecl2786);
            formalParameters();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_typeReference_in_functionDecl2788);
            typeReference();
            _fsp--;
            if (failed) return ;
            match(input,SEMI,FOLLOW_SEMI_in_functionDecl2790); if (failed) return ;

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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:425:1: operationDecl : modifierFlags OPERATION name formalParameters typeReference SEMI ;
    public final void operationDecl() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:426:2: ( modifierFlags OPERATION name formalParameters typeReference SEMI )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:426:4: modifierFlags OPERATION name formalParameters typeReference SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_operationDecl2805);
            modifierFlags();
            _fsp--;
            if (failed) return ;
            match(input,OPERATION,FOLLOW_OPERATION_in_operationDecl2809); if (failed) return ;
            pushFollow(FOLLOW_name_in_operationDecl2813);
            name();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_formalParameters_in_operationDecl2817);
            formalParameters();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_typeReference_in_operationDecl2821);
            typeReference();
            _fsp--;
            if (failed) return ;
            match(input,SEMI,FOLLOW_SEMI_in_operationDecl2826); if (failed) return ;

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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:428:1: attributeDefinition : ATTRIBUTE memberSelector EQ bindOpt expression SEMI ;
    public final void attributeDefinition() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:429:2: ( ATTRIBUTE memberSelector EQ bindOpt expression SEMI )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:429:4: ATTRIBUTE memberSelector EQ bindOpt expression SEMI
            {
            match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeDefinition2841); if (failed) return ;
            pushFollow(FOLLOW_memberSelector_in_attributeDefinition2845);
            memberSelector();
            _fsp--;
            if (failed) return ;
            match(input,EQ,FOLLOW_EQ_in_attributeDefinition2849); if (failed) return ;
            pushFollow(FOLLOW_bindOpt_in_attributeDefinition2851);
            bindOpt();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_expression_in_attributeDefinition2854);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,SEMI,FOLLOW_SEMI_in_attributeDefinition2858); if (failed) return ;

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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:431:1: memberOperationDefinition : OPERATION memberSelector formalParameters typeReference block ;
    public final void memberOperationDefinition() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:432:2: ( OPERATION memberSelector formalParameters typeReference block )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:432:4: OPERATION memberSelector formalParameters typeReference block
            {
            match(input,OPERATION,FOLLOW_OPERATION_in_memberOperationDefinition2872); if (failed) return ;
            pushFollow(FOLLOW_memberSelector_in_memberOperationDefinition2876);
            memberSelector();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_formalParameters_in_memberOperationDefinition2880);
            formalParameters();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_typeReference_in_memberOperationDefinition2884);
            typeReference();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_block_in_memberOperationDefinition2887);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:434:1: memberFunctionDefinition : FUNCTION memberSelector formalParameters typeReference block ;
    public final void memberFunctionDefinition() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:435:2: ( FUNCTION memberSelector formalParameters typeReference block )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:435:4: FUNCTION memberSelector formalParameters typeReference block
            {
            match(input,FUNCTION,FOLLOW_FUNCTION_in_memberFunctionDefinition2902); if (failed) return ;
            pushFollow(FOLLOW_memberSelector_in_memberFunctionDefinition2906);
            memberSelector();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_formalParameters_in_memberFunctionDefinition2910);
            formalParameters();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_typeReference_in_memberFunctionDefinition2914);
            typeReference();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_block_in_memberFunctionDefinition2917);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:437:1: functionBody : ( EQ expression ( whereVarDecls )? SEMI | LBRACE ( variableDefinition | localFunctionDefinition | localOperationDefinition )* RETURN expression ( SEMI )? RBRACE );
    public final void functionBody() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:438:2: ( EQ expression ( whereVarDecls )? SEMI | LBRACE ( variableDefinition | localFunctionDefinition | localOperationDefinition )* RETURN expression ( SEMI )? RBRACE )
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
                    new NoViableAltException("437:1: functionBody : ( EQ expression ( whereVarDecls )? SEMI | LBRACE ( variableDefinition | localFunctionDefinition | localOperationDefinition )* RETURN expression ( SEMI )? RBRACE );", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:438:4: EQ expression ( whereVarDecls )? SEMI
                    {
                    match(input,EQ,FOLLOW_EQ_in_functionBody2932); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_functionBody2936);
                    expression();
                    _fsp--;
                    if (failed) return ;
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:438:22: ( whereVarDecls )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==WHERE) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:0:0: whereVarDecls
                            {
                            pushFollow(FOLLOW_whereVarDecls_in_functionBody2940);
                            whereVarDecls();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }

                    match(input,SEMI,FOLLOW_SEMI_in_functionBody2946); if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:439:11: LBRACE ( variableDefinition | localFunctionDefinition | localOperationDefinition )* RETURN expression ( SEMI )? RBRACE
                    {
                    match(input,LBRACE,FOLLOW_LBRACE_in_functionBody2962); if (failed) return ;
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:439:20: ( variableDefinition | localFunctionDefinition | localOperationDefinition )*
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
                    	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:439:24: variableDefinition
                    	    {
                    	    pushFollow(FOLLOW_variableDefinition_in_functionBody2970);
                    	    variableDefinition();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;
                    	case 2 :
                    	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:439:49: localFunctionDefinition
                    	    {
                    	    pushFollow(FOLLOW_localFunctionDefinition_in_functionBody2978);
                    	    localFunctionDefinition();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;
                    	case 3 :
                    	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:439:79: localOperationDefinition
                    	    {
                    	    pushFollow(FOLLOW_localOperationDefinition_in_functionBody2986);
                    	    localOperationDefinition();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);

                    match(input,RETURN,FOLLOW_RETURN_in_functionBody2996); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_functionBody3000);
                    expression();
                    _fsp--;
                    if (failed) return ;
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:439:134: ( SEMI )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==SEMI) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:0:0: SEMI
                            {
                            match(input,SEMI,FOLLOW_SEMI_in_functionBody3004); if (failed) return ;

                            }
                            break;

                    }

                    match(input,RBRACE,FOLLOW_RBRACE_in_functionBody3010); if (failed) return ;

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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:440:1: whereVarDecls : WHERE whereVarDecl ( COMMA whereVarDecl )* ;
    public final void whereVarDecls() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:440:15: ( WHERE whereVarDecl ( COMMA whereVarDecl )* )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:440:17: WHERE whereVarDecl ( COMMA whereVarDecl )*
            {
            match(input,WHERE,FOLLOW_WHERE_in_whereVarDecls3018); if (failed) return ;
            pushFollow(FOLLOW_whereVarDecl_in_whereVarDecls3022);
            whereVarDecl();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:440:40: ( COMMA whereVarDecl )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==COMMA) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:440:44: COMMA whereVarDecl
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_whereVarDecls3030); if (failed) return ;
            	    pushFollow(FOLLOW_whereVarDecl_in_whereVarDecls3034);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:441:1: whereVarDecl : ( localFunctionDefinition | name typeReference EQ expression );
    public final void whereVarDecl() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:441:14: ( localFunctionDefinition | name typeReference EQ expression )
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
                        new NoViableAltException("441:1: whereVarDecl : ( localFunctionDefinition | name typeReference EQ expression );", 16, 2, input);

                    throw nvae;
                }
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("441:1: whereVarDecl : ( localFunctionDefinition | name typeReference EQ expression );", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:441:16: localFunctionDefinition
                    {
                    pushFollow(FOLLOW_localFunctionDefinition_in_whereVarDecl3048);
                    localFunctionDefinition();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:442:10: name typeReference EQ expression
                    {
                    pushFollow(FOLLOW_name_in_whereVarDecl3060);
                    name();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_typeReference_in_whereVarDecl3064);
                    typeReference();
                    _fsp--;
                    if (failed) return ;
                    match(input,EQ,FOLLOW_EQ_in_whereVarDecl3068); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_whereVarDecl3072);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:443:1: variableDefinition : VAR name typeReference EQ expression SEMI ;
    public final void variableDefinition() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:443:20: ( VAR name typeReference EQ expression SEMI )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:443:22: VAR name typeReference EQ expression SEMI
            {
            match(input,VAR,FOLLOW_VAR_in_variableDefinition3080); if (failed) return ;
            pushFollow(FOLLOW_name_in_variableDefinition3084);
            name();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_typeReference_in_variableDefinition3088);
            typeReference();
            _fsp--;
            if (failed) return ;
            match(input,EQ,FOLLOW_EQ_in_variableDefinition3091); if (failed) return ;
            pushFollow(FOLLOW_expression_in_variableDefinition3095);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,SEMI,FOLLOW_SEMI_in_variableDefinition3099); if (failed) return ;

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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:444:1: changeRule : ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );
    public final void changeRule() throws RecognitionException {
        MTIdent id1 = null;

        MTIdent id2 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:445:2: ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block )
            int alt17=7;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==LPAREN) ) {
                switch ( input.LA(2) ) {
                case DELETE:
                    {
                    int LA17_3 = input.LA(3);

                    if ( (LA17_3==QUOTED_IDENTIFIER||LA17_3==IDENTIFIER) ) {
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
                                new NoViableAltException("444:1: changeRule : ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 7, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("444:1: changeRule : ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 3, input);

                        throw nvae;
                    }
                    }
                    break;
                case NEW:
                    {
                    alt17=1;
                    }
                    break;
                case INSERT:
                    {
                    alt17=5;
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
                                if (backtracking>0) {failed=true; return ;}
                                NoViableAltException nvae =
                                    new NoViableAltException("444:1: changeRule : ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 11, input);

                                throw nvae;
                            }
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("444:1: changeRule : ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 8, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("444:1: changeRule : ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 6, input);

                        throw nvae;
                    }
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("444:1: changeRule : ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA17_0==QUOTED_IDENTIFIER||LA17_0==IDENTIFIER) ) {
                alt17=3;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("444:1: changeRule : ( LPAREN NEW typeName RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:445:4: LPAREN NEW typeName RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule3110); if (failed) return ;
                    match(input,NEW,FOLLOW_NEW_in_changeRule3114); if (failed) return ;
                    pushFollow(FOLLOW_typeName_in_changeRule3118);
                    typeName();
                    _fsp--;
                    if (failed) return ;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule3121); if (failed) return ;
                    pushFollow(FOLLOW_block_in_changeRule3124);
                    block();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:446:4: LPAREN memberSelector EQ identifier RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule3129); if (failed) return ;
                    pushFollow(FOLLOW_memberSelector_in_changeRule3133);
                    memberSelector();
                    _fsp--;
                    if (failed) return ;
                    match(input,EQ,FOLLOW_EQ_in_changeRule3136); if (failed) return ;
                    pushFollow(FOLLOW_identifier_in_changeRule3138);
                    identifier();
                    _fsp--;
                    if (failed) return ;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule3142); if (failed) return ;
                    pushFollow(FOLLOW_block_in_changeRule3145);
                    block();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:447:4: memberSelector EQ identifier block
                    {
                    pushFollow(FOLLOW_memberSelector_in_changeRule3150);
                    memberSelector();
                    _fsp--;
                    if (failed) return ;
                    match(input,EQ,FOLLOW_EQ_in_changeRule3153); if (failed) return ;
                    pushFollow(FOLLOW_identifier_in_changeRule3155);
                    identifier();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_block_in_changeRule3157);
                    block();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:448:4: LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule3162); if (failed) return ;
                    pushFollow(FOLLOW_memberSelector_in_changeRule3166);
                    memberSelector();
                    _fsp--;
                    if (failed) return ;
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_changeRule3170); if (failed) return ;
                    pushFollow(FOLLOW_identifier_in_changeRule3176);
                    id1=identifier();
                    _fsp--;
                    if (failed) return ;
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_changeRule3180); if (failed) return ;
                    match(input,EQ,FOLLOW_EQ_in_changeRule3184); if (failed) return ;
                    pushFollow(FOLLOW_identifier_in_changeRule3188);
                    id2=identifier();
                    _fsp--;
                    if (failed) return ;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule3192); if (failed) return ;
                    pushFollow(FOLLOW_block_in_changeRule3195);
                    block();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:449:4: LPAREN INSERT identifier INTO memberSelector RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule3200); if (failed) return ;
                    match(input,INSERT,FOLLOW_INSERT_in_changeRule3204); if (failed) return ;
                    pushFollow(FOLLOW_identifier_in_changeRule3208);
                    identifier();
                    _fsp--;
                    if (failed) return ;
                    match(input,INTO,FOLLOW_INTO_in_changeRule3212); if (failed) return ;
                    pushFollow(FOLLOW_memberSelector_in_changeRule3216);
                    memberSelector();
                    _fsp--;
                    if (failed) return ;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule3220); if (failed) return ;
                    pushFollow(FOLLOW_block_in_changeRule3222);
                    block();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:450:4: LPAREN DELETE identifier FROM memberSelector RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule3229); if (failed) return ;
                    match(input,DELETE,FOLLOW_DELETE_in_changeRule3233); if (failed) return ;
                    pushFollow(FOLLOW_identifier_in_changeRule3237);
                    identifier();
                    _fsp--;
                    if (failed) return ;
                    match(input,FROM,FOLLOW_FROM_in_changeRule3241); if (failed) return ;
                    pushFollow(FOLLOW_memberSelector_in_changeRule3245);
                    memberSelector();
                    _fsp--;
                    if (failed) return ;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule3249); if (failed) return ;
                    pushFollow(FOLLOW_block_in_changeRule3251);
                    block();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:451:4: LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule3258); if (failed) return ;
                    match(input,DELETE,FOLLOW_DELETE_in_changeRule3262); if (failed) return ;
                    pushFollow(FOLLOW_memberSelector_in_changeRule3265);
                    memberSelector();
                    _fsp--;
                    if (failed) return ;
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_changeRule3269); if (failed) return ;
                    pushFollow(FOLLOW_identifier_in_changeRule3273);
                    identifier();
                    _fsp--;
                    if (failed) return ;
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_changeRule3277); if (failed) return ;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule3281); if (failed) return ;
                    pushFollow(FOLLOW_block_in_changeRule3283);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:453:1: modifierFlags returns [MTModifiers mods] : (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? ;
    public final MTModifiers modifierFlags() throws RecognitionException {
        MTModifiers mods = null;

        long om1 = 0;

        long am1 = 0;

        long am2 = 0;

        long om2 = 0;


         long flags = 0; 
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:455:2: ( (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:455:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
            {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:455:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
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
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:455:6: om1= otherModifier (am1= accessModifier )?
                    {
                    pushFollow(FOLLOW_otherModifier_in_modifierFlags3306);
                    om1=otherModifier();
                    _fsp--;
                    if (failed) return mods;
                    if ( backtracking==0 ) {
                       flags |= om1; 
                    }
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:456:3: (am1= accessModifier )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( ((LA18_0>=PRIVATE && LA18_0<=PUBLIC)) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:456:5: am1= accessModifier
                            {
                            pushFollow(FOLLOW_accessModifier_in_modifierFlags3319);
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
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:457:6: am2= accessModifier (om2= otherModifier )?
                    {
                    pushFollow(FOLLOW_accessModifier_in_modifierFlags3341);
                    am2=accessModifier();
                    _fsp--;
                    if (failed) return mods;
                    if ( backtracking==0 ) {
                       flags |= am2; 
                    }
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:458:3: (om2= otherModifier )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==ABSTRACT||LA19_0==READONLY) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:458:5: om2= otherModifier
                            {
                            pushFollow(FOLLOW_otherModifier_in_modifierFlags3354);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:461:1: accessModifier returns [long flags = 0] : ( PUBLIC | PRIVATE | PROTECTED ) ;
    public final long accessModifier() throws RecognitionException {
        long flags =  0;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:462:2: ( ( PUBLIC | PRIVATE | PROTECTED ) )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:462:4: ( PUBLIC | PRIVATE | PROTECTED )
            {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:462:4: ( PUBLIC | PRIVATE | PROTECTED )
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
                    new NoViableAltException("462:4: ( PUBLIC | PRIVATE | PROTECTED )", 21, 0, input);

                throw nvae;
            }

            switch (alt21) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:462:5: PUBLIC
                    {
                    match(input,PUBLIC,FOLLOW_PUBLIC_in_accessModifier3402); if (failed) return flags;
                    if ( backtracking==0 ) {
                       flags |= Flags.PUBLIC; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:463:5: PRIVATE
                    {
                    match(input,PRIVATE,FOLLOW_PRIVATE_in_accessModifier3419); if (failed) return flags;
                    if ( backtracking==0 ) {
                       flags |= Flags.PUBLIC; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:464:5: PROTECTED
                    {
                    match(input,PROTECTED,FOLLOW_PROTECTED_in_accessModifier3435); if (failed) return flags;
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:465:1: otherModifier returns [long flags = 0] : ( ABSTRACT | READONLY ) ;
    public final long otherModifier() throws RecognitionException {
        long flags =  0;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:466:2: ( ( ABSTRACT | READONLY ) )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:466:4: ( ABSTRACT | READONLY )
            {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:466:4: ( ABSTRACT | READONLY )
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
                    new NoViableAltException("466:4: ( ABSTRACT | READONLY )", 22, 0, input);

                throw nvae;
            }
            switch (alt22) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:466:5: ABSTRACT
                    {
                    match(input,ABSTRACT,FOLLOW_ABSTRACT_in_otherModifier3459); if (failed) return flags;
                    if ( backtracking==0 ) {
                       flags |= Flags.ABSTRACT; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:467:5: READONLY
                    {
                    match(input,READONLY,FOLLOW_READONLY_in_otherModifier3474); if (failed) return flags;
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:468:1: memberSelector returns [MTMemberSelector value] : name1= name DOT name2= name ;
    public final MTMemberSelector memberSelector() throws RecognitionException {
        MTMemberSelector value = null;

        name_return name1 = null;

        name_return name2 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:469:2: (name1= name DOT name2= name )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:469:4: name1= name DOT name2= name
            {
            pushFollow(FOLLOW_name_in_memberSelector3500);
            name1=name();
            _fsp--;
            if (failed) return value;
            match(input,DOT,FOLLOW_DOT_in_memberSelector3504); if (failed) return value;
            pushFollow(FOLLOW_name_in_memberSelector3510);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:470:1: formalParameters returns [ListBuffer<MTTree> params = new ListBuffer<MTTree>()] : LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN ;
    public final ListBuffer<MTTree> formalParameters() throws RecognitionException {
        ListBuffer<MTTree> params =  new ListBuffer<MTTree>();

        MTVar fp0 = null;

        MTVar fpn = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:471:2: ( LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:471:4: LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_formalParameters3526); if (failed) return params;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:471:13: (fp0= formalParameter ( COMMA fpn= formalParameter )* )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==QUOTED_IDENTIFIER||LA24_0==IDENTIFIER) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:471:15: fp0= formalParameter ( COMMA fpn= formalParameter )*
                    {
                    pushFollow(FOLLOW_formalParameter_in_formalParameters3534);
                    fp0=formalParameter();
                    _fsp--;
                    if (failed) return params;
                    if ( backtracking==0 ) {
                       params.append(fp0); 
                    }
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:472:13: ( COMMA fpn= formalParameter )*
                    loop23:
                    do {
                        int alt23=2;
                        int LA23_0 = input.LA(1);

                        if ( (LA23_0==COMMA) ) {
                            alt23=1;
                        }


                        switch (alt23) {
                    	case 1 :
                    	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:472:15: COMMA fpn= formalParameter
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_formalParameters3553); if (failed) return params;
                    	    pushFollow(FOLLOW_formalParameter_in_formalParameters3559);
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

            match(input,RPAREN,FOLLOW_RPAREN_in_formalParameters3570); if (failed) return params;

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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:473:1: formalParameter returns [MTVar var] : name typeReference ;
    public final MTVar formalParameter() throws RecognitionException {
        MTVar var = null;

        name_return name18 = null;

        MTType typeReference19 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:474:2: ( name typeReference )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:474:4: name typeReference
            {
            pushFollow(FOLLOW_name_in_formalParameter3583);
            name18=name();
            _fsp--;
            if (failed) return var;
            pushFollow(FOLLOW_typeReference_in_formalParameter3585);
            typeReference19=typeReference();
            _fsp--;
            if (failed) return var;
            if ( backtracking==0 ) {
               var = F.at(name18.pos).Var(name18.value, typeReference19, F.Modifiers(Flags.PARAMETER), null, JavafxBindStatus.UNBOUND); 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:475:1: block returns [MTBlock value] : LBRACE statements RBRACE ;
    public final MTBlock block() throws RecognitionException {
        MTBlock value = null;

        Token LBRACE20=null;
        ListBuffer<MTStatement> statements21 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:476:2: ( LBRACE statements RBRACE )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:476:4: LBRACE statements RBRACE
            {
            LBRACE20=(Token)input.LT(1);
            match(input,LBRACE,FOLLOW_LBRACE_in_block3602); if (failed) return value;
            pushFollow(FOLLOW_statements_in_block3606);
            statements21=statements();
            _fsp--;
            if (failed) return value;
            match(input,RBRACE,FOLLOW_RBRACE_in_block3610); if (failed) return value;
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:478:1: statements returns [ListBuffer<MTStatement> stats = new ListBuffer<MTStatement>()] : ( statement )* ;
    public final ListBuffer<MTStatement> statements() throws RecognitionException {
        ListBuffer<MTStatement> stats =  new ListBuffer<MTStatement>();

        MTStatement statement22 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:479:2: ( ( statement )* )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:479:4: ( statement )*
            {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:479:4: ( statement )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( ((LA25_0>=POUND && LA25_0<=TYPEOF)||LA25_0==BREAK||LA25_0==DELETE||LA25_0==DO||(LA25_0>=RETURN && LA25_0<=VAR)||LA25_0==TRIGGER||LA25_0==INSERT||LA25_0==IF||(LA25_0>=THIS && LA25_0<=UNITINTERVAL)||(LA25_0>=WHILE && LA25_0<=CONTINUE)||LA25_0==TRY||LA25_0==FOREACH||(LA25_0>=NOT && LA25_0<=NEW)||(LA25_0>=OPERATION && LA25_0<=FUNCTION)||(LA25_0>=INDEXOF && LA25_0<=SUPER)||(LA25_0>=SIZEOF && LA25_0<=REVERSE)||LA25_0==LPAREN||LA25_0==LBRACKET||LA25_0==DOT||(LA25_0>=PLUSPLUS && LA25_0<=SUBSUB)||(LA25_0>=QUES && LA25_0<=STRING_LITERAL)||LA25_0==QUOTE_LBRACE_STRING_LITERAL||(LA25_0>=QUOTED_IDENTIFIER && LA25_0<=OCTAL_LITERAL)||LA25_0==HEX_LITERAL||LA25_0==FLOATING_POINT_LITERAL||LA25_0==IDENTIFIER) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:479:5: statement
            	    {
            	    pushFollow(FOLLOW_statement_in_statements3628);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:480:1: statement returns [MTStatement value] : ( statementExcept | localTriggerStatement );
    public final MTStatement statement() throws RecognitionException {
        MTStatement value = null;

        MTStatement statementExcept23 = null;

        MTStatement localTriggerStatement24 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:481:8: ( statementExcept | localTriggerStatement )
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( ((LA26_0>=POUND && LA26_0<=TYPEOF)||LA26_0==BREAK||LA26_0==DELETE||LA26_0==DO||(LA26_0>=RETURN && LA26_0<=VAR)||LA26_0==INSERT||LA26_0==IF||(LA26_0>=THIS && LA26_0<=UNITINTERVAL)||(LA26_0>=WHILE && LA26_0<=CONTINUE)||LA26_0==TRY||LA26_0==FOREACH||(LA26_0>=NOT && LA26_0<=NEW)||(LA26_0>=OPERATION && LA26_0<=FUNCTION)||(LA26_0>=INDEXOF && LA26_0<=SUPER)||(LA26_0>=SIZEOF && LA26_0<=REVERSE)||LA26_0==LPAREN||LA26_0==LBRACKET||LA26_0==DOT||(LA26_0>=PLUSPLUS && LA26_0<=SUBSUB)||(LA26_0>=QUES && LA26_0<=STRING_LITERAL)||LA26_0==QUOTE_LBRACE_STRING_LITERAL||(LA26_0>=QUOTED_IDENTIFIER && LA26_0<=OCTAL_LITERAL)||LA26_0==HEX_LITERAL||LA26_0==FLOATING_POINT_LITERAL||LA26_0==IDENTIFIER) ) {
                alt26=1;
            }
            else if ( (LA26_0==TRIGGER) ) {
                alt26=2;
            }
            else {
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("480:1: statement returns [MTStatement value] : ( statementExcept | localTriggerStatement );", 26, 0, input);

                throw nvae;
            }
            switch (alt26) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:481:10: statementExcept
                    {
                    pushFollow(FOLLOW_statementExcept_in_statement3679);
                    statementExcept23=statementExcept();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = statementExcept23; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:482:10: localTriggerStatement
                    {
                    pushFollow(FOLLOW_localTriggerStatement_in_statement3695);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:483:1: statementExcept returns [MTStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );
    public final MTStatement statementExcept() throws RecognitionException {
        MTStatement value = null;

        Token WHILE28=null;
        Token SEMI34=null;
        Token BREAK36=null;
        Token CONTINUE37=null;
        MTStatement variableDeclaration25 = null;

        MTStatement localFunctionDefinition26 = null;

        MTStatement localOperationDefinition27 = null;

        MTExpression expression29 = null;

        MTBlock block30 = null;

        MTStatement ifStatement31 = null;

        MTStatement insertStatement32 = null;

        MTStatement deleteStatement33 = null;

        MTExpression expression35 = null;

        MTStatement throwStatement38 = null;

        MTStatement returnStatement39 = null;

        MTStatement forAlphaStatement40 = null;

        MTStatement forJoinStatement41 = null;

        MTStatement tryStatement42 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:484:2: ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement )
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
                        new NoViableAltException("483:1: statementExcept returns [MTStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 2, input);

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
                        new NoViableAltException("483:1: statementExcept returns [MTStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 3, input);

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
                        new NoViableAltException("483:1: statementExcept returns [MTStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 4, input);

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
                        new NoViableAltException("483:1: statementExcept returns [MTStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 5, input);

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
                        new NoViableAltException("483:1: statementExcept returns [MTStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 7, input);

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
            case DECIMAL_LITERAL:
            case OCTAL_LITERAL:
            case HEX_LITERAL:
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
                int LA27_42 = input.LA(2);

                if ( (synpred58()) ) {
                    alt27=15;
                }
                else if ( (synpred59()) ) {
                    alt27=16;
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("483:1: statementExcept returns [MTStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 42, input);

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
                    new NoViableAltException("483:1: statementExcept returns [MTStatement value] : ( variableDeclaration | localFunctionDefinition | localOperationDefinition | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 0, input);

                throw nvae;
            }

            switch (alt27) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:484:4: variableDeclaration
                    {
                    pushFollow(FOLLOW_variableDeclaration_in_statementExcept3713);
                    variableDeclaration25=variableDeclaration();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = variableDeclaration25; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:485:4: localFunctionDefinition
                    {
                    pushFollow(FOLLOW_localFunctionDefinition_in_statementExcept3723);
                    localFunctionDefinition26=localFunctionDefinition();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = localFunctionDefinition26; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:486:4: localOperationDefinition
                    {
                    pushFollow(FOLLOW_localOperationDefinition_in_statementExcept3731);
                    localOperationDefinition27=localOperationDefinition();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = localOperationDefinition27; 
                    }

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:487:10: backgroundStatement
                    {
                    pushFollow(FOLLOW_backgroundStatement_in_statementExcept3745);
                    backgroundStatement();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:488:10: laterStatement
                    {
                    pushFollow(FOLLOW_laterStatement_in_statementExcept3760);
                    laterStatement();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:489:10: WHILE LPAREN expression RPAREN block
                    {
                    WHILE28=(Token)input.LT(1);
                    match(input,WHILE,FOLLOW_WHILE_in_statementExcept3775); if (failed) return value;
                    match(input,LPAREN,FOLLOW_LPAREN_in_statementExcept3777); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_statementExcept3779);
                    expression29=expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_statementExcept3781); if (failed) return value;
                    pushFollow(FOLLOW_block_in_statementExcept3783);
                    block30=block();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(WHILE28)).WhileLoop(expression29, block30); 
                    }

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:490:10: ifStatement
                    {
                    pushFollow(FOLLOW_ifStatement_in_statementExcept3796);
                    ifStatement31=ifStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = ifStatement31; 
                    }

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:491:10: insertStatement
                    {
                    pushFollow(FOLLOW_insertStatement_in_statementExcept3813);
                    insertStatement32=insertStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = insertStatement32; 
                    }

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:492:10: deleteStatement
                    {
                    pushFollow(FOLLOW_deleteStatement_in_statementExcept3829);
                    deleteStatement33=deleteStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = deleteStatement33; 
                    }

                    }
                    break;
                case 10 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:493:4: expression SEMI
                    {
                    pushFollow(FOLLOW_expression_in_statementExcept3839);
                    expression35=expression();
                    _fsp--;
                    if (failed) return value;
                    SEMI34=(Token)input.LT(1);
                    match(input,SEMI,FOLLOW_SEMI_in_statementExcept3843); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(SEMI34)).Exec(expression35); 
                    }

                    }
                    break;
                case 11 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:494:4: BREAK SEMI
                    {
                    BREAK36=(Token)input.LT(1);
                    match(input,BREAK,FOLLOW_BREAK_in_statementExcept3853); if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_statementExcept3857); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(BREAK36)).Break(null); 
                    }

                    }
                    break;
                case 12 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:495:4: CONTINUE SEMI
                    {
                    CONTINUE37=(Token)input.LT(1);
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_statementExcept3868); if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_statementExcept3872); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(CONTINUE37)).Continue(null); 
                    }

                    }
                    break;
                case 13 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:496:10: throwStatement
                    {
                    pushFollow(FOLLOW_throwStatement_in_statementExcept3888);
                    throwStatement38=throwStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = throwStatement38; 
                    }

                    }
                    break;
                case 14 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:497:10: returnStatement
                    {
                    pushFollow(FOLLOW_returnStatement_in_statementExcept3904);
                    returnStatement39=returnStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = returnStatement39; 
                    }

                    }
                    break;
                case 15 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:498:10: forAlphaStatement
                    {
                    pushFollow(FOLLOW_forAlphaStatement_in_statementExcept3920);
                    forAlphaStatement40=forAlphaStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = forAlphaStatement40; 
                    }

                    }
                    break;
                case 16 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:499:10: forJoinStatement
                    {
                    pushFollow(FOLLOW_forJoinStatement_in_statementExcept3936);
                    forJoinStatement41=forJoinStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = forJoinStatement41; 
                    }

                    }
                    break;
                case 17 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:500:10: tryStatement
                    {
                    pushFollow(FOLLOW_tryStatement_in_statementExcept3952);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:501:1: assertStatement returns [MTStatement value = null] : ASSERT expression ( COLON expression )? SEMI ;
    public final MTStatement assertStatement() throws RecognitionException {
        MTStatement value =  null;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:502:2: ( ASSERT expression ( COLON expression )? SEMI )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:502:4: ASSERT expression ( COLON expression )? SEMI
            {
            match(input,ASSERT,FOLLOW_ASSERT_in_assertStatement3971); if (failed) return value;
            pushFollow(FOLLOW_expression_in_assertStatement3975);
            expression();
            _fsp--;
            if (failed) return value;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:502:26: ( COLON expression )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==COLON) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:502:30: COLON expression
                    {
                    match(input,COLON,FOLLOW_COLON_in_assertStatement3983); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_assertStatement3987);
                    expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_assertStatement3997); if (failed) return value;

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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:503:1: localOperationDefinition returns [MTStatement value] : OPERATION name formalParameters typeReference block ;
    public final MTStatement localOperationDefinition() throws RecognitionException {
        MTStatement value = null;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:504:2: ( OPERATION name formalParameters typeReference block )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:504:4: OPERATION name formalParameters typeReference block
            {
            match(input,OPERATION,FOLLOW_OPERATION_in_localOperationDefinition4012); if (failed) return value;
            pushFollow(FOLLOW_name_in_localOperationDefinition4016);
            name();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_formalParameters_in_localOperationDefinition4020);
            formalParameters();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_typeReference_in_localOperationDefinition4024);
            typeReference();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_block_in_localOperationDefinition4027);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:506:1: localFunctionDefinition returns [MTStatement value] : ( FUNCTION )? name formalParameters typeReference block ;
    public final MTStatement localFunctionDefinition() throws RecognitionException {
        MTStatement value = null;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:507:2: ( ( FUNCTION )? name formalParameters typeReference block )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:507:4: ( FUNCTION )? name formalParameters typeReference block
            {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:507:4: ( FUNCTION )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==FUNCTION) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:0:0: FUNCTION
                    {
                    match(input,FUNCTION,FOLLOW_FUNCTION_in_localFunctionDefinition4046); if (failed) return value;

                    }
                    break;

            }

            pushFollow(FOLLOW_name_in_localFunctionDefinition4052);
            name();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_formalParameters_in_localFunctionDefinition4056);
            formalParameters();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_typeReference_in_localFunctionDefinition4060);
            typeReference();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_block_in_localFunctionDefinition4063);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:509:1: variableDeclaration returns [MTStatement value] : VAR name typeReference ( EQ bindOpt expression SEMI | SEMI ) ;
    public final MTStatement variableDeclaration() throws RecognitionException {
        MTStatement value = null;

        Token VAR43=null;
        name_return name44 = null;

        MTType typeReference45 = null;

        MTExpression expression46 = null;

        JavafxBindStatus bindOpt47 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:510:2: ( VAR name typeReference ( EQ bindOpt expression SEMI | SEMI ) )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:510:4: VAR name typeReference ( EQ bindOpt expression SEMI | SEMI )
            {
            VAR43=(Token)input.LT(1);
            match(input,VAR,FOLLOW_VAR_in_variableDeclaration4082); if (failed) return value;
            pushFollow(FOLLOW_name_in_variableDeclaration4085);
            name44=name();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_typeReference_in_variableDeclaration4088);
            typeReference45=typeReference();
            _fsp--;
            if (failed) return value;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:511:6: ( EQ bindOpt expression SEMI | SEMI )
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
                    new NoViableAltException("511:6: ( EQ bindOpt expression SEMI | SEMI )", 30, 0, input);

                throw nvae;
            }
            switch (alt30) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:511:8: EQ bindOpt expression SEMI
                    {
                    match(input,EQ,FOLLOW_EQ_in_variableDeclaration4099); if (failed) return value;
                    pushFollow(FOLLOW_bindOpt_in_variableDeclaration4101);
                    bindOpt47=bindOpt();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_expression_in_variableDeclaration4104);
                    expression46=expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_variableDeclaration4106); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(VAR43)).Var(name44.value, typeReference45, F.Modifiers(Flags.PARAMETER),
                      	    							expression46, bindOpt47); 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:513:8: SEMI
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_variableDeclaration4117); if (failed) return value;
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:517:1: bindOpt returns [JavafxBindStatus status = UNBOUND] : ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? ;
    public final JavafxBindStatus bindOpt() throws RecognitionException {
        JavafxBindStatus status =  UNBOUND;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:518:2: ( ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:518:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
            {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:518:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
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
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:518:6: BIND ( LAZY )?
                    {
                    match(input,BIND,FOLLOW_BIND_in_bindOpt4154); if (failed) return status;
                    if ( backtracking==0 ) {
                       status = UNIDIBIND; 
                    }
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:519:8: ( LAZY )?
                    int alt31=2;
                    int LA31_0 = input.LA(1);

                    if ( (LA31_0==LAZY) ) {
                        alt31=1;
                    }
                    switch (alt31) {
                        case 1 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:519:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt4170); if (failed) return status;
                            if ( backtracking==0 ) {
                               status = LAZY_UNIDIBIND; 
                            }

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:520:6: STAYS ( LAZY )?
                    {
                    match(input,STAYS,FOLLOW_STAYS_in_bindOpt4185); if (failed) return status;
                    if ( backtracking==0 ) {
                       status = UNIDIBIND; 
                    }
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:521:8: ( LAZY )?
                    int alt32=2;
                    int LA32_0 = input.LA(1);

                    if ( (LA32_0==LAZY) ) {
                        alt32=1;
                    }
                    switch (alt32) {
                        case 1 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:521:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt4201); if (failed) return status;
                            if ( backtracking==0 ) {
                               status = LAZY_UNIDIBIND; 
                            }

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:522:6: TIE ( LAZY )?
                    {
                    match(input,TIE,FOLLOW_TIE_in_bindOpt4216); if (failed) return status;
                    if ( backtracking==0 ) {
                       status = BIDIBIND; 
                    }
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:523:8: ( LAZY )?
                    int alt33=2;
                    int LA33_0 = input.LA(1);

                    if ( (LA33_0==LAZY) ) {
                        alt33=1;
                    }
                    switch (alt33) {
                        case 1 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:523:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt4232); if (failed) return status;
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:525:1: backgroundStatement : DO block ;
    public final void backgroundStatement() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:526:2: ( DO block )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:526:4: DO block
            {
            match(input,DO,FOLLOW_DO_in_backgroundStatement4258); if (failed) return ;
            pushFollow(FOLLOW_block_in_backgroundStatement4262);
            block();
            _fsp--;
            if (failed) return ;
            if ( backtracking==0 ) {
               assert false : "not implemented"; 
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
    // $ANTLR end backgroundStatement


    // $ANTLR start laterStatement
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:529:1: laterStatement : DO LATER block ;
    public final void laterStatement() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:530:2: ( DO LATER block )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:530:4: DO LATER block
            {
            match(input,DO,FOLLOW_DO_in_laterStatement4287); if (failed) return ;
            match(input,LATER,FOLLOW_LATER_in_laterStatement4291); if (failed) return ;
            pushFollow(FOLLOW_block_in_laterStatement4295);
            block();
            _fsp--;
            if (failed) return ;
            if ( backtracking==0 ) {
               assert false : "not implemented"; 
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
    // $ANTLR end laterStatement


    // $ANTLR start ifStatement
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:533:1: ifStatement returns [MTStatement value] : IF LPAREN expression RPAREN s1= block ( ELSE s2= block )? ;
    public final MTStatement ifStatement() throws RecognitionException {
        MTStatement value = null;

        Token IF48=null;
        MTBlock s1 = null;

        MTBlock s2 = null;

        MTExpression expression49 = null;


         MTStatement elsepart = null; 
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:535:2: ( IF LPAREN expression RPAREN s1= block ( ELSE s2= block )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:535:4: IF LPAREN expression RPAREN s1= block ( ELSE s2= block )?
            {
            IF48=(Token)input.LT(1);
            match(input,IF,FOLLOW_IF_in_ifStatement4325); if (failed) return value;
            match(input,LPAREN,FOLLOW_LPAREN_in_ifStatement4329); if (failed) return value;
            pushFollow(FOLLOW_expression_in_ifStatement4333);
            expression49=expression();
            _fsp--;
            if (failed) return value;
            match(input,RPAREN,FOLLOW_RPAREN_in_ifStatement4337); if (failed) return value;
            pushFollow(FOLLOW_block_in_ifStatement4343);
            s1=block();
            _fsp--;
            if (failed) return value;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:535:49: ( ELSE s2= block )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==ELSE) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:535:50: ELSE s2= block
                    {
                    match(input,ELSE,FOLLOW_ELSE_in_ifStatement4346); if (failed) return value;
                    pushFollow(FOLLOW_block_in_ifStatement4351);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:537:1: insertStatement returns [MTStatement value = null] : INSERT ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) ) SEMI ;
    public final MTStatement insertStatement() throws RecognitionException {
        MTStatement value =  null;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:538:2: ( INSERT ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) ) SEMI )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:538:4: INSERT ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) ) SEMI
            {
            match(input,INSERT,FOLLOW_INSERT_in_insertStatement4380); if (failed) return value;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:538:13: ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) )
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==DISTINCT) ) {
                alt38=1;
            }
            else if ( ((LA38_0>=POUND && LA38_0<=TYPEOF)||LA38_0==IF||(LA38_0>=THIS && LA38_0<=FALSE)||LA38_0==UNITINTERVAL||LA38_0==FOREACH||(LA38_0>=NOT && LA38_0<=NEW)||(LA38_0>=OPERATION && LA38_0<=FUNCTION)||(LA38_0>=INDEXOF && LA38_0<=SUPER)||(LA38_0>=SIZEOF && LA38_0<=REVERSE)||LA38_0==LPAREN||LA38_0==LBRACKET||LA38_0==DOT||(LA38_0>=PLUSPLUS && LA38_0<=SUBSUB)||(LA38_0>=QUES && LA38_0<=STRING_LITERAL)||LA38_0==QUOTE_LBRACE_STRING_LITERAL||(LA38_0>=QUOTED_IDENTIFIER && LA38_0<=OCTAL_LITERAL)||LA38_0==HEX_LITERAL||LA38_0==FLOATING_POINT_LITERAL||LA38_0==IDENTIFIER) ) {
                alt38=2;
            }
            else {
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("538:13: ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) )", 38, 0, input);

                throw nvae;
            }
            switch (alt38) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:538:17: DISTINCT expression INTO expression
                    {
                    match(input,DISTINCT,FOLLOW_DISTINCT_in_insertStatement4388); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_insertStatement4392);
                    expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,INTO,FOLLOW_INTO_in_insertStatement4396); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_insertStatement4400);
                    expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:538:65: expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression )
                    {
                    pushFollow(FOLLOW_expression_in_insertStatement4408);
                    expression();
                    _fsp--;
                    if (failed) return value;
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:538:78: ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression )
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
                            new NoViableAltException("538:78: ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression )", 37, 0, input);

                        throw nvae;
                    }

                    switch (alt37) {
                        case 1 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:538:82: ( ( AS ( FIRST | LAST ) )? INTO expression )
                            {
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:538:82: ( ( AS ( FIRST | LAST ) )? INTO expression )
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:538:86: ( AS ( FIRST | LAST ) )? INTO expression
                            {
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:538:86: ( AS ( FIRST | LAST ) )?
                            int alt36=2;
                            int LA36_0 = input.LA(1);

                            if ( (LA36_0==AS) ) {
                                alt36=1;
                            }
                            switch (alt36) {
                                case 1 :
                                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:538:90: AS ( FIRST | LAST )
                                    {
                                    match(input,AS,FOLLOW_AS_in_insertStatement4424); if (failed) return value;
                                    if ( (input.LA(1)>=FIRST && input.LA(1)<=LAST) ) {
                                        input.consume();
                                        errorRecovery=false;failed=false;
                                    }
                                    else {
                                        if (backtracking>0) {failed=true; return value;}
                                        MismatchedSetException mse =
                                            new MismatchedSetException(null,input);
                                        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_insertStatement4428);    throw mse;
                                    }


                                    }
                                    break;

                            }

                            match(input,INTO,FOLLOW_INTO_in_insertStatement4454); if (failed) return value;
                            pushFollow(FOLLOW_expression_in_insertStatement4458);
                            expression();
                            _fsp--;
                            if (failed) return value;

                            }


                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:538:156: AFTER expression
                            {
                            match(input,AFTER,FOLLOW_AFTER_in_insertStatement4470); if (failed) return value;
                            pushFollow(FOLLOW_expression_in_insertStatement4474);
                            expression();
                            _fsp--;
                            if (failed) return value;

                            }
                            break;
                        case 3 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:538:181: BEFORE expression
                            {
                            match(input,BEFORE,FOLLOW_BEFORE_in_insertStatement4482); if (failed) return value;
                            pushFollow(FOLLOW_expression_in_insertStatement4486);
                            expression();
                            _fsp--;
                            if (failed) return value;

                            }
                            break;

                    }


                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_insertStatement4500); if (failed) return value;

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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:539:1: deleteStatement returns [MTStatement value = null] : DELETE e1= expression ( FROM e2= expression | ) ;
    public final MTStatement deleteStatement() throws RecognitionException {
        MTStatement value =  null;

        Token DELETE50=null;
        MTExpression e1 = null;

        MTExpression e2 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:540:2: ( DELETE e1= expression ( FROM e2= expression | ) )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:540:4: DELETE e1= expression ( FROM e2= expression | )
            {
            DELETE50=(Token)input.LT(1);
            match(input,DELETE,FOLLOW_DELETE_in_deleteStatement4515); if (failed) return value;
            pushFollow(FOLLOW_expression_in_deleteStatement4520);
            e1=expression();
            _fsp--;
            if (failed) return value;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:541:5: ( FROM e2= expression | )
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==FROM) ) {
                alt39=1;
            }
            else if ( (LA39_0==EOF||(LA39_0>=POUND && LA39_0<=TYPEOF)||LA39_0==ABSTRACT||LA39_0==ATTRIBUTE||LA39_0==BREAK||(LA39_0>=CLASS && LA39_0<=DELETE)||LA39_0==DO||(LA39_0>=RETURN && LA39_0<=VAR)||LA39_0==IMPORT||LA39_0==TRIGGER||LA39_0==INSERT||LA39_0==IF||(LA39_0>=THIS && LA39_0<=UNITINTERVAL)||(LA39_0>=WHILE && LA39_0<=CONTINUE)||LA39_0==TRY||LA39_0==FOREACH||(LA39_0>=NOT && LA39_0<=READONLY)||(LA39_0>=INDEXOF && LA39_0<=SUPER)||(LA39_0>=SIZEOF && LA39_0<=REVERSE)||LA39_0==LPAREN||LA39_0==LBRACKET||LA39_0==DOT||(LA39_0>=PLUSPLUS && LA39_0<=SUBSUB)||(LA39_0>=QUES && LA39_0<=STRING_LITERAL)||LA39_0==QUOTE_LBRACE_STRING_LITERAL||LA39_0==RBRACE||(LA39_0>=QUOTED_IDENTIFIER && LA39_0<=OCTAL_LITERAL)||LA39_0==HEX_LITERAL||LA39_0==FLOATING_POINT_LITERAL||LA39_0==IDENTIFIER) ) {
                alt39=2;
            }
            else {
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("541:5: ( FROM e2= expression | )", 39, 0, input);

                throw nvae;
            }
            switch (alt39) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:541:7: FROM e2= expression
                    {
                    match(input,FROM,FOLLOW_FROM_in_deleteStatement4530); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_deleteStatement4534);
                    e2=expression();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(pos(DELETE50)).SequenceDelete(e2,e1); 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:542:37: 
                    {
                    if ( backtracking==0 ) {
                       value = F.at(pos(DELETE50)).SequenceDelete(e1); 
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
    // $ANTLR end deleteStatement


    // $ANTLR start throwStatement
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:545:1: throwStatement returns [MTStatement value = null] : THROW expression SEMI ;
    public final MTStatement throwStatement() throws RecognitionException {
        MTStatement value =  null;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:546:2: ( THROW expression SEMI )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:546:4: THROW expression SEMI
            {
            match(input,THROW,FOLLOW_THROW_in_throwStatement4572); if (failed) return value;
            pushFollow(FOLLOW_expression_in_throwStatement4576);
            expression();
            _fsp--;
            if (failed) return value;
            match(input,SEMI,FOLLOW_SEMI_in_throwStatement4580); if (failed) return value;

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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:547:1: returnStatement returns [MTStatement value] : RETURN ( expression )? SEMI ;
    public final MTStatement returnStatement() throws RecognitionException {
        MTStatement value = null;

        Token RETURN52=null;
        MTExpression expression51 = null;


         MTExpression expr = null; 
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:549:2: ( RETURN ( expression )? SEMI )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:549:4: RETURN ( expression )? SEMI
            {
            RETURN52=(Token)input.LT(1);
            match(input,RETURN,FOLLOW_RETURN_in_returnStatement4600); if (failed) return value;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:549:11: ( expression )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( ((LA40_0>=POUND && LA40_0<=TYPEOF)||LA40_0==IF||(LA40_0>=THIS && LA40_0<=FALSE)||LA40_0==UNITINTERVAL||LA40_0==FOREACH||(LA40_0>=NOT && LA40_0<=NEW)||(LA40_0>=OPERATION && LA40_0<=FUNCTION)||(LA40_0>=INDEXOF && LA40_0<=SUPER)||(LA40_0>=SIZEOF && LA40_0<=REVERSE)||LA40_0==LPAREN||LA40_0==LBRACKET||LA40_0==DOT||(LA40_0>=PLUSPLUS && LA40_0<=SUBSUB)||(LA40_0>=QUES && LA40_0<=STRING_LITERAL)||LA40_0==QUOTE_LBRACE_STRING_LITERAL||(LA40_0>=QUOTED_IDENTIFIER && LA40_0<=OCTAL_LITERAL)||LA40_0==HEX_LITERAL||LA40_0==FLOATING_POINT_LITERAL||LA40_0==IDENTIFIER) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:549:12: expression
                    {
                    pushFollow(FOLLOW_expression_in_returnStatement4603);
                    expression51=expression();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       expr = expression51; 
                    }

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_returnStatement4610); if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(pos(RETURN52)).Return(expr); 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:552:1: localTriggerStatement returns [MTStatement value = null] : TRIGGER ON ( localTriggerCondition | LPAREN localTriggerCondition RPAREN ) block ;
    public final MTStatement localTriggerStatement() throws RecognitionException {
        MTStatement value =  null;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:553:2: ( TRIGGER ON ( localTriggerCondition | LPAREN localTriggerCondition RPAREN ) block )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:553:4: TRIGGER ON ( localTriggerCondition | LPAREN localTriggerCondition RPAREN ) block
            {
            match(input,TRIGGER,FOLLOW_TRIGGER_in_localTriggerStatement4636); if (failed) return value;
            match(input,ON,FOLLOW_ON_in_localTriggerStatement4640); if (failed) return value;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:553:20: ( localTriggerCondition | LPAREN localTriggerCondition RPAREN )
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( (LA41_0==DELETE||LA41_0==INSERT||LA41_0==QUOTED_IDENTIFIER||LA41_0==IDENTIFIER) ) {
                alt41=1;
            }
            else if ( (LA41_0==LPAREN) ) {
                alt41=2;
            }
            else {
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("553:20: ( localTriggerCondition | LPAREN localTriggerCondition RPAREN )", 41, 0, input);

                throw nvae;
            }
            switch (alt41) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:553:22: localTriggerCondition
                    {
                    pushFollow(FOLLOW_localTriggerCondition_in_localTriggerStatement4647);
                    localTriggerCondition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:553:46: LPAREN localTriggerCondition RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_localTriggerStatement4651); if (failed) return value;
                    pushFollow(FOLLOW_localTriggerCondition_in_localTriggerStatement4655);
                    localTriggerCondition();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_localTriggerStatement4659); if (failed) return value;

                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_localTriggerStatement4663);
            block();
            _fsp--;
            if (failed) return value;
            if ( backtracking==0 ) {
               assert false : "not implemented"; 
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
    // $ANTLR end localTriggerStatement


    // $ANTLR start localTriggerCondition
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:556:1: localTriggerCondition returns [MTStatement value = null] : ( name ( LBRACKET name RBRACKET )? EQ expression | INSERT name INTO ( name EQ ) expression | DELETE name FROM ( name EQ ) expression );
    public final MTStatement localTriggerCondition() throws RecognitionException {
        MTStatement value =  null;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:557:2: ( name ( LBRACKET name RBRACKET )? EQ expression | INSERT name INTO ( name EQ ) expression | DELETE name FROM ( name EQ ) expression )
            int alt43=3;
            switch ( input.LA(1) ) {
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                alt43=1;
                }
                break;
            case INSERT:
                {
                alt43=2;
                }
                break;
            case DELETE:
                {
                alt43=3;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("556:1: localTriggerCondition returns [MTStatement value = null] : ( name ( LBRACKET name RBRACKET )? EQ expression | INSERT name INTO ( name EQ ) expression | DELETE name FROM ( name EQ ) expression );", 43, 0, input);

                throw nvae;
            }

            switch (alt43) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:557:4: name ( LBRACKET name RBRACKET )? EQ expression
                    {
                    pushFollow(FOLLOW_name_in_localTriggerCondition4688);
                    name();
                    _fsp--;
                    if (failed) return value;
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:557:11: ( LBRACKET name RBRACKET )?
                    int alt42=2;
                    int LA42_0 = input.LA(1);

                    if ( (LA42_0==LBRACKET) ) {
                        alt42=1;
                    }
                    switch (alt42) {
                        case 1 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:557:15: LBRACKET name RBRACKET
                            {
                            match(input,LBRACKET,FOLLOW_LBRACKET_in_localTriggerCondition4696); if (failed) return value;
                            pushFollow(FOLLOW_name_in_localTriggerCondition4700);
                            name();
                            _fsp--;
                            if (failed) return value;
                            match(input,RBRACKET,FOLLOW_RBRACKET_in_localTriggerCondition4704); if (failed) return value;

                            }
                            break;

                    }

                    match(input,EQ,FOLLOW_EQ_in_localTriggerCondition4714); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_localTriggerCondition4718);
                    expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:558:10: INSERT name INTO ( name EQ ) expression
                    {
                    match(input,INSERT,FOLLOW_INSERT_in_localTriggerCondition4730); if (failed) return value;
                    pushFollow(FOLLOW_name_in_localTriggerCondition4734);
                    name();
                    _fsp--;
                    if (failed) return value;
                    match(input,INTO,FOLLOW_INTO_in_localTriggerCondition4738); if (failed) return value;
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:558:33: ( name EQ )
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:558:35: name EQ
                    {
                    pushFollow(FOLLOW_name_in_localTriggerCondition4744);
                    name();
                    _fsp--;
                    if (failed) return value;
                    match(input,EQ,FOLLOW_EQ_in_localTriggerCondition4748); if (failed) return value;

                    }

                    pushFollow(FOLLOW_expression_in_localTriggerCondition4756);
                    expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:559:10: DELETE name FROM ( name EQ ) expression
                    {
                    match(input,DELETE,FOLLOW_DELETE_in_localTriggerCondition4768); if (failed) return value;
                    pushFollow(FOLLOW_name_in_localTriggerCondition4772);
                    name();
                    _fsp--;
                    if (failed) return value;
                    match(input,FROM,FOLLOW_FROM_in_localTriggerCondition4776); if (failed) return value;
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:559:33: ( name EQ )
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:559:35: name EQ
                    {
                    pushFollow(FOLLOW_name_in_localTriggerCondition4782);
                    name();
                    _fsp--;
                    if (failed) return value;
                    match(input,EQ,FOLLOW_EQ_in_localTriggerCondition4786); if (failed) return value;

                    }

                    pushFollow(FOLLOW_expression_in_localTriggerCondition4794);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:560:1: forAlphaStatement returns [MTStatement value = null] : FOR LPAREN alphaExpression RPAREN block ;
    public final MTStatement forAlphaStatement() throws RecognitionException {
        MTStatement value =  null;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:561:2: ( FOR LPAREN alphaExpression RPAREN block )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:561:4: FOR LPAREN alphaExpression RPAREN block
            {
            match(input,FOR,FOLLOW_FOR_in_forAlphaStatement4809); if (failed) return value;
            match(input,LPAREN,FOLLOW_LPAREN_in_forAlphaStatement4813); if (failed) return value;
            pushFollow(FOLLOW_alphaExpression_in_forAlphaStatement4817);
            alphaExpression();
            _fsp--;
            if (failed) return value;
            match(input,RPAREN,FOLLOW_RPAREN_in_forAlphaStatement4821); if (failed) return value;
            pushFollow(FOLLOW_block_in_forAlphaStatement4825);
            block();
            _fsp--;
            if (failed) return value;
            if ( backtracking==0 ) {
               assert false : "not implemented"; 
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
    // $ANTLR end forAlphaStatement


    // $ANTLR start alphaExpression
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:564:1: alphaExpression : UNITINTERVAL IN DUR expression ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )? ;
    public final void alphaExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:564:17: ( UNITINTERVAL IN DUR expression ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:564:19: UNITINTERVAL IN DUR expression ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )?
            {
            match(input,UNITINTERVAL,FOLLOW_UNITINTERVAL_in_alphaExpression4842); if (failed) return ;
            match(input,IN,FOLLOW_IN_in_alphaExpression4846); if (failed) return ;
            match(input,DUR,FOLLOW_DUR_in_alphaExpression4850); if (failed) return ;
            pushFollow(FOLLOW_expression_in_alphaExpression4854);
            expression();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:564:58: ( FPS expression )?
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==FPS) ) {
                int LA44_1 = input.LA(2);

                if ( (synpred81()) ) {
                    alt44=1;
                }
            }
            switch (alt44) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:564:62: FPS expression
                    {
                    match(input,FPS,FOLLOW_FPS_in_alphaExpression4862); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_alphaExpression4866);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:564:87: ( WHILE expression )?
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==WHILE) ) {
                int LA45_1 = input.LA(2);

                if ( (synpred82()) ) {
                    alt45=1;
                }
            }
            switch (alt45) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:564:91: WHILE expression
                    {
                    match(input,WHILE,FOLLOW_WHILE_in_alphaExpression4880); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_alphaExpression4884);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:564:118: ( CONTINUE IF expression )?
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==CONTINUE) ) {
                int LA46_1 = input.LA(2);

                if ( (synpred83()) ) {
                    alt46=1;
                }
            }
            switch (alt46) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:564:122: CONTINUE IF expression
                    {
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_alphaExpression4898); if (failed) return ;
                    match(input,IF,FOLLOW_IF_in_alphaExpression4902); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_alphaExpression4906);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:565:1: forJoinStatement returns [MTStatement value = null] : FOR LPAREN joinClause RPAREN ( LPAREN durClause RPAREN )? block ;
    public final MTStatement forJoinStatement() throws RecognitionException {
        MTStatement value =  null;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:566:2: ( FOR LPAREN joinClause RPAREN ( LPAREN durClause RPAREN )? block )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:566:4: FOR LPAREN joinClause RPAREN ( LPAREN durClause RPAREN )? block
            {
            match(input,FOR,FOLLOW_FOR_in_forJoinStatement4927); if (failed) return value;
            match(input,LPAREN,FOLLOW_LPAREN_in_forJoinStatement4931); if (failed) return value;
            pushFollow(FOLLOW_joinClause_in_forJoinStatement4935);
            joinClause();
            _fsp--;
            if (failed) return value;
            match(input,RPAREN,FOLLOW_RPAREN_in_forJoinStatement4939); if (failed) return value;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:566:41: ( LPAREN durClause RPAREN )?
            int alt47=2;
            int LA47_0 = input.LA(1);

            if ( (LA47_0==LPAREN) ) {
                alt47=1;
            }
            switch (alt47) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:566:45: LPAREN durClause RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_forJoinStatement4947); if (failed) return value;
                    pushFollow(FOLLOW_durClause_in_forJoinStatement4951);
                    durClause();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_forJoinStatement4955); if (failed) return value;

                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_forJoinStatement4965);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:567:1: joinClause : name IN expression ( COMMA name IN expression )* ( WHERE expression )? ;
    public final void joinClause() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:567:12: ( name IN expression ( COMMA name IN expression )* ( WHERE expression )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:567:14: name IN expression ( COMMA name IN expression )* ( WHERE expression )?
            {
            pushFollow(FOLLOW_name_in_joinClause4973);
            name();
            _fsp--;
            if (failed) return ;
            match(input,IN,FOLLOW_IN_in_joinClause4977); if (failed) return ;
            pushFollow(FOLLOW_expression_in_joinClause4981);
            expression();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:567:39: ( COMMA name IN expression )*
            loop48:
            do {
                int alt48=2;
                int LA48_0 = input.LA(1);

                if ( (LA48_0==COMMA) ) {
                    alt48=1;
                }


                switch (alt48) {
            	case 1 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:567:43: COMMA name IN expression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_joinClause4989); if (failed) return ;
            	    pushFollow(FOLLOW_name_in_joinClause4993);
            	    name();
            	    _fsp--;
            	    if (failed) return ;
            	    match(input,IN,FOLLOW_IN_in_joinClause4997); if (failed) return ;
            	    pushFollow(FOLLOW_expression_in_joinClause5001);
            	    expression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop48;
                }
            } while (true);

            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:567:82: ( WHERE expression )?
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==WHERE) ) {
                alt49=1;
            }
            switch (alt49) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:567:86: WHERE expression
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_joinClause5015); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_joinClause5019);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:1: durClause : DUR expression ( LINEAR | EASEIN | EASEOUT | EASEBOTH | MOTION expression )? ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )? ;
    public final void durClause() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:11: ( DUR expression ( LINEAR | EASEIN | EASEOUT | EASEBOTH | MOTION expression )? ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:13: DUR expression ( LINEAR | EASEIN | EASEOUT | EASEBOTH | MOTION expression )? ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )?
            {
            match(input,DUR,FOLLOW_DUR_in_durClause5033); if (failed) return ;
            pushFollow(FOLLOW_expression_in_durClause5037);
            expression();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:32: ( LINEAR | EASEIN | EASEOUT | EASEBOTH | MOTION expression )?
            int alt50=6;
            switch ( input.LA(1) ) {
                case LINEAR:
                    {
                    int LA50_1 = input.LA(2);

                    if ( (synpred87()) ) {
                        alt50=1;
                    }
                    }
                    break;
                case EASEIN:
                    {
                    int LA50_2 = input.LA(2);

                    if ( (synpred88()) ) {
                        alt50=2;
                    }
                    }
                    break;
                case EASEOUT:
                    {
                    int LA50_3 = input.LA(2);

                    if ( (synpred89()) ) {
                        alt50=3;
                    }
                    }
                    break;
                case EASEBOTH:
                    {
                    int LA50_4 = input.LA(2);

                    if ( (synpred90()) ) {
                        alt50=4;
                    }
                    }
                    break;
                case MOTION:
                    {
                    int LA50_5 = input.LA(2);

                    if ( (synpred91()) ) {
                        alt50=5;
                    }
                    }
                    break;
            }

            switch (alt50) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:36: LINEAR
                    {
                    match(input,LINEAR,FOLLOW_LINEAR_in_durClause5045); if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:49: EASEIN
                    {
                    match(input,EASEIN,FOLLOW_EASEIN_in_durClause5053); if (failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:62: EASEOUT
                    {
                    match(input,EASEOUT,FOLLOW_EASEOUT_in_durClause5061); if (failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:76: EASEBOTH
                    {
                    match(input,EASEBOTH,FOLLOW_EASEBOTH_in_durClause5069); if (failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:91: MOTION expression
                    {
                    match(input,MOTION,FOLLOW_MOTION_in_durClause5077); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_durClause5081);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:119: ( FPS expression )?
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==FPS) ) {
                int LA51_1 = input.LA(2);

                if ( (synpred92()) ) {
                    alt51=1;
                }
            }
            switch (alt51) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:123: FPS expression
                    {
                    match(input,FPS,FOLLOW_FPS_in_durClause5095); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_durClause5099);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:146: ( WHILE expression )?
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( (LA52_0==WHILE) ) {
                int LA52_1 = input.LA(2);

                if ( (synpred93()) ) {
                    alt52=1;
                }
            }
            switch (alt52) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:150: WHILE expression
                    {
                    match(input,WHILE,FOLLOW_WHILE_in_durClause5111); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_durClause5115);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:177: ( CONTINUE IF expression )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==CONTINUE) ) {
                int LA53_1 = input.LA(2);

                if ( (synpred94()) ) {
                    alt53=1;
                }
            }
            switch (alt53) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:181: CONTINUE IF expression
                    {
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_durClause5129); if (failed) return ;
                    match(input,IF,FOLLOW_IF_in_durClause5133); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_durClause5137);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:569:1: tryStatement returns [MTStatement value] : TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? ) ;
    public final MTStatement tryStatement() throws RecognitionException {
        MTStatement value = null;

        Token TRY54=null;
        MTBlock tb = null;

        MTBlock fb1 = null;

        MTBlock fb2 = null;

        MTCatch catchClause53 = null;


        	ListBuffer<MTCatch> catchers = new ListBuffer<MTCatch>();
        		MTBlock finalBlock = null;
        	
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:573:2: ( TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? ) )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:573:4: TRY tb= block ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )
            {
            TRY54=(Token)input.LT(1);
            match(input,TRY,FOLLOW_TRY_in_tryStatement5163); if (failed) return value;
            pushFollow(FOLLOW_block_in_tryStatement5169);
            tb=block();
            _fsp--;
            if (failed) return value;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:574:5: ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==FINALLY) ) {
                alt56=1;
            }
            else if ( (LA56_0==CATCH) ) {
                alt56=2;
            }
            else {
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("574:5: ( FINALLY fb1= block | ( catchClause )+ ( FINALLY fb2= block )? )", 56, 0, input);

                throw nvae;
            }
            switch (alt56) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:574:7: FINALLY fb1= block
                    {
                    match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement5181); if (failed) return value;
                    pushFollow(FOLLOW_block_in_tryStatement5187);
                    fb1=block();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       finalBlock = fb1; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:575:10: ( catchClause )+ ( FINALLY fb2= block )?
                    {
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:575:10: ( catchClause )+
                    int cnt54=0;
                    loop54:
                    do {
                        int alt54=2;
                        int LA54_0 = input.LA(1);

                        if ( (LA54_0==CATCH) ) {
                            alt54=1;
                        }


                        switch (alt54) {
                    	case 1 :
                    	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:575:11: catchClause
                    	    {
                    	    pushFollow(FOLLOW_catchClause_in_tryStatement5203);
                    	    catchClause53=catchClause();
                    	    _fsp--;
                    	    if (failed) return value;
                    	    if ( backtracking==0 ) {
                    	       catchers.append(catchClause53); 
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt54 >= 1 ) break loop54;
                    	    if (backtracking>0) {failed=true; return value;}
                                EarlyExitException eee =
                                    new EarlyExitException(54, input);
                                throw eee;
                        }
                        cnt54++;
                    } while (true);

                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:577:10: ( FINALLY fb2= block )?
                    int alt55=2;
                    int LA55_0 = input.LA(1);

                    if ( (LA55_0==FINALLY) ) {
                        alt55=1;
                    }
                    switch (alt55) {
                        case 1 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:577:11: FINALLY fb2= block
                            {
                            match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement5232); if (failed) return value;
                            pushFollow(FOLLOW_block_in_tryStatement5237);
                            fb2=block();
                            _fsp--;
                            if (failed) return value;
                            if ( backtracking==0 ) {
                               finalBlock = fb2; 
                            }

                            }
                            break;

                    }


                    }
                    break;

            }

            if ( backtracking==0 ) {
               value = F.at(pos(TRY54)).Try(tb, catchers.toList(), finalBlock); 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:581:1: catchClause returns [MTCatch value] : CATCH LPAREN formalParameter ( IF expression )? RPAREN block ;
    public final MTCatch catchClause() throws RecognitionException {
        MTCatch value = null;

        Token CATCH55=null;
        MTVar formalParameter56 = null;

        MTBlock block57 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:582:2: ( CATCH LPAREN formalParameter ( IF expression )? RPAREN block )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:582:4: CATCH LPAREN formalParameter ( IF expression )? RPAREN block
            {
            CATCH55=(Token)input.LT(1);
            match(input,CATCH,FOLLOW_CATCH_in_catchClause5286); if (failed) return value;
            match(input,LPAREN,FOLLOW_LPAREN_in_catchClause5289); if (failed) return value;
            pushFollow(FOLLOW_formalParameter_in_catchClause5292);
            formalParameter56=formalParameter();
            _fsp--;
            if (failed) return value;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:583:6: ( IF expression )?
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( (LA57_0==IF) ) {
                alt57=1;
            }
            switch (alt57) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:583:10: IF expression
                    {
                    match(input,IF,FOLLOW_IF_in_catchClause5303); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_catchClause5307);
                    expression();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       assert false : "if clause not implemented"; 
                    }

                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_catchClause5328); if (failed) return value;
            pushFollow(FOLLOW_block_in_catchClause5332);
            block57=block();
            _fsp--;
            if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(pos(CATCH55)).Catch(formalParameter56, block57); 
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
    // $ANTLR end catchClause


    // $ANTLR start expression
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:587:1: expression returns [MTExpression expr] : ( foreach | functionExpression | operationExpression | alphaExpression | ifExpression | selectExpression | LPAREN typeName RPAREN suffixedExpression | suffixedExpression );
    public final MTExpression expression() throws RecognitionException {
        MTExpression expr = null;

        MTExpression suffixedExpression58 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:588:2: ( foreach | functionExpression | operationExpression | alphaExpression | ifExpression | selectExpression | LPAREN typeName RPAREN suffixedExpression | suffixedExpression )
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
                        new NoViableAltException("587:1: expression returns [MTExpression expr] : ( foreach | functionExpression | operationExpression | alphaExpression | ifExpression | selectExpression | LPAREN typeName RPAREN suffixedExpression | suffixedExpression );", 58, 7, input);

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
            case DECIMAL_LITERAL:
            case OCTAL_LITERAL:
            case HEX_LITERAL:
            case FLOATING_POINT_LITERAL:
            case IDENTIFIER:
                {
                alt58=8;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return expr;}
                NoViableAltException nvae =
                    new NoViableAltException("587:1: expression returns [MTExpression expr] : ( foreach | functionExpression | operationExpression | alphaExpression | ifExpression | selectExpression | LPAREN typeName RPAREN suffixedExpression | suffixedExpression );", 58, 0, input);

                throw nvae;
            }

            switch (alt58) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:588:4: foreach
                    {
                    pushFollow(FOLLOW_foreach_in_expression5353);
                    foreach();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:589:11: functionExpression
                    {
                    pushFollow(FOLLOW_functionExpression_in_expression5366);
                    functionExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:590:11: operationExpression
                    {
                    pushFollow(FOLLOW_operationExpression_in_expression5379);
                    operationExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:591:11: alphaExpression
                    {
                    pushFollow(FOLLOW_alphaExpression_in_expression5392);
                    alphaExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:592:11: ifExpression
                    {
                    pushFollow(FOLLOW_ifExpression_in_expression5405);
                    ifExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:593:11: selectExpression
                    {
                    pushFollow(FOLLOW_selectExpression_in_expression5421);
                    selectExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:594:11: LPAREN typeName RPAREN suffixedExpression
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_expression5434); if (failed) return expr;
                    pushFollow(FOLLOW_typeName_in_expression5440);
                    typeName();
                    _fsp--;
                    if (failed) return expr;
                    match(input,RPAREN,FOLLOW_RPAREN_in_expression5443); if (failed) return expr;
                    pushFollow(FOLLOW_suffixedExpression_in_expression5447);
                    suffixedExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:595:11: suffixedExpression
                    {
                    pushFollow(FOLLOW_suffixedExpression_in_expression5462);
                    suffixedExpression58=suffixedExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = suffixedExpression58; 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:596:1: foreach returns [MTExpression expr] : FOREACH LPAREN in1= inClause ( COMMA in2= inClause )* RPAREN be= expression ;
    public final MTExpression foreach() throws RecognitionException {
        MTExpression expr = null;

        Token FOREACH59=null;
        MTForExpressionInClause in1 = null;

        MTForExpressionInClause in2 = null;

        MTExpression be = null;


         ListBuffer<MTForExpressionInClause> clauses = ListBuffer.lb(); 
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:598:2: ( FOREACH LPAREN in1= inClause ( COMMA in2= inClause )* RPAREN be= expression )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:598:4: FOREACH LPAREN in1= inClause ( COMMA in2= inClause )* RPAREN be= expression
            {
            FOREACH59=(Token)input.LT(1);
            match(input,FOREACH,FOLLOW_FOREACH_in_foreach5487); if (failed) return expr;
            match(input,LPAREN,FOLLOW_LPAREN_in_foreach5491); if (failed) return expr;
            pushFollow(FOLLOW_inClause_in_foreach5499);
            in1=inClause();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               clauses.append(in1); 
            }
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:600:3: ( COMMA in2= inClause )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( (LA59_0==COMMA) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:600:5: COMMA in2= inClause
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_foreach5519); if (failed) return expr;
            	    pushFollow(FOLLOW_inClause_in_foreach5523);
            	    in2=inClause();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       clauses.append(in2); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop59;
                }
            } while (true);

            match(input,RPAREN,FOLLOW_RPAREN_in_foreach5550); if (failed) return expr;
            pushFollow(FOLLOW_expression_in_foreach5554);
            be=expression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = F.at(pos(FOREACH59)).ForExpression(clauses.toList(), be); 
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
    // $ANTLR end foreach


    // $ANTLR start inClause
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:603:1: inClause returns [MTForExpressionInClause value] : formalParameter IN se= expression ( WHERE we= expression )? ;
    public final MTForExpressionInClause inClause() throws RecognitionException {
        MTForExpressionInClause value = null;

        Token IN60=null;
        MTExpression se = null;

        MTExpression we = null;

        MTVar formalParameter61 = null;


         MTVar var; 
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:605:2: ( formalParameter IN se= expression ( WHERE we= expression )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:605:4: formalParameter IN se= expression ( WHERE we= expression )?
            {
            pushFollow(FOLLOW_formalParameter_in_inClause5582);
            formalParameter61=formalParameter();
            _fsp--;
            if (failed) return value;
            IN60=(Token)input.LT(1);
            match(input,IN,FOLLOW_IN_in_inClause5584); if (failed) return value;
            pushFollow(FOLLOW_expression_in_inClause5588);
            se=expression();
            _fsp--;
            if (failed) return value;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:606:15: ( WHERE we= expression )?
            int alt60=2;
            int LA60_0 = input.LA(1);

            if ( (LA60_0==WHERE) ) {
                alt60=1;
            }
            switch (alt60) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:606:16: WHERE we= expression
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_inClause5606); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_inClause5611);
                    we=expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;

            }

            if ( backtracking==0 ) {
               value = F.at(pos(IN60)).InClause(formalParameter61, se, we); 
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
    // $ANTLR end inClause


    // $ANTLR start functionExpression
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:608:1: functionExpression : FUNCTION formalParameters ( typeReference )? functionBody ;
    public final void functionExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:608:20: ( FUNCTION formalParameters ( typeReference )? functionBody )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:608:22: FUNCTION formalParameters ( typeReference )? functionBody
            {
            match(input,FUNCTION,FOLLOW_FUNCTION_in_functionExpression5625); if (failed) return ;
            pushFollow(FOLLOW_formalParameters_in_functionExpression5629);
            formalParameters();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:608:52: ( typeReference )?
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
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:0:0: typeReference
                    {
                    pushFollow(FOLLOW_typeReference_in_functionExpression5633);
                    typeReference();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_functionBody_in_functionExpression5639);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:609:1: operationExpression : OPERATION formalParameters ( typeReference )? block ;
    public final void operationExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:609:21: ( OPERATION formalParameters ( typeReference )? block )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:609:23: OPERATION formalParameters ( typeReference )? block
            {
            match(input,OPERATION,FOLLOW_OPERATION_in_operationExpression5647); if (failed) return ;
            pushFollow(FOLLOW_formalParameters_in_operationExpression5651);
            formalParameters();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:609:54: ( typeReference )?
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
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:0:0: typeReference
                    {
                    pushFollow(FOLLOW_typeReference_in_operationExpression5655);
                    typeReference();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_operationExpression5661);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:610:1: ifExpression : IF expression THEN expression ELSE expression ;
    public final void ifExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:610:14: ( IF expression THEN expression ELSE expression )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:610:16: IF expression THEN expression ELSE expression
            {
            match(input,IF,FOLLOW_IF_in_ifExpression5669); if (failed) return ;
            pushFollow(FOLLOW_expression_in_ifExpression5673);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,THEN,FOLLOW_THEN_in_ifExpression5677); if (failed) return ;
            pushFollow(FOLLOW_expression_in_ifExpression5681);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,ELSE,FOLLOW_ELSE_in_ifExpression5685); if (failed) return ;
            pushFollow(FOLLOW_expression_in_ifExpression5689);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:611:1: selectExpression : SELECT ( DISTINCT )? expression FROM selectionVar ( COMMA selectionVar )* ( WHERE expression )? ;
    public final void selectExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:611:18: ( SELECT ( DISTINCT )? expression FROM selectionVar ( COMMA selectionVar )* ( WHERE expression )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:611:20: SELECT ( DISTINCT )? expression FROM selectionVar ( COMMA selectionVar )* ( WHERE expression )?
            {
            match(input,SELECT,FOLLOW_SELECT_in_selectExpression5697); if (failed) return ;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:611:29: ( DISTINCT )?
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==DISTINCT) ) {
                alt63=1;
            }
            switch (alt63) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:0:0: DISTINCT
                    {
                    match(input,DISTINCT,FOLLOW_DISTINCT_in_selectExpression5701); if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_expression_in_selectExpression5709);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,FROM,FOLLOW_FROM_in_selectExpression5713); if (failed) return ;
            pushFollow(FOLLOW_selectionVar_in_selectExpression5717);
            selectionVar();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:611:79: ( COMMA selectionVar )*
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
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:611:83: COMMA selectionVar
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_selectExpression5725); if (failed) return ;
            	    pushFollow(FOLLOW_selectionVar_in_selectExpression5729);
            	    selectionVar();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop64;
                }
            } while (true);

            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:611:112: ( WHERE expression )?
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
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:611:116: WHERE expression
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_selectExpression5743); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_selectExpression5747);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:612:1: selectionVar : name ( IN expression )? ;
    public final void selectionVar() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:612:14: ( name ( IN expression )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:612:16: name ( IN expression )?
            {
            pushFollow(FOLLOW_name_in_selectionVar5761);
            name();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:612:23: ( IN expression )?
            int alt66=2;
            int LA66_0 = input.LA(1);

            if ( (LA66_0==IN) ) {
                alt66=1;
            }
            switch (alt66) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:612:27: IN expression
                    {
                    match(input,IN,FOLLOW_IN_in_selectionVar5769); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_selectionVar5773);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:613:1: suffixedExpression returns [MTExpression expr] : e1= assignmentExpression ( indexOn | orderBy | durClause | PLUSPLUS | SUBSUB )? ;
    public final MTExpression suffixedExpression() throws RecognitionException {
        MTExpression expr = null;

        MTExpression e1 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:614:2: (e1= assignmentExpression ( indexOn | orderBy | durClause | PLUSPLUS | SUBSUB )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:614:4: e1= assignmentExpression ( indexOn | orderBy | durClause | PLUSPLUS | SUBSUB )?
            {
            pushFollow(FOLLOW_assignmentExpression_in_suffixedExpression5796);
            e1=assignmentExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:615:5: ( indexOn | orderBy | durClause | PLUSPLUS | SUBSUB )?
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
                    switch ( input.LA(2) ) {
                        case NEW:
                            {
                            int LA67_7 = input.LA(3);

                            if ( (LA67_7==QUOTED_IDENTIFIER||LA67_7==IDENTIFIER) ) {
                                int LA67_43 = input.LA(4);

                                if ( (synpred117()) ) {
                                    alt67=4;
                                }
                            }
                            }
                            break;
                        case QUOTED_IDENTIFIER:
                        case IDENTIFIER:
                            {
                            int LA67_8 = input.LA(3);

                            if ( (synpred117()) ) {
                                alt67=4;
                            }
                            }
                            break;
                        case LBRACKET:
                            {
                            int LA67_9 = input.LA(3);

                            if ( (synpred117()) ) {
                                alt67=4;
                            }
                            }
                            break;
                        case INDEXOF:
                            {
                            int LA67_10 = input.LA(3);

                            if ( (LA67_10==QUOTED_IDENTIFIER||LA67_10==IDENTIFIER) ) {
                                int LA67_44 = input.LA(4);

                                if ( (synpred117()) ) {
                                    alt67=4;
                                }
                            }
                            else if ( (LA67_10==DOT) ) {
                                int LA67_45 = input.LA(4);

                                if ( (synpred117()) ) {
                                    alt67=4;
                                }
                            }
                            }
                            break;
                        case DOT:
                            {
                            int LA67_11 = input.LA(3);

                            if ( (synpred117()) ) {
                                alt67=4;
                            }
                            }
                            break;
                        case THIS:
                            {
                            int LA67_12 = input.LA(3);

                            if ( (synpred117()) ) {
                                alt67=4;
                            }
                            }
                            break;
                        case SUPER:
                            {
                            int LA67_13 = input.LA(3);

                            if ( (synpred117()) ) {
                                alt67=4;
                            }
                            }
                            break;
                        case QUOTE_LBRACE_STRING_LITERAL:
                            {
                            int LA67_14 = input.LA(3);

                            if ( (synpred117()) ) {
                                alt67=4;
                            }
                            }
                            break;
                        case STRING_LITERAL:
                            {
                            int LA67_15 = input.LA(3);

                            if ( (synpred117()) ) {
                                alt67=4;
                            }
                            }
                            break;
                        case DECIMAL_LITERAL:
                            {
                            int LA67_16 = input.LA(3);

                            if ( (synpred117()) ) {
                                alt67=4;
                            }
                            }
                            break;
                        case OCTAL_LITERAL:
                            {
                            int LA67_17 = input.LA(3);

                            if ( (synpred117()) ) {
                                alt67=4;
                            }
                            }
                            break;
                        case HEX_LITERAL:
                            {
                            int LA67_18 = input.LA(3);

                            if ( (synpred117()) ) {
                                alt67=4;
                            }
                            }
                            break;
                        case FLOATING_POINT_LITERAL:
                            {
                            int LA67_19 = input.LA(3);

                            if ( (synpred117()) ) {
                                alt67=4;
                            }
                            }
                            break;
                        case TRUE:
                            {
                            int LA67_20 = input.LA(3);

                            if ( (synpred117()) ) {
                                alt67=4;
                            }
                            }
                            break;
                        case FALSE:
                            {
                            int LA67_21 = input.LA(3);

                            if ( (synpred117()) ) {
                                alt67=4;
                            }
                            }
                            break;
                        case NULL:
                            {
                            int LA67_22 = input.LA(3);

                            if ( (synpred117()) ) {
                                alt67=4;
                            }
                            }
                            break;
                        case LPAREN:
                            {
                            int LA67_23 = input.LA(3);

                            if ( (synpred117()) ) {
                                alt67=4;
                            }
                            }
                            break;
                        case EOF:
                        case BAR:
                        case POUND:
                        case TYPEOF:
                        case DOTDOT:
                        case ABSTRACT:
                        case AFTER:
                        case AS:
                        case ATTRIBUTE:
                        case BEFORE:
                        case BREAK:
                        case CLASS:
                        case DELETE:
                        case DO:
                        case EASEBOTH:
                        case EASEIN:
                        case EASEOUT:
                        case RETURN:
                        case THROW:
                        case VAR:
                        case IMPORT:
                        case FROM:
                        case TRIGGER:
                        case INSERT:
                        case INTO:
                        case IF:
                        case THEN:
                        case ELSE:
                        case FOR:
                        case UNITINTERVAL:
                        case FPS:
                        case WHILE:
                        case CONTINUE:
                        case LINEAR:
                        case MOTION:
                        case TRY:
                        case FOREACH:
                        case WHERE:
                        case NOT:
                        case PRIVATE:
                        case PROTECTED:
                        case PUBLIC:
                        case OPERATION:
                        case FUNCTION:
                        case READONLY:
                        case SELECT:
                        case SIZEOF:
                        case REVERSE:
                        case RPAREN:
                        case RBRACKET:
                        case SEMI:
                        case COMMA:
                        case PLUSPLUS:
                        case SUB:
                        case SUBSUB:
                        case COLON:
                        case QUES:
                        case LBRACE:
                        case RBRACE_QUOTE_STRING_LITERAL:
                        case RBRACE_LBRACE_STRING_LITERAL:
                        case RBRACE:
                            {
                            alt67=4;
                            }
                            break;
                    }

                    }
                    break;
                case SUBSUB:
                    {
                    switch ( input.LA(2) ) {
                        case NEW:
                            {
                            int LA67_25 = input.LA(3);

                            if ( (LA67_25==QUOTED_IDENTIFIER||LA67_25==IDENTIFIER) ) {
                                int LA67_46 = input.LA(4);

                                if ( (synpred118()) ) {
                                    alt67=5;
                                }
                            }
                            }
                            break;
                        case QUOTED_IDENTIFIER:
                        case IDENTIFIER:
                            {
                            int LA67_26 = input.LA(3);

                            if ( (synpred118()) ) {
                                alt67=5;
                            }
                            }
                            break;
                        case LBRACKET:
                            {
                            int LA67_27 = input.LA(3);

                            if ( (synpred118()) ) {
                                alt67=5;
                            }
                            }
                            break;
                        case INDEXOF:
                            {
                            int LA67_28 = input.LA(3);

                            if ( (LA67_28==QUOTED_IDENTIFIER||LA67_28==IDENTIFIER) ) {
                                int LA67_47 = input.LA(4);

                                if ( (synpred118()) ) {
                                    alt67=5;
                                }
                            }
                            else if ( (LA67_28==DOT) ) {
                                int LA67_48 = input.LA(4);

                                if ( (synpred118()) ) {
                                    alt67=5;
                                }
                            }
                            }
                            break;
                        case DOT:
                            {
                            int LA67_29 = input.LA(3);

                            if ( (synpred118()) ) {
                                alt67=5;
                            }
                            }
                            break;
                        case THIS:
                            {
                            int LA67_30 = input.LA(3);

                            if ( (synpred118()) ) {
                                alt67=5;
                            }
                            }
                            break;
                        case SUPER:
                            {
                            int LA67_31 = input.LA(3);

                            if ( (synpred118()) ) {
                                alt67=5;
                            }
                            }
                            break;
                        case QUOTE_LBRACE_STRING_LITERAL:
                            {
                            int LA67_32 = input.LA(3);

                            if ( (synpred118()) ) {
                                alt67=5;
                            }
                            }
                            break;
                        case STRING_LITERAL:
                            {
                            int LA67_33 = input.LA(3);

                            if ( (synpred118()) ) {
                                alt67=5;
                            }
                            }
                            break;
                        case DECIMAL_LITERAL:
                            {
                            int LA67_34 = input.LA(3);

                            if ( (synpred118()) ) {
                                alt67=5;
                            }
                            }
                            break;
                        case OCTAL_LITERAL:
                            {
                            int LA67_35 = input.LA(3);

                            if ( (synpred118()) ) {
                                alt67=5;
                            }
                            }
                            break;
                        case HEX_LITERAL:
                            {
                            int LA67_36 = input.LA(3);

                            if ( (synpred118()) ) {
                                alt67=5;
                            }
                            }
                            break;
                        case FLOATING_POINT_LITERAL:
                            {
                            int LA67_37 = input.LA(3);

                            if ( (synpred118()) ) {
                                alt67=5;
                            }
                            }
                            break;
                        case TRUE:
                            {
                            int LA67_38 = input.LA(3);

                            if ( (synpred118()) ) {
                                alt67=5;
                            }
                            }
                            break;
                        case FALSE:
                            {
                            int LA67_39 = input.LA(3);

                            if ( (synpred118()) ) {
                                alt67=5;
                            }
                            }
                            break;
                        case NULL:
                            {
                            int LA67_40 = input.LA(3);

                            if ( (synpred118()) ) {
                                alt67=5;
                            }
                            }
                            break;
                        case LPAREN:
                            {
                            int LA67_41 = input.LA(3);

                            if ( (synpred118()) ) {
                                alt67=5;
                            }
                            }
                            break;
                        case EOF:
                        case BAR:
                        case POUND:
                        case TYPEOF:
                        case DOTDOT:
                        case ABSTRACT:
                        case AFTER:
                        case AS:
                        case ATTRIBUTE:
                        case BEFORE:
                        case BREAK:
                        case CLASS:
                        case DELETE:
                        case DO:
                        case EASEBOTH:
                        case EASEIN:
                        case EASEOUT:
                        case RETURN:
                        case THROW:
                        case VAR:
                        case IMPORT:
                        case FROM:
                        case TRIGGER:
                        case INSERT:
                        case INTO:
                        case IF:
                        case THEN:
                        case ELSE:
                        case FOR:
                        case UNITINTERVAL:
                        case FPS:
                        case WHILE:
                        case CONTINUE:
                        case LINEAR:
                        case MOTION:
                        case TRY:
                        case FOREACH:
                        case WHERE:
                        case NOT:
                        case PRIVATE:
                        case PROTECTED:
                        case PUBLIC:
                        case OPERATION:
                        case FUNCTION:
                        case READONLY:
                        case SELECT:
                        case SIZEOF:
                        case REVERSE:
                        case RPAREN:
                        case RBRACKET:
                        case SEMI:
                        case COMMA:
                        case PLUSPLUS:
                        case SUB:
                        case SUBSUB:
                        case COLON:
                        case QUES:
                        case LBRACE:
                        case RBRACE_QUOTE_STRING_LITERAL:
                        case RBRACE_LBRACE_STRING_LITERAL:
                        case RBRACE:
                            {
                            alt67=5;
                            }
                            break;
                    }

                    }
                    break;
            }

            switch (alt67) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:615:6: indexOn
                    {
                    pushFollow(FOLLOW_indexOn_in_suffixedExpression5808);
                    indexOn();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:615:16: orderBy
                    {
                    pushFollow(FOLLOW_orderBy_in_suffixedExpression5812);
                    orderBy();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:615:26: durClause
                    {
                    pushFollow(FOLLOW_durClause_in_suffixedExpression5816);
                    durClause();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:615:38: PLUSPLUS
                    {
                    match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_suffixedExpression5820); if (failed) return expr;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:615:49: SUBSUB
                    {
                    match(input,SUBSUB,FOLLOW_SUBSUB_in_suffixedExpression5824); if (failed) return expr;

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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:616:1: assignmentExpression returns [MTExpression expr] : e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? ;
    public final MTExpression assignmentExpression() throws RecognitionException {
        MTExpression expr = null;

        Token EQ62=null;
        MTExpression e1 = null;

        MTExpression e2 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:617:2: (e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:617:4: e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )?
            {
            pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression5846);
            e1=assignmentOpExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:618:5: ( EQ e2= assignmentOpExpression )?
            int alt68=2;
            int LA68_0 = input.LA(1);

            if ( (LA68_0==EQ) ) {
                alt68=1;
            }
            switch (alt68) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:618:9: EQ e2= assignmentOpExpression
                    {
                    EQ62=(Token)input.LT(1);
                    match(input,EQ,FOLLOW_EQ_in_assignmentExpression5861); if (failed) return expr;
                    pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression5867);
                    e2=assignmentOpExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(EQ62)).Assign(expr, e2); 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:619:1: assignmentOpExpression returns [MTExpression expr] : e1= andExpression ( assignmentOperator e2= andExpression )? ;
    public final MTExpression assignmentOpExpression() throws RecognitionException {
        MTExpression expr = null;

        MTExpression e1 = null;

        MTExpression e2 = null;

        int assignmentOperator63 = 0;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:620:2: (e1= andExpression ( assignmentOperator e2= andExpression )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:620:4: e1= andExpression ( assignmentOperator e2= andExpression )?
            {
            pushFollow(FOLLOW_andExpression_in_assignmentOpExpression5894);
            e1=andExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:621:5: ( assignmentOperator e2= andExpression )?
            int alt69=2;
            int LA69_0 = input.LA(1);

            if ( ((LA69_0>=PLUSEQ && LA69_0<=PERCENTEQ)) ) {
                alt69=1;
            }
            switch (alt69) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:621:9: assignmentOperator e2= andExpression
                    {
                    pushFollow(FOLLOW_assignmentOperator_in_assignmentOpExpression5910);
                    assignmentOperator63=assignmentOperator();
                    _fsp--;
                    if (failed) return expr;
                    pushFollow(FOLLOW_andExpression_in_assignmentOpExpression5916);
                    e2=andExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.Assignop(assignmentOperator63,
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:623:1: andExpression returns [MTExpression expr] : e1= orExpression ( AND e2= orExpression )* ;
    public final MTExpression andExpression() throws RecognitionException {
        MTExpression expr = null;

        Token AND64=null;
        MTExpression e1 = null;

        MTExpression e2 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:624:2: (e1= orExpression ( AND e2= orExpression )* )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:624:4: e1= orExpression ( AND e2= orExpression )*
            {
            pushFollow(FOLLOW_orExpression_in_andExpression5942);
            e1=orExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:625:5: ( AND e2= orExpression )*
            loop70:
            do {
                int alt70=2;
                int LA70_0 = input.LA(1);

                if ( (LA70_0==AND) ) {
                    alt70=1;
                }


                switch (alt70) {
            	case 1 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:625:9: AND e2= orExpression
            	    {
            	    AND64=(Token)input.LT(1);
            	    match(input,AND,FOLLOW_AND_in_andExpression5958); if (failed) return expr;
            	    pushFollow(FOLLOW_orExpression_in_andExpression5964);
            	    e2=orExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(AND64)).Binary(MTTree.AND, expr, e2); 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:626:1: orExpression returns [MTExpression expr] : e1= instanceOfExpression ( OR e2= instanceOfExpression )* ;
    public final MTExpression orExpression() throws RecognitionException {
        MTExpression expr = null;

        Token OR65=null;
        MTExpression e1 = null;

        MTExpression e2 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:627:2: (e1= instanceOfExpression ( OR e2= instanceOfExpression )* )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:627:4: e1= instanceOfExpression ( OR e2= instanceOfExpression )*
            {
            pushFollow(FOLLOW_instanceOfExpression_in_orExpression5992);
            e1=instanceOfExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:628:5: ( OR e2= instanceOfExpression )*
            loop71:
            do {
                int alt71=2;
                int LA71_0 = input.LA(1);

                if ( (LA71_0==OR) ) {
                    alt71=1;
                }


                switch (alt71) {
            	case 1 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:628:9: OR e2= instanceOfExpression
            	    {
            	    OR65=(Token)input.LT(1);
            	    match(input,OR,FOLLOW_OR_in_orExpression6007); if (failed) return expr;
            	    pushFollow(FOLLOW_instanceOfExpression_in_orExpression6013);
            	    e2=instanceOfExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(OR65)).Binary(MTTree.OR, expr, e2); 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:629:1: instanceOfExpression returns [MTExpression expr] : e1= relationalExpression ( INSTANCEOF identifier )? ;
    public final MTExpression instanceOfExpression() throws RecognitionException {
        MTExpression expr = null;

        Token INSTANCEOF66=null;
        MTExpression e1 = null;

        MTIdent identifier67 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:630:2: (e1= relationalExpression ( INSTANCEOF identifier )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:630:4: e1= relationalExpression ( INSTANCEOF identifier )?
            {
            pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression6041);
            e1=relationalExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:631:5: ( INSTANCEOF identifier )?
            int alt72=2;
            int LA72_0 = input.LA(1);

            if ( (LA72_0==INSTANCEOF) ) {
                alt72=1;
            }
            switch (alt72) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:631:9: INSTANCEOF identifier
                    {
                    INSTANCEOF66=(Token)input.LT(1);
                    match(input,INSTANCEOF,FOLLOW_INSTANCEOF_in_instanceOfExpression6056); if (failed) return expr;
                    pushFollow(FOLLOW_identifier_in_instanceOfExpression6058);
                    identifier67=identifier();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(INSTANCEOF66)).Binary(MTTree.TYPETEST, expr, 
                      	   													 identifier67); 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:633:1: relationalExpression returns [MTExpression expr] : e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* ;
    public final MTExpression relationalExpression() throws RecognitionException {
        MTExpression expr = null;

        Token LTGT68=null;
        Token EQEQ69=null;
        Token LTEQ70=null;
        Token GTEQ71=null;
        Token LT72=null;
        Token GT73=null;
        Token IN74=null;
        MTExpression e1 = null;

        MTExpression e = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:634:2: (e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:634:4: e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            {
            pushFollow(FOLLOW_additiveExpression_in_relationalExpression6086);
            e1=additiveExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:635:5: ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
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
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:635:9: LTGT e= additiveExpression
            	    {
            	    LTGT68=(Token)input.LT(1);
            	    match(input,LTGT,FOLLOW_LTGT_in_relationalExpression6102); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression6108);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(LTGT68)).Binary(MTTree.NE, expr, e); 
            	    }

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:636:9: EQEQ e= additiveExpression
            	    {
            	    EQEQ69=(Token)input.LT(1);
            	    match(input,EQEQ,FOLLOW_EQEQ_in_relationalExpression6122); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression6128);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(EQEQ69)).Binary(MTTree.EQ, expr, e); 
            	    }

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:637:9: LTEQ e= additiveExpression
            	    {
            	    LTEQ70=(Token)input.LT(1);
            	    match(input,LTEQ,FOLLOW_LTEQ_in_relationalExpression6142); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression6148);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(LTEQ70)).Binary(MTTree.LE, expr, e); 
            	    }

            	    }
            	    break;
            	case 4 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:638:9: GTEQ e= additiveExpression
            	    {
            	    GTEQ71=(Token)input.LT(1);
            	    match(input,GTEQ,FOLLOW_GTEQ_in_relationalExpression6162); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression6168);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(GTEQ71)).Binary(MTTree.GE, expr, e); 
            	    }

            	    }
            	    break;
            	case 5 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:639:9: LT e= additiveExpression
            	    {
            	    LT72=(Token)input.LT(1);
            	    match(input,LT,FOLLOW_LT_in_relationalExpression6182); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression6190);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(LT72))  .Binary(MTTree.LT, expr, e); 
            	    }

            	    }
            	    break;
            	case 6 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:640:9: GT e= additiveExpression
            	    {
            	    GT73=(Token)input.LT(1);
            	    match(input,GT,FOLLOW_GT_in_relationalExpression6204); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression6212);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(GT73))  .Binary(MTTree.GT, expr, e); 
            	    }

            	    }
            	    break;
            	case 7 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:641:9: IN e= additiveExpression
            	    {
            	    IN74=(Token)input.LT(1);
            	    match(input,IN,FOLLOW_IN_in_relationalExpression6226); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression6234);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       /* expr = F.at(pos(IN74  )).Binary(JavaFXTag.IN, expr, $e2.expr); */ 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:643:1: additiveExpression returns [MTExpression expr] : e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* ;
    public final MTExpression additiveExpression() throws RecognitionException {
        MTExpression expr = null;

        Token PLUS75=null;
        Token SUB76=null;
        MTExpression e1 = null;

        MTExpression e = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:644:2: (e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:644:4: e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            {
            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression6263);
            e1=multiplicativeExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:645:5: ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            loop74:
            do {
                int alt74=3;
                int LA74_0 = input.LA(1);

                if ( (LA74_0==SUB) ) {
                    switch ( input.LA(2) ) {
                    case NEW:
                        {
                        int LA74_4 = input.LA(3);

                        if ( (LA74_4==QUOTED_IDENTIFIER||LA74_4==IDENTIFIER) ) {
                            int LA74_22 = input.LA(4);

                            if ( (synpred132()) ) {
                                alt74=2;
                            }


                        }


                        }
                        break;
                    case QUOTED_IDENTIFIER:
                    case IDENTIFIER:
                        {
                        int LA74_5 = input.LA(3);

                        if ( (synpred132()) ) {
                            alt74=2;
                        }


                        }
                        break;
                    case LBRACKET:
                        {
                        int LA74_6 = input.LA(3);

                        if ( (synpred132()) ) {
                            alt74=2;
                        }


                        }
                        break;
                    case INDEXOF:
                        {
                        int LA74_7 = input.LA(3);

                        if ( (LA74_7==QUOTED_IDENTIFIER||LA74_7==IDENTIFIER) ) {
                            int LA74_23 = input.LA(4);

                            if ( (synpred132()) ) {
                                alt74=2;
                            }


                        }
                        else if ( (LA74_7==DOT) ) {
                            int LA74_24 = input.LA(4);

                            if ( (synpred132()) ) {
                                alt74=2;
                            }


                        }


                        }
                        break;
                    case DOT:
                        {
                        int LA74_8 = input.LA(3);

                        if ( (synpred132()) ) {
                            alt74=2;
                        }


                        }
                        break;
                    case THIS:
                        {
                        int LA74_9 = input.LA(3);

                        if ( (synpred132()) ) {
                            alt74=2;
                        }


                        }
                        break;
                    case SUPER:
                        {
                        int LA74_10 = input.LA(3);

                        if ( (synpred132()) ) {
                            alt74=2;
                        }


                        }
                        break;
                    case QUOTE_LBRACE_STRING_LITERAL:
                        {
                        int LA74_11 = input.LA(3);

                        if ( (synpred132()) ) {
                            alt74=2;
                        }


                        }
                        break;
                    case STRING_LITERAL:
                        {
                        int LA74_12 = input.LA(3);

                        if ( (synpred132()) ) {
                            alt74=2;
                        }


                        }
                        break;
                    case DECIMAL_LITERAL:
                        {
                        int LA74_13 = input.LA(3);

                        if ( (synpred132()) ) {
                            alt74=2;
                        }


                        }
                        break;
                    case OCTAL_LITERAL:
                        {
                        int LA74_14 = input.LA(3);

                        if ( (synpred132()) ) {
                            alt74=2;
                        }


                        }
                        break;
                    case HEX_LITERAL:
                        {
                        int LA74_15 = input.LA(3);

                        if ( (synpred132()) ) {
                            alt74=2;
                        }


                        }
                        break;
                    case FLOATING_POINT_LITERAL:
                        {
                        int LA74_16 = input.LA(3);

                        if ( (synpred132()) ) {
                            alt74=2;
                        }


                        }
                        break;
                    case TRUE:
                        {
                        int LA74_17 = input.LA(3);

                        if ( (synpred132()) ) {
                            alt74=2;
                        }


                        }
                        break;
                    case FALSE:
                        {
                        int LA74_18 = input.LA(3);

                        if ( (synpred132()) ) {
                            alt74=2;
                        }


                        }
                        break;
                    case NULL:
                        {
                        int LA74_19 = input.LA(3);

                        if ( (synpred132()) ) {
                            alt74=2;
                        }


                        }
                        break;
                    case LPAREN:
                        {
                        int LA74_20 = input.LA(3);

                        if ( (synpred132()) ) {
                            alt74=2;
                        }


                        }
                        break;
                    case POUND:
                    case TYPEOF:
                    case NOT:
                    case SIZEOF:
                    case REVERSE:
                    case PLUSPLUS:
                    case SUB:
                    case SUBSUB:
                    case QUES:
                        {
                        alt74=2;
                        }
                        break;

                    }

                }
                else if ( (LA74_0==PLUS) ) {
                    alt74=1;
                }


                switch (alt74) {
            	case 1 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:645:9: PLUS e= multiplicativeExpression
            	    {
            	    PLUS75=(Token)input.LT(1);
            	    match(input,PLUS,FOLLOW_PLUS_in_additiveExpression6278); if (failed) return expr;
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression6284);
            	    e=multiplicativeExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(PLUS75)).Binary(MTTree.PLUS , expr, e); 
            	    }

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:646:9: SUB e= multiplicativeExpression
            	    {
            	    SUB76=(Token)input.LT(1);
            	    match(input,SUB,FOLLOW_SUB_in_additiveExpression6297); if (failed) return expr;
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression6304);
            	    e=multiplicativeExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(SUB76)) .Binary(MTTree.MINUS, expr, e); 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:648:1: multiplicativeExpression returns [MTExpression expr] : e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* ;
    public final MTExpression multiplicativeExpression() throws RecognitionException {
        MTExpression expr = null;

        Token STAR77=null;
        Token SLASH78=null;
        Token PERCENT79=null;
        MTExpression e1 = null;

        MTExpression e = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:649:2: (e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:649:4: e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            {
            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression6332);
            e1=unaryExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:650:5: ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
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
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:650:9: STAR e= unaryExpression
            	    {
            	    STAR77=(Token)input.LT(1);
            	    match(input,STAR,FOLLOW_STAR_in_multiplicativeExpression6348); if (failed) return expr;
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression6355);
            	    e=unaryExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(STAR77))   .Binary(MTTree.MUL  , expr, e); 
            	    }

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:651:9: SLASH e= unaryExpression
            	    {
            	    SLASH78=(Token)input.LT(1);
            	    match(input,SLASH,FOLLOW_SLASH_in_multiplicativeExpression6369); if (failed) return expr;
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression6375);
            	    e=unaryExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(SLASH78))  .Binary(MTTree.DIV  , expr, e); 
            	    }

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:652:9: PERCENT e= unaryExpression
            	    {
            	    PERCENT79=(Token)input.LT(1);
            	    match(input,PERCENT,FOLLOW_PERCENT_in_multiplicativeExpression6389); if (failed) return expr;
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression6393);
            	    e=unaryExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(pos(PERCENT79)).Binary(MTTree.MOD  , expr, e); 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:654:1: unaryExpression returns [MTExpression expr] : ( postfixExpression | unaryOperator postfixExpression );
    public final MTExpression unaryExpression() throws RecognitionException {
        MTExpression expr = null;

        MTExpression postfixExpression80 = null;

        int unaryOperator81 = 0;

        MTExpression postfixExpression82 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:655:2: ( postfixExpression | unaryOperator postfixExpression )
            int alt76=2;
            int LA76_0 = input.LA(1);

            if ( ((LA76_0>=THIS && LA76_0<=FALSE)||LA76_0==NEW||LA76_0==INDEXOF||LA76_0==SUPER||LA76_0==LPAREN||LA76_0==LBRACKET||LA76_0==DOT||LA76_0==STRING_LITERAL||LA76_0==QUOTE_LBRACE_STRING_LITERAL||(LA76_0>=QUOTED_IDENTIFIER && LA76_0<=OCTAL_LITERAL)||LA76_0==HEX_LITERAL||LA76_0==FLOATING_POINT_LITERAL||LA76_0==IDENTIFIER) ) {
                alt76=1;
            }
            else if ( ((LA76_0>=POUND && LA76_0<=TYPEOF)||LA76_0==NOT||(LA76_0>=SIZEOF && LA76_0<=REVERSE)||(LA76_0>=PLUSPLUS && LA76_0<=SUBSUB)||LA76_0==QUES) ) {
                alt76=2;
            }
            else {
                if (backtracking>0) {failed=true; return expr;}
                NoViableAltException nvae =
                    new NoViableAltException("654:1: unaryExpression returns [MTExpression expr] : ( postfixExpression | unaryOperator postfixExpression );", 76, 0, input);

                throw nvae;
            }
            switch (alt76) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:655:4: postfixExpression
                    {
                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression6423);
                    postfixExpression80=postfixExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = postfixExpression80; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:656:4: unaryOperator postfixExpression
                    {
                    pushFollow(FOLLOW_unaryOperator_in_unaryExpression6434);
                    unaryOperator81=unaryOperator();
                    _fsp--;
                    if (failed) return expr;
                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression6438);
                    postfixExpression82=postfixExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.Unary(unaryOperator81, postfixExpression82); 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:658:1: postfixExpression returns [MTExpression expr] : primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* ;
    public final MTExpression postfixExpression() throws RecognitionException {
        MTExpression expr = null;

        Token DOT84=null;
        Token LPAREN85=null;
        name_return name1 = null;

        MTExpression primaryExpression83 = null;

        ListBuffer<MTExpression> expressionListOpt86 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:659:2: ( primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:659:4: primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            {
            pushFollow(FOLLOW_primaryExpression_in_postfixExpression6458);
            primaryExpression83=primaryExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = primaryExpression83; 
            }
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:660:5: ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            loop80:
            do {
                int alt80=3;
                int LA80_0 = input.LA(1);

                if ( (LA80_0==LBRACKET) ) {
                    int LA80_62 = input.LA(2);

                    if ( (synpred141()) ) {
                        alt80=2;
                    }


                }
                else if ( (LA80_0==DOT) ) {
                    int LA80_64 = input.LA(2);

                    if ( (synpred139()) ) {
                        alt80=1;
                    }


                }


                switch (alt80) {
            	case 1 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:660:7: DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    {
            	    DOT84=(Token)input.LT(1);
            	    match(input,DOT,FOLLOW_DOT_in_postfixExpression6473); if (failed) return expr;
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:660:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
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
            	            new NoViableAltException("660:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )", 78, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt78) {
            	        case 1 :
            	            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:660:13: CLASS
            	            {
            	            match(input,CLASS,FOLLOW_CLASS_in_postfixExpression6477); if (failed) return expr;

            	            }
            	            break;
            	        case 2 :
            	            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:661:13: name1= name ( LPAREN expressionListOpt RPAREN )*
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression6501);
            	            name1=name();
            	            _fsp--;
            	            if (failed) return expr;
            	            if ( backtracking==0 ) {
            	               expr = F.at(pos(DOT84)).Select(expr, name1.value); 
            	            }
            	            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:662:14: ( LPAREN expressionListOpt RPAREN )*
            	            loop77:
            	            do {
            	                int alt77=2;
            	                int LA77_0 = input.LA(1);

            	                if ( (LA77_0==LPAREN) ) {
            	                    int LA77_60 = input.LA(2);

            	                    if ( (synpred138()) ) {
            	                        alt77=1;
            	                    }


            	                }


            	                switch (alt77) {
            	            	case 1 :
            	            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:662:16: LPAREN expressionListOpt RPAREN
            	            	    {
            	            	    LPAREN85=(Token)input.LT(1);
            	            	    match(input,LPAREN,FOLLOW_LPAREN_in_postfixExpression6526); if (failed) return expr;
            	            	    pushFollow(FOLLOW_expressionListOpt_in_postfixExpression6528);
            	            	    expressionListOpt86=expressionListOpt();
            	            	    _fsp--;
            	            	    if (failed) return expr;
            	            	    match(input,RPAREN,FOLLOW_RPAREN_in_postfixExpression6530); if (failed) return expr;
            	            	    if ( backtracking==0 ) {
            	            	       expr = F.at(pos(LPAREN85)).Apply(null, expr, expressionListOpt86.toList()); 
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
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:664:7: LBRACKET ( name BAR )? expression RBRACKET
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_postfixExpression6562); if (failed) return expr;
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:664:16: ( name BAR )?
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
            	            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:664:17: name BAR
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression6565);
            	            name();
            	            _fsp--;
            	            if (failed) return expr;
            	            match(input,BAR,FOLLOW_BAR_in_postfixExpression6567); if (failed) return expr;

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_expression_in_postfixExpression6571);
            	    expression();
            	    _fsp--;
            	    if (failed) return expr;
            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_postfixExpression6574); if (failed) return expr;

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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:666:1: primaryExpression returns [MTExpression expr] : ( newExpression | typeName LBRACE objectLiteral RBRACE | bracketExpression | ordinalExpression | contextExpression | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | LPAREN expression RPAREN );
    public final MTExpression primaryExpression() throws RecognitionException {
        MTExpression expr = null;

        Token LBRACE88=null;
        Token THIS91=null;
        Token SUPER92=null;
        Token LPAREN94=null;
        Token LPAREN98=null;
        MTExpression newExpression87 = null;

        MTExpression typeName89 = null;

        ListBuffer<MTStatement> objectLiteral90 = null;

        MTIdent identifier93 = null;

        ListBuffer<MTExpression> expressionListOpt95 = null;

        MTExpression stringExpression96 = null;

        MTExpression literal97 = null;

        MTExpression expression99 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:667:2: ( newExpression | typeName LBRACE objectLiteral RBRACE | bracketExpression | ordinalExpression | contextExpression | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | LPAREN expression RPAREN )
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
                        new NoViableAltException("666:1: primaryExpression returns [MTExpression expr] : ( newExpression | typeName LBRACE objectLiteral RBRACE | bracketExpression | ordinalExpression | contextExpression | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | LPAREN expression RPAREN );", 82, 2, input);

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
            case DECIMAL_LITERAL:
            case OCTAL_LITERAL:
            case HEX_LITERAL:
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
                    new NoViableAltException("666:1: primaryExpression returns [MTExpression expr] : ( newExpression | typeName LBRACE objectLiteral RBRACE | bracketExpression | ordinalExpression | contextExpression | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal | LPAREN expression RPAREN );", 82, 0, input);

                throw nvae;
            }

            switch (alt82) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:667:4: newExpression
                    {
                    pushFollow(FOLLOW_newExpression_in_primaryExpression6599);
                    newExpression87=newExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = newExpression87; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:668:4: typeName LBRACE objectLiteral RBRACE
                    {
                    pushFollow(FOLLOW_typeName_in_primaryExpression6611);
                    typeName89=typeName();
                    _fsp--;
                    if (failed) return expr;
                    LBRACE88=(Token)input.LT(1);
                    match(input,LBRACE,FOLLOW_LBRACE_in_primaryExpression6613); if (failed) return expr;
                    pushFollow(FOLLOW_objectLiteral_in_primaryExpression6616);
                    objectLiteral90=objectLiteral();
                    _fsp--;
                    if (failed) return expr;
                    match(input,RBRACE,FOLLOW_RBRACE_in_primaryExpression6618); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(LBRACE88)).PureObjectLiteral(typeName89, objectLiteral90.toList()); 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:669:4: bracketExpression
                    {
                    pushFollow(FOLLOW_bracketExpression_in_primaryExpression6628);
                    bracketExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:671:4: ordinalExpression
                    {
                    pushFollow(FOLLOW_ordinalExpression_in_primaryExpression6643);
                    ordinalExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:672:10: contextExpression
                    {
                    pushFollow(FOLLOW_contextExpression_in_primaryExpression6655);
                    contextExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:673:10: THIS
                    {
                    THIS91=(Token)input.LT(1);
                    match(input,THIS,FOLLOW_THIS_in_primaryExpression6667); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(THIS91)).Identifier(names._this); 
                    }

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:674:10: SUPER
                    {
                    SUPER92=(Token)input.LT(1);
                    match(input,SUPER,FOLLOW_SUPER_in_primaryExpression6686); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(SUPER92)).Identifier(names._super); 
                    }

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:675:10: identifier ( LPAREN expressionListOpt RPAREN )*
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression6705);
                    identifier93=identifier();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = identifier93; 
                    }
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:676:10: ( LPAREN expressionListOpt RPAREN )*
                    loop81:
                    do {
                        int alt81=2;
                        int LA81_0 = input.LA(1);

                        if ( (LA81_0==LPAREN) ) {
                            int LA81_62 = input.LA(2);

                            if ( (synpred149()) ) {
                                alt81=1;
                            }


                        }


                        switch (alt81) {
                    	case 1 :
                    	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:676:12: LPAREN expressionListOpt RPAREN
                    	    {
                    	    LPAREN94=(Token)input.LT(1);
                    	    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression6726); if (failed) return expr;
                    	    pushFollow(FOLLOW_expressionListOpt_in_primaryExpression6730);
                    	    expressionListOpt95=expressionListOpt();
                    	    _fsp--;
                    	    if (failed) return expr;
                    	    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression6734); if (failed) return expr;
                    	    if ( backtracking==0 ) {
                    	       expr = F.at(pos(LPAREN94)).Apply(null, expr, expressionListOpt95.toList()); 
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
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:677:10: stringExpression
                    {
                    pushFollow(FOLLOW_stringExpression_in_primaryExpression6753);
                    stringExpression96=stringExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = stringExpression96; 
                    }

                    }
                    break;
                case 10 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:678:10: literal
                    {
                    pushFollow(FOLLOW_literal_in_primaryExpression6771);
                    literal97=literal();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = literal97; 
                    }

                    }
                    break;
                case 11 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:679:10: LPAREN expression RPAREN
                    {
                    LPAREN98=(Token)input.LT(1);
                    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression6790); if (failed) return expr;
                    pushFollow(FOLLOW_expression_in_primaryExpression6792);
                    expression99=expression();
                    _fsp--;
                    if (failed) return expr;
                    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression6794); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(LPAREN98)).Parens(expression99); 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:681:1: newExpression returns [MTExpression expr] : NEW typeName ( LPAREN expressionListOpt RPAREN )? ;
    public final MTExpression newExpression() throws RecognitionException {
        MTExpression expr = null;

        Token NEW101=null;
        ListBuffer<MTExpression> expressionListOpt100 = null;

        MTExpression typeName102 = null;


         ListBuffer<MTExpression> args = null; 
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:683:2: ( NEW typeName ( LPAREN expressionListOpt RPAREN )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:683:4: NEW typeName ( LPAREN expressionListOpt RPAREN )?
            {
            NEW101=(Token)input.LT(1);
            match(input,NEW,FOLLOW_NEW_in_newExpression6826); if (failed) return expr;
            pushFollow(FOLLOW_typeName_in_newExpression6829);
            typeName102=typeName();
            _fsp--;
            if (failed) return expr;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:684:3: ( LPAREN expressionListOpt RPAREN )?
            int alt83=2;
            int LA83_0 = input.LA(1);

            if ( (LA83_0==LPAREN) ) {
                int LA83_1 = input.LA(2);

                if ( (synpred153()) ) {
                    alt83=1;
                }
            }
            switch (alt83) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:684:5: LPAREN expressionListOpt RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_newExpression6837); if (failed) return expr;
                    pushFollow(FOLLOW_expressionListOpt_in_newExpression6841);
                    expressionListOpt100=expressionListOpt();
                    _fsp--;
                    if (failed) return expr;
                    match(input,RPAREN,FOLLOW_RPAREN_in_newExpression6845); if (failed) return expr;
                    if ( backtracking==0 ) {
                       args = expressionListOpt100; 
                    }

                    }
                    break;

            }

            if ( backtracking==0 ) {
               expr = F.at(pos(NEW101)).Instanciate(null, typeName102, 
              												(args==null? new ListBuffer<MTExpression>() : args).toList(), null); 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:689:1: objectLiteral returns [ListBuffer<MTStatement> parts = new ListBuffer<MTStatement>()] : ( objectLiteralPart )* ;
    public final ListBuffer<MTStatement> objectLiteral() throws RecognitionException {
        ListBuffer<MTStatement> parts =  new ListBuffer<MTStatement>();

        MTStatement objectLiteralPart103 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:690:2: ( ( objectLiteralPart )* )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:690:4: ( objectLiteralPart )*
            {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:690:4: ( objectLiteralPart )*
            loop84:
            do {
                int alt84=2;
                int LA84_0 = input.LA(1);

                if ( (LA84_0==ATTRIBUTE||LA84_0==VAR||LA84_0==TRIGGER||(LA84_0>=OPERATION && LA84_0<=FUNCTION)||LA84_0==QUOTED_IDENTIFIER||LA84_0==IDENTIFIER) ) {
                    alt84=1;
                }


                switch (alt84) {
            	case 1 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:690:6: objectLiteralPart
            	    {
            	    pushFollow(FOLLOW_objectLiteralPart_in_objectLiteral6885);
            	    objectLiteralPart103=objectLiteralPart();
            	    _fsp--;
            	    if (failed) return parts;
            	    if ( backtracking==0 ) {
            	       parts.append(objectLiteralPart103); 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:691:1: objectLiteralPart returns [MTStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | ATTRIBUTE name typeReference EQ bindOpt expression SEMI | localOperationDefinition | localFunctionDefinition | localTriggerStatement | variableDefinition );
    public final MTStatement objectLiteralPart() throws RecognitionException {
        MTStatement value = null;

        Token COLON104=null;
        name_return name105 = null;

        MTExpression expression106 = null;

        JavafxBindStatus bindOpt107 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:692:2: ( name COLON bindOpt expression ( COMMA | SEMI )? | ATTRIBUTE name typeReference EQ bindOpt expression SEMI | localOperationDefinition | localFunctionDefinition | localTriggerStatement | variableDefinition )
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
                        new NoViableAltException("691:1: objectLiteralPart returns [MTStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | ATTRIBUTE name typeReference EQ bindOpt expression SEMI | localOperationDefinition | localFunctionDefinition | localTriggerStatement | variableDefinition );", 86, 1, input);

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
                    new NoViableAltException("691:1: objectLiteralPart returns [MTStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | ATTRIBUTE name typeReference EQ bindOpt expression SEMI | localOperationDefinition | localFunctionDefinition | localTriggerStatement | variableDefinition );", 86, 0, input);

                throw nvae;
            }

            switch (alt86) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:692:4: name COLON bindOpt expression ( COMMA | SEMI )?
                    {
                    pushFollow(FOLLOW_name_in_objectLiteralPart6911);
                    name105=name();
                    _fsp--;
                    if (failed) return value;
                    COLON104=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_objectLiteralPart6913); if (failed) return value;
                    pushFollow(FOLLOW_bindOpt_in_objectLiteralPart6916);
                    bindOpt107=bindOpt();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_expression_in_objectLiteralPart6918);
                    expression106=expression();
                    _fsp--;
                    if (failed) return value;
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:692:35: ( COMMA | SEMI )?
                    int alt85=2;
                    int LA85_0 = input.LA(1);

                    if ( ((LA85_0>=SEMI && LA85_0<=COMMA)) ) {
                        alt85=1;
                    }
                    switch (alt85) {
                        case 1 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:
                            {
                            if ( (input.LA(1)>=SEMI && input.LA(1)<=COMMA) ) {
                                input.consume();
                                errorRecovery=false;failed=false;
                            }
                            else {
                                if (backtracking>0) {failed=true; return value;}
                                MismatchedSetException mse =
                                    new MismatchedSetException(null,input);
                                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_objectLiteralPart6920);    throw mse;
                            }


                            }
                            break;

                    }

                    if ( backtracking==0 ) {
                       value = F.at(pos(COLON104)).ObjectLiteralPart(name105.value, expression106, bindOpt107); 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:693:10: ATTRIBUTE name typeReference EQ bindOpt expression SEMI
                    {
                    match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_objectLiteralPart6940); if (failed) return value;
                    pushFollow(FOLLOW_name_in_objectLiteralPart6944);
                    name();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_typeReference_in_objectLiteralPart6948);
                    typeReference();
                    _fsp--;
                    if (failed) return value;
                    match(input,EQ,FOLLOW_EQ_in_objectLiteralPart6952); if (failed) return value;
                    pushFollow(FOLLOW_bindOpt_in_objectLiteralPart6955);
                    bindOpt();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_expression_in_objectLiteralPart6957);
                    expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_objectLiteralPart6961); if (failed) return value;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:694:10: localOperationDefinition
                    {
                    pushFollow(FOLLOW_localOperationDefinition_in_objectLiteralPart6973);
                    localOperationDefinition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:695:10: localFunctionDefinition
                    {
                    pushFollow(FOLLOW_localFunctionDefinition_in_objectLiteralPart6985);
                    localFunctionDefinition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:696:10: localTriggerStatement
                    {
                    pushFollow(FOLLOW_localTriggerStatement_in_objectLiteralPart6997);
                    localTriggerStatement();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:697:10: variableDefinition
                    {
                    pushFollow(FOLLOW_variableDefinition_in_objectLiteralPart7009);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:698:1: bracketExpression : LBRACKET ( | expression ( | COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* ) | BAR generator ( COMMA ( generator | expression ) )* | DOTDOT ( LT )? expression ) ) RBRACKET ;
    public final void bracketExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:699:2: ( LBRACKET ( | expression ( | COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* ) | BAR generator ( COMMA ( generator | expression ) )* | DOTDOT ( LT )? expression ) ) RBRACKET )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:699:4: LBRACKET ( | expression ( | COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* ) | BAR generator ( COMMA ( generator | expression ) )* | DOTDOT ( LT )? expression ) ) RBRACKET
            {
            match(input,LBRACKET,FOLLOW_LBRACKET_in_bracketExpression7019); if (failed) return ;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:700:3: ( | expression ( | COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* ) | BAR generator ( COMMA ( generator | expression ) )* | DOTDOT ( LT )? expression ) )
            int alt94=2;
            int LA94_0 = input.LA(1);

            if ( (LA94_0==RBRACKET) ) {
                alt94=1;
            }
            else if ( ((LA94_0>=POUND && LA94_0<=TYPEOF)||LA94_0==IF||(LA94_0>=THIS && LA94_0<=FALSE)||LA94_0==UNITINTERVAL||LA94_0==FOREACH||(LA94_0>=NOT && LA94_0<=NEW)||(LA94_0>=OPERATION && LA94_0<=FUNCTION)||(LA94_0>=INDEXOF && LA94_0<=SUPER)||(LA94_0>=SIZEOF && LA94_0<=REVERSE)||LA94_0==LPAREN||LA94_0==LBRACKET||LA94_0==DOT||(LA94_0>=PLUSPLUS && LA94_0<=SUBSUB)||(LA94_0>=QUES && LA94_0<=STRING_LITERAL)||LA94_0==QUOTE_LBRACE_STRING_LITERAL||(LA94_0>=QUOTED_IDENTIFIER && LA94_0<=OCTAL_LITERAL)||LA94_0==HEX_LITERAL||LA94_0==FLOATING_POINT_LITERAL||LA94_0==IDENTIFIER) ) {
                alt94=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("700:3: ( | expression ( | COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* ) | BAR generator ( COMMA ( generator | expression ) )* | DOTDOT ( LT )? expression ) )", 94, 0, input);

                throw nvae;
            }
            switch (alt94) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:701:3: 
                    {
                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:701:5: expression ( | COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* ) | BAR generator ( COMMA ( generator | expression ) )* | DOTDOT ( LT )? expression )
                    {
                    pushFollow(FOLLOW_expression_in_bracketExpression7034);
                    expression();
                    _fsp--;
                    if (failed) return ;
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:702:9: ( | COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* ) | BAR generator ( COMMA ( generator | expression ) )* | DOTDOT ( LT )? expression )
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
                            new NoViableAltException("702:9: ( | COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* ) | BAR generator ( COMMA ( generator | expression ) )* | DOTDOT ( LT )? expression )", 93, 0, input);

                        throw nvae;
                    }

                    switch (alt93) {
                        case 1 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:703:9: 
                            {
                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:703:11: COMMA expression ( DOTDOT ( LT )? expression | ( COMMA expression )* )
                            {
                            match(input,COMMA,FOLLOW_COMMA_in_bracketExpression7061); if (failed) return ;
                            pushFollow(FOLLOW_expression_in_bracketExpression7063);
                            expression();
                            _fsp--;
                            if (failed) return ;
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:704:10: ( DOTDOT ( LT )? expression | ( COMMA expression )* )
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
                                    new NoViableAltException("704:10: ( DOTDOT ( LT )? expression | ( COMMA expression )* )", 89, 0, input);

                                throw nvae;
                            }
                            switch (alt89) {
                                case 1 :
                                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:704:12: DOTDOT ( LT )? expression
                                    {
                                    match(input,DOTDOT,FOLLOW_DOTDOT_in_bracketExpression7077); if (failed) return ;
                                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:704:21: ( LT )?
                                    int alt87=2;
                                    int LA87_0 = input.LA(1);

                                    if ( (LA87_0==LT) ) {
                                        alt87=1;
                                    }
                                    switch (alt87) {
                                        case 1 :
                                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:0:0: LT
                                            {
                                            match(input,LT,FOLLOW_LT_in_bracketExpression7081); if (failed) return ;

                                            }
                                            break;

                                    }

                                    pushFollow(FOLLOW_expression_in_bracketExpression7084);
                                    expression();
                                    _fsp--;
                                    if (failed) return ;

                                    }
                                    break;
                                case 2 :
                                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:705:12: ( COMMA expression )*
                                    {
                                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:705:12: ( COMMA expression )*
                                    loop88:
                                    do {
                                        int alt88=2;
                                        int LA88_0 = input.LA(1);

                                        if ( (LA88_0==COMMA) ) {
                                            alt88=1;
                                        }


                                        switch (alt88) {
                                    	case 1 :
                                    	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:705:13: COMMA expression
                                    	    {
                                    	    match(input,COMMA,FOLLOW_COMMA_in_bracketExpression7098); if (failed) return ;
                                    	    pushFollow(FOLLOW_expression_in_bracketExpression7102);
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
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:707:11: BAR generator ( COMMA ( generator | expression ) )*
                            {
                            match(input,BAR,FOLLOW_BAR_in_bracketExpression7129); if (failed) return ;
                            pushFollow(FOLLOW_generator_in_bracketExpression7133);
                            generator();
                            _fsp--;
                            if (failed) return ;
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:707:29: ( COMMA ( generator | expression ) )*
                            loop91:
                            do {
                                int alt91=2;
                                int LA91_0 = input.LA(1);

                                if ( (LA91_0==COMMA) ) {
                                    alt91=1;
                                }


                                switch (alt91) {
                            	case 1 :
                            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:707:30: COMMA ( generator | expression )
                            	    {
                            	    match(input,COMMA,FOLLOW_COMMA_in_bracketExpression7138); if (failed) return ;
                            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:707:38: ( generator | expression )
                            	    int alt90=2;
                            	    int LA90_0 = input.LA(1);

                            	    if ( (LA90_0==QUOTED_IDENTIFIER||LA90_0==IDENTIFIER) ) {
                            	        int LA90_1 = input.LA(2);

                            	        if ( (LA90_1==AND||LA90_1==DUR||LA90_1==IN||(LA90_1>=ORDER && LA90_1<=INSTANCEOF)||LA90_1==OR||LA90_1==LPAREN||(LA90_1>=LBRACKET && LA90_1<=RBRACKET)||(LA90_1>=COMMA && LA90_1<=PERCENTEQ)||LA90_1==LBRACE) ) {
                            	            alt90=2;
                            	        }
                            	        else if ( (LA90_1==LARROW) ) {
                            	            alt90=1;
                            	        }
                            	        else {
                            	            if (backtracking>0) {failed=true; return ;}
                            	            NoViableAltException nvae =
                            	                new NoViableAltException("707:38: ( generator | expression )", 90, 1, input);

                            	            throw nvae;
                            	        }
                            	    }
                            	    else if ( ((LA90_0>=POUND && LA90_0<=TYPEOF)||LA90_0==IF||(LA90_0>=THIS && LA90_0<=FALSE)||LA90_0==UNITINTERVAL||LA90_0==FOREACH||(LA90_0>=NOT && LA90_0<=NEW)||(LA90_0>=OPERATION && LA90_0<=FUNCTION)||(LA90_0>=INDEXOF && LA90_0<=SUPER)||(LA90_0>=SIZEOF && LA90_0<=REVERSE)||LA90_0==LPAREN||LA90_0==LBRACKET||LA90_0==DOT||(LA90_0>=PLUSPLUS && LA90_0<=SUBSUB)||(LA90_0>=QUES && LA90_0<=STRING_LITERAL)||LA90_0==QUOTE_LBRACE_STRING_LITERAL||(LA90_0>=DECIMAL_LITERAL && LA90_0<=OCTAL_LITERAL)||LA90_0==HEX_LITERAL||LA90_0==FLOATING_POINT_LITERAL) ) {
                            	        alt90=2;
                            	    }
                            	    else {
                            	        if (backtracking>0) {failed=true; return ;}
                            	        NoViableAltException nvae =
                            	            new NoViableAltException("707:38: ( generator | expression )", 90, 0, input);

                            	        throw nvae;
                            	    }
                            	    switch (alt90) {
                            	        case 1 :
                            	            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:707:39: generator
                            	            {
                            	            pushFollow(FOLLOW_generator_in_bracketExpression7143);
                            	            generator();
                            	            _fsp--;
                            	            if (failed) return ;

                            	            }
                            	            break;
                            	        case 2 :
                            	            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:707:51: expression
                            	            {
                            	            pushFollow(FOLLOW_expression_in_bracketExpression7147);
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
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:708:11: DOTDOT ( LT )? expression
                            {
                            match(input,DOTDOT,FOLLOW_DOTDOT_in_bracketExpression7166); if (failed) return ;
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:708:20: ( LT )?
                            int alt92=2;
                            int LA92_0 = input.LA(1);

                            if ( (LA92_0==LT) ) {
                                alt92=1;
                            }
                            switch (alt92) {
                                case 1 :
                                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:0:0: LT
                                    {
                                    match(input,LT,FOLLOW_LT_in_bracketExpression7170); if (failed) return ;

                                    }
                                    break;

                            }

                            pushFollow(FOLLOW_expression_in_bracketExpression7174);
                            expression();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }

            match(input,RBRACKET,FOLLOW_RBRACKET_in_bracketExpression7196); if (failed) return ;

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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:713:1: generator : name LARROW expression ;
    public final void generator() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:713:11: ( name LARROW expression )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:713:13: name LARROW expression
            {
            pushFollow(FOLLOW_name_in_generator7206);
            name();
            _fsp--;
            if (failed) return ;
            match(input,LARROW,FOLLOW_LARROW_in_generator7210); if (failed) return ;
            pushFollow(FOLLOW_expression_in_generator7214);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:714:1: ordinalExpression : INDEXOF ( name | DOT ) ;
    public final void ordinalExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:714:19: ( INDEXOF ( name | DOT ) )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:714:21: INDEXOF ( name | DOT )
            {
            match(input,INDEXOF,FOLLOW_INDEXOF_in_ordinalExpression7222); if (failed) return ;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:714:31: ( name | DOT )
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
                    new NoViableAltException("714:31: ( name | DOT )", 95, 0, input);

                throw nvae;
            }
            switch (alt95) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:714:35: name
                    {
                    pushFollow(FOLLOW_name_in_ordinalExpression7230);
                    name();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:714:46: DOT
                    {
                    match(input,DOT,FOLLOW_DOT_in_ordinalExpression7238); if (failed) return ;

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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:715:1: contextExpression : DOT ;
    public final void contextExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:715:19: ( DOT )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:715:21: DOT
            {
            match(input,DOT,FOLLOW_DOT_in_contextExpression7250); if (failed) return ;

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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:716:1: stringExpression returns [MTExpression expr] : ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL ;
    public final MTExpression stringExpression() throws RecognitionException {
        MTExpression expr = null;

        Token ql=null;
        Token rl=null;
        Token rq=null;
        MTExpression f1 = null;

        MTExpression e1 = null;

        MTExpression fn = null;

        MTExpression en = null;


         ListBuffer<MTExpression> strexp = new ListBuffer<MTExpression>(); 
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:718:2: (ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:718:4: ql= QUOTE_LBRACE_STRING_LITERAL f1= formatOrNull e1= expression (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )* rq= RBRACE_QUOTE_STRING_LITERAL
            {
            ql=(Token)input.LT(1);
            match(input,QUOTE_LBRACE_STRING_LITERAL,FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression7272); if (failed) return expr;
            if ( backtracking==0 ) {
               strexp.append(F.at(pos(ql)).Literal(TypeTags.CLASS, ql.getText())); 
            }
            pushFollow(FOLLOW_formatOrNull_in_stringExpression7281);
            f1=formatOrNull();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               strexp.append(f1); 
            }
            pushFollow(FOLLOW_expression_in_stringExpression7292);
            e1=expression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               strexp.append(e1); 
            }
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:721:4: (rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression )*
            loop96:
            do {
                int alt96=2;
                int LA96_0 = input.LA(1);

                if ( (LA96_0==RBRACE_LBRACE_STRING_LITERAL) ) {
                    alt96=1;
                }


                switch (alt96) {
            	case 1 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:721:7: rl= RBRACE_LBRACE_STRING_LITERAL fn= formatOrNull en= expression
            	    {
            	    rl=(Token)input.LT(1);
            	    match(input,RBRACE_LBRACE_STRING_LITERAL,FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression7307); if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       strexp.append(F.at(pos(rl)).Literal(TypeTags.CLASS, rl.getText())); 
            	    }
            	    pushFollow(FOLLOW_formatOrNull_in_stringExpression7319);
            	    fn=formatOrNull();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       strexp.append(fn); 
            	    }
            	    pushFollow(FOLLOW_expression_in_stringExpression7333);
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
            match(input,RBRACE_QUOTE_STRING_LITERAL,FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression7354); if (failed) return expr;
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:728:1: formatOrNull returns [MTExpression expr] : (fs= FORMAT_STRING_LITERAL | );
    public final MTExpression formatOrNull() throws RecognitionException {
        MTExpression expr = null;

        Token fs=null;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:729:2: (fs= FORMAT_STRING_LITERAL | )
            int alt97=2;
            int LA97_0 = input.LA(1);

            if ( (LA97_0==FORMAT_STRING_LITERAL) ) {
                alt97=1;
            }
            else if ( ((LA97_0>=POUND && LA97_0<=TYPEOF)||LA97_0==IF||(LA97_0>=THIS && LA97_0<=FALSE)||LA97_0==UNITINTERVAL||LA97_0==FOREACH||(LA97_0>=NOT && LA97_0<=NEW)||(LA97_0>=OPERATION && LA97_0<=FUNCTION)||(LA97_0>=INDEXOF && LA97_0<=SUPER)||(LA97_0>=SIZEOF && LA97_0<=REVERSE)||LA97_0==LPAREN||LA97_0==LBRACKET||LA97_0==DOT||(LA97_0>=PLUSPLUS && LA97_0<=SUBSUB)||(LA97_0>=QUES && LA97_0<=STRING_LITERAL)||LA97_0==QUOTE_LBRACE_STRING_LITERAL||(LA97_0>=QUOTED_IDENTIFIER && LA97_0<=OCTAL_LITERAL)||LA97_0==HEX_LITERAL||LA97_0==FLOATING_POINT_LITERAL||LA97_0==IDENTIFIER) ) {
                alt97=2;
            }
            else {
                if (backtracking>0) {failed=true; return expr;}
                NoViableAltException nvae =
                    new NoViableAltException("728:1: formatOrNull returns [MTExpression expr] : (fs= FORMAT_STRING_LITERAL | );", 97, 0, input);

                throw nvae;
            }
            switch (alt97) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:729:4: fs= FORMAT_STRING_LITERAL
                    {
                    fs=(Token)input.LT(1);
                    match(input,FORMAT_STRING_LITERAL,FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull7384); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(fs)).Literal(TypeTags.CLASS, fs.getText()); 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:730:22: 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:732:1: expressionListOpt returns [ListBuffer<MTExpression> args = new ListBuffer<MTExpression>()] : (e1= expression ( COMMA e= expression )* )? ;
    public final ListBuffer<MTExpression> expressionListOpt() throws RecognitionException {
        ListBuffer<MTExpression> args =  new ListBuffer<MTExpression>();

        MTExpression e1 = null;

        MTExpression e = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:733:2: ( (e1= expression ( COMMA e= expression )* )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:733:4: (e1= expression ( COMMA e= expression )* )?
            {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:733:4: (e1= expression ( COMMA e= expression )* )?
            int alt99=2;
            int LA99_0 = input.LA(1);

            if ( ((LA99_0>=POUND && LA99_0<=TYPEOF)||LA99_0==IF||(LA99_0>=THIS && LA99_0<=FALSE)||LA99_0==UNITINTERVAL||LA99_0==FOREACH||(LA99_0>=NOT && LA99_0<=NEW)||(LA99_0>=OPERATION && LA99_0<=FUNCTION)||(LA99_0>=INDEXOF && LA99_0<=SUPER)||(LA99_0>=SIZEOF && LA99_0<=REVERSE)||LA99_0==LPAREN||LA99_0==LBRACKET||LA99_0==DOT||(LA99_0>=PLUSPLUS && LA99_0<=SUBSUB)||(LA99_0>=QUES && LA99_0<=STRING_LITERAL)||LA99_0==QUOTE_LBRACE_STRING_LITERAL||(LA99_0>=QUOTED_IDENTIFIER && LA99_0<=OCTAL_LITERAL)||LA99_0==HEX_LITERAL||LA99_0==FLOATING_POINT_LITERAL||LA99_0==IDENTIFIER) ) {
                alt99=1;
            }
            switch (alt99) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:733:6: e1= expression ( COMMA e= expression )*
                    {
                    pushFollow(FOLLOW_expression_in_expressionListOpt7416);
                    e1=expression();
                    _fsp--;
                    if (failed) return args;
                    if ( backtracking==0 ) {
                       args.append(e1); 
                    }
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:734:6: ( COMMA e= expression )*
                    loop98:
                    do {
                        int alt98=2;
                        int LA98_0 = input.LA(1);

                        if ( (LA98_0==COMMA) ) {
                            alt98=1;
                        }


                        switch (alt98) {
                    	case 1 :
                    	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:734:7: COMMA e= expression
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_expressionListOpt7427); if (failed) return args;
                    	    pushFollow(FOLLOW_expression_in_expressionListOpt7433);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:735:1: orderBy returns [MTExpression expr] : ORDER BY expression ;
    public final MTExpression orderBy() throws RecognitionException {
        MTExpression expr = null;

        MTExpression expression108 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:736:2: ( ORDER BY expression )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:736:4: ORDER BY expression
            {
            match(input,ORDER,FOLLOW_ORDER_in_orderBy7455); if (failed) return expr;
            match(input,BY,FOLLOW_BY_in_orderBy7459); if (failed) return expr;
            pushFollow(FOLLOW_expression_in_orderBy7463);
            expression108=expression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = expression108; 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:737:1: indexOn returns [MTExpression expr = null] : INDEX ON name ( COMMA name )* ;
    public final MTExpression indexOn() throws RecognitionException {
        MTExpression expr =  null;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:738:2: ( INDEX ON name ( COMMA name )* )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:738:4: INDEX ON name ( COMMA name )*
            {
            match(input,INDEX,FOLLOW_INDEX_in_indexOn7478); if (failed) return expr;
            match(input,ON,FOLLOW_ON_in_indexOn7482); if (failed) return expr;
            pushFollow(FOLLOW_name_in_indexOn7486);
            name();
            _fsp--;
            if (failed) return expr;
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:738:24: ( COMMA name )*
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
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:738:28: COMMA name
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_indexOn7494); if (failed) return expr;
            	    pushFollow(FOLLOW_name_in_indexOn7498);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:739:1: multiplyOperator : ( STAR | SLASH | PERCENT );
    public final void multiplyOperator() throws RecognitionException {
        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:739:18: ( STAR | SLASH | PERCENT )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:740:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );
    public final int unaryOperator() throws RecognitionException {
        int optag = 0;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:741:2: ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB )
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
                    new NoViableAltException("740:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );", 101, 0, input);

                throw nvae;
            }

            switch (alt101) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:741:4: POUND
                    {
                    match(input,POUND,FOLLOW_POUND_in_unaryOperator7542); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:742:4: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_unaryOperator7553); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:743:4: SUB
                    {
                    match(input,SUB,FOLLOW_SUB_in_unaryOperator7566); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = MTTree.NEG; 
                    }

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:744:4: NOT
                    {
                    match(input,NOT,FOLLOW_NOT_in_unaryOperator7579); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = MTTree.NOT; 
                    }

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:745:4: SIZEOF
                    {
                    match(input,SIZEOF,FOLLOW_SIZEOF_in_unaryOperator7592); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:746:4: TYPEOF
                    {
                    match(input,TYPEOF,FOLLOW_TYPEOF_in_unaryOperator7605); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:747:4: REVERSE
                    {
                    match(input,REVERSE,FOLLOW_REVERSE_in_unaryOperator7618); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:748:4: PLUSPLUS
                    {
                    match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_unaryOperator7631); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:749:4: SUBSUB
                    {
                    match(input,SUBSUB,FOLLOW_SUBSUB_in_unaryOperator7644); if (failed) return optag;
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:751:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );
    public final int assignmentOperator() throws RecognitionException {
        int optag = 0;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:752:2: ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ )
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
                    new NoViableAltException("751:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );", 102, 0, input);

                throw nvae;
            }

            switch (alt102) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:752:4: PLUSEQ
                    {
                    match(input,PLUSEQ,FOLLOW_PLUSEQ_in_assignmentOperator7665); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = MTTree.PLUS_ASG; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:753:4: SUBEQ
                    {
                    match(input,SUBEQ,FOLLOW_SUBEQ_in_assignmentOperator7678); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = MTTree.MINUS_ASG; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:754:4: STAREQ
                    {
                    match(input,STAREQ,FOLLOW_STAREQ_in_assignmentOperator7691); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = MTTree.MUL_ASG; 
                    }

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:755:4: SLASHEQ
                    {
                    match(input,SLASHEQ,FOLLOW_SLASHEQ_in_assignmentOperator7704); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = MTTree.DIV_ASG; 
                    }

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:756:4: PERCENTEQ
                    {
                    match(input,PERCENTEQ,FOLLOW_PERCENTEQ_in_assignmentOperator7717); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = MTTree.MOD_ASG; 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:758:1: typeReference returns [MTType type] : ( COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | typeName ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? ;
    public final MTType typeReference() throws RecognitionException {
        MTType type = null;

        Token STAR110=null;
        int ccf = 0;

        int ccn = 0;

        int ccs = 0;

        MTExpression typeName109 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:759:2: ( ( COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | typeName ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:759:4: ( COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | typeName ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:759:4: ( COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | typeName ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            int alt105=2;
            int LA105_0 = input.LA(1);

            if ( (LA105_0==COLON) ) {
                alt105=1;
            }
            switch (alt105) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:759:6: COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | typeName ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    {
                    match(input,COLON,FOLLOW_COLON_in_typeReference7741); if (failed) return type;
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:759:13: ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | typeName ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
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
                            new NoViableAltException("759:13: ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | typeName ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )", 104, 0, input);

                        throw nvae;
                    }

                    switch (alt104) {
                        case 1 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:760:22: ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint )
                            {
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:760:22: ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint )
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:760:55: ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint
                            {
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:760:55: ( FUNCTION | OPERATION )?
                            int alt103=2;
                            int LA103_0 = input.LA(1);

                            if ( ((LA103_0>=OPERATION && LA103_0<=FUNCTION)) ) {
                                alt103=1;
                            }
                            switch (alt103) {
                                case 1 :
                                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:
                                    {
                                    if ( (input.LA(1)>=OPERATION && input.LA(1)<=FUNCTION) ) {
                                        input.consume();
                                        errorRecovery=false;failed=false;
                                    }
                                    else {
                                        if (backtracking>0) {failed=true; return type;}
                                        MismatchedSetException mse =
                                            new MismatchedSetException(null,input);
                                        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_typeReference7781);    throw mse;
                                    }


                                    }
                                    break;

                            }

                            pushFollow(FOLLOW_formalParameters_in_typeReference7790);
                            formalParameters();
                            _fsp--;
                            if (failed) return type;
                            pushFollow(FOLLOW_typeReference_in_typeReference7792);
                            typeReference();
                            _fsp--;
                            if (failed) return type;
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference7796);
                            ccf=cardinalityConstraint();
                            _fsp--;
                            if (failed) return type;

                            }


                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:762:22: typeName ccn= cardinalityConstraint
                            {
                            pushFollow(FOLLOW_typeName_in_typeReference7850);
                            typeName109=typeName();
                            _fsp--;
                            if (failed) return type;
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference7854);
                            ccn=cardinalityConstraint();
                            _fsp--;
                            if (failed) return type;
                            if ( backtracking==0 ) {
                               type = F.TypeClass(typeName109, ccn); 
                            }

                            }
                            break;
                        case 3 :
                            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:763:22: STAR ccs= cardinalityConstraint
                            {
                            STAR110=(Token)input.LT(1);
                            match(input,STAR,FOLLOW_STAR_in_typeReference7880); if (failed) return type;
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference7884);
                            ccs=cardinalityConstraint();
                            _fsp--;
                            if (failed) return type;
                            if ( backtracking==0 ) {
                               type = F.at(pos(STAR110)).TypeAny(ccs); 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:765:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );
    public final int cardinalityConstraint() throws RecognitionException {
        int ary = 0;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:766:2: ( LBRACKET RBRACKET | QUES | PLUS | STAR | )
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
                        new NoViableAltException("765:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 106, 1, input);

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
                        new NoViableAltException("765:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 106, 2, input);

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
                        new NoViableAltException("765:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 106, 3, input);

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
                        new NoViableAltException("765:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 106, 4, input);

                    throw nvae;
                }
                }
                break;
            case EOF:
            case IF:
            case IN:
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
                    new NoViableAltException("765:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 106, 0, input);

                throw nvae;
            }

            switch (alt106) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:766:5: LBRACKET RBRACKET
                    {
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_cardinalityConstraint7915); if (failed) return ary;
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_cardinalityConstraint7919); if (failed) return ary;
                    if ( backtracking==0 ) {
                       ary = MTType.CARDINALITY_ANY; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:767:5: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_cardinalityConstraint7931); if (failed) return ary;
                    if ( backtracking==0 ) {
                       ary = MTType.CARDINALITY_SINGLETON; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:768:5: PLUS
                    {
                    match(input,PLUS,FOLLOW_PLUS_in_cardinalityConstraint7958); if (failed) return ary;
                    if ( backtracking==0 ) {
                       ary = MTType.CARDINALITY_ANY; 
                    }

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:769:5: STAR
                    {
                    match(input,STAR,FOLLOW_STAR_in_cardinalityConstraint7985); if (failed) return ary;
                    if ( backtracking==0 ) {
                       ary = MTType.CARDINALITY_ANY; 
                    }

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:770:29: 
                    {
                    if ( backtracking==0 ) {
                       ary = MTType.CARDINALITY_SINGLETON; 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:772:1: literal returns [MTExpression expr] : (t= STRING_LITERAL | t= DECIMAL_LITERAL | t= OCTAL_LITERAL | t= HEX_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );
    public final MTExpression literal() throws RecognitionException {
        MTExpression expr = null;

        Token t=null;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:773:2: (t= STRING_LITERAL | t= DECIMAL_LITERAL | t= OCTAL_LITERAL | t= HEX_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL )
            int alt107=8;
            switch ( input.LA(1) ) {
            case STRING_LITERAL:
                {
                alt107=1;
                }
                break;
            case DECIMAL_LITERAL:
                {
                alt107=2;
                }
                break;
            case OCTAL_LITERAL:
                {
                alt107=3;
                }
                break;
            case HEX_LITERAL:
                {
                alt107=4;
                }
                break;
            case FLOATING_POINT_LITERAL:
                {
                alt107=5;
                }
                break;
            case TRUE:
                {
                alt107=6;
                }
                break;
            case FALSE:
                {
                alt107=7;
                }
                break;
            case NULL:
                {
                alt107=8;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return expr;}
                NoViableAltException nvae =
                    new NoViableAltException("772:1: literal returns [MTExpression expr] : (t= STRING_LITERAL | t= DECIMAL_LITERAL | t= OCTAL_LITERAL | t= HEX_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );", 107, 0, input);

                throw nvae;
            }

            switch (alt107) {
                case 1 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:773:4: t= STRING_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_literal8054); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.CLASS, t.getText()); 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:774:4: t= DECIMAL_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,DECIMAL_LITERAL,FOLLOW_DECIMAL_LITERAL_in_literal8064); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.INT, Convert.string2int(t.getText(), 10)); 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:775:4: t= OCTAL_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,OCTAL_LITERAL,FOLLOW_OCTAL_LITERAL_in_literal8074); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.INT, Convert.string2int(t.getText(), 8)); 
                    }

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:776:4: t= HEX_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,HEX_LITERAL,FOLLOW_HEX_LITERAL_in_literal8084); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.INT, Convert.string2int(t.getText(), 16)); 
                    }

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:777:4: t= FLOATING_POINT_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,FLOATING_POINT_LITERAL,FOLLOW_FLOATING_POINT_LITERAL_in_literal8095); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.DOUBLE, Double.valueOf(t.getText())); 
                    }

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:778:4: t= TRUE
                    {
                    t=(Token)input.LT(1);
                    match(input,TRUE,FOLLOW_TRUE_in_literal8105); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 1); 
                    }

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:779:4: t= FALSE
                    {
                    t=(Token)input.LT(1);
                    match(input,FALSE,FOLLOW_FALSE_in_literal8119); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(pos(t)).Literal(TypeTags.BOOLEAN, 0); 
                    }

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:780:4: t= NULL
                    {
                    t=(Token)input.LT(1);
                    match(input,NULL,FOLLOW_NULL_in_literal8133); if (failed) return expr;
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:782:1: typeName returns [MTExpression expr] : qualident ;
    public final MTExpression typeName() throws RecognitionException {
        MTExpression expr = null;

        MTExpression qualident111 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:783:8: ( qualident )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:783:10: qualident
            {
            pushFollow(FOLLOW_qualident_in_typeName8160);
            qualident111=qualident();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = qualident111; 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:785:1: qualident returns [MTExpression expr] : n1= name ( DOT nn= name )* ;
    public final MTExpression qualident() throws RecognitionException {
        MTExpression expr = null;

        name_return n1 = null;

        name_return nn = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:786:8: (n1= name ( DOT nn= name )* )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:786:10: n1= name ( DOT nn= name )*
            {
            pushFollow(FOLLOW_name_in_qualident8204);
            n1=name();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = F.at(n1.pos).Identifier(n1.value); 
            }
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:787:10: ( DOT nn= name )*
            loop108:
            do {
                int alt108=2;
                int LA108_0 = input.LA(1);

                if ( (LA108_0==DOT) ) {
                    int LA108_2 = input.LA(2);

                    if ( (LA108_2==QUOTED_IDENTIFIER||LA108_2==IDENTIFIER) ) {
                        int LA108_3 = input.LA(3);

                        if ( (synpred208()) ) {
                            alt108=1;
                        }


                    }


                }


                switch (alt108) {
            	case 1 :
            	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:787:12: DOT nn= name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_qualident8233); if (failed) return expr;
            	    pushFollow(FOLLOW_name_in_qualident8237);
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:789:1: identifier returns [MTIdent expr] : name ;
    public final MTIdent identifier() throws RecognitionException {
        MTIdent expr = null;

        name_return name112 = null;


        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:790:2: ( name )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:790:4: name
            {
            pushFollow(FOLLOW_name_in_identifier8274);
            name112=name();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = F.at(name112.pos).Ident(name112.value); 
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
    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:792:1: name returns [Name value, int pos] : tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) ;
    public final name_return name() throws RecognitionException {
        name_return retval = new name_return();
        retval.start = input.LT(1);

        Token tokid=null;

        try {
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:793:2: (tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) )
            // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:793:4: tokid= ( QUOTED_IDENTIFIER | IDENTIFIER )
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
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_name8308);    throw mse;
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
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:485:4: ( localFunctionDefinition )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:485:4: localFunctionDefinition
        {
        pushFollow(FOLLOW_localFunctionDefinition_in_synpred453723);
        localFunctionDefinition();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred45

    // $ANTLR start synpred46
    public final void synpred46_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:486:4: ( localOperationDefinition )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:486:4: localOperationDefinition
        {
        pushFollow(FOLLOW_localOperationDefinition_in_synpred463731);
        localOperationDefinition();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred46

    // $ANTLR start synpred47
    public final void synpred47_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:487:10: ( backgroundStatement )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:487:10: backgroundStatement
        {
        pushFollow(FOLLOW_backgroundStatement_in_synpred473745);
        backgroundStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred47

    // $ANTLR start synpred48
    public final void synpred48_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:488:10: ( laterStatement )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:488:10: laterStatement
        {
        pushFollow(FOLLOW_laterStatement_in_synpred483760);
        laterStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred48

    // $ANTLR start synpred50
    public final void synpred50_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:490:10: ( ifStatement )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:490:10: ifStatement
        {
        pushFollow(FOLLOW_ifStatement_in_synpred503796);
        ifStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred50

    // $ANTLR start synpred53
    public final void synpred53_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:493:4: ( expression SEMI )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:493:4: expression SEMI
        {
        pushFollow(FOLLOW_expression_in_synpred533839);
        expression();
        _fsp--;
        if (failed) return ;
        match(input,SEMI,FOLLOW_SEMI_in_synpred533843); if (failed) return ;

        }
    }
    // $ANTLR end synpred53

    // $ANTLR start synpred58
    public final void synpred58_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:498:10: ( forAlphaStatement )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:498:10: forAlphaStatement
        {
        pushFollow(FOLLOW_forAlphaStatement_in_synpred583920);
        forAlphaStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred58

    // $ANTLR start synpred59
    public final void synpred59_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:499:10: ( forJoinStatement )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:499:10: forJoinStatement
        {
        pushFollow(FOLLOW_forJoinStatement_in_synpred593936);
        forJoinStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred59

    // $ANTLR start synpred81
    public final void synpred81_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:564:62: ( FPS expression )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:564:62: FPS expression
        {
        match(input,FPS,FOLLOW_FPS_in_synpred814862); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred814866);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred81

    // $ANTLR start synpred82
    public final void synpred82_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:564:91: ( WHILE expression )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:564:91: WHILE expression
        {
        match(input,WHILE,FOLLOW_WHILE_in_synpred824880); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred824884);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred82

    // $ANTLR start synpred83
    public final void synpred83_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:564:122: ( CONTINUE IF expression )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:564:122: CONTINUE IF expression
        {
        match(input,CONTINUE,FOLLOW_CONTINUE_in_synpred834898); if (failed) return ;
        match(input,IF,FOLLOW_IF_in_synpred834902); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred834906);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred83

    // $ANTLR start synpred87
    public final void synpred87_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:36: ( LINEAR )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:36: LINEAR
        {
        match(input,LINEAR,FOLLOW_LINEAR_in_synpred875045); if (failed) return ;

        }
    }
    // $ANTLR end synpred87

    // $ANTLR start synpred88
    public final void synpred88_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:49: ( EASEIN )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:49: EASEIN
        {
        match(input,EASEIN,FOLLOW_EASEIN_in_synpred885053); if (failed) return ;

        }
    }
    // $ANTLR end synpred88

    // $ANTLR start synpred89
    public final void synpred89_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:62: ( EASEOUT )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:62: EASEOUT
        {
        match(input,EASEOUT,FOLLOW_EASEOUT_in_synpred895061); if (failed) return ;

        }
    }
    // $ANTLR end synpred89

    // $ANTLR start synpred90
    public final void synpred90_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:76: ( EASEBOTH )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:76: EASEBOTH
        {
        match(input,EASEBOTH,FOLLOW_EASEBOTH_in_synpred905069); if (failed) return ;

        }
    }
    // $ANTLR end synpred90

    // $ANTLR start synpred91
    public final void synpred91_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:91: ( MOTION expression )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:91: MOTION expression
        {
        match(input,MOTION,FOLLOW_MOTION_in_synpred915077); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred915081);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred91

    // $ANTLR start synpred92
    public final void synpred92_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:123: ( FPS expression )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:123: FPS expression
        {
        match(input,FPS,FOLLOW_FPS_in_synpred925095); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred925099);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred92

    // $ANTLR start synpred93
    public final void synpred93_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:150: ( WHILE expression )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:150: WHILE expression
        {
        match(input,WHILE,FOLLOW_WHILE_in_synpred935111); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred935115);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred93

    // $ANTLR start synpred94
    public final void synpred94_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:181: ( CONTINUE IF expression )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:568:181: CONTINUE IF expression
        {
        match(input,CONTINUE,FOLLOW_CONTINUE_in_synpred945129); if (failed) return ;
        match(input,IF,FOLLOW_IF_in_synpred945133); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred945137);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred94

    // $ANTLR start synpred105
    public final void synpred105_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:594:11: ( LPAREN typeName RPAREN suffixedExpression )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:594:11: LPAREN typeName RPAREN suffixedExpression
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred1055434); if (failed) return ;
        pushFollow(FOLLOW_typeName_in_synpred1055440);
        typeName();
        _fsp--;
        if (failed) return ;
        match(input,RPAREN,FOLLOW_RPAREN_in_synpred1055443); if (failed) return ;
        pushFollow(FOLLOW_suffixedExpression_in_synpred1055447);
        suffixedExpression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred105

    // $ANTLR start synpred108
    public final void synpred108_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:608:52: ( typeReference )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:608:52: typeReference
        {
        pushFollow(FOLLOW_typeReference_in_synpred1085633);
        typeReference();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred108

    // $ANTLR start synpred109
    public final void synpred109_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:609:54: ( typeReference )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:609:54: typeReference
        {
        pushFollow(FOLLOW_typeReference_in_synpred1095655);
        typeReference();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred109

    // $ANTLR start synpred111
    public final void synpred111_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:611:83: ( COMMA selectionVar )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:611:83: COMMA selectionVar
        {
        match(input,COMMA,FOLLOW_COMMA_in_synpred1115725); if (failed) return ;
        pushFollow(FOLLOW_selectionVar_in_synpred1115729);
        selectionVar();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred111

    // $ANTLR start synpred112
    public final void synpred112_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:611:116: ( WHERE expression )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:611:116: WHERE expression
        {
        match(input,WHERE,FOLLOW_WHERE_in_synpred1125743); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred1125747);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred112

    // $ANTLR start synpred117
    public final void synpred117_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:615:38: ( PLUSPLUS )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:615:38: PLUSPLUS
        {
        match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_synpred1175820); if (failed) return ;

        }
    }
    // $ANTLR end synpred117

    // $ANTLR start synpred118
    public final void synpred118_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:615:49: ( SUBSUB )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:615:49: SUBSUB
        {
        match(input,SUBSUB,FOLLOW_SUBSUB_in_synpred1185824); if (failed) return ;

        }
    }
    // $ANTLR end synpred118

    // $ANTLR start synpred132
    public final void synpred132_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:646:9: ( SUB multiplicativeExpression )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:646:9: SUB multiplicativeExpression
        {
        match(input,SUB,FOLLOW_SUB_in_synpred1326297); if (failed) return ;
        pushFollow(FOLLOW_multiplicativeExpression_in_synpred1326304);
        multiplicativeExpression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred132

    // $ANTLR start synpred138
    public final void synpred138_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:662:16: ( LPAREN expressionListOpt RPAREN )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:662:16: LPAREN expressionListOpt RPAREN
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred1386526); if (failed) return ;
        pushFollow(FOLLOW_expressionListOpt_in_synpred1386528);
        expressionListOpt();
        _fsp--;
        if (failed) return ;
        match(input,RPAREN,FOLLOW_RPAREN_in_synpred1386530); if (failed) return ;

        }
    }
    // $ANTLR end synpred138

    // $ANTLR start synpred139
    public final void synpred139_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:660:7: ( DOT ( CLASS | name ( LPAREN expressionListOpt RPAREN )* ) )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:660:7: DOT ( CLASS | name ( LPAREN expressionListOpt RPAREN )* )
        {
        match(input,DOT,FOLLOW_DOT_in_synpred1396473); if (failed) return ;
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:660:11: ( CLASS | name ( LPAREN expressionListOpt RPAREN )* )
        int alt120=2;
        int LA120_0 = input.LA(1);

        if ( (LA120_0==CLASS) ) {
            alt120=1;
        }
        else if ( (LA120_0==QUOTED_IDENTIFIER||LA120_0==IDENTIFIER) ) {
            alt120=2;
        }
        else {
            if (backtracking>0) {failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("660:11: ( CLASS | name ( LPAREN expressionListOpt RPAREN )* )", 120, 0, input);

            throw nvae;
        }
        switch (alt120) {
            case 1 :
                // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:660:13: CLASS
                {
                match(input,CLASS,FOLLOW_CLASS_in_synpred1396477); if (failed) return ;

                }
                break;
            case 2 :
                // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:661:13: name ( LPAREN expressionListOpt RPAREN )*
                {
                pushFollow(FOLLOW_name_in_synpred1396501);
                name();
                _fsp--;
                if (failed) return ;
                // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:662:14: ( LPAREN expressionListOpt RPAREN )*
                loop119:
                do {
                    int alt119=2;
                    int LA119_0 = input.LA(1);

                    if ( (LA119_0==LPAREN) ) {
                        alt119=1;
                    }


                    switch (alt119) {
                	case 1 :
                	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:662:16: LPAREN expressionListOpt RPAREN
                	    {
                	    match(input,LPAREN,FOLLOW_LPAREN_in_synpred1396526); if (failed) return ;
                	    pushFollow(FOLLOW_expressionListOpt_in_synpred1396528);
                	    expressionListOpt();
                	    _fsp--;
                	    if (failed) return ;
                	    match(input,RPAREN,FOLLOW_RPAREN_in_synpred1396530); if (failed) return ;

                	    }
                	    break;

                	default :
                	    break loop119;
                    }
                } while (true);


                }
                break;

        }


        }
    }
    // $ANTLR end synpred139

    // $ANTLR start synpred141
    public final void synpred141_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:664:7: ( LBRACKET ( name BAR )? expression RBRACKET )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:664:7: LBRACKET ( name BAR )? expression RBRACKET
        {
        match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred1416562); if (failed) return ;
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:664:16: ( name BAR )?
        int alt121=2;
        int LA121_0 = input.LA(1);

        if ( (LA121_0==QUOTED_IDENTIFIER||LA121_0==IDENTIFIER) ) {
            int LA121_1 = input.LA(2);

            if ( (LA121_1==BAR) ) {
                alt121=1;
            }
        }
        switch (alt121) {
            case 1 :
                // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:664:17: name BAR
                {
                pushFollow(FOLLOW_name_in_synpred1416565);
                name();
                _fsp--;
                if (failed) return ;
                match(input,BAR,FOLLOW_BAR_in_synpred1416567); if (failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_expression_in_synpred1416571);
        expression();
        _fsp--;
        if (failed) return ;
        match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred1416574); if (failed) return ;

        }
    }
    // $ANTLR end synpred141

    // $ANTLR start synpred143
    public final void synpred143_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:668:4: ( typeName LBRACE objectLiteral RBRACE )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:668:4: typeName LBRACE objectLiteral RBRACE
        {
        pushFollow(FOLLOW_typeName_in_synpred1436611);
        typeName();
        _fsp--;
        if (failed) return ;
        match(input,LBRACE,FOLLOW_LBRACE_in_synpred1436613); if (failed) return ;
        pushFollow(FOLLOW_objectLiteral_in_synpred1436616);
        objectLiteral();
        _fsp--;
        if (failed) return ;
        match(input,RBRACE,FOLLOW_RBRACE_in_synpred1436618); if (failed) return ;

        }
    }
    // $ANTLR end synpred143

    // $ANTLR start synpred149
    public final void synpred149_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:676:12: ( LPAREN expressionListOpt RPAREN )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:676:12: LPAREN expressionListOpt RPAREN
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred1496726); if (failed) return ;
        pushFollow(FOLLOW_expressionListOpt_in_synpred1496730);
        expressionListOpt();
        _fsp--;
        if (failed) return ;
        match(input,RPAREN,FOLLOW_RPAREN_in_synpred1496734); if (failed) return ;

        }
    }
    // $ANTLR end synpred149

    // $ANTLR start synpred150
    public final void synpred150_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:675:10: ( identifier ( LPAREN expressionListOpt RPAREN )* )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:675:10: identifier ( LPAREN expressionListOpt RPAREN )*
        {
        pushFollow(FOLLOW_identifier_in_synpred1506705);
        identifier();
        _fsp--;
        if (failed) return ;
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:676:10: ( LPAREN expressionListOpt RPAREN )*
        loop122:
        do {
            int alt122=2;
            int LA122_0 = input.LA(1);

            if ( (LA122_0==LPAREN) ) {
                alt122=1;
            }


            switch (alt122) {
        	case 1 :
        	    // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:676:12: LPAREN expressionListOpt RPAREN
        	    {
        	    match(input,LPAREN,FOLLOW_LPAREN_in_synpred1506726); if (failed) return ;
        	    pushFollow(FOLLOW_expressionListOpt_in_synpred1506730);
        	    expressionListOpt();
        	    _fsp--;
        	    if (failed) return ;
        	    match(input,RPAREN,FOLLOW_RPAREN_in_synpred1506734); if (failed) return ;

        	    }
        	    break;

        	default :
        	    break loop122;
            }
        } while (true);


        }
    }
    // $ANTLR end synpred150

    // $ANTLR start synpred153
    public final void synpred153_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:684:5: ( LPAREN expressionListOpt RPAREN )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:684:5: LPAREN expressionListOpt RPAREN
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred1536837); if (failed) return ;
        pushFollow(FOLLOW_expressionListOpt_in_synpred1536841);
        expressionListOpt();
        _fsp--;
        if (failed) return ;
        match(input,RPAREN,FOLLOW_RPAREN_in_synpred1536845); if (failed) return ;

        }
    }
    // $ANTLR end synpred153

    // $ANTLR start synpred177
    public final void synpred177_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:738:28: ( COMMA name )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:738:28: COMMA name
        {
        match(input,COMMA,FOLLOW_COMMA_in_synpred1777494); if (failed) return ;
        pushFollow(FOLLOW_name_in_synpred1777498);
        name();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred177

    // $ANTLR start synpred197
    public final void synpred197_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:766:5: ( LBRACKET RBRACKET )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:766:5: LBRACKET RBRACKET
        {
        match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred1977915); if (failed) return ;
        match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred1977919); if (failed) return ;

        }
    }
    // $ANTLR end synpred197

    // $ANTLR start synpred198
    public final void synpred198_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:767:5: ( QUES )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:767:5: QUES
        {
        match(input,QUES,FOLLOW_QUES_in_synpred1987931); if (failed) return ;

        }
    }
    // $ANTLR end synpred198

    // $ANTLR start synpred199
    public final void synpred199_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:768:5: ( PLUS )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:768:5: PLUS
        {
        match(input,PLUS,FOLLOW_PLUS_in_synpred1997958); if (failed) return ;

        }
    }
    // $ANTLR end synpred199

    // $ANTLR start synpred200
    public final void synpred200_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:769:5: ( STAR )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:769:5: STAR
        {
        match(input,STAR,FOLLOW_STAR_in_synpred2007985); if (failed) return ;

        }
    }
    // $ANTLR end synpred200

    // $ANTLR start synpred208
    public final void synpred208_fragment() throws RecognitionException {   
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:787:12: ( DOT name )
        // C:\\JavaFX\\3\\trunk\\src\\share\\classes\\com\\sun\\tools\\migrator\\antlr\\interp.g:787:12: DOT name
        {
        match(input,DOT,FOLLOW_DOT_in_synpred2088233); if (failed) return ;
        pushFollow(FOLLOW_name_in_synpred2088237);
        name();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred208

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
    public final boolean synpred83() {
        backtracking++;
        int start = input.mark();
        try {
            synpred83_fragment(); // can never throw exception
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
    public final boolean synpred149() {
        backtracking++;
        int start = input.mark();
        try {
            synpred149_fragment(); // can never throw exception
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
    public final boolean synpred141() {
        backtracking++;
        int start = input.mark();
        try {
            synpred141_fragment(); // can never throw exception
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
    public final boolean synpred117() {
        backtracking++;
        int start = input.mark();
        try {
            synpred117_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred118() {
        backtracking++;
        int start = input.mark();
        try {
            synpred118_fragment(); // can never throw exception
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
    public final boolean synpred94() {
        backtracking++;
        int start = input.mark();
        try {
            synpred94_fragment(); // can never throw exception
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
    public final boolean synpred139() {
        backtracking++;
        int start = input.mark();
        try {
            synpred139_fragment(); // can never throw exception
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
    public final boolean synpred138() {
        backtracking++;
        int start = input.mark();
        try {
            synpred138_fragment(); // can never throw exception
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
    public final boolean synpred208() {
        backtracking++;
        int start = input.mark();
        try {
            synpred208_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred132() {
        backtracking++;
        int start = input.mark();
        try {
            synpred132_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred153() {
        backtracking++;
        int start = input.mark();
        try {
            synpred153_fragment(); // can never throw exception
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


 

    public static final BitSet FOLLOW_packageDecl_in_module2233 = new BitSet(new long[]{0x499F914B81644260L,0xB82C007008ADC0FFL,0x0000000000000048L});
    public static final BitSet FOLLOW_moduleItems_in_module2236 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_module2238 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PACKAGE_in_packageDecl2271 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_qualident_in_packageDecl2273 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_packageDecl2275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_moduleItem_in_moduleItems2304 = new BitSet(new long[]{0x499F914B81644262L,0xB82C007008ADC0FFL,0x0000000000000048L});
    public static final BitSet FOLLOW_importDecl_in_moduleItem2346 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDefinition_in_moduleItem2361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeDefinition_in_moduleItem2376 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberOperationDefinition_in_moduleItem2390 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberFunctionDefinition_in_moduleItem2403 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRIGGER_in_moduleItem2416 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ON_in_moduleItem2418 = new BitSet(new long[]{0x0000000000000000L,0x0800000000200000L,0x0000000000000040L});
    public static final BitSet FOLLOW_changeRule_in_moduleItem2420 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statementExcept_in_moduleItem2433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importDecl2462 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_identifier_in_importDecl2465 = new BitSet(new long[]{0x0000000000000000L,0x000000000A000000L});
    public static final BitSet FOLLOW_DOT_in_importDecl2489 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_importDecl2491 = new BitSet(new long[]{0x0000000000000000L,0x000000000A000000L});
    public static final BitSet FOLLOW_DOT_in_importDecl2519 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_STAR_in_importDecl2521 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_importDecl2529 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_classDefinition2555 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_CLASS_in_classDefinition2558 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_classDefinition2560 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000400L});
    public static final BitSet FOLLOW_supers_in_classDefinition2562 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_classDefinition2564 = new BitSet(new long[]{0x0000000000004200L,0x020000000000009CL});
    public static final BitSet FOLLOW_classMembers_in_classDefinition2566 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_classDefinition2568 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXTENDS_in_supers2594 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_qualident_in_supers2598 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_supers2622 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_qualident_in_supers2626 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_attributeDecl_in_classMembers2660 = new BitSet(new long[]{0x0000000000004202L,0x000000000000009CL});
    public static final BitSet FOLLOW_functionDecl_in_classMembers2678 = new BitSet(new long[]{0x0000000000004202L,0x000000000000009CL});
    public static final BitSet FOLLOW_operationDecl_in_classMembers2697 = new BitSet(new long[]{0x0000000000004202L,0x000000000000009CL});
    public static final BitSet FOLLOW_modifierFlags_in_attributeDecl2725 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeDecl2727 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_attributeDecl2729 = new BitSet(new long[]{0x0000000000000000L,0x0002000002001900L});
    public static final BitSet FOLLOW_typeReference_in_attributeDecl2731 = new BitSet(new long[]{0x0000000000000000L,0x0000000002001900L});
    public static final BitSet FOLLOW_inverseClause_in_attributeDecl2733 = new BitSet(new long[]{0x0000000000000000L,0x0000000002001800L});
    public static final BitSet FOLLOW_orderBy_in_attributeDecl2737 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_indexOn_in_attributeDecl2741 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_attributeDecl2745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INVERSE_in_inverseClause2763 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_memberSelector_in_inverseClause2765 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_functionDecl2780 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_FUNCTION_in_functionDecl2782 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_functionDecl2784 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_functionDecl2786 = new BitSet(new long[]{0x0000000000000000L,0x0002000002000000L});
    public static final BitSet FOLLOW_typeReference_in_functionDecl2788 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_functionDecl2790 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_operationDecl2805 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_OPERATION_in_operationDecl2809 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_operationDecl2813 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_operationDecl2817 = new BitSet(new long[]{0x0000000000000000L,0x0002000002000000L});
    public static final BitSet FOLLOW_typeReference_in_operationDecl2821 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_operationDecl2826 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeDefinition2841 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_memberSelector_in_attributeDefinition2845 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_attributeDefinition2849 = new BitSet(new long[]{0x4017900060010060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_bindOpt_in_attributeDefinition2851 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_attributeDefinition2854 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_attributeDefinition2858 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATION_in_memberOperationDefinition2872 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_memberSelector_in_memberOperationDefinition2876 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_memberOperationDefinition2880 = new BitSet(new long[]{0x0000000000000000L,0x0042000000000000L});
    public static final BitSet FOLLOW_typeReference_in_memberOperationDefinition2884 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_memberOperationDefinition2887 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_memberFunctionDefinition2902 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_memberSelector_in_memberFunctionDefinition2906 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_memberFunctionDefinition2910 = new BitSet(new long[]{0x0000000000000000L,0x0042000000000000L});
    public static final BitSet FOLLOW_typeReference_in_memberFunctionDefinition2914 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_memberFunctionDefinition2917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQ_in_functionBody2932 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_functionBody2936 = new BitSet(new long[]{0x8000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_whereVarDecls_in_functionBody2940 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_functionBody2946 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_functionBody2962 = new BitSet(new long[]{0x0000000280000000L,0x0800000000000060L,0x0000000000000040L});
    public static final BitSet FOLLOW_variableDefinition_in_functionBody2970 = new BitSet(new long[]{0x0000000280000000L,0x0800000000000060L,0x0000000000000040L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_functionBody2978 = new BitSet(new long[]{0x0000000280000000L,0x0800000000000060L,0x0000000000000040L});
    public static final BitSet FOLLOW_localOperationDefinition_in_functionBody2986 = new BitSet(new long[]{0x0000000280000000L,0x0800000000000060L,0x0000000000000040L});
    public static final BitSet FOLLOW_RETURN_in_functionBody2996 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_functionBody3000 = new BitSet(new long[]{0x0000000000000000L,0x0200000002000000L});
    public static final BitSet FOLLOW_SEMI_in_functionBody3004 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_functionBody3010 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_whereVarDecls3018 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000040L,0x0000000000000040L});
    public static final BitSet FOLLOW_whereVarDecl_in_whereVarDecls3022 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_whereVarDecls3030 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000040L,0x0000000000000040L});
    public static final BitSet FOLLOW_whereVarDecl_in_whereVarDecls3034 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_whereVarDecl3048 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_whereVarDecl3060 = new BitSet(new long[]{0x0000000000000000L,0x0002000020000000L});
    public static final BitSet FOLLOW_typeReference_in_whereVarDecl3064 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_whereVarDecl3068 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_whereVarDecl3072 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variableDefinition3080 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_variableDefinition3084 = new BitSet(new long[]{0x0000000000000000L,0x0002000020000000L});
    public static final BitSet FOLLOW_typeReference_in_variableDefinition3088 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_variableDefinition3091 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_variableDefinition3095 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_variableDefinition3099 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule3110 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_changeRule3114 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_typeName_in_changeRule3118 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule3121 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule3129 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3133 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_changeRule3136 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_identifier_in_changeRule3138 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule3142 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3150 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_changeRule3153 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_identifier_in_changeRule3155 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3157 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule3162 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3166 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_LBRACKET_in_changeRule3170 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_identifier_in_changeRule3176 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_changeRule3180 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_changeRule3184 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_identifier_in_changeRule3188 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule3192 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3195 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule3200 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_INSERT_in_changeRule3204 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_identifier_in_changeRule3208 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_INTO_in_changeRule3212 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3216 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule3220 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3222 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule3229 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_DELETE_in_changeRule3233 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_identifier_in_changeRule3237 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_FROM_in_changeRule3241 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3245 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule3249 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule3258 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_DELETE_in_changeRule3262 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule3265 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_LBRACKET_in_changeRule3269 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_identifier_in_changeRule3273 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_changeRule3277 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule3281 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_changeRule3283 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags3306 = new BitSet(new long[]{0x0000000000000002L,0x000000000000001CL});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags3319 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags3341 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000080L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags3354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_accessModifier3402 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PRIVATE_in_accessModifier3419 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROTECTED_in_accessModifier3435 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ABSTRACT_in_otherModifier3459 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_READONLY_in_otherModifier3474 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_memberSelector3500 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_DOT_in_memberSelector3504 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_memberSelector3510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_formalParameters3526 = new BitSet(new long[]{0x0000000000000000L,0x0800000000400000L,0x0000000000000040L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters3534 = new BitSet(new long[]{0x0000000000000000L,0x0000000004400000L});
    public static final BitSet FOLLOW_COMMA_in_formalParameters3553 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters3559 = new BitSet(new long[]{0x0000000000000000L,0x0000000004400000L});
    public static final BitSet FOLLOW_RPAREN_in_formalParameters3570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_formalParameter3583 = new BitSet(new long[]{0x0000000000000002L,0x0002000000000000L});
    public static final BitSet FOLLOW_typeReference_in_formalParameter3585 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_block3602 = new BitSet(new long[]{0x499F914381440060L,0xBA2C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_statements_in_block3606 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_block3610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_statements3628 = new BitSet(new long[]{0x499F914381440062L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_statementExcept_in_statement3679 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localTriggerStatement_in_statement3695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclaration_in_statementExcept3713 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_statementExcept3723 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localOperationDefinition_in_statementExcept3731 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_backgroundStatement_in_statementExcept3745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_laterStatement_in_statementExcept3760 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_statementExcept3775 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_statementExcept3777 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_statementExcept3779 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_statementExcept3781 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_statementExcept3783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifStatement_in_statementExcept3796 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_insertStatement_in_statementExcept3813 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_deleteStatement_in_statementExcept3829 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_statementExcept3839 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_statementExcept3843 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BREAK_in_statementExcept3853 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_statementExcept3857 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_statementExcept3868 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_statementExcept3872 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_throwStatement_in_statementExcept3888 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_returnStatement_in_statementExcept3904 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forAlphaStatement_in_statementExcept3920 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forJoinStatement_in_statementExcept3936 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tryStatement_in_statementExcept3952 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_assertStatement3971 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_assertStatement3975 = new BitSet(new long[]{0x0000000000000000L,0x0002000002000000L});
    public static final BitSet FOLLOW_COLON_in_assertStatement3983 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_assertStatement3987 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_assertStatement3997 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATION_in_localOperationDefinition4012 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_localOperationDefinition4016 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_localOperationDefinition4020 = new BitSet(new long[]{0x0000000000000000L,0x0042000000000000L});
    public static final BitSet FOLLOW_typeReference_in_localOperationDefinition4024 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_localOperationDefinition4027 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_localFunctionDefinition4046 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_localFunctionDefinition4052 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_localFunctionDefinition4056 = new BitSet(new long[]{0x0000000000000000L,0x0042000000000000L});
    public static final BitSet FOLLOW_typeReference_in_localFunctionDefinition4060 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_localFunctionDefinition4063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variableDeclaration4082 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_variableDeclaration4085 = new BitSet(new long[]{0x0000000000000000L,0x0002000022000000L});
    public static final BitSet FOLLOW_typeReference_in_variableDeclaration4088 = new BitSet(new long[]{0x0000000000000000L,0x0000000022000000L});
    public static final BitSet FOLLOW_EQ_in_variableDeclaration4099 = new BitSet(new long[]{0x4017900060010060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_bindOpt_in_variableDeclaration4101 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_variableDeclaration4104 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_variableDeclaration4106 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_variableDeclaration4117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BIND_in_bindOpt4154 = new BitSet(new long[]{0x2000000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt4170 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAYS_in_bindOpt4185 = new BitSet(new long[]{0x2000000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt4201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TIE_in_bindOpt4216 = new BitSet(new long[]{0x2000000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt4232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DO_in_backgroundStatement4258 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_backgroundStatement4262 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DO_in_laterStatement4287 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LATER_in_laterStatement4291 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_laterStatement4295 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifStatement4325 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_ifStatement4329 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_ifStatement4333 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_ifStatement4337 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_ifStatement4343 = new BitSet(new long[]{0x0000400000000002L});
    public static final BitSet FOLLOW_ELSE_in_ifStatement4346 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_ifStatement4351 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INSERT_in_insertStatement4380 = new BitSet(new long[]{0x4017900000800060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_DISTINCT_in_insertStatement4388 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_insertStatement4392 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_INTO_in_insertStatement4396 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_insertStatement4400 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_expression_in_insertStatement4408 = new BitSet(new long[]{0x0000020000009400L});
    public static final BitSet FOLLOW_AS_in_insertStatement4424 = new BitSet(new long[]{0x00000C0000000000L});
    public static final BitSet FOLLOW_set_in_insertStatement4428 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_INTO_in_insertStatement4454 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_insertStatement4458 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_AFTER_in_insertStatement4470 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_insertStatement4474 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_BEFORE_in_insertStatement4482 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_insertStatement4486 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_insertStatement4500 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DELETE_in_deleteStatement4515 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_deleteStatement4520 = new BitSet(new long[]{0x0000001000000002L});
    public static final BitSet FOLLOW_FROM_in_deleteStatement4530 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_deleteStatement4534 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THROW_in_throwStatement4572 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_throwStatement4576 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_throwStatement4580 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnStatement4600 = new BitSet(new long[]{0x4017900000000060L,0xB82C00700AADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_returnStatement4603 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_returnStatement4610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRIGGER_in_localTriggerStatement4636 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ON_in_localTriggerStatement4640 = new BitSet(new long[]{0x0000010000400000L,0x0800000000200000L,0x0000000000000040L});
    public static final BitSet FOLLOW_localTriggerCondition_in_localTriggerStatement4647 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_LPAREN_in_localTriggerStatement4651 = new BitSet(new long[]{0x0000010000400000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_localTriggerCondition_in_localTriggerStatement4655 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_localTriggerStatement4659 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_localTriggerStatement4663 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4688 = new BitSet(new long[]{0x0000000000000000L,0x0000000020800000L});
    public static final BitSet FOLLOW_LBRACKET_in_localTriggerCondition4696 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4700 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_localTriggerCondition4704 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_localTriggerCondition4714 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_localTriggerCondition4718 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INSERT_in_localTriggerCondition4730 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4734 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_INTO_in_localTriggerCondition4738 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4744 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_localTriggerCondition4748 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_localTriggerCondition4756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DELETE_in_localTriggerCondition4768 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4772 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_FROM_in_localTriggerCondition4776 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4782 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_localTriggerCondition4786 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_localTriggerCondition4794 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_forAlphaStatement4809 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_forAlphaStatement4813 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_alphaExpression_in_forAlphaStatement4817 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_forAlphaStatement4821 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_forAlphaStatement4825 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNITINTERVAL_in_alphaExpression4842 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_alphaExpression4846 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_DUR_in_alphaExpression4850 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_alphaExpression4854 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_FPS_in_alphaExpression4862 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_alphaExpression4866 = new BitSet(new long[]{0x0180000000000002L});
    public static final BitSet FOLLOW_WHILE_in_alphaExpression4880 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_alphaExpression4884 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_alphaExpression4898 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_IF_in_alphaExpression4902 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_alphaExpression4906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_forJoinStatement4927 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_forJoinStatement4931 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_joinClause_in_forJoinStatement4935 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_forJoinStatement4939 = new BitSet(new long[]{0x0000000000000000L,0x0040000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_forJoinStatement4947 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_durClause_in_forJoinStatement4951 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_forJoinStatement4955 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_forJoinStatement4965 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_joinClause4973 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_joinClause4977 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_joinClause4981 = new BitSet(new long[]{0x8000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_joinClause4989 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_joinClause4993 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_joinClause4997 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_joinClause5001 = new BitSet(new long[]{0x8000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_WHERE_in_joinClause5015 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_joinClause5019 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DUR_in_durClause5033 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_durClause5037 = new BitSet(new long[]{0x07C000001C000002L});
    public static final BitSet FOLLOW_LINEAR_in_durClause5045 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_EASEIN_in_durClause5053 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_EASEOUT_in_durClause5061 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_EASEBOTH_in_durClause5069 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_MOTION_in_durClause5077 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_durClause5081 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_FPS_in_durClause5095 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_durClause5099 = new BitSet(new long[]{0x0180000000000002L});
    public static final BitSet FOLLOW_WHILE_in_durClause5111 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_durClause5115 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_durClause5129 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_IF_in_durClause5133 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_durClause5137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRY_in_tryStatement5163 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement5169 = new BitSet(new long[]{0x1000000000100000L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement5181 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement5187 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catchClause_in_tryStatement5203 = new BitSet(new long[]{0x1000000000100002L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement5232 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement5237 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CATCH_in_catchClause5286 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_catchClause5289 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_formalParameter_in_catchClause5292 = new BitSet(new long[]{0x0000100000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_IF_in_catchClause5303 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_catchClause5307 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_catchClause5328 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_catchClause5332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_foreach_in_expression5353 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionExpression_in_expression5366 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_operationExpression_in_expression5379 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_alphaExpression_in_expression5392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifExpression_in_expression5405 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_selectExpression_in_expression5421 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_expression5434 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_typeName_in_expression5440 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_expression5443 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_suffixedExpression_in_expression5447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_suffixedExpression_in_expression5462 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOREACH_in_foreach5487 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_foreach5491 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_inClause_in_foreach5499 = new BitSet(new long[]{0x0000000000000000L,0x0000000004400000L});
    public static final BitSet FOLLOW_COMMA_in_foreach5519 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_inClause_in_foreach5523 = new BitSet(new long[]{0x0000000000000000L,0x0000000004400000L});
    public static final BitSet FOLLOW_RPAREN_in_foreach5550 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_foreach5554 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameter_in_inClause5582 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_inClause5584 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_inClause5588 = new BitSet(new long[]{0x8000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_inClause5606 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_inClause5611 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_functionExpression5625 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_functionExpression5629 = new BitSet(new long[]{0x0000000000000000L,0x0042000020000000L});
    public static final BitSet FOLLOW_typeReference_in_functionExpression5633 = new BitSet(new long[]{0x0000000000000000L,0x0040000020000000L});
    public static final BitSet FOLLOW_functionBody_in_functionExpression5639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATION_in_operationExpression5647 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_operationExpression5651 = new BitSet(new long[]{0x0000000000000000L,0x0042000000000000L});
    public static final BitSet FOLLOW_typeReference_in_operationExpression5655 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_block_in_operationExpression5661 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifExpression5669 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_ifExpression5673 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_THEN_in_ifExpression5677 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_ifExpression5681 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ELSE_in_ifExpression5685 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_ifExpression5689 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SELECT_in_selectExpression5697 = new BitSet(new long[]{0x4017900000800060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_DISTINCT_in_selectExpression5701 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_selectExpression5709 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_FROM_in_selectExpression5713 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_selectionVar_in_selectExpression5717 = new BitSet(new long[]{0x8000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_selectExpression5725 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_selectionVar_in_selectExpression5729 = new BitSet(new long[]{0x8000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_WHERE_in_selectExpression5743 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_selectExpression5747 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_selectionVar5761 = new BitSet(new long[]{0x0020000000000002L});
    public static final BitSet FOLLOW_IN_in_selectionVar5769 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_selectionVar5773 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentExpression_in_suffixedExpression5796 = new BitSet(new long[]{0x0000000002000002L,0x0000005000001800L});
    public static final BitSet FOLLOW_indexOn_in_suffixedExpression5808 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orderBy_in_suffixedExpression5812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_durClause_in_suffixedExpression5816 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_suffixedExpression5820 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_suffixedExpression5824 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression5846 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_assignmentExpression5861 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression5867 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression5894 = new BitSet(new long[]{0x0000000000000002L,0x00007C0000000000L});
    public static final BitSet FOLLOW_assignmentOperator_in_assignmentOpExpression5910 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression5916 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orExpression_in_andExpression5942 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_AND_in_andExpression5958 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_orExpression_in_andExpression5964 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression5992 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_OR_in_orExpression6007 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression6013 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression6041 = new BitSet(new long[]{0x0000000000000002L,0x0000000000002000L});
    public static final BitSet FOLLOW_INSTANCEOF_in_instanceOfExpression6056 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_identifier_in_instanceOfExpression6058 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression6086 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_LTGT_in_relationalExpression6102 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression6108 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_EQEQ_in_relationalExpression6122 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression6128 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_LTEQ_in_relationalExpression6142 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression6148 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_GTEQ_in_relationalExpression6162 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression6168 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_LT_in_relationalExpression6182 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression6190 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_GT_in_relationalExpression6204 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression6212 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_IN_in_relationalExpression6226 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression6234 = new BitSet(new long[]{0x0020000000000002L,0x00000007D0000000L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression6263 = new BitSet(new long[]{0x0000000000000002L,0x0000002800000000L});
    public static final BitSet FOLLOW_PLUS_in_additiveExpression6278 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression6284 = new BitSet(new long[]{0x0000000000000002L,0x0000002800000000L});
    public static final BitSet FOLLOW_SUB_in_additiveExpression6297 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression6304 = new BitSet(new long[]{0x0000000000000002L,0x0000002800000000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression6332 = new BitSet(new long[]{0x0000000000000002L,0x0000038000000000L});
    public static final BitSet FOLLOW_STAR_in_multiplicativeExpression6348 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression6355 = new BitSet(new long[]{0x0000000000000002L,0x0000038000000000L});
    public static final BitSet FOLLOW_SLASH_in_multiplicativeExpression6369 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression6375 = new BitSet(new long[]{0x0000000000000002L,0x0000038000000000L});
    public static final BitSet FOLLOW_PERCENT_in_multiplicativeExpression6389 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression6393 = new BitSet(new long[]{0x0000000000000002L,0x0000038000000000L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression6423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryOperator_in_unaryExpression6434 = new BitSet(new long[]{0x0007800000000000L,0xB828000008A14002L,0x0000000000000048L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression6438 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primaryExpression_in_postfixExpression6458 = new BitSet(new long[]{0x0000000000000002L,0x0000000008800000L});
    public static final BitSet FOLLOW_DOT_in_postfixExpression6473 = new BitSet(new long[]{0x0000000000200000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_CLASS_in_postfixExpression6477 = new BitSet(new long[]{0x0000000000000002L,0x0000000008800000L});
    public static final BitSet FOLLOW_name_in_postfixExpression6501 = new BitSet(new long[]{0x0000000000000002L,0x0000000008A00000L});
    public static final BitSet FOLLOW_LPAREN_in_postfixExpression6526 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008EDC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expressionListOpt_in_postfixExpression6528 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_postfixExpression6530 = new BitSet(new long[]{0x0000000000000002L,0x0000000008A00000L});
    public static final BitSet FOLLOW_LBRACKET_in_postfixExpression6562 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_name_in_postfixExpression6565 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_BAR_in_postfixExpression6567 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_postfixExpression6571 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_postfixExpression6574 = new BitSet(new long[]{0x0000000000000002L,0x0000000008800000L});
    public static final BitSet FOLLOW_newExpression_in_primaryExpression6599 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeName_in_primaryExpression6611 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_primaryExpression6613 = new BitSet(new long[]{0x0000004200004000L,0x0A00000000000060L,0x0000000000000040L});
    public static final BitSet FOLLOW_objectLiteral_in_primaryExpression6616 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_primaryExpression6618 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_bracketExpression_in_primaryExpression6628 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ordinalExpression_in_primaryExpression6643 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_contextExpression_in_primaryExpression6655 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THIS_in_primaryExpression6667 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_primaryExpression6686 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression6705 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression6726 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008EDC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expressionListOpt_in_primaryExpression6730 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression6734 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_stringExpression_in_primaryExpression6753 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primaryExpression6771 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression6790 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_primaryExpression6792 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression6794 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_newExpression6826 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_typeName_in_newExpression6829 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_newExpression6837 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008EDC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expressionListOpt_in_newExpression6841 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_newExpression6845 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_objectLiteralPart_in_objectLiteral6885 = new BitSet(new long[]{0x0000004200004002L,0x0800000000000060L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_objectLiteralPart6911 = new BitSet(new long[]{0x0000000000000000L,0x0002000000000000L});
    public static final BitSet FOLLOW_COLON_in_objectLiteralPart6913 = new BitSet(new long[]{0x4017900060010060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_bindOpt_in_objectLiteralPart6916 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_objectLiteralPart6918 = new BitSet(new long[]{0x0000000000000002L,0x0000000006000000L});
    public static final BitSet FOLLOW_set_in_objectLiteralPart6920 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_objectLiteralPart6940 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_objectLiteralPart6944 = new BitSet(new long[]{0x0000000000000000L,0x0002000020000000L});
    public static final BitSet FOLLOW_typeReference_in_objectLiteralPart6948 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_EQ_in_objectLiteralPart6952 = new BitSet(new long[]{0x4017900060010060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_bindOpt_in_objectLiteralPart6955 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_objectLiteralPart6957 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_objectLiteralPart6961 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localOperationDefinition_in_objectLiteralPart6973 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_objectLiteralPart6985 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localTriggerStatement_in_objectLiteralPart6997 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDefinition_in_objectLiteralPart7009 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_bracketExpression7019 = new BitSet(new long[]{0x4017900000000060L,0xB82C007009ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_bracketExpression7034 = new BitSet(new long[]{0x0000000000000090L,0x0000000005000000L});
    public static final BitSet FOLLOW_COMMA_in_bracketExpression7061 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_bracketExpression7063 = new BitSet(new long[]{0x0000000000000080L,0x0000000005000000L});
    public static final BitSet FOLLOW_DOTDOT_in_bracketExpression7077 = new BitSet(new long[]{0x4017900000000060L,0xB82C007088ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_LT_in_bracketExpression7081 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_bracketExpression7084 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_COMMA_in_bracketExpression7098 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_bracketExpression7102 = new BitSet(new long[]{0x0000000000000000L,0x0000000005000000L});
    public static final BitSet FOLLOW_BAR_in_bracketExpression7129 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_generator_in_bracketExpression7133 = new BitSet(new long[]{0x0000000000000000L,0x0000000005000000L});
    public static final BitSet FOLLOW_COMMA_in_bracketExpression7138 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_generator_in_bracketExpression7143 = new BitSet(new long[]{0x0000000000000000L,0x0000000005000000L});
    public static final BitSet FOLLOW_expression_in_bracketExpression7147 = new BitSet(new long[]{0x0000000000000000L,0x0000000005000000L});
    public static final BitSet FOLLOW_DOTDOT_in_bracketExpression7166 = new BitSet(new long[]{0x4017900000000060L,0xB82C007088ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_LT_in_bracketExpression7170 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_bracketExpression7174 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_bracketExpression7196 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_generator7206 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_LARROW_in_generator7210 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_generator7214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INDEXOF_in_ordinalExpression7222 = new BitSet(new long[]{0x0000000000000000L,0x0800000008000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_ordinalExpression7230 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_ordinalExpression7238 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_contextExpression7250 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression7272 = new BitSet(new long[]{0x4017900000000060L,0xBC2C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression7281 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_stringExpression7292 = new BitSet(new long[]{0x0000000000000000L,0x0180000000000000L});
    public static final BitSet FOLLOW_RBRACE_LBRACE_STRING_LITERAL_in_stringExpression7307 = new BitSet(new long[]{0x4017900000000060L,0xBC2C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_formatOrNull_in_stringExpression7319 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_stringExpression7333 = new BitSet(new long[]{0x0000000000000000L,0x0180000000000000L});
    public static final BitSet FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression7354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FORMAT_STRING_LITERAL_in_formatOrNull7384 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt7416 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_expressionListOpt7427 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt7433 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_ORDER_in_orderBy7455 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_BY_in_orderBy7459 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_orderBy7463 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INDEX_in_indexOn7478 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ON_in_indexOn7482 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_indexOn7486 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_COMMA_in_indexOn7494 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_indexOn7498 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_set_in_multiplyOperator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_POUND_in_unaryOperator7542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_unaryOperator7553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUB_in_unaryOperator7566 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_unaryOperator7579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIZEOF_in_unaryOperator7592 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TYPEOF_in_unaryOperator7605 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REVERSE_in_unaryOperator7618 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_unaryOperator7631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_unaryOperator7644 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSEQ_in_assignmentOperator7665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBEQ_in_assignmentOperator7678 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAREQ_in_assignmentOperator7691 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SLASHEQ_in_assignmentOperator7704 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENTEQ_in_assignmentOperator7717 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLON_in_typeReference7741 = new BitSet(new long[]{0x0000000000000000L,0x0800008000200060L,0x0000000000000040L});
    public static final BitSet FOLLOW_set_in_typeReference7781 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_typeReference7790 = new BitSet(new long[]{0x0000000000000000L,0x0006008800800000L});
    public static final BitSet FOLLOW_typeReference_in_typeReference7792 = new BitSet(new long[]{0x0000000000000002L,0x0004008800800000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference7796 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeName_in_typeReference7850 = new BitSet(new long[]{0x0000000000000002L,0x0004008800800000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference7854 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_typeReference7880 = new BitSet(new long[]{0x0000000000000002L,0x0004008800800000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference7884 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_cardinalityConstraint7915 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_cardinalityConstraint7919 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_cardinalityConstraint7931 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUS_in_cardinalityConstraint7958 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_cardinalityConstraint7985 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_literal8054 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DECIMAL_LITERAL_in_literal8064 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OCTAL_LITERAL_in_literal8074 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_HEX_LITERAL_in_literal8084 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOATING_POINT_LITERAL_in_literal8095 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRUE_in_literal8105 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALSE_in_literal8119 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_literal8133 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualident_in_typeName8160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_qualident8204 = new BitSet(new long[]{0x0000000000000002L,0x0000000008000000L});
    public static final BitSet FOLLOW_DOT_in_qualident8233 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_qualident8237 = new BitSet(new long[]{0x0000000000000002L,0x0000000008000000L});
    public static final BitSet FOLLOW_name_in_identifier8274 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_name8308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_synpred453723 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localOperationDefinition_in_synpred463731 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_backgroundStatement_in_synpred473745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_laterStatement_in_synpred483760 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifStatement_in_synpred503796 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred533839 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_SEMI_in_synpred533843 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forAlphaStatement_in_synpred583920 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forJoinStatement_in_synpred593936 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FPS_in_synpred814862 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_synpred814866 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_synpred824880 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_synpred824884 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_synpred834898 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_IF_in_synpred834902 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_synpred834906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LINEAR_in_synpred875045 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EASEIN_in_synpred885053 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EASEOUT_in_synpred895061 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EASEBOTH_in_synpred905069 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MOTION_in_synpred915077 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_synpred915081 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FPS_in_synpred925095 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_synpred925099 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_synpred935111 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_synpred935115 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_synpred945129 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_IF_in_synpred945133 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_synpred945137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1055434 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_typeName_in_synpred1055440 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1055443 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_suffixedExpression_in_synpred1055447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeReference_in_synpred1085633 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeReference_in_synpred1095655 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMA_in_synpred1115725 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_selectionVar_in_synpred1115729 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_synpred1125743 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_synpred1125747 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_synpred1175820 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_synpred1185824 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUB_in_synpred1326297 = new BitSet(new long[]{0x0007800000000060L,0xB82C007008AD4003L,0x0000000000000048L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_synpred1326304 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1386526 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008EDC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expressionListOpt_in_synpred1386528 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1386530 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_synpred1396473 = new BitSet(new long[]{0x0000000000200000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_CLASS_in_synpred1396477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_synpred1396501 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1396526 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008EDC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expressionListOpt_in_synpred1396528 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1396530 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred1416562 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_name_in_synpred1416565 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_BAR_in_synpred1416567 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008ADC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expression_in_synpred1416571 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred1416574 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeName_in_synpred1436611 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_synpred1436613 = new BitSet(new long[]{0x0000004200004000L,0x0A00000000000060L,0x0000000000000040L});
    public static final BitSet FOLLOW_objectLiteral_in_synpred1436616 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_synpred1436618 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1496726 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008EDC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expressionListOpt_in_synpred1496730 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1496734 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_synpred1506705 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1506726 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008EDC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expressionListOpt_in_synpred1506730 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1506734 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1536837 = new BitSet(new long[]{0x4017900000000060L,0xB82C007008EDC063L,0x0000000000000048L});
    public static final BitSet FOLLOW_expressionListOpt_in_synpred1536841 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1536845 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMA_in_synpred1777494 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_synpred1777498 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred1977915 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred1977919 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_synpred1987931 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUS_in_synpred1997958 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_synpred2007985 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_synpred2088233 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_synpred2088237 = new BitSet(new long[]{0x0000000000000002L});

}