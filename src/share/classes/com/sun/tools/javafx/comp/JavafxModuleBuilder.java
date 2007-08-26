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

import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.TypeTags.BOT;
import static com.sun.tools.javac.code.TypeTags.VOID;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Name.Table;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.tree.*;
import static com.sun.tools.javafx.tree.JavafxTag.*;
import static com.sun.tools.javafx.tree.JavafxTag.IMPORT;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

public class JavafxModuleBuilder extends JavafxAbstractVisitor {
    protected static final Context.Key<JavafxModuleBuilder> javafxModuleBuilderKey =
        new Context.Key<JavafxModuleBuilder>();

    public static final String runMethodName = "javafx$run$";
    private Table nameTable;
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
        make = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        log = Log.instance(context);
    }

   @Override
   public void visitTopLevel(JCCompilationUnit tree) {
        try {
            preProcessJfxTopLevel(tree);
        } finally {
            topLevelNamesSet = null;
        }
    }

    private void preProcessJfxTopLevel(JCCompilationUnit module) {
        Name moduleClassName = moduleName(module);

        // Divide module defs between run method body, Java compilation unit, and module class
        ListBuffer<JCTree> topLevelDefs = new ListBuffer<JCTree>();
        ListBuffer<JCTree> moduleClassDefs = new ListBuffer<JCTree>();
        ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>();    
        JFXClassDeclaration moduleClass = null;
        for (JCTree tree : module.defs) {
            switch (tree.getTag()) {
            case IMPORT:
                topLevelDefs.append(tree);
                break;
            case CLASSDECL: {
                JFXClassDeclaration decl = (JFXClassDeclaration)tree;   
                Name name = decl.name;
                checkName(tree.pos, name);
                if (name == moduleClassName) {
                    moduleClass = decl;
                } else {
                    decl.mods.flags |= STATIC;
                    moduleClassDefs.append(tree);
                }
                break;
            }
            case RETROOPERATIONDEF:
            case RETROFUNCTIONDEF:
            case RETROATTRIBUTEDEF:
                moduleClassDefs.append(tree);
                break;
            case RETROOPERATIONLOCALDEF:
            case RETROFUNCTIONLOCALDEF:
                checkName(tree.pos, ((JFXRetroFuncOpLocalDefinition)tree).getName());
                moduleClassDefs.append(tree);
                break;
            case FUNCTIONDEF: {
                JFXFunctionDefinition decl = (JFXFunctionDefinition)tree;
                decl.modifiers.flags |= STATIC;
                Name name = decl.name;
                checkName(tree.pos, name);
                moduleClassDefs.append(tree);
                break;
            }
            case VARDECL:
            case VARINIT:
                checkName(tree.pos, ((JFXVar)tree).getName());
                stats.append((JCStatement)tree);
                break;

            default:
                stats.append((JCStatement)tree);
                break;
            }
        }
                
        List<JCExpression> emptyExpressionList = List.nil();
        List<JCVariableDecl> emptyVarDefList = List.nil();

        // Add run() method...
        moduleClassDefs.prepend(makeModuleMethod(runMethodName, emptyVarDefList, false, stats.toList()));

        // Add main method...
        JCNewClass newClass = make.NewClass(null, emptyExpressionList, make.Ident(moduleClassName),
                emptyExpressionList, null);
        JCFieldAccess select = make.Select(newClass, Name.fromString(nameTable, runMethodName));
        JCMethodInvocation runCall = make.Apply(emptyExpressionList, select, emptyExpressionList);
        List<JCStatement> mainStats = List.<JCStatement>of(make.Exec(runCall)); 
        List<JCVariableDecl> paramList = List.nil();
        paramList = paramList.append(
                make.VarDef(make.Modifiers(0), 
                            Name.fromString(nameTable, "args"), 
                            make.TypeArray(make.Ident(Name.fromString(nameTable, "String"))), 
                            null));        
        moduleClassDefs.prepend(makeModuleMethod("main", paramList, true, mainStats));

        if (moduleClass == null) {
            moduleClass =  make.ClassDeclaration(
                make.Modifiers(PUBLIC), 
                moduleClassName, 
                List.<JCExpression>nil(),             // no supertypes
                moduleClassDefs.toList());
        } else {
            moduleClass.declarations = moduleClass.declarations.appendList(moduleClassDefs);
        }
        topLevelDefs.append(moduleClass);
        
        module.defs = topLevelDefs.toList();
    }
    
    private JCMethodDecl makeModuleMethod(String name, List<JCVariableDecl> paramList, boolean isStatic, List<JCStatement> stats) {
        JCBlock body = make.Block(0, stats);
        return make.JavafxMethodDef(
                make.Modifiers(isStatic? PUBLIC | STATIC : PUBLIC), 
                JavafxFlags.SIMPLE_JAVA, 
                Name.fromString(nameTable, name), 
                make.TypeIdent(VOID),
                paramList, 
                body);        
    }
    
    private Name moduleName(JCCompilationUnit tree) {
        String fileObjName = null;

        try {
            fileObjName = new File(tree.getSourceFile().toUri().toURL().getFile()).getName();
            int lastDotIdx = fileObjName.lastIndexOf('.');
            if (lastDotIdx != -1) {
                fileObjName = fileObjName.substring(0, lastDotIdx);
            }
        } catch (MalformedURLException e) {
            assert false : "URL Exception!!!";
        }

        return Name.fromString(nameTable, fileObjName);
    }

    @Override
    public boolean shouldVisitRemoved() {
        return false;
    }
    
    @Override
    public boolean shouldVisitSynthetic() {
        return true;
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
}
