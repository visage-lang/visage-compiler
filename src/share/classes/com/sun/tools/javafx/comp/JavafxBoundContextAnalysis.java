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
import com.sun.tools.mjavac.util.ListBuffer;
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
    private JavafxTreeMaker fxmake;
    private JavafxDefs defs;

    private boolean inBindContext;

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
        fxmake = JavafxTreeMaker.instance(context);
        defs = JavafxDefs.instance(context);

        inBindContext = false;
    }

    public void analyzeBindContexts(JavafxEnv<JavafxAttrContext> attrEnv) {
        scan(attrEnv.tree);
    }

    private void mark(JFXBoundMarkable tree) {
        if (inBindContext) {
        tree.markBound();
        }
    }

    @Override
    public void visitScript(JFXScript tree) {
        inBindContext = false;
        super.visitScript(tree);
    }

    @Override
    public void visitVarInit(JFXVarInit tree) {
    }

    @Override
    public void visitVar(JFXVar tree) {
        // any changes here should also go into visitOverrideClassVar
        boolean wasInBindContext = inBindContext;
        inBindContext |= tree.isBound();
        mark(tree);
        scan(tree.getInitializer());
        inBindContext = wasInBindContext;
        for (JFXOnReplace.Kind triggerKind : JFXOnReplace.Kind.values()) {
            JFXOnReplace trigger = tree.getTrigger(triggerKind);
            if (trigger != null) {
                if (inBindContext) {
                    log.error(trigger.pos(), MsgSym.MESSAGE_TRIGGER_IN_BIND_NOT_ALLOWED, triggerKind);
                }
            }
        }
        scan(tree.getOnReplace());
        scan(tree.getOnInvalidate());
    }

    @Override
    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        boolean wasInBindContext = inBindContext;
        inBindContext |= tree.isBound();
        scan(tree.getInitializer());
        inBindContext = wasInBindContext;
        scan(tree.getOnReplace());
        scan(tree.getOnInvalidate());
    }

    @Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
        // these start over in a class definition
        boolean wasInBindContext = inBindContext;
        inBindContext = false;

        super.visitClassDeclaration(tree);

        inBindContext = wasInBindContext;
    }

    @Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        // start over in a function definition
        boolean wasInBindContext = inBindContext;

        inBindContext = tree.isBound();
        // don't use super, since we don't want to cancel the inBindContext
        scan(tree.getParams());
        if (tree.isBound()) {
            /*
             * For bound functions, make a new bound variable with initialization
             * expression to be the return expression and return the variable as
             * result. This way, JavafxLocalToClass will see this function with
             * a local bind variable (even if there are no other variables in the
             * function) and so create a class for local context.
             */
            JFXBlock blk = tree.getBodyExpression();
            JFXExpression returnExpr = (blk.value instanceof JFXReturn)?
                ((JFXReturn)blk.value).getExpression() : blk.value;
            if (returnExpr != null) {
                fxmake.at(blk.value.pos);
                JFXVar resultVar = fxmake.Var(defs.boundFunctionResultName, fxmake.TypeUnknown(),
                    fxmake.Modifiers(0), returnExpr, JavafxBindStatus.UNIDIBIND, null, null);
                blk.stats = blk.stats.append(resultVar);
                blk.value = fxmake.Ident(defs.boundFunctionResultName);
            }
        }
        scan(tree.getBodyExpression());
        inBindContext = wasInBindContext;
    }

    @Override
    public void visitForExpressionInClause(JFXForExpressionInClause tree) {
        mark(tree);
        super.visitForExpressionInClause(tree);
    }

    @Override
    public void visitFunctionValue(JFXFunctionValue tree) {
        // these start over in a function value
        boolean wasInBindContext = inBindContext;
        inBindContext = false;
        super.visitFunctionValue(tree);
        inBindContext = wasInBindContext;
    }

    @Override
    public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        boolean wasInBindContext = inBindContext;

        // bind doesn't permiate object literals, but...
        // ... it is still makes it a bound context
        inBindContext |= tree.isBound();
        scan(tree.getExpression());
        inBindContext = wasInBindContext;
    }

    @Override
    public void visitAssignop(JFXAssignOp tree) {
        if (inBindContext) {
            log.error(tree.pos(), MsgSym.MESSAGE_JAVAFX_NOT_ALLOWED_IN_BIND_CONTEXT, "compound assignment");
        }
        super.visitAssignop(tree);
    }

    @Override
    public void visitUnary(JFXUnary tree) {
        if (inBindContext) {
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
        boolean wasInBindContext = inBindContext;
        inBindContext = true;
        super.visitInterpolateValue(tree);
        inBindContext = wasInBindContext;
    }

    @Override
    public void visitKeyFrameLiteral(JFXKeyFrameLiteral tree) {
        if (inBindContext) {
            log.error(tree.pos(),
                    MsgSym.MESSAGE_JAVAFX_NOT_ALLOWED_IN_BIND_CONTEXT,
                    diags.fragment(MsgSym.MESSAGE_JAVAFX_KEYFRAME_LIT));
        }
        super.visitKeyFrameLiteral(tree);
    }

    @Override
    public void visitBlockExpression(JFXBlock tree) {
        if (inBindContext) {
            for (List<JFXExpression> l = tree.stats; l.nonEmpty(); l = l.tail) {
                if (l.head.getFXTag() != JavafxTag.VAR_DEF) {
                    log.error(l.head.pos(), MsgSym.MESSAGE_JAVAFX_NOT_ALLOWED_IN_BIND_CONTEXT, l.head.toString());
                }
            }
        }
        super.visitBlockExpression(tree);
    }

}

