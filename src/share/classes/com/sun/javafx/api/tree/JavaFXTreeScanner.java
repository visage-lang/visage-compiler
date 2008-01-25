/*
 * Copyright 2005-2006 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.source.tree.*;
import com.sun.source.util.TreeScanner;

/**
 * A TreeVisitor that visits all the child tree nodes.
 * To visit nodes of a particular type, just override the
 * corresponding visitXYZ method.
 * Inside your method, call super.visitXYZ to visit descendant
 * nodes.  This class extends the TreeScanner class to add support
 * for JavaFX Script tree nodes.
 * 
 * @see com.sun.source.util.TreeScanner
 * @author Tom Ball
 */
public class JavaFXTreeScanner<R,P> extends TreeScanner<R,P> implements JavaFXTreeVisitor<R,P> {

    private R scanAndReduce(Tree node, P p, R r) {
        return reduce(scan(node, p), r);
    }

    private R scanAndReduce(Iterable<? extends Tree> nodes, P p, R r) {
        return reduce(scan(nodes, p), r);
    }

/* ***************************************************************************
 * Visitor methods
 ****************************************************************************/

    public R visitBlockExpression(BlockExpressionTree node, P p) {
        R r = scan(node.getStatements(), p);
        return scanAndReduce(node.getValue(), p, r);
    }

    public R visitClassDeclaration(ClassDeclarationTree node, P p) {
        R r = scan(node.getModifiers(), p);
        r = scanAndReduce(node.getImplements(), p, r);
        r = scanAndReduce(node.getSupertypeList(), p, r);
        r = scanAndReduce(node.getExtends(), p, r);
        return scanAndReduce(node.getClassMembers(), p, r);
    }

    public R visitDoLater(DoLaterTree node, P p) {
        return scan(node.getBody(), p);
    }

    public R visitForExpression(ForExpressionTree node, P p) {
        R r = scan(node.getInClauses(), p);
        return scanAndReduce(node.getBodyExpression(), p, r);
    }

    public R visitForExpressionInClause(ForExpressionInClauseTree node, P p) {
        R r = scan(node.getVariable(), p);
        r = scanAndReduce(node.getSequenceExpression(), p, r);
        return scanAndReduce(node.getWhereExpression(), p, r);
    }
    
    public R visitIndexof(IndexofTree node, P p) {
       return null;
    }

    public R visitInitDefinition(InitDefinitionTree node, P p) {
        return scan(node.getBody(), p);
    }

    public R visitPostInitDefinition(InitDefinitionTree node, P p) {
        return scan(node.getBody(), p);
    }

    public R visitInstantiate(InstantiateTree node, P p) {
        R r = scan(node.getArguments(), p);
        r = scanAndReduce(node.getClassBody(), p, r);
        r = scanAndReduce(node.getIdentifier(), p, r);
        return scanAndReduce(node.getLiteralParts(), p, r);
    }

    public R visitMemberSelector(MemberSelectorTree node, P p) {
        return null;
    }

    public R visitObjectLiteralPart(ObjectLiteralPartTree node, P p) {
        return scan(node.getExpression(), p);
    }

    public R visitOnDeleteAll(OnDeleteAllTree node, P p) {
        R r = scan(node.getOldValue(), p);
        return scanAndReduce(node.getBody(), p, r);
    }

    public R visitOnDeleteElement(OnDeleteElementTree node, P p) {
        R r = scan(node.getIndex(), p);
        r = scanAndReduce(node.getOldValue(), p, r);
        return scanAndReduce(node.getBody(), p, r);
    }

    public R visitOnInsertElement(OnInsertElementTree node, P p) {
        R r = scan(node.getIndex(), p);
        r = scanAndReduce(node.getOldValue(), p, r);
        return scanAndReduce(node.getBody(), p, r);
    }

    public R visitOnReplace(OnReplaceTree node, P p) {
        R r = scan(node.getOldValue(), p);
        return scanAndReduce(node.getBody(), p, r);
    }

    public R visitOnReplaceElement(OnReplaceElementTree node, P p) {
        R r = scan(node.getIndex(), p);
        r = scanAndReduce(node.getOldValue(), p, r);
        return scanAndReduce(node.getBody(), p, r);
    }

    public R visitOperationDefinition(OperationDefinitionTree node, P p) {
        R r = scan(node.getModifiers(), p);
        return scanAndReduce(node.getOperationValue(), p, r);
    }

    public R visitOperationValue(OperationValueTree node, P p) {
        R r = scan(node.getType(), p);
        r = scanAndReduce(node.getParameters(), p, r);
        return scanAndReduce(node.getBodyExpression(), p, r);
    }

    public R visitSequenceDelete(SequenceDeleteTree node, P p) {
        R r = scan(node.getSequence(), p);
        r = scanAndReduce(node.getElement(), p, r);
        return scanAndReduce(node.getIndex(), p, r);
    }

    public R visitSequenceEmpty(SequenceEmptyTree node, P p) {
        return null;
    }

    public R visitSequenceExplicit(SequenceExplicitTree node, P p) {
        return scan(node.getItemList(), p);
    }

    public R visitSequenceIndexed(SequenceIndexedTree node, P p) {
        R r = scan(node.getSequence(), p);
        return scanAndReduce(node.getIndex(), p, r);
    }

    public R visitSequenceSlice(SequenceSliceTree node, P p) {
        R r = scan(node.getSequence(), p);
        r = scanAndReduce(node.getFirstIndex(), p, r);
        return scanAndReduce(node.getLastIndex(), p, r);
    }

    public R visitSequenceInsert(SequenceInsertTree node, P p) {
        R r = scan(node.getSequence(), p);
        return scanAndReduce(node.getElement(), p, r);
    }

    public R visitSequenceRange(SequenceRangeTree node, P p) {
        R r = scan(node.getLower(), p);
        r = scanAndReduce(node.getUpper(), p, r);
        return scanAndReduce(node.getStepOrNull(), p, r);
    }

    public R visitSetAttributeToObject(SetAttributeToObjectTree node, P p) {
        return null;
    }

    public R visitStringExpression(StringExpressionTree node, P p) {
        return scan(node.getPartList(), p);
    }

    public R visitTypeAny(TypeAnyTree node, P p) {
        return null;
    }

    public R visitTypeClass(TypeClassTree node, P p) {
        return scan(node.getClassName(), p);
    }

    public R visitTypeFunctional(TypeFunctionalTree node, P p) {
        R r = scan(node.getReturnType(), p);
        return scanAndReduce(node.getParameters(), p, r);
    }

    public R visitTypeUnknown(TypeUnknownTree node, P p) {
        return null;
    }

    public R visitTimeLiteral(TimeLiteralTree node, P p) {
        return null;
    }
}
