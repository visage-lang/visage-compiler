/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.comp;

import com.sun.tools.mjavac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javafx.tree.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** A class for environments, instances of which are passed as
 *  arguments to tree visitors.  Environments refer to important ancestors
 *  of the subtree that's currently visited, such as the enclosing method,
 *  the enclosing class, or the enclosing toplevel node. They also contain
 *  a generic component, represented as a type parameter, to carry further
 *  information specific to individual passes.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class JavafxEnv<A> implements Iterable<JavafxEnv<A>> {

    /** The next enclosing environment.
     */
    public JavafxEnv<A> next;

    /** The environment enclosing the current class.
     */
    public JavafxEnv<A> outer;

    /** The tree with which this environment is associated.
     */
    public JFXTree tree;

    /** The enclosing toplevel tree.
     */
    public JFXScript toplevel;

    /** The translated toplevel tree.
     */
    public JCCompilationUnit translatedToplevel;

    /** The next enclosing class definition.
     */
    public JFXClassDeclaration enclClass;

    /** The next enclosing method definition.
     */
    public JFXFunctionDefinition enclFunction;

    /** Location info for debugging
     */
    public JFXTree where;

    /** A generic field for further information.
     */
    public A info;

    /** Is this an environment for evaluating a base clause?
     */
    public boolean baseClause = false;

    /** Create an outermost environment for a given (toplevel)tree,
     *  with a given info field.
     */
    public JavafxEnv(JFXTree tree, A info) {
	this.next = null;
	this.outer = null;
	this.tree = tree;
	this.toplevel = null;
	this.enclClass = null;
	this.enclFunction = null;
	this.info = info;
    }

    /** Duplicate this environment, updating with given tree and info,
     *  and copying all other fields.
     */
    public JavafxEnv<A> dup(JFXTree tree, A info) {
	return dupto(new JavafxEnv<A>(tree, info));
    }

    /** Duplicate this environment into a given Environment,
     *  using its tree and info, and copying all other fields.
     */
    public JavafxEnv<A> dupto(JavafxEnv<A> that) {
	that.next = this;
	that.outer = this.outer;
	that.toplevel = this.toplevel;
 	that.enclClass = this.enclClass;
	that.enclFunction = this.enclFunction;
	return that;
    }

    /** Duplicate this environment, updating with given tree,
     *  and copying all other fields.
     */
    public JavafxEnv<A> dup(JFXTree tree) {
	return dup(tree, this.info);
    }

    /** Return closest enclosing environment which points to a tree with given tag.
     */
    public JavafxEnv<A> enclosing(JavafxTag tag) {
	JavafxEnv<A> env1 = this;
	while (env1 != null && env1.tree.getFXTag() != tag) env1 = env1.next;
	return env1;
    }
    
    @Override
    public String toString() {
        return "JavafxEnv[" + info + (outer == null ? "" : ",outer=" + outer) + "]";
    }

    public Iterator<JavafxEnv<A>> iterator() {
        return new Iterator<JavafxEnv<A>>() {
            JavafxEnv<A> next = JavafxEnv.this;
            public boolean hasNext() {
                return next.outer != null;
            }
            public JavafxEnv<A> next() {
                if (hasNext()) {
                    JavafxEnv<A> current = next;
                    next = current.outer;
                    return current;
                }
                throw new NoSuchElementException();

            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
