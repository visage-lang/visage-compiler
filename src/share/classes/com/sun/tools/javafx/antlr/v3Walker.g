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

script returns [JFXUnit result]
@init   { docComments = null;
          endPositions = genEndPos ? new HashMap<JCTree,Integer>() : null; }
@after  { $result.docComments = docComments;
          $result.endPositions = endPositions; }
	: ^(SCRIPT packageDecl? scriptItems DOC_COMMENT?)
                                                        { $result = F.TopLevel($packageDecl.value, $scriptItems.items.toList());
                                                          setDocComment($result, $DOC_COMMENT);
                                                          $result.pos = $result.pid != null ? $result.pid.pos : $result.defs.head.pos;
                                                          endPos($result, $SCRIPT); }
       	;
packageDecl  returns [JFXExpression value]
       	: ^(PACKAGE qualident)        			{ $value = $qualident.value; }
	;
scriptItems  returns [ListBuffer<JFXTree> items = new ListBuffer<JFXTree>()]
	: ^(SCRIPT_ITEMS ( scriptItem                   { $items.append($scriptItem.value); })*
           )
	;
scriptItem  returns [JFXTree value]
	: importDecl 					{ $value = $importDecl.value; }
        | functionDefinition                            { $value = $functionDefinition.value; }
	| statement      				{ $value = $statement.value; }
	;
importDecl  returns [JFXTree value]
 	: ^(IMPORT importId)				{ $value = F.at(pos($IMPORT)).Import($importId.pid, false);
                                                          endPos($value, $IMPORT); }
	;
importId  returns [JFXExpression pid]
 	: identifier					{ $pid = $identifier.value; }
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
	: ^(EXTENDS (typeName           		{ $ids.append($typeName.value); } )* )
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
	| classDefinition 				{ $member = $classDefinition.value; }
	;
functionDefinition  returns [JFXFunctionDefinition value]
	: ^(FUNCTION name modifiers formalParameters type blockExpression? DOC_COMMENT?)
	    						{ $value = F.at(pos($FUNCTION)).FunctionDefinition(
	    						  $modifiers.mods,
	    						  $name.value, $type.type,
	    						  $formalParameters.params.toList(), $blockExpression.value);
                                                          setDocComment($value, $DOC_COMMENT);
                                                          endPos($value, $FUNCTION); }
	;
initDefinition  returns [JFXInitDefinition value]
	: ^(INIT blockExpression)					{ $value = F.at(pos($INIT)).InitDefinition($blockExpression.value);
                                                          endPos($value, $INIT); }
	;
postInitDefinition  returns [JFXPostInitDefinition value]
	: ^(POSTINIT blockExpression)	 			{ $value = F.at(pos($POSTINIT)).PostInitDefinition($blockExpression.value);
                                                          endPos($value, $POSTINIT); }
	;
overrideDeclaration returns [JFXOverrideAttribute value]
	: ^(OVERRIDE identifier boundExpression? onReplaceClause?)
							{ $value = F.at(pos($OVERRIDE)).OverrideAttribute($identifier.value,
									$boundExpression.value, $boundExpression.status,
									$onReplaceClause.value);
                                                          endPos($value, $OVERRIDE);}
	| ^(WITH identifier onReplaceClause)		{ $value = F.at(pos($WITH)).TriggerWrapper($identifier.value, $onReplaceClause.value);
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
	|  READABLE       				{ $flag = JavafxFlags.READABLE;   $pos = pos($READABLE); }
	|  ABSTRACT        				{ $flag = Flags.ABSTRACT;    $pos = pos($ABSTRACT); }
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
blockExpression  returns [JFXBlockExpression value]
@init { ListBuffer<JFXExpression> stats = new ListBuffer<JFXExpression>(); JFXExpression val = null;}
	: ^(LBRACE   (
	                  statement			{ if (val != null) {
                                                              stats.append(val);
                                                          }
	     					  	  val = $statement.value; }
		     )*
	    )						{ $value = F.at(pos($LBRACE)).BlockExpression(0L, stats.toList(), val);
                                                          endPos($value, $LBRACE); }
	;
variableDeclaration    returns [JFXExpression value]
	: ^(VAR variableLabel modifiers name type boundExpressionOpt onReplaceClause? DOC_COMMENT?)
	    						{ JFXModifiers mods = $modifiers.mods;
	    						  mods.flags |= $variableLabel.modifiers;
	    						  $value = F.at($variableLabel.pos).Var($name.value,
	    							$type.type,
	    							mods,
	    							false,
	    							$boundExpressionOpt.value,
	    							$boundExpressionOpt.status,
	    							$onReplaceClause.value);
                                                          setDocComment($value, $DOC_COMMENT);
                                                          endPos($value, $VAR); }
	;
onReplaceClause     returns [JFXOnReplace value]
	: ^(ON_REPLACE_SLICE oldv=paramNameOpt
	       (^(SLICE_CLAUSE first=paramNameOpt last=paramNameOpt newElements=paramNameOpt))?
	    blockExpression)
							{ $value = F.at(pos($ON_REPLACE_SLICE)).OnReplace($oldv.var, $first.var, $last.var, $newElements.var, $blockExpression.value);
                                                          endPos($value, $ON_REPLACE_SLICE); }
	;
paramNameOpt returns [JFXVar var]
	: name						{ $var = F.at($name.pos).Param($name.value, F.TypeUnknown()); }
	| MISSING_NAME					{ $var = null; }
	;
variableLabel    returns [long modifiers, int pos]
	: VAR						{ $modifiers = 0L; $pos = pos($VAR); }
	| DEF						{ $modifiers = JavafxFlags.IS_DEF; $pos = pos($DEF); }
	| ATTRIBUTE					{ $modifiers = 0L; $pos = pos($ATTRIBUTE); }
	;
statement returns [JFXExpression value]
	: classDefinition 				{ $value = $classDefinition.value; }
	| BREAK    					{ $value = F.at(pos($BREAK)).Break(null);
                                                          endPos($value, $BREAK); }
	| CONTINUE  	 	 			{ $value = F.at(pos($CONTINUE)).Continue(null);
                                                          endPos($value, $CONTINUE); }
       	| ^(THROW expression)	   			{ $value = F.at(pos($THROW)).Throw($expression.value);
                                                          endPos($value, $THROW); }
	| ^(WHILE expression blockExpression)			{ $value = F.at(pos($WHILE)).WhileLoop($expression.value, $blockExpression.value);
                                                          endPos($value, $WHILE); }
	| ^(INTO elem=expression eseq=expression)	{ $value = F.at(pos($INTO)).SequenceInsert($eseq.value, $elem.value, null, false);
                                                          endPos($value, $INTO); }
	| ^(BEFORE elem=expression ^(SEQ_INDEX eseq=expression idx=expression rb=RBRACKET))
							{ $value = F.at(pos($BEFORE)).SequenceInsert($eseq.value, $elem.value, $idx.value, false);
                                                          endPos($value, $rb); }
	| ^(AFTER elem=expression ^(SEQ_INDEX eseq=expression idx=expression rb=RBRACKET))
							{ $value = F.at(pos($AFTER)).SequenceInsert($eseq.value, $elem.value, $idx.value, true);
                                                          endPos($value, $rb); }
	| ^(FROM e1=expression e2=expression)		{ $value = F.at(pos($FROM)).SequenceDelete($e2.value,$e1.value);
                                                          endPos($value, $FROM); }
	| ^(DELETE expression)				{ $value = F.at(pos($DELETE)).SequenceDelete($expression.value);
                                                          endPos($value, $DELETE); }
	| ^(RETURN expression?)				{ $value = F.at(pos($RETURN)).Return($expression.value);
                                                          endPos($value, $RETURN); }
	| ^(TRY blockExpression catchClauses finallyClause?)	{ $value = F.at(pos($TRY)).Try($blockExpression.value, $catchClauses.caught.toList(), $finallyClause.value);
                                                          endPos($value, $TRY); }
	| expression					{ $value = $expression.value; }
       	;
catchClauses  returns [ListBuffer<JFXCatch> caught = ListBuffer.lb()]
	: ( catchClause					{ $caught.append($catchClause.value); } )*
	;
catchClause  returns [JFXCatch value]
	: ^(CATCH formalParameter blockExpression)		{ $value = F.at(pos($CATCH)).Catch($formalParameter.var, $blockExpression.value);
                                                          endPos($value, $CATCH); }
	;
finallyClause  returns [JFXBlockExpression value]
	: ^(FINALLY blockExpression)				{ $value = $blockExpression.value; }
	;
boundExpression   returns [JavafxBindStatus status, JFXExpression value]
@init { boolean isLazy = false; boolean isBidirectional = false; }
	: ^(BIND
	      (LAZY					{ isLazy = true; } )?
	      (INVERSE					{ isBidirectional = true; } )?
	      expression				{ $value = $expression.value; }
	      						{ $status = isBidirectional? isLazy? LAZY_BIDIBIND : BIDIBIND
	  									   : isLazy? LAZY_UNIDIBIND :  UNIDIBIND; }
	   )
	| expression					{ $value = $expression.value; $status = UNBOUND; }
	;
boundExpressionOpt   returns [JavafxBindStatus status, JFXExpression value]
	: boundExpression				{ $value = $boundExpression.value; $status = $boundExpression.status; }
	| 						{ $value = null; $status = UNBOUND; }
	;
expression  returns [JFXExpression value]
       	: variableDeclaration				{ $value = $variableDeclaration.value; }
	| ^(FOR inClauses e0=statement)			{ $value = F.at(pos($FOR)).ForExpression($inClauses.clauses.toList(), $e0.value);
                                                          endPos($value, $FOR); }
	| ^(IF econd=expression ethen=statement eelse=statement?)
							{ $value = F.at(pos($IF)).Conditional($econd.value, $ethen.value, $eelse.value);
                                                          endPos($value, $IF); }
	| ^(EQ lhs=expression rhs=expression)		{ $value = F.at(pos($EQ)).Assign($lhs.value, $rhs.value);
                                                          endPos($value, $EQ); }
	| ^(PLUSEQ lhs=expression rhs=expression) 	{ $value = F.at(pos($PLUSEQ)).Assignop(JavafxTag.PLUS_ASG, $lhs.value, $rhs.value);
                                                          endPos($value, $PLUSEQ); }
	| ^(SUBEQ lhs=expression rhs=expression) 	{ $value = F.at(pos($SUBEQ)).Assignop(JavafxTag.MINUS_ASG, $lhs.value, $rhs.value);
                                                          endPos($value, $SUBEQ); }
	| ^(STAREQ lhs=expression rhs=expression) 	{ $value = F.at(pos($STAREQ)).Assignop(JavafxTag.MUL_ASG, $lhs.value, $rhs.value);
                                                          endPos($value, $STAREQ); }
	| ^(SLASHEQ lhs=expression rhs=expression) 	{ $value = F.at(pos($SLASHEQ)).Assignop(JavafxTag.DIV_ASG, $lhs.value, $rhs.value);
                                                          endPos($value, $SLASHEQ); }
	| ^(PERCENTEQ lhs=expression rhs=expression) 	{ $value = F.at(pos($PERCENTEQ)).Assignop(JavafxTag.MOD_ASG, $lhs.value, $rhs.value);
                                                          endPos($value, $PERCENTEQ); }
	| ^(AND e1=expression e2=expression) 		{ $value = F.at(pos($AND)).Binary(JavafxTag.AND, $e1.value, $e2.value);
                                                          endPos($value, $AND); }
	| ^(OR e1=expression e2=expression) 		{ $value = F.at(pos($OR)).Binary(JavafxTag.OR, $e1.value, $e2.value);
                                                          endPos($value, $OR); }
	| ^(INSTANCEOF e0=expression type)		{ $value = F.at(pos($INSTANCEOF)).TypeTest($e0.value, $type.type);
                                                          endPos($value, $INSTANCEOF); }
	| ^(AS e0=expression type)			{ $value = F.at(pos($AS)).TypeCast($type.type, $e0.value);
                                                          endPos($value, $AS); }
	| ^(LTGT e1=expression e2=expression)		{ $value = F.at(pos($LTGT)).Binary(JavafxTag.NE, $e1.value, $e2.value);
                                                          endPos($value, $LTGT); }
        | ^(NOTEQ e1=expression e2=expression)		{ $value = F.at(pos($NOTEQ)).Binary(JavafxTag.NE, $e1.value, $e2.value);
                                                          endPos($value, $NOTEQ); }
	| ^(EQEQ e1=expression e2=expression)		{ $value = F.at(pos($EQEQ)).Binary(JavafxTag.EQ, $e1.value, $e2.value);
                                                          endPos($value, $EQEQ); }
	| ^(LTEQ e1=expression e2=expression)		{ $value = F.at(pos($LTEQ)).Binary(JavafxTag.LE, $e1.value, $e2.value);
                                                          endPos($value, $LTEQ); }
	| ^(GTEQ e1=expression e2=expression)		{ $value = F.at(pos($GTEQ)).Binary(JavafxTag.GE, $e1.value, $e2.value);
                                                          endPos($value, $GTEQ); }
	| ^(LT   e1=expression e2=expression)		{ $value = F.at(pos($LT))  .Binary(JavafxTag.LT, $e1.value, $e2.value);
                                                          endPos($value, $LT); }
	| ^(GT   e1=expression e2=expression)		{ $value = F.at(pos($GT))  .Binary(JavafxTag.GT, $e1.value, $e2.value);
                                                          endPos($value, $GT); }
	| ^(PLUS e1=expression e2=expression)		{ $value = F.at(pos($PLUS)).Binary(JavafxTag.PLUS , $e1.value, $e2.value);
                                                          endPos($value, $PLUS); }
	| ^(SUB  e1=expression e2=expression)		{ $value = F.at(pos($SUB)) .Binary(JavafxTag.MINUS, $e1.value, $e2.value);
                                                          endPos($value, $SUB); }
	| ^(STAR    e1=expression e2=expression)	{ $value = F.at(pos($STAR))   .Binary(JavafxTag.MUL  , $e1.value, $e2.value);
                                                          endPos($value, $STAR); }
	| ^(SLASH   e1=expression e2=expression)	{ $value = F.at(pos($SLASH))  .Binary(JavafxTag.DIV  , $e1.value, $e2.value);
                                                          endPos($value, $SLASH); }
	| ^(PERCENT e1=expression e2=expression)	{ $value = F.at(pos($PERCENT)).Binary(JavafxTag.MOD  , $e1.value, $e2.value);
                                                          endPos($value, $PERCENT); }
        | ^(MOD e1=expression e2=expression)            { $value = F.at(pos($MOD)).Binary(JavafxTag.MOD  , $e1.value, $e2.value);
                                                          endPos($value, $MOD); }
	| ^(NEGATIVE e0=expression)			{ $value = F.at(pos($NEGATIVE)).Unary(JavafxTag.NEG, $e0.value);
                                                          endPos($value, $NEGATIVE); }
	| ^(NOT e0=expression)				{ $value = F.at(pos($NOT)).Unary(JavafxTag.NOT, $e0.value);
                                                          endPos($value, $NOT); }
	| ^(SIZEOF e0=expression)			{ $value = F.at(pos($SIZEOF)).Unary(JavafxTag.SIZEOF, $e0.value);
                                                          endPos($value, $SIZEOF); }
	| ^(PLUSPLUS e0=expression)   			{ $value = F.at(pos($PLUSPLUS)).Unary(JavafxTag.PREINC, $e0.value);
                                                          endPos($value, $PLUSPLUS); }
	| ^(SUBSUB e0=expression) 			{ $value = F.at(pos($SUBSUB)).Unary(JavafxTag.PREDEC, $e0.value);
                                                          endPos($value, $SUBSUB); }
	| ^(REVERSE e0=expression) 			{ $value = F.at(pos($REVERSE)).Unary(JavafxTag.REVERSE, $e0.value);
                                                          endPos($value, $REVERSE); }
	| ^(POSTINCR e0=expression)			{ $value = F.at($e0.value.pos).Unary(JavafxTag.POSTINC, $e0.value);
                                                          endPos($value, $POSTINCR); }
	| ^(POSTDECR e0=expression)			{ $value = F.at($e0.value.pos).Unary(JavafxTag.POSTDEC, $e0.value);
                                                          endPos($value, $POSTDECR); }
	| ^(DOT e0=expression name)			{ $value = F.at(pos($DOT)).Select($e0.value, $name.value);
                                                          endPos($value, $name.start); } // start is the CommonTree for the node
	| ^(FUNC_APPLY e0=expression expressionList)	{ $value = F.at(pos($FUNC_APPLY)).Apply(null, $e0.value, $expressionList.args.toList());
                                                          endPos($value, $FUNC_APPLY); }
	| ^(SEQ_INDEX seq=expression idx=expression rb=RBRACKET?)
                                                        { $value = F.at($seq.value.pos).SequenceIndexed($seq.value, $idx.value);
                                                          endPos($value, $rb); }
	| ^(SEQ_SLICE seq=expression first=expression last=expression?)
	                                                { $value = F.at(pos($SEQ_SLICE)).SequenceSlice($seq.value, $first.value, $last.value,
	                                                                                              SequenceSliceTree.END_INCLUSIVE);
                                                          endPos($value, $SEQ_SLICE); }
	| ^(SEQ_SLICE_EXCLUSIVE seq=expression first=expression last=expression?)
	                                                { $value = F.at(pos($SEQ_SLICE_EXCLUSIVE)).SequenceSlice($seq.value, $first.value, $last.value,
	                                                                                                        SequenceSliceTree.END_EXCLUSIVE);
                                                          endPos($value, $SEQ_SLICE_EXCLUSIVE); }
	| ^(OBJECT_LIT i=qualident objectLiteral)	{ $value = F.at(getStartPos($i.value)).ObjectLiteral($qualident.value, $objectLiteral.parts.toList());
                                                          endPos($value, $OBJECT_LIT); }
       	| ^(FUNC_EXPR formalParameters type blockExpression)
       							{ $value = F.at(pos($FUNC_EXPR)).FunctionValue($type.type, $formalParameters.params.toList(),
                                               								$blockExpression.value);
                                                          endPos($value, $FUNC_EXPR); }
	| ^(NEW typeName expressionList)		{ $value = F.at(pos($NEW)).InstanciateNew($typeName.value, $expressionList.args.toList());
                                                          endPos($value, $NEW); }
       	| ^(INDEXOF identifier)                		{ $value = F.at(pos($INDEXOF)).Indexof($identifier.value);
                                                          endPos($value, $INDEXOF); }
	| pipeExpression				{ $value = $pipeExpression.value; }
	| blockExpression				{ $value = $blockExpression.value; }
       	| stringExpression				{ $value = $stringExpression.value; }
	| explicitSequenceExpression			{ $value = $explicitSequenceExpression.value; }
	| ^(DOTDOT from=expression to=expression step=expression? LT?)
							{ $value = F.at(pos($DOTDOT)).RangeSequence($from.value, $to.value, $step.value, $LT!=null);
                                                          endPos($value, $DOTDOT); }
	| SEQ_EMPTY					{ $value = F.at(pos($SEQ_EMPTY)).EmptySequence();
                                                          endPos($value, $SEQ_EMPTY); }
       	| THIS						{ $value = F.at(pos($THIS)).Ident(names._this);
                                                          endPos($value, $THIS); }
       	| SUPER						{ $value = F.at(pos($SUPER)).Ident(names._super);
                                                          endPos($value, $SUPER); }
       	| identifier					{ $value = $identifier.value; }
	| t=STRING_LITERAL				{ $value = F.at(pos($t)).Literal(TypeTags.CLASS, $t.text);
                                                          endPos($value, $t); }
	| t=DECIMAL_LITERAL				{ $value = F.at(pos($t)).Literal(TypeTags.INT, (int)Convert.string2long($t.text, 10));
                                                          endPos($value, $t); }
	| t=OCTAL_LITERAL				{ $value = F.at(pos($t)).Literal(TypeTags.INT, (int)Convert.string2long($t.text, 8));
                                                          endPos($value, $t); }
	| t=HEX_LITERAL					{ $value = F.at(pos($t)).Literal(TypeTags.INT, (int)Convert.string2long($t.text, 16));
                                                          endPos($value, $t); }
	| t=FLOATING_POINT_LITERAL 			{ $value = F.at(pos($t)).Literal(TypeTags.DOUBLE, Double.valueOf($t.text));
                                                          endPos($value, $t); }
	| t=TIME_LITERAL         			{ $value = F.at(pos($t)).TimeLiteral($t.text);
                                                          endPos($value, $t); }
	| t=TRUE   					{ $value = F.at(pos($t)).Literal(TypeTags.BOOLEAN, 1);
                                                          endPos($value, $t); }
	| t=FALSE   					{ $value = F.at(pos($t)).Literal(TypeTags.BOOLEAN, 0);
                                                          endPos($value, $t); }
	| t=NULL 					{ $value = F.at(pos($t)).Literal(TypeTags.BOT, null);
                                                          endPos($value, $t); }
        | ^(SUCHTHAT target=expression such=expression (interpolate=expression)?)
                                                        { $value = F.at(pos($SUCHTHAT)).InterpolateValue($target.value, $such.value, $interpolate.value);
                                                          endPos($value, $SUCHTHAT);
                                                        }
        | ^(AT d=expression k=keyFrameLiteralPart)
                                                        { $value = F.at(pos($AT)).KeyFrameLiteral($d.value, $k.exprs.toList(), null);
                                                          endPos($value, $AT);
                                                        }
        ;
keyFrameLiteralPart returns [ListBuffer<JFXExpression> exprs = new ListBuffer<JFXExpression>(); ]
        : ^(KEY_FRAME_PART ( expression                 { exprs.append($expression.value); }
                           )+
           )
        ;
inClauses  returns [ListBuffer<JFXForExpressionInClause> clauses = ListBuffer.lb()]
	: ( inClause					{ clauses.append($inClause.value); } )*
	;
inClause  returns [JFXForExpressionInClause value]
	: ^(IN formalParameter se=expression we=expression?)
							{ $value = F.at(pos($IN)).InClause($formalParameter.var, $se.value, $we.value);
                                                          endPos($value, $IN); }
	;
pipeExpression  returns [JFXExpression value] //TODO: this is a hack
	: ^(PIPE seq=expression name cond=expression)	{ ListBuffer<JFXForExpressionInClause> clauses = ListBuffer.lb();
                  					  JFXVar var = F.at($name.pos).Param($name.value, F.TypeUnknown());
	          					  clauses.append(F.at(pos($PIPE)).InClause(var, $seq.value, $cond.value));
                  					  $value = F.at(pos($PIPE)).Predicate(clauses.toList(), F.at($name.pos).Ident($name.value));
                                                          endPos($value, $PIPE);
							}
	;
stringExpression  returns [JFXExpression value]
@init { ListBuffer<JFXExpression> strexp = new ListBuffer<JFXExpression>();
        String translationKey = null; }
	: ^(tk=TRANSLATION_KEY                          { translationKey = $tk.text; }
            STRING_LITERAL                              { strexp.append(F.at(pos($STRING_LITERAL)).Literal(TypeTags.CLASS, $STRING_LITERAL.text));
                                                          endPos($value, $STRING_LITERAL); }
		  					{ $value = F.at(pos($STRING_LITERAL)).StringExpression(strexp.toList(), translationKey);
                                                          endPos($value, $STRING_LITERAL); }
           )
	| ^(QUOTE_LBRACE_STRING_LITERAL			{ strexp.append(F.at(pos($QUOTE_LBRACE_STRING_LITERAL)).Literal(TypeTags.CLASS,
												 $QUOTE_LBRACE_STRING_LITERAL.text));
                                                          endPos($value, $QUOTE_LBRACE_STRING_LITERAL); }
		  f1=stringFormat			{ strexp.append($f1.value); }
		  e1=expression 			{ strexp.append($e1.value); }
		  (  rl=RBRACE_LBRACE_STRING_LITERAL	{ strexp.append(F.at(pos($rl)).Literal(TypeTags.CLASS, $rl.text)); }
		     fn=stringFormat			{ strexp.append($fn.value); }
		     en=expression 			{ strexp.append($en.value); }
		  )*
		  rq=RBRACE_QUOTE_STRING_LITERAL	{ strexp.append(F.at(pos($rq)).Literal(TypeTags.CLASS, $rq.text)); }
		  					{ $value = F.at(pos($QUOTE_LBRACE_STRING_LITERAL)).StringExpression(strexp.toList(), translationKey);
                                                          endPos($value, $QUOTE_LBRACE_STRING_LITERAL); }
	    )
	| ^(tk=TRANSLATION_KEY                          { translationKey = $tk.text; }
            QUOTE_LBRACE_STRING_LITERAL			{ strexp.append(F.at(pos($QUOTE_LBRACE_STRING_LITERAL)).Literal(TypeTags.CLASS,
												 $QUOTE_LBRACE_STRING_LITERAL.text));
                                                          endPos($value, $QUOTE_LBRACE_STRING_LITERAL); }
		  f1=stringFormat			{ strexp.append($f1.value); }
		  e1=expression 			{ strexp.append($e1.value); }
		  (  rl=RBRACE_LBRACE_STRING_LITERAL	{ strexp.append(F.at(pos($rl)).Literal(TypeTags.CLASS, $rl.text)); }
		     fn=stringFormat			{ strexp.append($fn.value); }
		     en=expression 			{ strexp.append($en.value); }
		  )*
		  rq=RBRACE_QUOTE_STRING_LITERAL	{ strexp.append(F.at(pos($rq)).Literal(TypeTags.CLASS, $rq.text)); }
		  					{ $value = F.at(pos($QUOTE_LBRACE_STRING_LITERAL)).StringExpression(strexp.toList(), translationKey);
                                                          endPos($value, $QUOTE_LBRACE_STRING_LITERAL); }
	    )
	;
stringFormat  returns [JFXExpression value]
	: fs=FORMAT_STRING_LITERAL			{ $value = F.at(pos($fs)).Literal(TypeTags.CLASS, $fs.text); }
	| EMPTY_FORMAT_STRING				{ $value = F.             Literal(TypeTags.CLASS, ""); }
	;
explicitSequenceExpression   returns [JFXSequenceExplicit value]
@init { ListBuffer<JFXExpression> exps = new ListBuffer<JFXExpression>(); }
	: ^(SEQ_EXPLICIT
	    ( expression 				{ exps.append($expression.value); } )*
	   )						{ $value = F.at(pos($SEQ_EXPLICIT)).ExplicitSequence(exps.toList());
                                                          endPos($value, $SEQ_EXPLICIT); }
	;
objectLiteral  returns [ListBuffer<JFXTree> parts = ListBuffer.<JFXTree>lb()]
	: ( objectLiteralPart  				{ $parts.append($objectLiteralPart.value); } ) *
	;
objectLiteralPart  returns [JFXTree value]
	: ^(OBJECT_LIT_PART n=name boundExpression)	{ $value = F.at($n.pos).ObjectLiteralPart($name.value,
								 $boundExpression.value, $boundExpression.status);
                                                          endPos($value, $OBJECT_LIT_PART); }
       	| variableDeclaration				{ $value = $variableDeclaration.value; }
       	| overrideDeclaration				{ $value = $overrideDeclaration.value; }
       	| functionDefinition				{ $value = $functionDefinition.value; }
       	;
expressionList  returns [ListBuffer<JFXExpression> args = new ListBuffer<JFXExpression>()]
	: ^(EXPR_LIST (expression			{ $args.append($expression.value); }  )* )
	;
type  returns [JFXType type]
	: ^(TYPE_NAMED typeName cardinality)		{ $type = F.at($typeName.value.pos).TypeClass($typeName.value, $cardinality.ary);
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
typeName  returns [JFXExpression value]
	: ^(TYPE_ARG qualident genericArguments)	{ log.error(pos($TYPE_ARG), "javafx.generalerror", "Java generic type declarations are not currently supported"); }
                                                        //TODO: remove or implement -- { $value = F.at(pos($TYPE_ARG)).TypeApply($qualident.value, $genericArguments.exprbuff.toList());
                                                        { $value = $qualident.value; // so that things don't fall over
                                                          endPos($value, $TYPE_ARG); }
	| qualident					{ $value = $qualident.value; }
	;
genericArguments  returns [ListBuffer<JFXExpression> exprbuff = ListBuffer.<JFXExpression>lb()]
	: ( genericArgument				{ $exprbuff.append($genericArgument.value); } )*
	;
genericArgument  returns [JFXExpression value]
@init { BoundKind bk = BoundKind.UNBOUND; JFXExpression texpr = null; }
	: typeName					{ $value = $typeName.value; }
	| ^(QUES (  ( EXTENDS 				{ bk = BoundKind.EXTENDS; }
		    | SUPER				{ bk = BoundKind.SUPER; }
		    )
		 typeName				{ texpr = $typeName.value; }
	         )?					{ //TODO: remove or implement -- $value = F.at(pos($QUES)).Wildcard(F.TypeBoundKind(bk), texpr);
                                                          endPos($value, $QUES); }
	    )
	;
qualident  returns [JFXExpression value]
	: name 				          	{ $value = F.at($name.pos).Ident($name.value);
                                                          endPos($value, $name.pos + $name.value.length()); }
	| ^(DOT id=qualident name)			{ $value = F.at(pos($DOT)).Select($id.value, $name.value);
                                                          endPos($value, $DOT); }
	;
identifier  returns [JFXIdent value]
	: name 				          	{ $value = F.at($name.pos).Ident($name.value);
                                                          endPos($value, $name.pos + $name.value.length()); }
	;
name returns [Name value, int pos]
	: IDENTIFIER					{ $value = Name.fromString(names, $IDENTIFIER.text); $pos = pos($IDENTIFIER); }
	;
