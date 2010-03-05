/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.tools.javafx.tree;

import com.sun.javafx.api.tree.*;
import com.sun.javafx.api.tree.Tree.JavaFXKind;

import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;
import com.sun.tools.mjavac.code.Symbol.ClassSymbol;
import com.sun.tools.mjavac.code.Scope;
import com.sun.tools.mjavac.tree.JCTree;

/**
 * A class declaration
 */
public class JFXClassDeclaration extends JFXExpression implements ClassDeclarationTree {
    public final JFXModifiers mods;
    private final Name name;
    private List<JFXExpression> extending;
    private List<JFXExpression> implementing;
    private List<JFXExpression> mixing;
    private List<JFXTree> defs;
    private List<JFXExpression> supertypes;
    private List<JavafxVarSymbol> objInitSyms;
    
    public ClassSymbol sym;
    public JFXFunctionDefinition runMethod;
    public Scope runBodyScope;
    public boolean isScriptClass;
    private boolean isScriptingModeScript;
    public boolean hasBeenTranslated; // prevent multiple translations
    
    private ListBuffer<JCTree> classInvokeCases;
    private ListBuffer<JCTree> scriptInvokeCases;

    protected JFXClassDeclaration() {
        this.mods = null;
        this.name = null;
        this.extending = null;
        this.implementing = null;
        this.mixing = null;
        this.defs = null;
        this.supertypes = null;
        this.objInitSyms = null;
        
        this.sym = null;
        this.runMethod = null;
        this.runBodyScope = null;
        this.isScriptClass = false;
        this.hasBeenTranslated = false;
        
        this.classInvokeCases = ListBuffer.lb();
        this.scriptInvokeCases = ListBuffer.lb();
    }
    protected JFXClassDeclaration(JFXModifiers mods,
            Name name,
            List<JFXExpression> supertypes,
            List<JFXTree> declarations,
            ClassSymbol sym) {
        this.mods = mods;
        this.name = name;           
        this.extending = null;
        this.implementing = null;
        this.mixing = null;
        this.defs = declarations;
        this.supertypes = supertypes;
        this.objInitSyms = null;
        
        this.sym = sym;
        this.runMethod = null;
        this.runBodyScope = null;
        this.isScriptClass = false;
        this.hasBeenTranslated = false;
        
        this.classInvokeCases = ListBuffer.lb();
        this.scriptInvokeCases = ListBuffer.lb();
    }

    public boolean isScriptClass() {
        return isScriptClass;
    }

    public java.util.List<ExpressionTree> getSupertypeList() {
        return convertList(ExpressionTree.class, supertypes);
    }

    public JFXModifiers getModifiers() {
        return mods;
    }

    public Name getName() {
        return name;
    }

    public List<JFXExpression> getSupertypes() {
        return supertypes;
    }

    public List<JFXTree> getMembers() {
        return defs;
    }

    public void setMembers(List<JFXTree> members) {
        defs = members;
    }

    public List<JFXExpression> getImplementing() {
        return implementing;
    }

    public List<JFXExpression> getExtending() {
        return extending;
    }

    public List<JFXExpression> getMixing() {
        return mixing;
    }

    public void setDifferentiatedExtendingImplementingMixing(List<JFXExpression> extending,
                                                       List<JFXExpression> implementing,
                                                       List<JFXExpression> mixing) {
        this.extending    = extending;
        this.implementing = implementing;
        this.mixing       = mixing;
        
        // JFXC-2820 - Reorder the supertypes during attribution.
        ListBuffer<JFXExpression> orderedSuperTypes = new ListBuffer<JFXExpression>();
        
        // Add supers according to declaration and normal, mixin and interface constraints.
        for (JFXExpression extend    : extending)    orderedSuperTypes.append(extend);
        for (JFXExpression mixin     : mixing)       orderedSuperTypes.append(mixin);
        for (JFXExpression implement : implementing) orderedSuperTypes.append(implement);
        
        // Replace supertypes so that all references use the correct ordering.
        supertypes = orderedSuperTypes.toList();
    }
    
    public boolean isMixinClass() {
        return (sym.flags_field & JavafxFlags.MIXIN) != 0;
    }
    
    public boolean isBoundFuncClass() {
        return (sym.flags_field & JavafxFlags.FX_BOUND_FUNCTION_CLASS) != 0L;
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.CLASS_DEF;
    }
    
    public void accept(JavafxVisitor v) {
        v.visitClassDeclaration(this);
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.CLASS_DECLARATION;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitClassDeclaration(this, data);
    }

    public javax.lang.model.element.Name getSimpleName() {
        return (javax.lang.model.element.Name)name;
    }

    public java.util.List<ExpressionTree> getImplements() {
        return JFXTree.convertList(ExpressionTree.class, implementing);
    }

    public java.util.List<Tree> getClassMembers() {
        return convertList(Tree.class, defs);
    }

    public java.util.List<ExpressionTree> getExtends() {
        return convertList(ExpressionTree.class, extending);
    }

    public java.util.List<ExpressionTree> getMixins() {
        return convertList(ExpressionTree.class, mixing);
    }
    
    public void setObjInitSyms(List<JavafxVarSymbol> syms) {
        objInitSyms = syms;
    }
    
    public List<JavafxVarSymbol> getObjInitSyms() {
        return objInitSyms;
    }
    
    public int addInvokeCase(JCTree invokeCase, boolean isScript) {
        if (isScript) {
            scriptInvokeCases.append(invokeCase);
            return scriptInvokeCases.size() - 1;
        } else {
            classInvokeCases.append(invokeCase);
            return classInvokeCases.size() - 1;
        }
    }
    
    public List<JCTree> invokeCases(boolean isScript) {
        if (isScript) {
            return scriptInvokeCases.toList();
        } else {
            return classInvokeCases.toList();
        }
    }

    public boolean isScriptingModeScript() {
        return isScriptingModeScript;
    }

    public void setScriptingModeScript() {
        isScriptingModeScript = true;
    }
}
