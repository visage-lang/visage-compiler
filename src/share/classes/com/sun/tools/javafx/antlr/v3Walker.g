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

tree grammar v3Walker;

options { 
   tokenVocab=v3;
   ASTLabelType=CommonTree;
   superClass = AbstractGeneratedTreeParser; 
}

@header {
package com.sun.tools.javafx.antlr;

import java.util.HashMap;
import java.util.Map;
import java.io.OutputStreamWriter;

import com.sun.tools.javac.tree.*;
import com.sun.tools.javafx.tree.*;
import com.sun.javafx.api.tree.*;

import com.sun.tools.javac.code.*;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javac.util.*;
import static com.sun.tools.javac.util.ListBuffer.lb;
import com.sun.javafx.api.JavafxBindStatus;
import static com.sun.javafx.api.JavafxBindStatus.*;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
}

@members {
    public void initialize(Context context) {
        super.initialize(context);
        whiteSpaceToken = v3Lexer.WS;
    }
}
	
module returns [JFXUnit result]
@init   { docComments = null;
          endPositions = genEndPos ? new HashMap<JCTree,Integer>() : null; }
@after  { $result.docComments = docComments; 
          $result.endPositions = endPositions; }
	: ^(MODULE packageDecl? moduleItems DOC_COMMENT?)		
                                                        { $result = F.TopLevel($packageDecl.value, $moduleItems.items.toList()); 
                                                          setDocComment($result, $DOC_COMMENT); 
                                                          $result.pos = $result.pid != null ? $result.pid.pos : $result.defs.head.pos;
                                                          endPos($result, $MODULE); }
       	;
packageDecl  returns [JFXExpression value]
       	: ^(PACKAGE qualident)        			{ $value = $qualident.expr; }
	;
moduleItems  returns [ListBuffer<JFXTree> items = new ListBuffer<JFXTree>()]  
	: ^(MODULE_ITEMS ( moduleItem                   { $items.append($moduleItem.value); })*
           )
	;
moduleItem  returns [JFXTree value]
	: importDecl 					{ $value = $importDecl.value; }
	| classDefinition 				{ $value = $classDefinition.value; }
        | functionDefinition                            { $value = $functionDefinition.value; }
	| statement      				{ $value = $statement.value; } 
	| expression 					{ $value = $expression.expr; } 
//        | EMPTY_MODULE_ITEM                             { $value = null; }
	;
importDecl  returns [JFXTree value]
 	: ^(IMPORT importId)				{ $value = F.at(pos($IMPORT)).Import($importId.pid, false); 
                                                          endPos($value, $IMPORT); }
	;
importId  returns [JFXExpression pid]
 	: identifier					{ $pid = $identifier.expr; }
        | ^(DOT in=importId	( name 			{ $pid = F.at($name.pos).Select($in.pid, $name.value); 
                                                          endPos($pid, $DOT); }
        			| STAR			{ $pid = F.at(pos($STAR)).Select($in.pid, names.asterisk); 
                                                          endPos($pid, $DOT); }
        			)
           )		
	;
classDefinition  returns [JFXClassDeclaration value]
	: ^(CLASS modifiers name supers classMembers DOC_COMMENT?)
	  						{ $value = F.at(pos($CLASS)).ClassDeclaration(
	  						  $modifiers.mods,
	  						  $name.value,
	                                	          $supers.ids.toList(), 
	                                	          $classMembers.mems.toList()); 
                                                          setDocComment($value, $DOC_COMMENT); 
                                                          endPos($value, $CLASS); }
	;
supers  returns [ListBuffer<JFXExpression> ids = new ListBuffer<JFXExpression>()]
	: ^(EXTENDS (typeName           		{ $ids.append($typeName.expr); } )* )
	;	  					
classMembers  returns [ListBuffer<JFXTree> mems = new ListBuffer<JFXTree>()]
	: ^(CLASS_MEMBERS ( classMember			{ $mems.append($classMember.member); } )* )
	;
classMember  returns [JFXTree member]
	: initDefinition				{ $member = $initDefinition.value; } 
	| postInitDefinition 				{ $member = $postInitDefinition.value; } 
	| variableDeclaration 				{ $member = $variableDeclaration.value; } 
	| functionDefinition 				{ $member = $functionDefinition.value; } 
	| overrideDeclaration 				{ $member = $overrideDeclaration.value; } 
	;
functionDefinition  returns [JFXFunctionDefinition value]
	: ^(FUNCTION name modifiers formalParameters type blockExpression? DOC_COMMENT?)
	    						{ $value = F.at(pos($FUNCTION)).FunctionDefinition(
	    						  $modifiers.mods,
	    						  $name.value, $type.type, 
	    						  $formalParameters.params.toList(), $blockExpression.expr); 
                                                          setDocComment($value, $DOC_COMMENT); 
                                                          endPos($value, $FUNCTION); }
	;
initDefinition  returns [JFXInitDefinition value]
	: ^(INIT block)					{ $value = F.at(pos($INIT)).InitDefinition($block.value); 
                                                          endPos($value, $INIT); }
	;
postInitDefinition  returns [JFXPostInitDefinition value]
	: ^(POSTINIT block)	 			{ $value = F.at(pos($POSTINIT)).PostInitDefinition($block.value); 
                                                          endPos($value, $POSTINIT); }
	;
overrideDeclaration returns [JFXOverrideAttribute value]
	: ^(OVERRIDE identifier boundExpression? onReplaceClause?)
							{ $value = F.at(pos($OVERRIDE)).OverrideAttribute($identifier.expr, 
									$boundExpression.expr, $boundExpression.status,
									$onReplaceClause.value);  
                                                          endPos($value, $OVERRIDE);}
	| ^(WITH identifier onReplaceClause)		{ $value = F.at(pos($WITH)).TriggerWrapper($identifier.expr, $onReplaceClause.value); 
                                                          endPos($value, $WITH); }
	;
modifiers  returns [JFXModifiers mods]
	: ^(MODIFIER modifierFlags)
	 		 				{ mods = F.at($modifierFlags.pos).Modifiers($modifierFlags.flags); 
                                                          endPos($mods, $MODIFIER); }
	;
modifierFlags   returns [long flags = 0L, int pos = -1]
	:  (modifierFlag				{ $flags |= $modifierFlag.flag; 
							  if (($pos == -1) && ($modifierFlag.pos != -1)) {
							      $pos = $modifierFlag.pos;
							  }
							} )*
	;
modifierFlag   returns [long flag, int pos]
	:  BOUND          				{ $flag = JavafxFlags.BOUND; $pos = pos($BOUND); }
	|  PUBLIC          				{ $flag = Flags.PUBLIC;      $pos = pos($PUBLIC); }
	|  PRIVATE         				{ $flag = Flags.PRIVATE;     $pos = pos($PRIVATE); }
	|  PROTECTED       				{ $flag = Flags.PROTECTED;   $pos = pos($PROTECTED); } 
	|  ABSTRACT        				{ $flag = Flags.ABSTRACT;    $pos = pos($ABSTRACT); }
	|  READONLY        				{ $flag = Flags.FINAL;       $pos = pos($READONLY); } 
	|  STATIC        				{ $flag = Flags.STATIC;      $pos = pos($STATIC); } 
	;
formalParameters  returns [ListBuffer<JFXVar> params = new ListBuffer<JFXVar>()]
	: ^(LPAREN (formalParameter			{ params.append($formalParameter.var); } )* )
	;
formalParameter returns [JFXVar var]
	: ^(PARAM name type)				{ $var = F.at($name.pos).Param($name.value, $type.type); 
                                                          endPos($var, $PARAM); }
	;
formalParameterOpt returns [JFXVar var]
	: formalParameter				{ $var = $formalParameter.var; } 
	|						{ $var = null; } 
	;
block  returns [JFXBlockExpression value]
@init { ListBuffer<JFXExpression> stats = ListBuffer.<JFXExpression>lb(); }
	: ^(BLOCK
		(	^(STATEMENT statement)		{ stats.append($statement.value); }	
		| 	^(EXPRESSION expression)	{ stats.append($expression.expr); }
		)*
	    )						{ $value = F.at(pos($BLOCK)).Block(0L, stats.toList()); 
                                                          endPos($value, $BLOCK); }
	;
blockExpression  returns [JFXBlockExpression expr]
@init { ListBuffer<JFXExpression> stats = new ListBuffer<JFXExpression>(); JFXExpression val = null; CommonTree tval = null;}
	: ^(LBRACE 
		(	^(STATEMENT statement)		{ if (val != null) {
                                                              stats.append(val);
                                                              val = null; 
                                                              tval = null;
                                                          }
	     					  	  stats.append($statement.value); }
		| 	^(EXPRESSION expression)	{ if (val != null) {
                                                              stats.append(val);
                                                          }
	     					  	  val = $expression.expr;
							  tval = $EXPRESSION; }
		)*
	    )						{ $expr = F.at(pos($LBRACE)).BlockExpression(0L, stats.toList(), val); 
                                                          endPos($expr, $LBRACE); }
	;
variableDeclaration    returns [JFXExpression value]
	: ^(VAR variableLabel modifiers name type boundExpressionOpt onReplaceClause? DOC_COMMENT?)
	    						{ $value = F.at($variableLabel.pos).Var($name.value, 
	    							$type.type, 
	    							$modifiers.mods,
	    							$variableLabel.local,
	    							$boundExpressionOpt.expr, 
	    							$boundExpressionOpt.status, 
	    							$onReplaceClause.value); 
                                                          setDocComment($value, $DOC_COMMENT); 
                                                          endPos($value, $VAR); }
	;
onReplaceClause     returns [JFXOnReplace value]
	: ^(ON_REPLACE_SLICE oldv=paramNameOpt
	       (^(SLICE_CLAUSE first=paramNameOpt last=paramNameOpt newElements=paramNameOpt))?
	    block)
							{ $value = F.at(pos($ON_REPLACE_SLICE)).OnReplace($oldv.var, $first.var, $last.var, $newElements.var, $block.value); 
                                                          endPos($value, $ON_REPLACE_SLICE); }
	;
paramNameOpt returns [JFXVar var]
	: name						{ $var = F.at($name.pos).Param($name.value, F.TypeUnknown()); }
	| MISSING_NAME					{ $var = null; }
	;
variableLabel    returns [boolean local, long modifiers, int pos]
	: VAR						{ $local = true; $modifiers = 0L; $pos = pos($VAR); }
	| LET						{ $local = true; $modifiers = Flags.FINAL; $pos = pos($LET); }
	| ATTRIBUTE					{ $local = false; $modifiers = 0L; $pos = pos($ATTRIBUTE); }
	;
statement returns [JFXExpression value]
	: variableDeclaration				{ $value = $variableDeclaration.value; }
//	| functionDefinition 				{ $value = $functionDefinition.value; }
	| BREAK    					{ $value = F.at(pos($BREAK)).Break(null); 
                                                          endPos($value, $BREAK); }
	| CONTINUE  	 	 			{ $value = F.at(pos($CONTINUE)).Continue(null); 
                                                          endPos($value, $CONTINUE); }
       	| ^(THROW expression)	   			{ $value = F.at(pos($THROW)).Throw($expression.expr); 
                                                          endPos($value, $THROW); } 
	| ^(WHILE expression block)			{ $value = F.at(pos($WHILE)).WhileLoop($expression.expr, $block.value); 
                                                          endPos($value, $WHILE); }
	| ^(INTO elem=expression eseq=expression)	{ $value = F.at(pos($INTO)).SequenceInsert($eseq.expr, $elem.expr, null, false); 
                                                          endPos($value, $INTO); } 
	| ^(BEFORE elem=expression ^(SEQ_INDEX eseq=expression idx=expression rb=RBRACKET))
							{ $value = F.at(pos($BEFORE)).SequenceInsert($eseq.expr, $elem.expr, $idx.expr, false); 
                                                          endPos($value, $rb); } 
	| ^(AFTER elem=expression ^(SEQ_INDEX eseq=expression idx=expression rb=RBRACKET))
							{ $value = F.at(pos($AFTER)).SequenceInsert($eseq.expr, $elem.expr, $idx.expr, true); 
                                                          endPos($value, $rb); } 
	| ^(FROM e1=expression e2=expression)		{ $value = F.at(pos($FROM)).SequenceDelete($e2.expr,$e1.expr); 
                                                          endPos($value, $FROM); } 
	| ^(DELETE expression)				{ $value = F.at(pos($DELETE)).SequenceDelete($expression.expr); 
                                                          endPos($value, $DELETE); } 
	| ^(RETURN expression?)				{ $value = F.at(pos($RETURN)).Return($expression.expr); 
                                                          endPos($value, $RETURN); } 
	| ^(TRY block catchClauses finallyClause?)	{ $value = F.at(pos($TRY)).Try($block.value, $catchClauses.caught.toList(), $finallyClause.value); 
                                                          endPos($value, $TRY); }
       	;
catchClauses  returns [ListBuffer<JFXCatch> caught = ListBuffer.lb()]
	: ( catchClause					{ $caught.append($catchClause.value); } )*
	;
catchClause  returns [JFXCatch value]
	: ^(CATCH formalParameter block)		{ $value = F.at(pos($CATCH)).Catch($formalParameter.var, $block.value); 
                                                          endPos($value, $CATCH); } 
	;
finallyClause  returns [JFXBlockExpression value]
	: ^(FINALLY block)				{ $value = $block.value; }
	;
boundExpression   returns [JavafxBindStatus status, JFXExpression expr]
@init { boolean isLazy = false; boolean isBidirectional = false; }
	: ^(BIND 				
	      (LAZY					{ isLazy = true; } )?
	      (INVERSE					{ isBidirectional = true; } )?
	      expression				{ $expr = $expression.expr; }
	      						{ $status = isBidirectional? isLazy? LAZY_BIDIBIND : BIDIBIND
	  									   : isLazy? LAZY_UNIDIBIND :  UNIDIBIND; }
	   )
	| ^(EXPRESSION expression)			{ $expr = $expression.expr; $status = UNBOUND; }
	;
boundExpressionOpt   returns [JavafxBindStatus status, JFXExpression expr]
	: boundExpression				{ $expr = $boundExpression.expr; $status = $boundExpression.status; }
	| 						{ $expr = null; $status = UNBOUND; }
	;
expression  returns [JFXExpression expr]
       	: ^(FOR inClauses e0=expression)		{ $expr = F.at(pos($FOR)).ForExpression($inClauses.clauses.toList(), $e0.expr); 
                                                          endPos($expr, $FOR); }
	| ^(IF econd=expression ethen=expression eelse=expression?)
							{ $expr = F.at(pos($IF)).Conditional($econd.expr, $ethen.expr, $eelse.expr); 
                                                          endPos($expr, $IF); }
	| ^(EQ lhs=expression rhs=expression)		{ $expr = F.at(pos($EQ)).Assign($lhs.expr, $rhs.expr); 
                                                          endPos($expr, $EQ); } 
	| ^(PLUSEQ lhs=expression rhs=expression) 	{ $expr = F.at(pos($PLUSEQ)).Assignop(JavafxTag.PLUS_ASG, $lhs.expr, $rhs.expr); 
                                                          endPos($expr, $PLUSEQ); }
	| ^(SUBEQ lhs=expression rhs=expression) 	{ $expr = F.at(pos($SUBEQ)).Assignop(JavafxTag.MINUS_ASG, $lhs.expr, $rhs.expr); 
                                                          endPos($expr, $SUBEQ); }
	| ^(STAREQ lhs=expression rhs=expression) 	{ $expr = F.at(pos($STAREQ)).Assignop(JavafxTag.MUL_ASG, $lhs.expr, $rhs.expr); 
                                                          endPos($expr, $STAREQ); }
	| ^(SLASHEQ lhs=expression rhs=expression) 	{ $expr = F.at(pos($SLASHEQ)).Assignop(JavafxTag.DIV_ASG, $lhs.expr, $rhs.expr); 
                                                          endPos($expr, $SLASHEQ); }
	| ^(PERCENTEQ lhs=expression rhs=expression) 	{ $expr = F.at(pos($PERCENTEQ)).Assignop(JavafxTag.MOD_ASG, $lhs.expr, $rhs.expr); 
                                                          endPos($expr, $PERCENTEQ); }
	| ^(AND e1=expression e2=expression) 		{ $expr = F.at(pos($AND)).Binary(JavafxTag.AND, $e1.expr, $e2.expr); 
                                                          endPos($expr, $AND); }
	| ^(OR e1=expression e2=expression) 		{ $expr = F.at(pos($OR)).Binary(JavafxTag.OR, $e1.expr, $e2.expr); 
                                                          endPos($expr, $OR); } 
	| ^(INSTANCEOF e0=expression type)		{ $expr = F.at(pos($INSTANCEOF)).TypeTest($e0.expr, $type.type); 
                                                          endPos($expr, $INSTANCEOF); }
	| ^(AS e0=expression type)			{ $expr = F.at(pos($AS)).TypeCast($type.type, $e0.expr); 
                                                          endPos($expr, $AS); }   
	| ^(LTGT e1=expression e2=expression)		{ $expr = F.at(pos($LTGT)).Binary(JavafxTag.NE, $e1.expr, $e2.expr); 
                                                          endPos($expr, $LTGT); }
        | ^(NOTEQ e1=expression e2=expression)		{ $expr = F.at(pos($NOTEQ)).Binary(JavafxTag.NE, $e1.expr, $e2.expr); 
                                                          endPos($expr, $NOTEQ); }                                                 
	| ^(EQEQ e1=expression e2=expression)		{ $expr = F.at(pos($EQEQ)).Binary(JavafxTag.EQ, $e1.expr, $e2.expr); 
                                                          endPos($expr, $EQEQ); }
	| ^(LTEQ e1=expression e2=expression)		{ $expr = F.at(pos($LTEQ)).Binary(JavafxTag.LE, $e1.expr, $e2.expr); 
                                                          endPos($expr, $LTEQ); }
	| ^(GTEQ e1=expression e2=expression)		{ $expr = F.at(pos($GTEQ)).Binary(JavafxTag.GE, $e1.expr, $e2.expr); 
                                                          endPos($expr, $GTEQ); }
	| ^(LT   e1=expression e2=expression)		{ $expr = F.at(pos($LT))  .Binary(JavafxTag.LT, $e1.expr, $e2.expr); 
                                                          endPos($expr, $LT); }
	| ^(GT   e1=expression e2=expression)		{ $expr = F.at(pos($GT))  .Binary(JavafxTag.GT, $e1.expr, $e2.expr); 
                                                          endPos($expr, $GT); }
	| ^(PLUS e1=expression e2=expression)		{ $expr = F.at(pos($PLUS)).Binary(JavafxTag.PLUS , $e1.expr, $e2.expr); 
                                                          endPos($expr, $PLUS); }
	| ^(SUB  e1=expression e2=expression)		{ $expr = F.at(pos($SUB)) .Binary(JavafxTag.MINUS, $e1.expr, $e2.expr); 
                                                          endPos($expr, $SUB); }
	| ^(STAR    e1=expression e2=expression)	{ $expr = F.at(pos($STAR))   .Binary(JavafxTag.MUL  , $e1.expr, $e2.expr); 
                                                          endPos($expr, $STAR); }
	| ^(SLASH   e1=expression e2=expression)	{ $expr = F.at(pos($SLASH))  .Binary(JavafxTag.DIV  , $e1.expr, $e2.expr); 
                                                          endPos($expr, $SLASH); }
	| ^(PERCENT e1=expression e2=expression)	{ $expr = F.at(pos($PERCENT)).Binary(JavafxTag.MOD  , $e1.expr, $e2.expr); 
                                                          endPos($expr, $PERCENT); } 
        | ^(MOD e1=expression e2=expression)            { $expr = F.at(pos($MOD)).Binary(JavafxTag.MOD  , $e1.expr, $e2.expr); 
                                                          endPos($expr, $MOD); } 
	| ^(NEGATIVE e0=expression)			{ $expr = F.at(pos($NEGATIVE)).Unary(JavafxTag.NEG, $e0.expr); 
                                                          endPos($expr, $NEGATIVE); }
	| ^(NOT e0=expression)				{ $expr = F.at(pos($NOT)).Unary(JavafxTag.NOT, $e0.expr); 
                                                          endPos($expr, $NOT); }	
	| ^(SIZEOF e0=expression)			{ $expr = F.at(pos($SIZEOF)).Unary(JavafxTag.SIZEOF, $e0.expr); 
                                                          endPos($expr, $SIZEOF); }
	| ^(PLUSPLUS e0=expression)   			{ $expr = F.at(pos($PLUSPLUS)).Unary(JavafxTag.PREINC, $e0.expr); 
                                                          endPos($expr, $PLUSPLUS); }
	| ^(SUBSUB e0=expression) 			{ $expr = F.at(pos($SUBSUB)).Unary(JavafxTag.PREDEC, $e0.expr); 
                                                          endPos($expr, $SUBSUB); }
	| ^(REVERSE e0=expression) 			{ $expr = F.at(pos($REVERSE)).Unary(JavafxTag.REVERSE, $e0.expr); 
                                                          endPos($expr, $REVERSE); }
	| ^(POSTINCR e0=expression)			{ $expr = F.at($e0.expr.pos).Unary(JavafxTag.POSTINC, $e0.expr); 
                                                          endPos($expr, $POSTINCR); }
	| ^(POSTDECR e0=expression)			{ $expr = F.at($e0.expr.pos).Unary(JavafxTag.POSTDEC, $e0.expr); 
                                                          endPos($expr, $POSTDECR); }
	| ^(DOT e0=expression name)			{ $expr = F.at(pos($DOT)).Select($e0.expr, $name.value); 
                                                          endPos($expr, $name.start); } // start is the CommonTree for the node
	| ^(FUNC_APPLY e0=expression expressionList)	{ $expr = F.at(pos($FUNC_APPLY)).Apply(null, $e0.expr, $expressionList.args.toList()); 
                                                          endPos($expr, $FUNC_APPLY); } 
	| ^(SEQ_INDEX seq=expression idx=expression rb=RBRACKET?)	
                                                        { $expr = F.at($seq.expr.pos).SequenceIndexed($seq.expr, $idx.expr); 
                                                          endPos($expr, $rb); }
	| ^(SEQ_SLICE seq=expression first=expression last=expression?)
	                                                { $expr = F.at(pos($SEQ_SLICE)).SequenceSlice($seq.expr, $first.expr, $last.expr, 
	                                                                                              SequenceSliceTree.END_INCLUSIVE); 
                                                          endPos($expr, $SEQ_SLICE); }
	| ^(SEQ_SLICE_EXCLUSIVE seq=expression first=expression last=expression?)
	                                                { $expr = F.at(pos($SEQ_SLICE_EXCLUSIVE)).SequenceSlice($seq.expr, $first.expr, $last.expr,  
	                                                                                                        SequenceSliceTree.END_EXCLUSIVE); 
                                                          endPos($expr, $SEQ_SLICE_EXCLUSIVE); }
	| ^(OBJECT_LIT i=qualident objectLiteral)	{ $expr = F.at(getStartPos($i.expr)).Instanciate($qualident.expr, null, $objectLiteral.parts.toList()); 
                                                          endPos($expr, $OBJECT_LIT); }
       	| ^(FUNC_EXPR formalParameters type blockExpression)
       							{ $expr = F.at(pos($FUNC_EXPR)).FunctionValue($type.type, $formalParameters.params.toList(),
                                               								$blockExpression.expr); 
                                                          endPos($expr, $FUNC_EXPR); }
	| ^(NEW typeName expressionList)		{ $expr = F.at(pos($NEW)).Instanciate($typeName.expr, $expressionList.args.toList(), null); 
                                                          endPos($expr, $NEW); }
       	| ^(INDEXOF identifier)                		{ $expr = F.at(pos($INDEXOF)).Indexof($identifier.expr); 
                                                          endPos($expr, $INDEXOF); }
	| pipeExpression				{ $expr = $pipeExpression.expr; }
	| blockExpression				{ $expr = $blockExpression.expr; }
       	| stringExpression				{ $expr = $stringExpression.expr; }
	| explicitSequenceExpression			{ $expr = $explicitSequenceExpression.expr; }
	| ^(DOTDOT from=expression to=expression step=expression? LT?)
							{ $expr = F.at(pos($DOTDOT)).RangeSequence($from.expr, $to.expr, $step.expr, $LT!=null); 
                                                          endPos($expr, $DOTDOT); }
	| SEQ_EMPTY					{ $expr = F.at(pos($SEQ_EMPTY)).EmptySequence(); 
                                                          endPos($expr, $SEQ_EMPTY); }
       	| THIS						{ $expr = F.at(pos($THIS)).Ident(names._this); 
                                                          endPos($expr, $THIS); }
       	| SUPER						{ $expr = F.at(pos($SUPER)).Ident(names._super); 
                                                          endPos($expr, $SUPER); }
       	| identifier					{ $expr = $identifier.expr; }
	| t=STRING_LITERAL				{ $expr = F.at(pos($t)).Literal(TypeTags.CLASS, $t.text); 
                                                          endPos($expr, $t); }
	| t=DECIMAL_LITERAL				{ $expr = F.at(pos($t)).Literal(TypeTags.INT, (int)Convert.string2long($t.text, 10)); 
                                                          endPos($expr, $t); }
	| t=OCTAL_LITERAL				{ $expr = F.at(pos($t)).Literal(TypeTags.INT, (int)Convert.string2long($t.text, 8)); 
                                                          endPos($expr, $t); }
	| t=HEX_LITERAL					{ $expr = F.at(pos($t)).Literal(TypeTags.INT, (int)Convert.string2long($t.text, 16)); 
                                                          endPos($expr, $t); }
	| t=FLOATING_POINT_LITERAL 			{ $expr = F.at(pos($t)).Literal(TypeTags.DOUBLE, Double.valueOf($t.text)); 
                                                          endPos($expr, $t); }
	| t=TIME_LITERAL         			{ $expr = F.at(pos($t)).TimeLiteral($t.text); 
                                                          endPos($expr, $t); }
	| t=TRUE   					{ $expr = F.at(pos($t)).Literal(TypeTags.BOOLEAN, 1); 
                                                          endPos($expr, $t); }
	| t=FALSE   					{ $expr = F.at(pos($t)).Literal(TypeTags.BOOLEAN, 0); 
                                                          endPos($expr, $t); }
	| t=NULL 					{ $expr = F.at(pos($t)).Literal(TypeTags.BOT, null); 
                                                          endPos($expr, $t); } 
        | ^(SUCHTHAT target=expression value=expression (interpolate=expression)?)
                                                        { $expr = F.at(pos($SUCHTHAT)).InterpolateValue($target.expr, $value.expr, $interpolate.expr);
                                                          endPos($expr, $SUCHTHAT);
                                                        } 
        | ^(AT d=expression k=keyFrameLiteralPart)  
                                                        { $expr = F.at(pos($AT)).KeyFrameLiteral($d.expr, $k.exprs.toList(), null);
                                                          endPos($expr, $AT);
                                                        }
        ;       
keyFrameLiteralPart returns [ListBuffer<JFXExpression> exprs = new ListBuffer<JFXExpression>(); ]
        : ^(KEY_FRAME_PART ( expression                 { exprs.append($expression.expr); }
                           )+
           )
        ;
inClauses  returns [ListBuffer<JFXForExpressionInClause> clauses = ListBuffer.lb()]
	: ( inClause					{ clauses.append($inClause.value); } )*	
	;
inClause  returns [JFXForExpressionInClause value] 
	: ^(IN formalParameter se=expression we=expression?)
							{ $value = F.at(pos($IN)).InClause($formalParameter.var, $se.expr, $we.expr); 
                                                          endPos($value, $IN); }
	;
pipeExpression  returns [JFXExpression expr] //TODO: this is a hack
	: ^(PIPE seq=expression name cond=expression)	{ ListBuffer<JFXForExpressionInClause> clauses = ListBuffer.lb(); 
                  					  JFXVar var = F.at($name.pos).Param($name.value, F.TypeUnknown());
	          					  clauses.append(F.at(pos($PIPE)).InClause(var, $seq.expr, $cond.expr));
                  					  $expr = F.at(pos($PIPE)).ForExpression(clauses.toList(), F.at($name.pos).Ident($name.value)); 
                                                          endPos($expr, $PIPE);
							}
	;
stringExpression  returns [JFXExpression expr] 
@init { ListBuffer<JFXExpression> strexp = new ListBuffer<JFXExpression>(); 
        String translationKey = null; }
	: ^(tk=TRANSLATION_KEY                          { translationKey = $tk.text; }
            STRING_LITERAL                              { strexp.append(F.at(pos($STRING_LITERAL)).Literal(TypeTags.CLASS, $STRING_LITERAL.text)); 
                                                          endPos($expr, $STRING_LITERAL); }
		  					{ $expr = F.at(pos($STRING_LITERAL)).StringExpression(strexp.toList(), translationKey); 
                                                          endPos($expr, $STRING_LITERAL); }
           )
	| ^(QUOTE_LBRACE_STRING_LITERAL			{ strexp.append(F.at(pos($QUOTE_LBRACE_STRING_LITERAL)).Literal(TypeTags.CLASS,
												 $QUOTE_LBRACE_STRING_LITERAL.text)); 
                                                          endPos($expr, $QUOTE_LBRACE_STRING_LITERAL); }
		  f1=stringFormat			{ strexp.append($f1.expr); }
		  e1=expression 			{ strexp.append($e1.expr); }
		  (  rl=RBRACE_LBRACE_STRING_LITERAL	{ strexp.append(F.at(pos($rl)).Literal(TypeTags.CLASS, $rl.text)); }
		     fn=stringFormat			{ strexp.append($fn.expr); }
		     en=expression 			{ strexp.append($en.expr); }
		  )*   
		  rq=RBRACE_QUOTE_STRING_LITERAL	{ strexp.append(F.at(pos($rq)).Literal(TypeTags.CLASS, $rq.text)); }
		  					{ $expr = F.at(pos($QUOTE_LBRACE_STRING_LITERAL)).StringExpression(strexp.toList(), translationKey); 
                                                          endPos($expr, $QUOTE_LBRACE_STRING_LITERAL); }
	    )
	| ^(tk=TRANSLATION_KEY                          { translationKey = $tk.text; }
            QUOTE_LBRACE_STRING_LITERAL			{ strexp.append(F.at(pos($QUOTE_LBRACE_STRING_LITERAL)).Literal(TypeTags.CLASS,
												 $QUOTE_LBRACE_STRING_LITERAL.text)); 
                                                          endPos($expr, $QUOTE_LBRACE_STRING_LITERAL); }
		  f1=stringFormat			{ strexp.append($f1.expr); }
		  e1=expression 			{ strexp.append($e1.expr); }
		  (  rl=RBRACE_LBRACE_STRING_LITERAL	{ strexp.append(F.at(pos($rl)).Literal(TypeTags.CLASS, $rl.text)); }
		     fn=stringFormat			{ strexp.append($fn.expr); }
		     en=expression 			{ strexp.append($en.expr); }
		  )*   
		  rq=RBRACE_QUOTE_STRING_LITERAL	{ strexp.append(F.at(pos($rq)).Literal(TypeTags.CLASS, $rq.text)); }
		  					{ $expr = F.at(pos($QUOTE_LBRACE_STRING_LITERAL)).StringExpression(strexp.toList(), translationKey); 
                                                          endPos($expr, $QUOTE_LBRACE_STRING_LITERAL); }
	    )
	;
stringFormat  returns [JFXExpression expr] 
	: fs=FORMAT_STRING_LITERAL			{ $expr = F.at(pos($fs)).Literal(TypeTags.CLASS, $fs.text); }
	| EMPTY_FORMAT_STRING				{ $expr = F.             Literal(TypeTags.CLASS, ""); }
	;
explicitSequenceExpression   returns [JFXSequenceExplicit expr]
@init { ListBuffer<JFXExpression> exps = new ListBuffer<JFXExpression>(); }
	: ^(SEQ_EXPLICIT   
	    ( expression 				{ exps.append($expression.expr); } )*
	   )						{ $expr = F.at(pos($SEQ_EXPLICIT)).ExplicitSequence(exps.toList()); 
                                                          endPos($expr, $SEQ_EXPLICIT); }
	;
objectLiteral  returns [ListBuffer<JFXTree> parts = ListBuffer.<JFXTree>lb()]
	: ( objectLiteralPart  				{ $parts.append($objectLiteralPart.value); } ) * 
	;
objectLiteralPart  returns [JFXTree value]
	: ^(OBJECT_LIT_PART n=name boundExpression)	{ $value = F.at($n.pos).ObjectLiteralPart($name.value,
								 $boundExpression.expr, $boundExpression.status); 
                                                          endPos($value, $OBJECT_LIT_PART); }
       	| variableDeclaration				{ $value = $variableDeclaration.value; }
       	| overrideDeclaration				{ $value = $overrideDeclaration.value; }
       	| functionDefinition				{ $value = $functionDefinition.value; }
       	;
expressionList  returns [ListBuffer<JFXExpression> args = new ListBuffer<JFXExpression>()] 
	: ^(EXPR_LIST (expression			{ $args.append($expression.expr); }  )* )
	;
type  returns [JFXType type]
	: ^(TYPE_NAMED typeName cardinality)		{ $type = F.at($typeName.expr.pos).TypeClass($typeName.expr, $cardinality.ary); 
                                                          endPos($type, $TYPE_NAMED); }
 	| ^(TYPE_FUNCTION typeArgList ret=type cardinality)
 							{ $type = F.at(pos($TYPE_FUNCTION)).TypeFunctional($typeArgList.ptypes.toList(), $ret.type, $cardinality.ary); 
                                                          endPos($type, $TYPE_FUNCTION); }
 	| ^(TYPE_ANY cardinality)			{ $type = F.at(pos($TYPE_ANY)).TypeAny($cardinality.ary); 
                                                          endPos($type, $TYPE_ANY); } 
 	| TYPE_UNKNOWN					{ $type = F.TypeUnknown(); }
 	;
typeArgList   returns [ListBuffer<JFXType> ptypes = ListBuffer.<JFXType>lb(); ]
 	: ^(TYPED_ARG_LIST (typeArg			{ ptypes.append($typeArg.type); } )* )
	;
typeArg  returns [JFXType type]
 	: ^(COLON name? type)				{ $type = $type.type; }
 	;
cardinality returns [TypeTree.Cardinality ary]
	: RBRACKET					{ ary = TypeTree.Cardinality.ANY; }
	|                         			{ ary = TypeTree.Cardinality.SINGLETON; } 
	;	
typeName  returns [JFXExpression expr]
	: ^(TYPE_ARG qualident genericArguments)	{ log.error(pos($TYPE_ARG), "javafx.generalerror", "Java generic type declarations are not currently supported"); }
                                                        //TODO: remove or implement -- { $expr = F.at(pos($TYPE_ARG)).TypeApply($qualident.expr, $genericArguments.exprbuff.toList());
                                                        { $expr = $qualident.expr; // so that things don't fall over
                                                          endPos($expr, $TYPE_ARG); }
	| qualident					{ $expr = $qualident.expr; }
	;
genericArguments  returns [ListBuffer<JFXExpression> exprbuff = ListBuffer.<JFXExpression>lb()]
	: ( genericArgument				{ $exprbuff.append($genericArgument.expr); } )* 
	;	
genericArgument  returns [JFXExpression expr]
@init { BoundKind bk = BoundKind.UNBOUND; JFXExpression texpr = null; }
	: typeName					{ $expr = $typeName.expr; }
	| ^(QUES (  ( EXTENDS 				{ bk = BoundKind.EXTENDS; }
		    | SUPER				{ bk = BoundKind.SUPER; }
		    ) 
		 typeName				{ texpr = $typeName.expr; }
	         )?					{ //TODO: remove or implement -- $expr = F.at(pos($QUES)).Wildcard(F.TypeBoundKind(bk), texpr); 
                                                          endPos($expr, $QUES); }
	    )
	;
qualident  returns [JFXExpression expr]
	: name 				          	{ $expr = F.at($name.pos).Ident($name.value); 
                                                          endPos($expr, $name.pos + $name.value.length()); } 
	| ^(DOT id=qualident name)			{ $expr = F.at(pos($DOT)).Select($id.expr, $name.value); 
                                                          endPos($expr, $DOT); } 
	;
identifier  returns [JFXIdent expr]
	: name 				          	{ $expr = F.at($name.pos).Ident($name.value); 
                                                          endPos($expr, $name.pos + $name.value.length()); } 
	;
name returns [Name value, int pos]
	: IDENTIFIER					{ $value = Name.fromString(names, $IDENTIFIER.text); $pos = pos($IDENTIFIER); }
	;
