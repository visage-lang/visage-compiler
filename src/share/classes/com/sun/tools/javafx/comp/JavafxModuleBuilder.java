/*
 * Copyright 1999-2005 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

package com.sun.tools.javafx.comp;

import com.sun.tools.javac.code.*;
import com.sun.tools.javac.jvm.*;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name.Table;
import com.sun.tools.javafx.tree.*;
import java.io.File;
import java.net.MalformedURLException;
import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.Kinds.*;
import static com.sun.tools.javac.code.TypeTags.*;
import static com.sun.tools.javafx.tree.JavafxTag.*;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import java.util.HashSet;
import java.util.Set;

@Version("@(#)JavafxEnter.java	1.140 07/05/05")
public class JavafxModuleBuilder extends JavafxAbstractVisitor {
    protected static final Context.Key<JavafxModuleBuilder> javafxModuleBuilderKey =
        new Context.Key<JavafxModuleBuilder>();

    private Table nameTable;
    private Target target;
    private JavafxTreeMaker make;
    private Log log;
    private Set<Name> topLevelNamesSet;

    public static JavafxModuleBuilder instance(Context context) {
        JavafxModuleBuilder instance = context.get(javafxModuleBuilderKey);
        if (instance == null)
            instance = new JavafxModuleBuilder(context);
        return instance;
    }

    protected JavafxModuleBuilder(Context context) {
        super(null);
        nameTable = Table.instance(context);
        target = Target.instance(context);
        make = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        log = Log.instance(context);
    }

   public void visitTopLevel(JCCompilationUnit tree) {
        try {
            preProcessJfxTopLevel(tree);
            super.visitTopLevel(tree);
        }
        finally {
            topLevelNamesSet = null;
        }
    }

    private void preProcessJfxTopLevel(JCCompilationUnit tree) {
        // TODO:
        // Maybe something different needs to be done for the importStats
        JCClassDecl topLevelClass = null;
        List<JCTree> newBody = List.nil();
        
        // TODO:
        String fileObjName = null;

        try {
            fileObjName = new File(tree.getSourceFile().toUri().toURL().getFile()).getName();
            int lastDotIdx = fileObjName.lastIndexOf('.');
            if (lastDotIdx != -1) {
               fileObjName = fileObjName.substring(0, lastDotIdx); 
            }
        }
        catch (MalformedURLException e) {
            assert false : "URL Exception!!!";
        }

        Name topLevelClassName = Name.fromString(nameTable, fileObjName);
        MemberHolder memberHolder = new MemberHolder(tree.defs);
        
        prependSyntheticModuleMembers(memberHolder, fileObjName);
        tree.defs = memberHolder.resultTopTreeDefs;
        
        List<JCTypeParameter> emptyTypeParamList = List.nil();
        List<JCExpression> emptyExpresssionList = List.nil();
        
        topLevelClass = make.ClassDef(make.Modifiers(PUBLIC), topLevelClassName, emptyTypeParamList, null, emptyExpresssionList, memberHolder.topLevelClassBody);
        tree.defs = tree.defs.append(topLevelClass);
    }
    
    private void prependSyntheticModuleMembers(MemberHolder memberHolder, String fileObjName) {
        char syntheticChar = target.syntheticNameChar();
        String instModNamed = syntheticChar + "module" + syntheticChar + "instance";
        
        List<JCTree> newBody = memberHolder.topLevelClassBody;
        List<JCTree> newDefs = memberHolder.inTopTreeDefs;
        
        // Create Module instance variable
        newBody = newBody.prepend(
                make.VarDef(make.Modifiers(PRIVATE|STATIC), 
                            Name.fromString(nameTable, instModNamed), 
                            make.Ident(Name.fromString(nameTable, fileObjName)), 
                            null));
        
        // Create getModuleInstance method
        List<JCVariableDecl> emptyVarDefList = List.nil();
        List<JFXExpression> emptyCaptureOuters = List.nil();
        List<JCStatement> stats = List.nil();
        stats = stats.append(make.Return(make.Ident(Name.fromString(nameTable, instModNamed))));
        
        JCBlock body = make.Block(0, stats);
        
        newBody = newBody.prepend(make.JavafxMethodDef(make.Modifiers(PUBLIC|STATIC), JavafxFlags.SIMPLE_JAVA, Name.fromString(nameTable,
                "getModuleInstance"), make.Ident(Name.fromString(nameTable, fileObjName)),
                emptyVarDefList, body, null, emptyCaptureOuters, null, null));

        // Create the run method...
        // Create getModuleInstance method
        emptyVarDefList = List.nil();
        List<JCExpression> emptyExpressionList = List.nil();
        
        stats = List.nil();
        JCAssign assign = make.Assign(make.Ident(Name.fromString(nameTable, instModNamed)),
                make.NewClass(null, emptyExpressionList, make.Ident(Name.fromString(nameTable, fileObjName)),
                emptyExpressionList, null));
        stats = stats.append(make.If(make.Binary(JCTree.NE, make.Ident(Name.fromString(nameTable, instModNamed)),
                make.Literal(BOT, null)), make.Return(null), null));
        
        stats = stats.append(make.Exec(assign));
        List<JCTree> retDefs = List.nil();
        for (JCTree tree : newDefs) {
            switch (tree.getTag()) {
                                                case IMPORT:
                retDefs = retDefs.append(tree);
                break;
            case CLASSDECL:
                checkName(tree.pos, ((JFXClassDeclaration)tree).name);
                newBody = newBody.append(tree);
                break;
            case RETROOPERATIONDEF:
            case RETROFUNCTIONDEF:
            case RETROATTRIBUTEDEF:
                newBody = newBody.append(tree);
                break;
            case RETROOPERATIONLOCALDEF:
            case RETROFUNCTIONLOCALDEF:
                checkName(tree.pos, ((JFXRetroFuncOpLocalDefinition)tree).getName());
                newBody = newBody.append(tree);
                break;
            case VARDECL:
                checkName(tree.pos, ((JFXVar)tree).getName());
                stats = stats.append((JFXVar)tree);
                break;
            case VARINIT:
                checkName(tree.pos, ((JFXVarInit)tree).getName());
                stats = stats.append((JFXVarInit)tree);
                break;

// TODO: Deal with all the possible types in here...
            default:
// TODO:                if (tree.tag >= SKIP && tree.tag <= ASSERT) {
                    stats = stats.append((JCStatement)tree);
// TODO:                } else {
// TODO: Enable when all trees here...
//                    log.error(tree.pos, "javafx.unexpected.tree", tree.toString()); // TODO: Need to log the error??? 
// TODO:                }             
            }
        }
                
        JCBlock runBody = make.Block(0, stats);
        emptyCaptureOuters = List.nil();
        newBody = newBody.prepend(make.JavafxMethodDef(make.Modifiers(PUBLIC), JavafxFlags.SIMPLE_JAVA, Name.fromString(nameTable,
                "run"), make.TypeIdent(VOID),
                emptyVarDefList, runBody, null, emptyCaptureOuters, null, null));

        // Add main method...
        JCNewClass newClass = make.NewClass(null, emptyExpressionList, make.Ident(Name.fromString(nameTable, fileObjName)),
                emptyExpressionList, null);
        
        JCFieldAccess select = make.Select(newClass, Name.fromString(nameTable, "run"));
        emptyExpressionList = List.nil();
        JCMethodInvocation runCall = make.Apply(emptyExpressionList, select, emptyExpressionList);
        stats = List.nil();
        stats = stats.append(make.Exec(runCall));
        
        List<JCVariableDecl> paramList = List.nil();

        paramList = paramList.append(
                make.VarDef(make.Modifiers(0), 
                            Name.fromString(nameTable, "args"), 
                            make.TypeArray(make.Ident(Name.fromString(nameTable, "String"))), 
                            null));
        
        emptyCaptureOuters = List.nil();
        newBody = newBody.prepend(make.JavafxMethodDef(make.Modifiers(PUBLIC|STATIC), JavafxFlags.SIMPLE_JAVA, Name.fromString(nameTable,
                "main"), make.TypeIdent(VOID),
                paramList, make.Block(0, stats), null, emptyCaptureOuters, null, null));

        memberHolder.resultTopTreeDefs = retDefs;
        memberHolder.topLevelClassBody = newBody;
    }


    public void visitClassDeclaration(JFXClassDeclaration that) {
        // TODO:
    }
    
    public void visitAttributeDeclaration(JFXAttributeDeclaration that) {
        // TODO:
    }
    
    public void visitFunctionDeclaration(JFXFunctionMemberDeclaration that) {
        // TODO:
    }

    public void visitOperationDeclaration(JFXOperationMemberDeclaration that) {
        // TODO:
    }

    public void visitRetroAttributeDefinition(JFXRetroAttributeDefinition that) {
        // TODO:
    }

    public void visitRetroOperationDefinition(JFXRetroOperationMemberDefinition that) {
        // TODO:
    }

    public void visitRetroFunctionDefinition(JFXRetroFunctionMemberDefinition that) {
        // TODO:
    }

    public void visitRetroOperationLocalDefinition(JFXRetroOperationLocalDefinition that) {
        // TODO:
    }

    public void visitRetroFunctionLocalDefinition(JFXRetroFunctionLocalDefinition that) {
        // TODO:
    }

    public void visitMemberSelector(JFXMemberSelector that) {
        // TODO:
    }

    public void visitDoLater(JFXDoLater that) {
        // TODO:
    }

    public void visitTypeAny(JFXTypeAny that) {
        // TODO:
    }

    public void visitTypeClass(JFXTypeClass that) {
        // TODO:
    }

    public void visitTypeFunctional(JFXTypeFunctional that) {
        // TODO:
    }

    public void visitTypeUnknown(JFXTypeUnknown that) {
        // TODO:
    }

    public void visitVar(JFXVar that) {
        // TODO:
    }

    public void visitVarInit(JFXVarInit that) {
        // TODO:
    }

    public boolean shouldVisitRemoved() {
        return false;
    }
    
    public boolean shouldVisitSynthetic() {
        return true;
    }
    
    public void visitVarStatement(JFXVarStatement that) {
        // TODO:
    }

    private void checkName(int pos, Name name) {
        if (topLevelNamesSet == null) {
            topLevelNamesSet = new HashSet<Name>();
        }
        
        if (topLevelNamesSet.contains(name)) {
            log.error(pos, "javafx.duplicate.module.member", name.toString());
        }
        
        topLevelNamesSet.add(name);
    }
    
    class MemberHolder {
        MemberHolder(List<JCTree> inTopTreeDefs) {
            this.inTopTreeDefs = inTopTreeDefs;
            resultTopTreeDefs = List.nil();
            topLevelClassBody = List.nil();
        }
        
        List<JCTree> inTopTreeDefs;
        List<JCTree> resultTopTreeDefs;
        List<JCTree> topLevelClassBody;
    }
}
