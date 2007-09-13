/*
 * JavafxLower.java
 * 
 * Created on Jul 25, 2007, 10:08:52 AM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.sun.tools.javafx.comp;

import com.sun.tools.javac.comp.Lower;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javafx.tree.JFXBlockExpression;

/**
 * @author bothner
 */
public class BlockExprLower extends Lower {

    public static void preRegister(final Context context) {
        context.put(lowerKey, new Context.Factory<Lower>() {
            public Lower make() {
                return new BlockExprLower(context);
            }
        });
    }

    public static BlockExprLower instance(Context context) {
        BlockExprLower instance = (BlockExprLower) context.get(lowerKey);
        if (instance == null)
            instance = new BlockExprLower(context);
        return instance;
    }

    protected BlockExprLower(Context context) {
        super(context);
    }

    public void visitBlockExpression(JFXBlockExpression tree) {
        tree.stats = translate(tree.stats);
        tree.value = translate(tree.value);
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
