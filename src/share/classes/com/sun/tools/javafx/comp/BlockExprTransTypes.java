/*
 * JavafxTransTypes.java
 * 
 * Created on Jul 24, 2007, 8:32:53 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.sun.tools.javafx.comp;
import com.sun.tools.javafx.tree.*;
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
     
     public void visitBlockExpression(JFXBlockExpression tree) {
         tree.stats = translate(tree.stats);
         tree.value = translate(tree.value, null);
         result = tree;
     }
     
     @Override
     public void visitTree(JCTree tree) {
         if (tree instanceof JFXBlockExpression)
             visitBlockExpression((JFXBlockExpression) tree);
         else
             super.visitTree(tree);
    }
}
