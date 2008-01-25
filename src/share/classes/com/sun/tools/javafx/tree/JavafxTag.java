/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.tree;

import com.sun.tools.javac.tree.JCTree;

/* Just a class to hang JavaFX specific tags on.
 * Never instanciated.
 */
public abstract class JavafxTag extends JCTree {
    
    private JavafxTag() {
    }

    /** class declaration
     */
    public static final int CLASS_DEF = LETEXPR + 1;  

    /** Operation definition
     */
    public static final int FUNCTION_DEF = CLASS_DEF + 1;      

    /** init definition
     */
    public static final int INIT_DEF = FUNCTION_DEF + 1;     

    /** postinit definition
     */
    public static final int POSTINIT_DEF = INIT_DEF + 1;     

    /** any var declaration including formal params
     */
    public static final int VAR_DEF = POSTINIT_DEF + 1;

    /** on change triggers
     */
    public static final int ON_REPLACE = VAR_DEF + 1;        

    /** on change triggers
     */
    public static final int ON_REPLACE_ELEMENT = ON_REPLACE + 1;        

    /** on change triggers
     */
    public static final int ON_INSERT_ELEMENT = ON_REPLACE_ELEMENT + 1;        

    /** on change triggers
     */
    public static final int ON_DELETE_ELEMENT = ON_INSERT_ELEMENT + 1;        

    /** on change triggers
     */
    public static final int ON_DELETE_ALL = ON_DELETE_ELEMENT + 1;        

    /** In object literal  "var: name"
     */
    public static final int VARISOBJECTBEINGINITIALIZED = ON_DELETE_ALL + 1;     
    
    /** In object literal  "attribute: name"
     */
    public static final int SETATTRIBUTETOOBJECTBEINGINITIALIZED = VARISOBJECTBEINGINITIALIZED + 1;     
    
    /** In object literal  "Identifier ':' [ 'bind' 'lazy'?] expression"
     */
    public static final int OBJECT_LITERAL_PART = SETATTRIBUTETOOBJECTBEINGINITIALIZED + 1;     
    
    /** pure object literal 
     */
    public static final int OBJECT_LITERAL = OBJECT_LITERAL_PART + 1;     
    
    /** String expression "Hello { world() %s }"
     */
    public static final int STRING_EXPRESSION = OBJECT_LITERAL + 1;     

    /** for expression 
     */
    public static final int FOR_EXPRESSION = STRING_EXPRESSION + 1;     

    /** for expression (x in seq where cond) clause
     */
    public static final int FOR_EXPRESSION_IN_CLAUSE = FOR_EXPRESSION + 1;     

    /** do later statement
     */
    public static final int DOLATER = FOR_EXPRESSION_IN_CLAUSE + 1;        

    /** do statement
     */
    public static final int DOBACKGROUND = DOLATER + 1;           

    /** for (unitinterval ...
     */
    public static final int FORALPHA = DOBACKGROUND + 1;           

    /** for ( id in ...
     */
    public static final int FORJOIN = FORALPHA + 1;        

    /** dur ...
     */
    public static final int DURCLAUSE = FORJOIN + 1;        

    /** block expression { ... }
     */
    public static final int BLOCK_EXPRESSION = DURCLAUSE + 1;

    /** explicit sequence [78, 6, 14, 21]
     */
    public static final int SEQUENCE_EXPLICIT = BLOCK_EXPRESSION + 1;        

    /** range sequence [1..100]
     */
    public static final int SEQUENCE_RANGE = SEQUENCE_EXPLICIT + 1;        

    /** empty sequence []
     */
    public static final int SEQUENCE_EMPTY = SEQUENCE_RANGE + 1;        

    /** index into a sequence
     */
    public static final int SEQUENCE_INDEXED = SEQUENCE_EMPTY + 1;        

    /** slice index into a sequence
     */
    public static final int SEQUENCE_SLICE = SEQUENCE_INDEXED + 1;        

    /** insert statement
     */
    public static final int INSERT = SEQUENCE_SLICE + 1;        

    /** delete statement
     */
    public static final int DELETE = INSERT + 1;             

    /** function expression
     */
    public static final int FUNCTIONEXPRESSION = DELETE + 1;        

    /** operation expression
     */
    public static final int OPERATIONEXPRESSION = FUNCTIONEXPRESSION + 1;        

    /** select expression
     */
    public static final int SELECTEXPRESSION = OPERATIONEXPRESSION + 1;        

    /** member selector
     */
    public static final int MEMBERSELECTOR = SELECTEXPRESSION + 1;        

    /** class type
     */
    public static final int TYPECLASS = MEMBERSELECTOR + 1;        

    /** functional type
     */
    public static final int TYPEFUNC = TYPECLASS + 1;        

    /** any type
     */
    public static final int TYPEANY = TYPEFUNC + 1;        

    /** type unspecified
     */
    public static final int TYPEUNKNOWN = TYPEANY + 1;        

    /** xor operator
     */
    public static final int XOR = TYPEUNKNOWN + 1;        
    public static final int JFX_OP_FIRST = XOR;        
    
    /** sizeof operator
     */
    public static final int SIZEOF = XOR + 1;

    /** internal bind unary operator
     */
    public static final int BINDOP = SIZEOF + 1;        

    /** internal lazy bind unary operator
     */
    public static final int LAZYOP = BINDOP + 1;

    /** The 'indexof name' operator.
     */
    public static final int INDEXOF = LAZYOP + 1;

    /** time literal
     */
    public static final int TIME_LITERAL = INDEXOF + 1;
    public static final int JFX_OP_LAST = TIME_LITERAL;        
}
