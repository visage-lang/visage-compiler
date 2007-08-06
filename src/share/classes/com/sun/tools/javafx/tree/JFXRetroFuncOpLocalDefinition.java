/*
 * Copyright 1999-2006 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.tree.JCTree;

/**
 * A local function or operation definition.
 */
public abstract class JFXRetroFuncOpLocalDefinition extends JFXStatement {
    public Name name; // TODO: Make this an Ident, so position info will be stored and tools will be able to operate.
    public JFXType restype;
    public List<JCTree> params;
    public JCBlock body;

    public MethodSymbol sym;
    public JCTree owner;
   /*
    * @param tag the tag for function/operation definition
    * @param name operation name
    * @param restype type of operation return value
    * @param params value parameters
    * @param body statements in the operation
    * @param sym method symbol
    */
    protected JFXRetroFuncOpLocalDefinition(
            Name name,
            JFXType restype,
            List<JCTree> params,
            JCBlock body,
            MethodSymbol sym) {
        this.name = name;
        this.restype = restype;
        this.params = params;
        this.body = body;
        this.sym = sym;
    }
    
    public Name getName() { return name; }
    public JFXType getType() { return restype; }
    public List<JCTree> getParameters() {
        return params;
    }
    public JCBlock getBody() { return body; }
}
