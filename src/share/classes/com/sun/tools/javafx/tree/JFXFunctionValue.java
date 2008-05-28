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

package com.sun.tools.javafx.tree;
import com.sun.javafx.api.tree.JavaFXTree.JavaFXKind;
import com.sun.javafx.api.tree.JavaFXTreeVisitor;
import com.sun.javafx.api.tree.FunctionValueTree;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.tree.Pretty;
import com.sun.tools.javac.tree.JCTree.*;

/**
 *
 * @author bothner
 */
public class JFXFunctionValue extends JFXExpression implements FunctionValueTree {
    public JFXType rettype;
    public List<JFXVar> funParams;
    public JFXBlockExpression bodyExpression;
    public JFXFunctionDefinition definition;

    public JFXFunctionValue(JFXType rettype, 
            List<JFXVar> params, 
            JFXBlockExpression bodyExpression) {
        this.rettype = rettype;
        this.funParams = params;
        this.bodyExpression = bodyExpression;
    }

    public JFXType getJFXReturnType() { return rettype; }
    public JFXType getType() {
        return rettype;
    }
    
    public List<JFXVar> getParams() {
        return funParams;
    }
    
    public java.util.List<? extends VariableTree> getParameters() {
        return (java.util.List)funParams;
    }

    public JFXBlockExpression getBodyExpression() {
        return bodyExpression;
    }

    public void accept(JavafxVisitor v) { v.visitFunctionValue(this); }
    
    @Override
    public void accept(Visitor v) {
        if (v instanceof JavafxVisitor) {
            this.accept((JavafxVisitor)v);
        } else if (v instanceof Pretty) {
            JavafxPretty.visitFunctionValue((Pretty) v, this);
        } else {
            assert false;
        }
    }

    @Override
    public int getTag() {
     return JavafxTag.FUNCTIONEXPRESSION;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.FUNCTION_VALUE;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitFunctionValue(this, data);
    }
}
