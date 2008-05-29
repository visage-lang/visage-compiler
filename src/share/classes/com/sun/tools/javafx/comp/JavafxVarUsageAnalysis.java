/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;

/**
 *
 * @author Robert Field
 */
public class JavafxVarUsageAnalysis extends JavafxTreeScanner {
    protected static final Context.Key<JavafxVarUsageAnalysis> varUsageKey =
            new Context.Key<JavafxVarUsageAnalysis>();
    
    private final JavafxTypeMorpher typeMorpher;
    private boolean inLHS;
    private boolean inBindContext;
    
    public static JavafxVarUsageAnalysis instance(Context context) {
        JavafxVarUsageAnalysis instance = context.get(varUsageKey);
        if (instance == null)
            instance = new JavafxVarUsageAnalysis(context);
        return instance;
    }
    
    JavafxVarUsageAnalysis(Context context) {
        context.put(varUsageKey, this);
        
        this.typeMorpher = JavafxTypeMorpher.instance(context);
        inLHS = false;
        inBindContext = false;
    }
    
    public void analyzeVarUse(JavafxEnv<JavafxAttrContext> attrEnv) {
        scan(attrEnv.tree);
    }
    
    private void markVarUse(Symbol sym) {
        if (sym instanceof VarSymbol) {
            VarSymbol vsym = (VarSymbol)sym;
            VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
            if (inBindContext) {
                if (inLHS) {
                    // ??? assignment in function / bind expression
                } else {
                    vmi.markBoundTo();
                }
            } else {
                if (inLHS) {
                    vmi.markAssignedTo();
                }
            }
        }
    }
    
    @Override
    public void visitVar(JFXVar tree) {
        boolean wasInBindContext = inBindContext;
        inBindContext |= tree.isBound();
        scan(tree.getInitializer());
        markVarUse(tree.sym);
        inBindContext = wasInBindContext;
        //TODO: need global handling of 'on replace' in bind context -- probably disallowed
        scan(tree.getOnReplace());
    }
    
   @Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        boolean wasInBindContext = inBindContext;
        inBindContext = false; //TODO should reset, but if bound, should mark as such (once there is only one)
        scan(tree.getFunctionValue());
        inBindContext = wasInBindContext;
    }
   
    @Override
    public void visitBindExpression(JFXBindExpression tree) {
        boolean wasInBindContext = inBindContext;
        inBindContext |= tree.getBindStatus().isBound();
        tree.getExpression().accept(this);
        inBindContext = wasInBindContext;
    }

    @Override
    public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        boolean wasInBindContext = inBindContext;
        inBindContext |= tree.isBound();
        markVarUse(tree.sym);
        tree.getExpression().accept(this);
        inBindContext = wasInBindContext;
    }

    @Override
    public void visitAssign(JCAssign tree) {
        boolean wasLHS = inLHS;
        inLHS = true;
        scan(tree.lhs);
        inLHS = wasLHS;
        scan(tree.rhs);
    }

    @Override
    public void visitIdent(JCIdent tree) {
        markVarUse(tree.sym);
    }
    
    @Override
    public void visitSelect(JCFieldAccess tree) {
        // this may or may not be in a LHS but in either
        // event the selector is a value expression
        boolean wasLHS = inLHS;
        inLHS = false;
        scan(tree.selected);
        inLHS = wasLHS;
        
        markVarUse(tree.sym);
    }
    
    @Override
    public void visitBlockExpression(JFXBlockExpression tree) {
        scan(tree.stats);
        scan(tree.value);
    }
}
