/*
 * Copyright 1999-2006 Sun Microsystems, Inc.  All Rights Reserved.
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

    /** Pseudo operation: Remove tree
     */
    public static final int PSEUDOREMOVE = LETEXPR + 1;     

    /** Pseudo operation: Synthetic tree
     */
    public static final int PSEUDOSYNTHETIC = PSEUDOREMOVE + 1;     

    /** Pseudo operation: Replace tree
     */
    public static final int PSEUDOREPLACE = PSEUDOSYNTHETIC + 1;     

    /** class declaration
     */
    public static final int CLASSDECL = PSEUDOREPLACE + 1;     

    /** attribute definition
     */
    public static final int ATTRIBUTEDEF = CLASSDECL + 1;     

    /** Operation definition
     */
    public static final int OPERATIONDEF = ATTRIBUTEDEF + 1;     

    /** function definition statement
     */
    public static final int FUNCTIONDEFSTATEMENT = ATTRIBUTEDEF + 1;     

    /** init definition
     */
    public static final int INITDEF = FUNCTIONDEFSTATEMENT + 1;     

    /** any var declaration including formal params
     */
    public static final int VARDECL = INITDEF + 1;        

    /** In object literal  "var: name"
     */
    public static final int VARISOBJECTBEINGINITIALIZED = VARDECL + 1;     
    
    /** In object literal  "attribute: name"
     */
    public static final int SETATTRIBUTETOOBJECTBEINGINITIALIZED = VARISOBJECTBEINGINITIALIZED + 1;     
    
    /** In object literal  "Identifier ':' [ 'bind' 'lazy'?] expression"
     */
    public static final int OBJECTLITERALPART = SETATTRIBUTETOOBJECTBEINGINITIALIZED + 1;     
    
    /** pure object literal 
     */
    public static final int PUREOBJECTLITERAL = OBJECTLITERALPART + 1;     
    
    /** String expression "Hello { world() %s }"
     */
    public static final int STRINGEXPRESSION = PUREOBJECTLITERAL + 1;     

    /** for expression 
     */
    public static final int FOREXPRESSION = STRINGEXPRESSION + 1;     

    /** for expression (x in seq where cond) clause
     */
    public static final int FOREXPRESSIONINCLAUSE = FOREXPRESSION + 1;     

    /** do later statement
     */
    public static final int DOLATER = FOREXPRESSIONINCLAUSE + 1;        

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

    /** insert statement
     */
    public static final int INSERT = SEQUENCE_INDEXED + 1;        

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

    /** internal bind unary operator
     */
    public static final int BINDOP = XOR + 1;        

    /** internal lazy bind unary operator
     */
    public static final int LAZYOP = BINDOP + 1;
    public static final int JFX_OP_LAST = LAZYOP;        
}
