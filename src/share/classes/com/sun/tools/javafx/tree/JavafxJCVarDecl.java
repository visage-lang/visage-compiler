package com.sun.tools.javafx.tree;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import com.sun.tools.javafx.code.JavafxBindStatus;

/**
 *
 * @author llitchev
 */
public class JavafxJCVarDecl extends JCVariableDecl {
    private final int javafxVarType;
    private JavafxBindStatus bindStatus;
    private final JCTree declaration;
    private final JCTree definition;
    
    /** Creates a new instance of JavafxVarDecl */
    public JavafxJCVarDecl(
            JCModifiers mods,
            Name name,
            int javafxVarType,
            JCExpression vartype,
            JCExpression init,
            JavafxVarSymbol sym,
            JavafxBindStatus bindStatus,
            JCTree definition,
            JCTree declaration) {
        super(mods, name, vartype, init, sym);
        this.javafxVarType = javafxVarType;
        this.bindStatus = bindStatus;
        this.definition = definition;
        this.declaration = declaration;
        if (definition != null) {
            pos = definition.pos;
        } else if (declaration != null) {
            pos = declaration.pos;
        }
    }
    
    public int getJavafxVarType() {
        return javafxVarType;
    }
    
    public JavafxBindStatus getBindStatus() { return bindStatus; }
    public boolean isBound()     { return bindStatus.isBound; }
    public boolean isUnidiBind() { return bindStatus.isUnidiBind; }
    public boolean isBidiBind()  { return bindStatus.isBidiBind; }
    public boolean isLazy()      { return bindStatus.isLazy; }
    
    public JCTree getDeclaration() {
        return declaration;
    }
    
    public JCTree getDefinition() {
        return definition;
    }
}
