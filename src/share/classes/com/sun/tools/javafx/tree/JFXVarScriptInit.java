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

package com.sun.tools.javafx.tree;

import com.sun.tools.mjavac.code.Symbol.VarSymbol;

/**
 * Initialization of a var inline in a script body
 *
 * @author Robert Field
 */
public class JFXVarScriptInit extends JFXVarBase {
    private final JFXVar var;

    protected JFXVarScriptInit(JFXVar var) {
        super(var.getName(),
            var.getJFXType(),
            var.getModifiers(),
            var.getInitializer(),
            var.getBindStatus(),
            var.getOnReplace(),
            var.getOnInvalidate(),
            var.getSymbol());
        this.var = var;
    }
    
    public JFXVar getVar() {
        return var;
    }

    public VarSymbol getSymbol() {
        return var.getSymbol();
    }

    public void accept(JavafxVisitor v) {
        v.visitVarScriptInit(this);
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.VAR_SCRIPT_INIT;
    }
    
    public boolean isOverride() {
        return false;
    }
}
