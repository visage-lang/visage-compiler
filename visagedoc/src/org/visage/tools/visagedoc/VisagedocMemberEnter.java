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

package org.visage.tools.visagedoc;

import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.Position;
import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Symbol.*;
import org.visage.tools.tree.*;
import org.visage.tools.comp.VisageMemberEnter;

/**
 *  Javadoc's own memberEnter phase does a few things above and beyond that
 *  done by javac.
 */
public class VisagedocMemberEnter extends VisageMemberEnter {
    public static VisagedocMemberEnter instance0(Context context) {
        VisageMemberEnter instance = context.get(visageMemberEnterKey);
        if (instance == null)
            instance = new VisagedocMemberEnter(context);
        return (VisagedocMemberEnter)instance;
    }

    public static void preRegister(final Context context) {
        context.put(visageMemberEnterKey, new Context.Factory<VisageMemberEnter>() {
               public VisageMemberEnter make() {
                   return new VisagedocMemberEnter(context);
               }
        });
    }

    final DocEnv docenv;

    protected VisagedocMemberEnter(Context context) {
        super(context);
        docenv = DocEnv.instance(context);
    }

    @Override
    public void visitFunctionDefinition(VisageFunctionDefinition tree) {
        super.visitFunctionDefinition(tree);
        MethodSymbol meth = tree.sym;
        if (meth == null || meth.kind != Kinds.MTH) return;
        String docComment = env.toplevel.docComments.get(tree);
        Position.LineMap lineMap = env.toplevel.lineMap;
        if (meth.isConstructor())
            docenv.makeConstructorDoc(meth, docComment, tree, lineMap);
        else
            docenv.makeFunctionDoc(meth, docComment, tree, lineMap);
    }

    @Override
    public void visitVar(VisageVar tree) {
        super.visitVar(tree);
        if (tree.sym != null &&
                tree.sym.kind == Kinds.VAR &&
                !isParameter(tree.sym)) {
            String docComment = env.toplevel.docComments.get(tree);
            Position.LineMap lineMap = env.toplevel.lineMap;
            docenv.makeFieldDoc(tree.sym, docComment, tree, lineMap);
        }
    }

    private static boolean isParameter(VarSymbol var) {
        return (var.flags() & Flags.PARAMETER) != 0;
    }
}
