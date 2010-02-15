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

package com.sun.tools.javafx.comp;

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.util.MsgSym;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.JCDiagnostic;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.Log;

/**
*
* @author Robert Field
*/
public class JavafxBoundContextAnalysis extends JavafxTreeScanner {

    protected static final Context.Key<JavafxBoundContextAnalysis> bindAnalysisKey =
            new Context.Key<JavafxBoundContextAnalysis>();

    private final Log log;
    private final JCDiagnostic.Factory diags;

    private JavafxBindStatus bindStatus;

    public static JavafxBoundContextAnalysis instance(Context context) {
        JavafxBoundContextAnalysis instance = context.get(bindAnalysisKey);
        if (instance == null) {
            instance = new JavafxBoundContextAnalysis(context);
        }
        return instance;
    }

    JavafxBoundContextAnalysis(Context context) {
        context.put(bindAnalysisKey, this);
        log = Log.instance(context);
        diags = JCDiagnostic.Factory.instance(context);

        bindStatus = JavafxBindStatus.UNBOUND;
    }

    public void analyzeBindContexts(JavafxEnv<JavafxAttrContext> attrEnv) {
        scan(attrEnv.tree);
    }

    private void mark(JFXBoundMarkable tree) {
        tree.markBound(bindStatus);
    }

    @Override
    public void visitScript(JFXScript tree) {
        bindStatus = JavafxBindStatus.UNBOUND;
        super.visitScript(tree);
    }

    @Override
    public void visitVarInit(JFXVarInit tree) {
    }

    private void analyzeVar(JFXAbstractVar tree) {
        // any changes here should also go into visitOverrideClassVar
        JavafxBindStatus prevBindStatus = bindStatus;
        bindStatus = tree.isBound()?
                            tree.getBindStatus() :
                            prevBindStatus;
        mark(tree);
        scan(tree.getInitializer());
        bindStatus = prevBindStatus;
        for (JFXOnReplace.Kind triggerKind : JFXOnReplace.Kind.values()) {
            JFXOnReplace trigger = tree.getTrigger(triggerKind);
            if (trigger != null) {
                if (bindStatus != JavafxBindStatus.UNBOUND) {
                    log.error(trigger.pos(), MsgSym.MESSAGE_TRIGGER_IN_BIND_NOT_ALLOWED, triggerKind);
                }
            }
        }
        scan(tree.getOnReplace());
        scan(tree.getOnInvalidate());
    }

    @Override
    public void visitVar(JFXVar tree) {
        analyzeVar(tree);
    }

    @Override
    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        analyzeVar(tree);
    }

    @Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
        // these start over in a class definition
        JavafxBindStatus prevBindStatus = bindStatus;
        bindStatus = JavafxBindStatus.UNBOUND;

        super.visitClassDeclaration(tree);

        bindStatus = prevBindStatus;
    }

    @Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        // start over in a function definition
        JavafxBindStatus prevBindStatus = bindStatus;

        bindStatus = tree.isBound()? JavafxBindStatus.UNIDIBIND : JavafxBindStatus.UNBOUND;
        // don't use super, since we don't want to cancel the inBindContext
        scan(tree.getParams());
        scan(tree.getBodyExpression());
        bindStatus = prevBindStatus;
    }

    @Override
    public void visitForExpressionInClause(JFXForExpressionInClause tree) {
        mark(tree);
        super.visitForExpressionInClause(tree);
    }

    @Override
    public void visitFunctionValue(JFXFunctionValue tree) {
        // these start over in a function value
        JavafxBindStatus prevBindStatus = bindStatus;
        bindStatus = JavafxBindStatus.UNBOUND;
        super.visitFunctionValue(tree);
        bindStatus = prevBindStatus;
    }

    @Override
    public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        JavafxBindStatus prevBindStatus = bindStatus;
        bindStatus = tree.isExplicitlyBound()?
                            tree.getBindStatus() :
                            prevBindStatus;
        tree.markBound(bindStatus);
        scan(tree.getExpression());
        bindStatus = prevBindStatus;
    }

    @Override
    public void visitAssignop(JFXAssignOp tree) {
        if (bindStatus != JavafxBindStatus.UNBOUND) {
            log.error(tree.pos(), MsgSym.MESSAGE_JAVAFX_NOT_ALLOWED_IN_BIND_CONTEXT, "compound assignment");
        }
        super.visitAssignop(tree);
    }

    @Override
    public void visitAssign(JFXAssign tree) {
        if (bindStatus != JavafxBindStatus.UNBOUND) {
            log.error(tree.pos(), MsgSym.MESSAGE_JAVAFX_NOT_ALLOWED_IN_BIND_CONTEXT, "=");
        }
        super.visitAssign(tree);
    }

    @Override
    public void visitUnary(JFXUnary tree) {
        mark(tree);
        if (bindStatus != JavafxBindStatus.UNBOUND) {
            switch (tree.getFXTag()) {
                case PREINC:
                case POSTINC:
                    log.error(tree.pos(), MsgSym.MESSAGE_JAVAFX_NOT_ALLOWED_IN_BIND_CONTEXT, "++");
                    break;
                case PREDEC:
                case POSTDEC:
                    log.error(tree.pos(), MsgSym.MESSAGE_JAVAFX_NOT_ALLOWED_IN_BIND_CONTEXT, "--");
                    break;
            }
        }
        super.visitUnary(tree);
    }

    @Override
    public void visitInterpolateValue(final JFXInterpolateValue tree) {
        JavafxBindStatus prevBindStatus = bindStatus;
        bindStatus = JavafxBindStatus.UNBOUND;  //TODO: ???
        super.visitInterpolateValue(tree);
        bindStatus = prevBindStatus;
    }

    @Override
    public void visitKeyFrameLiteral(JFXKeyFrameLiteral tree) {
        if (bindStatus != JavafxBindStatus.UNBOUND) {
            log.error(tree.pos(),
                    MsgSym.MESSAGE_JAVAFX_NOT_ALLOWED_IN_BIND_CONTEXT,
                    diags.fragment(MsgSym.MESSAGE_JAVAFX_KEYFRAME_LIT));
        }
        super.visitKeyFrameLiteral(tree);
    }

    @Override
    public void visitTry(JFXTry tree) {
        if (bindStatus != JavafxBindStatus.UNBOUND) {
            log.error(tree.pos(),
                    MsgSym.MESSAGE_JAVAFX_NOT_ALLOWED_IN_BIND_CONTEXT,
                    diags.fragment(MsgSym.MESSAGE_JAVAFX_TRY_CATCH));
        }
        super.visitTry(tree);
    }

    @Override
    public void visitBlockExpression(JFXBlock tree) {
        mark(tree);
        if (bindStatus != JavafxBindStatus.UNBOUND) {
            for (List<JFXExpression> l = tree.stats; l.nonEmpty(); l = l.tail) {
                if (l.head.getFXTag() != JavafxTag.VAR_DEF) {
                    log.error(l.head.pos(), MsgSym.MESSAGE_JAVAFX_NOT_ALLOWED_IN_BIND_CONTEXT, l.head.toString());
                }
            }
        }
        super.visitBlockExpression(tree);
    }

    @Override
    public void visitIfExpression(JFXIfExpression tree) {
        mark(tree);
        super.visitIfExpression(tree);
    }

    @Override
    public void visitFunctionInvocation(JFXFunctionInvocation tree) {
        mark(tree);
        super.visitFunctionInvocation(tree);
    }

    @Override
    public void visitParens(JFXParens tree) {
        mark(tree);
        super.visitParens(tree);
    }

    @Override
    public void visitBinary(JFXBinary tree) {
        mark(tree);
        super.visitBinary(tree);
    }

    @Override
    public void visitTypeCast(JFXTypeCast tree) {
        mark(tree);
        super.visitTypeCast(tree);
    }

    @Override
    public void visitInstanceOf(JFXInstanceOf tree) {
        mark(tree);
        super.visitInstanceOf(tree);
    }

    @Override
    public void visitSelect(JFXSelect tree) {
        mark(tree);
        super.visitSelect(tree);
    }

    @Override
    public void visitIdent(JFXIdent tree) {
        mark(tree);
        super.visitIdent(tree);
    }

    @Override
    public void visitLiteral(JFXLiteral tree) {
        mark(tree);
        super.visitLiteral(tree);
    }

    @Override
    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
        mark(tree);
        super.visitSequenceEmpty(tree);
    }

    @Override
    public void visitSequenceRange(JFXSequenceRange tree) {
        mark(tree);
        super.visitSequenceRange(tree);
    }

    @Override
    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
        mark(tree);
        super.visitSequenceExplicit(tree);
    }

    @Override
    public void visitSequenceIndexed(JFXSequenceIndexed tree) {
        mark(tree);
        super.visitSequenceIndexed(tree);
    }

    @Override
    public void visitSequenceSlice(JFXSequenceSlice tree) {
        mark(tree);
        super.visitSequenceSlice(tree);
    }

    @Override
    public void visitStringExpression(JFXStringExpression tree) {
        mark(tree);
        super.visitStringExpression(tree);
    }

    @Override
    public void visitInstanciate(JFXInstanciate tree) {
        mark(tree);
        super.visitInstanciate(tree);
    }

    @Override
    public void visitForExpression(JFXForExpression tree) {
        mark(tree);
        super.visitForExpression(tree);
    }

    @Override
    public void visitIndexof(JFXIndexof tree) {
        mark(tree);
        super.visitIndexof(tree);
    }

    @Override
    public void visitTimeLiteral(JFXTimeLiteral tree) {
        mark(tree);
        super.visitTimeLiteral(tree);
    }
}

