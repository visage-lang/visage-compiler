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
// Version 4 of the JavaDFX lexer and parser parser grammar.
//
// @author Jim Idle
//
// Version 4 of the grammar reverts to a lexer parser combination without a separate
// ANTLR based AST walker. This is because this is the easiest way (at the time of writing)
// to confine error recovery to the smallest possible set of side effects on the resulting
// JavafxTree. This is important for down stream tools such as code completion, which require
// as much of the AST as is possible to produce if they are to be effective.
//
// Derived from prior versions by:
//
// @author Robert Field
// @author Zhiqun Chen
//
grammar v4;

options { 

	// Rather than embed parser oriented Java code in this grammar, just to override
	// methods in the ANTLR base recognizer and derviative classes, we
	// instruct ANTLR to generate a class which is dervied from our own
	// super class. The super class is where we embody any code that does
	// not require direct access to the terminals and methods generated 
	// to implement the parser. Hence for instance this is where the 
	// JavafxTreeMaker lives.
	//
	superClass 	= AbstractGeneratedParserV4; 
}

// -----------------------------------------------------------------
// The simple token set that the lexer can produce, as well as any
// imaginary tokens that are used as nodes for the AST
//
tokens {
   
   // These tokens can start a statement/definition -- can insert semi-colons before -- alpha order
   //
  SEMI_INSERT_START;
   
   	ABSTRACT		= 'abstract';
   	ASSERT			= 'assert';
   	AT				= 'at';
   	ATTRIBUTE		= 'attribute';
   	BIND			= 'bind';
   	BOUND			= 'bound';
   	BREAK			= 'break';
   	CLASS			= 'class';
   	CONTINUE		= 'continue';
   	DEF				= 'def';
   	DELETE			= 'delete';
   	FALSE			= 'false';
   	FOR				= 'for';
   	FUNCTION		= 'function';
   	IF				= 'if';
   	IMPORT			= 'import';
   	INDEXOF			= 'indexof';
   	INIT			= 'init';
   	INSERT			= 'insert';
   	NEW				= 'new';
   	NON_WRITABLE	= 'non-writable';
   	NOT				= 'not';
   	NULL			= 'null';
   	OVERRIDE		= 'override';
   	PACKAGE			= 'package';
   	POSTINIT		= 'postinit';
   	PRIVATE			= 'private';
   	PROTECTED		= 'protected';
   	PUBLIC			= 'public';
    PUBLIC_INIT     = 'public-init';
    PUBLIC_READ     = 'public-read';
   	PUBLIC_READABLE	= 'public-readable';
   	READABLE		= 'readable';
   	RETURN			= 'return';
   	REVERSE			= 'reverse';
   	SUPER			= 'super';
   	SIZEOF			= 'sizeof';
   	STATIC			= 'static';
   	THIS			= 'this';
   	THROW			= 'throw';
   	TRY				= 'try';
   	TRUE			= 'true';
   	TYPEOF			= 'typeof';
   	VAR				= 'var';
   	WHILE			= 'while';
   
   	POUND			= '#';
   	LPAREN			= '(';
   	LBRACKET		= '[';
   	PLUSPLUS		= '++';
   	SUBSUB			= '--';
	PIPE			= '|';
   
  SEMI_INSERT_END;
	
   	// Cannot start a statement -- alpha order
   	//
   	AFTER		= 'after';
   	AND			= 'and';
   	AS			= 'as';
   	BEFORE		= 'before';
   	CATCH		= 'catch';
   	ELSE		= 'else';
   	EXCLUSIVE	= 'exclusive';
   	EXTENDS		= 'extends';
   	FINALLY		= 'finally';
   	FIRST		= 'first';
   	FROM		= 'from';
   	IN			= 'in';
   	INSTANCEOF	= 'instanceof';
   	INTO		= 'into';
   	INVERSE		= 'inverse';
   	LAST		= 'last';
   	LAZY		= 'lazy';
   	MOD			= 'mod';
   	ON			= 'on';
   	OR			= 'or';
   	REPLACE		= 'replace';
   	STEP		= 'step';
   	THEN		= 'then';
   	TRIGGER		= 'trigger';
   	WITH		= 'with';
   	WHERE		= 'where';
   
   	DOTDOT		= '..';
   	RPAREN		= ')';
   	RBRACKET	= ']';
   	SEMI		= ';';
   	COMMA		= ',';
   	DOT			= '.';
   	EQEQ		= '==';
  	EQ			= '=';
  	GT			= '>';
  	LT			= '<';
   	LTGT		= '<>';
   	LTEQ		= '<=';
   	GTEQ		= '>=';
   	PLUS		= '+';
   	SUB			= '-';
   	STAR		= '*';
   	SLASH		= '/';
   	PERCENT		= '%';
   	PLUSEQ		= '+=';
   	SUBEQ		= '-=';
   	STAREQ		= '*=';
   	SLASHEQ		= '/=';
   	PERCENTEQ	= '%=';
   	NOTEQ		= '!=';
   	COLON		= ':';
   	QUES		= '?';
   	TWEEN		= 'tween';
   	SUCHTHAT	= '=>';

   	// These are imaginary tokens, used as nodes in the AST
   	//
	CLASS_MEMBERS;
	DOC_COMMENT;
	EMPTY_FORMAT_STRING;
	EXPR_LIST;
	FUNC_APPLY;
	FUNC_EXPR;
	KEY_FRAME_PART;
	MISSING_NAME;
	MODIFIER;
	NEGATIVE;
	OBJECT_LIT;
	OBJECT_LIT_PART;
	ON_REPLACE;
	ON_REPLACE_SLICE;
	PARAM;
	POSTDECR;
	POSTINCR;
	SCRIPT;
	SCRIPT_ITEMS;
	SEQ_EMPTY;
	SEQ_EXPLICIT;
	SEQ_INDEX;
	SEQ_SLICE;
	SEQ_SLICE_EXCLUSIVE;
	SLICE_CLAUSE;
	TYPE_ANY;
	TYPE_ARG;
	TYPED_ARG_LIST;
	TYPE_FUNCTION;
	TYPE_NAMED;
	TYPE_UNKNOWN;

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

// -----------------------------------------------------------------
// This section provides package and other information
// to the parser. It is inserted at the start of the generated parser
// code
//
@parser::header {

// Package specification for the generated parser class
//
package com.sun.tools.javafx.antlr;

// Parser specific inports.
//

import java.util.HashMap;
import java.util.Map;
import java.io.OutputStreamWriter;

import com.sun.tools.javac.tree.*;
import com.sun.tools.javafx.tree.*;
import com.sun.javafx.api.tree.*;

import com.sun.tools.javac.util.*;
import com.sun.tools.javafx.util.MsgSym;

import com.sun.tools.javac.code.*;
import com.sun.tools.javafx.code.JavafxFlags;
import static com.sun.tools.javac.util.ListBuffer.lb;
import com.sun.javafx.api.JavafxBindStatus;

import static com.sun.javafx.api.JavafxBindStatus.*;

}

// ------------------------------------------------------------------
// Members required the generated lexer class are included verbatim in the
// generated lexer code.
//
@lexer::members {

    // The language allows certain linqustic constructs
    // to be specified without conventional delimiters used in
    // many other languages, such as ';' to end various constructs.
    // However, while this can be somewhat convenient for the
    // programmer of a script type language, it often results in
    // much ambiguity when analyzing a grammar in LL(*), such
    // as is the case in ANTLR. Rather than construct difficult
    // to read grammar rules, it is easier to identify those
    // lexical tokens that should be preceded by a SEMI token
    // (which is optional to the programmer) and insert the token
    // automatically.
    //
    // Hence we build a table of these tokens here, to referred
    // later in the emit() method (we override the default
    // ANTLR implementation in the Lexer super class).
    //
    static final byte NO_INSERT_SEMI 	= 0; // default
    static final byte INSERT_SEMI 		= 1; 
    static final byte IGNORE_FOR_SEMI 	= 2; 
    static final byte[] semiKind 		= new byte[LAST_TOKEN];
    
    {
		for (int i = SEMI_INSERT_START; i < SEMI_INSERT_END; ++i) {
			semiKind[i] = INSERT_SEMI;
      	}
      
		semiKind[RBRACE] 						= INSERT_SEMI;
		semiKind[STRING_LITERAL] 				= INSERT_SEMI;
		semiKind[QUOTE_LBRACE_STRING_LITERAL] 	= INSERT_SEMI;
		semiKind[DECIMAL_LITERAL] 				= INSERT_SEMI;
		semiKind[OCTAL_LITERAL] 				= INSERT_SEMI;
		semiKind[HEX_LITERAL] 					= INSERT_SEMI;
		semiKind[TIME_LITERAL] 					= INSERT_SEMI;
		semiKind[FLOATING_POINT_LITERAL] 		= INSERT_SEMI;
		semiKind[IDENTIFIER] 					= INSERT_SEMI;
      
		semiKind[WS] 							= IGNORE_FOR_SEMI;
		semiKind[COMMENT] 						= IGNORE_FOR_SEMI;
		semiKind[LINE_COMMENT] 					= IGNORE_FOR_SEMI;
    }
     
    
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

	// Indicate whether the supplied token type is of a type 
	// which we should track in order to determine whether
	// a SEMI token should be manufactured. If it is ignored
	// then we wil not record it as the previous token
	// in the emit() token generation method.
	//
    protected boolean verifyCurrentType(int ttype) {
        return ttype != EOF && semiKind[ttype] != IGNORE_FOR_SEMI;
    }

	// Indicate whether the intersection of the current token type and 
	// 
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


	/***
	 * Given a specific starting token, locate the first non-whitespace token
	 * that preceeds it, returning it if it is a comment.
	 *
	 * A number of syntactical constructs can be preceded by a documentatin COMMENT which 
	 * is assocaitaed with the construct and should be placed in the AST. Such comments
	 * must begin with the introduceer '/**'.
	 * This method scans backwards from the supplied token until it finds a token that is 
	 * not considered to be WHITESPACE. If the token is a qualifying COMMENT then it is
	 * deemed to belong to the construct that asked to locate the comment and is
	 * returned to the caller.
	 *
	 * @param start The token from whence to search backwards in the token stream.
	 * @return null if there is no associated comment, the token that contains the
	 *         comment, if there is.
	 */
    CommonToken getDocComment(Token start) {
    
    	// Locate the positoin of this token in the input stream
    	//
		int index = start.getTokenIndex() - 1;
		
		// Loop backwards through the token stream until
		// we find a token that is not considered to be whitespace
		// or we reach the start of the token stream.
		//
		while (index >= 0) { 
			Token tok = input.get(index);
			if (tok.getType() == WS || 
				tok.getType() == SEMI && tok.getText().equals("beginning of new statement")) {
              --index;
            } else {
              break;
			}
		}

		// Assuming that we have found a valid token (not reached the
		// the token stream start, check to see if that token is a COMMENT
		// and return null if it is not.
		//
		if (index < 0 || input.get(index).getType() != COMMENT) {
           return null;
		}
		
		// We have a valid token, see if it is a documentation
		// comment, rather than just a normal comment.
		//
		CommonToken token = (CommonToken)(input.get(index));
       	if (token.getText().startsWith("/**")) {
       	
       		// It was a documentation comment so change its type
       		// to reflect this, then return it.
       		//
       		// TODO: JI - Move this type changing into the lexer 
       		//
			token.setType(DOC_COMMENT);
			return token;
		}
		
		// The token was either not a comment or was not a documentation
		// comment.
		//
		return null;
    }
}
 
//------------------------------------------------------------------
// LEXER RULES

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
	

// ------------------------------------------------------------------    	
// ------------------------------------------------------------------
// PARSER RULES
//
// The parser consumes the token stream created by calling the lexer until
// we see EOF. When the parser starts, the entire token stream is created.
// We cannot do syntax directed parsing as it means you cannot use LL(*)
// algorithms for grammar analysis and code generation.
//
// The parsers job is to produce the JavaFX speciailized AST, which
// is the basis for all the rest of the tool chain, including symbol table and code 
// generation as well as code completion for editors and so on.
// ------------------------------------------------------------------

/**
 * The usual entry point for the JavaFX parser, this will parse a complete
 * script body and manufacture the JavaFX AST.
 *
 * A script, like many other syntactical elements, can have an associated
 * comment. When the parse is complete, we scan the tokens that are normally
 * hidden from the parser looking for comments and associate them with
 * AST node for the script.
 */
script

	returns	[JFXScript result]
	
@init
{
	// Search for the document comment token. At this point LT(1)
	// returns the first on channel token, so we can scan back from
	// there to see if there was a document comment.
	//
	CommonToken  docComment = getDocComment(input.LT(1));
}

	:  pd=packageDecl si=scriptItems 
	
		{
			// Construct the JavFX AST
			//
			$result = F.Script($packageDecl.value, $si.items.toList());
            setDocComment($result, docComment);	// Add any detected documentation comment
		}

		EOF 	// Forces parser to consume entire token stream or error out

		{		
			// Set tree span and endpoint map (if required).
        	//
        	$result.pos = $result.pid != null ? $result.pid.pos : $result.defs.head.pos;
        	endPos($result); 
        }
    ;
    

// ----------------------    
// Package specification.
// The package declaration is optional. It qualifes the namespace/location
// of all subsequent delcarations in the script.
//
packageDecl

	returns [JFXExpression value] 	// Package declaration builds a JFXExpression tree

    : PACKAGE qualname SEMI
    
    		{ $value = $qualname.value; }
    		
    | // No package specified
    
    		{ $value = null; }
	;
	
// ----------------
// Script elements.
// Zero or more script elements belong to a script. Script elements
// are allowed to be completely empty, or effectively empty by
// existing as a SEMI (semi colon only). This structure allows
// class definitions and function definitions to appear to be
// only optionally terminated with a SEMI. The language spec allows
// ONLY class definitions and function definitions to be optionally
// terminated in this way, other constructs MUST be terminated
// with a SEMI.
//
scriptItems

	
	returns [ListBuffer<JFXTree> items = new ListBuffer<JFXTree>()] // This rule builds a list of JFXTree, which is used 
																	// by the caller to build the actual AST.
																	//
	:
		(
			  // Certain script members may be prefixed with modifiers
			  // such as 'public'. We allow the parser to first consume 
			  // all modifier keywords, regardless of whether this is a 
			  // valid modifier for the upcoming declaration. Whether it is
			  // valid or not is a matter for semantic checks to decide.
			  //
			  (modifiers)=>modifiers
				(
					  classDefinition		[$modifiers.mods]
					| functionDefinition    [$modifiers.mods]
					| variableDeclaration 	[$modifiers.mods]
						SEMI
				)
				
			| importDecl SEMI
			
			| statement
			
			| SEMI
		)*
	;

// ----------
// Modifiers.
// Collects the modifier flags for all known modifiers, regardless
// of their validity with the declaration they will be associated with.
// Attributing will verify the smeantics of the modifiers.
//
modifiers

	returns [JFXModifiers mods]	// Constructs and returns a specialized modifer node

@init {

	// The flags we build up for the AST
	//
	long	flags 	= 0;
	
	// The start character position for this AST
	//
	int   	cPos		= pos();
}

	: 	(	
			mf=modifierFlag
			
			{
				// Or in the newly discovered modifier
				//
				flags	|= $mf.flag;
			}
	
		)*
		
		{
			// Build the modifier flags (just as empty if we did not pick any up)
			//
			$mods = F.at(cPos).Modifiers(flags);
			
			// Tree span
			//
			endPos($mods);
		}
	;

// ---------------
// Modifier flags.
// All the possible modifier keywords that can be applied to 
// constructs such as var, class and so on,
//
modifierFlag

	returns [long flag]
	
	: ABSTRACT			{ $flag = Flags.ABSTRACT;				}
	| BOUND				{ $flag = JavafxFlags.BOUND;			}
	| OVERRIDE			{ $flag = JavafxFlags.OVERRIDE;			}
	| PACKAGE			{ $flag = Flags.PUBLIC /*TODO:JavafxFlags.PACKAGE */;	}
	| PROTECTED			{ $flag = Flags.PROTECTED;				}
	| PUBLIC			{ $flag = Flags.PUBLIC;					}
	| PUBLIC_READ   	{ $flag = JavafxFlags.PUBLIC_READ;	}
	| PUBLIC_INIT		{ $flag = JavafxFlags.PUBLIC_INIT;		}
        
	
	//TODO: deprecated -- remove these at some point
	//                    For now, warn about their deprecation
	//
	| PUBLIC_READABLE	{ $flag = JavafxFlags.PUBLIC_READ;      }
	| NON_WRITABLE		{ $flag = JavafxFlags.PUBLIC_INIT;		}
	| PRIVATE			{ $flag = Flags.PRIVATE;    			log.warning(pos($PRIVATE), "javafx.not.supported.private"); }
	| READABLE			{ $flag = JavafxFlags.PUBLIC_READ;      log.error(pos($READABLE), "javafx.not.supported.readable"); }
	| STATIC			{ $flag = Flags.STATIC;      			}
	;

// -----------------	
// Import statement.
// Include definitions from an external source
//
importDecl

	returns [JFXTree value] // The import declaration is built as a generic JFXTree

 	: IMPORT importId
 	
 		{
 			// AST construction
 			$value = F.at(pos($IMPORT)).Import($importId.pid, false);
 			
 			// AST span
 			//
			endPos($value);
 		}
	;
	
// ------------
// Import spec.
// Parses the (possibly) qualifed name space that the script must import,
//
importId

	returns [JFXExpression pid]	// Qualified names are built as expression trees

 	: i1=identifier
 		{
 			$pid = $i1.value;
 		}
		( 
			d1=DOT n2=name
		
				{
					$pid = F.at($n2.pos).Select($pid, $n2.value);
                    endPos($pid);
				}
		)* 
        ( 
        	DOT STAR
        	
        		{
					$pid = F.at($n2.pos).Select($pid, names.asterisk);
                    endPos($pid);
				}
        )?  
	;
	
// Class definition.
// Parses a complete class definition and builds up the JFX AST
// that represents this.
//
// param mods The previously built modifier flags
//
classDefinition [ JFXModifiers mods ]

	returns [JFXClassDeclaration value]	// The class definition has its own JFXTree type
	
@init { 

	// Search for the document comment token. At this point LT(1)
	// returns the first on channel token, so we can scan back from
	// there to see if there was a document comment.
	//
	CommonToken  docComment = getDocComment(input.LT(1));

}

	: CLASS name supers 
		LBRACE 
			classMembers 
		RBRACE
		
		{ 
			$value = F.at(pos($CLASS)).ClassDeclaration
			
				(
	  						  
					$mods,	
					$name.value,
					$supers.ids.toList(),
					$classMembers.mems.toList()
				);
				setDocComment($value, docComment);	// Add any detected documentation comment
				endPos($value); 
		}
	;
	
// -----------------
// Super class spec.
// Parses a list of super classes for a class definition and builds the
// associated JFX AST.
//
supers 

	returns [ListBuffer<JFXExpression> ids = new ListBuffer<JFXExpression>()]	// The return is a list of JFX expressions representing one
																				// or more super class type name.

	: EXTENDS t1=typeName
			{
				$ids.append($t1.value);	// First type name in list
			}
           ( 
           	COMMA t2=typeName 
           	
           		{ 
           			$ids.append($t2.value); 
           		}
           )*
           
	| // Upsilon - this class inherits no other types so the list will be empty
	;
		  					
// --------------
// Class members.
// Parses all the possible elements of a class definition and produces the
// Java FX AST nodes that represent them
//
classMembers 

	returns [ListBuffer<JFXTree> mems = new ListBuffer<JFXTree>()]		// Returns a list of the class members, ready for the caller to produce the
																		// class defintion AST.
	: (
		classMember 
	
			{ 
				$mems.append($classMember.member); 
			}
		| SEMI
	  )*
	;
	
// --------------
// Class members.
// Parses all constructs that can be a member of a class and returns
// the JAva FX AST that represents it.
//
classMember

 	returns [JFXTree member]		// A class member has a specialized JFX tree node, which is what
									// we return from this rule.

 
	: initDefinition				{ $member = $initDefinition.value; }
	| postInitDefinition			{ $member = $postInitDefinition.value; }
	
	| (modifiers)=>m=modifiers
		(
			  variableDeclaration		[$m.mods] 		{ $member = $variableDeclaration.value; }
			| functionDefinition		[$m.mods]		{ $member = $functionDefinition.value; }
		)
	| overrideDeclaration 
	;

// ----------
// Functions.
// While funcitnos can be declared at any level, their syntax is the same.
// As always, the semantic pass of the JFX tree must verify that the
// supplied modifers are valid in this context.
//
functionDefinition [ JFXModifiers mods ]

	returns [JFXFunctionDefinition value]		// A function defintion has a specialized node in the JavaFX AST

@init { 

	// Search for the document comment token. At this point LT(1)
	// returns the first on channel token, so we can scan back from
	// there to see if there was a document comment.
	//
	CommonToken  docComment = getDocComment(input.LT(1));

}
	: FUNCTION name formalParameters typeReference ((LBRACE)=>block)?
	
		{
			// Create the function defintion AST
			//
			$value = F.at(pos($FUNCTION)).FunctionDefinition
							(
								$mods,
								$name.value, 
								$typeReference.rtype,
								$formalParameters.params.toList(), 
								$block.value
							);
							
			// Documentation comment (if any)
			//
			setDocComment($value, docComment);
			
			// Tree span
			//
			endPos($value); 
		}
	;

// ---------
// Override.
// Specifes that the local class overrides something that it has
// inherited - parse this and produce the JavaFX tree that reflects it.
//
overrideDeclaration

	returns [JFXOverrideClassVar value]

	: OVERRIDE variableLabel  i=identifier (EQ boundExpression)? onReplaceClause?
	
		{
			// Build the AST
			//
			$value = F.at(pos($OVERRIDE)).OverrideClassVar
						(
							$i.value,
							$boundExpression.value,
							$boundExpression.status,
							$onReplaceClause.value
						);
			
			// Tree span
			//
			endPos($value);
		}
	;

// ------------
// Init block.
// Parse the initialization block for a class definition.
// Note that we allow more than one of these syntactically.
//
initDefinition

	returns [JFXInitDefinition value]	// The initialisation block has a specialized JFX tree node
	
	: INIT block
	
		{
			// Build the AST
			//
			$value = F.at(pos($INIT)).InitDefinition($block.value);
			
			// Tree span
			//
			endPos($value); 
		}
	;

// Post initialization.
// Parse the post initialization block and produce the AST
//
postInitDefinition

	returns [JFXPostInitDefinition value]	// Post initialization has its own specialized JFX tree node

	: POSTINIT block
		{ 
			// Build the AST
			//
			$value = F.at(pos($POSTINIT)).PostInitDefinition($block.value);

			// Tree span
			//
			endPos($value); 
		}
	;
	
//triggerDefinition
//	: WITH name onReplaceClause		-> ^(WITH name onReplaceClause)
//	;


// Variables.
// While they can be defined at different levels (script, member, local) the syntax
// for declaring variables, and the modifiers and so on are all exactly
// the same (syntactically) at all levels.
// Parser a variable declaration and return the resultant JFX expression tree.
//
variableDeclaration [ JFXModifiers mods ]

	returns [JFXExpression value]

@init { 

	// Search for the document comment token. At this point LT(1)
	// returns the first on channel token, so we can scan back from
	// there to see if there was a document comment.
	//
	CommonToken  docComment = getDocComment(input.LT(1));

    // Bind status if present
    //
    JavafxBindStatus bStatus = null;

    // Bind value expression, if present
    //
    JFXExpression bValue = null;

    // ONReplace clause if present
    //
    JFXOnReplace  oValue = null;
}
	: variableLabel  name  typeReference 

        (
            (EQ)=>EQ boundExpression
                {
                    bValue  = $boundExpression.value;
                    bStatus = $boundExpression.status;
                }
        )? 
        
        (
            (ON)=>onReplaceClause
                {
                    oValue = $onReplaceClause.value;
                }
        )?
	
		{
			// Add in the modifier flags accumulated by the label type
			// Note that syntactiaclly, we allow all label types at all levels and must throw
			// out any invalid ones at the semantic checking phase
			//
			$mods.flags |= $variableLabel.modifiers;
	    	
	    	// Construct the varaible JFXTree
	    	//
	    	$value = F.at($variableLabel.pos).Var
	    				(
	    					$name.value,
	    					$typeReference.rtype,
	    					$mods,
	    					false,
	    					bValue,
	    					bStatus,
	    					oValue
	    				);
	    	
	    	// Documentation comment (if any)
	    	//
			setDocComment($value, docComment);
			
			// Tree span
			//
			endPos($value); 
		}
	;
	

// ----------------
// Parameter lists.
// Parse the formal parameters of a function declaration and produce the
// corresponding AST. 
//
formalParameters

	returns [ListBuffer<JFXVar> params = new ListBuffer<JFXVar>()]		// Return type is a list of all the AST nodes that represent a 
																		// formal parameter, this is used to generate the AST for the
																		// funciton definition itself.
 
	: LPAREN 
	
		(
			fp1=formalParameter 
	
			{
				// Capture the first parameter
				//
				params.append($fp1.var); 
			}
			(
				COMMA fp2=formalParameter
				
					{ 
						// Second and subsequent parameter ASTs
						//
						params.append($fp2.var); 
					} 
			)*  
		)?
			
	  RPAREN
	;
	
// -----------------
// Formal parameter.
// Parse the specification of an individual function parameter and
// produce the AST. Note that a parameter may be left empty
//
formalParameter

	returns [JFXVar var]	// Formal parameters are contained in a JFX tree var node.

	: name typeReference
	
		{ 
			$var = F.at($name.pos).Param($name.value, $typeReference.rtype);
			endPos($var); 
		}
	;

// ------
// block.
// A block component is actually a unit that returns a value, it is an expression.
// In certain contexts the braces are more lexigraphically significant, such as the
// boundaires of if blocks. Hence those contexts specify the block directly rather than
// leaving the expression statement to pick it up.
//
// This means that a statement such as:
//
// if (x) { y} -z
//
// Does not consume the { y } -z as a complete expression but does
// what a programmer intuitively expects and uses only {y } as the
// subject of the if, with -z being a separate expression statement.
//
// A programmer can treat the above construct as a single expression
// by enclosing it in braces:
//
// if (x) { { y } -z }
//
// Which is then obvious. This also improves error recovery possibilities,
// which is a requirement for code completion utilities and so forth.
//
block 

	returns [JFXBlock value]	// The block expression has a specialized node inthe JFX tree

@init { 

	// A list of all the statement ASTs that make up the block expression
	//
	ListBuffer<JFXExpression> stats = new ListBuffer<JFXExpression>(); 
	
	// For building invidual statements
	//
	JFXExpression val = null;
}
	
	: LBRACE 
	
		(
			statement
	
				{
					// If the current statement is not the first one
					// then append it to the list. This logic leaves us with 
					// the AST for the last statement in the block
					// in our val variable, which we need to build the
					// AST for the block.
					//
					if	(val != null)
					{
						stats.append(val);
					}
					
					// Pick up the AST for the staemetn we just parsed.
					//
					val = $statement.value;
				}
				
			| SEMI
	   )*
	
	  RBRACE
	  
	  	{
		  	$value = F.at(pos($LBRACE)).Block(0L, stats.toList(), val);
	  		endPos($value);
	  	}
	;

// -----------
// statements.
// Parse the set of elments that are viewed as programmig statements. Note
// that this includes expressions which are considered statements.
// Note that each individual statement specifies whether it requires a
// terminating SEMI, whether this is optional, or whether this is just
// not required (such as if () {} ).
//
statement 

	returns [JFXExpression value] // All statements return an expression tree

	: insertStatement		{ $value = $insertStatement.value; 								}
	| deleteStatement		{ $value = $deleteStatement.value; 								}
 	| whileStatement		{ $value = $whileStatement.value; 								}
	| BREAK    				{ $value = F.at(pos($BREAK)).Break(null); 		endPos($value); } SEMI
	| CONTINUE  	 	 	{ $value = F.at(pos($CONTINUE)).Continue(null);	endPos($value); } SEMI
    | throwStatement	   	{ $value = $throwStatement.value; 								}
    | returnStatement 		{ $value = $returnStatement.value; 								}
    | tryStatement			{ $value = $tryStatement.value; 								}
    | expression SEMI		{ $value = $expression.value; 									}
    ;
  
// -----------  
// ON REPLACE.
// Parse an ON REPLACE clause which is an optional element of variable
// declarations and OVERRIDEs.
//
onReplaceClause

	returns [JFXOnReplace value]	// onReplace has its own JFX Tree node type
	
@init
{
	// Indicates presence of first and last elements
	//
	boolean haveFirst = false;
}


	: ON REPLACE oldv=paramNameOpt 
	
		(
			LBRACKET first=paramName DOTDOT last=paramName RBRACKET
			
			{ 
				haveFirst = true;	// Signal for AST build
			}
		)? 
		EQ newElements=paramName
	
		block
		
		{ 
			// Build the appropriate AST
			//
			if	(haveFirst) {
			
				$value = F.at(pos($ON)).OnReplace($oldv.var, $first.var, $last.var, $newElements.var, $block.value);
				
			} else {
			
				$value = F.at(pos($ON)).OnReplace($oldv.var, null, null, $newElements.var, $block.value);
			}
			endPos($value); 
		}
	;
	
// ------------------
// Optional parameter
// Parse and construct an AST for optional parameters
//
paramNameOpt

	returns [JFXVar var]	// Returns a JFXVar tree node

    : paramName
    	{
    		{ $var = $paramName.var; }
    	}
    	
    |	{ $var = null; }
    ;

// ---------
// Parameter.
// Parse and construct the AST for a parameter
//
paramName

	returns [JFXVar var]	// Returns a JFXVar tree node

	: name
		{
    		{ $var = F.at($name.pos).Param($name.value, F.TypeUnknown()); }
    	}
	;
	
    
// The ways in which a variable can be declared
//
variableLabel 
	
	returns [long modifiers, int pos] // returns the appropriate modifier flags and the position of the token
	
	: VAR			{ $modifiers = 0L; $pos = pos($VAR); }
	| DEF			{ $modifiers = JavafxFlags.IS_DEF; $pos = pos($DEF); }
	| ATTRIBUTE		{ $modifiers = 0L; $pos = pos($ATTRIBUTE); log.warning(pos($ATTRIBUTE), "javafx.not.supported.attribute"); }
	;

// ------	
// Throw.
// Parse the standard exception throwing mechanism.
//
throwStatement

	returns [JFXExpression value]	// Returns the JFX Expression tree representing what we must throw

	: THROW expression SEMI
	
		{ 
			// AST for the thrown expression
			//
			$value = F.at(pos($THROW)).Throw($expression.value);
			
			// Tree span
			//
			endPos($value);
		}
	;

// ---------------
// While statement
//
whileStatement
	
	returns [JFXExpression value]	// Returns the JFX Expression tree representing the WHILE
	
@init
{
	JFXExpression loopVal = null;
}
	: WHILE LPAREN expression RPAREN 
	
		(	  (LBRACE)=>block
				{
					loopVal = $block.value;
				}
			| statement
			
				{
					loopVal = $statement.value;
				}
		)
		
		{
			
			// The AST for the WHILE, using either the block or statement
			//
			$value = F.at(pos($WHILE)).WhileLoop($expression.value, loopVal);
			
			// Tree span
			//
			endPos($value);
		}
	;

// -------
// INSERT.
// Parse the insert statement and produce the relevant AST
//
insertStatement  
	
	returns [JFXExpression value]	// All steatemetns return a JFX expression tree

	: INSERT elem=expression
		(
			  INTO eseq=expression
			  
			  	{
			  		// Form 1, INTO
			  		//
					$value = F.at(pos($INSERT)).SequenceInsert($eseq.value, $elem.value, null, false);
			  	}
			  	
			| BEFORE isfi=indexedSequenceForInsert
			
				{
					// Form 2, BEFORE
					//
					$value = F.at(pos($INSERT)).SequenceInsert($isfi.seq, $elem.value, $isfi.idx, false);
				}
				
			| AFTER isfi=indexedSequenceForInsert
			
				{
					// Form 3, AFTER
					//
					$value = F.at(pos($INSERT)).SequenceInsert($isfi.seq, $elem.value, $isfi.idx, true);
				}
		)

		{
			// Tree span
			//
			endPos($value);
		}	    
	    SEMI
	    
	;
	
// ----------------
// Insert seqeunce.
// Parse the syntax for an insert sequence specified by the 
// INSERT BEFORE and INSERT AFTER variants.
//
indexedSequenceForInsert

	returns [JFXExpression seq, JFXExpression idx]

	: primaryExpression 			
	
		{
			// Sequence expression
			//
			$seq = $primaryExpression.value;
		}
		
	  LBRACKET expression RBRACKET
	  
	  	{
	  		// Index expressions
	  		//
	  		$idx = $expression.value;
	  	}
	  		
 	;
 
// -----------------	
// DELETE statement.
// Parse the DELETE statement forms and return the appropriate AST
//
deleteStatement  

	returns [JFXExpression value]	// Delete returns a JFX Expression tree

	: DELETE e1=expression

	   ( 
	   		  (FROM)=>FROM e2=expression
	   		  	
	   		  	{
	   		  		$value = F.at(pos($DELETE)).SequenceDelete($e2.value,$e1.value);
	   		  	}
	   		  	
	   		| /* indexed and whole cases */
	   		
	   			{
	   				$value = F.at(pos($DELETE)).SequenceDelete($e1.value);
	   			}
	   			
	   )
	   
	   {
	   		// Tree span
	   		//
	   		endPos($value);
	   }
	   SEMI
	;

// -----------------
// RETURN statement.
// Parse the return statement forms and produce the relevant AST
//
returnStatement

	returns [JFXExpression value]	// RETURN returns a JFX Expression tree

	: RETURN 
		
		(
			  expression	{	$value = F.at(pos($RETURN)).Return($expression.value);	}
			|				{	$value = F.at(pos($RETURN)).Return(null);				}
		)
		
		{
			// Tree span
			//
			endPos($value);
		}
		
		SEMI
	;
	
// -----------------------------
// TRY..CATCH..FINALLY seqeunce.
// Parse and build the AST for the stabdard try sequence
// TODO: Come back and relax the syntax requirements so as to catch malformed structure at semantic level
//       I.E. "Too many finally claues for try at nnn"
tryStatement

	returns [JFXExpression value]	// returns a JFX Expression tree
	
@init
{
	// AST for any catch clauses
	//
	ListBuffer<JFXCatch> caught = ListBuffer.lb();
}
	: TRY block 			
		(
		 	  f1=finallyClause
	   		| (
	   				catchClause
	   				
	   				{
	   					// Accumulate the catch clauses
	   					//
	   					caught.append($catchClause.value);
	   				}
	   		  )+ 
	   			
	   			( 
	   				f1=finallyClause
	   			)?   
	   	)
	   	
	   	{
	   		// Build the AST
	   		//
	   		$value = F.at(pos($TRY)).Try($block.value, caught.toList(), $f1.value);
	   		
	   		// Tree span
	   		//
	   		endPos($value);
	   	}
	;
	
// -------
// FINALLY
// Parse the finally clause of a trey...catch...finally sequence
//
finallyClause

	returns [JFXBlock value] // returns a JFX Expression tree
	
	: FINALLY block
	
		{
			$value = $block.value;
			endPos($value);
		}
	;
	
// ------
// CATCH.
// Parse a catch clause of a try...catch...finally
//
catchClause

	returns [JFXCatch value]	// Catch has its own JFX tree node type
	
	: CATCH LPAREN formalParameter RPAREN block
	
		{
			$value = F.at(pos($CATCH)).Catch($formalParameter.var, $block.value);
			endPos($value);
		}
	;
	
// ---------------------
// Boundable expression.
// Used to parse expressions that can be bound to a variable.
//
boundExpression 

	returns [JavafxBindStatus status, JFXExpression value] 	// We nede to return a status flag to say how and if the
															// expression is bound, and the AST for the expression itself.

@init 
{ 
	boolean isLazy 			= false; 	// Signals presence of LAZY
	boolean isBidirectional	= false; 	// Signals presence of INVERSE
}

	: BIND e1=expression 
	
			(
				(WITH)=>WITH INVERSE
				
				{
					// Update status
					//
					isBidirectional = true;
				}
			)?
			
			{
				// Set up the bound expression
				//
				$value	= $e1.value;
				
				// Update the status
				//
				$status	= isBidirectional? isLazy? LAZY_BIDIBIND : BIDIBIND
	  									   : isLazy? LAZY_UNIDIBIND :  UNIDIBIND;
			}
	
	| e2=expression
	
		{
			// Unbound expression AST
			//
			$value 	= $e2.value;
			
			// Update the status
			//
			$status	= UNBOUND;
		}
	;
	
// -----------
// expression.
// General expression parse and AST build.
//
expression

	returns [JFXExpression value]	// Expression has its own dedicated JFX tree node type
 
	: ifExpression

		{
			$value = $ifExpression.value;
		}
				
	| forExpression   	

		{
			$value = $forExpression.value;
		}
		
	| newExpression

		{
			$value = $newExpression.value;
		}
		
	| assignmentExpression

		{
			$value = $assignmentExpression.value;
		}
		
	| m=modifiers variableDeclaration [$m.mods]
	
		{
			$value = $variableDeclaration.value;
		}
	;

// ------------------------
// FOR statement/expression
//
forExpression

	returns [JFXExpression value]	// All statements are expressions

@init
{
	// In clause accumulator
	//
	ListBuffer<JFXForExpressionInClause> clauses = ListBuffer.lb();
}
	: FOR 
		LPAREN 
		
			i1=inClause			{  clauses.append($i1.value); }
			(COMMA i2=inClause	{  clauses.append($i2.value); } )* 
			
		RPAREN 
		
		(
			  (LBRACE)=>block
			  
			  	{
			 		$value = F.at(pos($FOR)).ForExpression(clauses.toList(), $block.value);
				}
				
			| statement
				
				{
			 		$value = F.at(pos($FOR)).ForExpression(clauses.toList(), $statement.value);
				}
		)
		
		{
			// Tree span
			//
			endPos($value);
		}
	;

// ----------
// IN clause.
// Parse an individual IN clause of a FOR statement.
//
inClause

	returns [JFXForExpressionInClause value]	// Dedicated AST tree node

@init
{
	// Assume no WHERE expression
	//
	JFXExpression weVal = null;
}

	: formalParameter IN se=expression 
	
		(
			  WHERE we=expression	{ weVal = $we.value; }
			|
		)
		
		{
			$value = F.at(pos($IN)).InClause($formalParameter.var, $se.value, weVal);
			endPos($value); 
		}
	;
	
// -----------------------
// If Then Else expression
//
ifExpression 

	returns [JFXExpression value]	// The expression tree that represents the If expression
	
@init
{
	// Statement or block expression
	//
	JFXExpression sVal = null;
	
	// Else expression (if present)
	//
	JFXExpression eVal = null;
}
	: IF LPAREN econd=expression  RPAREN 
		THEN?  
			(	  (LBRACE)=>block	{ sVal = $block.value; 		}
				| statement  		{ sVal = $statement.value;		}
			)
			(
				(ELSE)=>elseClause	{ eVal = $elseClause.value;	}
			)?
			
		{
			// The IF AST
			//
			$value = F.at(pos($IF)).Conditional($econd.value, sVal, eVal);
			
			// Tree span
			//
			endPos($value);
		}
	;
	
// -----------
// Else clause
// Parse the else expression of an if statement
//
elseClause

	returns [JFXExpression value]	// The expression tree that represents the Else expression
	
	: ELSE  
		(
			  (LBRACE)=>block	{ $value = $block.value; 		}
			| statement			{ $value = $statement.value; 	}
		)
	;
	

// -----------
// Assignment.
// Parse and produce teh AST for an assignement expression. Note
// that name of this rule is a slight misnomer. It might encapsulate
// an assignment, but it might be just a straight expression.
//
assignmentExpression  

	returns [JFXExpression value]	// The expression tree that represents the assignment expression

@init
{
	// Work out current position in the input stream
	//
	int	rPos = pos();
}
	: lhs=assignmentOpExpression 
		(     
			  (EQ)=> EQ rhs=expression
			  
			  	{
			  		// This is actually an assign
			  		//
			  		$value = F.at(rPos).Assign($lhs.value, $rhs.value);
			  		
			  		// Tree span
			  		//
			  		endPos($value);
			  	}
			  	
			|	// Just an expression without an assignment
				//
				{
					$value = $lhs.value;
				}
		)
	;
	
assignmentOpExpression

	returns [JFXExpression value]	// The expression tree that represents the assignment expression

@init
{
	// Work out current position in the input stream
	//
	int	rPos = pos();
}

	: lhs=andExpression					
	  
		(     assignOp rhs=expression
		
				{
					// AST for assignement
					//
					$value = F.at(rPos).Assignop($assignOp.op, $lhs.value, $rhs.value);
				}
				
           	| SUCHTHAT such=andExpression (TWEEN i=andExpression)?
           	
           		{
           			// AST FOR Interpolation
           			//
           			$value = F.at(rPos).InterpolateValue($lhs.value, $such.value, $i.value);
           		}
           	
	   		|	{ 
	   				// AST for expressions
	   				//
	   				$value = $lhs.value; 
	   			}	
	   )
	   
	   {
	   		// AST Span
	   		//
	   		endPos($value);
	   }
	;

// -----------------
// Assign operators
// All the operators that involve assignments.
//	
assignOp

	returns	[JavafxTag op]	// Returns the operation token that we find
	
	: PLUSEQ		{ $op = JavafxTag.PLUS_ASG; 			}
	| SUBEQ			{ $op = JavafxTag.MINUS_ASG;			}
	| STAREQ		{ $op = JavafxTag.MUL_ASG;              }
	| SLASHEQ		{ $op = JavafxTag.DIV_ASG;				}
	| PERCENTEQ
		{ 
			$op = JavafxTag.MOD_ASG;
			log.warning(pos($PERCENTEQ), MsgSym.MESSAGE_JAVAFX_GENERALWARNING, "The operator \%= will not be supported in the JavaFX 1.0 release" );
		}
	;
	
// -------------
// AND opertator
// LL(k) AND precedence
//
andExpression

	returns [JFXExpression value] 	// Expression tree for AND
		
@init
{
	// Work out current position in the input stream
	//
	int	rPos = pos();
}

	:	e1=orExpression
			
			{
				$value = $e1.value;
			}
	  		( 
	  			AND e2=orExpression
	  			
	  			{
	  				$value = F.at(rPos).Binary(JavafxTag.AND, $e1.value, $e2.value);
	  				endPos($value);
	  			}
	  		)*
	;
	
// -----------
// OR operator
// LL(k) OR precedence
//
orExpression

	returns [JFXExpression value] 	// Expression tree for OR
		
@init
{
	// Work out current position in the input stream
	//
	int	rPos = pos();
}

	: e1=typeExpression

		{
			$value = $e1.value;
		}
	  	( 
	  		OR e2=typeExpression 
	  		
	  		{
	  			$value = F.at(rPos).Binary(JavafxTag.OR, $e1.value, $e2.value);
	  			endPos($value);
	  		}
	  	)*
	;
	
// ----------------
// Typed expression
// LL(k) precedence
//
typeExpression 

	returns [JFXExpression value] 	// Expression tree for typed expressions
		
@init
{
	// Work out current position in the input stream
	//
	int	rPos = pos();
}

	: relationalExpression		

		(
			  INSTANCEOF itn=type
			
				{
					$value = F.at(rPos).TypeTest($relationalExpression.value, $itn.rtype);
					endPos($value);
				}
			| AS atn=type
			
				{
					$value = F.at(rPos).TypeCast($atn.rtype, $relationalExpression.value);
					endPos($value);
				}
			
			| 	{
					$value = $relationalExpression.value;
				}
	   )
	;

// -----------
// Relationals
// LL(k) precedence
//	
relationalExpression  

	returns [JFXExpression value] 	// Expression tree for typed expressions
		
@init
{
	// Work out current position in the input stream
	//
	int	rPos = pos();
}

	: a1=additiveExpression	{ $value = $a1.value;	}
		(
			  relOps   a2=additiveExpression
			  	
			  	{
			  		$value = F.at(rPos).Binary($relOps.relOp, $a1.value, $a2.value);
			  		endPos($value);
			  	}
		)* 
	;
	
// ---------------------
// Relational operators.
// LL(k) precedence, all operators are same precedence
//
relOps

	returns [JavafxTag relOp]	// Returns the JFX operator type
	
	: LTGT
		{ 
			$relOp = JavafxTag.NE;
			log.warning(pos($LTGT), MsgSym.MESSAGE_JAVAFX_GENERALWARNING, "The not-equal operator <> will be replaced by !=" );
		}	
			  		
	| NOTEQ  { $relOp = JavafxTag.NE;	}
	| EQEQ   { $relOp = JavafxTag.EQ;	}
	| LTEQ   { $relOp = JavafxTag.LE;	}
	| GTEQ   { $relOp = JavafxTag.GE;	}
	| LT     { $relOp = JavafxTag.LT;	}
	| GT     { $relOp = JavafxTag.GT;	}
	;

// ---------------------
// Arithmetic operations
// LL(k) precedence.
//	
additiveExpression 

	returns [JFXExpression value] 	// Expression tree for additive expressions
		
@init
{
	// Work out current position in the input stream
	//
	int	rPos = pos();
}
	: m1=multiplicativeExpression	{ $value = $m1.value; }
		(
		      arithOps   m2=multiplicativeExpression

				{
					$value = F.at(rPos).Binary($arithOps.arithOp , $m1.value, $m2.value);
					endPos($value);
				}
		)* 
	;

// --------------------
// Arithmetic operators
//
arithOps

	returns [JavafxTag arithOp]	// Returns the JFX operator type
	
	: PLUS		{ $arithOp = JavafxTag.PLUS; 	}
	| SUB		{ $arithOp = JavafxTag.MINUS;	}
	;

// --------------------------
// Multiplicative expressions
// LL(k) precedence emboides all operators at the same precednce as MUL
//	
multiplicativeExpression

	returns [JFXExpression value] 	// Expression tree for additive expressions
		
@init
{
	// Work out current position in the input stream
	//
	int	rPos = pos();
}
	: u1=unaryExpression	{ $value = $u1.value; }
		(
			multOps u2=unaryExpression
				
				{
					$value = F.at(rPos).Binary($multOps.multOp, $u1.value, $u2.value);
					endPos($value);
				}
	   )* 
	;

// -------------------------
// Multiplicative operators.
// LL(k) precedence - incorporates any other operators at this precedence
//
multOps

	returns [JavafxTag multOp]	// Returns the JFX operator type
	
	: STAR    	{ $multOp = JavafxTag.MUL;	}
	| SLASH   	{ $multOp = JavafxTag.DIV;	}
	| PERCENT 	
			
		{
			$multOp = JavafxTag.MOD;
			log.warning(pos($PERCENT), MsgSym.MESSAGE_JAVAFX_GENERALWARNING, "The remainder operator \% will be replaced by mod" );
		}	
             
	| MOD		{ $multOp = JavafxTag.MOD;	}
	;
	
// -----------------	
// Unary expressions
// LL(k) Precedence
//
//TODO: POUND QUES TYPEOF REVERSE
unaryExpression

	returns [JFXExpression value] 	// Expression tree for unary expressions

@init
{
	// Work out current position in the input stream
	//
	int	rPos = pos();
}

	: se=suffixedExpression

		{
			$value = $se.value;
		}
		
	| INDEXOF		id=identifier
	
		{ 	
			$value = F.at(rPos).Indexof($id.value);
			endPos($value);
		}
		
	| unaryOps     	e=unaryExpression

		{
			$value = F.at(rPos).Unary($unaryOps.unOp, $e.value);
			endPos($value);
		}
	;
	
// -------------------------
// Unary operators.
// LL(k) precedence
//
unaryOps

	returns [JavafxTag unOp]	// Returns the JFX operator type
	
	: SUB			{ $unOp = JavafxTag.NEG; }
	| NOT			{ $unOp = JavafxTag.NOT; }
	| SIZEOF		{ $unOp = JavafxTag.SIZEOF; }
	| PLUSPLUS		{ $unOp = JavafxTag.PREINC; }
	| SUBSUB		{ $unOp = JavafxTag.PREDEC; }
	| REVERSE		{ $unOp = JavafxTag.REVERSE; }
	;

// ------------------
// Postfix operations
// LL(k) precedence
//
suffixedExpression 

	returns [JFXExpression value] 	// Expression tree for suffix expressions

@init
{
	// Work out current position in the input stream
	//
	int	rPos = pos();
}

	: pe=postfixExpression
		( 
			  PLUSPLUS
			  
			  	{
			  		$value = F.at($pe.value.pos).Unary(JavafxTag.POSTINC, $pe.value);
			  		endPos($value);
			  	}
			  	
			| SUBSUB
			
				{
					$value = F.at($pe.value.pos).Unary(JavafxTag.POSTDEC, $pe.value);
					endPos($value);
				}
				
			| { $value = $pe.value; }
		)
	;
	
// ------------------------
// Postfix-able expressions
// LL(k) precedence
//
postfixExpression 

	returns [JFXExpression value] 	// Expression tree for suffix expressions

@init
{
	// Work out current position in the input stream
	//
	int	rPos = pos();
	
	// Position for pipe epxression
	//
	int pPos;
	
	// Indicates if we had the LT token
	//
	int clusiveType = SequenceSliceTree.END_INCLUSIVE;
	
	// Last element of sequence (if present)
	//
	JFXExpression	lastExpr = null;
}

	: pe=primaryExpression	{ $value = $pe.value; }
	
		( 
			  DOT 
				( 
					  n1=name
					  
					  {
							$value = F.at(rPos).Select($value, $n1.value);
					  }
					  
					//TODO:		 | CLASS 
	         	)

			| LPAREN expressionList RPAREN
			
				{
					$value = F.at(rPos).Apply(null, $value, $expressionList.args.toList());
					endPos($value);
				}
				
			| LBRACKET
			
				{
					// INit our flags
					//
					clusiveType = SequenceSliceTree.END_INCLUSIVE;
					lastExpr 	= null;
				}
	
				(
					  n2=name PIPE e1=expression RBRACKET
					  
					  {
					  	// Build a list of clauses as AST builder expects this
					  	//
					  	ListBuffer<JFXForExpressionInClause> clauses = ListBuffer.lb();
                  		
                  		// Build a var reference
                  		//
                  		JFXVar var = F.at($n2.pos).Param($n2.value, F.TypeUnknown());
                  		
                  		// Set up the in clause
                  		//
	          			clauses.append(F.at(rPos).InClause(var, $value, $e1.value));
	          			
	          			// Predicate needs identifier AST
	          			//
                  		$value = F.at(rPos).Predicate(clauses.toList(), F.at($n2.pos).Ident($n2.value));
                  		
                  		// Tree span
                  		//
                  		endPos($value);
					  }
					  
					| first=expression
                            
						(
							  RBRACKET
							  
							  	{
							  		$value = F.at(rPos).SequenceIndexed($value, $first.value);
							  		endPos($value);
							  	}
							  	
	                    	| DOTDOT 
	                    		(
									  (LT { clusiveType = SequenceSliceTree.END_EXCLUSIVE; } )? 
									  	(
									  		last=expression
									  		{
									  			lastExpr = $last.value;
									  		}
									  	)?
									  	
								)
								
	                      	  RBRACKET
	                      	  
	                      	  {
	                      	  	// If we have LT, then this is an exclusive slice
	                      	  	//
	                      	  	$value = F.at(rPos).SequenceSlice
	                      	  									(
	                      	  										$value,
	                      	  										$first.value,
	                      	  										lastExpr,
	                                                            	clusiveType
	                                                            );
								endPos($value);
	                      	  }
                    	)
             	)
	   	)* 
	;

// -------------------
// Primary expressions
// LL(k) precedence - primitives which cannot be reduced other
// than to atoms.
//	
primaryExpression  

	returns [JFXExpression value] 	// Expression tree for primary expressions

@init
{
	// Work out current position in the input stream
	//
	int	rPos = pos();
	
	// Use to build a list of objectLiteral parts.
	//
	ListBuffer<JFXTree> parts = ListBuffer.<JFXTree>lb();

    // Used to construct time literal expression
    //
    JFXExpression sVal = null;
	
}
	: qualname
		{
			$value = $qualname.value;
		}
		(
			LBRACE  
			  	
					o1=objectLiteral
						
			RBRACE
	              
				{
					// AST
					//
					$value = F.at(rPos).ObjectLiteral($value, $o1.parts.toList());
					
					// Tree span
					//
					endPos($value);
				}
			|
		)

	| THIS

		{
			$value = F.at(pos($THIS)).Ident(names._this);
			endPos($value);
		}
		
	| SUPER
	
		{
			$value = F.at(pos($SUPER)).Ident(names._super);
			endPos($value);
		}
		
	| se=stringExpression

		{
			$value = $se.value;
		}
		
	| be=bracketExpression
	
		{
			$value = $be.value;
		}
		
	| block
	
		{
			$value = $block.value;
		}
		
	| literal
	
		{
			$value = $literal.value;
		}
		
	| fe=functionExpression
	
		{
			$value = $fe.value;
		}
	
	| LPAREN e=expression RPAREN
	
		{
			$value = $e.value;
		}
		
	| AT 
		LPAREN 
			TIME_LITERAL
            {
                sVal = F.at(pos($TIME_LITERAL)).Literal(TypeTags.CLASS, $TIME_LITERAL.text);
                endPos(sVal);
            }
		RPAREN 
		LBRACE 
			k=keyFrameLiteralPart 
		RBRACE
		
		{
			$value = F.at(rPos).KeyFrameLiteral(sVal, $k.exprs.toList(), null);
			endPos($value);
		}
	;
	
// ------------
// Frame values
//  
keyFrameLiteralPart

	returns [ListBuffer<JFXExpression> exprs = new ListBuffer<JFXExpression>(); ]	// Gathers a list of expressions representing frame values

	: k1=expression 			{ exprs.append($k1.value);	}
	
		(SEMI+
		
			k2=expression		{ exprs.append($k2.value);	}
		)* SEMI*
    ;

// -------------------
// Anonymous functions
//
functionExpression

	returns [JFXExpression value] 	// Expression tree for anonymous function

	: FUNCTION formalParameters typeReference block
	
		{
			// JFX AST
			//
			$value = F.at(pos($FUNCTION)).FunctionValue
								(
									$typeReference.rtype, 
									$formalParameters.params.toList(),
									$block.value
								);
								
			// Tree span
			//
			endPos($value);
		}
	;
	
// ---
// NEW
//
newExpression

	returns [JFXExpression value] 	// Expression tree for new expression
	
	: NEW typeName expressionListOpt
	
		{
			$value = F.at(pos($NEW)).InstanciateNew($typeName.value, $expressionListOpt.args.toList());
			endPos($value);
		}
	;

// ---------------
// Object literals
//
objectLiteral

	returns [ListBuffer<JFXTree> parts = ListBuffer.<JFXTree>lb()]	// Gather a list of all the object literal insitalizations
	
	:   (COMMA|SEMI)*	// Separators are optional and just syntactic sugar
		(
			oli=objectLiteralPart 
			
				(COMMA|SEMI)*	// Separators are optional and just syntactic sugar
			
			{
				parts.append($oli.value);
			}
		)+
	;

// Individual components of an object literal
//
objectLiteralPart

	returns [JFXTree value] 	// Expression tree for object literal elements

	: (OVERRIDE)=>overrideDeclaration
	
		{
			$value = $overrideDeclaration.value;
		}
		
	| modifiers
		(
			  variableDeclaration    [$modifiers.mods]
			  
			  	{
			  		$value = $variableDeclaration.value;
			  	}
			  	
			| functionDefinition	 [$modifiers.mods]
			
				{
					$value = $functionDefinition.value;
				}
		)
		
	| oli=objectLiteralInit
		
		{
			$value = $oli.value;
		}
    ;
  
// --------------------------     	
// Object literal initializer
//	
objectLiteralInit

	returns [JFXTree value]		// Construct the AST for a name value pair
	
@init
{
	// Work out current position in the input stream
	//
	int	rPos = pos();
	
}
	: name COLON  boundExpression
	
		{
			// AST
			//
			$value = F.at($name.pos).ObjectLiteralPart
									(
										$name.value,
								 		$boundExpression.value, 
								 		$boundExpression.status
								 	);
								 	
			// Tree span
			//
			endPos($value);
		}
	;

// -------	
// Strings
// JavaFX string expresoins are more richly expressive than the more usual
// quoted strings.
//
// 1) A translation key may prefix string literals;
// 2) The string literal itself may consist of multiple parts, which are
//    concatenated at compile time, rather than run time.
//    I.E. ##"MyTransKey" "String part 1" "String part 2\n"
//    This allows for multi line string literals, built at compile time
//    over which the script author then has unambiguous control over
//    leading spaces, can explictly insert new lines, and can comment
//    individual components.
//    I.E. 
//      var myString =
//          "<header>"       // This is the header
//          "some stuff\n"   // Some stuff now, with a trailing newline
//          "  level 1\n"    // More stuff, with leading spaces and a traliing newline
//
// Overall this leades to better error recovery for the parser, while leaving
// the language syntax obvious to and clearly controlled by, the script author;
//
// Notes: 
//
// 1) Only string literals can be compounded in this way - expressions are
//    parsed here, but are thrown out with a semantic error explaining
//    that this is a compile time concept, not a runtime concept;
// 2) A single translation key prefix applys to the entire compound string.
//    Individual compound parts cannot be translated individually;
//
stringExpression 

	returns [JFXExpression value] 	// Expression tree for stringExpressions

@init
{
	// Buffer in which to accumulate all string elements
	//
	ListBuffer<JFXExpression> strexp = new ListBuffer<JFXExpression>();
	
	// Translation key, if any, for the literal string
	//
    String translationKey = null;
    
    // Work out current position in the input stream
	//
	int	rPos = pos();
}

	: (
		  (
			// Translation key is optional
		  	//
		  	TRANSLATION_KEY	{ translationKey = $TRANSLATION_KEY.text; } 
		  )?
	
			// We must find at least one compund element to the string
			//
			strCompoundElement [ strexp ]
			
			(
				// After the first element, there may be any number of additional
				// elements, including zero. We must force the parser to take
				// the righteous path for syntactically correct constructs, then
				// error out semantically on anything else.
				//
				   (STRING_LITERAL|QUOTE_LBRACE_STRING_LITERAL) =>strCompoundElement [ strexp ]
			
					// Expressions are not allowed as compound elements but this will be
					// a common thing to happen while editing and probably a common mistake
					// so we parse it and then falg a semantic error rather than issue
					// a syntax error which would be difficult to recover from.
					// TODO: See if we can resolve the left recurssion problems
					// taht this causes
				//| 	{
						// Record where we are for the error message
						//
				//		ePos = pos();
				//	}
				
				//	expression
			
				//	{
						// Issue semantic error
						// TODO: perhaps this should be added tot the AST
						//
				//		log.error(ePos, "javafx.bad.str.compound");
				//	}

			)*
	  )
	  
		{
  			// AST for string expression
  			// If we accumulated just a single entry then by definition
  			// we accumulated just a simple string literal, but if there
  			// is more than one entry, or there is a translation key,
  			// then we have a string expression
  			//
  			if	(strexp.size() > 1 || translationKey != null)
			{
				// Complex expression
				//
	  			$value = F.at(rPos).StringExpression(strexp.toList(), translationKey);

				// Tree span
  				//
  				endPos($value);

	  		}
	  		else
	  		{
	  			// This is an individual string literal, and is already endPos'ed
	  			//
	  			$value  = strexp.toList().get(0);
			}  			
		}
	;
	
// --------------------------------------------
// An individual component of a compound string
//
strCompoundElement [ ListBuffer<JFXExpression> strexp ]
	
	: stringLiteral	[ strexp ]	  		
	| qlsl 			[ strexp ]
	;
	
// ---------------
// String literals
// We may have multiple string literals following each
// other, which we auto concatentate here at compile time
//
stringLiteral [ ListBuffer<JFXExpression> strexp ]


@init
{
	// Record position of invalid expression used in compile time
	// string compounding.
	//
	int ePos;
	
	// Used to accumulate multiple string literals
	//
	StringBuffer sbLit = new StringBuffer();
	
	// The string litereal we will created
	//
	JFXExpression sVal = null;
	
}
	: s1=STRING_LITERAL 
	
		{
			// Accumulate the literal
			//
			sbLit.append($s1.text);
		}
	
		(
			(STRING_LITERAL)=> s2=STRING_LITERAL
			
			{
				// Accumulate the literal
				//
				sbLit.append($s2.text);
			}
		)*
		
		{
			// Now we create the actual string literal
			//
			sVal = F.at(pos($s1)).Literal(TypeTags.CLASS, sbLit.toString());
			
			// Tree span
			//
			endPos(sVal);
			
			// Add to list
			//
			strexp.append(sVal);
			
			// Here, if we have as yet encountered no other string literals, then
			// we can just add the Literal because the first literal of
			// the string expression AST is always without formatting etc,
			// however, if this is the second or subsequent literal such as
			// in: "hhhhhh{expr}" "Second", then the AST for string expressions
			// expects a format and expression to follow as it is assuming
			// a string expression rather than a string literal. Hence we must fake an empty format
			// and an empty string expression.
			//
			if	(strexp.size() > 1)
			{
				// Already had the first literal, add a fake format and expression
				//
				strexp.append(F.at(pos()).Literal(TypeTags.CLASS, ""));
				strexp.append(F.at(pos()).Literal(TypeTags.CLASS, ""));
			}
		}
	;
	
// --------------------
// String lit component
// String literals with embedded formats/expressions
//
qlsl [ ListBuffer<JFXExpression> strexp]

	: 	ql=QUOTE_LBRACE_STRING_LITERAL	
	
			{
				// Add in the discovered literal value
				//
				strexp.append
						(
							F.at(pos($ql)).Literal
											(	TypeTags.CLASS,
											 	$ql.text
											)
						);	
			}
			
		// Optional string format
		//
	    stringFormat	[strexp]
	    
	    // An expression to evaluate at runtime
	    // 
	  	e2=expression 					{ strexp.append($e2.value);	}
	  
	  	// Any number of inner elements
	  	//
	  	( stringExpressionInner [strexp]  )*   
	  
	  	// The last component of the {} enclosing string literal
	  	//
	  	qr=RBRACE_QUOTE_STRING_LITERAL
	  
	  		{
	  			// Add in the discovered literal
	  			//
				strexp.append
						(
							F.at(pos($qr)).Literal
											(	TypeTags.CLASS,
											 	$qr.text
											)
						);	
			}
	;
	
// ----------------------
// String element with optional format expression
//
stringExpressionInner [ ListBuffer<JFXExpression> strexp]

	: rlsl=RBRACE_LBRACE_STRING_LITERAL 
	
		{
			// Construct a new literal for the leading literal
			//
			JFXExpression rb = F.at(pos($rlsl)).Literal(TypeTags.CLASS, $rlsl.text);
			
			// Record the span
			//
			endPos(rb);
			
			// Add the literal to the list
			//
			strexp.append(rb);
			
		}
		
		// Deal with the string format
		//
		stringFormat[strexp] 
		
		// Expression to evaluate at runtime
		//
		expression
		
		{
			strexp.append($expression.value);
		}
	;
	
// --------------------
// Format specification
// Optional format specifier in standard Java form
//
stringFormat [ ListBuffer<JFXExpression> strexp]

@init
{
	// The value to add in to the mix
	//
	JFXExpression value;
	
	// Work out current position in the input stream
	//
	int	rPos = pos();
}
	: fs=FORMAT_STRING_LITERAL
	
		{
			value = F.at(rPos).Literal(TypeTags.CLASS, $fs.text);
			endPos(value);
			strexp.append(value);
		}
		
	| // no format
		{
			value = F.at(rPos).Literal(TypeTags.CLASS, "");
			endPos(value);
			strexp.append(value);
		}
	;
	
// ---------------------------
// Sequence
// Which is a [] enclosed expression list
//
bracketExpression

	returns [JFXExpression value] 	// Expression tree for stringExpressions

@init
{
	// Buffer in which to accumulate all string elements
	//
	ListBuffer<JFXExpression> seqexp = new ListBuffer<JFXExpression>();
    
    // Work out current position in the input stream
	//
	int	rPos = pos();
	
	// Optional step expression
	//
	JFXExpression	stepEx = null;
	
	// Optional LT qualifier
	//
	boolean 	haveLT	= false;
}

	: LBRACKET   
	
		( 	: COMMA* e1=expression
				{
					seqexp.append($e1.value);
				}
		     	(   
		     		  
		     		  (
		     			COMMA 
		     				(
		     					e2=expression
		     						{
		     							seqexp.append($e2.value);
		     						}
		     				)?
		     		  )*
	                    
	                    {
	                    	// Explicit sequence detected
	                    	//
	                    	$value = F.at(rPos).ExplicitSequence(seqexp.toList());
	                    	endPos($value);
	                    }
	                    
		     		| DOTDOT 
		     			(LT { haveLT = true; })? 
		     			dd=expression
		     	    	( STEP st=expression { stepEx = $st.value; } )?
		     	    	
		     	    	{
		     	    		$value = F.at(pos($DOTDOT)).RangeSequence($e1.value, $dd.value, stepEx, haveLT);
		     	    		endPos($value);
		     	    	}
		     	)
		     	
		     |  // Empty sequence 
		     	{
		     		 $value = F.at(rPos).EmptySequence();
		     	}
	    )
	  RBRACKET
	;

// ----------------
// Expression list.
// Comma separated list of expressions.
//
expressionList

	returns [ListBuffer<JFXExpression> args = new ListBuffer<JFXExpression>()]	// List of expressions we pcik up

	: e1=expression
		
		{
			args.append($e1.value);
		}
		
		(
			COMMA 	(
						e2=expression
						
						{
							args.append($e2.value);
						}
					)?
		)*
	;

// ------------------------
// Optional expression list
// For the moment this is only used by New....
//
expressionListOpt
	
	returns [ListBuffer<JFXExpression> args = new ListBuffer<JFXExpression>()]	// List of expressions we pcik up

	: LPAREN expressionList RPAREN
		{
			$args = $expressionList.args;
		}
		
	|	// Was not present
	;


// -----
// Types
//
type

	returns [JFXType rtype]

@init
{
    // Work out current position in the input stream
	//
	int	rPos = pos();
}
	: typeName cardinality
	
		{
			$rtype = F.at(rPos).TypeClass($typeName.value, $cardinality.ary);
			endPos($rtype);
		}
		
 	| FUNCTION 
 		LPAREN 
 			typeArgList
		RPAREN 
		
			ret=typeReference 
          	cardinality	//TODO: this introduces an ambiguity: return cardinality vs type cardinality
          	
		{
			$rtype = F.at(rPos).TypeFunctional($typeArgList.ptypes.toList(), $ret.rtype, $cardinality.ary);
			endPos($rtype);
		}

 	| STAR cardinality
 	
 		{
 			$rtype = F.at(rPos).TypeAny($cardinality.ary);
 			endPos($rtype);
 		}
 	;

// ----------------------------
// A list of types as arguments
//
typeArgList
 	
 returns [ListBuffer<JFXType> ptypes = ListBuffer.<JFXType>lb(); ]
 
 	: t1=typeArg
 	
 		{
 			ptypes.append($t1.rtype);
 		}
 		 
 		(
 			COMMA 
 			(
 				t2=typeArg
 				
 				{
 					ptypes.append($t2.rtype);
 				}
 			)?
 		)*
	;

// -------------------------
// Individual typed argument
//
typeArg 

	returns [JFXType rtype]

 	: (
 		(
 			name	// TODO: Check this, it is currently ignored for AST and does not
 					//       look quite right.
 		)? 
 			COLON
 	  )?
 	  
 	  type
 	
 		{
 			$rtype = $type.rtype;
 		}
 	;
 	
 // --------------
 // Type reference
 // Used to build parameter lists for functions etc
typeReference

	returns	[JFXType rtype]
	
 	: COLON type
 	
 		{
 			$rtype = $type.rtype;
 		}
 	| /*nada*/
 	;
 	
// -------------------------
// Array indicator for types
//
cardinality

	returns [TypeTree.Cardinality ary]
	
	: (LBRACKET)=>LBRACKET RBRACKET
	
		{
			$ary = TypeTree.Cardinality.ANY;
		}
		
	|	{
			$ary = TypeTree.Cardinality.SINGLETON;
		}
	;

// ----------
// Named type
// Possibly a generic
//
typeName

	returns [JFXExpression value]

@init
{
	// Accumulate any generic arguments
	//
	ListBuffer<JFXExpression> exprbuff = ListBuffer.<JFXExpression>lb();
}

	: qualname 		
		(
			  LT ga1=genericArgument 	{ exprbuff.append($ga1.value); }
			  	
			  		(
			  			COMMA
			  				(
			  					ga2=genericArgument
			  				
			  							{ exprbuff.append($ga2.value); }
			  				)?
			  		)* 
			  GT
			  
			  {
			  	// AST for generic
			  	//
			  	// TODO: Implement this?
			  	//
			  	log.error(pos($LT), "javafx.generalerror", "Java generic type declarations are not currently supported");
			  }
			  
			|	// Non generic
				{
					$value = $qualname.value;
				}
		)
	;
	
genericArgument

	returns [JFXExpression value]

@init 
{
	BoundKind 		bk 		= BoundKind.UNBOUND;
	JFXExpression 	texpr 	= null; 
}

	: typeName	{ $value = $typeName.value; }
	
	| QUES 
		(  
			( 
				  EXTENDS 		{ bk = BoundKind.EXTENDS; 	}
		  		| SUPER			{ bk = BoundKind.SUPER; 	}
		  	) 
		 	typeName			{ texpr = $typeName.value; }
		)?
		
		{
			// TODO: NYI - Remove or implement?
		}
	;

// --------
// Literals.
// Incorporates all literals except STRING_LITERAL which is dealt with
// in the stringExpression rule
//
literal

	returns [JFXExpression value]
	
@init
{
    // Work out current position in the input stream
	//
	int	rPos = pos();
}
	: 
		(
			 DECIMAL_LITERAL
			
				{
					$value = F.at(rPos).Literal(TypeTags.INT, (int)Convert.string2long($DECIMAL_LITERAL.text, 10));
				}
				
			| OCTAL_LITERAL
			
				{
					$value = F.at(rPos).Literal(TypeTags.INT, (int)Convert.string2long($OCTAL_LITERAL.text, 8));
				}
			
			| HEX_LITERAL
			
				{
					$value = F.at(rPos).Literal(TypeTags.INT, (int)Convert.string2long($HEX_LITERAL.text, 16));
				}
				
		    | TIME_LITERAL
		    
		    	{
		    		$value = F.at(rPos).TimeLiteral($TIME_LITERAL.text);
		    	}
		    	
			| FLOATING_POINT_LITERAL
			
				{
					$value = F.at(rPos).Literal(TypeTags.DOUBLE, Double.valueOf($FLOATING_POINT_LITERAL.text));
				}
				
			| TRUE
			
				{
					$value = F.at(rPos).Literal(TypeTags.BOOLEAN, 1);
				}
				
			| FALSE
			
				{
					$value = F.at(rPos).Literal(TypeTags.BOOLEAN, 0);
				}
				
			| NULL
			
				{
					$value = F.at(rPos).Literal(TypeTags.BOT, null);
				}
		)
		
		{
			// Tree span
			//
			endPos($value);
		}
	;

// -------------------------	
// Qualified (possibly) name
//
qualname

	returns [JFXExpression value]
	
	: n1=name
		{
			$value = F.at($n1.pos).Ident($n1.value);
			endPos($value, $n1.pos + $n1.value.length());
		}
		( 
			(DOT)=> DOT n2=name
			
				{
					$value = F.at(pos($DOT)).Select($value, $n2.value);
					endPos($value); 
				}
			
		)*  
	;

// -----------------------
// ID
// Basic identifier parse
//
identifier

	returns [JFXIdent value]

	: n1=name
		{
			$value = F.at($n1.pos).Ident($n1.value);
			endPos($value, $n1.pos + $n1.value.length());
		}
	;

// ------------------------
// ID
// Parse and identifier token that isn't necessarilly an Identifier,
// it coudl just be a tag or function name etc.
//
name 

	returns [Name value, int pos]
	
	: IDENTIFIER
	
		{ 
			$value = Name.fromString(names, $IDENTIFIER.text); 
			$pos = pos($IDENTIFIER); 
		}
						
	;
