/*
 * Copyright 1999-2005 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.comp;

import com.sun.tools.javac.code.*;
import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Kinds.*;
import static com.sun.tools.javac.code.TypeTags.*;
import static com.sun.tools.javac.code.TypeTags.WILDCARD;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javafx.tree.JFXBlockExpression;
import com.sun.tools.javafx.tree.*;

/** This is the main context-dependent analysis phase in GJC. It
 *  encompasses name resolution, type checking and constant folding as
 *  subtasks. Some subtasks involve auxiliary classes.
 *  @see Check
 *  @see Resolve
 *  @see ConstFold
 *  @see Infer
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class BlockExprAttr extends Attr  {
    
    public static Attr instance0(Context context) {
        Attr instance = context.get(attrKey);
        if (instance == null)
            instance = new BlockExprAttr(context);
        return instance;
    }

    public static void preRegister(final Context context) {
        context.put(attrKey, new Context.Factory<Attr>() {
	       public Attr make() {
		   return new BlockExprAttr(context);
	       }
        });
    }

    protected BlockExprAttr(Context context) {
        super(context);
    }


    public void visitBlockExpression(JFXBlockExpression tree) {
        // Create a new local environment with a local scope.
        Env<AttrContext> localEnv =
                env.dup(tree,
                env.info.dup(env.info.scope.dup()));
        for (List<JCStatement> l = tree.stats; l.nonEmpty(); l = l.tail)
            attribStat(l.head, localEnv);
        if (tree.value != null) {
            Type valtype = attribExpr(tree.value, localEnv);
             result = check(tree, valtype, VAL, pkind, pt);
        }
        localEnv.info.scope.leave();
    }
}
