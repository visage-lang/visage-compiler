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
	superClass 	= AbstractGeneratedParser; 
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

import com.sun.tools.javac.util.*;
import com.sun.tools.javafx.util.MsgSym;

import com.sun.tools.javac.tree.*;
import com.sun.tools.javafx.tree.*;
import com.sun.javafx.api.tree.*;

import com.sun.tools.javac.code.*;
import com.sun.tools.javafx.code.JavafxFlags;
import static com.sun.tools.javac.util.ListBuffer.lb;
import com.sun.javafx.api.JavafxBindStatus;

import static com.sun.javafx.api.JavafxBindStatus.*;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

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
		Token token = input.get(index);
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
	;$keyword$
	

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
//

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
			$result = F.Script($packageDecl.value, $scriptItems.items.toList());
            setDocComment($result, docComment);	// Add any detected documentation comment
		}

		EOF 	// Forces parser to consume entire token stream or error out

		{		
			// Set tree span
        	//
        	$result.pos = $result.pid != null ? $result.pid.pos : $result.defs.head.pos;
        	endPos($result); 
        }
    ;
    
    
packageDecl

	returns [JFXExpression value]

    : PACKAGE qualname SEMI
    
    		{ $value = $qualname.value; }
    		
    | // No package specified
    
    		{ $value = null; }
	;
	
scriptItems
	: scriptItem (SEMI scriptItem )*
	;
	
scriptItem
	: importDecl 	
	| classDefinition 	
	| functionDefinition    
	| (varModifier)=>scriptVariableDeclaration
	| statement	
	|					
	;
	
catch [RecognitionException re] {

    reportError(re);
    
    if (input.LA(1) != Token.EOF) {
    
        input.consume();
        scriptItem_return r = scriptItem();
        retval.tree = r.tree;
        
    }
}

importDecl
 	: IMPORT importId
	;
	
importId
 	: name
		( DOT name)* 
        ( DOT STAR)?  
	;
	
classDefinition 
@after { 

	Tree docComment = getDocComment($classDefinition.start);
    $classDefinition.tree.addChild(docComment); 
}
	: classModifierFlags  CLASS name supers LBRACE classMembers RBRACE
	;
	
supers 
	: EXTENDS typeName
           ( COMMA typeName )*
	|
	;
		  					
classMembers 
	: classMember 
	  (SEMI
	     classMember 
	  )*
	;
	
classMember
	: initDefinition	
	| postInitDefinition
	| classVariableDeclaration 
	| overrideDeclaration 
	| functionDefinition 
    |
	;
	
catch [RecognitionException re] {
  
    reportError(re);

    if (input.LA(1) != Token.EOF) {
    
        input.consume();
        classMember_return r = classMember();
        retval.tree = r.tree;
    
    }
}

functionDefinition
@after { 

	Tree docComment = getDocComment($functionDefinition.start);
    $functionDefinition.tree.addChild(docComment); 
}
	: functionModifierFlags FUNCTION name formalParameters typeReference block?
	;
	
overrideDeclaration
	: OVERRIDE classVarLabel  name (EQ boundExpression)? onReplaceClause?
;

initDefinition
	: INIT block
	;

postInitDefinition
	: POSTINIT block
	;
	
//triggerDefinition
//	: WITH name onReplaceClause		-> ^(WITH name onReplaceClause)
//	;

//TODO: modifier flag testing should be done in JavafxAttr, where it would be cleaner and better errors could be generated
//
functionModifierFlags  
	: functionModifier*
	;
	
scriptVariableDeclaration
@after { 

	Tree docComment = getDocComment($scriptVariableDeclaration.start);
    $scriptVariableDeclaration.tree.addChild(docComment); 
}
	: varModifierFlags variableLabel  name  typeReference (EQ boundExpression)? onReplaceClause?
	;
	
classVariableDeclaration   
@after { 

	Tree docComment = getDocComment($classVariableDeclaration.start);
    $classVariableDeclaration.tree.addChild(docComment); 
}
	: varModifierFlags classVarLabel  name  typeReference (EQ boundExpression)? onReplaceClause?
	;
	
localVariableDeclaration
@after { Tree docComment = getDocComment($localVariableDeclaration.start);
         $localVariableDeclaration.tree.addChild(docComment); 
}
	: variableLabel  name  typeReference ((EQ)=>EQ boundExpression)? ((ON)=>onReplaceClause)?
	;
	
varModifierFlags
	: varModifier*
	;
	
classModifierFlags
	: classModifier*
	;
	
accessModifier 
	:  PUBLIC          			
	|  PROTECTED       	
	|  PACKAGE       	
	
	//TODO: deprecated -- remove this at some point
	|  PRIVATE         			
	;
	
functionModifier 
	:  ABSTRACT        			
	|  OVERRIDE
	|  BOUND
	|  accessModifier	
	
	//TODO: deprecated -- remove this at some point
	|  STATIC    
	;
	
varModifier 
	:  PUBLIC_READABLE
	|  NON_WRITABLE		
	|  accessModifier	
	
	//TODO: deprecated -- remove these at some point
	|  READABLE        	
	|  STATIC        			
	;
	
classModifier 
	:  ABSTRACT        			
	|  accessModifier	
	;
	
formalParameters
	: LPAREN formalParameter (COMMA formalParameter)*  RPAREN
	;
	
formalParameter
	: name typeReference
    |
	;

catch [RecognitionException re] {
    reportError(re);
    if (input.LA(1) != Token.EOF) {
        input.consume();
        formalParameter_return r = formalParameter();
        retval.tree = r.tree;
    }
}

block 
	: LBRACE blockComponent
	   (SEMI blockComponent) *
	  RBRACE
	;
	
blockComponent
	: statement		
	|
	;
	
catch [RecognitionException re] {
    
    reportError(re);
    
    if (input.LA(1) != Token.EOF) {
        input.consume();
        blockComponent_return r = blockComponent();
        retval.tree = r.tree;
    }
}

statement 
	: insertStatement 	
	| deleteStatement 
 	| whileStatement
	| BREAK    		
	| CONTINUE  	 	 	
    | throwStatement	   	
    | returnStatement 		
    | tryStatement	
    | expression
    ;
    
onReplaceClause
	: ON REPLACE oldval=paramNameOpt clause=sliceClause? block
	;
	
sliceClause
	: LBRACKET first=name DOTDOT last=name RBRACKET EQ newElements=name

	| EQ newValue=name

	;
	
paramNameOpt
    : name
    |
    ;
    
variableLabel 
	: VAR	
	| DEF	
	;
	
classVarLabel 
	: VAR	
	| DEF	
	| ATTRIBUTE
	;
	
throwStatement
	: THROW expression
	;
	
whileStatement
	: WHILE LPAREN expression RPAREN block
	;
	
insertStatement  
	: INSERT elem=expression
	    ( INTO eseq=expression
	    | BEFORE indexedSequenceForInsert
	    | AFTER indexedSequenceForInsert
	    )
	;
	
indexedSequenceForInsert
	: primaryExpression 			
	  LBRACKET expression RBRACKET
 	;
 	
deleteStatement  
	: DELETE  e1=expression  
	   ( 
	   		  (FROM)=>FROM e2=expression
	   		| /* indexed and whole cases */
	   )
	;
	
returnStatement
	: RETURN expression?
	;
	
tryStatement
	: TRY block 			
		( 	  finallyClause
	   		| catchClause+ finallyClause?   
	   	)
	;
	
finallyClause
	: FINALLY block
	;
	
catchClause
	: CATCH LPAREN formalParameter RPAREN block
	;
	
boundExpression 
	: BIND expression ((WITH)=>WITH INVERSE)?
	| expression
	;
	
expression 
	: ifExpression   		
	| forExpression   	
	| newExpression 	
	| assignmentExpression	
	| localVariableDeclaration
	;

forExpression
	: FOR LPAREN inClause (COMMA inClause)* RPAREN statement
	;
	
inClause
	: formalParameter IN in=expression (WHERE wh=expression)?
	;
	
ifExpression 
	: IF LPAREN econd=expression  RPAREN 
		THEN?  ethen=statement  elseClause
	;
	
elseClause
	: (ELSE)=>  ELSE  statement
	| /*nada*/
	;
	
assignmentExpression  
	: assignmentOpExpression 
		(     
			  (EQ)=> EQ expression
			|
		)
	;
	
assignmentOpExpression 
	: e1=andExpression					
	  
		(     PLUSEQ	e2=expression
	   		| SUBEQ   	e2=expression
	   		| STAREQ   	e2=expression
	   		| SLASHEQ   e2=expression
	   		| PERCENTEQ	e2=expression	
	   		
	   				{ 
	   					log.warning(pos($PERCENTEQ), MsgSym.MESSAGE_JAVAFX_GENERALWARNING, "The operator \%= will not be supported in the JavaFX 1.0 release" );
	   				}
	   				
           	| SUCHTHAT v=andExpression (TWEEN i=andExpression)?
	   		|
	   )
	;
	
andExpression  
	:	e1=orExpression
	  		( AND e2=orExpression)*
	;
	
orExpression  
	: e1=typeExpression
	  
	  	( OR   e2=typeExpression )*
	;
	
typeExpression 
	: relationalExpression		
		(     INSTANCEOF itn=type
	   		| AS atn=type
	   		|
	   )
	;
	
relationalExpression  
	: additiveExpression
		(
			  LTGT   e=additiveExpression      
			  
			  		{ 
			  			log.warning(pos($LTGT), MsgSym.MESSAGE_JAVAFX_GENERALWARNING, "The not-equal operator <> will be replaced by !=" );
			  		}	
			  		
			| NOTEQ  e=additiveExpression
			| EQEQ   e=additiveExpression
			| LTEQ   e=additiveExpression
			| GTEQ   e=additiveExpression
			| LT     e=additiveExpression
			| GT     e=additiveExpression
		)* 
	;
	
additiveExpression 
	: multiplicativeExpression
		(
		      PLUS   e=multiplicativeExpression
			| SUB    e=multiplicativeExpression
		)* 
	;
	
multiplicativeExpression
	: unaryExpression
		(
			  STAR    	e=unaryExpression
			| SLASH   	e=unaryExpression
			| PERCENT 	e=unaryExpression
			
				{
					log.warning(pos($PERCENT), MsgSym.MESSAGE_JAVAFX_GENERALWARNING, "The remainder operator \% will be replaced by mod" );
				}	
             
           | MOD    	e=unaryExpression
	   )* 
	;
	
//TODO: POUND QUES TYPEOF REVERSE
unaryExpression 
	: suffixedExpression
	| SUB      	e=unaryExpression
	| NOT      	e=unaryExpression
	| SIZEOF   	e=unaryExpression
	| PLUSPLUS 	e=unaryExpression
	| SUBSUB   	e=unaryExpression
	| REVERSE  	e=unaryExpression
    | INDEXOF 	name
	;
	
suffixedExpression 
	: postfixExpression
		( 
			  PLUSPLUS
			| SUBSUB
			|
		)
	;
	
postfixExpression 
	: primaryExpression
	
		( 
			  DOT 
				( 
					  name
					//TODO:		 | CLASS   					
	         	)

			| LPAREN expressionList RPAREN
			| LBRACKET 
	
				(
					  name PIPE expression RBRACKET
					| first=expression
                            
						(
							  RBRACKET
	                    	| DOTDOT 
	                    		(
									  LT last=expression?
									| last=expression?
								)
	                      		RBRACKET
                    	)
             	)
	   	)* 
	;
	
primaryExpression  
	: qualname					
		(
			  LBRACE  objectLiteralPart
	              (SEMI objectLiteralPart)*   RBRACE
			|
		)
	| THIS
	| SUPER
	| stringExpression
	| bracketExpression
	| block
	| literal
	| functionExpression
	| LPAREN expression RPAREN
	| AT 
		LPAREN 
			TIME_LITERAL 
		RPAREN 
		LBRACE 
			keyFrameLiteralPart 
		RBRACE
	;
	  
keyFrameLiteralPart
	: keyValuePart (SEMI keyValuePart)*
    ;
    
keyValuePart
	: expression
    |
    ;
    
catch [RecognitionException re] {
    reportError(re);
    if (input.LA(1) != Token.EOF) {
        input.consume();
        keyValuePart_return r = keyValuePart();
        retval.tree = r.tree;
    }
}

functionExpression  
	: FUNCTION formalParameters typeReference block
	;
	
newExpression 
	: NEW typeName expressionListOpt
	;
	
objectLiteralPart  
	: localVariableDeclaration    
	| overrideDeclaration	      
	| functionDefinition 	      		
	| objectLiteralInit*
    ;
       	
objectLiteralInit
	: name COLON  boundExpression COMMA?
	;
	
stringExpression  
	: TRANSLATION_KEY
	  	STRING_LITERAL
	  
	| QUOTE_LBRACE_STRING_LITERAL
	  	stringFormat	
	  	expression 	
	  	stringExpressionInner*   
	  RBRACE_QUOTE_STRING_LITERAL
	  
	| TRANSLATION_KEY
		QUOTE_LBRACE_STRING_LITERAL
	  
	  		stringFormat	
	  		expression 	
	  		stringExpressionInner*   
	  		
	  	RBRACE_QUOTE_STRING_LITERAL
	;
	
stringExpressionInner
	: RBRACE_LBRACE_STRING_LITERAL stringFormat expression 			
	;
	
stringFormat  
	: FORMAT_STRING_LITERAL
	| /* no format */
	;
	
bracketExpression  
	: LBRACKET   
		( 	  /*nada*/
	    	| e1=expression 	
		     	(   
		     		  /*nada*/
		     		| COMMA 
		     	   		(   
		     	   			  /*nada*/
	            			| expression 		
		     	         		(COMMA expression)*
	                          	COMMA?
	                    )
	                    
		     		| DOTDOT LT? dd=expression	
		     	    		( STEP st=expression )?
		     	)   
	    )
	  RBRACKET
	;

expressionList
	: expressionItem (COMMA expressionItem)*
	;

expressionItem
	: expression
	|
	;
	
catch [RecognitionException re] {
    reportError(re);
    if (input.LA(1) != Token.EOF) {
        input.consume();
        expressionItem_return r = expressionItem();
        retval.tree = r.tree;
    }
}
expressionListOpt  
	: LPAREN expressionList RPAREN
	|
	;
	
type 
	: typeName cardinality
 	| FUNCTION 
 		LPAREN 
 			typeArgList
		RPAREN 
		
			typeReference 
          	cardinality	//TODO: this introduces an ambiguity: return cardinality vs type cardinality

 	| STAR cardinality
 	;
 	
typeArgList
 	: typeArg (COMMA typeArg)*
// 	| /* emprty list */			-> ^(TYPED_ARG_LIST)
	;

typeArg 
 	: (name? COLON)? type
	|
 	;
 	
catch [RecognitionException re] {
    reportError(re);
    if (input.LA(1) != Token.EOF) {
        input.consume();
        typeArg_return r = typeArg();
        retval.tree = r.tree;
    }
}
typeReference 
 	: COLON type
 	| /*nada*/
 	;
 	
cardinality 
	: (LBRACKET)=>LBRACKET RBRACKET
	|
	;
	
typeName  
	: qualname 		
		(
			  LT genericArgument (COMMA genericArgument)* GT
			|
		)
	;
	
genericArgument 
	: typeName
	| QUES 
		(  
			( 
				  EXTENDS 		
		  		| SUPER		
		  	) 
		 	typeName		
		)?
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
	: name
		( 
			(DOT)=> DOT nn=name
		)*  
	;

name 
	: IDENTIFIER						
	;
