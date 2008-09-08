/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.javafx.tree;

import com.sun.javafx.api.tree.JavaFXTreeVisitor;
import com.sun.javafx.api.tree.MissingTree;

import com.sun.javafx.api.tree.Tree.JavaFXKind;

/**
 * The grammar was expecting an expression but did not find it.
 * This vacuous expression was filled in.
 * 
 * @author Robert Field
 */
public class JFXMissingExpression extends JFXExpression implements MissingTree {

    protected JFXMissingExpression() {
    }
    
    /**
     * Gets the JavaFX kind of this tree.
     *
     * @return the kind of this tree.
     */
    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.MISSING_EXPRESSION;
    }
    
    /**
     * Was this tree expected, but missing, and filled-in by the parser
     */
    @Override
    public boolean isMissing() {
        return true;
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.ERRONEOUS;
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitMissingExpression(this);
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> v, D d) {
        return v.visitMissingExpression(this, d);
    }
}
