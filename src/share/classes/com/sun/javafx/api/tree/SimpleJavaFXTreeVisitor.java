/*
 * Copyright 2005 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.api.tree;

import com.sun.source.util.SimpleTreeVisitor;

/**
 * A simple visitor for JavaFX tree nodes.
 *
 * @author Tom Ball
 */
public class SimpleJavaFXTreeVisitor <R,P> extends SimpleTreeVisitor<R, P> implements JavaFXTreeVisitor<R,P> {

    public R visitBindExpression(BindExpressionTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitBlockExpression(BlockExpressionTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitClassDeclaration(ClassDeclarationTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitForExpression(ForExpressionTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitIndexof(IndexofTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitForExpressionInClause(ForExpressionInClauseTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitInitDefinition(InitDefinitionTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitPostInitDefinition(InitDefinitionTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitInstantiate(InstantiateTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitMemberSelector(MemberSelectorTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitObjectLiteralPart(ObjectLiteralPartTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitTrigger(TriggerTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitOnReplace(OnReplaceTree node, P p) {
        return defaultAction(node, p);
    }
    
    public R visitFunctionDefinition(FunctionDefinitionTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitFunctionValue(FunctionValueTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitSequenceDelete(SequenceDeleteTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitSequenceEmpty(SequenceEmptyTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitSequenceExplicit(SequenceExplicitTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitSequenceIndexed(SequenceIndexedTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitSequenceSlice(SequenceSliceTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitSequenceInsert(SequenceInsertTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitSequenceRange(SequenceRangeTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitSetAttributeToObject(SetAttributeToObjectTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitStringExpression(StringExpressionTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitTypeAny(TypeAnyTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitTypeClass(TypeClassTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitTypeFunctional(TypeFunctionalTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitTypeUnknown(TypeUnknownTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitTimeLiteral(TimeLiteralTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitInterpolate(InterpolateTree node, P p) {
        return defaultAction(node, p);
    }

    public R visitInterpolateValue(InterpolateValueTree node, P p) {
        return defaultAction(node, p);
    }
}
