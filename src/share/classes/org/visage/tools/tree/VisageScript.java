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

package org.visage.tools.tree;

import org.visage.api.tree.*;
import org.visage.api.tree.Tree.VisageKind;

import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.code.Scope;
import com.sun.tools.mjavac.code.Symbol.PackageSymbol;
import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Position;
import java.util.Map;
import javax.tools.JavaFileObject;

/**
 * Everything in one source file is kept in a TopLevel structure.
 * @param pid              The tree representing the package clause.
 * @param sourcefile       The source file name.
 * @param defs             All definitions in this file (ClassDef, Import, and Skip)
 * @param packge           The package it belongs to.
 * @param namedImportScope A scope for all named imports.
 * @param starImportScope  A scope for all import-on-demands.
 * @param lineMap          Line starting positions, defined only
 *                         if option -g is set.
 * @param docComments      A hashtable that stores all documentation comments
 *                         indexed by the tree nodes they refer to.
 *                         defined only if option -s is set.
 * @param endPositions     A hashtable that stores ending positions of source
 *                         ranges indexed by the tree nodes they belong to.
 *                         Defined only if option -Xjcov is set.
 */
public class VisageScript extends VisageTree implements UnitTree {

    public final VisageExpression pid;
    public List<VisageTree> defs;
    public JavaFileObject sourcefile;
    public PackageSymbol packge;
    public Scope namedImportScope;
    public Scope starImportScope;
    public Scope scriptScope;
    public long flags;
    public boolean isLibrary = false;
    public boolean isRunnable = false;
    public boolean isEntered;
    public Position.LineMap lineMap = null;
    public Map<JCTree, String> docComments = null;
    public Map<JCTree, Integer> endPositions = null;
    public VisageClassDeclaration scriptLevelClass = null;

    protected VisageScript(
            VisageExpression pid,
            List<VisageTree> defs,
            JavaFileObject sourcefile,
            PackageSymbol packge,
            Scope namedImportScope,
            Scope starImportScope) {
        this.pid = pid;
        this.defs = defs;
        this.sourcefile = sourcefile;
        this.packge = packge;
        this.namedImportScope = namedImportScope;
        this.starImportScope = starImportScope;
    }

    @Override
    public void accept(VisageVisitor v) {
        v.visitScript(this);
    }

    public VisageKind getVisageKind() {
        return VisageKind.COMPILATION_UNIT;
    }

    public List<VisageImport> getImports() {
        ListBuffer<VisageImport> imports = new ListBuffer<VisageImport>();
        if (defs != null)
        {
            for (VisageTree tree : defs) {

                // Protect againtst invalid trees
                //
                if (tree == null) break;
                if (tree.getVisageTag() == VisageTag.IMPORT) {
                    imports.append((VisageImport) tree);
                } else {
                    break;
                }
            }
        }
        return imports.toList();
    }

    public VisageExpression getPackageName() {
        return pid;
    }

    public JavaFileObject getSourceFile() {
        return sourcefile;
    }

    public Position.LineMap getLineMap() {
        return lineMap;
    }

    public List<VisageTree> getTypeDecls() {
        List<VisageTree> typeDefs = defs;

        if (defs != null)
        {
            for (; !typeDefs.isEmpty(); typeDefs = typeDefs.tail) {
                if (typeDefs.head.getVisageTag() != VisageTag.IMPORT) {
                    break;
                }
            }
        }
        return typeDefs;
    }

    //@Override
    public <R, D> R accept(VisageTreeVisitor<R, D> v, D d) {
        return v.visitCompilationUnit(this, d);
    }

    @Override
    public VisageTag getVisageTag() {
        return VisageTag.TOPLEVEL;
    }
}
