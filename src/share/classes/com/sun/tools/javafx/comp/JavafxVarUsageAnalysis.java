/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.comp;

import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.code.*;

/**
 *
 * @author Robert Field
 */
public class JavafxVarUsageAnalysis extends TreeScanner {
    private boolean inLHS = false;
    private boolean inBindContext = false;
    
    JavafxVarUsageAnalysis() { }
    
    private void markVarUse(JavafxVarSymbol vsym) {
        if (inBindContext) {
            if (inLHS) {
                // ??? assignment in function / bind expression
            } else {
                vsym.markBoundTo();
            }
        } else {
            if (inLHS) {
                vsym.markAssignedTo();
            }
        }
    }
    
    @Override
    public void visitVarDef(JCVariableDecl tree) {
        scan(tree.mods);
        if (tree instanceof JavafxJCVarDecl) {
            JavafxJCVarDecl var = (JavafxJCVarDecl)tree;
            boolean wasInBindContext = inBindContext;
            inBindContext |= var.isBound();
            scan(tree.init);
            inBindContext = wasInBindContext;
        } else {
            scan(tree.init);
        }
    }
    
    @Override
    public void visitAssign(JCAssign tree) {
        inLHS = true;
        scan(tree.lhs);
        inLHS = false;
        boolean wasInBindContext = inBindContext;
        inBindContext = false;
        if (tree instanceof JavafxJCAssign) {
            JavafxJCAssign assign = (JavafxJCAssign)tree;
            inBindContext = assign.isBound();
        }
        scan(tree.rhs);
        inBindContext = wasInBindContext;
    }
    
    @Override
    public void visitAssignop(JCAssignOp tree) {
        inLHS = true;
        scan(tree.lhs);
        inLHS = false;
        scan(tree.rhs);
    }
    
    @Override
    public void visitIdent(JCIdent tree) {
        if (tree.sym instanceof JavafxVarSymbol) {
            markVarUse((JavafxVarSymbol)tree.sym);
        }
    }
    
    @Override
    public void visitSelect(JCFieldAccess tree) {
        // this may or may not be in a LHS but in either
        // event the selector is a value expression
        boolean wasLHS = inLHS;
        inLHS = false;
        scan(tree.selected);
        inLHS = wasLHS;
        
        if (tree.sym instanceof JavafxVarSymbol) {
            markVarUse((JavafxVarSymbol)tree.sym);
        }
    }
}
