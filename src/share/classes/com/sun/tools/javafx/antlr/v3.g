/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

grammar v3;

options { 
   output=AST;
   ASTLabelType=CommonTree;
   superClass = AbstractGeneratedParser; 
}
 
tokens {
   // these tokens can start a statement/definition -- can insert semi-colons before
   SEMI_INSERT_START;
   ABSTRACT='abstract';
   ASSERT='assert';
   ATTRIBUTE='attribute';
   BIND='bind';
   BREAK='break';
   CLASS='class';
   CONTINUE='continue';
   DELETE='delete';
   FALSE='false';
   FOR='for';
   FOREACH='foreach';
   FUNCTION='function';
   IF='if';
   IMPORT='import';
   INIT='init';
   INSERT='insert';
   LET='let';
   NEW='new';
   NOT='not';
   NULL='null';
   PACKAGE='package';
   PRIVATE='private';
   PROTECTED='protected';
   PUBLIC='public';
   READONLY='readonly';
   RETURN='return';
   SUPER='super';
   SIZEOF='sizeof';
   STATIC='static';
   THIS='this';
   THROW='throw';
   TRY='try';
   TRUE='true';
   VAR='var';
   WHILE='while';
   
   POUND='#';
   LPAREN='(';
   LBRACKET='[';
   PLUSPLUS='++';
   SUBSUB='--';
   PIPE='|';
   SEMI_INSERT_END;

   // cannot start a statement
   AFTER='after';
   AND='and';
   AS='as';
   BEFORE='before';
   BY='by';
   CATCH='catch';
   DO='do';
   DUR='dur';
   EASEBOTH='easeboth';
   EASEIN='easein';
   EASEOUT='easeout';
   ELSE='else';
   EXCLUSIVE='exclusive';
   EXTENDS='extends';
   FINALLY='finally';
   FIRST='first';
   FROM='from';
   IN='in';
   INDEXOF='indexof';
   INSTANCEOF='instanceof';
   INTO='into';
   INVERSE='inverse';
   LAST='last';
   LAZY='lazy';
   LINEAR='linear';
   MOTION='motion';
   ON='on';
   OR='or';
   ORDER='order';
   REPLACE='replace';
   REVERSE='reverse';
   STEP='step';
   THEN='then';
   TYPEOF='typeof';
   WITH='with';
   WHERE='where';
   
   DOTDOT='..';
   RPAREN=')';
   RBRACKET=']';
   SEMI=';';
   COMMA=',';
   DOT='.';
   EQEQ='==';
   EQ='=';
   GT='>';
   LT='<';
   LTGT='<>';
   LTEQ='<=';
   GTEQ='>=';
   PLUS='+';
   SUB='-';
   STAR='*';
   SLASH='/';
   PERCENT='%';
   PLUSEQ='+=';
   SUBEQ='-=';
   STAREQ='*=';
   SLASHEQ='/=';
   PERCENTEQ='%=';
   COLON=':';
   QUES='?';

   // these are imaginary tokens
   MODULE;
   MODIFIER;
   MEMBERSELECTOR;
   PARAM;
   FUNCTIONEXPR;
   STATEMENT;
   EXPRESSION;
   BLOCK;
   ON_REPLACE;
   ON_REPLACE_ELEMENT;
   ON_INSERT_ELEMENT;
   ON_DELETE_ELEMENT;
}

@lexer::header {
package com.sun.tools.javafx.antlr;

import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;
}

@header {
package com.sun.tools.javafx.antlr;

import java.util.HashMap;
import java.util.Map;
import java.io.OutputStreamWriter;

import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javafx.tree.*;
import com.sun.javafx.api.tree.*;

import com.sun.tools.javac.code.*;
import com.sun.tools.javac.util.*;
import static com.sun.tools.javac.util.ListBuffer.lb;
import com.sun.javafx.api.JavafxBindStatus;
import static com.sun.javafx.api.JavafxBindStatus.*;

import org.antlr.runtime.*;
}

@members {
        public v2Parser(Context context, CharSequence content) {
           this(new CommonTokenStream(new v2Lexer(context, new ANTLRStringStream(content.toString()))));
           initialize(context);
    	}
}

@lexer::members {
    /** The log to be used for error diagnostics.
     */
    private Log log;
    
    static final byte NO_INSERT_SEMI = 0; // default
    static final byte INSERT_SEMI = 1; 
    static final byte IGNORE_FOR_SEMI = 2; 
    static final byte[] semiKind = new byte[LAST_TOKEN];
    { 
      for (int i = SEMI_INSERT_START; i < SEMI_INSERT_END; ++i) {
          semiKind[i] = INSERT_SEMI;
      }
      semiKind[RBRACE] = INSERT_SEMI;
      semiKind[STRING_LITERAL] = INSERT_SEMI;
      semiKind[QUOTE_LBRACE_STRING_LITERAL] = INSERT_SEMI;
      semiKind[QUOTED_IDENTIFIER] = INSERT_SEMI;
      semiKind[DECIMAL_LITERAL] = INSERT_SEMI;
      semiKind[OCTAL_LITERAL] = INSERT_SEMI;
      semiKind[HEX_LITERAL] = INSERT_SEMI;
      semiKind[FLOATING_POINT_LITERAL] = INSERT_SEMI;
      semiKind[IDENTIFIER] = INSERT_SEMI;
      
      semiKind[WS] = IGNORE_FOR_SEMI;
      semiKind[COMMENT] = IGNORE_FOR_SEMI;
      semiKind[LINE_COMMENT] = IGNORE_FOR_SEMI;
    }
      
    int previousTokenType = -1;
    final Token syntheticSemi = new CommonToken(SEMI);
    List tokens = new ArrayList();
    
    public v2Lexer(Context context, CharStream input) {
    	this(input);
        this.log = Log.instance(context);
    }
    
    /** Allow emitting more than one token from a lexer rule
     */
    public void emit(Token token) {
        int ttype = token.getType();
        //System.err.println("::: " + ttype + " --- " + token.getText());
        if (previousTokenType == RBRACE && (ttype == EOF || semiKind[ttype] == INSERT_SEMI)) {
            this.token = syntheticSemi;
            tokens.add(syntheticSemi);
            //System.err.println("INSERTING in front of: " + ttype);
        } else {
            this.token = token;
        }
    	tokens.add(token);

        if (ttype != EOF && semiKind[ttype] != IGNORE_FOR_SEMI) {
            previousTokenType = ttype;
        }
    }
    public Token nextToken() {
        if ( tokens.size() > 0 ) {
            return (Token)tokens.remove(0);
        }
    	super.nextToken();
        if ( tokens.size()==0 ) {
            emit(Token.EOF_TOKEN);
        }
        Token cur = (Token)tokens.remove(0);
        return cur;
    }    
    
    /** Track "He{"l{"l"}o"} world" quotes
     */
    private static class BraceQuoteTracker {
        private static BraceQuoteTracker quoteStack = null;
        private int braceDepth;
        private char quote;
        private boolean percentIsFormat;
        private BraceQuoteTracker next;
        private BraceQuoteTracker(BraceQuoteTracker prev, char quote, boolean percentIsFormat) {
            this.quote = quote;
            this.percentIsFormat = percentIsFormat;
            this.braceDepth = 1;
            this.next = prev;
        }
        static void enterBrace(int quote, boolean percentIsFormat) {
            if (quote == 0) {  // exisiting string expression or non string expression
                if (quoteStack != null) {
                    ++quoteStack.braceDepth;
                    quoteStack.percentIsFormat = percentIsFormat;
                }
            } else {
                quoteStack = new BraceQuoteTracker(quoteStack, (char)quote, percentIsFormat); // push
            }
        }
        /** Return quote kind if we are reentering a quote
         * */
        static char leaveBrace() {
            if (quoteStack != null && --quoteStack.braceDepth == 0) {
                return quoteStack.quote;
            }
            return 0;
        }
        static boolean rightBraceLikeQuote(int quote) {
            return quoteStack != null && quoteStack.braceDepth == 1 && (quote == 0 || quoteStack.quote == (char)quote);
        }
        static void leaveQuote() {
            assert (quoteStack != null && quoteStack.braceDepth == 0);
            quoteStack = quoteStack.next; // pop
        }
        static boolean percentIsFormat() {
            return quoteStack != null && quoteStack.percentIsFormat;
        }
        static void resetPercentIsFormat() {
            quoteStack.percentIsFormat = false;
        }
        static boolean inBraceQuote() {
            return quoteStack != null;
        }
    }
    
    void processString() {
    	setText(StringLiteralProcessor.convert( log, getCharIndex(), getText() ));
    }

    // quote context --
    static final int CUR_QUOTE_CTX	= 0;	// 0 = use current quote context
    static final int SNG_QUOTE_CTX	= 1;	// 1 = single quote quote context
    static final int DBL_QUOTE_CTX	= 2;	// 2 = double quote quote context
 }

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

STRING_LITERAL  		: '"' DoubleQuoteBody '"'  	{ processString(); }
				| '\'' SingleQuoteBody '\''  	{ processString(); }
				;
// String Expression token implementation
QUOTE_LBRACE_STRING_LITERAL 	: '"' DoubleQuoteBody '{'   	{ processString(); }
				  NextIsPercent[DBL_QUOTE_CTX] 
				| '\'' SingleQuoteBody '{'   	{ processString(); }
				  NextIsPercent[SNG_QUOTE_CTX] 
				;
LBRACE				: '{'				{ BraceQuoteTracker.enterBrace(0, false); } 
				;
RBRACE_QUOTE_STRING_LITERAL 	:				{ BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) }?=>
				  '}' DoubleQuoteBody '"'	{ BraceQuoteTracker.leaveBrace(); 
				         			  BraceQuoteTracker.leaveQuote(); 
				         			  processString(); }
				|				{ BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) }?=>
				  '}' SingleQuoteBody '\''	{ BraceQuoteTracker.leaveBrace(); 
				         			  BraceQuoteTracker.leaveQuote(); 
				         			  processString(); }
				;
RBRACE_LBRACE_STRING_LITERAL 	:				{ BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) }?=>
				  '}' DoubleQuoteBody '{'	{ BraceQuoteTracker.leaveBrace(); 
				         			  processString(); }
				   NextIsPercent[CUR_QUOTE_CTX]	
				|				{ BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) }?=>
				  '}' SingleQuoteBody '{'	{ BraceQuoteTracker.leaveBrace(); 
				         			  processString(); }
				   NextIsPercent[CUR_QUOTE_CTX]	
				;
RBRACE				:				{ !BraceQuoteTracker.rightBraceLikeQuote(CUR_QUOTE_CTX) }?=>
				  '}'				{ BraceQuoteTracker.leaveBrace(); }
				;
fragment
DoubleQuoteBody  :	 (~('{' |'"'|'\\')|'\\' .)*  ;

fragment
SingleQuoteBody  :	 (~('{' |'\''|'\\')|'\\' .)*  ;

fragment
NextIsPercent[int quoteContext]
	 			: ((' '|'\r'|'\t'|'\u000C'|'\n')* '%')=>
	 							{ BraceQuoteTracker.enterBrace(quoteContext, true); }
				|				{ BraceQuoteTracker.enterBrace(quoteContext, false); }
				;
FORMAT_STRING_LITERAL		: 				{ BraceQuoteTracker.percentIsFormat() }?=>
				  '%' (~' ')* 			{ BraceQuoteTracker.resetPercentIsFormat(); }
				;
 
QUOTED_IDENTIFIER 
		:	'<<' (~'>'| '>' ~'>')* '>'* '>>'   	{ setText(getText().substring(2, getText().length()-2)); };
 
DECIMAL_LITERAL : ('0' | '1'..'9' '0'..'9'*) ;

OCTAL_LITERAL : '0' ('0'..'7')+ ;

HEX_LITERAL : '0' ('x'|'X') HexDigit+    			{ setText(getText().substring(2, getText().length())); };

fragment
HexDigit : ('0'..'9'|'a'..'f'|'A'..'F') ;

FLOATING_POINT_LITERAL
    :     d=DECIMAL_LITERAL RangeDots
    	  	{
    	  		$d.setType(DECIMAL_LITERAL);
    	  		emit($d);
          		$RangeDots.setType(DOTDOT);
    	  		emit($RangeDots);
    	  	}
    |     d=OCTAL_LITERAL RangeDots
    	  	{
    	  		$d.setType(OCTAL_LITERAL);
    	  		emit($d);
          		$RangeDots.setType(DOTDOT);
    	  		emit($RangeDots);
    	  	}
    |	  Digits '.' (Digits)? (Exponent)? 
    | '.' Digits (Exponent)? 
    |     Digits Exponent
    ;

fragment
RangeDots 
	:	'..'
	;
fragment
Digits	:	('0'..'9')+ 
 	;
fragment
Exponent : 	('e'|'E') ('+'|'-')? Digits
 	;

IDENTIFIER 
    :   Letter (Letter|JavaIDDigit)*
    ;

fragment
Letter
    :  '\u0024' |
       '\u0041'..'\u005a' |
       '\u005f' |
       '\u0061'..'\u007a' |
       '\u00c0'..'\u00d6' |
       '\u00d8'..'\u00f6' |
       '\u00f8'..'\u00ff' |
       '\u0100'..'\u1fff' |
       '\u3040'..'\u318f' |
       '\u3300'..'\u337f' |
       '\u3400'..'\u3d2d' |
       '\u4e00'..'\u9fff' |
       '\uf900'..'\ufaff'
    ;

fragment
JavaIDDigit
    :  '\u0030'..'\u0039' |
       '\u0660'..'\u0669' |
       '\u06f0'..'\u06f9' |
       '\u0966'..'\u096f' |
       '\u09e6'..'\u09ef' |
       '\u0a66'..'\u0a6f' |
       '\u0ae6'..'\u0aef' |
       '\u0b66'..'\u0b6f' |
       '\u0be7'..'\u0bef' |
       '\u0c66'..'\u0c6f' |
       '\u0ce6'..'\u0cef' |
       '\u0d66'..'\u0d6f' |
       '\u0e50'..'\u0e59' |
       '\u0ed0'..'\u0ed9' |
       '\u1040'..'\u1049'
   ;

WS  :  (' '|'\r'|'\t'|'\u000C'|'\n') {$channel=HIDDEN;}
    ;

COMMENT
    :   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

LINE_COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    ;

LAST_TOKEN
    :	'~~~~~~~~' {false}? '~~~~~~~~'
    ;
    	
/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

module
	: packageDecl? moduleItems EOF 		-> ^(MODULE packageDecl? moduleItems)
       	;
packageDecl 
       	: PACKAGE qualident SEMI         	-> qualident
	;
moduleItems    
	: moduleItem? (SEMI moduleItem? )*	-> moduleItem*
	;
moduleItem 
	: importDecl 				-> importDecl
	| classDefinition 			-> classDefinition
	| statement      			-> statement
	| expression 				-> expression
	;
importDecl
 	: IMPORT importId			-> ^(IMPORT importId)
	;
importId
 	: ( identifier				-> identifier)
                 ( DOT name			-> ^(DOT $importId name) )* 
                 ( DOT STAR			-> ^(DOT $importId STAR) )?  
	;
classDefinition 
	: classModifierFlags  CLASS name supers LBRACE classMembers RBRACE
	  					-> ^(CLASS classModifierFlags name supers classMembers)
	;
supers 
	: (EXTENDS typeName
           ( COMMA typeName )* 
	  )?
	  					-> ^(EXTENDS typeName+)
	;
classMembers 
	: classMember ?
	  (SEMI
	     classMember ?
	  ) *					-> classMember*
	;
classMember
	: initDefinition	
	| variableDeclaration 
	| functionDefinition 
	;
functionDefinition
	: functionModifierFlags FUNCTION name formalParameters typeReference  
	    					-> ^(FUNCTION name functionModifierFlags formalParameters typeReference)
	;
initDefinition
	: INIT block 				-> ^(INIT block)
	;
functionModifierFlags
	: accessModifier functionModifier?	-> ^(MODIFIER accessModifier  functionModifier?)
	| functionModifier accessModifier?	-> ^(MODIFIER accessModifier? functionModifier?)
	| 					-> ^(MODIFIER)
	;
varModifierFlags
	: accessModifier varModifier?		-> ^(MODIFIER accessModifier  varModifier?)
	| varModifier accessModifier?		-> ^(MODIFIER accessModifier? varModifier?)
	| 					-> ^(MODIFIER)
	;
classModifierFlags
	: accessModifier classModifier?		-> ^(MODIFIER accessModifier  classModifier?)
	| classModifier accessModifier?		-> ^(MODIFIER accessModifier? classModifier?)
	| 					-> ^(MODIFIER)
	;
accessModifier 
	:  PUBLIC          			
	|  PRIVATE         			
	|  PROTECTED       			
	;
functionModifier 
	:  ABSTRACT        			
	|  STATIC        			
	;
varModifier 
	:  READONLY        			
	|  STATIC        			
	;
classModifier 
	:  ABSTRACT        			
	;
memberSelector
	: name DOT name				-> ^(MEMBERSELECTOR name+)
	;
formalParameters
	: LPAREN ( formalParameter (COMMA formalParameter)* )?  RPAREN
						-> ^(LPAREN formalParameter*)
	;
formalParameter
	: name typeReference			-> ^(PARAM name typeReference)
	;
block
	: LBRACE blockComponent?
	   (SEMI blockComponent?) *
	  RBRACE				-> ^(BLOCK blockComponent*)
	;
blockExpression 
	: LBRACE blockComponent?
	   (SEMI blockComponent?) *
	  RBRACE				-> ^(LBRACE blockComponent*)
	;
blockComponent
	: statement				-> ^(STATEMENT statement)
	| expression				-> ^(EXPRESSION expression)
	;
statement 
	: variableDeclaration	
	| functionDefinition 
	| insertStatement 	
	| deleteStatement 
 	| whileStatement
	| BREAK    		
	| CONTINUE  	 	 	
       	| THROW expression 	   	
       	| returnStatement 		
       	| tryStatement			
       	;
variableDeclaration   
	: varModifierFlags variableLabel  name  typeReference (EQ boundExpression)? onChangeClause*
	    					-> ^(VAR variableLabel varModifierFlags name typeReference boundExpression? onChangeClause*)
	;
onChangeClause  
	: ON REPLACE (LPAREN oldv=formalParameter RPAREN)? block
						-> ^(ON_REPLACE $oldv block)
	| ON REPLACE LBRACKET index=formalParameter RBRACKET (LPAREN oldv=formalParameter RPAREN)? block
						-> ^(ON_REPLACE_ELEMENT $index $oldv? block)
	| ON INSERT LBRACKET index=formalParameter RBRACKET (LPAREN newv=formalParameter RPAREN)? block
						-> ^(ON_INSERT_ELEMENT $index $newv? block)
	| ON DELETE LBRACKET index=formalParameter RBRACKET (LPAREN oldv=formalParameter RPAREN)? block
						-> ^(ON_DELETE_ELEMENT $index $oldv? block)
	;
variableLabel 
	: VAR	
	| LET	
	| ATTRIBUTE	
	;
whileStatement
	: WHILE LPAREN expression RPAREN block	-> ^(WHILE expression block)
	;
insertStatement  
	: INSERT e1=expression INTO e2=expression 
						-> ^(INSERT $e1 $e2)
	;
deleteStatement  
	: DELETE  e1=expression  
	   ( FROM e2=expression 		-> ^(FROM $e1 $e2)
	   | /* indexed and whole cases */	-> ^(DELETE $e2)
	   )
	;
returnStatement
	: RETURN expression?			-> ^(RETURN expression?)
	;
tryStatement
	: TRY block 			
	   ( finallyClause
	   | catchClause+ finallyClause?   
	   ) 					-> ^(TRY block catchClause+ finallyClause?)
	;
finallyClause
	: FINALLY block				-> ^(FINALLY block)
	;
catchClause
	: CATCH LPAREN formalParameter RPAREN block
						-> ^(CATCH formalParameter block)
	;
boundExpression 
	: BIND LAZY? expression (WITH INVERSE)?
						-> ^(BIND LAZY? INVERSE? expression)
	| expression				-> ^(EXPRESSION expression)
	;
expression 
       	: blockExpression
       	| ifExpression   		
       	| forExpression   	
       	| newExpression 	
	| assignmentExpression	 
      	;
forExpression
	: FOR LPAREN inClause (COMMA inClause)* RPAREN expression
								-> ^(FOR inClause* expression)
	;
inClause
	: formalParameter IN expression (WHERE expression)?	-> ^(IN formalParameter IN expression expression?)
	;
ifExpression 
	: IF LPAREN econd=expression  RPAREN 
		THEN?  ethen=expression  elseClause 		-> ^(IF $econd $ethen elseClause)
	;
elseClause
	: (ELSE)=>  ELSE  expression				-> expression
	| /*nada*/ 						->
	;
assignmentExpression  
	: assignmentOpExpression 
		( (EQ)=>   EQ  expression			-> ^(EQ assignmentOpExpression expression)
		|						-> assignmentOpExpression
		)
	;
assignmentOpExpression 
	: e1=andExpression					
	   (   assignmentOperator   e2=expression		-> ^(assignmentOperator $e1, $e2) 
	   |							-> $e1
	   )
	;
andExpression  
	: e1=orExpression					
	   (   AND   e2=orExpression				-> ^(AND $e1, $e2) 
	   |							-> $e1
	   )
	;
orExpression  
	: e1=typeExpression				
	   (   OR   e2=typeExpression				-> ^(OR $e1, $e2) 
	   |							-> $e1
	   )
	;
typeExpression 
	: relationalExpression		
	   (   INSTANCEOF itn=typeName				-> ^(INSTANCEOF relationalExpression $itn) }
	   |   AS atn=typeName					-> ^(AS relationalExpression $atn) }
	   | 							-> relationalExpression
	   )
	;
relationalExpression  returns [JCExpression expr] 
	: e1=additiveExpression					{ $expr = $e1.expr; }
	   (   LTGT   e=additiveExpression			{ $expr = F.at(pos($LTGT)).Binary(JCTree.NE, $expr, $e.expr); }
	   |   EQEQ   e=additiveExpression			{ $expr = F.at(pos($EQEQ)).Binary(JCTree.EQ, $expr, $e.expr); }
	   |   LTEQ   e=additiveExpression			{ $expr = F.at(pos($LTEQ)).Binary(JCTree.LE, $expr, $e.expr); }
	   |   GTEQ   e=additiveExpression			{ $expr = F.at(pos($GTEQ)).Binary(JCTree.GE, $expr, $e.expr); }
	   |   LT     e=additiveExpression			{ $expr = F.at(pos($LT))  .Binary(JCTree.LT, $expr, $e.expr); }
	   |   GT     e=additiveExpression			{ $expr = F.at(pos($GT))  .Binary(JCTree.GT, $expr, $e.expr); }
	   ) * ;
additiveExpression  returns [JCExpression expr] 
	: e1=multiplicativeExpression				{ $expr = $e1.expr; }
	   (   PLUS   e=multiplicativeExpression		{ $expr = F.at(pos($PLUS)).Binary(JCTree.PLUS , $expr, $e.expr); }
	   |   SUB    e=multiplicativeExpression		{ $expr = F.at(pos($SUB)) .Binary(JCTree.MINUS, $expr, $e.expr); }
	   ) * ;
multiplicativeExpression  returns [JCExpression expr] 
	: e1=unaryExpression					{ $expr = $e1.expr; }
	   (   STAR    e=unaryExpression			{ $expr = F.at(pos($STAR))   .Binary(JCTree.MUL  , $expr, $e.expr); }
	   |   SLASH   e=unaryExpression			{ $expr = F.at(pos($SLASH))  .Binary(JCTree.DIV  , $expr, $e.expr); }
	   |   PERCENT e=unaryExpression			{ $expr = F.at(pos($PERCENT)).Binary(JCTree.MOD  , $expr, $e.expr); }   
	   ) * ;
unaryExpression  returns [JCExpression expr] 
	: suffixedExpression					{ $expr = $suffixedExpression.expr; }
	| prefixUnaryOperator   e=unaryExpression		{ $expr = F.Unary($prefixUnaryOperator.optag, $e.expr); }
	;
suffixedExpression  returns [JCExpression expr] 
	: e1=postfixExpression					{ $expr = $e1.expr; }
		( PLUSPLUS					{ $expr = F.at(pos($PLUSPLUS)).Unary(JCTree.POSTINC, $expr); } )?
		( SUBSUB					{ $expr = F.at(pos($SUBSUB)).Unary(JCTree.POSTDEC, $expr); } )?
	;
postfixExpression  returns [JCExpression expr] 
@init {
    ListBuffer<JFXForExpressionInClause> clauses;
    JFXVar var; 
}
	: primaryExpression 					{ $expr = $primaryExpression.expr; }
	   ( DOT ( CLASS   					//TODO
	  	| n1 = name()   					{ $expr = F.at(pos($DOT)).Select($expr, $n1.value); }
	         )   
	   | LPAREN expressionListOpt RPAREN   			{ $expr = F.at(pos($LPAREN)).Apply(null, $expr, $expressionListOpt.args.toList()); } 
	   | left1 = LBRACKET e1 = expression()  right1 = RBRACKET			{ $expr = F.at(pos(left1)).SequenceIndexed($expr, $e1.expr); }
	   ) * 
	;
primaryExpression  returns [JCExpression expr] 
	: qualident						{ $expr = $qualident.expr; }
		( LBRACE  objectLiteral RBRACE 			{ $expr = F.at(pos($LBRACE)).Instanciate($expr, null, $objectLiteral.parts.toList()); } 
		)?
       	| THIS							{ $expr = F.at(pos($THIS)).Ident(names._this); }
       	| SUPER							{ $expr = F.at(pos($SUPER)).Ident(names._super); }
       	| stringExpression 					{ $expr = $stringExpression.expr; }
       	| bracketExpression 					{ $expr = $bracketExpression.expr; }
       	| literal 						{ $expr = $literal.expr; }
      	| functionExpression					{ $expr = $functionExpression.expr; }
       	| LPAREN expression RPAREN				{ $expr = F.at(pos($LPAREN)).Parens($expression.expr); }
       	;
functionExpression  
	: FUNCTION   formalParameters   typeReference blockExpression
								-> ^(FUNCTIONEXPR formalParameters   typeReference blockExpression)
	;
newExpression  returns [JCExpression expr] 
@init { ListBuffer<JCExpression> args = ListBuffer.<JCExpression>lb(); }
	: NEW  typeName  
		( (LPAREN)=>LPAREN expressionListOpt  RPAREN 	{ args = $expressionListOpt.args; } 
		)?
								{ $expr = F.at(pos($NEW)).Instanciate($typeName.expr, args.toList(), null); }
		   //TODO: need anonymous subclasses
	;
objectLiteral  returns [ListBuffer<JCTree> parts = ListBuffer.<JCTree>lb()]
	: ( objectLiteralPart  					{ $parts.append($objectLiteralPart.value); } ) * 
	;
objectLiteralPart  returns [JCTree value]
	: name COLON  boundExpression (COMMA | SEMI)?		{ $value = F.at($name.pos).ObjectLiteralPart($name.value, $boundExpression.expr, $boundExpression.status); }
       	| variableDeclaration	(COMMA | SEMI)?			{ $value = $variableDeclaration.value; }
       	| functionDefinition 	(COMMA | SEMI)?			{ $value = $functionDefinition.value; }
       	;
stringExpression  returns [JCExpression expr] 
@init { ListBuffer<JCExpression> strexp = new ListBuffer<JCExpression>(); }
	: ql=QUOTE_LBRACE_STRING_LITERAL	{ strexp.append(F.at(pos($ql)).Literal(TypeTags.CLASS, $ql.text)); }
	  f1=stringFormat			{ strexp.append($f1.expr); }
	  e1=expression 			{ strexp.append($e1.expr); }
	  (  rl=RBRACE_LBRACE_STRING_LITERAL	{ strexp.append(F.at(pos($rl)).Literal(TypeTags.CLASS, $rl.text)); }
	     fn=stringFormat			{ strexp.append($fn.expr); }
	     en=expression 			{ strexp.append($en.expr); }
	  )*   
	  rq=RBRACE_QUOTE_STRING_LITERAL	{ strexp.append(F.at(pos($rq)).Literal(TypeTags.CLASS, $rq.text)); }
	  					{ $expr = F.at(pos($ql)).StringExpression(strexp.toList()); }
	;
stringFormat  returns [JCExpression expr] 
	: fs=FORMAT_STRING_LITERAL		{ $expr = F.at(pos($fs)).Literal(TypeTags.CLASS, $fs.text); }
	| /* no formar */			{ $expr = F.             Literal(TypeTags.CLASS, ""); }
	;
bracketExpression   returns [JFXAbstractSequenceCreator expr]
@init { ListBuffer<JCExpression> exps = new ListBuffer<JCExpression>(); JCExpression step = null; boolean exclusive = false; }
	: LBRACKET   
	    ( /*nada*/				{ $expr = F.at(pos($LBRACKET)).EmptySequence(); }
	    | e1=expression 			{ exps.append($e1.expr); }
	     	(   /*nada*/			{ $expr = F.at(pos($LBRACKET)).ExplicitSequence(exps.toList()); }
	     	| COMMA 
	     	   (   /*nada*/					{ $expr = F.at(pos($LBRACKET)).ExplicitSequence(exps.toList()); }
            	| e2=expression 		{ exps.append($e2.expr); }
	     	         (COMMA  en=expression	{ exps.append($en.expr); } )*
                          COMMA?
	     	       				{ $expr = F.at(pos($LBRACKET)).ExplicitSequence(exps.toList()); }
                    )
	     	| DOTDOT   dd=expression	
	     	    ( STEP st=expression	{ step = $st.expr; }
	     	    )?
	     	    ( EXCLUSIVE			{ exclusive = true; }
	     	    )?
	     					{ $expr = F.at(pos($LBRACKET)).RangeSequence($e1.expr, $dd.expr, step, exclusive); }
	     	)   
	    )
	  RBRACKET 
	;
expressionListOpt  returns [ListBuffer<JCExpression> args = new ListBuffer<JCExpression>()] 
	: ( e1=expression		{ $args.append($e1.expr); }
	    (COMMA   e=expression	{ $args.append($e.expr); }  )* )? ;
prefixUnaryOperator  returns [int optag]
//	: POUND		
//	| QUES   	
	: SUB   		
	| NOT   	
	| SIZEOF   	
//	| TYPEOF   	
//	| REVERSE   	
	| PLUSPLUS   	
	| SUBSUB 
	;
assignmentOperator 
	: PLUSEQ   
	| SUBEQ   		
	| STAREQ   
	| SLASHEQ   
	| PERCENTEQ   			
	;
type returns [JFXType type]
	: typeName cardinality		{ $type = F.TypeClass($typeName.expr, $cardinality.ary); }
 	| FUNCTION LPAREN tal=typeArgList
          	   RPAREN ret=typeReference 
          	   	cardinality	//TODO: this introduces an ambiguity: return cardinality vs type cardinality
          	   			{ $type = F.at(pos($FUNCTION)).TypeFunctional($tal.ptypes.toList(), $ret.type, $cardinality.ary); }
 	| STAR cardinality		{ $type = F.at(pos($STAR)).TypeAny($cardinality.ary); } 
 	;
typeArgList   returns [ListBuffer<JFXType> ptypes = ListBuffer.<JFXType>lb(); ]
 	: (pt0=typeArg			{ ptypes.append($pt0.type); }
	          (COMMA ptn=typeArg	{ ptypes.append($ptn.type); } 
	          )* )?
	;
typeArg returns [JFXType type]
 	: COLON type			{ $type = $type.type; }
 	| name COLON type		{ $type = $type.type; }
 	| name				{ $type = F.TypeUnknown(); }
 	;
typeReference returns [JFXType type]
 	: COLON type			{ $type = $type.type; }
 	| /*nada*/			{ $type = F.TypeUnknown(); }
 	;
cardinality returns [TypeTree.Cardinality ary]
	: (LBRACKET)=>LBRACKET RBRACKET { ary = TypeTree.Cardinality.ANY; }
	|                         	{ ary = TypeTree.Cardinality.SINGLETON; } 
	;
literal  returns [JCExpression expr]
	: t=STRING_LITERAL		{ $expr = F.at(pos($t)).Literal(TypeTags.CLASS, $t.text); }
	| t=DECIMAL_LITERAL		{ $expr = F.at(pos($t)).Literal(TypeTags.INT, Convert.string2int($t.text, 10)); }
	| t=OCTAL_LITERAL		{ $expr = F.at(pos($t)).Literal(TypeTags.INT, Convert.string2int($t.text, 8)); }
	| t=HEX_LITERAL			{ $expr = F.at(pos($t)).Literal(TypeTags.INT, Convert.string2int($t.text, 16)); }
	| t=FLOATING_POINT_LITERAL 	{ $expr = F.at(pos($t)).Literal(TypeTags.DOUBLE, Double.valueOf($t.text)); }
	| t=TRUE   			{ $expr = F.at(pos($t)).Literal(TypeTags.BOOLEAN, 1); }
	| t=FALSE   			{ $expr = F.at(pos($t)).Literal(TypeTags.BOOLEAN, 0); }
	| t=NULL 			{ $expr = F.at(pos($t)).Literal(TypeTags.BOT, null); } 
	;
	
typeName  returns [JCExpression expr]
	: qualident 			{ $expr = $qualident.expr; }
		(typeArguments		{ $expr = F.TypeApply($expr, $typeArguments.exprbuff.toList()); }
		)?
	;

typeArguments  returns [ListBuffer<JCExpression> exprbuff = ListBuffer.<JCExpression>lb()]
	: LT ta0=typeArgument 		{ $exprbuff.append($ta0.expr); }
	     (COMMA tan=typeArgument	{ $exprbuff.append($tan.expr); }
	     )* 
	  GT
	;
	
typeArgument  returns [JCExpression expr]
@init { BoundKind bk = BoundKind.UNBOUND; JCExpression texpr = null; }
	: typeName			{ $expr = $typeName.expr; }
	| QUES (  ( EXTENDS 		{ bk = BoundKind.EXTENDS; }
		  | SUPER		{ bk = BoundKind.SUPER; }
		  ) 
		 typeName		{ texpr = $typeName.expr; }
	       )?			{ $expr = F.at(pos($QUES)).Wildcard(
	       					F.TypeBoundKind(bk), texpr); }
	;
	
qualident returns [JCExpression expr]
	: n0=plainName            	{ $expr = F.at($n0.pos).Ident($n0.value); } 
         ( (DOT)=> DOT nn=plainName     { $expr = F.at($nn.pos).Select($expr, $nn.value); } 
         ) *  
	| frenchName			{ $expr = F.at($frenchName.pos).Identifier($frenchName.value); }
         ( (DOT)=> DOT nn=plainName     { $expr = F.at($nn.pos).Select($expr, $nn.value); } 
         ) *  
	;
identifier  returns [JCIdent expr]
	: name              		{ $expr = F.at($name.pos).Ident($name.value); } 
	;
name returns [Name value, int pos]
	: plainName			{ $value = $plainName.value; $pos = $plainName.pos; }
	| frenchName			{ $value = $frenchName.value; $pos = $frenchName.pos; }
	;
plainName returns [Name value, int pos]
	: IDENTIFIER			{ $value = Name.fromString(names, $IDENTIFIER.text); $pos = pos($IDENTIFIER); } 
	;
frenchName returns [Name value, int pos]
	: QUOTED_IDENTIFIER		{ $value = Name.fromString(names, $QUOTED_IDENTIFIER.text); $pos = pos($QUOTED_IDENTIFIER); } 
	;
