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

import com.sun.tools.javafx.tree.*;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.tree.JCTree.JCExpression;
import com.sun.tools.mjavac.tree.JCTree.JCVariableDecl;
import com.sun.tools.mjavac.util.Context;

/**
 * Translate bind expressions into code in bind defining methods
 * 
 * @author Robert Field
 */
public class JavafxTranslateDependent extends JavafxAbstractTranslation implements JavafxVisitor {

    protected static final Context.Key<JavafxTranslateDependent> jfxDependentTranslation =
        new Context.Key<JavafxTranslateDependent>();

    public static JavafxTranslateDependent instance(Context context) {
        JavafxTranslateDependent instance = context.get(jfxDependentTranslation);
        if (instance == null) {
            JavafxToJava toJava = JavafxToJava.instance(context);
            instance = new JavafxTranslateDependent(context, toJava);
        }
        return instance;
    }

    public JavafxTranslateDependent(Context context, JavafxToJava toJava) {
        super(context, toJava);

        context.put(jfxDependentTranslation, this);
    }

    static JCExpression TODO(String msg, JFXExpression tree) {
        return TODO(msg + " -- " + tree.getClass());
    }


    /***********************************************************************
     *
     * Translators
     *
     */

    class DependentSelectTranslator extends SelectTranslator {

         protected DependentSelectTranslator(JFXSelect tree) {
            super(tree);
        }

        @Override
        JCExpression fullExpression(JCExpression tToCheck) {
            JCExpression full = super.fullExpression(tToCheck);
            JFXExpression sel = getToCheck();
            Symbol checkSym = expressionSymbol(sel);
            if (checkSym == null | !(checkSym instanceof VarSymbol)) {
                return full;
            } else {
                // This is a "real" select, selector is a variable
                VarSymbol selectorSym = (VarSymbol) checkSym;
                addInterClassBindee(selectorSym, refSym);
                JCVariableDecl resVar = TmpVar(fullType, full);
                // If this is a synthetic selector, use it in addDependent
                JCExpression selected = staticReference ?
                                            translateExpr(sel, sel.type) :
                                            copyOfTranslatedToCheck(tToCheck);
                return BlockExpression(
                        resVar,
                        CallStmt(defs.FXBase_addDependent,
                            selected,
                            Offset(copyOfTranslatedToCheck(tToCheck), refSym),
                            getReceiverOrThis(selectorSym)),
                        id(resVar));
            }
        }
    }


    /***********************************************************************
     *
     * Visitors  (alphabetical order)
     *
     */

    JCExpression TODO(JFXTree tree) {
        return TODO("BIND functionality: " + tree.getClass().getSimpleName());
    }

    public void visitBlockExpression(JFXBlock tree) {
        result = new BoundBlockExpressionTranslator(tree).doit();
    }

    public void visitFunctionValue(JFXFunctionValue tree) {
        TODO(tree);
    }

    @Override
    public void visitIdent(JFXIdent tree) {
        result = new BoundIdentTranslator(tree).doit();
    }

    public void visitInterpolateValue(JFXInterpolateValue tree) {
        result = new InterpolateValueTranslator(tree).doit();
    }

    public void visitSelect(JFXSelect tree) {
        result = new DependentSelectTranslator(tree).doit();
    }


    /***********************************************************************
     *
     * Utilities
     *
     */

    protected String getSyntheticPrefix() {
        return "dfx$";
    }

}
