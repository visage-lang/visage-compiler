grammar v1;

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
        public v1Parser(Context context, CharSequence content) {
           this(new CommonTokenStream(new v1Lexer(new ANTLRStringStream(content.toString()))));
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

module returns [JCCompilationUnit result]
       : packageDecl? moduleItems EOF 
            { $result = F.TopLevel(noJCAnnotations(), $packageDecl.value, $moduleItems.items.toList()); };
packageDecl returns [JCExpression value]
       : PACKAGE qualident SEMI        { $value = $qualident.expr; };
moduleItems returns [ListBuffer<JCTree> items = new ListBuffer<JCTree>()]
       	: (moduleItem                  { items.append($moduleItem.value); } )*  ;
moduleItem returns [JCTree value]
       : importDecl			{ $value = $importDecl.value; }
       | classDefinition 		{ $value = $classDefinition.value; }
       | attributeDefinition 		{ $value = $attributeDefinition.def; }
       | memberOperationDefinition 	{ $value = $memberOperationDefinition.def; }
       | memberFunctionDefinition 	{ $value = $memberFunctionDefinition.def; }
       | TRIGGER ON changeRule		{ $value = $changeRule.value; }
       | statementExcept 		{ $value = $statementExcept.value; } ;
importDecl returns [JCTree value]
@init { JCExpression pid = null; }
        : IMPORT  identifier		{ pid = $identifier.expr; }
                 ( DOT name		{ pid = F.at($name.pos).Select(pid, $name.value); } )* 
                 ( DOT STAR		{ pid = F.at(pos($STAR)).Select(pid, names.asterisk); } )? SEMI 
          { $value = F.at(pos($IMPORT)).Import(pid, false); } ;
classDefinition returns [JFXClassDeclaration value]
	: modifierFlags  CLASS name supers LBRACE classMembers RBRACE 
	  { $value = F.at(pos($CLASS)).ClassDeclaration($modifierFlags.mods, $name.value,
	                                $supers.names.toList(), $classMembers.mems.toList()); } ;
supers returns [ListBuffer<Name> names = new ListBuffer<Name>()]
	: (EXTENDS name1=name       { $names.append($name1.value); }
           ( COMMA namen=name       { $names.append($namen.value); } )* 
	)?;
classMembers returns [ListBuffer<JFXAbstractMember> mems = new ListBuffer<JFXAbstractMember>()]
	:( attributeDecl                { $mems.append($attributeDecl.decl); }
	|  functionDecl                 { $mems.append($functionDecl.decl); }
	|  operationDecl                { $mems.append($operationDecl.decl); }
	) *   ;
attributeDecl returns [JFXAbstractMember decl]
	: modifierFlags ATTRIBUTE name typeReference inverseClause  (orderBy | indexOn)? SEMI 
		{ $decl = F.at(pos($ATTRIBUTE)).RetroAttributeDeclaration($modifierFlags.mods, $name.value, $typeReference.type,
	                                    $inverseClause.inverse, null/*order/index*/); } ;
inverseClause returns [JFXMemberSelector inverse = null]
	: (INVERSE memberSelector { inverse = $memberSelector.value; } )? ;
functionDecl returns [JFXAbstractMember decl]
	: modifierFlags FUNCTION name formalParameters typeReference SEMI 
		{ $decl =  F.at($name.pos).RetroFunctionDeclaration($modifierFlags.mods, $name.value, $typeReference.type,
	                                            $formalParameters.params.toList()); } ;

operationDecl returns [JFXAbstractMember decl]
	: modifierFlags   OPERATION   name   formalParameters   typeReference    SEMI 
		{ $decl = F.at(pos($OPERATION)).RetroOperationDeclaration($modifierFlags.mods, $name.value, $typeReference.type,
	                                            $formalParameters.params.toList()); } ;
attributeDefinition  returns [JFXRetroAttributeDefinition def]
	: ATTRIBUTE   memberSelector   EQ bindOpt  expression   SEMI 
		{ $def = F.at(pos($ATTRIBUTE)).RetroAttributeDefinition($memberSelector.value, $expression.expr, $bindOpt.status); } ;
memberOperationDefinition  returns [JFXRetroOperationMemberDefinition def]
	: OPERATION   memberSelector   formalParameters   typeReference  block 
		{ $def = F.at(pos($OPERATION)).RetroOperationDefinition($memberSelector.value, $typeReference.type, 
		              $formalParameters.params.toList(), $block.value); } ;
memberFunctionDefinition  returns [JFXRetroFunctionMemberDefinition def]
	: FUNCTION   memberSelector   formalParameters   typeReference  block /*TODO functionBody */
		{ $def = F.at(pos($FUNCTION)).RetroFunctionDefinition($memberSelector.value, $typeReference.type, 
		              $formalParameters.params.toList(), $block.value); } ;
functionBody // TODO
	: EQ   expression   whereVarDecls ?   SEMI    
        | LBRACE   (   variableDefinition   |   localFunctionDefinition   |   localOperationDefinition   ) *   RETURN   expression   SEMI ?   RBRACE ;
whereVarDecls : WHERE   whereVarDecl   (   COMMA   whereVarDecl   ) * ;
whereVarDecl : localFunctionDefinition 
       | name   typeReference   EQ   expression ;
variableDefinition : VAR   name   typeReference  EQ   expression   SEMI ;
changeRule  returns [JFXAbstractTriggerOn value]
	: LPAREN   NEW   typeName  RPAREN  block
	        { $value = F.at(pos($LPAREN)).TriggerOnNew($typeName.expr, null, $block.value); }
	| LPAREN   memberSelector  EQ identifier   RPAREN  block
	     	{ $value = F.at(pos($LPAREN)).TriggerOnReplace($memberSelector.value, $identifier.expr, $block.value); }
	| memberSelector  EQ identifier block
	     	{ $value = F.at(pos($EQ)).TriggerOnReplace($memberSelector.value, $identifier.expr, $block.value); }
	| LPAREN   memberSelector   LBRACKET   id1=identifier   RBRACKET   EQ id2=identifier   RPAREN  block
	     	{ $value = F.at(pos($LPAREN)).TriggerOnReplaceElement($memberSelector.value, $id1.expr, $id2.expr, $block.value); }
	| LPAREN   INSERT   identifier   INTO   memberSelector   RPAREN block 	
	| LPAREN   DELETE   identifier   FROM   memberSelector   RPAREN block 	
	| LPAREN   DELETE  memberSelector   LBRACKET   identifier   RBRACKET   RPAREN block
	;
modifierFlags returns [JCModifiers mods]
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
memberSelector returns [JFXMemberSelector value]
	: name1=name   DOT   name2=name		{ $value = F.at($name1.pos).MemberSelector($name1.value, $name2.value); } ;
formalParameters returns [ListBuffer<JCTree> params = new ListBuffer<JCTree>()]
	: LPAREN   ( fp0=formalParameter		{ params.append($fp0.var); }
	           ( COMMA   fpn=formalParameter	{ params.append($fpn.var); } )* )?  RPAREN ;
formalParameter returns [JFXVar var]
	: name typeReference			{ $var = F.at($name.pos).Var($name.value, $typeReference.type); } ;
block returns [JCBlock value]
	: LBRACE   statements   RBRACE		{ $value = F.at(pos($LBRACE)).Block(0L, $statements.stats.toList()); }
	;
statements returns [ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>()]
	: (statement                            { stats.append($statement.value); } )* ;
statement returns [JCStatement value]
       : statementExcept 			{ $value = $statementExcept.value; }
       | localTriggerStatement			{ $value = $localTriggerStatement.value; } ;
statementExcept  returns [JCStatement value]
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
assertStatement  returns [JCStatement value = null]
	: ASSERT   expression   (   COLON   expression   ) ?   SEMI ;
localOperationDefinition   returns [JCStatement value]
	: OPERATION   name   formalParameters   typeReference  block 
		{ $value = F.at(pos($OPERATION)).RetroOperationLocalDefinition($name.value, $typeReference.type, 
									$formalParameters.params.toList(), $block.value); } ;
localFunctionDefinition   returns [JCStatement value]
	: FUNCTION ?   name   formalParameters   typeReference  block // TODO? functionBody 
		{ $value = F.at($name.pos).RetroFunctionLocalDefinition($name.value, $typeReference.type, 
									$formalParameters.params.toList(), $block.value); } ;
variableDeclaration   returns [JCStatement value]
	: VAR  name  typeReference  
	    ( EQ bindOpt  expression SEMI	{ $value = F.at(pos($VAR)).VarInit($name.value, $typeReference.type, 
	    							$expression.expr, $bindOpt.status); }
	    | SEMI				{ $value = F.at(pos($VAR)).VarStatement($name.value, $typeReference.type); } 
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
ifStatement   returns [JCStatement value]
@init { JCStatement elsepart = null; }
	: IF   LPAREN   expression   RPAREN   s1=block (ELSE  s2=block { elsepart = $s2.value; }) ? 
						{ $value = F.at(pos($IF)).If($expression.expr, $s1.value, elsepart); } ;
insertStatement   returns [JCStatement value = null]
	: INSERT   (   DISTINCT   expression   INTO   expression   |   expression   (   (   (   AS   (   FIRST   |   LAST   )   ) ?   INTO   expression   )   |   AFTER   expression   |   BEFORE   expression   )   )     SEMI ;
deleteStatement   returns [JCStatement value = null]
	: DELETE   expression   SEMI ;
throwStatement   returns [JCStatement value = null]
	: THROW   expression   SEMI ;
returnStatement   returns [JCStatement value]
@init { JCExpression expr = null; }
	: RETURN (expression { expr = $expression.expr; } )? SEMI 
						{ $value = F.at(pos($RETURN)).Return(expr); } 
	;
localTriggerStatement   returns [JCStatement value = null]
	: TRIGGER   ON    ( localTriggerCondition | LPAREN   localTriggerCondition   RPAREN)  block ;
localTriggerCondition   returns [JCStatement value = null]
	: name   (   LBRACKET   name   RBRACKET   ) ?   EQ   expression 
       | INSERT   name   INTO   ( name   EQ ) /*?*/   expression 
       | DELETE   name   FROM   ( name   EQ ) /*?*/   expression ;
forAlphaStatement   returns [JCStatement value = null]
	: FOR   LPAREN   alphaExpression   RPAREN   block ;
alphaExpression : UNITINTERVAL   IN   DUR   expression   (   FPS   expression   ) ?   (   WHILE   expression   ) ?   (   CONTINUE   IF   expression   ) ? ;
forJoinStatement   returns [JCStatement value = null]
	: FOR   LPAREN   joinClause   RPAREN   (   LPAREN   durClause   RPAREN   ) ?   block ;
joinClause : name   IN   expression   (   COMMA   name   IN   expression   ) *   (   WHERE   expression   ) ? ;
durClause : DUR   expression   (   LINEAR   |   EASEIN   |   EASEOUT   |   EASEBOTH   |   MOTION   expression   ) ?   (   FPS   expression ) ?   (   WHILE   expression   ) ?   (   CONTINUE   IF   expression   ) ? ;
tryStatement   returns [JCStatement value = null]
	: TRY   block   (   FINALLY   block   |     catchClause +   (   FINALLY   block   ) ?   ) ;
catchClause : CATCH   LPAREN   name   typeReference ?   (   IF   expression   ) ?   RPAREN   block ;
expression returns [JCExpression expr] 
	: foreach 
       	| functionExpression 
       	| operationExpression 
       	| alphaExpression 
       	| ifExpression    //TODO: disabled for now? causes ambiguity, will be merged with if-statement
       	| selectExpression 
       	| LPAREN   /*JFX:typeSpec*/ typeName  RPAREN   suffixedExpression   
       	| suffixedExpression		{ $expr = $suffixedExpression.expr; }  ;
foreach : FOREACH   LPAREN   name   IN   expression   (   COMMA   name   IN   expression   ) *   (   WHERE   expression   ) ?   RPAREN   expression ;
functionExpression : FUNCTION   formalParameters   typeReference ?   functionBody ;
operationExpression : OPERATION   formalParameters   typeReference ?   block ;
ifExpression : IF   expression   THEN   expression   ELSE   expression ;
selectExpression : SELECT   DISTINCT ?     expression   FROM   selectionVar   (   COMMA   selectionVar   ) *   (   WHERE   expression   ) ? ;
selectionVar : name   (   IN   expression   ) ? ;
suffixedExpression  returns [JCExpression expr] 
	: e1=assignmentExpression				{ $expr = $e1.expr; }
	   (indexOn | orderBy | durClause | PLUSPLUS | SUBSUB) ? ;  //TODO
assignmentExpression  returns [JCExpression expr] 
	: e1=assignmentOpExpression				{ $expr = $e1.expr; }
	   (   EQ   e2=assignmentOpExpression			{ $expr = F.at(pos($EQ)).Assign($expr, $e2.expr); }   ) ? ;
assignmentOpExpression  returns [JCExpression expr] 
	: e1=andExpression					{ $expr = $e1.expr; }
	   (   assignmentOperator   e2=andExpression		{ $expr = F.Assignop($assignmentOperator.optag,
	   													$expr, $e2.expr); }   ) ? ;
andExpression  returns [JCExpression expr] 
	: e1=orExpression					{ $expr = $e1.expr; }
	   (   AND   e2=orExpression				{ $expr = F.at(pos($AND)).Binary(JCTree.AND, $expr, $e2.expr); }   ) * ;
orExpression  returns [JCExpression expr] 
	: e1=instanceOfExpression				{ $expr = $e1.expr; }
	   (   OR   e2=instanceOfExpression			{ $expr = F.at(pos($OR)).Binary(JCTree.OR, $expr, $e2.expr); }    ) * ;
instanceOfExpression  returns [JCExpression expr] 
	: e1=relationalExpression				{ $expr = $e1.expr; }
	   (   INSTANCEOF identifier				{ $expr = F.at(pos($INSTANCEOF)).Binary(JCTree.TYPETEST, $expr, 
	   													 $identifier.expr); }   ) ? ;
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
	: postfixExpression					{ $expr = $postfixExpression.expr; }
	| unaryOperator   postfixExpression			{ $expr = F.Unary($unaryOperator.optag, $postfixExpression.expr); }
	;
postfixExpression  returns [JCExpression expr] 
	: primaryExpression 					{ $expr = $primaryExpression.expr; }
	   ( DOT ( CLASS   					//TODO
	         | name1=name   				{ $expr = F.at(pos($DOT)).Select($expr, $name1.value); }
	            ( LPAREN expressionListOpt RPAREN   	{ $expr = F.at(pos($LPAREN)).Apply(null, $expr, $expressionListOpt.args.toList()); } ) *
	         )   
	   | LBRACKET (name BAR)? expression  RBRACKET		//TODO: selectionClause   
	   ) * ;
primaryExpression  returns [JCExpression expr] 
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
newExpression  returns [JCExpression expr] 
@init { ListBuffer<JCExpression> args = null; }
	: NEW  typeName  
		( LPAREN   expressionListOpt   RPAREN 		{ args = $expressionListOpt.args; } )?
								{ $expr = F.at(pos($NEW)).NewClass(null, null, $typeName.expr, 
												(args==null? new ListBuffer<JCExpression>() : args).toList(), null); }
		   //TODO: need objectLiteral 
	;
objectLiteral  returns [ListBuffer<JFXStatement> parts = new ListBuffer<JFXStatement>()]
	: ( objectLiteralPart  					{ $parts.append($objectLiteralPart.value); } ) * ;
objectLiteralPart  returns [JFXStatement value]
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
stringExpression  returns [JCExpression expr] 
@init { ListBuffer<JCExpression> strexp = new ListBuffer<JCExpression>(); }
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
formatOrNull  returns [JCExpression expr] 
	: fs=FORMAT_STRING_LITERAL		{ $expr = F.at(pos($fs)).Literal(TypeTags.CLASS, $fs.text); }
	| /* no formar */			{ $expr = null; }
	;
expressionListOpt  returns [ListBuffer<JCExpression> args = new ListBuffer<JCExpression>()] 
	: ( e1=expression		{ $args.append($e1.expr); }
	    (COMMA   e=expression	{ $args.append($e.expr); }  )* )? ;
orderBy returns [JCExpression expr]
	: ORDER   BY   expression	{ expr = $expression.expr; };
indexOn returns [JCExpression expr = null] //TODO
	: INDEX   ON   name   (   COMMA   name   ) * ;
multiplyOperator : STAR   |   SLASH   |   PERCENT ;
unaryOperator  returns [int optag]
	: POUND				{ $optag = 0; } //TODO
	| QUES   			{ $optag = 0; } //TODO
	| SUB   			{ $optag = JCTree.NEG; } 
	| NOT   			{ $optag = JCTree.NOT; } 
	| SIZEOF   			{ $optag = 0; } //TODO
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
typeReference returns [JFXType type]
	: ( COLON  (
                     (options { backtrack = true; } : (FUNCTION | OPERATION)? formalParameters typeReference ccf=cardinalityConstraint) 
                       					{ $type = F.TypeFunctional($formalParameters.params.toList(), 
                                                                                       $typeReference.type, $ccf.ary); }
                   | name ccn=cardinalityConstraint		{ $type = F.TypeClass($name.value, $ccn.ary); }
                   | STAR ccs=cardinalityConstraint		{ $type = F.at(pos($STAR)).TypeAny($ccs.ary); } ) )? 
        ;
cardinalityConstraint returns [int ary]
	:  LBRACKET   RBRACKET    	{ ary = JFXType.CARDINALITY_ANY; }
	|  QUES                   	{ ary = JFXType.CARDINALITY_OPTIONAL; }
	|  PLUS                   	{ ary = JFXType.CARDINALITY_ANY; }
	|  STAR                   	{ ary = JFXType.CARDINALITY_ANY; }
	|                         	{ ary = JFXType.CARDINALITY_OPTIONAL; } 
	;
literal  returns [JCExpression expr]
	: t=STRING_LITERAL		{ $expr = F.at(pos($t)).Literal(TypeTags.CLASS, $t.text); }
	| t=INTEGER_LITERAL		{ $expr = F.at(pos($t)).Literal(TypeTags.INT, Convert.string2int($t.text, 10)); }
	| t=FLOATING_POINT_LITERAL 	{ $expr = F.at(pos($t)).Literal(TypeTags.DOUBLE, Double.valueOf($t.text)); }
	| t=TRUE   			{ $expr = F.at(pos($t)).Literal(TypeTags.BOOLEAN, 1); }
	| t=FALSE   			{ $expr = F.at(pos($t)).Literal(TypeTags.BOOLEAN, 0); }
	| t=NULL 			{ $expr = F.at(pos($t)).Literal(TypeTags.BOT, null); } 
	;
typeName  returns [JCExpression expr]
       : qualident            		{ $expr = $qualident.expr; } 
       ;
qualident returns [JCExpression expr]
       : n1=name            		{ $expr = F.at($n1.pos).Identifier($n1.value); } 
         ( DOT nn=name     		{ $expr = F.at($nn.pos).Select($expr, $nn.value); } 
         ) *  ;
identifier  returns [JCIdent expr]
	: name              		{ $expr = F.at($name.pos).Ident($name.value); } 
	;
name returns [Name value, int pos]
	: tokid=( QUOTED_IDENTIFIER | IDENTIFIER ) { $value = Name.fromString(names, $tokid.text); $pos = pos($tokid); } 
	;
