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

import com.sun.source.tree.Tree;

/**
 * Common interface for all nodes in an abstract syntax tree for the 
 * JavaFX Script language.
 *
 * <p><b>WARNING:</b> This interface and its sub-interfaces are 
 * subject to change as the JavaFX Script programming language evolves.
 * These interfaces are implemented by Sun's JavaFX Script compiler (javafxc) 
 * and should not be implemented either directly or indirectly by 
 * other applications.
 *
 * @author Tom Ball
 */
public interface JavaFXTree extends Tree {
                
    /**
     * Enumerates all kinds of trees.
     */
    public enum JavaFXKind {

        /**
         * Used for instances of {@link BlockExpressionTree}.
         */
        BLOCK_EXPRESSION(BlockExpressionTree.class),

        /**
         * Used for instances of {@link ClassDeclarationTree}.
         */
        CLASS_DECLARATION(ClassDeclarationTree.class),

        /**
         * Used for instances of {@link DoLaterTree}.
         */
        DO_LATER(DoLaterTree.class),

        /**
         * Used for instances of {@link ForExpressionTree}.
         */
        FOR_EXPRESSION(ForExpressionTree.class),

        /**
         * Used for instances of {@link ForExpressionInClauseTree}.
         */
        FOR_EXPRESSION_IN_CLAUSE(ForExpressionInClauseTree.class),

        /**
         * Used for instances of {@link InitDefinitionTree}.
         */
        INIT_DEFINITION(InitDefinitionTree.class),

        /**
         * Used for instances of {@link PostInitDefinitionTree}.
         */
        POSTINIT_DEFINITION(InitDefinitionTree.class),

        /**
         * Used for instances of {@link InstatiateTree}.
         */
        INSTANTIATE(InstantiateTree.class),

        /**
         * Used for instances of {@link MemberSelectorTree}.
         */
        MEMBER_SELECTOR(MemberSelectorTree.class),

        /**
         * Used for instances of {@link ObjectLiteralPartTree}.
         */
        OBJECT_LITERAL_PART(ObjectLiteralPartTree.class),

        /**
         * Used for instances of {@link OnDeleteAllTree}.
         */
        ON_DELETE_ALL(OnDeleteAllTree.class),

        /**
         * Used for instances of {@link OnDeleteElementTree}.
         */
        ON_DELETE_ELEMENT(OnDeleteElementTree.class),

        /**
         * Used for instances of {@link OnInsertElementTree}.
         */
        ON_INSERT_ELEMENT(OnInsertElementTree.class),

        /**
         * Used for instances of {@link OnReplaceTree}.
         */
        ON_REPLACE(OnReplaceTree.class),

        /**
         * Used for instances of {@link OnReplaceElementTree}.
         */
        ON_REPLACE_ELEMENT(OnReplaceElementTree.class),

        /**
         * Used for instances of {@link OperatonDefinitionTree}.
         */
        OPERATION_DEFINITION(OperationDefinitionTree.class),

        /**
         * Used for instances of {@link OperationValueTree}.
         */
        OPERATION_VALUE(OperationValueTree.class),

        /**
         * Used for instances of {@link SequenceDeleteTree}.
         */
        SEQUENCE_DELETE(SequenceDeleteTree.class),

        /**
         * Used for instances of {@link SequenceEmptyTree}.
         */
        SEQUENCE_EMPTY(SequenceEmptyTree.class),

        /**
         * Used for instances of {@link SequenceExplicitTree}.
         */
        SEQUENCE_EXPLICIT(SequenceExplicitTree.class),

        /**
         * Used for instances of {@link SequenceIndexedTree}.
         */
        SEQUENCE_INDEXED(SequenceIndexedTree.class),

         /**
         * Used for instances of {@link SequenceSliceTree}.
         */
        SEQUENCE_SLICE(SequenceSliceTree.class),

        /**
         * Used for instances of {@link SequenceInsertTree}.
         */
        SEQUENCE_INSERT(SequenceInsertTree.class),

        /**
         * Used for instances of {@link SequenceRangeTree}.
         */
        SEQUENCE_RANGE(SequenceRangeTree.class),

        /**
         * Used for instances of {@link SetAttributeToObjectTree}.
         */
        SET_ATTRIBUTE_TO_OBJECT(SetAttributeToObjectTree.class),

        /**
         * Used for instances of {@link StringExpressionTree}.
         */
        STRING_EXPRESSION(StringExpressionTree.class),
        
        /**
         * Used for instances of {@link TimeLiteralTree}.
         */
        TIME_LITERAL(TimeLiteralTree.class),

        /**
         * Used for instances of {@link TypeAnyTree}.
         */
        TYPE_ANY(TypeAnyTree.class),

        /**
         * Used for instances of {@link TypeClassTree}.
         */
        TYPE_CLASS(TypeClassTree.class),

        /**
         * Used for instances of {@link TypeFunctionalTree}.
         */
        TYPE_FUNCTIONAL(TypeFunctionalTree.class),

        /**
         * Used for instances of {@link TypeUnknownTree}.
         */
        TYPE_UNKNOWN(TypeUnknownTree.class);

        JavaFXKind(Class<? extends JavaFXTree> intf) {
            associatedInterface = intf;
        }
                
        public Class<? extends Tree> asInterface() {
            return associatedInterface;
        }
        
        private final Class<? extends Tree> associatedInterface;
    }
    
    /**
     * Gets the com.sun.source.tree.Tree.Kind of this tree.  Because
     * Tree kinds cannot be extended, Kind.OTHER is always returned for
     * any instance of JavaFXTree.  Use <code>getJavaFXKind</code> to
     * get the JavaFX kind.
     *
     * @return Kind.OTHER
     */
    Kind getKind();
    
    /**
     * Gets the JavaFX kind of this tree.
     *
     * @return the kind of this tree.
     */
    JavaFXKind getJavaFXKind();
    
    /**
     * Accept method used to implement the visitor pattern.  The
     * visitor pattern is used to implement operations on trees.
     *
     * @param <R> result type of this operation.
     * @param <D> type of additonal data.
     */
    <R,D> R accept(JavaFXTreeVisitor<R,D> visitor, D data);
}
