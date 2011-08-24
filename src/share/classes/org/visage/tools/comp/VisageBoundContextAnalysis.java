/*
* Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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

import org.visage.api.VisageBindStatus;
import org.visage.tools.tree.*;
import org.visage.tools.util.MsgSym;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.JCDiagnostic;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.Log;

/**
*
* @author Robert Field
*/
public class VisageBoundContextAnalysis extends VisageTreeScanner {

    protected static final Context.Key<VisageBoundContextAnalysis> bindAnalysisKey =
            new Context.Key<VisageBoundContextAnalysis>();

    private final Log log;
    private final JCDiagnostic.Factory diags;

    private VisageBindStatus bindStatus;

    public static VisageBoundContextAnalysis instance(Context context) {
        VisageBoundContextAnalysis instance = context.get(bindAnalysisKey);
        if (instance == null) {
            instance = new VisageBoundContextAnalysis(context);
        }
        return instance;
    }

    VisageBoundContextAnalysis(Context context) {
        context.put(bindAnalysisKey, this);
        log = Log.instance(context);
        diags = JCDiagnostic.Factory.instance(context);

        bindStatus = VisageBindStatus.UNBOUND;
    }

    public void analyzeBindContexts(VisageEnv<VisageAttrContext> attrEnv) {
        scan(attrEnv.tree);
    }

    private void mark(VisageBoundMarkable tree) {
        tree.markBound(bindStatus);
    }

    @Override
    public void visitScript(VisageScript tree) {
        bindStatus = VisageBindStatus.UNBOUND;
        super.visitScript(tree);
    }

    @Override
    public void visitVarInit(VisageVarInit tree) {
    }

    private void analyzeVar(VisageAbstractVar tree) {
        // any changes here should also go into visitOverrideClassVar
        VisageBindStatus prevBindStatus = bindStatus;
        bindStatus = tree.isBound()?
                            tree.getBindStatus() :
                            prevBindStatus;
        mark(tree);
        scan(tree.getInitializer());
        bindStatus = prevBindStatus;
        for (VisageOnReplace.Kind triggerKind : VisageOnReplace.Kind.values()) {
            VisageOnReplace trigger = tree.getTrigger(triggerKind);
            if (trigger != null) {
                if (bindStatus != VisageBindStatus.UNBOUND) {
                    log.error(trigger.pos(), MsgSym.MESSAGE_TRIGGER_IN_BIND_NOT_ALLOWED, triggerKind);
                }
            }
        }
        scan(tree.getOnReplace());
        scan(tree.getOnInvalidate());
    }

    @Override
    public void visitVar(VisageVar tree) {
        analyzeVar(tree);
    }

    @Override
    public void visitOverrideClassVar(VisageOverrideClassVar tree) {
        analyzeVar(tree);
    }

    @Override
    public void visitClassDeclaration(VisageClassDeclaration tree) {
        // these start over in a class definition
        VisageBindStatus prevBindStatus = bindStatus;
        bindStatus = VisageBindStatus.UNBOUND;

        super.visitClassDeclaration(tree);

        bindStatus = prevBindStatus;
    }

    @Override
    public void visitFunctionDefinition(VisageFunctionDefinition tree) {
        // start over in a function definition
        VisageBindStatus prevBindStatus = bindStatus;

        bindStatus = tree.isBound()? VisageBindStatus.UNIDIBIND : VisageBindStatus.UNBOUND;
        // don't use super, since we don't want to cancel the inBindContext
        scan(tree.getParams());
        scan(tree.getBodyExpression());
        bindStatus = prevBindStatus;
    }

    @Override
    public void visitForExpressionInClause(VisageForExpressionInClause tree) {
        mark(tree);
        super.visitForExpressionInClause(tree);
    }

    @Override
    public void visitFunctionValue(VisageFunctionValue tree) {
        // these start over in a function value
        VisageBindStatus prevBindStatus = bindStatus;
        bindStatus = VisageBindStatus.UNBOUND;
        super.visitFunctionValue(tree);
        bindStatus = prevBindStatus;
    }

    @Override
    public void visitObjectLiteralPart(VisageObjectLiteralPart tree) {
        VisageBindStatus prevBindStatus = bindStatus;
        bindStatus = tree.isExplicitlyBound()?
                            tree.getBindStatus() :
                            prevBindStatus;
        tree.markBound(bindStatus);
        scan(tree.getExpression());
        bindStatus = prevBindStatus;
    }

    @Override
    public void visitAssignop(VisageAssignOp tree) {
        if (bindStatus != VisageBindStatus.UNBOUND) {
            log.error(tree.pos(), MsgSym.MESSAGE_VISAGE_NOT_ALLOWED_IN_BIND_CONTEXT, "compound assignment");
        }
        super.visitAssignop(tree);
    }

    @Override
    public void visitAssign(VisageAssign tree) {
        if (bindStatus != VisageBindStatus.UNBOUND) {
            log.error(tree.pos(), MsgSym.MESSAGE_VISAGE_NOT_ALLOWED_IN_BIND_CONTEXT, "=");
        }
        super.visitAssign(tree);
    }

    @Override
    public void visitUnary(VisageUnary tree) {
        mark(tree);
        if (bindStatus != VisageBindStatus.UNBOUND) {
            switch (tree.getVisageTag()) {
                case PREINC:
                case POSTINC:
                    log.error(tree.pos(), MsgSym.MESSAGE_VISAGE_NOT_ALLOWED_IN_BIND_CONTEXT, "++");
                    break;
                case PREDEC:
                case POSTDEC:
                    log.error(tree.pos(), MsgSym.MESSAGE_VISAGE_NOT_ALLOWED_IN_BIND_CONTEXT, "--");
                    break;
            }
        }
        super.visitUnary(tree);
    }

    @Override
    public void visitInterpolateValue(final VisageInterpolateValue tree) {
        VisageBindStatus prevBindStatus = bindStatus;
        bindStatus = VisageBindStatus.UNBOUND;  //TODO: ???
        super.visitInterpolateValue(tree);
        bindStatus = prevBindStatus;
    }

    @Override
    public void visitKeyFrameLiteral(VisageKeyFrameLiteral tree) {
        if (bindStatus != VisageBindStatus.UNBOUND) {
            log.error(tree.pos(),
                    MsgSym.MESSAGE_VISAGE_NOT_ALLOWED_IN_BIND_CONTEXT,
                    diags.fragment(MsgSym.MESSAGE_VISAGE_KEYFRAME_LIT));
        }
        super.visitKeyFrameLiteral(tree);
    }

    @Override
    public void visitTry(VisageTry tree) {
        if (bindStatus != VisageBindStatus.UNBOUND) {
            log.error(tree.pos(),
                    MsgSym.MESSAGE_VISAGE_NOT_ALLOWED_IN_BIND_CONTEXT,
                    diags.fragment(MsgSym.MESSAGE_VISAGE_TRY_CATCH));
        }
        super.visitTry(tree);
    }

    @Override
    public void visitBlockExpression(VisageBlock tree) {
        mark(tree);
        if (bindStatus != VisageBindStatus.UNBOUND) {
            for (List<VisageExpression> l = tree.stats; l.nonEmpty(); l = l.tail) {
                if (l.head.getVisageTag() != VisageTag.VAR_DEF) {
                    log.error(l.head.pos(), MsgSym.MESSAGE_VISAGE_NOT_ALLOWED_IN_BIND_CONTEXT, l.head.toString());
                }
            }
        }
        super.visitBlockExpression(tree);
    }

    @Override
    public void visitIfExpression(VisageIfExpression tree) {
        mark(tree);
        super.visitIfExpression(tree);
    }

    @Override
    public void visitFunctionInvocation(VisageFunctionInvocation tree) {
        mark(tree);
        super.visitFunctionInvocation(tree);
    }

    @Override
    public void visitParens(VisageParens tree) {
        mark(tree);
        super.visitParens(tree);
    }

    @Override
    public void visitBinary(VisageBinary tree) {
        mark(tree);
        super.visitBinary(tree);
    }

    @Override
    public void visitTypeCast(VisageTypeCast tree) {
        mark(tree);
        super.visitTypeCast(tree);
    }

    @Override
    public void visitInstanceOf(VisageInstanceOf tree) {
        mark(tree);
        super.visitInstanceOf(tree);
    }

    @Override
    public void visitSelect(VisageSelect tree) {
        mark(tree);
        super.visitSelect(tree);
    }

    @Override
    public void visitIdent(VisageIdent tree) {
        mark(tree);
        super.visitIdent(tree);
    }

    @Override
    public void visitLiteral(VisageLiteral tree) {
        mark(tree);
        super.visitLiteral(tree);
    }

    @Override
    public void visitSequenceEmpty(VisageSequenceEmpty tree) {
        mark(tree);
        super.visitSequenceEmpty(tree);
    }

    @Override
    public void visitSequenceRange(VisageSequenceRange tree) {
        mark(tree);
        super.visitSequenceRange(tree);
    }

    @Override
    public void visitSequenceExplicit(VisageSequenceExplicit tree) {
        mark(tree);
        super.visitSequenceExplicit(tree);
    }

    @Override
    public void visitSequenceIndexed(VisageSequenceIndexed tree) {
        mark(tree);
        super.visitSequenceIndexed(tree);
    }

    @Override
    public void visitSequenceSlice(VisageSequenceSlice tree) {
        mark(tree);
        super.visitSequenceSlice(tree);
    }

    @Override
    public void visitStringExpression(VisageStringExpression tree) {
        mark(tree);
        super.visitStringExpression(tree);
    }

    @Override
    public void visitInstanciate(VisageInstanciate tree) {
        mark(tree);
        super.visitInstanciate(tree);
    }

    @Override
    public void visitForExpression(VisageForExpression tree) {
        mark(tree);
        super.visitForExpression(tree);
    }

    @Override
    public void visitIndexof(VisageIndexof tree) {
        mark(tree);
        super.visitIndexof(tree);
    }

    @Override
    public void visitTimeLiteral(VisageTimeLiteral tree) {
        mark(tree);
        super.visitTimeLiteral(tree);
    }

    @Override
    public void visitLengthLiteral(VisageLengthLiteral tree) {
        mark(tree);
        super.visitLengthLiteral(tree);
    }

    @Override
    public void visitAngleLiteral(VisageAngleLiteral tree) {
        mark(tree);
        super.visitAngleLiteral(tree);
    }

    @Override
    public void visitColorLiteral(VisageColorLiteral tree) {
        mark(tree);
        super.visitColorLiteral(tree);
    }
}

