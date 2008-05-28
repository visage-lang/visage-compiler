/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javafx.tree.JFXBlockExpression;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.comp.MemberEnter;
import com.sun.tools.javac.tree.JCTree;


import static com.sun.tools.javac.tree.JCTree.*;

/** This is the second phase of Enter, in which classes are completed
 *  by entering their members into the class scope using
 *  MemberEnter.complete().  See Enter for an overview.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class BlockExprMemberEnter extends MemberEnter {
    boolean resolvingImport = false;
    public static MemberEnter instance0(Context context) {
        MemberEnter instance = context.get(memberEnterKey);
        if (instance == null)
            instance = new BlockExprMemberEnter(context);
        return instance;
    }

    public static void preRegister(final Context context) {
        context.put(memberEnterKey, new Context.Factory<MemberEnter>() {
	       public MemberEnter make() {
		   return new BlockExprMemberEnter(context);
	       }
        });
    }

    protected BlockExprMemberEnter(Context context) {
        super(context);
    }

    public void visitBlockExpression(JFXBlockExpression tree) {
        for (JCStatement stmt : tree.stats) {
            stmt.accept(this);
        }
        tree.value.accept(this);
    }

    public Type attribImportType(JCTree tree, Env<AttrContext> env) {
        assert completionEnabled;
        try {
            // To prevent deep recursion, suppress completion of some
            // types.
            completionEnabled = false;
            resolvingImport = true;
            return attr.attribType(tree, env);
        } finally {
            completionEnabled = true;
            resolvingImport = false;
        }
    }
}
