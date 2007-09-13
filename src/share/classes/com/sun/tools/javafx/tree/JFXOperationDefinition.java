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

package com.sun.tools.javafx.tree;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

import com.sun.tools.javac.tree.JCTree.*;

/**
 * A function definition.
 */
public class JFXOperationDefinition extends JCMethodDecl {

    public JFXType rettype;
    public JFXBlockExpression bodyExpression;
    public List<JCTree> funParams;
    public JFXFunctionDefinitionStatement funStat;

    /*
     * @param modifiers operation modifiers
     * @param name operation name
     * @param restype type of operation return value
     * @param params value parameters
     */
    protected JFXOperationDefinition(
            JCModifiers modifiers, 
            Name name, 
            JFXType restype, 
            List<JCTree> params, 
            JFXBlockExpression bodyExpression) {
        super(modifiers, name,
                (JCExpression)((restype == null || restype.getJCTypeTree() == null) ? null : restype.getJCTypeTree()),
                List.<JCTypeParameter>nil(), List.<JCVariableDecl>nil(),
                List.<JCExpression>nil(), null, null, null);
        this.bodyExpression = bodyExpression;
        this.funParams = params;
        this.rettype = restype;
    }
    
    protected JFXOperationDefinition(
            JCModifiers modifiers, 
            Name name, 
            JFXType restype, 
            List<JCTree> params, 
            JFXBlockExpression bodyExpression,
            JFXFunctionDefinitionStatement funStat) {
        this(modifiers, name,
                restype, params, bodyExpression);
        this.funStat = funStat;
    }

    public JFXBlockExpression getBodyExpression() {
        return bodyExpression;
    }

    public void accept(JavafxVisitor v) {
        v.visitOperationDefinition(this);
    }

    @Override
    public void accept(Visitor v) {
        if (v instanceof JavafxVisitor) {
            this.accept((JavafxVisitor)v);
        } else {
            assert false;
        }
    }

    @Override
    public int getTag() {
        return JavafxTag.OPERATIONDEF;
    }
}
