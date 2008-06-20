/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
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
   // these tokens can start a statement/definition -- can insert semi-colons before -- alpha order
   SEMI_INSERT_START;
   ABSTRACT='abstract';
   ASSERT='assert';
   AT='at';
   ATTRIBUTE='attribute';
   BIND='bind';
   BOUND='bound';
   BREAK='break';
   CLASS='class';
   CONTINUE='continue';
   DELETE='delete';
   FALSE='false';
   FOR='for';
   FUNCTION='function';
   IF='if';
   IMPORT='import';
   INDEXOF='indexof';
   INIT='init';
   INSERT='insert';
   LET='let';
   NEW='new';
   NOT='not';
   NULL='null';
   OVERRIDE='override';
   PACKAGE='package';
   POSTINIT='postinit';
   PRIVATE='private';
   PROTECTED='protected';
   PUBLIC='public';
   READONLY='readonly';
   RETURN='return';
   REVERSE='reverse';
   SUPER='super';
   SIZEOF='sizeof';
   STATIC='static';
   THIS='this';
   THROW='throw';
   TRY='try';
   TRUE='true';
   TYPEOF='typeof';
   VAR='var';
   WHILE='while';
   
   POUND='#';
   LPAREN='(';
   LBRACKET='[';
   PLUSPLUS='++';
   SUBSUB='--';
   PIPE='|';
   SEMI_INSERT_END;

   // cannot start a statement -- alpha order
   AFTER='after';
   AND='and';
   AS='as';
   BEFORE='before';
   CATCH='catch';
   ELSE='else';
   EXCLUSIVE='exclusive';
   EXTENDS='extends';
   FINALLY='finally';
   FIRST='first';
   FROM='from';
   IN='in';
   INSTANCEOF='instanceof';
   INTO='into';
   INVERSE='inverse';
   LAST='last';
   LAZY='lazy';
   MOD='mod';
   ON='on';
   OR='or';
   REPLACE='replace';
   STEP='step';
   THEN='then';
   TRIGGER='trigger';
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
   NOTEQ='!=';
   COLON=':';
   QUES='?';
   TWEEN='tween';
   SUCHTHAT='=>';

   // these are imaginary tokens
   MODULE;
   EMPTY_MODULE_ITEM;
   MODIFIER;
   CLASS_MEMBERS;
   PARAM;
   FUNC_EXPR;
   STATEMENT;
   EXPRESSION;
   BLOCK;
   MISSING_NAME;
   SLICE_CLAUSE;
   ON_REPLACE_SLICE;
   ON_REPLACE;
   EXPR_LIST;
   FUNC_APPLY;
   NEGATIVE;
   POSTINCR;
   POSTDECR;
   SEQ_INDEX;
   SEQ_SLICE;
   SEQ_SLICE_EXCLUSIVE;
   OBJECT_LIT;
   OBJECT_LIT_PART;
   SEQ_EMPTY;
   SEQ_EXPLICIT;
   EMPTY_FORMAT_STRING;
   TYPE_NAMED;
   TYPE_FUNCTION;
   TYPE_ANY;
   TYPE_UNKNOWN;
   TYPE_ARG;
   TYPED_ARG_LIST;
   DOC_COMMENT;
   ATTR_INTERPOLATE;
}

@lexer::header {
package com.sun.tools.javafx.antlr;

import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Convert;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javafx.util.MsgSym;
}

@header {
package com.sun.tools.javafx.antlr;

import com.sun.tools.javac.util.Context;
import org.antlr.runtime.tree.*;
import org.antlr.runtime.*;

import com.sun.tools.javafx.util.MsgSym;
}

@lexer::members {
    /** The log to be used for error diagnostics.
     */
//    private Log log;
    
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
      semiKind[DECIMAL_LITERAL] = INSERT_SEMI;
      semiKind[OCTAL_LITERAL] = INSERT_SEMI;
      semiKind[HEX_LITERAL] = INSERT_SEMI;
      semiKind[TIME_LITERAL] = INSERT_SEMI;
      semiKind[FLOATING_POINT_LITERAL] = INSERT_SEMI;
      semiKind[IDENTIFIER] = INSERT_SEMI;
      
      semiKind[WS] = IGNORE_FOR_SEMI;
      semiKind[COMMENT] = IGNORE_FOR_SEMI;
      semiKind[LINE_COMMENT] = IGNORE_FOR_SEMI;
    }
      
    public v3Lexer(Context context, CharStream input) {
    	this(input);
        this.log = Log.instance(context);
    }

    protected int getSyntheticSemiType() {
        return SEMI;
    }

    protected boolean verifyCurrentType(int ttype) {
        return ttype != EOF && semiKind[ttype] != IGNORE_FOR_SEMI;
    }

    protected boolean verifyPreviousType(int ttype, int previousTokenType) {
        return previousTokenType == RBRACE && (ttype == EOF || semiKind[ttype] == INSERT_SEMI);
    }


    protected void checkIntLiteralRange(String text, int pos, int radix) {
       
        long value = Convert.string2long(text, radix);
        
        pos = pos - text.length();
        
        if (previousTokenType == SUB) {
            value = -value;
            if ( value < Integer.MIN_VALUE )
                log.error(pos, MsgSym.MESSAGE_JAVAFX_LITERAL_OUT_OF_RANGE, "small", new String("-" + text));
             
        } else if (value > Integer.MAX_VALUE) {
            log.error(pos, MsgSym.MESSAGE_JAVAFX_LITERAL_OUT_OF_RANGE, "big", text);
            
        } 
    }

    // quote context --
    static final int CUR_QUOTE_CTX	= 0;	// 0 = use current quote context
    static final int SNG_QUOTE_CTX	= 1;	// 1 = single quote quote context
    static final int DBL_QUOTE_CTX	= 2;	// 2 = double quote quote context
            }

@members {
    Tree getDocComment(Token start) {
       int index = start.getTokenIndex() - 1;
       while (index >= 0) { 
           Token tok = input.get(index);
           if (tok.getType() == WS || 
               tok.getType() == SEMI && tok.getText().equals("beginning of new statement"))
              --index;
           else
              break;
       }
       if (index < 0 || input.get(index).getType() != COMMENT)
           return null;
       Token token = input.get(index);
       if (token.getText().startsWith("/**")) {
           token.setType(DOC_COMMENT);
           return new CommonTree(token);
       }
       return null;
    }
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
LBRACE				: '{'				{ enterBrace(0, false); } 
				;
RBRACE_QUOTE_STRING_LITERAL 	:				{ rightBraceLikeQuote(DBL_QUOTE_CTX) }?=>
				  '}' DoubleQuoteBody '"'	{ leaveBrace(); 
				         			  leaveQuote(); 
				         			  processString(); }
				|				{ rightBraceLikeQuote(SNG_QUOTE_CTX) }?=>
				  '}' SingleQuoteBody '\''	{ leaveBrace(); 
				         			  leaveQuote(); 
				         			  processString(); }
				;
RBRACE_LBRACE_STRING_LITERAL 	:				{ rightBraceLikeQuote(DBL_QUOTE_CTX) }?=>
				  '}' DoubleQuoteBody '{'	{ leaveBrace(); 
				         			  processString(); }
				   NextIsPercent[CUR_QUOTE_CTX]	
				|				{ rightBraceLikeQuote(SNG_QUOTE_CTX) }?=>
				  '}' SingleQuoteBody '{'	{ leaveBrace(); 
				         			  processString(); }
				   NextIsPercent[CUR_QUOTE_CTX]	
				;
RBRACE				:				{ !rightBraceLikeQuote(CUR_QUOTE_CTX) }?=>
				  '}'				{ leaveBrace(); }
				;
fragment
DoubleQuoteBody  :	 (~('{' |'"'|'\\')|'\\' .)*  ;

fragment
SingleQuoteBody  :	 (~('{' |'\''|'\\')|'\\' .)*  ;

fragment
NextIsPercent[int quoteContext]
	 			: ((' '|'\r'|'\t'|'\u000C'|'\n')* '%')=>
	 							{ enterBrace(quoteContext, true); }
				|				{ enterBrace(quoteContext, false); }
				;
FORMAT_STRING_LITERAL		: 				{ percentIsFormat() }?=>
				  '%' (~' ')* 			{ processFormatString();
	 							  resetPercentIsFormat(); }
				;
TRANSLATION_KEY                 : '##' 
                                  ( 
                                    '[' TranslationKeyBody ']'
                                  )?                            { processTranslationKey(); }
				;

fragment
TranslationKeyBody              : (~('[' | ']' | '\\')|'\\' .)+
                                ;
 
TIME_LITERAL : (DECIMAL_LITERAL | Digits '.' (Digits)? (Exponent)? | '.' Digits (Exponent)?) ( 'ms' | 'm' | 's' | 'h' ) ;

DECIMAL_LITERAL : ('0' | '1'..'9' '0'..'9'*)  { checkIntLiteralRange(getText(), getCharIndex(), 10); };

OCTAL_LITERAL : '0' ('0'..'7')+  { checkIntLiteralRange(getText(), getCharIndex(), 8); };

HEX_LITERAL : '0' ('x'|'X') HexDigit+    			{ setText(getText().substring(2, getText().length()));
                                                                  checkIntLiteralRange(getText(), getCharIndex(), 16); };

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
	:	DOTDOT
	;
fragment
Digits	:	('0'..'9')+ 
 	;
fragment
Exponent : 	('e'|'E') ('+'|'-')? Digits
 	;

IDENTIFIER 
	: Letter (Letter|JavaIDDigit)*
	| '<<' (~'>'| '>' ~'>')* '>'* '>>'			{ setText(getText().substring(2, getText().length()-2)); }
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
    : '//' ~('\n'|'\r')* '\r'? ('\n'|EOF) {$channel=HIDDEN;}
    ;

LAST_TOKEN
    :	'~~~~~~~~' {false}? '~~~~~~~~'
    ;
    	
/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

module
@after { Tree docComment = getDocComment($module.start);
         $module.tree.addChild(docComment); }
	: packageDecl? moduleItems EOF 		-> ^(MODULE packageDecl? moduleItems)
       	;
packageDecl 
       	: PACKAGE qualname SEMI         	-> ^(PACKAGE qualname)
	;
moduleItems    
	: moduleItem (SEMI moduleItem )*	-> moduleItem+
	;
moduleItem 
	: importDecl 				-> importDecl
	| classDefinition 			-> classDefinition
        | functionDefinition                    -> functionDefinition
	| statement				-> statement
	| expression				-> expression
	|					-> EMPTY_MODULE_ITEM
	;
importDecl
 	: IMPORT importId			-> ^(IMPORT importId)
	;
importId
 	: ( name				-> name)
                 ( DOT name			-> ^(DOT $importId name) )* 
                 ( DOT STAR			-> ^(DOT $importId STAR) )?  
	;
classDefinition 
@after { Tree docComment = getDocComment($classDefinition.start);
         $classDefinition.tree.addChild(docComment); }
	: classModifierFlags  CLASS name supers LBRACE classMembers RBRACE
	  					-> ^(CLASS classModifierFlags name supers classMembers)
	;
supers 
	: EXTENDS typeName
           ( COMMA typeName )* 			-> ^(EXTENDS typeName*)
	|                                       -> ^(EXTENDS)
	;	  					
classMembers 
	: classMember ?
	  (SEMI
	     classMember ?
	  ) *					-> ^(CLASS_MEMBERS classMember*)
	;
classMember
	: initDefinition	
	| postInitDefinition
	| attributeDeclaration 
	| overrideDeclaration 
	| functionDefinition 
//	| triggerDefinition
	;
functionDefinition
@after { Tree docComment = getDocComment($functionDefinition.start);
         $functionDefinition.tree.addChild(docComment); }
	: functionModifierFlags FUNCTION name formalParameters typeReference blockExpression?
	    					-> ^(FUNCTION name functionModifierFlags 
	    						formalParameters typeReference 
	    						blockExpression?)
	;
attributeDeclaration   
@after { Tree docComment = getDocComment($attributeDeclaration.start);
         $attributeDeclaration.tree.addChild(docComment); }
	: varModifierFlags ATTRIBUTE  name  typeReference (EQ boundExpression)? onReplaceClause?
	    					-> ^(VAR ATTRIBUTE varModifierFlags name typeReference boundExpression? onReplaceClause?)
	;
overrideDeclaration
	: OVERRIDE ATTRIBUTE  name (EQ boundExpression)? onReplaceClause?
	    					-> ^(OVERRIDE name boundExpression? onReplaceClause?)
;
initDefinition
	: INIT block 				-> ^(INIT block)
	;
postInitDefinition
	: POSTINIT block 			-> ^(POSTINIT block)
	;
//triggerDefinition
//	: WITH name onReplaceClause		-> ^(WITH name onReplaceClause)
//	;

//TODO: modifier flag testing should be done in JavafxAttr, where it would be cleaner and better errors could be generated
functionModifierFlags  
	: BOUND (accessModifier functionModifier? )?	-> ^(MODIFIER accessModifier? functionModifier? BOUND?)
	| accessModifier BOUND functionModifier		-> ^(MODIFIER accessModifier? functionModifier? BOUND?)
	| accessModifier functionModifier? BOUND?	-> ^(MODIFIER accessModifier? functionModifier? BOUND?)
	| BOUND functionModifier accessModifier?	-> ^(MODIFIER accessModifier? functionModifier? BOUND?)
	| functionModifier BOUND accessModifier?	-> ^(MODIFIER accessModifier? functionModifier? BOUND?)
	| functionModifier (accessModifier BOUND? )?	-> ^(MODIFIER accessModifier? functionModifier? BOUND?)
	| 						-> ^(MODIFIER)
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
	  RBRACE				-> ^(BLOCK[$LBRACE] blockComponent*)
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
//	| functionDefinition 
	| insertStatement 	
	| deleteStatement 
 	| whileStatement
	| BREAK    		
	| CONTINUE  	 	 	
       	| throwStatement	   	
       	| returnStatement 		
       	| tryStatement	
       	;
variableDeclaration   
@after { Tree docComment = getDocComment($variableDeclaration.start);
         $variableDeclaration.tree.addChild(docComment); }
	: varModifierFlags variableLabel  name  typeReference (EQ boundExpression)? onReplaceClause?
	    					-> ^(VAR variableLabel varModifierFlags name typeReference boundExpression? onReplaceClause?)
	;
onReplaceClause
	: ON REPLACE oldval=paramNameOpt clause=sliceClause? block
						-> ^(ON_REPLACE_SLICE[$ON] $oldval $clause? block)
	;
sliceClause
	: LBRACKET first=name DOTDOT last=name RBRACKET EQ newElements=name
						-> ^(SLICE_CLAUSE $first $last $newElements)
	| EQ newValue=name
						-> ^(SLICE_CLAUSE MISSING_NAME MISSING_NAME $newValue)
	;
paramNameOpt
        : name                                  -> name
        |                                       -> MISSING_NAME
        ;
variableLabel 
	: VAR	
	| LET	
	;
throwStatement
	: THROW expression 			-> ^(THROW expression)
	;
whileStatement
	: WHILE LPAREN expression RPAREN block	-> ^(WHILE expression block)
	;
insertStatement  
	: INSERT elem=expression
		( INTO eseq=expression 		-> ^(INTO $elem $eseq )
		| BEFORE where=expression	-> ^(BEFORE $elem $where)
		| AFTER where=expression	-> ^(AFTER $elem $where)
		)
	;
deleteStatement  
	: DELETE  e1=expression  
	   ( FROM e2=expression 		-> ^(FROM $e1 $e2)
	   | /* indexed and whole cases */	-> ^(DELETE $e1)
	   )
	;
returnStatement
	: RETURN expression?			-> ^(RETURN expression?)
	;
tryStatement
	: TRY block 			
	   ( finallyClause
	   | catchClause+ finallyClause?   
	   ) 					-> ^(TRY block catchClause* finallyClause?)
	;
finallyClause
	: FINALLY block				-> ^(FINALLY block)
	;
catchClause
	: CATCH LPAREN formalParameter RPAREN block
						-> ^(CATCH formalParameter block)
	;
boundExpression 
	: BIND expression (WITH INVERSE)?
						-> ^(BIND INVERSE? expression)
	| expression				-> ^(EXPRESSION expression)
	;
expression 
       	: ifExpression   		
       	| forExpression   	
       	| newExpression 	
	| assignmentExpression	
        | keyValueLiteralExpression
      	;
keyValueLiteralExpression
        : qualname SUCHTHAT 
                ( interpolatedExpression                       
                                                                -> ^(SUCHTHAT qualname interpolatedExpression)
                | LBRACE attributedInterpolatedExpression       
                         (COMMA attributedInterpolatedExpression)*                           
                  RBRACE                                        -> ^(SUCHTHAT qualname attributedInterpolatedExpression+) 
                )
        ;
interpolatedExpression
        : andExpression tweenExpression?      -> ^(TWEEN andExpression tweenExpression?)
        ;

attributedInterpolatedExpression
        : qualname COLON 
                        (interpolatedExpression                     -> ^(ATTR_INTERPOLATE qualname interpolatedExpression)
 //                       | LBRACE keyValueLiteralExpression RBRACE   -> ^(ATTR_INTERPOLATE qualname keyValueLiteralExpression)
                        )
        ;
tweenExpression
        : TWEEN andExpression                                   -> andExpression
        ;
forExpression
	: FOR LPAREN inClause (COMMA inClause)* RPAREN expression
								-> ^(FOR inClause* expression)
	;
inClause
	: formalParameter IN in=expression (WHERE wh=expression)?
								-> ^(IN formalParameter $in $wh?)
	;
ifExpression 
	: IF LPAREN econd=expression  RPAREN 
		THEN?  ethen=expression  elseClause 		-> ^(IF $econd $ethen elseClause?)
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
	   (   PLUSEQ   e2=expression				-> ^(PLUSEQ $e1 $e2) 
	   |   SUBEQ   e2=expression				-> ^(SUBEQ $e1 $e2) 
	   |   STAREQ   e2=expression				-> ^(STAREQ $e1 $e2) 
	   |   SLASHEQ   e2=expression				-> ^(SLASHEQ $e1 $e2) 
	   |   PERCENTEQ   e2=expression	{ log.warning(pos($PERCENTEQ), MsgSym.MESSAGE_JAVAFX_GENERALWARNING, "The operator \%= will not be supported in the JavaFX 1.0 release" );}			
                                                                -> ^(PERCENTEQ $e1 $e2) 
/*	   | SUCHTHAT expr=andExpression (TWEEN interpolate=andExpression)?
                                                               -> ^(SUCHTHAT $e1 $expr $interpolate?)*/
	   |							-> $e1
	   )
	;
andExpression  
	: ( e1=orExpression					-> $e1 )		
	  (   AND   e2=orExpression				-> ^(AND $andExpression $e2) )*
	;
orExpression  
	: ( e1=typeExpression					-> $e1 )		
	  (   OR   e2=typeExpression				-> ^(OR $orExpression $e2) )*
	;
typeExpression 
	: relationalExpression		
	   (   INSTANCEOF itn=type				-> ^(INSTANCEOF relationalExpression $itn)
	   |   AS atn=type					-> ^(AS relationalExpression $atn)
	   | 							-> relationalExpression
	   )
	;
relationalExpression  
	: ( additiveExpression					-> additiveExpression )
	   (   LTGT   e=additiveExpression      { log.warning(pos($LTGT), MsgSym.MESSAGE_JAVAFX_GENERALWARNING, "The not-equal operator <> will be replaced by !=" );}	
                                                                -> ^(LTGT $relationalExpression $e)
           |   NOTEQ  e=additiveExpression                      -> ^(NOTEQ $relationalExpression $e)
	   |   EQEQ   e=additiveExpression			-> ^(EQEQ $relationalExpression $e)
	   |   LTEQ   e=additiveExpression			-> ^(LTEQ $relationalExpression $e)
	   |   GTEQ   e=additiveExpression			-> ^(GTEQ $relationalExpression $e)
	   |   LT     e=additiveExpression			-> ^(LT   $relationalExpression $e)
	   |   GT     e=additiveExpression			-> ^(GT   $relationalExpression $e)
	   ) * 
	;
additiveExpression 
	: ( multiplicativeExpression				-> multiplicativeExpression )
	   (   PLUS   e=multiplicativeExpression		-> ^(PLUS $additiveExpression $e)
	   |   SUB    e=multiplicativeExpression		-> ^(SUB  $additiveExpression $e)
	   ) * 
	;
multiplicativeExpression
	: ( unaryExpression					-> unaryExpression )
	   (   STAR    e=unaryExpression			-> ^(STAR    $multiplicativeExpression $e)
	   |   SLASH   e=unaryExpression			-> ^(SLASH   $multiplicativeExpression $e)
	   |   PERCENT e=unaryExpression	{ log.warning(pos($PERCENT), MsgSym.MESSAGE_JAVAFX_GENERALWARNING, "The remainder operator \% will be replaced by mod" );}	
                                                                -> ^(PERCENT $multiplicativeExpression $e)
           |   MOD     e=unaryExpression			-> ^(MOD $multiplicativeExpression $e)
	   ) * 
	;
//TODO: POUND QUES TYPEOF REVERSE
unaryExpression 
	: suffixedExpression					-> suffixedExpression
	| SUB      e=unaryExpression				-> ^(NEGATIVE[$SUB] $e)
	| NOT      e=unaryExpression				-> ^(NOT $e)		
	| SIZEOF   e=unaryExpression				-> ^(SIZEOF $e)	
	| PLUSPLUS e=unaryExpression				-> ^(PLUSPLUS $e)   	
	| SUBSUB   e=unaryExpression				-> ^(SUBSUB $e)
	| REVERSE  e=unaryExpression				-> ^(REVERSE $e)
        | INDEXOF  name                      			-> ^(INDEXOF name)	
	;
suffixedExpression 
	: postfixExpression
		( PLUSPLUS					-> ^(POSTINCR[$PLUSPLUS] postfixExpression)
		| SUBSUB					-> ^(POSTDECR[$SUBSUB] postfixExpression)
		|						-> postfixExpression
		)
	;
postfixExpression 
	: ( primaryExpression 					-> primaryExpression )
	   ( DOT ( name						-> ^(DOT $postfixExpression name)
//TODO:		 | CLASS   					
	         )   
	   | LPAREN expressionList RPAREN           		-> ^(FUNC_APPLY[$LPAREN] $postfixExpression expressionList)
	   | LBRACKET (name PIPE expression RBRACKET		-> ^(PIPE $postfixExpression name expression)
	     | first=expression
               (RBRACKET					-> ^(SEQ_INDEX[$LBRACKET] $postfixExpression $first)
	       | DOTDOT (
	                  LT last=expression? 			-> ^(SEQ_SLICE_EXCLUSIVE[$LBRACKET] $postfixExpression $first $last?)
	                | last=expression? 			-> ^(SEQ_SLICE[$LBRACKET] $postfixExpression $first $last?)
	                )
	         RBRACKET
               )
             )
	   ) * 
	;
primaryExpression  
	: qualname					
		( LBRACE  objectLiteralPart* RBRACE 		-> ^(OBJECT_LIT[$LBRACE] qualname objectLiteralPart*)
		|						-> qualname
		)
       	| THIS							-> THIS
       	| SUPER							-> SUPER
       	| stringExpression 					-> stringExpression
       	| bracketExpression 					-> bracketExpression
        | blockExpression 					-> blockExpression
       	| literal 						-> literal
      	| functionExpression					-> functionExpression
       	| LPAREN expression RPAREN				-> expression
        | AT LPAREN TIME_LITERAL RPAREN LBRACE keyFrameLiteralPart* RBRACE    -> ^(AT TIME_LITERAL keyFrameLiteralPart*)
       	;        
keyFrameLiteralPart
        : keyValueLiteralExpression (SEMI)?                     -> keyValueLiteralExpression
        ;
functionExpression  
	: FUNCTION formalParameters typeReference blockExpression
								-> ^(FUNC_EXPR[$FUNCTION] formalParameters typeReference blockExpression)
	;
newExpression 
	: NEW typeName expressionListOpt			-> ^(NEW typeName expressionListOpt)
	;
objectLiteralPart  
	: name COLON  boundExpression (COMMA | SEMI)?		-> ^(OBJECT_LIT_PART[$COLON] name boundExpression)
       	| variableDeclaration	(COMMA | SEMI)?			-> variableDeclaration
       	| attributeDeclaration	(COMMA | SEMI)?			-> attributeDeclaration
	| overrideDeclaration	(COMMA | SEMI)?			-> overrideDeclaration
       	| functionDefinition 	(COMMA | SEMI)?			-> functionDefinition
       	;
stringExpression  
	: TRANSLATION_KEY
	  STRING_LITERAL			-> ^(TRANSLATION_KEY
                                                        STRING_LITERAL)
	| QUOTE_LBRACE_STRING_LITERAL
	  stringFormat	
	  expression 	
	  stringExpressionInner*   
	  RBRACE_QUOTE_STRING_LITERAL		-> ^(QUOTE_LBRACE_STRING_LITERAL 
	  							stringFormat expression 
	  							stringExpressionInner* 
	  							RBRACE_QUOTE_STRING_LITERAL)
	| TRANSLATION_KEY
          QUOTE_LBRACE_STRING_LITERAL
	  stringFormat	
	  expression 	
	  stringExpressionInner*   
	  RBRACE_QUOTE_STRING_LITERAL		-> ^(TRANSLATION_KEY
                                                                QUOTE_LBRACE_STRING_LITERAL 
	  							stringFormat expression 
	  							stringExpressionInner* 
	  							RBRACE_QUOTE_STRING_LITERAL)
	;
stringExpressionInner
	: RBRACE_LBRACE_STRING_LITERAL stringFormat expression 			
	;
stringFormat  
	: FORMAT_STRING_LITERAL			-> FORMAT_STRING_LITERAL
	| /* no formar */			-> EMPTY_FORMAT_STRING
	;
bracketExpression  
	: LBRACKET   
	    ( /*nada*/				-> ^(SEQ_EMPTY[$LBRACKET])
	    | e1=expression 	
	     	(   /*nada*/			-> ^(SEQ_EXPLICIT[$LBRACKET] expression*)
	     	| COMMA 
	     	   (   /*nada*/			-> ^(SEQ_EXPLICIT[$LBRACKET] expression*)
            	| expression 		
	     	         (COMMA expression)*
                          COMMA?
	     	       				-> ^(SEQ_EXPLICIT[$LBRACKET] expression*)
                    )
	     	| DOTDOT   LT? dd=expression	
	     	    ( STEP st=expression )?
	     					-> ^(DOTDOT $e1 $dd $st? LT?)
	     	)   
	    )
	  RBRACKET 
	;
expressionList
	: (expression (COMMA expression)*)?
						-> ^(EXPR_LIST expression*)
	;
expressionListOpt  
	: LPAREN expressionList RPAREN		-> expressionList
	|					-> ^(EXPR_LIST)
	;
type 
	: typeName cardinality			-> ^(TYPE_NAMED typeName cardinality?)
 	| FUNCTION LPAREN typeArgList
          	   RPAREN typeReference 
          	   	cardinality	//TODO: this introduces an ambiguity: return cardinality vs type cardinality
          	   				-> ^(TYPE_FUNCTION[$FUNCTION] typeArgList typeReference cardinality?)
 	| STAR cardinality			-> ^(TYPE_ANY[$STAR] cardinality?)
 	;
typeArgList
 	: typeArg (COMMA typeArg)*		-> ^(TYPED_ARG_LIST typeArg*)
 	| /* emprty list */			-> ^(TYPED_ARG_LIST)
	;
typeArg 
 	: (name? COLON)? type			-> ^(COLON name? type)
 	;
typeReference 
 	: COLON type				-> type
 	| /*nada*/				-> ^(TYPE_UNKNOWN)
 	;
cardinality 
	: (LBRACKET)=>LBRACKET RBRACKET 	-> RBRACKET
	|                         		->
	;
typeName  
	: qualname 		
		(LT genericArgument (COMMA genericArgument)* GT { log.error(pos($LT), "javafx.generalerror", "Java generic type declarations are not currently supported"); }
						-> ^(TYPE_ARG[$LT] qualname genericArgument+)
		|				-> qualname
		)
	;
genericArgument 
	: typeName				-> typeName
	| QUES (  ( EXTENDS 		
		  | SUPER		
		  ) 
		 typeName		
	       )?				-> ^(QUES EXTENDS? SUPER? typeName?)
	;
literal  
	: STRING_LITERAL	
	| DECIMAL_LITERAL	
	| OCTAL_LITERAL		
	| HEX_LITERAL
        | TIME_LITERAL			
	| FLOATING_POINT_LITERAL 
	| TRUE   		
	| FALSE   		
	| NULL 		
	;
qualname 
	: ( name				-> name )
          ( (DOT)=> DOT nn=name     		-> ^(DOT $qualname $nn)
          ) *  
	;
name 
	: IDENTIFIER						
	;
