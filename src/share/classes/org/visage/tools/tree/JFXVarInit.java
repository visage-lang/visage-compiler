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

package org.visage.tools.tree;

import org.visage.api.tree.*;
import org.visage.api.tree.Tree.VisageKind;

import com.sun.tools.mjavac.util.Name;
import org.visage.tools.code.JavafxVarSymbol;
import com.sun.tools.mjavac.util.ListBuffer;
import java.util.List;

/**
 * Initialization of a var inline in a local context.
 * This includes both script bodies (its original use)
 * and local contexts inflated into classes.
 * Initialization code remains attached to the var.
 * VarInit holds the actual var tree, thus access must
 * carefully consider this.
 *
 * @author Robert Field
 */
public class VisageVarInit extends VisageExpression implements VariableTree {
    private VisageVar var;
    private ListBuffer<VisageVarInit> shreddedVarInits;

    protected VisageVarInit(VisageVar var) {
        this.var = var;
        if (var!=null)
            var.setVarInit(this);
    }
    
    public VisageVar getVar() {
        return var;
    }

    public void resetVar(VisageVar res) {
        var = res;
        var.setVarInit(this);
    }

    public JavafxVarSymbol getSymbol() {
        return var.getSymbol();
    }

    public Name getName() {
        return var.getName();
    }

    // for VariableTree
    public VisageTree getType() {
        return var.getType();
    }

    public VisageExpression getInitializer() {
        return var.getInitializer();
    }

    public void accept(JavafxVisitor v) {
        v.visitVarInit(this);
    }

    public void addShreddedVarInit(VisageVarInit vi) {
        if (shreddedVarInits == null) {
            shreddedVarInits = ListBuffer.lb();
        }
        shreddedVarInits.append(vi);
    }

    public ListBuffer<VisageVarInit> getShreddedVarInits() {
        if (shreddedVarInits == null) {
            return ListBuffer.<VisageVarInit>lb();
        } else {
            return shreddedVarInits;
        }
    }

    public VisageType getJFXType() {
        return var.getJFXType();
    }

    public OnReplaceTree getOnReplaceTree() {
        return var.getOnReplaceTree();
    }
    
    public VisageOnReplace getOnReplace() {
        return var.getOnReplace();
    }

    public OnReplaceTree getOnInvalidateTree() {
        return var.getOnInvalidateTree();
    }

    public VisageOnReplace getOnInvalidate() {
        return var.getOnInvalidate();
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.VAR_SCRIPT_INIT;
    }
    
    public VisageModifiers getModifiers() {
        return var.getModifiers();
    }
    
    public boolean isOverride() {
        return false;
    }

    public VisageKind getJavaFXKind() {
        return VisageKind.VARIABLE;
    }

    public <R, D> R accept(VisageTreeVisitor<R, D> visitor, D data) {
        return visitor.visitVariable(this, data);
     }
}
