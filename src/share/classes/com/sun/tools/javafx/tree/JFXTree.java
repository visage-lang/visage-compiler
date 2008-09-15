/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.tree;

import com.sun.javafx.api.tree.*;
import com.sun.javafx.api.tree.Tree.JavaFXKind;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import com.sun.tools.javac.util.List;

//TODO for now we need this or classes like DiagnosticPosition are unhappy
import com.sun.tools.javac.tree.JCTree;
import com.sun.source.tree.TreeVisitor;



/**
 * The base of the JavaFX AST
 * well... except for things like statement which (at least for now) have to be subclassed
 * off other parts of the JCTree.
 */
public abstract class JFXTree extends JCTree implements Tree, Cloneable, DiagnosticPosition {
    
    /* The tag of this node -- one of the constants declared above.
     */
    public int getTag() {
        throw new RuntimeException("bad call to getTag() - class: " + getClass() + ", FX tag: " + getFXTag());
    }

    /* The tag of this node -- one of the constants declared above.
     */
    public abstract JavafxTag getFXTag();

    /** Set position field and return this tree.
     */
    @Override
    public JFXTree setPos(int pos) {
        this.pos = pos;
        return this;
    }

    /** Set type field and return this tree.
     */
    public JFXTree setType(Type type) {
        this.type = type;
        return this;
    }

    /** Get a default position for this tree node.
     */
    public DiagnosticPosition pos() {
        return this;
    }

    // for default DiagnosticPosition
    public JFXTree getTree() {
        return this;
    }

    public int getStartPosition() {
        return JavafxTreeInfo.getStartPos(this);
    }

    public int getEndPosition(Map<JCTree, Integer> endPosTable) {
        return JavafxTreeInfo.getEndPos(this, endPosTable);
    }

    // for default DiagnosticPosition
    public int getPreferredPosition() {
        return pos;
    }

    /** Initialize tree with given tag.
     */
    protected JFXTree() {
    }
    
    public abstract void accept(JavafxVisitor v);
    
    /**
     * Gets the JavaFX kind of this tree.
     *
     * @return the kind of this tree.
     */
    public abstract JavaFXKind getJavaFXKind();
    
    @SuppressWarnings("unchecked")
    public static <T> java.util.List<T> convertList(Class<T> klass, com.sun.tools.javac.util.List<?> list) {
	if (list == null)
	    return null;
	for (Object o : list)
	    klass.cast(o);
        return (java.util.List<T>)list;
    }

    /** Convert a tree to a pretty-printed string. */
    @Override
    public String toString() {
        StringWriter s = new StringWriter();
        try {
            new JavafxPretty(s, false).printExpr(this);
        }
        catch (IOException e) {
            // should never happen, because StringWriter is defined
            // never to throw any IOExceptions
            throw new AssertionError(e);
        }
        return s.toString();
    }
    
    /**
     * Was this tree expected, but missing, and filled-in by the parser
     */
    public boolean isMissing() {
        return false;
    }
    
    /**
     * Allow all nodes to become equivalent to Erronous by being abel to
     * return any Erroneous error nodes they are holding (default they don't have any).
     */
    public List<? extends JFXTree> getErrorTrees() {
        return null;
    }
    /****
     * Make JCTree happy
     */
    
    public Kind getKind() {
        throw new UnsupportedOperationException("Use getJavaFXKind() not getKind() - class: " + getClass() + ", FX tag: " + getFXTag());
    }

    public void accept(Visitor v) {
        throw new UnsupportedOperationException("We don't use visitors from JCTree - class: " + getClass() + ", FX tag: " + getFXTag());
    }

    public <R,D> R accept(TreeVisitor<R,D> v, D d) {
        throw new UnsupportedOperationException("We don't use visitors from JCTree - class: " + getClass() + ", FX tag: " + getFXTag());
    }

}
