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

package com.sun.tools.javafx.comp;
import com.sun.tools.javafx.tree.BlockExprJCBlockExpression;
import com.sun.tools.javac.comp.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.tree.*;

/**
 *
 * @author bothner
 */
public class BlockExprTransTypes extends TransTypes {
    
    public static void preRegister(final Context context) { 
        context.put(transTypesKey, new Context.Factory<TransTypes>() {
            public TransTypes make() {
                return new BlockExprTransTypes(context);
            }
        });
    }

    protected BlockExprTransTypes(Context context) {
        super(context);
    }

     public static BlockExprTransTypes instance(Context context) {
        BlockExprTransTypes instance = (BlockExprTransTypes) context.get(transTypesKey);
        if (instance == null)
            instance = new BlockExprTransTypes(context);
        return instance;
    }
     
     public void visitBlockExpression(BlockExprJCBlockExpression tree) {
         tree.stats = translate(tree.stats);
         tree.value = translate(tree.value, null);
         result = tree;
     }
     
     @Override
     public void visitTree(JCTree tree) {
         if (tree instanceof BlockExprJCBlockExpression)
             visitBlockExpression((BlockExprJCBlockExpression) tree);
         else
             super.visitTree(tree);
    }
}
