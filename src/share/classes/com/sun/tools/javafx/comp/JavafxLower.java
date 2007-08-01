/*
 * JavafxLower.java
 * 
 * Created on Jul 25, 2007, 10:08:52 AM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.sun.tools.javafx.comp;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javac.comp.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.comp.*;
import com.sun.tools.javac.tree.*;
/**
 *
 * @author bothner
 */
public class JavafxLower extends Lower {
    
    public static JavafxLower instance(Context context) {
        JavafxLower instance = (JavafxLower) context.get(lowerKey);
        if (instance == null)
            instance = new JavafxLower(context);
        return instance;
    }
     
    protected JavafxLower(Context context) {
        super(context);
    }
    
   public void visitBlockExpression(JFXBlockExpression tree) {
         tree.stats = translate(tree.stats);
         tree.value = translate(tree.value);
         result = tree;
     };
     
     public void visitTree(JCTree tree) {
         if (tree instanceof JFXBlockExpression)
             visitBlockExpression((JFXBlockExpression) tree);
         else
             super.visitTree(tree);
    }
}
