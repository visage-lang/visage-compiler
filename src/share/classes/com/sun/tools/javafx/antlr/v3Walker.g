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
import com.sun.tools.javac.tree.JCTree.*;
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
    void setDocComment(JCTree tree, CommonTree comment) {
        if (comment != null) {
            if (docComments == null)
                docComments = new HashMap<JCTree,String>();
            docComments.put(tree, comment.getText());
        }
    }
    HashMap<JCTree,String> docComments;
    HashMap<JCTree,Integer> endPositions;

    void endPos(JCTree tree, CommonTree node) {
        int endIndex = node.getTokenStopIndex();
        if (endIndex != -1) { // -1 means no such token
            TokenStream src = input.getTokenStream();
            CommonToken endToken = (CommonToken)src.get(endIndex);
            // backtrack over WS and imaginary tokens
            while (endToken.getType() == WS || endToken.getCharPositionInLine() == -1) { 
                if (--endIndex < 0)
                    return;
                endToken = (CommonToken)src.get(endIndex);
            }
            int endPos = endToken.getStopIndex();
            endPos(tree, endPos);
        }
    }

    void endPos(JCTree tree, com.sun.tools.javac.util.List<JFXInterpolateValue> list) {
        int endLast = endPositions.get(list.last());
        endPositions.put(tree, endLast);
    }

    void endPos(JCTree tree, int end) {
        endPositions.put(tree, end);
    }
}
	
module returns [JCCompilationUnit result]
@init   { docComments = null; 
          endPositions = new HashMap<JCTree,Integer>(); }
@after  { $result.docComments = docComments; 
          $result.endPositions = endPositions; }
	: ^(MODULE packageDecl? moduleItems)		{ $result = F.TopLevel(noJCAnnotations(), $packageDecl.value, $moduleItems.items.toList()); }
       	;
packageDecl  returns [JCExpression value]
       	: ^(PACKAGE qualident)        			{ $value = $qualident.expr; }
	;
moduleItems  returns [ListBuffer<JCTree> items = new ListBuffer<JCTree>()]  
	: ( moduleItem					{ $items.append($moduleItem.value); }
	  )*
	;
moduleItem  returns [JCTree value]
	: importDecl 					{ $value = $importDecl.value; }
	| classDefinition 				{ $value = $classDefinition.value; }
	| statement      				{ $value = $statement.value; } 
	| expression 					{ $value = $expression.expr; } 
	;
importDecl  returns [JCTree value]
 	: ^(IMPORT importId)				{ $value = F.at(pos($IMPORT)).Import($importId.pid, false); 
                                                          endPos($value, $IMPORT); }
	;
importId  returns [JCExpression pid]
 	: identifier					{ $pid = $identifier.expr; }
        | ^(DOT in=importId	( name 			{ $pid = F.at($name.pos).Select($in.pid, $name.value); }
        			| STAR			{ $pid = F.at(pos($STAR)).Select($in.pid, names.asterisk); }
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
supers  returns [ListBuffer<JCExpression> ids = new ListBuffer<JCExpression>()]
	: ^(EXTENDS (typeName           		{ $ids.append($typeName.expr); } )* )
	;	  					
classMembers  returns [ListBuffer<JCTree> mems = new ListBuffer<JCTree>()]
	: ^(CLASS_MEMBERS ( classMember			{ $mems.append($classMember.member); } )* )
	;
classMember  returns [JCTree member]
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
modifiers  returns [JCModifiers mods]
	: ^(MODIFIER modifierFlags)
	 		 				{ mods = F.at(pos($MODIFIER)).Modifiers($modifierFlags.flags); }
	;
modifierFlags   returns [long flags = 0L]
	:  (modifierFlag          			{ flags |= $modifierFlag.flag; } )*
	;
modifierFlag   returns [long flag]
	:  BOUND          				{ flag = JavafxFlags.BOUND; }
	|  PUBLIC          				{ flag = Flags.PUBLIC; }
	|  PRIVATE         				{ flag = Flags.PRIVATE; }
	|  PROTECTED       				{ flag = Flags.PROTECTED; } 
	|  ABSTRACT        				{ flag = Flags.ABSTRACT; }
	|  READONLY        				{ flag = Flags.FINAL; } 
	|  STATIC        				{ flag = Flags.STATIC; } 
	;
formalParameters  returns [ListBuffer<JFXVar> params = new ListBuffer<JFXVar>()]
	: ^(LPAREN (formalParameter			{ params.append($formalParameter.var); } )* )
	;
formalParameter returns [JFXVar var]
	: ^(PARAM name type)				{ $var = F.at($name.pos).Param($name.value, $type.type); } 
	;
formalParameterOpt returns [JFXVar var]
	: formalParameter				{ $var = $formalParameter.var; } 
	|						{ $var = null; } 
	;
block  returns [JCBlock value]
@init { ListBuffer<JCStatement> stats = ListBuffer.<JCStatement>lb(); }
	: ^(BLOCK
		(	^(STATEMENT statement)		{ stats.append($statement.value); }	
		| 	^(EXPRESSION expression)	{ stats.append(F.at($expression.expr.pos).Exec($expression.expr)); }
		)*
	    )						{ $value = F.at(pos($BLOCK)).Block(0L, stats.toList()); 
                                                          endPos($value, $BLOCK); }
	;
blockExpression  returns [JFXBlockExpression expr]
@init { ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>(); JCExpression val = null; }
	: ^(LBRACE 
		(	^(STATEMENT statement)		{ if (val != null) { stats.append(F.at(val.pos).Exec(val)); val = null; }
	     					  	  stats.append($statement.value); }
		| 	^(EXPRESSION expression)	{ if (val != null) { stats.append(F.at(val.pos).Exec(val)); }
	     					  	  val = $expression.expr; }
		)*
	    )						{ $expr = F.at(pos($LBRACE)).BlockExpression(0L, stats.toList(), val); 
                                                          endPos($expr, $LBRACE); }
	;
variableDeclaration    returns [JCStatement value]
	: ^(VAR variableLabel modifiers name type boundExpressionOpt onReplaceClause? DOC_COMMENT?)
	    						{ $value = F.at(pos($VAR)).Var($name.value, 
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
statement returns [JCStatement value]
	: variableDeclaration				{ $value = $variableDeclaration.value; }
	| functionDefinition 				{ $value = $functionDefinition.value; }
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
	| ^(BEFORE elem=expression ^(SEQ_INDEX eseq=expression idx=expression))
							{ $value = F.at(pos($BEFORE)).SequenceInsert($eseq.expr, $elem.expr, $idx.expr, false); 
                                                          endPos($value, $BEFORE); } 
	| ^(AFTER elem=expression ^(SEQ_INDEX eseq=expression idx=expression))
							{ $value = F.at(pos($AFTER)).SequenceInsert($eseq.expr, $elem.expr, $idx.expr, true); 
                                                          endPos($value, $AFTER); } 
	| ^(FROM e1=expression e2=expression)		{ $value = F.at(pos($FROM)).SequenceDelete($e2.expr,$e1.expr); 
                                                          endPos($value, $FROM); } 
	| ^(DELETE expression)				{ $value = F.at(pos($DELETE)).SequenceDelete($expression.expr); 
                                                          endPos($value, $DELETE); } 
	| ^(RETURN expression?)				{ $value = F.at(pos($RETURN)).Return($expression.expr); 
                                                          endPos($value, $RETURN); } 
	| ^(TRY block catchClauses finallyClause?)	{ $value = F.at(pos($TRY)).Try($block.value, $catchClauses.caught.toList(), $finallyClause.value); 
                                                          endPos($value, $TRY); }
       	;
catchClauses  returns [ListBuffer<JCCatch> caught = ListBuffer.lb()]
	: ( catchClause					{ $caught.append($catchClause.value); } )*
	;
catchClause  returns [JCCatch value]
	: ^(CATCH formalParameter block)		{ $value = F.at(pos($CATCH)).Catch($formalParameter.var, $block.value); 
                                                          endPos($value, $CATCH); } 
	;
finallyClause  returns [JCBlock value]
	: ^(FINALLY block)				{ $value = $block.value; }
	;
boundExpression   returns [JavafxBindStatus status, JCExpression expr]
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
boundExpressionOpt   returns [JavafxBindStatus status, JCExpression expr]
	: boundExpression				{ $expr = $boundExpression.expr; $status = $boundExpression.status; }
	| 						{ $expr = null; $status = UNBOUND; }
	;
expression  returns [JCExpression expr]
       	: ^(FOR inClauses e0=expression)		{ $expr = F.at(pos($FOR)).ForExpression($inClauses.clauses.toList(), $e0.expr); 
                                                          endPos($expr, $FOR); }
	| ^(IF econd=expression ethen=expression eelse=expression?)
							{ $expr = F.at(pos($IF)).Conditional($econd.expr, $ethen.expr, $eelse.expr); 
                                                          endPos($expr, $IF); }
	| ^(EQ lhs=expression rhs=expression)		{ $expr = F.at(pos($EQ)).Assign($lhs.expr, $rhs.expr); 
                                                          endPos($expr, $EQ); } 
	| ^(PLUSEQ lhs=expression rhs=expression) 	{ $expr = F.at(pos($PLUSEQ)).Assignop(JCTree.PLUS_ASG, $lhs.expr, $rhs.expr); 
                                                          endPos($expr, $PLUSEQ); }
	| ^(SUBEQ lhs=expression rhs=expression) 	{ $expr = F.at(pos($SUBEQ)).Assignop(JCTree.MINUS_ASG, $lhs.expr, $rhs.expr); 
                                                          endPos($expr, $SUBEQ); }
	| ^(STAREQ lhs=expression rhs=expression) 	{ $expr = F.at(pos($STAREQ)).Assignop(JCTree.MUL_ASG, $lhs.expr, $rhs.expr); 
                                                          endPos($expr, $STAREQ); }
	| ^(SLASHEQ lhs=expression rhs=expression) 	{ $expr = F.at(pos($SLASHEQ)).Assignop(JCTree.DIV_ASG, $lhs.expr, $rhs.expr); 
                                                          endPos($expr, $SLASHEQ); }
	| ^(PERCENTEQ lhs=expression rhs=expression) 	{ $expr = F.at(pos($PERCENTEQ)).Assignop(JCTree.MOD_ASG, $lhs.expr, $rhs.expr); 
                                                          endPos($expr, $PERCENTEQ); }
	| ^(AND e1=expression e2=expression) 		{ $expr = F.at(pos($AND)).Binary(JCTree.AND, $e1.expr, $e2.expr); 
                                                          endPos($expr, $AND); }
	| ^(OR e1=expression e2=expression) 		{ $expr = F.at(pos($OR)).Binary(JCTree.OR, $e1.expr, $e2.expr); 
                                                          endPos($expr, $OR); } 
	| ^(INSTANCEOF e0=expression type)		{ $expr = F.at(pos($INSTANCEOF)).TypeTest($e0.expr, $type.type); 
                                                          endPos($expr, $INSTANCEOF); }
	| ^(AS e0=expression type)			{ $expr = F.at(pos($AS)).TypeCast($type.type, $e0.expr); 
                                                          endPos($expr, $AS); }   
	| ^(LTGT e1=expression e2=expression)		{ $expr = F.at(pos($LTGT)).Binary(JCTree.NE, $e1.expr, $e2.expr); 
                                                          endPos($expr, $LTGT); }
	| ^(EQEQ e1=expression e2=expression)		{ $expr = F.at(pos($EQEQ)).Binary(JCTree.EQ, $e1.expr, $e2.expr); 
                                                          endPos($expr, $EQEQ); }
	| ^(LTEQ e1=expression e2=expression)		{ $expr = F.at(pos($LTEQ)).Binary(JCTree.LE, $e1.expr, $e2.expr); 
                                                          endPos($expr, $LTEQ); }
	| ^(GTEQ e1=expression e2=expression)		{ $expr = F.at(pos($GTEQ)).Binary(JCTree.GE, $e1.expr, $e2.expr); 
                                                          endPos($expr, $GTEQ); }
	| ^(LT   e1=expression e2=expression)		{ $expr = F.at(pos($LT))  .Binary(JCTree.LT, $e1.expr, $e2.expr); 
                                                          endPos($expr, $LT); }
	| ^(GT   e1=expression e2=expression)		{ $expr = F.at(pos($GT))  .Binary(JCTree.GT, $e1.expr, $e2.expr); 
                                                          endPos($expr, $GT); }
	| ^(PLUS e1=expression e2=expression)		{ $expr = F.at(pos($PLUS)).Binary(JCTree.PLUS , $e1.expr, $e2.expr); 
                                                          endPos($expr, $PLUS); }
	| ^(SUB  e1=expression e2=expression)		{ $expr = F.at(pos($SUB)) .Binary(JCTree.MINUS, $e1.expr, $e2.expr); 
                                                          endPos($expr, $SUB); }
	| ^(STAR    e1=expression e2=expression)	{ $expr = F.at(pos($STAR))   .Binary(JCTree.MUL  , $e1.expr, $e2.expr); 
                                                          endPos($expr, $STAR); }
	| ^(SLASH   e1=expression e2=expression)	{ $expr = F.at(pos($SLASH))  .Binary(JCTree.DIV  , $e1.expr, $e2.expr); 
                                                          endPos($expr, $SLASH); }
	| ^(PERCENT e1=expression e2=expression)	{ $expr = F.at(pos($PERCENT)).Binary(JCTree.MOD  , $e1.expr, $e2.expr); 
                                                          endPos($expr, $PERCENT); }   
	| ^(NEGATIVE e0=expression)			{ $expr = F.at(pos($NEGATIVE)).Unary(JCTree.NEG, $e0.expr); 
                                                          endPos($expr, $NEGATIVE); }
	| ^(NOT e0=expression)				{ $expr = F.at(pos($NOT)).Unary(JCTree.NOT, $e0.expr); 
                                                          endPos($expr, $NOT); }	
	| ^(SIZEOF e0=expression)			{ $expr = F.at(pos($SIZEOF)).Unary(JavafxTag.SIZEOF, $e0.expr); 
                                                          endPos($expr, $SIZEOF); }
	| ^(PLUSPLUS e0=expression)   			{ $expr = F.at(pos($PLUSPLUS)).Unary(JCTree.PREINC, $e0.expr); 
                                                          endPos($expr, $PLUSPLUS); }
	| ^(SUBSUB e0=expression) 			{ $expr = F.at(pos($SUBSUB)).Unary(JCTree.PREDEC, $e0.expr); 
                                                          endPos($expr, $SUBSUB); }
	| ^(REVERSE e0=expression) 			{ $expr = F.at(pos($REVERSE)).Unary(JavafxTag.REVERSE, $e0.expr); 
                                                          endPos($expr, $REVERSE); }
	| ^(POSTINCR e0=expression)			{ $expr = F.at($e0.expr.pos).Unary(JCTree.POSTINC, $e0.expr); }
	| ^(POSTDECR e0=expression)			{ $expr = F.at($e0.expr.pos).Unary(JCTree.POSTDEC, $e0.expr); }
	| ^(DOT e0=expression name)			{ $expr = F.at(pos($DOT)).Select($e0.expr, $name.value); 
                                                          endPos($expr, $DOT); }
	| ^(FUNC_APPLY e0=expression expressionList)	{ $expr = F.at(pos($FUNC_APPLY)).Apply(null, $e0.expr, $expressionList.args.toList()); 
                                                          endPos($expr, $FUNC_APPLY); } 
	| ^(SEQ_INDEX seq=expression idx=expression)	{ $expr = F.at(pos($SEQ_INDEX)).SequenceIndexed($seq.expr, $idx.expr); 
                                                          endPos($expr, $SEQ_INDEX); }
	| ^(SEQ_SLICE seq=expression first=expression last=expression?)
	                                                { $expr = F.at(pos($SEQ_SLICE)).SequenceSlice($seq.expr, $first.expr, $last.expr, 
	                                                                                              SequenceSliceTree.END_INCLUSIVE); 
                                                          endPos($expr, $SEQ_SLICE); }
	| ^(SEQ_SLICE_EXCLUSIVE seq=expression first=expression last=expression?)
	                                                { $expr = F.at(pos($SEQ_SLICE_EXCLUSIVE)).SequenceSlice($seq.expr, $first.expr, $last.expr,  
	                                                                                                        SequenceSliceTree.END_EXCLUSIVE); 
                                                          endPos($expr, $SEQ_SLICE_EXCLUSIVE); }
	| ^(OBJECT_LIT i=qualident objectLiteral)	{ $expr = F.at($i.expr.pos).Instanciate($qualident.expr, null, $objectLiteral.parts.toList()); } 
       	| ^(FUNC_EXPR formalParameters type blockExpression)
       							{ $expr = F.at(pos($FUNC_EXPR)).OperationValue($type.type, $formalParameters.params.toList(),
                                               								$blockExpression.expr); 
                                                          endPos($expr, $FUNC_EXPR); }
	| ^(NEW typeName expressionList)		{ $expr = F.at(pos($NEW)).Instanciate($typeName.expr, $expressionList.args.toList(), null); 
                                                          endPos($expr, $NEW); }
       	| ^(INDEXOF name)                      		{ $expr = F.at(pos($INDEXOF)).Indexof($name.value); 
                                                          endPos($expr, $INDEXOF); }
	| pipeExpression				{ $expr = $pipeExpression.expr; }
	| blockExpression				{ $expr = $blockExpression.expr; }
       	| stringExpression				{ $expr = $stringExpression.expr; }
        | interpolateExpression                         { $expr = $interpolateExpression.expr; }
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
	| t=DECIMAL_LITERAL				{ $expr = F.at(pos($t)).Literal(TypeTags.INT, Convert.string2int($t.text, 10)); 
                                                          endPos($expr, $t); }
	| t=OCTAL_LITERAL				{ $expr = F.at(pos($t)).Literal(TypeTags.INT, Convert.string2int($t.text, 8)); 
                                                          endPos($expr, $t); }
	| t=HEX_LITERAL					{ $expr = F.at(pos($t)).Literal(TypeTags.INT, Convert.string2int($t.text, 16)); 
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
	;
inClauses  returns [ListBuffer<JFXForExpressionInClause> clauses = ListBuffer.lb()]
	: ( inClause					{ clauses.append($inClause.value); } )*	
	;
inClause  returns [JFXForExpressionInClause value] 
	: ^(IN formalParameter se=expression we=expression?)
							{ $value = F.at(pos($IN)).InClause($formalParameter.var, $se.expr, $we.expr); 
                                                          endPos($value, $IN); }
	;
pipeExpression  returns [JCExpression expr] //TODO: this is a hack
	: ^(PIPE seq=expression name cond=expression)	{ ListBuffer<JFXForExpressionInClause> clauses = ListBuffer.lb(); 
                  					  JFXVar var = F.at($name.pos).Param($name.value, F.TypeUnknown());
	          					  clauses.append(F.at(pos($PIPE)).InClause(var, $seq.expr, $cond.expr));
                  					  $expr = F.at(pos($PIPE)).ForExpression(clauses.toList(), F.at($name.pos).Ident($name.value)); 
                                                          endPos($expr, $PIPE);
							}
	;
stringExpression  returns [JCExpression expr] 
@init { ListBuffer<JCExpression> strexp = new ListBuffer<JCExpression>(); 
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
stringFormat  returns [JCExpression expr] 
	: fs=FORMAT_STRING_LITERAL			{ $expr = F.at(pos($fs)).Literal(TypeTags.CLASS, $fs.text); }
	| EMPTY_FORMAT_STRING				{ $expr = F.             Literal(TypeTags.CLASS, ""); }
	;
interpolateExpression  returns [JCExpression expr]
@init { ListBuffer<JFXInterpolateValue> tweenProps = new ListBuffer<JFXInterpolateValue>(); }
        : ^(SUCHTHAT identifier
            ( tweenValue                                { tweenProps.append($tweenValue.prop); } )*
           )                                            { $expr = F.at($identifier.expr.pos).Interpolate($identifier.expr, tweenProps.toList()); 
                                                          endPos($expr, tweenProps.toList()); }
        | ^(SUCHTHAT_BLOCK identifier
            ( namedTweenValue                           { tweenProps.append($namedTweenValue.prop); } )*
           )                                            { $expr = F.at($identifier.expr.pos).Interpolate($identifier.expr, tweenProps.toList()); 
                                                          endPos($expr, tweenProps.toList()); }
        ;
tweenValue returns [JFXInterpolateValue prop]
        : ^(TWEEN expression name)                      { $prop = F.at($expression.expr.pos).InterpolateValue(null, $expression.expr, $name.value); 
                                                          endPos($prop, $name.pos + $name.value.length()); }
        ;
namedTweenValue returns [JFXInterpolateValue prop]
        : ^(NAMED_TWEEN identifier expression name?)
                                                        { $prop = F.at($identifier.expr.pos).InterpolateValue($identifier.expr, $expression.expr, $name.value); 
                                                          endPos($prop, $name.value != null ? $name.pos + $name.value.length() : $expression.expr.pos); }
        ;
explicitSequenceExpression   returns [JFXSequenceExplicit expr]
@init { ListBuffer<JCExpression> exps = new ListBuffer<JCExpression>(); }
	: ^(SEQ_EXPLICIT   
	    ( expression 				{ exps.append($expression.expr); } )*
	   )						{ $expr = F.at(pos($SEQ_EXPLICIT)).ExplicitSequence(exps.toList()); 
                                                          endPos($expr, $SEQ_EXPLICIT); }
	;
objectLiteral  returns [ListBuffer<JCTree> parts = ListBuffer.<JCTree>lb()]
	: ( objectLiteralPart  				{ $parts.append($objectLiteralPart.value); } ) * 
	;
objectLiteralPart  returns [JCTree value]
	: ^(OBJECT_LIT_PART n=name boundExpression)	{ $value = F.at($n.pos).ObjectLiteralPart($name.value,
								 $boundExpression.expr, $boundExpression.status); }
       	| variableDeclaration				{ $value = $variableDeclaration.value; }
       	| overrideDeclaration				{ $value = $overrideDeclaration.value; }
       	| functionDefinition				{ $value = $functionDefinition.value; }
       	;
expressionList  returns [ListBuffer<JCExpression> args = new ListBuffer<JCExpression>()] 
	: ^(EXPR_LIST (expression			{ $args.append($expression.expr); }  )* )
	;
type  returns [JFXType type]
	: ^(TYPE_NAMED typeName cardinality)		{ $type = F.at(pos($TYPE_NAMED)).TypeClass($typeName.expr, $cardinality.ary); 
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
typeName  returns [JCExpression expr]
	: ^(TYPE_ARG qualident genericArguments)	{ $expr = F.at(pos($TYPE_ARG)).TypeApply($qualident.expr, $genericArguments.exprbuff.toList()); 
                                                          endPos($expr, $TYPE_ARG); }
	| qualident					{ $expr = $qualident.expr; }
	;
genericArguments  returns [ListBuffer<JCExpression> exprbuff = ListBuffer.<JCExpression>lb()]
	: ( genericArgument				{ $exprbuff.append($genericArgument.expr); } )* 
	;	
genericArgument  returns [JCExpression expr]
@init { BoundKind bk = BoundKind.UNBOUND; JCExpression texpr = null; }
	: typeName					{ $expr = $typeName.expr; }
	| ^(QUES (  ( EXTENDS 				{ bk = BoundKind.EXTENDS; }
		    | SUPER				{ bk = BoundKind.SUPER; }
		    ) 
		 typeName				{ texpr = $typeName.expr; }
	         )?					{ $expr = F.at(pos($QUES)).Wildcard(F.TypeBoundKind(bk), texpr); 
                                                          endPos($expr, $QUES); }
	    )
	;
qualident  returns [JCExpression expr]
	: name 				          	{ $expr = F.at($name.pos).Ident($name.value); 
                                                          endPos($expr, $name.pos + $name.value.length()); } 
	| ^(DOT id=qualident name)			{ $expr = F.at(pos($DOT)).Select($id.expr, $name.value); 
                                                          endPos($expr, $DOT); } 
	;
identifier  returns [JCIdent expr]
	: name 				          	{ $expr = F.at($name.pos).Ident($name.value); } 
	;
name returns [Name value, int pos]
	: IDENTIFIER					{ $value = Name.fromString(names, $IDENTIFIER.text); $pos = pos($IDENTIFIER); }
	;
