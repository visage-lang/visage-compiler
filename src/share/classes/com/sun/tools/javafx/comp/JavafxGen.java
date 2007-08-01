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
import com.sun.tools.javac.jvm.Gen;
import com.sun.tools.javac.comp.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.tree.*;

/**
 *
 * @author bothner
 */
public class JavafxGen extends Gen {

    public JavafxGen(Context context) {
        super(context);
    }
    
    public static JavafxGen instance(Context context) {
        JavafxGen instance = (JavafxGen) context.get(genKey);
        if (instance == null)
            instance = new JavafxGen(context);
        return instance;
    }
  public void visitBlockExpression(JFXBlockExpression tree) {
      // super.visitBlock(tree, tree.stats, tree.value, tree.endpos);
      int limit = code.nextreg;
      Env<GenContext> localEnv = env.dup(tree, new GenContext());
      genStats(tree.stats, localEnv);
      if (tree.value != null) {
          result = genExpr(tree.value, tree.value.type);
      }
      // End the scope of all block-local variables in variable info.
      if (env.tree.tag != JCTree.METHODDEF) {
          code.statBegin(tree.endpos);
          code.endScopes(limit);
          code.pendingStatPos = Position.NOPOS;
      }
  }
  
  public void visitTree(JCTree tree) {
         if (tree instanceof JFXBlockExpression)
             visitBlockExpression((JFXBlockExpression) tree);
         else
             super.visitTree(tree);
  }
}
