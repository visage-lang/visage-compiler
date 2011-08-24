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
import com.sun.tools.mjavac.code.Symbol;

/**
 *
 * @author tball
 */
public class VisageInterpolateValue extends VisageExpression implements InterpolateValueTree {
    public final VisageExpression attribute;
    public VisageExpression value;
    public VisageExpression funcValue;
    public final VisageExpression interpolation;
    public Symbol sym;
    
    public VisageInterpolateValue(VisageExpression attr, VisageExpression v, VisageExpression interp) {
        attribute = attr;
        value = v;
        funcValue = null;
        interpolation = interp;
        sym = null;
    }

    public VisageExpression getAttribute() {
        return attribute;
    }

    public VisageExpression getInterpolation() {
        return interpolation;
    }

    public VisageExpression getValue() {
        return value;
    }

    public VisageKind getJavaFXKind() {
        return VisageKind.INTERPOLATE_VALUE;
    }

    public <R, D> R accept(VisageTreeVisitor<R, D> visitor, D data) {
        return visitor.visitInterpolateValue(this, data);
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitInterpolateValue(this);
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.INTERPOLATION_VALUE;
    }
}
