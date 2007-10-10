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

grammar interp;

options { backtrack = true; superClass = AbstractGeneratedParser; }
 
tokens {
   BAR='|';
   POUND='#';
   TYPEOF='typeof';
   DOTDOT='..';
   LARROW='<-';
   ABSTRACT='abstract';
   AFTER='after';
   AND='and';
   AS='as';
   ASSERT='assert';
   ATTRIBUTE='attribute';
   BEFORE='before';
   BIND='bind';
   BIBIND='bibind';
   BREAK='break';
   BY='by';
   CATCH='catch';
   CLASS='class';
   DELETE='delete';
   DISTINCT='distinct';
   DO='do';
   DUR='dur';
   EASEBOTH='easeboth';
   EASEIN='easein';
   EASEOUT='easeout';
   TIE='tie';
   STAYS='stays';
   RETURN='return';
   THROW='throw';
   VAR='var';
   PACKAGE='package';
   IMPORT='import';
   FROM='from';
   LATER='later';
   TRIGGER='trigger';
   ON='on';
   INSERT='insert';
   INTO='into';
   FIRST='first';
   LAST='last';
   IF='if';
   THEN='then';
   ELSE='else';
   THIS='this';
   NULL='null';
   TRUE='true';
   FALSE='false';
   FOR='for';
   UNITINTERVAL='unitinterval';
   IN='in';
   FPS='fps';
   WHILE='while';
   CONTINUE='continue';
   LINEAR='linear';
   MOTION='motion';
   TRY='try';
   FINALLY='finally';
   LAZY='lazy';
   FOREACH='foreach';
   WHERE='where';
   NOT='not';
   NEW='new';
   PRIVATE='private';
   PROTECTED='protected';
   PUBLIC='public';
   OPERATION='operation';
   FUNCTION='function';
   READONLY='readonly';
   INVERSE='inverse';
   TYPE='type';
   EXTENDS='extends';
   ORDER='order';
   INDEX='index';
   INSTANCEOF='instanceof';
   INDEXOF='indexof';
   SELECT='select';
   SUPER='super';
   OR='or';
   SIZEOF='sizeof';
   REVERSE='reverse';
   XOR='xor';
   LPAREN='(';
   RPAREN=')';
   LBRACKET='[';
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
   PLUSPLUS='++';
   SUB='-';
   SUBSUB='--';
   STAR='*';
   SLASH='/';
   PERCENT='%';
   PLUSEQ='+=';
   SUBEQ='-=';
   STAREQ='*=';
   SLASHEQ='/=';
   PERCENTEQ='%=';
   LTLT='<<';
   GTGT='>>';
   COLON=':';
   QUES='?';
}

@lexer::header {
package com.sun.tools.migrator.antlr;
}

@header {
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
}

@members {
        public interpParser(Context context, char[] content) {
           this(new CommonTokenStream(new interpLexer(new ANTLRStringStream(content.toString()))));
           initialize(context);
    	}
}

@lexer::members {
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
    
    void removeQuotes() {
    	setText(getText().substring(1, getText().length()-1));
    }
    
    // quote context --
    static final int CUR_QUOTE_CTX	= 0;	// 0 = use current quote context
    static final int SNG_QUOTE_CTX	= 1;	// 1 = single quote quote context
    static final int DBL_QUOTE_CTX	= 2;	// 2 = double quote quote context
 }

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

STRING_LITERAL  		: '"' (~('{' |'"'))* '"'  	{ removeQuotes(); }
				| '\'' (~('{' |'\''))* '\''  	{ removeQuotes(); }
				;
// String Expression token implementation
QUOTE_LBRACE_STRING_LITERAL 	: '"' (~('{' |'"'))* '{'   	{ removeQuotes(); }
				  NextIsPercent[DBL_QUOTE_CTX] 
				| '\'' (~('{' |'\''))* '{'   	{ removeQuotes(); }
				  NextIsPercent[SNG_QUOTE_CTX] 
				;
LBRACE				: '{'				{ BraceQuoteTracker.enterBrace(0, false); } 
				;
RBRACE_QUOTE_STRING_LITERAL 	:				{ BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) }?=>
				  '}' (~('{' |'"'))* '"'	{ BraceQuoteTracker.leaveBrace(); 
				         			  BraceQuoteTracker.leaveQuote(); 
				         			  removeQuotes(); }
				|				{ BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) }?=>
				  '}' (~('{' |'\''))* '\''	{ BraceQuoteTracker.leaveBrace(); 
				         			  BraceQuoteTracker.leaveQuote(); 
				         			  removeQuotes(); }
				;
RBRACE_LBRACE_STRING_LITERAL 	:				{ BraceQuoteTracker.rightBraceLikeQuote(DBL_QUOTE_CTX) }?=>
				  '}' (~('{' |'"'))* '{'	{ BraceQuoteTracker.leaveBrace(); 
				         			  removeQuotes(); }
				   NextIsPercent[CUR_QUOTE_CTX]	
				|				{ BraceQuoteTracker.rightBraceLikeQuote(SNG_QUOTE_CTX) }?=>
				  '}' (~('{' |'\''))* '{'	{ BraceQuoteTracker.leaveBrace(); 
				         			  removeQuotes(); }
				   NextIsPercent[CUR_QUOTE_CTX]	
				;
RBRACE				:				{ !BraceQuoteTracker.rightBraceLikeQuote(CUR_QUOTE_CTX) }?=>
				  '}'				{ BraceQuoteTracker.leaveBrace(); }
				;
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
	:	'<<' (~'>'| '>' ~'>')* '>'* '>>'   { setText(getText().substring(2, getText().length()-2)); };
 
INTEGER_LITERAL : ('0' | '1'..'9' '0'..'9'*) ;

FLOATING_POINT_LITERAL
    :   ('0'..'9')+ '.' ('0'..'9')* Exponent? 
    |   '.' ('0'..'9')+ Exponent? 
    |   ('0'..'9')+ Exponent 
	;

fragment
Exponent : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

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

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

module returns [MTCompilationUnit result]
       : packageDecl? moduleItems EOF 
            { $result = F.TopLevel($packageDecl.value, $moduleItems.items.toList()); };
packageDecl returns [MTExpression value]
       : PACKAGE qualident SEMI        { $value = $qualident.expr; };
moduleItems returns [ListBuffer<MTTree> items = new ListBuffer<MTTree>()]
       	: (moduleItem                  { items.append($moduleItem.value); } )*  ;
moduleItem returns [MTTree value]
       : importDecl			{ $value = $importDecl.value; }
       | classDefinition 		{ $value = $classDefinition.value; }
       | attributeDefinition 		
       | memberOperationDefinition 	
       | memberFunctionDefinition 	
       | TRIGGER ON changeRule		
       | statementExcept 		{ $value = $statementExcept.value; } ;
importDecl returns [MTTree value]
@init { MTExpression pid = null; }
        : IMPORT  identifier		{ pid = $identifier.expr; }
                 ( DOT name		{ pid = F.at($name.pos).Select(pid, $name.value); } )* 
                 ( DOT STAR		{ pid = F.at(pos($STAR)).Select(pid, names.asterisk); } )? SEMI 
          { $value = F.at(pos($IMPORT)).Import(pid, false); } ;
classDefinition returns [MTClassDeclaration value]
	: modifierFlags  CLASS name supers LBRACE classMembers RBRACE 
	  				{ $value = F.at(pos($CLASS)).ClassDeclaration($modifierFlags.mods, 
	  							$name.value,
	                                	                $supers.ids.toList(), 
	                                	                $classMembers.mems.toList()); } 
	;
supers returns [ListBuffer<MTExpression> ids = new ListBuffer<MTExpression>()]
	: (EXTENDS id1=qualident        { $ids.append($id1.expr); }
           ( COMMA idn=qualident        { $ids.append($idn.expr); } )* 
	  )?
	;
classMembers returns [ListBuffer<MTTree> mems = new ListBuffer<MTTree>()]
	:( attributeDecl            
	|  functionDecl             
	|  operationDecl           
	) *   ;
attributeDecl 
	: modifierFlags ATTRIBUTE name typeReference inverseClause  (orderBy | indexOn)? SEMI 
		 ;
inverseClause returns [MTMemberSelector inverse = null]
	: (INVERSE memberSelector { inverse = $memberSelector.value; } )? ;
functionDecl 
	: modifierFlags FUNCTION name formalParameters typeReference SEMI 
		 ;

operationDecl 
	: modifierFlags   OPERATION   name   formalParameters   typeReference    SEMI 
		 ;
attributeDefinition  
	: ATTRIBUTE   memberSelector   EQ bindOpt  expression   SEMI 
	 ;
memberOperationDefinition  
	: OPERATION   memberSelector   formalParameters   typeReference  block 
		 ;
memberFunctionDefinition  
	: FUNCTION   memberSelector   formalParameters   typeReference  block /*TODO functionBody */
		 ;
functionBody // TODO
	: EQ   expression   whereVarDecls ?   SEMI    
        | LBRACE   (   variableDefinition   |   localFunctionDefinition   |   localOperationDefinition   ) *   RETURN   expression   SEMI ?   RBRACE ;
whereVarDecls : WHERE   whereVarDecl   (   COMMA   whereVarDecl   ) * ;
whereVarDecl : localFunctionDefinition 
       | name   typeReference   EQ   expression ;
variableDefinition : VAR   name   typeReference  EQ   expression   SEMI ;
changeRule  
	: LPAREN   NEW   typeName  RPAREN  block
	| LPAREN   memberSelector  EQ identifier   RPAREN  block
	| memberSelector  EQ identifier block
	| LPAREN   memberSelector   LBRACKET   id1=identifier   RBRACKET   EQ id2=identifier   RPAREN  block
	| LPAREN   INSERT   identifier   INTO   memberSelector   RPAREN block 	
	| LPAREN   DELETE   identifier   FROM   memberSelector   RPAREN block 	
	| LPAREN   DELETE  memberSelector   LBRACKET   identifier   RBRACKET   RPAREN block
	;
modifierFlags returns [MTModifiers mods]
@init { long flags = 0; }
	: ( om1=otherModifier 			{ flags |= $om1.flags; }
		( am1=accessModifier  		{ flags |= $am1.flags; }  )?    
	  | am2=accessModifier 			{ flags |= $am2.flags; }
		( om2=otherModifier  		{ flags |= $om2.flags; }  )?  
	  ) ?         				{ $mods = F.Modifiers(flags); } 
	;
accessModifier returns [long flags = 0]
	: (PUBLIC          { flags |= Flags.PUBLIC; }
	|  PRIVATE         { flags |= Flags.PUBLIC; }
	|  PROTECTED       { flags |= Flags.PROTECTED; } ) ;
otherModifier returns [long flags = 0]
	: (ABSTRACT        { flags |= Flags.ABSTRACT; }
	|  READONLY        { flags |= Flags.FINAL; } ) ;
memberSelector returns [MTMemberSelector value]
	: name1=name   DOT   name2=name		{ $value = F.at($name1.pos).MemberSelector($name1.value, $name2.value); } ;
formalParameters returns [ListBuffer<MTTree> params = new ListBuffer<MTTree>()]
	: LPAREN   ( fp0=formalParameter		{ params.append($fp0.var); }
	           ( COMMA   fpn=formalParameter	{ params.append($fpn.var); } )* )?  RPAREN ;
formalParameter returns [MTVar var]
	: name typeReference			{ $var = F.at($name.pos).Var($name.value, $typeReference.type, F.Modifiers(Flags.PARAMETER), null, JavafxBindStatus.UNBOUND); } ;
block returns [MTBlock value]
	: LBRACE   statements   RBRACE		{ $value = F.at(pos($LBRACE)).Block(0L, $statements.stats.toList()); }
	;
statements returns [ListBuffer<MTStatement> stats = new ListBuffer<MTStatement>()]
	: (statement                            { stats.append($statement.value); } )* ;
statement returns [MTStatement value]
       : statementExcept 			{ $value = $statementExcept.value; }
       | localTriggerStatement			{ $value = $localTriggerStatement.value; } ;
statementExcept  returns [MTStatement value]
	: variableDeclaration 			{ $value = $variableDeclaration.value; }
	| localFunctionDefinition		{ $value = $localFunctionDefinition.value; }
	| localOperationDefinition		{ $value = $localOperationDefinition.value; }
       | backgroundStatement 			//{ $value = $backgroundStatement.value; }
       | laterStatement 			//{ $value = $laterStatement.value; }
       | WHILE LPAREN expression RPAREN block	{ $value = F.at(pos($WHILE)).WhileLoop($expression.expr, $block.value); }
       | ifStatement 				{ $value = $ifStatement.value; }
       | insertStatement 			{ $value = $insertStatement.value; }
       | deleteStatement 			{ $value = $deleteStatement.value; }
	| expression   SEMI 			{ $value = F.at(pos($SEMI)).Exec($expression.expr); }
	| BREAK   SEMI 				{ $value = F.at(pos($BREAK)).Break(null); }
	| CONTINUE   SEMI 			{ $value = F.at(pos($CONTINUE)).Continue(null); }
       | throwStatement 			{ $value = $throwStatement.value; }
       | returnStatement 			{ $value = $returnStatement.value; }
       | forAlphaStatement 			{ $value = $forAlphaStatement.value; }
       | forJoinStatement 			{ $value = $forJoinStatement.value; }
       | tryStatement				{ $value = $tryStatement.value; } ;
assertStatement  returns [MTStatement value = null]
	: ASSERT   expression   (   COLON   expression   ) ?   SEMI ;
localOperationDefinition   returns [MTStatement value]
	: OPERATION   name   formalParameters   typeReference  block 
		 ;
localFunctionDefinition   returns [MTStatement value]
	: FUNCTION ?   name   formalParameters   typeReference  block // TODO? functionBody 
		 ;
variableDeclaration   returns [MTStatement value]
	: VAR  name  typeReference  
	    ( EQ bindOpt  expression SEMI	{ $value = F.at(pos($VAR)).Var($name.value, $typeReference.type, F.Modifiers(Flags.PARAMETER),
	    							$expression.expr, $bindOpt.status); }
	    | SEMI				{ $value = F.at(pos($VAR)).Var($name.value, $typeReference.type, F.Modifiers(Flags.PARAMETER),
	    							$expression.expr, $bindOpt.status); } 
	    )   
	   ;
bindOpt   returns [JavafxBindStatus status = UNBOUND]
	: ( BIND 				{ $status = UNIDIBIND; }
	      (LAZY				{ $status = LAZY_UNIDIBIND; } )?
	  | STAYS 				{ $status = UNIDIBIND; }
	      (LAZY				{ $status = LAZY_UNIDIBIND; } )?
	  | TIE					{ $status = BIDIBIND; }
	      (LAZY				{ $status = LAZY_BIDIBIND; } )?
	  )? ;
backgroundStatement   // TODO remove?
	: DO   block ;
laterStatement       // TODO remove?
	: DO   LATER   block ;
ifStatement   returns [MTStatement value]
@init { MTStatement elsepart = null; }
	: IF   LPAREN   expression   RPAREN   s1=block (ELSE  s2=block { elsepart = $s2.value; }) ? 
						{ $value = F.at(pos($IF)).If($expression.expr, $s1.value, elsepart); } ;
insertStatement   returns [MTStatement value = null]
	: INSERT   (   DISTINCT   expression   INTO   expression   |   expression   (   (   (   AS   (   FIRST   |   LAST   )   ) ?   INTO   expression   )   |   AFTER   expression   |   BEFORE   expression   )   )     SEMI ;
deleteStatement   returns [MTStatement value = null]
	: DELETE   expression   SEMI ;
throwStatement   returns [MTStatement value = null]
	: THROW   expression   SEMI ;
returnStatement   returns [MTStatement value]
@init { MTExpression expr = null; }
	: RETURN (expression { expr = $expression.expr; } )? SEMI 
						{ $value = F.at(pos($RETURN)).Return(expr); } 
	;
localTriggerStatement   returns [MTStatement value = null]
	: TRIGGER   ON    ( localTriggerCondition | LPAREN   localTriggerCondition   RPAREN)  block ;
localTriggerCondition   returns [MTStatement value = null]
	: name   (   LBRACKET   name   RBRACKET   ) ?   EQ   expression 
       | INSERT   name   INTO   ( name   EQ ) /*?*/   expression 
       | DELETE   name   FROM   ( name   EQ ) /*?*/   expression ;
forAlphaStatement   returns [MTStatement value = null]
	: FOR   LPAREN   alphaExpression   RPAREN   block ;
alphaExpression : UNITINTERVAL   IN   DUR   expression   (   FPS   expression   ) ?   (   WHILE   expression   ) ?   (   CONTINUE   IF   expression   ) ? ;
forJoinStatement   returns [MTStatement value = null]
	: FOR   LPAREN   joinClause   RPAREN   (   LPAREN   durClause   RPAREN   ) ?   block ;
joinClause : name   IN   expression   (   COMMA   name   IN   expression   ) *   (   WHERE   expression   ) ? ;
durClause : DUR   expression   (   LINEAR   |   EASEIN   |   EASEOUT   |   EASEBOTH   |   MOTION   expression   ) ?   (   FPS   expression ) ?   (   WHILE   expression   ) ?   (   CONTINUE   IF   expression   ) ? ;
tryStatement   returns [MTStatement value = null]
	: TRY   block   (   FINALLY   block   |     catchClause +   (   FINALLY   block   ) ?   ) ;
catchClause : CATCH   LPAREN   name   typeReference ?   (   IF   expression   ) ?   RPAREN   block ;
expression returns [MTExpression expr] 
	: foreach 
       	| functionExpression 
       	| operationExpression 
       	| alphaExpression 
       	| ifExpression    //TODO: disabled for now? causes ambiguity, will be merged with if-statement
       	| selectExpression 
       	| LPAREN   /*MT:typeSpec*/ typeName  RPAREN   suffixedExpression   
       	| suffixedExpression		{ $expr = $suffixedExpression.expr; }  ;
foreach : FOREACH   LPAREN   name   IN   expression   (   COMMA   name   IN   expression   ) *   (   WHERE   expression   ) ?   RPAREN   expression ;
functionExpression : FUNCTION   formalParameters   typeReference ?   functionBody ;
operationExpression : OPERATION   formalParameters   typeReference ?   block ;
ifExpression : IF   expression   THEN   expression   ELSE   expression ;
selectExpression : SELECT   DISTINCT ?     expression   FROM   selectionVar   (   COMMA   selectionVar   ) *   (   WHERE   expression   ) ? ;
selectionVar : name   (   IN   expression   ) ? ;
suffixedExpression  returns [MTExpression expr] 
	: e1=assignmentExpression				{ $expr = $e1.expr; }
	   (indexOn | orderBy | durClause | PLUSPLUS | SUBSUB) ? ;  //TODO
assignmentExpression  returns [MTExpression expr] 
	: e1=assignmentOpExpression				{ $expr = $e1.expr; }
	   (   EQ   e2=assignmentOpExpression			{ $expr = F.at(pos($EQ)).Assign($expr, $e2.expr); }   ) ? ;
assignmentOpExpression  returns [MTExpression expr] 
	: e1=andExpression					{ $expr = $e1.expr; }
	   (   assignmentOperator   e2=andExpression		{ $expr = F.Assignop($assignmentOperator.optag,
	   													$expr, $e2.expr); }   ) ? ;
andExpression  returns [MTExpression expr] 
	: e1=orExpression					{ $expr = $e1.expr; }
	   (   AND   e2=orExpression				{ $expr = F.at(pos($AND)).Binary(MTTree.AND, $expr, $e2.expr); }   ) * ;
orExpression  returns [MTExpression expr] 
	: e1=instanceOfExpression				{ $expr = $e1.expr; }
	   (   OR   e2=instanceOfExpression			{ $expr = F.at(pos($OR)).Binary(MTTree.OR, $expr, $e2.expr); }    ) * ;
instanceOfExpression  returns [MTExpression expr] 
	: e1=relationalExpression				{ $expr = $e1.expr; }
	   (   INSTANCEOF identifier				{ $expr = F.at(pos($INSTANCEOF)).Binary(MTTree.TYPETEST, $expr, 
	   													 $identifier.expr); }   ) ? ;
relationalExpression  returns [MTExpression expr] 
	: e1=additiveExpression					{ $expr = $e1.expr; }
	   (   LTGT   e=additiveExpression			{ $expr = F.at(pos($LTGT)).Binary(MTTree.NE, $expr, $e.expr); }
	   |   EQEQ   e=additiveExpression			{ $expr = F.at(pos($EQEQ)).Binary(MTTree.EQ, $expr, $e.expr); }
	   |   LTEQ   e=additiveExpression			{ $expr = F.at(pos($LTEQ)).Binary(MTTree.LE, $expr, $e.expr); }
	   |   GTEQ   e=additiveExpression			{ $expr = F.at(pos($GTEQ)).Binary(MTTree.GE, $expr, $e.expr); }
	   |   LT     e=additiveExpression			{ $expr = F.at(pos($LT))  .Binary(MTTree.LT, $expr, $e.expr); }
	   |   GT     e=additiveExpression			{ $expr = F.at(pos($GT))  .Binary(MTTree.GT, $expr, $e.expr); }
	   |   IN     e=additiveExpression			{ /* $expr = F.at(pos($IN  )).Binary(JavaFXTag.IN, $expr, $e2.expr); */ }
	   ) * ;
additiveExpression  returns [MTExpression expr] 
	: e1=multiplicativeExpression				{ $expr = $e1.expr; }
	   (   PLUS   e=multiplicativeExpression		{ $expr = F.at(pos($PLUS)).Binary(MTTree.PLUS , $expr, $e.expr); }
	   |   SUB    e=multiplicativeExpression		{ $expr = F.at(pos($SUB)) .Binary(MTTree.MINUS, $expr, $e.expr); }
	   ) * ;
multiplicativeExpression  returns [MTExpression expr] 
	: e1=unaryExpression					{ $expr = $e1.expr; }
	   (   STAR    e=unaryExpression			{ $expr = F.at(pos($STAR))   .Binary(MTTree.MUL  , $expr, $e.expr); }
	   |   SLASH   e=unaryExpression			{ $expr = F.at(pos($SLASH))  .Binary(MTTree.DIV  , $expr, $e.expr); }
	   |   PERCENT e=unaryExpression			{ $expr = F.at(pos($PERCENT)).Binary(MTTree.MOD  , $expr, $e.expr); }   
	   ) * ;
unaryExpression  returns [MTExpression expr] 
	: postfixExpression					{ $expr = $postfixExpression.expr; }
	| unaryOperator   postfixExpression			{ $expr = F.Unary($unaryOperator.optag, $postfixExpression.expr); }
	;
postfixExpression  returns [MTExpression expr] 
	: primaryExpression 					{ $expr = $primaryExpression.expr; }
	   ( DOT ( CLASS   					//TODO
	         | name1=name   				{ $expr = F.at(pos($DOT)).Select($expr, $name1.value); }
	            ( LPAREN expressionListOpt RPAREN   	{ $expr = F.at(pos($LPAREN)).Apply(null, $expr, $expressionListOpt.args.toList()); } ) *
	         )   
	   | LBRACKET (name BAR)? expression  RBRACKET		//TODO: selectionClause   
	   ) * ;
primaryExpression  returns [MTExpression expr] 
	: newExpression 					{ $expr = $newExpression.expr; }
	| typeName LBRACE  objectLiteral RBRACE 		{ $expr = F.at(pos($LBRACE)).PureObjectLiteral($typeName.expr, $objectLiteral.parts.toList()); } 
	| bracketExpression 
        //  | typeReference 
	| ordinalExpression 
       | contextExpression 
       | THIS							{ $expr = F.at(pos($THIS)).Identifier(names._this); }
       | SUPER							{ $expr = F.at(pos($SUPER)).Identifier(names._super); }
       | identifier 						{ $expr = $identifier.expr; }
       		( LPAREN   expressionListOpt   RPAREN   	{ $expr = F.at(pos($LPAREN)).Apply(null, $expr, $expressionListOpt.args.toList()); } )*
       | stringExpression 					{ $expr = $stringExpression.expr; }
       | literal 						{ $expr = $literal.expr; }
       | LPAREN expression RPAREN				{ $expr = F.at(pos($LPAREN)).Parens($expression.expr); }
       ;
newExpression  returns [MTExpression expr] 
@init { ListBuffer<MTExpression> args = null; }
	: NEW  typeName  
		( LPAREN   expressionListOpt   RPAREN 		{ args = $expressionListOpt.args; } )?
								{ $expr = F.at(pos($NEW)).Instanciate(null, $typeName.expr, 
												(args==null? new ListBuffer<MTExpression>() : args).toList(), null); }
		   //TODO: need objectLiteral 
	;
objectLiteral  returns [ListBuffer<MTStatement> parts = new ListBuffer<MTStatement>()]
	: ( objectLiteralPart  					{ $parts.append($objectLiteralPart.value); } ) * ;
objectLiteralPart  returns [MTStatement value]
	: name COLON  bindOpt expression (COMMA | SEMI)?	{ $value = F.at(pos($COLON)).ObjectLiteralPart($name.value, $expression.expr, $bindOpt.status); }
       | ATTRIBUTE   name   typeReference   EQ  bindOpt expression   SEMI 
       | localOperationDefinition 
       | localFunctionDefinition 
       | localTriggerStatement 
       | variableDefinition ;
bracketExpression 
	: LBRACKET   
		( /*nada*/
		| expression 
	     		(   /*nada*/
	     		| COMMA expression 
	     			( DOTDOT   LT? expression
	     			| (COMMA   expression  )*
	     			)
	     		| BAR   generator   (COMMA   (generator | expression) )*   
	     		| DOTDOT   LT?  expression
	     		)   
		)
	  RBRACKET 
	;
generator : name   LARROW   expression ;
ordinalExpression : INDEXOF   (   name   |   DOT   ) ;
contextExpression : DOT ;
stringExpression  returns [MTExpression expr] 
@init { ListBuffer<MTExpression> strexp = new ListBuffer<MTExpression>(); }
	: ql=QUOTE_LBRACE_STRING_LITERAL	{ strexp.append(F.at(pos($ql)).Literal(TypeTags.CLASS, $ql.text)); }
	  f1=formatOrNull			{ strexp.append($f1.expr); }
	  e1=expression 			{ strexp.append($e1.expr); }
	  (  rl=RBRACE_LBRACE_STRING_LITERAL	{ strexp.append(F.at(pos($rl)).Literal(TypeTags.CLASS, $rl.text)); }
	     fn=formatOrNull			{ strexp.append($fn.expr); }
	     en=expression 			{ strexp.append($en.expr); }
	  )*   
	  rq=RBRACE_QUOTE_STRING_LITERAL	{ strexp.append(F.at(pos($rq)).Literal(TypeTags.CLASS, $rq.text)); }
	  					{ $expr = F.at(pos($ql)).StringExpression(strexp.toList()); }
	;
formatOrNull  returns [MTExpression expr] 
	: fs=FORMAT_STRING_LITERAL		{ $expr = F.at(pos($fs)).Literal(TypeTags.CLASS, $fs.text); }
	| /* no formar */			{ $expr = null; }
	;
expressionListOpt  returns [ListBuffer<MTExpression> args = new ListBuffer<MTExpression>()] 
	: ( e1=expression		{ $args.append($e1.expr); }
	    (COMMA   e=expression	{ $args.append($e.expr); }  )* )? ;
orderBy returns [MTExpression expr]
	: ORDER   BY   expression	{ expr = $expression.expr; };
indexOn returns [MTExpression expr = null] //TODO
	: INDEX   ON   name   (   COMMA   name   ) * ;
multiplyOperator : STAR   |   SLASH   |   PERCENT ;
unaryOperator  returns [int optag]
	: POUND				{ $optag = 0; } //TODO
	| QUES   			{ $optag = 0; } //TODO
	| SUB   			{ $optag = MTTree.NEG; } 
	| NOT   			{ $optag = MTTree.NOT; } 
	| SIZEOF   			{ $optag = 0; } //TODO
	| TYPEOF   			{ $optag = 0; } //TODO
	| REVERSE   			{ $optag = 0; } //TODO
	| PLUSPLUS   			{ $optag = 0; } //TODO
	| SUBSUB 			{ $optag = 0; } //TODO
	;
assignmentOperator  returns [int optag]
	: PLUSEQ   			{ $optag = MTTree.PLUS_ASG; } 
	| SUBEQ   			{ $optag = MTTree.MINUS_ASG; } 
	| STAREQ   			{ $optag = MTTree.MUL_ASG; } 
	| SLASHEQ   			{ $optag = MTTree.DIV_ASG; } 
	| PERCENTEQ   			{ $optag = MTTree.MOD_ASG; } 
	;
typeReference returns [MTType type]
	: ( COLON  (
                     (options { backtrack = true; } : (FUNCTION | OPERATION)? formalParameters typeReference ccf=cardinalityConstraint) 
                       					//TODO: steal from v2
                   | typeName ccn=cardinalityConstraint		{ $type = F.TypeClass($typeName.expr, $ccn.ary); }
                   | STAR ccs=cardinalityConstraint		{ $type = F.at(pos($STAR)).TypeAny($ccs.ary); } ) )? 
        ;
cardinalityConstraint returns [int ary]
	:  LBRACKET   RBRACKET    	{ ary = MTType.CARDINALITY_ANY; }
	|  QUES                   	{ ary = MTType.CARDINALITY_SINGLETON; }
	|  PLUS                   	{ ary = MTType.CARDINALITY_ANY; }
	|  STAR                   	{ ary = MTType.CARDINALITY_ANY; }
	|                         	{ ary = MTType.CARDINALITY_SINGLETON; } 
	;
literal  returns [MTExpression expr]
	: t=STRING_LITERAL		{ $expr = F.at(pos($t)).Literal(TypeTags.CLASS, $t.text); }
	| t=INTEGER_LITERAL		{ $expr = F.at(pos($t)).Literal(TypeTags.INT, Convert.string2int($t.text, 10)); }
	| t=FLOATING_POINT_LITERAL 	{ $expr = F.at(pos($t)).Literal(TypeTags.DOUBLE, Double.valueOf($t.text)); }
	| t=TRUE   			{ $expr = F.at(pos($t)).Literal(TypeTags.BOOLEAN, 1); }
	| t=FALSE   			{ $expr = F.at(pos($t)).Literal(TypeTags.BOOLEAN, 0); }
	| t=NULL 			{ $expr = F.at(pos($t)).Literal(TypeTags.BOT, null); } 
	;
typeName  returns [MTExpression expr]
       : qualident            		{ $expr = $qualident.expr; } 
       ;
qualident returns [MTExpression expr]
       : n1=name            		{ $expr = F.at($n1.pos).Identifier($n1.value); } 
         ( DOT nn=name     		{ $expr = F.at($nn.pos).Select($expr, $nn.value); } 
         ) *  ;
identifier  returns [MTIdent expr]
	: name              		{ $expr = F.at($name.pos).Ident($name.value); } 
	;
name returns [Name value, int pos]
	: tokid=( QUOTED_IDENTIFIER | IDENTIFIER ) { $value = Name.fromString(names, $tokid.text); $pos = pos($tokid); } 
	;
