/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.javafx.tree;

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.JavaFXTreeVisitor;
import com.sun.javafx.api.tree.OnReplaceTree;
import com.sun.javafx.api.tree.Tree.JavaFXKind;
import com.sun.javafx.api.tree.VariableTree;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.util.Name;

/**
 *
 * @author maurizio
 */
public abstract class JFXVarBase extends JFXExpression implements VariableTree {
    
    public JFXType typeExpr;
    public JFXModifiers mods;
    public Name name;
    public VarSymbol sym;
    public JFXExpression init;
    public JavafxBindStatus bindStatus;

    private JFXOnReplace[] triggers;

    protected JFXVarBase(Name name,
            JFXType type,
            JFXModifiers mods,    
            JFXExpression init,
            JavafxBindStatus bindStat,
            JFXOnReplace onReplace,
            JFXOnReplace onInvalidate,
            VarSymbol sym) {
        this.name = name;
        this.typeExpr = type;
        this.mods = mods;        
        this.init = init;
        this.bindStatus = bindStat == null ? JavafxBindStatus.UNBOUND : bindStat;
        this.triggers = new JFXOnReplace[JFXOnReplace.Kind.values().length];
        this.triggers[JFXOnReplace.Kind.ONREPLACE.ordinal()] = onReplace;
        this.triggers[JFXOnReplace.Kind.ONINVALIDATE.ordinal()] = onInvalidate;
        this.sym = sym;
    }

    abstract boolean isOverride();

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitVariable(this, data);
    }

    public VarSymbol getSymbol() {
        return sym;
    }

    public JavafxBindStatus getBindStatus() {
        return bindStatus;
    }

    public JFXExpression getInitializer() {
        return init;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.VARIABLE;
    }

    public JFXModifiers getModifiers() {
        return mods;
    }

    public Name getName() {
        return name;
    }

    public JFXOnReplace getOnInvalidate() {
        return triggers[JFXOnReplace.Kind.ONINVALIDATE.ordinal()];
    }

    public OnReplaceTree getOnInvalidateTree() {
        return triggers[JFXOnReplace.Kind.ONINVALIDATE.ordinal()];
    }

    public JFXOnReplace getOnReplace() {
        return triggers[JFXOnReplace.Kind.ONREPLACE.ordinal()];
    }

    public OnReplaceTree getOnReplaceTree() {
        return triggers[JFXOnReplace.Kind.ONREPLACE.ordinal()];
    }

    public JFXOnReplace getTrigger(JFXOnReplace.Kind triggerKind) {
        return triggers[triggerKind.ordinal()];
    }

    public OnReplaceTree getTriggerTree(JFXOnReplace.Kind triggerKind) {
        return triggers[triggerKind.ordinal()];
    }

    public JFXTree getType() {
        return typeExpr;
    }

    public JFXType getJFXType() {
        return typeExpr;
    }

    public boolean isBidiBind() {
        return bindStatus.isBidiBind();
    }

    public boolean isBound() {
        return bindStatus.isBound();
    }

    public boolean isLazy() {
        return bindStatus.isLazy();
    }

    public boolean isUnidiBind() {
        return bindStatus.isUnidiBind();
    }

}
