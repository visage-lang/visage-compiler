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

package org.visage.tools.comp;

import org.visage.tools.tree.*;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.util.Context;
import static org.visage.tools.code.VisageFlags.*;
import org.visage.api.VisageBindStatus;
import org.visage.tools.code.VisageTypes;
import org.visage.tools.code.VisageVarSymbol;
import org.visage.tools.comp.VisageCheck.ForwardReferenceChecker;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.mjavac.util.Name;
import java.util.EnumSet;

/**
 *
 * @author Robert Field
 */
public class VisageVarUsageAnalysis extends VisageTreeScanner {
    protected static final Context.Key<VisageVarUsageAnalysis> varUsageKey =
            new Context.Key<VisageVarUsageAnalysis>();
    
    private boolean inLHS;
    private VisageBindStatus bindStatus;
    private VisageClassDeclaration currentClass;
    private VisageTypes types;
    private Name.Table names;
    private VisageDefs defs;
    
    public static VisageVarUsageAnalysis instance(Context context) {
        VisageVarUsageAnalysis instance = context.get(varUsageKey);
        if (instance == null)
            instance = new VisageVarUsageAnalysis(context);
        return instance;
    }
    
    VisageVarUsageAnalysis(Context context) {
        context.put(varUsageKey, this);
        names = Name.Table.instance(context);
        defs = VisageDefs.instance(context);
        types = VisageTypes.instance(context);
        inLHS = false;
        bindStatus = VisageBindStatus.UNBOUND;
    }
    
    public void analyzeVarUse(VisageEnv<VisageAttrContext> attrEnv) {
        //TODO: if cleared, must be at script-level: new ClearOldMarks().scan(attrEnv.tree);
        scan(attrEnv.tree);
    }

    // Clear flags for vars defined in this script
    private class ClearOldMarks extends VisageTreeScanner {

        private long ALL_MARKED_VARUSE =
                VARUSE_BOUND_INIT |
                VARUSE_TMP_IN_INIT_EXPR |
                VARUSE_FORWARD_REFERENCE |
                VARUSE_SELF_REFERENCE |
                VARUSE_OBJ_LIT_INIT;

        private void clearMark(Symbol sym) {
            sym.flags_field &= ~ALL_MARKED_VARUSE;
        }

        @Override
        public void visitVar(VisageVar tree) {
            super.visitVar(tree);
            clearMark(tree.sym);
        }

        @Override
        public void visitOverrideClassVar(VisageOverrideClassVar tree) {
            super.visitOverrideClassVar(tree);
            clearMark(tree.sym);
        }
    }

    private void mark(Symbol sym, long flag) {
        sym.flags_field |= flag;
    }

    private void markVarAccess(Symbol sym) {
        if (sym instanceof VarSymbol && (sym.flags_field & VARUSE_SPECIAL) == 0L) {
            if (bindStatus.isBound()) {
                mark(sym, VARUSE_BIND_ACCESS);
                // Accessors are necessary to send notification.
                mark(sym, VARUSE_NEED_ACCESSOR);
                if (bindStatus.isBidiBind())
                    mark(sym, VARUSE_ASSIGNED_TO);
            } else {
                if (inLHS) {
                    // note the assignment the assignment
                    mark(sym, VARUSE_ASSIGNED_TO);
                }
            }
            
            sym.flags_field &= ~VARUSE_OPT_TRIGGER;
            
            checkExternallySeen(sym);
        }
        if (sym instanceof VisageVarSymbol) {
            ((VisageVarSymbol)sym).setUsedOutsideSizeof();
        }
    }
    
    private void checkExternallySeen(Symbol sym) {
        if (sym instanceof VisageVarSymbol && sym.owner != currentClass.sym) {
            ((VisageVarSymbol)sym).setIsExternallySeen();
        }
    }

    @Override
    public void visitScript(VisageScript tree) {
       inLHS = false;
       bindStatus = VisageBindStatus.UNBOUND;
       super.visitScript(tree);
    }

    @Override
    public void visitVarInit(VisageVarInit tree) {
        Symbol sym = tree.getVar().sym;
        if (sym instanceof VisageVarSymbol) {
            ((VisageVarSymbol)sym).setHasVarInit();
        }
    }

    private void scanVar(VisageAbstractVar tree) {
        VisageBindStatus wasBindStatus = bindStatus;
        bindStatus = tree.getBindStatus();
        if (bindStatus.isBound()) {
            mark(tree.sym, VARUSE_BOUND_INIT);
            mark(tree.sym, VARUSE_NEED_ACCESSOR);
        }
        if (tree.getInitializer() != null) {
            tree.sym.flags_field |= VARUSE_TMP_IN_INIT_EXPR;
            scan(tree.getInitializer());
            tree.sym.flags_field &= ~VARUSE_TMP_IN_INIT_EXPR;
            if (!tree.isLiteralInit()) {
                mark(tree.sym, VARUSE_NON_LITERAL);
            }
        }
        bindStatus = wasBindStatus;
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
        
        checkExternallySeen(tree.sym);
    }

    @Override
    public void visitVar(VisageVar tree) {
        scanVar(tree);
    }
    
    @Override
    public void visitVarRef(VisageVarRef tree) {
        mark(tree.getVarSymbol(), VARUSE_NEED_ACCESSOR | VARUSE_VARREF);
    }

    @Override
    public void visitOverrideClassVar(VisageOverrideClassVar tree) {
        scanVar(tree);
        mark(tree.sym, OVERRIDE);
    }

    @Override
    public void visitOnReplace(VisageOnReplace tree) {
        if (tree.getOldValue() != null)
            mark(tree.getOldValue().sym, VARUSE_OPT_TRIGGER);
        if (tree.getNewElements() != null)
            mark(tree.getNewElements().sym, VARUSE_OPT_TRIGGER);
        super.visitOnReplace(tree);
    }

    @Override
    public void visitClassDeclaration(VisageClassDeclaration tree) {
       VisageClassDeclaration previousClass = currentClass;
       currentClass = tree;
       // these start over in a class definition
       boolean wasLHS = inLHS;
       VisageBindStatus wasBindStatus = bindStatus;
       inLHS = false;
       bindStatus = VisageBindStatus.UNBOUND;

       super.visitClassDeclaration(tree);
       if (tree.isScriptClass) {
           markForwardReferences(tree);
       }

       bindStatus = wasBindStatus;
       inLHS = wasLHS;
       currentClass = previousClass;
    }

    @Override
    public void visitFunctionDefinition(VisageFunctionDefinition tree) {
       // these start over in a function definition
       boolean wasLHS = inLHS;
       VisageBindStatus wasBindStatus = bindStatus;
       inLHS = false;

       bindStatus = tree.getBindStatus();
       // don't use super, since we don't want to cancel the inBindContext
       for (VisageVar param : tree.getParams()) {
           scan(param);
       }
       scan(tree.getBodyExpression());

       bindStatus = wasBindStatus;
       inLHS = wasLHS;
    }
   
    @Override
    public void visitFunctionValue(VisageFunctionValue tree) {
       // these start over in a function value
       boolean wasLHS = inLHS;
       VisageBindStatus wasBindStatus = bindStatus;
       inLHS = false;
       bindStatus = VisageBindStatus.UNBOUND;

       super.visitFunctionValue(tree);

       bindStatus = wasBindStatus;
       inLHS = wasLHS;
    }

    @Override
    public void visitFunctionInvocation(VisageFunctionInvocation tree) {
        super.visitFunctionInvocation(tree);
    }

    @Override
    public void visitObjectLiteralPart(VisageObjectLiteralPart tree) {
        VisageBindStatus wasBindStatus = bindStatus;

        bindStatus = tree.getBindStatus();
        mark(tree.sym, VARUSE_OBJ_LIT_INIT);
        scan(tree.getExpression());

        checkExternallySeen(tree.sym);
        
        bindStatus = wasBindStatus;
    }

    @Override
    public void visitAssign(VisageAssign tree) {
        boolean wasLHS = inLHS;
        inLHS = true;
        scan(tree.lhs);
        inLHS = wasLHS;
        scan(tree.rhs);
    }

    @Override
    public void visitAssignop(VisageAssignOp tree) {
        boolean wasLHS = inLHS;
        inLHS = true;
        scan(tree.lhs);
        inLHS = wasLHS;
        scan(tree.rhs);
    }

    @Override
    public void visitUnary(VisageUnary tree) {
        boolean wasLHS = inLHS;
        VisageVarSymbol sym = null;
        boolean restoreOptTrigger = false;
        boolean restoreUsedOutsideSizeof = false;
        switch (tree.getVisageTag()) {
            case PREINC:
            case PREDEC:
            case POSTINC:
            case POSTDEC:
                inLHS = true;
                break;
            case SIZEOF:
               if (tree.arg instanceof VisageIdent) {
                   sym = (VisageVarSymbol) ((VisageIdent) tree.arg).sym;
                   restoreOptTrigger = (sym.flags_field & VARUSE_OPT_TRIGGER) != 0;
                   restoreUsedOutsideSizeof = ! sym.isUsedOutsideSizeof();
                   sym.setUsedInSizeof();
               }
        }
        scan(tree.arg);
        if (restoreOptTrigger) {
             sym.flags_field |= VARUSE_OPT_TRIGGER;
        }
        if (restoreUsedOutsideSizeof)
            sym.clearUsedOutsideSizeof();
        inLHS = wasLHS;
    }

    @Override
    public void visitIdent(VisageIdent tree) {
        markVarAccess(tree.sym);
    }
    
    @Override
    public void visitInitDefinition(VisageInitDefinition that) {
        assert !inLHS : "cannot have init blocks on LHS";
        assert !bindStatus.isBound() : "cannot have init blocks on bind";

        scan((VisageBlock)that.getBody());

        that.sym.owner.flags_field |= CLASS_HAS_INIT_BLOCK;
    }

    @Override
    public void visitInterpolateValue(final VisageInterpolateValue tree) {
        VisageBindStatus wasBindStatus = bindStatus;
        bindStatus = VisageBindStatus.UNIDIBIND;

        mark(tree.sym, VARUSE_OBJ_LIT_INIT);
        super.visitInterpolateValue(tree);

        bindStatus = wasBindStatus;
    }

    @Override
    public void visitSelect(VisageSelect tree) {
        // this may or may not be in a LHS but in either
        // event the selector is a value expression
        boolean wasLHS = inLHS;
        inLHS = false;
        scan(tree.selected);
        inLHS = wasLHS;
        
        markVarAccess(tree.sym);
    }

    @Override
    public void visitSequenceInsert(VisageSequenceInsert tree) {
        boolean wasLHS = inLHS;
        inLHS = true;
        scan(tree.getSequence());
        inLHS = wasLHS;
        scan(tree.getElement());
    }

    @Override
    public void visitSequenceDelete(VisageSequenceDelete tree) {
        boolean wasLHS = inLHS;
        inLHS = true;
        scan(tree.getSequence());
        inLHS = wasLHS;
        scan(tree.getElement());
    }

    @Override
    public void visitSequenceIndexed(VisageSequenceIndexed tree) {
        Symbol sym;
        boolean restoreOptTrigger;
        if (tree.getSequence() instanceof VisageIdent) {
            sym = ((VisageIdent) tree.getSequence()).sym;
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
    public void visitSequenceSlice(VisageSequenceSlice tree) {
        scan(tree.getSequence());
        boolean wasLHS = inLHS;
        inLHS = false;
        scan(tree.getFirstIndex());
        scan(tree.getLastIndex());
        inLHS = wasLHS;
    }

    @Override
    public void visitForExpressionInClause(VisageForExpressionInClause that) {
        scan(that.getVar());
        if (bindStatus.isBound()) {
            mark(that.getVar().sym, VARUSE_ASSIGNED_TO);
        }
        Symbol sym = null;
        boolean restoreOptTrigger = false;
        VisageExpression seq = that.getSequenceExpression();
        if (seq instanceof VisageIdent) {
            sym = ((VisageIdent) seq).sym;
            restoreOptTrigger = (sym.flags_field & VARUSE_OPT_TRIGGER) != 0;
        }
        else if (seq instanceof VisageSequenceSlice) {
            VisageSequenceSlice slice = (VisageSequenceSlice) seq;
            VisageExpression sseq = slice.getSequence();
            if (sseq instanceof VisageIdent) {
                sym = ((VisageIdent) sseq).sym;
                restoreOptTrigger = (sym.flags_field & VARUSE_OPT_TRIGGER) != 0;
            }
        }
        scan(seq);
        if (restoreOptTrigger)
            sym.flags_field |= VARUSE_OPT_TRIGGER;
        scan(that.getWhereExpression());
    }

    VisageOnReplace findOnReplace(Symbol sym, VisageOnReplace current) {
        //for
        return null;
    }

    void markForwardReferences(VisageTree tree) {
        new ForwardReferenceChecker(names, types, defs, EnumSet.allOf(ForwardReferenceChecker.ScopeKind.class)) {
            @Override
            protected void reportForwardReference(DiagnosticPosition pos, boolean selfReference, Symbol s, boolean potential) {
                if (selfReference) {
                    mark(s, VARUSE_SELF_REFERENCE);
                }
                else {
                    mark(s, VARUSE_FORWARD_REFERENCE);
                    mark(s, VARUSE_NEED_ACCESSOR);
                }
            }
        }.scan(tree);
    }
}
