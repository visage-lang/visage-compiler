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
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Name.Table;
import com.sun.tools.javafx.code.JavafxSymtab;
import static com.sun.tools.javafx.code.JavafxFlags.*;
import com.sun.tools.javafx.tree.*;
import static com.sun.tools.javafx.tree.JavafxTag.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import javax.tools.FileObject;

public class JavafxModuleBuilder {
    protected static final Context.Key<JavafxModuleBuilder> javafxModuleBuilderKey =
        new Context.Key<JavafxModuleBuilder>();

    private final JavafxDefs defs;
    private Table names;
    private JavafxTreeMaker make;
    private final JavafxToJava toJava;
    private Log log;
    private JavafxSymtab syms;
    private Set<Name> topLevelNamesSet;
    private Name pseudoFile;
    private Name pseudoDir;
    private Name commandLineArgs;

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
        pseudoFile = names.fromString("__FILE__");
        pseudoDir = names.fromString("__DIR__");
        commandLineArgs = names.fromString("__ARGS__");
    }

    public void preProcessJfxTopLevel(JCCompilationUnit module) {
        Name moduleClassName = moduleName(module);

        ListBuffer<JCTree> moduleClassDefs = new ListBuffer<JCTree>();
        ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>();

        // check for references to pseudo variables and if found, declare them
        final boolean[] usesFile = new boolean[1];
        final boolean[] usesDir = new boolean[1];
        final DiagnosticPosition[] diagPos = new DiagnosticPosition[1];
        new JavafxTreeScanner() {
            @Override
            public void visitIdent(JCIdent id) {
                super.visitIdent(id);
                if (id.name.equals(pseudoFile)) {
                    usesFile[0] = true;
                    markPosition(id);
                }
                if (id.name.equals(pseudoDir)) {
                    usesDir[0] = true;
                    markPosition(id);
                }
            }
            void markPosition(JCTree tree) {
                if (diagPos[0] == null) { // want the first only
                    diagPos[0] = tree.pos();
                }
            }
        }.scan(module.defs);
        addPseudoVariables(diagPos[0], moduleClassName, module, moduleClassDefs, usesFile[0], usesDir[0]);

        // Divide module defs between run method body, Java compilation unit, and module class
        ListBuffer<JCTree> topLevelDefs = new ListBuffer<JCTree>();
        JFXClassDeclaration moduleClass = null;
        JCExpression value = null;
        for (JCTree tree : module.defs) {
            if (value != null) {
                stats.append( make.at(value).Exec(value) );
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
                JFXFunctionDefinition decl =
                    (JFXFunctionDefinition)tree;
                decl.mods.flags |= STATIC;
                Name name = decl.name;
                checkName(tree.pos, name);
                moduleClassDefs.append(tree);
                //stats.append(decl);
                break;
            }
            case VAR_DEF: {
                JFXVar decl = (JFXVar) tree;
                
                
                checkName(tree.pos, decl.getName());
                stats.append(decl);
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
                
        // Add run() method... If the class can be a module class.
        JFXFunctionDefinition runMethod = makeRunFunction(defs.runMethodName, stats.toList(), value, syms.objectType);
        moduleClassDefs.prepend(runMethod);        

        if (moduleClass == null) {
            moduleClass =  make.ClassDeclaration(
                make.Modifiers(PUBLIC),   //public access needed for applet initialization 
                moduleClassName, 
                List.<JCExpression>nil(),             // no supertypes
                moduleClassDefs.toList());
        } else {
            moduleClass.appendToMembers(moduleClassDefs);
        }
        moduleClass.isModuleClass = true;
        moduleClass.runMethod = runMethod;

        topLevelDefs.append(moduleClass);
        
        module.defs = topLevelDefs.toList();

        topLevelNamesSet = null;
    }
    
    private void addPseudoVariables(DiagnosticPosition diagPos, Name moduleClassName, JCCompilationUnit module,
            ListBuffer<JCTree> stats, boolean usesFile, boolean usesDir) {
        if (usesFile || usesDir) {
            // java.net.URL __FILE__ = Util.get__FILE__(moduleClass);
            JCExpression moduleClassFQN = module.pid != null ?
                make.at(diagPos).Select(module.pid, moduleClassName) : make.at(diagPos).Ident(moduleClassName);
            JCExpression getFile = make.at(diagPos).Identifier("com.sun.javafx.runtime.Util.get__FILE__");
            JCExpression forName = make.at(diagPos).Identifier("java.lang.Class.forName");
            List<JCExpression> args = List.<JCExpression>of(make.at(diagPos).Literal(moduleClassFQN.toString()));
            JCExpression loaderCall = make.at(diagPos).Apply(List.<JCExpression>nil(), forName, args);
            args = List.<JCExpression>of(loaderCall);
            JCExpression getFileURL = make.at(diagPos).Apply(List.<JCExpression>nil(), getFile, args);
            JCStatement fileVar =
                make.at(diagPos).Var(pseudoFile, getURLType(diagPos), 
                         make.at(diagPos).Modifiers(Flags.FINAL|Flags.STATIC), 
                         false, getFileURL, JavafxBindStatus.UNBOUND, null);

            // java.net.URL __DIR__;
            if (usesDir) {
                JCExpression getDir = make.at(diagPos).Identifier("com.sun.javafx.runtime.Util.get__DIR__");
                args = List.<JCExpression>of(make.at(diagPos).Ident(pseudoFile));
                JCExpression getDirURL = make.at(diagPos).Apply(List.<JCExpression>nil(), getDir, args);
                stats.prepend(
                    make.at(diagPos).Var(pseudoDir, getURLType(diagPos), 
                             make.at(diagPos).Modifiers(Flags.FINAL|Flags.STATIC), 
                             false, getDirURL, JavafxBindStatus.UNBOUND, null));
            }

            stats.prepend(fileVar);  // must come before __DIR__ call
        }
    }
    
    private JFXType getURLType(DiagnosticPosition diagPos) {
        JCExpression urlFQN = make.at(diagPos).Identifier("java.net.URL");
        return make.at(diagPos).TypeClass(urlFQN, TypeTree.Cardinality.SINGLETON);
    }

    private JFXFunctionDefinition makeRunFunction(Name name, List<JCStatement> stats, JCExpression value, Type returnType) {
        JFXVar mainArgs = make.Param(commandLineArgs, 
                make.TypeClass(make.Identifier(Name.fromString(names, "String")), TypeTree.Cardinality.ANY));
        List<JFXVar> argsVarList = List.<JFXVar>of(mainArgs);
        return makeMethod(name, stats, value, returnType, argsVarList);
    }
    
    private JFXFunctionDefinition makeMethod(Name name, List<JCStatement> stats, JCExpression value, Type returnType, List<JFXVar> param) {
        JFXBlockExpression body = make.BlockExpression(0, stats, value);
        JCExpression rettree = toJava.makeTypeTree(returnType, null);

        rettree.type = returnType;
        return make.FunctionDefinition(
                make.Modifiers(PUBLIC | STATIC | SYNTHETIC), 
                name, 
                make.TypeClass(rettree, JFXType.Cardinality.SINGLETON),
                param, 
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
            topLevelNamesSet.add(commandLineArgs);
        }
        
        if (topLevelNamesSet.contains(name)) {
            log.error(pos, "javafx.duplicate.module.member", name.toString());
        }
        
        topLevelNamesSet.add(name);
    }
}
