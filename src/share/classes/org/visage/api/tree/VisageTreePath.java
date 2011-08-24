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

package org.visage.api.tree;

import java.util.Iterator;

/**
 * A path of tree nodes, typically used to represent the sequence of ancestor
 * nodes of a tree node up to the top level UnitTree node.
 *
 * @author Jonathan Gibbons
 * @since 1.6
 */
public class VisageTreePath implements Iterable<Tree> {
    /**
     * Gets a tree path for a tree node within a compilation unit.
     * @return null if the node is not found
     */
    public static VisageTreePath getPath(UnitTree unit, Tree target) {
        return getPath(new VisageTreePath(unit), target);
    }

    /**
     * Gets a tree path for a tree node within a subtree identified by a TreePath object.
     * @return null if the node is not found
     */
    public static VisageTreePath getPath(VisageTreePath path, Tree target) {
        path.getClass();
        target.getClass();

        class Result extends Error {
            static final long serialVersionUID = -5942088234594905625L;
            VisageTreePath path;
            Result(VisageTreePath path) {
                this.path = path;
            }
        }
        class PathFinder extends VisageTreePathScanner<VisageTreePath,Tree> {
            @Override
            public VisageTreePath scan(Tree tree, Tree target) {
                if (tree == target)
                    throw new Result(new VisageTreePath(getCurrentPath(), target));
                return super.scan(tree, target);
            }
        }

        try {
            new PathFinder().scan(path, target);
        } catch (Result result) {
            return result.path;
        }
        return null;
    }

    /**
     * Creates a TreePath for a root node.
     */
    public VisageTreePath(UnitTree t) {
        this(null, t);
    }

    /**
     * Creates a TreePath for a child node.
     */
    public VisageTreePath(VisageTreePath p, Tree t) {
        if (t.getJavaFXKind() == Tree.VisageKind.COMPILATION_UNIT) {
            compilationUnit = (UnitTree) t;
            parent = null;
        }
        else {
            compilationUnit = p.compilationUnit;
            parent = p;
        }
        leaf = t;
    }
    /**
     * Get the compilation unit associated with this path.
     */
    public UnitTree getCompilationUnit() {
        return compilationUnit;
    }

    /**
     * Get the leaf node for this path.
     */
    public Tree getLeaf() {
        return leaf;
    }

    /**
     * Get the path for the enclosing node, or null if there is no enclosing node.
     */
    public VisageTreePath getParentPath() {
        return parent;
    }

    public Iterator<Tree> iterator() {
        return new Iterator<Tree>() {
            public boolean hasNext() {
                return curr.parent != null;
            }

            public Tree next() {
                curr = curr.parent;
                return curr.leaf;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

            private VisageTreePath curr;
        };
    }

    private UnitTree compilationUnit;
    private Tree leaf;
    private VisageTreePath parent;
}
