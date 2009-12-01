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

import com.sun.tools.javafx.tree.*;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.util.Context;
import static com.sun.tools.javafx.code.JavafxFlags.*;

/**
 *
 * @author Robert Field
 */
public class JavafxVarUsageAnalysis extends JavafxTreeScanner {
    protected static final Context.Key<JavafxVarUsageAnalysis> varUsageKey =
            new Context.Key<JavafxVarUsageAnalysis>();
    
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
        
        inLHS = false;
        inBindContext = false;
    }
    
    public void analyzeVarUse(JavafxEnv<JavafxAttrContext> attrEnv) {
        new ClearOldMarks().scan(attrEnv.tree);
        scan(attrEnv.tree);
    }

    private class ClearOldMarks extends JavafxTreeScanner {

        private long ALL_MARKED_VARUSE =
                VARUSE_ASSIGNED_TO |
                VARUSE_TMP_IN_INIT_EXPR |
                VARUSE_DEFINITION_SEEN |
                VARUSE_FORWARD_REFERENCE |
                VARUSE_SELF_REFERENCE |
                VARUSE_OPT_TRIGGER |
                VARUSE_HAS_TRIGGER |
                VARUSE_OBJ_LIT_INIT;

        private void clearMark(Symbol sym) {
            sym.flags_field &= ~ALL_MARKED_VARUSE;
        }

        @Override
        public void visitVar(JFXVar tree) {
            clearMark(tree.sym);
        }

        @Override
        public void visitIdent(JFXIdent tree) {
            if (tree.sym instanceof VarSymbol)
                clearMark(tree.sym);
        }

        @Override
        public void visitOverrideClassVar(JFXOverrideClassVar tree) {
            clearMark(tree.sym);
        }

        @Override
        public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
            clearMark(tree.sym);
        }

        @Override
        public void visitInterpolateValue(final JFXInterpolateValue tree) {
            clearMark(tree.sym);
        }
    }

    private void mark(Symbol sym, long flag) {
        sym.flags_field |= flag;
    }

    private void markVarAccess(Symbol sym) {
        if (sym instanceof VarSymbol) {
            if (inBindContext) {
                mark(sym, VARUSE_NEED_ACCESSOR);
            } else {
                if (inLHS) {
                    // note the assignment the assignment
                    mark(sym, VARUSE_ASSIGNED_TO);
                }
            }
            if ((sym.flags_field & VARUSE_DEFINITION_SEEN) == 0L) {
                // this is a reference to this variable for which we have not yet seen a definition
                mark(sym, VARUSE_FORWARD_REFERENCE);
                mark(sym, VARUSE_NEED_ACCESSOR);
            }
            if ((sym.flags_field & VARUSE_TMP_IN_INIT_EXPR) != 0L) {
                // this is a reference to this variable from within its own initializer
                mark(sym, VARUSE_SELF_REFERENCE);
            }
            sym.flags_field &= ~VARUSE_OPT_TRIGGER;
        }
    }

    @Override
    public void visitScript(JFXScript tree) {
       inLHS = false;
       inBindContext = false;

       super.visitScript(tree);
    }

    @Override
    public void visitVarInit(JFXVarInit tree) {
    }

    private void scanVar(JFXAbstractVar tree) {
        boolean wasInBindContext = inBindContext;
        if (tree.isBound()) {
            mark(tree.sym, VARUSE_NEED_ACCESSOR);
            inBindContext = true;
        }
        if (tree.getInitializer() != null) {
            tree.sym.flags_field |= VARUSE_TMP_IN_INIT_EXPR;
            scan(tree.getInitializer());
            tree.sym.flags_field &= ~VARUSE_TMP_IN_INIT_EXPR;
        }
        mark(tree.sym, VARUSE_DEFINITION_SEEN);
        inBindContext = wasInBindContext;
        if (tree.getOnReplace() != null) {
            mark(tree.sym, VARUSE_HAS_TRIGGER);
            mark(tree.sym, VARUSE_NEED_ACCESSOR);
            scan(tree.getOnReplace());
        }
        if (tree.getOnInvalidate() != null) {
            mark(tree.sym, VARUSE_HAS_TRIGGER);
            mark(tree.sym, VARUSE_NEED_ACCESSOR);
            scan(tree.getOnInvalidate());
        }
    }

    @Override
    public void visitVar(JFXVar tree) {
        scanVar(tree);
    }
    
    @Override
    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        scanVar(tree);
    }

    @Override
    public void visitOnReplace(JFXOnReplace tree) {
        if (tree.getOldValue() != null)
            mark(tree.getOldValue().sym, VARUSE_OPT_TRIGGER);
        if (tree.getNewElements() != null)
            mark(tree.getNewElements().sym, VARUSE_OPT_TRIGGER);
        super.visitOnReplace(tree);
    }

    @Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
       // these start over in a class definition
       boolean wasLHS = inLHS;
       boolean wasInBindContext = inBindContext;
       inLHS = false;
       inBindContext = false;

       super.visitClassDeclaration(tree);

       inBindContext = wasInBindContext;
       inLHS = wasLHS;
    }

    @Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
       // these start over in a function definition
       boolean wasLHS = inLHS;
       boolean wasInBindContext = inBindContext;
       inLHS = false;

       inBindContext = tree.isBound();
        // don't use super, since we don't want to cancel the inBindContext
        for (JFXVar param : tree.getParams()) {
            scan(param);
        }
        scan(tree.getBodyExpression());

       inBindContext = wasInBindContext;
       inLHS = wasLHS;
    }
   
    @Override
    public void visitFunctionValue(JFXFunctionValue tree) {
       // these start over in a function value
       boolean wasLHS = inLHS;
       boolean wasInBindContext = inBindContext;
       inLHS = false;
       inBindContext = false;

       super.visitFunctionValue(tree);

       inBindContext = wasInBindContext;
       inLHS = wasLHS;
    }

    @Override
    public void visitFunctionInvocation(JFXFunctionInvocation tree) {
        super.visitFunctionInvocation(tree);
    }

    @Override
    public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        boolean wasInBindContext = inBindContext;

        inBindContext |= tree.isBound();
        mark(tree.sym, VARUSE_OBJ_LIT_INIT);
        scan(tree.getExpression());

        inBindContext = wasInBindContext;
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
        Symbol sym = null;
        boolean restoreOptTrigger = false;
        switch (tree.getFXTag()) {
            case PREINC:
            case PREDEC:
            case POSTINC:
            case POSTDEC:
                inLHS = true;
                break;
            case SIZEOF:
               if (tree.arg instanceof JFXIdent) {
                   sym = ((JFXIdent) tree.arg).sym;
                   restoreOptTrigger = (sym.flags_field & VARUSE_OPT_TRIGGER) != 0;
               }
        }
        scan(tree.arg);
        if (restoreOptTrigger) {
             sym.flags_field |= VARUSE_OPT_TRIGGER;
        }
        inLHS = wasLHS;
    }

    @Override
    public void visitIdent(JFXIdent tree) {
        markVarAccess(tree.sym);
    }
    
    @Override
    public void visitInitDefinition(JFXInitDefinition that) {
        assert !inLHS : "cannot have init blocks on LHS";
        assert !inBindContext : "cannot have init blocks on bind";

        scan((JFXBlock)that.getBody());

        that.sym.owner.flags_field |= CLASS_HAS_INIT_BLOCK;
    }

    @Override
    public void visitInterpolateValue(final JFXInterpolateValue tree) {
        boolean wasInBindContext = inBindContext;
        inBindContext = true;

        mark(tree.sym, VARUSE_OBJ_LIT_INIT);
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
    public void visitSequenceInsert(JFXSequenceInsert tree) {
        boolean wasLHS = inLHS;
        inLHS = true;
        scan(tree.getSequence());
        inLHS = wasLHS;
        scan(tree.getElement());
    }

    @Override
    public void visitSequenceDelete(JFXSequenceDelete tree) {
        boolean wasLHS = inLHS;
        inLHS = true;
        scan(tree.getSequence());
        inLHS = wasLHS;
        scan(tree.getElement());
    }

    @Override
    public void visitSequenceIndexed(JFXSequenceIndexed tree) {
        Symbol sym;
        boolean restoreOptTrigger;
        if (tree.getSequence() instanceof JFXIdent) {
            sym = ((JFXIdent) tree.getSequence()).sym;
            restoreOptTrigger = (sym.flags_field & VARUSE_OPT_TRIGGER) != 0;
        }
        else {
            sym = null;
            restoreOptTrigger = false;
        }
        scan(tree.getSequence());
        if (restoreOptTrigger)
            sym.flags_field |= VARUSE_OPT_TRIGGER;
        boolean wasLHS = inLHS;
        inLHS = false;
        scan(tree.getIndex());
        inLHS = wasLHS;
    }

    @Override
    public void visitSequenceSlice(JFXSequenceSlice tree) {
        scan(tree.getSequence());
        boolean wasLHS = inLHS;
        inLHS = false;
        scan(tree.getFirstIndex());
        scan(tree.getLastIndex());
        inLHS = wasLHS;
    }

    @Override
    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        scan(that.getVar());
        Symbol sym = null;
        boolean restoreOptTrigger = false;
        JFXExpression seq = that.getSequenceExpression();
        if (seq instanceof JFXIdent) {
            sym = ((JFXIdent) seq).sym;
            restoreOptTrigger = (sym.flags_field & VARUSE_OPT_TRIGGER) != 0;
        }
        else if (seq instanceof JFXSequenceSlice) {
            JFXSequenceSlice slice = (JFXSequenceSlice) seq;
            JFXExpression sseq = slice.getSequence();
            if (sseq instanceof JFXIdent) {
                sym = ((JFXIdent) sseq).sym;
                restoreOptTrigger = (sym.flags_field & VARUSE_OPT_TRIGGER) != 0;
            }
        }
        scan(seq);
        if (restoreOptTrigger)
            sym.flags_field |= VARUSE_OPT_TRIGGER;
        scan(that.getWhereExpression());
    }

    JFXOnReplace findOnReplace(Symbol sym, JFXOnReplace current) {
        //for
        return null;
    }

}
