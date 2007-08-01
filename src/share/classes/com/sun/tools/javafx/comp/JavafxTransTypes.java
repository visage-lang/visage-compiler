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
public class JavafxTransTypes extends TransTypes {
    
    protected JavafxTransTypes(Context context) {
        super(context);
    }

     public static JavafxTransTypes instance(Context context) {
        JavafxTransTypes instance = (JavafxTransTypes) context.get(transTypesKey);
        if (instance == null)
            instance = new JavafxTransTypes(context);
        return instance;
    }
     
     public void visitBlockExpression(JFXBlockExpression tree) {
         tree.stats = translate(tree.stats);
         tree.value = translate(tree.value, null);
         result = tree;
     };
     
     public void visitTree(JCTree tree) {
         if (tree instanceof JFXBlockExpression)
             visitBlockExpression((JFXBlockExpression) tree);
         else
             super.visitTree(tree);
    }
}
