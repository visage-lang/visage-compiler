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

/////////////////////////////////////////////////////////////////////////////////
// Version 4 of the JavaFX lexer
//
// @author Jim Idle
//
// Version 4 of the grammar reverts to a spearate lexer and parser grammar without a separate
// ANTLR based AST walker. This is because this is the easiest way (at the time of writing)
// to confine error recovery to the smallest possible set of side effects on the resulting
// JavafxTree. This is important for down stream tools such as code completion, which require
// as much of the AST as is possible to produce if they are to be effective.
// 
// The lexer is spearated from teh paresr because ANTLR cannot specify separate 
// superclasses for the lexer and the parser and v4 requires a different lexer
// superClass to the v3 lexer but must co-exits with the v3 lexer.
//
// Derived from prior versions by:
//
// @author Robert Field
// @author Zhiqun Chen
//
lexer grammar v4Lexer;

options { 

	// Rather than embed lexer oriented Java code in this grammar, just to override
	// methods in the ANTLR base recognizer and derviative classes, we
	// instruct ANTLR to generate a class which is dervied from our own
	// super class. The super class is where we embody any code that does
	// not require direct access to the terminals and methods generated 
	// to implement the lexer.
	//
	superClass 	= AbstractGeneratedLexerV4; 
}


// -----------------------------------------------------------------
// This section provides package, imports and other information
// to the lexer. It is inserted at the start of the generated lexer
// code
//
@lexer::header {

package com.sun.tools.javafx.antlr;

import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Convert;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javafx.util.MsgSym;

}

// ------------------------------------------------------------------
// Members required by the generated lexer class are included verbatim in the
// generated lexer code.
//
@lexer::members {

    // Constructor that creates a message log sink for the 
    // current context.
    // 
    public v4Lexer(Context context, CharStream input) {
    	this(input);
        this.log = Log.instance(context);
    }

	// Return the token type that we are using to indicate a 
	// manufactured ';'.
	// 
    protected int getSyntheticSemiType() {
        return SEMI;
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

 
//------------------------------------------------------------------
// LEXER RULES

// --------
// Keywords
//
ABSTRACT		: 'abstract';
AFTER			: 'after';
AND				: 'and';
AS				: 'as';
ASSERT			: 'assert';
AT				: 'at';
ATTRIBUTE		: 'attribute';
BEFORE			: 'before';
BIND			: 'bind';
BOUND			: 'bound';
BREAK			: 'break';
CATCH			: 'catch';
CLASS			: 'class';
CONTINUE		: 'continue';
DEF				: 'def';
DELETE			: 'delete';
ELSE			: 'else';
EXCLUSIVE		: 'exclusive';
EXTENDS			: 'extends';
FALSE			: 'false';
FINALLY			: 'finally';
FIRST			: 'first';
FOR				: 'for';
FROM			: 'from';
FUNCTION		: 'function';
IF				: 'if';
IMPORT			: 'import';
INDEXOF			: 'indexof';
IN				: 'in';
INIT			: 'init';
INSERT			: 'insert';
INSTANCEOF		: 'instanceof';
INTO			: 'into';
INVERSE			: 'inverse';
LAST			: 'last';
LAZY			: 'lazy';
MOD				: 'mod';
NEW				: 'new';
NON_WRITABLE	: 'non-writable';		// Deprecated - delete soon
NOT				: 'not';
NULL			: 'null';
ON				: 'on';
OR				: 'or';
OVERRIDE		: 'override';
PACKAGE			: 'package';
POSTINIT		: 'postinit';
PRIVATE			: 'private';
PROTECTED		: 'protected';
PUBLIC_INIT     : 'public-init';
PUBLIC			: 'public';
PUBLIC_READABLE	: 'public-readable';	// Deprecated - delete soon
PUBLIC_READ     : 'public-read';
READABLE		: 'readable';
REPLACE			: 'replace';
RETURN			: 'return';
REVERSE			: 'reverse';
SIZEOF			: 'sizeof';
STATIC			: 'static';
STEP			: 'step';
SUPER			: 'super';
THEN			: 'then';
THIS			: 'this';
THROW			: 'throw';
TRIGGER			: 'trigger';
TRUE			: 'true';
TRY				: 'try';
TWEEN			: 'tween';
TYPEOF			: 'typeof';
VAR				: 'var';
WHERE			: 'where';
WHILE			: 'while';
WITH			: 'with';

// -------------------------------
// Punctuation and syntactic sugar
//
LBRACKET	: '[';
LPAREN		: '(';
POUND		: '#';
PIPE		: '|';
PLUSPLUS	: '++';
DOTDOT		: '..';
RPAREN		: ')';
RBRACKET	: ']';
SEMI		: ';';
COMMA		: ',';
DOT			: '.';
EQEQ		: '==';
EQ			: '=';
GT			: '>';
LT			: '<';
LTGT		: '<>';
LTEQ		: '<=';
GTEQ		: '>=';
PLUS		: '+';
SUB			: '-';
STAR		: '*';
SLASH		: '/';
PERCENT		: '%';
PLUSEQ		: '+=';
SUBEQ		: '-=';
STAREQ		: '*=';
SLASHEQ		: '/=';
PERCENTEQ	: '%=';
NOTEQ		: '!=';
COLON		: ':';
QUES		: '?';
SUCHTHAT	: '=>';
SUBSUB		: '--';


// String literals being with either a single or double quote
// character (which must be matched). Additionally, string literals
// may span mulitple lines.
//
// TODO: Allow EOF to terminate a string so that we can
//       detect unterminated string literals and report them
//       as such, rather than trying to doctor this message in the
//       lexer superclass.
//
STRING_LITERAL  		
	: '"' DoubleQuoteBody '"'  	
	
			{ processString(); }
			
	| '\'' SingleQuoteBody '\''  	
	
			{ processString(); }
	;
	
// String Expression token implementation.
//
// String literals may contain formatting instructions
// which contain the sequence LBRACE PERCENT. This lexer
// rule is selected when, after looking ahead, we determine
// that the string contains this sequence. We then being
// to break out the literal strings in to multiple tokens
// so that the paresr can verify that they are correctly
// formulated.
//
// Note that we indicate the context (double or single quote)
// to the NextIsPercent fragment, which will invoke enterBrace()
// and begin tracking this level of brace string vs " or ' string.
//
QUOTE_LBRACE_STRING_LITERAL 	
	: '"' DoubleQuoteBody '{'   	
	
			{ processString(); }
	
			  NextIsPercent[DBL_QUOTE_CTX] 
	
	| '\'' SingleQuoteBody '{'   	
	
			{ processString(); }
	
			  NextIsPercent[SNG_QUOTE_CTX] 
	;
	
// The left brace character is significant 
// within quoted strings and as a block delimiter.
// If we find it vai this rule, then we enter a new brace context in the
// lexer, which, because we did not find this as a format
// introducer in QUOTE_LBRACE_STRING_LITERAL, means that
// it is not the start of a format specification.
//
LBRACE				
	: '{'				{ enterBrace(0, false); } 
	;
	
// When we currently scanning within a left brace context
// within a quoted string, then the next right brace terminates
// a format or expression embedded withing a quited string.
// the semantic predicate rightBraceLikeQuote() selects this
// lexer rule if we scan to a right brace and determine that
// we are currently expecting right brace to delimit an expression
// or format. This rule then matches the rest of the quoted string literal if it
// has no more embedded expressions.
//
RBRACE_QUOTE_STRING_LITERAL 	
	: { rightBraceLikeQuote(DBL_QUOTE_CTX) }?=>
		
		  '}' DoubleQuoteBody '"'	
				  
		  		{ 
		  			leaveBrace(); 
		         	leaveQuote(); 
		         	processString(); 
		       	}
	
	| { rightBraceLikeQuote(SNG_QUOTE_CTX) }?=>
		
		  '}' SingleQuoteBody '\''	
				  
		  		{ 
		  			leaveBrace(); 
		         	leaveQuote(); 
		         	processString(); 
		      	}
	;
	
// As in the rule RBRACE_QUOTE_STRING_LITERAL, this rule
// is selected when we are expecting a right brace to act as
// a delimiter for an embedded expression. However, here
// we find that the quoted string has further embedded
// format expressions and we therefore generate a different token
// to allow the parser to verify the syntax.
//
RBRACE_LBRACE_STRING_LITERAL 	
	: { rightBraceLikeQuote(DBL_QUOTE_CTX) }?=>
	
		  '}' DoubleQuoteBody '{'	
				  
		  		{ 
		  			leaveBrace(); 
					processString(); 
		    	}
				   
		   NextIsPercent[CUR_QUOTE_CTX]	
				   
	| { rightBraceLikeQuote(SNG_QUOTE_CTX) }?=>
	
				  '}' SingleQuoteBody '{'	
				  
				  		{ 
				  			leaveBrace(); 
				       		processString(); 
				       	}
				       	
				   NextIsPercent[CUR_QUOTE_CTX]	
	;
	
// Here, a right brace is returned as a simple token 
// type, because the semantic predicate will select this
// rule when it is determiend that we are not expecting
// right brace to terminate an embedded expression. This
// is the case when it is delimiting code blocks or block
// expressions.
//
RBRACE	
	:	{ !rightBraceLikeQuote(CUR_QUOTE_CTX) }?=>
		
			  '}'				{ leaveBrace(); }
	;

// Scans through the valid body of a double quoted
// string.
//				
fragment
DoubleQuoteBody  
	:	 (~('{' |'"'|'\\')|'\\' .)*  
	;

// Scans through the body of a single quoted string
//
fragment
SingleQuoteBody  
	:	 (~('{' |'\''|'\\')|'\\' .)*  
	;

// This rules is used as a syntactic predicate by quoted
// string rules to determine if the next character
// following a brace is a precentage sign, indicating that
// that this is a formatting sequence, or not, indicating
// that the left brace was introducing an embedded expression.
//
fragment
NextIsPercent[int quoteContext] 			
	:	((' '|'\r'|'\t'|'\u000C'|'\n')* '%')=>
	
			{ enterBrace(quoteContext, true); }
			
	|		{ enterBrace(quoteContext, false); }
	;
				
// If we have entered an embedded brace expression within
// a literal string, and determined that it is a formatting
// expression, then the semantic predicate function percentIsFormat
// will return 'true' and select this rule to introduce the
// formating string to the parser.
//
FORMAT_STRING_LITERAL		
	: 	{ percentIsFormat() }?=>
	
			'%' (~' ')* 			
			
				{ 
					processFormatString();
	 				resetPercentIsFormat(); 
	 			}
	;

// A translation key sequence is used to prefix string literals and 
// as the name suggests, translate it to something else according
// to locales.
//
TRANSLATION_KEY                 
	: '##' 
    	( 
        	'[' TranslationKeyBody ']'
        )?                            
        
        { 
        	processTranslationKey(); 
        }
	;

// The body of the translation key itself.
//
fragment
TranslationKeyBody              
	: (~('[' | ']' | '\\')|'\\' .)+
    ;
 
// Time literals are self evident in meaning and are currently
// recognized by the lexer. This may change as in some cases 
// trying to do too much in the lexer results in lexing errors
// that are difficult to recover from.
//
TIME_LITERAL 
	: (DECIMAL_LITERAL | Digits '.' (Digits)? (Exponent)? | '.' Digits (Exponent)?) ( 'ms' | 'm' | 's' | 'h' ) 
	;

// Decimal literals may not have leading zeros unless
// they are just the constant 0. They are integer only.
// In order to do more accurate error processing, these
// numeric literlas may merge into one rule that overrides
// the type.
//
DECIMAL_LITERAL 
	: ('0' | '1'..'9' '0'..'9'*)  
	
		{ 
			checkIntLiteralRange(getText(), getCharIndex(), 10); 
		}
	;

// Octal literals are preceeded by a leading zero and must be followed
// by one or more valid octal digits. 
//
OCTAL_LITERAL 
	: '0' ('0'..'7')+  
		
		{ 
			checkIntLiteralRange(getText(), getCharIndex(), 8); 
		}
	;

// Hex literals are preceded by 0X or 0x and must have one or
// more valid hex digits following them. The problem with specifying
// it like this is that a string such as 0x will cause a lexing error
// rather than a parse or semantic error, which is probably better
// and so this may change (see comments assocaited with DECIMAL_LITERAL)
//
HEX_LITERAL 
	: '0' ('x'|'X') HexDigit+    			
	
		{ 
			setText(getText().substring(2, getText().length()));
			checkIntLiteralRange(getText(), getCharIndex(), 16); 
		}
	;

// Validate hex digits
//
fragment
HexDigit 
	: ('0'..'9'|'a'..'f'|'A'..'F') 
	;

// Floating porint literals incorporate ranges as well as the
// the standard floating point number representations.
//
FLOATING_POINT_LITERAL
    : d=DECIMAL_LITERAL RangeDots
    	  	{
    	  		$d.setType(DECIMAL_LITERAL);
    	  		emit($d);
          		$RangeDots.setType(DOTDOT);
    	  		emit($RangeDots);
    	  	}
    | d=OCTAL_LITERAL RangeDots
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
Digits	
	:	('0'..'9')+ 
 	;
 	
fragment
Exponent 
	: 	('e'|'E') ('+'|'-')? Digits
 	;

// Identifiers are any sequence of vharacters considered
// to be alphanumeric in the Java specification. Identifiers
// that cannot match this pattern may be 'quoted' by surrounding
// them with '<<' and '>>' - this allows external references to
// methods, properties and so on , where the external language
// does not restrict the identifer names to such a pattern or does
// not regard JavaFX keywords as invalid identifiers.
//
IDENTIFIER 
	: Letter (Letter|JavaIDDigit)*
	| '<<' (~'>'| '>' ~'>')* '>'* '>>'			
	
			{ 
				setText(getText().substring(2, getText().length()-2)); 
			}
	;

// Validate the range of characters considered to be Alpha letter
//
fragment
Letter
    : '\u0024' 
    | '\u0041'..'\u005a' 
    | '\u005f' 
    | '\u0061'..'\u007a' 
    | '\u00c0'..'\u00d6' 
    | '\u00d8'..'\u00f6' 
    | '\u00f8'..'\u00ff' 
    | '\u0100'..'\u1fff' 
    | '\u3040'..'\u318f' 
    | '\u3300'..'\u337f' 
    | '\u3400'..'\u3d2d' 
    | '\u4e00'..'\u9fff' 
    | '\uf900'..'\ufaff'
    ;

// Validate the range of characters considered to be digits.
//
fragment
JavaIDDigit
    : '\u0030'..'\u0039' 
    | '\u0660'..'\u0669' 
    | '\u06f0'..'\u06f9' 
    | '\u0966'..'\u096f' 
    | '\u09e6'..'\u09ef' 
    | '\u0a66'..'\u0a6f' 
    | '\u0ae6'..'\u0aef' 
    | '\u0b66'..'\u0b6f' 
    | '\u0be7'..'\u0bef' 
    | '\u0c66'..'\u0c6f' 
    | '\u0ce6'..'\u0cef' 
    | '\u0d66'..'\u0d6f' 
    | '\u0e50'..'\u0e59' 
    | '\u0ed0'..'\u0ed9' 
    | '\u1040'..'\u1049'
	;

// Whitespace characters are essentially ignored
// by the parser and AST. They are preserved in the token stream
// by hiding the tokens on a token stream channel that the parser does not examine.
//
WS  :  (' '|'\r'|'\t'|'\u000C'|'\n') 

		{
			$channel=HIDDEN;
		}
    ;

// As with whitespace, JavaFX comments are not seen by the parser.
// However, certain constructs such as the script itself, will search
// the token stream for documentation comments of the form '/**' .* '*/'
// The are therefore preserved on the hidden channel.
//
COMMENT
    :   '/*' ( options {greedy=false;} : . )* '*/' 
    
    	{
    		$channel=HIDDEN;
    	}
    ;

fragment
DOC_COMMENT
	: '/**'	// Just for documentation, all we need is the token type
	;

LINE_COMMENT
    : 	'//' ~('\n'|'\r')* '\r'? ('\n'|EOF) 
    	
    	{
    		$channel=HIDDEN;
    	}
    ;

LAST_TOKEN
    :	'~~~~~~~~' {false}? '~~~~~~~~'
    ;
    
// This special token is always the last rule in the lexer grammar. It
// is basically a catch all for characters that are not covered by any
// other lexical construct and are therefore illegal. This rule allows
// us to create a sensible error message and AST node.
//
INVALIDC
	: .
	;
	
