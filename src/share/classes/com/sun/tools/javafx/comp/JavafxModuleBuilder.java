/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.code.Flags;
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
import com.sun.tools.javafx.tree.*;
import static com.sun.tools.javafx.tree.JavafxTag.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

public class JavafxModuleBuilder extends JavafxTreeScanner {
    protected static final Context.Key<JavafxModuleBuilder> javafxModuleBuilderKey =
        new Context.Key<JavafxModuleBuilder>();

    public static final String runMethodString = "javafx$run$";
    public static final String initMethodString = "javafx$init$";
    private Table names;
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
        names = Table.instance(context);
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
            case CLASS_DEF: {
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
            case FUNCTION_DEF: {
                JFXOperationDefinition decl = null;
                decl = (JFXOperationDefinition)tree;
                decl.mods.flags |= STATIC;
                Name name = decl.name;
                checkName(tree.pos, name);
                moduleClassDefs.append(tree);
                break;
            }
            case VAR_DEF:
                checkName(tree.pos, ((JFXVar)tree).getName());
                stats.append((JCStatement)tree);
                break;

            default:
                stats.append((JCStatement)tree);
                break;
            }
        }
                
        // Add run() method... If the class can be a module class.
        moduleClassDefs.prepend(makeMethod(runMethodString, true, stats.toList()));

        if (moduleClass == null) {
            moduleClass =  make.ClassDeclaration(
                make.Modifiers(0),   //TODO: maybe?  make.Modifiers(PUBLIC), 
                moduleClassName, 
                List.<JCExpression>nil(),             // no supertypes
                moduleClassDefs.toList());
        } else {
            moduleClass.defs = moduleClass.defs.appendList(moduleClassDefs);
        }
        moduleClass.accept((JavafxVisitor)this);
        moduleClass.isModuleClass = true;

        topLevelDefs.append(moduleClass);
        
        module.defs = topLevelDefs.toList();
    }
    
    /**
     * Create the init method now so that all the classes in a compilation
     * unit can be detected to be JavaFX.
     */
    @Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
        super.visitClassDeclaration(tree);
        List<JCStatement> initStats = List.<JCStatement>nil();
        tree.defs = tree.defs.prepend(makeMethod(
                initMethodString, 
                false, 
                initStats));
    }

    private JFXOperationDefinition makeMethod(String name, boolean isStatic, List<JCStatement> stats) {
        List<JFXVar> emptyVarList = List.nil();
        JFXBlockExpression body = make.BlockExpression(0, stats, null);
        return make.OperationDefinition(
                make.Modifiers(isStatic? PUBLIC | STATIC : PUBLIC), 
                Name.fromString(names, name), 
                make.TypeClass(make.Ident(Name.fromString(names, "Void")), JFXType.CARDINALITY_SINGLETON),
                emptyVarList, 
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

        return Name.fromString(names, fileObjName);
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
