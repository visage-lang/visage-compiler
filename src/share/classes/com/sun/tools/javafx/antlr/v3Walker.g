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

grammar v3Walker;

options { 
   tokenVocab=v3;
   ASTLabelType=CommonTree;
}

module
	: ^(MODULE packageDecl? moduleItems)
       	;
packageDecl 
       	: qualident
	;
moduleItems    
	: moduleItem*
	;
moduleItem 
	: importDecl
	| classDefinition
	| statement
	| expression
	;
importDecl
 	: ^(IMPORT importId)
	;
importId
 	: identifier
                 ( ^(DOT importId (name | STAR)) )* 
	;
classDefinition 
	: ^(CLASS classModifierFlags name supers? classMembers)
	;
supers 
	: ^(EXTENDS typeName*)
	;	  					
classMembers 
	: classMember*
	;
classMember
	: initDefinition	
	| variableDeclaration 
	| functionDefinition 
	;
functionDefinition
	: ^(FUNCTION name functionModifierFlags formalParameters type blockExpression?)
	;
initDefinition
	: ^(INIT block)
	;
functionModifierFlags
	: ^(MODIFIER accessModifier?  functionModifier?)
	;
varModifierFlags
	: ^(MODIFIER accessModifier? varModifier?)
	;
classModifierFlags
	: ^(MODIFIER accessModifier? classModifier?)
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
	: ^(MEMBERSELECTOR name+)
	;
formalParameters
	: ^(LPAREN formalParameter*)
	;
formalParameter
	: ^(PARAM name type)
	;
block
	: ^(BLOCK blockComponent*)
	;
blockExpression 
	: ^(LBRACE blockComponent*)
	;
blockComponent
	: ^(STATEMENT statement)
	| ^(EXPRESSION expression)
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
	: ^(VAR variableLabel varModifierFlags name type boundExpression? onChangeClause*)
	;
onChangeClause  
	: ^(ON_REPLACE formalParameter? block)
	| ^(ON_REPLACE_ELEMENT formalParameter formalParameter? block)
	| ^(ON_INSERT_ELEMENT formalParameter formalParameter? block)
	| ^(ON_DELETE_ELEMENT formalParameter formalParameter? block)
	;
variableLabel 
	: VAR	
	| LET	
	| ATTRIBUTE	
	;
whileStatement
	: ^(WHILE expression block)
	;
insertStatement  
	: ^(INSERT expression expression)
	;
deleteStatement  
	: ^(FROM expression expression)
	| ^(DELETE expression)
	;
returnStatement
	: ^(RETURN expression?)
	;
tryStatement
	: ^(TRY block catchClause* finallyClause?)
	;
finallyClause
	: ^(FINALLY block)
	;
catchClause
	: ^(CATCH formalParameter block)
	;
boundExpression 
	: ^(BIND LAZY? INVERSE? expression)
	| ^(EXPRESSION expression)
	;
expression 
       	: ^(FOR inClause* expression)
	| ^(IF expression expression expression?)
	| ^(EQ expression expression)
	| ^(PLUSEQ expression expression) 
	| ^(SUBEQ expression expression) 
	| ^(STAREQ expression expression) 
	| ^(SLASHEQ expression expression) 
	| ^(PERCENTEQ expression expression) 
	| ^(AND expression expression) 
	| ^(OR expression expression) 
	| ^(INSTANCEOF expression typeName)
	| ^(AS expression typeName)
	| ^(LTGT expression expression)
	| ^(EQEQ expression expression)
	| ^(LTEQ expression expression)
	| ^(GTEQ expression expression)
	| ^(LT   expression expression)
	| ^(GT   expression expression)
	| ^(PLUS expression expression)
	| ^(SUB  expression expression)
	| ^(STAR    expression expression)
	| ^(SLASH   expression expression)
	| ^(PERCENT expression expression)
	| ^(NEGATIVE expression)
	| ^(NOT expression)		
	| ^(SIZEOF expression)	
	| ^(PLUSPLUS expression)   	
	| ^(SUBSUB expression) 
	| ^(POSTINCR expression)
	| ^(POSTDECR expression)
	| ^(DOT expression name)
	| ^(FUNC_APPLY expression expressionList)
	| ^(SEQ_INDEX expression expression)
	| ^(OBJECT_LIT qualident objectLiteralPart*)
       	| ^(FUNC_EXPR formalParameters type blockExpression)
	| ^(NEW typeName expressionList)
	| ^(QUOTE_LBRACE_STRING_LITERAL stringFormat expression stringExpressionInner* RBRACE_QUOTE_STRING_LITERAL)
	| ^(SEQ_EXPLICIT expression*)
	| ^(DOTDOT expression expression expression? EXCLUSIVE?)
	| SEQ_EMPTY
	| qualident
       	| THIS
       	| SUPER
	;
inClause
	: ^(IN formalParameter expression expression?)
	;
stringExpressionInner
	: RBRACE_LBRACE_STRING_LITERAL stringFormat expression 			
	;
objectLiteralPart  
	: ^(OBJECT_LIT_PART name boundExpression)
       	| variableDeclaration
       	| functionDefinition
       	;
stringFormat  
	: FORMAT_STRING_LITERAL
	| EMPTY_FORMAT_STRING
	;
expressionList
	: ^(EXPR_LIST expression*)
	;
type 
	: ^(TYPE_NAMED typeName cardinality?)
 	| ^(TYPE_FUNCTION typeArgList type cardinality?)
 	| ^(TYPE_ANY cardinality?)
 	| TYPE_UNKNOWN
 	;
typeArgList
 	: typeArg*
	;
typeArg 
 	: ^(COLON name? type?)
 	;
cardinality 
	: RBRACKET
	;
literal  
	: STRING_LITERAL	
	| DECIMAL_LITERAL	
	| OCTAL_LITERAL		
	| HEX_LITERAL			
	| FLOATING_POINT_LITERAL 
	| TRUE   		
	| FALSE   		
	| NULL 		
	;
	
typeName  
	: ^(TYPE_ARG qualident typeArgument+)
	| qualident
	;
typeArgument 
	: typeName
	| ^(QUES EXTENDS? SUPER? typeName?)
	;
	
qualident 
	: (  plainName 
	  |  frenchName 
	  )

          ( ^(DOT qualident plainName)
          ) *  
	;
identifier 
	: name              	
	;
name 
	: plainName			
	| frenchName			
	;
plainName 
	: IDENTIFIER			
	;
frenchName 
	: QUOTED_IDENTIFIER		
	;
