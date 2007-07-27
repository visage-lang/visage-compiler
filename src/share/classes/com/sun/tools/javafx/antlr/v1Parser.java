// $ANTLR 3.0 C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g 2007-07-26 21:48:36

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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "BAR", "POUND", "TYPEOF", "DOTDOT", "LARROW", "ABSTRACT", "AFTER", "AND", "AS", "ASSERT", "ATTRIBUTE", "BEFORE", "BIND", "BIBIND", "BREAK", "BY", "CATCH", "CLASS", "DELETE", "DISTINCT", "DO", "DUR", "EASEBOTH", "EASEIN", "EASEOUT", "TIE", "STAYS", "RETURN", "THROW", "VAR", "PACKAGE", "IMPORT", "FROM", "LATER", "TRIGGER", "ON", "INSERT", "INTO", "FIRST", "LAST", "IF", "THEN", "ELSE", "THIS", "NULL", "TRUE", "FALSE", "FOR", "UNITINTERVAL", "IN", "FPS", "WHILE", "CONTINUE", "LINEAR", "MOTION", "TRY", "FINALLY", "LAZY", "FOREACH", "WHERE", "NOT", "NEW", "PRIVATE", "PROTECTED", "PUBLIC", "OPERATION", "FUNCTION", "READONLY", "INVERSE", "TYPE", "EXTENDS", "ORDER", "INDEX", "INSTANCEOF", "INDEXOF", "SELECT", "SUPER", "OR", "SIZEOF", "REVERSE", "XOR", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACKET", "RBRACKET", "SEMI", "COMMA", "DOT", "EQEQ", "EQ", "GT", "LT", "LTGT", "LTEQ", "GTEQ", "PLUS", "PLUSPLUS", "SUB", "SUBSUB", "STAR", "SLASH", "PERCENT", "PLUSEQ", "SUBEQ", "STAREQ", "SLASHEQ", "PERCENTEQ", "LTLT", "GTGT", "COLON", "QUES", "STRING_LITERAL", "QUOTE_LBRACE_STRING_LITERAL", "RBRACE_QUOTE_STRING_LITERAL", "RBRACE_LBRACE_STRING_LITERAL", "FORMAT_STRING_LITERAL", "QUOTED_IDENTIFIER", "INTEGER_LITERAL", "Exponent", "FLOATING_POINT_LITERAL", "Letter", "JavaIDDigit", "IDENTIFIER", "WS", "COMMENT", "LINE_COMMENT"
    };
    public static final int PACKAGE=34;
    public static final int FUNCTION=70;
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
    public static final int GTGT=114;
    public static final int EOF=-1;
    public static final int RBRACE_QUOTE_STRING_LITERAL=119;
    public static final int BREAK=18;
    public static final int TYPE=73;
    public static final int LBRACKET=89;
    public static final int RPAREN=86;
    public static final int IMPORT=35;
    public static final int LINEAR=57;
    public static final int STRING_LITERAL=117;
    public static final int FLOATING_POINT_LITERAL=125;
    public static final int INSERT=40;
    public static final int SUBSUB=104;
    public static final int BIND=16;
    public static final int STAREQ=110;
    public static final int RETURN=31;
    public static final int THIS=47;
    public static final int VAR=33;
    public static final int SUPER=80;
    public static final int EQ=95;
    public static final int LAST=43;
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
    public static final int AS=12;
    public static final int INDEX=76;
    public static final int SLASH=106;
    public static final int THEN=45;
    public static final int IN=53;
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
    public static final int GTEQ=100;
    public static final int AFTER=10;
    public static final int READONLY=71;
    public static final int TRUE=49;
    public static final int SEMI=91;
    public static final int COLON=115;
    public static final int PERCENTEQ=112;
    public static final int FINALLY=60;
    public static final int FORMAT_STRING_LITERAL=121;
    public static final int EASEIN=27;
    public static final int QUOTED_IDENTIFIER=122;
    public static final int FPS=54;
    public static final int PUBLIC=68;
    public static final int EXTENDS=74;
    public static final int BAR=4;
    public static final int FIRST=42;

        public v1Parser(TokenStream input) {
            super(input);
            ruleMemo = new HashMap[295+1];
         }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g"; }

    
            public v1Parser(Context context, CharSequence content) {
               this(new CommonTokenStream(new v1Lexer(new ANTLRStringStream(content.toString()))));
               initialize(context);
        	}



    // $ANTLR start module
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:233:1: module returns [JCCompilationUnit result] : ( packageDecl )? moduleItems EOF ;
    public final JCCompilationUnit module() throws RecognitionException {
        JCCompilationUnit result = null;

        JCExpression packageDecl1 = null;

        ListBuffer<JCTree> moduleItems2 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:234:8: ( ( packageDecl )? moduleItems EOF )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:234:10: ( packageDecl )? moduleItems EOF
            {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:234:10: ( packageDecl )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==PACKAGE) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: packageDecl
                    {
                    pushFollow(FOLLOW_packageDecl_in_module1681);
                    packageDecl1=packageDecl();
                    _fsp--;
                    if (failed) return result;

                    }
                    break;

            }

            pushFollow(FOLLOW_moduleItems_in_module1684);
            moduleItems2=moduleItems();
            _fsp--;
            if (failed) return result;
            match(input,EOF,FOLLOW_EOF_in_module1686); if (failed) return result;
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:236:1: packageDecl returns [JCExpression value] : PACKAGE qualident SEMI ;
    public final JCExpression packageDecl() throws RecognitionException {
        JCExpression value = null;

        JCExpression qualident3 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:237:8: ( PACKAGE qualident SEMI )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:237:10: PACKAGE qualident SEMI
            {
            match(input,PACKAGE,FOLLOW_PACKAGE_in_packageDecl1719); if (failed) return value;
            pushFollow(FOLLOW_qualident_in_packageDecl1721);
            qualident3=qualident();
            _fsp--;
            if (failed) return value;
            match(input,SEMI,FOLLOW_SEMI_in_packageDecl1723); if (failed) return value;
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:238:1: moduleItems returns [ListBuffer<JCTree> items = new ListBuffer<JCTree>()] : ( moduleItem )* ;
    public final ListBuffer<JCTree> moduleItems() throws RecognitionException {
        ListBuffer<JCTree> items =  new ListBuffer<JCTree>();

        JCTree moduleItem4 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:239:9: ( ( moduleItem )* )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:239:11: ( moduleItem )*
            {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:239:11: ( moduleItem )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=POUND && LA2_0<=TYPEOF)||LA2_0==ABSTRACT||LA2_0==ATTRIBUTE||LA2_0==BREAK||(LA2_0>=CLASS && LA2_0<=DELETE)||LA2_0==DO||(LA2_0>=RETURN && LA2_0<=VAR)||LA2_0==IMPORT||LA2_0==TRIGGER||LA2_0==INSERT||LA2_0==IF||(LA2_0>=THIS && LA2_0<=UNITINTERVAL)||(LA2_0>=WHILE && LA2_0<=CONTINUE)||LA2_0==TRY||LA2_0==FOREACH||(LA2_0>=NOT && LA2_0<=READONLY)||(LA2_0>=INDEXOF && LA2_0<=SUPER)||(LA2_0>=SIZEOF && LA2_0<=REVERSE)||LA2_0==LPAREN||LA2_0==LBRACKET||LA2_0==DOT||(LA2_0>=PLUSPLUS && LA2_0<=SUBSUB)||(LA2_0>=QUES && LA2_0<=QUOTE_LBRACE_STRING_LITERAL)||(LA2_0>=QUOTED_IDENTIFIER && LA2_0<=INTEGER_LITERAL)||LA2_0==FLOATING_POINT_LITERAL||LA2_0==IDENTIFIER) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:239:12: moduleItem
            	    {
            	    pushFollow(FOLLOW_moduleItem_in_moduleItems1752);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:240:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );
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
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:241:8: ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept )
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
                    alt3=4;
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("240:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );", 3, 4, input);

                    throw nvae;
                }
                }
                break;
            case FUNCTION:
                {
                int LA3_5 = input.LA(2);

                if ( (LA3_5==QUOTED_IDENTIFIER||LA3_5==IDENTIFIER) ) {
                    alt3=5;
                }
                else if ( (LA3_5==LPAREN) ) {
                    alt3=7;
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("240:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );", 3, 5, input);

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
                    new NoViableAltException("240:1: moduleItem returns [JCTree value] : ( importDecl | classDefinition | attributeDefinition | memberOperationDefinition | memberFunctionDefinition | TRIGGER ON changeRule | statementExcept );", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:241:10: importDecl
                    {
                    pushFollow(FOLLOW_importDecl_in_moduleItem1794);
                    importDecl5=importDecl();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = importDecl5; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:242:10: classDefinition
                    {
                    pushFollow(FOLLOW_classDefinition_in_moduleItem1809);
                    classDefinition6=classDefinition();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = classDefinition6; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:243:10: attributeDefinition
                    {
                    pushFollow(FOLLOW_attributeDefinition_in_moduleItem1824);
                    attributeDefinition7=attributeDefinition();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = attributeDefinition7; 
                    }

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:244:10: memberOperationDefinition
                    {
                    pushFollow(FOLLOW_memberOperationDefinition_in_moduleItem1839);
                    memberOperationDefinition8=memberOperationDefinition();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = memberOperationDefinition8; 
                    }

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:245:10: memberFunctionDefinition
                    {
                    pushFollow(FOLLOW_memberFunctionDefinition_in_moduleItem1853);
                    memberFunctionDefinition9=memberFunctionDefinition();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = memberFunctionDefinition9; 
                    }

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:246:10: TRIGGER ON changeRule
                    {
                    match(input,TRIGGER,FOLLOW_TRIGGER_in_moduleItem1867); if (failed) return value;
                    match(input,ON,FOLLOW_ON_in_moduleItem1869); if (failed) return value;
                    pushFollow(FOLLOW_changeRule_in_moduleItem1871);
                    changeRule10=changeRule();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = changeRule10; 
                    }

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:247:10: statementExcept
                    {
                    pushFollow(FOLLOW_statementExcept_in_moduleItem1885);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:248:1: importDecl returns [JCTree value] : IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI ;
    public final JCTree importDecl() throws RecognitionException {
        JCTree value = null;

        Token IMPORT14=null;
        JCIdent identifier12 = null;

        Name name13 = null;


         JCExpression pid = null; 
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:250:9: ( IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:250:11: IMPORT identifier ( DOT name )* ( DOT STAR )? SEMI
            {
            IMPORT14=(Token)input.LT(1);
            match(input,IMPORT,FOLLOW_IMPORT_in_importDecl1914); if (failed) return value;
            pushFollow(FOLLOW_identifier_in_importDecl1917);
            identifier12=identifier();
            _fsp--;
            if (failed) return value;
            if ( backtracking==0 ) {
               pid = identifier12; 
            }
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:251:18: ( DOT name )*
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
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:251:20: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_importDecl1941); if (failed) return value;
            	    pushFollow(FOLLOW_name_in_importDecl1943);
            	    name13=name();
            	    _fsp--;
            	    if (failed) return value;
            	    if ( backtracking==0 ) {
            	       pid = F.Select(pid, name13); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:252:18: ( DOT STAR )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==DOT) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:252:20: DOT STAR
                    {
                    match(input,DOT,FOLLOW_DOT_in_importDecl1971); if (failed) return value;
                    match(input,STAR,FOLLOW_STAR_in_importDecl1973); if (failed) return value;
                    if ( backtracking==0 ) {
                       pid = F.Select(pid, names.asterisk); 
                    }

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_importDecl1981); if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(IMPORT14.getTokenIndex()).Import(pid, false); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:254:1: classDefinition returns [JFXClassDeclaration value] : modifierFlags CLASS name supers LBRACE classMembers RBRACE ;
    public final JFXClassDeclaration classDefinition() throws RecognitionException {
        JFXClassDeclaration value = null;

        JCModifiers modifierFlags15 = null;

        Name name16 = null;

        ListBuffer<Name> supers17 = null;

        ListBuffer<JFXMemberDeclaration> classMembers18 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:255:2: ( modifierFlags CLASS name supers LBRACE classMembers RBRACE )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:255:4: modifierFlags CLASS name supers LBRACE classMembers RBRACE
            {
            pushFollow(FOLLOW_modifierFlags_in_classDefinition2007);
            modifierFlags15=modifierFlags();
            _fsp--;
            if (failed) return value;
            match(input,CLASS,FOLLOW_CLASS_in_classDefinition2010); if (failed) return value;
            pushFollow(FOLLOW_name_in_classDefinition2012);
            name16=name();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_supers_in_classDefinition2014);
            supers17=supers();
            _fsp--;
            if (failed) return value;
            match(input,LBRACE,FOLLOW_LBRACE_in_classDefinition2016); if (failed) return value;
            pushFollow(FOLLOW_classMembers_in_classDefinition2018);
            classMembers18=classMembers();
            _fsp--;
            if (failed) return value;
            match(input,RBRACE,FOLLOW_RBRACE_in_classDefinition2020); if (failed) return value;
            if ( backtracking==0 ) {
               value = F.ClassDeclaration(modifierFlags15, name16,
              	                                supers17.toList(), classMembers18.toList()); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:258:1: supers returns [ListBuffer<Name> names = new ListBuffer<Name>()] : ( EXTENDS name1= name ( COMMA namen= name )* )? ;
    public final ListBuffer<Name> supers() throws RecognitionException {
        ListBuffer<Name> names =  new ListBuffer<Name>();

        Name name1 = null;

        Name namen = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:259:2: ( ( EXTENDS name1= name ( COMMA namen= name )* )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:259:4: ( EXTENDS name1= name ( COMMA namen= name )* )?
            {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:259:4: ( EXTENDS name1= name ( COMMA namen= name )* )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==EXTENDS) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:259:5: EXTENDS name1= name ( COMMA namen= name )*
                    {
                    match(input,EXTENDS,FOLLOW_EXTENDS_in_supers2040); if (failed) return names;
                    pushFollow(FOLLOW_name_in_supers2044);
                    name1=name();
                    _fsp--;
                    if (failed) return names;
                    if ( backtracking==0 ) {
                       names.append(name1); 
                    }
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:260:12: ( COMMA namen= name )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==COMMA) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:260:14: COMMA namen= name
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_supers2067); if (failed) return names;
                    	    pushFollow(FOLLOW_name_in_supers2071);
                    	    namen=name();
                    	    _fsp--;
                    	    if (failed) return names;
                    	    if ( backtracking==0 ) {
                    	       names.append(namen); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:262:1: classMembers returns [ListBuffer<JFXMemberDeclaration> mems = new ListBuffer<JFXMemberDeclaration>()] : ( attributeDecl | functionDecl | operationDecl )* ;
    public final ListBuffer<JFXMemberDeclaration> classMembers() throws RecognitionException {
        ListBuffer<JFXMemberDeclaration> mems =  new ListBuffer<JFXMemberDeclaration>();

        JFXAttributeDeclaration attributeDecl19 = null;

        JFXFunctionMemberDeclaration functionDecl20 = null;

        JFXOperationMemberDeclaration operationDecl21 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:263:2: ( ( attributeDecl | functionDecl | operationDecl )* )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:263:3: ( attributeDecl | functionDecl | operationDecl )*
            {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:263:3: ( attributeDecl | functionDecl | operationDecl )*
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
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:263:5: attributeDecl
            	    {
            	    pushFollow(FOLLOW_attributeDecl_in_classMembers2100);
            	    attributeDecl19=attributeDecl();
            	    _fsp--;
            	    if (failed) return mems;
            	    if ( backtracking==0 ) {
            	       mems.append(attributeDecl19); 
            	    }

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:264:5: functionDecl
            	    {
            	    pushFollow(FOLLOW_functionDecl_in_classMembers2123);
            	    functionDecl20=functionDecl();
            	    _fsp--;
            	    if (failed) return mems;
            	    if ( backtracking==0 ) {
            	       mems.append(functionDecl20); 
            	    }

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:265:5: operationDecl
            	    {
            	    pushFollow(FOLLOW_operationDecl_in_classMembers2147);
            	    operationDecl21=operationDecl();
            	    _fsp--;
            	    if (failed) return mems;
            	    if ( backtracking==0 ) {
            	       mems.append(operationDecl21); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:267:1: attributeDecl returns [JFXAttributeDeclaration decl] : modifierFlags ATTRIBUTE name typeReference inverseClause ( orderBy | indexOn )? SEMI ;
    public final JFXAttributeDeclaration attributeDecl() throws RecognitionException {
        JFXAttributeDeclaration decl = null;

        JCModifiers modifierFlags22 = null;

        Name name23 = null;

        JFXType typeReference24 = null;

        JFXMemberSelector inverseClause25 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:268:2: ( modifierFlags ATTRIBUTE name typeReference inverseClause ( orderBy | indexOn )? SEMI )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:268:4: modifierFlags ATTRIBUTE name typeReference inverseClause ( orderBy | indexOn )? SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_attributeDecl2184);
            modifierFlags22=modifierFlags();
            _fsp--;
            if (failed) return decl;
            match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeDecl2186); if (failed) return decl;
            pushFollow(FOLLOW_name_in_attributeDecl2188);
            name23=name();
            _fsp--;
            if (failed) return decl;
            pushFollow(FOLLOW_typeReference_in_attributeDecl2190);
            typeReference24=typeReference();
            _fsp--;
            if (failed) return decl;
            pushFollow(FOLLOW_inverseClause_in_attributeDecl2192);
            inverseClause25=inverseClause();
            _fsp--;
            if (failed) return decl;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:268:62: ( orderBy | indexOn )?
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
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:268:63: orderBy
                    {
                    pushFollow(FOLLOW_orderBy_in_attributeDecl2196);
                    orderBy();
                    _fsp--;
                    if (failed) return decl;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:268:73: indexOn
                    {
                    pushFollow(FOLLOW_indexOn_in_attributeDecl2200);
                    indexOn();
                    _fsp--;
                    if (failed) return decl;

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_attributeDecl2204); if (failed) return decl;
            if ( backtracking==0 ) {
               decl = F.AttributeDeclaration(modifierFlags22, name23, typeReference24,
              	                                    inverseClause25, null/*order/index*/); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:271:1: inverseClause returns [JFXMemberSelector inverse = null] : ( INVERSE memberSelector )? ;
    public final JFXMemberSelector inverseClause() throws RecognitionException {
        JFXMemberSelector inverse =  null;

        JFXMemberSelector memberSelector26 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:272:2: ( ( INVERSE memberSelector )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:272:4: ( INVERSE memberSelector )?
            {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:272:4: ( INVERSE memberSelector )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==INVERSE) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:272:5: INVERSE memberSelector
                    {
                    match(input,INVERSE,FOLLOW_INVERSE_in_inverseClause2223); if (failed) return inverse;
                    pushFollow(FOLLOW_memberSelector_in_inverseClause2225);
                    memberSelector26=memberSelector();
                    _fsp--;
                    if (failed) return inverse;
                    if ( backtracking==0 ) {
                       inverse = memberSelector26; 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:273:1: functionDecl returns [JFXFunctionMemberDeclaration decl] : modifierFlags FUNCTION name formalParameters typeReference SEMI ;
    public final JFXFunctionMemberDeclaration functionDecl() throws RecognitionException {
        JFXFunctionMemberDeclaration decl = null;

        JCModifiers modifierFlags27 = null;

        Name name28 = null;

        JFXType typeReference29 = null;

        ListBuffer<JCTree> formalParameters30 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:274:2: ( modifierFlags FUNCTION name formalParameters typeReference SEMI )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:274:4: modifierFlags FUNCTION name formalParameters typeReference SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_functionDecl2243);
            modifierFlags27=modifierFlags();
            _fsp--;
            if (failed) return decl;
            match(input,FUNCTION,FOLLOW_FUNCTION_in_functionDecl2245); if (failed) return decl;
            pushFollow(FOLLOW_name_in_functionDecl2247);
            name28=name();
            _fsp--;
            if (failed) return decl;
            pushFollow(FOLLOW_formalParameters_in_functionDecl2249);
            formalParameters30=formalParameters();
            _fsp--;
            if (failed) return decl;
            pushFollow(FOLLOW_typeReference_in_functionDecl2251);
            typeReference29=typeReference();
            _fsp--;
            if (failed) return decl;
            match(input,SEMI,FOLLOW_SEMI_in_functionDecl2253); if (failed) return decl;
            if ( backtracking==0 ) {
               decl =  F.FunctionDeclaration(modifierFlags27, name28, typeReference29,
              	                                            formalParameters30.toList()); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:278:1: operationDecl returns [JFXOperationMemberDeclaration decl] : modifierFlags OPERATION name formalParameters typeReference SEMI ;
    public final JFXOperationMemberDeclaration operationDecl() throws RecognitionException {
        JFXOperationMemberDeclaration decl = null;

        JCModifiers modifierFlags31 = null;

        Name name32 = null;

        JFXType typeReference33 = null;

        ListBuffer<JCTree> formalParameters34 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:279:2: ( modifierFlags OPERATION name formalParameters typeReference SEMI )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:279:4: modifierFlags OPERATION name formalParameters typeReference SEMI
            {
            pushFollow(FOLLOW_modifierFlags_in_operationDecl2272);
            modifierFlags31=modifierFlags();
            _fsp--;
            if (failed) return decl;
            match(input,OPERATION,FOLLOW_OPERATION_in_operationDecl2276); if (failed) return decl;
            pushFollow(FOLLOW_name_in_operationDecl2280);
            name32=name();
            _fsp--;
            if (failed) return decl;
            pushFollow(FOLLOW_formalParameters_in_operationDecl2284);
            formalParameters34=formalParameters();
            _fsp--;
            if (failed) return decl;
            pushFollow(FOLLOW_typeReference_in_operationDecl2288);
            typeReference33=typeReference();
            _fsp--;
            if (failed) return decl;
            match(input,SEMI,FOLLOW_SEMI_in_operationDecl2293); if (failed) return decl;
            if ( backtracking==0 ) {
               decl = F.OperationDeclaration(modifierFlags31, name32, typeReference33,
              	                                            formalParameters34.toList()); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:282:1: attributeDefinition returns [JFXAttributeDefinition def] : ATTRIBUTE memberSelector EQ bindOpt expression SEMI ;
    public final JFXAttributeDefinition attributeDefinition() throws RecognitionException {
        JFXAttributeDefinition def = null;

        Token ATTRIBUTE35=null;
        JFXMemberSelector memberSelector36 = null;

        JCExpression expression37 = null;

        JavafxBindStatus bindOpt38 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:283:2: ( ATTRIBUTE memberSelector EQ bindOpt expression SEMI )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:283:4: ATTRIBUTE memberSelector EQ bindOpt expression SEMI
            {
            ATTRIBUTE35=(Token)input.LT(1);
            match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeDefinition2312); if (failed) return def;
            pushFollow(FOLLOW_memberSelector_in_attributeDefinition2316);
            memberSelector36=memberSelector();
            _fsp--;
            if (failed) return def;
            match(input,EQ,FOLLOW_EQ_in_attributeDefinition2320); if (failed) return def;
            pushFollow(FOLLOW_bindOpt_in_attributeDefinition2322);
            bindOpt38=bindOpt();
            _fsp--;
            if (failed) return def;
            pushFollow(FOLLOW_expression_in_attributeDefinition2325);
            expression37=expression();
            _fsp--;
            if (failed) return def;
            match(input,SEMI,FOLLOW_SEMI_in_attributeDefinition2329); if (failed) return def;
            if ( backtracking==0 ) {
               def = F.at(ATTRIBUTE35.getTokenIndex()).AttributeDefinition(memberSelector36, expression37, bindOpt38); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:285:1: memberOperationDefinition returns [JFXOperationMemberDefinition def] : OPERATION memberSelector formalParameters typeReference block ;
    public final JFXOperationMemberDefinition memberOperationDefinition() throws RecognitionException {
        JFXOperationMemberDefinition def = null;

        Token OPERATION39=null;
        JFXMemberSelector memberSelector40 = null;

        JFXType typeReference41 = null;

        ListBuffer<JCTree> formalParameters42 = null;

        JCBlock block43 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:286:2: ( OPERATION memberSelector formalParameters typeReference block )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:286:4: OPERATION memberSelector formalParameters typeReference block
            {
            OPERATION39=(Token)input.LT(1);
            match(input,OPERATION,FOLLOW_OPERATION_in_memberOperationDefinition2348); if (failed) return def;
            pushFollow(FOLLOW_memberSelector_in_memberOperationDefinition2352);
            memberSelector40=memberSelector();
            _fsp--;
            if (failed) return def;
            pushFollow(FOLLOW_formalParameters_in_memberOperationDefinition2356);
            formalParameters42=formalParameters();
            _fsp--;
            if (failed) return def;
            pushFollow(FOLLOW_typeReference_in_memberOperationDefinition2360);
            typeReference41=typeReference();
            _fsp--;
            if (failed) return def;
            pushFollow(FOLLOW_block_in_memberOperationDefinition2363);
            block43=block();
            _fsp--;
            if (failed) return def;
            if ( backtracking==0 ) {
               def = F.at(OPERATION39.getTokenIndex()).OperationDefinition(memberSelector40, typeReference41, 
              		              formalParameters42.toList(), block43); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:289:1: memberFunctionDefinition returns [JFXFunctionMemberDefinition def] : FUNCTION memberSelector formalParameters typeReference block ;
    public final JFXFunctionMemberDefinition memberFunctionDefinition() throws RecognitionException {
        JFXFunctionMemberDefinition def = null;

        Token FUNCTION44=null;
        JFXMemberSelector memberSelector45 = null;

        JFXType typeReference46 = null;

        ListBuffer<JCTree> formalParameters47 = null;

        JCBlock block48 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:290:2: ( FUNCTION memberSelector formalParameters typeReference block )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:290:4: FUNCTION memberSelector formalParameters typeReference block
            {
            FUNCTION44=(Token)input.LT(1);
            match(input,FUNCTION,FOLLOW_FUNCTION_in_memberFunctionDefinition2382); if (failed) return def;
            pushFollow(FOLLOW_memberSelector_in_memberFunctionDefinition2386);
            memberSelector45=memberSelector();
            _fsp--;
            if (failed) return def;
            pushFollow(FOLLOW_formalParameters_in_memberFunctionDefinition2390);
            formalParameters47=formalParameters();
            _fsp--;
            if (failed) return def;
            pushFollow(FOLLOW_typeReference_in_memberFunctionDefinition2394);
            typeReference46=typeReference();
            _fsp--;
            if (failed) return def;
            pushFollow(FOLLOW_block_in_memberFunctionDefinition2397);
            block48=block();
            _fsp--;
            if (failed) return def;
            if ( backtracking==0 ) {
               def = F.at(FUNCTION44.getTokenIndex()).FunctionDefinition(memberSelector45, typeReference46, 
              		              formalParameters47.toList(), block48); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:293:1: functionBody : ( EQ expression ( whereVarDecls )? SEMI | LBRACE ( variableDefinition | localFunctionDefinition | localOperationDefinition )* RETURN expression ( SEMI )? RBRACE );
    public final void functionBody() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:294:2: ( EQ expression ( whereVarDecls )? SEMI | LBRACE ( variableDefinition | localFunctionDefinition | localOperationDefinition )* RETURN expression ( SEMI )? RBRACE )
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
                    new NoViableAltException("293:1: functionBody : ( EQ expression ( whereVarDecls )? SEMI | LBRACE ( variableDefinition | localFunctionDefinition | localOperationDefinition )* RETURN expression ( SEMI )? RBRACE );", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:294:4: EQ expression ( whereVarDecls )? SEMI
                    {
                    match(input,EQ,FOLLOW_EQ_in_functionBody2413); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_functionBody2417);
                    expression();
                    _fsp--;
                    if (failed) return ;
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:294:22: ( whereVarDecls )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==WHERE) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: whereVarDecls
                            {
                            pushFollow(FOLLOW_whereVarDecls_in_functionBody2421);
                            whereVarDecls();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }

                    match(input,SEMI,FOLLOW_SEMI_in_functionBody2427); if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:295:11: LBRACE ( variableDefinition | localFunctionDefinition | localOperationDefinition )* RETURN expression ( SEMI )? RBRACE
                    {
                    match(input,LBRACE,FOLLOW_LBRACE_in_functionBody2443); if (failed) return ;
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:295:20: ( variableDefinition | localFunctionDefinition | localOperationDefinition )*
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
                    	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:295:24: variableDefinition
                    	    {
                    	    pushFollow(FOLLOW_variableDefinition_in_functionBody2451);
                    	    variableDefinition();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;
                    	case 2 :
                    	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:295:49: localFunctionDefinition
                    	    {
                    	    pushFollow(FOLLOW_localFunctionDefinition_in_functionBody2459);
                    	    localFunctionDefinition();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;
                    	case 3 :
                    	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:295:79: localOperationDefinition
                    	    {
                    	    pushFollow(FOLLOW_localOperationDefinition_in_functionBody2467);
                    	    localOperationDefinition();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);

                    match(input,RETURN,FOLLOW_RETURN_in_functionBody2477); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_functionBody2481);
                    expression();
                    _fsp--;
                    if (failed) return ;
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:295:134: ( SEMI )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==SEMI) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: SEMI
                            {
                            match(input,SEMI,FOLLOW_SEMI_in_functionBody2485); if (failed) return ;

                            }
                            break;

                    }

                    match(input,RBRACE,FOLLOW_RBRACE_in_functionBody2491); if (failed) return ;

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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:296:1: whereVarDecls : WHERE whereVarDecl ( COMMA whereVarDecl )* ;
    public final void whereVarDecls() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:296:15: ( WHERE whereVarDecl ( COMMA whereVarDecl )* )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:296:17: WHERE whereVarDecl ( COMMA whereVarDecl )*
            {
            match(input,WHERE,FOLLOW_WHERE_in_whereVarDecls2499); if (failed) return ;
            pushFollow(FOLLOW_whereVarDecl_in_whereVarDecls2503);
            whereVarDecl();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:296:40: ( COMMA whereVarDecl )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==COMMA) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:296:44: COMMA whereVarDecl
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_whereVarDecls2511); if (failed) return ;
            	    pushFollow(FOLLOW_whereVarDecl_in_whereVarDecls2515);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:297:1: whereVarDecl : ( localFunctionDefinition | name typeReference EQ expression );
    public final void whereVarDecl() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:297:14: ( localFunctionDefinition | name typeReference EQ expression )
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
                        new NoViableAltException("297:1: whereVarDecl : ( localFunctionDefinition | name typeReference EQ expression );", 16, 2, input);

                    throw nvae;
                }
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("297:1: whereVarDecl : ( localFunctionDefinition | name typeReference EQ expression );", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:297:16: localFunctionDefinition
                    {
                    pushFollow(FOLLOW_localFunctionDefinition_in_whereVarDecl2529);
                    localFunctionDefinition();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:298:10: name typeReference EQ expression
                    {
                    pushFollow(FOLLOW_name_in_whereVarDecl2541);
                    name();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_typeReference_in_whereVarDecl2545);
                    typeReference();
                    _fsp--;
                    if (failed) return ;
                    match(input,EQ,FOLLOW_EQ_in_whereVarDecl2549); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_whereVarDecl2553);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:299:1: variableDefinition : VAR name typeReference EQ expression SEMI ;
    public final void variableDefinition() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:299:20: ( VAR name typeReference EQ expression SEMI )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:299:22: VAR name typeReference EQ expression SEMI
            {
            match(input,VAR,FOLLOW_VAR_in_variableDefinition2561); if (failed) return ;
            pushFollow(FOLLOW_name_in_variableDefinition2565);
            name();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_typeReference_in_variableDefinition2569);
            typeReference();
            _fsp--;
            if (failed) return ;
            match(input,EQ,FOLLOW_EQ_in_variableDefinition2572); if (failed) return ;
            pushFollow(FOLLOW_expression_in_variableDefinition2576);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,SEMI,FOLLOW_SEMI_in_variableDefinition2580); if (failed) return ;

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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:300:1: changeRule returns [JFXAbstractTriggerOn value] : ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );
    public final JFXAbstractTriggerOn changeRule() throws RecognitionException {
        JFXAbstractTriggerOn value = null;

        Token LPAREN49=null;
        Token LPAREN52=null;
        Token EQ56=null;
        Token LPAREN60=null;
        JCIdent id1 = null;

        JCIdent id2 = null;

        JCIdent identifier50 = null;

        JCBlock block51 = null;

        JFXMemberSelector memberSelector53 = null;

        JCIdent identifier54 = null;

        JCBlock block55 = null;

        JFXMemberSelector memberSelector57 = null;

        JCIdent identifier58 = null;

        JCBlock block59 = null;

        JFXMemberSelector memberSelector61 = null;

        JCBlock block62 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:301:2: ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block )
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

                        if ( (LA17_7==DOT) ) {
                            alt17=7;
                        }
                        else if ( (LA17_7==FROM) ) {
                            alt17=6;
                        }
                        else {
                            if (backtracking>0) {failed=true; return value;}
                            NoViableAltException nvae =
                                new NoViableAltException("300:1: changeRule returns [JFXAbstractTriggerOn value] : ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 7, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (backtracking>0) {failed=true; return value;}
                        NoViableAltException nvae =
                            new NoViableAltException("300:1: changeRule returns [JFXAbstractTriggerOn value] : ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 4, input);

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
                                    new NoViableAltException("300:1: changeRule returns [JFXAbstractTriggerOn value] : ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 11, input);

                                throw nvae;
                            }
                        }
                        else {
                            if (backtracking>0) {failed=true; return value;}
                            NoViableAltException nvae =
                                new NoViableAltException("300:1: changeRule returns [JFXAbstractTriggerOn value] : ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 8, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (backtracking>0) {failed=true; return value;}
                        NoViableAltException nvae =
                            new NoViableAltException("300:1: changeRule returns [JFXAbstractTriggerOn value] : ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 6, input);

                        throw nvae;
                    }
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("300:1: changeRule returns [JFXAbstractTriggerOn value] : ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA17_0==QUOTED_IDENTIFIER||LA17_0==IDENTIFIER) ) {
                alt17=3;
            }
            else {
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("300:1: changeRule returns [JFXAbstractTriggerOn value] : ( LPAREN NEW identifier RPAREN block | LPAREN memberSelector EQ identifier RPAREN block | memberSelector EQ identifier block | LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block | LPAREN INSERT identifier INTO memberSelector RPAREN block | LPAREN DELETE identifier FROM memberSelector RPAREN block | LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block );", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:301:4: LPAREN NEW identifier RPAREN block
                    {
                    LPAREN49=(Token)input.LT(1);
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule2594); if (failed) return value;
                    match(input,NEW,FOLLOW_NEW_in_changeRule2598); if (failed) return value;
                    pushFollow(FOLLOW_identifier_in_changeRule2602);
                    identifier50=identifier();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule2605); if (failed) return value;
                    pushFollow(FOLLOW_block_in_changeRule2608);
                    block51=block();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(LPAREN49.getTokenIndex()).TriggerOnNew(identifier50, null, block51); 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:303:4: LPAREN memberSelector EQ identifier RPAREN block
                    {
                    LPAREN52=(Token)input.LT(1);
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule2624); if (failed) return value;
                    pushFollow(FOLLOW_memberSelector_in_changeRule2628);
                    memberSelector53=memberSelector();
                    _fsp--;
                    if (failed) return value;
                    match(input,EQ,FOLLOW_EQ_in_changeRule2631); if (failed) return value;
                    pushFollow(FOLLOW_identifier_in_changeRule2633);
                    identifier54=identifier();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule2637); if (failed) return value;
                    pushFollow(FOLLOW_block_in_changeRule2640);
                    block55=block();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(LPAREN52.getTokenIndex()).TriggerOnReplace(memberSelector53, identifier54, block55); 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:305:4: memberSelector EQ identifier block
                    {
                    pushFollow(FOLLOW_memberSelector_in_changeRule2654);
                    memberSelector57=memberSelector();
                    _fsp--;
                    if (failed) return value;
                    EQ56=(Token)input.LT(1);
                    match(input,EQ,FOLLOW_EQ_in_changeRule2657); if (failed) return value;
                    pushFollow(FOLLOW_identifier_in_changeRule2659);
                    identifier58=identifier();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_block_in_changeRule2661);
                    block59=block();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(EQ56.getTokenIndex()).TriggerOnReplace(memberSelector57, identifier58, block59); 
                    }

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:307:4: LPAREN memberSelector LBRACKET id1= identifier RBRACKET EQ id2= identifier RPAREN block
                    {
                    LPAREN60=(Token)input.LT(1);
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule2675); if (failed) return value;
                    pushFollow(FOLLOW_memberSelector_in_changeRule2679);
                    memberSelector61=memberSelector();
                    _fsp--;
                    if (failed) return value;
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_changeRule2683); if (failed) return value;
                    pushFollow(FOLLOW_identifier_in_changeRule2689);
                    id1=identifier();
                    _fsp--;
                    if (failed) return value;
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_changeRule2693); if (failed) return value;
                    match(input,EQ,FOLLOW_EQ_in_changeRule2697); if (failed) return value;
                    pushFollow(FOLLOW_identifier_in_changeRule2701);
                    id2=identifier();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule2705); if (failed) return value;
                    pushFollow(FOLLOW_block_in_changeRule2708);
                    block62=block();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(LPAREN60.getTokenIndex()).TriggerOnReplaceElement(memberSelector61, id1, id2, block62); 
                    }

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:309:4: LPAREN INSERT identifier INTO memberSelector RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule2722); if (failed) return value;
                    match(input,INSERT,FOLLOW_INSERT_in_changeRule2726); if (failed) return value;
                    pushFollow(FOLLOW_identifier_in_changeRule2730);
                    identifier();
                    _fsp--;
                    if (failed) return value;
                    match(input,INTO,FOLLOW_INTO_in_changeRule2734); if (failed) return value;
                    pushFollow(FOLLOW_memberSelector_in_changeRule2738);
                    memberSelector();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule2742); if (failed) return value;
                    pushFollow(FOLLOW_block_in_changeRule2744);
                    block();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:310:4: LPAREN DELETE identifier FROM memberSelector RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule2751); if (failed) return value;
                    match(input,DELETE,FOLLOW_DELETE_in_changeRule2755); if (failed) return value;
                    pushFollow(FOLLOW_identifier_in_changeRule2759);
                    identifier();
                    _fsp--;
                    if (failed) return value;
                    match(input,FROM,FOLLOW_FROM_in_changeRule2763); if (failed) return value;
                    pushFollow(FOLLOW_memberSelector_in_changeRule2767);
                    memberSelector();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule2771); if (failed) return value;
                    pushFollow(FOLLOW_block_in_changeRule2773);
                    block();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:311:4: LPAREN DELETE memberSelector LBRACKET identifier RBRACKET RPAREN block
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_changeRule2780); if (failed) return value;
                    match(input,DELETE,FOLLOW_DELETE_in_changeRule2784); if (failed) return value;
                    pushFollow(FOLLOW_memberSelector_in_changeRule2787);
                    memberSelector();
                    _fsp--;
                    if (failed) return value;
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_changeRule2791); if (failed) return value;
                    pushFollow(FOLLOW_identifier_in_changeRule2795);
                    identifier();
                    _fsp--;
                    if (failed) return value;
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_changeRule2799); if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_changeRule2803); if (failed) return value;
                    pushFollow(FOLLOW_block_in_changeRule2805);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:313:1: modifierFlags returns [JCModifiers mods] : (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? ;
    public final JCModifiers modifierFlags() throws RecognitionException {
        JCModifiers mods = null;

        long om1 = 0;

        long am1 = 0;

        long am2 = 0;

        long om2 = 0;


         long flags = 0; 
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:315:2: ( (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:315:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
            {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:315:4: (om1= otherModifier (am1= accessModifier )? | am2= accessModifier (om2= otherModifier )? )?
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
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:315:6: om1= otherModifier (am1= accessModifier )?
                    {
                    pushFollow(FOLLOW_otherModifier_in_modifierFlags2828);
                    om1=otherModifier();
                    _fsp--;
                    if (failed) return mods;
                    if ( backtracking==0 ) {
                       flags |= om1; 
                    }
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:316:3: (am1= accessModifier )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( ((LA18_0>=PRIVATE && LA18_0<=PUBLIC)) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:316:5: am1= accessModifier
                            {
                            pushFollow(FOLLOW_accessModifier_in_modifierFlags2841);
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
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:317:6: am2= accessModifier (om2= otherModifier )?
                    {
                    pushFollow(FOLLOW_accessModifier_in_modifierFlags2863);
                    am2=accessModifier();
                    _fsp--;
                    if (failed) return mods;
                    if ( backtracking==0 ) {
                       flags |= am2; 
                    }
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:318:3: (om2= otherModifier )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==ABSTRACT||LA19_0==READONLY) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:318:5: om2= otherModifier
                            {
                            pushFollow(FOLLOW_otherModifier_in_modifierFlags2876);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:321:1: accessModifier returns [long flags = 0] : ( PUBLIC | PRIVATE | PROTECTED ) ;
    public final long accessModifier() throws RecognitionException {
        long flags =  0;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:322:2: ( ( PUBLIC | PRIVATE | PROTECTED ) )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:322:4: ( PUBLIC | PRIVATE | PROTECTED )
            {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:322:4: ( PUBLIC | PRIVATE | PROTECTED )
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
                    new NoViableAltException("322:4: ( PUBLIC | PRIVATE | PROTECTED )", 21, 0, input);

                throw nvae;
            }

            switch (alt21) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:322:5: PUBLIC
                    {
                    match(input,PUBLIC,FOLLOW_PUBLIC_in_accessModifier2924); if (failed) return flags;
                    if ( backtracking==0 ) {
                       flags |= Flags.PUBLIC; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:323:5: PRIVATE
                    {
                    match(input,PRIVATE,FOLLOW_PRIVATE_in_accessModifier2941); if (failed) return flags;
                    if ( backtracking==0 ) {
                       flags |= Flags.PUBLIC; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:324:5: PROTECTED
                    {
                    match(input,PROTECTED,FOLLOW_PROTECTED_in_accessModifier2957); if (failed) return flags;
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:325:1: otherModifier returns [long flags = 0] : ( ABSTRACT | READONLY ) ;
    public final long otherModifier() throws RecognitionException {
        long flags =  0;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:326:2: ( ( ABSTRACT | READONLY ) )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:326:4: ( ABSTRACT | READONLY )
            {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:326:4: ( ABSTRACT | READONLY )
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
                    new NoViableAltException("326:4: ( ABSTRACT | READONLY )", 22, 0, input);

                throw nvae;
            }
            switch (alt22) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:326:5: ABSTRACT
                    {
                    match(input,ABSTRACT,FOLLOW_ABSTRACT_in_otherModifier2981); if (failed) return flags;
                    if ( backtracking==0 ) {
                       flags |= Flags.ABSTRACT; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:327:5: READONLY
                    {
                    match(input,READONLY,FOLLOW_READONLY_in_otherModifier2996); if (failed) return flags;
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:328:1: memberSelector returns [JFXMemberSelector value] : name1= name DOT name2= name ;
    public final JFXMemberSelector memberSelector() throws RecognitionException {
        JFXMemberSelector value = null;

        Name name1 = null;

        Name name2 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:329:2: (name1= name DOT name2= name )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:329:4: name1= name DOT name2= name
            {
            pushFollow(FOLLOW_name_in_memberSelector3022);
            name1=name();
            _fsp--;
            if (failed) return value;
            match(input,DOT,FOLLOW_DOT_in_memberSelector3026); if (failed) return value;
            pushFollow(FOLLOW_name_in_memberSelector3032);
            name2=name();
            _fsp--;
            if (failed) return value;
            if ( backtracking==0 ) {
               value = F.MemberSelector(name1, name2); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:330:1: formalParameters returns [ListBuffer<JCTree> params = new ListBuffer<JCTree>()] : LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN ;
    public final ListBuffer<JCTree> formalParameters() throws RecognitionException {
        ListBuffer<JCTree> params =  new ListBuffer<JCTree>();

        JFXVar fp0 = null;

        JFXVar fpn = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:331:2: ( LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:331:4: LPAREN (fp0= formalParameter ( COMMA fpn= formalParameter )* )? RPAREN
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_formalParameters3048); if (failed) return params;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:331:13: (fp0= formalParameter ( COMMA fpn= formalParameter )* )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==QUOTED_IDENTIFIER||LA24_0==IDENTIFIER) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:331:15: fp0= formalParameter ( COMMA fpn= formalParameter )*
                    {
                    pushFollow(FOLLOW_formalParameter_in_formalParameters3056);
                    fp0=formalParameter();
                    _fsp--;
                    if (failed) return params;
                    if ( backtracking==0 ) {
                       params.append(fp0); 
                    }
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:332:13: ( COMMA fpn= formalParameter )*
                    loop23:
                    do {
                        int alt23=2;
                        int LA23_0 = input.LA(1);

                        if ( (LA23_0==COMMA) ) {
                            alt23=1;
                        }


                        switch (alt23) {
                    	case 1 :
                    	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:332:15: COMMA fpn= formalParameter
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_formalParameters3075); if (failed) return params;
                    	    pushFollow(FOLLOW_formalParameter_in_formalParameters3081);
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

            match(input,RPAREN,FOLLOW_RPAREN_in_formalParameters3092); if (failed) return params;

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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:333:1: formalParameter returns [JFXVar var] : name typeReference ;
    public final JFXVar formalParameter() throws RecognitionException {
        JFXVar var = null;

        Name name63 = null;

        JFXType typeReference64 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:334:2: ( name typeReference )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:334:4: name typeReference
            {
            pushFollow(FOLLOW_name_in_formalParameter3105);
            name63=name();
            _fsp--;
            if (failed) return var;
            pushFollow(FOLLOW_typeReference_in_formalParameter3107);
            typeReference64=typeReference();
            _fsp--;
            if (failed) return var;
            if ( backtracking==0 ) {
               var = F.Var(name63, typeReference64); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:335:1: block returns [JCBlock value] : LBRACE statements RBRACE ;
    public final JCBlock block() throws RecognitionException {
        JCBlock value = null;

        Token LBRACE65=null;
        ListBuffer<JCStatement> statements66 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:336:2: ( LBRACE statements RBRACE )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:336:4: LBRACE statements RBRACE
            {
            LBRACE65=(Token)input.LT(1);
            match(input,LBRACE,FOLLOW_LBRACE_in_block3124); if (failed) return value;
            pushFollow(FOLLOW_statements_in_block3128);
            statements66=statements();
            _fsp--;
            if (failed) return value;
            match(input,RBRACE,FOLLOW_RBRACE_in_block3132); if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(LBRACE65.getTokenIndex()).Block(0L, statements66.toList()); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:338:1: statements returns [ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>()] : ( statement )* ;
    public final ListBuffer<JCStatement> statements() throws RecognitionException {
        ListBuffer<JCStatement> stats =  new ListBuffer<JCStatement>();

        JCStatement statement67 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:339:2: ( ( statement )* )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:339:4: ( statement )*
            {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:339:4: ( statement )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( ((LA25_0>=POUND && LA25_0<=TYPEOF)||LA25_0==BREAK||LA25_0==DELETE||LA25_0==DO||(LA25_0>=RETURN && LA25_0<=VAR)||LA25_0==TRIGGER||LA25_0==INSERT||LA25_0==IF||(LA25_0>=THIS && LA25_0<=UNITINTERVAL)||(LA25_0>=WHILE && LA25_0<=CONTINUE)||LA25_0==TRY||LA25_0==FOREACH||(LA25_0>=NOT && LA25_0<=NEW)||(LA25_0>=OPERATION && LA25_0<=FUNCTION)||(LA25_0>=INDEXOF && LA25_0<=SUPER)||(LA25_0>=SIZEOF && LA25_0<=REVERSE)||LA25_0==LPAREN||LA25_0==LBRACKET||LA25_0==DOT||(LA25_0>=PLUSPLUS && LA25_0<=SUBSUB)||(LA25_0>=QUES && LA25_0<=QUOTE_LBRACE_STRING_LITERAL)||(LA25_0>=QUOTED_IDENTIFIER && LA25_0<=INTEGER_LITERAL)||LA25_0==FLOATING_POINT_LITERAL||LA25_0==IDENTIFIER) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:339:5: statement
            	    {
            	    pushFollow(FOLLOW_statement_in_statements3150);
            	    statement67=statement();
            	    _fsp--;
            	    if (failed) return stats;
            	    if ( backtracking==0 ) {
            	       stats.append(statement67); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:340:1: statement returns [JCStatement value] : ( statementExcept | localTriggerStatement );
    public final JCStatement statement() throws RecognitionException {
        JCStatement value = null;

        JCStatement statementExcept68 = null;

        JCStatement localTriggerStatement69 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:341:8: ( statementExcept | localTriggerStatement )
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( ((LA26_0>=POUND && LA26_0<=TYPEOF)||LA26_0==BREAK||LA26_0==DELETE||LA26_0==DO||(LA26_0>=RETURN && LA26_0<=VAR)||LA26_0==INSERT||LA26_0==IF||(LA26_0>=THIS && LA26_0<=UNITINTERVAL)||(LA26_0>=WHILE && LA26_0<=CONTINUE)||LA26_0==TRY||LA26_0==FOREACH||(LA26_0>=NOT && LA26_0<=NEW)||(LA26_0>=OPERATION && LA26_0<=FUNCTION)||(LA26_0>=INDEXOF && LA26_0<=SUPER)||(LA26_0>=SIZEOF && LA26_0<=REVERSE)||LA26_0==LPAREN||LA26_0==LBRACKET||LA26_0==DOT||(LA26_0>=PLUSPLUS && LA26_0<=SUBSUB)||(LA26_0>=QUES && LA26_0<=QUOTE_LBRACE_STRING_LITERAL)||(LA26_0>=QUOTED_IDENTIFIER && LA26_0<=INTEGER_LITERAL)||LA26_0==FLOATING_POINT_LITERAL||LA26_0==IDENTIFIER) ) {
                alt26=1;
            }
            else if ( (LA26_0==TRIGGER) ) {
                alt26=2;
            }
            else {
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("340:1: statement returns [JCStatement value] : ( statementExcept | localTriggerStatement );", 26, 0, input);

                throw nvae;
            }
            switch (alt26) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:341:10: statementExcept
                    {
                    pushFollow(FOLLOW_statementExcept_in_statement3201);
                    statementExcept68=statementExcept();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = statementExcept68; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:342:10: localTriggerStatement
                    {
                    pushFollow(FOLLOW_localTriggerStatement_in_statement3217);
                    localTriggerStatement69=localTriggerStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = localTriggerStatement69; 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:343:1: statementExcept returns [JCStatement value] : ( variableDeclaration | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );
    public final JCStatement statementExcept() throws RecognitionException {
        JCStatement value = null;

        Token WHILE71=null;
        Token BREAK78=null;
        Token CONTINUE79=null;
        JCStatement variableDeclaration70 = null;

        JCExpression expression72 = null;

        JCBlock block73 = null;

        JCStatement ifStatement74 = null;

        JCStatement insertStatement75 = null;

        JCStatement deleteStatement76 = null;

        JCExpression expression77 = null;

        JCStatement throwStatement80 = null;

        JCStatement returnStatement81 = null;

        JCStatement forAlphaStatement82 = null;

        JCStatement forJoinStatement83 = null;

        JCStatement tryStatement84 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:344:2: ( variableDeclaration | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement )
            int alt27=15;
            switch ( input.LA(1) ) {
            case VAR:
                {
                alt27=1;
                }
                break;
            case DO:
                {
                int LA27_2 = input.LA(2);

                if ( (synpred45()) ) {
                    alt27=2;
                }
                else if ( (synpred46()) ) {
                    alt27=3;
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("343:1: statementExcept returns [JCStatement value] : ( variableDeclaration | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 2, input);

                    throw nvae;
                }
                }
                break;
            case WHILE:
                {
                alt27=4;
                }
                break;
            case IF:
                {
                int LA27_4 = input.LA(2);

                if ( (synpred48()) ) {
                    alt27=5;
                }
                else if ( (synpred51()) ) {
                    alt27=8;
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("343:1: statementExcept returns [JCStatement value] : ( variableDeclaration | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 4, input);

                    throw nvae;
                }
                }
                break;
            case INSERT:
                {
                alt27=6;
                }
                break;
            case DELETE:
                {
                alt27=7;
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
            case OPERATION:
            case FUNCTION:
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
                alt27=8;
                }
                break;
            case BREAK:
                {
                alt27=9;
                }
                break;
            case CONTINUE:
                {
                alt27=10;
                }
                break;
            case THROW:
                {
                alt27=11;
                }
                break;
            case RETURN:
                {
                alt27=12;
                }
                break;
            case FOR:
                {
                int LA27_40 = input.LA(2);

                if ( (synpred56()) ) {
                    alt27=13;
                }
                else if ( (synpred57()) ) {
                    alt27=14;
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("343:1: statementExcept returns [JCStatement value] : ( variableDeclaration | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 40, input);

                    throw nvae;
                }
                }
                break;
            case TRY:
                {
                alt27=15;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("343:1: statementExcept returns [JCStatement value] : ( variableDeclaration | backgroundStatement | laterStatement | WHILE LPAREN expression RPAREN block | ifStatement | insertStatement | deleteStatement | expression SEMI | BREAK SEMI | CONTINUE SEMI | throwStatement | returnStatement | forAlphaStatement | forJoinStatement | tryStatement );", 27, 0, input);

                throw nvae;
            }

            switch (alt27) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:344:4: variableDeclaration
                    {
                    pushFollow(FOLLOW_variableDeclaration_in_statementExcept3235);
                    variableDeclaration70=variableDeclaration();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = variableDeclaration70; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:345:10: backgroundStatement
                    {
                    pushFollow(FOLLOW_backgroundStatement_in_statementExcept3251);
                    backgroundStatement();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:346:10: laterStatement
                    {
                    pushFollow(FOLLOW_laterStatement_in_statementExcept3266);
                    laterStatement();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:347:10: WHILE LPAREN expression RPAREN block
                    {
                    WHILE71=(Token)input.LT(1);
                    match(input,WHILE,FOLLOW_WHILE_in_statementExcept3281); if (failed) return value;
                    match(input,LPAREN,FOLLOW_LPAREN_in_statementExcept3283); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_statementExcept3285);
                    expression72=expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_statementExcept3287); if (failed) return value;
                    pushFollow(FOLLOW_block_in_statementExcept3289);
                    block73=block();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(WHILE71.getTokenIndex()).WhileLoop(expression72, block73); 
                    }

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:348:10: ifStatement
                    {
                    pushFollow(FOLLOW_ifStatement_in_statementExcept3302);
                    ifStatement74=ifStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = ifStatement74; 
                    }

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:349:10: insertStatement
                    {
                    pushFollow(FOLLOW_insertStatement_in_statementExcept3319);
                    insertStatement75=insertStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = insertStatement75; 
                    }

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:350:10: deleteStatement
                    {
                    pushFollow(FOLLOW_deleteStatement_in_statementExcept3335);
                    deleteStatement76=deleteStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = deleteStatement76; 
                    }

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:351:4: expression SEMI
                    {
                    pushFollow(FOLLOW_expression_in_statementExcept3345);
                    expression77=expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_statementExcept3349); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.Exec(expression77); 
                    }

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:352:4: BREAK SEMI
                    {
                    BREAK78=(Token)input.LT(1);
                    match(input,BREAK,FOLLOW_BREAK_in_statementExcept3359); if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_statementExcept3363); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(BREAK78.getTokenIndex()).Break(null); 
                    }

                    }
                    break;
                case 10 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:353:4: CONTINUE SEMI
                    {
                    CONTINUE79=(Token)input.LT(1);
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_statementExcept3374); if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_statementExcept3378); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(CONTINUE79.getTokenIndex()).Continue(null); 
                    }

                    }
                    break;
                case 11 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:354:10: throwStatement
                    {
                    pushFollow(FOLLOW_throwStatement_in_statementExcept3394);
                    throwStatement80=throwStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = throwStatement80; 
                    }

                    }
                    break;
                case 12 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:355:10: returnStatement
                    {
                    pushFollow(FOLLOW_returnStatement_in_statementExcept3410);
                    returnStatement81=returnStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = returnStatement81; 
                    }

                    }
                    break;
                case 13 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:356:10: forAlphaStatement
                    {
                    pushFollow(FOLLOW_forAlphaStatement_in_statementExcept3426);
                    forAlphaStatement82=forAlphaStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = forAlphaStatement82; 
                    }

                    }
                    break;
                case 14 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:357:10: forJoinStatement
                    {
                    pushFollow(FOLLOW_forJoinStatement_in_statementExcept3442);
                    forJoinStatement83=forJoinStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = forJoinStatement83; 
                    }

                    }
                    break;
                case 15 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:358:10: tryStatement
                    {
                    pushFollow(FOLLOW_tryStatement_in_statementExcept3458);
                    tryStatement84=tryStatement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       value = tryStatement84; 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:359:1: assertStatement returns [JCStatement value = null] : ASSERT expression ( COLON expression )? SEMI ;
    public final JCStatement assertStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:360:2: ( ASSERT expression ( COLON expression )? SEMI )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:360:4: ASSERT expression ( COLON expression )? SEMI
            {
            match(input,ASSERT,FOLLOW_ASSERT_in_assertStatement3477); if (failed) return value;
            pushFollow(FOLLOW_expression_in_assertStatement3481);
            expression();
            _fsp--;
            if (failed) return value;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:360:26: ( COLON expression )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==COLON) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:360:30: COLON expression
                    {
                    match(input,COLON,FOLLOW_COLON_in_assertStatement3489); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_assertStatement3493);
                    expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_assertStatement3503); if (failed) return value;

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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:361:1: localOperationDefinition returns [JCStatement value] : OPERATION name formalParameters typeReference block ;
    public final JCStatement localOperationDefinition() throws RecognitionException {
        JCStatement value = null;

        Token OPERATION85=null;
        Name name86 = null;

        JFXType typeReference87 = null;

        ListBuffer<JCTree> formalParameters88 = null;

        JCBlock block89 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:362:2: ( OPERATION name formalParameters typeReference block )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:362:4: OPERATION name formalParameters typeReference block
            {
            OPERATION85=(Token)input.LT(1);
            match(input,OPERATION,FOLLOW_OPERATION_in_localOperationDefinition3518); if (failed) return value;
            pushFollow(FOLLOW_name_in_localOperationDefinition3522);
            name86=name();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_formalParameters_in_localOperationDefinition3526);
            formalParameters88=formalParameters();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_typeReference_in_localOperationDefinition3530);
            typeReference87=typeReference();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_block_in_localOperationDefinition3533);
            block89=block();
            _fsp--;
            if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(OPERATION85.getTokenIndex()).OperationLocalDefinition(name86, typeReference87, 
              									formalParameters88.toList(), block89); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:365:1: localFunctionDefinition returns [JCStatement value] : ( FUNCTION )? name formalParameters typeReference block ;
    public final JCStatement localFunctionDefinition() throws RecognitionException {
        JCStatement value = null;

        Name name90 = null;

        JFXType typeReference91 = null;

        ListBuffer<JCTree> formalParameters92 = null;

        JCBlock block93 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:366:2: ( ( FUNCTION )? name formalParameters typeReference block )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:366:4: ( FUNCTION )? name formalParameters typeReference block
            {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:366:4: ( FUNCTION )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==FUNCTION) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: FUNCTION
                    {
                    match(input,FUNCTION,FOLLOW_FUNCTION_in_localFunctionDefinition3553); if (failed) return value;

                    }
                    break;

            }

            pushFollow(FOLLOW_name_in_localFunctionDefinition3559);
            name90=name();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_formalParameters_in_localFunctionDefinition3563);
            formalParameters92=formalParameters();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_typeReference_in_localFunctionDefinition3567);
            typeReference91=typeReference();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_block_in_localFunctionDefinition3570);
            block93=block();
            _fsp--;
            if (failed) return value;
            if ( backtracking==0 ) {
               value = F.FunctionLocalDefinition(name90, typeReference91, 
              									formalParameters92.toList(), block93); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:369:1: variableDeclaration returns [JCStatement value] : VAR name typeReference ( EQ bindOpt expression SEMI | SEMI ) ;
    public final JCStatement variableDeclaration() throws RecognitionException {
        JCStatement value = null;

        Token VAR94=null;
        Name name95 = null;

        JFXType typeReference96 = null;

        JCExpression expression97 = null;

        JavafxBindStatus bindOpt98 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:370:2: ( VAR name typeReference ( EQ bindOpt expression SEMI | SEMI ) )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:370:4: VAR name typeReference ( EQ bindOpt expression SEMI | SEMI )
            {
            VAR94=(Token)input.LT(1);
            match(input,VAR,FOLLOW_VAR_in_variableDeclaration3590); if (failed) return value;
            pushFollow(FOLLOW_name_in_variableDeclaration3593);
            name95=name();
            _fsp--;
            if (failed) return value;
            pushFollow(FOLLOW_typeReference_in_variableDeclaration3596);
            typeReference96=typeReference();
            _fsp--;
            if (failed) return value;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:371:6: ( EQ bindOpt expression SEMI | SEMI )
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
                    new NoViableAltException("371:6: ( EQ bindOpt expression SEMI | SEMI )", 30, 0, input);

                throw nvae;
            }
            switch (alt30) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:371:8: EQ bindOpt expression SEMI
                    {
                    match(input,EQ,FOLLOW_EQ_in_variableDeclaration3607); if (failed) return value;
                    pushFollow(FOLLOW_bindOpt_in_variableDeclaration3609);
                    bindOpt98=bindOpt();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_expression_in_variableDeclaration3612);
                    expression97=expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_variableDeclaration3614); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(VAR94.getTokenIndex()).VarInit(name95, typeReference96, 
                      	    							expression97, bindOpt98); 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:373:8: SEMI
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_variableDeclaration3625); if (failed) return value;
                    if ( backtracking==0 ) {
                       value = F.at(VAR94.getTokenIndex()).VarStatement(name95, typeReference96); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:376:1: bindOpt returns [JavafxBindStatus status = UNBOUND] : ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? ;
    public final JavafxBindStatus bindOpt() throws RecognitionException {
        JavafxBindStatus status =  UNBOUND;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:377:2: ( ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:377:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
            {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:377:4: ( BIND ( LAZY )? | STAYS ( LAZY )? | TIE ( LAZY )? )?
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
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:377:6: BIND ( LAZY )?
                    {
                    match(input,BIND,FOLLOW_BIND_in_bindOpt3662); if (failed) return status;
                    if ( backtracking==0 ) {
                       status = UNIDIBIND; 
                    }
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:378:8: ( LAZY )?
                    int alt31=2;
                    int LA31_0 = input.LA(1);

                    if ( (LA31_0==LAZY) ) {
                        alt31=1;
                    }
                    switch (alt31) {
                        case 1 :
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:378:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3678); if (failed) return status;
                            if ( backtracking==0 ) {
                               status = LAZY_UNIDIBIND; 
                            }

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:379:6: STAYS ( LAZY )?
                    {
                    match(input,STAYS,FOLLOW_STAYS_in_bindOpt3693); if (failed) return status;
                    if ( backtracking==0 ) {
                       status = UNIDIBIND; 
                    }
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:380:8: ( LAZY )?
                    int alt32=2;
                    int LA32_0 = input.LA(1);

                    if ( (LA32_0==LAZY) ) {
                        alt32=1;
                    }
                    switch (alt32) {
                        case 1 :
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:380:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3709); if (failed) return status;
                            if ( backtracking==0 ) {
                               status = LAZY_UNIDIBIND; 
                            }

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:381:6: TIE ( LAZY )?
                    {
                    match(input,TIE,FOLLOW_TIE_in_bindOpt3724); if (failed) return status;
                    if ( backtracking==0 ) {
                       status = BIDIBIND; 
                    }
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:382:8: ( LAZY )?
                    int alt33=2;
                    int LA33_0 = input.LA(1);

                    if ( (LA33_0==LAZY) ) {
                        alt33=1;
                    }
                    switch (alt33) {
                        case 1 :
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:382:9: LAZY
                            {
                            match(input,LAZY,FOLLOW_LAZY_in_bindOpt3740); if (failed) return status;
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:384:1: backgroundStatement : DO block ;
    public final void backgroundStatement() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:385:2: ( DO block )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:385:4: DO block
            {
            match(input,DO,FOLLOW_DO_in_backgroundStatement3766); if (failed) return ;
            pushFollow(FOLLOW_block_in_backgroundStatement3770);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:386:1: laterStatement : DO LATER block ;
    public final void laterStatement() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:387:2: ( DO LATER block )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:387:4: DO LATER block
            {
            match(input,DO,FOLLOW_DO_in_laterStatement3786); if (failed) return ;
            match(input,LATER,FOLLOW_LATER_in_laterStatement3790); if (failed) return ;
            pushFollow(FOLLOW_block_in_laterStatement3794);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:388:1: ifStatement returns [JCStatement value] : IF LPAREN expression RPAREN s1= statement ( ELSE s2= statement )? ;
    public final JCStatement ifStatement() throws RecognitionException {
        JCStatement value = null;

        Token IF99=null;
        JCStatement s1 = null;

        JCStatement s2 = null;

        JCExpression expression100 = null;


         JCStatement elsepart = null; 
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:390:2: ( IF LPAREN expression RPAREN s1= statement ( ELSE s2= statement )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:390:4: IF LPAREN expression RPAREN s1= statement ( ELSE s2= statement )?
            {
            IF99=(Token)input.LT(1);
            match(input,IF,FOLLOW_IF_in_ifStatement3814); if (failed) return value;
            match(input,LPAREN,FOLLOW_LPAREN_in_ifStatement3818); if (failed) return value;
            pushFollow(FOLLOW_expression_in_ifStatement3822);
            expression100=expression();
            _fsp--;
            if (failed) return value;
            match(input,RPAREN,FOLLOW_RPAREN_in_ifStatement3826); if (failed) return value;
            pushFollow(FOLLOW_statement_in_ifStatement3832);
            s1=statement();
            _fsp--;
            if (failed) return value;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:390:53: ( ELSE s2= statement )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==ELSE) ) {
                int LA35_1 = input.LA(2);

                if ( (synpred67()) ) {
                    alt35=1;
                }
            }
            switch (alt35) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:390:54: ELSE s2= statement
                    {
                    match(input,ELSE,FOLLOW_ELSE_in_ifStatement3835); if (failed) return value;
                    pushFollow(FOLLOW_statement_in_ifStatement3840);
                    s2=statement();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       elsepart = s2; 
                    }

                    }
                    break;

            }

            if ( backtracking==0 ) {
               value = F.at(IF99.getTokenIndex()).If(expression100, s1, elsepart); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:392:1: insertStatement returns [JCStatement value = null] : INSERT ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) ) SEMI ;
    public final JCStatement insertStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:393:2: ( INSERT ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) ) SEMI )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:393:4: INSERT ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) ) SEMI
            {
            match(input,INSERT,FOLLOW_INSERT_in_insertStatement3869); if (failed) return value;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:393:13: ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) )
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==DISTINCT) ) {
                alt38=1;
            }
            else if ( ((LA38_0>=POUND && LA38_0<=TYPEOF)||LA38_0==IF||(LA38_0>=THIS && LA38_0<=FALSE)||LA38_0==UNITINTERVAL||LA38_0==FOREACH||(LA38_0>=NOT && LA38_0<=NEW)||(LA38_0>=OPERATION && LA38_0<=FUNCTION)||(LA38_0>=INDEXOF && LA38_0<=SUPER)||(LA38_0>=SIZEOF && LA38_0<=REVERSE)||LA38_0==LPAREN||LA38_0==LBRACKET||LA38_0==DOT||(LA38_0>=PLUSPLUS && LA38_0<=SUBSUB)||(LA38_0>=QUES && LA38_0<=QUOTE_LBRACE_STRING_LITERAL)||(LA38_0>=QUOTED_IDENTIFIER && LA38_0<=INTEGER_LITERAL)||LA38_0==FLOATING_POINT_LITERAL||LA38_0==IDENTIFIER) ) {
                alt38=2;
            }
            else {
                if (backtracking>0) {failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("393:13: ( DISTINCT expression INTO expression | expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression ) )", 38, 0, input);

                throw nvae;
            }
            switch (alt38) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:393:17: DISTINCT expression INTO expression
                    {
                    match(input,DISTINCT,FOLLOW_DISTINCT_in_insertStatement3877); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_insertStatement3881);
                    expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,INTO,FOLLOW_INTO_in_insertStatement3885); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_insertStatement3889);
                    expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:393:65: expression ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression )
                    {
                    pushFollow(FOLLOW_expression_in_insertStatement3897);
                    expression();
                    _fsp--;
                    if (failed) return value;
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:393:78: ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression )
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
                            new NoViableAltException("393:78: ( ( ( AS ( FIRST | LAST ) )? INTO expression ) | AFTER expression | BEFORE expression )", 37, 0, input);

                        throw nvae;
                    }

                    switch (alt37) {
                        case 1 :
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:393:82: ( ( AS ( FIRST | LAST ) )? INTO expression )
                            {
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:393:82: ( ( AS ( FIRST | LAST ) )? INTO expression )
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:393:86: ( AS ( FIRST | LAST ) )? INTO expression
                            {
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:393:86: ( AS ( FIRST | LAST ) )?
                            int alt36=2;
                            int LA36_0 = input.LA(1);

                            if ( (LA36_0==AS) ) {
                                alt36=1;
                            }
                            switch (alt36) {
                                case 1 :
                                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:393:90: AS ( FIRST | LAST )
                                    {
                                    match(input,AS,FOLLOW_AS_in_insertStatement3913); if (failed) return value;
                                    if ( (input.LA(1)>=FIRST && input.LA(1)<=LAST) ) {
                                        input.consume();
                                        errorRecovery=false;failed=false;
                                    }
                                    else {
                                        if (backtracking>0) {failed=true; return value;}
                                        MismatchedSetException mse =
                                            new MismatchedSetException(null,input);
                                        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_insertStatement3917);    throw mse;
                                    }


                                    }
                                    break;

                            }

                            match(input,INTO,FOLLOW_INTO_in_insertStatement3943); if (failed) return value;
                            pushFollow(FOLLOW_expression_in_insertStatement3947);
                            expression();
                            _fsp--;
                            if (failed) return value;

                            }


                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:393:156: AFTER expression
                            {
                            match(input,AFTER,FOLLOW_AFTER_in_insertStatement3959); if (failed) return value;
                            pushFollow(FOLLOW_expression_in_insertStatement3963);
                            expression();
                            _fsp--;
                            if (failed) return value;

                            }
                            break;
                        case 3 :
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:393:181: BEFORE expression
                            {
                            match(input,BEFORE,FOLLOW_BEFORE_in_insertStatement3971); if (failed) return value;
                            pushFollow(FOLLOW_expression_in_insertStatement3975);
                            expression();
                            _fsp--;
                            if (failed) return value;

                            }
                            break;

                    }


                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_insertStatement3989); if (failed) return value;

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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:394:1: deleteStatement returns [JCStatement value = null] : DELETE expression SEMI ;
    public final JCStatement deleteStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:395:2: ( DELETE expression SEMI )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:395:4: DELETE expression SEMI
            {
            match(input,DELETE,FOLLOW_DELETE_in_deleteStatement4004); if (failed) return value;
            pushFollow(FOLLOW_expression_in_deleteStatement4008);
            expression();
            _fsp--;
            if (failed) return value;
            match(input,SEMI,FOLLOW_SEMI_in_deleteStatement4012); if (failed) return value;

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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:396:1: throwStatement returns [JCStatement value = null] : THROW expression SEMI ;
    public final JCStatement throwStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:397:2: ( THROW expression SEMI )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:397:4: THROW expression SEMI
            {
            match(input,THROW,FOLLOW_THROW_in_throwStatement4027); if (failed) return value;
            pushFollow(FOLLOW_expression_in_throwStatement4031);
            expression();
            _fsp--;
            if (failed) return value;
            match(input,SEMI,FOLLOW_SEMI_in_throwStatement4035); if (failed) return value;

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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:398:1: returnStatement returns [JCStatement value] : RETURN ( expression )? SEMI ;
    public final JCStatement returnStatement() throws RecognitionException {
        JCStatement value = null;

        Token RETURN102=null;
        JCExpression expression101 = null;


         JCExpression expr = null; 
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:400:2: ( RETURN ( expression )? SEMI )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:400:4: RETURN ( expression )? SEMI
            {
            RETURN102=(Token)input.LT(1);
            match(input,RETURN,FOLLOW_RETURN_in_returnStatement4055); if (failed) return value;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:400:11: ( expression )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( ((LA39_0>=POUND && LA39_0<=TYPEOF)||LA39_0==IF||(LA39_0>=THIS && LA39_0<=FALSE)||LA39_0==UNITINTERVAL||LA39_0==FOREACH||(LA39_0>=NOT && LA39_0<=NEW)||(LA39_0>=OPERATION && LA39_0<=FUNCTION)||(LA39_0>=INDEXOF && LA39_0<=SUPER)||(LA39_0>=SIZEOF && LA39_0<=REVERSE)||LA39_0==LPAREN||LA39_0==LBRACKET||LA39_0==DOT||(LA39_0>=PLUSPLUS && LA39_0<=SUBSUB)||(LA39_0>=QUES && LA39_0<=QUOTE_LBRACE_STRING_LITERAL)||(LA39_0>=QUOTED_IDENTIFIER && LA39_0<=INTEGER_LITERAL)||LA39_0==FLOATING_POINT_LITERAL||LA39_0==IDENTIFIER) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:400:12: expression
                    {
                    pushFollow(FOLLOW_expression_in_returnStatement4058);
                    expression101=expression();
                    _fsp--;
                    if (failed) return value;
                    if ( backtracking==0 ) {
                       expr = expression101; 
                    }

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_returnStatement4065); if (failed) return value;
            if ( backtracking==0 ) {
               value = F.at(RETURN102.getTokenIndex()).Return(expr); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:403:1: localTriggerStatement returns [JCStatement value = null] : TRIGGER ON ( localTriggerCondition | LPAREN localTriggerCondition RPAREN ) block ;
    public final JCStatement localTriggerStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:404:2: ( TRIGGER ON ( localTriggerCondition | LPAREN localTriggerCondition RPAREN ) block )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:404:4: TRIGGER ON ( localTriggerCondition | LPAREN localTriggerCondition RPAREN ) block
            {
            match(input,TRIGGER,FOLLOW_TRIGGER_in_localTriggerStatement4091); if (failed) return value;
            match(input,ON,FOLLOW_ON_in_localTriggerStatement4095); if (failed) return value;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:404:20: ( localTriggerCondition | LPAREN localTriggerCondition RPAREN )
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
                    new NoViableAltException("404:20: ( localTriggerCondition | LPAREN localTriggerCondition RPAREN )", 40, 0, input);

                throw nvae;
            }
            switch (alt40) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:404:22: localTriggerCondition
                    {
                    pushFollow(FOLLOW_localTriggerCondition_in_localTriggerStatement4102);
                    localTriggerCondition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:404:46: LPAREN localTriggerCondition RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_localTriggerStatement4106); if (failed) return value;
                    pushFollow(FOLLOW_localTriggerCondition_in_localTriggerStatement4110);
                    localTriggerCondition();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_localTriggerStatement4114); if (failed) return value;

                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_localTriggerStatement4118);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:405:1: localTriggerCondition returns [JCStatement value = null] : ( name ( LBRACKET name RBRACKET )? EQ expression | INSERT name INTO ( name EQ ) expression | DELETE name FROM ( name EQ ) expression );
    public final JCStatement localTriggerCondition() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:406:2: ( name ( LBRACKET name RBRACKET )? EQ expression | INSERT name INTO ( name EQ ) expression | DELETE name FROM ( name EQ ) expression )
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
                    new NoViableAltException("405:1: localTriggerCondition returns [JCStatement value = null] : ( name ( LBRACKET name RBRACKET )? EQ expression | INSERT name INTO ( name EQ ) expression | DELETE name FROM ( name EQ ) expression );", 42, 0, input);

                throw nvae;
            }

            switch (alt42) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:406:4: name ( LBRACKET name RBRACKET )? EQ expression
                    {
                    pushFollow(FOLLOW_name_in_localTriggerCondition4133);
                    name();
                    _fsp--;
                    if (failed) return value;
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:406:11: ( LBRACKET name RBRACKET )?
                    int alt41=2;
                    int LA41_0 = input.LA(1);

                    if ( (LA41_0==LBRACKET) ) {
                        alt41=1;
                    }
                    switch (alt41) {
                        case 1 :
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:406:15: LBRACKET name RBRACKET
                            {
                            match(input,LBRACKET,FOLLOW_LBRACKET_in_localTriggerCondition4141); if (failed) return value;
                            pushFollow(FOLLOW_name_in_localTriggerCondition4145);
                            name();
                            _fsp--;
                            if (failed) return value;
                            match(input,RBRACKET,FOLLOW_RBRACKET_in_localTriggerCondition4149); if (failed) return value;

                            }
                            break;

                    }

                    match(input,EQ,FOLLOW_EQ_in_localTriggerCondition4159); if (failed) return value;
                    pushFollow(FOLLOW_expression_in_localTriggerCondition4163);
                    expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:407:10: INSERT name INTO ( name EQ ) expression
                    {
                    match(input,INSERT,FOLLOW_INSERT_in_localTriggerCondition4175); if (failed) return value;
                    pushFollow(FOLLOW_name_in_localTriggerCondition4179);
                    name();
                    _fsp--;
                    if (failed) return value;
                    match(input,INTO,FOLLOW_INTO_in_localTriggerCondition4183); if (failed) return value;
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:407:33: ( name EQ )
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:407:35: name EQ
                    {
                    pushFollow(FOLLOW_name_in_localTriggerCondition4189);
                    name();
                    _fsp--;
                    if (failed) return value;
                    match(input,EQ,FOLLOW_EQ_in_localTriggerCondition4193); if (failed) return value;

                    }

                    pushFollow(FOLLOW_expression_in_localTriggerCondition4201);
                    expression();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:408:10: DELETE name FROM ( name EQ ) expression
                    {
                    match(input,DELETE,FOLLOW_DELETE_in_localTriggerCondition4213); if (failed) return value;
                    pushFollow(FOLLOW_name_in_localTriggerCondition4217);
                    name();
                    _fsp--;
                    if (failed) return value;
                    match(input,FROM,FOLLOW_FROM_in_localTriggerCondition4221); if (failed) return value;
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:408:33: ( name EQ )
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:408:35: name EQ
                    {
                    pushFollow(FOLLOW_name_in_localTriggerCondition4227);
                    name();
                    _fsp--;
                    if (failed) return value;
                    match(input,EQ,FOLLOW_EQ_in_localTriggerCondition4231); if (failed) return value;

                    }

                    pushFollow(FOLLOW_expression_in_localTriggerCondition4239);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:409:1: forAlphaStatement returns [JCStatement value = null] : FOR LPAREN alphaExpression RPAREN block ;
    public final JCStatement forAlphaStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:410:2: ( FOR LPAREN alphaExpression RPAREN block )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:410:4: FOR LPAREN alphaExpression RPAREN block
            {
            match(input,FOR,FOLLOW_FOR_in_forAlphaStatement4254); if (failed) return value;
            match(input,LPAREN,FOLLOW_LPAREN_in_forAlphaStatement4258); if (failed) return value;
            pushFollow(FOLLOW_alphaExpression_in_forAlphaStatement4262);
            alphaExpression();
            _fsp--;
            if (failed) return value;
            match(input,RPAREN,FOLLOW_RPAREN_in_forAlphaStatement4266); if (failed) return value;
            pushFollow(FOLLOW_block_in_forAlphaStatement4270);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:1: alphaExpression : UNITINTERVAL IN DUR expression ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )? ;
    public final void alphaExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:17: ( UNITINTERVAL IN DUR expression ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:19: UNITINTERVAL IN DUR expression ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )?
            {
            match(input,UNITINTERVAL,FOLLOW_UNITINTERVAL_in_alphaExpression4278); if (failed) return ;
            match(input,IN,FOLLOW_IN_in_alphaExpression4282); if (failed) return ;
            match(input,DUR,FOLLOW_DUR_in_alphaExpression4286); if (failed) return ;
            pushFollow(FOLLOW_expression_in_alphaExpression4290);
            expression();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:58: ( FPS expression )?
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==FPS) ) {
                int LA43_1 = input.LA(2);

                if ( (synpred78()) ) {
                    alt43=1;
                }
            }
            switch (alt43) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:62: FPS expression
                    {
                    match(input,FPS,FOLLOW_FPS_in_alphaExpression4298); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_alphaExpression4302);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:87: ( WHILE expression )?
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==WHILE) ) {
                int LA44_1 = input.LA(2);

                if ( (synpred79()) ) {
                    alt44=1;
                }
            }
            switch (alt44) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:91: WHILE expression
                    {
                    match(input,WHILE,FOLLOW_WHILE_in_alphaExpression4316); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_alphaExpression4320);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:118: ( CONTINUE IF expression )?
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==CONTINUE) ) {
                int LA45_1 = input.LA(2);

                if ( (synpred80()) ) {
                    alt45=1;
                }
            }
            switch (alt45) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:122: CONTINUE IF expression
                    {
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_alphaExpression4334); if (failed) return ;
                    match(input,IF,FOLLOW_IF_in_alphaExpression4338); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_alphaExpression4342);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:412:1: forJoinStatement returns [JCStatement value = null] : FOR LPAREN joinClause RPAREN ( LPAREN durClause RPAREN )? block ;
    public final JCStatement forJoinStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:413:2: ( FOR LPAREN joinClause RPAREN ( LPAREN durClause RPAREN )? block )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:413:4: FOR LPAREN joinClause RPAREN ( LPAREN durClause RPAREN )? block
            {
            match(input,FOR,FOLLOW_FOR_in_forJoinStatement4363); if (failed) return value;
            match(input,LPAREN,FOLLOW_LPAREN_in_forJoinStatement4367); if (failed) return value;
            pushFollow(FOLLOW_joinClause_in_forJoinStatement4371);
            joinClause();
            _fsp--;
            if (failed) return value;
            match(input,RPAREN,FOLLOW_RPAREN_in_forJoinStatement4375); if (failed) return value;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:413:41: ( LPAREN durClause RPAREN )?
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==LPAREN) ) {
                alt46=1;
            }
            switch (alt46) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:413:45: LPAREN durClause RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_forJoinStatement4383); if (failed) return value;
                    pushFollow(FOLLOW_durClause_in_forJoinStatement4387);
                    durClause();
                    _fsp--;
                    if (failed) return value;
                    match(input,RPAREN,FOLLOW_RPAREN_in_forJoinStatement4391); if (failed) return value;

                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_forJoinStatement4401);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:414:1: joinClause : name IN expression ( COMMA name IN expression )* ( WHERE expression )? ;
    public final void joinClause() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:414:12: ( name IN expression ( COMMA name IN expression )* ( WHERE expression )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:414:14: name IN expression ( COMMA name IN expression )* ( WHERE expression )?
            {
            pushFollow(FOLLOW_name_in_joinClause4409);
            name();
            _fsp--;
            if (failed) return ;
            match(input,IN,FOLLOW_IN_in_joinClause4413); if (failed) return ;
            pushFollow(FOLLOW_expression_in_joinClause4417);
            expression();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:414:39: ( COMMA name IN expression )*
            loop47:
            do {
                int alt47=2;
                int LA47_0 = input.LA(1);

                if ( (LA47_0==COMMA) ) {
                    alt47=1;
                }


                switch (alt47) {
            	case 1 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:414:43: COMMA name IN expression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_joinClause4425); if (failed) return ;
            	    pushFollow(FOLLOW_name_in_joinClause4429);
            	    name();
            	    _fsp--;
            	    if (failed) return ;
            	    match(input,IN,FOLLOW_IN_in_joinClause4433); if (failed) return ;
            	    pushFollow(FOLLOW_expression_in_joinClause4437);
            	    expression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop47;
                }
            } while (true);

            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:414:82: ( WHERE expression )?
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==WHERE) ) {
                alt48=1;
            }
            switch (alt48) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:414:86: WHERE expression
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_joinClause4451); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_joinClause4455);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:1: durClause : DUR expression ( LINEAR | EASEIN | EASEOUT | EASEBOTH | MOTION expression )? ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )? ;
    public final void durClause() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:11: ( DUR expression ( LINEAR | EASEIN | EASEOUT | EASEBOTH | MOTION expression )? ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:13: DUR expression ( LINEAR | EASEIN | EASEOUT | EASEBOTH | MOTION expression )? ( FPS expression )? ( WHILE expression )? ( CONTINUE IF expression )?
            {
            match(input,DUR,FOLLOW_DUR_in_durClause4469); if (failed) return ;
            pushFollow(FOLLOW_expression_in_durClause4473);
            expression();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:32: ( LINEAR | EASEIN | EASEOUT | EASEBOTH | MOTION expression )?
            int alt49=6;
            switch ( input.LA(1) ) {
                case LINEAR:
                    {
                    int LA49_1 = input.LA(2);

                    if ( (synpred84()) ) {
                        alt49=1;
                    }
                    }
                    break;
                case EASEIN:
                    {
                    int LA49_2 = input.LA(2);

                    if ( (synpred85()) ) {
                        alt49=2;
                    }
                    }
                    break;
                case EASEOUT:
                    {
                    int LA49_3 = input.LA(2);

                    if ( (synpred86()) ) {
                        alt49=3;
                    }
                    }
                    break;
                case EASEBOTH:
                    {
                    int LA49_4 = input.LA(2);

                    if ( (synpred87()) ) {
                        alt49=4;
                    }
                    }
                    break;
                case MOTION:
                    {
                    int LA49_5 = input.LA(2);

                    if ( (synpred88()) ) {
                        alt49=5;
                    }
                    }
                    break;
            }

            switch (alt49) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:36: LINEAR
                    {
                    match(input,LINEAR,FOLLOW_LINEAR_in_durClause4481); if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:49: EASEIN
                    {
                    match(input,EASEIN,FOLLOW_EASEIN_in_durClause4489); if (failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:62: EASEOUT
                    {
                    match(input,EASEOUT,FOLLOW_EASEOUT_in_durClause4497); if (failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:76: EASEBOTH
                    {
                    match(input,EASEBOTH,FOLLOW_EASEBOTH_in_durClause4505); if (failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:91: MOTION expression
                    {
                    match(input,MOTION,FOLLOW_MOTION_in_durClause4513); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_durClause4517);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:119: ( FPS expression )?
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==FPS) ) {
                int LA50_1 = input.LA(2);

                if ( (synpred89()) ) {
                    alt50=1;
                }
            }
            switch (alt50) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:123: FPS expression
                    {
                    match(input,FPS,FOLLOW_FPS_in_durClause4531); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_durClause4535);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:146: ( WHILE expression )?
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==WHILE) ) {
                int LA51_1 = input.LA(2);

                if ( (synpred90()) ) {
                    alt51=1;
                }
            }
            switch (alt51) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:150: WHILE expression
                    {
                    match(input,WHILE,FOLLOW_WHILE_in_durClause4547); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_durClause4551);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:177: ( CONTINUE IF expression )?
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( (LA52_0==CONTINUE) ) {
                int LA52_1 = input.LA(2);

                if ( (synpred91()) ) {
                    alt52=1;
                }
            }
            switch (alt52) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:181: CONTINUE IF expression
                    {
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_durClause4565); if (failed) return ;
                    match(input,IF,FOLLOW_IF_in_durClause4569); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_durClause4573);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:416:1: tryStatement returns [JCStatement value = null] : TRY block ( FINALLY block | ( catchClause )+ ( FINALLY block )? ) ;
    public final JCStatement tryStatement() throws RecognitionException {
        JCStatement value =  null;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:417:2: ( TRY block ( FINALLY block | ( catchClause )+ ( FINALLY block )? ) )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:417:4: TRY block ( FINALLY block | ( catchClause )+ ( FINALLY block )? )
            {
            match(input,TRY,FOLLOW_TRY_in_tryStatement4594); if (failed) return value;
            pushFollow(FOLLOW_block_in_tryStatement4598);
            block();
            _fsp--;
            if (failed) return value;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:417:18: ( FINALLY block | ( catchClause )+ ( FINALLY block )? )
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
                    new NoViableAltException("417:18: ( FINALLY block | ( catchClause )+ ( FINALLY block )? )", 55, 0, input);

                throw nvae;
            }
            switch (alt55) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:417:22: FINALLY block
                    {
                    match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement4606); if (failed) return value;
                    pushFollow(FOLLOW_block_in_tryStatement4610);
                    block();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:417:46: ( catchClause )+ ( FINALLY block )?
                    {
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:417:46: ( catchClause )+
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
                    	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: catchClause
                    	    {
                    	    pushFollow(FOLLOW_catchClause_in_tryStatement4620);
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

                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:417:62: ( FINALLY block )?
                    int alt54=2;
                    int LA54_0 = input.LA(1);

                    if ( (LA54_0==FINALLY) ) {
                        alt54=1;
                    }
                    switch (alt54) {
                        case 1 :
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:417:66: FINALLY block
                            {
                            match(input,FINALLY,FOLLOW_FINALLY_in_tryStatement4630); if (failed) return value;
                            pushFollow(FOLLOW_block_in_tryStatement4634);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:418:1: catchClause : CATCH LPAREN name ( typeReference )? ( IF expression )? RPAREN block ;
    public final void catchClause() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:418:13: ( CATCH LPAREN name ( typeReference )? ( IF expression )? RPAREN block )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:418:15: CATCH LPAREN name ( typeReference )? ( IF expression )? RPAREN block
            {
            match(input,CATCH,FOLLOW_CATCH_in_catchClause4652); if (failed) return ;
            match(input,LPAREN,FOLLOW_LPAREN_in_catchClause4656); if (failed) return ;
            pushFollow(FOLLOW_name_in_catchClause4660);
            name();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:418:39: ( typeReference )?
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

                    if ( (synpred95()) ) {
                        alt56=1;
                    }
                    }
                    break;
                case RPAREN:
                    {
                    int LA56_3 = input.LA(2);

                    if ( (synpred95()) ) {
                        alt56=1;
                    }
                    }
                    break;
            }

            switch (alt56) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: typeReference
                    {
                    pushFollow(FOLLOW_typeReference_in_catchClause4664);
                    typeReference();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:418:57: ( IF expression )?
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( (LA57_0==IF) ) {
                alt57=1;
            }
            switch (alt57) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:418:61: IF expression
                    {
                    match(input,IF,FOLLOW_IF_in_catchClause4674); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_catchClause4678);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_catchClause4688); if (failed) return ;
            pushFollow(FOLLOW_block_in_catchClause4692);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:419:1: expression returns [JCExpression expr] : ( foreach | functionExpression | operationExpression | alphaExpression | ifExpression | selectExpression | LPAREN typeName RPAREN suffixedExpression | suffixedExpression );
    public final JCExpression expression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression suffixedExpression103 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:420:2: ( foreach | functionExpression | operationExpression | alphaExpression | ifExpression | selectExpression | LPAREN typeName RPAREN suffixedExpression | suffixedExpression )
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
                alt58=7;
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
                    new NoViableAltException("419:1: expression returns [JCExpression expr] : ( foreach | functionExpression | operationExpression | alphaExpression | ifExpression | selectExpression | LPAREN typeName RPAREN suffixedExpression | suffixedExpression );", 58, 0, input);

                throw nvae;
            }

            switch (alt58) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:420:4: foreach
                    {
                    pushFollow(FOLLOW_foreach_in_expression4706);
                    foreach();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:421:11: functionExpression
                    {
                    pushFollow(FOLLOW_functionExpression_in_expression4719);
                    functionExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:422:11: operationExpression
                    {
                    pushFollow(FOLLOW_operationExpression_in_expression4732);
                    operationExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:423:11: alphaExpression
                    {
                    pushFollow(FOLLOW_alphaExpression_in_expression4745);
                    alphaExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:424:11: ifExpression
                    {
                    pushFollow(FOLLOW_ifExpression_in_expression4758);
                    ifExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:425:11: selectExpression
                    {
                    pushFollow(FOLLOW_selectExpression_in_expression4774);
                    selectExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:426:11: LPAREN typeName RPAREN suffixedExpression
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_expression4787); if (failed) return expr;
                    pushFollow(FOLLOW_typeName_in_expression4793);
                    typeName();
                    _fsp--;
                    if (failed) return expr;
                    match(input,RPAREN,FOLLOW_RPAREN_in_expression4796); if (failed) return expr;
                    pushFollow(FOLLOW_suffixedExpression_in_expression4800);
                    suffixedExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:427:11: suffixedExpression
                    {
                    pushFollow(FOLLOW_suffixedExpression_in_expression4815);
                    suffixedExpression103=suffixedExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = suffixedExpression103; 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:428:1: foreach : FOREACH LPAREN name IN expression ( COMMA name IN expression )* ( WHERE expression )? RPAREN expression ;
    public final void foreach() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:428:9: ( FOREACH LPAREN name IN expression ( COMMA name IN expression )* ( WHERE expression )? RPAREN expression )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:428:11: FOREACH LPAREN name IN expression ( COMMA name IN expression )* ( WHERE expression )? RPAREN expression
            {
            match(input,FOREACH,FOLLOW_FOREACH_in_foreach4827); if (failed) return ;
            match(input,LPAREN,FOLLOW_LPAREN_in_foreach4831); if (failed) return ;
            pushFollow(FOLLOW_name_in_foreach4835);
            name();
            _fsp--;
            if (failed) return ;
            match(input,IN,FOLLOW_IN_in_foreach4839); if (failed) return ;
            pushFollow(FOLLOW_expression_in_foreach4843);
            expression();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:428:55: ( COMMA name IN expression )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( (LA59_0==COMMA) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:428:59: COMMA name IN expression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_foreach4851); if (failed) return ;
            	    pushFollow(FOLLOW_name_in_foreach4855);
            	    name();
            	    _fsp--;
            	    if (failed) return ;
            	    match(input,IN,FOLLOW_IN_in_foreach4859); if (failed) return ;
            	    pushFollow(FOLLOW_expression_in_foreach4863);
            	    expression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop59;
                }
            } while (true);

            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:428:98: ( WHERE expression )?
            int alt60=2;
            int LA60_0 = input.LA(1);

            if ( (LA60_0==WHERE) ) {
                alt60=1;
            }
            switch (alt60) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:428:102: WHERE expression
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_foreach4877); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_foreach4881);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_foreach4891); if (failed) return ;
            pushFollow(FOLLOW_expression_in_foreach4895);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:429:1: functionExpression : FUNCTION formalParameters ( typeReference )? functionBody ;
    public final void functionExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:429:20: ( FUNCTION formalParameters ( typeReference )? functionBody )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:429:22: FUNCTION formalParameters ( typeReference )? functionBody
            {
            match(input,FUNCTION,FOLLOW_FUNCTION_in_functionExpression4903); if (failed) return ;
            pushFollow(FOLLOW_formalParameters_in_functionExpression4907);
            formalParameters();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:429:52: ( typeReference )?
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

                    if ( (synpred106()) ) {
                        alt61=1;
                    }
                    }
                    break;
                case LBRACE:
                    {
                    int LA61_3 = input.LA(2);

                    if ( (synpred106()) ) {
                        alt61=1;
                    }
                    }
                    break;
            }

            switch (alt61) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: typeReference
                    {
                    pushFollow(FOLLOW_typeReference_in_functionExpression4911);
                    typeReference();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_functionBody_in_functionExpression4917);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:430:1: operationExpression : OPERATION formalParameters ( typeReference )? block ;
    public final void operationExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:430:21: ( OPERATION formalParameters ( typeReference )? block )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:430:23: OPERATION formalParameters ( typeReference )? block
            {
            match(input,OPERATION,FOLLOW_OPERATION_in_operationExpression4925); if (failed) return ;
            pushFollow(FOLLOW_formalParameters_in_operationExpression4929);
            formalParameters();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:430:54: ( typeReference )?
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==COLON) ) {
                alt62=1;
            }
            else if ( (LA62_0==LBRACE) ) {
                int LA62_2 = input.LA(2);

                if ( (synpred107()) ) {
                    alt62=1;
                }
            }
            switch (alt62) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: typeReference
                    {
                    pushFollow(FOLLOW_typeReference_in_operationExpression4933);
                    typeReference();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_operationExpression4939);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:431:1: ifExpression : IF expression THEN expression ELSE expression ;
    public final void ifExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:431:14: ( IF expression THEN expression ELSE expression )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:431:16: IF expression THEN expression ELSE expression
            {
            match(input,IF,FOLLOW_IF_in_ifExpression4947); if (failed) return ;
            pushFollow(FOLLOW_expression_in_ifExpression4951);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,THEN,FOLLOW_THEN_in_ifExpression4955); if (failed) return ;
            pushFollow(FOLLOW_expression_in_ifExpression4959);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,ELSE,FOLLOW_ELSE_in_ifExpression4963); if (failed) return ;
            pushFollow(FOLLOW_expression_in_ifExpression4967);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:1: selectExpression : SELECT ( DISTINCT )? expression FROM selectionVar ( COMMA selectionVar )* ( WHERE expression )? ;
    public final void selectExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:18: ( SELECT ( DISTINCT )? expression FROM selectionVar ( COMMA selectionVar )* ( WHERE expression )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:20: SELECT ( DISTINCT )? expression FROM selectionVar ( COMMA selectionVar )* ( WHERE expression )?
            {
            match(input,SELECT,FOLLOW_SELECT_in_selectExpression4975); if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:29: ( DISTINCT )?
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==DISTINCT) ) {
                alt63=1;
            }
            switch (alt63) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: DISTINCT
                    {
                    match(input,DISTINCT,FOLLOW_DISTINCT_in_selectExpression4979); if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_expression_in_selectExpression4987);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,FROM,FOLLOW_FROM_in_selectExpression4991); if (failed) return ;
            pushFollow(FOLLOW_selectionVar_in_selectExpression4995);
            selectionVar();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:79: ( COMMA selectionVar )*
            loop64:
            do {
                int alt64=2;
                int LA64_0 = input.LA(1);

                if ( (LA64_0==COMMA) ) {
                    int LA64_2 = input.LA(2);

                    if ( (synpred109()) ) {
                        alt64=1;
                    }


                }


                switch (alt64) {
            	case 1 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:83: COMMA selectionVar
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_selectExpression5003); if (failed) return ;
            	    pushFollow(FOLLOW_selectionVar_in_selectExpression5007);
            	    selectionVar();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop64;
                }
            } while (true);

            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:112: ( WHERE expression )?
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( (LA65_0==WHERE) ) {
                int LA65_1 = input.LA(2);

                if ( (synpred110()) ) {
                    alt65=1;
                }
            }
            switch (alt65) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:116: WHERE expression
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_selectExpression5021); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_selectExpression5025);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:433:1: selectionVar : name ( IN expression )? ;
    public final void selectionVar() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:433:14: ( name ( IN expression )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:433:16: name ( IN expression )?
            {
            pushFollow(FOLLOW_name_in_selectionVar5039);
            name();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:433:23: ( IN expression )?
            int alt66=2;
            int LA66_0 = input.LA(1);

            if ( (LA66_0==IN) ) {
                alt66=1;
            }
            switch (alt66) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:433:27: IN expression
                    {
                    match(input,IN,FOLLOW_IN_in_selectionVar5047); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_selectionVar5051);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:434:1: suffixedExpression returns [JCExpression expr] : e1= assignmentExpression ( indexOn | orderBy | durClause | PLUSPLUS | SUBSUB )? ;
    public final JCExpression suffixedExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:435:2: (e1= assignmentExpression ( indexOn | orderBy | durClause | PLUSPLUS | SUBSUB )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:435:4: e1= assignmentExpression ( indexOn | orderBy | durClause | PLUSPLUS | SUBSUB )?
            {
            pushFollow(FOLLOW_assignmentExpression_in_suffixedExpression5074);
            e1=assignmentExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:436:5: ( indexOn | orderBy | durClause | PLUSPLUS | SUBSUB )?
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
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:436:6: indexOn
                    {
                    pushFollow(FOLLOW_indexOn_in_suffixedExpression5086);
                    indexOn();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:436:16: orderBy
                    {
                    pushFollow(FOLLOW_orderBy_in_suffixedExpression5090);
                    orderBy();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:436:26: durClause
                    {
                    pushFollow(FOLLOW_durClause_in_suffixedExpression5094);
                    durClause();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:436:38: PLUSPLUS
                    {
                    match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_suffixedExpression5098); if (failed) return expr;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:436:49: SUBSUB
                    {
                    match(input,SUBSUB,FOLLOW_SUBSUB_in_suffixedExpression5102); if (failed) return expr;

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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:437:1: assignmentExpression returns [JCExpression expr] : e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? ;
    public final JCExpression assignmentExpression() throws RecognitionException {
        JCExpression expr = null;

        Token EQ104=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:438:2: (e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:438:4: e1= assignmentOpExpression ( EQ e2= assignmentOpExpression )?
            {
            pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression5124);
            e1=assignmentOpExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:439:5: ( EQ e2= assignmentOpExpression )?
            int alt68=2;
            int LA68_0 = input.LA(1);

            if ( (LA68_0==EQ) ) {
                alt68=1;
            }
            switch (alt68) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:439:9: EQ e2= assignmentOpExpression
                    {
                    EQ104=(Token)input.LT(1);
                    match(input,EQ,FOLLOW_EQ_in_assignmentExpression5139); if (failed) return expr;
                    pushFollow(FOLLOW_assignmentOpExpression_in_assignmentExpression5145);
                    e2=assignmentOpExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(EQ104.getTokenIndex()).Assign(expr, e2); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:440:1: assignmentOpExpression returns [JCExpression expr] : e1= andExpression ( assignmentOperator e2= andExpression )? ;
    public final JCExpression assignmentOpExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression e1 = null;

        JCExpression e2 = null;

        int assignmentOperator105 = 0;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:441:2: (e1= andExpression ( assignmentOperator e2= andExpression )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:441:4: e1= andExpression ( assignmentOperator e2= andExpression )?
            {
            pushFollow(FOLLOW_andExpression_in_assignmentOpExpression5172);
            e1=andExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:442:5: ( assignmentOperator e2= andExpression )?
            int alt69=2;
            int LA69_0 = input.LA(1);

            if ( ((LA69_0>=PLUSEQ && LA69_0<=PERCENTEQ)) ) {
                alt69=1;
            }
            switch (alt69) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:442:9: assignmentOperator e2= andExpression
                    {
                    pushFollow(FOLLOW_assignmentOperator_in_assignmentOpExpression5188);
                    assignmentOperator105=assignmentOperator();
                    _fsp--;
                    if (failed) return expr;
                    pushFollow(FOLLOW_andExpression_in_assignmentOpExpression5194);
                    e2=andExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.Assignop(assignmentOperator105,
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:444:1: andExpression returns [JCExpression expr] : e1= orExpression ( AND e2= orExpression )* ;
    public final JCExpression andExpression() throws RecognitionException {
        JCExpression expr = null;

        Token AND106=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:445:2: (e1= orExpression ( AND e2= orExpression )* )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:445:4: e1= orExpression ( AND e2= orExpression )*
            {
            pushFollow(FOLLOW_orExpression_in_andExpression5220);
            e1=orExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:446:5: ( AND e2= orExpression )*
            loop70:
            do {
                int alt70=2;
                int LA70_0 = input.LA(1);

                if ( (LA70_0==AND) ) {
                    alt70=1;
                }


                switch (alt70) {
            	case 1 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:446:9: AND e2= orExpression
            	    {
            	    AND106=(Token)input.LT(1);
            	    match(input,AND,FOLLOW_AND_in_andExpression5236); if (failed) return expr;
            	    pushFollow(FOLLOW_orExpression_in_andExpression5242);
            	    e2=orExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(AND106.getTokenIndex()).Binary(JCTree.AND, expr, e2); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:447:1: orExpression returns [JCExpression expr] : e1= instanceOfExpression ( OR e2= instanceOfExpression )* ;
    public final JCExpression orExpression() throws RecognitionException {
        JCExpression expr = null;

        Token OR107=null;
        JCExpression e1 = null;

        JCExpression e2 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:448:2: (e1= instanceOfExpression ( OR e2= instanceOfExpression )* )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:448:4: e1= instanceOfExpression ( OR e2= instanceOfExpression )*
            {
            pushFollow(FOLLOW_instanceOfExpression_in_orExpression5270);
            e1=instanceOfExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:449:5: ( OR e2= instanceOfExpression )*
            loop71:
            do {
                int alt71=2;
                int LA71_0 = input.LA(1);

                if ( (LA71_0==OR) ) {
                    alt71=1;
                }


                switch (alt71) {
            	case 1 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:449:9: OR e2= instanceOfExpression
            	    {
            	    OR107=(Token)input.LT(1);
            	    match(input,OR,FOLLOW_OR_in_orExpression5285); if (failed) return expr;
            	    pushFollow(FOLLOW_instanceOfExpression_in_orExpression5291);
            	    e2=instanceOfExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(OR107.getTokenIndex()).Binary(JCTree.OR, expr, e2); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:450:1: instanceOfExpression returns [JCExpression expr] : e1= relationalExpression ( INSTANCEOF identifier )? ;
    public final JCExpression instanceOfExpression() throws RecognitionException {
        JCExpression expr = null;

        Token INSTANCEOF108=null;
        JCExpression e1 = null;

        JCIdent identifier109 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:451:2: (e1= relationalExpression ( INSTANCEOF identifier )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:451:4: e1= relationalExpression ( INSTANCEOF identifier )?
            {
            pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression5319);
            e1=relationalExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:452:5: ( INSTANCEOF identifier )?
            int alt72=2;
            int LA72_0 = input.LA(1);

            if ( (LA72_0==INSTANCEOF) ) {
                alt72=1;
            }
            switch (alt72) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:452:9: INSTANCEOF identifier
                    {
                    INSTANCEOF108=(Token)input.LT(1);
                    match(input,INSTANCEOF,FOLLOW_INSTANCEOF_in_instanceOfExpression5334); if (failed) return expr;
                    pushFollow(FOLLOW_identifier_in_instanceOfExpression5336);
                    identifier109=identifier();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(INSTANCEOF108.getTokenIndex()).Binary(JCTree.TYPETEST, expr, 
                      	   													 identifier109); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:454:1: relationalExpression returns [JCExpression expr] : e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* ;
    public final JCExpression relationalExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LTGT110=null;
        Token EQEQ111=null;
        Token LTEQ112=null;
        Token GTEQ113=null;
        Token LT114=null;
        Token GT115=null;
        Token IN116=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:455:2: (e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )* )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:455:4: e1= additiveExpression ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
            {
            pushFollow(FOLLOW_additiveExpression_in_relationalExpression5364);
            e1=additiveExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:456:5: ( LTGT e= additiveExpression | EQEQ e= additiveExpression | LTEQ e= additiveExpression | GTEQ e= additiveExpression | LT e= additiveExpression | GT e= additiveExpression | IN e= additiveExpression )*
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
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:456:9: LTGT e= additiveExpression
            	    {
            	    LTGT110=(Token)input.LT(1);
            	    match(input,LTGT,FOLLOW_LTGT_in_relationalExpression5380); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5386);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(LTGT110.getTokenIndex()).Binary(JCTree.NE, expr, e); 
            	    }

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:457:9: EQEQ e= additiveExpression
            	    {
            	    EQEQ111=(Token)input.LT(1);
            	    match(input,EQEQ,FOLLOW_EQEQ_in_relationalExpression5400); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5406);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(EQEQ111.getTokenIndex()).Binary(JCTree.EQ, expr, e); 
            	    }

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:458:9: LTEQ e= additiveExpression
            	    {
            	    LTEQ112=(Token)input.LT(1);
            	    match(input,LTEQ,FOLLOW_LTEQ_in_relationalExpression5420); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5426);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(LTEQ112.getTokenIndex()).Binary(JCTree.LE, expr, e); 
            	    }

            	    }
            	    break;
            	case 4 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:459:9: GTEQ e= additiveExpression
            	    {
            	    GTEQ113=(Token)input.LT(1);
            	    match(input,GTEQ,FOLLOW_GTEQ_in_relationalExpression5440); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5446);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(GTEQ113.getTokenIndex()).Binary(JCTree.GE, expr, e); 
            	    }

            	    }
            	    break;
            	case 5 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:460:9: LT e= additiveExpression
            	    {
            	    LT114=(Token)input.LT(1);
            	    match(input,LT,FOLLOW_LT_in_relationalExpression5460); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5468);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(LT114.getTokenIndex())  .Binary(JCTree.LT, expr, e); 
            	    }

            	    }
            	    break;
            	case 6 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:461:9: GT e= additiveExpression
            	    {
            	    GT115=(Token)input.LT(1);
            	    match(input,GT,FOLLOW_GT_in_relationalExpression5482); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5490);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(GT115.getTokenIndex())  .Binary(JCTree.GT, expr, e); 
            	    }

            	    }
            	    break;
            	case 7 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:462:9: IN e= additiveExpression
            	    {
            	    IN116=(Token)input.LT(1);
            	    match(input,IN,FOLLOW_IN_in_relationalExpression5504); if (failed) return expr;
            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression5512);
            	    e=additiveExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       /* expr = F.at(IN116  .index).Binary(JavaFXTag.IN, expr, $e2.expr); */ 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:464:1: additiveExpression returns [JCExpression expr] : e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* ;
    public final JCExpression additiveExpression() throws RecognitionException {
        JCExpression expr = null;

        Token PLUS117=null;
        Token SUB118=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:465:2: (e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )* )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:465:4: e1= multiplicativeExpression ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
            {
            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression5541);
            e1=multiplicativeExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:466:5: ( PLUS e= multiplicativeExpression | SUB e= multiplicativeExpression )*
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
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:466:9: PLUS e= multiplicativeExpression
            	    {
            	    PLUS117=(Token)input.LT(1);
            	    match(input,PLUS,FOLLOW_PLUS_in_additiveExpression5556); if (failed) return expr;
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression5562);
            	    e=multiplicativeExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(PLUS117.getTokenIndex()).Binary(JCTree.PLUS , expr, e); 
            	    }

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:467:9: SUB e= multiplicativeExpression
            	    {
            	    SUB118=(Token)input.LT(1);
            	    match(input,SUB,FOLLOW_SUB_in_additiveExpression5575); if (failed) return expr;
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression5582);
            	    e=multiplicativeExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(SUB118.getTokenIndex()) .Binary(JCTree.MINUS, expr, e); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:469:1: multiplicativeExpression returns [JCExpression expr] : e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* ;
    public final JCExpression multiplicativeExpression() throws RecognitionException {
        JCExpression expr = null;

        Token STAR119=null;
        Token SLASH120=null;
        Token PERCENT121=null;
        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:470:2: (e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )* )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:470:4: e1= unaryExpression ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
            {
            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression5610);
            e1=unaryExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = e1; 
            }
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:471:5: ( STAR e= unaryExpression | SLASH e= unaryExpression | PERCENT e= unaryExpression )*
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
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:471:9: STAR e= unaryExpression
            	    {
            	    STAR119=(Token)input.LT(1);
            	    match(input,STAR,FOLLOW_STAR_in_multiplicativeExpression5626); if (failed) return expr;
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression5633);
            	    e=unaryExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(STAR119.getTokenIndex())   .Binary(JCTree.MUL  , expr, e); 
            	    }

            	    }
            	    break;
            	case 2 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:472:9: SLASH e= unaryExpression
            	    {
            	    SLASH120=(Token)input.LT(1);
            	    match(input,SLASH,FOLLOW_SLASH_in_multiplicativeExpression5647); if (failed) return expr;
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression5653);
            	    e=unaryExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(SLASH120.getTokenIndex())  .Binary(JCTree.DIV  , expr, e); 
            	    }

            	    }
            	    break;
            	case 3 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:473:9: PERCENT e= unaryExpression
            	    {
            	    PERCENT121=(Token)input.LT(1);
            	    match(input,PERCENT,FOLLOW_PERCENT_in_multiplicativeExpression5667); if (failed) return expr;
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression5671);
            	    e=unaryExpression();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.at(PERCENT121.getTokenIndex()).Binary(JCTree.MOD  , expr, e); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:475:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );
    public final JCExpression unaryExpression() throws RecognitionException {
        JCExpression expr = null;

        JCExpression postfixExpression122 = null;

        int unaryOperator123 = 0;

        JCExpression postfixExpression124 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:476:2: ( postfixExpression | unaryOperator postfixExpression )
            int alt76=2;
            int LA76_0 = input.LA(1);

            if ( ((LA76_0>=THIS && LA76_0<=FALSE)||LA76_0==NEW||LA76_0==INDEXOF||LA76_0==SUPER||LA76_0==LBRACKET||LA76_0==DOT||(LA76_0>=STRING_LITERAL && LA76_0<=QUOTE_LBRACE_STRING_LITERAL)||(LA76_0>=QUOTED_IDENTIFIER && LA76_0<=INTEGER_LITERAL)||LA76_0==FLOATING_POINT_LITERAL||LA76_0==IDENTIFIER) ) {
                alt76=1;
            }
            else if ( ((LA76_0>=POUND && LA76_0<=TYPEOF)||LA76_0==NOT||(LA76_0>=SIZEOF && LA76_0<=REVERSE)||(LA76_0>=PLUSPLUS && LA76_0<=SUBSUB)||LA76_0==QUES) ) {
                alt76=2;
            }
            else {
                if (backtracking>0) {failed=true; return expr;}
                NoViableAltException nvae =
                    new NoViableAltException("475:1: unaryExpression returns [JCExpression expr] : ( postfixExpression | unaryOperator postfixExpression );", 76, 0, input);

                throw nvae;
            }
            switch (alt76) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:476:4: postfixExpression
                    {
                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression5701);
                    postfixExpression122=postfixExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = postfixExpression122; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:477:4: unaryOperator postfixExpression
                    {
                    pushFollow(FOLLOW_unaryOperator_in_unaryExpression5712);
                    unaryOperator123=unaryOperator();
                    _fsp--;
                    if (failed) return expr;
                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression5716);
                    postfixExpression124=postfixExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.Unary(unaryOperator123, postfixExpression124); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:479:1: postfixExpression returns [JCExpression expr] : primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* ;
    public final JCExpression postfixExpression() throws RecognitionException {
        JCExpression expr = null;

        Token DOT126=null;
        Token LPAREN127=null;
        Name name1 = null;

        JCExpression primaryExpression125 = null;

        ListBuffer<JCExpression> expressionListOpt128 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:480:2: ( primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )* )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:480:4: primaryExpression ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
            {
            pushFollow(FOLLOW_primaryExpression_in_postfixExpression5736);
            primaryExpression125=primaryExpression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = primaryExpression125; 
            }
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:481:5: ( DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* ) | LBRACKET ( name BAR )? expression RBRACKET )*
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
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:481:7: DOT ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
            	    {
            	    DOT126=(Token)input.LT(1);
            	    match(input,DOT,FOLLOW_DOT_in_postfixExpression5751); if (failed) return expr;
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:481:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )
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
            	            new NoViableAltException("481:11: ( CLASS | name1= name ( LPAREN expressionListOpt RPAREN )* )", 78, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt78) {
            	        case 1 :
            	            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:481:13: CLASS
            	            {
            	            match(input,CLASS,FOLLOW_CLASS_in_postfixExpression5755); if (failed) return expr;

            	            }
            	            break;
            	        case 2 :
            	            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:482:13: name1= name ( LPAREN expressionListOpt RPAREN )*
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression5774);
            	            name1=name();
            	            _fsp--;
            	            if (failed) return expr;
            	            if ( backtracking==0 ) {
            	               expr = F.at(DOT126.getTokenIndex()).Select(expr, name1); 
            	            }
            	            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:483:14: ( LPAREN expressionListOpt RPAREN )*
            	            loop77:
            	            do {
            	                int alt77=2;
            	                int LA77_0 = input.LA(1);

            	                if ( (LA77_0==LPAREN) ) {
            	                    alt77=1;
            	                }


            	                switch (alt77) {
            	            	case 1 :
            	            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:483:16: LPAREN expressionListOpt RPAREN
            	            	    {
            	            	    LPAREN127=(Token)input.LT(1);
            	            	    match(input,LPAREN,FOLLOW_LPAREN_in_postfixExpression5799); if (failed) return expr;
            	            	    pushFollow(FOLLOW_expressionListOpt_in_postfixExpression5801);
            	            	    expressionListOpt128=expressionListOpt();
            	            	    _fsp--;
            	            	    if (failed) return expr;
            	            	    match(input,RPAREN,FOLLOW_RPAREN_in_postfixExpression5803); if (failed) return expr;
            	            	    if ( backtracking==0 ) {
            	            	       expr = F.at(LPAREN127.getTokenIndex()).Apply(null, expr, expressionListOpt128.toList()); 
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
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:485:7: LBRACKET ( name BAR )? expression RBRACKET
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_postfixExpression5835); if (failed) return expr;
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:485:16: ( name BAR )?
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
            	            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:485:17: name BAR
            	            {
            	            pushFollow(FOLLOW_name_in_postfixExpression5838);
            	            name();
            	            _fsp--;
            	            if (failed) return expr;
            	            match(input,BAR,FOLLOW_BAR_in_postfixExpression5840); if (failed) return expr;

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_expression_in_postfixExpression5844);
            	    expression();
            	    _fsp--;
            	    if (failed) return expr;
            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_postfixExpression5847); if (failed) return expr;

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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:487:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | bracketExpression | ordinalExpression | contextExpression | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal );
    public final JCExpression primaryExpression() throws RecognitionException {
        JCExpression expr = null;

        Token LBRACE130=null;
        Token THIS133=null;
        Token SUPER134=null;
        Token LPAREN136=null;
        JCExpression newExpression129 = null;

        JCIdent identifier131 = null;

        ListBuffer<JFXStatement> objectLiteral132 = null;

        JCIdent identifier135 = null;

        ListBuffer<JCExpression> expressionListOpt137 = null;

        JCExpression stringExpression138 = null;

        JCExpression literal139 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:488:2: ( newExpression | identifier LBRACE objectLiteral RBRACE | bracketExpression | ordinalExpression | contextExpression | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal )
            int alt82=10;
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

                if ( (synpred141()) ) {
                    alt82=2;
                }
                else if ( (synpred148()) ) {
                    alt82=8;
                }
                else {
                    if (backtracking>0) {failed=true; return expr;}
                    NoViableAltException nvae =
                        new NoViableAltException("487:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | bracketExpression | ordinalExpression | contextExpression | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal );", 82, 2, input);

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
            default:
                if (backtracking>0) {failed=true; return expr;}
                NoViableAltException nvae =
                    new NoViableAltException("487:1: primaryExpression returns [JCExpression expr] : ( newExpression | identifier LBRACE objectLiteral RBRACE | bracketExpression | ordinalExpression | contextExpression | THIS | SUPER | identifier ( LPAREN expressionListOpt RPAREN )* | stringExpression | literal );", 82, 0, input);

                throw nvae;
            }

            switch (alt82) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:488:4: newExpression
                    {
                    pushFollow(FOLLOW_newExpression_in_primaryExpression5872);
                    newExpression129=newExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = newExpression129; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:489:4: identifier LBRACE objectLiteral RBRACE
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression5884);
                    identifier131=identifier();
                    _fsp--;
                    if (failed) return expr;
                    LBRACE130=(Token)input.LT(1);
                    match(input,LBRACE,FOLLOW_LBRACE_in_primaryExpression5886); if (failed) return expr;
                    pushFollow(FOLLOW_objectLiteral_in_primaryExpression5889);
                    objectLiteral132=objectLiteral();
                    _fsp--;
                    if (failed) return expr;
                    match(input,RBRACE,FOLLOW_RBRACE_in_primaryExpression5891); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(LBRACE130.getTokenIndex()).PureObjectLiteral(identifier131, objectLiteral132.toList()); 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:490:10: bracketExpression
                    {
                    pushFollow(FOLLOW_bracketExpression_in_primaryExpression5907);
                    bracketExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:492:10: ordinalExpression
                    {
                    pushFollow(FOLLOW_ordinalExpression_in_primaryExpression5927);
                    ordinalExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:493:10: contextExpression
                    {
                    pushFollow(FOLLOW_contextExpression_in_primaryExpression5939);
                    contextExpression();
                    _fsp--;
                    if (failed) return expr;

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:494:10: THIS
                    {
                    THIS133=(Token)input.LT(1);
                    match(input,THIS,FOLLOW_THIS_in_primaryExpression5951); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(THIS133.getTokenIndex()).Identifier(names._this); 
                    }

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:495:10: SUPER
                    {
                    SUPER134=(Token)input.LT(1);
                    match(input,SUPER,FOLLOW_SUPER_in_primaryExpression5970); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(SUPER134.getTokenIndex()).Identifier(names._super); 
                    }

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:496:10: identifier ( LPAREN expressionListOpt RPAREN )*
                    {
                    pushFollow(FOLLOW_identifier_in_primaryExpression5989);
                    identifier135=identifier();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = identifier135; 
                    }
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:497:10: ( LPAREN expressionListOpt RPAREN )*
                    loop81:
                    do {
                        int alt81=2;
                        int LA81_0 = input.LA(1);

                        if ( (LA81_0==LPAREN) ) {
                            alt81=1;
                        }


                        switch (alt81) {
                    	case 1 :
                    	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:497:12: LPAREN expressionListOpt RPAREN
                    	    {
                    	    LPAREN136=(Token)input.LT(1);
                    	    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression6010); if (failed) return expr;
                    	    pushFollow(FOLLOW_expressionListOpt_in_primaryExpression6014);
                    	    expressionListOpt137=expressionListOpt();
                    	    _fsp--;
                    	    if (failed) return expr;
                    	    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression6018); if (failed) return expr;
                    	    if ( backtracking==0 ) {
                    	       expr = F.at(LPAREN136.getTokenIndex()).Apply(null, expr, expressionListOpt137.toList()); 
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
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:498:10: stringExpression
                    {
                    pushFollow(FOLLOW_stringExpression_in_primaryExpression6037);
                    stringExpression138=stringExpression();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = stringExpression138; 
                    }

                    }
                    break;
                case 10 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:499:10: literal
                    {
                    pushFollow(FOLLOW_literal_in_primaryExpression6055);
                    literal139=literal();
                    _fsp--;
                    if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = literal139; 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:501:1: newExpression returns [JCExpression expr] : NEW identifier ( LPAREN expressionListOpt RPAREN )? ;
    public final JCExpression newExpression() throws RecognitionException {
        JCExpression expr = null;

        Token NEW141=null;
        ListBuffer<JCExpression> expressionListOpt140 = null;

        JCIdent identifier142 = null;


         ListBuffer<JCExpression> args = null; 
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:503:2: ( NEW identifier ( LPAREN expressionListOpt RPAREN )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:503:4: NEW identifier ( LPAREN expressionListOpt RPAREN )?
            {
            NEW141=(Token)input.LT(1);
            match(input,NEW,FOLLOW_NEW_in_newExpression6090); if (failed) return expr;
            pushFollow(FOLLOW_identifier_in_newExpression6093);
            identifier142=identifier();
            _fsp--;
            if (failed) return expr;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:3: ( LPAREN expressionListOpt RPAREN )?
            int alt83=2;
            int LA83_0 = input.LA(1);

            if ( (LA83_0==LPAREN) ) {
                alt83=1;
            }
            switch (alt83) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:504:5: LPAREN expressionListOpt RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_newExpression6101); if (failed) return expr;
                    pushFollow(FOLLOW_expressionListOpt_in_newExpression6105);
                    expressionListOpt140=expressionListOpt();
                    _fsp--;
                    if (failed) return expr;
                    match(input,RPAREN,FOLLOW_RPAREN_in_newExpression6109); if (failed) return expr;
                    if ( backtracking==0 ) {
                       args = expressionListOpt140; 
                    }

                    }
                    break;

            }

            if ( backtracking==0 ) {
               expr = F.at(NEW141.getTokenIndex()).NewClass(null, null, identifier142, 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:509:1: objectLiteral returns [ListBuffer<JFXStatement> parts = new ListBuffer<JFXStatement>()] : ( objectLiteralPart )* ;
    public final ListBuffer<JFXStatement> objectLiteral() throws RecognitionException {
        ListBuffer<JFXStatement> parts =  new ListBuffer<JFXStatement>();

        JFXStatement objectLiteralPart143 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:2: ( ( objectLiteralPart )* )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:4: ( objectLiteralPart )*
            {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:4: ( objectLiteralPart )*
            loop84:
            do {
                int alt84=2;
                int LA84_0 = input.LA(1);

                if ( (LA84_0==ATTRIBUTE||LA84_0==VAR||LA84_0==TRIGGER||(LA84_0>=OPERATION && LA84_0<=FUNCTION)||LA84_0==QUOTED_IDENTIFIER||LA84_0==IDENTIFIER) ) {
                    alt84=1;
                }


                switch (alt84) {
            	case 1 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:510:6: objectLiteralPart
            	    {
            	    pushFollow(FOLLOW_objectLiteralPart_in_objectLiteral6149);
            	    objectLiteralPart143=objectLiteralPart();
            	    _fsp--;
            	    if (failed) return parts;
            	    if ( backtracking==0 ) {
            	       parts.append(objectLiteralPart143); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:511:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | ATTRIBUTE name typeReference EQ bindOpt expression SEMI | localOperationDefinition | localFunctionDefinition | localTriggerStatement | variableDefinition );
    public final JFXStatement objectLiteralPart() throws RecognitionException {
        JFXStatement value = null;

        Token COLON144=null;
        Name name145 = null;

        JCExpression expression146 = null;

        JavafxBindStatus bindOpt147 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:512:2: ( name COLON bindOpt expression ( COMMA | SEMI )? | ATTRIBUTE name typeReference EQ bindOpt expression SEMI | localOperationDefinition | localFunctionDefinition | localTriggerStatement | variableDefinition )
            int alt86=6;
            switch ( input.LA(1) ) {
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                int LA86_1 = input.LA(2);

                if ( (LA86_1==LPAREN) ) {
                    alt86=4;
                }
                else if ( (LA86_1==COLON) ) {
                    alt86=1;
                }
                else {
                    if (backtracking>0) {failed=true; return value;}
                    NoViableAltException nvae =
                        new NoViableAltException("511:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | ATTRIBUTE name typeReference EQ bindOpt expression SEMI | localOperationDefinition | localFunctionDefinition | localTriggerStatement | variableDefinition );", 86, 1, input);

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
                    new NoViableAltException("511:1: objectLiteralPart returns [JFXStatement value] : ( name COLON bindOpt expression ( COMMA | SEMI )? | ATTRIBUTE name typeReference EQ bindOpt expression SEMI | localOperationDefinition | localFunctionDefinition | localTriggerStatement | variableDefinition );", 86, 0, input);

                throw nvae;
            }

            switch (alt86) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:512:4: name COLON bindOpt expression ( COMMA | SEMI )?
                    {
                    pushFollow(FOLLOW_name_in_objectLiteralPart6175);
                    name145=name();
                    _fsp--;
                    if (failed) return value;
                    COLON144=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_objectLiteralPart6177); if (failed) return value;
                    pushFollow(FOLLOW_bindOpt_in_objectLiteralPart6180);
                    bindOpt147=bindOpt();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_expression_in_objectLiteralPart6182);
                    expression146=expression();
                    _fsp--;
                    if (failed) return value;
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:512:35: ( COMMA | SEMI )?
                    int alt85=2;
                    int LA85_0 = input.LA(1);

                    if ( ((LA85_0>=SEMI && LA85_0<=COMMA)) ) {
                        alt85=1;
                    }
                    switch (alt85) {
                        case 1 :
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
                            {
                            if ( (input.LA(1)>=SEMI && input.LA(1)<=COMMA) ) {
                                input.consume();
                                errorRecovery=false;failed=false;
                            }
                            else {
                                if (backtracking>0) {failed=true; return value;}
                                MismatchedSetException mse =
                                    new MismatchedSetException(null,input);
                                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_objectLiteralPart6184);    throw mse;
                            }


                            }
                            break;

                    }

                    if ( backtracking==0 ) {
                       value = F.at(COLON144.getTokenIndex()).ObjectLiteralPart(name145, expression146, bindOpt147); 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:513:10: ATTRIBUTE name typeReference EQ bindOpt expression SEMI
                    {
                    match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_objectLiteralPart6204); if (failed) return value;
                    pushFollow(FOLLOW_name_in_objectLiteralPart6208);
                    name();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_typeReference_in_objectLiteralPart6212);
                    typeReference();
                    _fsp--;
                    if (failed) return value;
                    match(input,EQ,FOLLOW_EQ_in_objectLiteralPart6216); if (failed) return value;
                    pushFollow(FOLLOW_bindOpt_in_objectLiteralPart6219);
                    bindOpt();
                    _fsp--;
                    if (failed) return value;
                    pushFollow(FOLLOW_expression_in_objectLiteralPart6221);
                    expression();
                    _fsp--;
                    if (failed) return value;
                    match(input,SEMI,FOLLOW_SEMI_in_objectLiteralPart6225); if (failed) return value;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:514:10: localOperationDefinition
                    {
                    pushFollow(FOLLOW_localOperationDefinition_in_objectLiteralPart6237);
                    localOperationDefinition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:515:10: localFunctionDefinition
                    {
                    pushFollow(FOLLOW_localFunctionDefinition_in_objectLiteralPart6249);
                    localFunctionDefinition();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:516:10: localTriggerStatement
                    {
                    pushFollow(FOLLOW_localTriggerStatement_in_objectLiteralPart6261);
                    localTriggerStatement();
                    _fsp--;
                    if (failed) return value;

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:517:10: variableDefinition
                    {
                    pushFollow(FOLLOW_variableDefinition_in_objectLiteralPart6273);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:1: bracketExpression : LBRACKET ( generatorClause | dotDotClause | expressionListOpt ) RBRACKET ;
    public final void bracketExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:19: ( LBRACKET ( generatorClause | dotDotClause | expressionListOpt ) RBRACKET )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:21: LBRACKET ( generatorClause | dotDotClause | expressionListOpt ) RBRACKET
            {
            match(input,LBRACKET,FOLLOW_LBRACKET_in_bracketExpression6281); if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:32: ( generatorClause | dotDotClause | expressionListOpt )
            int alt87=3;
            switch ( input.LA(1) ) {
            case FOREACH:
                {
                int LA87_1 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 1, input);

                    throw nvae;
                }
                }
                break;
            case FUNCTION:
                {
                int LA87_2 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 2, input);

                    throw nvae;
                }
                }
                break;
            case OPERATION:
                {
                int LA87_3 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 3, input);

                    throw nvae;
                }
                }
                break;
            case UNITINTERVAL:
                {
                int LA87_4 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 4, input);

                    throw nvae;
                }
                }
                break;
            case IF:
                {
                int LA87_5 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 5, input);

                    throw nvae;
                }
                }
                break;
            case SELECT:
                {
                int LA87_6 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 6, input);

                    throw nvae;
                }
                }
                break;
            case LPAREN:
                {
                int LA87_7 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 7, input);

                    throw nvae;
                }
                }
                break;
            case NEW:
                {
                int LA87_8 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 8, input);

                    throw nvae;
                }
                }
                break;
            case QUOTED_IDENTIFIER:
            case IDENTIFIER:
                {
                int LA87_9 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 9, input);

                    throw nvae;
                }
                }
                break;
            case LBRACKET:
                {
                int LA87_10 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 10, input);

                    throw nvae;
                }
                }
                break;
            case INDEXOF:
                {
                int LA87_11 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 11, input);

                    throw nvae;
                }
                }
                break;
            case DOT:
                {
                int LA87_12 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 12, input);

                    throw nvae;
                }
                }
                break;
            case THIS:
                {
                int LA87_13 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 13, input);

                    throw nvae;
                }
                }
                break;
            case SUPER:
                {
                int LA87_14 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 14, input);

                    throw nvae;
                }
                }
                break;
            case QUOTE_LBRACE_STRING_LITERAL:
                {
                int LA87_15 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 15, input);

                    throw nvae;
                }
                }
                break;
            case STRING_LITERAL:
                {
                int LA87_16 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 16, input);

                    throw nvae;
                }
                }
                break;
            case INTEGER_LITERAL:
                {
                int LA87_17 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 17, input);

                    throw nvae;
                }
                }
                break;
            case FLOATING_POINT_LITERAL:
                {
                int LA87_18 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 18, input);

                    throw nvae;
                }
                }
                break;
            case TRUE:
                {
                int LA87_19 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 19, input);

                    throw nvae;
                }
                }
                break;
            case FALSE:
                {
                int LA87_20 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 20, input);

                    throw nvae;
                }
                }
                break;
            case NULL:
                {
                int LA87_21 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 21, input);

                    throw nvae;
                }
                }
                break;
            case POUND:
                {
                int LA87_22 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 22, input);

                    throw nvae;
                }
                }
                break;
            case QUES:
                {
                int LA87_23 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 23, input);

                    throw nvae;
                }
                }
                break;
            case SUB:
                {
                int LA87_24 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 24, input);

                    throw nvae;
                }
                }
                break;
            case NOT:
                {
                int LA87_25 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 25, input);

                    throw nvae;
                }
                }
                break;
            case SIZEOF:
                {
                int LA87_26 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 26, input);

                    throw nvae;
                }
                }
                break;
            case TYPEOF:
                {
                int LA87_27 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 27, input);

                    throw nvae;
                }
                }
                break;
            case REVERSE:
                {
                int LA87_28 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 28, input);

                    throw nvae;
                }
                }
                break;
            case PLUSPLUS:
                {
                int LA87_29 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 29, input);

                    throw nvae;
                }
                }
                break;
            case SUBSUB:
                {
                int LA87_30 = input.LA(2);

                if ( (synpred159()) ) {
                    alt87=1;
                }
                else if ( (synpred160()) ) {
                    alt87=2;
                }
                else if ( (true) ) {
                    alt87=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 30, input);

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
                    new NoViableAltException("518:32: ( generatorClause | dotDotClause | expressionListOpt )", 87, 0, input);

                throw nvae;
            }

            switch (alt87) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:36: generatorClause
                    {
                    pushFollow(FOLLOW_generatorClause_in_bracketExpression6289);
                    generatorClause();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:58: dotDotClause
                    {
                    pushFollow(FOLLOW_dotDotClause_in_bracketExpression6297);
                    dotDotClause();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:77: expressionListOpt
                    {
                    pushFollow(FOLLOW_expressionListOpt_in_bracketExpression6305);
                    expressionListOpt();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,RBRACKET,FOLLOW_RBRACKET_in_bracketExpression6313); if (failed) return ;

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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:519:1: dotDotClause : expression ( COMMA expression )? DOTDOT ( LT )? expression ;
    public final void dotDotClause() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:519:14: ( expression ( COMMA expression )? DOTDOT ( LT )? expression )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:519:16: expression ( COMMA expression )? DOTDOT ( LT )? expression
            {
            pushFollow(FOLLOW_expression_in_dotDotClause6321);
            expression();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:519:29: ( COMMA expression )?
            int alt88=2;
            int LA88_0 = input.LA(1);

            if ( (LA88_0==COMMA) ) {
                alt88=1;
            }
            switch (alt88) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:519:33: COMMA expression
                    {
                    match(input,COMMA,FOLLOW_COMMA_in_dotDotClause6329); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_dotDotClause6333);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,DOTDOT,FOLLOW_DOTDOT_in_dotDotClause6343); if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:519:69: ( LT )?
            int alt89=2;
            int LA89_0 = input.LA(1);

            if ( (LA89_0==LT) ) {
                alt89=1;
            }
            switch (alt89) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: LT
                    {
                    match(input,LT,FOLLOW_LT_in_dotDotClause6347); if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_expression_in_dotDotClause6353);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:520:1: generatorClause : expression BAR generator ( COMMA ( generator | expression ) )* ;
    public final void generatorClause() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:520:17: ( expression BAR generator ( COMMA ( generator | expression ) )* )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:520:19: expression BAR generator ( COMMA ( generator | expression ) )*
            {
            pushFollow(FOLLOW_expression_in_generatorClause6361);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,BAR,FOLLOW_BAR_in_generatorClause6365); if (failed) return ;
            pushFollow(FOLLOW_generator_in_generatorClause6369);
            generator();
            _fsp--;
            if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:520:50: ( COMMA ( generator | expression ) )*
            loop91:
            do {
                int alt91=2;
                int LA91_0 = input.LA(1);

                if ( (LA91_0==COMMA) ) {
                    alt91=1;
                }


                switch (alt91) {
            	case 1 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:520:56: COMMA ( generator | expression )
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_generatorClause6379); if (failed) return ;
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:520:64: ( generator | expression )
            	    int alt90=2;
            	    int LA90_0 = input.LA(1);

            	    if ( (LA90_0==QUOTED_IDENTIFIER||LA90_0==IDENTIFIER) ) {
            	        int LA90_1 = input.LA(2);

            	        if ( (LA90_1==EOF||LA90_1==AND||LA90_1==DUR||LA90_1==IN||(LA90_1>=ORDER && LA90_1<=INSTANCEOF)||LA90_1==OR||LA90_1==LPAREN||LA90_1==LBRACE||(LA90_1>=LBRACKET && LA90_1<=RBRACKET)||(LA90_1>=COMMA && LA90_1<=PERCENTEQ)) ) {
            	            alt90=2;
            	        }
            	        else if ( (LA90_1==LARROW) ) {
            	            alt90=1;
            	        }
            	        else {
            	            if (backtracking>0) {failed=true; return ;}
            	            NoViableAltException nvae =
            	                new NoViableAltException("520:64: ( generator | expression )", 90, 1, input);

            	            throw nvae;
            	        }
            	    }
            	    else if ( ((LA90_0>=POUND && LA90_0<=TYPEOF)||LA90_0==IF||(LA90_0>=THIS && LA90_0<=FALSE)||LA90_0==UNITINTERVAL||LA90_0==FOREACH||(LA90_0>=NOT && LA90_0<=NEW)||(LA90_0>=OPERATION && LA90_0<=FUNCTION)||(LA90_0>=INDEXOF && LA90_0<=SUPER)||(LA90_0>=SIZEOF && LA90_0<=REVERSE)||LA90_0==LPAREN||LA90_0==LBRACKET||LA90_0==DOT||(LA90_0>=PLUSPLUS && LA90_0<=SUBSUB)||(LA90_0>=QUES && LA90_0<=QUOTE_LBRACE_STRING_LITERAL)||LA90_0==INTEGER_LITERAL||LA90_0==FLOATING_POINT_LITERAL) ) {
            	        alt90=2;
            	    }
            	    else {
            	        if (backtracking>0) {failed=true; return ;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("520:64: ( generator | expression )", 90, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt90) {
            	        case 1 :
            	            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:520:68: generator
            	            {
            	            pushFollow(FOLLOW_generator_in_generatorClause6387);
            	            generator();
            	            _fsp--;
            	            if (failed) return ;

            	            }
            	            break;
            	        case 2 :
            	            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:520:84: expression
            	            {
            	            pushFollow(FOLLOW_expression_in_generatorClause6395);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:1: generator : name LARROW expression ;
    public final void generator() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:11: ( name LARROW expression )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:521:13: name LARROW expression
            {
            pushFollow(FOLLOW_name_in_generator6415);
            name();
            _fsp--;
            if (failed) return ;
            match(input,LARROW,FOLLOW_LARROW_in_generator6419); if (failed) return ;
            pushFollow(FOLLOW_expression_in_generator6423);
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:522:1: ordinalExpression : INDEXOF ( name | DOT ) ;
    public final void ordinalExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:522:19: ( INDEXOF ( name | DOT ) )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:522:21: INDEXOF ( name | DOT )
            {
            match(input,INDEXOF,FOLLOW_INDEXOF_in_ordinalExpression6431); if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:522:31: ( name | DOT )
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
                    new NoViableAltException("522:31: ( name | DOT )", 92, 0, input);

                throw nvae;
            }
            switch (alt92) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:522:35: name
                    {
                    pushFollow(FOLLOW_name_in_ordinalExpression6439);
                    name();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:522:46: DOT
                    {
                    match(input,DOT,FOLLOW_DOT_in_ordinalExpression6447); if (failed) return ;

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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:523:1: contextExpression : DOT ;
    public final void contextExpression() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:523:19: ( DOT )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:523:21: DOT
            {
            match(input,DOT,FOLLOW_DOT_in_contextExpression6459); if (failed) return ;

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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:524:1: stringExpression returns [JCExpression expr] : QUOTE_LBRACE_STRING_LITERAL ( FORMAT_STRING_LITERAL )? ( stringExpressionPart )* RBRACE_QUOTE_STRING_LITERAL ;
    public final JCExpression stringExpression() throws RecognitionException {
        JCExpression expr = null;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:2: ( QUOTE_LBRACE_STRING_LITERAL ( FORMAT_STRING_LITERAL )? ( stringExpressionPart )* RBRACE_QUOTE_STRING_LITERAL )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:4: QUOTE_LBRACE_STRING_LITERAL ( FORMAT_STRING_LITERAL )? ( stringExpressionPart )* RBRACE_QUOTE_STRING_LITERAL
            {
            match(input,QUOTE_LBRACE_STRING_LITERAL,FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression6474); if (failed) return expr;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:34: ( FORMAT_STRING_LITERAL )?
            int alt93=2;
            int LA93_0 = input.LA(1);

            if ( (LA93_0==FORMAT_STRING_LITERAL) ) {
                int LA93_1 = input.LA(2);

                if ( (synpred166()) ) {
                    alt93=1;
                }
            }
            switch (alt93) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: FORMAT_STRING_LITERAL
                    {
                    match(input,FORMAT_STRING_LITERAL,FOLLOW_FORMAT_STRING_LITERAL_in_stringExpression6478); if (failed) return expr;

                    }
                    break;

            }

            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:60: ( stringExpressionPart )*
            loop94:
            do {
                int alt94=2;
                int LA94_0 = input.LA(1);

                if ( (LA94_0==FORMAT_STRING_LITERAL) ) {
                    alt94=1;
                }


                switch (alt94) {
            	case 1 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: stringExpressionPart
            	    {
            	    pushFollow(FOLLOW_stringExpressionPart_in_stringExpression6484);
            	    stringExpressionPart();
            	    _fsp--;
            	    if (failed) return expr;

            	    }
            	    break;

            	default :
            	    break loop94;
                }
            } while (true);

            match(input,RBRACE_QUOTE_STRING_LITERAL,FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression6490); if (failed) return expr;

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


    // $ANTLR start stringExpressionPart
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:526:1: stringExpressionPart : FORMAT_STRING_LITERAL ( FORMAT_STRING_LITERAL )? ;
    public final void stringExpressionPart() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:526:22: ( FORMAT_STRING_LITERAL ( FORMAT_STRING_LITERAL )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:526:24: FORMAT_STRING_LITERAL ( FORMAT_STRING_LITERAL )?
            {
            match(input,FORMAT_STRING_LITERAL,FOLLOW_FORMAT_STRING_LITERAL_in_stringExpressionPart6498); if (failed) return ;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:526:48: ( FORMAT_STRING_LITERAL )?
            int alt95=2;
            int LA95_0 = input.LA(1);

            if ( (LA95_0==FORMAT_STRING_LITERAL) ) {
                int LA95_1 = input.LA(2);

                if ( (synpred168()) ) {
                    alt95=1;
                }
            }
            switch (alt95) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:0:0: FORMAT_STRING_LITERAL
                    {
                    match(input,FORMAT_STRING_LITERAL,FOLLOW_FORMAT_STRING_LITERAL_in_stringExpressionPart6502); if (failed) return ;

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
    // $ANTLR end stringExpressionPart


    // $ANTLR start expressionListOpt
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:527:1: expressionListOpt returns [ListBuffer<JCExpression> args = new ListBuffer<JCExpression>()] : (e1= expression ( COMMA e= expression )* )? ;
    public final ListBuffer<JCExpression> expressionListOpt() throws RecognitionException {
        ListBuffer<JCExpression> args =  new ListBuffer<JCExpression>();

        JCExpression e1 = null;

        JCExpression e = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:528:2: ( (e1= expression ( COMMA e= expression )* )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:528:4: (e1= expression ( COMMA e= expression )* )?
            {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:528:4: (e1= expression ( COMMA e= expression )* )?
            int alt97=2;
            int LA97_0 = input.LA(1);

            if ( ((LA97_0>=POUND && LA97_0<=TYPEOF)||LA97_0==IF||(LA97_0>=THIS && LA97_0<=FALSE)||LA97_0==UNITINTERVAL||LA97_0==FOREACH||(LA97_0>=NOT && LA97_0<=NEW)||(LA97_0>=OPERATION && LA97_0<=FUNCTION)||(LA97_0>=INDEXOF && LA97_0<=SUPER)||(LA97_0>=SIZEOF && LA97_0<=REVERSE)||LA97_0==LPAREN||LA97_0==LBRACKET||LA97_0==DOT||(LA97_0>=PLUSPLUS && LA97_0<=SUBSUB)||(LA97_0>=QUES && LA97_0<=QUOTE_LBRACE_STRING_LITERAL)||(LA97_0>=QUOTED_IDENTIFIER && LA97_0<=INTEGER_LITERAL)||LA97_0==FLOATING_POINT_LITERAL||LA97_0==IDENTIFIER) ) {
                alt97=1;
            }
            switch (alt97) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:528:6: e1= expression ( COMMA e= expression )*
                    {
                    pushFollow(FOLLOW_expression_in_expressionListOpt6523);
                    e1=expression();
                    _fsp--;
                    if (failed) return args;
                    if ( backtracking==0 ) {
                       args.append(e1); 
                    }
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:529:6: ( COMMA e= expression )*
                    loop96:
                    do {
                        int alt96=2;
                        int LA96_0 = input.LA(1);

                        if ( (LA96_0==COMMA) ) {
                            alt96=1;
                        }


                        switch (alt96) {
                    	case 1 :
                    	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:529:7: COMMA e= expression
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_expressionListOpt6534); if (failed) return args;
                    	    pushFollow(FOLLOW_expression_in_expressionListOpt6540);
                    	    e=expression();
                    	    _fsp--;
                    	    if (failed) return args;
                    	    if ( backtracking==0 ) {
                    	       args.append(e); 
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop96;
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:530:1: orderBy returns [JCExpression expr] : ORDER BY expression ;
    public final JCExpression orderBy() throws RecognitionException {
        JCExpression expr = null;

        JCExpression expression148 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:531:2: ( ORDER BY expression )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:531:4: ORDER BY expression
            {
            match(input,ORDER,FOLLOW_ORDER_in_orderBy6562); if (failed) return expr;
            match(input,BY,FOLLOW_BY_in_orderBy6566); if (failed) return expr;
            pushFollow(FOLLOW_expression_in_orderBy6570);
            expression148=expression();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = expression148; 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:532:1: indexOn returns [JCExpression expr = null] : INDEX ON name ( COMMA name )* ;
    public final JCExpression indexOn() throws RecognitionException {
        JCExpression expr =  null;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:533:2: ( INDEX ON name ( COMMA name )* )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:533:4: INDEX ON name ( COMMA name )*
            {
            match(input,INDEX,FOLLOW_INDEX_in_indexOn6585); if (failed) return expr;
            match(input,ON,FOLLOW_ON_in_indexOn6589); if (failed) return expr;
            pushFollow(FOLLOW_name_in_indexOn6593);
            name();
            _fsp--;
            if (failed) return expr;
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:533:24: ( COMMA name )*
            loop98:
            do {
                int alt98=2;
                int LA98_0 = input.LA(1);

                if ( (LA98_0==COMMA) ) {
                    int LA98_2 = input.LA(2);

                    if ( (LA98_2==QUOTED_IDENTIFIER||LA98_2==IDENTIFIER) ) {
                        int LA98_3 = input.LA(3);

                        if ( (synpred171()) ) {
                            alt98=1;
                        }


                    }


                }


                switch (alt98) {
            	case 1 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:533:28: COMMA name
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_indexOn6601); if (failed) return expr;
            	    pushFollow(FOLLOW_name_in_indexOn6605);
            	    name();
            	    _fsp--;
            	    if (failed) return expr;

            	    }
            	    break;

            	default :
            	    break loop98;
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:534:1: multiplyOperator : ( STAR | SLASH | PERCENT );
    public final void multiplyOperator() throws RecognitionException {
        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:534:18: ( STAR | SLASH | PERCENT )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:535:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );
    public final int unaryOperator() throws RecognitionException {
        int optag = 0;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:536:2: ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB )
            int alt99=9;
            switch ( input.LA(1) ) {
            case POUND:
                {
                alt99=1;
                }
                break;
            case QUES:
                {
                alt99=2;
                }
                break;
            case SUB:
                {
                alt99=3;
                }
                break;
            case NOT:
                {
                alt99=4;
                }
                break;
            case SIZEOF:
                {
                alt99=5;
                }
                break;
            case TYPEOF:
                {
                alt99=6;
                }
                break;
            case REVERSE:
                {
                alt99=7;
                }
                break;
            case PLUSPLUS:
                {
                alt99=8;
                }
                break;
            case SUBSUB:
                {
                alt99=9;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return optag;}
                NoViableAltException nvae =
                    new NoViableAltException("535:1: unaryOperator returns [int optag] : ( POUND | QUES | SUB | NOT | SIZEOF | TYPEOF | REVERSE | PLUSPLUS | SUBSUB );", 99, 0, input);

                throw nvae;
            }

            switch (alt99) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:536:4: POUND
                    {
                    match(input,POUND,FOLLOW_POUND_in_unaryOperator6649); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:537:4: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_unaryOperator6660); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:538:4: SUB
                    {
                    match(input,SUB,FOLLOW_SUB_in_unaryOperator6673); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.NEG; 
                    }

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:539:4: NOT
                    {
                    match(input,NOT,FOLLOW_NOT_in_unaryOperator6686); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.NOT; 
                    }

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:540:4: SIZEOF
                    {
                    match(input,SIZEOF,FOLLOW_SIZEOF_in_unaryOperator6699); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:541:4: TYPEOF
                    {
                    match(input,TYPEOF,FOLLOW_TYPEOF_in_unaryOperator6712); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 7 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:542:4: REVERSE
                    {
                    match(input,REVERSE,FOLLOW_REVERSE_in_unaryOperator6725); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 8 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:543:4: PLUSPLUS
                    {
                    match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_unaryOperator6738); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = 0; 
                    }

                    }
                    break;
                case 9 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:544:4: SUBSUB
                    {
                    match(input,SUBSUB,FOLLOW_SUBSUB_in_unaryOperator6751); if (failed) return optag;
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:546:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );
    public final int assignmentOperator() throws RecognitionException {
        int optag = 0;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:547:2: ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ )
            int alt100=5;
            switch ( input.LA(1) ) {
            case PLUSEQ:
                {
                alt100=1;
                }
                break;
            case SUBEQ:
                {
                alt100=2;
                }
                break;
            case STAREQ:
                {
                alt100=3;
                }
                break;
            case SLASHEQ:
                {
                alt100=4;
                }
                break;
            case PERCENTEQ:
                {
                alt100=5;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return optag;}
                NoViableAltException nvae =
                    new NoViableAltException("546:1: assignmentOperator returns [int optag] : ( PLUSEQ | SUBEQ | STAREQ | SLASHEQ | PERCENTEQ );", 100, 0, input);

                throw nvae;
            }

            switch (alt100) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:547:4: PLUSEQ
                    {
                    match(input,PLUSEQ,FOLLOW_PLUSEQ_in_assignmentOperator6772); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.PLUS_ASG; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:548:4: SUBEQ
                    {
                    match(input,SUBEQ,FOLLOW_SUBEQ_in_assignmentOperator6785); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.MINUS_ASG; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:549:4: STAREQ
                    {
                    match(input,STAREQ,FOLLOW_STAREQ_in_assignmentOperator6798); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.MUL_ASG; 
                    }

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:550:4: SLASHEQ
                    {
                    match(input,SLASHEQ,FOLLOW_SLASHEQ_in_assignmentOperator6811); if (failed) return optag;
                    if ( backtracking==0 ) {
                       optag = JCTree.DIV_ASG; 
                    }

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:551:4: PERCENTEQ
                    {
                    match(input,PERCENTEQ,FOLLOW_PERCENTEQ_in_assignmentOperator6824); if (failed) return optag;
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:553:1: typeReference returns [JFXType type] : ( COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? ;
    public final JFXType typeReference() throws RecognitionException {
        JFXType type = null;

        int ccf = 0;

        int ccn = 0;

        int ccs = 0;

        ListBuffer<JCTree> formalParameters149 = null;

        Name name150 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:554:2: ( ( COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )? )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:554:4: ( COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:554:4: ( COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint ) )?
            int alt103=2;
            int LA103_0 = input.LA(1);

            if ( (LA103_0==COLON) ) {
                alt103=1;
            }
            switch (alt103) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:554:6: COLON ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    {
                    match(input,COLON,FOLLOW_COLON_in_typeReference6848); if (failed) return type;
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:554:13: ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )
                    int alt102=3;
                    switch ( input.LA(1) ) {
                    case OPERATION:
                    case FUNCTION:
                    case LPAREN:
                        {
                        alt102=1;
                        }
                        break;
                    case QUOTED_IDENTIFIER:
                    case IDENTIFIER:
                        {
                        alt102=2;
                        }
                        break;
                    case STAR:
                        {
                        alt102=3;
                        }
                        break;
                    default:
                        if (backtracking>0) {failed=true; return type;}
                        NoViableAltException nvae =
                            new NoViableAltException("554:13: ( ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint ) | name ccn= cardinalityConstraint | STAR ccs= cardinalityConstraint )", 102, 0, input);

                        throw nvae;
                    }

                    switch (alt102) {
                        case 1 :
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:555:22: ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint )
                            {
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:555:22: ( options {backtrack=true; } : ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint )
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:555:55: ( FUNCTION | OPERATION )? formalParameters typeReference ccf= cardinalityConstraint
                            {
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:555:55: ( FUNCTION | OPERATION )?
                            int alt101=2;
                            int LA101_0 = input.LA(1);

                            if ( ((LA101_0>=OPERATION && LA101_0<=FUNCTION)) ) {
                                alt101=1;
                            }
                            switch (alt101) {
                                case 1 :
                                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:
                                    {
                                    if ( (input.LA(1)>=OPERATION && input.LA(1)<=FUNCTION) ) {
                                        input.consume();
                                        errorRecovery=false;failed=false;
                                    }
                                    else {
                                        if (backtracking>0) {failed=true; return type;}
                                        MismatchedSetException mse =
                                            new MismatchedSetException(null,input);
                                        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_typeReference6888);    throw mse;
                                    }


                                    }
                                    break;

                            }

                            pushFollow(FOLLOW_formalParameters_in_typeReference6897);
                            formalParameters149=formalParameters();
                            _fsp--;
                            if (failed) return type;
                            pushFollow(FOLLOW_typeReference_in_typeReference6899);
                            typeReference();
                            _fsp--;
                            if (failed) return type;
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference6903);
                            ccf=cardinalityConstraint();
                            _fsp--;
                            if (failed) return type;

                            }

                            if ( backtracking==0 ) {
                               type = F.TypeFunctional(formalParameters149.toList(), 
                                                                                                                     type, ccf); 
                            }

                            }
                            break;
                        case 2 :
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:558:22: name ccn= cardinalityConstraint
                            {
                            pushFollow(FOLLOW_name_in_typeReference6958);
                            name150=name();
                            _fsp--;
                            if (failed) return type;
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference6962);
                            ccn=cardinalityConstraint();
                            _fsp--;
                            if (failed) return type;
                            if ( backtracking==0 ) {
                               type = F.TypeClass(name150, ccn); 
                            }

                            }
                            break;
                        case 3 :
                            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:559:22: STAR ccs= cardinalityConstraint
                            {
                            match(input,STAR,FOLLOW_STAR_in_typeReference6988); if (failed) return type;
                            pushFollow(FOLLOW_cardinalityConstraint_in_typeReference6992);
                            ccs=cardinalityConstraint();
                            _fsp--;
                            if (failed) return type;
                            if ( backtracking==0 ) {
                               type = F.TypeAny(ccs); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:561:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );
    public final int cardinalityConstraint() throws RecognitionException {
        int ary = 0;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:562:2: ( LBRACKET RBRACKET | QUES | PLUS | STAR | )
            int alt104=5;
            switch ( input.LA(1) ) {
            case LBRACKET:
                {
                int LA104_1 = input.LA(2);

                if ( (synpred191()) ) {
                    alt104=1;
                }
                else if ( (true) ) {
                    alt104=5;
                }
                else {
                    if (backtracking>0) {failed=true; return ary;}
                    NoViableAltException nvae =
                        new NoViableAltException("561:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 104, 1, input);

                    throw nvae;
                }
                }
                break;
            case QUES:
                {
                int LA104_2 = input.LA(2);

                if ( (synpred192()) ) {
                    alt104=2;
                }
                else if ( (true) ) {
                    alt104=5;
                }
                else {
                    if (backtracking>0) {failed=true; return ary;}
                    NoViableAltException nvae =
                        new NoViableAltException("561:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 104, 2, input);

                    throw nvae;
                }
                }
                break;
            case PLUS:
                {
                int LA104_3 = input.LA(2);

                if ( (synpred193()) ) {
                    alt104=3;
                }
                else if ( (true) ) {
                    alt104=5;
                }
                else {
                    if (backtracking>0) {failed=true; return ary;}
                    NoViableAltException nvae =
                        new NoViableAltException("561:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 104, 3, input);

                    throw nvae;
                }
                }
                break;
            case STAR:
                {
                int LA104_4 = input.LA(2);

                if ( (synpred194()) ) {
                    alt104=4;
                }
                else if ( (true) ) {
                    alt104=5;
                }
                else {
                    if (backtracking>0) {failed=true; return ary;}
                    NoViableAltException nvae =
                        new NoViableAltException("561:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 104, 4, input);

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
            case LBRACE:
            case SEMI:
            case COMMA:
            case EQ:
                {
                alt104=5;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ary;}
                NoViableAltException nvae =
                    new NoViableAltException("561:1: cardinalityConstraint returns [int ary] : ( LBRACKET RBRACKET | QUES | PLUS | STAR | );", 104, 0, input);

                throw nvae;
            }

            switch (alt104) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:562:5: LBRACKET RBRACKET
                    {
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_cardinalityConstraint7023); if (failed) return ary;
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_cardinalityConstraint7027); if (failed) return ary;
                    if ( backtracking==0 ) {
                       ary = JFXType.CARDINALITY_ANY; 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:563:5: QUES
                    {
                    match(input,QUES,FOLLOW_QUES_in_cardinalityConstraint7039); if (failed) return ary;
                    if ( backtracking==0 ) {
                       ary = JFXType.CARDINALITY_OPTIONAL; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:564:5: PLUS
                    {
                    match(input,PLUS,FOLLOW_PLUS_in_cardinalityConstraint7066); if (failed) return ary;
                    if ( backtracking==0 ) {
                       ary = JFXType.CARDINALITY_ANY; 
                    }

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:565:5: STAR
                    {
                    match(input,STAR,FOLLOW_STAR_in_cardinalityConstraint7093); if (failed) return ary;
                    if ( backtracking==0 ) {
                       ary = JFXType.CARDINALITY_ANY; 
                    }

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:566:29: 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:568:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );
    public final JCExpression literal() throws RecognitionException {
        JCExpression expr = null;

        Token t=null;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:569:2: (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL )
            int alt105=6;
            switch ( input.LA(1) ) {
            case STRING_LITERAL:
                {
                alt105=1;
                }
                break;
            case INTEGER_LITERAL:
                {
                alt105=2;
                }
                break;
            case FLOATING_POINT_LITERAL:
                {
                alt105=3;
                }
                break;
            case TRUE:
                {
                alt105=4;
                }
                break;
            case FALSE:
                {
                alt105=5;
                }
                break;
            case NULL:
                {
                alt105=6;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return expr;}
                NoViableAltException nvae =
                    new NoViableAltException("568:1: literal returns [JCExpression expr] : (t= STRING_LITERAL | t= INTEGER_LITERAL | t= FLOATING_POINT_LITERAL | t= TRUE | t= FALSE | t= NULL );", 105, 0, input);

                throw nvae;
            }

            switch (alt105) {
                case 1 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:569:4: t= STRING_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_literal7162); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(t.getTokenIndex()).Literal(TypeTags.CLASS, t.getText()); 
                    }

                    }
                    break;
                case 2 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:570:4: t= INTEGER_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,INTEGER_LITERAL,FOLLOW_INTEGER_LITERAL_in_literal7172); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(t.getTokenIndex()).Literal(TypeTags.INT, Convert.string2int(t.getText(), 10)); 
                    }

                    }
                    break;
                case 3 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:571:4: t= FLOATING_POINT_LITERAL
                    {
                    t=(Token)input.LT(1);
                    match(input,FLOATING_POINT_LITERAL,FOLLOW_FLOATING_POINT_LITERAL_in_literal7182); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(t.getTokenIndex()).Literal(TypeTags.DOUBLE, Double.valueOf(t.getText())); 
                    }

                    }
                    break;
                case 4 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:572:4: t= TRUE
                    {
                    t=(Token)input.LT(1);
                    match(input,TRUE,FOLLOW_TRUE_in_literal7192); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(t.getTokenIndex()).Literal(TypeTags.BOOLEAN, 1); 
                    }

                    }
                    break;
                case 5 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:573:4: t= FALSE
                    {
                    t=(Token)input.LT(1);
                    match(input,FALSE,FOLLOW_FALSE_in_literal7206); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(t.getTokenIndex()).Literal(TypeTags.BOOLEAN, 0); 
                    }

                    }
                    break;
                case 6 :
                    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:574:4: t= NULL
                    {
                    t=(Token)input.LT(1);
                    match(input,NULL,FOLLOW_NULL_in_literal7220); if (failed) return expr;
                    if ( backtracking==0 ) {
                       expr = F.at(t.getTokenIndex()).Literal(TypeTags.BOT, null); 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:576:1: typeName returns [JCExpression expr] : qualident ;
    public final JCExpression typeName() throws RecognitionException {
        JCExpression expr = null;

        JCExpression qualident151 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:577:8: ( qualident )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:577:10: qualident
            {
            pushFollow(FOLLOW_qualident_in_typeName7247);
            qualident151=qualident();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = qualident151; 
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:579:1: qualident returns [JCExpression expr] : identifier ( DOT name )* ;
    public final JCExpression qualident() throws RecognitionException {
        JCExpression expr = null;

        JCIdent identifier152 = null;

        Name name153 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:580:8: ( identifier ( DOT name )* )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:580:10: identifier ( DOT name )*
            {
            pushFollow(FOLLOW_identifier_in_qualident7289);
            identifier152=identifier();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = identifier152; 
            }
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:581:10: ( DOT name )*
            loop106:
            do {
                int alt106=2;
                int LA106_0 = input.LA(1);

                if ( (LA106_0==DOT) ) {
                    alt106=1;
                }


                switch (alt106) {
            	case 1 :
            	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:581:12: DOT name
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_qualident7317); if (failed) return expr;
            	    pushFollow(FOLLOW_name_in_qualident7319);
            	    name153=name();
            	    _fsp--;
            	    if (failed) return expr;
            	    if ( backtracking==0 ) {
            	       expr = F.Select(expr, name153); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop106;
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
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:583:1: identifier returns [JCIdent expr] : name ;
    public final JCIdent identifier() throws RecognitionException {
        JCIdent expr = null;

        Name name154 = null;


        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:584:2: ( name )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:584:4: name
            {
            pushFollow(FOLLOW_name_in_identifier7356);
            name154=name();
            _fsp--;
            if (failed) return expr;
            if ( backtracking==0 ) {
               expr = F.Ident(name154); 
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


    // $ANTLR start name
    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:586:1: name returns [Name value] : tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) ;
    public final Name name() throws RecognitionException {
        Name value = null;

        Token tokid=null;

        try {
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:587:2: (tokid= ( QUOTED_IDENTIFIER | IDENTIFIER ) )
            // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:587:4: tokid= ( QUOTED_IDENTIFIER | IDENTIFIER )
            {
            tokid=(Token)input.LT(1);
            if ( input.LA(1)==QUOTED_IDENTIFIER||input.LA(1)==IDENTIFIER ) {
                input.consume();
                errorRecovery=false;failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return value;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_name7390);    throw mse;
            }

            if ( backtracking==0 ) {
               value = Name.fromString(names, tokid.getText()); 
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
    // $ANTLR end name

    // $ANTLR start synpred45
    public final void synpred45_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:345:10: ( backgroundStatement )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:345:10: backgroundStatement
        {
        pushFollow(FOLLOW_backgroundStatement_in_synpred453251);
        backgroundStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred45

    // $ANTLR start synpred46
    public final void synpred46_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:346:10: ( laterStatement )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:346:10: laterStatement
        {
        pushFollow(FOLLOW_laterStatement_in_synpred463266);
        laterStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred46

    // $ANTLR start synpred48
    public final void synpred48_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:348:10: ( ifStatement )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:348:10: ifStatement
        {
        pushFollow(FOLLOW_ifStatement_in_synpred483302);
        ifStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred48

    // $ANTLR start synpred51
    public final void synpred51_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:351:4: ( expression SEMI )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:351:4: expression SEMI
        {
        pushFollow(FOLLOW_expression_in_synpred513345);
        expression();
        _fsp--;
        if (failed) return ;
        match(input,SEMI,FOLLOW_SEMI_in_synpred513349); if (failed) return ;

        }
    }
    // $ANTLR end synpred51

    // $ANTLR start synpred56
    public final void synpred56_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:356:10: ( forAlphaStatement )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:356:10: forAlphaStatement
        {
        pushFollow(FOLLOW_forAlphaStatement_in_synpred563426);
        forAlphaStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred56

    // $ANTLR start synpred57
    public final void synpred57_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:357:10: ( forJoinStatement )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:357:10: forJoinStatement
        {
        pushFollow(FOLLOW_forJoinStatement_in_synpred573442);
        forJoinStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred57

    // $ANTLR start synpred67
    public final void synpred67_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:390:54: ( ELSE statement )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:390:54: ELSE statement
        {
        match(input,ELSE,FOLLOW_ELSE_in_synpred673835); if (failed) return ;
        pushFollow(FOLLOW_statement_in_synpred673840);
        statement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred67

    // $ANTLR start synpred78
    public final void synpred78_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:62: ( FPS expression )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:62: FPS expression
        {
        match(input,FPS,FOLLOW_FPS_in_synpred784298); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred784302);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred78

    // $ANTLR start synpred79
    public final void synpred79_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:91: ( WHILE expression )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:91: WHILE expression
        {
        match(input,WHILE,FOLLOW_WHILE_in_synpred794316); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred794320);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred79

    // $ANTLR start synpred80
    public final void synpred80_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:122: ( CONTINUE IF expression )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:411:122: CONTINUE IF expression
        {
        match(input,CONTINUE,FOLLOW_CONTINUE_in_synpred804334); if (failed) return ;
        match(input,IF,FOLLOW_IF_in_synpred804338); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred804342);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred80

    // $ANTLR start synpred84
    public final void synpred84_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:36: ( LINEAR )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:36: LINEAR
        {
        match(input,LINEAR,FOLLOW_LINEAR_in_synpred844481); if (failed) return ;

        }
    }
    // $ANTLR end synpred84

    // $ANTLR start synpred85
    public final void synpred85_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:49: ( EASEIN )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:49: EASEIN
        {
        match(input,EASEIN,FOLLOW_EASEIN_in_synpred854489); if (failed) return ;

        }
    }
    // $ANTLR end synpred85

    // $ANTLR start synpred86
    public final void synpred86_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:62: ( EASEOUT )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:62: EASEOUT
        {
        match(input,EASEOUT,FOLLOW_EASEOUT_in_synpred864497); if (failed) return ;

        }
    }
    // $ANTLR end synpred86

    // $ANTLR start synpred87
    public final void synpred87_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:76: ( EASEBOTH )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:76: EASEBOTH
        {
        match(input,EASEBOTH,FOLLOW_EASEBOTH_in_synpred874505); if (failed) return ;

        }
    }
    // $ANTLR end synpred87

    // $ANTLR start synpred88
    public final void synpred88_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:91: ( MOTION expression )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:91: MOTION expression
        {
        match(input,MOTION,FOLLOW_MOTION_in_synpred884513); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred884517);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred88

    // $ANTLR start synpred89
    public final void synpred89_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:123: ( FPS expression )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:123: FPS expression
        {
        match(input,FPS,FOLLOW_FPS_in_synpred894531); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred894535);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred89

    // $ANTLR start synpred90
    public final void synpred90_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:150: ( WHILE expression )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:150: WHILE expression
        {
        match(input,WHILE,FOLLOW_WHILE_in_synpred904547); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred904551);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred90

    // $ANTLR start synpred91
    public final void synpred91_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:181: ( CONTINUE IF expression )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:415:181: CONTINUE IF expression
        {
        match(input,CONTINUE,FOLLOW_CONTINUE_in_synpred914565); if (failed) return ;
        match(input,IF,FOLLOW_IF_in_synpred914569); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred914573);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred91

    // $ANTLR start synpred95
    public final void synpred95_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:418:39: ( typeReference )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:418:39: typeReference
        {
        pushFollow(FOLLOW_typeReference_in_synpred954664);
        typeReference();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred95

    // $ANTLR start synpred106
    public final void synpred106_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:429:52: ( typeReference )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:429:52: typeReference
        {
        pushFollow(FOLLOW_typeReference_in_synpred1064911);
        typeReference();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred106

    // $ANTLR start synpred107
    public final void synpred107_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:430:54: ( typeReference )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:430:54: typeReference
        {
        pushFollow(FOLLOW_typeReference_in_synpred1074933);
        typeReference();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred107

    // $ANTLR start synpred109
    public final void synpred109_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:83: ( COMMA selectionVar )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:83: COMMA selectionVar
        {
        match(input,COMMA,FOLLOW_COMMA_in_synpred1095003); if (failed) return ;
        pushFollow(FOLLOW_selectionVar_in_synpred1095007);
        selectionVar();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred109

    // $ANTLR start synpred110
    public final void synpred110_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:116: ( WHERE expression )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:432:116: WHERE expression
        {
        match(input,WHERE,FOLLOW_WHERE_in_synpred1105021); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred1105025);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred110

    // $ANTLR start synpred141
    public final void synpred141_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:489:4: ( identifier LBRACE objectLiteral RBRACE )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:489:4: identifier LBRACE objectLiteral RBRACE
        {
        pushFollow(FOLLOW_identifier_in_synpred1415884);
        identifier();
        _fsp--;
        if (failed) return ;
        match(input,LBRACE,FOLLOW_LBRACE_in_synpred1415886); if (failed) return ;
        pushFollow(FOLLOW_objectLiteral_in_synpred1415889);
        objectLiteral();
        _fsp--;
        if (failed) return ;
        match(input,RBRACE,FOLLOW_RBRACE_in_synpred1415891); if (failed) return ;

        }
    }
    // $ANTLR end synpred141

    // $ANTLR start synpred148
    public final void synpred148_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:496:10: ( identifier ( LPAREN expressionListOpt RPAREN )* )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:496:10: identifier ( LPAREN expressionListOpt RPAREN )*
        {
        pushFollow(FOLLOW_identifier_in_synpred1485989);
        identifier();
        _fsp--;
        if (failed) return ;
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:497:10: ( LPAREN expressionListOpt RPAREN )*
        loop120:
        do {
            int alt120=2;
            int LA120_0 = input.LA(1);

            if ( (LA120_0==LPAREN) ) {
                alt120=1;
            }


            switch (alt120) {
        	case 1 :
        	    // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:497:12: LPAREN expressionListOpt RPAREN
        	    {
        	    match(input,LPAREN,FOLLOW_LPAREN_in_synpred1486010); if (failed) return ;
        	    pushFollow(FOLLOW_expressionListOpt_in_synpred1486014);
        	    expressionListOpt();
        	    _fsp--;
        	    if (failed) return ;
        	    match(input,RPAREN,FOLLOW_RPAREN_in_synpred1486018); if (failed) return ;

        	    }
        	    break;

        	default :
        	    break loop120;
            }
        } while (true);


        }
    }
    // $ANTLR end synpred148

    // $ANTLR start synpred159
    public final void synpred159_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:36: ( generatorClause )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:36: generatorClause
        {
        pushFollow(FOLLOW_generatorClause_in_synpred1596289);
        generatorClause();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred159

    // $ANTLR start synpred160
    public final void synpred160_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:58: ( dotDotClause )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:518:58: dotDotClause
        {
        pushFollow(FOLLOW_dotDotClause_in_synpred1606297);
        dotDotClause();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred160

    // $ANTLR start synpred166
    public final void synpred166_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:34: ( FORMAT_STRING_LITERAL )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:525:34: FORMAT_STRING_LITERAL
        {
        match(input,FORMAT_STRING_LITERAL,FOLLOW_FORMAT_STRING_LITERAL_in_synpred1666478); if (failed) return ;

        }
    }
    // $ANTLR end synpred166

    // $ANTLR start synpred168
    public final void synpred168_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:526:48: ( FORMAT_STRING_LITERAL )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:526:48: FORMAT_STRING_LITERAL
        {
        match(input,FORMAT_STRING_LITERAL,FOLLOW_FORMAT_STRING_LITERAL_in_synpred1686502); if (failed) return ;

        }
    }
    // $ANTLR end synpred168

    // $ANTLR start synpred171
    public final void synpred171_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:533:28: ( COMMA name )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:533:28: COMMA name
        {
        match(input,COMMA,FOLLOW_COMMA_in_synpred1716601); if (failed) return ;
        pushFollow(FOLLOW_name_in_synpred1716605);
        name();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred171

    // $ANTLR start synpred191
    public final void synpred191_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:562:5: ( LBRACKET RBRACKET )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:562:5: LBRACKET RBRACKET
        {
        match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred1917023); if (failed) return ;
        match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred1917027); if (failed) return ;

        }
    }
    // $ANTLR end synpred191

    // $ANTLR start synpred192
    public final void synpred192_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:563:5: ( QUES )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:563:5: QUES
        {
        match(input,QUES,FOLLOW_QUES_in_synpred1927039); if (failed) return ;

        }
    }
    // $ANTLR end synpred192

    // $ANTLR start synpred193
    public final void synpred193_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:564:5: ( PLUS )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:564:5: PLUS
        {
        match(input,PLUS,FOLLOW_PLUS_in_synpred1937066); if (failed) return ;

        }
    }
    // $ANTLR end synpred193

    // $ANTLR start synpred194
    public final void synpred194_fragment() throws RecognitionException {   
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:565:5: ( STAR )
        // C:\\JavaFX\\opensvn\\trunk\\src\\share\\classes\\com\\sun\\tools\\javafx\\antlr\\v1.g:565:5: STAR
        {
        match(input,STAR,FOLLOW_STAR_in_synpred1947093); if (failed) return ;

        }
    }
    // $ANTLR end synpred194

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
    public final boolean synpred148() {
        backtracking++;
        int start = input.mark();
        try {
            synpred148_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred84() {
        backtracking++;
        int start = input.mark();
        try {
            synpred84_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred85() {
        backtracking++;
        int start = input.mark();
        try {
            synpred85_fragment(); // can never throw exception
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
    public final boolean synpred67() {
        backtracking++;
        int start = input.mark();
        try {
            synpred67_fragment(); // can never throw exception
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
    public final boolean synpred107() {
        backtracking++;
        int start = input.mark();
        try {
            synpred107_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred106() {
        backtracking++;
        int start = input.mark();
        try {
            synpred106_fragment(); // can never throw exception
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
    public final boolean synpred160() {
        backtracking++;
        int start = input.mark();
        try {
            synpred160_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred168() {
        backtracking++;
        int start = input.mark();
        try {
            synpred168_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred166() {
        backtracking++;
        int start = input.mark();
        try {
            synpred166_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred95() {
        backtracking++;
        int start = input.mark();
        try {
            synpred95_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred57() {
        backtracking++;
        int start = input.mark();
        try {
            synpred57_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred56() {
        backtracking++;
        int start = input.mark();
        try {
            synpred56_fragment(); // can never throw exception
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
    public final boolean synpred78() {
        backtracking++;
        int start = input.mark();
        try {
            synpred78_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred79() {
        backtracking++;
        int start = input.mark();
        try {
            synpred79_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred110() {
        backtracking++;
        int start = input.mark();
        try {
            synpred110_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred51() {
        backtracking++;
        int start = input.mark();
        try {
            synpred51_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred171() {
        backtracking++;
        int start = input.mark();
        try {
            synpred171_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred159() {
        backtracking++;
        int start = input.mark();
        try {
            synpred159_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred191() {
        backtracking++;
        int start = input.mark();
        try {
            synpred191_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred192() {
        backtracking++;
        int start = input.mark();
        try {
            synpred192_fragment(); // can never throw exception
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


 

    public static final BitSet FOLLOW_packageDecl_in_module1681 = new BitSet(new long[]{0x499F914B81644260L,0x2C7001C0222DC0FFL,0x0000000000000001L});
    public static final BitSet FOLLOW_moduleItems_in_module1684 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_module1686 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PACKAGE_in_packageDecl1719 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_qualident_in_packageDecl1721 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_packageDecl1723 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_moduleItem_in_moduleItems1752 = new BitSet(new long[]{0x499F914B81644262L,0x2C7001C0222DC0FFL,0x0000000000000001L});
    public static final BitSet FOLLOW_importDecl_in_moduleItem1794 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDefinition_in_moduleItem1809 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeDefinition_in_moduleItem1824 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberOperationDefinition_in_moduleItem1839 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberFunctionDefinition_in_moduleItem1853 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRIGGER_in_moduleItem1867 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ON_in_moduleItem1869 = new BitSet(new long[]{0x0000000000000000L,0x0400000000200000L,0x0000000000000001L});
    public static final BitSet FOLLOW_changeRule_in_moduleItem1871 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statementExcept_in_moduleItem1885 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importDecl1914 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_identifier_in_importDecl1917 = new BitSet(new long[]{0x0000000000000000L,0x0000000028000000L});
    public static final BitSet FOLLOW_DOT_in_importDecl1941 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_importDecl1943 = new BitSet(new long[]{0x0000000000000000L,0x0000000028000000L});
    public static final BitSet FOLLOW_DOT_in_importDecl1971 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
    public static final BitSet FOLLOW_STAR_in_importDecl1973 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_importDecl1981 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_classDefinition2007 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_CLASS_in_classDefinition2010 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_classDefinition2012 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800400L});
    public static final BitSet FOLLOW_supers_in_classDefinition2014 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_LBRACE_in_classDefinition2016 = new BitSet(new long[]{0x0000000000004200L,0x000000000100009CL});
    public static final BitSet FOLLOW_classMembers_in_classDefinition2018 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACE_in_classDefinition2020 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXTENDS_in_supers2040 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_supers2044 = new BitSet(new long[]{0x0000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_supers2067 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_supers2071 = new BitSet(new long[]{0x0000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_attributeDecl_in_classMembers2100 = new BitSet(new long[]{0x0000000000004202L,0x000000000000009CL});
    public static final BitSet FOLLOW_functionDecl_in_classMembers2123 = new BitSet(new long[]{0x0000000000004202L,0x000000000000009CL});
    public static final BitSet FOLLOW_operationDecl_in_classMembers2147 = new BitSet(new long[]{0x0000000000004202L,0x000000000000009CL});
    public static final BitSet FOLLOW_modifierFlags_in_attributeDecl2184 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeDecl2186 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_attributeDecl2188 = new BitSet(new long[]{0x0000000000000000L,0x0008000008001900L});
    public static final BitSet FOLLOW_typeReference_in_attributeDecl2190 = new BitSet(new long[]{0x0000000000000000L,0x0000000008001900L});
    public static final BitSet FOLLOW_inverseClause_in_attributeDecl2192 = new BitSet(new long[]{0x0000000000000000L,0x0000000008001800L});
    public static final BitSet FOLLOW_orderBy_in_attributeDecl2196 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_indexOn_in_attributeDecl2200 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_attributeDecl2204 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INVERSE_in_inverseClause2223 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_memberSelector_in_inverseClause2225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_functionDecl2243 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_FUNCTION_in_functionDecl2245 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_functionDecl2247 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_functionDecl2249 = new BitSet(new long[]{0x0000000000000000L,0x0008000008000000L});
    public static final BitSet FOLLOW_typeReference_in_functionDecl2251 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_functionDecl2253 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifierFlags_in_operationDecl2272 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_OPERATION_in_operationDecl2276 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_operationDecl2280 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_operationDecl2284 = new BitSet(new long[]{0x0000000000000000L,0x0008000008000000L});
    public static final BitSet FOLLOW_typeReference_in_operationDecl2288 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_operationDecl2293 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeDefinition2312 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_memberSelector_in_attributeDefinition2316 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_EQ_in_attributeDefinition2320 = new BitSet(new long[]{0x4017900060010060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_bindOpt_in_attributeDefinition2322 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_attributeDefinition2325 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_attributeDefinition2329 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATION_in_memberOperationDefinition2348 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_memberSelector_in_memberOperationDefinition2352 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_memberOperationDefinition2356 = new BitSet(new long[]{0x0000000000000000L,0x0008000000800000L});
    public static final BitSet FOLLOW_typeReference_in_memberOperationDefinition2360 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_memberOperationDefinition2363 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_memberFunctionDefinition2382 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_memberSelector_in_memberFunctionDefinition2386 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_memberFunctionDefinition2390 = new BitSet(new long[]{0x0000000000000000L,0x0008000000800000L});
    public static final BitSet FOLLOW_typeReference_in_memberFunctionDefinition2394 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_memberFunctionDefinition2397 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQ_in_functionBody2413 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_functionBody2417 = new BitSet(new long[]{0x8000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_whereVarDecls_in_functionBody2421 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_functionBody2427 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_functionBody2443 = new BitSet(new long[]{0x0000000280000000L,0x0400000000000060L,0x0000000000000001L});
    public static final BitSet FOLLOW_variableDefinition_in_functionBody2451 = new BitSet(new long[]{0x0000000280000000L,0x0400000000000060L,0x0000000000000001L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_functionBody2459 = new BitSet(new long[]{0x0000000280000000L,0x0400000000000060L,0x0000000000000001L});
    public static final BitSet FOLLOW_localOperationDefinition_in_functionBody2467 = new BitSet(new long[]{0x0000000280000000L,0x0400000000000060L,0x0000000000000001L});
    public static final BitSet FOLLOW_RETURN_in_functionBody2477 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_functionBody2481 = new BitSet(new long[]{0x0000000000000000L,0x0000000009000000L});
    public static final BitSet FOLLOW_SEMI_in_functionBody2485 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACE_in_functionBody2491 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_whereVarDecls2499 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000040L,0x0000000000000001L});
    public static final BitSet FOLLOW_whereVarDecl_in_whereVarDecls2503 = new BitSet(new long[]{0x0000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_whereVarDecls2511 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000040L,0x0000000000000001L});
    public static final BitSet FOLLOW_whereVarDecl_in_whereVarDecls2515 = new BitSet(new long[]{0x0000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_whereVarDecl2529 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_whereVarDecl2541 = new BitSet(new long[]{0x0000000000000000L,0x0008000080000000L});
    public static final BitSet FOLLOW_typeReference_in_whereVarDecl2545 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_EQ_in_whereVarDecl2549 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_whereVarDecl2553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variableDefinition2561 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_variableDefinition2565 = new BitSet(new long[]{0x0000000000000000L,0x0008000080000000L});
    public static final BitSet FOLLOW_typeReference_in_variableDefinition2569 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_EQ_in_variableDefinition2572 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_variableDefinition2576 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_variableDefinition2580 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule2594 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_changeRule2598 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_identifier_in_changeRule2602 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule2605 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_changeRule2608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule2624 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule2628 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_EQ_in_changeRule2631 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_identifier_in_changeRule2633 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule2637 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_changeRule2640 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule2654 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_EQ_in_changeRule2657 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_identifier_in_changeRule2659 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_changeRule2661 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule2675 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule2679 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_LBRACKET_in_changeRule2683 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_identifier_in_changeRule2689 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RBRACKET_in_changeRule2693 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_EQ_in_changeRule2697 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_identifier_in_changeRule2701 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule2705 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_changeRule2708 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule2722 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_INSERT_in_changeRule2726 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_identifier_in_changeRule2730 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_INTO_in_changeRule2734 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule2738 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule2742 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_changeRule2744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule2751 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_DELETE_in_changeRule2755 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_identifier_in_changeRule2759 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_FROM_in_changeRule2763 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule2767 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule2771 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_changeRule2773 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_changeRule2780 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_DELETE_in_changeRule2784 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_memberSelector_in_changeRule2787 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_LBRACKET_in_changeRule2791 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_identifier_in_changeRule2795 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RBRACKET_in_changeRule2799 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_changeRule2803 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_changeRule2805 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags2828 = new BitSet(new long[]{0x0000000000000002L,0x000000000000001CL});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags2841 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_accessModifier_in_modifierFlags2863 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000080L});
    public static final BitSet FOLLOW_otherModifier_in_modifierFlags2876 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_accessModifier2924 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PRIVATE_in_accessModifier2941 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROTECTED_in_accessModifier2957 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ABSTRACT_in_otherModifier2981 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_READONLY_in_otherModifier2996 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_memberSelector3022 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_DOT_in_memberSelector3026 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_memberSelector3032 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_formalParameters3048 = new BitSet(new long[]{0x0000000000000000L,0x0400000000400000L,0x0000000000000001L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters3056 = new BitSet(new long[]{0x0000000000000000L,0x0000000010400000L});
    public static final BitSet FOLLOW_COMMA_in_formalParameters3075 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_formalParameter_in_formalParameters3081 = new BitSet(new long[]{0x0000000000000000L,0x0000000010400000L});
    public static final BitSet FOLLOW_RPAREN_in_formalParameters3092 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_formalParameter3105 = new BitSet(new long[]{0x0000000000000002L,0x0008000000000000L});
    public static final BitSet FOLLOW_typeReference_in_formalParameter3107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_block3124 = new BitSet(new long[]{0x499F914381440060L,0x2C7001C0232DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_statements_in_block3128 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACE_in_block3132 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_statements3150 = new BitSet(new long[]{0x499F914381440062L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_statementExcept_in_statement3201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localTriggerStatement_in_statement3217 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclaration_in_statementExcept3235 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_backgroundStatement_in_statementExcept3251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_laterStatement_in_statementExcept3266 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_statementExcept3281 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_statementExcept3283 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_statementExcept3285 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_statementExcept3287 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_statementExcept3289 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifStatement_in_statementExcept3302 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_insertStatement_in_statementExcept3319 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_deleteStatement_in_statementExcept3335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_statementExcept3345 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_statementExcept3349 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BREAK_in_statementExcept3359 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_statementExcept3363 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_statementExcept3374 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_statementExcept3378 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_throwStatement_in_statementExcept3394 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_returnStatement_in_statementExcept3410 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forAlphaStatement_in_statementExcept3426 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forJoinStatement_in_statementExcept3442 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tryStatement_in_statementExcept3458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_assertStatement3477 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_assertStatement3481 = new BitSet(new long[]{0x0000000000000000L,0x0008000008000000L});
    public static final BitSet FOLLOW_COLON_in_assertStatement3489 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_assertStatement3493 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_assertStatement3503 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATION_in_localOperationDefinition3518 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_localOperationDefinition3522 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_localOperationDefinition3526 = new BitSet(new long[]{0x0000000000000000L,0x0008000000800000L});
    public static final BitSet FOLLOW_typeReference_in_localOperationDefinition3530 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_localOperationDefinition3533 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_localFunctionDefinition3553 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_localFunctionDefinition3559 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_localFunctionDefinition3563 = new BitSet(new long[]{0x0000000000000000L,0x0008000000800000L});
    public static final BitSet FOLLOW_typeReference_in_localFunctionDefinition3567 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_localFunctionDefinition3570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variableDeclaration3590 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_variableDeclaration3593 = new BitSet(new long[]{0x0000000000000000L,0x0008000088000000L});
    public static final BitSet FOLLOW_typeReference_in_variableDeclaration3596 = new BitSet(new long[]{0x0000000000000000L,0x0000000088000000L});
    public static final BitSet FOLLOW_EQ_in_variableDeclaration3607 = new BitSet(new long[]{0x4017900060010060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_bindOpt_in_variableDeclaration3609 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_variableDeclaration3612 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_variableDeclaration3614 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_variableDeclaration3625 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BIND_in_bindOpt3662 = new BitSet(new long[]{0x2000000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3678 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAYS_in_bindOpt3693 = new BitSet(new long[]{0x2000000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3709 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TIE_in_bindOpt3724 = new BitSet(new long[]{0x2000000000000002L});
    public static final BitSet FOLLOW_LAZY_in_bindOpt3740 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DO_in_backgroundStatement3766 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_backgroundStatement3770 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DO_in_laterStatement3786 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LATER_in_laterStatement3790 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_laterStatement3794 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifStatement3814 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_ifStatement3818 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_ifStatement3822 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_ifStatement3826 = new BitSet(new long[]{0x499F914381440060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_statement_in_ifStatement3832 = new BitSet(new long[]{0x0000400000000002L});
    public static final BitSet FOLLOW_ELSE_in_ifStatement3835 = new BitSet(new long[]{0x499F914381440060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_statement_in_ifStatement3840 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INSERT_in_insertStatement3869 = new BitSet(new long[]{0x4017900000800060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_DISTINCT_in_insertStatement3877 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_insertStatement3881 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_INTO_in_insertStatement3885 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_insertStatement3889 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_expression_in_insertStatement3897 = new BitSet(new long[]{0x0000020000009400L});
    public static final BitSet FOLLOW_AS_in_insertStatement3913 = new BitSet(new long[]{0x00000C0000000000L});
    public static final BitSet FOLLOW_set_in_insertStatement3917 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_INTO_in_insertStatement3943 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_insertStatement3947 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_AFTER_in_insertStatement3959 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_insertStatement3963 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_BEFORE_in_insertStatement3971 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_insertStatement3975 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_insertStatement3989 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DELETE_in_deleteStatement4004 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_deleteStatement4008 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_deleteStatement4012 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THROW_in_throwStatement4027 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_throwStatement4031 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_throwStatement4035 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnStatement4055 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C02A2DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_returnStatement4058 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_returnStatement4065 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRIGGER_in_localTriggerStatement4091 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ON_in_localTriggerStatement4095 = new BitSet(new long[]{0x0000010000400000L,0x0400000000200000L,0x0000000000000001L});
    public static final BitSet FOLLOW_localTriggerCondition_in_localTriggerStatement4102 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_LPAREN_in_localTriggerStatement4106 = new BitSet(new long[]{0x0000010000400000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_localTriggerCondition_in_localTriggerStatement4110 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_localTriggerStatement4114 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_localTriggerStatement4118 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4133 = new BitSet(new long[]{0x0000000000000000L,0x0000000082000000L});
    public static final BitSet FOLLOW_LBRACKET_in_localTriggerCondition4141 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4145 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RBRACKET_in_localTriggerCondition4149 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_EQ_in_localTriggerCondition4159 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_localTriggerCondition4163 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INSERT_in_localTriggerCondition4175 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4179 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_INTO_in_localTriggerCondition4183 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4189 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_EQ_in_localTriggerCondition4193 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_localTriggerCondition4201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DELETE_in_localTriggerCondition4213 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4217 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_FROM_in_localTriggerCondition4221 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_localTriggerCondition4227 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_EQ_in_localTriggerCondition4231 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_localTriggerCondition4239 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_forAlphaStatement4254 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_forAlphaStatement4258 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_alphaExpression_in_forAlphaStatement4262 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_forAlphaStatement4266 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_forAlphaStatement4270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNITINTERVAL_in_alphaExpression4278 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_alphaExpression4282 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_DUR_in_alphaExpression4286 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_alphaExpression4290 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_FPS_in_alphaExpression4298 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_alphaExpression4302 = new BitSet(new long[]{0x0180000000000002L});
    public static final BitSet FOLLOW_WHILE_in_alphaExpression4316 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_alphaExpression4320 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_alphaExpression4334 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_IF_in_alphaExpression4338 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_alphaExpression4342 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_forJoinStatement4363 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_forJoinStatement4367 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_joinClause_in_forJoinStatement4371 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_forJoinStatement4375 = new BitSet(new long[]{0x0000000000000000L,0x0000000000A00000L});
    public static final BitSet FOLLOW_LPAREN_in_forJoinStatement4383 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_durClause_in_forJoinStatement4387 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_forJoinStatement4391 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_forJoinStatement4401 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_joinClause4409 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_joinClause4413 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_joinClause4417 = new BitSet(new long[]{0x8000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_joinClause4425 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_joinClause4429 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_joinClause4433 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_joinClause4437 = new BitSet(new long[]{0x8000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_WHERE_in_joinClause4451 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_joinClause4455 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DUR_in_durClause4469 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_durClause4473 = new BitSet(new long[]{0x07C000001C000002L});
    public static final BitSet FOLLOW_LINEAR_in_durClause4481 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_EASEIN_in_durClause4489 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_EASEOUT_in_durClause4497 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_EASEBOTH_in_durClause4505 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_MOTION_in_durClause4513 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_durClause4517 = new BitSet(new long[]{0x01C0000000000002L});
    public static final BitSet FOLLOW_FPS_in_durClause4531 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_durClause4535 = new BitSet(new long[]{0x0180000000000002L});
    public static final BitSet FOLLOW_WHILE_in_durClause4547 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_durClause4551 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_durClause4565 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_IF_in_durClause4569 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_durClause4573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRY_in_tryStatement4594 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_tryStatement4598 = new BitSet(new long[]{0x1000000000100000L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement4606 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_tryStatement4610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catchClause_in_tryStatement4620 = new BitSet(new long[]{0x1000000000100002L});
    public static final BitSet FOLLOW_FINALLY_in_tryStatement4630 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_tryStatement4634 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CATCH_in_catchClause4652 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_catchClause4656 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_catchClause4660 = new BitSet(new long[]{0x0000100000000000L,0x0008000000400000L});
    public static final BitSet FOLLOW_typeReference_in_catchClause4664 = new BitSet(new long[]{0x0000100000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_IF_in_catchClause4674 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_catchClause4678 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_catchClause4688 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_catchClause4692 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_foreach_in_expression4706 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionExpression_in_expression4719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_operationExpression_in_expression4732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_alphaExpression_in_expression4745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifExpression_in_expression4758 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_selectExpression_in_expression4774 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_expression4787 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_typeName_in_expression4793 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_expression4796 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_suffixedExpression_in_expression4800 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_suffixedExpression_in_expression4815 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOREACH_in_foreach4827 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_foreach4831 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_foreach4835 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_foreach4839 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_foreach4843 = new BitSet(new long[]{0x8000000000000000L,0x0000000010400000L});
    public static final BitSet FOLLOW_COMMA_in_foreach4851 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_foreach4855 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_IN_in_foreach4859 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_foreach4863 = new BitSet(new long[]{0x8000000000000000L,0x0000000010400000L});
    public static final BitSet FOLLOW_WHERE_in_foreach4877 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_foreach4881 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_foreach4891 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_foreach4895 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_functionExpression4903 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_functionExpression4907 = new BitSet(new long[]{0x0000000000000000L,0x0008000080800000L});
    public static final BitSet FOLLOW_typeReference_in_functionExpression4911 = new BitSet(new long[]{0x0000000000000000L,0x0000000080800000L});
    public static final BitSet FOLLOW_functionBody_in_functionExpression4917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATION_in_operationExpression4925 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_operationExpression4929 = new BitSet(new long[]{0x0000000000000000L,0x0008000000800000L});
    public static final BitSet FOLLOW_typeReference_in_operationExpression4933 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_operationExpression4939 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifExpression4947 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_ifExpression4951 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_THEN_in_ifExpression4955 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_ifExpression4959 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ELSE_in_ifExpression4963 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_ifExpression4967 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SELECT_in_selectExpression4975 = new BitSet(new long[]{0x4017900000800060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_DISTINCT_in_selectExpression4979 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_selectExpression4987 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_FROM_in_selectExpression4991 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_selectionVar_in_selectExpression4995 = new BitSet(new long[]{0x8000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_selectExpression5003 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_selectionVar_in_selectExpression5007 = new BitSet(new long[]{0x8000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_WHERE_in_selectExpression5021 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_selectExpression5025 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_selectionVar5039 = new BitSet(new long[]{0x0020000000000002L});
    public static final BitSet FOLLOW_IN_in_selectionVar5047 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_selectionVar5051 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentExpression_in_suffixedExpression5074 = new BitSet(new long[]{0x0000000002000002L,0x0000014000001800L});
    public static final BitSet FOLLOW_indexOn_in_suffixedExpression5086 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orderBy_in_suffixedExpression5090 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_durClause_in_suffixedExpression5094 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_suffixedExpression5098 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_suffixedExpression5102 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression5124 = new BitSet(new long[]{0x0000000000000002L,0x0000000080000000L});
    public static final BitSet FOLLOW_EQ_in_assignmentExpression5139 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_assignmentOpExpression_in_assignmentExpression5145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression5172 = new BitSet(new long[]{0x0000000000000002L,0x0001F00000000000L});
    public static final BitSet FOLLOW_assignmentOperator_in_assignmentOpExpression5188 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_andExpression_in_assignmentOpExpression5194 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orExpression_in_andExpression5220 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_AND_in_andExpression5236 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_orExpression_in_andExpression5242 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression5270 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_OR_in_orExpression5285 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_instanceOfExpression_in_orExpression5291 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression5319 = new BitSet(new long[]{0x0000000000000002L,0x0000000000002000L});
    public static final BitSet FOLLOW_INSTANCEOF_in_instanceOfExpression5334 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_identifier_in_instanceOfExpression5336 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5364 = new BitSet(new long[]{0x0020000000000002L,0x0000001F40000000L});
    public static final BitSet FOLLOW_LTGT_in_relationalExpression5380 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5386 = new BitSet(new long[]{0x0020000000000002L,0x0000001F40000000L});
    public static final BitSet FOLLOW_EQEQ_in_relationalExpression5400 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5406 = new BitSet(new long[]{0x0020000000000002L,0x0000001F40000000L});
    public static final BitSet FOLLOW_LTEQ_in_relationalExpression5420 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5426 = new BitSet(new long[]{0x0020000000000002L,0x0000001F40000000L});
    public static final BitSet FOLLOW_GTEQ_in_relationalExpression5440 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5446 = new BitSet(new long[]{0x0020000000000002L,0x0000001F40000000L});
    public static final BitSet FOLLOW_LT_in_relationalExpression5460 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5468 = new BitSet(new long[]{0x0020000000000002L,0x0000001F40000000L});
    public static final BitSet FOLLOW_GT_in_relationalExpression5482 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5490 = new BitSet(new long[]{0x0020000000000002L,0x0000001F40000000L});
    public static final BitSet FOLLOW_IN_in_relationalExpression5504 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression5512 = new BitSet(new long[]{0x0020000000000002L,0x0000001F40000000L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression5541 = new BitSet(new long[]{0x0000000000000002L,0x000000A000000000L});
    public static final BitSet FOLLOW_PLUS_in_additiveExpression5556 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression5562 = new BitSet(new long[]{0x0000000000000002L,0x000000A000000000L});
    public static final BitSet FOLLOW_SUB_in_additiveExpression5575 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression5582 = new BitSet(new long[]{0x0000000000000002L,0x000000A000000000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression5610 = new BitSet(new long[]{0x0000000000000002L,0x00000E0000000000L});
    public static final BitSet FOLLOW_STAR_in_multiplicativeExpression5626 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression5633 = new BitSet(new long[]{0x0000000000000002L,0x00000E0000000000L});
    public static final BitSet FOLLOW_SLASH_in_multiplicativeExpression5647 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression5653 = new BitSet(new long[]{0x0000000000000002L,0x00000E0000000000L});
    public static final BitSet FOLLOW_PERCENT_in_multiplicativeExpression5667 = new BitSet(new long[]{0x0007800000000060L,0x2C7001C0220D4003L,0x0000000000000001L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression5671 = new BitSet(new long[]{0x0000000000000002L,0x00000E0000000000L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression5701 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryOperator_in_unaryExpression5712 = new BitSet(new long[]{0x0007800000000000L,0x2C60000022014002L,0x0000000000000001L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression5716 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primaryExpression_in_postfixExpression5736 = new BitSet(new long[]{0x0000000000000002L,0x0000000022000000L});
    public static final BitSet FOLLOW_DOT_in_postfixExpression5751 = new BitSet(new long[]{0x0000000000200000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_CLASS_in_postfixExpression5755 = new BitSet(new long[]{0x0000000000000002L,0x0000000022000000L});
    public static final BitSet FOLLOW_name_in_postfixExpression5774 = new BitSet(new long[]{0x0000000000000002L,0x0000000022200000L});
    public static final BitSet FOLLOW_LPAREN_in_postfixExpression5799 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0226DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expressionListOpt_in_postfixExpression5801 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_postfixExpression5803 = new BitSet(new long[]{0x0000000000000002L,0x0000000022200000L});
    public static final BitSet FOLLOW_LBRACKET_in_postfixExpression5835 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_postfixExpression5838 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_BAR_in_postfixExpression5840 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_postfixExpression5844 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RBRACKET_in_postfixExpression5847 = new BitSet(new long[]{0x0000000000000002L,0x0000000022000000L});
    public static final BitSet FOLLOW_newExpression_in_primaryExpression5872 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression5884 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_LBRACE_in_primaryExpression5886 = new BitSet(new long[]{0x0000004200004000L,0x0400000001000060L,0x0000000000000001L});
    public static final BitSet FOLLOW_objectLiteral_in_primaryExpression5889 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACE_in_primaryExpression5891 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_bracketExpression_in_primaryExpression5907 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ordinalExpression_in_primaryExpression5927 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_contextExpression_in_primaryExpression5939 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THIS_in_primaryExpression5951 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_primaryExpression5970 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_primaryExpression5989 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression6010 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0226DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expressionListOpt_in_primaryExpression6014 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression6018 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_stringExpression_in_primaryExpression6037 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primaryExpression6055 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_newExpression6090 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_identifier_in_newExpression6093 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_newExpression6101 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0226DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expressionListOpt_in_newExpression6105 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_newExpression6109 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_objectLiteralPart_in_objectLiteral6149 = new BitSet(new long[]{0x0000004200004002L,0x0400000000000060L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_objectLiteralPart6175 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_COLON_in_objectLiteralPart6177 = new BitSet(new long[]{0x4017900060010060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_bindOpt_in_objectLiteralPart6180 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_objectLiteralPart6182 = new BitSet(new long[]{0x0000000000000002L,0x0000000018000000L});
    public static final BitSet FOLLOW_set_in_objectLiteralPart6184 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_objectLiteralPart6204 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_objectLiteralPart6208 = new BitSet(new long[]{0x0000000000000000L,0x0008000080000000L});
    public static final BitSet FOLLOW_typeReference_in_objectLiteralPart6212 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_EQ_in_objectLiteralPart6216 = new BitSet(new long[]{0x4017900060010060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_bindOpt_in_objectLiteralPart6219 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_objectLiteralPart6221 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_objectLiteralPart6225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localOperationDefinition_in_objectLiteralPart6237 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localFunctionDefinition_in_objectLiteralPart6249 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localTriggerStatement_in_objectLiteralPart6261 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDefinition_in_objectLiteralPart6273 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_bracketExpression6281 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0262DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_generatorClause_in_bracketExpression6289 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_dotDotClause_in_bracketExpression6297 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_expressionListOpt_in_bracketExpression6305 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RBRACKET_in_bracketExpression6313 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_dotDotClause6321 = new BitSet(new long[]{0x0000000000000080L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_dotDotClause6329 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_dotDotClause6333 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_DOTDOT_in_dotDotClause6343 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C2222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_LT_in_dotDotClause6347 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_dotDotClause6353 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_generatorClause6361 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_BAR_in_generatorClause6365 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_generator_in_generatorClause6369 = new BitSet(new long[]{0x0000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_generatorClause6379 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_generator_in_generatorClause6387 = new BitSet(new long[]{0x0000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_expression_in_generatorClause6395 = new BitSet(new long[]{0x0000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_name_in_generator6415 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_LARROW_in_generator6419 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_generator6423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INDEXOF_in_ordinalExpression6431 = new BitSet(new long[]{0x0000000000000000L,0x0400000020000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_ordinalExpression6439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_ordinalExpression6447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_contextExpression6459 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTE_LBRACE_STRING_LITERAL_in_stringExpression6474 = new BitSet(new long[]{0x0000000000000000L,0x0280000000000000L});
    public static final BitSet FOLLOW_FORMAT_STRING_LITERAL_in_stringExpression6478 = new BitSet(new long[]{0x0000000000000000L,0x0280000000000000L});
    public static final BitSet FOLLOW_stringExpressionPart_in_stringExpression6484 = new BitSet(new long[]{0x0000000000000000L,0x0280000000000000L});
    public static final BitSet FOLLOW_RBRACE_QUOTE_STRING_LITERAL_in_stringExpression6490 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FORMAT_STRING_LITERAL_in_stringExpressionPart6498 = new BitSet(new long[]{0x0000000000000002L,0x0200000000000000L});
    public static final BitSet FOLLOW_FORMAT_STRING_LITERAL_in_stringExpressionPart6502 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt6523 = new BitSet(new long[]{0x0000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_expressionListOpt6534 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_expressionListOpt6540 = new BitSet(new long[]{0x0000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_ORDER_in_orderBy6562 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_BY_in_orderBy6566 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_orderBy6570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INDEX_in_indexOn6585 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ON_in_indexOn6589 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_indexOn6593 = new BitSet(new long[]{0x0000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_indexOn6601 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_indexOn6605 = new BitSet(new long[]{0x0000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_set_in_multiplyOperator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_POUND_in_unaryOperator6649 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_unaryOperator6660 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUB_in_unaryOperator6673 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_unaryOperator6686 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIZEOF_in_unaryOperator6699 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TYPEOF_in_unaryOperator6712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REVERSE_in_unaryOperator6725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_unaryOperator6738 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_unaryOperator6751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSEQ_in_assignmentOperator6772 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBEQ_in_assignmentOperator6785 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAREQ_in_assignmentOperator6798 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SLASHEQ_in_assignmentOperator6811 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENTEQ_in_assignmentOperator6824 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLON_in_typeReference6848 = new BitSet(new long[]{0x0000000000000000L,0x0400020000200060L,0x0000000000000001L});
    public static final BitSet FOLLOW_set_in_typeReference6888 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_formalParameters_in_typeReference6897 = new BitSet(new long[]{0x0000000000000000L,0x0018022002000000L});
    public static final BitSet FOLLOW_typeReference_in_typeReference6899 = new BitSet(new long[]{0x0000000000000002L,0x0010022002000000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference6903 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_typeReference6958 = new BitSet(new long[]{0x0000000000000002L,0x0010022002000000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference6962 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_typeReference6988 = new BitSet(new long[]{0x0000000000000002L,0x0010022002000000L});
    public static final BitSet FOLLOW_cardinalityConstraint_in_typeReference6992 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_cardinalityConstraint7023 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RBRACKET_in_cardinalityConstraint7027 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_cardinalityConstraint7039 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUS_in_cardinalityConstraint7066 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_cardinalityConstraint7093 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_literal7162 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_LITERAL_in_literal7172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOATING_POINT_LITERAL_in_literal7182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRUE_in_literal7192 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALSE_in_literal7206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_literal7220 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualident_in_typeName7247 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_qualident7289 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
    public static final BitSet FOLLOW_DOT_in_qualident7317 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_qualident7319 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
    public static final BitSet FOLLOW_name_in_identifier7356 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_name7390 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_backgroundStatement_in_synpred453251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_laterStatement_in_synpred463266 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifStatement_in_synpred483302 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred513345 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_synpred513349 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forAlphaStatement_in_synpred563426 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forJoinStatement_in_synpred573442 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ELSE_in_synpred673835 = new BitSet(new long[]{0x499F914381440060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_statement_in_synpred673840 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FPS_in_synpred784298 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_synpred784302 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_synpred794316 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_synpred794320 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_synpred804334 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_IF_in_synpred804338 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_synpred804342 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LINEAR_in_synpred844481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EASEIN_in_synpred854489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EASEOUT_in_synpred864497 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EASEBOTH_in_synpred874505 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MOTION_in_synpred884513 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_synpred884517 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FPS_in_synpred894531 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_synpred894535 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_synpred904547 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_synpred904551 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_synpred914565 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_IF_in_synpred914569 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_synpred914573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeReference_in_synpred954664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeReference_in_synpred1064911 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeReference_in_synpred1074933 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMA_in_synpred1095003 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_selectionVar_in_synpred1095007 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_synpred1105021 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0222DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expression_in_synpred1105025 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_synpred1415884 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_LBRACE_in_synpred1415886 = new BitSet(new long[]{0x0000004200004000L,0x0400000001000060L,0x0000000000000001L});
    public static final BitSet FOLLOW_objectLiteral_in_synpred1415889 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACE_in_synpred1415891 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_synpred1485989 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1486010 = new BitSet(new long[]{0x4017900000000060L,0x2C7001C0226DC063L,0x0000000000000001L});
    public static final BitSet FOLLOW_expressionListOpt_in_synpred1486014 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1486018 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_generatorClause_in_synpred1596289 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dotDotClause_in_synpred1606297 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FORMAT_STRING_LITERAL_in_synpred1666478 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FORMAT_STRING_LITERAL_in_synpred1686502 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMA_in_synpred1716601 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_name_in_synpred1716605 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred1917023 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred1917027 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_synpred1927039 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUS_in_synpred1937066 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_synpred1947093 = new BitSet(new long[]{0x0000000000000002L});

}