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

import com.sun.tools.javafx.tree.*;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.util.Context;
import static com.sun.tools.javafx.code.JavafxFlags.*;

/**
 *
 * @author Robert Field
 */
public class JavafxVarUsageAnalysis extends JavafxTreeScanner {
    protected static final Context.Key<JavafxVarUsageAnalysis> varUsageKey =
            new Context.Key<JavafxVarUsageAnalysis>();
    
    private final JavafxTypeMorpher typeMorpher;
    private boolean inLHS;
    private boolean inInitBlock;
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
        inInitBlock = false;
        inBindContext = false;
    }
    
    public void analyzeVarUse(JavafxEnv<JavafxAttrContext> attrEnv) {
        scan(attrEnv.tree);
    }

    private void mark(Symbol sym, long flag) {
        sym.flags_field |= flag;
    }
    
    private void markVarAccess(Symbol sym) {
        if (sym instanceof VarSymbol) {
            if (inBindContext) {
                mark(sym, VARUSE_USED_IN_BIND);
            } else {
                if (inLHS) {
                    // note the assignment the assignment
                    if (inInitBlock) {
                        mark(sym, VARUSE_INIT_ASSIGNED_TO);
                    } else {
                        mark(sym, VARUSE_ASSIGNED_TO);
                    }
                }
                if ((sym.flags_field & VARUSE_TMP_IN_INIT_EXPR) != 0) {
                    // this is a reference to this variable from within its own initializer
                    mark(sym, VARUSE_SELF_REFERENCE);
                }
            }
        }
    }

    private void markDefinition(Symbol sym) {
        if (inBindContext) {
            mark(sym, VARUSE_BOUND_INIT);
        }
    }

    @Override
    public void visitScript(JFXScript tree) {
       inInitBlock = false;
       inLHS = false;
       inBindContext = false;

       super.visitScript(tree);
    }

    @Override
    public void visitVarScriptInit(JFXVarScriptInit tree) {
    }

    @Override
    public void visitVar(JFXVar tree) {
        // any changes here should also go into visitOverrideClassVar
        boolean wasInBindContext = inBindContext;
        inBindContext |= tree.isBound();
        markDefinition(tree.sym);
        tree.sym.flags_field |= VARUSE_TMP_IN_INIT_EXPR;
        scan(tree.getInitializer());
        tree.sym.flags_field &= ~VARUSE_TMP_IN_INIT_EXPR;
        inBindContext = wasInBindContext;
        if (tree.getOnReplace() != null) {
            mark(tree.sym, VARUSE_HAS_ON_REPLACE);
            scan(tree.getOnReplace());
        }
    }
    
    @Override
    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        boolean wasInBindContext = inBindContext;
        inBindContext |= tree.isBound();
        markDefinition(tree.sym);
        mark(tree.sym, VARUSE_OVERRIDDEN);
        tree.sym.flags_field |= VARUSE_TMP_IN_INIT_EXPR;
        scan(tree.getInitializer());
        tree.sym.flags_field &= ~VARUSE_TMP_IN_INIT_EXPR;
        inBindContext = wasInBindContext;
        if (tree.getOnReplace() != null) {
            mark(tree.sym, VARUSE_HAS_ON_REPLACE);
            scan(tree.getOnReplace());
        }
    }

    @Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
       // these start over in a class definition
       boolean wasLHS = inLHS;
       boolean wasInBindContext = inBindContext;
       boolean wasInInitBlock = inInitBlock;
       inInitBlock = false;
       inLHS = false;
       inBindContext = false;

       super.visitClassDeclaration(tree);

       inInitBlock = wasInInitBlock;
       inBindContext = wasInBindContext;
       inLHS = wasLHS;
    }

    @Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
       // these start over in a function definition
       boolean wasLHS = inLHS;
       boolean wasInBindContext = inBindContext;
       boolean wasInInitBlock = inInitBlock;
       inInitBlock = false;
       inLHS = false;

       inBindContext = tree.isBound();
       super.visitFunctionDefinition(tree);

       inInitBlock = wasInInitBlock;
       inBindContext = wasInBindContext;
       inLHS = wasLHS;
    }
   
    @Override
    public void visitFunctionInvocation(JFXFunctionInvocation tree) {
        super.visitFunctionInvocation(tree);
        Symbol msym = expressionSymbol(tree.meth);
        if (msym != null && msym instanceof MethodSymbol && (msym.flags_field & FUNC_IS_INITIALIZED) != 0) {
            Symbol asym = expressionSymbol(tree.args.head);
            asym.flags_field |= VARUSE_IS_INITIALIZED_USED;
        }
    }

    @Override
    public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        boolean wasInBindContext = inBindContext;

        // bind doesn't permiate object literals, but...
        // Locations are needed for updating bound object literals
        inBindContext |= tree.isBound();
        markDefinition(tree.sym);
        mark(tree.sym, VARUSE_OBJ_LIT_INIT);
        scan(tree.getExpression());

        inBindContext = wasInBindContext;
    }

    @Override
    public void visitInstanciate(JFXInstanciate tree) {
        super.visitInstanciate(tree);

        // Local vars of an object literal are effectively an inner accessed
        for (JFXVar var : tree.getLocalvars()) {
            mark(var.sym, VARUSE_INNER_ACCESS);
        }
    }

    @Override
    public void visitAssign(JFXAssign tree) {
        boolean wasLHS = inLHS;
        inLHS = true;
        scan(tree.lhs);
        inLHS = wasLHS;
        scan(tree.rhs);
    }

    @Override
    public void visitAssignop(JFXAssignOp tree) {
        boolean wasLHS = inLHS;
        inLHS = true;
        scan(tree.lhs);
        inLHS = wasLHS;
        scan(tree.rhs);
    }

    @Override
    public void visitUnary(JFXUnary tree) {
        boolean wasLHS = inLHS;
        switch (tree.getFXTag()) {
            case PREINC:
            case PREDEC:
            case POSTINC:
            case POSTDEC:
                inLHS = true;
                break;
        }
        scan(tree.arg);
        inLHS = wasLHS;
    }

    @Override
    public void visitIdent(JFXIdent tree) {
        markVarAccess(tree.sym);
    }
    
    @Override
    public void visitInitDefinition(JFXInitDefinition that) {
        assert !inInitBlock : "cannot have nested init blocks without intervening class";
        assert !inLHS : "cannot have init blocks on LHS";
        assert !inBindContext : "cannot have init blocks on bind";

        inInitBlock = true;
        scan((JFXBlock)that.getBody());

        inInitBlock = false;
    }

    @Override
    public void visitInterpolateValue(final JFXInterpolateValue tree) {
        boolean wasInBindContext = inBindContext;
        inBindContext = true;

        super.visitInterpolateValue(tree);

        inBindContext = wasInBindContext;
    }

    @Override
    public void visitSelect(JFXSelect tree) {
        // this may or may not be in a LHS but in either
        // event the selector is a value expression
        boolean wasLHS = inLHS;
        inLHS = false;
        scan(tree.selected);
        inLHS = wasLHS;
        
        markVarAccess(tree.sym);
    }

    @Override
    public void visitSequenceIndexed(JFXSequenceIndexed tree) {
        boolean wasLHS = inLHS;
        inLHS = false;
        super.visitSequenceIndexed(tree);
        inLHS = wasLHS;
    }

    @Override
    public void visitSequenceSlice(JFXSequenceSlice tree) {
        boolean wasLHS = inLHS;
        inLHS = false;
        super.visitSequenceSlice(tree);
        inLHS = wasLHS;
    }

    //TODO: cloned from JavafxTranslationSupport, common locaion is needed
    private Symbol expressionSymbol(JFXExpression tree) {
        switch (tree.getFXTag()) {
            case IDENT:
                return ((JFXIdent) tree).sym;
            case SELECT:
                return ((JFXSelect) tree).sym;
            default:
                return null;
        }
    }

}
