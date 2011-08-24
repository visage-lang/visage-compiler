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

package org.visage.tools.tree;

import org.visage.api.tree.*;
import org.visage.api.tree.Tree.VisageKind;

import org.visage.tools.code.VisageFlags;
import org.visage.tools.code.VisageVarSymbol;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;
import com.sun.tools.mjavac.code.Symbol.ClassSymbol;
import com.sun.tools.mjavac.code.Scope;
import com.sun.tools.mjavac.tree.JCTree;

/**
 * A class declaration
 */
public class VisageClassDeclaration extends VisageExpression implements ClassDeclarationTree {
    public final VisageModifiers mods;
    private final Name name;
    private List<VisageExpression> extending;
    private List<VisageExpression> implementing;
    private List<VisageExpression> mixing;
    private List<VisageTree> defs;
    private List<VisageExpression> supertypes;
    private List<VisageVarSymbol> objInitSyms;
    
    public ClassSymbol sym;
    public VisageFunctionDefinition runMethod;
    public Scope runBodyScope;
    public boolean isScriptClass;
    private boolean isScriptingModeScript;
    public boolean hasBeenTranslated; // prevent multiple translations
    
    private ListBuffer<JCTree> classInvokeCases;
    private ListBuffer<JCTree> scriptInvokeCases;

    protected VisageClassDeclaration() {
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
    protected VisageClassDeclaration(VisageModifiers mods,
            Name name,
            List<VisageExpression> supertypes,
            List<VisageTree> declarations,
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

    public VisageModifiers getModifiers() {
        return mods;
    }

    public Name getName() {
        return name;
    }

    public List<VisageExpression> getSupertypes() {
        return supertypes;
    }

    public List<VisageTree> getMembers() {
        return defs;
    }

    public void setMembers(List<VisageTree> members) {
        defs = members;
    }

    public List<VisageExpression> getImplementing() {
        return implementing;
    }

    public List<VisageExpression> getExtending() {
        return extending;
    }

    public List<VisageExpression> getMixing() {
        return mixing;
    }

    public void setDifferentiatedExtendingImplementingMixing(List<VisageExpression> extending,
                                                       List<VisageExpression> implementing,
                                                       List<VisageExpression> mixing) {
        this.extending    = extending;
        this.implementing = implementing;
        this.mixing       = mixing;
        
        // VSGC-2820 - Reorder the supertypes during attribution.
        ListBuffer<VisageExpression> orderedSuperTypes = new ListBuffer<VisageExpression>();
        
        // Add supers according to declaration and normal, mixin and interface constraints.
        for (VisageExpression extend    : extending)    orderedSuperTypes.append(extend);
        for (VisageExpression mixin     : mixing)       orderedSuperTypes.append(mixin);
        for (VisageExpression implement : implementing) orderedSuperTypes.append(implement);
        
        // Replace supertypes so that all references use the correct ordering.
        supertypes = orderedSuperTypes.toList();
    }
    
    public boolean isMixinClass() {
        return (sym.flags_field & VisageFlags.MIXIN) != 0;
    }
    
    public boolean isBoundFuncClass() {
        return (sym.flags_field & VisageFlags.VISAGE_BOUND_FUNCTION_CLASS) != 0L;
    }

    @Override
    public VisageTag getVisageTag() {
        return VisageTag.CLASS_DEF;
    }
    
    public void accept(VisageVisitor v) {
        v.visitClassDeclaration(this);
    }

    public VisageKind getVisageKind() {
        return VisageKind.CLASS_DECLARATION;
    }

    public <R, D> R accept(VisageTreeVisitor<R, D> visitor, D data) {
        return visitor.visitClassDeclaration(this, data);
    }

    public javax.lang.model.element.Name getSimpleName() {
        return (javax.lang.model.element.Name)name;
    }

    public java.util.List<ExpressionTree> getImplements() {
        return VisageTree.convertList(ExpressionTree.class, implementing);
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
    
    public void setObjInitSyms(List<VisageVarSymbol> syms) {
        objInitSyms = syms;
    }
    
    public List<VisageVarSymbol> getObjInitSyms() {
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
