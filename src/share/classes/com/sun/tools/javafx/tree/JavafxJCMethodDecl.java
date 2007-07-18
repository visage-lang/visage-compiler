package com.sun.tools.javafx.tree;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxMethodSymbol;

/**
 *
 * @author llitchev
 */
public class JavafxJCMethodDecl extends JCMethodDecl {
    private int javafxMethodType = JavafxFlags.SIMPLE_JAVA;
    private List<JFXExpression> capturedOuters = List.nil();
    
    public JFXTree declaration;
    public JFXStatement definition;
    
    /** Creates a new instance of JavafxMethodDecl */
    public JavafxJCMethodDecl(JCModifiers mods,
                            int javafxMethodType,
                            Name name,
                            JCExpression restype,
                            List<JCTypeParameter> typarams,
                            List<JCVariableDecl> params,
                            List<JCExpression> thrown,
                            JCBlock body,
                            JavafxMethodSymbol sym,
                            List<JFXExpression> capturedOuters,
                            JFXStatement definition,
                            JFXTree declaration) {
        super(mods, name, restype, typarams, params, thrown, body, null, sym);
        this.javafxMethodType = javafxMethodType;
        this.capturedOuters = capturedOuters;
        this.definition = definition;
        this.declaration = declaration;
        if (definition != null) {
            pos = definition.pos;
        }
        else if (declaration != null) {
            pos = declaration.pos;
        }
    }
    
    public int getJavafxMethodType() {
        return javafxMethodType;
    }
    
    public JCTree getDeclaration() {
        return declaration;
    }

    public JCTree getDefinition() {
        return definition;
    }
}
