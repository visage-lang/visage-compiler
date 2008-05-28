/*
 * Copyright 2005-2006 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.api.tree;

import com.sun.source.tree.TreeVisitor;

/**
 * A visitor of JavaFX Script trees, which extends the TreeVisitor in 
 * the javac Compiler API.
 *
 * <p> Classes implementing this interface may or may not throw a
 * {@code NullPointerException} if the additional parameter {@code p}
 * is {@code null}; see documentation of the implementing class for
 * details.
 * 
 * <p> <b>WARNING:</b> It is possible that methods will be added to
 * this interface to accommodate new, currently unknown, language
 * structures added to future versions of the Java&trade; programming
 * language.  Therefore, visitor classes directly implementing this
 * interface may be source incompatible with future versions of the
 * platform.
 *
 * @param <R> the return type of this visitor's methods.  Use {@link
 * 	      Void} for visitors that do not need to return results.
 * @param <P> the type of the additional parameter to this visitor's
 *            methods.  Use {@code Void} for visitors that do not need an
 *            additional parameter.
 *
 * @author Tom Ball
 */
public interface JavaFXTreeVisitor<R,P> extends TreeVisitor<R,P> {
    R visitBlockExpression(BlockExpressionTree node, P p);
    R visitBindExpression(BindExpressionTree node, P p);
    R visitClassDeclaration(ClassDeclarationTree node, P p);
    R visitForExpression(ForExpressionTree node, P p);
    R visitForExpressionInClause(ForExpressionInClauseTree node, P p);
    R visitInitDefinition(InitDefinitionTree node, P p);
    R visitInterpolate(InterpolateTree node, P p);
    R visitInterpolateValue(InterpolateValueTree node, P p);
    R visitIndexof(IndexofTree node, P p);
    R visitInstantiate(InstantiateTree node, P p);
    R visitKeyFrameLiteral(KeyFrameLiteralTree node, P p);
    R visitObjectLiteralPart(ObjectLiteralPartTree node, P p);
    R visitOnReplace(OnReplaceTree node, P p);
    R visitFunctionDefinition(FunctionDefinitionTree node, P p);
    R visitFunctionValue(FunctionValueTree node, P p);
    R visitPostInitDefinition(InitDefinitionTree node, P p);
    R visitSequenceDelete(SequenceDeleteTree node, P p);
    R visitSequenceEmpty(SequenceEmptyTree node, P p);
    R visitSequenceExplicit(SequenceExplicitTree node, P p);
    R visitSequenceIndexed(SequenceIndexedTree node, P p);
    R visitSequenceSlice(SequenceSliceTree node, P p);
    R visitSequenceInsert(SequenceInsertTree node, P p);
    R visitSequenceRange(SequenceRangeTree node, P p);
    R visitSetAttributeToObject(SetAttributeToObjectTree node, P p);
    R visitStringExpression(StringExpressionTree node, P p);
    R visitTimeLiteral(TimeLiteralTree node, P p);
    R visitTrigger(TriggerTree node, P p);
    R visitTypeAny(TypeAnyTree node, P p);
    R visitTypeClass(TypeClassTree node, P p);
    R visitTypeFunctional(TypeFunctionalTree node, P p);
    R visitTypeUnknown(TypeUnknownTree node, P p);
    R visitVariable(JavaFXVariableTree node, P p);
}
