/*
 * JavafxGen.java
 * 
 * Created on Jul 25, 2007, 10:20:46 AM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.sun.tools.javafx.comp;

import com.sun.tools.javafx.tree.*;
import com.sun.tools.javac.jvm.*;
import com.sun.tools.javac.comp.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.tree.*;

/**
 *
 * @author bothner
 */
public class BlockExprGen extends Gen {

    public static void preRegister(final Context context) {
        context.put(genKey, new Context.Factory<Gen>() {
            public Gen make() {
                return new BlockExprGen(context);
            }
        });
    }

    public BlockExprGen(Context context) {
        super(context);
    }
    
    public static BlockExprGen instance(Context context) {
        BlockExprGen instance = (BlockExprGen) context.get(genKey);
        if (instance == null)
            instance = new BlockExprGen(context);
        return instance;
    }
  public void visitBlockExpression(JFXBlockExpression tree) {
      // super.visitBlock(tree, tree.stats, tree.value, tree.endpos);
      int limit = code.nextreg;
      Env<GenContext> localEnv = env.dup(tree, new GenContext());
      genStats(tree.stats, localEnv);
      if (tree.value != null) {
          result = genExpr(tree.value, tree.value.type);
          result= result.load();
          if (result instanceof Items.LocalItem
                  /* && we're about to exit result's scope -- FIXME */) {
              result= result.load();
          }
      }
      // End the scope of all block-local variables in variable info.
      if (env.tree.getTag() != JCTree.METHODDEF) {
          code.statBegin(tree.endpos);
          code.endScopes(limit);
          code.pendingStatPos = Position.NOPOS;
      }
  }
  
  @Override
  public void visitTree(JCTree tree) {
         if (tree instanceof JFXBlockExpression)
             visitBlockExpression((JFXBlockExpression) tree);
         else
             super.visitTree(tree);
  }
}
