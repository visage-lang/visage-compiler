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

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.TypeTree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;
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
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.tree.*;
import static com.sun.tools.javafx.tree.JavafxTag.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import javax.tools.FileObject;

public class JavafxModuleBuilder extends JavafxTreeScanner {
    protected static final Context.Key<JavafxModuleBuilder> javafxModuleBuilderKey =
        new Context.Key<JavafxModuleBuilder>();

    private final JavafxDefs defs;
    private Table names;
    private JavafxTreeMaker make;
    private final JavafxToJava toJava;
    private Log log;
    private JavafxSymtab syms;
    Name tmpRunReturnName;
    private Set<Name> topLevelNamesSet;
    private Name pseudoFile;
    private Name pseudoDir;

    public static JavafxModuleBuilder instance(Context context) {
        JavafxModuleBuilder instance = context.get(javafxModuleBuilderKey);
        if (instance == null)
            instance = new JavafxModuleBuilder(context);
        return instance;
    }

    protected JavafxModuleBuilder(Context context) {
        defs = JavafxDefs.instance(context);
        names = Table.instance(context);
        make = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        log = Log.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        toJava = JavafxToJava.instance(context);
        tmpRunReturnName = names.fromString("run$return$");
        pseudoFile = names.fromString("__FILE__");
        pseudoDir = names.fromString("__DIR__");
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
        JCExpression value = null;
        for (JCTree tree : module.defs) {
            if (value != null) {
                stats.append( make.Exec(value) );
                value = null;
            }
            switch (tree.getTag()) {
            case IMPORT:
                topLevelDefs.append(tree);
                break;
            case CLASS_DEF: {
                JFXClassDeclaration decl = (JFXClassDeclaration)tree;   
                Name name = decl.getName();
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
                JFXOperationDefinition decl =
                    (JFXOperationDefinition)tree;
                decl.mods.flags |= STATIC;
                Name name = decl.name;
                checkName(tree.pos, name);
                moduleClassDefs.append(tree);
                //stats.append(decl);
                break;
            }
            case VAR_DEF: {
                JFXVar decl = (JFXVar) tree;
                //decl.mods.flags |= STATIC;
                checkName(tree.pos, decl.getName());
                stats.append(decl);
                //moduleClassDefs.append(tree);
                break;
            }
            default:
                if (tree instanceof JCExpression) {
                    value = (JCExpression)tree;
                } else {
                    stats.append((JCStatement)tree);
                }
                break;
            }
        }
        
        // check for references to pseudo variables and if found, declare them
        final boolean[] usesFile = new boolean[1];
        final boolean[] usesDir = new boolean[1];
        new JavafxTreeScanner() {
            @Override
            public void visitIdent(JCIdent id) {
                super.visitIdent(id);
                if (id.name.equals(pseudoFile))
                    usesFile[0] = true;
                if (id.name.equals(pseudoDir))
                    usesDir[0] = true;
            }
        }.scan(module.defs);
        addPseudoVariables(moduleClassName, module, stats, usesFile[0], usesDir[0]);
                
        // Add run() method... If the class can be a module class.
        moduleClassDefs.prepend(makeMethod(defs.runMethodName, stats.toList(), value, syms.objectType));        

        if (moduleClass == null) {
            moduleClass =  make.ClassDeclaration(
                make.Modifiers(PUBLIC),   //public access needed for applet initialization 
                moduleClassName, 
                List.<JCExpression>nil(),             // no supertypes
                moduleClassDefs.toList());
        } else {
            moduleClass.appendToMembers(moduleClassDefs);
        }
        moduleClass.accept((JavafxVisitor)this);
        moduleClass.isModuleClass = true;

        topLevelDefs.append(moduleClass);
        
        module.defs = topLevelDefs.toList();
    }
    
    private void addPseudoVariables(Name moduleClassName, JCCompilationUnit module,
            ListBuffer<JCStatement> stats, boolean usesFile, boolean usesDir) {
        if (usesFile || usesDir) {
            // java.net.URL __FILE__ = Util.get__FILE__(moduleClass);
            JCExpression moduleClassFQN = module.pid != null ?
                make.Select(module.pid, moduleClassName) : make.Ident(moduleClassName);
            JCExpression getFile = make.Identifier("com.sun.javafx.runtime.Util.get__FILE__");
            JCExpression forName = make.Identifier("java.lang.Class.forName");
            List<JCExpression> args = List.<JCExpression>of(make.Literal(moduleClassFQN.toString()));
            JCExpression loaderCall = make.Apply(List.<JCExpression>nil(), forName, args);
            args = List.<JCExpression>of(loaderCall);
            JCExpression getFileURL = make.Apply(List.<JCExpression>nil(), getFile, args);
            JCStatement fileVar =
                make.Var(pseudoFile, getURLType(), 
                         make.Modifiers(Flags.FINAL), 
                         false, getFileURL, JavafxBindStatus.UNBOUND, 
                         List.<JFXAbstractOnChange>nil());

            // java.net.URL __DIR__;
            if (usesDir) {
                JCExpression getDir = make.Identifier("com.sun.javafx.runtime.Util.get__DIR__");
                args = List.<JCExpression>of(make.Ident(pseudoFile));
                JCExpression getDirURL = make.Apply(List.<JCExpression>nil(), getDir, args);
                stats.prepend(
                    make.Var(pseudoDir, getURLType(), 
                             make.Modifiers(Flags.FINAL), 
                             false, getDirURL, JavafxBindStatus.UNBOUND, 
                             List.<JFXAbstractOnChange>nil()));
            }

            stats.prepend(fileVar);  // must come before __DIR__ call
        }
    }
    
    private JFXType getURLType() {
        JCExpression urlFQN = make.Identifier("java.net.URL");
        return make.TypeClass(urlFQN, TypeTree.Cardinality.SINGLETON);
    }

    /**
     * Create the init method now so that all the classes in a compilation
     * unit can be detected to be JavaFX.
     */
    @Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
        super.visitClassDeclaration(tree);
    }

    private JFXOperationDefinition makeMethod(Name name, List<JCStatement> stats, JCExpression value, Type returnType) {
        List<JFXVar> emptyVarList = List.nil();
        JFXBlockExpression body = make.BlockExpression(0, stats, value);
        JCExpression rettree = toJava.makeTypeTree(returnType, null);

        rettree.type = returnType;
        return make.OperationDefinition(
                make.Modifiers(PUBLIC | STATIC | SYNTHETIC), 
                name, 
                make.TypeClass(rettree, JFXType.Cardinality.SINGLETON),
                emptyVarList, 
                body);        
    }
    
    private Name moduleName(JCCompilationUnit tree) {
        String fileObjName = null;

        FileObject fo = tree.getSourceFile();
        URI uri = fo.toUri();
        String path = uri.getPath();
        int i = path.lastIndexOf('/') + 1;
        fileObjName = path.substring(i);
        int lastDotIdx = fileObjName.lastIndexOf('.');
        if (lastDotIdx != -1) {
            fileObjName = fileObjName.substring(0, lastDotIdx);
        }

        return Name.fromString(names, fileObjName);
    }

    private void checkName(int pos, Name name) {
        if (topLevelNamesSet == null) {
            topLevelNamesSet = new HashSet<Name>();
            
            // make sure no one tries to declare these reserved names
            topLevelNamesSet.add(pseudoFile);
            topLevelNamesSet.add(pseudoDir);
        }
        
        if (topLevelNamesSet.contains(name)) {
            log.error(pos, "javafx.duplicate.module.member", name.toString());
        }
        
        topLevelNamesSet.add(name);
    }
}
