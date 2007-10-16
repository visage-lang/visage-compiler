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

grammar v2;

options { superClass = AbstractGeneratedParser; }
 
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
   OPERATION='operation';
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
   IMPLEMENTS='implements';
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


}

@lexer::header {
package com.sun.tools.javafx.antlr;
}

@header {
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
}

@members {
        public v2Parser(Context context, CharSequence content) {
           this(new CommonTokenStream(new v2Lexer(new ANTLRStringStream(content.toString()))));
           initialize(context);
    	}
}

@lexer::members {
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

module returns [JCCompilationUnit result]
       : packageDecl? moduleItems EOF 		{ $result = F.TopLevel(noJCAnnotations(), $packageDecl.value, $moduleItems.items.toList()); };
packageDecl returns [JCExpression value]
       : PACKAGE qualident SEMI         	{ $value = $qualident.expr; };
moduleItems       returns [ListBuffer<JCTree> items = new ListBuffer<JCTree>()]
	:    ( mi1=moduleItem     		{ items.append($mi1.value); }
	     ) ?
	  (SEMI
	     ( mi2=moduleItem     		{ items.append($mi2.value); }
	     ) ?
	  ) *
	;
moduleItem returns [JCTree value]
       : importDecl 				{ $value = $importDecl.value; }
       | classDefinition 			{ $value = $classDefinition.value; }
       | statement      			{ $value = $statement.value; } 
       | expression 				{ $value = F.Exec($expression.expr); } 
       ;
importDecl returns [JCTree value]
@init { JCExpression pid = null; }
        : IMPORT  identifier			{ pid = $identifier.expr; }
                 ( DOT name			{ pid = F.at($name.pos).Select(pid, $name.value); } )* 
                 ( DOT STAR			{ pid = F.at(pos($STAR)).Select(pid, names.asterisk); } )?  
          					{ $value = F.at(pos($IMPORT)).Import(pid, false); } ;
classDefinition returns [JFXClassDeclaration value]
	: classModifierFlags  CLASS name supers interfaces LBRACE classMembers RBRACE
	  					{ $value = F.at(pos($CLASS)).ClassDeclaration(
	  						F.at(pos($CLASS)).Modifiers($classModifierFlags.flags),
	  						$name.value,
	                                	        $supers.ids.toList(), $interfaces.ids.toList(), 
	                                	        $classMembers.mems.toList()); } ;
supers returns [ListBuffer<JCExpression> ids = new ListBuffer<JCExpression>()]
	: (EXTENDS id1=typeName           	{ $ids.append($id1.expr); }
           ( COMMA idn=typeName           	{ $ids.append($idn.expr); } )* 
	  )?
	;
interfaces returns [ListBuffer<JCExpression> ids = new ListBuffer<JCExpression>()]
	: (IMPLEMENTS id1=typeName           	{ $ids.append($id1.expr); }
           ( COMMA idn=typeName         	{ $ids.append($idn.expr); } )* 
	  )?
	;
classMembers returns [ListBuffer<JCTree> mems = new ListBuffer<JCTree>()]
@init { boolean initSeen = false; }
	:    ( id1=initDefinition		{ initSeen = true; $mems.append($id1.value); }			
	     | vd1=variableDeclaration 		{ $mems.append($vd1.value); }
	     | fd1=functionDefinition     	{ $mems.append($fd1.value); }
	     ) ?
	  (SEMI
	     ( {!initSeen}? id2=initDefinition	{ initSeen = true; $mems.append($id2.value); }			
	     | vd2=variableDeclaration 		{ $mems.append($vd2.value); }
	     | fd2=functionDefinition     	{ $mems.append($fd2.value); }
	     ) ?
	  ) *
	;
functionDefinition  returns [JFXOperationDefinition value]
	: mods=functionModifierFlags functionLabel name formalParameters  typeReference  
	    ( {($mods.flags & Flags.ABSTRACT) == 0}? be=blockExpression 
	    | {($mods.flags & Flags.ABSTRACT) != 0}? 
	    ) 
	    					{ $value = F.at($functionLabel.pos).OperationDefinition(
	    						F.at($functionLabel.pos).Modifiers($mods.flags),
	    						$name.value, $typeReference.type, 
	    						$formalParameters.params.toList(), $be.expr); }
	;
functionLabel    returns [int pos]
	: FUNCTION				{ $pos = pos($FUNCTION); }
	| OPERATION				{ $pos = pos($OPERATION); }
	;
initDefinition  returns [JFXInitDefinition value]
	: INIT block 				{ $value = F.at(pos($INIT)).InitDefinition($block.value); }
	;
functionModifierFlags returns [long flags]
	: accessModifier 		 	{ flags = $accessModifier.flag; }
		(	 functionModifier 	{ flags |= $functionModifier.flag; } )?
	| functionModifier 			{ flags = $functionModifier.flag; }
		(	 accessModifier 	{ flags |= $accessModifier.flag; } )?
	| /*nada*/				{ flags = 0; }
	;
varModifierFlags returns [long flags]
	: accessModifier 		 	{ flags = $accessModifier.flag; }
		(	 varModifier 		{ flags |= $varModifier.flag; } )?
	| varModifier	 			{ flags = $varModifier.flag; }
		(	 accessModifier 	{ flags |= $accessModifier.flag; } )?
	| /*nada*/				{ flags = 0; }
	;
classModifierFlags returns [long flags]
	: accessModifier 		 	{ flags = $accessModifier.flag; }
		(	 classModifier 		{ flags |= $classModifier.flag; } )?
	| classModifier 			{ flags = $classModifier.flag; }
		(	 accessModifier 	{ flags |= $accessModifier.flag; } )?
	| /*nada*/				{ flags = 0; }
	;
accessModifier returns [long flag]
	:  PUBLIC          			{ flag = Flags.PUBLIC; }
	|  PRIVATE         			{ flag = Flags.PRIVATE; }
	|  PROTECTED       			{ flag = Flags.PROTECTED; } 
	;
functionModifier returns [long flag]
	:  ABSTRACT        			{ flag = Flags.ABSTRACT; }
	|  STATIC        			{ flag = Flags.STATIC; } 
	;
varModifier returns [long flag]
	:  READONLY        			{ flag = Flags.FINAL; } 
	|  STATIC        			{ flag = Flags.STATIC; } 
	;
classModifier returns [long flag]
	:  ABSTRACT        			{ flag = Flags.ABSTRACT; }
	;
memberSelector returns [JFXMemberSelector value]
	: name1=name   DOT   name2=name		{ $value = F.at($name1.pos).MemberSelector($name1.value, $name2.value); } 
	;
formalParameters returns [ListBuffer<JFXVar> params = new ListBuffer<JFXVar>()]
	: LPAREN  ( fp0=formalParameter		{ params.append($fp0.var); }
	          ( COMMA   fpn=formalParameter	{ params.append($fpn.var); } )* )?  RPAREN 
	;
formalParameter returns [JFXVar var]
	: name typeReference			{ $var = F.at($name.pos).Param($name.value, $typeReference.type); } 
	;
block returns [JCBlock value]
@init { ListBuffer<JCStatement> stats = ListBuffer.<JCStatement>lb(); }
	: LBRACE
	     ( st1=statement			{ stats.append($st1.value); }			
	     | ex1=expression     		{ stats.append(F.Exec($ex1.expr)); }
	     ) ?
	  (SEMI
	     ( st2=statement 			{ stats.append($st2.value); }
	     | ex2=expression     		{ stats.append(F.Exec($ex2.expr)); }
	     ) ?
	  ) *
	  RBRACE				{ $value = F.at(pos($LBRACE)).Block(0L, stats.toList()); }
	;
functionExpression  returns [JFXOperationValue expr]
       : FUNCTION   formalParameters   typeReference blockExpression {
   $expr = F.at(pos($FUNCTION)).OperationValue($typeReference.type, 
                                               $formalParameters.params.toList(),
                                               $blockExpression.expr);
   };
operationExpression  returns [JFXOperationValue expr]
       : OPERATION   formalParameters   typeReference blockExpression {
   $expr = F.at(pos($OPERATION)).OperationValue($typeReference.type, 
                                                $formalParameters.params.toList(),
                                                $blockExpression.expr);
   }
   ;
blockExpression returns [JFXBlockExpression expr]
@init { ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>(); JCExpression val = null; }
	: LBRACE
	     ( st1=statement			{ stats.append($st1.value); }			
	     | ex1=expression     		{ val = $ex1.expr; }
	     ) ?
	  (SEMI
	     ( st2=statement 			{ if (val != null) { stats.append(F.Exec(val)); val = null; }
	     					  stats.append($st2.value); }
	     | ex2=expression     		{ if (val != null) { stats.append(F.Exec(val)); }
	     					  val = $ex2.expr; }
	     ) ?
	  ) *
	  RBRACE				{ $expr = F.at(pos($LBRACE)).BlockExpression(0L, stats.toList(), val); }
	;
statement returns [JCStatement value]
	: variableDeclaration			{ $value = $variableDeclaration.value; }
	| functionDefinition 			{ $value = $functionDefinition.value; }
	| insertStatement 			{ $value = $insertStatement.value; }
	| deleteStatement 			{ $value = $deleteStatement.value; }
        | WHILE LPAREN expression RPAREN block	{ $value = F.at(pos($WHILE)).WhileLoop($expression.expr, $block.value); }
	| BREAK    				{ $value = F.at(pos($BREAK)).Break(null); }
	| CONTINUE  	 	 		{ $value = F.at(pos($CONTINUE)).Continue(null); }
       	| THROW expression 	   		{ $value = F.at(pos($THROW)).Throw($expression.expr); } 
       	| returnStatement 			{ $value = $returnStatement.value; }
       	| tryStatement				{ $value = $tryStatement.value; } 
       	;
variableDeclaration   returns [JCStatement value]
	: varModifierFlags variableLabel  name  typeReference  eqBoundExpressionOpt onChanges
	    					{ $value = F.at($variableLabel.pos).Var($name.value, 
	    							$typeReference.type, 
	    							F.at($variableLabel.pos).Modifiers($varModifierFlags.flags),
	    							$eqBoundExpressionOpt.expr, 
	    							$eqBoundExpressionOpt.status, 
	    							$onChanges.listb.toList()); }
	;
eqBoundExpressionOpt   returns [JavafxBindStatus status, JCExpression expr]
	: EQ boundExpression	 		{ $expr = $boundExpression.expr; $status = $boundExpression.status; }
	| /*nada*/		 		{ $expr = null; $status = UNBOUND; }
	;
onChanges   returns [ListBuffer<JFXAbstractOnChange> listb = ListBuffer.<JFXAbstractOnChange>lb()]
	: ( onChangeClause			{ listb.append($onChangeClause.value); } )*
	;
onChangeClause   returns [JFXAbstractOnChange value]
	: ON REPLACE (LPAREN oldv=formalParameter RPAREN)? block
						{ $value = F.at(pos($ON)).OnReplace($oldv.var, $block.value); }
	| ON REPLACE LBRACKET index=formalParameter RBRACKET (LPAREN oldv=formalParameter RPAREN)? block
						{ $value = F.at(pos($ON)).OnReplaceElement($index.var, $oldv.var, $block.value); }
	| ON INSERT LBRACKET index=formalParameter RBRACKET (LPAREN newv=formalParameter RPAREN)? block
						{ $value = F.at(pos($ON)).OnInsertElement($index.var, $newv.var, $block.value); }
	| ON DELETE LBRACKET index=formalParameter RBRACKET (LPAREN oldv=formalParameter RPAREN)? block
						{ $value = F.at(pos($ON)).OnDeleteElement($index.var, $oldv.var, $block.value); }
	;
variableLabel    returns [int pos]
	: VAR					{ $pos = pos($VAR); }
	| LET					{ $pos = pos($LET); }
	| ATTRIBUTE				{ $pos = pos($ATTRIBUTE); }
	;
insertStatement   returns [JCStatement value]
	: INSERT e1=expression  INTO e2=expression 
						{ $value = F.at(pos($INSERT)).SequenceInsert($e2.expr, $e1.expr); } 
	;
deleteStatement   returns [JCStatement value]
	: DELETE  e1=expression  
	   ( FROM e2=expression 		{ $value = F.at(pos($DELETE)).SequenceDelete($e2.expr,$e1.expr); } 
	   | /* indexed and whole cases */	{ $value = F.at(pos($DELETE)).SequenceDelete($e1.expr); } 
	   )
	;
returnStatement   returns [JCStatement value]
@init { JCExpression expr = null; }
	: RETURN (expression 			{ expr = $expression.expr; } )?  
	   					{ $value = F.at(pos($RETURN)).Return(expr); } 
	;
tryStatement   returns [JCStatement value]
@init	{	ListBuffer<JCCatch> catchers = new ListBuffer<JCCatch>();
		JCBlock finalBlock = null;
	}
	: TRY   tb=block 			
	   ( FINALLY   fb1=block		{ finalBlock = $fb1.value; } 
	   |    (catchClause 			{ catchers.append($catchClause.value); } 
	   	)+   
	        (FINALLY  fb2=block		{ finalBlock = $fb2.value; } 
	        )?   
	   ) 					{ $value = F.at(pos($TRY)).Try($tb.value, catchers.toList(), finalBlock); }
	;
catchClause    returns [JCCatch value]
	: CATCH  LPAREN  formalParameter
	  RPAREN   block 			{ $value = F.at(pos($CATCH)).Catch($formalParameter.var, $block.value); } 
	;
boundExpression   returns [JavafxBindStatus status, JCExpression expr]
@init { boolean isLazy = false; }
	: ( BIND 				
	      (LAZY				{ isLazy = true; } )?
	      e1=expression			{ $expr = $e1.expr; }
	      (WITH INVERSE			{ $status = isLazy? JavafxBindStatus.LAZY_BIDIBIND :  JavafxBindStatus.BIDIBIND; }
	      |					{ $status = isLazy? JavafxBindStatus.LAZY_UNIDIBIND :  JavafxBindStatus.UNIDIBIND; }
	      )
	  )
	| e2=expression				{ $expr = $e2.expr; $status = UNBOUND; }
	;
expression returns [JCExpression expr] 
       	: blockExpression					{ $expr = $blockExpression.expr; }
       	| ifExpression   					{ $expr = $ifExpression.expr; }  
       	| forExpression   					{ $expr = $forExpression.expr; }  
       	| newExpression 					{ $expr = $newExpression.expr; }
	| assignmentExpression					{ $expr = $assignmentExpression.expr; }  
//     	| LPAREN  typeName  RPAREN   suffixedExpression     //FIXME: CAST
      	;
forExpression   returns [JCExpression expr] 
@init { ListBuffer<JFXForExpressionInClause> clauses = ListBuffer.lb(); }
	: FOREACH   LPAREN  
		in1=inClause					{ clauses.append($in1.value); }	       
		( COMMA in2=inClause				{ clauses.append($in2.value); } )*	       
	        RPAREN be=expression 				{ $expr = F.at(pos($FOREACH)).ForExpression(clauses.toList(), $be.expr); }
	;
inClause   returns [JFXForExpressionInClause value] 
@init { JFXVar var; }
	: formalParameter IN se=expression 
	          	  (WHERE  we=expression)?		{ $value = F.at(pos($IN)).InClause($formalParameter.var, $se.expr, $we.expr); }
	;
ifExpression  returns [JCExpression expr] 
	: IF LPAREN econd=expression  RPAREN 
		THEN?  ethen=expression  elseClause 		{ $expr = F.at(pos($IF)).Conditional($econd.expr, $ethen.expr, $elseClause.expr); }
	;
elseClause  returns [JCExpression expr] 
	: (ELSE)=>  ELSE  expression				{ $expr = $expression.expr; }
	| /*nada*/ 						{ $expr = null; }
	;
assignmentExpression  returns [JCExpression expr] 
	: e1=assignmentOpExpression assignmentClause[$e1.expr]	{ $expr = $assignmentClause.expr; }
	;
assignmentClause [JCExpression lhs] returns [JCExpression expr] 
	: (EQ)=>   EQ  expression				{ $expr = F.at(pos($EQ)).Assign($lhs, $expression.expr); } 
	|							{ $expr = $lhs; } 
	;
assignmentOpExpression  returns [JCExpression expr] 
	: e1=andExpression					{ $expr = $e1.expr; }
	   (   assignmentOperator   e2=expression		{ $expr = F.Assignop($assignmentOperator.optag, $expr, $e2.expr); }   ) ? 
	;
andExpression  returns [JCExpression expr] 
	: e1=orExpression					{ $expr = $e1.expr; }
	   (   AND   e2=orExpression				{ $expr = F.at(pos($AND)).Binary(JCTree.AND, $expr, $e2.expr); }   ) * ;
orExpression  returns [JCExpression expr] 
	: e1=typeExpression				{ $expr = $e1.expr; }
	   (   OR   e2=typeExpression			{ $expr = F.at(pos($OR)).Binary(JCTree.OR, $expr, $e2.expr); }    ) * ;
typeExpression  returns [JCExpression expr] 
	: e1=relationalExpression				{ $expr = $e1.expr; }
	   (   INSTANCEOF itn=typeName				{ $expr = F.at(pos($INSTANCEOF)).TypeTest($expr, $itn.expr); }
	   |   AS atn=typeName					{ $expr = F.at(pos($AS)).TypeCast($atn.expr, $expr); }   
	   ) ? 
	;
relationalExpression  returns [JCExpression expr] 
	: e1=additiveExpression					{ $expr = $e1.expr; }
	   (   LTGT   e=additiveExpression			{ $expr = F.at(pos($LTGT)).Binary(JCTree.NE, $expr, $e.expr); }
	   |   EQEQ   e=additiveExpression			{ $expr = F.at(pos($EQEQ)).Binary(JCTree.EQ, $expr, $e.expr); }
	   |   LTEQ   e=additiveExpression			{ $expr = F.at(pos($LTEQ)).Binary(JCTree.LE, $expr, $e.expr); }
	   |   GTEQ   e=additiveExpression			{ $expr = F.at(pos($GTEQ)).Binary(JCTree.GE, $expr, $e.expr); }
	   |   LT     e=additiveExpression			{ $expr = F.at(pos($LT))  .Binary(JCTree.LT, $expr, $e.expr); }
	   |   GT     e=additiveExpression			{ $expr = F.at(pos($GT))  .Binary(JCTree.GT, $expr, $e.expr); }
	   |   IN     e=additiveExpression			{ /* $expr = F.at(pos($IN  )).Binary(JavaFXTag.IN, $expr, $e2.expr); */ }
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
	| unaryOperator   e=unaryExpression			{ $expr = F.Unary($unaryOperator.optag, $e.expr); }
	;
suffixedExpression  returns [JCExpression expr] 
	: e1=postfixExpression					{ $expr = $e1.expr; }
	   (PLUSPLUS | SUBSUB) ?   				//TODO
	;
postfixExpression  returns [JCExpression expr] 
	: primaryExpression 					{ $expr = $primaryExpression.expr; }
	   ( DOT ( CLASS   					//TODO
	         | name   					{ $expr = F.at(pos($DOT)).Select($expr, $name.value); }
	         )   
	   | LPAREN expressionListOpt RPAREN   			{ $expr = F.at(pos($LPAREN)).Apply(null, $expr, $expressionListOpt.args.toList()); } 
	   | LBRACKET expression  RBRACKET			{ $expr = F.at(pos($LBRACKET)).SequenceIndexed($expr, $expression.expr); }
	   ) * 
	;
primaryExpression  returns [JCExpression expr] 
	: qualident						{ $expr = $qualident.expr; }
		( LBRACE  objectLiteral RBRACE 			{ $expr = F.at(pos($LBRACE)).PureObjectLiteral($expr, $objectLiteral.parts.toList()); } 
		)?
       	| THIS							{ $expr = F.at(pos($THIS)).Ident(names._this); }
       	| SUPER							{ $expr = F.at(pos($SUPER)).Ident(names._super); }
       	| stringExpression 					{ $expr = $stringExpression.expr; }
       	| bracketExpression 					{ $expr = $bracketExpression.expr; }
       	| literal 						{ $expr = $literal.expr; }
      	| functionExpression					{ $expr = $functionExpression.expr; }
       	| operationExpression					{ $expr = $operationExpression.expr; }
       	| LPAREN expression RPAREN				{ $expr = F.at(pos($LPAREN)).Parens($expression.expr); }
       	;
newExpression  returns [JCExpression expr] 
@init { ListBuffer<JCExpression> args = null; }
	: NEW  typeName  
		( (LPAREN)=>LPAREN expressionListOpt  RPAREN 	{ args = $expressionListOpt.args; } 
		)?
								{ $expr = F.at(pos($NEW)).Instanciate(null, null, $typeName.expr, 
												(args==null? new ListBuffer<JCExpression>() : args).toList(), null); }
		   //TODO: need anonymous subclasses
	;
objectLiteral  returns [ListBuffer<JCStatement> parts = new ListBuffer<JCStatement>()]
	: ( objectLiteralPart  					{ $parts.append($objectLiteralPart.value); } ) * 
	;
objectLiteralPart  returns [JFXStatement value]
	: name COLON  boundExpression (COMMA | SEMI)?	{ $value = F.at(pos($COLON)).ObjectLiteralPart($name.value, $boundExpression.expr, $boundExpression.status); }
       	| variableDeclaration
       	| functionDefinition 
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
	     	| COMMA e2=expression 		{ exps.append($e2.expr); }
	     	    (
//	     	      DOTDOT dds=expression	{  }
//	     	    | 			
	     	      (COMMA  en=expression	{ exps.append($en.expr); } )*
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
unaryOperator  returns [int optag]
	: POUND				{ $optag = 0; } //TODO
	| QUES   			{ $optag = 0; } //TODO
	| SUB   			{ $optag = JCTree.NEG; } 
	| NOT   			{ $optag = JCTree.NOT; } 
	| SIZEOF   			{ $optag = JavafxTag.SIZEOF; } //TODO
	| TYPEOF   			{ $optag = 0; } //TODO
	| REVERSE   			{ $optag = 0; } //TODO
	| PLUSPLUS   			{ $optag = 0; } //TODO
	| SUBSUB 			{ $optag = 0; } //TODO
	;
assignmentOperator  returns [int optag]
	: PLUSEQ   			{ $optag = JCTree.PLUS_ASG; } 
	| SUBEQ   			{ $optag = JCTree.MINUS_ASG; } 
	| STAREQ   			{ $optag = JCTree.MUL_ASG; } 
	| SLASHEQ   			{ $optag = JCTree.DIV_ASG; } 
	| PERCENTEQ   			{ $optag = JCTree.MOD_ASG; } 
	;
type returns [JFXType type]
	: typeName cardinality		{ $type = F.TypeClass($typeName.expr, $cardinality.ary); }
        | FUNCTION LPAREN tal=typeArgList?
          	   RPAREN ret=type	//TODO: it is unclear why the type syntax is different 
          	   	cardinality	//TODO: this introduces an ambiguity: return cardinality vs type cardinality
          	   			{ $type = F.at(pos($FUNCTION)).TypeFunctional($tal.ptypes.toList(), $ret.type, $cardinality.ary); }
        | STAR cardinality		{ $type = F.at(pos($STAR)).TypeAny($cardinality.ary); } 
        ;
typeArgList   returns [ListBuffer<JFXType> ptypes = ListBuffer.<JFXType>lb(); ]
        : pt0=type			{ ptypes.append($pt0.type); }
	          ( COMMA ptn=type	{ ptypes.append($ptn.type); } 
	          )* 
	;
typeReference returns [JFXType type]
        : COLON type			{ $type = $type.type; }
 	| /*nada*/			{ $type = F.TypeUnknown(); }
        ;
cardinality returns [int ary]
	: (LBRACKET)=>LBRACKET RBRACKET { ary = JFXType.CARDINALITY_ANY; }
	|                         	{ ary = JFXType.CARDINALITY_SINGLETON; } 
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
